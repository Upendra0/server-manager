package com.elitecore.sm.mis.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.mis.model.DynamicReportData;

/**
 * 
 * @author chetan.kaila
 *
 */

@Repository(value="dynamicReportDataDao")
public class DynamicReportDataDaoImpl extends GenericDAOImpl<DynamicReportData> implements IDynamicReportDataDao{
	
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public Map<String, Object> getRuleForSearchTableConditionList(String reportName, String reportDesc) {
		
		logger.debug("getRuleForSearchTableConditionList, reportName :"+reportName+"reportDesc :"+reportDesc);
		
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		if(StringUtils.isNotBlank(reportName)){
			conditionList.add(Restrictions.like("reportName", "%" + StringUtils.trim(reportName) + "%").ignoreCase());
		}
		if(StringUtils.isNotBlank(reportDesc)){
			conditionList.add(Restrictions.like("description", "%" + StringUtils.trim(reportDesc) + "%").ignoreCase());
		}
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		return returnMap;
	}
	
	@Override
	public DynamicReportData getDynamicReportDataByReportName(String reportName) {
		
		logger.debug("getDynamicReportDataByReportName, reportName :"+reportName);
		
		Criteria criteria = getCurrentSession().createCriteria(DynamicReportData.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		criteria.add(Restrictions.like("reportName", "%" + StringUtils.trim(reportName) + "%").ignoreCase());

		List<DynamicReportData> dynamicReportDataList = criteria.list();
		return (dynamicReportDataList != null && !dynamicReportDataList.isEmpty()) ? dynamicReportDataList.get(0) : null;
	}
	
}
