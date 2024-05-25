/**
 * 
 */
package com.elitecore.sm.migration.service;

import static com.elitecore.sm.common.constants.MigrationConstants.PARSING_SERVICE_XML;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.MigrationConstants;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.migration.model.MigrationEntityMapping;
import com.elitecore.sm.migration.model.ParsingServiceEntity;
import com.elitecore.sm.parser.model.AsciiParserMapping;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.ParsingService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.MigrationUtil;

/**
 * @author vandana.awatramani
 *
 */
@org.springframework.stereotype.Service("parsingServiceMigration")
public class ParsingServiceMigrationImpl implements ParsingServiceMigration {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private MigrationUtil migrationUtil;
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private ParserPluginMigrationService parserPluginMigrationService;
	
	private String migrationPrefix;
	
	private int staffId;
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.migration.service.ParsingServiceMigration#getParsingServiceAndDependents(java.util.List, int, com.elitecore.sm.services.model.Service, java.lang.String, com.elitecore.sm.serverinstance.model.ServerInstance, int, java.lang.String)
	 */
	public ResponseObject getParsingServiceAndDependents(List<Service> serviceInstanceList, int position, Service service, String folderDirPath, ServerInstance serverInstance, int staffId, String migrationPrefix) throws MigrationSMException {

		this.migrationPrefix = migrationPrefix;
		this.staffId = staffId;
		logger.debug("Fetching parsing service configuration for service id : " + service.getServInstanceId());
		String serviceKey = EngineConstants.PARSING_SERVICE + "-"+ service.getServInstanceId();
		ResponseObject responseObject = migrationUtil.getFileContentFromMap(serviceKey);
		if (responseObject.isSuccess()) {
			byte[] fileContent = (byte[]) responseObject.getObject();
			
			responseObject = migrationUtil.getAndValidateMappingEntityObj(PARSING_SERVICE_XML);
			if (responseObject.isSuccess()) {
				MigrationEntityMapping  entityMapping = (MigrationEntityMapping) responseObject.getObject();
				responseObject = migrationUtil.getSMAndJaxbObjectFromXml(fileContent, entityMapping.getXsdName(), entityMapping.getXmlName());
				if (responseObject.isSuccess()) {
					logger.debug("Parsing service unmarshalling and dozer conversion done successfully!");
					Map<String, Object> returnObjects = (Map<String, Object>) responseObject.getObject();

					ParsingService parsingService = (ParsingService) returnObjects.get(MigrationConstants.MAP_SM_CLASS_KEY);

					ParsingServiceEntity parsingServiceEntity = (ParsingServiceEntity) returnObjects.get(MigrationConstants.MAP_JAXB_CLASS_KEY);

					List<ParsingServiceEntity.PathList.Path> jaxbPathList = parsingServiceEntity.getPathList().getPath();

					if (parsingService != null) {
						ServiceType serviceType = (ServiceType) MapCache.getConfigValueAsObject(service.getSvctype().getAlias());
						parsingService.setId(0);
						parsingService.setServerInstance(serverInstance);
						parsingService.setServInstanceId(service.getServInstanceId());
						parsingService.setName(migrationUtil.getRandomName(service.getName()));
						parsingService.setStatus(StateEnum.ACTIVE);
						parsingService.setSvctype(serviceType);
						migrationUtil.setCurrentDateAndStaffId(parsingService, 1);
						
						responseObject = loadPathListInParsingService(jaxbPathList, parsingService);
						
						if (responseObject.isSuccess() && responseObject.getObject() instanceof ParsingService) {
							parsingService = (ParsingService) responseObject.getObject();
						} else {
							return responseObject;
						}
						/*dispayParsingFields(parsingService);*/
						
						
						serviceInstanceList.set(position, parsingService);
						responseObject.setSuccess(true);
					}
				}
			}
		}
		return responseObject;
	}
	
	@SuppressWarnings("unchecked")
	public ResponseObject loadPathListInParsingService(List<ParsingServiceEntity.PathList.Path> jaxbPathList, ParsingService parsingService) throws MigrationSMException {
		ResponseObject responseObject = null;
		
		// map for ASCII parser mapping
		Map<Integer, ParserMapping> asciiParserMap = null;
		boolean isAsciiPluginConfigRead  = false;
		boolean isNatflowPluginConfigRead  = false;
		boolean isRegexPluginConfigRead  = false;
		
		if(jaxbPathList != null && !jaxbPathList.isEmpty()) {
			List<PathList> pathList = getParsingPathListFromJaxbPathList(jaxbPathList, parsingService);			
			
			if(pathList != null && !pathList.isEmpty()) {
				int pathLength = pathList.size();
				for(int j = pathLength-1; j>=0; j--) {
					
					if (pathList.get(j) instanceof ParsingPathList) {
						
						ParsingPathList parsingPathList = (ParsingPathList) pathList.get(j);
						List<Parser> parserList = parsingPathList.getParserWrappers();
						
						if (parserList != null && !parserList.isEmpty()) {
							
							int parserLength = parserList.size();
							for(int i = parserLength-1; i>=0; i--) {
								parserList.get(i).setName(migrationUtil.getRandomName(this.migrationPrefix+"_PARSER"));
								 String tempParserType = parserList.get(i).getParserType().getAlias(); 
								
								PluginTypeMaster parserType = (PluginTypeMaster) MapCache.getConfigValueAsObject(tempParserType);
								parserList.get(i).setParserType(parserType);
								
								switch (parserList.get(i).getParserType().getAlias()) {
									case EngineConstants.ASCII_PARSING_PLUGIN:
										if(!isAsciiPluginConfigRead){
										
											responseObject = parserPluginMigrationService.getParserMapping(EngineConstants.ASCII_PARSING_PLUGIN, MigrationConstants.ASCII_PARSER_PLUGIN_XML, migrationPrefix, this.staffId);
											if (responseObject.isSuccess()) {
												asciiParserMap = (Map<Integer, ParserMapping>) responseObject.getObject();
												isAsciiPluginConfigRead = true;
											} else {
												return responseObject;
											}
										}
										
										if (asciiParserMap != null) {
											ParserMapping parserMapping = asciiParserMap.get(parserList.get(i).getId());
											parserMapping.setId(0);
											parserList.get(i).setParserMapping(parserMapping);
										}
										break;
									case EngineConstants.ASN1_PARSING_PLUGIN:
										break;
									case EngineConstants.DETAIL_LOCAL_PARSING_PLUGIN:
										break;
									case EngineConstants.DIGITEL_BINARY_PARSING_PLUGIN:
										break;
									case EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN:
										break;
									case EngineConstants.MT_ALCATEL_BINARY_PARSING_PLUGIN:
										break;
									case EngineConstants.MT_SIEMENS_BINARY_PARSING_PLUGIN:
										break;
										
									case EngineConstants.NATFLOW_PARSING_PLUGIN:
										if(!isNatflowPluginConfigRead) {
											responseObject = parserPluginMigrationService
													.getParserMapping(EngineConstants.NATFLOW_PARSING_PLUGIN, MigrationConstants.NATFLOW_PARSING_PLUGIN_XML, migrationPrefix, this.staffId);
											
											if (responseObject.isSuccess()) {
												asciiParserMap = (Map<Integer, ParserMapping>) responseObject.getObject();
												isNatflowPluginConfigRead = true;
											} else {
												return responseObject;
											}
										}

										if (asciiParserMap != null) {
											ParserMapping parserMapping = asciiParserMap.get(parserList.get(i).getId());
											parserMapping.setId(0);
											parserList.get(i).setParserMapping(parserMapping);
										}
										break;
									
									case EngineConstants.REGEX_PARSING_PLUGIN:
										
										if(!isRegexPluginConfigRead) {
											responseObject = parserPluginMigrationService.getParserMapping(EngineConstants.REGEX_PARSING_PLUGIN, MigrationConstants.REGEX_PARSER_PLUGIN_XML, migrationPrefix, this.staffId);
											
											if (responseObject.isSuccess()) {
												asciiParserMap = (Map<Integer, ParserMapping>) responseObject.getObject();
												isRegexPluginConfigRead = true;
											} else {
												return responseObject;
											}
										}

										if (asciiParserMap != null) {
											ParserMapping parserMapping = asciiParserMap.get(0);
											parserMapping.setId(0);
											parserMapping.setId(0);
											parserList.get(i).setParserMapping(parserMapping);
										}
										break;
									case EngineConstants.SSTP_XML_PARSING_PLUGIN:
										break;
									case EngineConstants.XML_PARSING_PLUGIN:
										break;
									default:
										break;
								}
							}
						}
					}
				}
			}
			
			parsingService.setSvcPathList(pathList);
			if(responseObject == null) {
				responseObject = new ResponseObject();
			}
			responseObject.setObject(parsingService);
			responseObject.setSuccess(true);
		}
		return responseObject;
	}
	
	public List<PathList> getParsingPathListFromJaxbPathList(List<?> jaxbPathList, ParsingService parsingService) throws MigrationSMException {
		logger.info("ParsingServiceMigrationImpl.getParsingPathListFromJaxbPathList()");
		Class<?> destinationClass = migrationUtil.getFullClassName("com.elitecore.sm.pathlist.model.ParsingPathList");
		List<PathList> parsingPathList = new ArrayList<>();
		for (Object jaxbPath : jaxbPathList) {
			ResponseObject responseObject = migrationUtil.convertJaxbToSMObject(jaxbPath, destinationClass);
			if (responseObject.isSuccess() && responseObject.getObject() instanceof ParsingPathList) {
				ParsingPathList pathList = (ParsingPathList) responseObject.getObject();
				pathList.setName(migrationUtil.getRandomName(this.migrationPrefix+"_PARSING_PATHLIST"));
				pathList.setService(parsingService);
				parsingPathList.add(pathList);
			}
		}
		return parsingPathList;
	}
	
	private void displayAsciiParserMapping(AsciiParserMapping mapping) {
		logger.info("========= Ascii Parser Mapping (START) =========== ");
		logger.info("id : "+mapping.getId());
		logger.info("srcCharSetName : "+mapping.getSrcCharSetName());
		logger.info("srcDateFormat : "+mapping.getSrcDateFormat());
		logger.info("keyValueRecordEnable : "+mapping.isKeyValueRecordEnable());
		logger.info("keyValueSeparator : "+mapping.getKeyValueSeparator());
		logger.info("fieldSeparator : "+mapping.getFieldSeparator());
		logger.info("find : "+mapping.getFind());
		logger.info("replace : "+mapping.getReplace());
		logger.info("recordHeaderEnable : "+mapping.isRecordHeaderEnable());
		logger.info("recordHeaderSeparator : "+mapping.getRecordHeaderSeparator());
		logger.info("recordHeaderLength : "+mapping.getRecordHeaderLength());
		logger.info("fileHeaderEnable : "+mapping.getFileHeaderEnable());
		logger.info("fileHeaderParser : "+mapping.getFileHeaderParser());
		logger.info("fileHeaderContainsFields : "+mapping.getFileHeaderContainsFields());
		logger.info("fileFooterEnable : "+mapping.getFileFooterEnable());
		logger.info("fileFooterParser : "+mapping.getFileFooterParser());
		logger.info("fileFooterContains : "+mapping.getFileFooterContains());
		List<ParserAttribute> attList = mapping.getParserAttributes();
		for (ParserAttribute parserAttribute : attList) {
			logger.info("Attribute ---------------------------- > ");
			logger.info("unifiedField : "+parserAttribute.getUnifiedField());
			logger.info("sourceField : "+parserAttribute.getSourceField());
			logger.info("defaultValue : "+parserAttribute.getDefaultValue());
			logger.info("description : "+parserAttribute.getDescription());
			logger.info("trimChars : "+parserAttribute.getTrimChars());
		}
		logger.info("===========Ascii Parser Mapping (END) =========== ");
		
	}
	private void dispayParsingFields(ParsingService parsingService) {
		logger.info("============================================== PARSING SERVICE FIELDS (start)==========================================");

		logger.info("Service Name is : " + parsingService.getName());
		logger.info("Service id  is : " + parsingService.getId());
		logger.info("Service serv instance id  is : " + parsingService.getServInstanceId());

		logger.info("****************************************");
		logger.info(" minthread  :  "
				+ parsingService.getSvcExecParams().getMinThread());
		logger.info("  sorting criteria :  " + parsingService.getSvcExecParams().getSortingCriteria());
		logger.info(" max thread :  " + parsingService.getSvcExecParams().getMaxThread());
		logger.info(" file batch size :  " + parsingService.getSvcExecParams().getFileBatchSize());
		logger.info(" queue size  :  "	+ parsingService.getSvcExecParams().getQueueSize());
		logger.info("fileSeqOrderEnable  :  "	+ parsingService.isFileSeqOrderEnable());
		logger.info(" startupmode :  "	+ parsingService.getSvcExecParams().getStartupMode());
		logger.info("minFileRange  : " + parsingService.getMinFileRange());
		logger.info("maxFileRange  : " + parsingService.getMaxFileRange());
		logger.info("noFileAlert  : " + parsingService.getNoFileAlert());
		logger.info(" exe interval :  "	+ parsingService.getSvcExecParams().getExecutionInterval());
		logger.info(" execute on startup :  "	+ parsingService.getSvcExecParams().isExecuteOnStartup());
		logger.info("storeCDRFileSummaryDB  :  "+ parsingService.isStoreCDRFileSummaryDB());
		logger.info("equalCheckField  :  "	+ parsingService.getEqualCheckField());
		logger.info("equalCheckValue  :  " + parsingService.getEqualCheckValue());
		logger.info("dateFieldForSummary  :  "	+ parsingService.getDateFieldForSummary());
		logger.info("typeForSummary  :  " + parsingService.getTypeForSummary());
		logger.info("overrideFileDateType  :  "	+ parsingService.getOverrideFileDateType());
		logger.info("sorting type :  "	+ parsingService.getSvcExecParams().getSortingType());
		logger.info("File grouping enable  is : "	+ parsingService.getFileGroupingParameter().isFileGroupEnable());
		logger.info("File grouping parameter grouping type : "	+ parsingService.getFileGroupingParameter().getGroupingType());
		logger.info("File grouping parameter file archive : " + parsingService.getFileGroupingParameter().isFileGroupEnable());
		logger.info(" recordBatchSize  :  " + parsingService.getRecordBatchSize());

		logger.info("isEnableFileStats  is : "	+ parsingService.isEnableFileStats());

		logger.info("overrideFileDateEnabled  : this is not set in migration  "
				+ parsingService.getOverrideFileDateEnabled());
		
		List<PathList> pathList = parsingService.getSvcPathList();
		for(PathList p : pathList) {
			if(p instanceof ParsingPathList) {
				logger.info("PathList --------------------------------------> ");
				ParsingPathList path = (ParsingPathList) p;
				logger.info("readFilePath : "+path.getReadFilePath());
				List<Parser> parsers = path.getParserWrappers();
				for(Parser parser : parsers) {
					logger.info("Parser --------------------------------------> ");
					logger.info("parserType.alias : "+parser.getParserType().getAlias());
					logger.info("compressInFileEnabled : "+parser.isCompressInFileEnabled());
					logger.info("compressOutFileEnabled : "+parser.isCompressOutFileEnabled());
					logger.info("maxFileCountAlert : "+parser.getMaxFileCountAlert());
					logger.info("readFilenamePrefix : "+parser.getReadFilenamePrefix());
					logger.info("readFilenameSuffix : "+parser.getReadFilenameSuffix());
					logger.info("readFilenameContains : "+parser.getReadFilenameContains());
					logger.info("readFilenameExcludeTypes : "+parser.getReadFilenameExcludeTypes());
					logger.info("writeFilePath : "+parser.getWriteFilePath());
					logger.info("writeFileSplit : "+parser.isWriteFileSplit());
					logger.info("writeCdrHeaderFooterEnabled : "+parser.isWriteCdrHeaderFooterEnabled());
					logger.info("writeCdrDefaultAttributes : "+parser.isWriteCdrDefaultAttributes());
					logger.info("id : "+parser.getId());
					ParserMapping mapping = parser.getParserMapping();
					if(mapping != null && mapping instanceof AsciiParserMapping) {
						displayAsciiParserMapping((AsciiParserMapping)mapping);
					}
				}
			}
		}
		logger.info(parsingService);
		logger.info("============================================== PARSING SERVICE FIELDS (end)==========================================");
	}
}
