/**
 * 
 */
package com.elitecore.sm.test;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.common.model.FileActionParamEnum;
import com.elitecore.sm.common.model.FileFetchTypeEnum;
import com.elitecore.sm.common.model.FileGroupEnum;
import com.elitecore.sm.common.model.FileTransferModeEnum;
import com.elitecore.sm.common.model.FilterActionEnum;
import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.drivers.model.ConnectionParameter;
import com.elitecore.sm.drivers.model.DriverType;
import com.elitecore.sm.drivers.model.FTPCollectionDriver;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.pathlist.model.CollectionDriverPathList;
import com.elitecore.sm.pathlist.model.FileFetchParams;
import com.elitecore.sm.pathlist.model.FileGroupingParameter;
import com.elitecore.sm.pathlist.model.FileGroupingParameterCollection;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.serverinstance.model.LogsDetail;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.CollectionService;
import com.elitecore.sm.services.model.NetflowCollectionService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceExecutionParams;
import com.elitecore.sm.services.model.ServiceSchedulingParams;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.snmp.model.SnmpAlertCustomObject;

/**
 * @author vandana.awatramani
 *
 */
@SuppressWarnings( {"unused","unchecked"})
public class TestCollectionService {

	public static void main(String args[]) {
		try {
			int serverInsId = 1;
			//serverInsId = createServerInstance(serverInsId);
			System.out.println("TestCollectionService.main()");

			/*String svcId = createCollSvcPage1(serverInsId);

			System.out.println(createCollSvcPage2(svcId));
			System.out.println(createCollSvcPage3(svcId));*/
			
		//	int svcId = addNetFlowServicePage1(serverInsId);
			
			
		//	List<ProfileEntity> profileEntity=testEntityProfiling();
		//	displayResult(profileEntity);
		//	System.out.println(createNetFlowPart2(svcId));
			getAlertListWithThreshold();
			
		} catch (Exception e) {
			System.out.println("App.main() caught ex ");
			e.printStackTrace();
		} finally {
			// HibernateUtil.shutdown();
			System.exit(1);
		}
	}

	public static int createServerInstance(int serverInsId) {
		System.out.println("TestEntities.createServerInstance()");
		Session session = HibernateUtil.getSessionFactory().openSession();

		ServerInstance serverInsFromDB = (ServerInstance) session.get(ServerInstance.class, serverInsId);
		if (serverInsFromDB != null) {
			return serverInsId;
		} else {

			session.beginTransaction();

			ServerType cgfType = (ServerType) session.get(ServerType.class, "STYP002");

			Server testServer = new Server();
			testServer.setName("test trng Server");
			testServer.setIpAddress("127.0.0.1");
			testServer.setDescription("testing entity defs");
			testServer.setServerType(cgfType);

			DataSourceConfig dsConfig = new DataSourceConfig();
			dsConfig.setConnURL("asfsdf");
			dsConfig.setName("MEDIATION_DS");
			dsConfig.setType("Oracle");
			dsConfig.setUsername("scott");
			dsConfig.setPassword("tiger");

			LogsDetail logsDetail = new LogsDetail();

			// Server connection details and ds config will be read by system
			// before instance creation.

			ServerInstance mySrvInstance = new ServerInstance();

			Agent fileRenameAgent = new Agent();
		//	fileRenameAgent.setTypeOfAgent("FILE_RENAME_AGENT");
			fileRenameAgent.setServerInstance(mySrvInstance);

			List<Agent> agentList = new ArrayList<Agent>();
			agentList.add(fileRenameAgent);
			mySrvInstance.setAgentList(agentList);

			session.save(dsConfig);
			session.save(testServer);

			session.save(mySrvInstance);
			session.save(fileRenameAgent);
			session.getTransaction().commit();
			session.close();
			return mySrvInstance.getId();
		}
	}

	public static int createCollSvcPage1(int serverInstanceId) {
		int svcId = 0;
		System.out.println("TestEntities.createCollSvcPage1()");
		try {

			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			ServerInstance serverInsFromDB = (ServerInstance) session.get(ServerInstance.class, serverInstanceId);
			ServiceType collSvcType = (ServiceType) session.get(ServiceType.class, "STYP0001");

			Service collService = new CollectionService();
			// below are the properties first used in making a service, rest all
			// config is done later
			collService.setSvctype(collSvcType);
			collService.setName("test netflow collection service ddd1");
			collService.setServerInstance(serverInsFromDB);

			List<Service> svcList = null;
			if (serverInsFromDB.getServices() != null) {
				svcList = serverInsFromDB.getServices();

			} else
				svcList = new ArrayList<Service>();

			svcList.add(collService);
			serverInsFromDB.setServices(svcList);
			serverInsFromDB.setSyncChildStatus(false);
			serverInsFromDB.setSyncSIStatus(false);

			serverInsFromDB.getLogsDetail().setLogPathLocation("/home/group1/crestelsetup/crestelpengine/modules/mediation/si_test_logs");

			session.save(serverInsFromDB);
			session.save(collService);
			session.getTransaction().commit();
			session.close();
			svcId = collService.getId();

		} catch (Exception e) {
			e.printStackTrace();

		}
		return svcId;

	}

	public static boolean createCollSvcPage2(String colSvcId) {
		// page of svc config
		// svc startup
		// svc execution params

		System.out.println("TestEntities.createCollSvcPage2()");

		boolean executionStatus = true;
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			CollectionService collSvc = (CollectionService) session.get(CollectionService.class, colSvcId);

			ServiceExecutionParams svcExecParams = new ServiceExecutionParams();
			svcExecParams.setMinThread(10);
			svcExecParams.setMaxThread(10);

			collSvc.setSvcExecParams(svcExecParams);
			// its optional so remove for now

			
			  ServiceSchedulingParams svcSchedule = new		  ServiceSchedulingParams();
			//  svcSchedule.setDate(SchedulingDateEnum.Second.name());
			  svcSchedule.setSchedulingEnabled(true);
			 /* svcSchedule.setSchType(SchedulingTypeEnum.Daily.getValue());
			  svcSchedule.setDay(SchedulingDayEnum.Monday);
			  svcSchedule.setTime(12);*/
			  
			  collSvc.setServiceSchedulingParams(svcSchedule);
			  
			  session.save(svcSchedule);
			 

			session.save(collSvc);

			session.getTransaction().commit();

			session.close();

		} catch (Exception e) {
			e.printStackTrace();
			executionStatus = false;
		}
		return executionStatus;

	}

	public static boolean createCollSvcPage3(String colSvcId) {
		// add driver, file fetch rule, pathlist array
		boolean executionStatus = true;
		System.out.println("TestEntities.createCollSvcPage3()");
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();

			session.beginTransaction();

			CollectionService collService = (CollectionService) session.get(CollectionService.class, colSvcId);

			DriverType ftpDriverType= (DriverType) session.get (DriverType.class, "DTYP0001");

			/*DriverType driverType = new DriverType();
			driverType.setType(CollectionDriverTypeEnum.FTP_COLLECTION_DRIVER.toString());
			driverType.setCategory(DriverCategory.COLLECTION);
			driverType.setDescription("FTP Collection Driver");
			driverType.setAlias(EngineConstants.FTP_COLLECTION_DRIVER);
			driverType.setDriverFullClassName("com.elitecore.sm.drivers.model.collection.FTPCollectionDriver");*/
			
			
			FTPCollectionDriver delhiFTPdriver = new FTPCollectionDriver();
			delhiFTPdriver.setDriverType(ftpDriverType);
			
			ConnectionParameter ftpConnParams = new ConnectionParameter();
			ftpConnParams.setiPAddressHost("192.168.0.146");
			ftpConnParams.setPassword("mediationngt");
			ftpConnParams.setUsername("mediationngt");
			ftpConnParams.setPort(21);
			ftpConnParams.setFileSeparator("/");
		//	ftpConnParams.setFileSeqOrderEnable(true);
			ftpConnParams.setFileTransferMode(FileTransferModeEnum.BINARY);
			ftpConnParams.setTimeout(1000);

			delhiFTPdriver.setFtpConnectionParams(ftpConnParams);

			FileGroupingParameterCollection fileGroup = new FileGroupingParameterCollection();

		//	fileGroup.setArchivePath("abc");
			//fileGroup.setEnableForArchive(true);
			fileGroup.setGroupingType(FileGroupEnum.DAY.getFileGroupEnum());
			fileGroup.setFileGroupEnable(true);

			delhiFTPdriver.setFileGroupingParameter(fileGroup);

			FileFetchParams fetchparam = new FileFetchParams();
			fetchparam.setFileFetchIntervalMin(5);
			fetchparam.setFileFetchRuleEnabled(false);
			fetchparam.setFileFetchType(FileFetchTypeEnum.LOCAL.getValue());
			fetchparam.setTimeZone("GMT");

			delhiFTPdriver.setMyFileFetchParams(fetchparam);

			FileGroupingParameterCollection fileGrpParam = new FileGroupingParameterCollection();
			fileGrpParam.setFileGroupEnable(true);
			fileGrpParam.setGroupingType(FileGroupEnum.DAY.getFileGroupEnum());

			FileFetchParams fileFetchParam = new FileFetchParams();
			fileFetchParam.setFileFetchRuleEnabled(false);

			delhiFTPdriver.setMyFileFetchParams(fileFetchParam);

			delhiFTPdriver.setFileGroupingParameter(fileGrpParam);

			delhiFTPdriver.setName("DelhiSite FTP 44");
			delhiFTPdriver.setService(collService);
			delhiFTPdriver.setApplicationOrder(0);
			delhiFTPdriver.setTimeout(100);
			delhiFTPdriver.setNoFileAlert(1);

			delhiFTPdriver.setMinFileRange(1);
			delhiFTPdriver.setMaxFileRange(25);
			;
			delhiFTPdriver.setFileSeqOrder(true);
			delhiFTPdriver.setMaxRetrycount(3);

			collService.getMyDrivers().add(delhiFTPdriver);

			CollectionDriverPathList colPathList = new CollectionDriverPathList();

			colPathList.setDriver(delhiFTPdriver);
			// colPathList.setService(collService);

			colPathList.setReadFilePath("/home/mediationngt/BackupFiles/Collection_data/collection_input");
			colPathList.setWriteFilePath("/home/group1/crestelsetup/testData/collection_output");
			colPathList.setWriteFilenamePrefix("abc");
			colPathList.setReadFilenameContains(".csv");
			colPathList.setReadFilenameExcludeTypes("");
			colPathList.setReadFilenamePrefix("");
			colPathList.setReadFilenameSuffix("");
			colPathList.setFileGrepDateEnabled(false);
			colPathList.setSeqStartIndex(5);
			colPathList.setDateFormat("mm-dd-yy");
			colPathList.setEndIndex(5);
			colPathList.setFileSeqAlertEnabled(true);
			//colPathList.setMaxCounterLimit(6);
			colPathList.setMaxFilesCountAlert(7);
			colPathList.setPosition(PositionEnum.RIGHT.getValue());
			colPathList.setRemoteFileAction(FilterActionEnum.MOVE.getValue());
			colPathList.setRemoteFileActionParamName(FileActionParamEnum.DESTINATIONPATH.getValue());
			colPathList.setRemoteFileActionValue("/home/copyfolders3/src/move1");

			colPathList.setRemoteFileAction(FilterActionEnum.MOVE.getValue());
			colPathList.setRemoteFileActionParamName(FileActionParamEnum.DESTINATIONPATH.getValue());
			colPathList.setRemoteFileActionValue("/home/mediationngt/BackupFiles/Collection_data/moveLocation");
			colPathList.setName("Coll PAth list " + Math.random());

			delhiFTPdriver.getDriverPathList().add(colPathList);
			
			
			session.save(collService);
			session.save(fileGrpParam);
			session.save(delhiFTPdriver);

			// collService.getSvcPathList().add(colPathList);

			session.save(colPathList);

			session.getTransaction().commit();

			session.close();

		} catch (Exception e) {
			e.printStackTrace();
			executionStatus = false;
		}
		return executionStatus;
	}
	
	public static int addNetFlowServicePage1(int serverInstanceId){
		
		int svcId = 0;
		System.out.println("TestEntities.addNetFlowServicePage1()");
		try {

			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			ServerInstance serverInsFromDB = (ServerInstance) session.get(ServerInstance.class, serverInstanceId);
			ServiceType collSvcType = (ServiceType) session.get(ServiceType.class, "STYP0005");

			Service collService = new NetflowCollectionService();
			// below are the properties first used in making a service, rest all
			// config is done later
			collService.setSvctype(collSvcType);
			collService.setName("netflow test collection service t1");
			collService.setServerInstance(serverInsFromDB);
			

			List<Service> svcList = null;
			if (serverInsFromDB.getServices() != null) {
				svcList = serverInsFromDB.getServices();

			} else
				svcList = new ArrayList<Service>();

			svcList.add(collService);
			serverInsFromDB.setServices(svcList);
			serverInsFromDB.setSyncChildStatus(false);
			serverInsFromDB.setSyncSIStatus(false);

			serverInsFromDB.getLogsDetail().setLogPathLocation("/home/group1/crestelsetup/crestelpengine/modules/mediation/si_test_logs");

			session.update(serverInsFromDB);
			session.save(collService);
			session.getTransaction().commit();
			session.close();
			svcId = collService.getId();

		} catch (Exception e) {
			e.printStackTrace();

		}
		return svcId;
		
	}
	
	public static boolean createNetFlowPart2(int colSvcId){
		System.out.println("TestEntities.createNetFlowPart2()");

		boolean executionStatus = true;
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			NetflowCollectionService collSvc = (NetflowCollectionService) session.get(NetflowCollectionService.class, colSvcId);
			collSvc.setEnableParallelBinaryWrite(false);
			collSvc.setReadTemplateOnInit(false);
			collSvc.setOptionTemplateEnable(true);
			collSvc.setOptionTemplateId("167");
			collSvc.setOptionTemplateKey("168");
			collSvc.setOptionTemplateValue("168");
			collSvc.setOptionCopytoTemplateId("201");
			collSvc.setOptionCopyTofield("fiel"); // size
			//collSvc.setBinaryfileLocation("/var/logs");
			
			collSvc.setNetFlowPort(1621);
			
			collSvc.setBulkWriteLimit(10);
			collSvc.setMaxPktSize(123333);
			collSvc.setMaxWriteBufferSize(4545454);
			collSvc.setParallelFileWriteCount(10);
			collSvc.setSnmpTimeInterval(123);
			collSvc.setMaxIdelCommuTime(1000);
			
			NetflowClient client = new NetflowClient();
			client.setName("Testlient");
			client.setClientIpAddress("127.0.0.1");
			client.setClientPort(1617);
			client.setFileNameFormat("sample.log");
			client.setMinFileSeqValue(1);
			client.setMaxFileSeqValue(5);
			client.setOutFileLocation("/var");  // size
			client.setVolLogRollingUnit(1);
			client.setTimeLogRollingUnit(1);
			client.setBkpBinaryfileLocation("/var/binary");
			
			//session.save(client);
			
			List<NetflowClient> liclient = new ArrayList<NetflowClient>();
			liclient.add(client);
			
			collSvc.setNetFLowClientList(liclient);
			session.update(collSvc);
			
			client.setService(collSvc);
			session.save(client);

			session.getTransaction().commit();

			session.close();

		} catch (Exception e) {
			e.printStackTrace();
			executionStatus = false;
		}
		return executionStatus;
	}
	
	public static List<SnmpAlertCustomObject> getAlertListWithThreshold(){
		String hql = "select distinct a.id,a.alertId,a.name,a.desc,b.id,c.service.id,c.threshold from SNMPAlertWrapper b right join b.alert a left join b.serviceThreshold c";
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery(hql);
		List<SnmpAlertCustomObject> results = query.list();
		System.out.println("Inside getAlertListWithThreshold"+results);
		return results;
	}
/*	public static  List<ProfileEntity> testEntityProfiling(){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Criteria criteria=session.createCriteria(ProfileEntity.class);
		
		criteria.add(Restrictions.isNull("parentEntity"));

		List<ProfileEntity> entitiesProfile=criteria.list();
		
		entitiesProfile=fetchChildEntities(entitiesProfile);	
		   
		session.close();
		
		 return  entitiesProfile;
	    
	}

    public static List<ProfileEntity> fetchChildEntities(List<ProfileEntity> entitiesProfile){
    	Session session = HibernateUtil.getSessionFactory().openSession();

		System.out.println("fetchChildEntities called");
		
		for(ProfileEntity entity:entitiesProfile){ 
			
		System.out.println("Process entity  :: "+entity.getEntityAlias());
		Criteria criteria=session.createCriteria(ProfileEntity.class);
		criteria.add(Restrictions.eq("parentEntity.id", entity.getId()));
	
        List<ProfileEntity> entitiesProfile1=criteria.list();
        if(entitiesProfile1 !=null && !entitiesProfile1.isEmpty()){
             entity.setChildEntities(entitiesProfile1);
           fetchChildEntities(entitiesProfile1);
        }
      
      }
		
		for(ProfileEntity entity:entitiesProfile){ 
 	    	System.out.println("Entity found :: "+entity.getEntityAlias());
 	    	List<ProfileEntity> entitiesProfile1=entity.getChildEntities();
 	    	if(entitiesProfile1!=null &&  !entitiesProfile1.isEmpty()){
 	    		for(ProfileEntity entity1:entitiesProfile1){
 	 	    		 System.out.println("child Entity found for  :: "+entity.getEntityAlias() + " is :: "+entity1.getEntityAlias() );
 	 	    		if(entity1.getChildEntities() !=null && !entity1.getChildEntities().isEmpty()){
 	 	    			for(ProfileEntity entity2:entity1.getChildEntities() ){
		 	 	    		 System.out.println("child Entity found for  :: "+entity1.getEntityAlias() + " is :: "+entity2.getEntityAlias() );
	 	 	    		}
 	 	    		}
 	 	    	}
 	    	}
 	    }
		session.close();
		return entitiesProfile;
    }
    
    public  static void displayResult(List<ProfileEntity> entitiesProfile){
    	Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
    	
    	
    	 session.getTransaction().commit();
    	 session.close();
    }
*/}
