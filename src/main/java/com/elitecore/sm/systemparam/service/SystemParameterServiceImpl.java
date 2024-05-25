/**
 * 
 */
package com.elitecore.sm.systemparam.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.SystemBackUpPathOptionEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.iam.dao.AccessGroupDAO;
import com.elitecore.sm.iam.dao.StaffDAO;
import com.elitecore.sm.iam.model.AccessGroup;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.iam.service.StaffService;
import com.elitecore.sm.systemparam.dao.ImageModelDao;
import com.elitecore.sm.systemparam.dao.SystemParamDataDao;
import com.elitecore.sm.systemparam.dao.SystemParamGroupDataDao;
import com.elitecore.sm.systemparam.dao.SystemParamValuePoolDataDao;
import com.elitecore.sm.systemparam.model.ImageModel;
import com.elitecore.sm.systemparam.model.SystemParameterData;
import com.elitecore.sm.systemparam.model.SystemParameterFormWrapper;
import com.elitecore.sm.systemparam.model.SystemParameterGroupData;
import com.elitecore.sm.systemparam.model.SystemParameterValuePoolData;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * @author vandana.awatramani
 *
 */
@Service(value = "systemParameterService")
public class SystemParameterServiceImpl implements SystemParameterService {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	private SystemParamDataDao systemParamDataDao;

	@Autowired
	private SystemParamGroupDataDao systemParamGroupDataDao;

	@Autowired
	private SystemParamValuePoolDataDao systemParamValuePoolDataDao;
	
	@Autowired
	private ImageModelDao imageModelDao;
	
	@Autowired
	private AccessGroupDAO accessGroupDAO;
	
	@Autowired
	private StaffDAO staffDAO;
	
	@Autowired
	private StaffService staffService;
		
	@Autowired
	ServletContext servletContext;
	
	private static String authServerUrl;
	private static String realm;
	private static final String MASTER_REALM = "master";
	private static final String CLIENT_ID = "admin-cli";
	/**
	 * Insert system Parameter Group in Database
	 */
	@Override
	@Transactional
	public ResponseObject addSystemParameterGroup(SystemParameterGroupData systemParamGroup) {
		ResponseObject responseObject=new ResponseObject();
		systemParamGroupDataDao.save(systemParamGroup);
		if(systemParamGroup.getId()!= 0){
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SYSTEM_PARAM_GROUP_INSERT_SUCCESS);
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SYSTEM_PARAM_GROUP_INSERT_FAIL);
		}
		return responseObject;
	}

	/**
	 * Insert System Parameter in database
	 */
	@Override
	@Transactional
	public ResponseObject addSystemParameter(SystemParameterData systemParam) {
		ResponseObject responseObject=new ResponseObject();
		systemParamDataDao.save(systemParam);
		if(systemParam.getId()!= 0){
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SYSTEM_PARAMETER_INSERT_SUCCESS);
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SYSTEM_PARAMETER_INSERT_FAIL);
		}
		return responseObject;
	}
	
	/**
	 * Insert System Parameter Value pool in database
	 */
	@Override
	@Transactional
	public ResponseObject addSystemParameterValuePool(SystemParameterValuePoolData systemParameterValuePool) {
		ResponseObject responseObject=new ResponseObject();
		systemParamValuePoolDataDao.save(systemParameterValuePool);
		
		if(systemParameterValuePool.getId()!= 0){
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SYSTEM_PARAM_VALUEPOOL_INSERT_SUCCESS);
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SYSTEM_PARAM_VALUEPOOL_INSERT_FAIL);
		}
		return responseObject;
	}

	/**
	 * Update System Parameter
	 */
	@Override
	@Transactional
	public ResponseObject updateSystemParameters(List<SystemParameterData> listOfParams) {
		
		SystemParameterService sysetemParamImpl = (SystemParameterService) SpringApplicationContext.getBean("systemParameterService");	
		for (SystemParameterData systemParam : listOfParams) {			
			sysetemParamImpl.updateSystemParameters(systemParam);
		}
		
		ResponseObject responseObject=new ResponseObject();
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.SYSTEM_PARAMETER_UPDATE_SUCCESS);
        return responseObject;
	}

	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_GENERAL_PARAMETER,actionType = BaseConstants.SM_UPDATE_ACTION_BULK_LIST,  currentEntity = SystemParameterData.class, ignorePropList= "parameterDetail,parameterGroup,errorMessage,displayOrder,passwordDescription")
	public ResponseObject updateSystemParameters(SystemParameterData systemParameterData) {
		ResponseObject responseObject=new ResponseObject();
		responseObject.setSuccess(true);
		systemParamDataDao.merge(systemParameterData);
		responseObject.setObject(systemParameterData);
		 return responseObject;
	}
	
	
	/**
	 * Set Value pool detail list in system parameter
	 */
	@Override
	@Transactional(readOnly=true)
	public List<SystemParameterData> setValuePoolDetail(List<SystemParameterData> sysParamList,List<SystemParameterValuePoolData> sysValuePoolList) {
		List<SystemParameterValuePoolData> sysValuePoolSet;
		
			for (SystemParameterData sysparamData : sysParamList) {
				sysValuePoolSet = new ArrayList<>();
				int sysparamId = sysparamData.getId();
				for (SystemParameterValuePoolData valuePool : sysValuePoolList) {
					int sysParamIdFromPool = valuePool.getParentSystemParameter().getId();
					if (sysparamId == sysParamIdFromPool)
						sysValuePoolSet.add(valuePool);
				}
				sysparamData.setParameterDetail(sysValuePoolSet);
			}
			return sysParamList;
		} 
	
	/**
	 * Get All system parameter and add into cache
	 */
	@SuppressWarnings("deprecation")
	@Override
	@Transactional(readOnly=true)
	public SystemParameterFormWrapper loadAllSystemParameter() throws SMException{
		 
		//load System Parameter
		List<SystemParameterData> sysParamList=systemParamDataDao.loadSystemParameterList();
		//load System Parameter Value Pool
		List<SystemParameterValuePoolData> sysParamValuePool=systemParamDataDao.loadSystemParamValuePoolData();
			
		sysParamList=setValuePoolDetail(sysParamList,sysParamValuePool);
		
		List<SystemParameterData> generalParamlist=new ArrayList<>();
		List<SystemParameterData> pwdParamlist=new ArrayList<>();
		List<SystemParameterData> custParamlist=new ArrayList<>();
		List<SystemParameterData> custLogoParamlist=new ArrayList<>();
		List<SystemParameterData> fileReprocessingParamlist=new ArrayList<>();
		List<SystemParameterData> emailParamList=new ArrayList<>();
		List<SystemParameterData> emailFooterLogoList=new ArrayList<>();
		List<SystemParameterData> ldapParamList=new ArrayList<>();
        List<SystemParameterData> ssoParamList=new ArrayList<>();
		
		SystemParameterFormWrapper formWrapper = new SystemParameterFormWrapper();
		
		if(sysParamList!=null){
			int size=sysParamList.size();
			for(int i=0;i<size;i++){
				SystemParameterData sysParamData=sysParamList.get(i);
				String sysGroupName=sysParamData.getParameterGroup().getName();
				if(SystemParametersConstant.GENERAL_PARAMETERS.equals(sysGroupName)){					
					generalParamlist.add(sysParamData);
				}else if(SystemParametersConstant.PASSWORD_PARAMETERS.equals(sysGroupName)){
					pwdParamlist.add(sysParamData);
				}else if(SystemParametersConstant.FILE_REPROCESSING_PARAMETERS.equals(sysGroupName)) {
					fileReprocessingParamlist.add(sysParamData);
				}else if(SystemParametersConstant.EMAIL_PARAM.equals(sysGroupName)) {
					if(SystemParametersConstant.FROM_EMAIL_PASSWORD.equals(sysParamData.getAlias()) && sysParamData.getValue()!=null){
						sysParamData.setValue(EliteUtils.decryptData(sysParamData.getValue()));
					}
					emailParamList.add(sysParamData);
				}else if(SystemParametersConstant.SSO_PARAM.equals(sysGroupName)) {	
					if(sysParamData.getAlias().equalsIgnoreCase("DEFAULT_SSO_ACCESS_GROUP")) {
						List<String> accessGroups = new ArrayList<>();
							accessGroups.add("Select Access Group");
						List<String> accessGroups1 = accessGroupDAO.getAllAccessGroupByStaffType(BaseConstants.SSO_STAFF);
						List<SystemParameterValuePoolData> systemParameterValuePoolDatas = new ArrayList<SystemParameterValuePoolData>();
						Integer count = 0;
						if(accessGroups1 != null && !accessGroups1.isEmpty())
							accessGroups.addAll(accessGroups1);
						for(String accessGroup : accessGroups) {
							SystemParameterValuePoolData systemParameterValuePoolData = new SystemParameterValuePoolData();
							systemParameterValuePoolData.setId(++count);
							systemParameterValuePoolData.setName(accessGroup);
							systemParameterValuePoolData.setValue(accessGroup);
							systemParameterValuePoolData.setParentSystemParameter(sysParamData);
							systemParameterValuePoolDatas.add(systemParameterValuePoolData);
						}
						sysParamData.setParameterDetail(systemParameterValuePoolDatas);						
					}  else if(SystemParametersConstant.SSO_ADMIN_PASSWORD.equals(sysParamData.getAlias()) && sysParamData.getValue()!=null){
						sysParamData.setValue(EliteUtils.decryptData(sysParamData.getValue()));
					}
					ssoParamList.add(sysParamData);
				}else if(SystemParametersConstant.LOGIN_PARAM.equals(sysGroupName)) {
					if(sysParamData.getAlias().equalsIgnoreCase("DEFAULT_ACCESS_GROUP")) {
						List<String> accessGroups = new ArrayList<>();
							accessGroups.add("Select Access Group");
						List<String> accessGroups1 = accessGroupDAO.getAllAccessGroupByStaffType(BaseConstants.LDAP_STAFF);
						List<SystemParameterValuePoolData> systemParameterValuePoolDatas = new ArrayList<SystemParameterValuePoolData>();
						Integer count = 0;
						if(accessGroups1 != null && !accessGroups1.isEmpty())
							accessGroups.addAll(accessGroups1);
						for(String accessGroup : accessGroups) {
							SystemParameterValuePoolData systemParameterValuePoolData = new SystemParameterValuePoolData();
							systemParameterValuePoolData.setId(++count);
							systemParameterValuePoolData.setName(accessGroup);
							systemParameterValuePoolData.setValue(accessGroup);
							systemParameterValuePoolData.setParentSystemParameter(sysParamData);
							systemParameterValuePoolDatas.add(systemParameterValuePoolData);
						}
						sysParamData.setParameterDetail(systemParameterValuePoolDatas);
					} else if(sysParamData.getAlias().equalsIgnoreCase("DEFAULT_SSO_ACCESS_GROUP")) {
						List<String> accessGroups = new ArrayList<>();
							accessGroups.add("Select Access Group");
						List<String> accessGroups1 = accessGroupDAO.getAllAccessGroupByStaffType(BaseConstants.SSO_STAFF);
						List<SystemParameterValuePoolData> systemParameterValuePoolDatas = new ArrayList<SystemParameterValuePoolData>();
						Integer count = 0;
						if(accessGroups1 != null && !accessGroups1.isEmpty())
							accessGroups.addAll(accessGroups1);
						for(String accessGroup : accessGroups) {
							SystemParameterValuePoolData systemParameterValuePoolData = new SystemParameterValuePoolData();
							systemParameterValuePoolData.setId(++count);
							systemParameterValuePoolData.setName(accessGroup);
							systemParameterValuePoolData.setValue(accessGroup);
							systemParameterValuePoolData.setParentSystemParameter(sysParamData);
							systemParameterValuePoolDatas.add(systemParameterValuePoolData);
						}
						sysParamData.setParameterDetail(systemParameterValuePoolDatas);						
					} else if(sysParamData.getAlias().equalsIgnoreCase("LDAP_AUTH_ENABLE")) {
						String[] stringArr = new String[] {"True","False"};
						List<SystemParameterValuePoolData> systemParameterValuePoolDatas = new ArrayList<SystemParameterValuePoolData>();
						Integer count = 0;
						for(String ldapAuthEnable : stringArr) {
							SystemParameterValuePoolData systemParameterValuePoolData = new SystemParameterValuePoolData();
							systemParameterValuePoolData.setId(++count);
							systemParameterValuePoolData.setName(ldapAuthEnable);
							systemParameterValuePoolData.setValue(ldapAuthEnable);
							systemParameterValuePoolData.setParentSystemParameter(sysParamData);
							systemParameterValuePoolDatas.add(systemParameterValuePoolData);
						}
						sysParamData.setParameterDetail(systemParameterValuePoolDatas);
					} else if(SystemParametersConstant.SSO_ADMIN_PASSWORD.equals(sysParamData.getAlias()) && sysParamData.getValue()!=null){
						sysParamData.setValue(EliteUtils.decryptData(sysParamData.getValue()));
					}
					ldapParamList.add(sysParamData);				
				}else{
					if(SystemParametersConstant.CUSTOMER_LOGO.equals(sysParamData.getAlias())){
						String smallLogoPath=sysParamData.getValue();
						
						
						/*if(smallLogoPath.startsWith(String.valueOf(sysParamData.getId()))){
							logger.debug("Found Custom customer small logo , so fetch from file system ");
							ResponseObject responseObject =getCustomerLogoFile(String.valueOf(sysParamData.getId()));	
							
							if(responseObject.isSuccess()){
								File sampleFile=(File)responseObject.getObject();
								
								formWrapper.setSmallLogoPath(BaseConstants.IMAGE_BYTE_CONSTANT+setCustomerLogoFile(sampleFile));
								}else{
									logger.debug("Customer Small logo not found at specified location ");
									formWrapper.setSmallLogoPath(smallLogoPath);
								}
						}else{
							logger.debug("Found Default customer small logo. ");
							formWrapper.setSmallLogoPath(smallLogoPath);
						}*/
						if(sysParamData.getImage()!=null && sysParamData.getImage().length!=0){
							logger.debug("Found Custom customer small logo , so fetch from file system ");
							formWrapper.setSmallLogoPath(BaseConstants.IMAGE_BYTE_CONSTANT+setCustomerLogoFile(sysParamData.getImage()));
						}else{
							logger.debug("Found Default customer small logo. ");
							formWrapper.setSmallLogoPath(smallLogoPath);
						}
						custLogoParamlist.add(sysParamData);
					}else if(SystemParametersConstant.CUSTOMER_LOGO_LARGE.equals(sysParamData.getAlias())){
						String largeLogoPath=sysParamData.getValue();
						/*if(largeLogoPath.startsWith(String.valueOf(sysParamData.getId()))){
							logger.debug("Found Custom customer large logo , so fetch from file system ");
							ResponseObject responseObject =getCustomerLogoFile(String.valueOf(sysParamData.getId()));	
							
							if(responseObject.isSuccess()){
								File sampleFile=(File)responseObject.getObject();
								
								formWrapper.setLargeLogoPath(BaseConstants.IMAGE_BYTE_CONSTANT+setCustomerLogoFile(sampleFile));
								}else{
									logger.debug("Customer Large logo not found at specified location ");
									formWrapper.setLargeLogoPath(largeLogoPath);
								}

						}else{
							logger.debug("Found Default customer large logo. ");
							formWrapper.setLargeLogoPath(largeLogoPath);
						}*/
						if(sysParamData.getImage()!=null && sysParamData.getImage().length!=0){
							logger.debug("Found Custom customer large logo , so fetch from file system ");
							formWrapper.setLargeLogoPath(BaseConstants.IMAGE_BYTE_CONSTANT+setCustomerLogoFile(sysParamData.getImage()));
						}else{
							logger.debug("Found Default customer large logo. ");
							formWrapper.setLargeLogoPath(largeLogoPath);
						}
						custLogoParamlist.add(sysParamData);
					}else if(SystemParametersConstant.FOOTER_IMAGE.equals(sysParamData.getAlias())) {
						String emailFooterPath=sysParamData.getValue();
						if(emailFooterPath != null && emailFooterPath.startsWith(String.valueOf(sysParamData.getId()))){
							logger.debug("Found email footer image , so fetch from file system ");
							ResponseObject responseObject =getCustomerLogoFile(String.valueOf(sysParamData.getId()));	
							if(responseObject.isSuccess()){
								File sampleFile=(File)responseObject.getObject();
								formWrapper.setEmailFooterImagePath(BaseConstants.IMAGE_BYTE_CONSTANT+setCustomerLogoFile(sampleFile));
								}else{
									logger.debug("Email footer image not found at specified location ");
									formWrapper.setEmailFooterImagePath(emailFooterPath);
								}
						}else{
							logger.debug("Found Default customer large logo. ");
							formWrapper.setEmailFooterImagePath(emailFooterPath);
						}
						emailFooterLogoList.add(sysParamData);
					}else{
						custParamlist.add(sysParamData);	
					}
				}
			}
			addAllSystemParameterInCache(SystemParametersConstant.GENERAL_PARAMETERS,generalParamlist);
			addAllSystemParameterInCache(SystemParametersConstant.PASSWORD_PARAMETERS,pwdParamlist);
			addAllSystemParameterInCache(SystemParametersConstant.CUSTOMER_PARAMETERS,custParamlist);
			addAllSystemParameterInCache(SystemParametersConstant.CUSTOMER_LOGO_PARAMETERS,custLogoParamlist);
			addAllSystemParameterInCache(SystemParametersConstant.FILE_REPROCESSING_PARAMETERS,fileReprocessingParamlist);
			addAllSystemParameterInCache(SystemParametersConstant.EMAIL_PARAM,emailParamList);
			addAllSystemParameterInCache(SystemParametersConstant.LOGIN_PARAM,ldapParamList);
			addAllSystemParamValuePoolInCache(SystemParametersConstant.SYSTEM_PARAM_VALUE_POOL,sysParamValuePool);
			addAllSystemParameterInCache(SystemParametersConstant.EMAIL_FOOTER_IMAGE_PARAM,emailFooterLogoList);
            addAllSystemParameterInCache(SystemParametersConstant.SSO_PARAM,ssoParamList);

			formWrapper.setGenParamList(generalParamlist);
			formWrapper.setPwdParamList(pwdParamlist);
			formWrapper.setCustParamList(custParamlist);
			formWrapper.setCustLogoParamList(custLogoParamlist);
			formWrapper.setFileReprocessingParamList(fileReprocessingParamlist);
			formWrapper.setEmailParamList(emailParamList);
			formWrapper.setLdapParamList(ldapParamList);
			formWrapper.setEmailFooterLogoList(emailFooterLogoList);
			formWrapper.setSystemParamValuePoolList(sysParamValuePool);
            formWrapper.setSsoParamList(ssoParamList);
			
			MapCache.addConfigObject(SystemParametersConstant.CUSTOMER_LOGO, formWrapper.getSmallLogoPath());
			MapCache.addConfigObject(SystemParametersConstant.CUSTOMER_LOGO_LARGE, formWrapper.getLargeLogoPath());
			MapCache.addConfigObject(SystemParametersConstant.FOOTER_IMAGE, formWrapper.getEmailFooterImagePath());
		}
		return formWrapper;
	}
	
	/**
	 * Add System Parameter list in cache 
	 * @param sysParamGroup
	 * @param sysParamList
	 */
	@Override
	@Transactional(readOnly=true)
	public void addAllSystemParameterInCache(String sysParamGroup,List<SystemParameterData> sysParamList){
		if(sysParamList!=null){
			int size=sysParamList.size();
			for(int i=0;i<size;i++){
				SystemParameterData sysParamData=sysParamList.get(i);				
				/*if(SystemParametersConstant.FROM_EMAIL_PASSWORD.equals(sysParamData.getAlias()) && sysParamData.getValue()!=null 
						|| SystemParametersConstant.SSO_ADMIN_PASSWORD.equals(sysParamData.getAlias()) && sysParamData.getValue()!=null){
					sysParamData.setValue(EliteUtils.encryptData(sysParamData.getValue()));
				}*/
				MapCache.addSystemParameterList(sysParamGroup,sysParamData.getAlias(),sysParamData);
			}
		}
	}
	
	/**
	 * Add System Parameter  Value Pool list in cache 
	 * @param sysParamGroup
	 * @param sysParamList
	 */
	@Override
	@Transactional(readOnly=true)
	public void addAllSystemParamValuePoolInCache(String sysParamGroup,List<SystemParameterValuePoolData> sysParamList){
		if(sysParamList!=null){
			int size=sysParamList.size();
			for(int i=0;i<size;i++){
				SystemParameterValuePoolData sysParamData=sysParamList.get(i);
				MapCache.addSystemParameterList(sysParamGroup,String.valueOf(sysParamData.getId()),sysParamData);
			}
		}
	}
	
	/**
	 * Insert Customer logo in database
	 */
	@Override
	@Transactional
	public ResponseObject addImage(ImageModel imageModel){
		ResponseObject responseObject=new ResponseObject();
		imageModelDao.save(imageModel);
		
		if(imageModel.getId()!= 0){
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.IMAGE_INSERT_SUCCESS);
			responseObject.setObject(imageModel.getId());
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.IMAGE_INSERT_FAIL);
		}
		return responseObject;
		
	}
	
	/**
	 * Get System parameter data using alias
	 */
	@Override
	@Transactional(readOnly=true)
	public SystemParameterData getSystemParameterByAlias(String alias){
		
		return systemParamDataDao.getSystemParameterByAlias(alias);
	}
	
	/**
	 * Get image model using image id
	 */
	@Override
	@Transactional(readOnly=true)
	public ImageModel findImageByPrimaryKey(Class<ImageModel> klass, Serializable id){
		return imageModelDao.findByPrimaryKey(klass, id);
	}
	
	/**
	 * Load all system parameter
	 */
	@Override
	@Transactional(readOnly=true)
	public List<SystemParameterData> loadSystemParameterList(){
		return systemParamDataDao.loadSystemParameterList();
	}
	
	/**
	 * Load Customer Logo blob
	 * @param imageid
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public Blob getCustLogoImageAsBlob(int imageid) throws SMException{
		return imageModelDao.getCustLogoImageAsBlob(imageid);
	}
	
	/**
	 * Get All Image Model
	 */
	@Override
	@Transactional(readOnly=true)
	public List<ImageModel> getAllImageModels(){
		return imageModelDao.getAllObject(ImageModel.class);
	}
	
	/**
	 * Fetch customer logo file from backup location
	 */
	@Override
	public ResponseObject getCustomerLogoFile(String custLogoId){
		
		ResponseObject responseObject=getLocationOfSystemBackUpPath(SystemBackUpPathOptionEnum.SYSTEMPARAMIMAGE);
		File sampleFile;
		if(responseObject.isSuccess()){
			
			String backupLocation=responseObject.getObject().toString();
			logger.debug("System Backup Location :: "+backupLocation);
			
			File directory = new File(backupLocation);
			
			String fileList[]=directory.list(new FilenameFilter() {
				
				@Override
				public boolean accept(File directory, String name) {
					return name.startsWith(custLogoId+"_");
				}
			});
			
			if (fileList != null && fileList.length>0) {
				
				 String filename = fileList[0];
		         logger.debug("Sample data file found : "+filename);
		         sampleFile=new File(backupLocation+File.separator+filename);
		         responseObject.setSuccess(true);
		         responseObject.setObject(sampleFile);
		      }  else {
		    	  logger.debug("Either dir does not exist or is not a directory");
		    	  responseObject.setSuccess(false);
			      responseObject.setResponseCode(ResponseCode.REGEX_PARSER_NO_SAMPLE_FILE_FOUND);
		      } 
		}
		else{
			logger.debug("System Backup path is not valid");
		}
		
		return responseObject;
	}
	
	/**
	 * Set customer logo bytes
	 * @param custLogoFile
	 * @return
	 * @throws SMException
	 */
	@Override
	public String setCustomerLogoFile(File custLogoFile) throws SMException{
		
		try(FileInputStream inputStream = new FileInputStream(custLogoFile) ; ByteArrayOutputStream bos=new ByteArrayOutputStream() ) {

			int b;
			byte[] buffer = new byte[(int) custLogoFile.length()];
	
			while((b=inputStream.read(buffer))!=-1){
			   bos.write(buffer,0,b);
			}
			bos.flush();
			return new String(Base64.encodeBase64(bos.toByteArray()));
			
		} catch (IOException e) {
			throw new SMException(e); 
		}
	}
	
	/**
	 * Find path of customer logo 
	 * @param alias
	 * @return
	 * @throws SMException
	 */
	@SuppressWarnings("deprecation")
	@Transactional(readOnly=true)
	@Override
	public String getCustomerLogoPath(String alias) throws SMException{
		
		SystemParameterData systemParameter=systemParamDataDao.getSystemParameterByAlias(alias);
		String custLogoPath = null;
		if(systemParameter !=null){
			 custLogoPath=systemParameter.getValue();
			 if(systemParameter.getImage()!=null  && systemParameter.getImage().length!=0) {
					custLogoPath=BaseConstants.IMAGE_BYTE_CONSTANT+setCustomerLogoFile(systemParameter.getImage());	
			 }else {
					logger.debug("Found Default customer small logo. ");
			 }
			 MapCache.addConfigObject(alias, custLogoPath);
		}else{
			logger.debug("System Parameter data is null ");
		}
		
		return custLogoPath;
	}
	
	/**
	 *  Fetch the location configured in system backup path system parameter
	 * @param backUpValue
	 * @return
	 */
	@Override
	public ResponseObject getLocationOfSystemBackUpPath(SystemBackUpPathOptionEnum backUpValue) {

		ResponseObject responseObject = new ResponseObject();
		String exportXmlPath = MapCache.getConfigValueAsString(SystemParametersConstant.SYSTEM_BACKUP_FILE_PATH,
				SystemParametersConstant.SYSTEM_BACKUP_FILE_PATH_VALUE);

		if (exportXmlPath.contains("$" + BaseConstants.TOMCAT_HOME)) {
			String tomcatHome = System.getenv(BaseConstants.TOMCAT_HOME);
			logger.debug("Found default backup path tomcat home is :" + tomcatHome);
			if (!StringUtils.isEmpty(tomcatHome)) {

				exportXmlPath = exportXmlPath.replace("$" + BaseConstants.TOMCAT_HOME, tomcatHome);

			} else {
				logger.debug("$TOMCAT_HOME is not set or found null");
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.TOMCAT_HOME_NOT_FOUND);
				return responseObject;
			}
		} else {
			logger.debug("Found Updated backup path" + exportXmlPath);
			if (exportXmlPath.contains("$")) {
				logger.debug("Backup path is not valid");
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.INVALID_EXPORT_BACKUP_PATH);
				responseObject.setArgs(new Object[] { exportXmlPath });
				return responseObject;
			}
		}

		if (SystemBackUpPathOptionEnum.DELETE.equals(backUpValue)) {
			exportXmlPath = exportXmlPath + File.separator + BaseConstants.TOMCAT_DELETE_BACKUP_DIR_NAME;
			logger.debug("Found Export Befor Delete , so add Export dir : " + exportXmlPath);
		} else if (SystemBackUpPathOptionEnum.REGEXPARSER.equals(backUpValue)) {
			exportXmlPath = exportXmlPath + File.separator + BaseConstants.TOMCAT_REGEX_PARSER_SAMPLEFILE_DIR_NAME;
			logger.debug("Found RegEx Parser Sample Data  : " + exportXmlPath);
		} else if (SystemBackUpPathOptionEnum.SYSTEMPARAMIMAGE.equals(backUpValue)) {
			exportXmlPath = exportXmlPath + File.separator + BaseConstants.TOMCAT_CUSTOM_IMAGE_DIR_NAME + File.separator
					+ BaseConstants.TOMCAT_SYSTEMPARAM_IMAGE_DIR_NAME;
			logger.debug("Found System Parameter Image Sample Path  : " + exportXmlPath);
		} else if (SystemBackUpPathOptionEnum.STAFFIMAGE.equals(backUpValue)) {
			exportXmlPath = exportXmlPath + File.separator + BaseConstants.TOMCAT_CUSTOM_IMAGE_DIR_NAME + File.separator
					+ BaseConstants.TOMCAT_STAFF_IMAGE_DIR_NAME;
			logger.debug("Found Staff Image Sample path: " + exportXmlPath);
		}
		else if (SystemBackUpPathOptionEnum.MIGRATION_EXTRACT.equals(backUpValue)) {
			exportXmlPath = exportXmlPath + File.separator + BaseConstants.TOMCAT_MIGRATION_DIR_NAME + File.separator + BaseConstants.TOMCAT_MIGRATION_EXTRACT_FILE_DIR_NAME;
			logger.debug("Found migration zip file path : " + exportXmlPath);
		}
		else if (SystemBackUpPathOptionEnum.MIGRATION_ZIP.equals(backUpValue)) {
			exportXmlPath = exportXmlPath + File.separator + BaseConstants.TOMCAT_MIGRATION_DIR_NAME + File.separator + BaseConstants.TOMCAT_MIGRATION_ZIP_FILE_DIR_NAME;
			logger.debug("Found migration extract file path : " + exportXmlPath);
		}

		// Create backup directories at path if not exist
		File directory = new File(exportXmlPath);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		responseObject.setSuccess(true);
		responseObject.setObject(exportXmlPath);

		return responseObject;
	}

	/**
	 * Parse the string and gives regex and password policy name
	 * @param passwordPolicy
	 * @return String[]
	 */
	@Override
	public String[] parsePasswordType(String passwordPolicy){
		String delims = "~"; // so the delimiters is: ~
		String[] tokens =null;
		if(passwordPolicy!=null && !StringUtils.isEmpty(passwordPolicy) ){
			tokens= passwordPolicy.split(delims);
		}
		return tokens;
	}

	@Transactional
	@Override
	public void migrateEmailPassword() {
		SystemParameterData parameterData = this.getSystemParameterByAlias(SystemParametersConstant.FROM_EMAIL_PASSWORD);
		if(!StringUtils.isEmpty(parameterData.getValue())){
			parameterData.setValue(EliteUtils.encryptData(parameterData.getValue()));
			systemParamDataDao.merge(parameterData);
		}
	}
	
	/**
	 * Insert system Parameter Group in Database
	 */
	@Override
	@Transactional
	public ResponseObject migrateStaffToKeycloak() {
		ResponseObject responseObject=new ResponseObject();
		String message = null;
		List<Staff> staffList = staffDAO.getAllStaffDetails();			
		try {
			message = migrateUsers(staffList, MapCache.getConfigValueAsString(SystemParametersConstant.SSO_ADMIN_USERNAME, ""), 
					MapCache.getConfigValueAsString(SystemParametersConstant.SSO_ADMIN_PASSWORD, ""));
		} catch (IOException e) {
			logger.error("Error while migrating staff. Please check sso configuration parameters.", e);
		} catch (Exception e) {
			logger.error("Error while migrating staff. Please check sso configuration parameters.", e);
		}
		if(message !=null){
			responseObject.setSuccess(true);
			responseObject.setObject(message);
			responseObject.setResponseCode(ResponseCode.STAFF_MIGRATION_TO_KEYCLOAK_SUCCESS);
		}else{
			responseObject=new ResponseObject();
			responseObject.setSuccess(false);
			responseObject.setObject("Error while migrating staff. Please check sso configuration parameters.");
			responseObject.setResponseCode(ResponseCode.STAFF_MIGRATION_TO_KEYCLOAK_FAIL);
		}
		return responseObject;
	}
	
	public static Keycloak getKeyCloakInstance(String kcAdminUsername, String kcAdminPassword) {		
		
		//return KeycloakBuilder.builder().serverUrl(authServerUrl).realm(MASTER_REALM).username(kcAdminUsername).password(kcAdminPassword)
				//.clientId(CLIENT_ID).resteasyClient(new ClientBuilder().connectionPoolSize(10).build()).build();
		return KeycloakBuilder.builder().serverUrl(authServerUrl).realm(MASTER_REALM).username(kcAdminUsername).password(kcAdminPassword)
				.clientId(CLIENT_ID).resteasyClient(((ResteasyClientBuilder) ClientBuilder.newBuilder()).connectionPoolSize(10).build()).build();
	}

	public void readKCJson() throws IOException {
		
		String keycloakJsonFile = servletContext.getRealPath(BaseConstants.KEYCLOAK_JSON_FILE_PATH) + File.separator;
		File initialFile = new File(keycloakJsonFile);
	
		StringWriter writer = new StringWriter();
		try(InputStream is = new FileInputStream(initialFile)){
			
			IOUtils.copy(is, writer, StandardCharsets.UTF_8);
			String str = writer.toString();
			JsonObject jobj = new Gson().fromJson(str, JsonObject.class);
			realm = jobj.get("realm").getAsString();//NOSONAR
			authServerUrl = jobj.get("auth-server-url").getAsString();//NOSONAR
		} catch (IOException e) {
			logger.error(e);
		} 
	}
		
	@Transactional
	public String migrateUsers(List<Staff> staffList, String kcAdminUsername, String kcAdminPassword) throws IOException {
		readKCJson();
		kcAdminPassword =  EliteUtils.decryptData(kcAdminPassword);
		Keycloak keycloak = getKeyCloakInstance(kcAdminUsername,kcAdminPassword);
		keycloak.tokenManager().getAccessToken();
		int successCount = 0;
		int failCount = 0;		
		String summary = "";
		StringBuilder messageBuilder=new StringBuilder();
		messageBuilder.append("Successful User Migration Count is : ${successCnt} \n Failed User Migration Count is : ${failCnt} \n");		
		if (staffList != null && !staffList.isEmpty()) {				
			Iterator<Staff> itr = staffList.iterator();
			while (itr.hasNext()) {
				Staff staffData = itr.next();
				CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
				credentialRepresentation.setType(CredentialRepresentation.PASSWORD);				
				credentialRepresentation.setValue(staffData.getPassword());
 
				UserRepresentation userRepresentation = new UserRepresentation();
				userRepresentation.setUsername(staffData.getUsername());
				userRepresentation.setFirstName(staffData.getFirstName());
				userRepresentation.setLastName(staffData.getLastName());
				userRepresentation.setEmail(staffData.getEmailId());				
				userRepresentation.setCredentials(Arrays.asList(credentialRepresentation));				
				userRepresentation.setEnabled(true);
				try(Response response = keycloak.realm(realm).users().create(userRepresentation);) {
					if (response.getStatus() == 201 || response.getStatus() == 200) {
						successCount += 1;
						staffData.setStafftype(BaseConstants.SSO_STAFF);
						try {
							AccessGroup accessGrp = accessGroupDAO.getAccessGroup(MapCache.getConfigValueAsString(SystemParametersConstant.DEFAULT_SSO_ACCESS_GROUP,""));
							List<AccessGroup> list = new ArrayList<>();
							list.add(accessGrp);						
							staffData.setAccessGroupList(list);
							staffService.updateStaff(staffData);
						} catch (Exception e) {
							logger.error("Error while updating staff.",e);
						}
					} else {  
						failCount += 1;
						messageBuilder.append("User Name : "+ staffData.getUsername() + " , Failure Reason : "+ response.getStatusInfo() + " \n");
					}
				}
			}			
			summary = messageBuilder.toString();
			summary = summary.replace("${successCnt}", Integer.toString(successCount));
			summary = summary.replace("${failCnt}", Integer.toString(failCount));
		}
		return summary;
	}

	@Override
	public String setCustomerLogoFile(byte[] custLogoFileBytes) throws SMException {
		return new String(Base64.encodeBase64(custLogoFileBytes));
	}

	@Override
	@Transactional
	public ResponseObject createRoleInKeycloak(String accessGrp) throws IOException {
		ResponseObject responseObject=new ResponseObject();
		String kcAdminUsername = MapCache.getConfigValueAsString(SystemParametersConstant.SSO_ADMIN_USERNAME, "");
		String kcAdminPassword = MapCache.getConfigValueAsString(SystemParametersConstant.SSO_ADMIN_PASSWORD, "");
		readKCJson();
		kcAdminPassword =  EliteUtils.decryptData(kcAdminPassword);
		Keycloak keycloak = getKeyCloakInstance(kcAdminUsername,kcAdminPassword);
		keycloak.tokenManager().getAccessToken();
		try {
			RoleRepresentation roleRepresentation = new RoleRepresentation();
			roleRepresentation.setName(accessGrp);
			keycloak.realm(realm).roles().create(roleRepresentation);
			responseObject.setSuccess(true);
			responseObject.setObject(accessGrp + " role is created successfully in keycloak server.");
			responseObject.setResponseCode(ResponseCode.ROLE_CREATION_TO_KEYCLOAK_SUCCESS);
		}catch(javax.ws.rs.ClientErrorException e) {
			logger.error(e);
			responseObject.setSuccess(false);
			responseObject.setObject("Error while creating role in keycloak server. Please contact system administrator.");
			responseObject.setResponseCode(ResponseCode.ROLE_CREATION_TO_KEYCLOAK_FAIL);
		}catch (Exception e) {
			logger.error(e);
			responseObject.setSuccess(false);
			responseObject.setObject("Error while creating role in keycloak server. Please contact system administrator.");
			responseObject.setResponseCode(ResponseCode.ROLE_CREATION_TO_KEYCLOAK_FAIL);
		}
		return responseObject;
	}
	


}