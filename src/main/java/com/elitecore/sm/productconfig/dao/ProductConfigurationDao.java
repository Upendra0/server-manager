package com.elitecore.sm.productconfig.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.productconfig.model.ProfileEntity;

/**
 * 
 * @author avani.panchal
 *
 */
public interface ProductConfigurationDao extends GenericDAO<ProfileEntity>{
	
	public List<ProfileEntity> findProfileDetailByServerTypeId(int serverTypeId,int licenseId,boolean isdefault);
	
	public ProfileEntity findProfileEntityByIdAndServerType(int id , int serverTypeId);
	
	public List<ProfileEntity> findProfileDetailByServerTypeId(int serverTypeId,String entityType,String entityAlias);
	
	public ProfileEntity findCustomProfileEntity(int serverTypeId,String entityAlias,boolean isDefault);

}
