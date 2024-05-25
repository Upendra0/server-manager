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
import com.elitecore.sm.rulelookup.model.AutoUploadJobDetail;
import com.elitecore.sm.rulelookup.model.RuleLookupTableData;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;


@Repository(value="autoUploadConfigurationDao")
public class AutoUploadConfigurationDaoImpl extends GenericDAOImpl<AutoUploadJobDetail> implements IAutoUploadConfigurationDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<AutoUploadJobDetail> getListOfAutoUploadConfig() {
		
		Criteria criteria=getCurrentSession().createCriteria(AutoUploadJobDetail.class);
		
		 
		/** criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE)); **/
		 
		
		return (List<AutoUploadJobDetail>)criteria.list();
	}
	
	@Override
	public Map<String,Object> getAutoUploadConditionList(){
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		return returnMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public AutoUploadJobDetail getJobDetailById(int id) {
		AutoUploadJobDetail jobDetail = null;
		Criteria criteria = getCurrentSession().createCriteria(AutoUploadJobDetail.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq(BaseConstants.ID, id));
		List<AutoUploadJobDetail> list = criteria.list();
		if(list!=null && list.size()>0){
			jobDetail = list.get(0);
		}
		return jobDetail;
	}
	
	@Override 
	public Map<String, Object> getAutoUploadSearchConditionList(String searchSourceDir, String searchTableName,
			String searchScheduler) {
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		
		if(searchSourceDir !=null && !searchSourceDir.equals("-1") && StringUtils.isNotBlank(searchSourceDir)){
			/** conditionList.add(Restrictions.eq("sourceDirectory", searchSourceDir.trim()).ignoreCase()); **/
			conditionList.add(Restrictions.like("sourceDirectory", "%" + StringUtils.trim(searchSourceDir) + "%").ignoreCase());
		}
		if(searchTableName !=null && !searchTableName.equals("-1") && StringUtils.isNotBlank(searchTableName)){
			conditionList.add(Restrictions.eq("ruleLookupTableData.id", Integer.parseInt(StringUtils.trim(searchTableName))));
		}
		if(searchScheduler !=null && !searchScheduler.equals("-1") && StringUtils.isNotBlank(searchScheduler)){
			aliases.put("scheduler", "jet");
			aliases.put("jet.trigger", "ttriggerId");		
			conditionList.add(Restrictions.eq("ttriggerId.ID", Integer.parseInt(searchScheduler)));
		}
		
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		

		
		return returnMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public AutoUploadJobDetail getAutoUploadJobDetailByJobId(int id) {
		AutoUploadJobDetail jobDetail = null;
		Criteria criteria = getCurrentSession().createCriteria(AutoUploadJobDetail.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("scheduler.ID", id));
		List<AutoUploadJobDetail> list = criteria.list();
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
	
}
