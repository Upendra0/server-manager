package com.elitecore.sm.services.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContext;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.transform.TransformerException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;

import com.elitecore.core.server.hazelcast.HazelcastCacheConstants;
import com.elitecore.core.server.hazelcast.HazelcastUtility;
import com.elitecore.core.services.data.LiveServiceSummary;
import com.elitecore.core.util.mbean.data.config.CrestelNetConfigurationData;
import com.elitecore.core.util.mbean.data.config.CrestelNetDataDefinitionData;
import com.elitecore.core.util.mbean.data.config.CrestelNetDriverData;
import com.elitecore.core.util.mbean.data.config.CrestelNetPluginData;
import com.elitecore.core.util.mbean.data.config.CrestelNetServerData;
import com.elitecore.core.util.mbean.data.config.CrestelNetServiceData;
import com.elitecore.core.util.mbean.data.live.CrestelNetServerDetails;
import com.elitecore.sm.agent.dao.AgentDao;
import com.elitecore.sm.agent.model.AgentType;
import com.elitecore.sm.agent.model.PacketStatisticsAgent;
import com.elitecore.sm.agent.model.ServicePacketStatsConfig;
import com.elitecore.sm.agent.service.AgentService;
import com.elitecore.sm.aggregationservice.model.AggregationAttribute;
import com.elitecore.sm.aggregationservice.model.AggregationCondition;
import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.aggregationservice.model.AggregationKeyAttribute;
import com.elitecore.sm.aggregationservice.validator.AggregationDefinitionValidator;
import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.FileGroupEnum;
import com.elitecore.sm.common.model.FilterGroupTypeEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.SystemBackUpPathOptionEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.model.DataConsolidationAttribute;
import com.elitecore.sm.consolidationservice.model.DataConsolidationGroupAttribute;
import com.elitecore.sm.consolidationservice.model.DataConsolidationMapping;
import com.elitecore.sm.consolidationservice.service.IDataConsolidationService;
import com.elitecore.sm.consolidationservice.validator.ConsolidationAttributeValidator;
import com.elitecore.sm.diameterpeer.model.DiameterPeer;
import com.elitecore.sm.diameterpeer.service.DiameterPeerService;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.drivers.service.DriversService;
import com.elitecore.sm.integration.engine.RemoteJMXHelper;
import com.elitecore.sm.license.model.Circle;
import com.elitecore.sm.license.model.License;
import com.elitecore.sm.license.model.LicenseTypeEnum;
import com.elitecore.sm.license.service.LicenseService;
import com.elitecore.sm.license.util.LicenseUtility;
import com.elitecore.sm.netflowclient.model.NatFlowProxyClient;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.netflowclient.service.NetflowClientService;
import com.elitecore.sm.netflowclient.service.ProxyClientConfigurationService;
import com.elitecore.sm.nfv.commons.constants.NFVResponseCode;
import com.elitecore.sm.parser.comparator.ParserAttributeComparator;
import com.elitecore.sm.parser.dao.ParserDao;
import com.elitecore.sm.parser.dao.ParserMappingDao;
import com.elitecore.sm.parser.dao.VarLengthAsciiDataDefinitionFileDao;
import com.elitecore.sm.parser.model.AsciiParserAttribute;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.RegExPattern;
import com.elitecore.sm.parser.model.RegexParserAttribute;
import com.elitecore.sm.parser.model.RegexParserMapping;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.parser.model.VarLengthAsciiDataDefinitionFile;
import com.elitecore.sm.parser.service.ParserMappingService;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.pathlist.dao.FileGroupingParameterDao;
import com.elitecore.sm.pathlist.dao.PathListDao;
import com.elitecore.sm.pathlist.model.CharRenameOperation;
import com.elitecore.sm.pathlist.model.DataConsolidationPathList;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;
import com.elitecore.sm.pathlist.model.FileGroupingParamConsolidation;
import com.elitecore.sm.pathlist.model.FileGroupingParameter;
import com.elitecore.sm.pathlist.model.FileGroupingParameterAggregation;
import com.elitecore.sm.pathlist.model.FileGroupingParameterCollection;
import com.elitecore.sm.pathlist.model.FileGroupingParameterParsing;
import com.elitecore.sm.pathlist.model.FileGroupingParameterProcessing;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.pathlist.model.ProcessingPathList;
import com.elitecore.sm.pathlist.service.PathListService;
import com.elitecore.sm.policy.dao.IPolicyDao;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.policy.service.IPolicyService;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.dao.PartitionParamDao;
import com.elitecore.sm.services.dao.ServiceSchedulingParamsDao;
import com.elitecore.sm.services.dao.ServiceTypeDao;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.model.AggregationService;
import com.elitecore.sm.services.model.CDRFileDateTypeEnum;
import com.elitecore.sm.services.model.CoAPCollectionService;
import com.elitecore.sm.services.model.CollectionService;
import com.elitecore.sm.services.model.DataConsolidationService;
import com.elitecore.sm.services.model.DateRangeEnum;
import com.elitecore.sm.services.model.DiameterCollectionService;
import com.elitecore.sm.services.model.DistributionService;
import com.elitecore.sm.services.model.GTPPrimeCollectionService;
import com.elitecore.sm.services.model.HashSeparatorEnum;
import com.elitecore.sm.services.model.Http2CollectionService;
import com.elitecore.sm.services.model.IPLogParsingService;
import com.elitecore.sm.services.model.MqttCollectionService;
import com.elitecore.sm.services.model.NetflowBinaryCollectionService;
import com.elitecore.sm.services.model.NetflowCollectionService;
import com.elitecore.sm.services.model.ParsingService;
import com.elitecore.sm.services.model.PartitionFieldEnum;
import com.elitecore.sm.services.model.PartitionParam;
import com.elitecore.sm.services.model.ProcessingService;
import com.elitecore.sm.services.model.RadiusCollectionService;
import com.elitecore.sm.services.model.SearchServices;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceCategoryEnum;
import com.elitecore.sm.services.model.ServiceExecutionParams;
import com.elitecore.sm.services.model.ServiceSchedulingParams;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.services.model.ServiceTypeEnum;
import com.elitecore.sm.services.model.SortingCriteriaEnum;
import com.elitecore.sm.services.model.SortingTypeEnum;
import com.elitecore.sm.services.model.StartUpModeEnum;
import com.elitecore.sm.services.model.SysLogCollectionService;
import com.elitecore.sm.services.validator.ServiceValidator;
import com.elitecore.sm.systemaudit.service.AuditDetailsService;
import com.elitecore.sm.systemaudit.service.SystemAuditService;
import com.elitecore.sm.systemparam.service.SystemParameterService;
import com.elitecore.sm.util.EliteExceptionUtils;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.Utilities;

/**
 * 
 * @author avani.panchal
 *
 */
@org.springframework.stereotype.Service(value = "servicesService")
public class ServicesServiceImpl implements ServicesService {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	SynchronizationProcessor xsltProcessor = new SynchronizationProcessor();
	
	@Autowired
	ParserMappingDao parserMappingDao;
	
	@Autowired
	private ServicesDao servicesDao;

	@Autowired
	private ServiceTypeDao servicetypeDao;

	@Autowired
	@Qualifier(value = "serverInstanceService")
	private ServerInstanceService serverInstanceService;

	@Autowired
	private DriversService driversService;

	@Autowired
	private ServiceSchedulingParamsDao serviceSchedulingParamsDao;

	@Autowired
	@Qualifier(value = "serverInstanceDao")
	private ServerInstanceDao serverInstanceDao;

	@Autowired
	private NetflowClientService clientService;
	
	@Autowired
	private DiameterPeerService diameterPeerService;

	@Autowired
	@Qualifier(value = "auditDetailsService")
	AuditDetailsService auditDetailsService;

	@Autowired
	@Qualifier(value = "systemAuditService")
	SystemAuditService systemAuditService;

	@Autowired
	ServiceValidator serviceValidator;

	@Autowired
	ParserService parserService;

	@Autowired
	PartitionParamDao paramDao;

	@Autowired
	PathListService pathlistService;

	@Autowired
	FileGroupingParameterDao fileGroupParamDao;

	@Autowired
	PartitionParamService partitionParamService;

	@Autowired
	NetflowClientService netflowClientService;

	@Autowired
	ParserMappingService parserMappingService;

	@Autowired
	AgentService agentService;

	@Autowired
	AgentDao agentDao;
	
	@Autowired
	SystemParameterService systemParamService;
	
	@Autowired
	IPolicyService policyService;
	
	/** The policy dao. */
	@Autowired
	IPolicyDao policyDao;
	
	@Autowired
	IDataConsolidationService iDataConsolidation;
	
	@Autowired
	ConsolidationAttributeValidator consolidationAttributeValidator;
	
	@Autowired
	AggregationDefinitionValidator aggDefinitionValidator;
	
	@Autowired
	ServiceEntityImportService serviceEntityImportService;
	
	@Autowired
	PathListDao pathListDao;
	
	@Autowired
	ParserDao parserDao;
	
	
	@Autowired
	ProxyClientConfigurationService proxyClientConfigurationService;
	
	@Autowired
	VarLengthAsciiDataDefinitionFileDao varLengthAsciiDataDefinitionFileDao;
	
	@Autowired
	LicenseService licenseService;
	
	@Autowired
	ServletContext servletcontext;
	/**
	 * Generate JAXB XML for service object using service type and id
	 */

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getServiceJAXBByTypeAndId(int serviceId, String serviceClassName, String jaxbXmlPath, String fileName)
			throws SMException {

		Map<String, Object> serviceJAXBMap = new HashMap<>();

		try {

			Class<? extends Service> serviceName ;

			logger.info("inside getServiceJAXBByTypeAndId : service class name:" + serviceClassName);
			serviceName = (Class<? extends Service>) Class.forName(serviceClassName);

			Service serviceDetail = servicesDao.getServicefullHierarchyWithoutMarshlling(serviceId, serviceClassName);
			String inputXmlPath;

			if (fileName != null && !"".equals(fileName)) {
				inputXmlPath = jaxbXmlPath + File.separator + fileName;
			} else {
				inputXmlPath = jaxbXmlPath + File.separator + serviceDetail.getSvctype().getAlias() + BaseConstants.XML_FILE_EXT;
			}
			File inputxml = new File(inputXmlPath);
			JAXBContext jaxbContext;

			jaxbContext = JAXBContext.newInstance(serviceName);

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(serviceDetail, inputxml);
			try (BufferedReader br = new BufferedReader(new FileReader(inputxml))) {

				String fileContent;
				StringBuilder sb = new StringBuilder();
				while ((fileContent = br.readLine()) != null) {
					sb.append(fileContent);
				}
				logger.debug("JAXB XML is" + sb.toString());

				serviceJAXBMap.put(BaseConstants.SERVICE_JAXB_OBJECT, serviceDetail);
				serviceJAXBMap.put(BaseConstants.SERVICE_JAXB_FILE, inputxml);
			}
		} catch (Exception e) {
			throw new SMException(e);
		}

		return serviceJAXBMap;

	}

	/**
	 * Add Service type
	 */
	@Transactional
	@Override
	public ResponseObject addServicetype(ServiceType svcType) {
		ResponseObject responseObject = new ResponseObject();
		servicetypeDao.save(svcType);
		if (svcType.getId() != 0) {
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SERVICE_TYPE_ADD_SUCCESS);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVICE_TYPE_ADD_FAIL);
		}
		return responseObject;
	}

	@Override
	public String getMaxServiceInstanceIdForServer(int serverInstanceId, int serviceTypeId){

		String servInstanceIdStr = servicesDao.getMaxServiceInstanceIdforServerInstance
				(serverInstanceId, serviceTypeId);
		Integer servInstanceId = 0;
		if(servInstanceIdStr != null)
			servInstanceId = Integer.parseInt(servInstanceIdStr) + 1;
		
		return Utilities.getFormattedServiceInstanceId(servInstanceId);
		
	}
	
	/**
	 * Add service into database
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_SERVICE, actionType = BaseConstants.CREATE_ACTION, currentEntity = Service.class, ignorePropList= "")
	public ResponseObject addService(Service service) throws SMException {
		
		ResponseObject responseObject = new ResponseObject();

		ServiceType serviceType = servicetypeDao.getServiceTypeByAlias(service.getSvctype().getAlias());
		
		if(serviceType == null) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVICE_ADD_FAIL);
			return responseObject;
		}
		
		String serviceClassName = serviceType.getServiceFullClassName();

		logger.info("inside addService : service class name:" + serviceClassName);
		Class<? extends Service> serviceTypeName;
		try {
			serviceTypeName = (Class<? extends Service>) Class.forName(serviceClassName);
		} catch (ClassNotFoundException e) {
			logger.error("Exception occured ", e);
			throw new SMException(e.getMessage());
		}

		ServerInstance serverInstance = serverInstanceService.getServerInstance(service.getServerInstance().getId());
		
		if(serverInstance==null) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVICE_ADD_FAIL);
			return responseObject;
		}
		
		Service newService;
		try {
			newService = serviceTypeName.newInstance();

			newService.setName(service.getName().trim());
			newService.setDescription(service.getDescription());
			newService.setServerInstance(serverInstance);
			newService.setSvctype(serviceType);
			newService.setSvcExecParams(new ServiceExecutionParams());
			newService.setCreatedByStaffId(service.getCreatedByStaffId());
			newService.setCreatedDate(new Date());
			newService.setLastUpdatedDate(service.getCreatedDate());
			newService.setLastUpdatedByStaffId(service.getCreatedByStaffId());
			
			String servInstanceIdStr = getMaxServiceInstanceIdForServer(serverInstance.getId(), serviceType.getId());
			newService.setServInstanceId(servInstanceIdStr);

			int serviceCount = servicesDao.getServiceCount(newService.getName(),serverInstance.getId());
			
			if (serviceCount > 0) {
				logger.info("inside addService : duplicate service name found:" + newService.getName());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DUPLICATE_SERVICE_NAME);
				return responseObject;
			} 

			if (newService instanceof NetflowCollectionService) {

				newService.getSvcExecParams().setMinThread(BaseConstants.INITIAL_NETFLOW_COLLECTION_MIN_THREAD);
				newService.getSvcExecParams().setMaxThread(BaseConstants.INITIAL_NETFLOW_COLLECTION_MAX_THREAD);
				newService.getSvcExecParams().setQueueSize(BaseConstants.INITIAL_NETFLOW_COLLECTION_QUEUE_SIZE);
				((NetflowCollectionService) newService).setServerIp(newService.getServerInstance().getServer().getIpAddress());
				((NetflowCollectionService) newService).setServerPort(newService.getServerInstance().getPort());
				logger.info("inside addService : Found Netflow Collection Service object ");

			} else if (newService instanceof SysLogCollectionService) {

				newService.getSvcExecParams().setMinThread(BaseConstants.INITIAL_NETFLOW_COLLECTION_MIN_THREAD);
				newService.getSvcExecParams().setMaxThread(BaseConstants.INITIAL_NETFLOW_COLLECTION_MAX_THREAD);
				newService.getSvcExecParams().setQueueSize(BaseConstants.INITIAL_NETFLOW_COLLECTION_QUEUE_SIZE);
				((SysLogCollectionService) newService).setServerIp(newService.getServerInstance().getServer().getIpAddress());
				((SysLogCollectionService) newService).setServerPort(newService.getServerInstance().getPort());
				logger.info("inside addService : Found Syslog Collection Service object ");

			} else if (newService instanceof GTPPrimeCollectionService) {

				newService.getSvcExecParams().setMinThread(BaseConstants.INITIAL_NETFLOW_COLLECTION_MIN_THREAD);
				newService.getSvcExecParams().setMaxThread(BaseConstants.INITIAL_NETFLOW_COLLECTION_MAX_THREAD);
				newService.getSvcExecParams().setQueueSize(BaseConstants.INITIAL_NETFLOW_GTP_COLLECTION_QUEUE_SIZE);
				((GTPPrimeCollectionService) newService).setNetFlowPort(3386); //According to RFC.
				((GTPPrimeCollectionService) newService).setServerIp(newService.getServerInstance().getServer().getIpAddress());
				((GTPPrimeCollectionService) newService).setServerPort(newService.getServerInstance().getPort());
				logger.info("inside addService : Found Syslog Collection Service object ");

			} else if (newService instanceof NetflowBinaryCollectionService) {

				// Any optional param for Netflow Binary Collection
				// Service
				newService.getSvcExecParams().setMinThread(BaseConstants.INITIAL_NETFLOW_COLLECTION_MIN_THREAD);
				newService.getSvcExecParams().setMaxThread(BaseConstants.INITIAL_NETFLOW_COLLECTION_MAX_THREAD);
				newService.getSvcExecParams().setQueueSize(BaseConstants.INITIAL_NETFLOW_COLLECTION_QUEUE_SIZE);
				((NetflowBinaryCollectionService) newService).setServerIp(newService.getServerInstance().getServer().getIpAddress());
				((NetflowBinaryCollectionService) newService).setServerPort(newService.getServerInstance().getPort());
				logger.info("inside addService : Found Netflow Binary Collection Service object ");
			
			} else if (newService instanceof DiameterCollectionService) {
				newService.getSvcExecParams().setMinThread(BaseConstants.INITIAL_NETFLOW_COLLECTION_MIN_THREAD);
				newService.getSvcExecParams().setMaxThread(BaseConstants.INITIAL_NETFLOW_COLLECTION_MAX_THREAD);
				newService.getSvcExecParams().setQueueSize(BaseConstants.INITIAL_NETFLOW_COLLECTION_QUEUE_SIZE);
				((DiameterCollectionService) newService).setFieldSeparator(BaseConstants.COMMA_SEPARATOR);
				((DiameterCollectionService) newService).setKeyValueSeparator(BaseConstants.EQUALTO_SEPARATOR);
				((DiameterCollectionService) newService).setGroupFieldSeparator(BaseConstants.HASH_SEPARATOR);
				((DiameterCollectionService) newService).setStackIp(newService.getServerInstance().getServer().getIpAddress());
				logger.info("inside addService : Found Diameter Collection Service object ");
				
			} else if (newService instanceof CollectionService) {

				logger.info("inside addService : Found collection service object ");
				((CollectionService) newService).setServiceSchedulingParams(new ServiceSchedulingParams());
				((CollectionService) newService).getServiceSchedulingParams().setCreatedByStaffId(newService.getCreatedByStaffId());
				((CollectionService) newService).getServiceSchedulingParams().setLastUpdatedByStaffId(newService.getCreatedByStaffId());

			} else if (newService instanceof IPLogParsingService) {

				logger.info("inside addService : Found iplog parsing service object ");
				newService.getSvcExecParams().setExecutionInterval(10);
				newService.getSvcExecParams().setMinThread(5);
				newService.getSvcExecParams().setMaxThread(8);
				newService.getSvcExecParams().setQueueSize(1500000);
				newService.getSvcExecParams().setFileBatchSize(50);
				newService.setCreatedByStaffId(newService.getCreatedByStaffId());
				newService.setLastUpdatedByStaffId(newService.getCreatedByStaffId());
				((IPLogParsingService) newService).setRecordBatchSize(5000);

				List<PartitionParam> liPartitionParam = new ArrayList<>();

				PartitionParam p1 = new PartitionParam();
				p1.setPartitionField(PartitionFieldEnum.Date);
				p1.setUnifiedField(UnifiedFieldEnum.StartDate.getName());
				p1.setPartitionRange(DateRangeEnum.HOUR.toString());
				p1.setParsingService((IPLogParsingService) newService);
				p1.setCreatedByStaffId(newService.getCreatedByStaffId());
				p1.setLastUpdatedByStaffId(newService.getCreatedByStaffId());
				liPartitionParam.add(p1);

				PartitionParam p2 = new PartitionParam();
				p2.setPartitionField(PartitionFieldEnum.PUBLIC_IP);
				p2.setUnifiedField(UnifiedFieldEnum.General7.getName());
				p2.setPartitionRange(String.valueOf(50));
				p2.setParsingService((IPLogParsingService) newService);
				p1.setCreatedByStaffId(newService.getCreatedByStaffId());
				p1.setLastUpdatedByStaffId(newService.getCreatedByStaffId());
				liPartitionParam.add(p2);

				PartitionParam p3 = new PartitionParam();
				p3.setPartitionField(PartitionFieldEnum.PRIVATE_IP);
				p3.setUnifiedField(UnifiedFieldEnum.General8.getName());
				p3.setPartitionRange(String.valueOf(100));
				p3.setParsingService((IPLogParsingService) newService);
				p1.setCreatedByStaffId(newService.getCreatedByStaffId());
				p1.setLastUpdatedByStaffId(newService.getCreatedByStaffId());
				liPartitionParam.add(p3);

				((IPLogParsingService) newService).setHashSeparator(HashSeparatorEnum.UNDERSCORE.getValue());
				((IPLogParsingService) newService).setPartionParamList(liPartitionParam);
				((IPLogParsingService) newService).setFileGroupingParameter(new FileGroupingParameterParsing());

			} else if (newService instanceof ParsingService) {

				logger.info("inside addService : Found parsing service object ");
				newService.getSvcExecParams().setExecutionInterval(10);
				newService.getSvcExecParams().setMinThread(5);
				newService.getSvcExecParams().setMaxThread(8);
				newService.getSvcExecParams().setQueueSize(3000);
				newService.setCreatedByStaffId(newService.getCreatedByStaffId());
				newService.setLastUpdatedByStaffId(newService.getCreatedByStaffId());
				((ParsingService) newService).setFileGroupingParameter(new FileGroupingParameterParsing());

			} else if (newService instanceof DistributionService) {
				logger.info("inside addService : Found distribution service object ");

				((DistributionService) newService).setServiceSchedulingParams(new ServiceSchedulingParams());
				((DistributionService) newService).getServiceSchedulingParams().setCreatedByStaffId(newService.getCreatedByStaffId());
				((DistributionService) newService).getServiceSchedulingParams().setLastUpdatedByStaffId(newService.getCreatedByStaffId());
				((DistributionService) newService).setFileGroupingParameter(new FileGroupingParameterParsing());

			} else if (newService instanceof ProcessingService) {
				logger.info("inside addService : Found Processing service object ");
				newService.getSvcExecParams().setStartupMode(StartUpModeEnum.Automatic);
				newService.getSvcExecParams().setExecutionInterval(10);
				newService.getSvcExecParams().setExecuteOnStartup(true);
				newService.getSvcExecParams().setFileBatchSize(10);
				newService.getSvcExecParams().setMinThread(10);
				newService.getSvcExecParams().setMaxThread(15);
				newService.getSvcExecParams().setQueueSize(3000);
				newService.getSvcExecParams().setSortingType(SortingTypeEnum.Ascending.getValue());
				newService.getSvcExecParams().setSortingCriteria(SortingCriteriaEnum.Last_Modified_Date.getValue());
				newService.setCreatedByStaffId(newService.getCreatedByStaffId());
				newService.setLastUpdatedByStaffId(newService.getCreatedByStaffId());
				
				((ProcessingService) newService).setFileSeqOrderEnable(false);
				((ProcessingService) newService).setMinFileRange(1);
				((ProcessingService) newService).setMaxFileRange(300);
				((ProcessingService) newService).setNoFileAlert(-1);
				((ProcessingService) newService).setRecordBatchSize(5000);
				
				((ProcessingService) newService).setAcrossFileDuplicatePurgeCacheInterval(5);	
				((ProcessingService) newService).setGlobalSeqEnabled(false);
				((ProcessingService) newService).setGlobalSeqMaxLimit(100000);
				((ProcessingService) newService).setStoreCDRFileSummaryDB(true);
				((ProcessingService) newService).setOverrideFileDateEnabled(false);
				((ProcessingService) newService).setOverrideFileDateType(CDRFileDateTypeEnum.MAXIMUM.getValue());
				
				
				
				FileGroupingParameterProcessing groupingParameters = new FileGroupingParameterProcessing();
				groupingParameters.setFileGroupEnable(true);
				groupingParameters.setGroupingType(FileGroupEnum.DAY.getFileGroupEnum());
				groupingParameters.setEnableForArchive(true);
				groupingParameters.setEnableForDuplicate(true);
				groupingParameters.setEnableForFilter(true);
				groupingParameters.setEnableForInvalid(true);
				groupingParameters.setFilterGroupType(FilterGroupTypeEnum.Rulewise);
				((ProcessingService) newService).setFileGroupingParameter(groupingParameters);
				
			} else if (newService instanceof DataConsolidationService){
				FileGroupingParamConsolidation groupingParam = new FileGroupingParamConsolidation();
				groupingParam.setFileGroupEnable(true);
				groupingParam.setGroupingType(FileGroupEnum.DAY.getFileGroupEnum());
				groupingParam.setEnableForArchive(true);
				((DataConsolidationService) newService).setFileGroupParam(groupingParam);
				((DataConsolidationService) newService).getSvcExecParams().setQueueSize(10000);
				((DataConsolidationService) newService).getSvcExecParams().setMaxThread(10);
				((DataConsolidationService) newService).getSvcExecParams().setFileBatchSize(10);
				
			} else if (newService instanceof AggregationService) {
				logger.info("inside addService : Found Aggregation service object ");
				newService.getSvcExecParams().setStartupMode(StartUpModeEnum.Automatic);
				newService.getSvcExecParams().setExecutionInterval(60);
				newService.getSvcExecParams().setExecuteOnStartup(true);
				newService.getSvcExecParams().setFileBatchSize(10);
				newService.getSvcExecParams().setMinThread(5);
				newService.getSvcExecParams().setMaxThread(10);
				newService.getSvcExecParams().setQueueSize(10000);
				newService.getSvcExecParams().setSortingType(SortingTypeEnum.Ascending.getValue());
				newService.getSvcExecParams().setSortingCriteria(SortingCriteriaEnum.Last_Modified_Date.getValue());
				newService.setCreatedByStaffId(newService.getCreatedByStaffId());
				newService.setLastUpdatedByStaffId(newService.getCreatedByStaffId());
				((AggregationService) newService).setMinFileRange(10);
				((AggregationService) newService).setMaxFileRange(300);
				((AggregationService) newService).setNoFileAlert(10);
				((AggregationService) newService).setDelimiter(BaseConstants.PIPE_DELIMITER);		
				
				FileGroupingParameterAggregation groupingParameters = new FileGroupingParameterAggregation();
				groupingParameters.setFileGroupEnable(false);
				groupingParameters.setSourcewiseArchive(false);
				groupingParameters.setGroupingType(FileGroupEnum.DAY.getFileGroupEnum());
				
				((AggregationService) newService).setFileGroupingParameter(groupingParameters);
				
				ServiceSchedulingParams serviceSchedulingParams = new ServiceSchedulingParams();
				serviceSchedulingParams.setSchedulingEnabled(false);
				
				((AggregationService) newService).setServiceSchedulingParams(serviceSchedulingParams);
				
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVICE_ADD_FAIL);
			}
			// implement code for another service
			logger.info("inside addService : Going to save service and all dependants are save automatically ");
			servicesDao.addServiceDetails(newService);

			if (newService.getId() != 0) {

				logger.debug(" Service Add Successfully now , add enable / disable flag for packet statistics agent");

				AgentType agentType = agentService.getAgentTypeByAlias(BaseConstants.PACKET_STATISTICS_AGENT);
				if (agentType != null) {
					ResponseObject agentResponseObject = agentService.getAgentByServerInstanceIdAndAgentTypeID(serverInstance.getId(),
							agentType.getId());
					if (agentResponseObject.isSuccess()) {
						List<ServicePacketStatsConfig> servicePacketList = new ArrayList<>();
						PacketStatisticsAgent agent = (PacketStatisticsAgent) agentResponseObject.getObject();
						 
						ServicePacketStatsConfig servicePacket = new ServicePacketStatsConfig();
						if (ServiceCategoryEnum.ONLINE.equals(serviceType.getServiceCategory()) || (serviceType.getAlias().equalsIgnoreCase("PARSING_SERVICE") && serverInstance.getServer().getServerType().getAlias().equalsIgnoreCase("MEDIATION"))) {
							servicePacket.setEnable(true);
						} else {
							servicePacket.setEnable(false);
						}
						servicePacket.setService(newService);
						servicePacket.setAgent(agent);
						servicePacketList.add(servicePacket);
						agent.setServiceList(servicePacketList);
						agentDao.merge(agent);
					}
				}
				responseObject.setSuccess(true);
				responseObject.setObject(newService.getId());
			} else {
				responseObject.setSuccess(false);
			}
		} catch (InstantiationException | IllegalAccessException | NumberFormatException e ) {
			logger.error("Exception occured ", e);
			throw new SMException(e.getMessage());
		}

		return responseObject;
	}

	/**
	 * Update Service
	 * 
	 * @param service
	 * @return service
	 */
	@Override
	public ResponseObject updateService(Service service) {
		ResponseObject responseObject = new ResponseObject();

		ServerInstance serverInstance = serverInstanceService.getServerInstance(service.getServerInstance().getId());
		if (serverInstance != null) {

			service.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
			service.setLastUpdatedDate(new Date());
			service.setServerInstance(serverInstance);
			service.setName(service.getName().trim());
			servicesDao.merge(service);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SERVICE_UPDATE_SUCCESS);
			responseObject.setObject(service);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVICE_UPDATE_FAIL);
		}

		return responseObject;
	}
	
	
	/**
	 * Update Service
	 * 
	 * @param service
	 * @return service
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseObject updateMigService(Service service) {
		ResponseObject responseObject = new ResponseObject();

		if(service!=null) {
			servicesDao.save(service);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SERVICE_UPDATE_SUCCESS);
			responseObject.setObject(service);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVICE_UPDATE_FAIL);
		}

		return responseObject;
	}
	

	/**
	 * Method will add or delete scheduling parameters for new service based on
	 * service type.
	 * 
	 * @param service
	 * @param isImport
	 * @return
	 */
	public void iterateServiceSchedulingParams(Service newService, boolean isImport) {

		logger.debug("Going to create new scheduling parameters for new service");
		ServiceSchedulingParams schedulingParam = null;
		CollectionService collectionService = null;
		DistributionService distributionService = null;
		AggregationService aggregationService = null;
		if (newService instanceof CollectionService) {
			logger.debug("Found collection service object.");
			collectionService = (CollectionService) newService;
			schedulingParam = collectionService.getServiceSchedulingParams();
		} else if (newService instanceof DistributionService) {
			logger.debug("Found Distribution service object.");
			distributionService = (DistributionService) newService;
			schedulingParam = distributionService.getServiceSchedulingParams();
		} else if (newService instanceof AggregationService) {
			logger.debug("Found Aggregation service object.");
			aggregationService = (AggregationService) newService;
			schedulingParam = aggregationService.getServiceSchedulingParams();
		}
		if (isImport) { // import call
			if (schedulingParam != null) {
				schedulingParam.setId(0);
				schedulingParam.setCreatedByStaffId(newService.getLastUpdatedByStaffId());
				schedulingParam.setCreatedDate(new Date());
				schedulingParam.setLastUpdatedByStaffId(newService.getLastUpdatedByStaffId());
			} else { // when scheduling is not in file
				schedulingParam = new ServiceSchedulingParams();
				schedulingParam.setCreatedByStaffId(newService.getLastUpdatedByStaffId());
				schedulingParam.setCreatedDate(new Date());
				schedulingParam.setLastUpdatedByStaffId(newService.getLastUpdatedByStaffId());
			}
		} else {
			if (schedulingParam != null) {
				schedulingParam.setStatus(StateEnum.DELETED);
				schedulingParam.setLastUpdatedByStaffId(newService.getLastUpdatedByStaffId());
			}
		}

		if (newService instanceof CollectionService && collectionService != null) {
			collectionService.setServiceSchedulingParams(schedulingParam);
		} else if (newService instanceof DistributionService && distributionService != null) {
			distributionService.setServiceSchedulingParams(schedulingParam);
		} else if (newService instanceof AggregationService && aggregationService != null) {
			aggregationService.setServiceSchedulingParams(schedulingParam);
		}
	}
	
	@Override
	public void importServiceScheduleParamsUpdateMode(ServiceSchedulingParams dbParam, ServiceSchedulingParams exportedParam) {
		dbParam.setSchedulingEnabled(exportedParam.isSchedulingEnabled());
		dbParam.setSchType(exportedParam.getSchType());
		dbParam.setDate(exportedParam.getDate());
		dbParam.setDay(exportedParam.getDay());
		dbParam.setTime(exportedParam.getTime());
	}
	
	@Override
	public void importServiceScheduleParamsAddAndKeepBothMode(ServiceSchedulingParams exportedParam) {
		exportedParam.setId(0);
		exportedParam.setCreatedDate(EliteUtils.getDateForImport(false));
	}
	
	/**
	 * Method will create new file grouping parameters and set it to selected
	 * service instance.
	 * 
	 * @param newService
	 * @param isImport
	 * @return
	 */
	public ResponseObject iterateServiceFileGroupParams(Service newService, boolean isImport) {

		ResponseObject responseObject;
		logger.debug("Going to create new File Grouping parameters for new service");
		FileGroupingParameterParsing fileGroupParamsParsing;

		if (newService instanceof ParsingService) {
			logger.debug("Found parsing service object.");
			ParsingService parsingService = (ParsingService) newService;

			fileGroupParamsParsing = parsingService.getFileGroupingParameter();
			fileGroupParamsParsing.setCreatedByStaffId(newService.getLastUpdatedByStaffId());
			fileGroupParamsParsing.setLastUpdatedByStaffId(newService.getLastUpdatedByStaffId());

			responseObject = iterateFileGroupParams(fileGroupParamsParsing, isImport);

			if (responseObject.isSuccess()) {
				parsingService.setFileGroupingParameter(fileGroupParamsParsing);
				responseObject.setObject(parsingService);
			}
		}else if (newService instanceof ProcessingService){
			logger.debug("Found parsing service object.");
			ProcessingService processingService = (ProcessingService) newService;
			FileGroupingParameterProcessing processingGrouping = processingService.getFileGroupingParameter();
			processingGrouping.setCreatedByStaffId(newService.getLastUpdatedByStaffId());
			processingGrouping.setLastUpdatedByStaffId(newService.getLastUpdatedByStaffId());
			responseObject = iterateFileGroupParams(processingGrouping, isImport);
			if (responseObject.isSuccess()) {
				processingService.setFileGroupingParameter(processingGrouping);
				responseObject.setObject(processingService);
			}
		}else if (newService instanceof DataConsolidationService) {
			logger.debug("Found DataConsolidation service object.");
			DataConsolidationService dataconsolidationService = (DataConsolidationService) newService;

			FileGroupingParamConsolidation fileGroupParamsCons = dataconsolidationService.getFileGroupParam();
			fileGroupParamsCons.setCreatedByStaffId(newService.getLastUpdatedByStaffId());
			fileGroupParamsCons.setLastUpdatedByStaffId(newService.getLastUpdatedByStaffId());

			responseObject = iterateFileGroupParams(fileGroupParamsCons, isImport);

			if (responseObject.isSuccess()) {
				dataconsolidationService.setFileGroupParam(fileGroupParamsCons);
				responseObject.setObject(dataconsolidationService);
			}
		}else if (newService instanceof AggregationService) {
			logger.debug("Found Aggregation service object.");
			AggregationService aggregationService = (AggregationService) newService;

			FileGroupingParameterAggregation  fileGroupParamsAgg = aggregationService.getFileGroupingParameter();
			fileGroupParamsAgg.setCreatedByStaffId(newService.getLastUpdatedByStaffId());
			fileGroupParamsAgg.setLastUpdatedByStaffId(newService.getLastUpdatedByStaffId());
			responseObject = iterateFileGroupParams(fileGroupParamsAgg, isImport);

			if (responseObject.isSuccess()) {
				aggregationService.setFileGroupingParameter(fileGroupParamsAgg);
				responseObject.setObject(aggregationService);
			}
		}
		else {
			logger.debug("Found distributionService service object.");
			DistributionService distributionService = (DistributionService) newService;

			fileGroupParamsParsing = distributionService.getFileGroupingParameter();
			fileGroupParamsParsing.setCreatedByStaffId(newService.getLastUpdatedByStaffId());
			fileGroupParamsParsing.setLastUpdatedByStaffId(newService.getLastUpdatedByStaffId());

			responseObject = iterateFileGroupParams(fileGroupParamsParsing, isImport);

			if (responseObject.isSuccess()) {
				distributionService.setFileGroupingParameter(fileGroupParamsParsing);
				responseObject.setObject(distributionService);
			}
		}
		return responseObject;
	}
	
	@Override
	public void importParsingServiceBasicParameterUpdateMode(ParsingService dbParsingService, ParsingService exportedParsingService) {
		dbParsingService.setNoFileAlert(exportedParsingService.getNoFileAlert());
		dbParsingService.setMinFileRange(exportedParsingService.getMinFileRange());
		dbParsingService.setMaxFileRange(exportedParsingService.getMaxFileRange());
		dbParsingService.setFileSeqOrderEnable(exportedParsingService.isFileSeqOrderEnable());
		dbParsingService.setStoreCDRFileSummaryDB(exportedParsingService.isStoreCDRFileSummaryDB());
		dbParsingService.setEqualCheckField(exportedParsingService.getEqualCheckField());
		dbParsingService.setEqualCheckFunction(exportedParsingService.getEqualCheckFunction());
		dbParsingService.setFileStatInsertEnable(exportedParsingService.isFileStatInsertEnable());
		dbParsingService.setEqualCheckValue(exportedParsingService.getEqualCheckValue());
		dbParsingService.setDateFieldForSummary(exportedParsingService.getDateFieldForSummary());
		dbParsingService.setTypeForSummary(exportedParsingService.getTypeForSummary());
		dbParsingService.setOverrideFileDateEnabled(exportedParsingService.getOverrideFileDateEnabled());
		dbParsingService.setOverrideFileDateType(exportedParsingService.getOverrideFileDateType());
		dbParsingService.setRecordBatchSize(exportedParsingService.getRecordBatchSize());
		dbParsingService.setErrorPath(exportedParsingService.getErrorPath());
	}
	
	@Override
	public void importDistributionServiceBasicParameterUpdateMode(DistributionService dbDistributionService, DistributionService exportedDistributionService) {
		dbDistributionService.setThirdPartyTransferEnabled(exportedDistributionService.isThirdPartyTransferEnabled());
		dbDistributionService.setTimestenDatasourceName(exportedDistributionService.getTimestenDatasourceName());
		dbDistributionService.setProcessRecordLimit(exportedDistributionService.getProcessRecordLimit());
		dbDistributionService.setWriteRecordLimit(exportedDistributionService.getWriteRecordLimit());
		dbDistributionService.setFileMergeEnabled(exportedDistributionService.isFileMergeEnabled());
		dbDistributionService.setFileMergeGroupingBy(exportedDistributionService.getFileMergeGroupingBy());
		dbDistributionService.setRemainingFileMergeEnabled(exportedDistributionService.isRemainingFileMergeEnabled());
		dbDistributionService.setErrorPath(exportedDistributionService.getErrorPath());
	}
	
	@Override
	public void importProcessingServiceBasicParameterUpdateMode(ProcessingService dbProcessingService, ProcessingService exportedProcessingService) {
		dbProcessingService.setFileSeqOrderEnable(exportedProcessingService.isFileSeqOrderEnable());
		dbProcessingService.setMinFileRange(exportedProcessingService.getMinFileRange());
		dbProcessingService.setMaxFileRange(exportedProcessingService.getMaxFileRange());
		dbProcessingService.setRecordBatchSize(exportedProcessingService.getRecordBatchSize());
		dbProcessingService.setGlobalSeqEnabled(exportedProcessingService.isGlobalSeqEnabled());
		dbProcessingService.setGlobalSeqDeviceName(exportedProcessingService.getGlobalSeqDeviceName());
		dbProcessingService.setGlobalSeqMaxLimit(exportedProcessingService.getGlobalSeqMaxLimit());
		dbProcessingService.setErrorPath(exportedProcessingService.getErrorPath());
		dbProcessingService.setAcrossFileDuplicatePurgeCacheInterval(exportedProcessingService.getAcrossFileDuplicatePurgeCacheInterval());
		dbProcessingService.setNoFileAlert(exportedProcessingService.getNoFileAlert());
		dbProcessingService.setDateFieldForSummary(exportedProcessingService.getDateFieldForSummary());
		dbProcessingService.setOverrideFileDateEnabled(exportedProcessingService.getOverrideFileDateEnabled());
		dbProcessingService.setOverrideFileDateType(exportedProcessingService.getOverrideFileDateType());
		dbProcessingService.setStoreCDRFileSummaryDB(exportedProcessingService.isStoreCDRFileSummaryDB());
		dbProcessingService.setTypeForSummary(exportedProcessingService.getTypeForSummary());
		
		
	}
	
	@Override
	public void importIpLogParsingServiceBasicParameterForUpdateMode(IPLogParsingService dbService, IPLogParsingService exportedService) {
		dbService.setEqualCheckField(exportedService.getEqualCheckField());
		dbService.setEqualCheckFunction(exportedService.getEqualCheckFunction());
		dbService.setEqualCheckValue(exportedService.getEqualCheckValue());
		dbService.setRecordBatchSize(exportedService.getRecordBatchSize());
		dbService.setHashSeparator(exportedService.getHashSeparator());
		dbService.setIndexType(exportedService.getIndexType());
		dbService.setPurgeInterval(exportedService.getPurgeInterval());
		dbService.setPurgeDelayInterval(exportedService.getPurgeDelayInterval());
		dbService.setOutFileHeaders(exportedService.getOutFileHeaders());
		dbService.setFileStatsEnabled(exportedService.isFileStatsEnabled());
		dbService.setFileStatsLoc(exportedService.getFileStatsLoc());
		dbService.setCorrelEnabled(exportedService.isCorrelEnabled());
		dbService.setMappedSourceField(exportedService.getMappedSourceField());
		dbService.setDestPortField(exportedService.getDestPortField());
		dbService.setDestPortFilter(exportedService.getDestPortFilter());
		dbService.setCreateRecDestPath(exportedService.getCreateRecDestPath());
		dbService.setDeleteRecDestPath(exportedService.getDeleteRecDestPath());
		dbService.setDataType(exportedService.getDataType());
		dbService.setOutputFileHeader(exportedService.getOutputFileHeader());
	}
	
	@Override
	public void importServicePartitionParamAddAndKeepBothMode(IPLogParsingService exportedService) {
		List<PartitionParam> exportedPartitionParamList = exportedService.getPartionParamList();
		if(!CollectionUtils.isEmpty(exportedPartitionParamList)) {
			int length = exportedPartitionParamList.size();
			for(int i = length-1; i >= 0; i--) {
				PartitionParam exportedPartitionParam = exportedPartitionParamList.get(i);
				if(exportedPartitionParam != null) {
					partitionParamService.importPartitionParamAddAndKeepBothMode(exportedPartitionParam, exportedService);
				}
			}
		}
	}
	
	@Override
	public void importServicePartitionParamUpdateMode(IPLogParsingService dbService, IPLogParsingService exportedService) {
		logger.debug("import : going to update partition param for service : "+dbService.getName());
		List<PartitionParam> dbPartitionParamList = dbService.getPartionParamList();
		List<PartitionParam> exportedPartitionParamList = exportedService.getPartionParamList();
		if(CollectionUtils.isEmpty(exportedPartitionParamList) && 
			(CollectionUtils.isEmpty(dbPartitionParamList) || isPartitionParamListDeleted(dbPartitionParamList)) ){
			
			dbPartitionParamList = new ArrayList<>();
			dbService.setPartionParamList(dbPartitionParamList);
			PartitionParam p1 = new PartitionParam();
			p1.setPartitionField(PartitionFieldEnum.Date);
			p1.setUnifiedField(UnifiedFieldEnum.StartDate.getName());
			p1.setPartitionRange(DateRangeEnum.HOUR.toString());
			p1.setParsingService(dbService);
			p1.setCreatedByStaffId(dbService.getCreatedByStaffId());
			p1.setLastUpdatedByStaffId(dbService.getCreatedByStaffId());
			dbPartitionParamList.add(p1);

			PartitionParam p2 = new PartitionParam();
			p2.setPartitionField(PartitionFieldEnum.PUBLIC_IP);
			p2.setUnifiedField(UnifiedFieldEnum.General7.getName());
			p2.setPartitionRange(String.valueOf(50));
			p2.setParsingService(dbService);
			p2.setCreatedByStaffId(dbService.getCreatedByStaffId());
			p2.setLastUpdatedByStaffId(dbService.getCreatedByStaffId());
			dbPartitionParamList.add(p2);

			PartitionParam p3 = new PartitionParam();
			p3.setPartitionField(PartitionFieldEnum.PRIVATE_IP);
			p3.setUnifiedField(UnifiedFieldEnum.General8.getName());
			p3.setPartitionRange(String.valueOf(100));
			p3.setParsingService(dbService);
			p3.setCreatedByStaffId(dbService.getCreatedByStaffId());
			p3.setLastUpdatedByStaffId(dbService.getCreatedByStaffId());
			dbPartitionParamList.add(p3);
			
		}else{
			if(CollectionUtils.isEmpty(dbPartitionParamList)){
				dbPartitionParamList = new ArrayList<>();
				dbService.setPartionParamList(dbPartitionParamList);
			}
			if(!CollectionUtils.isEmpty(exportedPartitionParamList)) {
				int length = exportedPartitionParamList.size();
				for(int i = length-1; i >= 0; i--) {
					PartitionParam exportedPartitionParam = exportedPartitionParamList.get(i);
					if(exportedPartitionParam != null) {
						PartitionParam dbPartitionParam = partitionParamService.getPartitionParamFromList(dbPartitionParamList, exportedPartitionParam.getPartitionField());
						if(dbPartitionParam != null) {
							logger.debug("going to update partition param for import : "+dbPartitionParam.getPartitionField());
							partitionParamService.importPartitionParamUpdateMode(dbPartitionParam, exportedPartitionParam);
							dbPartitionParamList.add(dbPartitionParam);
						}else{
							dbPartitionParam = new PartitionParam();
							dbPartitionParam.setId(0);
							dbPartitionParam.setPartitionField(exportedPartitionParam.getPartitionField());
							dbPartitionParam.setParsingService(dbService);
							dbPartitionParam.setCreatedByStaffId(dbService.getCreatedByStaffId());
							dbPartitionParam.setLastUpdatedByStaffId(dbService.getCreatedByStaffId());
							partitionParamService.importPartitionParamUpdateMode(dbPartitionParam, exportedPartitionParam);
							dbPartitionParamList.add(dbPartitionParam);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void importServicePartitionParamAddMode(IPLogParsingService dbService, IPLogParsingService exportedService) {
		logger.debug("import : going to update partition param for service : "+dbService.getName());
		List<PartitionParam> dbPartitionParamList = dbService.getPartionParamList();
		List<PartitionParam> exportedPartitionParamList = exportedService.getPartionParamList();
		if(!CollectionUtils.isEmpty(exportedPartitionParamList) && 
				(CollectionUtils.isEmpty(dbPartitionParamList) || 
						!isPartitionParamListDeleted(dbPartitionParamList))){			
			dbPartitionParamList = new ArrayList<>();
			dbService.setPartionParamList(dbPartitionParamList);
			if(!CollectionUtils.isEmpty(exportedPartitionParamList)) {
				int length = exportedPartitionParamList.size();
				for(int i = length-1; i >= 0; i--) {
					PartitionParam exportedPartitionParam = exportedPartitionParamList.get(i);
					if(exportedPartitionParam != null) {
						PartitionParam dbPartitionParam = partitionParamService.getPartitionParamFromList(dbPartitionParamList, exportedPartitionParam.getPartitionField());
						if(dbPartitionParam == null) {							
							dbPartitionParam = new PartitionParam();
							dbPartitionParam.setId(0);
							dbPartitionParam.setPartitionField(exportedPartitionParam.getPartitionField());
							dbPartitionParam.setParsingService(dbService);
							dbPartitionParam.setCreatedByStaffId(dbService.getCreatedByStaffId());
							dbPartitionParam.setLastUpdatedByStaffId(dbService.getCreatedByStaffId());
							partitionParamService.importPartitionParamUpdateMode(dbPartitionParam, exportedPartitionParam);
							dbPartitionParamList.add(dbPartitionParam);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void importDataConsolidationServiceBasicParameterForUpdateMode(DataConsolidationService dbService, DataConsolidationService exportedService) {
		dbService.setNoFileAlertInterval(exportedService.getNoFileAlertInterval());
		dbService.setMinFileRange(exportedService.getMinFileRange());
		dbService.setMaxFileRange(exportedService.getMaxFileRange());
		dbService.setProcessingType(exportedService.getProcessingType());
		dbService.setMergeDelimiter(exportedService.getMergeDelimiter());
		dbService.setMinFileBatchSize(exportedService.getMinFileBatchSize());
		dbService.setMaxFileBatchSize(exportedService.getMaxFileBatchSize());
		dbService.setConsolidationType(exportedService.getConsolidationType());
		dbService.setAcrossFileProcessingType(exportedService.getAcrossFileProcessingType());
		dbService.setAcrossFilePartition(exportedService.getAcrossFilePartition());
		dbService.setAcrossFileMinBatchSize(exportedService.getAcrossFileMinBatchSize());
		dbService.setAcrossFileMaxBatchSize(exportedService.getAcrossFileMaxBatchSize());
	}
	
	@Override
	public void importAggreagationServiceBasicParameterForUpdateMode(AggregationService dbService, AggregationService exportedService){
		dbService.getSvcExecParams().setExecuteOnStartup(exportedService.getSvcExecParams().isExecuteOnStartup());
		dbService.getSvcExecParams().setStartupMode(exportedService.getSvcExecParams().getStartupMode());
		dbService.getSvcExecParams().setExecutionInterval(exportedService.getSvcExecParams().getExecutionInterval());
		dbService.getSvcExecParams().setQueueSize(exportedService.getSvcExecParams().getQueueSize());
		dbService.getSvcExecParams().setMinThread(exportedService.getSvcExecParams().getMinThread());
		dbService.getSvcExecParams().setMaxThread(exportedService.getSvcExecParams().getMaxThread());		
		dbService.getSvcExecParams().setFileBatchSize(exportedService.getSvcExecParams().getFileBatchSize());
		dbService.getSvcExecParams().setSortingType(exportedService.getSvcExecParams().getSortingType());
		dbService.getSvcExecParams().setMaxThread(exportedService.getSvcExecParams().getMaxThread());		
		dbService.setMinFileRange(exportedService.getMinFileRange());
		dbService.setMaxFileRange(exportedService.getMaxFileRange());
		dbService.setNoFileAlert(exportedService.getNoFileAlert());
		dbService.setErrorPath(exportedService.getErrorPath());
		dbService.setDelimiter(exportedService.getDelimiter());
	}
	

	@Override
	public void importDiameterCollectionServiceBasicParameterForUpdateMode(DiameterCollectionService dbService, DiameterCollectionService exportedService) {
		dbService.setStackIp(exportedService.getStackIp());
		dbService.setStackPort(exportedService.getStackPort());
		dbService.setStackIdentity(exportedService.getStackIdentity());
		dbService.setStackRealm(exportedService.getStackRealm());
		dbService.setSessionCleanupInterval(exportedService.getSessionCleanupInterval());
		dbService.setSessionTimeout(exportedService.getSessionTimeout());
		dbService.setActionOnOverload(exportedService.getActionOnOverload());
		dbService.setResultCodeOnOverload(exportedService.getResultCodeOnOverload());
		dbService.setDuplicateRequestCheck(exportedService.isDuplicateRequestCheck());
		dbService.setDuplicatePurgeInterval(exportedService.getDuplicatePurgeInterval());
		dbService.setFieldSeparator(exportedService.getFieldSeparator());
		dbService.setKeyValueSeparator(exportedService.getKeyValueSeparator());
		dbService.setGroupFieldSeparator(exportedService.getGroupFieldSeparator());
	}
	
	@Override
	public void importOnlineCollectionServiceBasicParameterForUpdateMode(Service dbService, Service exportedService, int importMode) {
		if(dbService instanceof NetflowCollectionService && exportedService instanceof NetflowCollectionService) {
			if(importMode==BaseConstants.IMPORT_MODE_ADD) {				
				iteratenatFlowProxyClientInAddMode((NetflowCollectionService) dbService, (NetflowCollectionService) exportedService);
			} else {
				iterateNetflowCollectionService((NetflowCollectionService) dbService, (NetflowCollectionService) exportedService);	
			}
		} else {
			if(importMode==BaseConstants.IMPORT_MODE_ADD) {				
				iteratenatFlowProxyClientInAddMode((NetflowBinaryCollectionService) dbService, (NetflowBinaryCollectionService) exportedService);
			} else { 
				iterateNetflowBinaryCollectionService((NetflowBinaryCollectionService) dbService, (NetflowBinaryCollectionService) exportedService);
			}
		}
	}
	
	public void iterateNetflowCollectionService(NetflowCollectionService dbService, NetflowCollectionService exportedService) {
		dbService.setEnableParallelBinaryWrite(exportedService.isEnableParallelBinaryWrite());
		dbService.setReadTemplateOnInit(exportedService.isReadTemplateOnInit());
		dbService.setOptionTemplateEnable(exportedService.isOptionTemplateEnable());
		dbService.setOptionTemplateId(exportedService.getOptionTemplateId());
		dbService.setOptionTemplateKey(exportedService.getOptionTemplateKey());
		dbService.setOptionTemplateValue(exportedService.getOptionTemplateValue());
		dbService.setOptionCopytoTemplateId(exportedService.getOptionCopytoTemplateId());
		dbService.setOptionCopyTofield(exportedService.getOptionCopyTofield());
		
		iterateNetflowBinaryCollectionService(dbService, exportedService);
	}
	
	public void iterateNetflowBinaryCollectionService(NetflowBinaryCollectionService dbService, NetflowBinaryCollectionService exportedService) {
		dbService.setServerIp(exportedService.getServerIp());
		dbService.setServerPort(exportedService.getServerPort());
		dbService.setNetFlowPort(exportedService.getNetFlowPort());
		dbService.setNewLineCharAvailable(exportedService.isNewLineCharAvailable());
		dbService.setBulkWriteLimit(exportedService.getBulkWriteLimit());
		dbService.setMaxPktSize(exportedService.getMaxPktSize());
		dbService.setMaxWriteBufferSize(exportedService.getMaxWriteBufferSize());
		dbService.setParallelFileWriteCount(exportedService.getParallelFileWriteCount());
		dbService.setIsTCPProtocol(exportedService.getIsTCPProtocol());
		dbService.setSnmpAlertEnable(exportedService.isSnmpAlertEnable());
		dbService.setSnmpTimeInterval(exportedService.getSnmpTimeInterval());
		dbService.setMaxIdelCommuTime(exportedService.getMaxIdelCommuTime());
		dbService.setRedirectionIP(exportedService.getRedirectionIP());
		dbService.setMaxReadRate(exportedService.getMaxReadRate());
		dbService.setReceiverBufferSize(exportedService.getReceiverBufferSize());
		dbService.setConnectAttemptsMax(exportedService.getConnectAttemptsMax());
		dbService.setReconnectAttemptsMax(exportedService.getReconnectAttemptsMax());
		dbService.setReconnectDelay(exportedService.getReconnectDelay());
		// Import Overrite & update for natFlow Collection Services
		dbService.setProxyClientEnable(exportedService.isProxyClientEnable());
		dbService.setProxyServicePort(exportedService.getProxyServicePort());
		iteratenatFlowProxyClientInUpdateMode(dbService,exportedService);
		
		dbService.setPacketThreshold(exportedService.getPacketThreshold());
		dbService.setBitThreshold(exportedService.getBitThreshold());
		
		if(dbService instanceof Http2CollectionService && exportedService instanceof Http2CollectionService) {
			Http2CollectionService dbHttp2Service = (Http2CollectionService) dbService;
			Http2CollectionService exportedHttp2Service = (Http2CollectionService) exportedService;
			dbHttp2Service.setEncryption(exportedHttp2Service.isEncryption());
			dbHttp2Service.setSecureScheme(exportedHttp2Service.getSecureScheme());
			dbHttp2Service.setKeystorePassword(exportedHttp2Service.getKeystorePassword());
			dbHttp2Service.setKeymanagerPassword(exportedHttp2Service.getKeymanagerPassword());
		}
	}
	
	@Override
	public void iteratenatFlowProxyClientInUpdateMode(NetflowBinaryCollectionService dbService, NetflowBinaryCollectionService exportedService) { // MED-8256
		
		Iterator<NatFlowProxyClient> exportIterator = exportedService.getNatFlowProxyClients().iterator();
		while(exportIterator.hasNext()) {
			NatFlowProxyClient natFlowProxyClient = exportIterator.next();
			natFlowProxyClient.setId(0);
			natFlowProxyClient.setService(dbService);
		}
		
		Iterator<NatFlowProxyClient> dbNatFlowProxyClients = dbService.getNatFlowProxyClients().iterator();
		boolean isUpdateMode = false;
		while (dbNatFlowProxyClients.hasNext()) {
			NatFlowProxyClient natFlowProxyClient = dbNatFlowProxyClients.next();
			if(natFlowProxyClient.getStatus().equals(StateEnum.ACTIVE)) {
					isUpdateMode = true;
					break;
			}
		}
		if(!isUpdateMode) {
			dbService.setNatFlowProxyClients(exportedService.getNatFlowProxyClients());
			return;
		}
		
		Map<Map<String,Integer>,NatFlowProxyClient> proxyClientMap = new HashMap<>();
		List<NatFlowProxyClient> natFlowProxyClients = dbService.getNatFlowProxyClients();
		natFlowProxyClients.addAll(exportedService.getNatFlowProxyClients());
		Iterator<NatFlowProxyClient> natflowproxyIterator = natFlowProxyClients.iterator();
		while(natflowproxyIterator.hasNext()) {
			NatFlowProxyClient natFlowProxyClient = natflowproxyIterator.next();
			if(!natFlowProxyClient.getStatus().equals(StateEnum.ACTIVE)) {
				natflowproxyIterator.remove();
				continue;
			}
			Map<String,Integer> proxyMap = new HashMap<>();
			proxyMap.put(natFlowProxyClient.getProxyIp(), natFlowProxyClient.getProxyPort());
			if(proxyClientMap.containsKey(proxyMap)) {
				natflowproxyIterator.remove();
			}else {
					proxyClientMap.put(proxyMap, natFlowProxyClient);
			}
		}
		dbService.setNatFlowProxyClients(natFlowProxyClients);
	}
	
	@Override
	public void iteratenatFlowProxyClientInAddMode(NetflowBinaryCollectionService dbService, NetflowBinaryCollectionService exportedService) { // MED-8256
		
		Iterator<NatFlowProxyClient> exportIterator = exportedService.getNatFlowProxyClients().iterator();
		while(exportIterator.hasNext()) {
			NatFlowProxyClient natFlowProxyClient = exportIterator.next();
			natFlowProxyClient.setId(0);
			natFlowProxyClient.setService(dbService);
		}
		
		Iterator<NatFlowProxyClient> dbNatFlowProxyClients = dbService.getNatFlowProxyClients().iterator();
		boolean isUpdateMode = false;
		while (dbNatFlowProxyClients.hasNext()) {
			NatFlowProxyClient natFlowProxyClient = dbNatFlowProxyClients.next();
			if(natFlowProxyClient.getStatus().equals(StateEnum.ACTIVE)) {
					isUpdateMode = true;
					break;
			}
		}
		if(!isUpdateMode) {
			dbService.setNatFlowProxyClients(exportedService.getNatFlowProxyClients());
			return;
		}
		
		Map<Map<String,Integer>,NatFlowProxyClient> proxyClientMap = new HashMap<>();
		List<NatFlowProxyClient> natFlowProxyClients = dbService.getNatFlowProxyClients();
		natFlowProxyClients.addAll(exportedService.getNatFlowProxyClients());
		Iterator<NatFlowProxyClient> natflowproxyIterator = natFlowProxyClients.iterator();
		while(natflowproxyIterator.hasNext()) {
			NatFlowProxyClient natFlowProxyClient = natflowproxyIterator.next();
			if(!natFlowProxyClient.getStatus().equals(StateEnum.ACTIVE)) {
				natflowproxyIterator.remove();
				continue;
			}
			Map<String,Integer> proxyMap = new HashMap<>();
			proxyMap.put(natFlowProxyClient.getProxyIp(), natFlowProxyClient.getProxyPort());
			if(proxyClientMap.containsKey(proxyMap)) {
				natflowproxyIterator.remove();
			}else {
					proxyClientMap.put(proxyMap, natFlowProxyClient);
			}
		}
		dbService.setNatFlowProxyClients(natFlowProxyClients);
	}
	
	@Override
	public void importFileGroupingParameterAddAndKeepBothMode(FileGroupingParameter exportedFileGroupParams) {
		exportedFileGroupParams.setId(0);
		exportedFileGroupParams.setCreatedDate(EliteUtils.getDateForImport(false));
		exportedFileGroupParams.setLastUpdatedDate(EliteUtils.getDateForImport(false));
	}
	
	@Override
	public void importFileGroupingParameterUpdateMode(FileGroupingParameter dbParam, FileGroupingParameter exportedParam) {
		if(dbParam instanceof FileGroupingParameterParsing 
				&& exportedParam instanceof FileGroupingParameterParsing) {
			iterateFileGroupingParameterParsing((FileGroupingParameterParsing) dbParam, (FileGroupingParameterParsing) exportedParam);
		} else if(dbParam instanceof FileGroupingParamConsolidation 
				&& exportedParam instanceof FileGroupingParamConsolidation) {
			iterateFileGroupingParamConsolidation((FileGroupingParamConsolidation) dbParam, (FileGroupingParamConsolidation) exportedParam);
		} else if(dbParam instanceof FileGroupingParameterCollection 
				&& exportedParam instanceof FileGroupingParameterCollection) {
			iterateFileGroupingParameterCollection((FileGroupingParameterCollection) dbParam, (FileGroupingParameterCollection) exportedParam);
		} else if(dbParam instanceof FileGroupingParameterProcessing 
				&& exportedParam instanceof FileGroupingParameterProcessing) {
			iterateFileGroupingParameterProcessing((FileGroupingParameterProcessing) dbParam, (FileGroupingParameterProcessing) exportedParam);
		} else if(dbParam instanceof FileGroupingParameterAggregation 
				&& exportedParam instanceof FileGroupingParameterAggregation) {
			iterateFileGroupingParameterAggregation((FileGroupingParameterAggregation) dbParam, (FileGroupingParameterAggregation) exportedParam);
		} else {
			iterateFileGroupingParam(dbParam, exportedParam);
		}
	}
	
	@Override
	public void importFileGroupingParameterAddMode(FileGroupingParameter fileGroupParam, int staffId) {
		fileGroupParam.setId(0);
		fileGroupParam.setCreatedByStaffId(staffId);
		fileGroupParam.setCreatedDate(EliteUtils.getDateForImport(false));
		fileGroupParam.setLastUpdatedByStaffId(staffId);
		fileGroupParam.setLastUpdatedDate(EliteUtils.getDateForImport(false));
	}
	
	public void iterateFileGroupingParameterParsing(FileGroupingParameterParsing dbParam, FileGroupingParameterParsing exportedParam) {
		dbParam.setEnableForArchive(exportedParam.isEnableForArchive());
		dbParam.setArchivePath(exportedParam.getArchivePath());
		
		iterateFileGroupingParam(dbParam, exportedParam);
	}
	
	public void iterateFileGroupingParamConsolidation(FileGroupingParamConsolidation dbParam, FileGroupingParamConsolidation exportedParam) {
		dbParam.setEnableForArchive(exportedParam.isEnableForArchive());
		dbParam.setArchivePath(exportedParam.getArchivePath());
		
		iterateFileGroupingParam(dbParam, exportedParam);
	}
	
	public void iterateFileGroupingParameterCollection(FileGroupingParameterCollection dbParam, FileGroupingParameterCollection exportedParam) {
		dbParam.setDuplicateDirPath(exportedParam.getDuplicateDirPath());
		
		iterateFileGroupingParam(dbParam, exportedParam);
	}
	
	public void iterateFileGroupingParameterProcessing(FileGroupingParameterProcessing dbParam, FileGroupingParameterProcessing exportedParam) {
		dbParam.setDuplicateDirPath(exportedParam.getDuplicateDirPath());
		dbParam.setEnableForFilter(exportedParam.isEnableForFilter());
		dbParam.setFilterDirPath(exportedParam.getFilterDirPath());
		dbParam.setEnableForInvalid(exportedParam.isEnableForInvalid());
		dbParam.setInvalidDirPath(exportedParam.getInvalidDirPath());
		dbParam.setEnableForArchive(exportedParam.isEnableForArchive());
		dbParam.setArchivePath(exportedParam.getArchivePath());
		dbParam.setFilterGroupType(exportedParam.getFilterGroupType());
		
		iterateFileGroupingParam(dbParam, exportedParam);
	}
	
	public void iterateFileGroupingParameterAggregation(FileGroupingParameterAggregation dbParam, FileGroupingParameterAggregation exportedParam) {
		dbParam.setArchivePath(exportedParam.getArchivePath());
		iterateFileGroupingParam(dbParam, exportedParam);
	}
	
	public void iterateFileGroupingParam(FileGroupingParameter dbParam, FileGroupingParameter exportedParam) {
		dbParam.setFileGroupEnable(exportedParam.isFileGroupEnable());
		dbParam.setSourcewiseArchive(exportedParam.isSourcewiseArchive());
		dbParam.setGroupingType(exportedParam.getGroupingType());
		dbParam.setEnableForDuplicate(exportedParam.isEnableForDuplicate());
		dbParam.setGroupingDateType(exportedParam.getGroupingDateType());
	}

	/**
	 * Method will create new file group params.
	 * 
	 * @param fileGroupParams
	 * @param isImport
	 * @return
	 */
	@Override
	public ResponseObject iterateFileGroupParams(FileGroupingParameter fileGroupParams, boolean isImport) {
		ResponseObject responseObject = new ResponseObject();

		if (fileGroupParams != null) {
			Date date = new Date();
			if (isImport) {
				logger.debug("Going to create file group paramters.");

				fileGroupParams.setId(0);
				fileGroupParams.setCreatedDate(date);
				fileGroupParams.setCreatedByStaffId(fileGroupParams.getLastUpdatedByStaffId());
				fileGroupParams.setLastUpdatedByStaffId(fileGroupParams.getLastUpdatedByStaffId());
				fileGroupParams.setLastUpdatedDate(date);

				responseObject.setObject(fileGroupParams);

			} else {

				logger.debug("Delete  file group paramters.");
				fileGroupParams.setStatus(StateEnum.DELETED);
				fileGroupParams.setLastUpdatedByStaffId(fileGroupParams.getLastUpdatedByStaffId());
				fileGroupParams.setLastUpdatedDate(date);

			}
		} else {
			if (isImport) {
				responseObject.setObject(new FileGroupingParameterParsing());
			}
		}
		responseObject.setSuccess(true);
		return responseObject;
	}

	/**
	 * Import or delete Iplog service dependents (FileGroupingParam,
	 * PartitionParamList)
	 * 
	 * @param newService
	 * @param isImport
	 * @return
	 */
	@Transactional
	public ResponseObject iterateIplogParsingServiceDependents(IPLogParsingService exportedService, boolean isImport, boolean isServiceLevel) {

		ResponseObject responseObject = iterateFileGroupParams(exportedService.getFileGroupingParameter(), isImport); // Import
																														// or
																														// delete
																														// file
																														// grouping
																														// parameters.
		exportedService.setFileGroupingParameter(exportedService.getFileGroupingParameter());
		List<PartitionParam> paramList = exportedService.getPartionParamList();
		if (!isServiceLevel && paramList != null && !paramList.isEmpty()) {
			logger.debug("create or import partition parameters.");
			for (PartitionParam param : paramList) {
				if (isImport) {
					param.setId(0);
					param.setParsingService((IPLogParsingService) exportedService);
					param.setCreatedByStaffId(exportedService.getLastUpdatedByStaffId());
					param.setCreatedDate(new Date());
					param.setLastUpdatedByStaffId(exportedService.getLastUpdatedByStaffId());
					param.setLastUpdatedDate(new Date());
				} else {
					param.setStatus(StateEnum.DELETED);
					param.setLastUpdatedByStaffId(exportedService.getLastUpdatedByStaffId());
					param.setLastUpdatedDate(new Date());
					param.setParsingService((IPLogParsingService) exportedService);
				}
			}
		}
		exportedService.setPartionParamList(paramList);

		return responseObject;
	}

	/**
	 * Provide the total service count based on input.
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getTotalServiceCount(int serverInstanceId) {

		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditions = new ArrayList<>();

		aliases.put("serverInstance", "s");
		conditions.add(Restrictions.eq("s.id", serverInstanceId));
		conditions.add(Restrictions.ne("status", StateEnum.DELETED));
		returnMap.put(BaseConstants.ALIASES, aliases);
		returnMap.put(BaseConstants.CONDITIONS, conditions);

		Map<String, Object> conditionsAndAliases = returnMap;
		return servicesDao.getQueryCount(Service.class, (List<Criterion>) conditionsAndAliases.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) conditionsAndAliases.get(BaseConstants.ALIASES));

	}

	/**
	 * Get paginated list
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Service> getPaginatedList(int serverInstanceId, int startIndex, int limit, String sidx, String sord) {

		Map<String, Object> conditionsAndAliases = servicesDao.createCriteriaConditions(serverInstanceId);

		return servicesDao.getPaginatedList(Service.class, (List<Criterion>) conditionsAndAliases.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) conditionsAndAliases.get(BaseConstants.ALIASES), startIndex, limit, sidx, sord);

	}

	/**
	 * Delete service and its dependents
	 * 
	 * @param serviceList
	 * @param serverInstance
	 * @param staffId
	 * @return
	 */
	@Override
	public ResponseObject deleteServicesandDepedants(List<Service> serviceList, ServerInstance serverInstance, boolean isCopy, String jaxbXmlPath)
			throws SMException {

		ResponseObject responseObjectService = new ResponseObject();
		boolean isServiceRunning = false;

		if (isCopy) {
			isServiceRunning = isAnyServiceRunning(serverInstance, "0","");//NOSONAR
		}

		if (!isServiceRunning) {
			logger.info("No service running for current server instance , going to delete service and depedants");

			Service service;
			for (int i = 0, size = serviceList.size(); i < size; i++) {

				service = serviceList.get(i);
				if (!StateEnum.DELETED.equals(service.getStatus())) {
					Map<String, Object> serviceMap;
					serviceMap = getServiceJAXBByTypeAndId(service.getId(), service.getSvctype().getServiceFullClassName(), jaxbXmlPath, null);//NOSONAR
					Service serviceDetail = (Service) serviceMap.get(BaseConstants.SERVICE_JAXB_OBJECT);
					serviceDetail.setServerInstance(serverInstance);
					responseObjectService = iterateOverServiceAndDepedantsForImport(serverInstance, serviceDetail, false, true, null, null, false, false);//NOSONAR

					if (!responseObjectService.isSuccess()) {
						break;
					}
				} else {
					logger.info("No need to delete depedants for already deleted service");
					responseObjectService.setSuccess(true);
				}
			}

		} else {
			logger.info("Service is runing for current server instance");
			responseObjectService.setSuccess(false);
			responseObjectService.setResponseCode(ResponseCode.SERVICE_RUNNING);
		}
		return responseObjectService;
	}

	/**
	 * Check if any service is running for current server instance And also
	 * check status of one service using service Id , using JMX call
	 * 
	 * @param serverInstance
	 * @param serviceId
	 * @return serviceStatus
	 */
	@Transactional(readOnly = true)
	public boolean isAnyServiceRunning(ServerInstance serverInstance, String serviceId, String serviceAlias) {

		RemoteJMXHelper jmxCall = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(),
				serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());

		boolean runningStatus = false;
		CrestelNetServerDetails crestelNetServerDetails = jmxCall.readServerDetails();

		if (crestelNetServerDetails != null) {

			List<LiveServiceSummary> serviceSummaryList = crestelNetServerDetails.getServiceSummaryList();
			// If any service is configured , then only list is populated
			if (serviceSummaryList != null && !serviceSummaryList.isEmpty()) {
				logger.info("LiveServiceSummary list found ");

				for (LiveServiceSummary liveServiceSummary : serviceSummaryList) {
					// If want to check status of one service then , serviceId
					// is not null
					if (serviceId != "0") {
						liveServiceSummary.getServiceType();
						if (liveServiceSummary.getInstanceId().contains(String.valueOf(serviceId)) && 
								liveServiceSummary.getInstanceId().startsWith(serviceAlias)) {
							liveServiceSummary.getInstanceName();
							if (liveServiceSummary.getStatus().equals(BaseConstants.RUNNING_SERVICE_STATUS_ENGINE)) {
								logger.info("Service is Running");
								runningStatus = true;
							} else {
								runningStatus = false;
							}
							break;
						}
					} else {
						// If want to check , any of the service form
						// serverInstance is active or not
						if (liveServiceSummary.getStatus().equals(BaseConstants.RUNNING_SERVICE_STATUS_ENGINE)) {
							logger.info("Service is Running");
							runningStatus = true;
							break;
						} else {
							runningStatus = false;
						}
					}
				} // end of for loop
			} else {
				logger.info("LiveServiceSummary list not found");
			}
		} else {
			logger.info("ServerInstance is not running: " + serverInstance.getPort());
			runningStatus = false;
		}
		return runningStatus;
	}

	/**
	 * Fetch service type list
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ServiceType> getServiceTypeList() {

		List<ServiceType> liServiceType;
		liServiceType = servicetypeDao.getEnableServiceTypeList();
		return liServiceType;
	}
	/**
	 * Fetch service type
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Service> getServiceListByAlias(String serviceAlias) {
		ServiceType serviceType = servicetypeDao.getServiceIDbyAlias(serviceAlias);
		return servicesDao.getServerListByType(serviceType.getId());		
	}

	/**
	 * Prepare service data as required to engine for sync
	 */
	@Transactional
	@Override
	public CrestelNetServiceData getServicesSampleDataForSync(int serviceId, String serviceClassName, String jaxbXmlPath, String xsltFilePath,
			String engineSampleXmlPath) throws SMException {

		Map<String, Object> serviceJAXBMap = getServiceJAXBByTypeAndId(serviceId, serviceClassName, jaxbXmlPath, null);
		CrestelNetServiceData serviceData = null;

		if (serviceJAXBMap != null) {

			Service serviceDetail = (Service) serviceJAXBMap.get(BaseConstants.SERVICE_JAXB_OBJECT);
			File inputServiceXMl = (File) serviceJAXBMap.get(BaseConstants.SERVICE_JAXB_FILE);
			String serviceType = serviceDetail.getSvctype().getAlias();
			
			List<CrestelNetDataDefinitionData> dataDefinitionDataList = new ArrayList<CrestelNetDataDefinitionData>();
			
			List<CrestelNetConfigurationData> configurationList = getServiceConfigurationList(serviceDetail, inputServiceXMl, serviceType,
					xsltFilePath, dataDefinitionDataList);

			List<CrestelNetDriverData> driverList = getServiceDriverList(serviceDetail, inputServiceXMl, xsltFilePath);

			List<CrestelNetPluginData> pluginList = getServicePluginsList();

			serviceData = getService(serviceDetail.getSvctype().getAlias() + "-" + serviceDetail.getServInstanceId(), serviceDetail.getSvctype().getAlias(),
					serviceDetail.getServInstanceId(), serviceDetail.getName(), serviceDetail.getDescription(), configurationList, driverList, pluginList, dataDefinitionDataList);

		} else {
			logger.info("Some problem in fetch JAXB xml for Service");
		}
		return serviceData;

	}

	/**
	 * Get Configuration list for service
	 * 
	 * @param serviceDetail
	 * @param inputServiceXMl
	 * @param serviceType
	 * @return List<CrestelNetConfigurationData>
	 * @throws TransformerException
	 * @throws SMException
	 */

	private List<CrestelNetConfigurationData> getServiceConfigurationList(Service serviceDetail, File inputServiceXMl, String serviceType,
			String xsltFilePath, List<CrestelNetDataDefinitionData> dataDefinitionDataList) throws SMException {

		List<CrestelNetConfigurationData> configurationList = new ArrayList<>();

		if (inputServiceXMl != null) {

			if (serviceDetail != null) {

				File xsltFile = xsltProcessor.getXSLTFromFileSystem(serviceType, xsltFilePath);

				String fileContent = xsltProcessor.doTransformation(xsltFile, inputServiceXMl, 0);

				configurationList.add(getConfiguration(serviceDetail.getSvctype().getAlias(), fileContent));

				// if service is of type iplog parsing service than parser list
				// need to be include in configuration list
				if (EngineConstants.IPLOG_PARSING_SERVICE.equals(serviceType) || EngineConstants.PARSING_SERVICE.equals(serviceType)) {

					List<PathList> pathList = serviceDetail.getSvcPathList();

					if (pathList != null && !pathList.isEmpty()) {
						for (PathList path : pathList) {
							if (!(StateEnum.DELETED.equals(path.getStatus()))) {
								if (path instanceof ParsingPathList) {
									ParsingPathList parsingPathList = (ParsingPathList) path;

									List<Parser> wrapperList = parsingPathList.getParserWrappers();

									for (Parser wrapper : wrapperList) {

										if (wrapper.getParserMapping() != null) {
											String pluginType = wrapper.getParserType().getAlias();
											
											if(EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType)){
												int mappingId = wrapper.getParserMapping().getId();
												VarLengthAsciiDataDefinitionFile vLAsciiDataDefinitionFile = varLengthAsciiDataDefinitionFileDao.getDataDefinitionByMappingId(mappingId); 
												if(vLAsciiDataDefinitionFile != null) {
													Blob blob = vLAsciiDataDefinitionFile.getDataDefinitionFile();
													byte[] dataDefinitionFileContent = EliteUtils.convertBlobToByteArray(blob);
													String dataDefinitionFileName = vLAsciiDataDefinitionFile.getDataDefinitionFileName();
													CrestelNetDataDefinitionData dataDefinitionData = new CrestelNetDataDefinitionData();
													dataDefinitionData.setNetDataDefinitionFileName(dataDefinitionFileName);
													dataDefinitionData.setNetDataDefinitionFile(dataDefinitionFileContent);
													dataDefinitionDataList.add(dataDefinitionData);
												}
											}
											
											
											logger.info("Sync for Plugin: " + pluginType);

											File pluginXsltFile = xsltProcessor.getXSLTFromFileSystem(pluginType, xsltFilePath);

											String pluginFileContent = xsltProcessor.doTransformation(pluginXsltFile, inputServiceXMl, 0);

											configurationList.add(getConfiguration(pluginType, pluginFileContent));
										}
									}
								}
							}
						}

					} else {
						logger.debug("Plugin not configured");
					}
				}
			} else {
				logger.debug(" Service detail unavailable");
			}
		} else {
			logger.info("JAXB XML is null");
		}
		return configurationList;
	}

	/**
	 * Get Driver list for service
	 * 
	 * @param serviceDetail
	 * @param inputServiceXMl
	 * @param serviceType
	 * @return List<CrestelNetDriverData>
	 * @throws TransformerException
	 * @throws SMException
	 */

	private List<CrestelNetDriverData> getServiceDriverList(Service serviceDetail, File inputServiceXMl, String xsltFilePath) throws SMException {

		List<CrestelNetDriverData> driversList = new ArrayList<>();

		if (serviceDetail != null) {

			List<Drivers> driverList = serviceDetail.getMyDrivers();

			if (driverList != null && !driverList.isEmpty()) {

				for (Drivers driver : driverList) {
					if (!(StateEnum.DELETED.equals(driver.getStatus()))) {

						List<CrestelNetConfigurationData> driverconfigurationList = new ArrayList<>();
						List<CrestelNetPluginData> pluginList = new ArrayList<>();

						String driverType = driver.getDriverType().getAlias();

						logger.info("Sync for Driver: " + driverType);

						File xsltFile = xsltProcessor.getXSLTFromFileSystem(driverType, xsltFilePath);

						String fileContent = xsltProcessor.doTransformation(xsltFile, inputServiceXMl, driver.getId());

						driverconfigurationList.add(getConfiguration(driverType, fileContent));

						if (EngineConstants.DISTRIBUTION_SERVICE.equals(serviceDetail.getSvctype().getAlias())) { // execute
																													// only
																													// for
																													// distribution
																													// service
							getDistributionPluginList(driverconfigurationList, driver, inputServiceXMl, xsltFilePath);
						}

						driversList.add(getDriverData(serviceDetail.getSvctype().getAlias() + "-" + serviceDetail.getId() + "-" + driverType,
								driverType, driver.getName(), driver.getApplicationOrder(), driverType, null, driverconfigurationList, pluginList));

					}
				}
			} else {
				logger.info("Driver not configured");
			}
		} else {
			logger.info("Some problem in fetch service detail");
		}
		return driversList;
	}

	/**
	 * Get Plugins list for service
	 * 
	 * @param serviceDetail
	 * @param inputServiceXMl
	 * @param serviceType
	 * @return List<CrestelNetPluginData>
	 */

	private List<CrestelNetPluginData> getServicePluginsList() {

		return new ArrayList<>();
	}

	/**
	 * Method will iterate distribution dependents and set it to
	 * CRESTELNETDATACONFIGURATION.
	 * 
	 * @param driverconfigurationList
	 * @param driver
	 * @param inputServiceXMl
	 * @param xsltFilePath
	 * @throws SMException
	 */
	private void getDistributionPluginList(List<CrestelNetConfigurationData> driverconfigurationList, Drivers driver, File inputServiceXMl,
			String xsltFilePath) throws SMException {
		logger.debug("Iterating distribution driver dependents .");
		List<PathList> distribuiontDriverPathList = driver.getDriverPathList();
		if (distribuiontDriverPathList != null && !distribuiontDriverPathList.isEmpty()) {
			for (PathList pathlist : distribuiontDriverPathList) {
				DistributionDriverPathList distributionPathlist = (DistributionDriverPathList) pathlist;
				List<Composer> pluginList = distributionPathlist.getComposerWrappers();
				if (pluginList != null && !pluginList.isEmpty()) {
					for (Composer composer : pluginList) {
						logger.info("Sync for for Distribution composer plugins: " + composer.getName());

						if (!StateEnum.DELETED.equals(composer.getStatus()) && !composer.getComposerType().getAlias().equalsIgnoreCase(EngineConstants.DEFAULT_COMPOSER_PLUGIN)) {
							List<CharRenameOperation> charRenameParams = composer.getCharRenameOperationList();
							if (charRenameParams != null && !charRenameParams.isEmpty()) {
								for (CharRenameOperation charRenameOperation : charRenameParams) {
									charRenameOperation.getId(); // to load each
																	// char
																	// rename
																	// parameter
																	// for
																	// current
																	// composer
																	// plug-in.
								}
							}

							String pluginType = composer.getComposerType().getAlias();
							File xsltFile = xsltProcessor.getXSLTFromFileSystem(pluginType, xsltFilePath);
							String fileContent = xsltProcessor.doTransformation(xsltFile, inputServiceXMl, 0);
							driverconfigurationList.add(getConfiguration(pluginType, fileContent));
						}

					}
				} else {
					logger.debug("No Composer plugin found for service synchronization.");
				}
			}
		} else {
			logger.debug("No distribution pathlist configuration found for service synchronization.");
		}
	}

	/**
	 * Prepare Service data for JMX Call
	 * 
	 * @param serviceId
	 * @param serviceName
	 * @param instanceId
	 * @param serviceInstanceName
	 * @param description
	 * @param configurationList
	 * @param driverList
	 * @param pluginList
	 * @return
	 */

	private CrestelNetServiceData getService(String serviceId, String serviceName, String instanceId, String serviceInstanceName, String description,
			List<CrestelNetConfigurationData> configurationList, List<CrestelNetDriverData> driverList, List<CrestelNetPluginData> pluginList, List<CrestelNetDataDefinitionData> dataDefinitionDataList) {
		CrestelNetServiceData service = new CrestelNetServiceData();

		service.setNetServiceId(serviceId);
		service.setNetServiceName(serviceName);
		service.setNetInstanceId(instanceId);
		service.setNetServiceInstanceName(serviceInstanceName);
		service.setDescription(description);
		service.setNetConfigurationList(configurationList);
		service.setNetDriverList(driverList);
		service.setNetPluginList(pluginList);
		service.setNetDataDefinitionList(dataDefinitionDataList);

		return service;
	}

	/**
	 * Prepare Driver data for sync
	 * 
	 * @param id
	 * @param name
	 * @param description
	 * @param instanceId
	 * @param instanceName
	 * @param instanceMode
	 * @param configurationList
	 * @param pluginList
	 * @return
	 */
	@Transactional
	public CrestelNetDriverData getDriverData(String id, String name, String description, int instanceId, String instanceName, String instanceMode,
			List<CrestelNetConfigurationData> configurationList, List<CrestelNetPluginData> pluginList) {

		CrestelNetDriverData driver = new CrestelNetDriverData();
		driver.setNetDriverId(id);
		driver.setNetDriverName(name);
		driver.setDescription(description);
		driver.setNetDriverInstanceId(instanceId);
		driver.setNetDriverInstanceName(instanceName);
		driver.setNetDriverInstanceMode(instanceMode);
		driver.setNetConfigurationList(configurationList);
		driver.setNetPluginList(pluginList);
		return driver;
	}

	/**
	 * Prepare Plugin data for sync
	 * 
	 * @param id
	 * @param name
	 * @param description
	 * @param instanceId
	 * @param instanceName
	 * @param instanceMode
	 * @param configurationList
	 * @param pluginList
	 * @return
	 */
	@Transactional
	public CrestelNetPluginData getPluginData(String id, String name, String description, int instanceId, String instanceName, String instanceMode,
			List<CrestelNetConfigurationData> configurationList) {

		CrestelNetPluginData plugin = new CrestelNetPluginData();
		plugin.setNetPluginId(id);
		plugin.setNetPluginName(name);
		plugin.setDescription(description);
		plugin.setNetPluginInstanceId(instanceId);
		plugin.setNetPluginInstanceName(instanceName);
		plugin.setNetPluginInstanceMode(instanceMode);
		plugin.setNetConfigurationList(configurationList);
		return plugin;
	}

	/**
	 * Return configuration data as required to engine with key and file content
	 * is in bytes
	 * 
	 * @param key
	 * @param xmlData
	 * @return
	 */

	private CrestelNetConfigurationData getConfiguration(String key, String xmlData) {
		CrestelNetConfigurationData configuration = new CrestelNetConfigurationData();

		configuration.setNetConfigurationKey(key);
		configuration.setNetConfigurationData(xmlData.getBytes());
		return configuration;
	}

	/**
	 * Load service running status using JMX call
	 */
	@Transactional
	@Override
	public ResponseObject loadServiceStatus(int serviceId) {
		ResponseObject responseObject = new ResponseObject();
		Service service = servicesDao.getServiceWithServerInstanceById(serviceId);

		if (service != null) {
			ServerInstance serverInstance = service.getServerInstance();
			if (serverInstance != null) {
				boolean runningStatus = isAnyServiceRunning(serverInstance, service.getServInstanceId(),service.getSvctype().getAlias());
				logger.debug("Running status for service :  " + serviceId + " is : " + runningStatus);
				if (runningStatus) {
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.ACTIVE_SERVICE);
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.INACTIVE_SERVICE);
				}
				responseObject.setObject(service);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.INACTIVE_SERVICE);
		}
		return responseObject;
	}

	/**
	 * Fetch All Service from Database
	 */
	@Transactional(readOnly = true)
	@Override
	public List<Service> getServicesforServerInstance(int serverInstanceId) {

		return servicesDao.getServicesforServerInstance(serverInstanceId);

	}

	/**
	 * Fetch Service object dependents by service id
	 */
	@Transactional(readOnly = true)
	@Override
	public Service getAllServiceDepedantsByServiceId(int serviceId) {

		return servicesDao.getAllServiceDepedantsByServiceId(serviceId);

	}

	/**
	 * Update Collection service configuration
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Service.class, ignorePropList= "svcPathList,myDrivers,servInstanceId,enableFileStats,enableDBStats,serverInstance,svctype")
	public ResponseObject updateCollectionServiceConfiguration(CollectionService service) {

		ServiceSchedulingParams schedulingParam = service.getServiceSchedulingParams();

		schedulingParam.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
		schedulingParam.setLastUpdatedDate(new Date());
		service.setServiceSchedulingParams(schedulingParam);
		serviceSchedulingParamsDao.merge(schedulingParam);

		return updateService(service);
	}

	/**
	 * Update Collection service configuration
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Service.class, ignorePropList= "")
	public ResponseObject updateNetflowCollectionServiceConfiguration(NetflowCollectionService service) {

		service.setServiceSchedulingParams(null);
		return updateService(service);

	}

	/**
	 * Update Collection service configuration
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Service.class, ignorePropList= "")
	public ResponseObject updateNetflowBinaryCollectionServiceConfiguration(NetflowBinaryCollectionService service) {

		service.setServiceSchedulingParams(null);
		return updateService(service);
	}

	/**
	 * Update Syslog Collection service configuration
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Service.class, ignorePropList= "")
	public ResponseObject updateSyslogCollectionServiceConfiguration(SysLogCollectionService service) {

		service.setServiceSchedulingParams(null);
		return updateService(service);
	}
	
	/**
	 * Update Mqtt Collection service configuration
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Service.class, ignorePropList= "")
	public ResponseObject updateMqttCollectionServiceConfiguration(MqttCollectionService service) {

		service.setServiceSchedulingParams(null);
		return updateService(service);
	}
	
	/**
	 * Update CoAP Collection service configuration
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Service.class, ignorePropList= "")
	public ResponseObject updateCoAPCollectionServiceConfiguration(CoAPCollectionService service) {

		service.setServiceSchedulingParams(null);
		return updateService(service);
	}
	
	/**
	 * Update Http2 Collection service configuration
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Service.class, ignorePropList= "")
	public ResponseObject updateHttp2CollectionServiceConfiguration(Http2CollectionService service) {

		service.setServiceSchedulingParams(null);
		return updateService(service);
	}

	/**
	 * Update Radius Collection service configuration
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Service.class, ignorePropList= "")
	public ResponseObject updateRadiusCollectionServiceConfiguration(RadiusCollectionService service) {

		service.setServiceSchedulingParams(null);
		return updateService(service);
	}
	
	/**
	 * Update GTPPrime Collection service configuration
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Service.class, ignorePropList= "")
	public ResponseObject updateGtpPrimeCollectionServiceConfiguration(GTPPrimeCollectionService service) {

		service.setServiceSchedulingParams(null);
		return updateService(service);
	}

	/**
	 * Update IPLog parsing service configuration
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = IPLogParsingService.class, ignorePropList= "partionParamList,fileGroupingParameter")
	public ResponseObject updateIplogParsingServiceConfiguration(IPLogParsingService service) {
		FileGroupingParameterParsing fileGroupParam = service.getFileGroupingParameter();
		fileGroupParam.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
		fileGroupParam.setLastUpdatedDate(new Date());
		return updateService(service);

	}

	/**
	 * Update IPLog parsing service configuration
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParsingService.class, ignorePropList= "")
	public ResponseObject updateParsingServiceConfiguration(ParsingService service) {

		FileGroupingParameter fileGroupParam = service.getFileGroupingParameter();
		fileGroupParam.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
		fileGroupParam.setLastUpdatedDate(new Date());

		return updateService(service);

	}

	
	/**
	 * Update Processing service configuration
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Service.class, ignorePropList= "myDrivers")
	public ResponseObject updateProcessingServiceConfiguration(ProcessingService service) {

		FileGroupingParameter fileGroupParam = service.getFileGroupingParameter();
		fileGroupParam.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
		fileGroupParam.setLastUpdatedDate(new Date());

		return updateService(service);

	}

	/**
	 * Update Aggregation service configuration
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Service.class, ignorePropList= "myDrivers,aggregationDefinition,svcPathList")
	public ResponseObject updateAggregationServiceConfiguration(AggregationService service,String serviceId,int lastUpdatedByStaffId) {
		int iserviceId=Integer.parseInt(serviceId);
		AggregationService oldService = (AggregationService) servicesDao.getAllServiceDepedantsByServiceId(iserviceId);
		service.setAggregationDefinition(oldService.getAggregationDefinition());
		
		FileGroupingParameterAggregation fileGroupParam = service.getFileGroupingParameter();
		fileGroupParam.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
		fileGroupParam.setLastUpdatedDate(new Date());
		fileGroupParamDao.merge(fileGroupParam);
		
		ServiceSchedulingParams schedulingParam = service.getServiceSchedulingParams();
		schedulingParam.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
		schedulingParam.setLastUpdatedDate(new Date());
		service.setServiceSchedulingParams(schedulingParam);
		serviceSchedulingParamsDao.merge(schedulingParam);
		
		return updateService(service);

	}
	
	/**
	 * Method will fetch total service instances count.
	 * 
	 * @see com.elitecore.sm.services.service.ServicesService#getTotalServiceInstancesCount()
	 * @return long (total count of services)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getTotalServiceInstancesCount(SearchServices service) {
		Map<String, Object> serviceConditions = servicesDao.getServiceBySearchParameters(service);
		return servicesDao.getQueryCount(Service.class, (List<Criterion>) serviceConditions.get("conditions"),
				(HashMap<String, String>) serviceConditions.get("aliases"));
	}

	/**
	 * @param service
	 * @param startIndex
	 * @param limit
	 * @param sidx
	 * @param sord
	 * @return List of services
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Service> getPaginatedList(SearchServices service, int startIndex, int limit, String sidx, String sord) {
		Map<String, Object> serviceConditions = servicesDao.getServiceBySearchParameters(service);		
		return servicesDao.getServicesPaginatedList(Service.class, (List<Criterion>) serviceConditions.get("conditions"),
				(HashMap<String, String>) serviceConditions.get("aliases"), startIndex, limit, sidx, sord);

	}

	/**
	 * Method will get Service Instance JAXB XML file.
	 * 
	 * @param serviceInstanceId
	 * @param isExportForDelete
	 * @param exportTempPath
	 * @return ResponseObject
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.EXPORT_SERVICE_CONFIGURATION, actionType = BaseConstants.EXPORT_ACTION, currentEntity = Service.class, ignorePropList= "")
	public ResponseObject getServiceInstaceJAXBXML(int serviceInstanceId, boolean isExportForDelete, String jaxbXmlPath) throws SMException {
		Service service = servicesDao.findByPrimaryKey(Service.class, serviceInstanceId);
		ResponseObject responseObject = new ResponseObject();
		String exportXmlPath;
		String fileName;

		if (service != null) {
			DateFormat dateFormatter = new SimpleDateFormat(BaseConstants.DELETE_EXPORT_DATE_TIME_FORMATTER);
			if (isExportForDelete) {
				logger.debug("Call for Export configuration before Delete");

				responseObject = systemParamService.getLocationOfSystemBackUpPath(SystemBackUpPathOptionEnum.DELETE);

				if (responseObject.isSuccess()) {
					exportXmlPath = (String) responseObject.getObject();
					logger.debug("Going to store export backup file at location :" + exportXmlPath);
					fileName = service.getName() + "_" + BaseConstants.SUFFIX_FOR_DELETE + dateFormatter.format(new Date())
							+ BaseConstants.XML_FILE_EXT; // File name with service name for export with delete
															
				} else {
					logger.debug("Export backup path is not valid:");
					return responseObject;
				}

			} else {
				fileName = service.getName() + "_" + service.getSvctype().getAlias() + "_" + dateFormatter.format(new Date())
						+ BaseConstants.XML_FILE_EXT; // File name with service name for only export action.
				exportXmlPath = jaxbXmlPath;
			}

			Map<String, Object> serviceMap = getServiceJAXBByTypeAndId(service.getId(), service.getSvctype().getServiceFullClassName(),
					exportXmlPath, fileName);
			if (serviceMap != null) {
				responseObject.setSuccess(true);
				responseObject.setObject(serviceMap);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.EXPORT_FAIL);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_NOT_FOUND);
		}

		return responseObject;
	}

	/**
	 * Method will import/override service configuration from uploaded file and
	 * will remove all older service configuration.
	 * 
	 * @param serviceInstanceId
	 * @param importFile
	 * @param staffId
	 * @param jaxbXmlPath
	 * @return Response Object
	 * @throws SMException
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.IMPORT_SERVICE_CONFIGURATION, actionType = BaseConstants.SM_ACTION, currentEntity = Service.class, ignorePropList= "")
	public ResponseObject importServiceInstanceConfig(int importedserviceId, File importFile, int staffId, String jaxbXmlPath, int serverId, int importMode) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		Service serviceObject = getServiceandServerinstance(importedserviceId); // fetch service object
																				
		if (serviceObject != null && !isAnyServiceRunning(serviceObject.getServerInstance(), 
				serviceObject.getServInstanceId(),serviceObject.getSvctype().getAlias())) { // check whether service object not null and service is not running
			responseObject = convertXMLToServiceInstance(importFile, serviceObject, jaxbXmlPath); // convert XML file to service Object using UNMARSHLING
			if (responseObject.isSuccess()) {
				logger.debug("UnMarshlling done successfully , Now validate imported file");
				Service exportedService = (Service) responseObject.getObject();
				
				exportedService.setServInstanceId(serviceObject.getServInstanceId());
				logger.debug("UnMarshlling done successfully , Service status" + exportedService.getStatus());
				logger.debug("UnMarshlling done successfully , Service mode" + exportedService.getSvcExecParams().getStartupMode());

				List<ImportValidationErrors> importValidationError = validateServiceForImport(exportedService,new ArrayList<ImportValidationErrors>(),serviceObject.getId());

				if (importValidationError != null && !importValidationError.isEmpty()) {

					logger.debug("Validation Fail for imported file of service");
					JSONArray finaljArray = new JSONArray();

					for (ImportValidationErrors errors : importValidationError) {

						JSONArray jArray = new JSONArray();
						jArray.put(errors.getModuleName());
						jArray.put(errors.getEntityName());
						jArray.put(errors.getPropertyName());
						jArray.put(errors.getPropertyValue());
						jArray.put(errors.getErrorMessage());
						finaljArray.put(jArray);
					}

					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.IMPORT_VALIDATION_FAIL);
					responseObject.setObject(finaljArray);
				} else {
					logger.debug("Validation Done Successfully for imported file , now process import");
					
					if(importMode > 0) {
						responseObject = serviceEntityImportService.importService(importedserviceId, exportedService, staffId, jaxbXmlPath, serverId, importMode);
					} else {
						responseObject = setServiceConfiguration(importedserviceId, exportedService, staffId, jaxbXmlPath, serverId); // Set all service parameters
					}
																														
					if (responseObject.isSuccess()) {
						logger.debug("Import operation performed successfully");
						responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_IMPORT_SUCCESS);
					} else {
						logger.debug("Import operation Fail");
						responseObject.setResponseCode(ResponseCode.IMPORT_FAIL);
					}
				}
			} else {
				logger.debug("UnMarshlling Fail For service");
			}
		} else {
			responseObject.setSuccess(false);

			if (serviceObject == null) {
				responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_NOT_FOUND);
			} else {
				responseObject.setResponseCode(ResponseCode.IMPORT_SERVICE_RUNNING);
			}
		}
		return responseObject;
	}
	

	/**
	 * Method will read uploaded XML file and convert it to Service object using
	 * JAXB Unmarshal.
	 * 
	 * @param xmlFileObj
	 * @return Response Object
	 */

	private ResponseObject convertXMLToServiceInstance(File xmlFileObj, Service serviceDB, String jaxbXmlPath) {

		ResponseObject responseObject = new ResponseObject();

		final JSONArray finaljArray = new JSONArray();

		try {

			ValidationEventCollector serviceValidatorimport = new ValidationEventCollector() {
				@Override
				public boolean handleEvent(ValidationEvent event) {
					logger.debug("XSD Validation error occured");

					JSONArray jArray = new JSONArray();
					jArray.put(event.getLocator().getLineNumber());
					jArray.put(event.getMessage());

					finaljArray.put(jArray);
					return true;
				}
			};

			// Unmarshal Service instances
			String xsdPAth = jaxbXmlPath + File.separator + BaseConstants.IMPORT_VALIDATION_XSD;
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);//NOSONAR
			Schema schema = sf.newSchema(new File(xsdPAth));

			JAXBContext jaxbContext = JAXBContext.newInstance(Service.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			jaxbUnmarshaller.setSchema(schema);
			jaxbUnmarshaller.setEventHandler(serviceValidatorimport);
			Service exportedserviceInstance = (Service) jaxbUnmarshaller.unmarshal(xmlFileObj);

			responseObject = checkisValidFileForService(serviceDB, exportedserviceInstance, finaljArray);

		} catch (JAXBException e) {
			logger.error("Error ooccured  in convertXMLToServiceInstance:" + e);
			if (e.getCause() != null) {
				logger.error("Unable to unamarshall server instance xml file due to  :" + e.getCause().getMessage());
				responseObject.setObject(new String(e.getCause().getMessage()));
			} else {
				logger.error("Unable to unamarshall server instance xml file due to  :" + e.getMessage());
				responseObject.setObject(new String(e.getMessage()));
			}
			responseObject.setResponseCode(ResponseCode.SERVICE_UNMARSHALL_FAIL);
			responseObject.setSuccess(false);
		} catch (SAXException e) {
			logger.error("Error :" + e);
			logger.error("Error :" + e.getCause().getMessage());
		}
		return responseObject;
	}

	/**
	 * Fetch Service and ServerInstance object by service id
	 * 
	 * @param serviceId
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public Service getServiceandServerinstance(int serviceId) {
		return servicesDao.findByPrimaryKey(Service.class, serviceId);
	}

	/**
	 * Method will set all service configuration.
	 * 
	 * @param serviceInstanceId
	 * @param serviceInstance
	 * @param staffId
	 * @param jaxbXMLPath
	 * @return ResponseObject
	 * @throws SMException
	 */
	private ResponseObject setServiceConfiguration(int serviceInstanceId, Service serviceInstance, int staffId, String jaxbXMLPath, int serverId)
			throws SMException {
		ResponseObject responseObject = new ResponseObject();

		Service dbServiceInstance = servicesDao.findByPrimaryKey(Service.class, serviceInstanceId);

		if (dbServiceInstance != null) {

			dbServiceInstance.setSvcExecParams(serviceInstance.getSvcExecParams());
			dbServiceInstance.setEnableFileStats(serviceInstance.isEnableFileStats());
			dbServiceInstance.setEnableDBStats(serviceInstance.isEnableDBStats());
			dbServiceInstance.setLastUpdatedDate(new Date());
			dbServiceInstance.setLastUpdatedByStaffId(staffId);
			// servicesDao.merge(dbServiceInstance); // It will updated service
			// default created field. like id, name , description

			responseObject.setSuccess(true);

				Map<String, Object> serviceMap = getServiceJAXBByTypeAndId(dbServiceInstance.getId(), dbServiceInstance.getSvctype()
						.getServiceFullClassName(), jaxbXMLPath, null);
				Service serviceDetail = (Service) serviceMap.get(BaseConstants.SERVICE_JAXB_OBJECT);
				
				// Delete service configuration first
				responseObject = iterateOverServiceAndDepedantsForImport(serviceDetail.getServerInstance(), serviceDetail, false, false, null, null, false, false);
				logger.debug(" Delete operation successful for service");

				// After successful delete perform import operation
				if (responseObject.isSuccess()) {
					serviceInstance.setServerInstance(serviceDetail.getServerInstance());
					serviceInstance.setId(dbServiceInstance.getId());
					serviceInstance.setName(dbServiceInstance.getName());
					logger.debug("service Name:" + serviceInstance.getName());
					if(serviceInstance instanceof ProcessingService) {
						if(serviceInstance.getSvcPathList() != null && !serviceInstance.getSvcPathList().isEmpty()) {
							for(PathList pathList : serviceInstance.getSvcPathList()) {
								ProcessingPathList processingPathList = (ProcessingPathList) pathList;
								String policyAlias = processingPathList.getPolicyAlias();
								if(org.apache.commons.lang3.StringUtils.isNotEmpty(policyAlias)) {
									processingPathList.setPolicy(policyDao.getPolicyByAlias(policyAlias,serverId));
								}
							}
						}
						
					}
					responseObject = iterateOverServiceAndDepedantsForImport(serviceDetail.getServerInstance(), serviceInstance, true, false, null, null, false, true);
					if (responseObject.isSuccess()) {

						responseObject.setSuccess(true);
						responseObject.setObject(responseObject.getObject());
						responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_IMPORT_SUCCESS);
					}
				} else {
					responseObject.setResponseCode(ResponseCode.IMPORT_FAIL);
					responseObject.setSuccess(false);
				}
		

		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_NOT_FOUND);
			logger.debug("Fail to fetch Service instance from database");
		}

		return responseObject;
	}

	/**
	 * Method will delete service instance and its dependents
	 * 
	 * @param serviceInstancesId
	 * @param staffid
	 * @param jaxbXmlPath
	 * @return Response Object
	 * @throws SMException
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public ResponseObject deleteServiceInstance(int serviceInstanceId, int staffId, String jaxbXmlPath) throws SMException {

		ResponseObject responseObject = new ResponseObject();
		Service serviceObject = getServiceandServerinstance(serviceInstanceId); // it will fetch service object
		if (serviceObject != null && !isAnyServiceRunning(serviceObject.getServerInstance(), 
				serviceObject.getServInstanceId(), serviceObject.getSvctype().getAlias())) { // check whether service object not null and service is not running

			if (!StateEnum.DELETED.equals(serviceObject.getStatus())) {
				ResponseObject exportresponseObject = getServiceInstaceJAXBXML(serviceInstanceId, true, null);
				if (exportresponseObject != null && exportresponseObject.isSuccess()) {

					Map<String, Object> serviceMap = getServiceJAXBByTypeAndId(serviceObject.getId(), serviceObject.getSvctype()
							.getServiceFullClassName(), jaxbXmlPath, null);
					Service serviceDetail = (Service) serviceMap.get(BaseConstants.SERVICE_JAXB_OBJECT);
					serviceObject.setLastUpdatedByStaffId(staffId);
		
					//iterate agent service pkt stat
					agentService.deletePacketStatAgentServicePktStat(serviceDetail.getServerInstance().getAgentList(),serviceDetail.getId());
					responseObject = iterateOverServiceAndDepedantsForImport(serviceDetail.getServerInstance(), serviceDetail, false, false, null, null, false, false);

					if (responseObject.isSuccess()) {

						ServicesService serviceImpl = (ServicesService) SpringApplicationContext.getBean("servicesService"); // getting spring bean for aop context issue.
						responseObject = serviceImpl.deleteServiceInstanceConfig(serviceDetail);
						if (responseObject.isSuccess()) {

							Map<String, Object> serviceInstanceJAXB = (Map<String, Object>) exportresponseObject.getObject();
							File exportServiceXml = (File) serviceInstanceJAXB.get(BaseConstants.SERVICE_JAXB_FILE);
							String kubernetesFlag =  (String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV);
							if(kubernetesFlag != null && !kubernetesFlag.isEmpty() && Boolean.TRUE.toString().equals(kubernetesFlag)){
									
								CrestelNetServiceData serviceData = new CrestelNetServiceData();
								serviceData.setNetServiceId(serviceDetail.getSvctype().getAlias() + "-" + serviceDetail.getServInstanceId());
								Map<String, Object> statusMap = HazelcastUtility.getHazelcastUtility().updateCrestelNetServiceDataInHazelcastCache(serviceObject.getServerInstance().getServer().getIpAddress(),String.valueOf(serviceObject.getServerInstance().getPort()), serviceData, BaseConstants.ACTION_DELETE);
								boolean hazelcastMapUpdateFlag = (boolean)statusMap.get(HazelcastCacheConstants.SUCCESS_STATUS);
								
								if(hazelcastMapUpdateFlag){
									CrestelNetServerData serverData = (CrestelNetServerData)statusMap.get(HazelcastCacheConstants.SUCCESS_OBJECT);
									serverInstanceService.saveOrUpdateAutoConfigSI(serverData, serviceObject.getServerInstance().getLastUpdatedByStaffId(),
											serviceObject.getServerInstance().getServer().getIpAddress(), serviceObject.getServerInstance().getServer().getUtilityPort(),
											serviceObject.getServerInstance().getPort());
									responseObject.setObject(exportServiceXml.getAbsolutePath());
									responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_DELETE_SUCCESS);
								}else {
									responseObject.setSuccess(false);
									responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_DELETE_FAIL);
								}
							}else {
								responseObject.setSuccess(true);
								responseObject.setObject(exportServiceXml.getAbsolutePath());
								responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_DELETE_SUCCESS);
							}
							
						} else {
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_DELETE_FAIL);
						}
					} else {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_DELETE_FAIL);
					}
				} else {
					logger.debug("Fail to export service instance configuration");
					return exportresponseObject;

				}
			}
		} else {
			responseObject.setSuccess(false);
			if (serviceObject == null) {
				responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_NOT_FOUND);
			} else {
				responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_DELETE_FAIL_RUNNING);
			}
		}
		return responseObject;
	}

	/**
	 * Method will change the service details status to deleted it will not
	 * remove from db.
	 * 
	 * @param serviceInstance
	 * @return Response object
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_SERVICE, actionType = BaseConstants.DELETE_CUSTOM_ACTION, currentEntity = Service.class, ignorePropList= "")
	public ResponseObject deleteServiceInstanceConfig(Service serviceInstance) {
		ResponseObject responseObject = new ResponseObject();
		if(serviceInstance instanceof ProcessingService){
			serviceInstance = deletePolicyPathListRelation((ProcessingService)serviceInstance);
		}
		serviceInstance.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,serviceInstance.getName()));
		serviceInstance.setStatus(StateEnum.DELETED);
		serviceInstance.setLastUpdatedDate(new Date());

		servicesDao.merge(serviceInstance);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_DELETE_SUCCESS);
		return responseObject;
	}

	private Service deletePolicyPathListRelation(ProcessingService serviceInstance) {
		List<PathList> pathList = serviceInstance.getSvcPathList();
		for(PathList pathListData: pathList){
			ProcessingPathList processingPathList = (ProcessingPathList)pathListData;
			Policy policy = null;
			if(processingPathList != null){
				policy = processingPathList.getPolicy();
			}
			List<ProcessingPathList> processingPathsList = null;
			if(policy != null){
				processingPathsList = policy.getProcessingPathList();
				if(processingPathsList != null){
					processingPathsList.remove(processingPathList);
					policy.setProcessingPathList(processingPathsList);
					processingPathList.setPolicy(null);
				}
			}
		}
		serviceInstance.setSvcPathList(pathList);
		return serviceInstance;
	}

	/**
	 * Method will synchronize service instances and return each service status
	 * messages with response object.
	 * 
	 * @param serviceInstanceDetailMap
	 * @return Map
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.SYNCHRONIZE_SERVICE, actionType = BaseConstants.SM_ACTION_BULK_MAP, currentEntity = Service.class, ignorePropList= "")
	public Map<String, ResponseObject> syncServiceInstance(Map<String, String> serviceInstanceDetailMap) {
		String serviceInstancesId = null;
		String serviceInstancesStatus = null;
		String jaxbXmlPath = null;
		String xsltFilePath = null;

		if (serviceInstanceDetailMap != null) {
			serviceInstancesId = serviceInstanceDetailMap.get(BaseConstants.SERVICE_INSTANCES_ID);
			serviceInstancesStatus = serviceInstanceDetailMap.get(BaseConstants.SERVICE_INSTANCES_STATUS);
			jaxbXmlPath = serviceInstanceDetailMap.get(BaseConstants.JAXB_XML_PATH_CONSTANT);
			xsltFilePath = serviceInstanceDetailMap.get(BaseConstants.XSLT_PATH_CONSTANT);
		}

		String jmxResponse;
		ResponseObject responseObject;
		Map<String, ResponseObject> responseMap = new HashMap<>();

		if (serviceInstancesId != null && serviceInstancesStatus != null) {
			String[] serviceInstanceIds = serviceInstancesId.split(",");
			String[] serviceInstanceStatusArray = serviceInstancesStatus.split(",");
			Map<Integer, ServerInstance> uniqueServerInstanceMap = new HashMap<>();

			for (int j = 0; j < serviceInstanceIds.length; j++) {
				responseObject = new ResponseObject();
				String serviceInstanceId = serviceInstanceIds[j];
				String serviceInstanceStatus = serviceInstanceStatusArray[j];
				int iserviceInstanceId = Integer.parseInt(serviceInstanceId);
				try {

					logger.info("Sync for service: " + serviceInstanceId + "status is : " + serviceInstanceStatus);

					Service service = getServiceandServerinstance(iserviceInstanceId);

					if (service != null && !service.isSyncStatus()) {
						responseObject = validateParsingServiceLicense(service);
						if(responseObject.isSuccess()){
							int serverInstanceId = service.getServerInstance().getId();
							if (!uniqueServerInstanceMap.containsKey(serverInstanceId)) {
								uniqueServerInstanceMap.put(service.getServerInstance().getId(), service.getServerInstance());
							}

							List<CrestelNetServiceData> serviceConfigList = new ArrayList<>(0);
							ServerInstance serverInstance = service.getServerInstance();
							String kubernetesFlag =  (String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV);
							if(kubernetesFlag != null && !kubernetesFlag.isEmpty() && Boolean.TRUE.toString().equals(kubernetesFlag)){

									logger.debug("Sync for Service Name: " + service.getSvctype().getAlias());
									CrestelNetServiceData serviceData =getServicesSampleDataForSync(service.getId(), service.getSvctype().getServiceFullClassName(),
											jaxbXmlPath, xsltFilePath, null); 
									serviceConfigList.add(serviceData);
									Map<String, Object> statusMap  = HazelcastUtility.getHazelcastUtility().updateCrestelNetServiceDataInHazelcastCache(serverInstance.getServer().getIpAddress(),String.valueOf(serverInstance.getPort()), serviceData, BaseConstants.ACTION_TYPE_ADD);
									boolean hazelcastMapUpdateFlag = (boolean)statusMap.get(HazelcastCacheConstants.SUCCESS_STATUS);
									if (hazelcastMapUpdateFlag) {
										CrestelNetServerData serverData = (CrestelNetServerData)statusMap.get(HazelcastCacheConstants.SUCCESS_OBJECT);
										serverInstanceService.saveOrUpdateAutoConfigSI(serverData, serverInstance.getLastUpdatedByStaffId(),
												serverInstance.getServer().getIpAddress(), serverInstance.getServer().getUtilityPort(),
												serverInstance.getPort());
										responseObject = resetServiceSyncStatus(service);

										if (responseObject.isSuccess()) {

											uniqueServerInstanceMap.put(service.getServerInstance().getId(), service.getServerInstance());
											responseObject = resetSISyncChildStatus(service.getServerInstance());

											responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_SYNCHRONIZATION_SUCCESS);
											responseObject.setSuccess(true);
											responseObject.setObject(service);
										} else {
											responseObject.setSuccess(false);
											responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_SYNCHRONIZATION_FAIL);
										}
										responseMap.put(serviceInstanceId, responseObject);
									} else {
										responseObject.setSuccess(false);
										responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_SYNCHRONIZATION_FAIL);
										responseMap.put(serviceInstanceId, responseObject);
									}
							}else {

								RemoteJMXHelper jmxConnection = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(),
										serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());

								String versionInfo = jmxConnection.versionInformation();

								if (versionInfo != null && jmxConnection.getErrorMessage() == null) {

									logger.debug("Sync for Service Name: " + service.getSvctype().getAlias());
									CrestelNetServiceData serviceData =getServicesSampleDataForSync(service.getId(), service.getSvctype().getServiceFullClassName(),
											jaxbXmlPath, xsltFilePath, null); 
									serviceConfigList.add(serviceData);
									if (!serviceConfigList.isEmpty()) {
										jmxResponse = jmxConnection.syncListOfServicesToEngine(serviceConfigList);
										boolean isSyncSuccess = true;
										if (service instanceof Http2CollectionService ) {
											Http2CollectionService http2Service = (Http2CollectionService) service;
											if(http2Service.getKeystoreFile()!=null) {
												isSyncSuccess = jmxConnection.syncKeystoreFile(http2Service.getKeystoreFileName(), http2Service.getKeystoreFilePath(), http2Service.getKeystoreFile());
											}
											if(!isSyncSuccess) {
												jmxConnection.setErrorMessage(BaseConstants.KEYSTORE_FILE_SYNC_FAILURE);
											}
										}
										
										versionInfo = jmxConnection.versionInformation();

										if (versionInfo != null && jmxConnection.getErrorMessage() == null) {
											if (BaseConstants.SERVICE_INSTANCE_JMX_RESPONSE.equals(jmxResponse)) {
												responseObject = resetServiceSyncStatus(service);

												if (responseObject.isSuccess()) {

													uniqueServerInstanceMap.put(service.getServerInstance().getId(), service.getServerInstance());
													responseObject = resetSISyncChildStatus(service.getServerInstance());

													responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_SYNCHRONIZATION_SUCCESS);
													responseObject.setSuccess(true);
													responseObject.setObject(service);
												} else {
													responseObject.setSuccess(false);
													responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_SYNCHRONIZATION_FAIL);
												}
												responseMap.put(serviceInstanceId, responseObject);
											} else if (jmxConnection.getErrorMessage() != null
													&& jmxConnection.getErrorMessage().equals(BaseConstants.KEYSTORE_FILE_SYNC_FAILURE)) {
												responseObject.setSuccess(false);
												responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_SYNCHRONIZATION_FAIL);
												responseMap.put(serviceInstanceId, responseObject);
											} else if (jmxConnection.getErrorMessage() != null
													&& jmxConnection.getErrorMessage().equals(BaseConstants.JMX_CONNECTION_FAILURE)) {
												responseObject.setSuccess(false);
												responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_SYNC_FAIL_JMX_CONNECTION_FAIL);
												responseMap.put(serviceInstanceId, responseObject);
											} else if (jmxConnection.getErrorMessage() != null
													&& jmxConnection.getErrorMessage().equals(BaseConstants.JMX_API_FAILURE)) {
												responseObject.setSuccess(false);
												responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_SYNC_FAIL_JMX_API_FAIL);
												responseMap.put(serviceInstanceId, responseObject);
											} else {
												responseObject.setSuccess(false);
												responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_SYNCHRONIZATION_FAIL);
												responseMap.put(serviceInstanceId, responseObject);
											}

										} else {
											responseObject.setSuccess(false);
											responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_SYNC_FAIL_JMX_CONNECTION_FAIL);
											responseMap.put(serviceInstanceId, responseObject);
										}
									} else {
										responseObject.setSuccess(false);
										responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_SYNCHRONIZATION_FAIL);
										responseMap.put(serviceInstanceId, responseObject);
									}
								} else if (jmxConnection.getErrorMessage() != null
										&& jmxConnection.getErrorMessage().equals(BaseConstants.JMX_CONNECTION_FAILURE)) {
									responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_JMX_CON_FAIL);
									responseObject.setSuccess(false);
									responseMap.put(serviceInstanceId, responseObject);
								} else {
									responseObject.setSuccess(false);
									responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_SYNCHRONIZATION_FAIL);
									responseMap.put(serviceInstanceId, responseObject);
								}
							}
							
						}else {
							logger.info("Invalid License. Please Service Mapped with Appropriate license" + serviceInstanceId);
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_SYNC_FAIL_DUE_TO_INVALID_LICENSE);
							responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE);
							responseMap.put(serviceInstancesId, responseObject);	
						}
						
					} else {
						
						if (service == null) {
							responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_NOT_FOUND); // Service not found
							responseObject.setSuccess(false);
						} else {
							responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_ALREADY_SYNC); // already sync
							responseObject.setSuccess(true);
						}
						responseMap.put(serviceInstanceId, responseObject);
					}

				} catch (Exception exception) {
					logger.error(exception.getMessage(), exception);
					logger.trace(EliteExceptionUtils.getRootCauseStackTraceAsString(exception));
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_SYNCHRONIZATION_FAIL); 
					responseMap.put(serviceInstanceId, responseObject);
				}
			}// End of For loop
		}
		return responseMap;
	}

	/**
	 * Method will change Service sync status once service synchronization will
	 * be done successfully.
	 * 
	 * @param service
	 * @return Response Object
	 */
	@Transactional
	@Override
	public ResponseObject resetServiceSyncStatus(Service service) {
		ResponseObject responseObject = new ResponseObject();

		if (service != null) {
			service.setSyncStatus(true);
			servicesDao.updateForResetSyncFlagOfService(service);
			responseObject.setSuccess(true);
		} else {
			logger.debug("Service is null");
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * This method is check if all service flag are reset then reset server
	 * instance child flag
	 * 
	 * @param service
	 * @return Response Object
	 */
	@Transactional
	@Override
	public ResponseObject resetSISyncChildStatus(ServerInstance serverInstance) {
		ResponseObject responseObject = new ResponseObject();
		boolean bSyncStatus = false;

		if (serverInstance != null) {
			// Fetch list of service , from serverInstanceId
			List<Service> serviceList = servicesDao.getServicesforServerInstance(serverInstance.getId());
			if (serviceList != null && !serviceList.isEmpty()) {
				for (int i = 0; i < serviceList.size(); i++) {
					Service service = serviceList.get(i);

					if (service.isSyncStatus()) { // Sync flag of service is
													// reset
						bSyncStatus = true;
					} else { // Sync flag of any one service is dirty then no
								// need to check for another , break the loop
						bSyncStatus = false;
						break;
					}
				}
				logger.debug("Update Sync status of Server Instance child flag is " + bSyncStatus);
				serverInstance.setSyncChildStatus(bSyncStatus);
				serverInstanceDao.updateForResetSyncFlagofServerInstance(serverInstance);
				responseObject.setSuccess(true);
			} else {
				logger.debug("Service List is null");
				responseObject.setSuccess(false);
			}
		} else {
			logger.debug("ServerInstance is null");
			responseObject.setSuccess(false);
		}

		return responseObject;
	}

	/**
	 * Method will start service instance with JMX start service call in engine
	 * 
	 * @see com.elitecore.sm.services.service.ServicesService#stopServiceInstance(java.lang.String)
	 * @param serviceInstanceId
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.START_SERVICE, actionType = BaseConstants.SM_ACTION, currentEntity = Service.class, ignorePropList= "")
	public ResponseObject startServiceInstance(int serviceInstanceId) {
		ResponseObject responseObject = new ResponseObject();
		if (serviceInstanceId != 0) {
			return startStopServiceInstance(serviceInstanceId, BaseConstants.START_SERVICE);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_ID_NOT_FOUND);
			return responseObject;
		}
	}

	/**
	 * Method will stop service instance with JMX stop service call in engine
	 * 
	 * @see com.elitecore.sm.services.service.ServicesService#stopServiceInstance(java.lang.String)
	 * @param serviceInstanceId
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.STOP_SERVICE, actionType = BaseConstants.SM_ACTION, currentEntity = Service.class, ignorePropList= "")
	public ResponseObject stopServiceInstance(int serviceInstanceId) {
		ResponseObject responseObject = new ResponseObject();
		if (serviceInstanceId != 0) {
			return startStopServiceInstance(serviceInstanceId, BaseConstants.STOP_SERVICE);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_ID_NOT_FOUND);
			return responseObject;
		}
	}

	/**
	 * Method will call start/stop service instance based on action.
	 * 
	 * @param serviceInstanceId
	 * @param actionType
	 * @return Response object
	 */
	private ResponseObject startStopServiceInstance(int serviceInstanceId, String actionType) {
		ResponseObject responseObject = new ResponseObject();
		Service serviceObj = getServiceandServerinstance(serviceInstanceId);

		if (serviceObj != null) {
			ServerInstance serverInstance = serviceObj.getServerInstance();

			RemoteJMXHelper remoteJMXHelper = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(),
					serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());
			String versionInfo = remoteJMXHelper.versionInformation();

			if (versionInfo != null && remoteJMXHelper.getErrorMessage() == null) {

				if (BaseConstants.START_SERVICE.equals(actionType)) {
					remoteJMXHelper.startExistingService(serviceObj.getSvctype().getAlias() + "-" + serviceObj.getServInstanceId());
				} else if (BaseConstants.STOP_SERVICE.equals(actionType)) {
					remoteJMXHelper.stopService(serviceObj.getSvctype().getAlias() + "-" + serviceObj.getServInstanceId());
				}

				versionInfo = remoteJMXHelper.versionInformation();
				if (versionInfo != null && remoteJMXHelper.getErrorMessage() == null) {
					if (BaseConstants.START_SERVICE.equals(actionType)) {
						responseObject.setSuccess(true);
						responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_START_SUCCESS);
					} else if (BaseConstants.STOP_SERVICE.equals(actionType)) {
						responseObject.setSuccess(true);
						responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_STOP_SUCCESS);
					}
				} else {
					if (BaseConstants.START_SERVICE.equals(actionType)) {
						responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_START_JMX_API_FAIL);
					} else {
						responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_STOP_JMX_API_FAIL);
					}
					responseObject.setSuccess(false);
				}
			} else if (remoteJMXHelper.getErrorMessage() != null && remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_CONNECTION_FAILURE)) {
				responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_JMX_CON_FAIL);
				responseObject.setSuccess(false);
			} else if (remoteJMXHelper.getErrorMessage() != null && remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_API_FAILURE)) {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_JMX_API_FAIL);
			} else {
				if (BaseConstants.START_SERVICE.equals(actionType)) {
					responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_START_FAIL);
				} else {
					responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_STOP_FAIL);
				}
				responseObject.setSuccess(false);
			}
		} else {
			responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_NOT_FOUND);
			responseObject.setSuccess(false);
		}
		responseObject.setObject(serviceObj);
		;
		return responseObject;
	}

	/**
	 * Method will get service Counter details using JMX call to P - ENGINE.
	 * 
	 * @param serviceId
	 * @return ResponseObj
	 */
	@Override
	@Transactional
	public ResponseObject getServiceCounterDetails(int serviceId) {
		return fetchCounterDetailsByService(serviceId, "");
	}

	/**
	 * Method will reset service Counter details using JMX call to P - ENGINE.
	 * 
	 * @param serviceId
	 * @return ResponseObj
	 */
	@Override
	@Transactional
	public ResponseObject resetServiceCounterDetails(int serviceId) {
		return fetchCounterDetailsByService(serviceId, "reset");
	}

	/**
	 * Method will check JMX connection and service running or not.
	 * 
	 * @param serviceId
	 * @return ResponseObj
	 */
	private ResponseObject fetchCounterDetailsByService(int serviceId, String actionType) {
		ResponseObject responseObject = new ResponseObject();
		Service serviceObj = getServiceandServerinstance(serviceId);

		if (serviceObj != null) {
			ServerInstance serverInstance = serviceObj.getServerInstance();
			RemoteJMXHelper jmxConnection = new RemoteJMXHelper(serviceObj.getServerInstance().getServer().getIpAddress(), serviceObj
					.getServerInstance().getPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(),
					serverInstance.getConnectionTimeout());
			String jmxResponse;
			String versionInfo = jmxConnection.versionInformation();

			if (versionInfo != null && jmxConnection.getErrorMessage() == null) {

				if (isAnyServiceRunning(serviceObj.getServerInstance(), serviceObj.getServInstanceId(), 
						serviceObj.getSvctype().getAlias())) {

					if ("reset".equals(actionType)) {
						jmxResponse = jmxConnection.resetServiceCounterDetails(serviceObj.getSvctype().getAlias(), serviceObj.getServInstanceId());
					} else {
						jmxResponse = jmxConnection.getServiceCounterDetailsJSONData(serviceObj.getSvctype().getAlias(), serviceObj.getServInstanceId());
					}

					versionInfo = jmxConnection.versionInformation();

					if (versionInfo != null && jmxConnection.getErrorMessage() == null) {
						responseObject.setSuccess(true);

						JSONArray array = new JSONArray();
						JSONObject jsonObj = new JSONObject(jmxResponse);
						array.put(jsonObj);
						jsonObj.put("servInstanceId", serviceObj.getServInstanceId());
						
						responseObject.setObject(jsonObj);

					} else {
						responseObject.setSuccess(true);
						responseObject.setObject(null);
						responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_COUNTER_JMX_API_FAIL);
					}
				} else {
					responseObject.setObject(null);
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_STOP);
				}
			} else {
				responseObject.setObject(null);
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_CON_FAIL);
			}

		} else {
			responseObject.setObject(null);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_NOT_FOUND);
		}

		return responseObject;
	}

	/**
	 * Validate service parameter for import operation
	 * 
	 * @param service
	 * @param importErrorList
	 * @return
	 */
	@Transactional
	@Override
	public List<ImportValidationErrors> validateServiceForImport(Service service, List<ImportValidationErrors> importErrorList, int importedServiceId) {

		serviceValidator.validateServiceConfigurationParameter(service, null, importErrorList, true);

		logger.debug("Validate Service Depedants");

		if (service instanceof NetflowCollectionService) {
			NetflowCollectionService netFlow = (NetflowCollectionService) service;
			return validateClientList(netFlow.getNetFLowClientList(), importErrorList);
		} else if (service instanceof DiameterCollectionService) {
			DiameterCollectionService diameterCollectionService = (DiameterCollectionService) service;
			return validateDiameterPeerList(diameterCollectionService.getDiameterPeerList(), importErrorList);
		} else if (service instanceof SysLogCollectionService) {
			SysLogCollectionService netFlow = (SysLogCollectionService) service;
			return validateClientList(netFlow.getNetFLowClientList(), importErrorList);
		} else if (service instanceof MqttCollectionService) {
			MqttCollectionService netFlow = (MqttCollectionService) service;
			return validateClientList(netFlow.getNetFLowClientList(), importErrorList);
		} else if (service instanceof CoAPCollectionService) {
			CoAPCollectionService netFlow = (CoAPCollectionService) service;
			return validateClientList(netFlow.getNetFLowClientList(), importErrorList);
		} else if (service instanceof Http2CollectionService) {
			Http2CollectionService netFlow = (Http2CollectionService) service;
			return validateClientList(netFlow.getNetFLowClientList(), importErrorList);
		} else if (service instanceof NetflowBinaryCollectionService) {
			NetflowBinaryCollectionService netFlow = (NetflowBinaryCollectionService) service;
			return validateClientList(netFlow.getNetFLowClientList(), importErrorList);
		} else if (service instanceof CollectionService) {
			List<Drivers> drivers = service.getMyDrivers();
			if (drivers != null && !drivers.isEmpty()) {
				Drivers driver;
				for (int i = 0, size = drivers.size(); i < size; i++) {
					driver = drivers.get(i);
					Service currentService = driver.getService();
					driver.setService(service);
					if (!StateEnum.DELETED.equals(driver.getStatus())) {
						importErrorList = driversService.validateDriverForImport(driver, importErrorList);
					}
					driver.setService(currentService);
				}
				return importErrorList;
			}
		} else if (service instanceof IPLogParsingService || service instanceof ParsingService) {
			validateIPLogAndParsingDepedants(service, importErrorList);
		} else if (service instanceof DistributionService) {
			//TODO: add distribution service dependents validation. 
		} else if(service instanceof DataConsolidationService){
			//validateDataConsolidationDependents((DataConsolidationService)service,importErrorList,importedServiceId);
		} else if(service instanceof AggregationService){
			validateAggregationDependents((AggregationService)service,importErrorList);
		}

		return importErrorList;
	}
	
	/**
	 * Method will validate all data consolidate service dependents 
	 * Currently validate will work only for cons name of data consolidation entity
	 * Other entity validation will be done in other jira 
	 * @param service
	 */
	@Override
	public void validateDataConsolidationDependents(Service service, List<ImportValidationErrors> importErrorList, int importedServiceId){
		
		if(service != null){
			DataConsolidationService dataConsolidationService = (DataConsolidationService) service ;
			List<DataConsolidation> dataConsolidationList = dataConsolidationService.getConsolidation();
			if(dataConsolidationList != null && !dataConsolidationList.isEmpty()){
				
				int size = dataConsolidationList.size();
				for (int i = 0; i < size; i++) {
					DataConsolidation dataConsolidation = dataConsolidationList.get(i);
					if(!StateEnum.DELETED.equals(dataConsolidation.getStatus())){
						if(validateDataConsolidationNameForService(dataConsolidation.getConsName(), importedServiceId)){
							this.consolidationAttributeValidator.setDataConsolidationValidationMsg(dataConsolidation,importErrorList);
						}
					}
				}
			}
 		}
	}
	
	/**
	 * Method will check data consolidation definition name unique for service  
	 * @param name
	 * @param serviceId
	 * @return
	 */
	public boolean validateDataConsolidationNameForService(String name, int serviceId){
		List<DataConsolidation> dataConsolidationList = this.iDataConsolidation.getDataConsolidationServicewiseCount(name, serviceId);
		boolean returnFlag = false;
		if(dataConsolidationList != null && !dataConsolidationList.isEmpty()){
			returnFlag = true;
		}
		return returnFlag;
	}
	
	
	/**
	 * Iterate over service dependants , change id and name for import operation
	 * 
	 * @param serverInstanceDB
	 * @param exportedService
	 * @param isUpdateServiceInfo
	 * @return ResponseObject
	 */
	@Transactional(rollbackFor = SMException.class)
	@Override
	public ResponseObject iterateOverServiceAndDepedantsForImport(ServerInstance serverInstanceDB, Service exportedService, boolean isImport,
			boolean isUpdateServiceInfo, Map<Integer, Integer> svcIdMap, Map<String, Integer> policyMap, boolean importModule, boolean isServiceLevel) {
		ResponseObject responseObject = new ResponseObject();
		int oldSvcId = 0;
		if (exportedService != null) {

			if (isImport && isUpdateServiceInfo) { // when import server
													// instance operation
				logger.debug("Iterate over service for import server instance operation");
				logger.debug("Befor Service Save , old Id for service name is : " + exportedService.getName() + " id is ::  " + exportedService.getId());
				oldSvcId = exportedService.getId();
				exportedService.setId(0);
				exportedService.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,exportedService.getName()));
				exportedService.setServerInstance(serverInstanceDB);
				exportedService.setCreatedByStaffId(serverInstanceDB.getCreatedByStaffId());
				exportedService.setCreatedDate(new Date());
			}

			if (isImport) { // only for import operation for both service and
							// server instance
				logger.debug("Iterate over service for import operation set service exec param");
				if (exportedService.getSvcExecParams() != null) {
					exportedService.setSvcExecParams(exportedService.getSvcExecParams());
				} else {
					exportedService.setSvcExecParams(new ServiceExecutionParams());
				}
			} else { // when need to delete service
				if (isUpdateServiceInfo) {
					exportedService.setStatus(StateEnum.DELETED);
					exportedService.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,exportedService.getName()));
				}
			}

			exportedService.setLastUpdatedDate(new Date());
			exportedService.setLastUpdatedByStaffId(serverInstanceDB.getCreatedByStaffId());

			logger.debug("Import Service Type = " + exportedService.getClass().getName());
			
			responseObject = iterateOverServiceDepedantsForImport(exportedService, serverInstanceDB, isImport, policyMap, isServiceLevel);
			if (isImport && importModule) { 
				
				exportedService.setServInstanceId(getMaxServiceInstanceIdForServer(exportedService.getServerInstance().getId(), exportedService.getSvctype().getId()));
			}
			
			if (responseObject.isSuccess()) {
				logger.debug("Create or delete service and all its depedants updated automatically");
				
				if (isImport && isUpdateServiceInfo) {
					logger.debug(" Service saved");

					servicesDao.save(exportedService);

					logger.debug("After Service Save , new Id for service name is : " + exportedService.getName() + " id is ::  "
							+ exportedService.getId());
					if (svcIdMap != null && oldSvcId != 0) {
						svcIdMap.put(oldSvcId, exportedService.getId());
					}
				} else {
					logger.debug(" Service merged");
					servicesDao.merge(exportedService);
				}

			} else {
				logger.debug("Fail to create or updateService");
			}

		} else {
			responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_NOT_FOUND);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	/**
	 * Use to iterate through consolidation configuration for import
	 * @param service
	 * @param isImport
	 * @return
	 */
	private ResponseObject iterateConsolidationAttributeGroup(DataConsolidationService service, boolean isImport){
		ResponseObject responseObject = new ResponseObject();
		if (service != null) {
			List<DataConsolidation> consolidationList = service.getConsolidation();
			List<DataConsolidation>  newConsolidationList = new ArrayList<>();
			if(consolidationList != null && !consolidationList.isEmpty()){
				for(DataConsolidation consolidation : consolidationList){
					if(!StateEnum.DELETED.equals(consolidation.getStatus())){
						consolidation.setCreatedByStaffId(service.getLastUpdatedByStaffId());
						consolidation.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
						consolidation.setConsService(service);
						
						if(isImport){
							consolidation.setId(0);
							if(!StateEnum.DELETED.equals(consolidation.getStatus())){
								consolidation.setConsName(consolidation.getConsName());
								iterateGroupAttribute(consolidation, isImport);
							}
						}
						else {
							consolidation.setConsName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,consolidation.getConsName()));
							consolidation.setStatus(StateEnum.DELETED);
							consolidation.setConsAttList(null);
							consolidation.setConsGrpAttList(null);
						}
						
						newConsolidationList.add(consolidation);
					}
				}
				service.setConsolidation(newConsolidationList);
			}
		}
		return responseObject;
	}
	
	private ResponseObject iterateAggregationDefinitionParam(AggregationDefinition aggDefinition, boolean isImport){
		
		ResponseObject responseObject = new ResponseObject();
		if (aggDefinition != null) {
			Date date = new Date();
			if (isImport) {
				logger.debug("Going to create new definition paramters.");
				aggDefinition.setId(0);
				aggDefinition.setCreatedDate(date);
				aggDefinition.setCreatedByStaffId(aggDefinition.getLastUpdatedByStaffId());
				aggDefinition.setLastUpdatedByStaffId(aggDefinition.getLastUpdatedByStaffId());
				aggDefinition.setLastUpdatedDate(date);
				if(aggDefinition.getAggConditionList() != null){
					for(AggregationCondition aggCond : aggDefinition.getAggConditionList()){
						aggCond.setAggregationDefinition(aggDefinition);
					}
				}
				if(aggDefinition.getAggKeyAttrList() != null){
					for(AggregationKeyAttribute aggKeyAttribute : aggDefinition.getAggKeyAttrList()){
						aggKeyAttribute.setAggregationDefinition(aggDefinition);
					}
				}
				if(aggDefinition.getAggAttrList() != null){
					for(AggregationAttribute aggAttribute : aggDefinition.getAggAttrList()){
						aggAttribute.setAggregationDefinition(aggDefinition);
					}
				}
				responseObject.setObject(aggDefinition);
			} else {
				logger.debug("Delete  aggregation definition paramters.");
				aggDefinition.setLastUpdatedByStaffId(aggDefinition.getLastUpdatedByStaffId());
				aggDefinition.setLastUpdatedDate(date);
				aggDefinition.setAggDefName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,aggDefinition.getAggDefName()));
				aggDefinition.setStatus(StateEnum.DELETED);
				aggDefinition.getAggregationService().setAggregationDefinition(null);
				if(aggDefinition.getAggConditionList() != null){
					logger.debug("Delete  aggregation condition paramters.");
					for(AggregationCondition aggCond : aggDefinition.getAggConditionList()){
						aggCond.setLastUpdatedByStaffId(aggDefinition.getLastUpdatedByStaffId());
						aggCond.setLastUpdatedDate(date);
						aggCond.setCondExpression(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,aggCond.getCondExpression()));
						aggCond.setStatus(StateEnum.DELETED);
					}
				}
				
				if(aggDefinition.getAggKeyAttrList() != null){
					logger.debug("Delete  aggregation Key Attribute paramters.");
					for(AggregationKeyAttribute aggKeyAttribute : aggDefinition.getAggKeyAttrList()){
						aggKeyAttribute.setLastUpdatedByStaffId(aggDefinition.getLastUpdatedByStaffId());
						aggKeyAttribute.setLastUpdatedDate(date);
						aggKeyAttribute.setFieldName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,aggKeyAttribute.getFieldName()));
						aggKeyAttribute.setStatus(StateEnum.DELETED);
					}
				}
				
				if(aggDefinition.getAggAttrList() != null){
					logger.debug("Delete  aggregation attribute paramters.");
					for(AggregationAttribute aggAttribute : aggDefinition.getAggAttrList()){
						aggAttribute.setLastUpdatedByStaffId(aggDefinition.getLastUpdatedByStaffId());
						aggAttribute.setLastUpdatedDate(date);
						aggAttribute.setOutputFieldName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,aggAttribute.getOutputFieldName()));
						aggAttribute.setStatus(StateEnum.DELETED);
					}
				}
				aggDefinition.setAggAttrList(null);
				aggDefinition.setAggKeyAttrList(null);
				aggDefinition.setAggConditionList(null);
				responseObject.setObject(null);
			}
		} 
		responseObject.setSuccess(true);
		return responseObject;
	}
	
	private ResponseObject iterateAggregationDefinitionGroup(AggregationService service, boolean isImport){
		ResponseObject responseObject;
		logger.debug("Found Aggregation definition object.");
		AggregationDefinition  aggDefinitionParams = service.getAggregationDefinition();
		if(aggDefinitionParams != null){
			aggDefinitionParams.setCreatedByStaffId(service.getLastUpdatedByStaffId());
			aggDefinitionParams.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
		}
		
		responseObject = iterateAggregationDefinitionParam(aggDefinitionParams, isImport);
		if (responseObject.isSuccess()) {
			AggregationDefinition aggDefinition = (AggregationDefinition) responseObject.getObject();
			service.setAggregationDefinition(aggDefinition);
			responseObject.setObject(service);
		}
		return responseObject;
	}
	
	/**
	 * Iterate Consolidation Group and Attribute For Import
	 * @param consolidation
	 */
	private void iterateGroupAttribute(DataConsolidation consolidation, boolean isImport){
		List<DataConsolidationGroupAttribute> grpAttrList = consolidation.getConsGrpAttList();
		List<DataConsolidationAttribute> attrList = consolidation.getConsAttList();
		List<DataConsolidationAttribute> newAttrList = new ArrayList<>();
		List<DataConsolidationGroupAttribute> newGrpList = new ArrayList<>();
		
		if(grpAttrList != null && !grpAttrList.isEmpty()){
			for(DataConsolidationGroupAttribute grpAttr : grpAttrList){
				if(!StateEnum.DELETED.equals(grpAttr.getStatus())){
					grpAttr.setCreatedByStaffId(consolidation.getLastUpdatedByStaffId());
					grpAttr.setLastUpdatedByStaffId(consolidation.getLastUpdatedByStaffId());
					grpAttr.setDataConsolidation(consolidation);
					newGrpList.add(grpAttr);
					if(isImport){
						grpAttr.setId(0);
					}else{
						grpAttr.setStatus(StateEnum.DELETED);
					}
				}				
			}
			consolidation.setConsGrpAttList(newGrpList);
		}
		
		if(attrList != null && !attrList.isEmpty()){
			for(DataConsolidationAttribute attr : attrList){
				if(!StateEnum.DELETED.equals(attr.getStatus())){
					attr.setCreatedByStaffId(consolidation.getLastUpdatedByStaffId());
					attr.setLastUpdatedByStaffId(consolidation.getLastUpdatedByStaffId());
					attr.setDataConsolidation(consolidation);
					newAttrList.add(attr);
					if(isImport){
						attr.setId(0);
					}else{
						attr.setStatus(StateEnum.DELETED);
					}
				}
			}
			consolidation.setConsAttList(newAttrList);
		}
	}
	
	/**
	 * Iterate Consolidation Path List Mapping Attribute
	 * @param pathList
	 * @param isImport
	 */
	private void iterateDataConsolidationPathListMapping(DataConsolidationPathList pathList, boolean isImport){
		List<DataConsolidationMapping> newConsolidationMapping = new ArrayList<>();
		if(pathList.getConMappingList() != null && !pathList.getConMappingList().isEmpty()){
			for(DataConsolidationMapping consolidationMapping : pathList.getConMappingList()){
				if(!StateEnum.DELETED.equals(consolidationMapping.getStatus())){
					consolidationMapping.setCreatedByStaffId(pathList.getLastUpdatedByStaffId());
					consolidationMapping.setLastUpdatedByStaffId(pathList.getLastUpdatedByStaffId());
					consolidationMapping.setDataConsPathList(pathList);
					newConsolidationMapping.add(consolidationMapping);
					if(isImport){
						consolidationMapping.setId(0);
						//consolidationMapping.setMappingName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,consolidationMapping.getMappingName()));
						consolidationMapping.setMappingName(consolidationMapping.getMappingName());
					}else{
						consolidationMapping.setMappingName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,consolidationMapping.getMappingName()));
						consolidationMapping.setStatus(StateEnum.DELETED);
					}
				}
			}
			pathList.setConMappingList(newConsolidationMapping);
		}
	}


	/**
	 * Method will set Service Pathlist status to Deleted. Method will be used
	 * for remove all service pathlist dependents.
	 * 
	 * @param service
	 * @return Response Object
	 */

	private ResponseObject iterateServicePathListDetails(Service service, boolean isImport, Map<String, Integer> policyMap, int importMode) {
		ResponseObject responseObject = new ResponseObject();
		if (service != null) {
			List<PathList> pathlist = service.getSvcPathList();
			List<PathList> importedPathList = new ArrayList<>();
			if (pathlist != null && !pathlist.isEmpty()) {
				PathList path;

				for (int i = 0, size = pathlist.size(); i < size; i++) {
					path = pathlist.get(i);
					if(!StateEnum.DELETED.equals(path.getStatus())){
						importedPathList.add(path);
						logger.debug("Pathlist Name =" + path.getName() + " & Pathlist Status =" + path.getStatus());
						if (isImport) {
							path.setId(0);
							if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
								path.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,path.getName()));
							}
							path.setCreatedByStaffId(service.getLastUpdatedByStaffId());
							path.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
							path.setService(service);
							
							/*
							 *  We are setting Policy based on alias name exported to File to avoid Policy and related objects in export/import.
							 */
							if(path instanceof ProcessingPathList && !StringUtils.isEmpty(((ProcessingPathList) path).getPolicyAlias())){
								List<ProcessingPathList> processingPathList = new ArrayList<>();
								if(policyMap != null && policyMap.get(((ProcessingPathList) path).getPolicyAlias()) != null){
									Policy tempPolicy = policyDao.findByPrimaryKey(Policy.class, policyMap.get(((ProcessingPathList) path).getPolicyAlias()));
									if(tempPolicy != null){
										((ProcessingPathList) path).setPolicy(tempPolicy);
										((ProcessingPathList) path).setPolicyAlias(tempPolicy.getAlias());
										processingPathList.add((ProcessingPathList)path);
										tempPolicy.setProcessingPathList(processingPathList);
									}
									
								}
							}
							if(path instanceof DataConsolidationPathList){
								iterateDataConsolidationPathListMapping((DataConsolidationPathList)path, isImport);
							}/*if(path instanceof AggregationServicePathList){
								((AggregationServicePathList) path).setMaxFilesCountAlert(maxFilesCountAlert);
							}*/
						} else {
							path.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,path.getName()));
							path.setStatus(StateEnum.DELETED);
							path.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
						}
						if(path instanceof ParsingPathList){
							responseObject = pathlistService.iterateOverParsingPathListConfig((ParsingPathList)path, isImport, importMode);
						}
					}
				}
				service.setSvcPathList(importedPathList);
			} else {
				logger.debug("Path List not configured for service " + service.getId());
				responseObject.setSuccess(true);
				responseObject.setObject(service);
			}
		}
		return responseObject;
	}
	
	/**
	 * Check if imported service and service in file both are same object or not
	 * 
	 * @param serviceDB
	 * @param exportedserviceInstance
	 * @param finaljArray
	 * @return
	 */
	public ResponseObject checkisValidFileForService(Service serviceDB, Service exportedserviceInstance, JSONArray finaljArray) {

		ResponseObject responseObject = new ResponseObject();

		if (serviceDB instanceof NetflowCollectionService) {
			logger.debug("DB service is NetflowCollectionService");

			if (exportedserviceInstance instanceof NetflowCollectionService) {
				checkForXSDValidation(finaljArray, responseObject, exportedserviceInstance);
			} else {
				logger.debug("Invalid file found , DB service is NetflowCollectionService");
				returnInvalidFileError(EngineConstants.NATFLOW_COLLECTION_SERVICE, responseObject);
			}

		} else if (serviceDB instanceof SysLogCollectionService) {
			logger.debug("DB service is SysLogCollectionService");

			if (exportedserviceInstance instanceof SysLogCollectionService) {
				checkForXSDValidation(finaljArray, responseObject, exportedserviceInstance);
			} else {
				logger.debug("Invalid file found , DB service is SysLogCollectionService");
				returnInvalidFileError(EngineConstants.SYSLOG_COLLECTION_SERVICE, responseObject);
			}
			
		} else if (serviceDB instanceof MqttCollectionService) {
			logger.debug("DB service is MqttCollectionService");

			if (exportedserviceInstance instanceof MqttCollectionService) {
				checkForXSDValidation(finaljArray, responseObject, exportedserviceInstance);
			} else {
				logger.debug("Invalid file found , DB service is MqttCollectionService");
				returnInvalidFileError(EngineConstants.MQTT_COLLECTION_SERVICE, responseObject);
			}	

		} else if (serviceDB instanceof CoAPCollectionService) {
			logger.debug("DB service is CoAPCollectionService");

			if (exportedserviceInstance instanceof CoAPCollectionService) {
				checkForXSDValidation(finaljArray, responseObject, exportedserviceInstance);
			} else {
				logger.debug("Invalid file found , DB service is CoAPCollectionService");
				returnInvalidFileError(EngineConstants.COAP_COLLECTION_SERVICE, responseObject);
			}	

		} else if (serviceDB instanceof Http2CollectionService) {
			logger.debug("DB service is Http2CollectionService");

			if (exportedserviceInstance instanceof Http2CollectionService) {
				checkForXSDValidation(finaljArray, responseObject, exportedserviceInstance);
			} else {
				logger.debug("Invalid file found , DB service is Http2CollectionService");
				returnInvalidFileError(EngineConstants.HTTP2_COLLECTION_SERVICE, responseObject);
			}	

		} else if (serviceDB instanceof GTPPrimeCollectionService) {
			logger.debug("DB service is GTPPrimeCollectionService");

			if (exportedserviceInstance instanceof GTPPrimeCollectionService) {
				checkForXSDValidation(finaljArray, responseObject, exportedserviceInstance);
			} else {
				logger.debug("Invalid file found , DB service is gtpPrimeCollectionService");
				returnInvalidFileError(EngineConstants.GTPPRIME_COLLECTION_SERVICE, responseObject);
			}

		} else if (serviceDB instanceof NetflowBinaryCollectionService) {
			logger.debug("DB service is NetflowBinaryCollectionService");

			if (exportedserviceInstance instanceof NetflowCollectionService || exportedserviceInstance instanceof SysLogCollectionService) {
				logger.debug("Invalid file found , DB service is NetflowBinaryCollectionService");
				returnInvalidFileError(EngineConstants.NATFLOWBINARY_COLLECTION_SERVICE, responseObject);
			} else if (exportedserviceInstance instanceof NetflowBinaryCollectionService) {
				checkForXSDValidation(finaljArray, responseObject, exportedserviceInstance);
			} else {
				logger.debug("Invalid file found , DB service is NetflowBinaryCollectionService");
				returnInvalidFileError(EngineConstants.NATFLOWBINARY_COLLECTION_SERVICE, responseObject);
			}

		} else if (serviceDB instanceof CollectionService) {
			logger.debug("DB service is CollectionService");

			if (exportedserviceInstance instanceof NetflowCollectionService || exportedserviceInstance instanceof SysLogCollectionService
					|| exportedserviceInstance instanceof NetflowBinaryCollectionService) {
				logger.debug("Invalid file found , DB service is CollectionService");
				returnInvalidFileError(EngineConstants.COLLECTION_SERVICE, responseObject);
			} else if (exportedserviceInstance instanceof CollectionService) {
				checkForXSDValidation(finaljArray, responseObject, exportedserviceInstance);
			} else {
				logger.debug("Invalid file found , DB service is CollectionService");
				returnInvalidFileError(EngineConstants.COLLECTION_SERVICE, responseObject);
			}

		} else if (serviceDB instanceof IPLogParsingService) { // Added for
																// IPLog parsing
																// service for
																// xsd
																// validation
			logger.debug("DB service is IPLog Parsing Service");

			if (exportedserviceInstance instanceof IPLogParsingService) {
				checkForXSDValidation(finaljArray, responseObject, exportedserviceInstance);
			} else {
				logger.debug("Invalid file found , DB service is IPLogParsingService");
				returnInvalidFileError(EngineConstants.IPLOG_PARSING_SERVICE, responseObject);
			}

		} else if (serviceDB instanceof ParsingService) { // Added for IPLog
															// parsing service
															// for xsd
															// validation
			logger.debug("DB service is  Parsing Service");

			if (exportedserviceInstance instanceof ParsingService) {
				checkForXSDValidation(finaljArray, responseObject, exportedserviceInstance);
			} else {
				logger.debug("Invalid file found , DB service is ParsingService");
				returnInvalidFileError(EngineConstants.PARSING_SERVICE, responseObject);
			}

		} else if (serviceDB instanceof DistributionService) { // Added for
																// Distribution
																// service for
																// xsd
																// validation
			logger.debug("DB service is  DistributionService Service");

			if (exportedserviceInstance instanceof DistributionService) {
				checkForXSDValidation(finaljArray, responseObject, exportedserviceInstance);
			} else {
				logger.debug("Invalid file found , DB service is DistributionService");
				returnInvalidFileError(EngineConstants.DISTRIBUTION_SERVICE, responseObject);
			}

		}else if (serviceDB instanceof ProcessingService) {
			logger.debug("DB service is  Processing Service");
			if (exportedserviceInstance instanceof ProcessingService) {
				checkForXSDValidation(finaljArray, responseObject, exportedserviceInstance);
			}else {
				logger.debug("Invalid file found , DB service is Processing Service");
				returnInvalidFileError(EngineConstants.PROCESSING_SERVICE, responseObject);
			}
		}else if(serviceDB instanceof DataConsolidationService){
			logger.debug("DB service is  DataConsolidation Service");
			if (exportedserviceInstance instanceof DataConsolidationService) {
				checkForXSDValidation(finaljArray, responseObject, exportedserviceInstance);
			}else {
				logger.debug("Invalid file found , DB service is Processing Service");
				returnInvalidFileError(EngineConstants.DATA_CONSOLIDATION_SERVICE, responseObject);
			}
		}else if(serviceDB instanceof AggregationService){
			logger.debug("DB service is  Aggregation Service");
			if (exportedserviceInstance instanceof AggregationService) {
				checkForXSDValidation(finaljArray, responseObject, exportedserviceInstance);
			}else {
				logger.debug("Invalid file found , DB service is Not Aggregation Service");
				returnInvalidFileError(EngineConstants.AGGREGATION_SERVICE, responseObject);
			}
		}

		return responseObject;
	}

	/**
	 * validate list of client
	 * 
	 * @param clients
	 * @param serviceimportErrorList
	 * @return
	 */
	public List<ImportValidationErrors> validateClientList(List<NetflowClient> clients, List<ImportValidationErrors> serviceimportErrorList) {
		if (clients != null && !clients.isEmpty()) {
			NetflowClient client;

			for (int i = 0, size = clients.size(); i < size; i++) {
				client = clients.get(i);
				if (!StateEnum.DELETED.equals(client.getStatus())) {

					serviceimportErrorList = clientService.validateClientForImport(client, serviceimportErrorList);

				}
			}
			return serviceimportErrorList;
		} else {
			return serviceimportErrorList;
		}
	}
	
	/**
	 * validate list of diameter peer
	 * 
	 * @param diameterPeers
	 * @param serviceimportErrorList
	 * @return
	 */
	public List<ImportValidationErrors> validateDiameterPeerList(List<DiameterPeer> diameterPeers, List<ImportValidationErrors> serviceimportErrorList) {
		if (diameterPeers != null && !diameterPeers.isEmpty()) {
			DiameterPeer diameterPeer;

			for (int i = 0, size = diameterPeers.size(); i < size; i++) {
				diameterPeer = diameterPeers.get(i);
				if (!StateEnum.DELETED.equals(diameterPeer.getStatus())) {

					serviceimportErrorList = diameterPeerService.validateDiameterPeerForImport(diameterPeer, serviceimportErrorList);

				}
			}
			return serviceimportErrorList;
		} else {
			return serviceimportErrorList;
		}
	}

	/**
	 * Iterate over service depedants based on type
	 * 
	 * @param exportedService
	 * @param serverInstanceDB
	 * @return
	 */
	public ResponseObject iterateOverServiceDepedantsForImport(Service exportedService, ServerInstance serverInstanceDB, boolean isImport, Map<String, Integer> policyMap, boolean isServiceLevel) {
		ResponseObject responseObject = new ResponseObject();
		if (exportedService instanceof NetflowCollectionService) {

			logger.debug("Process Netflow Collection service detail");
			((NetflowCollectionService) exportedService).setServerIp(serverInstanceDB.getServer().getIpAddress());
			((NetflowCollectionService) exportedService).setServerPort(serverInstanceDB.getPort());
			exportedService.setSvctype(servicetypeDao.getServiceTypeByAlias(EngineConstants.NATFLOW_COLLECTION_SERVICE));
			netflowClientService.iterateServiceClientDetails((NetflowCollectionService) exportedService, isImport);
			proxyClientConfigurationService.iterateServiceClientDetails((NetflowCollectionService) exportedService, isImport);

		} else if (exportedService instanceof DiameterCollectionService) {

			logger.debug("Process Diameter Collection service detail");
			((DiameterCollectionService) exportedService).setStackIp(serverInstanceDB.getServer().getIpAddress());
			((DiameterCollectionService) exportedService).setStackPort(serverInstanceDB.getPort());
			exportedService.setSvctype(servicetypeDao.getServiceTypeByAlias(EngineConstants.DIAMETER_COLLECTION_SERVICE));
			diameterPeerService.iterateServicePeerDetails((DiameterCollectionService) exportedService, isImport);

		} else if (exportedService instanceof SysLogCollectionService) {

			logger.debug("Process SysLog Collection service detail");
			((SysLogCollectionService) exportedService).setServerIp(serverInstanceDB.getServer().getIpAddress());
			((SysLogCollectionService) exportedService).setServerPort(serverInstanceDB.getPort());
			exportedService.setSvctype(servicetypeDao.getServiceTypeByAlias(EngineConstants.SYSLOG_COLLECTION_SERVICE));
			netflowClientService.iterateServiceClientDetails((SysLogCollectionService) exportedService, isImport);

		} else if (exportedService instanceof MqttCollectionService) {

			logger.debug("Process SysLog Collection service detail");
			((MqttCollectionService) exportedService).setServerIp(serverInstanceDB.getServer().getIpAddress());
			((MqttCollectionService) exportedService).setServerPort(serverInstanceDB.getPort());
			exportedService.setSvctype(servicetypeDao.getServiceTypeByAlias(EngineConstants.MQTT_COLLECTION_SERVICE));
			netflowClientService.iterateServiceClientDetails((MqttCollectionService) exportedService, isImport);

		} else if (exportedService instanceof CoAPCollectionService) {

			logger.debug("Process CoAP Collection service detail");
			((CoAPCollectionService) exportedService).setServerIp(serverInstanceDB.getServer().getIpAddress());
			((CoAPCollectionService) exportedService).setServerPort(serverInstanceDB.getPort());
			exportedService.setSvctype(servicetypeDao.getServiceTypeByAlias(EngineConstants.COAP_COLLECTION_SERVICE));
			netflowClientService.iterateServiceClientDetails((CoAPCollectionService) exportedService, isImport);

		} else if (exportedService instanceof Http2CollectionService) {

			logger.debug("Process Http2 Collection service detail");
			((Http2CollectionService) exportedService).setServerIp(serverInstanceDB.getServer().getIpAddress());
			((Http2CollectionService) exportedService).setServerPort(serverInstanceDB.getPort());
			exportedService.setSvctype(servicetypeDao.getServiceTypeByAlias(EngineConstants.HTTP2_COLLECTION_SERVICE));
			netflowClientService.iterateServiceClientDetails((Http2CollectionService) exportedService, isImport);

		} else if (exportedService instanceof GTPPrimeCollectionService) {

			logger.debug("Process SysLog Collection service detail");
			((GTPPrimeCollectionService) exportedService).setServerIp(serverInstanceDB.getServer().getIpAddress());
			((GTPPrimeCollectionService) exportedService).setServerPort(serverInstanceDB.getPort());
			exportedService.setSvctype(servicetypeDao.getServiceTypeByAlias(EngineConstants.GTPPRIME_COLLECTION_SERVICE));
			netflowClientService.iterateServiceClientDetails((GTPPrimeCollectionService) exportedService, isImport);

		} else if(exportedService instanceof RadiusCollectionService) {
			logger.debug("Process Radius Collection service detail");
			((RadiusCollectionService) exportedService).setServerIp(serverInstanceDB.getServer().getIpAddress());
			((RadiusCollectionService) exportedService).setServerPort(serverInstanceDB.getPort());
			exportedService.setSvctype(servicetypeDao.getServiceTypeByAlias(EngineConstants.RADIUS_COLLECTION_SERVICE));
			netflowClientService.iterateServiceClientDetails((RadiusCollectionService) exportedService, isImport);
		}else if (exportedService instanceof NetflowBinaryCollectionService) {

			logger.debug("Process NetFlow Binary Collection service detail");
			((NetflowBinaryCollectionService) exportedService).setServerIp(serverInstanceDB.getServer().getIpAddress());
			((NetflowBinaryCollectionService) exportedService).setServerPort(serverInstanceDB.getPort());
			exportedService.setSvctype(servicetypeDao.getServiceTypeByAlias(EngineConstants.NATFLOWBINARY_COLLECTION_SERVICE));
			netflowClientService.iterateServiceClientDetails((NetflowBinaryCollectionService) exportedService, isImport);
			proxyClientConfigurationService.iterateServiceClientDetails((NetflowBinaryCollectionService) exportedService, isImport);

		} else if (exportedService instanceof CollectionService) {

			logger.debug("Process Collection service driver detail");
			exportedService.setSvctype(servicetypeDao.getServiceTypeByAlias(EngineConstants.COLLECTION_SERVICE));
			iterateServiceSchedulingParams((CollectionService) exportedService, isImport);
			driversService.iterateServiceDriverDetails(exportedService, isImport);

		} else if (exportedService instanceof IPLogParsingService) {

			logger.debug("Process IPLog parsing service details");
			exportedService.setSvctype(servicetypeDao.getServiceTypeByAlias(EngineConstants.IPLOG_PARSING_SERVICE));
			iterateIplogParsingServiceDependents((IPLogParsingService) exportedService, isImport, isServiceLevel);//NOSONAR
			iterateServicePathListDetails((IPLogParsingService) exportedService, isImport, null, BaseConstants.IMPORT_MODE_KEEP_BOTH);

		} else if (exportedService instanceof ParsingService) {

			logger.debug("Process Parsing service detail");
			exportedService.setSvctype(servicetypeDao.getServiceTypeByAlias(EngineConstants.PARSING_SERVICE));
			iterateServiceFileGroupParams((ParsingService) exportedService, isImport);
			iterateServicePathListDetails((ParsingService) exportedService, isImport, null, BaseConstants.IMPORT_MODE_KEEP_BOTH);

		} else if (exportedService instanceof DistributionService) {
			logger.debug("Service is Distribution Service , Process Distribution Service and its dependents");

			iterateServiceSchedulingParams((DistributionService) exportedService, isImport);
			iterateServiceFileGroupParams((DistributionService) exportedService, isImport);
			driversService.iterateServiceDriverDetails(exportedService, isImport);

		}else if (exportedService instanceof ProcessingService) {

			logger.debug("Process Processing service detail");
			exportedService.setSvctype(servicetypeDao.getServiceTypeByAlias(EngineConstants.PROCESSING_SERVICE));
			iterateServiceFileGroupParams((ProcessingService) exportedService, isImport);
			iterateServicePathListDetails((ProcessingService) exportedService, isImport, policyMap, BaseConstants.IMPORT_MODE_KEEP_BOTH);
			
		}else if(exportedService instanceof DataConsolidationService){
			exportedService.setSvctype(servicetypeDao.getServiceTypeByAlias(EngineConstants.DATA_CONSOLIDATION_SERVICE));
			iterateServiceFileGroupParams((DataConsolidationService) exportedService, isImport);
			iterateServicePathListDetails((DataConsolidationService) exportedService, isImport, null, BaseConstants.IMPORT_MODE_KEEP_BOTH);
			iterateConsolidationAttributeGroup((DataConsolidationService) exportedService, isImport);
		}else if(exportedService instanceof AggregationService){
			exportedService.setSvctype(servicetypeDao.getServiceTypeByAlias(EngineConstants.AGGREGATION_SERVICE));
			iterateServiceFileGroupParams((AggregationService) exportedService, isImport);
			iterateServiceSchedulingParams((AggregationService) exportedService, isImport);
			iterateServicePathListDetails((AggregationService) exportedService, isImport, null, BaseConstants.IMPORT_MODE_KEEP_BOTH);
			iterateAggregationDefinitionGroup((AggregationService) exportedService, isImport);
		}
		responseObject.setSuccess(true);
		responseObject.setObject(exportedService);

		return responseObject;
	}

	/**
	 * Update iplog service hash config param in db
	 * 
	 * @param service
	 * @param partitionParamStr
	 * @return
	 * @throws CloneNotSupportedException 
	 */
	@Transactional
	@Override
	public ResponseObject updateIplogParsingServiceHashConfiguration(IPLogParsingService service, String partitionParamStr) throws CloneNotSupportedException {
		ResponseObject responseObject;
		List<PartitionParam> liPartitionParam = new ArrayList<>();

		service.setLastUpdatedDate(new Date());

		JSONArray jPartitionParamArr = new JSONArray(partitionParamStr);

		for (int index = 0; index < jPartitionParamArr.length(); index++) {
			JSONObject jParam = jPartitionParamArr.getJSONObject(index);
			PartitionParam param = paramDao.findByPrimaryKey(PartitionParam.class, jParam.getInt("id"));
			PartitionParam newPartitionParam = (PartitionParam) param.clone();
			if (newPartitionParam != null) {
				newPartitionParam.setPartitionField(PartitionFieldEnum.valueOf(jParam.getString("partitionField")));
				newPartitionParam.setUnifiedField(jParam.getString("unifiedField"));
				
				newPartitionParam.setPartitionRange(jParam.getString("partitionRange"));
				
				if(!jParam.isNull("baseUnifiedField") && !"NA".equals(jParam.getString("baseUnifiedField"))){
					newPartitionParam.setBaseUnifiedField(jParam.getString("baseUnifiedField"));
				}else if(!jParam.isNull("baseUnifiedField") && "NA".equals(jParam.getString("baseUnifiedField"))){
					newPartitionParam.setBaseUnifiedField(null);
				}
				
				if(!jParam.isNull("netMask") ||  jParam.getInt("netMask") == -1 ){
					newPartitionParam.setNetMask(jParam.getInt("netMask"));  
				}
				
				newPartitionParam.setStatus(StateEnum.valueOf(jParam.getString("status")));
				liPartitionParam.add(newPartitionParam);

			}
		}
		service.setPartionParamList(liPartitionParam);
		ServicesService  serviceServiceImpl = (ServicesService) SpringApplicationContext.getBean("servicesService"); // getting spring bean for aop context issue to audit.
		responseObject = serviceServiceImpl.updatePartitionParameters(service);
		return responseObject;
	}
	

	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_IPLOG_HASH_CONFIGURATION,actionType = BaseConstants.UPDATE_ACTION, currentEntity = IPLogParsingService.class, ignorePropList= "fileGroupingParameter")
	public ResponseObject updatePartitionParameters(IPLogParsingService service){
		ResponseObject responseObject = new ResponseObject();
		ServerInstance serverInstance = serverInstanceService.getServerInstance(service.getServerInstance().getId());
		if (serverInstance != null) {

			service.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
			service.setLastUpdatedDate(new Date());
			service.setServerInstance(serverInstance);

			servicesDao.merge(service);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SERVICE_UPDATE_SUCCESS);
			responseObject.setObject(service);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVICE_UPDATE_FAIL);
		}

		return responseObject;
	}
	
	/**
	 * Return XSD validation error
	 * 
	 * @param finaljArray
	 * @param responseObject
	 */
	public void checkForXSDValidation(JSONArray finaljArray, ResponseObject responseObject, Service exportedserviceInstance) {

		if (finaljArray != null && finaljArray.length() > 0) {
			logger.debug("XSD Validation fail , so return validation error");

			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.IMPORT_XSD_VALIDATION_FAIL);
			responseObject.setObject(finaljArray);

		} else {
			logger.debug("XSD Validation Done Successfully");
			responseObject.setSuccess(true);
			responseObject.setObject(exportedserviceInstance);
		}

	}

	/**
	 * Return invalid file error
	 * 
	 * @param dbServiceType
	 * @param exportedServiceType
	 * @param responseObject
	 */
	public void returnInvalidFileError(String dbServiceType, ResponseObject responseObject) {

		responseObject.setResponseCode(ResponseCode.IMPORT_SERVICE_UNMARSHALL_FAIL_CLASSCAST);
		responseObject.setArgs(new Object[] { dbServiceType });
		responseObject.setSuccess(false);

	}

	public void validateAggregationDependents(AggregationService service,List<ImportValidationErrors> importErrorList){
		serviceValidator.validateServiceConfigurationParameter(service, null, importErrorList, true);
		AggregationDefinition aggDefinition = service.getAggregationDefinition();
		if(aggDefinition != null){
			aggDefinitionValidator.validateAggregationDefinitionParams(aggDefinition, null, importErrorList, true);
		}
	}
	
	/**
	 * Validate iplog and parsing service depedents
	 * 
	 * @param service
	 * @param importErrorList
	 */
	public void validateIPLogAndParsingDepedants(Service service, List<ImportValidationErrors> importErrorList) {

		if (service instanceof IPLogParsingService) {
			logger.debug(" Validate dependents of IPLogParsingService");
			IPLogParsingService iplogService = (IPLogParsingService) service;
			partitionParamService.validatePartitionParamForImport(iplogService.getPartionParamList(), importErrorList);
		}

		List<PathList> pathlist = service.getSvcPathList();

		if (pathlist != null && !pathlist.isEmpty()) {
			ParsingPathList path;

			for (int i = 0, size = pathlist.size(); i < size; i++) {
				path = (ParsingPathList) pathlist.get(i);
				if (!StateEnum.DELETED.equals(path.getStatus())) {
					path.setService(service);
					List<Parser> wrapperList = path.getParserWrappers();

					if (wrapperList != null && !wrapperList.isEmpty()) {
						for (Parser wrapper : wrapperList) {
							if (!StateEnum.DELETED.equals(wrapper.getStatus())) {
								wrapper.setParsingPathList(path);
								pathlistService.validateWrapperForImport(wrapper, importErrorList);
								// parser validation check for all parser types
								logger.debug("Going to validate parser configuration and mapping details.");
								parserMappingService.validateImportedMappingDetails(wrapper.getParserMapping(), importErrorList);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Method will validate distribution service and its dependents.
	 * @param service
	 * @param importErrorList
	 */
	public void validateDistributionServiceAndDependents(Service distributionService, List<ImportValidationErrors> importErrorList) {
		logger.debug("Validating Distribution service parameters.");
		serviceValidator.validateServiceConfigurationParameter(distributionService, null, importErrorList, true); // It will validate all required service parametes.
		List<Drivers> distributionDriverList = distributionService.getMyDrivers();
		if (distributionDriverList != null && !distributionDriverList.isEmpty()) {
			logger.debug("Found " + distributionDriverList.size() + " drivers for distribution  service  " + distributionService.getName());
			for (Drivers driver : distributionDriverList) {
				driversService.validateDriverForImport(driver, importErrorList); // It will validate driver parameters.
			}

		}

	}

	/**
	 * Method will update distribution service configuration parameters.
	 * @param distributionService
	 * @return ResponseObject
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Service.class, ignorePropList= "")
	public ResponseObject updateDistributionServiceConfiguration(DistributionService distributionService) {

		ServiceSchedulingParams schedulingParam = distributionService.getServiceSchedulingParams();
		schedulingParam.setLastUpdatedByStaffId(distributionService.getLastUpdatedByStaffId());
		schedulingParam.setLastUpdatedDate(new Date());
		serviceSchedulingParamsDao.merge(schedulingParam);

		FileGroupingParameter fileGroupingParameter = distributionService.getFileGroupingParameter();
		fileGroupingParameter.setLastUpdatedByStaffId(distributionService.getLastUpdatedByStaffId());
		fileGroupingParameter.setLastUpdatedDate(new Date());
		
		
		return updateService(distributionService);
	}
	
	
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Service.class, ignorePropList= "consolidation,myDrivers")
	public ResponseObject updateConsolidationServiceConfiguration(DataConsolidationService service) {

		FileGroupingParameter fileGroupParam = service.getFileGroupParam();
		fileGroupParam.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
		fileGroupParam.setLastUpdatedDate(new Date());

		return updateService(service);

	}

	/**
	 * Fetch service type using alias
	 * 
	 * @param serviceTypeAlias
	 * @return
	 */
	@Transactional
	@Override
	public ResponseObject getServiceTypeByAlias(String serviceTypeAlias) {
		ResponseObject responseObject = new ResponseObject();
		ServiceType serviceType = servicetypeDao.getServiceTypeByAlias(serviceTypeAlias);
		if (serviceType != null) {
			responseObject.setSuccess(true);
			responseObject.setObject(serviceType);
		}
		return responseObject;
	}

	/**
	 * Change Service status
	 * 
	 * @throws CloneNotSupportedException
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_STATUS, actionType = BaseConstants.UPDATE_CUSTOM_ACTION, currentEntity = Service.class, ignorePropList= "")
	public ResponseObject updateServiceStatus(int id, String serviceStatus) throws CloneNotSupportedException {

		logger.debug("Going to update service status for id " + id + " to status " + serviceStatus);
		ResponseObject responseObject = new ResponseObject();

		Service service = servicesDao.getServiceWithServerInstanceById(id);

		if (service != null) {
			if (StateEnum.ACTIVE.name().equals(serviceStatus.trim())) {
				service.setStatus(StateEnum.ACTIVE);
			} else {
				service.setStatus(StateEnum.INACTIVE);
			}

			servicesDao.updateServiceDetails(service);
			logger.debug("Service status has been updated successfully.");
			responseObject.setResponseCode(ResponseCode.SERVICE_UPDATE_SUCCESS);
			responseObject.setObject(servicesDao.getServiceWithServerInstanceById(id));
			responseObject.setSuccess(true);

		} else {
			logger.info("Failed to update service status.");
			responseObject.setResponseCode(ResponseCode.SERVICE_UPDATE_FAIL);
			responseObject.setSuccess(false);
		}

		return responseObject;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Service> getServiceList(int serverId) {
		List<Service>	serviceList = servicesDao.getServiceList(serverId);
		return serviceList;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Service> getServiceListToValidate(int serverId) {
		List<Service> serviceList = servicesDao.getServiceListForValidation(serverId);
		return serviceList;
	}
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.services.service.ServicesService#iterateServiceConfigDetails(com.elitecore.sm.services.model.Service)
	 */
	@Override
	@Transactional
	public void iterateServiceConfigDetails(Service service) {
		if(service instanceof CollectionService){
			CollectionService collectionService = (CollectionService) service;
			collectionService.getServiceSchedulingParams().getId();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Service getServiceListByIDAndTypeAlias(String serviceAlias,String serviceId,int serverInstanceId) {
		Service service=null;
		ServiceType serviceType = servicetypeDao.getServiceIDbyAlias(serviceAlias);
		List<Service> svcList= servicesDao.getServiceListByTypeAndServId(serviceType.getId(),serviceId,serverInstanceId);		
		if(svcList!=null && !svcList.isEmpty()){
			logger.debug("Service found!!!!!");
			service=svcList.get(0);
		}
		else{
			logger.debug("Service not found!!!!!");
		}
		return service;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Service getServiceById(int serviceId){
		Service service=null;
		if(serviceId!=0){
		 service = servicesDao.findByPrimaryKey(Service.class, serviceId);
		}
		return service;
	}
	
	/**
	 * Method will get all service list details by selected id's using HQL criteria.
	 * 
	 * @param ids
	 * @return responseObject
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getAllServiceByIds(Integer[] ids) {
		ResponseObject responseObject = new ResponseObject();

		logger.debug("Going to fetch service list");
		List<Service> serviceList = servicesDao.getAllServiceByIds(ids);

		if (serviceList != null && !serviceList.isEmpty()) {
			responseObject.setSuccess(true);
			responseObject.setObject(serviceList);
			logger.info(serviceList.size() + "  Service list found.");
		} else {
			logger.info("Failed to service list for id ");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_SERVICE_BY_IDS);
		}
		return responseObject;
	}

	@Transactional(readOnly = true)
	@Override
	public boolean isUniqueServiceName(int serviceId,String newName,int serverInstanceId){
		String oldName = servicesDao.findByPrimaryKey(Service.class, serviceId).getName();
		int count = 0;
		if (org.apache.commons.lang3.StringUtils.isNotBlank(newName)) {
			count = servicesDao.getServiceCount(newName, serverInstanceId);
		}
		if(!org.apache.commons.lang3.StringUtils.equalsIgnoreCase(oldName, newName) && count>0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Update Diameter Collection service configuration
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Service.class, ignorePropList= "")
	public ResponseObject updateDiameterCollectionServiceConfiguration(DiameterCollectionService service) {
		service.setServiceSchedulingParams(null);
		return updateService(service);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<String> getUnifiedFieldConfigInPluging(int iserviceId){
		List<String> unifiedFieldList = new ArrayList<String>();
		List<PathList> pathListList = pathListDao.getPathListByServiceId(iserviceId);
		if(pathListList!=null && !pathListList.isEmpty()){
			PathList pathList = pathListList.get(0);
			if(pathList!=null){
				Parser parser = parserDao.getParserByPathListId(pathList.getId());
				if(parser!=null){
					ParserMapping parserMapping = parser.getParserMapping();
					if(parserMapping!=null){
						//Start MED-8332:outputFileHeader field suport in regex parser plugin
						if(parserMapping instanceof RegexParserMapping){
							RegexParserMapping regExParserMapping=parserMappingDao.getRegExParserMappingById(parserMapping.getId());
							if (regExParserMapping != null) {
								List<RegExPattern> regExPatternList = regExParserMapping.getPatternList();
								for (RegExPattern regExPattern : regExPatternList) {
									List<RegexParserAttribute> regexParserAttributeList = regExPattern
											.getAttributeList();
									for (RegexParserAttribute regexParserAttribute : regexParserAttributeList) {
										if(regexParserAttribute.getStatus()!=StateEnum.DELETED){
										 unifiedFieldList.add(regexParserAttribute.getUnifiedField());
										}
									}
								}
								// to filter unique list used Set here
								Set<String> uniqueUnifiedFieldList = new HashSet<String>(unifiedFieldList);
								unifiedFieldList.clear();
								unifiedFieldList.addAll(uniqueUnifiedFieldList);
								Collections.sort(unifiedFieldList);
							}
						}else{
						List<ParserAttribute> parserAttributeList = parserMapping.getParserAttributes();
						Collections.sort(parserAttributeList,new ParserAttributeComparator());
						for(ParserAttribute parserAttribute : parserAttributeList){
							if(parserAttribute.getStatus()!=StateEnum.DELETED){
								unifiedFieldList.add(parserAttribute.getUnifiedField());
								try{
									AsciiParserAttribute asciiParserAttribute = (AsciiParserAttribute) parserAttribute;
									if(asciiParserAttribute.getPortUnifiedField()!=null){
										unifiedFieldList.add(asciiParserAttribute.getPortUnifiedField());
									}
								} catch(ClassCastException castException){}//NOSONAR
								
							}
						}
					}
				  }
				}
			}
		}
		return unifiedFieldList;
	}
	
	public boolean isPartitionParamListDeleted(List<PartitionParam> dbPartitionParamList){
		for(PartitionParam param : dbPartitionParamList){
			if(param.getStatus().equals(StateEnum.ACTIVE)){
				return false;
			}
		}
		return true;
	}
	
	
	


	private ResponseObject validateParsingServiceLicense(Service service) throws IOException {
		ResponseObject responseObject = new ResponseObject();
		boolean validLicenseFlag=false;
		String msg="";
		if (StateEnum.ACTIVE.equals(service.getStatus())
				&& service.getSvctype().getTypeOfService().equals(ServiceTypeEnum.MAIN)
				&& (service.getSvctype().getAlias().equals(EngineConstants.PARSING_SERVICE))) {
			
			validLicenseFlag = false;
			
			Map<Integer, Map<String, Object>> map=new LicenseUtility().extractCircleAndDeviceFromLicense(licenseService.getLicenseList(),servletcontext.getRealPath(BaseConstants.LICENSE_PATH) + File.separator);					
			
			List<ParsingPathList> parsingPathLists = pathListDao.getParsingPathListByServiceId(service.getId());
			
			if (parsingPathLists != null && !parsingPathLists.isEmpty()) {
				for (ParsingPathList path : parsingPathLists) {
					if (path.getCircle().getId() != 1) {
						if (map.containsKey(path.getCircle().getId())) {
							Map<String, Object> circleMap=map.get(path.getCircle().getId());
							if( circleMap.containsKey((LicenseTypeEnum.DEVICE).toString())) {
								List<String> devices = (List<String>) circleMap.get(LicenseTypeEnum.DEVICE.toString());
								if (devices.contains(path.getParentDevice().getDeviceType().getName())) {
									validLicenseFlag = true;
								}else {
									msg+="For Parsing service ("+service.getName()+"), please map pathlist ("+path.getName()+") with circle and device that has valid license association "
											+ "OR apply valid license key for selected circle ("+path.getCircle().getName()+") and device ("+path.getParentDevice().getDecodeType()+").";
								}
							}else {
								validLicenseFlag=true;
							}
						} else {
							msg+="For Parsing service ("+service.getName()+"), please map pathlist ("+path.getName()+") with circle that has valid license association "
									+ "OR apply valid license key for selected circle ("+path.getCircle().getName()+").";							
							break;
						}
					} else {
						validLicenseFlag = true;
					}
				}
			} else {
				validLicenseFlag = true;
			}
		}else {
			validLicenseFlag=true;		
		}
		responseObject.setSuccess(validLicenseFlag);
		responseObject.setMsg(msg);;
		return responseObject;
	}
}
