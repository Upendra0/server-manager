package com.elitecore.sm.rulelookup.service;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.elitecore.sm.rulelookup.model.AutoJobStatistic;
import com.elitecore.sm.rulelookup.model.SearchAutoUploadReloadDetail;

public interface IAutoJobStatisticsService {

	public Map<String,Object> getAutoJobProcessCount(boolean isSearch, SearchAutoUploadReloadDetail searchAutoUploadReloadDetail);

	public List<AutoJobStatistic> getAutoJobProcessPaginatedList(Integer startIndex, int limit, String sortColumn, String sortOrder, boolean isSearch, SearchAutoUploadReloadDetail searchAutoUploadReloadDetail, List<Criterion> conditionList);
}
