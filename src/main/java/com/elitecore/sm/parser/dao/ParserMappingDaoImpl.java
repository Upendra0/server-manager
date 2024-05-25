/**
 * 
 */
package com.elitecore.sm.parser.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.device.dao.DeviceDao;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.device.model.SearchDeviceMapping;
import com.elitecore.sm.parser.model.ASN1ParserMapping;
import com.elitecore.sm.parser.model.AsciiParserMapping;
import com.elitecore.sm.parser.model.DetailLocalParserMapping;
import com.elitecore.sm.parser.model.FixedLengthASCIIParserMapping;
import com.elitecore.sm.parser.model.FixedLengthBinaryParserMapping;
import com.elitecore.sm.parser.model.HtmlParserMapping;
import com.elitecore.sm.parser.model.JsonParserMapping;
import com.elitecore.sm.parser.model.MTSiemensParserMapping;
import com.elitecore.sm.parser.model.NRTRDEParserMapping;
import com.elitecore.sm.parser.model.PDFParserMapping;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.ParserGroupAttribute;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.RapParserMapping;
import com.elitecore.sm.parser.model.RegExPattern;
import com.elitecore.sm.parser.model.RegexParserAttribute;
import com.elitecore.sm.parser.model.RegexParserMapping;
import com.elitecore.sm.parser.model.TapParserMapping;
import com.elitecore.sm.parser.model.VarLengthAsciiParserMapping;
import com.elitecore.sm.parser.model.VarLengthBinaryParserMapping;
import com.elitecore.sm.parser.model.XMLParserMapping;
import com.elitecore.sm.parser.model.XlsParserMapping;
import com.elitecore.sm.pathlist.dao.PathListDao;

/**
 * @author Ranjitsinh Reval
 *
 */
@Repository(value="parserMappingDao")
public class ParserMappingDaoImpl extends GenericDAOImpl<ParserMapping> implements ParserMappingDao{


	@Autowired
	PathListDao pathListDao;
	
	@Autowired
	DeviceDao deviceDao;
	
	
	/**
	 * Method will  fetch all device mapping list by all search parameters.
	 * @param device 
	 * @return Map
	 */
	@Override
	public Map<String, Object> getDeviceMappingBySearchParameters(SearchDeviceMapping deviceMapping) {
		
		logger.debug(">> getDeviceMappingBySearchParameters in ParserMappingDaoImpl "  + deviceMapping);
		
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		
		aliases.put("device", "dev");
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		
		logger.debug("<< getDeviceMappingBySearchParameters in ParserMappingDaoImpl ");
		return returnMap;
	}
	
	/**
	 * Method will fetch all mapping association details for selected mapping.
	 * @param mappingId
	 * @return List<Parser>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Parser> getMappingAssociationDetails(int mappingId) {
		logger.debug(">> getMappingAssociationDetails : mappingId : " + mappingId);
		
		Criteria criteria=getCurrentSession().createCriteria(Parser.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("parserMapping.id", mappingId));
		
		logger.debug("<< getMappingAssociationDetails ");
		return criteria.list();
	}

	/**
	 * Method will get all mapping list by list of ids
	 * @param ids
	 * @return List<ParserMapping>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ParserMapping> getAllMappingById(Integer[] ids) {
		logger.debug(">>  getAllMappingById ");
		Criteria criteria = getCurrentSession().createCriteria(ParserMapping.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		List<ParserMapping> parserMapping = null;
		if(ids != null && ids.length > 0){
			criteria.add(Restrictions.in("id", ids));
			parserMapping = criteria.list(); 
			return parserMapping;
		}else{
			return parserMapping;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ParserMapping> getAllMappingByDeviceId(Integer[] deviceIds) {
		logger.debug(">>  getAllMappingByDeviceId ");
		Criteria criteria = getCurrentSession().createCriteria(ParserMapping.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		List<ParserMapping> parserMapping = null;
		if(deviceIds != null && deviceIds.length > 0){
			criteria.add(Restrictions.in("device.id", deviceIds));
			parserMapping = criteria.list(); 
			return parserMapping;
		}else{
			return parserMapping;
		}
	}

	/**
	 * Method will get all Mapping by device and parser type. 
	 * @param deviceId
	 * @param parserType
	 * @return List<ParserMapping>
	 * @see com.elitecore.sm.parser.dao.ParserMappingDao#getAllMappingBydeviceAndType()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ParserMapping>  getAllMappingBydeviceAndType(int deviceId, int pluginMasterId) {
		logger.debug(">> getAllMappingBydeviceAndType ");
		Criteria criteria = getCurrentSession().createCriteria(ParserMapping.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("device.id",deviceId));
		criteria.add(Restrictions.eq("parserType.id",pluginMasterId));
		logger.debug("<< getAllMappingBydeviceAndType ");
		return criteria.list(); 
	}
	
	/** Method will get regex parser by mapping id.
	 *  (non-Javadoc)
	 * @see com.elitecore.sm.parser.dao.ParserMappingDao#getRegExParserMappingById(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RegexParserMapping getRegExParserMappingById(int parserMappingId){
		
		RegexParserMapping regExParser=null;
		Criteria criteria = getCurrentSession().createCriteria(RegexParserMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("id", parserMappingId));
		
		List<RegexParserMapping> regExParserList=(List<RegexParserMapping>)criteria.list();
		
		if(regExParserList!=null && !regExParserList.isEmpty()){
			regExParser=regExParserList.get(0);
			Hibernate.initialize(regExParser.getDevice());
			Hibernate.initialize(regExParser.getPatternList());
		}
		
		return regExParser;
	}
	
	
	/**
	 * Method will update parser mapping and mark flag dirty to all associated entities.
	 * @param parserMapping
	 */
	@Override
	public void update (ParserMapping parserMapping){
		getCurrentSession().update(parserMapping);
		List<Parser> parserList = parserMapping.getParserWrapper();
		for (Parser parser : parserList) {
				parser.getId();
				pathListDao.merge(parser.getParsingPathList());
		}		
	}
	
	/**
	 * Method will update parser mapping and mark flag dirty to all associated entities.
	 * @param parserMapping
	 */
	@Override
	public void merge (ParserMapping parserMapping){
		getCurrentSession().merge(parserMapping);
		//List<Parser> parserList = parserMapping.getParserWrapper();
		//MED-9367(due to getting ConcurrentModification)
		List<Parser> parserList =new ArrayList<>(parserMapping.getParserWrapper()); 
		for (Parser parser : parserList) {
				parser.getId();
				pathListDao.merge(parser.getParsingPathList());
		}		
	}

	/**
	 * Method will check if already mapping existed with this name.
	 * @param name
	 * @return
	 */
	@Override
	public int getMappingCount(String name) {
		
		Criteria criteria = getCurrentSession().createCriteria(ParserMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		if (name != null) {
			criteria.add(Restrictions.eq("name", name).ignoreCase());
		} 
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	/**
	 *  Find Ascii Parser Mapping By Id
	 * @param parserMappingId
	 * @return AsciiParserMapping
	 */
	@SuppressWarnings("unchecked")
	@Override
	public AsciiParserMapping getAsciiParserMappingById(int parserMappingId){
		
		AsciiParserMapping asciiParser=null;
		Criteria criteria = getCurrentSession().createCriteria(AsciiParserMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("id", parserMappingId));
		
		List<AsciiParserMapping> asciiParserList=(List<AsciiParserMapping>)criteria.list();
		
		if(asciiParserList!=null && !asciiParserList.isEmpty()){
			asciiParser=asciiParserList.get(0);
			Hibernate.initialize(asciiParser.getDevice());
		}
		
		return asciiParser;
	}
	
	/**
	 *  Find Var Length Ascii Parser Mapping By Id
	 * @param parserMappingId
	 * @return VarLengthAsciiParserMapping
	 */
	@SuppressWarnings("unchecked")
	@Override
	public VarLengthAsciiParserMapping getVarLengthAsciiParserMappingById(int parserMappingId){
		
		VarLengthAsciiParserMapping varLengthAsciiParserMapping=null;
		Criteria criteria = getCurrentSession().createCriteria(VarLengthAsciiParserMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq(BaseConstants.ID, parserMappingId));
		
		List<VarLengthAsciiParserMapping> varLengthAsciiParserMappingList=(List<VarLengthAsciiParserMapping>)criteria.list();
		
		if(varLengthAsciiParserMappingList!=null && !varLengthAsciiParserMappingList.isEmpty()){
			varLengthAsciiParserMapping=varLengthAsciiParserMappingList.get(0);
			Hibernate.initialize(varLengthAsciiParserMapping.getDevice());
		}
		
		return varLengthAsciiParserMapping;
	}
	
	/**
	 *  Find Var Length Binary Parser Mapping By Id
	 * @param parserMappingId
	 * @return VarLengthBinaryParserMapping
	 */
	@SuppressWarnings("unchecked")
	@Override
	public VarLengthBinaryParserMapping getVarLengthBinaryParserMappingById(int parserMappingId){
		
		VarLengthBinaryParserMapping varLengthBinaryParserMapping=null;
		Criteria criteria = getCurrentSession().createCriteria(VarLengthBinaryParserMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq(BaseConstants.ID, parserMappingId));
		
		List<VarLengthBinaryParserMapping> varLengthBinaryParserMappingList=(List<VarLengthBinaryParserMapping>)criteria.list();
		
		if(varLengthBinaryParserMappingList!=null && !varLengthBinaryParserMappingList.isEmpty()){
			varLengthBinaryParserMapping=varLengthBinaryParserMappingList.get(0);
			Hibernate.initialize(varLengthBinaryParserMapping.getDevice());
		}
		
		return varLengthBinaryParserMapping;
	}
	
	/**
	 * Fetch all depedants of parser mapping
	 */
	@Override
	public void iterateOverParserMapping(ParserMapping parserMapping){
		
		if(parserMapping !=null){
			
			Hibernate.initialize(parserMapping.getParserType());
			Device device=parserMapping.getDevice();
			if(device !=null && !StateEnum.DELETED.equals(device.getStatus())){
				deviceDao.iterateOverDevice(device);
			}
			if(parserMapping instanceof RegexParserMapping){
				List<RegExPattern> regExPatternList=((RegexParserMapping) parserMapping).getPatternList();
				Iterator<RegExPattern> itr = regExPatternList.iterator();
				while(itr.hasNext()){
					RegExPattern regExPattern = itr.next();
					if(!StateEnum.DELETED.equals(regExPattern.getStatus())){
						Hibernate.initialize(regExPattern.getAttributeList());
						List<RegexParserAttribute> regExPatternAttributes=regExPattern.getAttributeList();
						Iterator<RegexParserAttribute> itrAttr = regExPatternAttributes.iterator();
						while(itrAttr.hasNext()){
							RegexParserAttribute regExPatternattr = itrAttr.next();
							if(StateEnum.DELETED.equals(regExPatternattr.getStatus())){
								itrAttr.remove();
							}
						}
						regExPattern.setAttributeList(regExPatternAttributes);
					}else{
						itr.remove();
					}
 				}
				((RegexParserMapping) parserMapping).setPatternList(regExPatternList);
			}else{
				Hibernate.initialize(parserMapping.getParserAttributes());
				List<ParserAttribute> parserAttributes=parserMapping.getParserAttributes();
				// Attribute sorting based on attribute order
				Collections.sort(parserAttributes, new Comparator<ParserAttribute>() {
					@Override
					public int compare(ParserAttribute object1, ParserAttribute object2) {
						return ((Integer)object1.getAttributeOrder()).compareTo((Integer)object2.getAttributeOrder());
					}
				});
				Iterator<ParserAttribute> itrParserAttributes = parserAttributes.iterator();
				Integer maxOrder = 1;				
				while(itrParserAttributes.hasNext()){
					ParserAttribute parserAttr = itrParserAttributes.next();
					if(StateEnum.DELETED.equals(parserAttr.getStatus()))
						itrParserAttributes.remove();
					else
						parserAttr.setAttributeOrder(maxOrder++);
				}
				parserMapping.setParserAttributes(parserAttributes);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParserMapping getMappingDetailsByNameAndType(String name, int pluginTypeId) {
		logger.debug("Fetching mapping details for name " + name  + " and plugin id : " + pluginTypeId);
		Criteria criteria = getCurrentSession().createCriteria(ParserMapping.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("name",name));
		criteria.add(Restrictions.eq("parserType.id",pluginTypeId));
		List<ParserMapping> parserMappinglist = criteria.list();
		
		if(parserMappinglist != null && !parserMappinglist.isEmpty()){
			logger.info("Mapping list found sucessfully.");
			return parserMappinglist.get(0);
		}else{
			logger.info("Failed to get mapping details.");
			return null;
		}
		
	}
	
	/**
	 * Method will fetch all parser mapping list for all deviceIds and parser type. 
	 * @param deviceIds
	 * @param plugInType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllMappingByDeviceTypeAndParserType(List<Integer> deviceIds, String plugInType) {
		String hql = "SELECT pm.id,pm.name FROM ParserMapping pm WHERE pm.device.id in (:idList) AND pm.parserType.alias=:pluginType AND pm.status=:status";
		Query query = getCurrentSession().createQuery(hql);
		query.setParameterList("idList",deviceIds);
		query.setParameter("pluginType", plugInType);
		query.setParameter(BaseConstants.STATUS,StateEnum.ACTIVE);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ASN1ParserMapping getAsn1ParserMappingById(int parserMappingId) {
		Criteria criteria = getCurrentSession().createCriteria(ASN1ParserMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("id", parserMappingId));
		List<ASN1ParserMapping> asn1ParserList=(List<ASN1ParserMapping>)criteria.list();
		if(asn1ParserList!=null && !asn1ParserList.isEmpty()){
			ASN1ParserMapping asn1ParserMapping = asn1ParserList.get(0);
			Hibernate.initialize(asn1ParserMapping.getDevice());
			return asn1ParserMapping;
		}
		return null;		
	}
	
	/**
	 *  Find XML Parser Mapping By Id
	 * @param parserMappingId
	 * @return XMLParserMapping
	 */
	@SuppressWarnings("unchecked")
	@Override
	public XMLParserMapping getXMLParserMappingById(int parserMappingId){
		
		XMLParserMapping xmlParser=null;
		Criteria criteria = getCurrentSession().createCriteria(XMLParserMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("id", parserMappingId));
		
		List<XMLParserMapping> xamlParserList=(List<XMLParserMapping>)criteria.list();
		
		if(xamlParserList!=null && !xamlParserList.isEmpty()){
			xmlParser=xamlParserList.get(0);
			Hibernate.initialize(xmlParser.getDevice());
		}
		
		return xmlParser;
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public FixedLengthASCIIParserMapping getFixedLengthASCIIParserMappingById(int fixedLengthASCIIParserMappingId){
		Criteria criteria = getCurrentSession().createCriteria(FixedLengthASCIIParserMapping.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("id", fixedLengthASCIIParserMappingId));
		List<FixedLengthASCIIParserMapping> fixedLengthASCIIParserMappings = (List<FixedLengthASCIIParserMapping>)criteria.list();
		if(fixedLengthASCIIParserMappings != null && !fixedLengthASCIIParserMappings.isEmpty()){
			FixedLengthASCIIParserMapping fixedLengthASCIIParserMapping = fixedLengthASCIIParserMappings.get(0);
			Hibernate.initialize(fixedLengthASCIIParserMapping.getDevice());
			return fixedLengthASCIIParserMapping;
		}
		return null;
	}
	@Override
	@SuppressWarnings("unchecked")
	public RapParserMapping getRapParserMappingById(int rapParserMappingId){
		Criteria criteria = getCurrentSession().createCriteria(RapParserMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("id", rapParserMappingId));
		List<RapParserMapping> rapParserList=(List<RapParserMapping>)criteria.list();
		if(rapParserList!=null && !rapParserList.isEmpty()){
			RapParserMapping rapParserMapping = rapParserList.get(0);
			Hibernate.initialize(rapParserMapping.getDevice());
			return rapParserMapping;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public TapParserMapping getTapParserMappingById(int tapParserMappingById) {
		Criteria criteria=getCurrentSession().createCriteria(TapParserMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("id", tapParserMappingById));
		List<TapParserMapping> tapParserList =(List<TapParserMapping>)criteria.list();
		if(tapParserList!=null && !tapParserList.isEmpty()){
			TapParserMapping tapParserMapping = tapParserList.get(0);
			Hibernate.initialize(tapParserMapping.getDevice());
			return tapParserMapping;
		}
		
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public NRTRDEParserMapping getNRTRDEParserMappingById(int nrtrdeParserMappingId) {
		
		Criteria criteria=getCurrentSession().createCriteria(NRTRDEParserMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("id", nrtrdeParserMappingId));
		List<NRTRDEParserMapping> NRTRDEParserList=(List<NRTRDEParserMapping>)criteria.list();
		if(NRTRDEParserList!=null&& !NRTRDEParserList.isEmpty()){
			NRTRDEParserMapping nrtrdeParserMapping = NRTRDEParserList.get(0);
			Hibernate.initialize(nrtrdeParserMapping);
			return nrtrdeParserMapping;
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public FixedLengthBinaryParserMapping getFixedLengthBinaryParserMappingById(int fixedLengthBinaryParserMappingId) {
		
		Criteria criteria=getCurrentSession().createCriteria(FixedLengthBinaryParserMapping.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("id", fixedLengthBinaryParserMappingId));
		List<FixedLengthBinaryParserMapping> fixedLengthBinaryParserMappings=(List<FixedLengthBinaryParserMapping>)criteria.list();
		if(fixedLengthBinaryParserMappings !=null&& !fixedLengthBinaryParserMappings.isEmpty()){
			FixedLengthBinaryParserMapping	fixedLengthBinaryParserMapping = fixedLengthBinaryParserMappings.get(0);
			Hibernate.initialize(fixedLengthBinaryParserMapping.getDevice());
			return fixedLengthBinaryParserMapping;
		}
		return null;
	}

	@Override
	public HtmlParserMapping getHtmlParserMappingById(int parserMappingId) {
		Criteria criteria=getCurrentSession().createCriteria(HtmlParserMapping.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("id", parserMappingId));
		List<HtmlParserMapping> htmlParserMappings=(List<HtmlParserMapping>)criteria.list();
		if(htmlParserMappings !=null&& !htmlParserMappings.isEmpty()){
			HtmlParserMapping	htmlParserMapping = htmlParserMappings.get(0);
			Hibernate.initialize(htmlParserMapping.getDevice());
			Hibernate.initialize(htmlParserMapping.getGroupAttributeList());
			return htmlParserMapping;
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public PDFParserMapping getPDFParserMappingById(int pdfParserMappingId) {
		Criteria criteria=getCurrentSession().createCriteria(PDFParserMapping.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("id", pdfParserMappingId));
		List<PDFParserMapping> pdfParserMappings=(List<PDFParserMapping>)criteria.list();
		if(pdfParserMappings !=null&& !pdfParserMappings.isEmpty()){
			PDFParserMapping pdfParserMapping = pdfParserMappings.get(0);
			Hibernate.initialize(pdfParserMapping.getDevice());
			Hibernate.initialize(pdfParserMapping.getGroupAttributeList());
			List<ParserGroupAttribute> pgaList = pdfParserMapping.getGroupAttributeList();
			if(pgaList != null && !pgaList.isEmpty()){
				for(ParserGroupAttribute pga : pgaList){
					Hibernate.initialize(pga.getAttributeList());
					Hibernate.initialize(pga.getParserPageConfigurationList());
				}
			}
			Hibernate.initialize(pdfParserMapping.getParserAttributes());
			return pdfParserMapping;
		}
		return null;
	}

	@Override
	public XlsParserMapping getXlsParserMappingById(int parserMappingId) {
		Criteria criteria=getCurrentSession().createCriteria(XlsParserMapping.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("id", parserMappingId));
		List<XlsParserMapping> xlsParserMappings=(List<XlsParserMapping>)criteria.list();
		if(xlsParserMappings !=null&& !xlsParserMappings.isEmpty()){
			XlsParserMapping	xlsParserMapping = xlsParserMappings.get(0);
			Hibernate.initialize(xlsParserMapping.getDevice());
			Hibernate.initialize(xlsParserMapping.getGroupAttributeList());
			return xlsParserMapping;
		}
		return null;
	}
	
	@Override
	public List<ParserMapping>  getUserDefinedMappingIds() {
		logger.debug(">> getAllUserDefienedMappingId ");
		Criteria criteria = getCurrentSession().createCriteria(ParserMapping.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("mappingType",1));
		logger.debug("<< getAllUserDefienedMappingId ");
		return criteria.list(); 
	}

	@Override
	public JsonParserMapping getJsonParserMappingById(int parserMappingId) {
		JsonParserMapping jsonParser=null;
		Criteria criteria = getCurrentSession().createCriteria(JsonParserMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("id", parserMappingId));
		
		List<JsonParserMapping> jsonParserList=(List<JsonParserMapping>)criteria.list();
		
		if(jsonParserList!=null && !jsonParserList.isEmpty()){
			jsonParser=jsonParserList.get(0);
			Hibernate.initialize(jsonParser.getDevice());
		}
		
		return jsonParser;
	}
	
	@Override
	public MTSiemensParserMapping getMTSiemensParserMappingById(int parserMappingId) {
		MTSiemensParserMapping mtsiemensParser=null;
		Criteria criteria = getCurrentSession().createCriteria(MTSiemensParserMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("id", parserMappingId));
		
		List<MTSiemensParserMapping> mtsiemensParserList=(List<MTSiemensParserMapping>)criteria.list();
		
		if(mtsiemensParserList!=null && !mtsiemensParserList.isEmpty()){
			mtsiemensParser=mtsiemensParserList.get(0);
			Hibernate.initialize(mtsiemensParser.getDevice());
		}
		
		return mtsiemensParser;
	}

	/**
	 *  Find Detail Local Parser Mapping By Id
	 * @param parserMappingId
	 * @return DetailLocalParserMapping
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DetailLocalParserMapping getDetailLocalParserMappingById(int parserMappingId) {
		// TODO Auto-generated method stub
		DetailLocalParserMapping detailLocalParser=null;
		Criteria criteria = getCurrentSession().createCriteria(DetailLocalParserMapping.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("id", parserMappingId));
		
		List<DetailLocalParserMapping> detailLocalParserList=(List<DetailLocalParserMapping>)criteria.list();
		
		if(detailLocalParserList!=null && !detailLocalParserList.isEmpty()){
			detailLocalParser=detailLocalParserList.get(0);
			Hibernate.initialize(detailLocalParser.getDevice());
		}
		
		return detailLocalParser;
	}

	
}
