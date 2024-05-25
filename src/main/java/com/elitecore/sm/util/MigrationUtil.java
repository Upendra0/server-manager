package com.elitecore.sm.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.MappingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.elitecore.sm.common.constants.MigrationConstants;
import com.elitecore.sm.common.exception.BooleanException;
import com.elitecore.sm.common.exception.EnumException;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.migration.model.MigrationEntityMapping;
import com.elitecore.sm.services.model.Service;

/**
 * @author Ranjitsinh Reval
 *
 */
public class MigrationUtil {

	private static Logger logger = Logger.getLogger(MigrationUtil.class);


	private DozerBeanMapper mapper = new DozerBeanMapper();
	
	
	private Map<String,byte[]> entityFileContent;

	@Autowired
	private ServletContext servletContext;

	private MigrationUtil() {
		List<String> mappingFiles = new ArrayList<>();
		mappingFiles.add("com/elitecore/sm/migration/mapping/DozerMapping.xml");
		mapper.setMappingFiles(mappingFiles);
	}

	/**
	 * @author brijesh.soni
	 * @param prefix
	 * @return This method will append random string of 5 character after given
	 *         prefix
	 */
	public String getRandomName(String prefix) {
		String characterList = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int characterLength = characterList.length();
		int randomStringLength = 5;
		StringBuilder randomString = new StringBuilder();
		randomString.append(prefix);
		randomString.append("_");
		for (int i = randomStringLength - 1; i >= 0; i--) {
			randomString.append(characterList
					.charAt(getRandomNumber(characterLength)));
		}
		return randomString.toString();
	}

	/**
	 * @author brijesh.soni
	 * @param length
	 * @return This method will return random number
	 */
	private int getRandomNumber(int number) {
		Random randomGenerator = new Random();//NOSONAR
		int randomInt = randomGenerator.nextInt(number);
		if (randomInt - 1 == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}
	}
	
	
	public ResponseObject unmarshalObjectFromFile(byte [] fileData, Class<?> clazz, String xsdFileName, String xmlName) throws MigrationSMException {
		ResponseObject responseObject = new ResponseObject();

		
		if(clazz == null){
			responseObject.setResponseCode(ResponseCode.MIGRATION_UNMARSHAL_FAIL);
			responseObject.setSuccess(false);
			throw new MigrationSMException(responseObject, "Class object found null ");
		}
		
		final JSONArray finaljArray = new JSONArray();
		//boolean isValidate = false;
		try {
		/*	ValidationEventHandler validator = event -> {
				JSONArray jArray = new JSONArray();
				jArray.put(event.getLocator().getLineNumber());
				jArray.put(event.getMessage());
				finaljArray.put(jArray);
				return true;
			};
			Schema schema = null;
			if (xsdFileName != null && !"".equals(xsdFileName)) {
				String xsdFilePath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH)
						+ File.separator + xsdFileName;
				SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				schema = sf.newSchema(new File(xsdFilePath));
				isValidate = false;
			}*/
			
			JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			/*if (isValidate) {
				jaxbUnmarshaller.setSchema(schema);
				jaxbUnmarshaller.setEventHandler(validator);
			}*/

			
			InputStream inputStream = new ByteArrayInputStream(fileData);
			Object object = jaxbUnmarshaller.unmarshal(inputStream);

			if (finaljArray.length() > 0) {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.MIGRATION_XSD_VALIDATION_FAIL);
				responseObject.setObject(finaljArray);
				responseObject.setModuleName(xmlName);
			} else {
				responseObject.setSuccess(true);
				responseObject.setObject(object);
			}
		} catch (JAXBException e) {
			JSONArray jArray = new JSONArray();
			logger.error("Error ooccurred  in convertXMLToServiceInstance:" + e);
			if (e.getCause() != null) {
				jArray.put(new String(e.getCause().getMessage()));
			} else {
				jArray.put(new String(e.getMessage()));
			}
			finaljArray.put(jArray);
			logger.error("Failed to unamarshall server instance xml file due to  :" + e.getMessage());
			responseObject.setResponseCode(ResponseCode.MIGRATION_UNMARSHAL_FAIL);
			responseObject.setSuccess(false);
			responseObject.setObject(finaljArray);
			responseObject.setModuleName(xmlName);
			throw new MigrationSMException(responseObject, "Error occurred  in convertXMLToServiceInstance:" + e);
		} /*catch (SAXException e) {
			JSONArray jArray = new JSONArray();
			logger.error("Error occurred during jaxb convesion :" + e);
			logger.error(e.getCause().getMessage());
			jArray.put(new String(e.getMessage()));
			responseObject.setResponseCode(ResponseCode.MIGRATION_UNMARSHAL_FAIL);
			responseObject.setSuccess(false);
			finaljArray.put(jArray);
			responseObject.setObject(finaljArray);
			responseObject.setModuleName(xmlName);
			throw new MigrationSMException(responseObject, "Error occurred during jaxb convesion :" + e);
		}*/
		
		return responseObject;
	}
	
	/**
	 * Method will convert source to destination object for simple classes
	 * without any dozer custom converters.
	 * 
	 * @param file
	 * @param servletContext
	 * @param xsdFileName
	 * @param dozerType
	 * @return
	 */
	public ResponseObject getSMAndJaxbObjectFromXml(byte [] fileData, String xsdFileName, String xmlName) throws MigrationSMException {

		ResponseObject responseObject;
		final JSONArray finaljArray = new JSONArray();
		Map<String, Object> mapOfObjects = new HashMap<>();
		Map<String, Class<?>> mapOfClasses = getClasses(xmlName);
		responseObject = unmarshalObjectFromFile(fileData,mapOfClasses.get(MigrationConstants.MAP_JAXB_CLASS_KEY),xsdFileName,xmlName);
		if (responseObject.isSuccess()) {
			try {
				mapOfObjects.put(MigrationConstants.MAP_JAXB_CLASS_KEY,responseObject.getObject());
				Object object = mapper.map(responseObject.getObject(),mapOfClasses.get(MigrationConstants.MAP_SM_CLASS_KEY));
				mapOfObjects.put(MigrationConstants.MAP_SM_CLASS_KEY, object);
				responseObject.setSuccess(true);
				responseObject.setObject(mapOfObjects);
			
			} catch(EnumException enumException){
				JSONArray jArray = new JSONArray();
				logger.info("Failed to unmarshall server instance xml file due to : " +  enumException.getMessage());
				jArray.put(xmlName);
				jArray.put(enumException.getMessage());
				finaljArray.put(jArray);
				responseObject.setObject(finaljArray);
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DOZER_MAPPING_FAIL);
				throw new MigrationSMException(responseObject, "EnumException Error occurred  in getSMAndJaxbObjectFromXml:" + enumException);
			}
			catch(BooleanException booleanException){
				JSONArray jArray = new JSONArray();
				logger.info("Failed to unmarshall server instance xml file due to : " +  booleanException.getMessage());
				jArray.put(xmlName);
				jArray.put(booleanException.getMessage());
				finaljArray.put(jArray);
				responseObject.setObject(finaljArray);
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DOZER_MAPPING_FAIL);
				throw new MigrationSMException(responseObject, "EnumException Error occurred  in getSMAndJaxbObjectFromXml:" + booleanException);
			}
			catch (MappingException me) {
				JSONArray jArray = new JSONArray();
				logger.error("MappingException Error ooccurred  in getSMAndJaxbObjectFromXml:" + me);
				if (me.getCause() != null) {
					  jArray.put(xmlName);
					jArray.put(new String(me.getCause().getMessage()));
				  
				   
				} else {
					jArray.put(xmlName);
					jArray.put(new String(me.getMessage()));
					
				}
				
				finaljArray.put(jArray);
				
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DOZER_MAPPING_FAIL);
				responseObject.setObject(finaljArray);
				throw new MigrationSMException(responseObject, "MappingException Error ooccurred  in getSMAndJaxbObjectFromXml:" + me);
			}
		}
		return responseObject;
	}
	/**
	 * Method will get all mapping entity configuration from map cache.
	 * @param xmlName
	 * @return
	 */
	public Map<String, Class<?>> getClasses(String xmlName) {
		logger.debug("Geting class object for xml " + xmlName);
		Map<String, Class<?>> mapOfClass = new HashMap<>();
		MigrationEntityMapping entityMapping  = getEntityMappingObject(xmlName);
		if(entityMapping != null ){
			Class<?> jaxbClass = getFullClassName(entityMapping.getJaxbClassName());
			Class<?> smClass = getFullClassName(entityMapping.getSmClassName());
			mapOfClass.put(MigrationConstants.MAP_JAXB_CLASS_KEY, jaxbClass);
			mapOfClass.put(MigrationConstants.MAP_SM_CLASS_KEY, smClass);
			logger.info("Mapping entity object found successfully.");
		}else {
			mapOfClass.put(MigrationConstants.MAP_JAXB_CLASS_KEY, null);
			mapOfClass.put(MigrationConstants.MAP_SM_CLASS_KEY, null);
			logger.info("Failed to get mapping object for xml file " + xmlName);
		}
		return mapOfClass;
	}

	
	
	/**
	 * Method will get entity mapping object from map cache from xml name.
	 * @param xmlName
	 * @return
	 */
	public MigrationEntityMapping getEntityMappingObject(String xmlName){
		logger.debug("Geting mapping Entity class object for xml " + xmlName);
		Object object = MapCache.getConfigValueAsObject(xmlName);
		if (object != null && object instanceof MigrationEntityMapping) {
			return (MigrationEntityMapping) object;
		}else{
			return null;
		}
	}
	
	
	/**
	 * Method will get and validate mapping entity object.
	 * @param xmlFileName
	 * @return
	 */
	public ResponseObject getAndValidateMappingEntityObj(String xmlFileName){
		logger.info("xml name::"+xmlFileName);
		
		ResponseObject responseObject = new ResponseObject();
		Object object = MapCache.getConfigValueAsObject(xmlFileName);
		logger.info("object::"+object);
		if (object != null && object instanceof MigrationEntityMapping) {
			responseObject.setSuccess(true);
			responseObject.setObject((MigrationEntityMapping) object);
			logger.info("Mapping entity  details object get successfully.");
		}else{
			logger.info("Failed to get mapping entity details object from map cache");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_ENTITY_MAPPING_DETAILS);
			responseObject.setArgs(new Object[] {xmlFileName});
		}
		
		return responseObject;
	}
	
	/**
	 * Method will get file content as byte array from map and return entity file content.
	 * @param entityKey
	 * @return
	 */
	public ResponseObject getFileContentFromMap(String entityKey){
		logger.debug("Fetching file content from map " + entityKey);
		ResponseObject responseObject = new ResponseObject();
		Map<String, byte[]> fileContent = getEntityFileContent();
		if (fileContent.get(entityKey) != null) {
			logger.info("File content get successfully for enityt key " + entityKey);
			responseObject.setSuccess(true);
			responseObject.setObject((byte[])fileContent.get(entityKey));
		}else{
			logger.info("Failed to get file content for key " + entityKey);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_BYTE_DATA);
			responseObject.setArgs(new Object[] {entityKey});
		}

		return responseObject;
	}
	
	
	
	/**
	 * Method will set current logged in staff id and current date to the
	 * converted entity.
	 * @param entity
	 * @param staffId
	 */
	public void setCurrentDateAndStaffId(BaseModel entity, int staffId) {
		logger.debug("Setting base model property for entity " + entity.getClass().getName());
		entity.setCreatedDate(new Date());
		entity.setCreatedByStaffId(staffId);
		entity.setLastUpdatedDate(new Date());
		entity.setLastUpdatedByStaffId(staffId);
	}

	/**
	 * Method will convert source to destination object for simple classes
	 * without any dozer custom converters.
	 * 
	 * @param sourceObject
	 * @param destinationClass
	 * @return
	 */
	public ResponseObject convertJaxbToSMObject(Object sourceObject, Class<?> destinationClass) throws MigrationSMException {
		logger.debug("converting jaxb to sm object");

		ResponseObject responseObject = new ResponseObject();

		try {
			Object convertedObject = mapper.map(sourceObject, destinationClass);
			responseObject.setObject(convertedObject);
			responseObject.setSuccess(true);
			logger.info("JAXB to SM object conversion done successfully.");
		} catch (MappingException me) {
			logger.error(me.getMessage(), me);
			if (me.getCause() != null) {
				logger.error("Unable to unamarshall server instance xml file due to  :"
						+ me.getCause().getMessage());
				responseObject.setObject(new String(me.getCause().getMessage()));
			} else {
				logger.error("Unable to unamarshall server instance xml file due to  :"
						+ me.getMessage());
				responseObject.setObject(new String(me.getMessage()));
			}

			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DOZER_MAPPING_FAIL);
			throw new MigrationSMException(responseObject, "Error occurred  in convertJaxbToSMObject:" + me);
		} catch (Exception e) {
			logger.error("Error :" + e);
			logger.error("Error :" + e.getCause().getMessage());
			responseObject.setResponseCode(ResponseCode.DOZER_MAPPING_FAIL);
			responseObject.setSuccess(false);
			throw new MigrationSMException(responseObject, "Error occurred  in convertJaxbToSMObject:" + e);
		}

		return responseObject;
	}

	/**
	 * Method will get full class object from name.
	 * 
	 * @param className
	 * @return
	 */
	public Class<?> getFullClassName(String className) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return clazz;
	}

	/**
	 * @param applicationOrder
	 * @param driverName
	 * @return
	 */
	public String getDriverNameFromApplicationOrder(int applicationOrder,String driverName) {
		String name;
		if (applicationOrder > 0) {
			name = applicationOrder + "-" + driverName;
		} else {
			name = driverName;
		}
		return name.trim();
	}
	
	/**
	 * 
	 * @param object
	 * @param applicationOrder
	 * @param driverNamePrefix
	 * @param service
	 * @return
	 */
	public Drivers getUpdatedDriverObject(Drivers driver, int applicationOrder,
			String driverNamePrefix, Service service) {
		driver.setName(getRandomName(driverNamePrefix));
		driver.setApplicationOrder(applicationOrder);
		driver.setService(service);
		return driver;
	}

	public String getServiceDirectoryPath(String directoryPath,	String serviceDirectoryName,
			String serviceInstanceId) {
		return directoryPath + File.separator
				+ MigrationConstants.ENGINE_SERVICE_FOLDER + File.separator
				+ serviceDirectoryName + File.separator + serviceInstanceId;
		
	}
	
	public Map<String, byte[]> getEntityFileContent() {
		return entityFileContent;
	}

	
	public void setEntityFileContent(Map<String, byte[]> entityFileContent) {
		this.entityFileContent = entityFileContent;
	}
	
	/**
	 * @author brijesh.soni
	 * @param ajaxResponse
	 * @return it will return fixed length (<4000) json object to store in remark column
	 */
	public JSONObject getFixedLengthJsonObjectFromAjaxResponse(AjaxResponse ajaxResponse) {
		final String objectArrayKey = "object";
		final int processObjectCount = 30;
		JSONObject jsonObject = new JSONObject(ajaxResponse);
		if(jsonObject.toString().length() > 4000) {
			JSONArray newObjectArray = new JSONArray();
			JSONArray objectArray = jsonObject.getJSONArray(objectArrayKey);
			if(objectArray != null && objectArray.length() >= processObjectCount) {
				for(int i = 0; i < processObjectCount; i++){
					newObjectArray.put(objectArray.get(i));
				}
			}
			jsonObject.remove(objectArrayKey);
			jsonObject.put(objectArrayKey, newObjectArray);
		}
		return jsonObject;
	}
	
	/**
	 * @author brijesh.soni
	 * @param string
	 * @return it will return fixed length (<4000) string to store in remark column
	 */
	public String getFixedLengthString(String string) {
		if(string != null) {
			if(string.length() > 4000) {
				return string.substring(0, 4000);
			} else {
				return string;
			}
		}
		return null;
	}
	
}
