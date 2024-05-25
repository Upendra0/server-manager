/**
 * 
 */
package com.elitecore.sm.systemparam.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Blob;
import java.util.List;

import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.SystemBackUpPathOptionEnum;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.systemparam.model.ImageModel;
import com.elitecore.sm.systemparam.model.SystemParameterData;
import com.elitecore.sm.systemparam.model.SystemParameterFormWrapper;
import com.elitecore.sm.systemparam.model.SystemParameterGroupData;
import com.elitecore.sm.systemparam.model.SystemParameterValuePoolData;

/**
 * @author vandana.awatramani
 * 
 */

public interface SystemParameterService {
	public ResponseObject addSystemParameter(SystemParameterData systemParam);

	public ResponseObject addSystemParameterGroup(SystemParameterGroupData systemParamGroup);

	public ResponseObject updateSystemParameters(List<SystemParameterData> listOfParams);
	
	public ResponseObject updateSystemParameters(SystemParameterData listOfParams);

	public ResponseObject addSystemParameterValuePool(SystemParameterValuePoolData systemParameterValuePool);

	public List<SystemParameterData> setValuePoolDetail(List<SystemParameterData> sysParamList,List<SystemParameterValuePoolData> sysParamValuePoolList);
	
	public SystemParameterFormWrapper loadAllSystemParameter() throws SMException;
	
	public ResponseObject addImage(ImageModel imageModel);
	
	public SystemParameterData getSystemParameterByAlias(String alias);
	
	public ImageModel findImageByPrimaryKey(Class<ImageModel> klass, Serializable id);
	
	public List<SystemParameterData> loadSystemParameterList();
	
	public void addAllSystemParameterInCache(String sysParamGroup,List<SystemParameterData> sysParamList);
	
	public void addAllSystemParamValuePoolInCache(String sysParamGroup,List<SystemParameterValuePoolData> sysParamList);
	
	public Blob getCustLogoImageAsBlob(int imageid) throws SMException;
	
	public List<ImageModel> getAllImageModels();
	
	public ResponseObject getCustomerLogoFile(String custLogoId);
	
	public String setCustomerLogoFile(File custLogoFile) throws SMException;
	
	public String getCustomerLogoPath(String alias) throws SMException;
	
	public ResponseObject getLocationOfSystemBackUpPath(SystemBackUpPathOptionEnum backUpValue) ;
	
	public String[] parsePasswordType(String passwordPolicy);
	
	public void migrateEmailPassword();
	
	public ResponseObject migrateStaffToKeycloak();
	
	public String setCustomerLogoFile(byte[] custLogoFileBytes) throws SMException;
	
	public ResponseObject createRoleInKeycloak(String accessGrp) throws IOException;
	
}
