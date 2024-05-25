/**
 * 
 */
package com.elitecore.sm.parser.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.drivers.dao.DriversDao;
import com.elitecore.sm.parser.model.ASN1ATTRTYPE;
import com.elitecore.sm.parser.model.ASN1ParserAttribute;
import com.elitecore.sm.parser.model.NRTRDEParserAttribute;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.RAPATTRTYPE;
import com.elitecore.sm.parser.model.RAPParserAttribute;
import com.elitecore.sm.parser.model.RegexParserAttribute;
import com.elitecore.sm.parser.model.TAPATTRTYPE;
import com.elitecore.sm.parser.model.TAPParserAttribute;
import com.elitecore.sm.parser.model.VarLengthBinaryParserAttribute;
import com.elitecore.sm.services.dao.ServicesDao;

import org.hibernate.SQLQuery;

import com.elitecore.sm.parser.model.AsciiParserAttribute;
/**s
 * @author Ranjitsinh Reval
 *
 */
@Repository(value="parserAttributeDao")
public class ParserAttributeDaoImpl extends GenericDAOImpl<ParserAttribute> implements ParserAttributeDao {

	@Autowired
	DriversDao driversDao;
	
	@Autowired
	ServicesDao servicesDao;
	
	@Autowired
	ParserMappingDao parserMappingDao;
	
	@Autowired
	RegExPatternDao regExPatternDao;
	
	
	/**
	 * Method will fetch all parser attributes for selected device mapping.
	 * @param mappingId
	 * @return List<ParserAttribute>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ParserAttribute> getAttributeListByMappingId(int mappingId) {
		logger.debug(">> getAttributeListByMappingId : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ParserAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		criteria.addOrder(Order.asc("attributeOrder"));
		logger.debug("<< getAttributeListByMappingId ");
		
		return criteria.list();
	}

	/**
	 * Method will create a condition list to fetch parser attribute list grid.
	 * @param mappingId
	 * @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> getAttributeConditionList(int mappingId) {
		
		logger.debug(">> getAttributeConditionList in ParserAttributeDaoImpl "  +mappingId);
		
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		
		conditionList.add(Restrictions.eq("parserMapping.id", mappingId));
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		
		logger.debug("<< getServiceBySearchParameters in ServicesDaoImpl ");
		return returnMap;
	}
	
	/**
	 * Method will update  and mark all associated entities flag mark as dirty.
	 * @param parserAttribute
	 * 
	 */
	@Override
	public void update (ParserAttribute parserAttribute){
		getCurrentSession().update(parserAttribute);
		ParserMapping parserMapping;
		if(parserAttribute instanceof RegexParserAttribute){
			logger.debug("Object type found RegEx Parser Attribute.");
			((RegexParserAttribute) parserAttribute).getPattern().getId();
			((RegexParserAttribute) parserAttribute).getPattern().getParserMapping().getId();
			parserMapping=((RegexParserAttribute) parserAttribute).getPattern().getParserMapping();
			
		}else{
			
			parserAttribute.getParserMapping().getId();
			parserMapping=parserAttribute.getParserMapping();
		}
		parserMappingDao.merge(parserMapping);
		
	}
	
	/**
	 * Method will save and  mark all associated entities flag mark as dirty.
	 * @param parserAttribute
	 * 
	 */
	@Override
	public void save(ParserAttribute parserAttribute){
		getCurrentSession().save(parserAttribute);
		ParserMapping parserMapping;
		if(parserAttribute instanceof RegexParserAttribute){
			
			logger.debug("RegEx Parser Attribute Object found");
			((RegexParserAttribute) parserAttribute).getPattern().getId();
			((RegexParserAttribute) parserAttribute).getPattern().getParserMapping().getId();
			parserMapping=((RegexParserAttribute) parserAttribute).getPattern().getParserMapping();
			
		}else{
			
			parserAttribute.getParserMapping().getId();
			parserMapping=parserAttribute.getParserMapping();
		}
		parserMappingDao.merge(parserMapping);
	}
	
	/**
	 * Method will update and mark all associated entities flag mark as dirty.
	 * @param parserAttribute
	 */
	@Override
	public void merge(ParserAttribute parserAttribute){
		getCurrentSession().merge(parserAttribute);
		
		ParserMapping parserMapping;
		if(parserAttribute instanceof RegexParserAttribute){
			
			logger.debug("RegEx Parser Attribute Object found");
			((RegexParserAttribute) parserAttribute).getPattern().getId();
			((RegexParserAttribute) parserAttribute).getPattern().getParserMapping().getId();
			parserMapping=((RegexParserAttribute) parserAttribute).getPattern().getParserMapping();
			
		}else{
			
			parserAttribute.getParserMapping().getId();
			parserMapping=parserAttribute.getParserMapping();
		}
		parserMappingDao.merge(parserMapping);
	}

	/**
	 * Method will check mapping name for the current mapping.
	 * @param mappingId
	 * @param name
	 * @return
	 */
	@Override
	public int getAttributeCountByName(int mappingId, String sourceField, String unifiedField, int attributeId) {
		logger.debug("Going fetch attribute for name " + sourceField + " and mapping id " + mappingId);
		Criteria criteria = getCurrentSession().createCriteria(ParserAttribute.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		if(attributeId > 0){
			criteria.add(Restrictions.ne("id", attributeId));
		}
		if(StringUtils.isNotBlank(sourceField)){
			criteria.add(Restrictions.eq("sourceField", sourceField).ignoreCase());

		}
		if(StringUtils.isNotBlank(unifiedField)){
			criteria.add(Restrictions.eq("unifiedField", unifiedField).ignoreCase());
		}
		 
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	@Override
	public int getAttributeCountByIPPortName(int mappingId, String unifiedField, int attributeId) {
		if(StringUtils.isNotBlank(unifiedField)){
		Criteria criteria = getCurrentSession().createCriteria(ParserAttribute.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		if(attributeId > 0){
			criteria.add(Restrictions.ne("id", attributeId));
		}
		Criterion salary = Restrictions.eq("unifiedField", unifiedField).ignoreCase();
		Criterion name = Restrictions.eq("portUnifiedField", unifiedField).ignoreCase();
		LogicalExpression orExp = Restrictions.or(salary, name);
		criteria.add( orExp );
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		}
		return 0;
	}
			
	@SuppressWarnings("unchecked")
	@Override
	public List<RegexParserAttribute> getRegExAttributeByPatternId(int regExPatternId){
		
		Criteria criteria=getCurrentSession().createCriteria(ParserAttribute.class);
		criteria.add(Restrictions.eq("pattern.id", regExPatternId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		List<RegexParserAttribute> regexAttrList=(List<RegexParserAttribute>)criteria.list();
		
		return  ((regexAttrList !=null) &&  !regexAttrList.isEmpty()) ?  regexAttrList : null;
	}
	
	@Override
	public ASN1ParserAttribute getASN1ParserAttributeById(int attributeId){
		
		Criteria criteria=getCurrentSession().createCriteria(ASN1ParserAttribute.class);
		criteria.add(Restrictions.eq("id", attributeId));
		ASN1ParserAttribute asn1ParserAttribute = (ASN1ParserAttribute) criteria.uniqueResult();
		if(asn1ParserAttribute != null){
			return asn1ParserAttribute;
		}
		return null;
	}
	
	/**
	 * Method will check mapping name for the current mapping while update attribute.
	 * @param mappingId
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ParserAttribute checkUniqueAttributeNameForUpdate(int mappingId, String unifiedField,String name,String pluginType) {
		logger.debug("Going fetch attribute for name " + name  + " and mapping id " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ParserAttribute.class);
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		if(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType) || EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
			criteria.add(Restrictions.eq("unifiedField", name).ignoreCase());
		}else{
			if(EngineConstants.NATFLOW_PARSING_PLUGIN.equals(pluginType) || EngineConstants.ASN1_PARSING_PLUGIN.equals(pluginType))
				criteria.add(Restrictions.eq("unifiedField", unifiedField).ignoreCase());
			criteria.add(Restrictions.eq("sourceField", name).ignoreCase());
		}
		
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		List<ParserAttribute> parserAttributeList = criteria.list();
		if(parserAttributeList != null && !parserAttributeList.isEmpty() ){
			logger.info("Mapping found with name " + name);
			return parserAttributeList.get(0);
		}else{
			logger.info("No mapping found with name " + name);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ParserAttribute> getVarLengthBinaryAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype) {
		logger.debug(">> getVarLengthBinaryAttributeListByMappingId : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(VarLengthBinaryParserAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		if(asn1attrtype != null){
			criteria.add(Restrictions.eq("attrType", asn1attrtype));
		}
		criteria.addOrder(Order.asc("attributeOrder"));
		logger.debug("<< getVarLengthBinaryAttributeListByMappingId ");
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ParserAttribute> getAsn1AttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype) {
		logger.debug(">> getAsn1AttributeListByMappingId : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(ASN1ParserAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		if(asn1attrtype != null){
			criteria.add(Restrictions.eq("attrType", asn1attrtype));
		}
		criteria.addOrder(Order.asc("attributeOrder"));
		logger.debug("<< getAsn1AttributeListByMappingId ");
		
		return criteria.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ParserAttribute> getRapAttributeListByMappingId(int mappingId, RAPATTRTYPE rapattrtype) {
		logger.debug(">> getRapAttributeListByMappingId : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(RAPParserAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		criteria.add(Restrictions.isNull("parserGroupAttribute.id"));
		if(rapattrtype != null){
			criteria.add(Restrictions.eq("attrType", rapattrtype));
		}
		criteria.addOrder(Order.asc("attributeOrder"));
		logger.debug("<< getRapAttributeListByMappingId ");
		
		return criteria.list();
	}

	@Override
	public Map<String, Object> getASN1AttributeConditionList(int mappingId,ASN1ATTRTYPE asn1attrtype) {
	logger.debug(">> getASn1AttributeConditionList in ComposerAttributeDaoImpl "  +mappingId);
		
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		
		conditionList.add(Restrictions.eq("parserMapping.id", mappingId));
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		if(asn1attrtype != null){
			conditionList.add(Restrictions.eq("attrType", asn1attrtype));
		}
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		
		logger.debug("<< getServiceBySearchParameters in ServicesDaoImpl ");
		return returnMap;
	}
	@Override
	public RAPParserAttribute getRAPParserAttributeById(int attributeId){
		
		Criteria criteria=getCurrentSession().createCriteria(RAPParserAttribute.class);
		criteria.add(Restrictions.eq("id", attributeId));
		RAPParserAttribute rapParserAttribute = (RAPParserAttribute)criteria.uniqueResult();
		if(rapParserAttribute != null){
			return rapParserAttribute;
		}
		return null;
	}
	@Override
	public TAPParserAttribute getTAPParserAttributeById(int attributeId) {
		Criteria criteria=getCurrentSession().createCriteria(TAPParserAttribute.class);
		criteria.add(Restrictions.eq("id", attributeId));
		TAPParserAttribute tapParserAttribute = (TAPParserAttribute)criteria.uniqueResult();
		if(tapParserAttribute != null){
			return tapParserAttribute;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ParserAttribute> getTapAttributeListByMappingId(int mappingId, TAPATTRTYPE tapattrtype) {
		logger.debug(">> getRapAttributeListByMappingId : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(TAPParserAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		criteria.add(Restrictions.isNull("parserGroupAttribute.id"));
		if(tapattrtype != null){
			criteria.add(Restrictions.eq("attrType", tapattrtype));
		}
		criteria.addOrder(Order.asc("attributeOrder"));
		logger.debug("<< getRapAttributeListByMappingId ");
		
		return criteria.list();
	}

	@Override
	public NRTRDEParserAttribute getNrtrdeParserAttributeById(int attributeId) {
		Criteria criteria=getCurrentSession().createCriteria(NRTRDEParserAttribute.class);
		criteria.add(Restrictions.eq("id", attributeId));
		NRTRDEParserAttribute nrtrdeParserAttribute = (NRTRDEParserAttribute)criteria.uniqueResult();
		if(nrtrdeParserAttribute!=null){
			return nrtrdeParserAttribute;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ParserAttribute> getNrtrdeAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype) {
		logger.debug(">> getRapAttributeListByMappingId : mappingId : " + mappingId);
		Criteria criteria=getCurrentSession().createCriteria(NRTRDEParserAttribute.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		criteria.add(Restrictions.isNull("parserGroupAttribute.id"));
		if(asn1attrtype!=null){
			criteria.add(Restrictions.eq("attrType", asn1attrtype));
		}
		criteria.addOrder(Order.asc("attributeOrder"));
		logger.debug("<< getNrtrdeAttributeListByMappingId ");

		return criteria.list();
	}
	@Override
	public ParserAttribute getParserAttributeByAttributeId(int attributeId, int mappingId) {
		Criteria criteria = getCurrentSession().createCriteria(ParserAttribute.class);
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("id", attributeId));
		return (ParserAttribute)criteria.list().get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ParserAttribute> getParserAttributeListByGroupId(int gropuId, int mappingId) {
		Criteria criteria = getCurrentSession().createCriteria(ParserAttribute.class);
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("parserGroupAttribute.id", gropuId));
		return criteria.list();
	}
	
	public int getAttributeCountByNameForRegex(int patternId, String sourceField, String unifiedField, int attributeId) {
		logger.debug("Going fetch attribute for name " + sourceField + " and mapping id " + patternId);
		Criteria criteria = getCurrentSession().createCriteria(RegexParserAttribute.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("pattern.id", patternId));
		/*if(StringUtils.isNotBlank(sourceField)){
			criteria.add(Restrictions.eq("sourceField", sourceField).ignoreCase());

		}*/
		if(StringUtils.isNotBlank(unifiedField)){
			criteria.add(Restrictions.eq("unifiedField", unifiedField).ignoreCase());
		}
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	@Override
	public int updateAsciiParserTypeByMappingId(int mappingId) {
		String sql = "UPDATE TBLTPARSERATTR SET TYPE=:type WHERE PARSERMAPPINGID=:mappingId";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setParameter("type", AsciiParserAttribute.class.getSimpleName());
		query.setParameter("mappingId", mappingId);
		return query.executeUpdate();				
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<RegexParserAttribute> getRegExAttributeByPatternIdAndUnifiedField(int regExPatternId,String unifiedField){
			Criteria criteria=getCurrentSession().createCriteria(ParserAttribute.class);
			criteria.add(Restrictions.eq("pattern.id", regExPatternId));
			criteria.add(Restrictions.eq("unifiedField", unifiedField));
			criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
			List<RegexParserAttribute> regexAttrList=(List<RegexParserAttribute>)criteria.list();
			return  ((regexAttrList !=null) &&  !regexAttrList.isEmpty()) ?  regexAttrList : null;
	}
	
 }
