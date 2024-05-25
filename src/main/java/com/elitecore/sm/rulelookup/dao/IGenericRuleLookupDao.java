package com.elitecore.sm.rulelookup.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.elitecore.sm.rulelookup.model.RuleLookupTableData;

public interface IGenericRuleLookupDao {

	public String getDatabaseEnv();
	
	public void createViewInDb(RuleLookupTableData ruuleLookupTableData);

	void createViewInOracle(RuleLookupTableData ruleLookupTableData);

	void createViewInMySql(RuleLookupTableData ruleLookupTableData);

	void createViewInPostgreSql(RuleLookupTableData ruleLookupTableData);

	void deleteViewFormDb(RuleLookupTableData ruleLookupTableData);

	void deleteViewInPostgreSql(RuleLookupTableData ruleLookupTableData);

	void deleteViewInMySql(RuleLookupTableData ruleLookupTableData);

	void deleteViewInOracle(RuleLookupTableData ruleLookupTableData);

	String generateCreateQuery(RuleLookupTableData ruleLookupTableData);

	void deleteViewDataFormDb(int recordId, String viewName);

	void deleteViewDataInOracle(int recordId, String viewName);

	void deleteViewDataInMySql(int recordId, String viewName);

	void deleteViewDataInPostgreSql(int recordId, String viewName);

	int createRuleLookupRecordInView(String viewName, Map<String, String> lookUpdata, int staffId, int tableId);

	int createViewDataInOracle(String viewName, Map<String, String> lookUpdata, int staffId, int tableId, Timestamp timestamp);

	int createViewDataInMySql(String viewName, Map<String, String> lookUpdata, int staffId, int tableId, Timestamp timestamp);

	int createViewDataInPostgreSql(String viewName, Map<String, String> lookUpdata, int staffId, int tableId, Timestamp timestamp);

	int updateRuleLookupRecordInView(String viewName, Map<String, String> lookUpdata, int staffId);

	int updateViewDataInOracle(String viewName, Map<String, String> lookUpdata, int staffId, Timestamp timestamp);

	int updateViewDataInMySql(String viewName, Map<String, String> lookUpdata, int staffId, Timestamp timestamp);

	int updateViewDataInPostgreSql(String viewName, Map<String, String> lookUpdata, int staffId, Timestamp timestamp);

	void deleteViewDataFormDb(String viewName);

	void deleteViewDataInOracle(String viewName);

	void deleteViewDataInMySql(String viewName);

	void deleteViewDataInPostgreSql(String viewName);

	
	public void deleteViewDataSql(String viewName);
	
	public int updateLookupRecordsInView(String viewName, List<Map<String,String>> duplicateTableRowList,
			List<Map<String,String>> errorTableRowList, List<Map<String,String>> successTableRowList,Set<String> uniqueHeaderSet , int staffId);
		
	public int autoCreateRuleLookupRecord(String viewName, List<Map<String,String>>  tableRowList, List<Map<String,String>>  duplicateTableRowList,
			List<Map<String,String>>  errorTableRowList, List<Map<String,String>>  successTableRowList ,int staffId, int tableId, Set<String> uniqueHeaderSet);

}
