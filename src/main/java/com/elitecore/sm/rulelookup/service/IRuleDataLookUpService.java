package com.elitecore.sm.rulelookup.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.rulelookup.model.AutoJobStatistic;
import com.elitecore.sm.rulelookup.model.AutoReloadJobDetail;
import com.elitecore.sm.rulelookup.model.LookupFieldDetailData;
import com.elitecore.sm.rulelookup.model.RuleLookupData;
import com.elitecore.sm.rulelookup.model.RuleLookupTableData;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;

public interface IRuleDataLookUpService {
	
	public ResponseObject createRuleLookUpTable(RuleLookupTableData ruleLookupTableData, int staffId);
	
	public long getAllTableListCount(boolean isSearch, String searchName, String searchDesc);

	public List<RuleLookupTableData> getPaginatedList(int startIndex, int limit, String sidx,
			String sord, String searchName, String searchDesc, boolean isSearch);
	
	public long getFieldListCountByTableId(int tableId);

	public List<LookupFieldDetailData> getFieldListPaginatedList(int tableId, int startIndex, int limit, String sidx,
			String sord);

	public ResponseObject deleteLookupTable(int tableId, int staffId);
	
	public ResponseObject updateRuleLookupTable(RuleLookupTableData ruleLookupTableData, int staffId);

	boolean isUniqueForUpdate(String viewName, String dbName);

	public String getCsvHeader(int tableId);

	/** public ResponseObject insertDataIntoLookupTable(File lookupDataFile, String repositoryPath, String tableId); **/
	public ResponseObject insertDataIntoLookupTable(File lookupDataFile, String repositoryPath, String tableId , String mode);

	void insertIntoProperStr(RuleLookupData ruleLookupData, int count, String token);

	boolean checkUniqueForInsert(int count, List<Set<String>> uniqueSet, String tableId, String token);

	public List<List<String>> getLookupTableData(int parseInt);

	void clearLookupDataAfterUpdate(List<LookupFieldDetailData> lookUpFieldDetailData,List<RuleLookupData> ruleLookupDatas);
	
	public List<String> getLookupTableFields(int tableId);
	
	public long getRuleLookupDataListCountById(int tableId);

	public List<RuleLookupData> getRuleLookupDataPaginatedList(int startIndex, int limit, String sidx, String sord, int tableId);
	
	public ResponseObject deleteRuleLookupTableRecords(String recordsIds, String viewName);

	public ResponseObject deleteLookupRecords(int recordId, String viewName);

	/** Edited for search lookup **/
	public List<Map<String,Object>> getSearchLookupDataPaginatedList(int startIndex,
			int limit, String sidx, String sord, int tableId , String tableviewname , String searchQuery ,ResponseObject responseObject );

	long getSearchLookupDataListCountById(String tableviewname, int tableId, String searchQuery);
	
	public ResponseObject deleteRuleLookupTables(String tableIds, int staffId);
	
	//public void doLookupTableDataReload(RuleLookupTableConfiguration configObject);
	
	public ResponseObject doLookupTableDataReload(AutoReloadJobDetail configObject,AutoJobStatistic autoJobStatistic);
	
	public ResponseObject createRuleLookupRecord(String viewName, Map<String, String> lookUpdata, int staffId, int tableId);
	
	public ResponseObject updateRuleLookupRecord(String viewName, Map<String, String> lookUpdata, int staffId);
	
	List<Map<String, Object>> getLookupViewData(int tableId,
			String tableviewname, String searchQuery,
			ResponseObject responseObject);
	
	public void reloadImmediateUploadDataInView(String viewName, Timestamp lookupDataReloadFromDate);
	
	public ResponseObject insertDataIntoLookupView(String header ,RuleLookupTableData LookupTableData ,File lookupDataFile, String repositoryPath, String tableId ,
			String mode ,int staffId,AutoJobStatistic autoJobStatistic);

	public RuleLookupTableData getLookUpTableData(String tableId);

	public ResponseObject createAutoReloadCache(AutoReloadJobDetail autoReloadCache,int staffId);
	
	public ResponseObject updateAutoReloadCache(AutoReloadJobDetail autoReloadCache,int staffId);
	
	public ResponseObject deleteMultipleAutoReloadCache(String ids, int staffId); 

	public long getAutoReloadCacheCount(boolean isSearch, String searchName, String searchServerInstance,
			String searchDBQuery);

	public List<AutoReloadJobDetail> getAutoReloadCachePaginatedList(int startIndex, int limit, String sidx,
			String sord,boolean isSearch, String searchName, String searchServerInstance,
			String searchDBQuery);

	public Integer isJobAssociated(int id);
	
	public CrestelSMJob getJobByJobDetailId(int id);
	
	public CrestelSMTrigger getScheduler(int id);
	
	public ResponseObject getRuleLookUpDataTableNameList();
	
	public AutoJobStatistic saveAutoJobStatistic(Object object);
	
	public AutoJobStatistic updateAutoJobStatistic(AutoJobStatistic autoJobStatistic);
	
	public String getDatabaseEnv();
	
	public void merge(CrestelSMJob job);
	
	public String getUniqueFieldsByTableId(int tableId);
}
