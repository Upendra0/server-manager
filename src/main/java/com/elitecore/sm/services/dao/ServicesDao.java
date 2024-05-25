package com.elitecore.sm.services.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.services.model.SearchServices;
import com.elitecore.sm.services.model.Service;

public interface ServicesDao  extends GenericDAO<Service>{

	public Map<String, Object> getServiceJAXBByTypeAndId(int serviceId,String serviceType,String jaxbXmlPath , String fileName) throws SMException;
	
	public void updateForResetSyncFlagOfService(Service service);
	
	public Map<String, Object> createCriteriaConditions(int searchInstanceId);
	
	public Service getServiceWithServerInstanceById(int serviceId);
	
	public List<Service> getServicesforServerInstance(int serverInsId);
	
	public String getMaxServiceInstanceIdforServerInstance(int serverInsId, int serviceType);
	
	public Service getServiceByServiceInstanceId(int serverInsId, int serviceType , String servInstanceId);
	
	public int getServiceCount(String serviceName, int serverInstanceId);
	
	public Service getAllServiceDepedantsByServiceId(int serviceId);
	
	public Map<String, Object>  getServiceBySearchParameters(SearchServices service);
	
	public Service getServicefullHierarchyWithoutMarshlling(int serviceId,String serviceClassName) throws SMException;
	
	public List<Service> getServiceForSNMPServiceThreshold(int serviceId) ;

	public List<Service> getServicesPaginatedList(Class<Service> classInstance, List<Criterion> conditions, Map<String, String> aliases,
			int offset, int limit, String sortColumn, String sortOrder);
	
	public void addServiceDetails(Service service);
	
	public void updateServiceDetails(Service service);

	public List<Service> getServiceList(int serverId);

	public List<Service> getServerListByType(int serviceTypeId);

	public List<Service> getServiceListByTypeAndServId(int serviceTypeId, String servId, int serverInstanceId);

	public List<Service> getAllServiceByIds(Integer[] ids);

	List<Service> getServiceListForValidation(int serverId);

}
