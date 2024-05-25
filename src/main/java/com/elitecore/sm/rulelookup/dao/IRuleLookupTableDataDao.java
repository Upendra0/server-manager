package com.elitecore.sm.rulelookup.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.rulelookup.model.RuleLookupTableData;

public interface IRuleLookupTableDataDao extends GenericDAO<RuleLookupTableData>{
	
	public int getCountByName(String name);
	
	public Map<String,Object> getRuleTableConditionList();

	public Map<String, Object> getRuleForSearchTableConditionList(String searchName, String searchDesc);

	public List<RuleLookupTableData> getRuleLookUpTableList();
	
	public int getRuleLookTableIdByViewName(String viewName);
	
}
