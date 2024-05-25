/**
 * 
 */
package com.elitecore.sm.services.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.services.model.ServiceType;

/**
 * @author vandana.awatramani
 *
 */
public interface ServiceTypeDao extends GenericDAO<ServiceType> {
	
	public ServiceType getServiceTypeByAlias(String serviceAlias);
	
	public List<ServiceType> getEnableServiceTypeList();
	public List<ServiceType> getServiceTypeList();
	public ServiceType getServiceIDbyAlias(String serviceAlias);

	public List<ServiceType> getAllServiceTypeList();

}
