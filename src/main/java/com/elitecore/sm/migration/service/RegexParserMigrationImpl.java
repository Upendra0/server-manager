package com.elitecore.sm.migration.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.MigrationConstants;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.migration.model.AsciiComposerPluginEntity;
import com.elitecore.sm.migration.model.MigrationEntityMapping;
import com.elitecore.sm.services.service.MigrationServiceImpl;
import com.elitecore.sm.util.MigrationUtil;

public class RegexParserMigrationImpl implements RegexParserMigration{

	
	@Autowired
	private MigrationUtil migrationUtil;
	
	@Autowired
	private DeviceService deviceService;
	
	private static Logger logger = Logger.getLogger(MigrationServiceImpl.class);
	
	
	/**
	 * Method will get All ASCII composer plug-in list
	 * @param migrationPrefic
	 * @param staffId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseObject getRegexParserPluginList(Class<?> destinationClass, String migrationPrefix, int staffId) throws MigrationSMException {
		logger.debug("Going to read ascii composer plugin file.");
		
		String keyName = EngineConstants.ASCII_COMPOSER_PLUGIN;
		ResponseObject responseObject = migrationUtil.getFileContentFromMap(keyName);
		if (responseObject.isSuccess()) {
			byte[] fileContent = (byte[]) responseObject.getObject();
			responseObject = migrationUtil.getAndValidateMappingEntityObj(MigrationConstants.ASCII_COMPOSER_PLUGIN_XML);
			if (responseObject.isSuccess()) {
				MigrationEntityMapping  entityMapping = (MigrationEntityMapping) responseObject.getObject();
				responseObject = migrationUtil.getSMAndJaxbObjectFromXml(fileContent, entityMapping.getXsdName(), entityMapping.getXmlName());
				if(responseObject.isSuccess()){
					Map<String, Object> returnObjects = (Map<String, Object>) responseObject.getObject(); 
					AsciiComposerPluginEntity jaxbPlugin = (AsciiComposerPluginEntity) returnObjects.get(MigrationConstants.MAP_JAXB_CLASS_KEY);;
					List<AsciiComposerPluginEntity.Instance> instanceList = jaxbPlugin.getInstance();
					Map<Integer, AsciiComposerPluginEntity.Instance> inatanceMap =  instanceList.stream().collect(Collectors.toMap(AsciiComposerPluginEntity.Instance::getId,Function.identity()));
					responseObject.setObject(inatanceMap);
					responseObject.setSuccess(true);
				}
			}
		}
		return responseObject;
	}
	
}
