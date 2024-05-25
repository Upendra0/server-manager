package com.elitecore.sm.rulelookup.dao;

import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.rulelookup.model.AutoJobStatistic;
import com.elitecore.sm.rulelookup.model.SearchAutoUploadReloadDetail;

public interface IAutoJobStatisticsDao extends GenericDAO<AutoJobStatistic> {

	public Map<String, Object> getAutoJobStatisticsConditionList(boolean isSearch, SearchAutoUploadReloadDetail searchAutoUploadReloadDetail);
}
