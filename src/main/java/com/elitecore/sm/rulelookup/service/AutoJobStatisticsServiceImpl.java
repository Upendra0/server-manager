package com.elitecore.sm.rulelookup.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.rulelookup.dao.IAutoJobStatisticsDao;
import com.elitecore.sm.rulelookup.model.AutoJobStatistic;
import com.elitecore.sm.rulelookup.model.AutoReloadJobDetail;
import com.elitecore.sm.rulelookup.model.SearchAutoUploadReloadDetail;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;



@Service(value="autoJobStatisticsServiceImpl")
public class AutoJobStatisticsServiceImpl implements IAutoJobStatisticsService {

	@Autowired
	IAutoJobStatisticsDao autoJobStatisticsDao;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<String,Object> getAutoJobProcessCount(boolean isSearch, SearchAutoUploadReloadDetail searchAutoUploadReloadDetail) {
		Map<String, Object> autoJobProcessMap = autoJobStatisticsDao.getAutoJobStatisticsConditionList(isSearch, searchAutoUploadReloadDetail);
		long count = autoJobStatisticsDao.getQueryCount(AutoJobStatistic.class, (List<Criterion>) autoJobProcessMap.get("conditions"), (Map<String,String>)autoJobProcessMap.get("aliases"));
		autoJobProcessMap.put("count", count);
		return autoJobProcessMap;
	}

	@Override
	@Transactional
	public List<AutoJobStatistic> getAutoJobProcessPaginatedList(Integer startIndex, int limit, String sortColumn, String sortOrder, boolean isSearch, SearchAutoUploadReloadDetail searchAutoUploadReloadDetail,List<Criterion> criterions) {
		return autoJobStatisticsDao.getPaginatedList(AutoJobStatistic.class, criterions, null, startIndex, limit, sortColumn, sortOrder);
	}

}
