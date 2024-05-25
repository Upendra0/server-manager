package com.elitecore.sm.rulelookup.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.rulelookup.model.LookupFieldDetailData;

public interface ILookupFieldDetailDataDao extends GenericDAO<LookupFieldDetailData>{

	Map<String, Object> getFieldListConditionList(int tableId);

	List<LookupFieldDetailData> getSortedLookUpFieldDetailData(int tableId);

}
