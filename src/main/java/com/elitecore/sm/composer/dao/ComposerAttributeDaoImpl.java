package com.elitecore.sm.composer.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.composer.model.ASN1ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.composer.model.NRTRDEComposerAttribute;
import com.elitecore.sm.composer.model.RAPComposerAttribute;
import com.elitecore.sm.composer.model.TAPComposerAttribute;
import com.elitecore.sm.parser.model.ASN1ATTRTYPE;

/**
 * 
 * @author avani.panchal
 *
 */
@Repository(value="composerAttributeDao")
public class ComposerAttributeDaoImpl extends GenericDAOImpl<ComposerAttribute> implements ComposerAttributeDao{
	
	@Autowired
	ComposerMappingDao composerMappingDao;
	
	/**
	 * Method will create a condition list to fetch composer attribute list grid.
	 * @param mappingId
	 * @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> getAttributeConditionList(int mappingId) {
		
		logger.debug(">> getAttributeConditionList in ComposerAttributeDaoImpl "  +mappingId);
		
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		
		conditionList.add(Restrictions.eq(BaseConstants.CRITERIA_COMPOSER_ID, mappingId));
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		
		logger.debug("<< getServiceBySearchParameters in ServicesDaoImpl ");
		return returnMap;
	}
	
	/**
	 * Count number of attribute in same mapping
	 * @param mappingId
	 * @return int
	 */
	@Override
	public int getAttrSeqNoByMappingId(int mappingId){
		
		Criteria criteria = getCurrentSession().createCriteria(ComposerAttribute.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		if (mappingId != 0) {
			criteria.add(Restrictions.eq(BaseConstants.CRITERIA_COMPOSER_ID, mappingId));
		} 
		
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
	}
	
	/**
	 * Get All Composer Attribute using mapping id
	 * @param mappingId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ComposerAttribute> getAllAttributeByMappingId(int mappingId){
		
		Criteria criteria=getCurrentSession().createCriteria(ComposerAttribute.class);
		
		criteria.add(Restrictions.eq(BaseConstants.CRITERIA_COMPOSER_ID, mappingId));
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		criteria.addOrder(Order.asc("sequenceNumber"));	
		return (List<ComposerAttribute>)criteria.list();
		
	}
	
	/**
	 * Method will update  and mark all associated entities flag mark as dirty.
	 * @param composerAttribute
	 * 
	 */
	@Override
	public void update (ComposerAttribute composerAttribute){
		getCurrentSession().update(composerAttribute);
			
		ComposerMapping composerMapping=composerAttribute.getMyComposer();
		composerMapping.getId();
	
		composerMappingDao.merge(composerMapping);
		
	}
	
	/**
	 * Method will save and  mark all associated entities flag mark as dirty.
	 * @param composerAttribute
	 * 
	 */
	@Override
	public void save(ComposerAttribute composerAttribute){
		getCurrentSession().save(composerAttribute);
		
		ComposerMapping composerMapping=composerAttribute.getMyComposer();
		composerMapping.getId();
	
		composerMappingDao.merge(composerMapping);
	}
	
	/**
	 * Method will update and mark all associated entities flag mark as dirty.
	 * @param composerAttribute
	 */
	@Override
	public void merge(ComposerAttribute composerAttribute){
		getCurrentSession().merge(composerAttribute);
		
		ComposerMapping composerMapping=composerAttribute.getMyComposer();
		composerMapping.getId();
	
		composerMappingDao.merge(composerMapping);
		
	}
	
	/**
	 * Method will fetch all composer attributes for selected device mapping.
	 * @param mappingId
	 * @return List<ComposerAttribute>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ComposerAttribute> getAttributeListByMappingId(int mappingId) {
		logger.debug(">> getAttributeListByMappingId : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ComposerAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("myComposer.id", mappingId));
		try{
			criteria.addOrder(Order.asc("attributeOrder"));
		}
		catch(NullPointerException e){
			logger.debug("Attribute order is null so adding it to criteria");
			logger.trace(e);
		}
		logger.debug("<< getAttributeListByMappingId ");
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ComposerAttribute> getAsn1AttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype) {
		logger.debug(">> getAsn1AttributeListByMappingId : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ASN1ComposerAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("myComposer.id", mappingId));
		if(asn1attrtype != null){
			criteria.add(Restrictions.eq("attrType", asn1attrtype));
		}
		criteria.addOrder(Order.asc("attributeOrder"));
		logger.debug("<< getAsn1AttributeListByMappingId ");
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ComposerAttribute> getTapAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype) {
		logger.debug(">> getTapAttributeListByMappingId : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(TAPComposerAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("myComposer.id", mappingId));
		criteria.add(Restrictions.isNull("composerGroupAttribute.id"));
		if(asn1attrtype != null){
			criteria.add(Restrictions.eq("attrType", asn1attrtype));
		}
		criteria.addOrder(Order.asc("attributeOrder"));
		logger.debug("<< getTapAttributeListByMappingId ");
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ComposerAttribute> getRapAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype) {
		logger.debug(">> getRapAttributeListByMappingId : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(RAPComposerAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("myComposer.id", mappingId));
		criteria.add(Restrictions.isNull("composerGroupAttribute.id"));
		if(asn1attrtype != null){
			criteria.add(Restrictions.eq("attrType", asn1attrtype));
		}
		criteria.addOrder(Order.asc("attributeOrder"));
		logger.debug("<< getRapAttributeListByMappingId ");
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ComposerAttribute> getNrtrdeAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype) {
		logger.debug(">> getNrtrdeAttributeListByMappingId : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(NRTRDEComposerAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("myComposer.id", mappingId));
		criteria.add(Restrictions.isNull("composerGroupAttribute.id"));
		if(asn1attrtype != null){
			criteria.add(Restrictions.eq("attrType", asn1attrtype));
		}
		criteria.addOrder(Order.asc("attributeOrder"));
		logger.debug("<< getNrtrdeAttributeListByMappingId ");
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public ComposerAttribute checkUniqueAttributeNameForUpdate(int mappingId, String name) {
		logger.debug("Going fetch attribute for name >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + name  + " and mapping id " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ComposerAttribute.class);
		criteria.add(Restrictions.eq("myComposer.id", mappingId));
		criteria.add(Restrictions.eq("destinationField", name).ignoreCase());
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		List<ComposerAttribute> composerAttributeList = criteria.list();
		if(composerAttributeList != null && !composerAttributeList.isEmpty() ){
			logger.info("Mapping found with name " + name);
			return composerAttributeList.get(0);
		}else{
			logger.info("No mapping found with name " + name);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getAllAttributeSeqNumberByMappingId(int mappingId,int composerId) {
		Criteria criteria = getCurrentSession().createCriteria(ComposerAttribute.class);
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("sequenceNumber"));
		criteria.add(Restrictions.eq(BaseConstants.CRITERIA_COMPOSER_ID, mappingId));
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		criteria.add(Restrictions.ne("id", composerId));
		criteria.setProjection(projectionList);
		return (List<Integer>)criteria.list();
	}

	@Override
	public Map<String, Object> getASN1AttributeConditionList(int mappingId, ASN1ATTRTYPE asn1attrtype) {
		
		logger.debug(">> getASn1AttributeConditionList in ComposerAttributeDaoImpl "  +mappingId);
		
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		
		conditionList.add(Restrictions.eq(BaseConstants.CRITERIA_COMPOSER_ID, mappingId));
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		if(asn1attrtype != null){
			conditionList.add(Restrictions.eq("attrType", asn1attrtype));
		}
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		
		logger.debug("<< getServiceBySearchParameters in ServicesDaoImpl ");
		return returnMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getAllASN1AttributeSeqNumberByMappingId(ComposerAttribute composerAttr) {
		Criteria criteria = getCurrentSession().createCriteria(ASN1ComposerAttribute.class);
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("sequenceNumber"));
		criteria.add(Restrictions.eq(BaseConstants.CRITERIA_COMPOSER_ID, composerAttr.getMyComposer().getId()));
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		criteria.add(Restrictions.ne("id", composerAttr.getId()));
		ASN1ComposerAttribute asn1ComposerAttribute = (ASN1ComposerAttribute)composerAttr;
		if(asn1ComposerAttribute.getAttrType() !=null){
			criteria.add(Restrictions.eq("attrType", asn1ComposerAttribute.getAttrType()));
		}
		criteria.setProjection(projectionList);
		return (List<Integer>)criteria.list();
	}

	@Override
	public ComposerAttribute getComposerAttributeByAttributeId(int attributeId, int mappingId) {
		Criteria criteria = getCurrentSession().createCriteria(ComposerAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.CRITERIA_COMPOSER_ID, mappingId));
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("id", attributeId));
		return (ComposerAttribute)criteria.list().get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ComposerAttribute> getComposerAttributeListByGroupId(int gropuId, int mappingId) {
		Criteria criteria = getCurrentSession().createCriteria(ComposerAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.CRITERIA_COMPOSER_ID, mappingId));
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("composerGroupAttribute.id", gropuId));
		return criteria.list();
	}


}
