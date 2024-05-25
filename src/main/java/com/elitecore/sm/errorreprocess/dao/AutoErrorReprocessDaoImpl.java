package com.elitecore.sm.errorreprocess.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.errorreprocess.model.AutoErrorReprocessDetail;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.rulelookup.model.AutoReloadJobDetail;
import com.elitecore.sm.rulelookup.model.RuleLookupTableData;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;

@Repository(value = "autoErrorReprocessDao")
public class AutoErrorReprocessDaoImpl extends GenericDAOImpl<AutoErrorReprocessDetail> implements AutoErrorReprocessDao {

	@Override
	public Map<String, Object> getProcessingServiceDetailsByRule() {
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		return returnMap;
	}
	
	@Override
	public Map<String, Object> getProcessingServiceDetailsBySearchRule(String serviceInstanceIds,
			String category, String severity, String reasonCategory, String rule, String errorCode) {
		
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		String[] serviceInstancesIdsArr = serviceInstanceIds.split(",");
		Integer[] intArr = new Integer[serviceInstancesIdsArr.length];
		for(Integer i = 0;i<serviceInstancesIdsArr.length;i++){
			intArr[i] = Integer.parseInt(serviceInstancesIdsArr[i].trim());
		}
		conditionList.add(Restrictions.in("service.id", intArr));
		conditionList.add(Restrictions.eq("category", category));
		if(!severity.equals("-1"))
			conditionList.add(Restrictions.eq("severity", severity));
		conditionList.add(Restrictions.eq("reasoncategory", reasonCategory));
		conditionList.add(Restrictions.eq("rule", rule));
		if(!errorCode.isEmpty())
			conditionList.add(Restrictions.eq("errorCode", errorCode));
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		return returnMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AutoErrorReprocessDetail> getAllList() {
		logger.info(">> getAllTriggers ");
		Criteria criteria=getCurrentSession().createCriteria(AutoErrorReprocessDetail.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.addOrder(Order.asc("id"));
		logger.info("<< getAllTriggers ");
		return criteria.list();
	}
	
	@Override
	public AutoErrorReprocessDetail getErrorReprocessJobById(int jobId) {
		
		logger.debug("getErrorReprocessJobById, jobId :"+jobId);
		AutoErrorReprocessDetail jobDetail = null;
		Criteria criteria = getCurrentSession().createCriteria(AutoErrorReprocessDetail.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("job.id", jobId));

		List<AutoErrorReprocessDetail> list = criteria.list();
		if(list!=null && list.size()>0){
			jobDetail = list.get(0);
			Hibernate.initialize(jobDetail.getJob().getTrigger());
			CrestelSMJob job = jobDetail.getJob();
			Hibernate.initialize(job.getTrigger());
			CrestelSMTrigger trigger = job.getTrigger();
			job.setTrigger(trigger);
			jobDetail.setJob(job);
		}		
		return jobDetail;
	}

}
