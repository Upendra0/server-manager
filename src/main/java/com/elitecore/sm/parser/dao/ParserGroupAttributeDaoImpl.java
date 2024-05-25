package com.elitecore.sm.parser.dao;



import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.ParserGroupAttribute;
import com.elitecore.sm.parser.model.ParserPageConfiguration;
/**
 * @author ELITECOREADS\kiran.kagithapu
 *
 */
@Repository
public class ParserGroupAttributeDaoImpl extends GenericDAOImpl<ParserGroupAttribute> implements ParserGroupAttributeDao {
	
	@SuppressWarnings("unchecked")
	@Override
	public ParserGroupAttribute checkUniqueGroupAttributeNameForUpdate(int mappingId, String name) {
		logger.debug("Going fetch group attribute for name >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + name  + " and mapping id " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ParserGroupAttribute.class);
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		criteria.add(Restrictions.eq("name", name).ignoreCase());
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		List<ParserGroupAttribute> groupAttributeList = criteria.list();
		if(groupAttributeList != null && !groupAttributeList.isEmpty()){
			logger.info("Mapping found with name " + name);
			return groupAttributeList.get(0);
		}else{
			logger.info("No mapping found with name " + name);
			return null;
		}
	}

	@Override
	public ParserGroupAttribute getGroupAttributeByGroupId(int groupId, int mappingId) {
		logger.debug(">> getGroupAttributeListEligibleToAttachWithGroup : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ParserGroupAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		criteria.add(Restrictions.eq("id", groupId));
		return (ParserGroupAttribute)criteria.list().get(0);
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ParserGroupAttribute> getGroupAttributeListByGroupId(int groupId, int mappingId) {
		logger.debug(">> getAttributeListByGroupId : GroupId : " + groupId);
		Criteria criteria=getCurrentSession().createCriteria(ParserGroupAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		criteria.add(Restrictions.eq("baseGroup.id", groupId));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ParserPageConfiguration> getParserPageConfigurationByGroupId(int groupId) {
		Criteria criteria=getCurrentSession().createCriteria(ParserPageConfiguration.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("parserGroupAttribute.id", groupId));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ParserGroupAttribute> getGroupAttributeListEligibleToAttachWithGroupByMappingId(int mappingId) {
		logger.debug(">> getGroupAttributeListEligibleToAttachWithGroup : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ParserGroupAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		criteria.add(Restrictions.isNull("baseGroup.id"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ParserAttribute> getAttributeListEligibleToAttachWithGroup(int mappingId) {
		logger.debug(">> getAttributeListEligibleToAttachWithGroup : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ParserAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		criteria.add(Restrictions.isNull("parserGroupAttribute.id"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ParserGroupAttribute> getAttachedGroupAttributeListByGroupId(int groupId, int mappingId) {
		logger.debug(">> getAttachedGroupAttributeListByGroupId : GroupId : " + groupId);
		Criteria criteria=getCurrentSession().createCriteria(ParserGroupAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		criteria.add(Restrictions.eq("baseGroup.id", groupId));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ParserAttribute> getAttachedAttributeListByGroupId(int groupId, int mappingId) {
		logger.debug(">> getAttachedAttributeListByGroupId : GroupId : " + groupId);
		Criteria criteria=getCurrentSession().createCriteria(ParserAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		criteria.add(Restrictions.eq("parserGroupAttribute.id", groupId));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ParserGroupAttribute> getGroupAttributeListByMappingId(int mappingId) {
		logger.debug(">> getGroupAttributeListByMappingId : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ParserGroupAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParserGroupAttribute getGroupAttributeByMappingIdAndGroupName(int mappingId, String groupName) {
		logger.debug("Going fetch group attribute for name >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + groupName  + " and mapping id " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ParserGroupAttribute.class);
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		criteria.add(Restrictions.eq("name", groupName).ignoreCase());
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		List<ParserGroupAttribute> groupAttributeList = criteria.list();
		if(groupAttributeList != null && !groupAttributeList.isEmpty()){
			logger.info("Mapping found with name " + groupName);
			return groupAttributeList.get(0);
		}else{
			logger.info("No mapping found with name " + groupName);
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getSubGroupNameListByGroupId(ParserGroupAttribute groupAttribute) {
		logger.debug("Going fetch sub group name by groupId : " + groupAttribute.getId());
		Criteria criteria=getCurrentSession().createCriteria(ParserGroupAttribute.class);		
		criteria.add(Restrictions.eq("baseGroup.id", groupAttribute.getId()));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		List<ParserGroupAttribute> groupAttributeList = criteria.list();
		if(groupAttributeList != null && !groupAttributeList.isEmpty()){
			StringBuilder groupNameBuilder = new StringBuilder();
			for(ParserGroupAttribute attr : groupAttributeList) {
				groupNameBuilder.append(attr.getName()+BaseConstants.HASH_SEPARATOR);
			}
			return groupNameBuilder.substring(0,groupNameBuilder.length()-1);
		}else{
			logger.info("No sub group mapping found with group name " + groupAttribute.getName());
			return null;
		}
	}

	
}
