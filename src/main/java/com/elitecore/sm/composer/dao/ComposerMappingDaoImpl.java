/**
 * 
 */
package com.elitecore.sm.composer.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.composer.model.ASCIIComposerMapping;
import com.elitecore.sm.composer.model.ASN1ComposerMapping;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.composer.model.FixedLengthASCIIComposerMapping;
import com.elitecore.sm.composer.model.NRTRDEComposerMapping;
import com.elitecore.sm.composer.model.RAPComposerMapping;
import com.elitecore.sm.composer.model.TAPComposerMapping;
import com.elitecore.sm.device.dao.DeviceDao;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.pathlist.dao.PathListDao;
import com.elitecore.sm.composer.model.XMLComposerMapping;
/**
 * @author Ranjitsinh Reval
 *
 */
@Repository(value="composerMappingDao")
public class ComposerMappingDaoImpl extends GenericDAOImpl<ComposerMapping> implements ComposerMappingDao {
	
	@Autowired
	PathListDao pathListDao;
	
	@Autowired
	DeviceDao deviceDao;
	
	/**
	 * Method will get all Mapping by device and composer type. 
	 * @param deviceId
	 * @param pluginMasterId
	 * @return List<ParserMapping>
	 * @see com.elitecore.sm.parser.dao.ParserMappingDao#getAllMappingBydeviceAndType()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ComposerMapping>  getAllMappingBydeviceAndComposerType(int deviceId, int pluginMasterId) {

		logger.debug(">> getAllMappingBydeviceAndComposerType ");
		Criteria criteria = getCurrentSession().createCriteria(ComposerMapping.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("device.id",deviceId));
		criteria.add(Restrictions.eq("composerType.id",pluginMasterId));
		logger.debug("<< getAllMappingBydeviceAndComposerType ");
		return criteria.list();
	}
	
	/**
	 * Method will check if already mapping existed with this name.
	 * @param name
	 * @return
	 */
	@Override
	public int getMappingCount(String name) {
		
		Criteria criteria = getCurrentSession().createCriteria(ComposerMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		if (name != null) {
			criteria.add(Restrictions.eq("name", name).ignoreCase());
		} 
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	/**
	 *  Find Ascii Composer Mapping By Id
	 * @param composerMappingId
	 * @return ASCIIComposerMapping
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ASCIIComposerMapping getAsciiComposerMappingById(int composerMappingId){
		
		ASCIIComposerMapping asciiComposer=null;
		Criteria criteria = getCurrentSession().createCriteria(ASCIIComposerMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("id", composerMappingId));
		
		List<ASCIIComposerMapping> asciiComposerList=(List<ASCIIComposerMapping>)criteria.list();
		
		if(asciiComposerList!=null && !asciiComposerList.isEmpty()){
			asciiComposer=asciiComposerList.get(0);
			Hibernate.initialize(asciiComposer.getDevice());
		}
		
		return asciiComposer;
	}
	
	/**
	 * Method will fetch all mapping association details for selected mapping.
	 * @param mappingId
	 * @return List<Parser>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Composer> getMappingAssociationDetails(int mappingId) {
		logger.debug(">> getMappingAssociationDetails : mappingId : " + mappingId);
		
		Criteria criteria=getCurrentSession().createCriteria(Composer.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("composerMapping.id", mappingId));
		
		logger.debug("<< getMappingAssociationDetails ");
		return criteria.list();
	}
	
	/**
	 * Method will update composer mapping and mark flag dirty to all associated entities.
	 * @param parserMapping
	 */
	@Override
	public void update (ComposerMapping composerMapping){
		getCurrentSession().update(composerMapping);
		List<Composer> composerList = composerMapping.getComposerWrapper();
		for (Composer composer : composerList) {
				composer.getId();
				pathListDao.merge(composer.getMyDistDrvPathlist());
		}		
	}
	
	/**
	 * Method will update parser mapping and mark flag dirty to all associated entities.
	 * @param parserMapping
	 */
	@Override
	public void merge (ComposerMapping composerMapping){
		getCurrentSession().merge(composerMapping);
		List<Composer> composerList = composerMapping.getComposerWrapper();
		for (Composer composer : composerList) {
				composer.getId();
				pathListDao.merge(composer.getMyDistDrvPathlist());
		}		
	}
	
	/**
	 * Fetch all dependents of composer mapping
	 * @param composerMapping
	 */
	@Override
	public void iterateOverComposerMapping(ComposerMapping composerMapping){
		
		if(composerMapping !=null){
			Hibernate.initialize(composerMapping.getComposerType());
			Device device=composerMapping.getDevice();
			if(device !=null && !StateEnum.DELETED.equals(device.getStatus())){
				deviceDao.iterateOverDevice(device);
			}
			Hibernate.initialize(composerMapping.getAttributeList());
			List<ComposerAttribute> composerAttributes=composerMapping.getAttributeList();
			Iterator<ComposerAttribute> itr = composerAttributes.iterator();
			while(itr.hasNext()){
				ComposerAttribute composerAttr = itr.next();
				if(StateEnum.DELETED.equals(composerAttr.getStatus())){
					itr.remove();
				}
			}
			composerMapping.setAttributeList(composerAttributes);
		}
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public ComposerMapping getComposerMappingDetailsByNameAndType(String name, int pluginTypeId) {
		logger.debug("Fetching composer mapping details for name " + name  + " and composer plugin id : " + pluginTypeId);
		Criteria criteria = getCurrentSession().createCriteria(ComposerMapping.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("name",name));
		if(pluginTypeId>-1) {
			criteria.add(Restrictions.eq("composerType.id",pluginTypeId));
		}
		List<ComposerMapping> composerMappinglist = criteria.list();
		
		if(composerMappinglist != null && !composerMappinglist.isEmpty()){
			logger.info("Mapping list found sucessfully.");
			iterateOverComposerMapping(composerMappinglist.get(0));  // It will fetch dependents.
			return composerMappinglist.get(0);
		}else{
			logger.info("Failed to get mapping details.");
			return null;
		}
	}

	@Override
	public ASN1ComposerMapping getAsn1ComposerMappingById(int composerMappingId) {
		ASN1ComposerMapping asciiComposer=null;
		Criteria criteria = getCurrentSession().createCriteria(ASN1ComposerMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("id", composerMappingId));
		
		@SuppressWarnings("unchecked")
		List<ASN1ComposerMapping> asciiComposerList=(List<ASN1ComposerMapping>)criteria.list();
		
		if(asciiComposerList!=null && !asciiComposerList.isEmpty()){
			asciiComposer=asciiComposerList.get(0);
			Hibernate.initialize(asciiComposer.getDevice());
		}
		
		return asciiComposer;
	}
	
	@Override
	public TAPComposerMapping getTapComposerMappingById(int composerMappingId) {
		TAPComposerMapping tapComposer=null;
		Criteria criteria = getCurrentSession().createCriteria(TAPComposerMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("id", composerMappingId));
		
		@SuppressWarnings("unchecked")
		List<TAPComposerMapping> tapComposerList=(List<TAPComposerMapping>)criteria.list();
		
		if(tapComposerList!=null && !tapComposerList.isEmpty()){
			tapComposer=tapComposerList.get(0);
			Hibernate.initialize(tapComposer.getDevice());
		}
		
		return tapComposer;
	}
	
	@Override
	public RAPComposerMapping getRapComposerMappingById(int composerMappingId) {
		RAPComposerMapping rapComposer=null;
		Criteria criteria = getCurrentSession().createCriteria(RAPComposerMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("id", composerMappingId));
		
		@SuppressWarnings("unchecked")
		List<RAPComposerMapping> rapComposerList=(List<RAPComposerMapping>)criteria.list();
		
		if(rapComposerList!=null && !rapComposerList.isEmpty()){
			rapComposer=rapComposerList.get(0);
			Hibernate.initialize(rapComposer.getDevice());
		}
		
		return rapComposer;
	}
	
	@Override
	public NRTRDEComposerMapping getNrtrdeComposerMappingById(int composerMappingId) {
		NRTRDEComposerMapping nrtrdeComposer=null;
		Criteria criteria = getCurrentSession().createCriteria(NRTRDEComposerMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("id", composerMappingId));
		
		@SuppressWarnings("unchecked")
		List<NRTRDEComposerMapping> nrtrdeComposerList=(List<NRTRDEComposerMapping>)criteria.list();
		
		if(nrtrdeComposerList!=null && !nrtrdeComposerList.isEmpty()){
			nrtrdeComposer=nrtrdeComposerList.get(0);
			Hibernate.initialize(nrtrdeComposer.getDevice());
		}
		
		return nrtrdeComposer;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public XMLComposerMapping getXMLComposerMappingById(int composerMappingId){
		
		XMLComposerMapping xmlComposer=null;
		Criteria criteria = getCurrentSession().createCriteria(XMLComposerMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("id", composerMappingId));
		
		List<XMLComposerMapping> xmlComposerList=(List<XMLComposerMapping>)criteria.list();
		
		if(xmlComposerList!=null && !xmlComposerList.isEmpty()){
			xmlComposer=xmlComposerList.get(0);
			Hibernate.initialize(xmlComposer.getDevice());
		}
		
		return xmlComposer;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FixedLengthASCIIComposerMapping getFixedLengthAsciiComposerMappingById(int fixedLengthAsciiComposerMappingId){
		FixedLengthASCIIComposerMapping fixedLengthASCIIComposerMapping = null;
		Criteria criteria = getCurrentSession().createCriteria(FixedLengthASCIIComposerMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS,StateEnum.DELETED));
		criteria.add(Restrictions.eq("id",fixedLengthAsciiComposerMappingId));
		
		List<FixedLengthASCIIComposerMapping> fixedLengthASCIIComposerMappings = (List<FixedLengthASCIIComposerMapping>)criteria.list();
		
		if(fixedLengthASCIIComposerMappings!= null && !fixedLengthASCIIComposerMappings.isEmpty()){
			fixedLengthASCIIComposerMapping = fixedLengthASCIIComposerMappings.get(0);
			Hibernate.initialize(fixedLengthASCIIComposerMapping.getDevice());
		}
		
		return fixedLengthASCIIComposerMapping;
	}

	
	/**
	 * Method will get all mapping list by list of ids
	 * @param ids
	 * @return List<ComposerMapping>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ComposerMapping> getAllMappingById(Integer[] ids) {
		logger.debug(">>  getAllMappingById of Composer");
		Criteria criteria = getCurrentSession().createCriteria(ComposerMapping.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		List<ComposerMapping> composerMapping = null;
		if(ids != null && ids.length > 0){
			criteria.add(Restrictions.in("id", ids));
			composerMapping = criteria.list(); 
		}
		return composerMapping;
	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ComposerMapping> getAllMappingByDeviceId(Integer[] deviceIds) {
		logger.debug(">>  getAllMappingByDeviceId ");
		Criteria criteria = getCurrentSession().createCriteria(ComposerMapping.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		List<ComposerMapping> composerMapping = null;
		if(deviceIds != null && deviceIds.length > 0){
			criteria.add(Restrictions.in("device.id", deviceIds));
			composerMapping = criteria.list(); 
			return composerMapping;
		}else{
			return composerMapping;
		}
	}
}
