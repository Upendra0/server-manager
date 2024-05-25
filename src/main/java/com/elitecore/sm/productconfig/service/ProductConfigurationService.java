package com.elitecore.sm.productconfig.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.license.model.License;
import com.elitecore.sm.productconfig.model.ProfileEntity;

/**
 * 
 * @author avani.panchal
 *
 */
public interface ProductConfigurationService {
	
	public ResponseObject createCustomProfileUsingServerType(License license);
	
	public ResponseObject findDefaultProfileAndCreateCustomProfile(String serverTypeAlias,License license);
	
	public ResponseObject findProfileDetailByServerTypeId(int serverTypeId,int licenseId,boolean isDefault);
	
	public ResponseObject updateProductConfiguration(int serverTypeId,String selectedEntities,int staffId) throws CloneNotSupportedException;
	
	public ResponseObject resetProductConfiguration(int serverTypeId,int staffId);
	
	public void refreshProfileEntityStatusCache(int serverTypeId,List<ProfileEntity> customProfileEntityList) ;
	
	public ResponseObject updateProductConfig(ProfileEntity profileEntity);

	public ResponseObject resetProductConfiguration2(ProfileEntity customprofileEntity);
}
