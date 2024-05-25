package com.elitecore.sm.rulelookup.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.rulelookup.model.RuleLookupData;

public interface IRuleLookupDataDao extends GenericDAO<RuleLookupData>{

	public List<List<String>> getRuleLookupTableDataByTableId(int tableId);
	
	public List<String> getLookupFieldsByTableId(int tableID);
	
	public Map<String, Object> getRuleTableFieldConditionList(int id);

	List<Map<String, Object>> getListUsingSQL(String nativeQuery,
			Map<String, Object> conditions, int offset, int limit,
			ResponseObject responseObject);

	public String getUniqueFieldsByTableId(int tableId);
}
