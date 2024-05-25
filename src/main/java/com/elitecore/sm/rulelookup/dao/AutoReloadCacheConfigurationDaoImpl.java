package com.elitecore.sm.rulelookup.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.rulelookup.model.AutoReloadJobDetail;
import com.elitecore.sm.rulelookup.model.RuleLookupTableData;
import com.elitecore.sm.rulelookup.model.ScheduleTypeEnum;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;

@Repository
public class AutoReloadCacheConfigurationDaoImpl extends GenericDAOImpl<AutoReloadJobDetail> implements IAutoReloadCacheConfigurationDao{

	@Override
	public Map<String,Object> getAutoReloadConditionList(){
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		return returnMap;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Integer isJobAssociated(int id) {
		Integer jobId = null;
		Criteria criteria = getCurrentSession().createCriteria(AutoReloadJobDetail.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		List<AutoReloadJobDetail> list = criteria.list();
		if(list!=null && list.size()>0){
			AutoReloadJobDetail autoReloadJobDetail = list.get(0);
			if(autoReloadJobDetail.getRuleLookupTableData()!=null){
				CrestelSMJob job = autoReloadJobDetail.getScheduler();
				if(job!=null){
					jobId = job.getID();
				}
			}
		}
		return jobId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AutoReloadJobDetail getAutoReloadJobDetailById(int id) {
		AutoReloadJobDetail jobDetail = null;
		Criteria criteria = getCurrentSession().createCriteria(AutoReloadJobDetail.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq(BaseConstants.ID, id));
		List<AutoReloadJobDetail> list = criteria.list();
		if(list!=null && list.size()>0){
			jobDetail = list.get(0);
			Hibernate.initialize(jobDetail.getScheduler());
			jobDetail.setScheduler(jobDetail.getScheduler());
		}
		return jobDetail;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public AutoReloadJobDetail getAutoReloadJobDetailByJobId(int id) {
		AutoReloadJobDetail jobDetail = null;
		Criteria criteria = getCurrentSession().createCriteria(AutoReloadJobDetail.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("scheduler.ID", id));
		List<AutoReloadJobDetail> list = criteria.list();
		if(list!=null && list.size()>0){
			jobDetail = list.get(0);
			Hibernate.initialize(jobDetail.getRuleLookupTableData());
			Hibernate.initialize(jobDetail.getScheduler().getTrigger());
			RuleLookupTableData ruleLookupTableData = jobDetail.getRuleLookupTableData();
			jobDetail.setRuleLookupTableData(ruleLookupTableData);
			CrestelSMJob job = jobDetail.getScheduler();
			Hibernate.initialize(job.getTrigger());
			CrestelSMTrigger trigger = job.getTrigger();
			job.setTrigger(trigger);
			jobDetail.setScheduler(job);
		}
		return jobDetail;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AutoReloadJobDetail> getListOfImmediateExecutionByViewName(int viewId) {
		Criteria criteria=getCurrentSession().createCriteria(AutoReloadJobDetail.class);
		
		criteria.add(Restrictions.eq("ruleLookupTableData.id", viewId));
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		criteria.add(Restrictions.eq(BaseConstants.SCHEDULER_TYPE,ScheduleTypeEnum.Immediate));
		
		return (List<AutoReloadJobDetail>)criteria.list();
	}

	@Override
	public Map<String, Object> getAutoReloadSearchConditionList(String searchName, String searchServerInstance,
			String searchDBQuery) {
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		if(!searchName.equals("-1") && StringUtils.isNotBlank(searchName)){
			conditionList.add(Restrictions.eq("ruleLookupTableData.id", Integer.parseInt(StringUtils.trim(searchName))));
		}
		if(!searchServerInstance.equals("-1") && StringUtils.isNotBlank(searchServerInstance)){
			conditionList.add(Restrictions.eq("serverInstance.id", Integer.parseInt(StringUtils.trim(searchServerInstance))));
		}
		if(!searchDBQuery.equals("-1") && StringUtils.isNotBlank(searchDBQuery)){
			conditionList.add(Restrictions.like("databaseQueryList", "%"+searchDBQuery.trim()+"%").ignoreCase());
		}
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		return returnMap;
	}

}
