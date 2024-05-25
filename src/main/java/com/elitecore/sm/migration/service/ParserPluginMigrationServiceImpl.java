package com.elitecore.sm.migration.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.MigrationConstants;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.model.DecodeTypeEnum;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.migration.model.AsciiParserPluginEntity;
import com.elitecore.sm.migration.model.MigrationEntityMapping;
import com.elitecore.sm.migration.model.NatflowParserPluginEntity;
import com.elitecore.sm.migration.model.RegexParserPluginEntity;
import com.elitecore.sm.migration.model.RegexParserPluginEntity.Instance.PatternList.Pattern;
import com.elitecore.sm.migration.model.RegexParserPluginEntity.Instance.PatternList.Pattern.AttributeList.Attribute;
import com.elitecore.sm.parser.model.AsciiParserMapping;
import com.elitecore.sm.parser.model.FileTypeEnum;
import com.elitecore.sm.parser.model.NATFlowParserMapping;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.parser.model.RegExPattern;
import com.elitecore.sm.parser.model.RegexParserAttribute;
import com.elitecore.sm.parser.model.RegexParserMapping;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.MigrationUtil;

@Service("parserPluginMigrationService")
public class ParserPluginMigrationServiceImpl implements ParserPluginMigrationService {
	
	@Autowired
	private MigrationUtil migrationUtil;
	
	@Autowired
	private DeviceService deviceService;
	/**
	 * This method will return ResponseObject which contains map(key:id,value:ParserMapping object)
	 *
	 * @param asciiParsingPluginFile
	 * @return ResponseObject
	 */
	@Override
	public ResponseObject getParserMapping(String pluginType, String xmlName, String migrationPrefix, int staffId) throws MigrationSMException {
		List<ParserMapping> parserMappingList = new ArrayList<>();
		Map<String, Class<?>> mapOfClasses = migrationUtil.getClasses(xmlName);
		
		ResponseObject responseObject = migrationUtil.getFileContentFromMap(pluginType);
		if (responseObject.isSuccess()) {
			byte[] fileContent = (byte[]) responseObject.getObject();
			responseObject = migrationUtil.getAndValidateMappingEntityObj(xmlName);
			if (responseObject.isSuccess()) {
				MigrationEntityMapping  entityMapping = (MigrationEntityMapping) responseObject.getObject();
				responseObject = migrationUtil.unmarshalObjectFromFile(fileContent, mapOfClasses.get(MigrationConstants.MAP_JAXB_CLASS_KEY),entityMapping.getXsdName(),entityMapping.getXsdName());
				
				if(responseObject.isSuccess() && responseObject.getObject() instanceof AsciiParserPluginEntity) {
					// logic to get AsciiParserMapping
					if (responseObject.getObject() instanceof AsciiParserPluginEntity) {
						AsciiParserPluginEntity asciiParserPluginEntity = (AsciiParserPluginEntity) responseObject.getObject();
						List<AsciiParserPluginEntity.Instance> instanceList = asciiParserPluginEntity.getInstance();
						if (instanceList != null && !instanceList.isEmpty()) {
							int instanceLength = instanceList.size();
							for (int i = instanceLength-1; i >= 0; i--) {
								responseObject = migrationUtil.convertJaxbToSMObject(instanceList.get(i), mapOfClasses.get(MigrationConstants.MAP_SM_CLASS_KEY));
								if(responseObject.isSuccess() && responseObject.getObject() instanceof AsciiParserMapping) {
									AsciiParserMapping mapping = (AsciiParserMapping) responseObject.getObject();
									if(mapping.isKeyValueRecordEnable()){
										mapping.setFileTypeEnum(FileTypeEnum.KEY_VALUE_RECORD);
									}
									else if(mapping.isRecordHeaderEnable()){
										mapping.setFileTypeEnum(FileTypeEnum.RECORD_HEADER);
									}
									else if(mapping.getFileHeaderEnable() || mapping.getFileFooterEnable()){
										mapping.setFileTypeEnum(FileTypeEnum.FILE_HEADER_FOOTER);
									}
									else {
										mapping.setFileTypeEnum(FileTypeEnum.DELIMITER);
									}
									mapping.setName(migrationUtil.getRandomName(migrationPrefix+"_"+EngineConstants.ASCII_PARSING_PLUGIN));

									migrationUtil.setCurrentDateAndStaffId(mapping, staffId);
									
									PluginTypeMaster parserType = (PluginTypeMaster) MapCache.getConfigValueAsObject(EngineConstants.ASCII_PARSING_PLUGIN);

									ResponseObject deviceResponseObject =  deviceService.setDeviceDetails(migrationPrefix, staffId,DecodeTypeEnum.UPSTREAM);
									if (deviceResponseObject.isSuccess()) {
										Device device = (Device) deviceResponseObject.getObject();
										mapping.setDevice(device);
									}
									mapping.setParserType(parserType);
									parserMappingList.add(mapping);
								}
							}
						}
					} //more else if comes here for other parser mapping
					
				}
				else if(responseObject.isSuccess() && responseObject.getObject() instanceof NatflowParserPluginEntity) {
					
					// logic to get AsciiParserMapping
					if (responseObject.getObject() instanceof NatflowParserPluginEntity) {
						
						NatflowParserPluginEntity natflowParserPluginEntity = (NatflowParserPluginEntity) responseObject.getObject();
						
						List<NatflowParserPluginEntity.Instance> instanceList = natflowParserPluginEntity.getInstance();
						
						if (instanceList != null && !instanceList.isEmpty()) {
							int instanceLength = instanceList.size();
							for (int i = instanceLength-1; i >= 0; i--) {
								
								responseObject = migrationUtil.convertJaxbToSMObject(instanceList.get(i), mapOfClasses.get(MigrationConstants.MAP_SM_CLASS_KEY));
								if(responseObject.isSuccess() && responseObject.getObject() instanceof NATFlowParserMapping) {
									NATFlowParserMapping mapping = (NATFlowParserMapping) responseObject.getObject();
									mapping.setName(migrationUtil.getRandomName(migrationPrefix+"_"+EngineConstants.NATFLOW_PARSING_PLUGIN));
			
									migrationUtil.setCurrentDateAndStaffId(mapping, staffId);
									
									PluginTypeMaster parserType = (PluginTypeMaster) MapCache.getConfigValueAsObject(EngineConstants.NATFLOW_PARSING_PLUGIN);

									ResponseObject deviceResponseObject =  deviceService.setDeviceDetails(migrationPrefix, staffId,DecodeTypeEnum.UPSTREAM);
									
									if (deviceResponseObject.isSuccess()) {
										Device device = (Device) deviceResponseObject.getObject();
										mapping.setDevice(device);
									}
									mapping.setParserType(parserType);
									parserMappingList.add(mapping);
								}
							}
						}
					} //more else if comes here for other parser mapping
					
				}
				else if(responseObject.isSuccess() && responseObject.getObject() instanceof RegexParserPluginEntity){
					
					RegexParserPluginEntity regexParserPluginEntity = (RegexParserPluginEntity) responseObject.getObject();
					List<RegexParserPluginEntity.Instance> instanceList = regexParserPluginEntity.getInstance();
										
					if (instanceList != null && !instanceList.isEmpty()) {
						int instanceLength = instanceList.size();
						for (int i = instanceLength-1; i >= 0; i--) {
												
						responseObject = migrationUtil.convertJaxbToSMObject(instanceList.get(i), mapOfClasses.get(MigrationConstants.MAP_SM_CLASS_KEY));
						
						if(responseObject.isSuccess() && responseObject.getObject() instanceof RegexParserMapping) {
							
							RegexParserMapping regexParserMapping = (RegexParserMapping) responseObject.getObject();
							regexParserMapping.setName(migrationUtil.getRandomName(migrationPrefix+"_"+EngineConstants.REGEX_PARSING_PLUGIN));

							migrationUtil.setCurrentDateAndStaffId(regexParserMapping, staffId);
							
							PluginTypeMaster parserType = (PluginTypeMaster) MapCache.getConfigValueAsObject(EngineConstants.REGEX_PARSING_PLUGIN);
							
							List<RegexParserPluginEntity.Instance.PatternList.Pattern> patternList = instanceList.get(i).getPatternList().getPattern();
						
							if (patternList != null) {								
								List<RegExPattern> smRegexPatternList = new ArrayList<>();
								for (Pattern pattern : patternList) {
									
									RegExPattern regexPattern = new RegExPattern();
									regexPattern.setPatternRegExName(migrationUtil.getRandomName(migrationPrefix+"_"+EngineConstants.REGEX_PARSING_PLUGIN));									
									regexPattern.setId(0);
									migrationUtil.setCurrentDateAndStaffId(regexPattern, staffId);
									regexPattern.setPatternRegEx(pattern.getPatternRegex());
									regexPattern.setPatternRegExId(pattern.getPatternRegexid());
									regexPattern.setParserMapping(regexParserMapping);
									
									List<RegexParserAttribute> regexSmParserAttributeList =  new ArrayList<>();
									List<RegexParserPluginEntity.Instance.PatternList.Pattern.AttributeList.Attribute> attributeList = pattern.getAttributeList().getAttribute();
									
									for (Attribute attribute : attributeList) {
										
											RegexParserAttribute regexParserAttribute = new RegexParserAttribute();
											
											regexParserAttribute.setDefaultValue(attribute.getDefaultValue());
											regexParserAttribute.setDescription(attribute.getDescription());
											regexParserAttribute.setRegex(attribute.getRegex());
											regexParserAttribute.setSeqNumber(attribute.getSequenceNo());
											regexParserAttribute.setTrimChars(attribute.getTrimChars());
											regexParserAttribute.setUnifiedField(attribute.getUnifiedField());
											regexParserAttribute.setPattern(regexPattern);
								
											regexSmParserAttributeList.add(regexParserAttribute);
										
									}
									regexPattern.setAttributeList(regexSmParserAttributeList);
									smRegexPatternList.add(regexPattern);				
									
								}
								regexParserMapping.setPatternList(smRegexPatternList);
							}
							regexParserMapping.setParserType(parserType);
							ResponseObject deviceResponseObject =  deviceService.setDeviceDetails(migrationPrefix, staffId,DecodeTypeEnum.UPSTREAM);
							if (deviceResponseObject.isSuccess()) {
								Device device = (Device) deviceResponseObject.getObject();
								regexParserMapping.setDevice(device);
							}
							parserMappingList.add(regexParserMapping);
							
						}
					}
				}
			}  
	
				if (responseObject.isSuccess()) {
					// list to map -> map(key:id,value:ParserMapping object)
					responseObject.setObject(parserMappingList.stream().collect(Collectors.toMap(ParserMapping::getId, Function.identity())));
				}
			
			}
				
		}
		return responseObject;
	}
	
}
