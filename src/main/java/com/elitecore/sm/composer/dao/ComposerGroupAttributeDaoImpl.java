package com.elitecore.sm.composer.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerGroupAttribute;

@Repository
public class ComposerGroupAttributeDaoImpl extends GenericDAOImpl<ComposerGroupAttribute> implements ComposerGroupAttributeDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ComposerGroupAttribute> getGroupAttributeListByMappingId(int mappingId) {
		logger.debug(">> getGroupAttributeListByMappingId : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ComposerGroupAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("myComposer.id", mappingId));
		return criteria.list();
	}

	@Override
	public ComposerGroupAttribute getGroupAttributeByMappingId(int mappingId, int groupId) {
		logger.debug(">> getGroupAttributeByMappingId : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ComposerGroupAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("myComposer.id", mappingId));
		criteria.add(Restrictions.eq("id", groupId));
		return (ComposerGroupAttribute)criteria.list().get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ComposerGroupAttribute checkUniqueGroupAttributeNameForUpdate(int mappingId, String name) {
		logger.debug("Going fetch group attribute for name >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + name  + " and mapping id " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ComposerGroupAttribute.class);
		criteria.add(Restrictions.eq("myComposer.id", mappingId));
		criteria.add(Restrictions.eq("name", name).ignoreCase());
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		List<ComposerGroupAttribute> groupAttributeList = criteria.list();
		if(groupAttributeList != null && !groupAttributeList.isEmpty()){
			logger.info("Mapping found with name " + name);
			return groupAttributeList.get(0);
		}else{
			logger.info("No mapping found with name " + name);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ComposerGroupAttribute> getGroupAttributeListByGroupId(int groupId, int mappingId) {
		logger.debug(">> getAttributeListByGroupId : GroupId : " + groupId);
		Criteria criteria=getCurrentSession().createCriteria(ComposerGroupAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("myComposer.id", mappingId));
		criteria.add(Restrictions.eq("baseGroup.id", groupId));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ComposerAttribute> getAttachedAttributeListByGroupId(int groupId, int mappingId) {
		logger.debug(">> getAttachedAttributeListByGroupId : GroupId : " + groupId);
		Criteria criteria=getCurrentSession().createCriteria(ComposerAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("myComposer.id", mappingId));
		criteria.add(Restrictions.eq("composerGroupAttribute.id", groupId));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ComposerGroupAttribute> getAttachedGroupAttributeListByGroupId(int groupId, int mappingId) {
		logger.debug(">> getAttachedGroupAttributeListByGroupId : GroupId : " + groupId);
		Criteria criteria=getCurrentSession().createCriteria(ComposerGroupAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("myComposer.id", mappingId));
		criteria.add(Restrictions.eq("baseGroup.id", groupId));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ComposerAttribute> getAttributeListEligibleToAttachWithGroup(int mappingId) {
		logger.debug(">> getAttributeListEligibleToAttachWithGroup : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ComposerAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("myComposer.id", mappingId));
		criteria.add(Restrictions.isNull("composerGroupAttribute.id"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ComposerGroupAttribute> getGroupAttributeListEligibleToAttachWithGroupByMappingId(int mappingId) {
		logger.debug(">> getGroupAttributeListEligibleToAttachWithGroup : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ComposerGroupAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("myComposer.id", mappingId));
		criteria.add(Restrictions.isNull("baseGroup.id"));
		return criteria.list();
	}

	@Override
	public ComposerGroupAttribute getGroupAttributeByGroupId(int groupId, int mappingId) {
		logger.debug(">> getGroupAttributeListEligibleToAttachWithGroup : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ComposerGroupAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("myComposer.id", mappingId));
		criteria.add(Restrictions.eq("id", groupId));
		return (ComposerGroupAttribute)criteria.list().get(0);
	}

}
