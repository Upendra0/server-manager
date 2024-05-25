package com.elitecore.sm.dictionarymanager.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.configmanager.model.VersionConfig;
import com.elitecore.sm.dictionarymanager.model.DictionaryConfig;
import com.elitecore.sm.server.model.Server;

@Repository(value = "dictionaryConfigDao")
public class DictionaryConfigDaoImpl extends GenericDAOImpl<DictionaryConfig> implements DictionaryConfigDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<DictionaryConfig> getDefaultDictionaryConfigObj(){
		Criteria criteria=getCurrentSession().createCriteria(DictionaryConfig.class);
		criteria.add(Restrictions.eq("isDefault", true));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		return criteria.list();
	}
	
	@Override
	public int getDictionaryConfigCount(String ipAddress,int utilityPort) {
		Criteria criteria = getCurrentSession().createCriteria(Server.class);
		criteria.add(Restrictions.eq("ipAddress", ipAddress));
		criteria.add(Restrictions.eq("utilityPort", utilityPort));
		@SuppressWarnings("unchecked")
		List<VersionConfig> resultList=criteria.list();
		return resultList.size();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DictionaryConfig> getDictionaryConfigList(String ipAddress,int utilityPort) {
		
		List<DictionaryConfig> resultList;
		Criteria criteria = getCurrentSession().createCriteria(DictionaryConfig.class);
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		criteria.add(Restrictions.eq("ipAddress", ipAddress));
		criteria.add(Restrictions.eq("utilityPort", utilityPort));
		resultList = criteria.list();
		return resultList;
	}
	
	@Override
	public Map<String, Object> getRuleForSearchTableConditionList(String ipAddress, int utilityPort) {
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		if(StringUtils.isNotBlank(ipAddress)){
			conditionList.add(Restrictions.eq("ipAddress",ipAddress).ignoreCase());
		}
		conditionList.add(Restrictions.eq("utilityPort",utilityPort));
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		return returnMap;
	}
	
	
	@Override
	public DictionaryConfig getDictionaryConfigObj(int id){
		Criteria criteria = getCurrentSession().createCriteria(DictionaryConfig.class);
		criteria.add(Restrictions.eq("id",id));
		return (DictionaryConfig) criteria.uniqueResult();
	}

	@Override
	public List<DictionaryConfig> getCustomDictionaryConfigList(String ipAddress, int utilityPort) {
		List<DictionaryConfig> dictionaryConfigs;
		Criteria criteria = getCurrentSession().createCriteria(DictionaryConfig.class);
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		criteria.add(Restrictions.eq("ipAddress", ipAddress));
		criteria.add(Restrictions.eq("utilityPort", utilityPort));
		criteria.add(Restrictions.ilike("path", "/dictionary/custom-plugins", MatchMode.START));
		dictionaryConfigs = criteria.list();
		return dictionaryConfigs;
	}
}
