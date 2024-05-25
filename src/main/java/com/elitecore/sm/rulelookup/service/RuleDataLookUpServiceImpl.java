package com.elitecore.sm.rulelookup.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.model.JobTypeEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.integration.engine.RemoteJMXHelper;
import com.elitecore.sm.job.dao.JobDao;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.job.service.JobService;
import com.elitecore.sm.rulelookup.dao.IAutoJobStatisticConfigurationDao;
import com.elitecore.sm.rulelookup.dao.IAutoReloadCacheConfigurationDao;
import com.elitecore.sm.rulelookup.dao.IGenericRuleLookupDao;
import com.elitecore.sm.rulelookup.dao.ILookupFieldDetailDataDao;
import com.elitecore.sm.rulelookup.dao.IRuleLookupDataDao;
import com.elitecore.sm.rulelookup.dao.IRuleLookupTableConfigurationDao;
import com.elitecore.sm.rulelookup.dao.IRuleLookupTableDataDao;
import com.elitecore.sm.rulelookup.model.AutoJobStatistic;
import com.elitecore.sm.rulelookup.model.AutoReloadJobDetail;
import com.elitecore.sm.rulelookup.model.AutoUploadJobDetail;
import com.elitecore.sm.rulelookup.model.LookupFieldDetailData;
import com.elitecore.sm.rulelookup.model.RuleLookupData;
import com.elitecore.sm.rulelookup.model.RuleLookupTableData;
import com.elitecore.sm.rulelookup.model.ScheduleTypeEnum;
import com.elitecore.sm.scheduler.QuartJobSchedulingListener;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.systemparam.service.SystemParameterService;
import com.elitecore.sm.trigger.dao.TriggerDao;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;
import com.elitecore.sm.util.CSVUtils;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;

@Service(value="ruleDataLookUpService")
public class RuleDataLookUpServiceImpl implements IRuleDataLookUpService{
	
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	ServerInstanceService servInstanceService;

	@Autowired
	IRuleLookupTableDataDao ruleLookUpTableDao;
	
	@Autowired
	ILookupFieldDetailDataDao lookupFieldDetailDataDao;
	
	@Autowired
	IRuleLookupDataDao lookupDataDao;
	
	@Autowired
	IGenericRuleLookupDao genericLookupDao;
	
	@Autowired
	ServerInstanceDao serverInstanceDao;
	
	@Autowired
	SystemParameterService systemParamService;
	
	@Autowired
	IRuleLookupTableConfigurationDao ruleLookupTableConfigurationDao;
	
	@Autowired
	IAutoReloadCacheConfigurationDao autoReloadCacheDao;
	
	@Autowired
	IAutoJobStatisticConfigurationDao autoJobStatisticDao;
	
	@Autowired
	JobService jobService;
	
	@Autowired
	JobDao jobDao;
			
	@Autowired
	TriggerDao triggerDao;
	
	@Autowired
	QuartJobSchedulingListener joblistener;
	
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_RULE_LOOKUP_TABLE, actionType = BaseConstants.CREATE_ACTION, currentEntity = RuleLookupTableData.class, ignorePropList = "lookUpFieldDetailData,ruleLookupData")
	public ResponseObject createRuleLookUpTable(RuleLookupTableData ruleLookupTableData, int staffId){
		ResponseObject responseObject = new ResponseObject();
		ruleLookupTableData.setCreatedByStaffId(staffId);
		ruleLookupTableData.setLastUpdatedByStaffId(staffId);
		ruleLookupTableData.setCreatedDate(new Date());
		ruleLookupTableData.setLastUpdatedDate(new Date());
		int count = ruleLookUpTableDao.getCountByName(ruleLookupTableData.getViewName());
			if(count>0){
				logger.info("Duplicate Lookup Table nameFound");
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.DUPLICATE_LOOKUP_TABLE_FOUND);
			}else{
				/**   **/
				List<LookupFieldDetailData> fieldList = ruleLookupTableData.getLookUpFieldDetailData();				
				if(!CollectionUtils.isEmpty(fieldList)){
					Iterator<LookupFieldDetailData> itr = fieldList.iterator();
					int columnIsUniqueCount = 0;
					while (itr.hasNext()) {
						LookupFieldDetailData fields = itr.next();
						
						 if(fields.getIsUnique()) 
							 ++columnIsUniqueCount;
					}
					if(columnIsUniqueCount > 3){							
						responseObject.setSuccess(false);
						responseObject.setObject(null);
						responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FIELD_MORE_THAN_THREE_UNIQUE_FAIL);					
					}else if(columnIsUniqueCount < 1){
						responseObject.setSuccess(false);
						responseObject.setObject(null);
						responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FIELD_ATLEAST_ONE_UNIQUE_FAILURE);
					} else {
						ruleLookupTableData.setRuleLookupData(null);
						ruleLookUpTableDao.save(ruleLookupTableData);
						genericLookupDao.createViewInDb(ruleLookupTableData);
						if(ruleLookupTableData.getId()>0){
							responseObject.setSuccess(true);
							responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_TABLE_ADD_SUCESS);
							responseObject.setObject(ruleLookupTableData);
						}else{
							responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_TABLE_ADD_FAILURE);
							responseObject.setSuccess(false);
							responseObject.setObject(null);
						}
					}	
				} else {
					logger.info("Creating Lookup Table without any field");
					responseObject.setSuccess(false);
					responseObject.setObject(null);
					responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_VIEW_WITHOUT_ANY_FIELD);
				}
			}
		return responseObject;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getAllTableListCount(boolean isSearch,String searchName,String searchDesc){
		Map<String, Object> ruleTableDataConditions;
		if(isSearch){
			ruleTableDataConditions = ruleLookUpTableDao.getRuleForSearchTableConditionList(searchName,searchDesc);
		}else{
			ruleTableDataConditions = ruleLookUpTableDao.getRuleTableConditionList();			
		}
		return ruleLookUpTableDao.getQueryCount(RuleLookupTableData.class, (List<Criterion>) ruleTableDataConditions.get("conditions"),
				(HashMap<String, String>) ruleTableDataConditions.get("aliases"));
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RuleLookupTableData> getPaginatedList(int startIndex, int limit, String sidx,
			String sord,String searchName,String searchDesc, boolean isSearch) {
		Map<String,Object> tableConditionList ;
		if(isSearch){
			tableConditionList = ruleLookUpTableDao.getRuleForSearchTableConditionList(searchName,searchDesc);
		}else{
			tableConditionList = ruleLookUpTableDao.getRuleTableConditionList();			
		}
		return ruleLookUpTableDao.getPaginatedList(RuleLookupTableData.class, (List<Criterion>) tableConditionList.get("conditions"),
				(HashMap<String, String>) tableConditionList.get("aliases"), startIndex, limit, sidx, sord);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getFieldListCountByTableId(int tableId) {
		Map<String, Object> fieldListConditions = lookupFieldDetailDataDao.getFieldListConditionList(tableId);
		return lookupFieldDetailDataDao.getQueryCount(LookupFieldDetailData.class, (List<Criterion>) fieldListConditions.get("conditions"),
				(HashMap<String, String>) fieldListConditions.get("aliases"));
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<LookupFieldDetailData> getFieldListPaginatedList(int tableId, int startIndex, int limit, String sidx,
			String sord) {
		Map<String,Object> fieldListConditionList ;
		fieldListConditionList = lookupFieldDetailDataDao.getFieldListConditionList(tableId);
		return lookupFieldDetailDataDao.getPaginatedList(LookupFieldDetailData.class, (List<Criterion>) fieldListConditionList.get("conditions"),
				(HashMap<String, String>) fieldListConditionList.get("aliases"), startIndex, limit, sidx, sord);
	}

	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_RULE_LOOKUP_TABLE, actionType = BaseConstants.DELETE_ACTION, currentEntity = RuleLookupTableData.class, ignorePropList = "lookUpFieldDetailData,ruleLookupData")	
	public ResponseObject deleteLookupTable(int tableId, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		if(tableId > 0){
			RuleLookupTableData ruleLookupTableData = ruleLookUpTableDao.findByPrimaryKey(RuleLookupTableData.class, tableId);
			if(ruleLookupTableData != null){
				ruleLookupTableData.setLastUpdatedByStaffId(staffId);
				ruleLookupTableData.setLastUpdatedDate(new Date());
				ruleLookupTableData.setStatus(StateEnum.DELETED);
				List<LookupFieldDetailData> lookupFieldDetailDatas = ruleLookupTableData.getLookUpFieldDetailData();
				for(LookupFieldDetailData lookupFieldDetailData : lookupFieldDetailDatas){
					lookupFieldDetailData.setLastUpdatedByStaffId(staffId);
					lookupFieldDetailData.setLastUpdatedDate(new Date());
					lookupFieldDetailData.setStatus(StateEnum.DELETED);
				}
				genericLookupDao.deleteViewDataFormDb(ruleLookupTableData.getViewName());
				genericLookupDao.deleteViewFormDb(ruleLookupTableData);
				ruleLookupTableData.setViewName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, ruleLookupTableData.getViewName()));
				ruleLookUpTableDao.merge(ruleLookupTableData);
				responseObject.setSuccess(true);
				responseObject.setObject(ruleLookupTableData);
				responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_TABLE_DELETE_SUCESS);
			}else{
				responseObject.setSuccess(true);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_TABLE_DELETE_FAILURE);
			}
		}else{
			responseObject.setSuccess(true);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_TABLE_DELETE_FAILURE);
		}
		return responseObject;
	}

	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_RULE_LOOKUP_TABLE, actionType = BaseConstants.UPDATE_ACTION, currentEntity = RuleLookupTableData.class, ignorePropList = "ruleLookupData")
	public ResponseObject updateRuleLookupTable(RuleLookupTableData ruleLookupTableData, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		ruleLookupTableData.setLastUpdatedByStaffId(staffId);
		ruleLookupTableData.setCreatedDate(new Date());
		ruleLookupTableData.setLastUpdatedDate(new Date());
		RuleLookupTableData dbRuleLookupTableData = ruleLookUpTableDao.findByPrimaryKey(RuleLookupTableData.class, ruleLookupTableData.getId());
		if(dbRuleLookupTableData!=null && isUniqueForUpdate(ruleLookupTableData.getViewName(),dbRuleLookupTableData.getViewName())){
			logger.info("duplicate table name found:" + ruleLookupTableData.getViewName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_LOOKUP_TABLE_FOUND);
		}else{
			if(dbRuleLookupTableData != null){
				
				List<LookupFieldDetailData> fieldList = ruleLookupTableData.getLookUpFieldDetailData();				
				if(!CollectionUtils.isEmpty(fieldList)){
					Iterator<LookupFieldDetailData> itr = fieldList.iterator();
					int columnIsUniqueCount = 0;
					while (itr.hasNext()) {
						LookupFieldDetailData fields = itr.next();
						
						 if(fields.getIsUnique()) 
							 ++columnIsUniqueCount;
					}
					if(columnIsUniqueCount > 3){							
						responseObject.setSuccess(false);
						responseObject.setObject(null);
						responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FIELD_MORE_THAN_THREE_UNIQUE_FAIL);					
					}else if(columnIsUniqueCount < 1){
						responseObject.setSuccess(false);
						responseObject.setObject(null);
						responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FIELD_ATLEAST_ONE_UNIQUE_FAILURE);
					} else {
						dbRuleLookupTableData.setViewName(ruleLookupTableData.getViewName());
						dbRuleLookupTableData.clearLookupFieldDetailData();
						dbRuleLookupTableData.addLookupFieldDetailData(ruleLookupTableData.getLookUpFieldDetailData());
						dbRuleLookupTableData.setDescription(ruleLookupTableData.getDescription());
						clearLookupDataAfterUpdate(dbRuleLookupTableData.getLookUpFieldDetailData(),dbRuleLookupTableData.getRuleLookupData());
						ruleLookUpTableDao.merge(dbRuleLookupTableData);
						genericLookupDao.createViewInDb(ruleLookupTableData);
						responseObject.setSuccess(true);
						responseObject.setObject(ruleLookupTableData);
						responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_TABLE_UPDATE_SUCCESS);
					}	
				} else {
					logger.info("Updating Lookup Table without any field");
					responseObject.setSuccess(false);
					responseObject.setObject(null);
					responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_VIEW_WITHOUT_ANY_FIELD);
				}
			}else{
				responseObject.setObject(null);
				responseObject.setSuccess(false);	
			}
		}
		return responseObject;
	}
	
	
	@Override
	@Transactional
	public void clearLookupDataAfterUpdate(List<LookupFieldDetailData> lookUpFieldDetailData,List<RuleLookupData> ruleLookupDatas) {
		for(RuleLookupData ruleLookupData : ruleLookupDatas){
			int size = 0;
			for(LookupFieldDetailData lookupFieldDetailData2 : lookUpFieldDetailData){
				if(lookupFieldDetailData2 != null)
					++size;
			}
			++size;
			try{
				while(size<=20){
					Method method = ruleLookupData.getClass().getMethod("setStrParam"+size,String.class);
					method.invoke(ruleLookupData,"");
					size++;
				}
			}
			catch(Exception e){
				logger.debug("Problem occurred while restting values"+e);
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isUniqueForUpdate(String viewName, String dbName){
		if(!viewName.equalsIgnoreCase(dbName) && ruleLookUpTableDao.getCountByName(viewName) > 0){
			return true;
		}
		return false;
	}

	@Override
	@Transactional(readOnly = true)
	public String getCsvHeader(int tableId) {
		List<LookupFieldDetailData> lookupFieldDetailDatas = lookupFieldDetailDataDao.getSortedLookUpFieldDetailData(tableId);
		List<String> fieldNameList = new ArrayList<>();
		for(LookupFieldDetailData lookupFieldDetailData : lookupFieldDetailDatas){
			String fieldName = lookupFieldDetailData.getIsUnique() ? lookupFieldDetailData.getViewFieldName() + BaseConstants.TABLE_FIELD_UNIQUE_SEPERATOR : lookupFieldDetailData.getViewFieldName();
			fieldNameList.add(fieldName);
		}
		return StringUtils.join(fieldNameList.toArray(), ",");
	}

	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.DATA_UPLOADED_FOR_RULE_LOOKUP, actionType = BaseConstants.CREATE_ACTION, currentEntity = RuleLookupTableData.class, ignorePropList = "ruleLookupData")
	public ResponseObject insertDataIntoLookupTable(File lookupDataFile, String repositoryPath, String tableId , String mode) {
		ResponseObject responseObject = new ResponseObject();
	    String line = "";
	    String cvsSplitBy = ",";
	    int totalRecordsInFile = 0;
	    Map<String,String> uploadSummery = new HashMap<>();
	    //StringTokenizer st = null;
	    try(BufferedReader br = new BufferedReader(new FileReader(lookupDataFile))){
	    	line = br.readLine();
	    	if(line == null){
	    		responseObject.setSuccess(false);
	    		responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FILE_EMPTY);
	    	}
	    	else{
	    		if(StringUtils.equalsIgnoreCase(line, getCsvHeader(Integer.parseInt(tableId)))){
	    			RuleLookupTableData ruleLookupTableData = ruleLookUpTableDao.findByPrimaryKey(RuleLookupTableData.class, Integer.parseInt(tableId));	    		
	    						
	    			List<String> strParamIDList = new ArrayList<>();
	    			List<String> lookupDuplicateDataForCSV = new ArrayList<>();
	    			getLookUpDataIDSet( ruleLookupTableData ,  strParamIDList );
	    			
	    			List<RuleLookupData> ruleLookupDatas = new ArrayList<>();
	    			int length = getCsvHeader(Integer.parseInt(tableId)).split(cvsSplitBy).length;
	    			List<Set<String>> uniqueSet = new ArrayList<>(length);
	    			initUniqueSet(uniqueSet,length);
	    			line = br.readLine();
	    			if(line != null){
	    				while(line != null){
	    				/**	st = new StringTokenizer(line, cvsSplitBy); **/
	    					RuleLookupData ruleLookupData = new RuleLookupData();
	    					/** if(st.countTokens() == length){
	    						int count = 1;
	    						while(st.hasMoreTokens()){
	    							String token = st.nextToken();
	    							if(ruleLookupTableData.getLookUpFieldDetailData().get(count-1).getIsUnique() && !checkUniqueForInsert(count,uniqueSet, tableId, token)){
	    								responseObject.setSuccess(false);
	    								responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_DATA_UNIQUE_VIOLATION);
	    								responseObject.setObject(null);
	    								return responseObject;
	    							}
	    							insertIntoProperStr(ruleLookupData,count,token);
	    							count++;
	    						}
	    					}else{
	    						responseObject.setSuccess(false);
	    						responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FILE_DATA_INVALID);
	    						responseObject.setObject(null);
	    					}*/
	    					List<String> tokens = CSVUtils.parseLine(line);
	    					if(!CollectionUtils.isEmpty(tokens)) {
	    						int tokensLength = tokens.size();
	    						if(tokensLength == length) {
	    							int count = 1;
	    							for(int i = 0; i < tokensLength; i++) {
	    								String token = tokens.get(i);
	    								if(i == 0 && (token == null || token.isEmpty())) {
	    									responseObject.setSuccess(false);
		    								responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_DATA_EMPTY_OR_NULL_VALUE_IN_ID);
		    								responseObject.setObject(null);
		    								return responseObject;
	    								} else if (ruleLookupTableData.getLookUpFieldDetailData().get(count-1).getIsUnique() && !checkUniqueForInsert(count,uniqueSet, tableId, token)){
		    								responseObject.setSuccess(false);
		    								responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_DATA_UNIQUE_VIOLATION);
		    								responseObject.setObject(null);
		    								return responseObject;
		    							}
		    							insertIntoProperStr(ruleLookupData,count,token);
		    							count++;
		    						}
	    						} else {
	    							responseObject.setSuccess(false);
		    						responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FILE_DATA_INVALID);
		    						responseObject.setObject(null);
	    						}	    						
	    					}
			    				if( "append".equals( mode )){
			    					separateDuplicateIDFromFile(strParamIDList, tokens, lookupDuplicateDataForCSV, line, ruleLookupData, 
			    							  ruleLookupTableData, ruleLookupDatas);
			    				}else{
			    					ruleLookupData.setRuleLookUpTable(ruleLookupTableData);
			    					ruleLookupDatas.add(ruleLookupData);
			    				}
			    				++totalRecordsInFile;	
	    				  line = br.readLine();
	    				}
	    				/** existing data maintained **/
	    				
	    				uploadSummery.put( "TotalRecord" , Integer.toString( totalRecordsInFile ) );
		    			uploadSummery.put( "DuplicateRecord" , Integer.toString( lookupDuplicateDataForCSV.size() ) );
		    			uploadSummery.put( "ValidRecord" , Integer.toString( totalRecordsInFile - lookupDuplicateDataForCSV.size() ) );
		    			
	    				if( "append".equals( mode )){
	    					appendExistingData(ruleLookupTableData, ruleLookupDatas , mode );
	    				}
	    				
	    			}else{	    				
	    				   appendExistingData(ruleLookupTableData, ruleLookupDatas , mode );
	    				
	    				responseObject.setSuccess(false);
		    			responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FILE_EMPTY);
		    			responseObject.setObject(null);
	    			}
	    			
	    			if( lookupDuplicateDataForCSV.isEmpty() ){
	    				logger.info("duplicate table data not found in file");
	    			}
	    			else{	    				
		    			logger.info("Numbers of "+lookupDuplicateDataForCSV.size() + " duplicate table data found in file : " + lookupDataFile.getName());
	    			}
	    			 writeLookUpDataForDupliateIDS( lookupDuplicateDataForCSV , repositoryPath , lookupDataFile.getName() , tableId , uploadSummery );
	    			
	    			responseObject.setArgs(new Object[]{JSONValue.toJSONString(uploadSummery)});
	    		    
	    			ruleLookupTableData.clearRuleLookupData();
	    			ruleLookupTableData.addRuleLookupData(ruleLookupDatas);
	    			ruleLookUpTableDao.merge(ruleLookupTableData);
	    			responseObject.setSuccess(true);
					responseObject.setObject(ruleLookupTableData);
					responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_DATA_INSERT_SUCCESS);
					
					logger.info("Numbers of "+ (totalRecordsInFile - lookupDuplicateDataForCSV.size()) + " valid records of file : " + lookupDataFile.getName() + " inserted in database");		
					
	    		}else{
	    			responseObject.setSuccess(false);
	    			responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FILE_HEADER_MISMATCH);
	    			responseObject.setObject(null);
	   			}
	    	}
	    }catch(FileNotFoundException e){
	    	logger.trace("Lookup data file not found : ",e);
	    	responseObject.setSuccess(false);
	    	responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FILE_READING_ERROR);
	    	responseObject.setObject(null);
	    }catch (IOException e) {
			logger.trace("Problem occurred while reading file : ",e);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FILE_READING_ERROR);
			responseObject.setObject(null);
		}catch (Exception e) {
			logger.trace("Problem occurred while reading file : ",e);
			System.out.println( "Problem occurred while reading file : " + e );
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FILE_READING_ERROR);
			responseObject.setObject(null);
		}
		return responseObject;
	}

	private void initUniqueSet(List<Set<String>> uniqueSet, int length) {
		for(int i=0;i<length;i++){
		 	Set <String> token = new HashSet<>();
		 	uniqueSet.add(token);
		}
	}

	@Override
	public void insertIntoProperStr(RuleLookupData ruleLookupData,int count,String token){
		switch (count) {
		case 1:
			ruleLookupData.setStrParam1(token);
			break;
		case 2:
			ruleLookupData.setStrParam2(token);
			break;
		case 3:
			ruleLookupData.setStrParam3(token);
			break;
		case 4:
			ruleLookupData.setStrParam4(token);
			break;
		case 5:
			ruleLookupData.setStrParam5(token);
			break;
		case 6:
			ruleLookupData.setStrParam6(token);
			break;
		case 7:
			ruleLookupData.setStrParam7(token);
			break;
		case 8:
			ruleLookupData.setStrParam8(token);
			break;
		case 9:
			ruleLookupData.setStrParam9(token);
			break;
		case 10:
			ruleLookupData.setStrParam10(token);
			break;
		case 11:
			ruleLookupData.setStrParam11(token);
			break;
		case 12:
			ruleLookupData.setStrParam12(token);
			break;
		case 13:
			ruleLookupData.setStrParam13(token);
			break;
		case 14:
			ruleLookupData.setStrParam14(token);
			break;
		case 15:
			ruleLookupData.setStrParam15(token);
			break;
		case 16:
			ruleLookupData.setStrParam16(token);
			break;
		case 17:
			ruleLookupData.setStrParam17(token);
			break;
		case 18:
			ruleLookupData.setStrParam18(token);
			break;
		case 19:
			ruleLookupData.setStrParam19(token);
			break;
		case 20:
			ruleLookupData.setStrParam20(token);
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public boolean checkUniqueForInsert(int count, List<Set<String>> uniqueSet, String tableId, String token){
		if(uniqueSet.get(count-1).add(token)){
			return true;
		}else{
			return false;
		}
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<List<String>> getLookupTableData(int tableId) {
		return lookupDataDao.getRuleLookupTableDataByTableId(tableId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> getLookupTableFields(int tableId) {
		 return lookupDataDao.getLookupFieldsByTableId( tableId );
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getRuleLookupDataListCountById(int tableId){
		Map<String, Object> tableFieldConditionList;
		tableFieldConditionList = lookupDataDao.getRuleTableFieldConditionList(tableId);			
		return lookupDataDao.getQueryCount(RuleLookupData.class, (List<Criterion>) tableFieldConditionList.get("conditions"),
				(HashMap<String, String>) tableFieldConditionList.get("aliases"));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<RuleLookupData> getRuleLookupDataPaginatedList(int startIndex, int limit, String sidx, String sord, int tableId) {
		Map<String,Object> tableFieldConditionList ;
		tableFieldConditionList = lookupDataDao.getRuleTableFieldConditionList(tableId);	
		return lookupDataDao.getPaginatedList(RuleLookupData.class, (List<Criterion>) tableFieldConditionList.get("conditions"),
				(HashMap<String, String>) tableFieldConditionList.get("aliases"), startIndex, limit, sidx, sord);
	}
	
	@Override
	@Transactional(readOnly = true)
	public long getSearchLookupDataListCountById(String tableviewname , int tableId, String searchQuery){
		
		long result =-1;	
		String sql="select COUNT(RECORDROWID) AS count from "+ tableviewname +" where " + searchQuery;
		if(searchQuery==null || searchQuery.trim() == ""){
			sql="select COUNT(*) AS count from "+ tableviewname ;
		}
		try{
			result = lookupDataDao.getTotalCountUsingSQL(sql, null);
			
		  }catch(Exception ex){		 		
				logger.trace("Invalid Lookup data query  : ",ex);
			}
		return result;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Map<String,Object>> getSearchLookupDataPaginatedList(int startIndex, int limit, String sidx, String sord, int tableId, String tableviewname,
			String searchQuery ,ResponseObject responseObject) {
		
		StringBuilder sql = new StringBuilder("select * from ").append(tableviewname);
		if(StringUtils.isNotBlank(searchQuery)){
			sql.append(" where ").append(searchQuery);
		}
		if(StringUtils.isNotBlank(sidx) && StringUtils.isNotBlank(sord)){
			sql.append(" ORDER BY ").append(sidx).append(" ").append(sord);
		}
		
		List<Map<String,Object>> searchLookUpDataList =null;
		
		try{
			searchLookUpDataList =	lookupDataDao.getListUsingSQL(sql.toString(), null , startIndex , limit , responseObject);
			if(searchLookUpDataList != null){
				responseObject.setSuccess(true);
			}
		}catch(Exception ex){
			responseObject.setSuccess(false);			
			logger.info("Invalid Lookup data query  : ",ex);
		}
		
		return searchLookUpDataList;
	}
	
	private void getLookUpDataIDSet(RuleLookupTableData ruleLookupTableData , List<String> strParamIDList ) {
		 List<RuleLookupData>  ruleLookupDatas = ruleLookupTableData.getRuleLookupData();
		 if(ruleLookupDatas.isEmpty()){
			 logger.info("Data not found for LookUpTables : " + ruleLookupTableData.getViewName() );
		 }else{
			 logger.info("Total Numbers of "+ ruleLookupDatas.size() +" Data found for LookUpTables : " + ruleLookupTableData.getViewName() );
		 }		 
		for(RuleLookupData ruleLookupData : ruleLookupDatas){
			strParamIDList.add(ruleLookupData.getStrParam1());
		}
	}
	
	private Map<String,String> writeLookUpDataForDupliateIDS( List<String>  lookupDuplicateDataForCSV , String repositoryPath ,String fileName ,
			String tableId , Map<String,String> uploadSummery) {
		BufferedWriter outw = null;
		FileWriter fw=null;
		fileName = fileName.split(".csv")[0];		
		try{ //NOSONAR
			if(!lookupDuplicateDataForCSV.isEmpty())
			{
				String duplicateDataFileName = fileName +"_" + new Date().getTime() + ".csv";
				fw=new FileWriter( repositoryPath + duplicateDataFileName );
				outw = new BufferedWriter(fw);
				logger.info("duplicate table data written in file : " + repositoryPath + duplicateDataFileName );
				 
				outw.append( getCsvHeader( Integer.parseInt(tableId) ) );
				outw.append( "\n" );
				
				 for(String ruleLookupData : lookupDuplicateDataForCSV){					 
					 outw.append( ruleLookupData );
					 outw.append( "\n" );
				 }
				 uploadSummery.put("duplicateRecordFileName",  duplicateDataFileName ); 
			}
		}catch(Exception exio){
			logger.trace("Problem occurred while writing file : ", exio);
		} finally {
			if(fw!=null)
				try {
					fw.close();
				} catch (IOException e) {
					logger.trace("Problem occurred while writing file : ", e);
				}
			if(outw !=null)
				try {
					outw.close();
				} catch (IOException e) {
					logger.trace("Problem occurred while writing file : ", e);
				}
		}
		
		return uploadSummery;
	}
	
	private void separateDuplicateIDFromFile(List<String> strParamIDList ,List<String> tokens ,List<String> lookupDuplicateDataForCSV , 
			String line ,RuleLookupData  ruleLookupData ,RuleLookupTableData ruleLookupTableData ,List<RuleLookupData> ruleLookupDatas ){
		
		 if( strParamIDList.contains( tokens.get(0) ) ){	
				lookupDuplicateDataForCSV.add( line );	
			}else{
				ruleLookupData.setRuleLookUpTable(ruleLookupTableData);
				ruleLookupDatas.add(ruleLookupData);
			}
	}
	
	private void appendExistingData( RuleLookupTableData ruleLookupTableData , List<RuleLookupData> ruleLookupDatas , String mode ){		
		if( mode.equals("append")){
			List<RuleLookupData> existingLookUpDatas = ruleLookupTableData.getRuleLookupData();    					
			for(RuleLookupData lookupdata : existingLookUpDatas){
					ruleLookupDatas.add( lookupdata );
			}
		}
	}
	
	@Override
	@Transactional
	public ResponseObject deleteRuleLookupTableRecords(String recordIds, String viewName) {
		ResponseObject responseObject = new ResponseObject();
		
		if(!StringUtils.isEmpty(recordIds)){
			String [] recordIdList = recordIds.split(",");
			
			for(int i = 0; i < recordIdList.length; i ++ ){
				responseObject = deleteLookupRecords(Integer.parseInt(recordIdList[i]), viewName);
			}
			
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_VIEW_DATA_DELETE_SUCCESS);
		}else{
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_VIEW_DATA_DELETE_FAIL);
		}
		
		return responseObject;
	}
	
	@Override
	@Transactional
	public ResponseObject deleteLookupRecords(int recordId, String viewName) {
		ResponseObject responseObject = new ResponseObject();
		if(recordId > 0){
			genericLookupDao.deleteViewDataFormDb(recordId, viewName);
		}else{
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_VIEW_DATA_DELETE_FAIL);
		}
		return responseObject;
	}

	private List<AutoReloadJobDetail> getListOfImmediateExecution(String viewName) {
		int id = ruleLookUpTableDao.getRuleLookTableIdByViewName(viewName);
		return autoReloadCacheDao.getListOfImmediateExecutionByViewName(id);
	}
	
	@Override
	@Transactional
	public ResponseObject deleteRuleLookupTables(String tableIds, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		
		if(!StringUtils.isEmpty(tableIds)){
			String [] tableIdList = tableIds.split(",");
			
			for(int i = 0; i < tableIdList.length; i ++ ){
				responseObject = deleteLookupTable(Integer.parseInt(tableIdList[i]), staffId);
			}
			
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_TABLE_DELETE_SUCESS);
		}else{
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_TABLE_DELETE_FAILURE);
		}
		
		return responseObject;
	}
	
	@Override
	public ResponseObject doLookupTableDataReload(AutoReloadJobDetail configObject,AutoJobStatistic autoJobStatistic) {
		
		ResponseObject responseObject = new ResponseObject();
		int iserverInstanceId = configObject.getServerInstance().getId();
		if(iserverInstanceId!=0){
			ServerInstance serverInstance = servInstanceService.getServerInstance(iserverInstanceId);
			if (serverInstance != null && serverInstance.getServer() != null && serverInstance.getServer().getIpAddress() != null) {
				RemoteJMXHelper remoteJMXHelper = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(),
						serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());
				List<String> databaseQueryList =  Arrays.asList(configObject.getDatabaseQueryList().split(","));
				
				Map<String, Map<String,Integer>> dbQueryDetailMap = remoteJMXHelper.reloadLookupDataCache(databaseQueryList, configObject.getScheduler().getLastRunTime(), configObject.getReloadOptions().toString());
 
				if (remoteJMXHelper.getErrorMessage() == null) {
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_SOFT_RESTART_SUCCESS);
					responseObject.setObject(serverInstance);
					if(autoJobStatistic!=null){
						StringBuffer dbQueryList = new StringBuffer();
						StringBuffer reloadRecordCount = new StringBuffer();
						StringBuffer reloadDBQueryStatus = new StringBuffer();
						for(String databaseQuery : databaseQueryList){
							if(org.apache.commons.lang3.StringUtils.isNotBlank(dbQueryList)){
								dbQueryList.append(",");
							}
							dbQueryList.append(databaseQuery);
							Map<String,Integer> recordCountMap = dbQueryDetailMap.get(databaseQuery);
							Set<String> queryStatus = recordCountMap.keySet();
							if(queryStatus!=null && !queryStatus.isEmpty()){
								for(String status : queryStatus){
	 								Integer recordCount = recordCountMap.get(status);
	 								if(org.apache.commons.lang3.StringUtils.isNotBlank(reloadDBQueryStatus)){
	 									reloadDBQueryStatus.append(",");
	 								}
	 								reloadDBQueryStatus.append(status);
	 								if(org.apache.commons.lang3.StringUtils.isNotBlank(reloadRecordCount)){
	 									reloadRecordCount.append(",");
	 								}
	 								reloadRecordCount.append(recordCount.toString());
								}
								autoJobStatistic.setReloadDbQueryStatus(reloadDBQueryStatus.toString());
								autoJobStatistic.setReloadRecordCount(reloadRecordCount.toString());
							}
						}
						
					}
				} else if (remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_CONNECTION_FAILURE)) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_SOFT_RESTART_JMX_CONN_FAIL);
					autoJobStatistic.setReason("Server Instance JMX Connection Failed");
				} else if (remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_API_FAILURE)) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_SOFT_RESTART_JMX_API_FAIL);
					autoJobStatistic.setReason("Server Instance JMX API Failed");
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_SOFT_RESTART_FAIL);
					autoJobStatistic.setReason("Server Instance Soft Restart Failed");
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UNAVALIABLE);
				autoJobStatistic.setReason("Server Instance unavailable");
			}
		}
		return responseObject;
	}
	
	@Override
	@Transactional
	public ResponseObject createRuleLookupRecord(String viewName, Map<String, String> lookUpdata, int staffId, int tableId) {
		ResponseObject responseObject = new ResponseObject();
		if(!CollectionUtils.isEmpty(lookUpdata)){
			int createdRow = genericLookupDao.createRuleLookupRecordInView(viewName, lookUpdata, staffId, tableId);
			if(createdRow == 1){
				responseObject.setSuccess(true);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_VIEW_DATA_CREATE_SUCCESS);
			} else{
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_VIEW_DATA_CREATE_FAIL);
			}
		}
		return responseObject;
	}
	
	@Override
	@Transactional
	public ResponseObject updateRuleLookupRecord(String viewName, Map<String, String> lookUpdata, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		if(!CollectionUtils.isEmpty(lookUpdata)){
			int updatedRow = genericLookupDao.updateRuleLookupRecordInView(viewName, lookUpdata, staffId);
			if(updatedRow == 1){
				responseObject.setSuccess(true);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_VIEW_DATA_UPDATE_SUCCESS);
			} else{
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_VIEW_DATA_UPDATE_FAIL);
			}
		}
		return responseObject;
	}
	
	@Override 
	@Transactional(readOnly = true)
	public List<Map<String,Object>> getLookupViewData(int tableId, String tableviewname,String searchQuery ,ResponseObject responseObject) {
		
		String sql="select * from "+ tableviewname +" where  " + searchQuery;
		if(searchQuery==null || searchQuery.trim() == ""){
			sql="select * from "+ tableviewname;
		}
		List<Map<String,Object>> searchLookUpDataList =null;
		
		try{
			searchLookUpDataList =	lookupDataDao.getListUsingSQL(sql, null , -1 , -1 , responseObject);
			
			if(searchLookUpDataList != null){
				responseObject.setSuccess(true);
			}
		}catch(Exception ex){
			responseObject.setSuccess(false);			
			logger.info("Invalid Lookup data query  : ",ex);
		}
		
		return searchLookUpDataList;
	}
	
	@Override
	@Transactional(readOnly=true)
	public void reloadImmediateUploadDataInView(String viewName, Timestamp lookupDataReloadFromDate){
		List<AutoReloadJobDetail> autoReloadJobDetailList = this.getListOfImmediateExecution(viewName);
		if(autoReloadJobDetailList != null && autoReloadJobDetailList.size()>0){								
			ExecutorService executorService = Executors.newFixedThreadPool(autoReloadJobDetailList.size());
			for(AutoReloadJobDetail configObject : autoReloadJobDetailList){
				CrestelSMJob job = new CrestelSMJob();
				job.setLastRunTime(lookupDataReloadFromDate);
				configObject.setScheduler(job);
				executorService.execute(new Runnable() {
				    @Override
					public void run() {									    	
				    	doLookupTableDataReload(configObject,null);
				    }
				});														
			}
			executorService.shutdown();
		}
	}
	
	//@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Transactional
	@Override	
	public ResponseObject insertDataIntoLookupView(String header ,RuleLookupTableData lookUpData,File lookupDataFile, String repositoryPath, 
			String tableId , String mode , int staffId,AutoJobStatistic autoJobStatistic) {
		ResponseObject responseObject = new ResponseObject();
	    String line = "";
	    String cvsSplitBy = ",";
	    int totalRecordsInFile=0;
	    String tableName= null;
	    Map<String,String> uploadSummery = new HashMap<>();	    
	    Map<String,String> tableRow = null;
	    List<Map<String,String>> tableRowList = new ArrayList<>(); 
	    Set<String> uniqueHeaderSet = new HashSet<>();
	    int failedFileCount = 0;
	    int successFileCount = 0;
		List<String> errorRowList = new ArrayList<String>();
	    
	    if(autoJobStatistic!=null){
	    	failedFileCount = autoJobStatistic.getFailedFileCount();
	 	    successFileCount = autoJobStatistic.getSuccessFileCount();
	    }
	    try(BufferedReader br = new BufferedReader(new FileReader(lookupDataFile))){
	    	line = br.readLine();
	    	if(line == null){
	    		if(autoJobStatistic!=null){
	    			failedFileCount++;
	    			autoJobStatistic.setFailedFileCount(failedFileCount);
	    			writeRejectedFile( lookupDataFile, getSystemPathTowriteFile( repositoryPath,lookUpData.getViewName(), "Error") , lookupDataFile.getName()  );
	    		}
	    		responseObject.setSuccess(false);
	    		responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FILE_EMPTY);
	    		return  responseObject;
	    	}
	    	else{
	    		//if(StringUtils.equalsIgnoreCase(line, getCsvHeader(Integer.parseInt(tableId)))){
	    		line = line.replace(BaseConstants.TABLE_FIELD_UNIQUE_SEPERATOR, "");
	    		header = header.replace(BaseConstants.TABLE_FIELD_UNIQUE_SEPERATOR, "");
	    		if(isColumnMatch(line,header,cvsSplitBy)){
	    			/** RuleLookupTableData ruleLookupTableData = ruleLookUpTableDao.findByPrimaryKey(RuleLookupTableData.class, Integer.parseInt(tableId));
	    			**/
	    			header = line;
	    			RuleLookupTableData ruleLookupTableData = lookUpData; /** getLookUpTableData(tableId); **/
	    			
	    			for( LookupFieldDetailData  tableField: ruleLookupTableData.getLookUpFieldDetailData() ){	    				
	    				if(tableField.getIsUnique()){
	    					uniqueHeaderSet.add( tableField.getViewFieldName() );
	    				}
	    			}
	    		 
	    			tableName =  ruleLookupTableData.getViewName(); 
	    			String[] fileHeader = header.split(cvsSplitBy);
	    			
	    			int length = header.split(cvsSplitBy).length;
	    			List<Set<String>> uniqueSet = new ArrayList<>(length);
	    			initUniqueSet(uniqueSet,length);
	    			line = br.readLine();
	    			if(line != null){
	    				while(line != null){
	    					tableRow = new Hashtable<>();
	    					List<String> tokens = CSVUtils.parseLine(line);
	    					boolean islengthMatched = false;
	    					if(!CollectionUtils.isEmpty(tokens)) {
	    						int tokensLength = tokens.size();
	    						if(tokensLength == length) {
	    							islengthMatched = true;
	    							int count = 1;
	    							for(int i = 0; i < tokensLength; i++) {
	    								String token = tokens.get(i);
	    								// MED-6783 - upload and download issue with Regex
	    								if (token != null && !token.isEmpty() && token.contains(","))
	    								{
	    									if (token.charAt(0) == '"') {
	    										token = token.substring(1, token.length());
	    									}
	    									if (token.charAt(token.length()-1) == '"') {
	    										token = token.substring(0, token.length() - 1);
	    									}
	    								}
	    								/**if(i == 0 && (token == null || token.isEmpty())) {
	    									responseObject.setSuccess(false);
		    								responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_DATA_EMPTY_OR_NULL_VALUE_IN_ID);
		    								responseObject.setObject(null);
		    								return responseObject;
	    								}  else if (ruleLookupTableData.getLookUpFieldDetailData().get(count-1).getIsUnique() && !checkUniqueForInsert(count,uniqueSet, tableId, token)){
		    								responseObject.setSuccess(false);
		    								responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_DATA_UNIQUE_VIOLATION);
		    								responseObject.setObject(null);
		    								return responseObject;
		    							}*/
		    						/**	 insertIntoProperStr(ruleLookupData,count,token); **/
		    							count++;
		    							
		    						  tableRow.put( fileHeader[i] , token);
		    						}
	    						} else {
	    							if(autoJobStatistic!=null){
	    								failedFileCount++;
	    				    			autoJobStatistic.setFailedFileCount(failedFileCount);
	    				    			writeRejectedFile( lookupDataFile, getSystemPathTowriteFile( repositoryPath,lookUpData.getViewName(), "Error") , lookupDataFile.getName()  );
	    				    		}
	    							/*responseObject.setSuccess(false);
		    						responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FILE_DATA_INVALID);
		    						responseObject.setObject(null);
		    						return  responseObject;*/
	    							errorRowList.add(line);
	    						}	    						
	    					}
	    					if(islengthMatched)
	    						tableRowList.add( tableRow );
			    		  ++totalRecordsInFile;	//NOSONAR
	    				  line = br.readLine();
	    				}
	    				/** existing data maintained **/
	    			}else{
	    				if(autoJobStatistic!=null){
	    					failedFileCount++;
	    	    			autoJobStatistic.setFailedFileCount(failedFileCount);
	    	    			writeRejectedFile( lookupDataFile, getSystemPathTowriteFile( repositoryPath,lookUpData.getViewName(), "Error") , lookupDataFile.getName()  );
	    	    		}
	    				responseObject.setSuccess(false);
		    			responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FILE_EMPTY);
		    			responseObject.setObject(null);
		    			return  responseObject;
	    			}	    			
	    			
	    			responseObject.setSuccess(true);
					responseObject.setObject(ruleLookupTableData);
					/**	responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_DATA_INSERT_SUCCESS); **/
					ruleLookupTableData.clearRuleLookupData();
	    		}else{
	    			if(autoJobStatistic!=null){
	    				failedFileCount++;
		    			autoJobStatistic.setFailedFileCount(failedFileCount);
		    			writeRejectedFile( lookupDataFile, getSystemPathTowriteFile( repositoryPath,lookUpData.getViewName(), "Error") , lookupDataFile.getName()  );
		    		}
	    			responseObject.setSuccess(false);
	    			responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FILE_HEADER_MISMATCH);
	    			responseObject.setObject(null);
	    			return  responseObject;
	   			}
	    	}
	    	/** valid file is going to store in database**/
	    	if( responseObject.isSuccess() && "append".equals(mode)){
	    		appendLookUpData( tableRowList , tableId , uploadSummery , 
	    				 staffId ,  tableName ,  totalRecordsInFile ,  lookupDataFile.getName()  ,  responseObject , header, autoJobStatistic, repositoryPath,uniqueHeaderSet,errorRowList);	
	    		successFileCount++;
		    }else if( responseObject.isSuccess() && "overwrite".equals(mode)){		    	
		    	overwriteLookUpData(  tableRowList , tableId ,  uploadSummery , 
		    			 staffId ,  tableName ,  totalRecordsInFile ,  lookupDataFile.getName() ,  responseObject , header, autoJobStatistic, repositoryPath , uniqueHeaderSet,errorRowList);
		    	successFileCount++;
		    }else if(responseObject.isSuccess() && "update".equals(mode)){
		    	updateLookUpData(  tableRowList , tableId , uploadSummery , 
		    			 staffId ,  tableName ,  totalRecordsInFile ,  lookupDataFile.getName() ,  responseObject , uniqueHeaderSet , header, autoJobStatistic, repositoryPath,errorRowList);
		    	successFileCount++;
		    }else{
		    	if(autoJobStatistic!=null){
	    			writeRejectedFile( lookupDataFile, getSystemPathTowriteFile( repositoryPath,tableName, "Error") , lookupDataFile.getName()  );
	    		}
		    	logger.trace("Lookup data not inserted");
		    	responseObject.setSuccess(false);
		    	responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FILE_READING_ERROR);
		    	responseObject.setObject(null);
		    	failedFileCount++;
		    }
	    	
	    }catch(FileNotFoundException e){
	    	if(autoJobStatistic!=null){
    			writeRejectedFile( lookupDataFile, getSystemPathTowriteFile( repositoryPath,tableName, "Error") , lookupDataFile.getName()  );
    		}
	    	logger.trace("Lookup data file not found : ",e);
	    	responseObject.setSuccess(false);
	    	responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FILE_READING_ERROR);
	    	responseObject.setObject(null);
	    	failedFileCount++;
	    }catch (IOException e) {
	    	if(autoJobStatistic!=null){
    			writeRejectedFile( lookupDataFile, getSystemPathTowriteFile( repositoryPath,tableName, "Error") , lookupDataFile.getName()  );
    		}
			logger.trace("Problem occurred while reading file : ",e);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FILE_READING_ERROR);
			responseObject.setObject(null);
			failedFileCount++;
		}catch (Exception e) {
	    	if(autoJobStatistic!=null){
    			writeRejectedFile( lookupDataFile, getSystemPathTowriteFile( repositoryPath,tableName, "Error") , lookupDataFile.getName()  );
    		}
			logger.trace("Problem occurred while reading file : ",e);			
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_FILE_READING_ERROR);
			responseObject.setObject(null);
			failedFileCount++;
		}
	    finally{
	    	logger.info("File is deleted at "+lookupDataFile.getAbsolutePath() + " : "+lookupDataFile.delete());
	    }
	    if(autoJobStatistic!=null){
	    	autoJobStatistic.setFailedFileCount(failedFileCount);
		    autoJobStatistic.setSuccessFileCount(successFileCount);
	    }
		return responseObject;
		
	}

	private boolean isColumnMatch(String line, String header, String cvsSplitBy) {
		try {
			String[] lineArray = line.split(cvsSplitBy);
			String[] headerArray = header.split(cvsSplitBy);
			if(lineArray.length != headerArray.length)
				return false;
			Arrays.sort(lineArray);
			Arrays.sort(headerArray);
			for(Integer i = 0;i<lineArray.length;i++) {
				if(!lineArray[i].equalsIgnoreCase(headerArray[i]))
					return false;
			}
		} catch (Exception e) {
			logger.debug("There is some error while matching header "+e);
			return false;
		}
		return true;
	}

	//@Transactional(readOnly=true,propagation = Propagation.REQUIRED, rollbackFor = ConstraintViolationException.class)
	protected  ResponseObject appendLookUpData( List<Map<String,String>>  tableRowList ,String tableId , Map<String,String> uploadSummery , 
			int staffId , String tableName , int totalRecordsInFile , String fileName , ResponseObject responseObject , String columnHeader, AutoJobStatistic autoJobStatistic, String repositoryPath , Set<String> uniqueHeaderSet,List<String> errorRowList) {
		
		List<Map<String,String>> duplicateTableRowList = new ArrayList<>();
		List<Map<String,String>> errorTableRowList = new ArrayList<>();
		List<Map<String,String>> successTableRowList = new ArrayList<>();
		
		String recordType = "Duplicate";
    	int result = 0;
    	 		
    		try{
    		 genericLookupDao.autoCreateRuleLookupRecord( tableName , tableRowList,duplicateTableRowList,errorTableRowList, successTableRowList, staffId, Integer.parseInt(tableId), uniqueHeaderSet);
    			/**if(result==1){
    				successTableRowList.add( tableViewRow );
    			}else if(result==2){
    				duplicateTableRowList.add( tableViewRow );
    			}else {
    				errorTableRowList.add( tableViewRow );
    			}*/
    		}catch(Exception e){
    			logger.trace("Lookup data not inserted = ",e);
    		}	
    	String systemPath = "";
    	 if(autoJobStatistic==null){
    		 repositoryPath = MapCache.getConfigValueAsObject(SystemParametersConstant.LOOKUP_DUPLICATE_DATA_PATHDETAIL).toString();
    	 }
    	systemPath = getSystemPathTowriteFile( repositoryPath, 
	    			tableName, recordType);
    	writeLookUpDataFileStorage( duplicateTableRowList , systemPath, fileName , tableId, uploadSummery,recordType , columnHeader,errorRowList );
    	
    	recordType="Error";
    	systemPath = getSystemPathTowriteFile(repositoryPath, 
    			tableName, recordType);
    	writeLookUpDataFileStorage( errorTableRowList , systemPath, fileName , tableId, uploadSummery,recordType , columnHeader,errorRowList);
    	  
    	recordType="Success";
    	  systemPath = getSystemPathTowriteFile(repositoryPath, 
      			tableName, recordType);
    	  writeLookUpDataFileStorage( successTableRowList , systemPath, fileName , tableId, uploadSummery,recordType , columnHeader,null);
    	
    	uploadSummery.put( "TotalRecord" , Integer.toString( totalRecordsInFile ) );
		uploadSummery.put( "DuplicateRecord" , Integer.toString( duplicateTableRowList.size() ) );
		uploadSummery.put( "ValidRecord" , Integer.toString( totalRecordsInFile - duplicateTableRowList.size() - (errorTableRowList.size() + errorRowList.size())) );
		uploadSummery.put( "ErrorRecord" , Integer.toString( errorTableRowList.size() + errorRowList.size() ) );
		
		if(autoJobStatistic!=null){
			int failedRecordCount = autoJobStatistic.getFailedRecordCount();
			int duplicateRecordCount = autoJobStatistic.getDuplicateRecordCount();
			int successRecordCount = autoJobStatistic.getSuccessRecordCount();
			autoJobStatistic.setFailedRecordCount(failedRecordCount+errorTableRowList.size());
			autoJobStatistic.setDuplicateRecordCount(duplicateRecordCount+duplicateTableRowList.size());
			autoJobStatistic.setSuccessRecordCount( successRecordCount+(totalRecordsInFile - duplicateTableRowList.size() - errorTableRowList.size()));
		}
		responseObject.setArgs(new Object[]{JSONValue.toJSONString(uploadSummery)});
		
		duplicateTableRowList.clear();
		errorTableRowList.clear();
		successTableRowList.clear();
		tableRowList.clear();
		
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_DATA_INSERT_SUCCESS);
		
		return  responseObject;
	}
	
	protected ResponseObject overwriteLookUpData( List<Map<String,String>>  tableRowList ,String tableId , Map<String,String> uploadSummery , 
			int staffId , String tableName , int totalRecordsInFile , String fileName , ResponseObject responseObject , String columnHeader, AutoJobStatistic autoJobStatistic, String repositoryPath, Set<String> uniqueHeaderSet, List<String> errorRowList) {		
		
		List<Map<String,String>> duplicateTableRowList = new ArrayList<>();
		List<Map<String,String>> errorTableRowList = new ArrayList<>();
		List<Map<String,String>> successTableRowList = new ArrayList<>();
		
    	genericLookupDao.deleteViewDataSql(tableName );
    	
    	String recordType ;
    		try{
    			 genericLookupDao.autoCreateRuleLookupRecord( tableName , tableRowList,duplicateTableRowList,errorTableRowList, successTableRowList,
    					staffId, Integer.parseInt(tableId), uniqueHeaderSet);    			
    		}catch(Exception e){
    			logger.trace("Lookup data not inserted = ",e);
    		}		    		
    	
    	String systemPath = "";
	   	 if(autoJobStatistic==null){
	   		repositoryPath = MapCache.getConfigValueAsObject(SystemParametersConstant.LOOKUP_DUPLICATE_DATA_PATHDETAIL).toString();
	   	 }
	   	 
	   	recordType = "Duplicate"; 
	   	systemPath = getSystemPathTowriteFile( repositoryPath, tableName, recordType);
	   	writeLookUpDataFileStorage( duplicateTableRowList , systemPath, fileName , tableId, uploadSummery,recordType , columnHeader,errorRowList );
	   	 
    	recordType="Error";
    	systemPath = getSystemPathTowriteFile(repositoryPath, 
    			tableName, recordType);
    	/*errorTableRowList.addAll(duplicateTableRowList);*/
    	writeLookUpDataFileStorage( errorTableRowList , systemPath, fileName , tableId, uploadSummery,recordType , columnHeader,errorRowList);
    	  
    	recordType="Success";
    	systemPath = getSystemPathTowriteFile(repositoryPath, 
      			tableName, recordType);
    	writeLookUpDataFileStorage( successTableRowList , systemPath, fileName , tableId, uploadSummery,recordType , columnHeader,errorRowList);
    	
    	uploadSummery.put( "TotalRecord" , Integer.toString( totalRecordsInFile ) );
		uploadSummery.put( "DuplicateRecord" , Integer.toString( duplicateTableRowList.size() ) );
		uploadSummery.put( "ValidRecord" , Integer.toString( totalRecordsInFile - duplicateTableRowList.size() - errorTableRowList.size() - errorRowList.size()) );
		uploadSummery.put( "ErrorRecord" , Integer.toString( errorTableRowList.size() + errorRowList.size() ) );
		
		if(autoJobStatistic!=null){
			int failedRecordCount = autoJobStatistic.getFailedRecordCount();
			int duplicateRecordCount = autoJobStatistic.getDuplicateRecordCount();
			int successRecordCount = autoJobStatistic.getSuccessRecordCount();
			autoJobStatistic.setFailedRecordCount(failedRecordCount+errorTableRowList.size());
			autoJobStatistic.setDuplicateRecordCount(duplicateRecordCount+duplicateTableRowList.size());
			autoJobStatistic.setSuccessRecordCount( successRecordCount+(totalRecordsInFile - duplicateTableRowList.size() - errorTableRowList.size()));
		}
		
		responseObject.setArgs(new Object[]{JSONValue.toJSONString(uploadSummery)});
		
		duplicateTableRowList.clear();
		errorTableRowList.clear();
		successTableRowList.clear();
		tableRowList.clear();
		
		responseObject.setSuccess(true);			
		responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_DATA_INSERT_SUCCESS);
		
		return responseObject;
	}
	
	protected ResponseObject updateLookUpData( List<Map<String,String>>  tableRowList ,String tableId , Map<String,String> uploadSummery , 
			int staffId , String tableName , int totalRecordsInFile , String fileName , ResponseObject responseObject ,Set<String> uniqueHeaderSet,String columnHeader, AutoJobStatistic autoJobStatistic, String repositoryPath, List<String> errorRowList) {
		 	
		    List<Map<String,String>> duplicateTableRowList = new ArrayList<>();
		  	List<Map<String,String>> errorTableRowList = new ArrayList<>();
		  	List<Map<String,String>> successTableRowList = new ArrayList<>();
	    	
	    	Map<String,String> uniqueDataSet = null;
	    	
	    	String recordType ;
	    	  	
	    	/** for( Map<String,String> tableViewRow : tableRowList ){ **/		    		
	    		try{
	    			 genericLookupDao.autoCreateRuleLookupRecord( tableName , tableRowList,duplicateTableRowList,errorTableRowList, successTableRowList,
	    					staffId, Integer.parseInt(tableId), uniqueHeaderSet);		    			
	    		}catch(Exception e){
	    			logger.trace("Lookup data not inserted : ",e);
	    		}	 
	    	/**	 	
	    		 uniqueDataSet = new HashMap<>();	    				
	    		 for(String ufield :  uniqueHeaderSet){
	    					uniqueDataSet.put( ufield ,  tableViewRow.get(ufield));
				        }	    				
				  **/      
	    			/** genericLookupDao.updateLookupRecordsInView( tableName , tableViewRow , uniqueDataSet , staffId );  **/
	    		 genericLookupDao.updateLookupRecordsInView( tableName , duplicateTableRowList,errorTableRowList, successTableRowList, uniqueHeaderSet ,
	    				 staffId );
	    		     			    		
	    	/** } **/
	    	
	    	String systemPath ="";
	    	if(autoJobStatistic==null){
		   		repositoryPath = MapCache.getConfigValueAsObject(SystemParametersConstant.LOOKUP_DUPLICATE_DATA_PATHDETAIL).toString();
		   	 }
	    	recordType="Error";
	    	systemPath = getSystemPathTowriteFile(repositoryPath, 
	    			tableName, recordType);
	    	writeLookUpDataFileStorage( errorTableRowList , systemPath, fileName , tableId, uploadSummery , recordType , columnHeader,errorRowList);
	    	  
	    	recordType="Success";
	    	  systemPath = getSystemPathTowriteFile(repositoryPath, 
	      			tableName, recordType);
	    	writeLookUpDataFileStorage( successTableRowList , systemPath, fileName , tableId, uploadSummery ,recordType , columnHeader,errorRowList);
	    	
	    	uploadSummery.put( "TotalRecord" , Integer.toString( totalRecordsInFile ) );
			uploadSummery.put( "ErrorRecord" , Integer.toString( errorTableRowList.size() + errorRowList.size() ) );
			uploadSummery.put( "ValidRecord" , Integer.toString( totalRecordsInFile - errorTableRowList.size() - errorRowList.size()) );
			
			if(autoJobStatistic!=null){
				int failedRecordCount = autoJobStatistic.getFailedRecordCount();
				int duplicateRecordCount = autoJobStatistic.getDuplicateRecordCount();
				int successRecordCount = autoJobStatistic.getSuccessRecordCount();
				autoJobStatistic.setFailedRecordCount(failedRecordCount+errorTableRowList.size());
				autoJobStatistic.setDuplicateRecordCount(duplicateRecordCount+duplicateTableRowList.size());
				autoJobStatistic.setSuccessRecordCount( successRecordCount+(totalRecordsInFile - duplicateTableRowList.size() - errorTableRowList.size()));
			}
			
			responseObject.setArgs(new Object[]{JSONValue.toJSONString(uploadSummery)});			
			
			duplicateTableRowList.clear();
			errorTableRowList.clear();
			successTableRowList.clear();
			tableRowList.clear();
			
			responseObject.setSuccess(true);			
			responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_DATA_INSERT_SUCCESS);
		
		return responseObject;
	}
	
	private Map<String,String> writeLookUpDataFileStorage( List< Map<String,String>>  tableRowList , String systemPath ,String fileName ,
			String tableId , Map<String,String> uploadSummery , String recordType , String columnHeader, List<String> errorRowList ) {
		String cvsSplitBy = ",";
		
		String[] fileHeader = columnHeader.split(cvsSplitBy);  // getCsvHeader(Integer.parseInt(tableId)).split(cvsSplitBy);
		
		try{
			if(!tableRowList.isEmpty() || (errorRowList != null && !errorRowList.isEmpty()))
			{		
				fileName = fileName.split(".csv")[0];
				fileName = fileName +"_" + new Date().getTime() + ".csv";
				File duplicateDataFile = new File( systemPath + fileName );
				if(!duplicateDataFile.exists()){
					if(!duplicateDataFile.getParentFile().exists()){
						duplicateDataFile.getParentFile().mkdirs();
					}
					duplicateDataFile.createNewFile();
				}
				
				BufferedWriter outw = new BufferedWriter( new FileWriter( duplicateDataFile )); //NOSONAR
				logger.info("duplicate table data written in file : " + systemPath + fileName );
				 
				outw.append( columnHeader  ); // getCsvHeader( Integer.parseInt(tableId) )
				outw.append( "\n" );
				
				int dataSize = 0;
				int  commaChecker = 0;				
				for( Map<String,String> tableViewRow : tableRowList ){
					   dataSize = tableViewRow.size();
					   commaChecker = 0;
					 StringBuffer sb = new StringBuffer();
					 for(String colmn  : fileHeader ){
						 ++commaChecker;
						if (tableViewRow.get(colmn) != null && !tableViewRow.get(colmn).isEmpty() && tableViewRow.get(colmn).contains(","))
						{
						 sb.append('"').append(tableViewRow.get(colmn)).append('"');
						 tableViewRow.put(colmn,sb.toString());
						 sb.setLength(0);
						} 
						 outw.append((tableViewRow.get(colmn) != null) ? tableViewRow.get(colmn) : "");
						 if( dataSize > commaChecker )								
							 outw.append(",");
					 }
					 outw.append( "\n" );
				 }	
				if("Duplicate".equalsIgnoreCase(recordType)){
					uploadSummery.put("duplicateRecordFileName", systemPath + fileName );
				}
				
				if("Error".equalsIgnoreCase(recordType)){
					if(errorRowList != null && !errorRowList.isEmpty()) {
						for(String row : errorRowList)
							outw.append(row+"\n");
					}
					uploadSummery.put("errorRecordFileName", systemPath + fileName );
				}
				
				outw.close();
			}
		}catch(Exception exio){
			logger.trace("Problem occurred while writing file : ",exio);
		}
		
		return uploadSummery;
	}
	
	
	protected String getSystemPathTowriteFile(String rootPath , String tableName , String dirStructure ){		
		return rootPath + File.separator + tableName + File.separator +  dirStructure + File.separator
    			+ new SimpleDateFormat("YYYYMMdd").format( new Date() ) + File.separator;	
	}
	
	@Override
	@Transactional
	public RuleLookupTableData getLookUpTableData(String tableId){
		return ruleLookUpTableDao.findByPrimaryKey(RuleLookupTableData.class, Integer.parseInt(tableId));
	}

	@Override
	@Transactional
	public ResponseObject createAutoReloadCache(AutoReloadJobDetail autoReloadCache, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		autoReloadCache.setCreatedByStaffId(staffId);
		autoReloadCache.setLastUpdatedByStaffId(staffId);
		autoReloadCache.setCreatedDate(new Date());
		autoReloadCache.setLastUpdatedDate(new Date());
		boolean unique = true;
		if(autoReloadCache.getScheduleType().toString().equalsIgnoreCase(ScheduleTypeEnum.Immediate.toString())){
			String[] dbQueryList = autoReloadCache.getDatabaseQueryList().split(",");
			List<AutoReloadJobDetail> autoReloadCacheJobList = autoReloadCacheDao.getListOfImmediateExecutionByViewName(autoReloadCache.getRuleLookupTableData().getId());
			List<String> dbList = new ArrayList<String>();
			for(AutoReloadJobDetail autoReloadJob : autoReloadCacheJobList){
				String[] allDBQuery = autoReloadJob.getDatabaseQueryList().split(",");
				dbList.addAll(Arrays.asList(allDBQuery));
			}
			for(String dbQuery : dbQueryList){
				if(dbList.contains(dbQuery)) {
					unique = false;
					break;
				}	
			}
			autoReloadCache.setScheduler(null);
		}
		else if(autoReloadCache.getScheduleType().toString().equalsIgnoreCase(ScheduleTypeEnum.Schedule.toString())){
			CrestelSMJob job = autoReloadCache.getScheduler();
			job.setJobName("AUTORELOADCACHE_"+autoReloadCache.getRuleLookupTableData().getId()+"_job_"+UUID.randomUUID());
			job.setDescription("AUTORELOADCACHE_"+autoReloadCache.getRuleLookupTableData().getId()+"_job");
			job.setParentTrigger(BaseConstants.SCHEDULER_PARENT_TRIGGER_PREFIX+job.getTrigger().getID());
			job.setOriginalTrigger(BaseConstants.SCHEDULER_ORIGINAL_TRIGGER_PREFIX);
			job.setJobType(JobTypeEnum.AutoReloadCache.name());
			jobDao.save(job);
			job.setOriginalTrigger(BaseConstants.SCHEDULER_ORIGINAL_TRIGGER_PREFIX+job.getID());
			jobDao.update(job);
			autoReloadCache.setScheduler(job);
		}
		if(unique){
			autoReloadCacheDao.save(autoReloadCache);
		} else {
			responseObject.setResponseCode(ResponseCode.AUTO_RELOAD_IMMEDIATE_FAILURE);
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			return responseObject;
		}
		
		if(autoReloadCache.getId()>0){
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.AUTO_RELOAD_TABLE_ADD_SUCCESS);
			responseObject.setObject(autoReloadCache);
		}else{
			responseObject.setResponseCode(ResponseCode.AUTO_RELOAD_TABLE_ADD_FAILURE);
			responseObject.setSuccess(false);
			responseObject.setObject(null);
		}
		return responseObject;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getAutoReloadCacheCount(boolean isSearch, String searchName, String searchServerInstance,
			String searchDBQuery) {
		Map<String, Object> autoReloadConditions = autoReloadCacheDao.getAutoReloadConditionList();
		
		if(isSearch){
			autoReloadConditions = autoReloadCacheDao.getAutoReloadSearchConditionList(searchName, searchServerInstance, searchDBQuery);
		}else{
			autoReloadConditions = autoReloadCacheDao.getAutoReloadConditionList();			
		}
		
		return autoReloadCacheDao.getQueryCount(AutoReloadJobDetail.class, (List<Criterion>) autoReloadConditions.get("conditions"),
				(HashMap<String, String>) autoReloadConditions.get("aliases"));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AutoReloadJobDetail> getAutoReloadCachePaginatedList(int startIndex, int limit, String sidx,
			String sord,boolean isSearch, String searchName, String searchServerInstance,
			String searchDBQuery) {
		Map<String,Object> autoReloadConditionList ;
		if(isSearch)
			autoReloadConditionList = autoReloadCacheDao.getAutoReloadSearchConditionList(searchName, searchServerInstance, searchDBQuery);
		else
			autoReloadConditionList = autoReloadCacheDao.getAutoReloadConditionList();
		return autoReloadCacheDao.getPaginatedList(AutoReloadJobDetail.class, (List<Criterion>) autoReloadConditionList.get("conditions"),
				(HashMap<String, String>) autoReloadConditionList.get("aliases"), startIndex, limit, sidx, sord);
	}

	@Override
	@Transactional
	public ResponseObject updateAutoReloadCache(AutoReloadJobDetail autoReloadCache, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		autoReloadCache.setLastUpdatedByStaffId(staffId);
		autoReloadCache.setLastUpdatedDate(new Date());
		boolean unique = true;
		if(autoReloadCache.getScheduleType().toString().equalsIgnoreCase(ScheduleTypeEnum.Immediate.toString())){
			String[] dbQueryList = autoReloadCache.getDatabaseQueryList().split(",");
			List<AutoReloadJobDetail> autoReloadCacheJobList = autoReloadCacheDao.getListOfImmediateExecutionByViewName(autoReloadCache.getRuleLookupTableData().getId());
			List<String> dbList = new ArrayList<String>();
			for(AutoReloadJobDetail autoReloadJob : autoReloadCacheJobList){
				if(autoReloadJob.getId()==autoReloadCache.getId()){
					continue;
				}
				String[] allDBQuery = autoReloadJob.getDatabaseQueryList().split(",");
				dbList.addAll(Arrays.asList(allDBQuery));
			}
			for(String dbQuery : dbQueryList){
				if(dbList.contains(dbQuery)) {
					unique = false;
					break;
				}
			}
			autoReloadCache.setScheduler(null);
		}else if(autoReloadCache.getScheduleType().toString().equalsIgnoreCase(ScheduleTypeEnum.Schedule.toString())){
			CrestelSMJob job = autoReloadCache.getScheduler();
			autoReloadCache.setScheduler(job);
			jobService.merge(job);
		}
		if(unique){
			autoReloadCacheDao.merge(autoReloadCache);
		} else {
			responseObject.setResponseCode(ResponseCode.AUTO_RELOAD_IMMEDIATE_FAILURE);
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			return responseObject;
		}
		if(autoReloadCache.getId()>0){
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.AUTO_RELOAD_UPDATE_SUCESS);
			responseObject.setObject(autoReloadCache);
		}else{
			responseObject.setResponseCode(ResponseCode.AUTO_RELOAD_UPDATE_FAILURE);
			responseObject.setSuccess(false);
			responseObject.setObject(null);
		}
		return responseObject;
	}

	@Override
	@Transactional
	public Integer isJobAssociated(int id) {
		return autoReloadCacheDao.isJobAssociated(id);
	}

	@Override
	@Transactional
	public CrestelSMJob getJobByJobDetailId(int id) {
		 AutoReloadJobDetail autoReloadJob = autoReloadCacheDao.getAutoReloadJobDetailById(id);
		 return autoReloadJob.getScheduler();
	}

	@Override
	@Transactional
	public ResponseObject deleteMultipleAutoReloadCache(String ids, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		
		if(!StringUtils.isEmpty(ids)){
			String [] idList = ids.split(",");
			
			for(int i = 0; i < idList.length; i ++ ){
				responseObject = deleteAutoReloadCache(Integer.parseInt(idList[i]), staffId);
			}
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.AUTO_RELOAD_DELETE_SUCESS);
			
		}else{
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.AUTO_RELOAD_DELETE_FAILURE);
		}
		
		return responseObject;
	}
	
	private ResponseObject deleteAutoReloadCache(int id, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		
		if(id > 0){
			try{
				AutoReloadJobDetail jobDetail = autoReloadCacheDao.getAutoReloadJobDetailById(id);
				CrestelSMJob job = jobDetail.getScheduler();
				if(jobDetail != null){
					if(job!=null){
						joblistener.deleteQuartzJob(job);
						job.setLastUpdatedByStaffId(staffId);
						job.setLastUpdatedDate(new Date());
						job.setStatus(StateEnum.DELETED);
						jobDao.merge(job);
					}
					jobDetail.setLastUpdatedByStaffId(staffId);
					jobDetail.setLastUpdatedDate(new Date());
					jobDetail.setStatus(StateEnum.DELETED);
					autoReloadCacheDao.merge(jobDetail);
					responseObject.setSuccess(true);
					responseObject.setObject(jobDetail);
					responseObject.setResponseCode(ResponseCode.AUTO_RELOAD_DELETE_SUCESS);
				}
			}catch(Exception e){
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.AUTO_RELOAD_DELETE_FAILURE);
				logger.error(e);
			}
			
		}else{
			responseObject.setSuccess(true);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.AUTO_RELOAD_DELETE_FAILURE);
		}
		
		return responseObject;
	}

	@Override
	@Transactional
	public CrestelSMTrigger getScheduler(int id) {
		CrestelSMTrigger trigger = null;
		AutoReloadJobDetail autoReloadJobDetail = autoReloadCacheDao.findByPrimaryKey(AutoReloadJobDetail.class, id);
		Hibernate.initialize(autoReloadJobDetail.getScheduler());
		CrestelSMJob job = autoReloadJobDetail.getScheduler();
		if(job!=null){
			job = jobDao.findByPrimaryKey(CrestelSMJob.class, job.getID());	
			Hibernate.initialize(job.getTrigger());
			trigger = job.getTrigger();
		}
		return trigger;
	}
	
	@Override
	@Transactional
	public ResponseObject getRuleLookUpDataTableNameList() {
		
		ResponseObject responseObject = new ResponseObject();
		List<RuleLookupTableData> ruleLookupTableDataList = ruleLookUpTableDao.getRuleLookUpTableList();
		if(ruleLookupTableDataList != null){
			responseObject.setObject(ruleLookupTableDataList);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_TABLE_GET_SUCCESS);
		}else{
			responseObject.setObject(null);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.RULE_LOOKUP_TABLE_GET_FAIL);
		}
		return responseObject;
	}
	
	@Override
	@Transactional
	public AutoJobStatistic saveAutoJobStatistic(Object object) {
		AutoJobStatistic autoJobStatistic = new AutoJobStatistic();
		RuleLookupTableData ruleLookupTableData = null;
		CrestelSMTrigger trigger = null;
		if(object instanceof AutoReloadJobDetail){
			AutoReloadJobDetail autoReloadcache = (AutoReloadJobDetail) object;
			ruleLookupTableData = autoReloadcache.getRuleLookupTableData();
			String dbQuery = autoReloadcache.getDatabaseQueryList();
			autoJobStatistic.setDbQuery(dbQuery);
			if(dbQuery!=null && !dbQuery.isEmpty()){
				String[] dbQueryArray = dbQuery.split(",");
				StringBuffer reloadRecordCount = new StringBuffer();
				StringBuffer reloadDBQueryStatus = new StringBuffer();
				for(String s:dbQueryArray){
					if (org.apache.commons.lang3.StringUtils.isNotBlank(reloadDBQueryStatus)) {
						reloadDBQueryStatus.append(",");
					}
					reloadDBQueryStatus.append("Failed");
					if (org.apache.commons.lang3.StringUtils.isNotBlank(reloadRecordCount)) {
						reloadRecordCount.append(",");
					}
					reloadRecordCount.append("0");
				}
				autoJobStatistic.setReloadDbQueryStatus(reloadDBQueryStatus.toString());
				autoJobStatistic.setReloadRecordCount(reloadRecordCount.toString());
				
			}
			autoJobStatistic.setServerInstance(autoReloadcache.getServerInstance().getName());
			trigger = autoReloadcache.getScheduler().getTrigger();
			autoJobStatistic.setSchedulerType(BaseConstants.SCHEDULED);
			autoJobStatistic.setProcessType(autoReloadcache.getScheduler().getJobType());
			autoJobStatistic.setJobId(autoReloadcache.getScheduler().getID());
		} else if (object instanceof AutoUploadJobDetail){
			AutoUploadJobDetail autoUploadJobDetail = (AutoUploadJobDetail) object;
			ruleLookupTableData = autoUploadJobDetail.getRuleLookupTableData();
			trigger = autoUploadJobDetail.getScheduler().getTrigger();
			autoJobStatistic.setProcessType(autoUploadJobDetail.getScheduler().getJobType());
			autoJobStatistic.setAction(autoUploadJobDetail.getAction());
			autoJobStatistic.setSourceDirectory(autoUploadJobDetail.getSourceDirectory());
			autoJobStatistic.setSchedulerType(trigger.getRecurrenceType());
			autoJobStatistic.setJobId(autoUploadJobDetail.getScheduler().getID());
			autoJobStatistic.setFilePrefix(autoUploadJobDetail.getFilePrefix());
			autoJobStatistic.setFileContains(autoUploadJobDetail.getFileContains());
 		}
		if(ruleLookupTableData!=null)
			autoJobStatistic.setTableName(ruleLookupTableData.getViewName());
		if(trigger!=null)
			autoJobStatistic.setSchedulerName(trigger.getTriggerName());
		autoJobStatistic.setExecutionStart(new Timestamp(new Date().getTime()));
		autoJobStatistic.setJobStatus(BaseConstants.FAILED);
		autoJobStatisticDao.save(autoJobStatistic);
		return autoJobStatistic;
	}

	@Override
	@Transactional
	public AutoJobStatistic updateAutoJobStatistic(AutoJobStatistic autoJobStatistic) {
		autoJobStatistic.setExecutionEnd(new Timestamp(new Date().getTime()));
		autoJobStatisticDao.merge(autoJobStatistic);
		return autoJobStatistic;
	}
	
	private void writeRejectedFile(File repositoryFile,String systemPath ,String fileName  ) {		 
		try{			 		
				fileName = fileName.split(".csv")[0];
				fileName = fileName +"_" + new Date().getTime() + ".csv";
				File duplicateDataFile = new File( systemPath + fileName );
				if(!duplicateDataFile.exists()){
					if(!duplicateDataFile.getParentFile().exists()){
						duplicateDataFile.getParentFile().mkdirs();
					}
					duplicateDataFile.createNewFile();
				}
				
				BufferedWriter outw = new BufferedWriter( new FileWriter( duplicateDataFile ));		//NOSONAR	 	
				 
				String line ="";
				 try(BufferedReader br = new BufferedReader(new FileReader( repositoryFile ))){					 
					 while(line!=null){
					 line = br.readLine();
					 if(line!=null){
						 outw.append( line );
						 outw.append("\n");
					  }
					 }
					 br.close();
				 }catch(Exception e){
					 logger.trace("Problem occurred while writing file : ",e);
				 }	
				outw.close();
			 
		}catch(Exception exio){
			logger.trace("Problem occurred while writing file : ",exio);
		}
		
		 
	}
	
	@Transactional(readOnly = true)
	@Override
	public String getDatabaseEnv(){
		return genericLookupDao.getDatabaseEnv();
	}

	@Override
	@Transactional
	public void merge(CrestelSMJob job) {
		jobDao.merge(job);
	}
	
	@Override
	@Transactional
	public String getUniqueFieldsByTableId(int tableId){
		return lookupDataDao.getUniqueFieldsByTableId(tableId);
	}
}
