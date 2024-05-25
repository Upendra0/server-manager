/**
 * 
 */
package com.elitecore.sm.parser.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.dao.ComposerDao;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.device.dao.DeviceDao;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.parser.controller.ParserAttributeFactory;
import com.elitecore.sm.parser.dao.ParserAttributeDao;
import com.elitecore.sm.parser.dao.ParserMappingDao;
import com.elitecore.sm.parser.dao.RegExPatternDao;
import com.elitecore.sm.parser.model.ASN1ParserAttribute;
import com.elitecore.sm.parser.model.ASN1ParserMapping;
import com.elitecore.sm.parser.model.AsciiParserMapping;
import com.elitecore.sm.parser.model.DetailLocalParserMapping;
import com.elitecore.sm.parser.model.FixedLengthASCIIParserMapping;
import com.elitecore.sm.parser.model.FixedLengthBinaryParserMapping;
import com.elitecore.sm.parser.model.HtmlParserMapping;
import com.elitecore.sm.parser.model.JsonParserMapping;
import com.elitecore.sm.parser.model.MTSiemensParserMapping;
import com.elitecore.sm.parser.model.NATFlowParserMapping;
import com.elitecore.sm.parser.model.NRTRDEParserMapping;
import com.elitecore.sm.parser.model.NatflowASN1ParserMapping;
import com.elitecore.sm.parser.model.PDFParserMapping;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.parser.model.RapParserMapping;
import com.elitecore.sm.parser.model.RegExPattern;
import com.elitecore.sm.parser.model.RegexParserAttribute;
import com.elitecore.sm.parser.model.RegexParserMapping;
import com.elitecore.sm.parser.model.SstpXmlParserMapping;
import com.elitecore.sm.parser.model.TapParserMapping;
import com.elitecore.sm.parser.model.VarLengthAsciiParserMapping;
import com.elitecore.sm.parser.model.VarLengthBinaryParserMapping;
import com.elitecore.sm.parser.model.XMLParserMapping;
import com.elitecore.sm.parser.model.XlsParserMapping;
import com.elitecore.sm.parser.validator.ParserMappingValidator;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.util.EliteUtils;
import com.mysql.cj.protocol.a.DebugBufferingPacketReader;

/**
 * @author Ranjitsinh Reval
 *
 */
@Service(value = "parserMappingService")
public class ParserMappingServiceImpl implements ParserMappingService {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	@Qualifier(value = "parserMappingDao")
	private ParserMappingDao parserMappingDao;

	@Autowired
	private ParserAttributeDao parserAttributeDao;

	@Autowired
	private ParserService parserService;

	@Autowired
	private ParserAttributeService parserAttributeService;
	
	@Autowired
	ParserGroupAttributeService parserGroupAttributeService;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private RegExParserService regExParserService;

	@Autowired
	private RegExPatternDao regExPatternDao;

	@Autowired
	private ParserMappingValidator parserMappingValidator;

	@Autowired
	private ComposerDao composerDao;
	
	@Autowired
	private DeviceDao deviceDao;
	
	private String serviceConstant = "parserMappingService";

	/**
	 * Method will fetch all association details for selected mapping
	 * 
	 * @param mappingId
	 * @return ResponseObject
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getMappingAssociationDetails(int mappingId, String decodeType) {

		logger.debug("Going to fetch all association for mapping : " + mappingId);
		logger.debug("Going to fetch all association for decodeType : " + decodeType);

		ResponseObject responseObject = new ResponseObject();

		if (mappingId > 0) {
			if ("DOWNSTREAM".equalsIgnoreCase(decodeType)) {

				List<Composer> composerList = composerDao.getMappingAssociationDetails(mappingId);

				if (composerList != null && !composerList.isEmpty()) {

					responseObject.setObject(iterateMappingAssocitionDetailsForComposer(composerList));

					responseObject.setSuccess(true);
				} else {
					logger.info("Failed t o fetch mapping details for mapping id " + mappingId);

					responseObject.setObject(composerList);
					responseObject.setSuccess(false);
				}
			} else if ("UPSTREAM".equalsIgnoreCase(decodeType)) {

				List<Parser> parserList = parserMappingDao.getMappingAssociationDetails(mappingId);

				if (parserList != null && !parserList.isEmpty()) {

					logger.info(parserList.size() + " association found " + " for mapping " + mappingId);
					responseObject.setObject(iterateMappingAssocitionDetails(parserList)); // It
					// mapping.
					responseObject.setSuccess(true);
				} else {
					logger.info("Failed  to fetch mapping details for mapping id " + mappingId);
					responseObject.setObject(parserList);

					responseObject.setSuccess(false);
				}
			} else {
				logger.info("Failed to fetch mapping details for mapping id " + mappingId);
				responseObject.setObject(null);
				responseObject.setSuccess(false);
			}
		} else {
			logger.info("Failed to fetch mapping details for mapping id " + mappingId);
			responseObject.setObject(null);
			responseObject.setSuccess(false);
		}

		return responseObject;
	}

	/**
	 * Method will iterate parser details and get association name, service,
	 * server instances for the selected mapping.
	 * 
	 * @param parserList
	 * 
	 */
	private JSONArray iterateMappingAssocitionDetails(List<Parser> parserList) {

		JSONArray jsonArray = new JSONArray();

		for (Parser parser : parserList) {

			parser.getParsingPathList().getService().getName();
			parser.getParsingPathList().getService().getServerInstance().getServer().getIpAddress();

			JSONObject jsonObject = new JSONObject();

			jsonObject.put("serverInstanceName", parser.getParsingPathList().getService().getServerInstance().getName());
			jsonObject.put("serverIpAddress", parser.getParsingPathList().getService().getServerInstance().getServer().getIpAddress());
			jsonObject.put("serverInstancePort", parser.getParsingPathList().getService().getServerInstance().getPort());
			jsonObject.put(BaseConstants.SERVICE_NAME, parser.getParsingPathList().getService().getName());
			jsonObject.put("pluginName", parser.getName());
			jsonArray.put(jsonObject);
		}

		return jsonArray;
	}

	/**
	 * Method will iterate composer details and get association name, service,
	 * server instances for the selected mapping.
	 * 
	 * @param composerList
	 * 
	 */
	private JSONArray iterateMappingAssocitionDetailsForComposer(List<Composer> composerList) {

		JSONArray jsonArray = new JSONArray();

		for (Composer composer : composerList) {
			logger.debug("iterateMappingAssocitionDetailsForComposer");

			JSONObject jsonObject = new JSONObject();

			jsonObject.put("serverInstanceName", composer.getMyDistDrvPathlist().getDriver().getService().getServerInstance().getName());
			jsonObject
					.put("serverIpAddress", composer.getMyDistDrvPathlist().getDriver().getService().getServerInstance().getServer().getIpAddress());
			jsonObject.put("serverInstancePort", composer.getMyDistDrvPathlist().getDriver().getService().getServerInstance().getPort());
			jsonObject.put(BaseConstants.SERVICE_NAME, composer.getMyDistDrvPathlist().getDriver().getService().getName());
			jsonObject.put("pluginName", composer.getName());
			jsonArray.put(jsonObject);
		}

		return jsonArray;
	}

	/**
	 * Method will get all mapping details by selected id's using HQL criteria.
	 * 
	 * @param ids
	 * @return responseObject
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getAllMappingById(Integer[] ids) {
		ResponseObject responseObject = new ResponseObject();

		logger.debug("Going to fetch parser mapping list");
		List<ParserMapping> parserMappingList = parserMappingDao.getAllMappingById(ids);

		if (parserMappingList != null && !parserMappingList.isEmpty()) {
			responseObject.setSuccess(true);
			responseObject.setObject(parserMappingList);
			logger.info(parserMappingList.size() + "  Parser mapping list found.");
		} else {
			logger.info("Failed to fetch parser mapping list for id ");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_MAPPING_BY_ID);
		}
		return responseObject;
	}
	
	
	public ResponseObject getAllMappingByDeviceId(Integer[] deviceIds){
		ResponseObject responseObject = new ResponseObject();

		logger.debug("Going to fetch parser mapping list using given device IDs");
		List<ParserMapping> parserMappingList = parserMappingDao.getAllMappingByDeviceId(deviceIds);

		if (parserMappingList != null && !parserMappingList.isEmpty()) {
			responseObject.setSuccess(true);
			responseObject.setObject(parserMappingList);
			logger.info(parserMappingList.size() + "  Parser mapping list found.");
		} else {
			logger.info("Failed to fetch parser mapping list for id ");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_MAPPING_BY_ID);
		}
		return responseObject;
	}

	/**
	 * Method will delete parser mapping details and its associated parser
	 * attributes.
	 * 
	 * @param parserMapping
	 * @param staffId
	 * @return ResponseObject
	 */
	@Override
	public ResponseObject deleteMapping(ParserMapping parserMapping, int staffId) {
		ResponseObject responseObject = new ResponseObject();

		logger.debug("Going to delete parser mapping details.");
		if (parserMapping != null) {
			List<Integer> regexPatternIds = new ArrayList<Integer>();
			//MED-8334  if parserMapping instanceof RegexParserMapping , then from mappiing 1.delete patterns and 2.delete attr from patterns
			if(parserMapping instanceof RegexParserMapping){
				logger.debug("parserMapping is type of RegexParserMapping , going to delete patterns and attributes");
				RegexParserMapping regexParserMapping=(RegexParserMapping)parserMapping;
				if(regexParserMapping.getPatternList()!=null && !regexParserMapping.getPatternList().isEmpty()){
					List<RegExPattern> regExPatternObj=regexParserMapping.getPatternList();
					for(RegExPattern regExPattern : regExPatternObj){
						regexPatternIds.add(regExPattern.getId());
					}
					for (Integer regexPatternId : regexPatternIds) {
						regExParserService.deleteRegExPattern(regexPatternId,staffId);
					}
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.DELETE_MAPPING_SUCCESS);
				}else if(regexParserMapping.getPatternList()==null || regexParserMapping.getPatternList().isEmpty()){
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.DELETE_MAPPING_SUCCESS);
				}
			}else if (parserMapping.getParserAttributes() != null && !parserMapping.getParserAttributes().isEmpty()) {

				logger.debug("Attribute found for parser mapping  " + parserMapping.getParserAttributes().size() + " going to delete all.");

				List<ParserAttribute> attributeList = parserMapping.getParserAttributes();
				int size = attributeList.size();
				for (int i = 0; i < size; i++) {
					parserAttributeService.deleteAttribute(attributeList.get(i).getId(), staffId);
				}
				parserMappingDao.merge(parserMapping);
				logger.info("Parser mapping and its associated parser attribute have been deleted successfully.");
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.DELETE_MAPPING_SUCCESS);
			} else {
				logger.info("Parser mapping details has been deleted successfully.");
				parserMappingDao.merge(parserMapping);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.DELETE_MAPPING_SUCCESS);
			}
		} else {

			logger.info("Failed to delete parser mapping details.");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_DELETE_MAPPING);
		}

		return responseObject;
	}

	/**
	 * Method will get all mapping details for selected device and plug-in
	 * type.(It will display mapping list selected parser plug-in)
	 * 
	 * @param deviceId
	 * @param parserType
	 * @return ResponseObject
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getMappingByDeviceAndParserType(int deviceId, String parserType) {

		logger.debug("Going to fetch all mapping details for  device id " + deviceId + " and  plugin type " + parserType + ".");
		ResponseObject responseObject;

		responseObject = parserService.getPluginByType(parserType);
		if (responseObject.isSuccess()) {
			PluginTypeMaster pluginMaster = (PluginTypeMaster) responseObject.getObject();
			logger.debug("Plugin list found not going to fetch device details for device id " + deviceId);

			if (deviceId > 0) {
				List<ParserMapping> parserMappingList = parserMappingDao.getAllMappingBydeviceAndType(deviceId, pluginMaster.getId());

				if (parserMappingList != null && !parserMappingList.isEmpty()) {
					responseObject.setSuccess(true);
					responseObject.setObject(parserMappingList);
					logger.info(parserMappingList.size() + " mapping list found successfully!");

				} else {
					logger.info("Failed to fetch parser mapping details for device id " + deviceId + " and plug-in type " + parserType);
					responseObject.setSuccess(false);
					responseObject.setObject(null);
					responseObject.setResponseCode(ResponseCode.FAIL_GET_MAPPING_DEVICE_ID);
				}
			} else {
				logger.info("Failed  fetch mapping details due to device id found " + deviceId);
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.FAIL_GET_MAPPING_DEVICE_ID);
			}

		} else {
			logger.info("Failed to get mapping list due not able to get plugin master for parser type " + parserType);
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_MAPPING_DEVICE_ID);
		}

		return responseObject;
	}

	/**
	 * Method will get mapping details by mapping id and plug-in type.
	 * 
	 * @param mappingId
	 * @return ResponseObject
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getMappingDetailsById(int mappingId, String pluginType) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Going to fetch mapping details for mapping id " + mappingId + " and plug in type " + pluginType);

		if (mappingId > 0) {
			ParserMapping parserMapping = parserMappingDao.findByPrimaryKey(ParserMapping.class, mappingId);

			if (parserMapping != null) {
				logger.debug("Parser mapping details found successfully.");

				if (EngineConstants.NATFLOW_PARSING_PLUGIN.equalsIgnoreCase(pluginType)) {
					logger.info("Plug-in type found " + EngineConstants.NATFLOW_PARSING_PLUGIN);
					NATFlowParserMapping natFlowParser = (NATFlowParserMapping) parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(natFlowParser);
				}else if (EngineConstants.REGEX_PARSING_PLUGIN.equalsIgnoreCase(pluginType)) {
					logger.info("Plugin type found " + EngineConstants.REGEX_PARSING_PLUGIN);
					RegexParserMapping regexParser = (RegexParserMapping) parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(regexParser);
				
				}else if(EngineConstants.NATFLOW_ASN_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){

					logger.info("Plugin  type found " + EngineConstants.NATFLOW_ASN_PARSING_PLUGIN);
					NatflowASN1ParserMapping natflowAsnParser = (NatflowASN1ParserMapping) parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(natflowAsnParser);

				} else if (EngineConstants.ASCII_PARSING_PLUGIN.equalsIgnoreCase(pluginType)) {
					logger.info("Plugin type found " + EngineConstants.ASCII_PARSING_PLUGIN);
					AsciiParserMapping asciiParser = (AsciiParserMapping) parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(asciiParser);
				
				}else if(EngineConstants.ASN1_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){
					logger.info("Plugin type found " + EngineConstants.ASN1_PARSING_PLUGIN);
					ASN1ParserMapping asn1Parser = (ASN1ParserMapping) parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(asn1Parser);
					
				}else if(EngineConstants.DETAIL_LOCAL_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){
					logger.info("Plugin type found " + EngineConstants.DETAIL_LOCAL_PARSING_PLUGIN);
					DetailLocalParserMapping detailLocalParser = (DetailLocalParserMapping) parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(detailLocalParser);
					
				}else if(EngineConstants.XML_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){
					logger.info("Plugin type found " + EngineConstants.XML_PARSING_PLUGIN);
					XMLParserMapping xmlParser = (XMLParserMapping) parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(xmlParser);
				}else if (EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){		
					logger.info("Plugin type found " + EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN);
					FixedLengthASCIIParserMapping fixedLengthASCIIParserMapping = (FixedLengthASCIIParserMapping) parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(fixedLengthASCIIParserMapping);
				}
				else if (EngineConstants.RAP_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){		
					logger.info("Plugin type found " + EngineConstants.RAP_PARSING_PLUGIN);
					RapParserMapping rapParserMapping = (RapParserMapping) parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(rapParserMapping);
				}else if (EngineConstants.TAP_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){		
					logger.info("Plugin type found " + EngineConstants.TAP_PARSING_PLUGIN);
					TapParserMapping tapParserMapping = (TapParserMapping) parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(tapParserMapping);
				}else if (EngineConstants.NRTRDE_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){		
					logger.info("Plugin type found " + EngineConstants.NRTRDE_PARSING_PLUGIN);
					NRTRDEParserMapping nrtrdeParserMapping = (NRTRDEParserMapping) parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(nrtrdeParserMapping);
				}else if (EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){
					logger.info("Plugin type found " + EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN);
					FixedLengthBinaryParserMapping fixedLengthBinaryParserMapping=(FixedLengthBinaryParserMapping)parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(fixedLengthBinaryParserMapping);
				} else if (EngineConstants.HTML_PARSING_PLUGIN.equalsIgnoreCase(pluginType)) {
					logger.info("Plugin type found " + EngineConstants.HTML_PARSING_PLUGIN);
					HtmlParserMapping htmlParser = (HtmlParserMapping) parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(htmlParser);
				}else if (EngineConstants.PDF_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){
					logger.info("Plugin type found " + EngineConstants.PDF_PARSING_PLUGIN);
					PDFParserMapping pdfParserMapping=(PDFParserMapping)parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(pdfParserMapping);
				}else if (EngineConstants.XLS_PARSING_PLUGIN.equalsIgnoreCase(pluginType)) {
					logger.info("Plugin type found " + EngineConstants.XLS_PARSING_PLUGIN);
					XlsParserMapping xlsParser = (XlsParserMapping) parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(xlsParser);
				}else if (EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN.equalsIgnoreCase(pluginType)) {
					logger.info("Plugin type found " + EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN);
					VarLengthAsciiParserMapping varLengthAsciiParserMapping = (VarLengthAsciiParserMapping) parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(varLengthAsciiParserMapping);
				}else if (EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equalsIgnoreCase(pluginType)) {
					logger.info("Plugin type found " + EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN);
					VarLengthBinaryParserMapping varLengthBinaryParserMapping = (VarLengthBinaryParserMapping) parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(varLengthBinaryParserMapping);
				} else if (EngineConstants.JSON_PARSING_PLUGIN.equalsIgnoreCase(pluginType)) {
					logger.info("Plugin type found " + EngineConstants.JSON_PARSING_PLUGIN);
					JsonParserMapping jsonParser = (JsonParserMapping) parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(jsonParser);
				
				} else if (EngineConstants.MTSIEMENS_BINARY_PARSING_PLUGIN.equalsIgnoreCase(pluginType)) {
					logger.info("Plugin type found " + EngineConstants.MTSIEMENS_BINARY_PARSING_PLUGIN);
					MTSiemensParserMapping mtsiemensBinaryParser = (MTSiemensParserMapping) parserMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(mtsiemensBinaryParser);
				
				}
				
			} else {
				logger.info("Failed to fetch parser mapping details for mapping id " + mappingId + " and plugin type " + pluginType
						+ " as parser mapping object found " + parserMapping);
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.FAIL_GET_MAPPING_BY_ID);
			}

		} else if (mappingId == -2) { // Front end option will selected to
										// CUSTOM so need provide default data
										// for this as there is not CUSTOME
										// mapping in database.

			if (EngineConstants.NATFLOW_PARSING_PLUGIN.equalsIgnoreCase(pluginType)) {

				logger.info("Setting default values for plugin " + EngineConstants.NATFLOW_ASN_PARSING_PLUGIN);
				NATFlowParserMapping natFlowParser = new NATFlowParserMapping();
				natFlowParser.setReadTemplatesInitially(true);
				natFlowParser.setMappingType(BaseConstants.USER_DEFINED_MAPPING); // setting
																					// 1
																					// for
																					// user
																					// defined
																					// mapping
				responseObject.setSuccess(true);
				responseObject.setObject(natFlowParser);

			} else if (EngineConstants.NATFLOW_ASN_PARSING_PLUGIN.equalsIgnoreCase(pluginType)) {
				logger.info("Setting default values for plugin " + EngineConstants.NATFLOW_ASN_PARSING_PLUGIN);
				NatflowASN1ParserMapping natFlowASNParser = new NatflowASN1ParserMapping();
				natFlowASNParser.setReadTemplatesInitially(true);
				natFlowASNParser.setMappingType(BaseConstants.USER_DEFINED_MAPPING); // setting
																						// 1
																						// for
																						// user
																						// defined
																						// mapping
				responseObject.setSuccess(true);
				responseObject.setObject(natFlowASNParser);
			} else if (EngineConstants.REGEX_PARSING_PLUGIN.equalsIgnoreCase(pluginType)) {
				RegexParserMapping regexParserMapping = new RegexParserMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(regexParserMapping);
			}  else if (EngineConstants.ASCII_PARSING_PLUGIN.equalsIgnoreCase(pluginType)) {
				AsciiParserMapping asciiParserMapping = new AsciiParserMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(asciiParserMapping);
			} else if (EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN.equalsIgnoreCase(pluginType)) {
				VarLengthAsciiParserMapping vLengthAsciiParserMapping = new VarLengthAsciiParserMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(vLengthAsciiParserMapping);
			} else if(EngineConstants.ASN1_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){
				ASN1ParserMapping asn1ParserMapping = new ASN1ParserMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(asn1ParserMapping);
			} else if(EngineConstants.DETAIL_LOCAL_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){
				DetailLocalParserMapping detailLocalParserMapping = new DetailLocalParserMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(detailLocalParserMapping);
			} else if(EngineConstants.XML_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){
				XMLParserMapping xmlParserMapping = new XMLParserMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(xmlParserMapping);
			} else if(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){
				FixedLengthASCIIParserMapping fixedLengthASCIIParserMapping = new FixedLengthASCIIParserMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(fixedLengthASCIIParserMapping);
			}else if (EngineConstants.RAP_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){		
				logger.info("Plugin type found " + EngineConstants.RAP_PARSING_PLUGIN);
				RapParserMapping rapParserMapping = new RapParserMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(rapParserMapping);
			}else if (EngineConstants.TAP_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){		
				logger.info("Plugin type found " + EngineConstants.TAP_PARSING_PLUGIN);
				TapParserMapping tapParserMapping = new TapParserMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(tapParserMapping);
			}else if (EngineConstants.NRTRDE_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){		
				logger.info("Plugin type found " + EngineConstants.NRTRDE_PARSING_PLUGIN);
				NRTRDEParserMapping nrtedeParserMapping = new NRTRDEParserMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(nrtedeParserMapping);
			}else if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){
				logger.info("Plugin type found " + EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN);
                FixedLengthBinaryParserMapping fixedLengthBinaryParserMapping=new FixedLengthBinaryParserMapping();
                responseObject.setSuccess(true);
				responseObject.setObject(fixedLengthBinaryParserMapping);
			}else if(EngineConstants.PDF_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){
				logger.info("Plugin type found " + EngineConstants.PDF_PARSING_PLUGIN);
                PDFParserMapping pdfParserMapping=new PDFParserMapping();
                responseObject.setSuccess(true);
				responseObject.setObject(pdfParserMapping);
			}else if(EngineConstants.HTML_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){
				logger.info("Plugin type found " + EngineConstants.HTML_PARSING_PLUGIN);
                HtmlParserMapping htmlParserMapping=new HtmlParserMapping();
                responseObject.setSuccess(true);
				responseObject.setObject(htmlParserMapping);
			}else if(EngineConstants.XLS_PARSING_PLUGIN.equalsIgnoreCase(pluginType)){
				logger.info("Plugin type found " + EngineConstants.XLS_PARSING_PLUGIN);
                XlsParserMapping xlsParserMapping=new XlsParserMapping();
                responseObject.setSuccess(true);
				responseObject.setObject(xlsParserMapping);
			}else if (EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equalsIgnoreCase(pluginType)) {
				logger.info("Plugin type found " + EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN);
				VarLengthBinaryParserMapping varLengthBinaryParserMapping = new VarLengthBinaryParserMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(varLengthBinaryParserMapping);
			}else if (EngineConstants.JSON_PARSING_PLUGIN.equalsIgnoreCase(pluginType)) {
				JsonParserMapping jsonParserMapping = new JsonParserMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(jsonParserMapping);
			}else if (EngineConstants.MTSIEMENS_BINARY_PARSING_PLUGIN.equalsIgnoreCase(pluginType)) {
				MTSiemensParserMapping mtsiemensBinaryParserMapping = new MTSiemensParserMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(mtsiemensBinaryParserMapping);
			}
		} else {
			logger.info("Failed to fetch parser mapping details for mapping id " + mappingId + " and plugin type " + pluginType);
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_MAPPING_BY_ID);
		}
		return responseObject;
	}

	/**
	 * Method will update parser mapping details based on the parser type and
	 * associate it with parser.
	 * 
	 * @param parserMapping
	 * @param parserType
	 * @return ResponseObject
	 */
	@SuppressWarnings("unused")
	@Transactional
	@Override
	public ResponseObject updateAndAssociateParserMapping(ParserMapping parserMapping, String pluginType, int plugInId, String actionType, int staffId) {
		logger.debug("Going to update plugin association with parser mapping.");
		ResponseObject responseObject = new ResponseObject();

		if (actionType != null && "NO_ACTION".equals(actionType)) {
			if (parserMapping.getId() > 0) {
				logger.debug("Customization option is not selected so going to update simple association with selected plugin with selected mapping.");
				responseObject = updateParserMappingDetails(parserMapping.getId(), plugInId, staffId);
				if (responseObject.isSuccess()) {
					logger.debug("Parser is associated with selected parser mapping successfully.");
				}
			} else {
				logger.debug("Failed to update mapping details as mapping id found " + parserMapping.getId());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PARSER_UPDATE_FAIL);
			}
		} else if (actionType != null && "UPDATE".equals(actionType)) {
			if (parserMapping.getId() > 0) {

				if (!EngineConstants.REGEX_PARSING_PLUGIN.equals(pluginType)) {
					ParserMapping parserMappingDetails = parserMappingDao.findByPrimaryKey(ParserMapping.class, parserMapping.getId());
					if (parserMappingDetails != null) {
						updateMappingDetails(parserMapping, parserMappingDetails, staffId);
					} else {
						logger.debug("Failed to update parser mapping details due to parserMappingDetails found " + parserMappingDetails
								+ " for mapping id " + parserMapping.getId());
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.PARSER_UPDATE_FAIL);
					}
				}
				responseObject = updateParserMappingDetails(parserMapping.getId(), plugInId, staffId);
				logger.debug("Mapping and plugin association has been updated successfully.");

			} else {
				logger.debug("Failed to update parser mapping details due to mapping id found " + parserMapping.getId());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PARSER_UPDATE_FAIL);
			}
		} else {
			logger.debug("Failed to update parser mapping details as current action type not found.");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PARSER_UPDATE_FAIL);
		}

		return responseObject;
	}

	/**
	 * Method will update mapping details for select mapping.
	 * 
	 * @param mappingId
	 * @param plugInId
	 * @param staffId
	 * @return ResponseObject
	 */
	private ResponseObject updateParserMappingDetails(int mappingId, int plugInId, int staffId) {
		ResponseObject responseObject = new ResponseObject();

		logger.debug("Going to update parser with selected mapping.");
		ParserMapping parserMappingDetails = parserMappingDao.findByPrimaryKey(ParserMapping.class, mappingId);

		
		if (parserMappingDetails != null && plugInId > 0) {
			Parser parser = parserService.getParserById(plugInId);

			if (parser != null) {
				parser.setParserMapping(parserMappingDetails);
				parser.setLastUpdatedDate(new Date());
				parser.setLastUpdatedByStaffId(staffId);
				responseObject = parserService.updateParserMapping(parser);
				if (responseObject.isSuccess()) {
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.PARSER_UPDATE_SUCCESS);
					logger.info("Parser mapping is associated successfully with selected plugin.");

				} else {
					logger.info("Failed to update parser details for selected mapping.");
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.PARSER_UPDATE_FAIL);
				}
			} else {
				logger.info("Failed to update parser details for selected mapping.");
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PARSER_UPDATE_FAIL);
			}
		} else {
			logger.info("Failed to associate parser as parser mapping object found " + parserMappingDetails + " for id " + mappingId);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PARSER_UPDATE_FAIL);
		}
		return responseObject;
	}

	/**
	 * Method will update mapping details for selected plugin type.
	 * 
	 * @param newParserMapping
	 * @param dbParserMapping
	 * @param staffId
	 */
	private void updateMappingDetails(ParserMapping newParserMapping, ParserMapping dbParserMapping, int staffId) {
		logger.debug("Going to update mapping details.");

		NATFlowParserMapping newNatflowParserMapping ;
		NATFlowParserMapping dbNatflowParserMapping ;
		AsciiParserMapping  newasciiParserMapping;
		AsciiParserMapping  dbasciiParserMapping;
		DetailLocalParserMapping  newdetailLocalParserMapping;
		DetailLocalParserMapping  dbdetailLocalParserMapping;
		ASN1ParserMapping  newasn1ParserMapping;
		ASN1ParserMapping  dbasn1ParserMapping;
		XMLParserMapping  newxmlParserMapping;
		XMLParserMapping  dbxmlParserMapping;
		RapParserMapping newrapParserMapping;
		RapParserMapping dbrapParserMapping;
		TapParserMapping newtapParserMapping;
		TapParserMapping dbtapParserMapping;
		NRTRDEParserMapping newnrtrdeParserMapping;
		NRTRDEParserMapping dbnrtrdeParserMapping;
		FixedLengthASCIIParserMapping newFixedLengthASCIIParserMapping;
		FixedLengthASCIIParserMapping dbFixedLengthASCIIParserMapping;
		FixedLengthBinaryParserMapping newfixedLengthBinaryParserMapping;
		FixedLengthBinaryParserMapping dbfixedLengthBinaryParserMapping;
		HtmlParserMapping  newhtmlParserMapping;
		HtmlParserMapping  dbhtmlParserMapping;
		PDFParserMapping newPDFParserMapping;
		PDFParserMapping dbPDFParserMapping;
		XlsParserMapping  newXlsParserMapping;
		XlsParserMapping  dbXlsParserMapping;
		VarLengthAsciiParserMapping newvarLengthAsciiParserMapping;
		VarLengthAsciiParserMapping dbvarLengthAsciiParserMapping;
		VarLengthBinaryParserMapping newvarLengthBinaryParserMapping;
		VarLengthBinaryParserMapping dbvarLengthBinaryParserMapping;
		dbParserMapping.setLastUpdatedByStaffId(staffId);

		ParserMappingService mappingServiceImpl = (ParserMappingService) SpringApplicationContext.getBean(serviceConstant);
		if (newParserMapping instanceof NatflowASN1ParserMapping) {
			logger.info("Plugin type found NatflowASN1Parser. ");
			newNatflowParserMapping = (NatflowASN1ParserMapping) newParserMapping;
			dbNatflowParserMapping = (NatflowASN1ParserMapping) dbParserMapping;
			mappingServiceImpl.setAndUpdateNetflowParserDetail(newNatflowParserMapping, dbNatflowParserMapping);
		} else if (newParserMapping instanceof NATFlowParserMapping) {
			logger.info("Plugin type found NATFlowParserMapping. ");
			newNatflowParserMapping = (NATFlowParserMapping) newParserMapping;
			dbNatflowParserMapping = (NATFlowParserMapping) dbParserMapping;
			mappingServiceImpl.setAndUpdateNetflowParserDetail(newNatflowParserMapping, dbNatflowParserMapping);

		} else if (newParserMapping instanceof AsciiParserMapping) {
			logger.info("Plugin type found AsciiParserMapping. ");

			newasciiParserMapping = (AsciiParserMapping) newParserMapping;
			dbasciiParserMapping = (AsciiParserMapping) dbParserMapping;
			mappingServiceImpl.setAndUpdateAsciiParserDetail(newasciiParserMapping, dbasciiParserMapping);
		
		} else if (newParserMapping instanceof DetailLocalParserMapping) {
			logger.info("Plugin type found DetailLocaliParserMapping. ");

			newdetailLocalParserMapping = (DetailLocalParserMapping) newParserMapping;
			dbdetailLocalParserMapping = (DetailLocalParserMapping) dbParserMapping;
			mappingServiceImpl.setAndUpdateDetailLocalParserDetail(newdetailLocalParserMapping, dbdetailLocalParserMapping);
		
		} else if (newParserMapping instanceof VarLengthAsciiParserMapping) {
			logger.info("Plugin type found VarLengthAsciiParserMapping. ");

			newvarLengthAsciiParserMapping = (VarLengthAsciiParserMapping) newParserMapping;
			dbvarLengthAsciiParserMapping = (VarLengthAsciiParserMapping) dbParserMapping;
			mappingServiceImpl.setAndUpdateVarLengthAsciiParserDetail(newvarLengthAsciiParserMapping, dbvarLengthAsciiParserMapping);
		
		} else if(newParserMapping instanceof ASN1ParserMapping){
			logger.info("Plugin type found AsciiParserMapping. ");
			
			newasn1ParserMapping = (ASN1ParserMapping) newParserMapping;
			dbasn1ParserMapping  = (ASN1ParserMapping) dbParserMapping;
			List<ParserAttribute> parserAttriList = dbasn1ParserMapping.getParserAttributes();
			String newRecMainAttribute = ((ASN1ParserMapping) newParserMapping).getRecMainAttribute();
			if(newRecMainAttribute.contains(".")){
				newRecMainAttribute = newRecMainAttribute.substring(0,newRecMainAttribute.lastIndexOf("."));
				for(ParserAttribute parserAttribute : parserAttriList){
					try{
						ASN1ParserAttribute asn1ParserAttribute = (ASN1ParserAttribute)parserAttribute; 
						String asn1Type = asn1ParserAttribute.getASN1DataType();
						if(asn1Type!=null && asn1Type.contains(BaseConstants.REC_MAIN_ATTR_SYMBOL)){
							String oldRecMainAttribute = asn1Type.substring(asn1Type.indexOf(BaseConstants.REC_MAIN_ATTR_SYMBOL)+BaseConstants.REC_MAIN_ATTR_SYMBOL.length());
							asn1ParserAttribute.setASN1DataType(newRecMainAttribute+oldRecMainAttribute);
						}
					} catch(Exception e) {
						logger.error(e);
						logger.error("Error during replacing '$$$' in asn1parser attribute.");
					}
				}
			}
			setAndUpdateAsn1ParserDetail(newasn1ParserMapping,dbasn1ParserMapping);
		} else if(newParserMapping instanceof XMLParserMapping){
			logger.info("Plugin type found XMLParserMapping. ");
			
			newxmlParserMapping = (XMLParserMapping) newParserMapping;
			dbxmlParserMapping  = (XMLParserMapping) dbParserMapping;
			mappingServiceImpl.setAndUpdateXMLParserDetail(newxmlParserMapping,dbxmlParserMapping);
		}
		else if(newParserMapping instanceof FixedLengthASCIIParserMapping){
			logger.info("Plugin type found FixedLengthASCIIParser");
			
			newFixedLengthASCIIParserMapping = (FixedLengthASCIIParserMapping) newParserMapping;
			dbFixedLengthASCIIParserMapping = (FixedLengthASCIIParserMapping) dbParserMapping;
			mappingServiceImpl.setAndUpdateFixedLengthASCIIParserDetail(newFixedLengthASCIIParserMapping,dbFixedLengthASCIIParserMapping);
		}
		else if(newParserMapping instanceof RapParserMapping){
			logger.info("Plugin type found RapParserMapping. ");
			newrapParserMapping = (RapParserMapping) newParserMapping;
			dbrapParserMapping  = (RapParserMapping) dbParserMapping;
			mappingServiceImpl.setAndUpdateRapParserDetail(newrapParserMapping,dbrapParserMapping);
		}
		else if(newParserMapping instanceof TapParserMapping){
			logger.info("Plugin type found TapParserMapping. ");
			newtapParserMapping = (TapParserMapping) newParserMapping;
			dbtapParserMapping  = (TapParserMapping) dbParserMapping;
			mappingServiceImpl.setAndUpdateTapParserDetail(newtapParserMapping,dbtapParserMapping);
		}else if(newParserMapping instanceof NRTRDEParserMapping){
			logger.info("Plugin type found NRTRDEParserMapping. ");
			newnrtrdeParserMapping = (NRTRDEParserMapping) newParserMapping;
			dbnrtrdeParserMapping  = (NRTRDEParserMapping) dbParserMapping;
			mappingServiceImpl.setAndUpdateNrtrdeParserDetail(newnrtrdeParserMapping,dbnrtrdeParserMapping);
		} else if(newParserMapping instanceof FixedLengthBinaryParserMapping){
			logger.info("Plugin type found FixedLengthBinaryParserMapping.");
			newfixedLengthBinaryParserMapping= (FixedLengthBinaryParserMapping)newParserMapping;
			dbfixedLengthBinaryParserMapping= (FixedLengthBinaryParserMapping)dbParserMapping;
			mappingServiceImpl.setAndUpdateFixedLengthBinaryParserDetail(newfixedLengthBinaryParserMapping, dbfixedLengthBinaryParserMapping);
		}else if(newParserMapping instanceof HtmlParserMapping){
			logger.info("Plugin type found XMLParserMapping. ");
			newhtmlParserMapping = (HtmlParserMapping) newParserMapping;
			dbhtmlParserMapping  = (HtmlParserMapping) dbParserMapping;
			mappingServiceImpl.setAndUpdateHtmlParserDetail(newhtmlParserMapping,dbhtmlParserMapping);
		}else if(newParserMapping instanceof XlsParserMapping){
			logger.info("Plugin type found XLSParserMapping. ");
			newXlsParserMapping = (XlsParserMapping) newParserMapping;
			dbXlsParserMapping  = (XlsParserMapping) dbParserMapping;
			mappingServiceImpl.setAndUpdateXlsParserDetail(newXlsParserMapping,dbXlsParserMapping);
		}else if(newParserMapping instanceof PDFParserMapping){
			logger.info("Plugin type found PDFParserMapping.");
			newPDFParserMapping= (PDFParserMapping)newParserMapping;
			dbPDFParserMapping= (PDFParserMapping)dbParserMapping;
			setAndUpdatePDFParserDetail(newPDFParserMapping, dbPDFParserMapping);
		}else if(newParserMapping instanceof HtmlParserMapping){//NOSONAR
			logger.info("Plugin type found HtmlParserMapping.");
			newhtmlParserMapping= (HtmlParserMapping)newParserMapping;
			dbhtmlParserMapping= (HtmlParserMapping)dbParserMapping;
			mappingServiceImpl.setAndUpdateHtmlParserDetail(newhtmlParserMapping, dbhtmlParserMapping);
		} else if (newParserMapping instanceof VarLengthBinaryParserMapping) {
			logger.info("Plugin type found VarLengthBinaryParserMapping. ");
			newvarLengthBinaryParserMapping = (VarLengthBinaryParserMapping) newParserMapping;
			dbvarLengthBinaryParserMapping = (VarLengthBinaryParserMapping) dbParserMapping;
			mappingServiceImpl.setAndUpdateVarLengthBinaryParserDetail(newvarLengthBinaryParserMapping, dbvarLengthBinaryParserMapping);
		}

	}
	
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class,
	ignorePropList = "parserAttributes,parserWrapper,device,parserType,name,dateFormat,srcDateFormat,srcCharSetName")
	public ResponseObject setAndUpdateVarLengthBinaryParserDetail(
			VarLengthBinaryParserMapping newVarLengthBinaryParserMapping,
			VarLengthBinaryParserMapping dbVarLengthBinaryParserMapping) {
		ResponseObject responseObject = new ResponseObject();
		dbVarLengthBinaryParserMapping.setSkipFileHeader(newVarLengthBinaryParserMapping.isSkipFileHeader());
		dbVarLengthBinaryParserMapping.setSkipSubFileHeader(newVarLengthBinaryParserMapping.isSkipSubFileHeader());
		dbVarLengthBinaryParserMapping.setFileHeaderSize(newVarLengthBinaryParserMapping.getFileHeaderSize());
		dbVarLengthBinaryParserMapping.setSubFileHeaderSize(newVarLengthBinaryParserMapping.getSubFileHeaderSize());
		dbVarLengthBinaryParserMapping.setSubFileLength(newVarLengthBinaryParserMapping.getSubFileLength());
		dbVarLengthBinaryParserMapping.setExtractionRuleKey(newVarLengthBinaryParserMapping.getExtractionRuleKey());
		dbVarLengthBinaryParserMapping.setExtractionRuleValue(newVarLengthBinaryParserMapping.getExtractionRuleValue());
		dbVarLengthBinaryParserMapping.setRecordLengthAttribute(newVarLengthBinaryParserMapping.getRecordLengthAttribute());
		dbVarLengthBinaryParserMapping.setDataDefinitionPath(newVarLengthBinaryParserMapping.getDataDefinitionPath());
		parserMappingDao.merge(dbVarLengthBinaryParserMapping);
		responseObject.setSuccess(true);
		return responseObject;
	}
	
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class,
			ignorePropList = "parserAttributes,parserWrapper,device,name,parserType,dateFormat,groupAttributeList,srcDateFormat")
	private void setAndUpdateAsn1ParserDetail(ASN1ParserMapping newasn1ParserMapping,
			ASN1ParserMapping dbasn1ParserMapping) {
		logger.debug("Enter into the Set and Update ASN1 Parsing plugin");

		dbasn1ParserMapping.setHeaderOffset(newasn1ParserMapping.getHeaderOffset());
		dbasn1ParserMapping.setRecMainAttribute(newasn1ParserMapping.getRecMainAttribute());
		dbasn1ParserMapping.setRecOffset(newasn1ParserMapping.getRecOffset());
		dbasn1ParserMapping.setRemoveAddByte(newasn1ParserMapping.isRemoveAddByte());
		dbasn1ParserMapping.setRemoveFillers(newasn1ParserMapping.getRemoveFillers());
		dbasn1ParserMapping.setRemoveAddHeaderFooter(newasn1ParserMapping.isRemoveAddHeaderFooter());
		dbasn1ParserMapping.setRecordStartIds(newasn1ParserMapping.getRecordStartIds());
		dbasn1ParserMapping.setSkipAttributeMapping(newasn1ParserMapping.isSkipAttributeMapping());
		dbasn1ParserMapping.setDecodeFormat(newasn1ParserMapping.getDecodeFormat());
		dbasn1ParserMapping.setRootNodeName(newasn1ParserMapping.getRootNodeName());
		dbasn1ParserMapping.setBufferSize(newasn1ParserMapping.getBufferSize());
		parserMappingDao.merge(dbasn1ParserMapping);
	}
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class,
			ignorePropList = "parserAttributes,parserWrapper,device,name,parserType,dateFormat,groupAttributeList,srcDateFormat")
   public ResponseObject setAndUpdateRapParserDetail(RapParserMapping newrapParserMapping,
			RapParserMapping dbrapParserMapping) {
		logger.debug("Enter into the Set and Update RAP  Parsing plugin");
		ResponseObject responseObject = new ResponseObject();
		dbrapParserMapping.setHeaderOffset(newrapParserMapping.getHeaderOffset());
		dbrapParserMapping.setRecMainAttribute(newrapParserMapping.getRecMainAttribute());
		dbrapParserMapping.setRecOffset(newrapParserMapping.getRecOffset());
		dbrapParserMapping.setRemoveAddByte(newrapParserMapping.isRemoveAddByte());
		dbrapParserMapping.setRemoveFillers(newrapParserMapping.getRemoveFillers());
		dbrapParserMapping.setRemoveAddHeaderFooter(newrapParserMapping.isRemoveAddHeaderFooter());
		dbrapParserMapping.setRecordStartIds(newrapParserMapping.getRecordStartIds());
		dbrapParserMapping.setSkipAttributeMapping(newrapParserMapping.isSkipAttributeMapping());
		dbrapParserMapping.setDecodeFormat(newrapParserMapping.getDecodeFormat());
		dbrapParserMapping.setRootNodeName(newrapParserMapping.getRootNodeName());
		dbrapParserMapping.setBufferSize(newrapParserMapping.getBufferSize());
		parserMappingDao.merge(dbrapParserMapping);
		responseObject.setSuccess(true);
		return responseObject;
		
	}
	
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class,
			ignorePropList = "parserAttributes,parserWrapper,device,name,parserType,dateFormat,groupAttributeList,srcDateFormat")
	public ResponseObject setAndUpdateDetailLocalParserDetail(DetailLocalParserMapping newdetailLocalParserMapping,
			DetailLocalParserMapping dbdetailLocalParserMapping) {
		// TODO Auto-generated method stub
		logger.debug("Enter into the Set and Update DetailLocal Parsing plugin");
		ResponseObject responseObject = new ResponseObject();
		dbdetailLocalParserMapping.setAttributeSeperator(newdetailLocalParserMapping.getAttributeSeperator());
		dbdetailLocalParserMapping.setVendorNameSeparatorEnable(newdetailLocalParserMapping.isVendorNameSeparatorEnable());
		dbdetailLocalParserMapping.setVendorSeparatorValue(newdetailLocalParserMapping.getVendorSeparatorValue());
		
		parserMappingDao.merge(dbdetailLocalParserMapping);
		responseObject.setSuccess(true);
		return responseObject;
		
		/*
		dbasn1ParserMapping.setHeaderOffset(newasn1ParserMapping.getHeaderOffset());
		dbasn1ParserMapping.setRecMainAttribute(newasn1ParserMapping.getRecMainAttribute());
		dbasn1ParserMapping.setRecOffset(newasn1ParserMapping.getRecOffset());
		dbasn1ParserMapping.setRemoveAddByte(newasn1ParserMapping.isRemoveAddByte());
		dbasn1ParserMapping.setRemoveFillers(newasn1ParserMapping.getRemoveFillers());
		dbasn1ParserMapping.setRemoveAddHeaderFooter(newasn1ParserMapping.isRemoveAddHeaderFooter());
		dbasn1ParserMapping.setRecordStartIds(newasn1ParserMapping.getRecordStartIds());
		dbasn1ParserMapping.setSkipAttributeMapping(newasn1ParserMapping.isSkipAttributeMapping());
		dbasn1ParserMapping.setDecodeFormat(newasn1ParserMapping.getDecodeFormat());
		dbasn1ParserMapping.setRootNodeName(newasn1ParserMapping.getRootNodeName());
		dbasn1ParserMapping.setBufferSize(newasn1ParserMapping.getBufferSize());
		parserMappingDao.merge(dbasn1ParserMapping);
		
		dbrapParserMapping.setRecMainAttribute(newrapParserMapping.getRecMainAttribute());
		dbrapParserMapping.setRecOffset(newrapParserMapping.getRecOffset());
		dbrapParserMapping.setRemoveAddByte(newrapParserMapping.isRemoveAddByte());
		dbrapParserMapping.setRemoveFillers(newrapParserMapping.getRemoveFillers());
		dbrapParserMapping.setRemoveAddHeaderFooter(newrapParserMapping.isRemoveAddHeaderFooter());
		dbrapParserMapping.setRecordStartIds(newrapParserMapping.getRecordStartIds());
		dbrapParserMapping.setSkipAttributeMapping(newrapParserMapping.isSkipAttributeMapping());
		dbrapParserMapping.setDecodeFormat(newrapParserMapping.getDecodeFormat());
		dbrapParserMapping.setRootNodeName(newrapParserMapping.getRootNodeName());
		dbrapParserMapping.setBufferSize(newrapParserMapping.getBufferSize());
		parserMappingDao.merge(dbrapParserMapping);
		responseObject.setSuccess(true);
		return responseObject;
		*/
	}
	
	
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class,
			ignorePropList = "parserAttributes,parserWrapper,device,name,parserType,dateFormat,groupAttributeList,srcDateFormat")
	public ResponseObject setAndUpdateTapParserDetail(TapParserMapping newtapParserMapping,
			TapParserMapping dbtapParserMapping) {
		logger.debug("Enter into the Set and Update TAP  Parsing plugin");
		ResponseObject responseObject = new ResponseObject();
		dbtapParserMapping.setHeaderOffset(newtapParserMapping.getHeaderOffset());
		dbtapParserMapping.setRecMainAttribute(newtapParserMapping.getRecMainAttribute());
		dbtapParserMapping.setRecOffset(newtapParserMapping.getRecOffset());
		dbtapParserMapping.setRemoveAddByte(newtapParserMapping.isRemoveAddByte());
		dbtapParserMapping.setRemoveFillers(newtapParserMapping.getRemoveFillers());
		dbtapParserMapping.setRemoveAddHeaderFooter(newtapParserMapping.isRemoveAddHeaderFooter());
		dbtapParserMapping.setRecordStartIds(newtapParserMapping.getRecordStartIds());
		dbtapParserMapping.setSkipAttributeMapping(newtapParserMapping.isSkipAttributeMapping());
		dbtapParserMapping.setDecodeFormat(newtapParserMapping.getDecodeFormat());
		dbtapParserMapping.setRootNodeName(newtapParserMapping.getRootNodeName());
		dbtapParserMapping.setBufferSize(newtapParserMapping.getBufferSize());
		parserMappingDao.merge(dbtapParserMapping);
		responseObject.setSuccess(true);
		return responseObject;
	}
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class,
			ignorePropList = "parserAttributes,parserWrapper,device,name,parserType,dateFormat,groupAttributeList,srcDateFormat")
	public ResponseObject setAndUpdateNrtrdeParserDetail(NRTRDEParserMapping newnrtrdeParserMapping,
			NRTRDEParserMapping dbnrtrdeParserMapping) {
		logger.debug("Enter into the Set and Update NRTRDE Parsing plugin");
		ResponseObject responseObject = new ResponseObject();
		dbnrtrdeParserMapping.setHeaderOffset(newnrtrdeParserMapping.getHeaderOffset());
		dbnrtrdeParserMapping.setRecMainAttribute(newnrtrdeParserMapping.getRecMainAttribute());
		dbnrtrdeParserMapping.setRecOffset(newnrtrdeParserMapping.getRecOffset());
		dbnrtrdeParserMapping.setRemoveAddByte(newnrtrdeParserMapping.isRemoveAddByte());
		dbnrtrdeParserMapping.setRemoveFillers(newnrtrdeParserMapping.getRemoveFillers());
		dbnrtrdeParserMapping.setRemoveAddHeaderFooter(newnrtrdeParserMapping.isRemoveAddHeaderFooter());
		dbnrtrdeParserMapping.setRecordStartIds(newnrtrdeParserMapping.getRecordStartIds());
		dbnrtrdeParserMapping.setSkipAttributeMapping(newnrtrdeParserMapping.isSkipAttributeMapping());
		dbnrtrdeParserMapping.setDecodeFormat(newnrtrdeParserMapping.getDecodeFormat());
		dbnrtrdeParserMapping.setRootNodeName(newnrtrdeParserMapping.getRootNodeName());
		dbnrtrdeParserMapping.setBufferSize(newnrtrdeParserMapping.getBufferSize());
		parserMappingDao.merge(dbnrtrdeParserMapping);
		responseObject.setSuccess(true);
		return responseObject;
	}
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class,
			ignorePropList = "parserAttributes,parserWrapper,device,parserType,name,dateFormat,srcDateFormat,srcCharSetName,groupAttributeList")
	public ResponseObject setAndUpdateFixedLengthASCIIParserDetail(FixedLengthASCIIParserMapping newFixedLengthASCIIParser,
			FixedLengthASCIIParserMapping dbFixedLengthASCIIParser){
		logger.debug("Inside Set and update fixed Length ascii parser");
		ResponseObject responseObject = new ResponseObject();
		dbFixedLengthASCIIParser.setFileHeaderEnable(newFixedLengthASCIIParser.getFileHeaderEnable());
		dbFixedLengthASCIIParser.setFileHeaderContainsFields(newFixedLengthASCIIParser.getFileHeaderContainsFields());
		parserMappingDao.merge(dbFixedLengthASCIIParser);
		responseObject.setSuccess(true);
		return responseObject;
	}
	
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class,
	ignorePropList = "parserAttributes,parserWrapper,device,parserType,name,dateFormat,srcDateFormat,srcCharSetName")
	public ResponseObject setAndUpdateFixedLengthBinaryParserDetail(
			FixedLengthBinaryParserMapping newfixedLengthBinaryParserMapping,
			FixedLengthBinaryParserMapping dbfixedLengthBinaryParserMapping) {
		ResponseObject responseObject = new ResponseObject();
		parserMappingDao.merge(dbfixedLengthBinaryParserMapping);
		responseObject.setSuccess(true);
		return responseObject;
	}
	
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class,
	ignorePropList = "parserAttributes,parserWrapper,device,parserType,name,dateFormat,srcDateFormat,srcCharSetName")
	public ResponseObject setAndUpdatePDFParserDetail(
			PDFParserMapping newPDFParserMapping,
			PDFParserMapping dbPDFParserMapping) {
		ResponseObject responseObject = new ResponseObject();
		dbPDFParserMapping.setRecordWisePdfFormat(newPDFParserMapping.isRecordWisePdfFormat());
		dbPDFParserMapping.setFileParsed(newPDFParserMapping.isFileParsed());
		dbPDFParserMapping.setMultiInvoice(newPDFParserMapping.isMultiInvoice());
		dbPDFParserMapping.setMultiPages(newPDFParserMapping.isMultiPages());
		parserMappingDao.merge(dbPDFParserMapping);
		responseObject.setSuccess(true);
		return responseObject;
	}
	
	/**
	 * Method will create or update the parser mapping using selected base
	 * mapping or none option.
	 * 
	 * @param parserMapping
	 * @param mappingId
	 * @param staffId
	 * @param actionType
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	public ResponseObject createOrUpdateParserMappingDetails(ParserMapping newParserMapping, int mappingId, int staffId, String actionType,
			String parserType, int pluginId) {

		logger.debug("Going to create new mapping details for different actions.Like create from base mapping or create fresh copy of mapping.");

		ResponseObject responseObject = new ResponseObject();

		int mappingCount = parserMappingDao.getMappingCount(newParserMapping.getName());

		if (mappingCount > 0) {
			logger.info("Duplicate mapping name found:" + newParserMapping.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_MAPPING_FOUND);
		} else {

			if (BaseConstants.CREATE.equals(actionType) && mappingId > 0) {
				ParserMapping baseParserMapping = parserMappingDao.findByPrimaryKey(ParserMapping.class, mappingId);
				responseObject = createMappingFromBaseMapping(baseParserMapping, newParserMapping, parserType, staffId, pluginId);
			} else if (BaseConstants.CUSTOM_CREATE.equals(actionType) && (mappingId > 0 && newParserMapping.getDevice().getId() > 0)) {
				ParserMapping baseParserMapping = parserMappingDao.findByPrimaryKey(ParserMapping.class, mappingId);
				responseObject = this.deviceService.getDeviceByDeviceId(newParserMapping.getDevice().getId(), "DEVICE");

				if (baseParserMapping != null && responseObject.isSuccess()) {
					baseParserMapping.setDevice((Device) responseObject.getObject()); // Setting
																						// device
																						//object
					responseObject = createMappingFromBaseMapping(baseParserMapping, newParserMapping, parserType, staffId, pluginId);
				} else {
					responseObject = setMappingFailureMessage();
				}
			} else if (BaseConstants.CREATE.equals(actionType) && mappingId == -2 && newParserMapping.getDevice().getId() > 0) { // From front end mapping
				Device device;
				responseObject = this.deviceService.getDeviceByDeviceId(newParserMapping.getDevice().getId(), "DEVICE");
				if (responseObject.isSuccess()) {

					device = (Device) responseObject.getObject();
					responseObject = parserService.getPluginByType(parserType);

					if (responseObject.isSuccess()) {
						PluginTypeMaster pluginTypeMaster = (PluginTypeMaster) responseObject.getObject();

						ParserMapping parserMapping = null;
						logger.info("Found plugin type " + parserType);

						if (EngineConstants.NATFLOW_PARSING_PLUGIN.equalsIgnoreCase(parserType)) {
							parserMapping = new NATFlowParserMapping();
							((NATFlowParserMapping) parserMapping).setReadTemplatesInitially(true);
						} else if (EngineConstants.NATFLOW_ASN_PARSING_PLUGIN.equalsIgnoreCase(parserType)) {
							parserMapping = new NatflowASN1ParserMapping();
							((NatflowASN1ParserMapping) parserMapping).setReadTemplatesInitially(true);
						} else if (EngineConstants.REGEX_PARSING_PLUGIN.equalsIgnoreCase(parserType)) {
							parserMapping = new RegexParserMapping();
						} else if (EngineConstants.ASCII_PARSING_PLUGIN.equalsIgnoreCase(parserType)) {
							parserMapping = new AsciiParserMapping();
						} else if (EngineConstants.DETAIL_LOCAL_PARSING_PLUGIN.equalsIgnoreCase(parserType)) {
							parserMapping = new DetailLocalParserMapping();
						} else if (EngineConstants.ASN1_PARSING_PLUGIN.equalsIgnoreCase(parserType)) {
							parserMapping = new ASN1ParserMapping();
						} else if (EngineConstants.XML_PARSING_PLUGIN.equalsIgnoreCase(parserType)) {
							parserMapping = new XMLParserMapping();
						} else if (EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equalsIgnoreCase(parserType)){
							parserMapping = new FixedLengthASCIIParserMapping();
						}
						else if (EngineConstants.RAP_PARSING_PLUGIN.equalsIgnoreCase(parserType)){
							parserMapping = new RapParserMapping();
						}else if (EngineConstants.TAP_PARSING_PLUGIN.equalsIgnoreCase(parserType)){
							parserMapping = new TapParserMapping();
						}else if (EngineConstants.NRTRDE_PARSING_PLUGIN.equalsIgnoreCase(parserType)){
							parserMapping = new NRTRDEParserMapping();
						}else if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equalsIgnoreCase(parserType)){
							parserMapping=new FixedLengthBinaryParserMapping();
						}else if(EngineConstants.HTML_PARSING_PLUGIN.equalsIgnoreCase(parserType)){
							parserMapping=new HtmlParserMapping();
						}else if(EngineConstants.PDF_PARSING_PLUGIN.equalsIgnoreCase(parserType)){
							parserMapping=new PDFParserMapping();
						}else if(EngineConstants.XLS_PARSING_PLUGIN.equalsIgnoreCase(parserType)){
							parserMapping=new XlsParserMapping();
						}else if(EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN.equalsIgnoreCase(parserType)){
							parserMapping=new VarLengthAsciiParserMapping();
						}else if(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equalsIgnoreCase(parserType)){
							parserMapping=new VarLengthBinaryParserMapping();
						} else if (EngineConstants.JSON_PARSING_PLUGIN.equalsIgnoreCase(parserType)) {
							parserMapping = new JsonParserMapping();
						}else if (EngineConstants.MTSIEMENS_BINARY_PARSING_PLUGIN.equalsIgnoreCase(parserType)) {
							parserMapping = new MTSiemensParserMapping();
						}
						
						if (parserMapping != null) {
							Date date = new Date();
							parserMapping.setDevice(device);
							parserMapping.setParserType(pluginTypeMaster);
							parserMapping.setParserWrapper(null);
							parserMapping.setCreatedDate(date);
							parserMapping.setLastUpdatedDate(date);
							parserMapping.setCreatedByStaffId(staffId);
							parserMapping.setLastUpdatedByStaffId(staffId);
							parserMapping.setParserAttributes(null);
							parserMapping.setMappingType(BaseConstants.USER_DEFINED_MAPPING);
							parserMapping.setName(newParserMapping.getName());
							ParserMappingService mappingServiceImpl = (ParserMappingService) SpringApplicationContext.getBean(serviceConstant);
							responseObject = mappingServiceImpl.createNewMapping(parserMapping, staffId, pluginId);
							if (responseObject.isSuccess()) {
								logger.info("Parser mapping has been created successfully.");
							} else {
								logger.info("Failed to create mapping.");
								responseObject = setMappingFailureMessage();
							}
						} else {
							logger.info("Failed to create mapping..");
							responseObject = setMappingFailureMessage();
						}
					} else {
						logger.info("Failed to get Parser type master details.");
						responseObject = setMappingFailureMessage();
					}
				} else {
					logger.info("Failed to get device details.");
					responseObject = setMappingFailureMessage();
				}
			}
		}
		return responseObject;
	}

	/**
	 * Method wil create new mapping using selected base mapping.
	 * 
	 * @param baseMapping
	 * @return
	 */
	private ResponseObject createMappingFromBaseMapping(ParserMapping baseParserMapping, ParserMapping newParserMapping, String parserType,
			int staffId, int pluginId) {
		ResponseObject responseObject;
		ParserMappingService mappingServiceImpl = (ParserMappingService) SpringApplicationContext.getBean(serviceConstant);
		if (baseParserMapping != null) {
			logger.debug("Parser mapping found successfully now going create mapping.");

			try {
				Date date = new Date();
				ParserMapping mappingObj = (ParserMapping) baseParserMapping.clone();
				mappingObj.setCreatedByStaffId(staffId);

				if (EngineConstants.NATFLOW_PARSING_PLUGIN.equals(parserType)
						|| EngineConstants.NATFLOW_ASN_PARSING_PLUGIN.equals(parserType)
						|| EngineConstants.ASCII_PARSING_PLUGIN.equals(parserType)
						|| EngineConstants.DETAIL_LOCAL_PARSING_PLUGIN.equals(parserType)
						|| EngineConstants.XML_PARSING_PLUGIN.equals(parserType)
						|| EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(parserType)
						|| EngineConstants.RAP_PARSING_PLUGIN.equals(parserType)
						|| EngineConstants.TAP_PARSING_PLUGIN.equals(parserType)
						|| EngineConstants.NRTRDE_PARSING_PLUGIN.equals(parserType)
						||EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(parserType)
						||EngineConstants.XLS_PARSING_PLUGIN.equals(parserType)
						||EngineConstants.PDF_PARSING_PLUGIN.equals(parserType)
						||EngineConstants.HTML_PARSING_PLUGIN.equals(parserType)
						||EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN.equals(parserType)
						||EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(parserType)
						||EngineConstants.JSON_PARSING_PLUGIN.equals(parserType)
						||EngineConstants.MTSIEMENS_BINARY_PARSING_PLUGIN.equals(parserType)) { // NOSONAR

					createParserAttributes(mappingObj);
				} else if (EngineConstants.REGEX_PARSING_PLUGIN.equals(parserType)) {
					RegexParserMapping regExParserMapping = (RegexParserMapping) mappingObj;
					createRegExParserAttributes(regExParserMapping);
				}
				mappingObj.setId(0);
				mappingObj.setMappingType(BaseConstants.USER_DEFINED_MAPPING); // Setting
																				// user
																				// defined
																				// mapping
																				// type.
				mappingObj.setName(newParserMapping.getName());
				mappingObj.setParserWrapper(null);
				mappingObj.setCreatedDate(date);
				mappingObj.setLastUpdatedDate(date);
				mappingObj.setCreatedByStaffId(staffId);
				mappingObj.setLastUpdatedByStaffId(staffId);

				responseObject = mappingServiceImpl.createNewMapping(mappingObj, staffId, pluginId);
				if (responseObject.isSuccess()) {
					logger.info("Mapping has been created successfully from selected base mapping.");
				} else {
					logger.info("Failed to create mapping as selected base mapping found  " + baseParserMapping);
					responseObject = setMappingFailureMessage();
				}
			} catch (CloneNotSupportedException e) {
				logger.error("Clone not supported", e);
				responseObject = setMappingFailureMessage();
			} 
			
		} else {
			logger.info("Failed to create mapping as selected base mapping found  " + baseParserMapping);
			responseObject = setMappingFailureMessage();

		}

		return responseObject;
	}

	/**
	 * Setting failure message in response object for mapping.
	 * 
	 * @return
	 */
	private ResponseObject setMappingFailureMessage() {
		ResponseObject responseObject = new ResponseObject();
		responseObject.setSuccess(false);
		responseObject.setObject(null);
		responseObject.setResponseCode(ResponseCode.FAIL_CREATE_MAPPING);
		return responseObject;
	}

	/**
	 * Method will create new mapping details and also it will check for the
	 * duplicate name for parser mapping.
	 * 
	 * @param parserMapping
	 * @param staffId
	 * @return ResponseObject
	 */
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_MAPPING, actionType = BaseConstants.CREATE_ACTION, currentEntity = ParserMapping.class,
			ignorePropList = "parserAttributes,parserWrapper,device")
	public ResponseObject createNewMapping(ParserMapping parserMapping, int staffId, int pluginId) {
		ResponseObject responseObject = new ResponseObject();

		logger.debug("Going to create new mapping details");

		parserMappingDao.save(parserMapping);
		ResponseObject mappingWithPluginStatus = null;
		if (parserMapping.getId() > 0) {
			mappingWithPluginStatus = updateParserMappingDetails(parserMapping.getId(), pluginId, staffId);
		}
		if (parserMapping.getId() > 0 && mappingWithPluginStatus != null && mappingWithPluginStatus.isSuccess()) {
			logger.info("Parser Mapping details created successfully.");
			responseObject.setSuccess(true);
			responseObject.setObject(parserMapping);
			responseObject.setResponseCode(ResponseCode.CREATE_MAPPING_SUCCESS);
		} else {
			logger.info("Failed to create parser mapping details.");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_CREATE_MAPPING);
		}

		return responseObject;
	}

	/**
	 * Create Parser Attribute
	 * 
	 * @param mappingObj
	 */
	public void createParserAttributes(ParserMapping mappingObj) {

		List<ParserAttribute> attributeList;
		if (mappingObj.getParserAttributes() != null && !mappingObj.getParserAttributes().isEmpty()) {
			mappingObj.getParserAttributes().get(0).getId();
			attributeList = mappingObj.getParserAttributes();

			List<ParserAttribute> tempAttributeList = new ArrayList<>();

			if (attributeList != null && !attributeList.isEmpty()) {
				Date date = new Date();
				for (ParserAttribute parserAttribute : attributeList) {
					parserAttributeDao.evict(parserAttribute);
					parserAttribute.setParserMapping(mappingObj);
					parserAttribute.setId(0);
					parserAttribute.setCreatedDate(date);
					parserAttribute.setLastUpdatedDate(date);
					parserAttribute.setCreatedByStaffId(mappingObj.getCreatedByStaffId());
					parserAttribute.setLastUpdatedByStaffId(mappingObj.getCreatedByStaffId());

					tempAttributeList.add(parserAttribute);
				}
				mappingObj.setParserAttributes(tempAttributeList);

			} else {
				mappingObj.setParserAttributes(null);
			}
		} else {
			mappingObj.setParserAttributes(null);
		}

	}

	/**
	 * Create REgex Parser attribute
	 * 
	 * @param regExParserMapping
	 */
	public void createRegExParserAttributes(RegexParserMapping regExParserMapping) {

		regExParserMapping.setParserAttributes(null);
		List<RegExPattern> patternList = regExPatternDao.getRegExPatternListByMappingId(regExParserMapping.getId());
		if (patternList != null && !patternList.isEmpty()) {

			List<RegExPattern> tempPatternList = new ArrayList<>();
			Date date = new Date();
			for (RegExPattern regExPattern : patternList) {
				regExPatternDao.evict(regExPattern);
				regExPattern.setParserMapping(regExParserMapping);
				regExPattern.setPatternRegExName(regExPattern.getPatternRegExName() + "_" + new Date().getTime());
				regExPattern.setCreatedDate(date);
				regExPattern.setLastUpdatedDate(date);
				regExPattern.setCreatedByStaffId(regExParserMapping.getCreatedByStaffId());
				regExPattern.setLastUpdatedByStaffId(regExParserMapping.getCreatedByStaffId());

				List<RegexParserAttribute> parserAttrList = parserAttributeDao.getRegExAttributeByPatternId(regExPattern.getId());
				if (parserAttrList != null && !patternList.isEmpty()) {

					List<RegexParserAttribute> tempAttrList = new ArrayList<>();

					for (RegexParserAttribute parserAttr : parserAttrList) {
						parserAttributeDao.evict(parserAttr);

						parserAttr.setPattern(regExPattern);
						parserAttr.setId(0);
						parserAttr.setCreatedDate(date);
						parserAttr.setLastUpdatedDate(date);
						parserAttr.setCreatedByStaffId(regExParserMapping.getCreatedByStaffId());
						parserAttr.setLastUpdatedByStaffId(regExParserMapping.getCreatedByStaffId());

						tempAttrList.add(parserAttr);

					}
					regExPattern.setId(0);
					regExPattern.setAttributeList(tempAttrList);
				} else {
					regExPattern.setAttributeList(null);
				}
				tempPatternList.add(regExPattern);
			}
			regExParserMapping.setPatternList(tempPatternList);
		} else {
			regExParserMapping.setPatternList(null);
		}
	}

	/**
	 * Set and update netflow parser detail
	 * 
	 * @param newNatflowParserMapping
	 * @param dbNatflowParserMapping
	 */
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class,
			ignorePropList = "parserAttributes,parserWrapper,device,parserType,name,srcCharSetName,dateFormat,srcDateFormat")
	public ResponseObject setAndUpdateNetflowParserDetail(NATFlowParserMapping newNatflowParserMapping, NATFlowParserMapping dbNatflowParserMapping) {

		ResponseObject responseObject = new ResponseObject();

		dbNatflowParserMapping.setOptionCopyTofield(newNatflowParserMapping.getOptionCopyTofield());
		dbNatflowParserMapping.setReadTemplatesInitially(newNatflowParserMapping.isReadTemplatesInitially());
		dbNatflowParserMapping.setOptionCopytoTemplateId(newNatflowParserMapping.getOptionCopytoTemplateId());
		dbNatflowParserMapping.setOptionTemplateEnable(newNatflowParserMapping.isOptionTemplateEnable());
		dbNatflowParserMapping.setOptionTemplateId(newNatflowParserMapping.getOptionTemplateId());
		dbNatflowParserMapping.setOptionTemplateKey(newNatflowParserMapping.getOptionTemplateKey());
		dbNatflowParserMapping.setOptionTemplateValue(newNatflowParserMapping.getOptionTemplateValue());
		dbNatflowParserMapping.setLastUpdatedDate(new Date());
		dbNatflowParserMapping.setSkipAttributeForValidation(newNatflowParserMapping.getSkipAttributeForValidation());
		dbNatflowParserMapping.setOverrideTemplate(newNatflowParserMapping.isOverrideTemplate());
		dbNatflowParserMapping.setDefaultTemplate(newNatflowParserMapping.getDefaultTemplate());
		dbNatflowParserMapping.setFilterEnable(newNatflowParserMapping.isFilterEnable());
		dbNatflowParserMapping.setFilterProtocol(newNatflowParserMapping.getFilterProtocol());
		dbNatflowParserMapping.setFilterTransport(newNatflowParserMapping.getFilterTransport());
		dbNatflowParserMapping.setFilterPort(newNatflowParserMapping.getFilterPort());

		parserMappingDao.merge(dbNatflowParserMapping); // It will update
														// natflow parser
														// details for selected
														// custom mapping and
														// mark associated
														// entities flag dirty.
		responseObject.setSuccess(true);
		return responseObject;
	}

	/**
	 * set and update ascii parser detail
	 * 
	 * @param newasciiParserMapping
	 * @param dbasciiParserMapping
	 */
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class, ignorePropList = "parserAttributes,parserWrapper,device,parserType,name,dateFormat,srcDateFormat")
	public ResponseObject setAndUpdateAsciiParserDetail(AsciiParserMapping newasciiParserMapping, AsciiParserMapping dbasciiParserMapping) {

		ResponseObject responseObject = new ResponseObject();
		if (BaseConstants.KEY_VALUE_RECORD.equals(newasciiParserMapping.getFileTypeEnum().name())) {

			dbasciiParserMapping.setFileTypeEnum(newasciiParserMapping.getFileTypeEnum());
			dbasciiParserMapping.setKeyValueRecordEnable(true);
			dbasciiParserMapping.setKeyValueSeparator(newasciiParserMapping.getKeyValueSeparator());
			dbasciiParserMapping.setFieldSeparator(newasciiParserMapping.getFieldSeparator());
			dbasciiParserMapping.setFind(newasciiParserMapping.getFind());
			dbasciiParserMapping.setReplace(newasciiParserMapping.getReplace());
			dbasciiParserMapping.setRecordHeaderEnable(false);
			dbasciiParserMapping.setFileHeaderEnable(false);
			dbasciiParserMapping.setFileFooterEnable(false);
			dbasciiParserMapping.setLinearKeyValueRecordEnable(false);
		} else if (BaseConstants.RECORD_HEADER.equals(newasciiParserMapping.getFileTypeEnum().name())) {

			dbasciiParserMapping.setFileTypeEnum(newasciiParserMapping.getFileTypeEnum());
			dbasciiParserMapping.setRecordHeaderEnable(true);
			dbasciiParserMapping.setRecordHeaderSeparator(newasciiParserMapping.getRecordHeaderSeparator());
			dbasciiParserMapping.setFieldSeparator(newasciiParserMapping.getFieldSeparator());
			dbasciiParserMapping.setRecordHeaderLength(newasciiParserMapping.getRecordHeaderLength());
			dbasciiParserMapping.setFind(newasciiParserMapping.getFind());
			dbasciiParserMapping.setReplace(newasciiParserMapping.getReplace());
			dbasciiParserMapping.setKeyValueRecordEnable(false);
			dbasciiParserMapping.setFileHeaderEnable(false);
			dbasciiParserMapping.setFileFooterEnable(false);
			dbasciiParserMapping.setLinearKeyValueRecordEnable(false);
		} else if (BaseConstants.FILE_HEADER_FOOTER.equals(newasciiParserMapping.getFileTypeEnum().name())) {

			dbasciiParserMapping.setFileTypeEnum(newasciiParserMapping.getFileTypeEnum());
			dbasciiParserMapping.setFileHeaderEnable(newasciiParserMapping.getFileHeaderEnable());
			dbasciiParserMapping.setFieldSeparator(newasciiParserMapping.getFieldSeparator());
			dbasciiParserMapping.setFileHeaderParser(newasciiParserMapping.getFileHeaderParser());
			dbasciiParserMapping.setFileHeaderContainsFields(newasciiParserMapping.getFileHeaderContainsFields());
			dbasciiParserMapping.setFileFooterEnable(newasciiParserMapping.getFileFooterEnable());
			dbasciiParserMapping.setFileFooterParser(newasciiParserMapping.getFileFooterParser());
			dbasciiParserMapping.setFileFooterContains(newasciiParserMapping.getFileFooterContains());
			dbasciiParserMapping.setFind(newasciiParserMapping.getFind());
			dbasciiParserMapping.setReplace(newasciiParserMapping.getReplace());
			dbasciiParserMapping.setKeyValueRecordEnable(false);
			dbasciiParserMapping.setRecordHeaderEnable(false);
			dbasciiParserMapping.setLinearKeyValueRecordEnable(false);
		} else if (BaseConstants.DELIMITER.equals(newasciiParserMapping.getFileTypeEnum().name())) {
			dbasciiParserMapping.setFileTypeEnum(newasciiParserMapping.getFileTypeEnum());
			dbasciiParserMapping.setFieldSeparator(newasciiParserMapping.getFieldSeparator());
			dbasciiParserMapping.setFileFooterEnable(newasciiParserMapping.getFileFooterEnable());
			dbasciiParserMapping.setFileFooterParser(newasciiParserMapping.getFileFooterParser());
			dbasciiParserMapping.setFileFooterContains(newasciiParserMapping.getFileFooterContains());
			dbasciiParserMapping.setFind(newasciiParserMapping.getFind());
			dbasciiParserMapping.setReplace(newasciiParserMapping.getReplace());
			dbasciiParserMapping.setFileHeaderEnable(false);
			dbasciiParserMapping.setKeyValueRecordEnable(false);
			dbasciiParserMapping.setRecordHeaderEnable(false);
			dbasciiParserMapping.setLinearKeyValueRecordEnable(false);
		} else if (BaseConstants.LINEAR_KEY_VALUE_RECORD.equals(newasciiParserMapping.getFileTypeEnum().name())){
			dbasciiParserMapping.setFileTypeEnum(newasciiParserMapping.getFileTypeEnum());
			dbasciiParserMapping.setKeyValueSeparator((newasciiParserMapping.getKeyValueSeparator()));
			dbasciiParserMapping.setRecordHeaderIdentifier(newasciiParserMapping.getRecordHeaderIdentifier());
			dbasciiParserMapping.setExcludeLinesStart(newasciiParserMapping.getExcludeLinesStart());
			dbasciiParserMapping.setExcludeCharactersMin(newasciiParserMapping.getExcludeCharactersMin());
			dbasciiParserMapping.setExcludeCharactersMax(newasciiParserMapping.getExcludeCharactersMax());
			dbasciiParserMapping.setFileHeaderEnable(false);
			dbasciiParserMapping.setKeyValueRecordEnable(false);
			dbasciiParserMapping.setRecordHeaderEnable(false);
			dbasciiParserMapping.setFileFooterEnable(false);
			dbasciiParserMapping.setLinearKeyValueRecordEnable(true);
			
		}

		dbasciiParserMapping.setLastUpdatedDate(new Date());
		parserMappingDao.merge(dbasciiParserMapping); // It will update natflow
														// parser details for
														// selected custom
														// mapping and mark
														// associated entities
														// flag dirty.
		responseObject.setSuccess(true);
		return responseObject;
	}
	
	/**
	 * set and update var length ascii parser detail
	 * 
	 * @param newVarLengthAsciiParserMapping
	 * @param dbVarLengthAsciiParserMapping
	 */
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class, ignorePropList = "parserAttributes,parserWrapper,device,parserType,name,dateFormat,srcDateFormat")
	public ResponseObject setAndUpdateVarLengthAsciiParserDetail(VarLengthAsciiParserMapping newVarLengthAsciiParserMapping, VarLengthAsciiParserMapping dbVarLengthAsciiParserMapping) {

		ResponseObject responseObject = new ResponseObject();
		if (BaseConstants.KEY_VALUE_RECORD.equals(newVarLengthAsciiParserMapping.getFileTypeEnum().name())) {

			dbVarLengthAsciiParserMapping.setFileTypeEnum(newVarLengthAsciiParserMapping.getFileTypeEnum());
			dbVarLengthAsciiParserMapping.setKeyValueRecordEnable(true);
			dbVarLengthAsciiParserMapping.setKeyValueSeparator(newVarLengthAsciiParserMapping.getKeyValueSeparator());
			dbVarLengthAsciiParserMapping.setFieldSeparator(newVarLengthAsciiParserMapping.getFieldSeparator());
			dbVarLengthAsciiParserMapping.setFind(newVarLengthAsciiParserMapping.getFind());
			dbVarLengthAsciiParserMapping.setReplace(newVarLengthAsciiParserMapping.getReplace());
			dbVarLengthAsciiParserMapping.setRecordHeaderEnable(false);
			dbVarLengthAsciiParserMapping.setFileHeaderEnable(false);
			dbVarLengthAsciiParserMapping.setFileFooterEnable(false);
			dbVarLengthAsciiParserMapping.setLinearKeyValueRecordEnable(false);
		} else if (BaseConstants.RECORD_HEADER.equals(newVarLengthAsciiParserMapping.getFileTypeEnum().name())) {

			dbVarLengthAsciiParserMapping.setFileTypeEnum(newVarLengthAsciiParserMapping.getFileTypeEnum());
			dbVarLengthAsciiParserMapping.setRecordHeaderEnable(true);
			dbVarLengthAsciiParserMapping.setRecordHeaderSeparator(newVarLengthAsciiParserMapping.getRecordHeaderSeparator());
			dbVarLengthAsciiParserMapping.setFieldSeparator(newVarLengthAsciiParserMapping.getFieldSeparator());
			dbVarLengthAsciiParserMapping.setRecordHeaderLength(newVarLengthAsciiParserMapping.getRecordHeaderLength());
			dbVarLengthAsciiParserMapping.setFind(newVarLengthAsciiParserMapping.getFind());
			dbVarLengthAsciiParserMapping.setReplace(newVarLengthAsciiParserMapping.getReplace());
			dbVarLengthAsciiParserMapping.setKeyValueRecordEnable(false);
			dbVarLengthAsciiParserMapping.setFileHeaderEnable(false);
			dbVarLengthAsciiParserMapping.setFileFooterEnable(false);
			dbVarLengthAsciiParserMapping.setLinearKeyValueRecordEnable(false);
		} else if (BaseConstants.FILE_HEADER_FOOTER.equals(newVarLengthAsciiParserMapping.getFileTypeEnum().name())) {

			dbVarLengthAsciiParserMapping.setFileTypeEnum(newVarLengthAsciiParserMapping.getFileTypeEnum());
			dbVarLengthAsciiParserMapping.setFileHeaderEnable(newVarLengthAsciiParserMapping.getFileHeaderEnable());
			dbVarLengthAsciiParserMapping.setFieldSeparator(newVarLengthAsciiParserMapping.getFieldSeparator());
			dbVarLengthAsciiParserMapping.setFileHeaderParser(newVarLengthAsciiParserMapping.getFileHeaderParser());
			dbVarLengthAsciiParserMapping.setFileHeaderContainsFields(newVarLengthAsciiParserMapping.getFileHeaderContainsFields());
			dbVarLengthAsciiParserMapping.setFileFooterEnable(newVarLengthAsciiParserMapping.getFileFooterEnable());
			dbVarLengthAsciiParserMapping.setFileFooterParser(newVarLengthAsciiParserMapping.getFileFooterParser());
			dbVarLengthAsciiParserMapping.setFileFooterContains(newVarLengthAsciiParserMapping.getFileFooterContains());
			dbVarLengthAsciiParserMapping.setFind(newVarLengthAsciiParserMapping.getFind());
			dbVarLengthAsciiParserMapping.setReplace(newVarLengthAsciiParserMapping.getReplace());
			dbVarLengthAsciiParserMapping.setKeyValueRecordEnable(false);
			dbVarLengthAsciiParserMapping.setRecordHeaderEnable(false);
			dbVarLengthAsciiParserMapping.setLinearKeyValueRecordEnable(false);
		} else if (BaseConstants.DELIMITER.equals(newVarLengthAsciiParserMapping.getFileTypeEnum().name())) {
			dbVarLengthAsciiParserMapping.setFileTypeEnum(newVarLengthAsciiParserMapping.getFileTypeEnum());
			dbVarLengthAsciiParserMapping.setFieldSeparator(newVarLengthAsciiParserMapping.getFieldSeparator());
			dbVarLengthAsciiParserMapping.setFileFooterEnable(newVarLengthAsciiParserMapping.getFileFooterEnable());
			dbVarLengthAsciiParserMapping.setFileFooterParser(newVarLengthAsciiParserMapping.getFileFooterParser());
			dbVarLengthAsciiParserMapping.setFileFooterContains(newVarLengthAsciiParserMapping.getFileFooterContains());
			dbVarLengthAsciiParserMapping.setFind(newVarLengthAsciiParserMapping.getFind());
			dbVarLengthAsciiParserMapping.setReplace(newVarLengthAsciiParserMapping.getReplace());
			dbVarLengthAsciiParserMapping.setFileHeaderEnable(false);
			dbVarLengthAsciiParserMapping.setKeyValueRecordEnable(false);
			dbVarLengthAsciiParserMapping.setRecordHeaderEnable(false);
			dbVarLengthAsciiParserMapping.setLinearKeyValueRecordEnable(false);
		} else if (BaseConstants.LINEAR_KEY_VALUE_RECORD.equals(newVarLengthAsciiParserMapping.getFileTypeEnum().name())){
			dbVarLengthAsciiParserMapping.setFileTypeEnum(newVarLengthAsciiParserMapping.getFileTypeEnum());
			dbVarLengthAsciiParserMapping.setKeyValueSeparator((newVarLengthAsciiParserMapping.getKeyValueSeparator()));
			dbVarLengthAsciiParserMapping.setRecordHeaderIdentifier(newVarLengthAsciiParserMapping.getRecordHeaderIdentifier());
			dbVarLengthAsciiParserMapping.setExcludeLinesStart(newVarLengthAsciiParserMapping.getExcludeLinesStart());
			dbVarLengthAsciiParserMapping.setExcludeCharactersMin(newVarLengthAsciiParserMapping.getExcludeCharactersMin());
			dbVarLengthAsciiParserMapping.setExcludeCharactersMax(newVarLengthAsciiParserMapping.getExcludeCharactersMax());
			dbVarLengthAsciiParserMapping.setFileHeaderEnable(false);
			dbVarLengthAsciiParserMapping.setKeyValueRecordEnable(false);
			dbVarLengthAsciiParserMapping.setRecordHeaderEnable(false);
			dbVarLengthAsciiParserMapping.setFileFooterEnable(false);
			dbVarLengthAsciiParserMapping.setLinearKeyValueRecordEnable(true);
		}
		dbVarLengthAsciiParserMapping.setDataDefinitionPath(newVarLengthAsciiParserMapping.getDataDefinitionPath());
		dbVarLengthAsciiParserMapping.setLastUpdatedDate(new Date());
		responseObject.setSuccess(true);
		return responseObject;
	}

	/**
	 * set and update xml parser detail
	 * 
	 * @param newxmlParserMapping
	 * @param dbxmlParserMapping
	 */
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class,
			ignorePropList = "parserAttributes,parserWrapper,device,parserType,name,dateFormat,srcDateFormat,srcCharSetName")
	public ResponseObject setAndUpdateXMLParserDetail(XMLParserMapping newxmlParserMapping, XMLParserMapping dbxmlParserMapping) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Enter into the Set and Update XML Parsing plugin");
		dbxmlParserMapping.setCommonFields(newxmlParserMapping.getCommonFields());
		dbxmlParserMapping.setRecordWiseXmlFormat(newxmlParserMapping.getRecordWiseXmlFormat());
		parserMappingDao.merge(dbxmlParserMapping);
		responseObject.setSuccess(true);
		return responseObject;
	}
	
	/**
	 * Method will validate and import mapping details.
	 * 
	 * @see com.elitecore.sm.parser.service.ParserMappingService#validateAndImportMappingDetails(com.elitecore.sm.parser.model.ParserMapping,
	 *      java.util.List)
	 */
	@Override
	public void validateImportedMappingDetails(ParserMapping parserMapping, List<ImportValidationErrors> importErrorList) {
		if (parserMapping != null) {
			// Validating parser mapping details
			parserMappingValidator.validateImportedMappingParameters(parserMapping, parserMapping.getName(), true, importErrorList);
			
			// validating device and its dependents.
			validateDeviceDetails(parserMapping, importErrorList); 
			
			List<ParserAttribute> attributeList = parserMapping.getParserAttributes();
			if (attributeList != null && !attributeList.isEmpty()) {
				logger.debug("Validating parser attribute for mapping " + parserMapping.getName());
				for (ParserAttribute attribute : attributeList) {
					// Validation for attribute details
					parserAttributeService.validateImportedAttributesDetails(attribute, importErrorList);
				}
			}
		} else {
			logger.debug("No mapping found.");
		}
	}

	/**
	 * Method will validate device details.
	 * 
	 * @param parserMapping
	 * @param importErrorList
	 */
	private void validateDeviceDetails(ParserMapping parserMapping, List<ImportValidationErrors> importErrorList) {
		logger.debug("Validating device and its dependents.");
		if (parserMapping.getMappingType() != BaseConstants.SYSTEM_DEFINED_MAPPING) {
			Device device = parserMapping.getDevice();
			if (device != null) {
				deviceService.validateImportDeviceDetails(parserMapping.getDevice(), importErrorList); // Device
																										// and
																										// its
																										// associated
																										// entity(DeviceType
																										// and
																										// VendorType)
																										// validation.
				if (device.getIsPreConfigured() == BaseConstants.USER_DEFINED_DEVICE && device.getDeviceType() == null) {
					ImportValidationErrors importErrors = new ImportValidationErrors(device.getName(), device.getName(), "DeviceType", null,
							"fail.import.device.type");
					importErrorList.add(importErrors);
				} else if (device.getIsPreConfigured() == BaseConstants.USER_DEFINED_DEVICE && device.getVendorType() == null) {
					ImportValidationErrors importErrors = new ImportValidationErrors(device.getName(), device.getName(), "VendorType", null,
							"fail.import.vendor.type");
					importErrorList.add(importErrors);
				}
			} else {
				ImportValidationErrors importErrors = new ImportValidationErrors(parserMapping.getName(), parserMapping.getName(), "Device", null,
						"device.details.get.fail");
				importErrorList.add(importErrors);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.elitecore.sm.parser.service.ParserMappingService#
	 * importMappingAndDependents(com.elitecore.sm.parser.model.Parser, boolean)
	 */
	@Transactional
	@Override
	public ResponseObject importParserMappingAndDependents(Parser parser, boolean isImport, int importMode) {
		logger.debug("Importing Parser mapping details.");
		ResponseObject responseObject = null;
		ParserMapping parserMapping = parser.getParserMapping();
		if (parserMapping != null && parserMapping.getMappingType() == BaseConstants.SYSTEM_DEFINED_MAPPING) {
			logger.debug("Found System defined mapping.");
			responseObject = setParserMappingDetails(parser, true, importMode); // Get and set mapping object by name and plug-in type.
		} else if ((parserMapping != null && parserMapping.getMappingType() == BaseConstants.USER_DEFINED_MAPPING)
				&& (parserMapping.getDevice() != null && parserMapping.getDevice().getIsPreConfigured() == BaseConstants.SYSTEM_DEFINED_DEVICE)) {
			logger.debug("Found user defined mapping and System defined Device.");
			responseObject = setParserMappingDetails(parser, false, importMode); // Get and set mapping object by name and plug-in type.
		} else if ((parserMapping != null && parserMapping.getMappingType() == BaseConstants.USER_DEFINED_MAPPING)
				&& (parserMapping.getDevice() != null && parserMapping.getDevice().getIsPreConfigured() == BaseConstants.USER_DEFINED_DEVICE)) {
			responseObject = setParserMappingDetails(parser, false, importMode); // Get and set mapping object by name and plug-in type.
		}
		return responseObject;
	}
	
	@Override
	public void importParserMappingForAddAndKeepBothMode(Parser exportedParser, ParsingPathList pathList, int importMode) {
		if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
			importParserMappingAndDependents(exportedParser, true, importMode);//NOSONAR
		} else {
			ParserMapping exportedParserMapping = exportedParser.getParserMapping();
			if(exportedParserMapping != null) {
				ParserMapping dbParserMapping = parserMappingDao.getMappingDetailsByNameAndType(exportedParserMapping.getName(), exportedParserMapping.getParserType().getId());
				if(dbParserMapping != null) {
					exportedParser.setParserMapping(dbParserMapping);
				} else {
					importParserMappingAndDependents(exportedParser, true, importMode);//NOSONAR
				}
			}
		}
	}

	/**
	 * Method will import mapping details and verify the device, vendor type and
	 * device type entity.
	 * 
	 * @param parserMapping
	 * @return
	 * @throws SMException
	 */
	private ResponseObject importeMappingDetails(ParserMapping parserMapping, int importMode) {
		Date date = new Date();
		logger.debug("Iterating parser mapping details.");
		ResponseObject responseObject = verifyAndCreateDeviceDetails(parserMapping);
		if (responseObject.isSuccess()) {
			if (EngineConstants.REGEX_PARSING_PLUGIN.equals(parserMapping.getParserType().getAlias())) {
				regExParserService.iterateAndAddRegexParserAttribute(parserMapping, importMode); // Iterate and add attribute and pattern list.
			} else {
				parserAttributeService.importParserAttribute(parserMapping); // Iterate and add attribute and pattern list.
			}
		}
		if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
			parserMapping.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, parserMapping.getName()));
		}
		parserMapping.setId(0);
		parserMapping.setCreatedDate(date);
		parserMapping.setLastUpdatedDate(date);
		return responseObject;
	}

	/**
	 * Method will verify and create device details.
	 * 
	 * @param parserMapping
	 * @return
	 */
	private ResponseObject verifyAndCreateDeviceDetails(ParserMapping parserMapping) {
		ResponseObject responseObject;
		if (parserMapping.getDevice().getIsPreConfigured() == BaseConstants.SYSTEM_DEFINED_DEVICE) {
			responseObject = deviceService.getDeviceByName(parserMapping.getDevice().getName());
			if (responseObject.isSuccess()) {
				logger.debug("Found already created device  with name " + parserMapping.getDevice().getName());
				parserMapping.setDevice((Device) responseObject.getObject());
				responseObject.setSuccess(true);
			}
		} else {
			logger.debug("Failed to get device  with name " + parserMapping.getDevice().getName() + " so creating new device.");
			responseObject = deviceService.verifyAndCreateDeviceDetails(parserMapping.getDevice(), parserMapping.getCreatedByStaffId(), BaseConstants.IMPORT_MODE_KEEP_BOTH);
			if (responseObject.isSuccess()) {
				parserMapping.setDevice((Device) responseObject.getObject());
			}
		}
		return responseObject;
	}

	@Transactional
	@Override
	public ResponseObject getMappingDetailsByNameAndType(String name, int pluginTypeId) {
		ResponseObject responseObject = new ResponseObject();
		ParserMapping parserMapping = parserMappingDao.getMappingDetailsByNameAndType(name, pluginTypeId);
		if (parserMapping != null) {
			responseObject.setSuccess(true);
			responseObject.setObject(parserMapping);
		} else {
			logger.info("Failed to get mapping details for given name " + name + " and plugin id " + pluginTypeId);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * Method will fetch mapping object and set it to parser object.
	 * 
	 * @param name
	 * @param pluginTypeId
	 * @param parser
	 * @return
	 * @throws SMException
	 */
	private ResponseObject setParserMappingDetails(Parser parser, boolean flag, int importMode) {
		ParserMapping parserMapping = parser.getParserMapping();
		ResponseObject responseObject;
		if (flag) {
			responseObject = getMappingDetailsByNameAndType(parserMapping.getName(), parserMapping.getParserType().getId());
			if (responseObject.isSuccess()) {
				parserMapping = (ParserMapping) responseObject.getObject();
				parser.setParserMapping(parserMapping);
				responseObject.setSuccess(true);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.IMPORT_FAIL);
			}
		} else {
			responseObject = importeMappingDetails(parserMapping, importMode);
		}
		return responseObject;
	}

	/**
	 * Method will fetch all mapping details by Device type id and plug-in type.
	 * 
	 * @param deviceId
	 * @param pluginType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public ResponseObject getMappingListByDeviceTypeAndParserType(int deviceTypeId, String pluginType) {
		logger.debug("Fetching all mapping by Device type and parser type.");

		ResponseObject responseObject = deviceService.getAllDeviceIdsByDeviceType(deviceTypeId);
		if (responseObject.isSuccess()) {

			List<Object[]> parserMappingList = parserMappingDao.getAllMappingByDeviceTypeAndParserType((List<Integer>) responseObject.getObject(),
					pluginType);
			if (parserMappingList != null && !parserMappingList.isEmpty()) {
				JSONArray jsArray = new JSONArray(parserMappingList);
				responseObject.setSuccess(true);
				responseObject.setObject(jsArray);
				logger.info(parserMappingList.size() + " mapping list found successfully!");
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FAIL_GET_MAPPING_DEVICE_ID);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_MAPPING_DEVICE_ID);
		}
		return responseObject;
	}

	@Override
	@Transactional
	public ParserMapping getParserMappingById(int parserMappingId) {
		return parserMappingDao.findByPrimaryKey(ParserMapping.class, parserMappingId);
	}
	
	@Override
	public void importParserMappingForUpdateMode(ParserMapping dbParserMapping, ParserMapping exportedParserMapping) {
		
		//set basic parameter of parser mapping + regex parser mapping
		updateParserMappingBasicParameters(dbParserMapping, exportedParserMapping);
		
		//set device to parser mapping
		Device dbDevice = deviceService.getDeviceForImportUpdateMode(exportedParserMapping.getDevice(), dbParserMapping.getCreatedByStaffId(), BaseConstants.IMPORT_MODE_UPDATE);
		if(dbDevice.getId() != 0){
			dbParserMapping.setDevice(dbDevice);
		} else {
			ResponseObject responseObject = verifyAndCreateDeviceDetails(exportedParserMapping);
			if(responseObject.isSuccess()){
				dbParserMapping.setDevice(exportedParserMapping.getDevice());
			}
		}
		
		List<ParserAttribute> dbParserAttributeList = dbParserMapping.getParserAttributes();
		List<ParserAttribute> exportedParserAttributeList = exportedParserMapping.getParserAttributes();
		Iterator<ParserAttribute> itrExp = exportedParserAttributeList.iterator();
		while(itrExp.hasNext()){
			ParserAttribute composerAttr = itrExp.next();
			if(composerAttr.isAssociatedByGroup()){
				itrExp.remove();
			}
		}
		String pluginType = dbParserMapping.getParserType().getAlias();
		//set attributes to parser mapping
		if(!CollectionUtils.isEmpty(exportedParserAttributeList)) {
			int length = exportedParserAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				ParserAttribute exportedParserAttribute = exportedParserAttributeList.get(i);
				if(exportedParserAttribute != null && !exportedParserAttribute.getStatus().equals(StateEnum.DELETED)) {
					ParserAttribute dbParserAttribute = getParserAttributeFromList(dbParserAttributeList, exportedParserAttribute.getSourceField(), exportedParserAttribute.getUnifiedField(), false);
					/** MED-9175 : Ascii Attributes are not viewing on GUI after importing service. */
					ParserAttribute exportedParserAttribute1 = ParserAttributeFactory.getParserAttributeByType(pluginType);
					BeanUtils.copyProperties(exportedParserAttribute, exportedParserAttribute1);
					exportedParserAttribute = exportedParserAttribute1;
					if(dbParserAttribute != null) {
						parserAttributeService.updateParserAttributeForImport(dbParserAttribute, exportedParserAttribute);
						dbParserAttributeList.add(dbParserAttribute);
					} else {
						parserAttributeService.saveParserAttributeForImport(exportedParserAttribute, dbParserMapping, null);
						dbParserAttributeList.add(exportedParserAttribute);
					}
				}
			}
		}
		
		//set parser group attribute to parser mapping
		parserGroupAttributeService.importParserGroupAttributesForUpdateMode(dbParserMapping, exportedParserMapping);
	}
	
	@Override
	public void importParserMappingForAddMode(ParserMapping dbParserMapping, ParserMapping exportedParserMapping) {
						
		//set device to parser mapping
		Device dbDevice = deviceService.getDeviceForImportUpdateMode(exportedParserMapping.getDevice(), dbParserMapping.getCreatedByStaffId(), BaseConstants.IMPORT_MODE_UPDATE);
		if(dbDevice.getId() != 0){
			dbParserMapping.setDevice(dbDevice);
		} else {
			ResponseObject responseObject = verifyAndCreateDeviceDetails(exportedParserMapping);
			if(responseObject.isSuccess()){
				dbParserMapping.setDevice(exportedParserMapping.getDevice());
			}
		}
		
		List<ParserAttribute> dbParserAttributeList = dbParserMapping.getParserAttributes();
		List<ParserAttribute> exportedParserAttributeList = exportedParserMapping.getParserAttributes();
		Iterator<ParserAttribute> itrExp = exportedParserAttributeList.iterator();
		while(itrExp.hasNext()){
			ParserAttribute composerAttr = itrExp.next();
			if(composerAttr.isAssociatedByGroup()){
				itrExp.remove();
			}
		}

		//set attributes to parser mapping
		if(!CollectionUtils.isEmpty(exportedParserAttributeList)) {
			int length = exportedParserAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				ParserAttribute exportedParserAttribute = exportedParserAttributeList.get(i);
				if(exportedParserAttribute != null && !exportedParserAttribute.getStatus().equals(StateEnum.DELETED)) {
					ParserAttribute dbParserAttribute = getParserAttributeFromList(dbParserAttributeList, exportedParserAttribute.getSourceField(), exportedParserAttribute.getUnifiedField(), false);
					if(dbParserAttribute == null) {						
						parserAttributeService.saveParserAttributeForImport(exportedParserAttribute, dbParserMapping, null);
						dbParserAttributeList.add(exportedParserAttribute);
					}
				}
			}
		}
		
		//set parser group attribute to parser mapping
		parserGroupAttributeService.importParserGroupAttributesForUpdateMode(dbParserMapping, exportedParserMapping);
	}
	
	@Override
	public ParserAttribute getParserAttributeFromList(List<ParserAttribute> parserAttributeList, String sourceField, String unifiedField, boolean associatedFlag) {
		if(!CollectionUtils.isEmpty(parserAttributeList)) {
			int length = parserAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				ParserAttribute parserAttribute = parserAttributeList.get(i);
				if(parserAttribute != null && !parserAttribute.getStatus().equals(StateEnum.DELETED) && parserAttribute.isAssociatedByGroup()==associatedFlag){
					if(isParserAttributeUnique(parserAttribute, sourceField, unifiedField)) {
						return parserAttributeList.remove(i);
					}
				}
			}
		}
		return null;
	}
	
	public boolean isParserAttributeUnique(ParserAttribute attribute, String sourceField, String unifiedField) {
		boolean isSourceFieldMatched = false;
		boolean isUnifiedFieldMatched = false;
		if(attribute != null && !attribute.getStatus().equals(StateEnum.DELETED)) {
			if(attribute.getSourceField() == null && sourceField == null) {
				isSourceFieldMatched = true;
			} else if(attribute.getSourceField() != null && sourceField != null
					&& (attribute.getSourceField().equalsIgnoreCase(sourceField))) {
				isSourceFieldMatched = true;
			}
			if(attribute.getUnifiedField() == null && unifiedField == null) {
				isUnifiedFieldMatched = true;
			} else if(attribute.getUnifiedField() != null && unifiedField != null
					&& (attribute.getUnifiedField().equalsIgnoreCase(unifiedField))) {
				isUnifiedFieldMatched = true;
			}
		}
		return isSourceFieldMatched && isUnifiedFieldMatched ? true : false;
	}
	
	@Override
	public void updateParserMappingBasicParameters(ParserMapping dbParserMapping, ParserMapping exportedParserMapping) {
		if(dbParserMapping instanceof AsciiParserMapping && exportedParserMapping instanceof AsciiParserMapping) {
			iterateAsciiParserMapping((AsciiParserMapping) dbParserMapping, (AsciiParserMapping) exportedParserMapping);
		} else if(dbParserMapping instanceof ASN1ParserMapping && exportedParserMapping instanceof ASN1ParserMapping) {
			iterateASN1ParserMapping((ASN1ParserMapping) dbParserMapping, (ASN1ParserMapping) exportedParserMapping);
		} else if(dbParserMapping instanceof DetailLocalParserMapping && exportedParserMapping instanceof DetailLocalParserMapping) {
			iterateDetailLocalParserMapping((DetailLocalParserMapping) dbParserMapping, (DetailLocalParserMapping) exportedParserMapping);
		} else if(dbParserMapping instanceof FixedLengthASCIIParserMapping && exportedParserMapping instanceof FixedLengthASCIIParserMapping) {
			iterateFixedLengthASCIIParserMapping((FixedLengthASCIIParserMapping) dbParserMapping, (FixedLengthASCIIParserMapping) exportedParserMapping);
		} else if(dbParserMapping instanceof FixedLengthBinaryParserMapping && exportedParserMapping instanceof FixedLengthBinaryParserMapping){
			iterateFixedLengthBinaryParserMapping((FixedLengthBinaryParserMapping) dbParserMapping, (FixedLengthBinaryParserMapping) exportedParserMapping);
		}else if(dbParserMapping instanceof PDFParserMapping && exportedParserMapping instanceof PDFParserMapping){
			iteratePDFParserMapping((PDFParserMapping) dbParserMapping, (PDFParserMapping) exportedParserMapping);
		}else if(dbParserMapping instanceof NATFlowParserMapping && exportedParserMapping instanceof NATFlowParserMapping) {
			iterateNATFlowParserMapping((NATFlowParserMapping) dbParserMapping, (NATFlowParserMapping) exportedParserMapping);
		} else if(dbParserMapping instanceof NatflowASN1ParserMapping && exportedParserMapping instanceof NatflowASN1ParserMapping) {
			iterateNatflowASN1ParserMapping((NatflowASN1ParserMapping) dbParserMapping, (NatflowASN1ParserMapping) exportedParserMapping);
		} else if(dbParserMapping instanceof RegexParserMapping && exportedParserMapping instanceof RegexParserMapping) {
			iterateRegexParserMapping((RegexParserMapping) dbParserMapping, (RegexParserMapping) exportedParserMapping);
		} else if(dbParserMapping instanceof SstpXmlParserMapping && exportedParserMapping instanceof SstpXmlParserMapping) {
			iterateSstpXmlParserMapping((SstpXmlParserMapping) dbParserMapping, (SstpXmlParserMapping) exportedParserMapping);
		} else if(dbParserMapping instanceof XMLParserMapping && exportedParserMapping instanceof XMLParserMapping) {
			iterateXMLParserMapping((XMLParserMapping) dbParserMapping, (XMLParserMapping) exportedParserMapping);
		} else if(dbParserMapping instanceof HtmlParserMapping && exportedParserMapping instanceof HtmlParserMapping) {
			iterateHtmlParserMapping((HtmlParserMapping) dbParserMapping, (HtmlParserMapping) exportedParserMapping);
		}else if(dbParserMapping instanceof VarLengthAsciiParserMapping && exportedParserMapping instanceof VarLengthAsciiParserMapping) {
			iterateVLAParserMapping((VarLengthAsciiParserMapping) dbParserMapping, (VarLengthAsciiParserMapping) exportedParserMapping);
		}else if(dbParserMapping instanceof VarLengthBinaryParserMapping && exportedParserMapping instanceof VarLengthBinaryParserMapping) {
			iterateVLBParserMapping((VarLengthBinaryParserMapping) dbParserMapping, (VarLengthBinaryParserMapping) exportedParserMapping);
		} else {
			iterateParserMapping(dbParserMapping, exportedParserMapping);
		}
	}
	
	public void iterateVLBParserMapping(VarLengthBinaryParserMapping dbParserMapping, VarLengthBinaryParserMapping exportedParserMapping) {
		dbParserMapping.setSkipFileHeader(exportedParserMapping.isSkipFileHeader());
		dbParserMapping.setSkipSubFileHeader(exportedParserMapping.isSkipSubFileHeader());
		dbParserMapping.setFileHeaderSize(exportedParserMapping.getFileHeaderSize());
		dbParserMapping.setSubFileHeaderSize(exportedParserMapping.getSubFileHeaderSize());
		dbParserMapping.setSubFileLength(exportedParserMapping.getSubFileLength());
		dbParserMapping.setExtractionRuleKey(exportedParserMapping.getExtractionRuleKey());
		dbParserMapping.setExtractionRuleValue(exportedParserMapping.getExtractionRuleValue());
		dbParserMapping.setRecordLengthAttribute(exportedParserMapping.getRecordLengthAttribute());
		dbParserMapping.setDataDefinitionPath(exportedParserMapping.getDataDefinitionPath());
	}
	
	public void iterateVLAParserMapping(VarLengthAsciiParserMapping dbParserMapping, VarLengthAsciiParserMapping exportedParserMapping) {
		dbParserMapping.setFileTypeEnum(exportedParserMapping.getFileTypeEnum());
		dbParserMapping.setFieldSeparatorEnum(exportedParserMapping.getFieldSeparatorEnum());
		dbParserMapping.setFieldSeparator(exportedParserMapping.getFieldSeparator());
		dbParserMapping.setFind(exportedParserMapping.getFind());
		dbParserMapping.setReplace(exportedParserMapping.getReplace());
		dbParserMapping.setFileHeaderEnable(exportedParserMapping.getFileHeaderEnable());
		dbParserMapping.setFileHeaderContainsFields(exportedParserMapping.getFileHeaderContainsFields());
		dbParserMapping.setFileHeaderParser(exportedParserMapping.getFileHeaderParser());
		dbParserMapping.setFileHeaderRow(exportedParserMapping.getFileHeaderRow());
		dbParserMapping.setFileHeaderStartIndex(exportedParserMapping.getFileHeaderStartIndex());
		dbParserMapping.setFileFooterEnable(exportedParserMapping.getFileFooterEnable());
		dbParserMapping.setFileFooterParser(exportedParserMapping.getFileFooterParser());
		dbParserMapping.setFileFooterContains(exportedParserMapping.getFileFooterContains());
		dbParserMapping.setFileFooterRows(exportedParserMapping.getFileFooterRows());
		dbParserMapping.setKeyValueRecordEnable(exportedParserMapping.isKeyValueRecordEnable());
		dbParserMapping.setKeyValueSeparatorEnum(exportedParserMapping.getKeyValueSeparatorEnum());
		dbParserMapping.setKeyValueSeparator(exportedParserMapping.getKeyValueSeparator());
		dbParserMapping.setRecordHeaderEnable(exportedParserMapping.isRecordHeaderEnable());
		dbParserMapping.setRecordHeaderSepEnum(exportedParserMapping.getRecordHeaderSepEnum());
		dbParserMapping.setRecordHeaderSeparator(exportedParserMapping.getRecordHeaderSeparator());
		dbParserMapping.setRecordHeaderLength(exportedParserMapping.getRecordHeaderLength());
		dbParserMapping.setRecordHeaderIdentifier(exportedParserMapping.getRecordHeaderIdentifier());
		dbParserMapping.setExcludeCharactersMin(exportedParserMapping.getExcludeCharactersMin());
		dbParserMapping.setExcludeCharactersMax(exportedParserMapping.getExcludeCharactersMax());
		dbParserMapping.setExcludeLinesStart(exportedParserMapping.getExcludeLinesStart());
		dbParserMapping.setLinearKeyValueRecordEnable(exportedParserMapping.isLinearKeyValueRecordEnable());
		iterateParserMapping(dbParserMapping, exportedParserMapping);
	}
	
	public void iterateAsciiParserMapping(AsciiParserMapping dbParserMapping, AsciiParserMapping exportedParserMapping) {
		dbParserMapping.setFileTypeEnum(exportedParserMapping.getFileTypeEnum());
		dbParserMapping.setFieldSeparatorEnum(exportedParserMapping.getFieldSeparatorEnum());
		dbParserMapping.setFieldSeparator(exportedParserMapping.getFieldSeparator());
		dbParserMapping.setFind(exportedParserMapping.getFind());
		dbParserMapping.setReplace(exportedParserMapping.getReplace());
		dbParserMapping.setFileHeaderEnable(exportedParserMapping.getFileHeaderEnable());
		dbParserMapping.setFileHeaderContainsFields(exportedParserMapping.getFileHeaderContainsFields());
		dbParserMapping.setFileHeaderParser(exportedParserMapping.getFileHeaderParser());
		dbParserMapping.setFileHeaderRow(exportedParserMapping.getFileHeaderRow());
		dbParserMapping.setFileHeaderStartIndex(exportedParserMapping.getFileHeaderStartIndex());
		dbParserMapping.setFileFooterEnable(exportedParserMapping.getFileFooterEnable());
		dbParserMapping.setFileFooterParser(exportedParserMapping.getFileFooterParser());
		dbParserMapping.setFileFooterContains(exportedParserMapping.getFileFooterContains());
		dbParserMapping.setFileFooterRows(exportedParserMapping.getFileFooterRows());
		dbParserMapping.setKeyValueRecordEnable(exportedParserMapping.isKeyValueRecordEnable());
		dbParserMapping.setKeyValueSeparatorEnum(exportedParserMapping.getKeyValueSeparatorEnum());
		dbParserMapping.setKeyValueSeparator(exportedParserMapping.getKeyValueSeparator());
		dbParserMapping.setRecordHeaderEnable(exportedParserMapping.isRecordHeaderEnable());
		dbParserMapping.setRecordHeaderSepEnum(exportedParserMapping.getRecordHeaderSepEnum());
		dbParserMapping.setRecordHeaderSeparator(exportedParserMapping.getRecordHeaderSeparator());
		dbParserMapping.setRecordHeaderLength(exportedParserMapping.getRecordHeaderLength());
		dbParserMapping.setRecordHeaderIdentifier(exportedParserMapping.getRecordHeaderIdentifier());
		dbParserMapping.setExcludeCharactersMin(exportedParserMapping.getExcludeCharactersMin());
		dbParserMapping.setExcludeCharactersMax(exportedParserMapping.getExcludeCharactersMax());
		dbParserMapping.setExcludeLinesStart(exportedParserMapping.getExcludeLinesStart());
		dbParserMapping.setLinearKeyValueRecordEnable(exportedParserMapping.isLinearKeyValueRecordEnable());
		iterateParserMapping(dbParserMapping, exportedParserMapping);
	}
	
	public void iterateASN1ParserMapping(ASN1ParserMapping dbParserMapping, ASN1ParserMapping exportedParserMapping) {
		dbParserMapping.setRecMainAttribute(exportedParserMapping.getRecMainAttribute());
		dbParserMapping.setRemoveAddByte(exportedParserMapping.isRemoveAddByte());
		dbParserMapping.setHeaderOffset(exportedParserMapping.getHeaderOffset());
		dbParserMapping.setRecOffset(exportedParserMapping.getRecOffset());
		dbParserMapping.setRemoveFillers(exportedParserMapping.getRemoveFillers());
		dbParserMapping.setRemoveAddHeaderFooter(exportedParserMapping.isRemoveAddHeaderFooter());
		dbParserMapping.setRecordStartIds(exportedParserMapping.getRecordStartIds());
		dbParserMapping.setSkipAttributeMapping(exportedParserMapping.isSkipAttributeMapping());
		dbParserMapping.setDecodeFormat(exportedParserMapping.getDecodeFormat());
		dbParserMapping.setRootNodeName(exportedParserMapping.getRootNodeName());
		dbParserMapping.setBufferSize(exportedParserMapping.getBufferSize());
		iterateParserMapping(dbParserMapping, exportedParserMapping);
	}
	
	public void iterateDetailLocalParserMapping(DetailLocalParserMapping dbParserMapping, DetailLocalParserMapping exportedParserMapping) {
		dbParserMapping.setAttributeSeperator(exportedParserMapping.getAttributeSeperator());
		dbParserMapping.setVendorNameSeparatorEnable(exportedParserMapping.isVendorNameSeparatorEnable());
		dbParserMapping.setVendorSeparatorValue(exportedParserMapping.getVendorSeparatorValue());
		
		iterateParserMapping(dbParserMapping, exportedParserMapping);
	}
	
	public void iterateFixedLengthASCIIParserMapping(FixedLengthASCIIParserMapping dbParserMapping, FixedLengthASCIIParserMapping exportedParserMapping) {
		dbParserMapping.setFileHeaderEnable(exportedParserMapping.getFileHeaderEnable());
		dbParserMapping.setFileHeaderContainsFields(exportedParserMapping.getFileHeaderContainsFields());
		
		iterateParserMapping(dbParserMapping, exportedParserMapping);
	}
	
	public void iterateFixedLengthBinaryParserMapping(FixedLengthBinaryParserMapping dbParserMapping, FixedLengthBinaryParserMapping exportedParserMapping) {
		dbParserMapping.setRecordLength(exportedParserMapping.getRecordLength());
		iterateParserMapping(dbParserMapping, exportedParserMapping);
	}
	
	public void iteratePDFParserMapping(PDFParserMapping dbParserMapping, PDFParserMapping exportedParserMapping) {
		dbParserMapping.setFileParsed(exportedParserMapping.isFileParsed());
		dbParserMapping.setRecordWisePdfFormat(exportedParserMapping.isRecordWisePdfFormat());
		dbParserMapping.setMultiInvoice(exportedParserMapping.isMultiInvoice());
		dbParserMapping.setMultiPages(exportedParserMapping.isMultiPages());
		iterateParserMapping(dbParserMapping, exportedParserMapping);
	}
	
	public void iterateNATFlowParserMapping(NATFlowParserMapping dbParserMapping, NATFlowParserMapping exportedParserMapping) {
		dbParserMapping.setReadTemplatesInitially(exportedParserMapping.isReadTemplatesInitially());
		dbParserMapping.setOptionTemplateEnable(exportedParserMapping.isOptionTemplateEnable());
		dbParserMapping.setOptionTemplateId(exportedParserMapping.getOptionTemplateId());
		dbParserMapping.setOptionTemplateKey(exportedParserMapping.getOptionTemplateKey());
		dbParserMapping.setOptionTemplateValue(exportedParserMapping.getOptionTemplateValue());
		dbParserMapping.setOptionCopytoTemplateId(exportedParserMapping.getOptionCopytoTemplateId());
		dbParserMapping.setOptionCopyTofield(exportedParserMapping.getOptionCopyTofield());
		dbParserMapping.setSkipAttributeForValidation(exportedParserMapping.getSkipAttributeForValidation());
		dbParserMapping.setOverrideTemplate(exportedParserMapping.isOverrideTemplate());
		dbParserMapping.setDefaultTemplate(exportedParserMapping.getDefaultTemplate());
		dbParserMapping.setFilterEnable(exportedParserMapping.isFilterEnable());
		dbParserMapping.setFilterProtocol(exportedParserMapping.getFilterProtocol());
		dbParserMapping.setFilterTransport(exportedParserMapping.getFilterTransport());
		dbParserMapping.setFilterPort(exportedParserMapping.getFilterPort());
		iterateParserMapping(dbParserMapping, exportedParserMapping);
	}
	
	public void iterateNatflowASN1ParserMapping(NatflowASN1ParserMapping dbParserMapping, NatflowASN1ParserMapping exportedParserMapping) {
		iterateParserMapping(dbParserMapping, exportedParserMapping);
	}
	
	public void iterateRegexParserMapping(RegexParserMapping dbParserMapping, RegexParserMapping exportedParserMapping) {
		dbParserMapping.setLogPatternRegex(exportedParserMapping.getLogPatternRegex());
		dbParserMapping.setLogPatternRegexId(exportedParserMapping.getLogPatternRegexId());
		dbParserMapping.setAvilablelogPatternRegexId(exportedParserMapping.getAvilablelogPatternRegexId());
		
		List<RegExPattern> dbRegexPatternList = dbParserMapping.getPatternList();
		List<RegExPattern> exportedRegexPatternList = exportedParserMapping.getPatternList();
		
		if(!CollectionUtils.isEmpty(exportedRegexPatternList)) {
			int length = exportedRegexPatternList.size();
			for(int i = length-1; i >= 0; i--) {
				RegExPattern exportedRegexPattern = exportedRegexPatternList.get(i);
				if(exportedRegexPattern != null && !exportedRegexPattern.getStatus().equals(StateEnum.DELETED)) {
					RegExPattern dbRegexPattern = regExParserService.getRegexPatternFromList(dbRegexPatternList, exportedRegexPattern.getPatternRegExName());
					if(dbRegexPattern != null) {
						regExParserService.importRegexPatternForUpdateMode(dbRegexPattern, exportedRegexPattern);
						dbRegexPatternList.add(dbRegexPattern);
					} else {
						regExParserService.importRegexPatternForAddAndKeepBothMode(exportedRegexPattern, dbParserMapping, BaseConstants.IMPORT_MODE_UPDATE);
						dbRegexPatternList.add(exportedRegexPattern);
					}
				}
			}
		}
		
		iterateParserMapping(dbParserMapping, exportedParserMapping);
	}
	
	public void updateRegexParserAttributeInList(List<RegexParserAttribute> regexParserAttributeList, RegexParserAttribute attribute) {
		if(regexParserAttributeList != null && attribute != null) {
			int length = regexParserAttributeList.size();
			boolean isAttributeAvailable = false;
			for(int i = length-1; i >= 0; i--) {
				RegexParserAttribute regexParserAttribute = regexParserAttributeList.get(i);
				if(regexParserAttribute != null && !regexParserAttribute.getStatus().equals(StateEnum.DELETED)
						&& regexParserAttribute.getSourceField().equalsIgnoreCase(attribute.getSourceField())
						&& regexParserAttribute.getUnifiedField().equalsIgnoreCase(attribute.getUnifiedField())) {
					isAttributeAvailable = true;
					regexParserAttributeList.set(i, attribute);
					break;
				}
			}
			if(!isAttributeAvailable) {
				regexParserAttributeList.add(attribute);
			}
		}
	}
	
	public void iterateSstpXmlParserMapping(SstpXmlParserMapping dbParserMapping, SstpXmlParserMapping exportedParserMapping) {
		iterateParserMapping(dbParserMapping, exportedParserMapping);
	}
	
	public void iterateXMLParserMapping(XMLParserMapping dbParserMapping, XMLParserMapping exportedParserMapping) {
		dbParserMapping.setRecordWiseXmlFormat(exportedParserMapping.getRecordWiseXmlFormat());
		dbParserMapping.setCommonFields(exportedParserMapping.getCommonFields());
		
		iterateParserMapping(dbParserMapping, exportedParserMapping);
	}
	
	public void iterateHtmlParserMapping(HtmlParserMapping dbParserMapping, HtmlParserMapping exportedParserMapping) {
		dbParserMapping.setRecordWiseExcelFormat(exportedParserMapping.getRecordWiseExcelFormat());
		dbParserMapping.setIsFileParsed(exportedParserMapping.getIsFileParsed());
		
		iterateParserMapping(dbParserMapping, exportedParserMapping);
	}
	
	public void iterateParserMapping(ParserMapping dbParserMapping, ParserMapping exportedParserMapping) {
		dbParserMapping.setDateFormat(exportedParserMapping.getDateFormat());
		dbParserMapping.setSrcDateFormat(exportedParserMapping.getSrcDateFormat());
		dbParserMapping.setSrcCharSetName(exportedParserMapping.getSrcCharSetName());
		dbParserMapping.setLastUpdatedDate(EliteUtils.getDateForImport(false));
	}

	
	

	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class,
			ignorePropList = "parserAttributes,parserWrapper,device,parserType,name,dateFormat,srcDateFormat,srcCharSetName")
	public ResponseObject setAndUpdateHtmlParserDetail(HtmlParserMapping newhtmlParserMapping,
			HtmlParserMapping dbhtmlParserMapping) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Enter into the Set and Update Html Parsing plugin");
		dbhtmlParserMapping.setIsFileParsed(newhtmlParserMapping.getIsFileParsed());
		dbhtmlParserMapping.setRecordWiseExcelFormat(newhtmlParserMapping.getRecordWiseExcelFormat());
		parserMappingDao.merge(dbhtmlParserMapping);
		responseObject.setSuccess(true);
		return responseObject;
	}
	
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class,
			ignorePropList = "parserAttributes,parserWrapper,device,parserType,name,dateFormat,srcDateFormat,srcCharSetName")
	public ResponseObject setAndUpdateXlsParserDetail(XlsParserMapping newXlsParserMapping,
			XlsParserMapping dbXlsParserMapping) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Enter into the Set and Update XLS Parsing plugin");
		dbXlsParserMapping.setIsFileParsed(newXlsParserMapping.getIsFileParsed());
		dbXlsParserMapping.setRecordWiseExcelFormat(newXlsParserMapping.getRecordWiseExcelFormat());
		parserMappingDao.merge(dbXlsParserMapping);
		responseObject.setSuccess(true);
		return responseObject;
	}
	
}
