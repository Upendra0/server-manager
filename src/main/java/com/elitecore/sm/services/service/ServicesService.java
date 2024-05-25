package com.elitecore.sm.services.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.elitecore.core.util.mbean.data.config.CrestelNetServiceData;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.pathlist.model.FileGroupingParameter;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.AggregationService;
import com.elitecore.sm.services.model.CoAPCollectionService;
import com.elitecore.sm.services.model.CollectionService;
import com.elitecore.sm.services.model.DataConsolidationService;
import com.elitecore.sm.services.model.DiameterCollectionService;
import com.elitecore.sm.services.model.DistributionService;
import com.elitecore.sm.services.model.GTPPrimeCollectionService;
import com.elitecore.sm.services.model.Http2CollectionService;
import com.elitecore.sm.services.model.IPLogParsingService;
import com.elitecore.sm.services.model.MqttCollectionService;
import com.elitecore.sm.services.model.NetflowBinaryCollectionService;
import com.elitecore.sm.services.model.NetflowCollectionService;
import com.elitecore.sm.services.model.ParsingService;
import com.elitecore.sm.services.model.ProcessingService;
import com.elitecore.sm.services.model.RadiusCollectionService;
import com.elitecore.sm.services.model.SearchServices;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceSchedulingParams;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.services.model.SysLogCollectionService;

public interface ServicesService {

	public Map<String,Object> getServiceJAXBByTypeAndId(int serviceId,String serviceType,String jaxbXmlPath ,String fileName) throws SMException;
	
	public ResponseObject addServicetype(ServiceType svcType);
	
	public ResponseObject addService(Service service) throws SMException;
	
	public ResponseObject updateService(Service service);
	
	public long getTotalServiceCount(int serverInstanceId);
	
	public List<Service> getPaginatedList(int serverInstanceId,int startIndex, int limit, String sidx, String sord);
	
	public ResponseObject deleteServicesandDepedants(List<Service> serviceList,ServerInstance serverInstance,boolean isCopy,String jaxbXmlPath) throws SMException; 
	
	public List<ServiceType> getServiceTypeList();
	
	public CrestelNetServiceData getServicesSampleDataForSync(int serviceId, String serviceClassName,String jaxbXmlPath,String xsltFilePath,String engineSampleXmlPath) throws SMException;
	
	public ResponseObject loadServiceStatus(int serviceId);
	
	public  List<Service> getServicesforServerInstance(int serverInstanceId);
	
	public Service getAllServiceDepedantsByServiceId(int serviceId);
	
	public ResponseObject updateCollectionServiceConfiguration(CollectionService service);
	
	public long getTotalServiceInstancesCount(SearchServices service);
	
	public List<Service> getPaginatedList(SearchServices service, int startIndex, int limit, String sidx, String sord);
	
	public ResponseObject getServiceInstaceJAXBXML(int serviceInstanceId , boolean isExportForDelete , String exportTempPath) throws SMException;
	
	public ResponseObject importServiceInstanceConfig(int serviceInstanceId,File importFile,int staffId,String jaxbXmlPath,int serverId, int importMode) throws SMException;
	
	public ResponseObject deleteServiceInstance(int serviceInstancesId,int staffid,String jaxbXmlPath) throws SMException;
	
	public ResponseObject deleteServiceInstanceConfig(Service serviceInstance);
	
	public Map<String, ResponseObject> syncServiceInstance(Map<String,String> serviceInstanceDetailMap);
	
	public ResponseObject resetServiceSyncStatus(Service service);
	
	public ResponseObject startServiceInstance(int serviceInstanceId);
	
	public ResponseObject stopServiceInstance(int serviceInstanceId);
	
	public Service getServiceandServerinstance(int serviceId);
	
	public ResponseObject getServiceCounterDetails(int serviceId);
	
	public ResponseObject resetServiceCounterDetails(int serviceId);
	
	public ResponseObject updateNetflowCollectionServiceConfiguration(NetflowCollectionService service);
	
	public ResponseObject resetSISyncChildStatus(ServerInstance serverInstance) ;
	
	public ResponseObject updateNetflowBinaryCollectionServiceConfiguration(NetflowBinaryCollectionService service);
	
	public ResponseObject updateSyslogCollectionServiceConfiguration(SysLogCollectionService service);
	
	public ResponseObject updateMqttCollectionServiceConfiguration(MqttCollectionService service);
	
	public ResponseObject updateCoAPCollectionServiceConfiguration(CoAPCollectionService service);
	
	public ResponseObject updateHttp2CollectionServiceConfiguration(Http2CollectionService service);
	
	public ResponseObject updateGtpPrimeCollectionServiceConfiguration(GTPPrimeCollectionService service);
	
	public List<ImportValidationErrors> validateServiceForImport(Service service,List<ImportValidationErrors> importErrorList,int importedServiceId);
	
	public ResponseObject updateIplogParsingServiceConfiguration(IPLogParsingService service);
	
	public ResponseObject updateIplogParsingServiceHashConfiguration(IPLogParsingService service,String partitionParamStr) throws CloneNotSupportedException;
	
	public ResponseObject updateParsingServiceConfiguration(ParsingService service);
	
	public ResponseObject updateDistributionServiceConfiguration(DistributionService service);
	
	public ResponseObject iterateFileGroupParams(FileGroupingParameter fileGroupParams, boolean isImport);
	
	public ResponseObject getServiceTypeByAlias(String serviceTypeAlias);

	public ResponseObject iterateOverServiceAndDepedantsForImport(ServerInstance serverInstanceDB, Service exportedService, boolean isImport,
			boolean isUpdateServiceInfo, Map<Integer,Integer> svcIdMap, Map<String, Integer> policyMap, boolean importModule, boolean isServiceLevel);
	
	public ResponseObject updateServiceStatus(int id, String serviceStatus) throws CloneNotSupportedException ;
	
	public List<Service> getServiceList(int serverId);

	public List<Service> getServiceListByAlias(String serviceAlias);
	
	public void iterateServiceConfigDetails(Service service);
	
	public ResponseObject updateProcessingServiceConfiguration(ProcessingService service);	
	
	public ResponseObject updateAggregationServiceConfiguration(AggregationService service,String serviceId,int lastUpdatedByStaffId);

	public ResponseObject updatePartitionParameters(IPLogParsingService service);
	
	public ResponseObject updateConsolidationServiceConfiguration(DataConsolidationService service);
	
	public ResponseObject updateMigService(Service service) ;

	public Service getServiceListByIDAndTypeAlias(String serviceAlias, String serviceId, int serverInstanceId);

	public Service getServiceById(int serviceId);

	public ResponseObject getAllServiceByIds(Integer[] ids);

	boolean isUniqueServiceName(int serviceId, String newName, int serverInstanceId);

	List<Service> getServiceListToValidate(int serverId);
	
	public void validateDataConsolidationDependents(Service service, List<ImportValidationErrors> importErrorList,int importedServiceId);
	
	public void importFileGroupingParameterUpdateMode(FileGroupingParameter dbParam, FileGroupingParameter exportedParam);
	
	public void importFileGroupingParameterAddMode(FileGroupingParameter fileGroupParam, int staffId);
	
	public String getMaxServiceInstanceIdForServer(int serverInstanceId, int serviceTypeId);
	
	public void importParsingServiceBasicParameterUpdateMode(ParsingService dbParsingService, ParsingService exportedParsingService);
	
	public void importDistributionServiceBasicParameterUpdateMode(DistributionService dbDistributionService, DistributionService exportedDistributionService);

	public void importDiameterCollectionServiceBasicParameterForUpdateMode(DiameterCollectionService dbService, DiameterCollectionService exportedService);
	
	public void importOnlineCollectionServiceBasicParameterForUpdateMode(Service dbService, Service exportedService, int importMode);
	
	public void importProcessingServiceBasicParameterUpdateMode(ProcessingService dbProcessingService, ProcessingService exportedProcessingService);
	
	public void importIpLogParsingServiceBasicParameterForUpdateMode(IPLogParsingService dbService, IPLogParsingService exportedService);
	
	public void importDataConsolidationServiceBasicParameterForUpdateMode(DataConsolidationService dbService, DataConsolidationService exportedService);
	
	public void importAggreagationServiceBasicParameterForUpdateMode(AggregationService dbService, AggregationService exportedService);
	
	public void importServicePartitionParamAddAndKeepBothMode(IPLogParsingService exportedService);
	
	public void importServicePartitionParamUpdateMode(IPLogParsingService dbService, IPLogParsingService exportedService);
	
	public void importServiceScheduleParamsUpdateMode(ServiceSchedulingParams dbParam, ServiceSchedulingParams exportedParam);
	
	public void importServiceScheduleParamsAddAndKeepBothMode(ServiceSchedulingParams exportedParam);
	
	public void importFileGroupingParameterAddAndKeepBothMode(FileGroupingParameter exportedFileGroupParams);
	
	public ResponseObject updateDiameterCollectionServiceConfiguration(DiameterCollectionService service);
	
	public ResponseObject updateRadiusCollectionServiceConfiguration(RadiusCollectionService radiusCollectionService);
	
	public List<String> getUnifiedFieldConfigInPluging(int iserviceId);

	void iteratenatFlowProxyClientInUpdateMode(NetflowBinaryCollectionService dbService,
			NetflowBinaryCollectionService exportedService);
	
	public void iteratenatFlowProxyClientInAddMode(NetflowBinaryCollectionService dbService, NetflowBinaryCollectionService exportedService);
	
	public void importServicePartitionParamAddMode(IPLogParsingService dbService, IPLogParsingService exportedService);
}
