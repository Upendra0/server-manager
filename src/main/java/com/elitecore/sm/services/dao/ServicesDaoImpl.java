package com.elitecore.sm.services.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.elitecore.sm.aggregationservice.model.AggregationServicePathList;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.model.DataConsolidationAttribute;
import com.elitecore.sm.consolidationservice.model.DataConsolidationMapping;
import com.elitecore.sm.diameterpeer.model.DiameterAVP;
import com.elitecore.sm.diameterpeer.model.DiameterPeer;
import com.elitecore.sm.drivers.dao.DriversDao;
import com.elitecore.sm.drivers.model.CollectionDriver;
import com.elitecore.sm.drivers.model.DistributionDriver;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.netflowclient.model.NatFlowProxyClient;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.pathlist.dao.PathListDao;
import com.elitecore.sm.pathlist.model.DataConsolidationPathList;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.pathlist.model.ProcessingPathList;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
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
import com.elitecore.sm.services.model.SearchServices;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.SysLogCollectionService;

/**
 * 
 * @author avani.panchal
 *
 */
@Repository(value="servicesDao")
public class ServicesDaoImpl extends GenericDAOImpl<Service> implements ServicesDao {
	
	@Autowired
	private ServerInstanceDao serverInstanceDao;
	
	@Autowired
	private DriversDao driversDao;
	
	@Autowired
	private PathListDao pathListDao;
	
	@Autowired 
	PartitionParamDao partitionParamDao;

	/**
	 * Fetch service hierarchy , prepare JAXB XML and return
	 * @throws ClassNotFoundException 
	 * @throws JAXBException 
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public Map<String, Object> getServiceJAXBByTypeAndId(int serviceId, String serviceClassName,String jaxbXmlPath , String fileName) throws SMException {

		Class<? extends Service> serviceName = null;
		List<Service> serviceList;
		Map<String, Object> serviceJAXBMap = null;

		logger.info("inside getServiceJAXBByTypeAndId : service class name:" + serviceClassName);
		try {
			serviceName = (Class<? extends Service>) Class.forName(serviceClassName);
		} catch (ClassNotFoundException e1) {
			logger.error("Exception occured ", e1);
			throw new  SMException(e1.getMessage());
		}

		Criteria criteria = getCurrentSession().createCriteria(serviceName);
		criteria.add(Restrictions.eq("id", serviceId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		serviceList = (List<Service>) criteria.list();
		Service serviceDetail = serviceList.get(0);
		
		String inputXmlPath;
			
			if(fileName != null && !"".equals(fileName)){
				inputXmlPath = jaxbXmlPath + File.separator + fileName;
			}else{
				inputXmlPath = jaxbXmlPath + File.separator + serviceDetail.getSvctype().getAlias() + BaseConstants.XML_FILE_EXT;
			}
			
		
		File inputxml = new File(inputXmlPath);
		JAXBContext jaxbContext;
 
		try {
			jaxbContext = JAXBContext.newInstance(serviceName);
		
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(serviceDetail, inputxml);
	}	catch (JAXBException e1) {
		logger.error("Exception occured ", e1);
		throw new  SMException(e1.getMessage());
		}
		 String fileContent = null;
		 
		try(BufferedReader br = new BufferedReader(new FileReader(inputxml))) {
		
		 StringBuilder sb=new StringBuilder();
		 while ((fileContent = br.readLine()) != null) {
			 sb.append(fileContent);
		 }
		 logger.debug("JAXB XML is"+sb.toString());

		serviceJAXBMap = new HashMap<>();
		serviceJAXBMap.put(BaseConstants.SERVICE_JAXB_FILE, inputxml);
		serviceJAXBMap.put(BaseConstants.SERVICE_JAXB_OBJECT, serviceDetail);
	}catch (Exception e){
		logger.error("chking" , e);
	}
		return serviceJAXBMap;
	}
	
	/**
	 * Mark service own and server instance child flag dirty , then update service
	 */
	@Override
	public void update(Service service){
		
		service.setSyncStatus(false);
		
		serverInstanceDao.markServerInstanceChildFlagDirty(service.getServerInstance());
		
		
	}
	
	@Override
	public void addServiceDetails(Service service) {
		service.setSyncStatus(false);
		serverInstanceDao.markServerInstanceFlagDirty(service.getServerInstance());
		getCurrentSession().save(service);
	}

	@Override
	public void updateServiceDetails(Service service) {
		service.setSyncStatus(false);
		serverInstanceDao.markServerInstanceFlagDirty(service.getServerInstance());
		getCurrentSession().merge(service);
	}
	/**
	 * Mark server instance dirty , then save service
	 */
	@Override
	public void save(Service service){
		
		service.setSyncStatus(false);
		
		serverInstanceDao.markServerInstanceChildFlagDirty(service.getServerInstance());
		
		getCurrentSession().save(service);
	}
	
	/**
	 * Mark server instance dirty , then update service
	 */
	@Override
	public void merge(Service service){
		
		service.setSyncStatus(false);
		
		serverInstanceDao.markServerInstanceChildFlagDirty(service.getServerInstance());
		
		getCurrentSession().merge(service);
		
		getCurrentSession().flush();
		
	}
	
	/**
	 * Reset Sync flag for Service
	 */
	@Override
	public void updateForResetSyncFlagOfService(Service service){
		super.merge(service);
	}
	
	/**
	 * Creates the criteria conditions based on the input.
	 */
	@Override
	public Map<String, Object> createCriteriaConditions(int searchInstanceId) {
		Map<String, Object> returnMap = new HashMap<>();

		HashMap<String, String> aliases = new HashMap<>();

		List<Criterion> conditions = new ArrayList<>();

		if (searchInstanceId != 0) {
			aliases.put(BaseConstants.SERVER_INSTANCE_CONSTANT, "s");
			conditions.add(Restrictions.eq("s.id", searchInstanceId ));
		}
		conditions.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		returnMap.put("aliases", aliases);
		logger.debug("List =" + conditions.toString());
		returnMap.put("conditions", conditions);

		return returnMap;
	}
	
	@SuppressWarnings("unchecked")
	public List<Service> getPaginatedList(Class<Service> instance, List<Criterion> conditions, Map<String,String> aliases, int offset,int limit,String sortColumn,String sortOrder){
		List<Service> resultList;
		Criteria criteria = getCurrentSession().createCriteria(instance);
	
		logger.debug("Sort column ="+sortColumn);
		if("desc".equalsIgnoreCase(sortOrder)){
			if(sortColumn.equals(BaseConstants.SERVICE_TYPE))
				criteria.createAlias(BaseConstants.SVCTYPE,BaseConstants.SVCTYPE).addOrder(Order.desc(BaseConstants.SVCTYPE_TYPE));
			else
				criteria.addOrder(Order.desc(sortColumn));
		}
		else if("asc".equalsIgnoreCase(sortOrder)){
			if(sortColumn.equals(BaseConstants.SERVICE_TYPE))
				criteria.createAlias(BaseConstants.SVCTYPE,BaseConstants.SVCTYPE).addOrder(Order.asc(BaseConstants.SVCTYPE_TYPE));
			else
				criteria.addOrder(Order.asc(sortColumn));
		}

		if(conditions!=null){
			for(Criterion condition : conditions){
				criteria.add(condition);
			}
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		if(aliases!=null){
			for (Entry<String, String> entry : aliases.entrySet()){
				criteria.createAlias(entry.getKey(), entry.getValue());
			}
		}

		criteria.setFirstResult(offset);//first record is 2
		criteria.setMaxResults(limit);//records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}
	
	/**
	 * Fetch Server Instance from db based on service Id
	 */
	@Override
	public Service getServiceWithServerInstanceById(int serviceId){
		Service service=findByPrimaryKey(Service.class,serviceId);
		if(service!=null){
			service.getServerInstance();
		}
		return service;
	}
	
	/**
	 * Fetch only specific field from service for sync
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Service> getServicesforServerInstance(int serverInsId) {
		Criteria criteria = getCurrentSession().createCriteria(Service.class);
		if(serverInsId!=0){
			criteria.add(Restrictions.eq(BaseConstants.SERVERINSTANCE_ID, serverInsId));	
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.addOrder(Order.asc("createdDate"));

		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.property("id").as("id"));
		projections.add(Projections.property("name").as("name"));
		projections.add(Projections.property(BaseConstants.STATUS).as(BaseConstants.STATUS));
		projections.add(Projections.property(BaseConstants.SYNC_STATUS).as(BaseConstants.SYNC_STATUS));
		projections.add(Projections.property(BaseConstants.SVCTYPE).as(BaseConstants.SVCTYPE));
		projections.add(Projections.property(BaseConstants.SERVER_INSTANCE_CONSTANT).as(BaseConstants.SERVER_INSTANCE_CONSTANT));
		projections.add(Projections.property("servInstanceId").as("servInstanceId"));
		projections.add(Projections.property("enableFileStats").as("enableFileStats"));
		projections.add(Projections.property("enableDBStats").as("enableDBStats"));

		criteria.setProjection(projections);
		criteria.setResultTransformer(Transformers.aliasToBean(Service.class));
		
		return criteria.list();
	}
	
	/**
	 * Fetch only specific field from service for sync
	 */
	@Override
	public String getMaxServiceInstanceIdforServerInstance(int serverInsId, int serviceType) {

		Criteria criteria = getCurrentSession().createCriteria(Service.class);

		if(serverInsId!=0){
			criteria.add(Restrictions.eq(BaseConstants.SERVERINSTANCE_ID, serverInsId));	
			criteria.add(Restrictions.eq(BaseConstants.SVCTYPE_ID, serviceType));
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.setProjection(Projections.max("servInstanceId"));
		
		return (String) criteria.uniqueResult();
	}
	
	@Override
	public Service getServiceByServiceInstanceId(int serverInsId, int serviceType, String servInstanceId) {

		Criteria criteria = getCurrentSession().createCriteria(Service.class);

		if(serverInsId!=0){
			criteria.add(Restrictions.eq(BaseConstants.SERVERINSTANCE_ID, serverInsId));	
			criteria.add(Restrictions.eq(BaseConstants.SVCTYPE_ID, serviceType));
			criteria.add(Restrictions.eq(BaseConstants.SERVINSTANCEID, servInstanceId));
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		if(!CollectionUtils.isEmpty(criteria.list())){
			return (Service)criteria.list().get(0);
		} else {
			return null;
		}
	
	}
	
	/**
	 * Fetch Service count based on name , for check unique service name
	 */
	@Override
	public int getServiceCount(String serviceName,int serverInstanceId){
				
		Criteria criteria = getCurrentSession().createCriteria(Service.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		if (org.apache.commons.lang3.StringUtils.isNotBlank(serviceName)) {
			criteria.add(Restrictions.eq("name", serviceName));
		}
		criteria.add(Restrictions.eq(BaseConstants.SERVERINSTANCE_ID, serverInstanceId));
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
	}
	
	/**
	 * Fetch Service Detail from service id
	 */
	@Override
	public Service getAllServiceDepedantsByServiceId(int serviceId){
		
		Service service = findByPrimaryKey(Service.class,serviceId);
		if(service instanceof NetflowCollectionService){
			// do nothing as there is no dependent
		} else if (service instanceof SysLogCollectionService){
			// do nothing as there is no dependent
		} else if (service instanceof MqttCollectionService){	
			// do nothing as there is no dependent
		} else if (service instanceof CoAPCollectionService){	
			// do nothing as there is no dependent
		} else if (service instanceof Http2CollectionService){	
			// do nothing as there is no dependent
		} else if (service instanceof NetflowBinaryCollectionService){
		 // do nothing as there is no dependent
		} else if(service instanceof CollectionService){
			
			iterateOverCollectionService( (CollectionService) service);
			
		} else if(service instanceof IPLogParsingService){

			logger.debug("Iterage over iplog parsing service");
			iterateOverIPLogParsingService( (IPLogParsingService) service);
			
		}  else if(service instanceof ParsingService){

			logger.debug("Iterage over parsing service");
			iterateOverParsingService((ParsingService) service);
			
		} else if(service instanceof DistributionService){
			logger.debug("Fetching distribution service configuration detailed parameters.");
			iterateOverDistributionSErvice((DistributionService)service);
		} else if(service instanceof ProcessingService){
			logger.debug("Fetching Processing service configuration detailed parameters.");
			iterateOverProcessingService((ProcessingService)service);
		} else if(service instanceof AggregationService){
			logger.debug("Fetching Aggregation service configuration detailed parameters.");
			iterateOverAggregationService((AggregationService)service);
		}
		return service;
	}
	
	
	/**
	 * Method will  fetch service list by all search parameters.
	 * @see com.elitecore.sm.services.dao.ServicesDao#getServiceBySearchParameters(com.elitecore.sm.services.model.Service)
	 * @return Map
	 */
	@Override
	public Map<String, Object> getServiceBySearchParameters(SearchServices service) {
		logger.debug(">> getServiceBySearchParameters in ServicesDaoImpl "  +service);
		
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		
		if (!StringUtils.isEmpty(service.getServiceInstanceName() )) {
			conditionList.add(Restrictions.like("name", "%" + service.getServiceInstanceName().trim() + "%").ignoreCase());
		}
		
		if (!service.getServiceId().equals("0")) {
			conditionList.add(Restrictions.eq(BaseConstants.SERV_INSTANCE_ID, service.getServiceId()));
		}
		
		if (!StringUtils.isEmpty(service.getSyncStatus()) && !"undefined".equals(service.getSyncStatus())) {
			conditionList.add(Restrictions.eq(BaseConstants.SYNC_STATUS, Boolean.parseBoolean(service.getSyncStatus().trim())));
		}
		
		if (!StringUtils.isEmpty(service.getServiceType() ) && !"-1".equals(service.getServiceType())) {
			aliases.put(BaseConstants.SVCTYPE, "s");
			conditionList.add(Restrictions.eq("s.alias", service.getServiceType().trim()));  
		}
		
		if (!StringUtils.isEmpty(service.getServerInstanceName())) {
			aliases.put(BaseConstants.SERVER_INSTANCE_CONSTANT, "si");
			conditionList.add(Restrictions.like("si.name", "%" + service.getServerInstanceName().trim() + "%"));
		}
		
		conditionList.add(Restrictions.ne(BaseConstants.STATUS,StateEnum.DELETED));
		
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		
		logger.debug("<< getServiceBySearchParameters in ServicesDaoImpl ");
		return returnMap;
	}
	

	/**
	 * Fetch Service Full Herarchy
	 * @param serviceId
	 * @return Service
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Service getServicefullHierarchyWithoutMarshlling(int serviceId,String serviceClassName) throws SMException{
		logger.debug("Inside getServicefullHierarchyWithoutMarshlling for Service Id" +serviceId );
		Service serviceDetail=null;
		try{
			Class<? extends Service> serviceName;

			logger.info("inside getServiceJAXBByTypeAndId : service class name:" + serviceClassName);
			serviceName = (Class<? extends Service>) Class.forName(serviceClassName);

			
			Criteria criteria = getCurrentSession().createCriteria(serviceName);
			criteria.add(Restrictions.eq("id", serviceId));
			criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

			List<Service> serviceList = (List<Service>) criteria.list();
			serviceDetail = serviceList.get(0);
					
			if(serviceDetail!=null){
				Hibernate.initialize(serviceDetail.getServerInstance());
				Hibernate.initialize(serviceDetail.getSvctype());
				
				if(serviceDetail instanceof NetflowCollectionService){
					iterateOverNetflowCollectionService((NetflowCollectionService)serviceDetail);
				} else if (serviceDetail instanceof DiameterCollectionService){
					iterateOverDiameterCollectionService((DiameterCollectionService)serviceDetail);
				} else if (serviceDetail instanceof SysLogCollectionService){
					iterateOverSysLogCollectionService((SysLogCollectionService)serviceDetail);
				} else if (serviceDetail instanceof MqttCollectionService){
					iterateOverMqttCollectionService((MqttCollectionService)serviceDetail);
				} else if (serviceDetail instanceof CoAPCollectionService){
					iterateOverCoAPCollectionService((CoAPCollectionService)serviceDetail);
				} else if (serviceDetail instanceof Http2CollectionService){
					iterateOverHttp2CollectionService((Http2CollectionService)serviceDetail);
				} else if (serviceDetail instanceof NetflowBinaryCollectionService){
					iterateOverNetFlowBinaryCollectionService((NetflowBinaryCollectionService)serviceDetail);
				}else if(serviceDetail instanceof CollectionService){
					iterateOverCollectionService((CollectionService)serviceDetail);
				}else if(serviceDetail instanceof ParsingService){
					iterateOverParsingService((ParsingService)serviceDetail);
				}else if(serviceDetail instanceof IPLogParsingService){
					iterateOverIPLogParsingService((IPLogParsingService)serviceDetail);
				}else if(serviceDetail instanceof DistributionService){
					iterateOverDistributionSErvice((DistributionService)serviceDetail);
				}else if(serviceDetail instanceof ProcessingService){
					iterateOverProcessingService((ProcessingService)serviceDetail);
				}else if(serviceDetail instanceof DataConsolidationService){
					iterateOverDataConsolidationService((DataConsolidationService)serviceDetail);
				}else if(serviceDetail instanceof AggregationService){
					iterateOverAggregationService((AggregationService)serviceDetail);
				}
				
				if(serviceDetail.getMyDrivers()!=null){
					List<Drivers> driverList=serviceDetail.getMyDrivers();
					Iterator<Drivers> itr = driverList.iterator();

					while(itr.hasNext()){
						Drivers driver = itr.next();
						if(!StateEnum.DELETED.equals(driver.getStatus())){
							Hibernate.initialize(driver.getDriverType());
							if(driver instanceof CollectionDriver){
								logger.debug("Going to fetch CollectionDriver detail " );
								driversDao.irerateOverCollectionDriver((CollectionDriver)driver);
							}else if(driver instanceof DistributionDriver){
								logger.debug("Going to fetch DistributionDriver detail " );
								driversDao.irerateOverDistributionDriver((DistributionDriver)driver);
							}
						}else{
							itr.remove();
						}
					}
					serviceDetail.setMyDrivers(driverList);
				}
				
				if(serviceDetail.getSvcPathList()!=null){
					List<PathList> pathListDetail=serviceDetail.getSvcPathList();
					Iterator<PathList> itrPathList = pathListDetail.iterator();
					while(itrPathList.hasNext()){
						PathList pathlist = itrPathList.next();
						if(!StateEnum.DELETED.equals(pathlist.getStatus()) && pathlist instanceof ParsingPathList){
								logger.debug("Going to fetch ParsingPathList detail " );
								pathListDao.irerateOverParsingPathList((ParsingPathList)pathlist);
						}
						else if(!StateEnum.DELETED.equals(pathlist.getStatus()) && pathlist instanceof ProcessingPathList){
							if(((ProcessingPathList)pathlist).getPolicy() != null){
								String aliasName = ((ProcessingPathList)pathlist).getPolicy().getAlias();
								((ProcessingPathList)pathlist).setPolicyAlias(aliasName);
							}else{
								((ProcessingPathList)pathlist).setPolicyAlias("");
							}
						}
						else if(!StateEnum.DELETED.equals(pathlist.getStatus()) && pathlist instanceof DataConsolidationPathList){
							//Stuff Specific For Consolidation Path List
						}
						else if(!StateEnum.DELETED.equals(pathlist.getStatus()) && pathlist instanceof AggregationServicePathList){
							//Stuff Specific For Aggregation Path List
						}
						else{
							itrPathList.remove();
						}
					}
					serviceDetail.setSvcPathList(pathListDetail);
				}
			}
		}catch(Exception e){
			throw new SMException(e);
		}
		
		
		return serviceDetail;
		
	}
	
	/**
	 * iterate over Netflow Collection service object and fetch its depedants
	 * @param netflowCollSvc
	 */
	public void iterateOverNetflowCollectionService(NetflowCollectionService netflowCollSvc){
		logger.debug("Iterate over NetflowCollectionService and fetch client detail" );
		if(netflowCollSvc !=null){
			Hibernate.initialize(netflowCollSvc.getNetFLowClientList());	

			List<NetflowClient> clients=netflowCollSvc.getNetFLowClientList();
			Iterator<NetflowClient> itr = clients.iterator();

			while(itr.hasNext()){
				NetflowClient client = itr.next();
				if(StateEnum.DELETED.equals(client.getStatus())){
					itr.remove();
				}
			}
			netflowCollSvc.setNetFLowClientList(clients);
			
			Hibernate.initialize(netflowCollSvc.getNatFlowProxyClients());
			List<NatFlowProxyClient> natFlowProxyClients=netflowCollSvc.getNatFlowProxyClients();
			Iterator<NatFlowProxyClient> iterator = natFlowProxyClients.iterator();
			while(iterator.hasNext()) {
				NatFlowProxyClient natFlowProxyClient = iterator.next();
				if(StateEnum.DELETED.equals(natFlowProxyClient.getStatus()))
					iterator.remove();
			}
			netflowCollSvc.setNatFlowProxyClients(natFlowProxyClients);
		}
	}
	
	/**
	 * iterate over Diameter Collection Service object and fetch its depedants
	 * @param diameterCollSvc
	 */
	public void iterateOverDiameterCollectionService(DiameterCollectionService diameterCollSvc){
		logger.debug("Iterate over DiameterCollectionService and fetch peer detail" );
		if(diameterCollSvc !=null){
			Hibernate.initialize(diameterCollSvc.getDiameterPeerList());	

			List<DiameterPeer> peers=diameterCollSvc.getDiameterPeerList();
			Iterator<DiameterPeer> itr = peers.iterator();

			while(itr.hasNext()){
				DiameterPeer peer = itr.next();
				if(StateEnum.DELETED.equals(peer.getStatus())){
					itr.remove();
				}
				else
				{
					Hibernate.initialize(peer.getDiameterAVPs());	
					List<DiameterAVP> avps= peer.getDiameterAVPs();
					Iterator<DiameterAVP> itravp = avps.iterator();
					while(itravp.hasNext()){
						DiameterAVP avp = itravp.next();
						if(StateEnum.DELETED.equals(avp.getStatus())){
							itravp.remove();
						}
					}
					peer.setDiameterAVPs(avps);
				}
			}
			diameterCollSvc.setDiameterPeerList(peers);
		}
	}
	
	/**
	 * iterate over syslog Collection service object and fetch its dependents
	 * @param sysLogCollSvc
	 */
	public void iterateOverSysLogCollectionService(SysLogCollectionService sysLogCollSvc){
		logger.debug("Iterate over SysLogCollectionService and fetch client detail" );
		if(sysLogCollSvc  !=null){
			Hibernate.initialize(sysLogCollSvc.getNetFLowClientList());
			
			List<NetflowClient> clients=sysLogCollSvc.getNetFLowClientList();
			Iterator<NetflowClient> itr = clients.iterator();

			while(itr.hasNext()){
				NetflowClient client = itr.next();
				if(StateEnum.DELETED.equals(client.getStatus())){
					itr.remove();
				}
			}
			sysLogCollSvc.setNetFLowClientList(clients);
		}
	}
	
	/**
	 * iterate over mqtt Collection service object and fetch its dependents
	 * @param sysLogCollSvc
	 */
	public void iterateOverMqttCollectionService(MqttCollectionService sysLogCollSvc){
		logger.debug("Iterate over SysLogCollectionService and fetch client detail" );
		if(sysLogCollSvc  !=null){
			Hibernate.initialize(sysLogCollSvc.getNetFLowClientList());
			
			List<NetflowClient> clients=sysLogCollSvc.getNetFLowClientList();
			Iterator<NetflowClient> itr = clients.iterator();

			while(itr.hasNext()){
				NetflowClient client = itr.next();
				if(StateEnum.DELETED.equals(client.getStatus())){
					itr.remove();
				}
			}
			sysLogCollSvc.setNetFLowClientList(clients);
		}
	}
	
	/**
	 * iterate over coap Collection service object and fetch its dependents
	 * @param coapCollSvc
	 */
	public void iterateOverCoAPCollectionService(CoAPCollectionService coapCollSvc){
		logger.debug("Iterate over CoAPCollectionService and fetch client detail" );
		if(coapCollSvc  !=null){
			Hibernate.initialize(coapCollSvc.getNetFLowClientList());
			
			List<NetflowClient> clients=coapCollSvc.getNetFLowClientList();
			Iterator<NetflowClient> itr = clients.iterator();

			while(itr.hasNext()){
				NetflowClient client = itr.next();
				if(StateEnum.DELETED.equals(client.getStatus())){
					itr.remove();
				}
			}
			coapCollSvc.setNetFLowClientList(clients);
		}
	}
	
	/**
	 * iterate over http2 Collection service object and fetch its dependents
	 * @param http2CollSvc
	 */
	public void iterateOverHttp2CollectionService(Http2CollectionService http2CollSvc){
		logger.debug("Iterate over Http2CollectionService and fetch client detail" );
		if(http2CollSvc  !=null){
			Hibernate.initialize(http2CollSvc.getNetFLowClientList());
			
			List<NetflowClient> clients=http2CollSvc.getNetFLowClientList();
			Iterator<NetflowClient> itr = clients.iterator();

			while(itr.hasNext()){
				NetflowClient client = itr.next();
				if(StateEnum.DELETED.equals(client.getStatus())){
					itr.remove();
				}
			}
			http2CollSvc.setNetFLowClientList(clients);
		}
	}
	
	/**
	 * iterate over Netflow Binary Collection service object and fetch its dependents
	 * @param netflowBinaryCollSvc
	 */
	public void iterateOverNetFlowBinaryCollectionService(NetflowBinaryCollectionService netflowBinaryCollSvc){
		logger.debug("Iterate over NetflowBinaryCollectionService and fetch client detail" );
		if(netflowBinaryCollSvc !=null){
			Hibernate.initialize(netflowBinaryCollSvc.getNetFLowClientList());
			List<NetflowClient> clients=netflowBinaryCollSvc.getNetFLowClientList();
			Iterator<NetflowClient> itr = clients.iterator();

			while(itr.hasNext()){
				NetflowClient client = itr.next();
				if(StateEnum.DELETED.equals(client.getStatus())){
					itr.remove();
				}
			}
			netflowBinaryCollSvc.setNetFLowClientList(clients);
			
		}
	}
	
	/**
	 * iterate over collection service object and fetch its depedants
	 * @param service
	 */
	private void iterateOverCollectionService(CollectionService service){
		logger.debug("Iterate over CollectionService and fetch service scheduling param " );
		if(service!=null){
			Hibernate.initialize(service.getServiceSchedulingParams());	
		}
		
	}
	
	
	/**
	 * iterate over IPLog parsing service object and fetch its depedants
	 * @param service
	 */
	private void iterateOverIPLogParsingService(IPLogParsingService service){
		logger.debug("Iterate over IPLogParsingService and fetch file grouping param and partition param " );
		
		if(service!=null){
			
			Hibernate.initialize(service.getFileGroupingParameter());
			if(service.getId() > 0){
				  service.setPartionParamList(partitionParamDao.getAllParamByServiceId(service.getId()));
			}
		}
	}
	
	/**
	 * iterate over Parsing service object and fetch its dependents
	 * @param service
	 */
	private void iterateOverParsingService(ParsingService service){
		logger.debug("Iterate over ParsingService and fetch file grouping param" );
		
		if(service!=null){
			Hibernate.initialize(service.getFileGroupingParameter());
		}
	}
	
	/**
	 * Method will get Distribution service configuration parameters.
	 * @param distributionService
	 */
	private void iterateOverDistributionSErvice(DistributionService distributionService){
		
		if(distributionService != null){
			logger.info("Fetching distribution service scheduling parameters and file grouping params.");
			Hibernate.initialize(distributionService.getServiceSchedulingParams());	
			Hibernate.initialize(distributionService.getFileGroupingParameter());
		}
	}
	
	/**
	 * Method will get Processing service configuration parameters which are Lazy.
	 * @param processingService
	 */
	private void iterateOverProcessingService(ProcessingService processingService){
		
		if(processingService != null){
			logger.info("Fetching processing service filegrouping parameters.");
			Hibernate.initialize(processingService.getFileGroupingParameter());	
			Hibernate.initialize(processingService.getServerInstance());
			Hibernate.initialize(processingService.getSvcPathList());
		}
	}
	

	/**
	 * Method will get Aggregation service configuration parameters which are Lazy.
	 * @param processingService
	 */
	private void iterateOverAggregationService(AggregationService aggregationService){
		
		if(aggregationService != null){
			logger.info("Fetching aggregation service related lazy parameters.");
			Hibernate.initialize(aggregationService.getServerInstance());
			Hibernate.initialize(aggregationService.getSvcPathList());
			Hibernate.initialize(aggregationService.getAggregationDefinition());
			if(aggregationService.getAggregationDefinition() != null){
				Hibernate.initialize(aggregationService.getAggregationDefinition().getAggAttrList());
				Hibernate.initialize(aggregationService.getAggregationDefinition().getAggConditionList());
				Hibernate.initialize(aggregationService.getAggregationDefinition().getAggKeyAttrList());
			}
		}
		
	}
	
	/**
	 * Method will get Processing service configuration parameters which are Lazy.
	 * @param consolidataionService
	 */
	private void iterateOverDataConsolidationService(DataConsolidationService consolidataionService){
		
		if(consolidataionService != null){
			logger.info("Fetching processing service filegrouping parameters.");
			Hibernate.initialize(consolidataionService.getServerInstance());
			Hibernate.initialize(consolidataionService.getSvcPathList());
			Hibernate.initialize(consolidataionService.getConsolidation());
			
			List<DataConsolidation> consolidationList = consolidataionService.getConsolidation();
			Iterator<DataConsolidation> itr = consolidationList.iterator();

			while(itr.hasNext()){
				DataConsolidation consolidation = itr.next();
				if(StateEnum.DELETED.equals(consolidation.getStatus())){
					itr.remove();
				} else {
					List<DataConsolidationAttribute> consAttList = consolidation.getConsAttList();
					Iterator<DataConsolidationAttribute> consAttListItr = consAttList.iterator();
					
					while(consAttListItr.hasNext()){
						DataConsolidationAttribute attribute = consAttListItr.next();
						if(StateEnum.DELETED.equals(attribute.getStatus())){
							consAttListItr.remove();
						}
					}
				}
			}
			consolidataionService.setConsolidation(consolidationList);
			
			List<PathList> pathList = consolidataionService.getSvcPathList();
			Iterator<PathList> pathItr = pathList.iterator();

			while(pathItr.hasNext()){
				PathList path = pathItr.next();
				if(path != null && path instanceof DataConsolidationPathList) {
					DataConsolidationPathList dataConsolidationPath = (DataConsolidationPathList) path;
					if(StateEnum.DELETED.equals(dataConsolidationPath.getStatus())){
						pathItr.remove();
					} else {
						List<DataConsolidationMapping> mappingList = dataConsolidationPath.getConMappingList();
						Iterator<DataConsolidationMapping> mappingItr = mappingList.iterator();
						while(mappingItr.hasNext()){
							DataConsolidationMapping mapping = mappingItr.next();
							if(StateEnum.DELETED.equals(mapping.getStatus())){
								mappingItr.remove();
							}
						}
					}
				}
				
			}
			
			consolidataionService.setSvcPathList(pathList);
		}
	}
	
	/**
	 * Fetch only specific field from service for snmp service threshold
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Service> getServiceForSNMPServiceThreshold(int serviceId) {
		
		Criteria criteria = getCurrentSession().createCriteria(Service.class);

		if(serviceId!=0){
			criteria.add(Restrictions.eq("id", serviceId));	
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		ProjectionList projections = Projections.projectionList();

		projections.add(Projections.property("id").as("id"));
		projections.add(Projections.property("name").as("name"));
		projections.add(Projections.property(BaseConstants.STATUS).as(BaseConstants.STATUS));
		projections.add(Projections.property(BaseConstants.SVCTYPE).as(BaseConstants.SVCTYPE));

		criteria.setProjection(projections);
		criteria.setResultTransformer(Transformers.aliasToBean(Service.class));
		
		return  criteria.list();
		
	}
	
	/**
	 * Get the list of all the Snmp Server
	 * 
	 * @param classInstance
	 * @param conditions
	 * @param aliases
	 * @param offset
	 * @param limit
	 * @param sortColumn
	 * @param sortOrder
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Service> getServicesPaginatedList(Class<Service> classInstance, List<Criterion> conditions,
			Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder) {

		List<Service> resultList;
		Criteria criteria = getCurrentSession().createCriteria(classInstance);

		logger.debug("Sort column =" + sortColumn);

		if (("desc").equalsIgnoreCase(sortOrder)) {
			if (("name").equals(sortColumn)) {
				criteria.addOrder(Order.desc("name"));
			} else if (("id").equals(sortColumn)) {
				criteria.addOrder(Order.desc("id"));
			}
		 else if ((BaseConstants.SVCTYPE_TYPE).equals(sortColumn)) {
			criteria.addOrder(Order.desc(BaseConstants.SVCTYPE_ID));
		}
		 else if (("serverInstance.name").equals(sortColumn)) {
				criteria.addOrder(Order.desc(BaseConstants.SERVERINSTANCE_ID));
			}
		 else if (("serviceState").equals(sortColumn)) {
				criteria.addOrder(Order.desc(BaseConstants.STATUS));
			}
			else if ((BaseConstants.SYNC_STATUS).equals(sortColumn)) {
				criteria.addOrder(Order.desc(BaseConstants.SYNC_STATUS));
			}else {
				criteria.addOrder(Order.desc(sortColumn));
			}
		} else if (("asc").equalsIgnoreCase(sortOrder)) {
			if (("name").equals(sortColumn)) {
				criteria.addOrder(Order.asc("name"));
			} else if ("id".equals(sortColumn)) {
				criteria.addOrder(Order.asc("id"));
			} else if ((BaseConstants.SVCTYPE_TYPE).equals(sortColumn)) {
				criteria.addOrder(Order.asc(BaseConstants.SVCTYPE_ID));
			}
			else if (("serverInstance.name").equals(sortColumn)) {
				criteria.addOrder(Order.asc(BaseConstants.SERVERINSTANCE_ID));
			}
			else if (("serviceState").equals(sortColumn)) {
				criteria.addOrder(Order.asc(BaseConstants.STATUS));
			}
			else if ((BaseConstants.SYNC_STATUS).equals(sortColumn)) {
				criteria.addOrder(Order.asc(BaseConstants.SYNC_STATUS));
			}
			
			
			else {
				criteria.addOrder(Order.asc(sortColumn));
			}
		}

		if (conditions != null) {
			for (Criterion condition : conditions) {
				criteria.add(condition);
			}
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		

		if (aliases != null) {
			for (Entry<String, String> entry : aliases.entrySet()) {
				criteria.createAlias(entry.getKey(), entry.getValue());
			}
		}

		criteria.setFirstResult(offset);// first record is 2
		criteria.setMaxResults(limit);// records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Service> getServiceList(int serverId) {
		
		List<Service> serviceList;
			Criteria criteria = getCurrentSession().createCriteria(Service.class);
			criteria.add(Restrictions.eq("serverInstance.id", serverId));
			serviceList = criteria.list();
			
		return serviceList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Service> getServiceListForValidation(int serverId) {
		
		List<Service> serviceList;
			Criteria criteria = getCurrentSession().createCriteria(Service.class);
			criteria.add(Restrictions.eq("serverInstance.id", serverId));
			criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
			serviceList = criteria.list();
			loadServiceListForValidation(serviceList);
		return serviceList;
	}
	
	private void loadServiceListForValidation(List<Service> serviceList) {
		for(Service service : serviceList){
			if(service instanceof NetflowBinaryCollectionService){
				Hibernate.initialize(((NetflowBinaryCollectionService) service).getNetFLowClientList());
			}else if(service instanceof NetflowCollectionService){
				Hibernate.initialize(((NetflowCollectionService) service).getNetFLowClientList());
			}else if(service instanceof SysLogCollectionService){
				Hibernate.initialize(((SysLogCollectionService) service).getNetFLowClientList());
			}else if(service instanceof GTPPrimeCollectionService){
				Hibernate.initialize(((GTPPrimeCollectionService) service).getNetFLowClientList());
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Service> getServerListByType(int serviceTypeId){
		List<Service> serviceList;
		Criteria criteria = getCurrentSession().createCriteria(Service.class);
		criteria.add(Restrictions.eq(BaseConstants.SVCTYPE_ID, serviceTypeId));
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		serviceList = criteria.list();
		return serviceList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Service> getServiceListByTypeAndServId(int serviceTypeId,String servId,int serverInstanceId){
		Criteria criteria;
		if(serviceTypeId == 3) {// as per master entry, 3 is processing service used for import
			criteria = getCurrentSession().createCriteria(ProcessingService.class);
		} else {
			criteria = getCurrentSession().createCriteria(Service.class);
		}
		
		if(serverInstanceId!=0){
			criteria.add(Restrictions.eq(BaseConstants.SERVERINSTANCE_ID, serverInstanceId));	
		}
		criteria.add(Restrictions.eq(BaseConstants.SVCTYPE_ID, serviceTypeId));
		criteria.add(Restrictions.eq("servInstanceId", servId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		return  criteria.list();
	}
	
	/**
	 * Method will get all service list by list of ids
	 * @param ids
	 * @return List<Service>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Service> getAllServiceByIds(Integer[] ids) {
		logger.debug(">>  getAllServiceByIds ");
		
		Criteria criteria = getCurrentSession().createCriteria(Service.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		List<Service> serviceList = null;
		if(ids != null && ids.length > 0){
			criteria.add(Restrictions.in("id", ids));
			serviceList = criteria.list();
			if(serviceList != null && !serviceList.isEmpty()) {
				int serviceLength = serviceList.size();
				for(int i = serviceLength-1; i >= 0; i--) {
					loadServiceForErrorReprocess(serviceList.get(i));
				}
			}
			return serviceList;
		}else{
			return serviceList;
		}
	}
	
	private void loadServiceForErrorReprocess(Service service) {
		Hibernate.initialize(service.getSvcPathList());
		
		
		if(service instanceof ParsingService) {
			List<PathList> pathList = service.getSvcPathList();
			int pathListLength = pathList.size();
			for(int i = pathListLength-1; i >= 0; i--) {
				if(pathList.get(i) != null && pathList.get(i) instanceof ParsingPathList) {
					Hibernate.initialize(((ParsingPathList) pathList.get(i)).getParserWrappers());
					List<Parser> parserList = ((ParsingPathList)pathList.get(i)).getParserWrappers();
					if(parserList != null && !parserList.isEmpty()){
						for(Parser parser: parserList){
							Hibernate.initialize(parser.getParserType().getAlias());
						}
					}
				}
			}
		} else if(service instanceof DistributionService){
			Hibernate.initialize(service.getMyDrivers());
			List<Drivers> driverList = service.getMyDrivers();
			if(driverList != null && !driverList.isEmpty()) {
				int driverLength = driverList.size();
				for(int i = driverLength-1; i >= 0; i--) {
					Drivers driver = driverList.get(i);
					if(driver != null) {
						Hibernate.initialize(driver.getDriverPathList());
						List<PathList> pathList = driver.getDriverPathList();
						if(pathList != null && !pathList.isEmpty()) {
							int pathListLength = pathList.size();
							for(int j = pathListLength-1; j >= 0; j--) {
								PathList path = pathList.get(j);
								if(path != null && path instanceof DistributionDriverPathList) {
									DistributionDriverPathList distributionDriverPathList = (DistributionDriverPathList) path;
									Hibernate.initialize(distributionDriverPathList.getComposerWrappers());
									List<Composer> composerList = distributionDriverPathList.getComposerWrappers();
									if(composerList != null && !composerList.isEmpty()) {
										int composerLength = composerList.size();
										for(int k = composerLength-1; k >= 0; k--) {
											Composer composer = composerList.get(k);
											if(composer != null) {
												Hibernate.initialize(composer.getComposerType().getAlias());
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} else if(service instanceof CollectionService){
			Hibernate.initialize(service.getMyDrivers());
			List<Drivers> driverList = service.getMyDrivers();
			
			if(CollectionUtils.isEmpty(driverList)){
				return;
			}
			int driverLength = driverList.size();
			for(int i = driverLength-1; i >= 0; i--) {
				Drivers driver = driverList.get(i);
				if(driver==null){
					continue;
				}
				Hibernate.initialize(driver.getDriverPathList());
			}
		}
	}
}
