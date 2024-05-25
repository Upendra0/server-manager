package com.elitecore.sm.rulelookup.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.rulelookup.model.AutoJobStatistic;
import com.elitecore.sm.rulelookup.model.SearchAutoUploadReloadDetail;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.MapCache;

@Repository
public class AutoJobStatisticsDaoImpl extends GenericDAOImpl<AutoJobStatistic> implements IAutoJobStatisticsDao{

	@SuppressWarnings("deprecation")
	@Override
	public Map<String, Object> getAutoJobStatisticsConditionList(boolean isSearch, SearchAutoUploadReloadDetail searchAutoUploadReloadDetail) {
		Map<String, Object> returnMap = new HashMap<String,Object>();
		HashMap<String, String> aliases = new HashMap<String,String>();
		List<Criterion> conditionList = new ArrayList<Criterion>();
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		conditionList.add(Restrictions.eq("processType", searchAutoUploadReloadDetail.getProcess()));
		if(isSearch){
			if(searchAutoUploadReloadDetail.getBatchId() != null && searchAutoUploadReloadDetail.getBatchId() != 0)
				conditionList.add(Restrictions.eq("id", searchAutoUploadReloadDetail.getBatchId()));
			if(!searchAutoUploadReloadDetail.getViewName().equals("-1"))//NOSONAR
				conditionList.add(Restrictions.eq("tableName", searchAutoUploadReloadDetail.getViewName()).ignoreCase());
			if(!searchAutoUploadReloadDetail.getStatus().equals("-1"))
				conditionList.add(Restrictions.eq("jobStatus", searchAutoUploadReloadDetail.getStatus()).ignoreCase());
			if(!searchAutoUploadReloadDetail.getScheduleType().equals("-1"))
				conditionList.add(Restrictions.eq("schedulerType", searchAutoUploadReloadDetail.getScheduleType()).ignoreCase());
			if(!searchAutoUploadReloadDetail.getScheduler().equals("-1"))
				conditionList.add(Restrictions.eq("schedulerName", searchAutoUploadReloadDetail.getScheduler()).ignoreCase());
			if(!searchAutoUploadReloadDetail.getAction().equals("-1"))
				conditionList.add(Restrictions.eq("action", searchAutoUploadReloadDetail.getAction()).ignoreCase());
			if(!searchAutoUploadReloadDetail.getSourceDirectory().isEmpty())
				conditionList.add(Restrictions.like("sourceDirectory", "%"+searchAutoUploadReloadDetail.getSourceDirectory()+"%").ignoreCase());
			if(!StringUtils.isEmpty(searchAutoUploadReloadDetail.getFromDate()) ){
				Date startDate = DateFormatter.formatDate(searchAutoUploadReloadDetail.getFromDate(), MapCache.getConfigValueAsString(SystemParametersConstant.DATE_FORMAT,"dd-MM-yyyy HH:mm:ss"));
				if(startDate != null){
					LogicalExpression orExp = Restrictions.or(Restrictions.ge("executionStart", startDate), Restrictions.ge("executionStart", startDate));
					conditionList.add(orExp);
				}
			}
			if(!StringUtils.isEmpty(searchAutoUploadReloadDetail.getToDate()) ){
				Date endDate = DateFormatter.formatDate(searchAutoUploadReloadDetail.getToDate(), MapCache.getConfigValueAsString(SystemParametersConstant.DATE_FORMAT,"dd-MM-yyyy HH:mm:ss"));
				if(endDate != null){
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(endDate);
					calendar.set(Calendar.HOUR_OF_DAY, 23);
					calendar.set(Calendar.MINUTE, 59);
					calendar.set(Calendar.SECOND, 59);
					LogicalExpression orExp = Restrictions.or(Restrictions.le("executionEnd", calendar.getTime()), Restrictions.le("executionEnd", calendar.getTime()));
					conditionList.add(orExp);
				}
			}
		}
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		return returnMap;
	}

}
