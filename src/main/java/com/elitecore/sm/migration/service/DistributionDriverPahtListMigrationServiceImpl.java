/**
 * 
 */
package com.elitecore.sm.migration.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.DecodeTypeEnum;
import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.composer.model.ASCIIComposerAttr;
import com.elitecore.sm.composer.model.ASCIIComposerMapping;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.model.VendorType;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.drivers.model.DistributionDriver;
import com.elitecore.sm.migration.model.AsciiComposerPluginEntity;
import com.elitecore.sm.migration.model.AsciiComposerPluginEntity.Instance;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.pathlist.model.CharRenameOperation;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.services.service.MigrationServiceImpl;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.MigrationUtil;


/**
 * @author Ranjitsinh Reval
 *
 */
@org.springframework.stereotype.Service("distributionDriverPahtListMigrationService")
public class DistributionDriverPahtListMigrationServiceImpl implements DistributionDriverPahtListMigrationService {

	private static Logger logger = Logger.getLogger(MigrationServiceImpl.class);
	
	@Autowired
	private MigrationUtil migrationUtil;
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	AsciiComposerMigrationService asciiComposerMigrationService;
	
	
	
	/**
	 * Method will UN-MARSHAL and get distribution driver path list and its dependents.
	 * @param jaxbPathList
	 * @param distributionDriver
	 * @param staffId
	 * @param migrationPrefix
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseObject getDistributionDriverPathListDetails(List<?> jaxbPathList, DistributionDriver distributionDriver, int staffId, String migrationPrefix) throws MigrationSMException {
		logger.debug("getting distribution driver pathlist configuration!");
		ResponseObject responseObject  = new ResponseObject();
		Map<Integer, AsciiComposerPluginEntity.Instance> instanceMap = null;

		boolean isAscciiPluginFilRead = false;
		Class<?> destinationClass = migrationUtil.getFullClassName("com.elitecore.sm.pathlist.model.DistributionDriverPathList");
		Class<?> pluginDestinationClass = migrationUtil.getFullClassName("com.elitecore.sm.composer.model.ASCIIComposerMapping");
		Class<?> attributeDestinationClass = migrationUtil.getFullClassName("com.elitecore.sm.composer.model.ASCIIComposerAttr");

		List<PathList> collectionDriverPathList = new ArrayList<>();

		if(jaxbPathList != null && !jaxbPathList.isEmpty()){
			for (Object jaxbPath : jaxbPathList) {

				responseObject =  migrationUtil.convertJaxbToSMObject(jaxbPath, destinationClass);
				if(responseObject.isSuccess()){
					
					Object smPathList = responseObject.getObject();
					
					if (smPathList instanceof DistributionDriverPathList) {
						DistributionDriverPathList smDistributionDriverPathList = (DistributionDriverPathList) smPathList;
						migrationUtil.setCurrentDateAndStaffId(smDistributionDriverPathList, staffId); // Setting current,last date and staff id for create and update for entity.
						smDistributionDriverPathList.setName(migrationUtil.getRandomName(migrationPrefix + "Distribution_PathList"));
						smDistributionDriverPathList.setId(0);
						smDistributionDriverPathList.setDriver(distributionDriver);

						List<Composer> composers = smDistributionDriverPathList.getComposerWrappers();
						if (composers != null && !composers.isEmpty() ) {
							
							for (int i = 0; i < composers.size(); i++) {
								Composer composer = composers.get(i);
								logger.info("composor id in distribution driver path list : " + composer.getId());
								
								//Setting composer char rename operation list name
								List<CharRenameOperation> charRenameOperationList = composer.getCharRenameOperationList();
								if(charRenameOperationList != null && !charRenameOperationList.isEmpty()){
									for (CharRenameOperation charRenameOperation : charRenameOperationList) {
										
										charRenameOperation.setId(0);
										charRenameOperation.setSvcFileRenConfig(null);
										charRenameOperation.setComposer(composer);
										migrationUtil.setCurrentDateAndStaffId(charRenameOperation, staffId);
									}
									
								}	
								
								if (EngineConstants.ASCII_COMPOSER_PLUGIN.equalsIgnoreCase(composer.getComposerType().getAlias())) {

									if(!isAscciiPluginFilRead){
										responseObject = asciiComposerMigrationService.getAsciiComposerPluginList(pluginDestinationClass, migrationPrefix, staffId);
										if (responseObject.isSuccess()) {
											instanceMap = (Map<Integer, Instance>) responseObject.getObject();
											isAscciiPluginFilRead = true;
										}else{
											throw new MigrationSMException(responseObject, "Failed to get ascii composer plugin details.Check logs for more details.");
										}
									}
								
									if(instanceMap != null && !instanceMap.isEmpty()){
										
										AsciiComposerPluginEntity.Instance jaxbPlugin = instanceMap.get(composer.getId());
										responseObject = migrationUtil.convertJaxbToSMObject(jaxbPlugin, pluginDestinationClass);
										if(responseObject.isSuccess()){
											Object smPlugin = responseObject.getObject();

											if (smPlugin instanceof ASCIIComposerMapping) {
												
												ResponseObject tempResponseObject =  deviceService.setDeviceDetails(migrationPrefix, staffId, DecodeTypeEnum.DOWNSTREAM);
												//Setting composer details
												composer.setComposerType((PluginTypeMaster) MapCache.getConfigValueAsObject(composer.getComposerType().getAlias()));
												composer.setId(0);
												composer.setMyDistDrvPathlist(smDistributionDriverPathList);
												composer.setName(migrationUtil.getRandomName(migrationPrefix + "_composer"));
												migrationUtil.setCurrentDateAndStaffId(composer, staffId); // Setting current date and last and created date for entity.
												
												if(tempResponseObject.isSuccess()){
													logger.info("Found ascii composer plugin.");
													ComposerMapping composerMapping = (ASCIIComposerMapping) smPlugin;
													composerMapping.setName(migrationUtil.getRandomName(migrationPrefix + "_ascii"));
													composerMapping.setDevice((Device) tempResponseObject.getObject());
													composerMapping.setId(0);

													List<ComposerAttribute> attributeList = new ArrayList<>();
													composerMapping.setComposerType(composer.getComposerType());
													migrationUtil.setCurrentDateAndStaffId(composerMapping, staffId); // Setting current date and last and created date for entity.

													List<AsciiComposerPluginEntity.Instance.AttributeList.Attribute> jaxbAttributeList = jaxbPlugin.getAttributeList().getAttribute();
													for (AsciiComposerPluginEntity.Instance.AttributeList.Attribute attribute : jaxbAttributeList) {
														
														responseObject = migrationUtil.convertJaxbToSMObject(attribute, attributeDestinationClass);
														
														if(responseObject.isSuccess()){
															Object smAttribute  = responseObject.getObject();
															if (smAttribute instanceof ASCIIComposerAttr) {
																ASCIIComposerAttr tempAttribute = (ASCIIComposerAttr) smAttribute;
																tempAttribute.setId(0);
																migrationUtil.setCurrentDateAndStaffId(tempAttribute, staffId);
																tempAttribute.setPaddingType(PositionEnum.LEFT);
																attributeList.add(tempAttribute);
															}
														}
													}
													composerMapping.setAttributeList(attributeList);
													logger.debug("setting composer mapping to composer........");
													composer.setComposerMapping(composerMapping);
												}else{
													responseObject = tempResponseObject;
													break;
												}
											}
										}
									}
								}//Add more condition for other composer
							}
						} else {
							logger.debug("Plugin not configure for pathlist.");
							responseObject.setSuccess(true);
						}
						collectionDriverPathList.add(smDistributionDriverPathList);
					}else{
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.DISTRIBUTION_PATH_LIST_FAIL);
						break;
					}
				}
			}
			responseObject.setObject(collectionDriverPathList);
		}else{
			responseObject.setSuccess(true);
			logger.debug("No path list for driver " + distributionDriver.getName());
		}

		return responseObject;
	}

	
	/**
	 * Method will set device object with its all dependents.
	 * @param migrationPrefix
	 * @param staffId
	 * @return
	 * @throws MigrationSMException 
	 * 
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseObject setDeviceDetails(String migrationPrefix, int staffId) throws MigrationSMException{
		
		logger.debug("Creating device , device type and vendor type");
		ResponseObject responseObject = new ResponseObject();
		
		//Setting device type details.
		DeviceType deviceType = new DeviceType();
		deviceType.setName(migrationUtil.getRandomName(migrationPrefix + "_devicetype"));	
		deviceType.setId(0);
		deviceType.setDescription("This is created for migration use only.");
		migrationUtil.setCurrentDateAndStaffId(deviceType, staffId);
		
		VendorType vendorType = new VendorType();
		vendorType.setName(migrationUtil.getRandomName(migrationPrefix + "_vendortype"));	
		vendorType.setId(0);
		vendorType.setDescription("This is created for migration use only.");
		migrationUtil.setCurrentDateAndStaffId(vendorType, staffId);
		Device device = new Device(0, migrationUtil.getRandomName(migrationPrefix + "device"), "Migration device ", DecodeTypeEnum.DOWNSTREAM.getValue(), BaseConstants.USER_DEFINED_DEVICE, deviceType, vendorType);
		try {
			logger.info("Adding device details");
			responseObject = deviceService.createDevice(device, staffId, BaseConstants.BOTH, BaseConstants.ACTION_TYPE_ADD);
			responseObject.setObject(device);
		} catch (SMException e) {
			logger.error(e.getMessage(), e);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DEVICE_CREATE_FAIL);
			throw new MigrationSMException(responseObject, e.getMessage());
		}
		
		return responseObject;
	}
	
	
}
