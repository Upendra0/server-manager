package com.elitecore.sm.rulelookup.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.rulelookup.model.LookupFieldDetailData;
import com.elitecore.sm.rulelookup.model.RuleLookupTableData;

@SuppressWarnings("rawtypes")
@Repository(value = "genericLookupDao")
public class GenericRuleLookupDaoImpl extends GenericDAOImpl implements IGenericRuleLookupDao{
	
	
	String ORACLE = "oracle";
	String POSTGRESQL = "postgresql";
	String MYSQL = "mysql";
	
	@Override
	public String getDatabaseEnv() {
		try{
			Session session = getCurrentSession();
			Object dialect = org.apache.commons.beanutils.PropertyUtils.getProperty(session.getSessionFactory(),
					"dialect");
			if(dialect != null && StringUtils.isNotBlank(dialect.toString())){
				if(StringUtils.containsIgnoreCase(dialect.toString(), ORACLE)){
					return ORACLE;
				}else if(StringUtils.containsIgnoreCase(dialect.toString(), MYSQL)){
					return MYSQL;
				}else if(StringUtils.containsIgnoreCase(dialect.toString(), POSTGRESQL)){
					return POSTGRESQL;
				}
				logger.info("dialect found : "+dialect.toString());
			}else{
				logger.debug("Dialect obtained was null.");
				throw new NullPointerException();
			}
		}catch(Exception e){
			logger.trace("Problem occured while getting hibernate dialect : ",e);
		}
		return null;
	}

	@Override
	public void createViewInDb(RuleLookupTableData ruleLookupTableData) {
		if(ORACLE.equalsIgnoreCase(getDatabaseEnv())){
			createViewInOracle(ruleLookupTableData);
		}else if(MYSQL.equalsIgnoreCase(getDatabaseEnv())){
			createViewInMySql(ruleLookupTableData);
		}else if(POSTGRESQL.equalsIgnoreCase(getDatabaseEnv())){
			createViewInPostgreSql(ruleLookupTableData);
		}
	}

	@Override
	public void deleteViewFormDb(RuleLookupTableData ruleLookupTableData){
		if(ORACLE.equalsIgnoreCase(getDatabaseEnv())){
			deleteViewInOracle(ruleLookupTableData);
		}else if(MYSQL.equalsIgnoreCase(getDatabaseEnv())){
			deleteViewInMySql(ruleLookupTableData);
		}else if(POSTGRESQL.equalsIgnoreCase(getDatabaseEnv())){
			deleteViewInPostgreSql(ruleLookupTableData);
		}
	}
	
	@Override
	public void deleteViewInPostgreSql(RuleLookupTableData ruleLookupTableData) {
		String strQuery;
		Session session = getCurrentSession();
		try{
			strQuery = generateDeleteQuery(ruleLookupTableData);
			if(StringUtils.isNotBlank(strQuery)){
				Query query = session.createSQLQuery(strQuery);
				query.executeUpdate();
			}else{
				logger.debug("Query obtained was blank");
			}
		}catch(Exception e){
			logger.debug("Problem occurred while deleting view from oracle : "+e);
		}
	}

	@Override
	public void deleteViewInMySql(RuleLookupTableData ruleLookupTableData) {
		String strQuery;
		Session session = getCurrentSession();
		try{
			strQuery = generateDeleteQuery(ruleLookupTableData);
			if(StringUtils.isNotBlank(strQuery)){
				Query query = session.createSQLQuery(strQuery);
				query.executeUpdate();
			}else{
				logger.debug("Query obtained was blank");
			}
		}catch(Exception e){
			logger.debug("Problem occurred while deleting view from oracle : "+e);
		}
	}

	@Override 
	public void deleteViewInOracle(RuleLookupTableData ruleLookupTableData) {
		String strQuery;
		Session session = getCurrentSession();
		try{
			strQuery = generateDeleteQuery(ruleLookupTableData);
			if(StringUtils.isNotBlank(strQuery)){
				Query query = session.createSQLQuery(strQuery);
				query.list();
			}else{
				logger.debug("Query obtained was blank");
			}
		}catch(Exception e){
			logger.debug("Problem occurred while deleting view from oracle : "+e);
		}
	}

	
	public String generateDeleteQuery(RuleLookupTableData ruleLookupTableData){
		return "DROP VIEW "+ruleLookupTableData.getViewName();
	}
	
	
	@Override
	public String generateCreateQuery(RuleLookupTableData ruleLookupTableData){
		String strQuery = "";
		StringBuffer columns = new StringBuffer();
		for(LookupFieldDetailData lookupFieldData : ruleLookupTableData.getLookUpFieldDetailData()){
			if(StringUtils.isNotBlank(columns)){
				columns.append(",");
			}
			columns.append(lookupFieldData.getFieldName()).append(" ").append(lookupFieldData.getViewFieldName());
		}
		if(StringUtils.isNotBlank(ruleLookupTableData.getViewName())){
			String commonColumnForView =" ID RECORDROWID,CREATEDBYSTAFFID,CREATEDDATE,LASTUPDATEDBYSTAFFID,LASTUPDATEDDATE,STATUS, ";
			strQuery = "CREATE OR REPLACE VIEW " + ruleLookupTableData.getViewName() + " as select " + commonColumnForView + columns.toString() + " ,LOOKUPTABLEID" +
						" from TBLTRULELOOKUPDATA where LOOKUPTABLEID = '"+ruleLookupTableData.getId()+"'";
			logger.debug("Query : " + strQuery);
		}
		return strQuery;
	}
	
	
	
	@Override
	public void createViewInOracle(RuleLookupTableData ruleLookupTableData){
		Session session = getCurrentSession();
		String strQuery = generateCreateQuery(ruleLookupTableData);
		if(StringUtils.isNotBlank(strQuery)){
			try{
				Query query = session.createSQLQuery(strQuery);
				query.list();
				logger.debug("query : "+strQuery+" executed successfully. ");
			}catch(Exception e){
				logger.trace("Could not execute query exception : "+e);
			}
		}else{
			logger.debug("SQL query obtained was blank ");
		}
	}
	
	@Override
	public void createViewInMySql(RuleLookupTableData ruleLookupTableData){
		Session session = getCurrentSession();
		String strQuery = generateCreateQuery(ruleLookupTableData);
		if(StringUtils.isNotBlank(strQuery)){
			try{
				Query query = session.createSQLQuery(strQuery);
				query.executeUpdate();
				logger.debug("query : "+strQuery+" executed successfully. ");
			}catch(Exception e){
				logger.trace("Could not execute query exception : "+e);
			}
		}else{
			logger.debug("SQL query obtained was blank ");
		}
	}
	
	@Override
	public void createViewInPostgreSql(RuleLookupTableData ruleLookupTableData){
		Session session = getCurrentSession();
		String strQuery = generateCreateQuery(ruleLookupTableData);
		//Postgresql doesn't support dropping of column from create or replace view so need to drop then add again
		String deletePostgresQuery = "DROP VIEW IF EXISTS "+ruleLookupTableData.getViewName();
		try{
			Query delQuery = session.createSQLQuery(deletePostgresQuery);
			delQuery.executeUpdate();
			logger.debug("View dropped successfully now adding ");
		}catch(Exception e){
			logger.trace("Could not exceute query exception : "+e);
		}
		
		if(StringUtils.isNotBlank(strQuery)){
			try{
				Query query = session.createSQLQuery(strQuery);
				query.executeUpdate();
				logger.debug("query : "+strQuery+" executed successfully. ");
			}catch(Exception e){
				logger.trace("Could not execute query exception : "+e);
			}
		}else{
			logger.debug("SQL query obtained was blank ");
		}
	}

	@Override
	public void deleteViewDataFormDb(int recordId, String viewName) {
		if(ORACLE.equalsIgnoreCase(getDatabaseEnv())){
			deleteViewDataInOracle(recordId, viewName);
		}else if(MYSQL.equalsIgnoreCase(getDatabaseEnv())){
			deleteViewDataInMySql(recordId, viewName);
		}else if(POSTGRESQL.equalsIgnoreCase(getDatabaseEnv())){
			deleteViewDataInPostgreSql(recordId, viewName);
		}
	}

	@Override
	public void deleteViewDataInOracle(int recordId, String viewName) {
		String strQuery;
		Session session = getCurrentSession();
		try{
			strQuery = generateDeleteQuery(recordId, viewName);
			if(StringUtils.isNotBlank(strQuery)){
				Query query = session.createSQLQuery(strQuery);
				query.executeUpdate();
			}else{
				logger.debug("Query obtained was blank");
			}
		}catch(Exception e){
			logger.debug("Problem occurred while deleting view data from oracle : "+e);
		}
	}
	
	@Override
	public void deleteViewDataInMySql(int recordId, String viewName) {
		String strQuery;
		Session session = getCurrentSession();
		try{
			strQuery = generateDeleteQuery(recordId, viewName);
			if(StringUtils.isNotBlank(strQuery)){
				Query query = session.createSQLQuery(strQuery);
				query.executeUpdate();
			}else{
				logger.debug("Query obtained was blank");
			}
		}catch(Exception e){
			logger.debug("Problem occurred while deleting view data from mySql : "+e);
		}
	}
	
	@Override
	public void deleteViewDataInPostgreSql(int recordId, String viewName) {
		String strQuery;
		Session session = getCurrentSession();
		try{
			strQuery = generateDeleteQuery(recordId, viewName);
			if(StringUtils.isNotBlank(strQuery)){
				Query query = session.createSQLQuery(strQuery);
				query.executeUpdate();
			}else{
				logger.debug("Query obtained was blank");
			}
		}catch(Exception e){
			logger.debug("Problem occurred while deleting view data from postgreSql : "+e);
		}
	}
	
	public String generateDeleteQuery(int recordId, String viewName){
		return "DELETE FROM " + viewName.toUpperCase() + " WHERE RECORDROWID = " + recordId;
	}
	
	@Override
	public void deleteViewDataFormDb(String viewName) {
		if(ORACLE.equalsIgnoreCase(getDatabaseEnv())){
			deleteViewDataInOracle(viewName);
		}else if(MYSQL.equalsIgnoreCase(getDatabaseEnv())){
			deleteViewDataInMySql(viewName);
		}else if(POSTGRESQL.equalsIgnoreCase(getDatabaseEnv())){
			deleteViewDataInPostgreSql(viewName);
		}
	}

	@Override
	public void deleteViewDataInOracle(String viewName) {
		String strQuery;
		Session session = getCurrentSession();
		try{
			strQuery = generateDeleteQuery(viewName);
			if(StringUtils.isNotBlank(strQuery)){
				Query query = session.createSQLQuery(strQuery);
				query.executeUpdate();
			}else{
				logger.debug("Query obtained was blank");
			}
		}catch(Exception e){
			logger.debug("Problem occurred while deleting view data from oracle : "+e);
		}
	}
	
	@Override
	public void deleteViewDataInMySql(String viewName) {
		String strQuery;
		Session session = getCurrentSession();
		try{
			strQuery = generateDeleteQuery(viewName);
			if(StringUtils.isNotBlank(strQuery)){
				Query query = session.createSQLQuery(strQuery);
				query.executeUpdate();
			}else{
				logger.debug("Query obtained was blank");
			}
		}catch(Exception e){
			logger.debug("Problem occurred while deleting view data from mySql : "+e);
		}
	}
	
	@Override
	public void deleteViewDataInPostgreSql(String viewName) {
		String strQuery;
		Session session = getCurrentSession();
		try{
			strQuery = generateDeleteQuery(viewName);
			if(StringUtils.isNotBlank(strQuery)){
				Query query = session.createSQLQuery(strQuery);
				query.executeUpdate();
			}else{
				logger.debug("Query obtained was blank");
			}
		}catch(Exception e){
			logger.debug("Problem occurred while deleting view data from postgreSql : "+e);
		}
	}
	
	public String generateDeleteQuery(String viewName){
		return "DELETE FROM " + viewName.toUpperCase();
	}

	@Override
	public int createRuleLookupRecordInView(String viewName, Map<String, String> lookUpdata, int staffId, int tableId) {
		Timestamp timestamp = new Timestamp(new Date().getTime());
		if(ORACLE.equalsIgnoreCase(getDatabaseEnv())){
			return createViewDataInOracle(viewName, lookUpdata, staffId, tableId,timestamp);
		}else if(MYSQL.equalsIgnoreCase(getDatabaseEnv())){
			return createViewDataInMySql(viewName, lookUpdata, staffId, tableId,timestamp);
		}else if(POSTGRESQL.equalsIgnoreCase(getDatabaseEnv())){
			return createViewDataInPostgreSql(viewName, lookUpdata, staffId, tableId,timestamp);
		}
		return 0;
	}
	
	@Override
	public int createViewDataInOracle(String viewName, Map<String, String> lookUpdata, int staffId, int tableId,Timestamp timestamp) {
		String strQuery;
		int createdRow = 0;
		Session session = getCurrentSession();
		try{
			strQuery = generateCreateQuery(viewName, lookUpdata, staffId, tableId,ORACLE);
			if(StringUtils.isNotBlank(strQuery)){
				Query query = session.createSQLQuery(strQuery).setTimestamp("createdDate", timestamp).setTimestamp("lastUpdatedDate", timestamp);
				createdRow = query.executeUpdate();
			}else{
				logger.debug("Query obtained was blank");
			}
		}catch(Exception e){
			logger.debug("Problem occurred while creating view data from oracle : "+e);
		}
		return createdRow;
	}

	@Override
	public int createViewDataInMySql(String viewName, Map<String, String> lookUpdata, int staffId, int tableId,Timestamp timestamp) {
		String strQuery;
		int createdRow = 0;
		Session session = getCurrentSession();
		try{
			strQuery = generateCreateQuery(viewName, lookUpdata, staffId, tableId,MYSQL);
			if(StringUtils.isNotBlank(strQuery)){
				Query query = session.createSQLQuery(strQuery).setTimestamp("createdDate", timestamp).setTimestamp("lastUpdatedDate", timestamp);
				createdRow = query.executeUpdate();
			}else{
				logger.debug("Query obtained was blank");
			}
		}catch(Exception e){
			logger.debug("Problem occurred while creating view data from MySql : "+e);
		}
		return createdRow;
	}

	@Override
	public int createViewDataInPostgreSql(String viewName, Map<String, String> lookUpdata, int staffId, int tableId,Timestamp timestamp) {
		String strQuery;
		int createdRow = 0;
		Session session = getCurrentSession();
		try{
			strQuery = generateCreateQuery(viewName, lookUpdata, staffId, tableId, POSTGRESQL);
			if(StringUtils.isNotBlank(strQuery)){
				Query query = session.createSQLQuery(strQuery).setTimestamp("createdDate", timestamp).setTimestamp("lastUpdatedDate", timestamp);
				createdRow = query.executeUpdate();
			}else{
				logger.debug("Query obtained was blank");
			}
		}catch(Exception e){
			logger.debug("Problem occurred while creating view data from PostgreSql : "+e);
		}
		return createdRow;
	}

	public String generateCreateQuery(String viewName, Map<String, String> lookUpdata, int staffId, int tableId, String DatabaseType){
		String commonViewFieldNames ="RECORDROWID,CREATEDBYSTAFFID,CREATEDDATE,LASTUPDATEDBYSTAFFID,LASTUPDATEDDATE,STATUS,";
		String commonViewFieldDatas = "(SELECT COALESCE(max(ID+1),1) FROM TBLTRULELOOKUPDATA)," + staffId + ",:createdDate," + staffId + ",:lastUpdatedDate,'" + StateEnum.ACTIVE + "',";
		StringBuffer dynamicViewFieldNames = new StringBuffer();
		StringBuffer dynamicViewFieldDatas = new StringBuffer();
		String replaceChar = "";
		
		if(ORACLE.equalsIgnoreCase(DatabaseType)){
			replaceChar = "''";
		}else if(MYSQL.equalsIgnoreCase(DatabaseType)){
			replaceChar = "\'";
		}else if(POSTGRESQL.equalsIgnoreCase(DatabaseType)){
			replaceChar = "''";
		}
		
		for (Map.Entry<String,String> entry : lookUpdata.entrySet()) {
			if(StringUtils.isNotBlank(dynamicViewFieldNames)){
				dynamicViewFieldNames.append(",");
			}
			dynamicViewFieldNames.append(entry.getKey());
			if(StringUtils.isNotBlank(dynamicViewFieldDatas)){
				dynamicViewFieldDatas.append(",");
			}
			dynamicViewFieldDatas.append("'"+entry.getValue().replace("'", replaceChar)  +"'");
		}
		return "INSERT INTO "+ viewName + " (" + commonViewFieldNames + dynamicViewFieldNames + ",LOOKUPTABLEID) VALUES (" 
				+ commonViewFieldDatas + dynamicViewFieldDatas + "," + tableId + ")";
	}
	
	@Override
	public int updateRuleLookupRecordInView(String viewName, Map<String, String> lookUpdata, int staffId) {
		Timestamp timestamp = new Timestamp(new Date().getTime());
		if(ORACLE.equalsIgnoreCase(getDatabaseEnv())){
			return updateViewDataInOracle(viewName, lookUpdata, staffId, timestamp);
		}else if(MYSQL.equalsIgnoreCase(getDatabaseEnv())){
			return updateViewDataInMySql(viewName, lookUpdata, staffId, timestamp);
		}else if(POSTGRESQL.equalsIgnoreCase(getDatabaseEnv())){
			return updateViewDataInPostgreSql(viewName, lookUpdata, staffId, timestamp);
		}
		return 0;
	}
	
	@Override
	public int updateViewDataInOracle(String viewName, Map<String, String> lookUpdata, int staffId, Timestamp timestamp) {
		String strQuery;
		int updatedRow = 0;
		Session session = getCurrentSession();
		try{
			strQuery = generateUpdateQuery(viewName, lookUpdata, staffId, ORACLE);
			if(StringUtils.isNotBlank(strQuery)){
				Query query = session.createSQLQuery(strQuery).setTimestamp("lastUpdatedDate", timestamp);
				updatedRow = query.executeUpdate();
			}else{
				logger.debug("Query obtained was blank");
			}
		}catch(Exception e){
			logger.debug("Problem occurred while updating view data from oracle : "+e);
		}
		return updatedRow;
	}

	@Override
	public int updateViewDataInMySql(String viewName, Map<String, String> lookUpdata, int staffId, Timestamp timestamp) {
		String strQuery;
		int updatedRow = 0;
		Session session = getCurrentSession();
		try{
			strQuery = generateUpdateQuery(viewName, lookUpdata, staffId, MYSQL);
			if(StringUtils.isNotBlank(strQuery)){
				Query query = session.createSQLQuery(strQuery).setTimestamp("lastUpdatedDate", timestamp);
				updatedRow = query.executeUpdate();
			}else{
				logger.debug("Query obtained was blank");
			}
		}catch(Exception e){
			logger.debug("Problem occurred while updating view data from MySql : "+e);
		}
		return updatedRow;
	}

	@Override
	public int updateViewDataInPostgreSql(String viewName, Map<String, String> lookUpdata, int staffId, Timestamp timestamp) {
		String strQuery;
		int updatedRow = 0;
		Session session = getCurrentSession();
		try{
			strQuery = generateUpdateQuery(viewName, lookUpdata, staffId, POSTGRESQL);
			if(StringUtils.isNotBlank(strQuery)){
				Query query = session.createSQLQuery(strQuery).setTimestamp("lastUpdatedDate", timestamp);
				updatedRow = query.executeUpdate();
			}else{
				logger.debug("Query obtained was blank");
			}
		}catch(Exception e){
			logger.debug("Problem occurred while updating view data from PostgreSql : "+e);
		}
		return updatedRow;
	}

	public String generateUpdateQuery(String viewName, Map<String, String> lookUpdata, int staffId, String DatabaseType){
		StringBuffer updateViewField =new StringBuffer();
		String replaceChar = "";
		if(ORACLE.equalsIgnoreCase(DatabaseType)){
			replaceChar = "''";
		}else if(MYSQL.equalsIgnoreCase(DatabaseType)){
			replaceChar = "\'";
		}else if(POSTGRESQL.equalsIgnoreCase(DatabaseType)){
			replaceChar = "''";
		}
			
		updateViewField.append("LASTUPDATEDBYSTAFFID = " + staffId + ", LASTUPDATEDDATE = :lastUpdatedDate");
		
		String recordRowId = lookUpdata.remove("RECORDROWID");
		for (Map.Entry<String,String> entry : lookUpdata.entrySet()) {
			updateViewField.append(", " + entry.getKey() + " = '" + entry.getValue().replace("'", replaceChar) + "'");
		}
		return "UPDATE "+ viewName + " SET "+ updateViewField + " WHERE RECORDROWID = " + recordRowId;
	}
	
	@Override
	public void deleteViewDataSql(String viewName) {
		String strQuery;
		/** Session session = getCurrentSession(); **/
		Session session = openCurrentSession();
		Transaction tx = session.beginTransaction();
		try{
			strQuery = "DELETE FROM " + viewName.toUpperCase();
			if(StringUtils.isNotBlank(strQuery)){
				Query query = session.createSQLQuery(strQuery);
				query.executeUpdate();
				
				tx.commit();
				session.close();
			}else{
				logger.debug("Query obtained was blank");
				session.close();
			}
		}catch(Exception e){
			logger.debug("Problem occurred while deleting view data from DB : "+e);
			session.close();
		}
	}
	
	/** update data for upload **/
	@Override
	public int updateLookupRecordsInView(String viewName, List<Map<String,String>> duplicateTableRowList,
			List<Map<String,String>> errorTableRowList, List<Map<String,String>> successTableRowList,Set<String> uniqueHeaderSet , int staffId) {
		Timestamp timestamp = new Timestamp(new Date().getTime());
		if(ORACLE.equalsIgnoreCase(getDatabaseEnvironment())){
			return updateViewDatasInOracle( viewName, duplicateTableRowList , errorTableRowList, successTableRowList,uniqueHeaderSet, staffId, timestamp);
		}else if(MYSQL.equalsIgnoreCase(getDatabaseEnvironment())){
			return updateViewDatasInMySql(viewName, duplicateTableRowList , errorTableRowList, successTableRowList,uniqueHeaderSet, staffId, timestamp);
		}else if(POSTGRESQL.equalsIgnoreCase(getDatabaseEnvironment())){
			return updateViewDatasInPostGres(viewName, duplicateTableRowList , errorTableRowList, successTableRowList,uniqueHeaderSet, staffId, timestamp);
		}
		
		return 0;
	}	
	
	private int updateViewDatasInOracle(String viewName,List<Map<String,String>> duplicateTableRowList,
			List<Map<String,String>> errorTableRowList,List<Map<String,String>> successTableRowList,Set<String> uniqueHeaderSet , int staffId, Timestamp timestamp) {
		String strQuery;
		int updatedRow = 0;
		/** Session session = getCurrentSession(); **/
		Session session = openCurrentSession();
		Transaction tx = session.beginTransaction();
		Map<String,String> uniqueDataSet = null;

		for( Map<String,String> tableViewRow : duplicateTableRowList ){
			
			 uniqueDataSet = new HashMap<>();
				
    		 for(String ufield :  uniqueHeaderSet){
    					uniqueDataSet.put( ufield ,  tableViewRow.get(ufield));
			     }
			try{
				strQuery = generateUpdateViewQuery(viewName, tableViewRow, uniqueDataSet , staffId, ORACLE);
				if(StringUtils.isNotBlank(strQuery)){
					Query query = session.createSQLQuery(strQuery).setTimestamp("lastUpdatedDate", timestamp);
					updatedRow = query.executeUpdate();
					if (tx.getStatus() != TransactionStatus.COMMITTED){ 
						tx.commit();
					}
					successTableRowList.add( tableViewRow );  
				}else{
					logger.debug("Query obtained was blank");
					errorTableRowList.add( tableViewRow );
					if (tx.getStatus() != TransactionStatus.COMMITTED){ 
						tx.commit();
					}
				}
			}catch(Exception e){ //ConstraintViolationException
				logger.debug("Error occurred while creating view data from oracle : "+e);
				errorTableRowList.add( tableViewRow );
				 if (tx.getStatus() != TransactionStatus.COMMITTED){  
					tx.commit();
				}
			}
		}
		
		try{
			session.close();
		}catch(Exception e){
			logger.error(e);
			logger.debug("error while session closing");
		}
		
		return 1;
	}
	
	
	private int updateViewDatasInPostGres(String viewName,List<Map<String,String>> duplicateTableRowList,
			List<Map<String,String>> errorTableRowList,List<Map<String,String>> successTableRowList,Set<String> uniqueHeaderSet , int staffId, Timestamp timestamp) {
		String strQuery;
		int updatedRow = 0;
		/** Session session = getCurrentSession(); **/
		Session session = openCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Map<String,String> uniqueDataSet = null;
		
		for( Map<String,String> tableViewRow : duplicateTableRowList ){
			
			 uniqueDataSet = new HashMap<>();
				
    		 for(String ufield :  uniqueHeaderSet){
    					uniqueDataSet.put( ufield ,  tableViewRow.get(ufield));
			     }
			try{
				strQuery = generateUpdateViewQuery(viewName, tableViewRow, uniqueDataSet , staffId ,POSTGRESQL);
				if(StringUtils.isNotBlank(strQuery)){
					Query query = session.createSQLQuery(strQuery).setTimestamp("lastUpdatedDate", timestamp);
					updatedRow = query.executeUpdate();
					 if (tx.getStatus() != TransactionStatus.COMMITTED){  
						tx.commit();
					}
					successTableRowList.add( tableViewRow );  
				}else{
					logger.debug("Query obtained was blank");
					errorTableRowList.add( tableViewRow );
					 if (tx.getStatus() != TransactionStatus.COMMITTED){  
						tx.commit();
					}
				}
			}catch(Exception e){ //ConstraintViolationException
				logger.error(e);
				logger.debug("Error occurred while creating view data from oracle : "+e);
				errorTableRowList.add( tableViewRow );
				 if (tx.getStatus() != TransactionStatus.COMMITTED){  
					tx.commit();
				}
			}
		}
		
		try{
			session.close();
		}catch(Exception e){
			logger.error(e);
			logger.debug("error while session closing");
		}
		
		return 1;
	}
	private int updateViewDatasInMySql(String viewName,List<Map<String,String>> duplicateTableRowList,
			List<Map<String,String>> errorTableRowList,List<Map<String,String>> successTableRowList,Set<String> uniqueHeaderSet , int staffId, Timestamp timestamp) {
		String strQuery;
		int updatedRow = 0;
		/** Session session = getCurrentSession(); **/
		Session session = openCurrentSession();
		Transaction tx = session.beginTransaction();
		Map<String,String> uniqueDataSet = null;
		
		for( Map<String,String> tableViewRow : duplicateTableRowList ){
			
			 uniqueDataSet = new HashMap<>();
				
    		 for(String ufield :  uniqueHeaderSet){
    					uniqueDataSet.put( ufield ,  tableViewRow.get(ufield));
			     }
			try{
				strQuery = generateUpdateViewQuery(viewName, tableViewRow, uniqueDataSet , staffId, MYSQL);
				if(StringUtils.isNotBlank(strQuery)){
					Query query = session.createSQLQuery(strQuery).setTimestamp("lastUpdatedDate", timestamp);
					updatedRow = query.executeUpdate();
					 if (tx.getStatus() != TransactionStatus.COMMITTED){  
						tx.commit();
					}
					successTableRowList.add( tableViewRow );  
				}else{
					logger.debug("Query obtained was blank");
					errorTableRowList.add( tableViewRow );
					 if (tx.getStatus() != TransactionStatus.COMMITTED){  
						tx.commit();
					}
				}
			}catch(Exception e){ //ConstraintViolationException
				logger.debug("Error occurred while creating view data from oracle : "+e);
				errorTableRowList.add( tableViewRow );
				 if (tx.getStatus() != TransactionStatus.COMMITTED){  
					tx.commit();
				}
			}
		}		
		try{
			session.close();
		}catch(Exception e){
			logger.error(e);
			logger.debug("error while session closing");
		}
		return 1;
	}

	private  String  generateUpdateViewQuery(String viewName, Map<String, String> lookUpdata, Map<String, String> uniqueconditionlist, int staffId, String DatabaseType){
		
		StringBuffer updateViewField =new StringBuffer();
		String replaceChar = "";
		updateViewField.append("LASTUPDATEDBYSTAFFID = " + staffId + ", LASTUPDATEDDATE = :lastUpdatedDate");
		
		if(ORACLE.equalsIgnoreCase(DatabaseType)){
			replaceChar = "''";
		}else if(MYSQL.equalsIgnoreCase(DatabaseType)){
			replaceChar = "\'";
		}else if(POSTGRESQL.equalsIgnoreCase(DatabaseType)){
			replaceChar = "''";
		}
		
		for (Map.Entry<String,String> entry : lookUpdata.entrySet()) {
			updateViewField.append(", " + entry.getKey() + " = '" + entry.getValue().replace("'", replaceChar) + "'");
		}
		
		StringBuffer updateCondition =new StringBuffer();
		int conditionSize = uniqueconditionlist.size();
		int andChecker = 0;
		for (Map.Entry<String,String> entry : uniqueconditionlist.entrySet()) {
			updateCondition.append( entry.getKey() + " = '" + entry.getValue().replace("'", replaceChar) + "'");			
			++andChecker;			
			if( conditionSize > andChecker )
				updateCondition.append(" AND ");			
		}		
		return "UPDATE "+ viewName + " SET "+ updateViewField + " WHERE  " + updateCondition.toString();		
	}
	/** insert query with unique constraint  **/
	@Override
	public int autoCreateRuleLookupRecord(String viewName, List<Map<String,String>>  tableRowList, List<Map<String,String>>  duplicateTableRowList,
			List<Map<String,String>>  errorTableRowList, List<Map<String,String>>  successTableRowList ,int staffId, int tableId, Set<String> uniqueHeaderSet) {
		Timestamp timestamp = new Timestamp(new Date().getTime());
		if(ORACLE.equalsIgnoreCase(getDatabaseEnvironment())){
			return autoCreateViewDataInOracle(viewName, tableRowList, duplicateTableRowList, errorTableRowList,successTableRowList, staffId, tableId,uniqueHeaderSet,timestamp);
		}else if(MYSQL.equalsIgnoreCase(getDatabaseEnvironment())){
			return autoCreateViewDataInMySql(viewName,tableRowList, duplicateTableRowList, errorTableRowList,successTableRowList, staffId, tableId,uniqueHeaderSet,timestamp);
		}else if(POSTGRESQL.equalsIgnoreCase(getDatabaseEnvironment())){
			try{
				 return	 autoCreateViewDataInPostgreSql(viewName,tableRowList, duplicateTableRowList, errorTableRowList,successTableRowList, staffId, tableId,uniqueHeaderSet,timestamp);			
			}catch(Exception e){
				logger.info("postgres db error : " + e);
				return 3;
			}
		}
		return 0;
	}
	
	
	protected int autoCreateViewDataInOracle(String viewName, List<Map<String,String>>  tableRowList, List<Map<String,String>>  duplicateTableRowList,
			List<Map<String,String>>  errorTableRowList, List<Map<String,String>>  successTableRowList ,int staffId, int tableId , Set<String> uniqueHeaderSet,Timestamp timestamp) {
		String strQuery;
		int createdRow = 0;
		/** Session session = getCurrentSession(); **/
		Session session = openCurrentSession();
		Transaction tx = session.beginTransaction();
		
		for( Map<String,String> tableViewRow : tableRowList ){
			boolean isUniqueNotBlank = false;
			for (String uniqueField : uniqueHeaderSet) {
				if (tableViewRow.get(uniqueField) != null && !tableViewRow.get(uniqueField).trim().equals("")) {
					isUniqueNotBlank = true;
					break;
				}
			}

			if (isUniqueNotBlank) {
				try {
					strQuery = generateCreateQuery(viewName, tableViewRow, staffId, tableId, ORACLE);
					if (StringUtils.isNotBlank(strQuery)) {
						Query query = session.createSQLQuery(strQuery).setTimestamp("createdDate", timestamp).setTimestamp("lastUpdatedDate", timestamp);
						createdRow = query.executeUpdate();
						 if (tx.getStatus() != TransactionStatus.COMMITTED){  
							tx.commit();
						}
						successTableRowList.add(tableViewRow);
					} else {
						logger.debug("Query obtained was blank");
					}
				} catch (ConstraintViolationException e) {
					logger.error(e);
					duplicateTableRowList.add(tableViewRow);
					 if (tx.getStatus() != TransactionStatus.COMMITTED){  
						tx.commit();
					}
				} catch (Exception e) {
					logger.error(e);
					errorTableRowList.add(tableViewRow);
					 if (tx.getStatus() != TransactionStatus.COMMITTED){  
						tx.commit();
					}
				}
			} else {
				errorTableRowList.add(tableViewRow);
			}
		  }
		try{
			session.close();
		}catch(Exception e){
			logger.error(e);
			logger.debug("error while session closing");
		}
		return 1;
	}

	
	protected int autoCreateViewDataInMySql(String viewName, List<Map<String,String>>  tableRowList, List<Map<String,String>>  duplicateTableRowList,
			List<Map<String,String>>  errorTableRowList, List<Map<String,String>>  successTableRowList ,int staffId, int tableId, Set<String> uniqueHeaderSet,Timestamp timestamp) {
		String strQuery;
		int createdRow = 0;
		/** Session session = getCurrentSession(); **/
		Session session = openCurrentSession();
		Transaction tx = session.beginTransaction();
		
		for( Map<String,String> tableViewRow : tableRowList ){
			boolean isUniqueNotBlank = false;
			for (String uniqueField : uniqueHeaderSet) {
				if (tableViewRow.get(uniqueField) != null && !tableViewRow.get(uniqueField).trim().equals("")) {
					isUniqueNotBlank = true;
					break;
				}
			}
		
			if (isUniqueNotBlank) {
				try {
					strQuery = generateCreateQuery(viewName, tableViewRow, staffId, tableId, MYSQL);
					if (StringUtils.isNotBlank(strQuery)) {
						Query query = session.createSQLQuery(strQuery).setTimestamp("createdDate", timestamp).setTimestamp("lastUpdatedDate", timestamp);
						createdRow = query.executeUpdate();
						 if (tx.getStatus() != TransactionStatus.COMMITTED){  
							tx.commit();
						}
						successTableRowList.add(tableViewRow);
					} else {
						logger.debug("Query obtained was blank");
					}
				} catch (ConstraintViolationException e) {
					logger.error(e);
					duplicateTableRowList.add(tableViewRow);
					 if (tx.getStatus() != TransactionStatus.COMMITTED){  
						tx.commit();
					}
				} catch (Exception e) {
					logger.error(e);
					errorTableRowList.add(tableViewRow);
					 if (tx.getStatus() != TransactionStatus.COMMITTED){  
						tx.commit();
					}
				}
			} else {
				errorTableRowList.add(tableViewRow);
			}
		  }
		try{
			session.close();
		}catch(Exception e){
			logger.error(e);
			logger.debug("error while session closing");
		}
		return 1;
	}


	protected int autoCreateViewDataInPostgreSql(String viewName, List<Map<String,String>>  tableRowList, List<Map<String,String>>  duplicateTableRowList,
			List<Map<String,String>>  errorTableRowList, List<Map<String,String>>  successTableRowList ,int staffId, int tableId, Set<String> uniqueHeaderSet,Timestamp timestamp) {
		String strQuery;
		int createdRow = 0;
		/** Session session = getCurrentSession(); **/
		Session session = openCurrentSession();
		Transaction tx = session.beginTransaction();

		for (Map<String, String> tableViewRow : tableRowList) {
			boolean isUniqueNotBlank = false;
			for (String uniqueField : uniqueHeaderSet) {
				if (tableViewRow.get(uniqueField) != null && !tableViewRow.get(uniqueField).trim().equals("")) {
					isUniqueNotBlank = true;
					break;
				}
			}

			if (isUniqueNotBlank) {
				try {
					strQuery = generateCreateQuery(viewName, tableViewRow, staffId, tableId, POSTGRESQL);
					if (StringUtils.isNotBlank(strQuery)) {
						Query query = session.createSQLQuery(strQuery).setTimestamp("createdDate", timestamp).setTimestamp("lastUpdatedDate", timestamp);
						createdRow = query.executeUpdate();
						 if (tx.getStatus() != TransactionStatus.COMMITTED){  
							tx.commit();
						}
						successTableRowList.add(tableViewRow);
					} else {
						logger.debug("Query obtained was blank");
					}
				} catch (ConstraintViolationException e) {
					logger.error(e);
					duplicateTableRowList.add(tableViewRow);
					 if (tx.getStatus() != TransactionStatus.COMMITTED){  
						tx.commit();
					}
				} catch (Exception e) {
					logger.error(e);
					errorTableRowList.add(tableViewRow);
					 if (tx.getStatus() != TransactionStatus.COMMITTED){  
						tx.commit();
					}
				}
			} else {
				errorTableRowList.add(tableViewRow);
			}
		}
		try{
			session.close();
		}catch(Exception e){
			logger.error(e);
			logger.debug("error while session closing");
		}
		return 1;
	}
	
	public String getDatabaseEnvironment() {
		Session session = openCurrentSession();
		try{			
			Transaction tx = session.beginTransaction();
			Object dialect = org.apache.commons.beanutils.PropertyUtils.getProperty(session.getSessionFactory(),
					"dialect");
			if(dialect != null && StringUtils.isNotBlank(dialect.toString())){
				if(StringUtils.containsIgnoreCase(dialect.toString(), ORACLE)){
					return ORACLE;
				}else if(StringUtils.containsIgnoreCase(dialect.toString(), MYSQL)){
					return MYSQL;
				}else if(StringUtils.containsIgnoreCase(dialect.toString(), POSTGRESQL)){
					return POSTGRESQL;
				}
				logger.info("dialect found : "+dialect.toString());
				
				tx.commit();
				session.flush();
				session.close();
			}else{
				logger.debug("Dialect obtained was null.");
				session.close();				
			}
		}catch(Exception e){
			session.close();
			logger.trace("Problem occured while getting hibernate dialect : ",e);
		}
		return new String("");
	}
	
}
