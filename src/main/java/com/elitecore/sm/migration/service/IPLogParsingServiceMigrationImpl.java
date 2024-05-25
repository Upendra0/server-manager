package com.elitecore.sm.migration.service;

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
import com.elitecore.sm.migration.model.IPLogParsingServiceEntity;
import com.elitecore.sm.migration.model.MigrationEntityMapping;
import com.elitecore.sm.parser.model.AsciiParserMapping;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.IPLogParsingService;
import com.elitecore.sm.services.model.ParsingService;
import com.elitecore.sm.services.model.PartitionParam;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.MigrationUtil;

@org.springframework.stereotype.Service(value = "ipLogParsingServiceMigration")
public class IPLogParsingServiceMigrationImpl implements IPLogParsingServiceMigration {

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
	public ResponseObject getIPLogParsingServiceAndDependents(
			List<Service> serviceInstanceList, int position, Service service,
			String folderDirPath, ServerInstance serverInstance, int staffId,
			String migrationPrefix) throws MigrationSMException {

		this.migrationPrefix = migrationPrefix;
		this.staffId = staffId;
		logger.debug("Fetching iplog parsing service configuration for service id : " + service.getServInstanceId());
		String serviceKey = EngineConstants.IPLOG_PARSING_SERVICE + "-" + service.getServInstanceId();
		ResponseObject responseObject = migrationUtil.getFileContentFromMap(serviceKey);
		
		if (responseObject.isSuccess()) {
			byte[] fileContent = (byte[]) responseObject.getObject();

			responseObject = migrationUtil.getAndValidateMappingEntityObj(MigrationConstants.IPLOG_PARSING_SERVICE_XML);
			
			if (responseObject.isSuccess()) {
				MigrationEntityMapping entityMapping = (MigrationEntityMapping) responseObject.getObject();
				responseObject = migrationUtil.getSMAndJaxbObjectFromXml(fileContent, "", entityMapping.getXmlName());
				
				if (responseObject.isSuccess()) {
					logger.debug("Parsing service unmarshalling and dozer conversion done successfully!");
					Map<String, Object> returnObjects = (Map<String, Object>) responseObject.getObject();

					IPLogParsingService ipLogParsingService = (IPLogParsingService) returnObjects.get(MigrationConstants.MAP_SM_CLASS_KEY);

					IPLogParsingServiceEntity ipLogParsingServiceEntity = (IPLogParsingServiceEntity) returnObjects.get(MigrationConstants.MAP_JAXB_CLASS_KEY);
					
					boolean compressInFileEnabled = Boolean.parseBoolean(ipLogParsingServiceEntity.getIsCompressed());
					boolean compressOutFileEnabled = Boolean.parseBoolean(ipLogParsingServiceEntity.getFileCompression());
					
					Parser parser = new Parser(
							ipLogParsingServiceEntity.getPluginName(),
							ipLogParsingServiceEntity.getSelectFileOnPrefixes(),
							ipLogParsingServiceEntity.getSelectFileOnSuffixes(),
							ipLogParsingServiceEntity.getExcludeFileTypes(),
							ipLogParsingServiceEntity.getSyslogOutputConfiguration().getDestinationDirectoryPath(),
							compressInFileEnabled, 
							compressOutFileEnabled);

					ParsingPathList parsingPathList = new ParsingPathList();
					parsingPathList.setReadFilePath(ipLogParsingServiceEntity.getSourcePath());
					
					List<Parser> parserList = new ArrayList<>();
					
					PluginTypeMaster parserType = new PluginTypeMaster();
					parserType.setAlias(ipLogParsingServiceEntity.getPluginName());
					parser.setParserType(parserType);
					
					parserList.add(parser);
					parsingPathList.setParserWrappers(parserList);
					parsingPathList.setName(migrationUtil.getRandomName(this.migrationPrefix + "_PARSER"));
					
					if (ipLogParsingService != null) {
						
						List<PartitionParam> partitionParamSmList = ipLogParsingService.getPartionParamList();
						for (PartitionParam partitionParam : partitionParamSmList) {
							partitionParam.setParsingService(ipLogParsingService);
						}
						
						ServiceType serviceType = (ServiceType) MapCache.getConfigValueAsObject(service.getSvctype().getAlias());
						ipLogParsingService.setId(0);
						ipLogParsingService.setServerInstance(serverInstance);
						ipLogParsingService.setServInstanceId(service.getServInstanceId());
						ipLogParsingService.setName(migrationUtil.getRandomName(service.getName()));
						migrationUtil.setCurrentDateAndStaffId(ipLogParsingService, 1);
						ipLogParsingService.setStatus(StateEnum.ACTIVE);
						ipLogParsingService.setSvctype(serviceType);
						
						
						responseObject = loadPathListInIPLogParsingService(parsingPathList, ipLogParsingService);

						if (responseObject.isSuccess() && responseObject.getObject() instanceof IPLogParsingService) {
							ipLogParsingService = (IPLogParsingService) responseObject.getObject();
						} else {
							return responseObject;
						}
						displayIPLogParsingFields(ipLogParsingService);

						serviceInstanceList.set(position, ipLogParsingService);
						responseObject.setSuccess(true);
					}
				}
			}
		}
		return responseObject;
	}

	@SuppressWarnings("unchecked")
	public ResponseObject loadPathListInIPLogParsingService(ParsingPathList parsingPathList, IPLogParsingService ipLogParsingService) throws MigrationSMException {
		ResponseObject responseObject = null;

		// map for ASCII parser mapping
		Map<Integer, ParserMapping> asciiParserMap = null;
		boolean isAsciiPluginConfigRead = false;
		boolean isRegexPluginConfigRead = false;
		boolean isNatflowPluginConfigRead = false;		
		
		
		List<PathList> listParsingPathList = new ArrayList<>();
		if (parsingPathList != null) {
				
			List<Parser> parserList = parsingPathList.getParserWrappers();
				
				if (parserList != null && !parserList.isEmpty()) {
					int parserLength = parserList.size();
					for (int i = parserLength - 1; i >= 0; i--) {
						
						parserList.get(i).setName(migrationUtil.getRandomName(this.migrationPrefix + "_PARSER"));

						 String tempParserType = parserList.get(i).getParserType().getAlias(); 
							
						PluginTypeMaster parserType = (PluginTypeMaster) MapCache.getConfigValueAsObject(tempParserType);
						parserList.get(i).setParserType(parserType);
						
						switch (parserList.get(i).getParserType().getAlias()) {
						
						case EngineConstants.ASCII_PARSING_PLUGIN:
							
							if (!isAsciiPluginConfigRead) {
								
								responseObject = parserPluginMigrationService.getParserMapping(EngineConstants.ASCII_PARSING_PLUGIN,
												MigrationConstants.ASCII_PARSER_PLUGIN_XML,migrationPrefix, this.staffId);
								if (responseObject.isSuccess()) {
									asciiParserMap = (Map<Integer, ParserMapping>) responseObject.getObject();
									isAsciiPluginConfigRead = true;
								} else {
									return responseObject;
								}
							}

							if (asciiParserMap != null) {
								ParserMapping parserMapping = asciiParserMap.get(parserList.get(i).getId());
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
								parserList.get(i).setParserMapping(parserMapping);
							}
							break;
							
						case EngineConstants.REGEX_PARSING_PLUGIN:
							
							if(!isRegexPluginConfigRead) {
								responseObject = parserPluginMigrationService
										.getParserMapping(EngineConstants.REGEX_PARSING_PLUGIN, MigrationConstants.REGEX_PARSER_PLUGIN_XML, migrationPrefix, this.staffId);
								
								if (responseObject.isSuccess()) {
									asciiParserMap = (Map<Integer, ParserMapping>) responseObject.getObject();
									isRegexPluginConfigRead = true;
								} else {
									return responseObject;
								}
							}

							if (asciiParserMap != null) {
								ParserMapping parserMapping = asciiParserMap.get(parserList.get(i).getId());
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
			
			/*List<PathList> pathlist = new ArrayList<>();
			pathlist.add(parsingPathList);*/
			listParsingPathList.add(parsingPathList);
			
			ipLogParsingService.setSvcPathList((listParsingPathList));
			if (responseObject == null) {
				responseObject = new ResponseObject();
			}
			responseObject.setObject(ipLogParsingService);
			responseObject.setSuccess(true);
		}
		return responseObject;
	}

	public List<PathList> getIPLogParsingPathListFromJaxbPathList(List<?> jaxbPathList, ParsingService parsingService) throws MigrationSMException {
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
		logger.info("id : " + mapping.getId());
		logger.info("srcCharSetName : " + mapping.getSrcCharSetName());
		logger.info("srcDateFormat : " + mapping.getSrcDateFormat());
		logger.info("keyValueRecordEnable : "
				+ mapping.isKeyValueRecordEnable());
		logger.info("keyValueSeparator : " + mapping.getKeyValueSeparator());
		logger.info("fieldSeparator : " + mapping.getFieldSeparator());
		logger.info("find : " + mapping.getFind());
		logger.info("replace : " + mapping.getReplace());
		logger.info("recordHeaderEnable : " + mapping.isRecordHeaderEnable());
		logger.info("recordHeaderSeparator : "
				+ mapping.getRecordHeaderSeparator());
		logger.info("recordHeaderLength : " + mapping.getRecordHeaderLength());
		logger.info("fileHeaderEnable : " + mapping.getFileHeaderEnable());
		logger.info("fileHeaderParser : " + mapping.getFileHeaderParser());
		logger.info("fileHeaderContainsFields : "
				+ mapping.getFileHeaderContainsFields());
		logger.info("fileFooterEnable : " + mapping.getFileFooterEnable());
		logger.info("fileFooterParser : " + mapping.getFileFooterParser());
		logger.info("fileFooterContains : " + mapping.getFileFooterContains());
		List<ParserAttribute> attList = mapping.getParserAttributes();
		for (ParserAttribute parserAttribute : attList) {
			logger.info("Attribute ---------------------------- > ");
			logger.info("unifiedField : " + parserAttribute.getUnifiedField());
			logger.info("sourceField : " + parserAttribute.getSourceField());
			logger.info("defaultValue : " + parserAttribute.getDefaultValue());
			logger.info("description : " + parserAttribute.getDescription());
			logger.info("trimChars : " + parserAttribute.getTrimChars());
		}
		logger.info("===========Ascii Parser Mapping (END) =========== ");

	}

	private void displayIPLogParsingFields(IPLogParsingService parsingService) {
		logger.info("==============================================IPLOG PARSING SERVICE FIELDS (start)==========================================");

		logger.info("Service Name is : " + parsingService.getName());
		logger.info("Service id  is : " + parsingService.getId());
		logger.info("Service serv instance id  is : "
				+ parsingService.getServInstanceId());

		logger.info("****************************************");
		logger.info(" minthread  :  "
				+ parsingService.getSvcExecParams().getMinThread());
		logger.info("  sorting criteria :  "
				+ parsingService.getSvcExecParams().getSortingCriteria());
		logger.info(" max thread :  "
				+ parsingService.getSvcExecParams().getMaxThread());
		logger.info(" file batch size :  "
				+ parsingService.getSvcExecParams().getFileBatchSize());
		logger.info(" queue size  :  "
				+ parsingService.getSvcExecParams().getQueueSize());
		logger.info(" startupmode :  "
				+ parsingService.getSvcExecParams().getStartupMode());
		logger.info(" exe interval :  "
				+ parsingService.getSvcExecParams().getExecutionInterval());
		logger.info(" execute on startup :  "
				+ parsingService.getSvcExecParams().isExecuteOnStartup());
		logger.info("equalCheckField  :  "
				+ parsingService.getEqualCheckField());
		logger.info("equalCheckValue  :  "
				+ parsingService.getEqualCheckValue());
		logger.info("sorting type :  "
				+ parsingService.getSvcExecParams().getSortingType());
		logger.info("File grouping enable  is : "
				+ parsingService.getFileGroupingParameter().isFileGroupEnable());
		logger.info("File grouping parameter grouping type : "
				+ parsingService.getFileGroupingParameter().getGroupingType());
		logger.info("File grouping parameter file archive : "
				+ parsingService.getFileGroupingParameter().isFileGroupEnable());
		logger.info(" recordBatchSize  :  "
				+ parsingService.getRecordBatchSize());
		logger.info("isEnableFileStats  is : "
				+ parsingService.isEnableFileStats());

		
		List<PartitionParam> partitionParamList = parsingService.getPartionParamList();
		for (PartitionParam partitionParam : partitionParamList) {
			
			logger.info("***************************************************************************");
			System.out.println("*****************************************************************************");
			logger.info("partition field  is : "
					+ partitionParam.getPartitionField() );	
			
			logger.info("unifiedField field  is : "
					+ partitionParam.getUnifiedField() );

			logger.info("partitionRange field  is : "
					+ partitionParam.getPartitionRange());
			
			System.out.println("*****************************************************************************");
			System.out.println("*****************************************************************************");
		}
		

		
		List<PathList> pathList = parsingService.getSvcPathList();
		for (PathList p : pathList) {
			if (p instanceof ParsingPathList) {
				logger.info("PathList --------------------------------------> ");
				ParsingPathList path = (ParsingPathList) p;
				logger.info("readFilePath : " + path.getReadFilePath());
				List<Parser> parsers = path.getParserWrappers();
				for (Parser parser : parsers) {
					logger.info("Parser --------------------------------------> ");
					logger.info("parserType.alias : "
							+ parser.getParserType().getAlias());
					logger.info("compressInFileEnabled : "
							+ parser.isCompressInFileEnabled());
					logger.info("compressOutFileEnabled : "
							+ parser.isCompressOutFileEnabled());
					logger.info("maxFileCountAlert : "
							+ parser.getMaxFileCountAlert());
					logger.info("readFilenamePrefix : "
							+ parser.getReadFilenamePrefix());
					logger.info("readFilenameSuffix : "
							+ parser.getReadFilenameSuffix());
					logger.info("readFilenameContains : "
							+ parser.getReadFilenameContains());
					logger.info("readFilenameExcludeTypes : "
							+ parser.getReadFilenameExcludeTypes());
					logger.info("writeFilePath : " + parser.getWriteFilePath());
					logger.info("writeFileSplit : " + parser.isWriteFileSplit());
					logger.info("id : " + parser.getId());
					ParserMapping mapping = parser.getParserMapping();
					if (mapping != null
							&& mapping instanceof AsciiParserMapping) {
						displayAsciiParserMapping((AsciiParserMapping) mapping);
					}
				}
			}
		}
		logger.info(parsingService);
		logger.info("==============================================IPLOG PARSING SERVICE FIELDS (end)==========================================");
	}

}
