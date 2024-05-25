/**
 * 
 */
package com.elitecore.sm.test;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.common.model.CharSetEnum;
import com.elitecore.sm.common.model.DataTypeEnum;
import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.composer.model.ASCIIComposerAttr;
import com.elitecore.sm.composer.model.ASCIIComposerMapping;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.drivers.model.ConnectionParameter;
import com.elitecore.sm.drivers.model.DriverType;
import com.elitecore.sm.drivers.model.FTPDistributionDriver;
import com.elitecore.sm.parser.model.FileHeaderFooterTypeEnum;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.pathlist.model.CharRenameOperation;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;
import com.elitecore.sm.pathlist.model.FileGroupingParameter;
import com.elitecore.sm.pathlist.model.FileGroupingParameterParsing;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.serverinstance.model.LogsDetail;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.DistributionService;
import com.elitecore.sm.services.model.SchedulingDateEnum;
import com.elitecore.sm.services.model.SchedulingTypeEnum;
import com.elitecore.sm.services.model.ServiceSchedulingParams;
import com.elitecore.sm.services.model.ServiceType;

/**
 * @author Ranjitsinh Reval
 *
 */
@SuppressWarnings( {"unused","unchecked"})
public class TestDistributionService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			int serverInsId = 1;
			//serverInsId = createServerInstance(serverInsId);
			//System.out.println("Server Instance created successfully.");
			//int serviceId = createDistributionService(serverInsId);
			int serviceId =100;
			System.out.println("Distribution service Created Successfully" + serviceId);
			//int driverId = createDriverAndItsDependents(serviceId);
			
			//createPathlistAndComposer(serviceId, driverId);
			
			createComposerAttribute(1);
			
		} catch (Exception e) {
			System.out.println("App.main() caught ex " + e);
			
			e.printStackTrace();
		} finally {
			System.exit(1);
		}
		

	}

	
	public static int createServerInstance(int serverInstanceId){
		Session session = HibernateUtil.getSessionFactory().openSession();

		ServerInstance serverInsFromDB = (ServerInstance) session.get(ServerInstance.class, serverInstanceId);
		if (serverInsFromDB != null) {
			return serverInstanceId;
		} else {
			session.beginTransaction();

			ServerType cgfType = (ServerType) session.get(ServerType.class, 2);

			Server testServer = new Server();
			testServer.setName("IPLMS Server");
			testServer.setIpAddress("192.168.0.146");
			testServer.setDescription("IPLMS Server");
			testServer.setServerType(cgfType);

			DataSourceConfig dsConfig = new DataSourceConfig();
			dsConfig.setConnURL("abc");
			dsConfig.setName("MEDIATION_DS");
			dsConfig.setType("Oracle");
			dsConfig.setUsername("scott");
			dsConfig.setPassword("tiger");

			LogsDetail logsDetail = new LogsDetail();

			// Server connection details and ds config will be read by system
			// before instance creation.

			ServerInstance mySrvInstance = new ServerInstance();

			Agent fileRenameAgent = new Agent();
	//		fileRenameAgent.setTypeOfAgent("FILE_RENAME_AGENT");
			fileRenameAgent.setServerInstance(mySrvInstance);

			List<Agent> agentList = new ArrayList<>();
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
	
	public static int createDistributionService(int serverInstanceId){
		
		System.out.println("Going to create new distribution Service");
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		ServerInstance serverInsFromDB = (ServerInstance) session.get(ServerInstance.class, serverInstanceId);

		ServiceType disSvcType = (ServiceType) session.get(ServiceType.class,4);

		DistributionService dService = new DistributionService();
			dService.setSvctype(disSvcType);
			dService.setName("Dist Service 2");
			dService.setTimestenDatasourceName("datasource");
			dService.setFileMergeEnabled(true);
			dService.setFileMergeGroupingBy("");
			dService.setProcessRecordLimit(23);
			dService.setStatus(StateEnum.ACTIVE);
			dService.setSyncStatus(false);
			dService.setThirdPartyTransferEnabled(false);
			dService.setWriteRecordLimit(1);
			dService.setServerInstance(serverInsFromDB);
		
		
			ServiceSchedulingParams svcSchedule = new ServiceSchedulingParams();
			svcSchedule.setDate(SchedulingDateEnum.Second.getNumVal());
			svcSchedule.setSchedulingEnabled(true);
			svcSchedule.setSchType(SchedulingTypeEnum.Monthly.getValue());
			dService.setServiceSchedulingParams(svcSchedule);
			
			
			FileGroupingParameterParsing fileGroupParams = new  FileGroupingParameterParsing(); 
			dService.setFileGroupingParameter(fileGroupParams);
		
			session.save(svcSchedule);
			session.save(fileGroupParams);
			session.save(dService);
			session.getTransaction().commit();
			session.close();
		
		if(dService.getId() > 0 ){
			return dService.getId();
		}else{
			return 0;
		}
		
	}
	
	public static int createDriverAndItsDependents(int serviceId){
		System.out.println("Going to create driver and its dependants." + serviceId);
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		DistributionService dService = (DistributionService) session.get(DistributionService.class,serviceId);
		
		System.out.println("Service name is :: " + dService);
		DriverType dtype = (DriverType) session.get(DriverType.class,4);
		
		
		FTPDistributionDriver disDriver = new FTPDistributionDriver();
		disDriver.setName("Delhi FTP 2");
		disDriver.setService(dService);
		disDriver.setApplicationOrder(0);
		disDriver.setTimeout(100);
		disDriver.setDriverType(dtype);	
		disDriver.setService(dService);
		disDriver.setNoFileAlert(1);
		
		dService.getMyDrivers().add(disDriver);
		
		ConnectionParameter connectionParameter = new ConnectionParameter();
		connectionParameter.setiPAddressHost("192.168.0.146");
		connectionParameter.setUsername("smrewamp");
		connectionParameter.setPassword("smrewamp123");
		disDriver.setFtpConnectionParams(connectionParameter);
		
		session.save(disDriver);
	
		session.getTransaction().commit();
		session.close();
	
		
		if(disDriver.getId() > 0 ){
			return disDriver.getId();
		}else{
			return 0;
		}
		
	}
	
	
	
	public static int createPathlistAndComposer(int serviceId, int driverId){

		System.out.println("Going to create path list and composers.");
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		DistributionService dService = (DistributionService) session.get(DistributionService.class,serviceId);
		FTPDistributionDriver ftpDriver = (FTPDistributionDriver) session.get(FTPDistributionDriver.class,driverId);
		PluginTypeMaster pluginTypeMaster = (PluginTypeMaster) session.get(PluginTypeMaster.class,9);
		Device device = (Device) session.get(Device.class,1);
		
		
		
		DistributionDriverPathList pathlist = new DistributionDriverPathList();
		pathlist.setDriver(ftpDriver);
		pathlist.setName("Distribution Driver Pathlist 1");
		pathlist.setCompressInFileEnabled(true);
		pathlist.setCompressOutFileEnabled(true);
		pathlist.setReadFilePath("/data/smrewamp/test_data/ranjit/iplogparsing/input");
		pathlist.setWriteFilePath("");
		pathlist.setWriteFilenamePrefix("");
		pathlist.setReadFilenameContains("");
		pathlist.setReadFilenameExcludeTypes("");
		pathlist.setReadFilenamePrefix("");
		pathlist.setReadFilenameSuffix("");
		pathlist.setService(null);
		
		Composer composer = new Composer();
		CharRenameOperation charrename = new CharRenameOperation();
		charrename.setDefaultValue("abc");
		charrename.setEndIndex(3);
		charrename.setLength(12);
		charrename.setPaddingType(PositionEnum.LEFT.getValue());
		//charrename.setPaddingValue("av");
		charrename.setPosition(PositionEnum.RIGHT.getValue());
		charrename.setQuery("select");
		charrename.setSequenceNo(1);
		charrename.setStartIndex(0);
			
		composer.setName("Composer 1");
		//composer.setCharRenameOperation(charrename);
		composer.setDestPath("/data/smrewamp/test_data/ranjit/distribution/LocalTesting/output");
		composer.setFileBackupPath("");
		//composer.setReadFilenamePrefix("");
		//composer.setReadFilenameSuffix("");
		composer.setMyDistDrvPathlist(pathlist);
		composer.setComposerMapping(null);
		composer.setComposerType(pluginTypeMaster);
		
		
		
		ASCIIComposerMapping composerMapping = new ASCIIComposerMapping();
		composerMapping.setComposerType(pluginTypeMaster);
		composerMapping.setMappingType(1);
		composerMapping.setName("Mapping1");
		composerMapping.setDevice(device);
		composerMapping.setFileHeaderEnable(false);
		composerMapping.setFileHeaderContainsFields(false);
		composerMapping.setFieldSeparator(",");
		composerMapping.setFileHeaderParser(FileHeaderFooterTypeEnum.STANDARD);
		composerMapping.setDestCharset(CharSetEnum.UTF8);
		composerMapping.setDestDateFormat("yyyy-MM-dd+HH:mm:ss");
		composerMapping.setDestFileExt(".txt");
		
		
		
		List<? super ComposerAttribute> asciiAttributionList = new ArrayList<>();
		
		ASCIIComposerAttr asciiAttr1 = new ASCIIComposerAttr();
		asciiAttr1.setSequenceNumber(1);
		asciiAttr1.setDestinationField("General1");
		asciiAttr1.setUnifiedField("General1");
		asciiAttr1.setDefualtValue("");
		asciiAttr1.setTrimchars("");
		asciiAttr1.setReplaceConditionList("");
		asciiAttr1.setDataType(DataTypeEnum.STRING);
		asciiAttr1.setDateFormat("");
		
		/*ASCIIComposerAttr asciiAttr2 = new ASCIIComposerAttr();
		asciiAttr2.setSequenceNumber(2);
		asciiAttr2.setDestinationField("General2");
		asciiAttr2.setUnifiedField("General2");
		asciiAttr2.setDefualtValue("");
		asciiAttr2.setTrimchars("");
		asciiAttr2.setReplaceConditionList("271:C,272:D");
		asciiAttr2.setDataType(DataTypeEnum.STRING);
		asciiAttr2.setDateFormat("");
		asciiAttr2.setPaddingEnable(true);
		asciiAttr2.setLength(9);
		asciiAttr2.setPaddingType(PositionEnum.LEFT);
		asciiAttr2.setPrefix("${General64}");
		asciiAttr2.setSuffix("");*/
		
		ASCIIComposerAttr asciiAttr3 = new ASCIIComposerAttr();
		asciiAttr3.setSequenceNumber(1);
		asciiAttr3.setDestinationField("General3");
		asciiAttr3.setUnifiedField("General3");
		asciiAttr3.setDefualtValue("");
		asciiAttr3.setTrimchars("");
		asciiAttr3.setReplaceConditionList("");
		asciiAttr3.setDataType(DataTypeEnum.STRING);
		asciiAttr3.setDateFormat("");
		
		
		ASCIIComposerAttr asciiAttr4 = new ASCIIComposerAttr();
		asciiAttr4.setSequenceNumber(1);
		asciiAttr4.setDestinationField("General4");
		asciiAttr4.setUnifiedField("General4");
		asciiAttr4.setDefualtValue("");
		asciiAttr4.setTrimchars("");
		asciiAttr4.setReplaceConditionList("");
		asciiAttr4.setDataType(DataTypeEnum.DATE);
		asciiAttr4.setDateFormat("");
		
		asciiAttributionList.add(asciiAttr1);
		//asciiAttributionList.add(asciiAttr2);
		//asciiAttributionList.add(asciiAttr3);
		//asciiAttributionList.add(asciiAttr4);
		
		composerMapping.setAttributeList((List<ComposerAttribute>) asciiAttributionList);
		
		session.save(composerMapping);
		
		composer.setComposerMapping(composerMapping);
		
		session.save(pathlist);
		session.save(composer);
		session.getTransaction().commit();
		session.close();
		
		System.out.println("Plugin and pathlist created successfully.");
		return 0;
	}
	
	
	public static void createComposerAttribute(int composerId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Composer composer = (Composer) session.get(Composer.class,composerId);
		PluginTypeMaster pluginTypeMaster = (PluginTypeMaster) session.get(PluginTypeMaster.class,9);
		Device device = (Device) session.get(Device.class,1);
		
		ASCIIComposerMapping composerMapping = new ASCIIComposerMapping();
		composerMapping.setComposerType(pluginTypeMaster);
		composerMapping.setMappingType(1);
		composerMapping.setName("Mapping3");
		composerMapping.setDevice(device);
		composerMapping.setFileHeaderEnable(true);
		composerMapping.setFileHeaderContainsFields(false);
		composerMapping.setFieldSeparator(",");
		composerMapping.setFileHeaderParser(FileHeaderFooterTypeEnum.STANDARD);
		composerMapping.setDestCharset(CharSetEnum.UTF8);
		composerMapping.setDestDateFormat("dd/MM/yyyy HH:mm:ss");
		composerMapping.setDestFileExt(".txt");
		
		
		
		List<? super ComposerAttribute> asciiAttributionList = new ArrayList<>();
		
		ASCIIComposerAttr asciiAttr1 = new ASCIIComposerAttr();
		asciiAttr1.setSequenceNumber(1);
		asciiAttr1.setDestinationField("General1");
		asciiAttr1.setUnifiedField("General1");
		asciiAttr1.setDefualtValue("");
		asciiAttr1.setTrimchars("");
		asciiAttr1.setReplaceConditionList("");
		asciiAttr1.setDataType(DataTypeEnum.STRING);	
		asciiAttr1.setDateFormat("");
		
		asciiAttributionList.add(asciiAttr1);
		
		
		ASCIIComposerAttr asciiAttr2 = new ASCIIComposerAttr();
		asciiAttr2.setSequenceNumber(2);
		asciiAttr2.setDestinationField("General2");
		asciiAttr2.setUnifiedField("General2");
		asciiAttr2.setDefualtValue("");
		asciiAttr2.setTrimchars("");
		asciiAttr2.setReplaceConditionList("");
		asciiAttr2.setDataType(DataTypeEnum.STRING);	
		asciiAttr2.setDateFormat("");
		
		asciiAttributionList.add(asciiAttr2);
		
		ASCIIComposerAttr asciiAttr3 = new ASCIIComposerAttr();
		asciiAttr3.setSequenceNumber(3);
		asciiAttr3.setDestinationField("General3");
		asciiAttr3.setUnifiedField("General3");
		asciiAttr3.setDefualtValue("");
		asciiAttr3.setTrimchars("");
		asciiAttr3.setReplaceConditionList("");
		asciiAttr3.setDataType(DataTypeEnum.STRING);	
		asciiAttr3.setDateFormat("");
		
		asciiAttributionList.add(asciiAttr3);
		
		ASCIIComposerAttr asciiAttr4 = new ASCIIComposerAttr();
		asciiAttr4.setSequenceNumber(4);
		asciiAttr4.setDestinationField("General4");
		asciiAttr4.setUnifiedField("General4");
		asciiAttr4.setDefualtValue("");
		asciiAttr4.setTrimchars("");
		asciiAttr4.setReplaceConditionList("");
		asciiAttr4.setDataType(DataTypeEnum.STRING);	
		asciiAttr4.setDateFormat("");
		
		asciiAttributionList.add(asciiAttr4);
		
		ASCIIComposerAttr asciiAttr5 = new ASCIIComposerAttr();
		asciiAttr5.setSequenceNumber(5);
		asciiAttr5.setDestinationField("StartDate");
		asciiAttr5.setUnifiedField("StartDate");
		asciiAttr5.setDefualtValue("");
		asciiAttr5.setTrimchars("");
		asciiAttr5.setReplaceConditionList("");
		asciiAttr5.setDataType(DataTypeEnum.STRING);	
		asciiAttr5.setDateFormat("");
		
		asciiAttributionList.add(asciiAttr5);
		
		/*ASCIIComposerAttr asciiAttr2 = new ASCIIComposerAttr();
		asciiAttr2.setSequenceNumber(5);
		asciiAttr2.setDestinationField("General7");
		asciiAttr2.setUnifiedField("General7");
		asciiAttr2.setDefualtValue("");
		asciiAttr2.setTrimchars("");
		asciiAttr2.setReplaceConditionList("");
		asciiAttr2.setDataType(DataTypeEnum.DATE);
		asciiAttr2.setDateFormat("");
   
		asciiAttributionList.add(asciiAttr2);*/
		
		/*ASCIIComposerAttr asciiAttr3 = new ASCIIComposerAttr();
		asciiAttr3.setSequenceNumber(2);
		asciiAttr3.setDestinationField("General2");
		asciiAttr3.setUnifiedField("General2");
		asciiAttr3.setDefualtValue("");
		asciiAttr3.setTrimchars("");
		asciiAttr3.setReplaceConditionList("271:C,272:D");
		asciiAttr3.setDataType(DataTypeEnum.STRING);
		asciiAttr3.setDateFormat("");
		asciiAttr3.setPaddingEnable(true);
		asciiAttr3.setLength(9);
		asciiAttr3.setPaddingType(PositionEnum.LEFT);
		asciiAttr3.setPrefix("${General64}");
		asciiAttr3.setSuffix("");
		asciiAttributionList.add(asciiAttr3);
		ASCIIComposerAttr asciiAttr4 = new ASCIIComposerAttr();
		asciiAttr4.setSequenceNumber(3);
		asciiAttr4.setDestinationField("General3");
		asciiAttr4.setUnifiedField("General3");
		asciiAttr4.setDefualtValue("");
		asciiAttr4.setTrimchars("");
		asciiAttr4.setReplaceConditionList("");
		asciiAttr4.setDataType(DataTypeEnum.STRING);
		asciiAttr4.setDateFormat("");
		
		asciiAttributionList.add(asciiAttr4);
		
		ASCIIComposerAttr asciiAttr5 = new ASCIIComposerAttr();
		asciiAttr5.setSequenceNumber(4);
		asciiAttr5.setDestinationField("General4");
		asciiAttr5.setUnifiedField("General4");
		asciiAttr5.setDefualtValue("");
		asciiAttr5.setTrimchars("");
		asciiAttr5.setReplaceConditionList("");
		asciiAttr5.setDataType(DataTypeEnum.DATE);
		asciiAttr5.setDateFormat("");
		asciiAttributionList.add(asciiAttr5);*/
		
		composerMapping.setAttributeList((List<ComposerAttribute>) asciiAttributionList);
		
		session.save(composerMapping);
		composer.setComposerMapping(composerMapping);
		session.merge(composer);
		session.getTransaction().commit();
		session.close();
		
		System.out.println("Successfully created mapping for selected composer plugin id : " + composerId);
		
	}
	
	
}



