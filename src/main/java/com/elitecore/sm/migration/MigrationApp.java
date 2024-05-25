package com.elitecore.sm.migration;

/*import static com.elitecore.sm.common.constants.MigrationConstants.AGENT_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.AGGREGATION_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.AGGREGATION_SERVICE_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.ALERT_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.ASCII_COMPOSER_PLUGIN_XML;
import static com.elitecore.sm.common.constants.MigrationConstants.COLLECTION_SERVICE_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.COLLECTION_SERVICE_XML;
import static com.elitecore.sm.common.constants.MigrationConstants.CORRELATION_SERVICE_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.DATABASE_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.DATA_CONSOLIDATION_SERVICE_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.DISTRIBUTION_SERVICE_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.DRIVERS_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.FTP_COLLECTION_DRIVER;
import static com.elitecore.sm.common.constants.MigrationConstants.FTP_COLLECTION_DRIVER_XML;
import static com.elitecore.sm.common.constants.MigrationConstants.GTP_PRIME_COLLECTION_SERVICE_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.IPLOG_CORRELATION_SERVICE_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.IPLOG_PARSING_SERVICE_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.LOCAL_COLLECTION_DRIVER;
import static com.elitecore.sm.common.constants.MigrationConstants.LOCAL_COLLECTION_DRIVER_XML;
import static com.elitecore.sm.common.constants.MigrationConstants.MAP_JAXB_CLASS_KEY;
import static com.elitecore.sm.common.constants.MigrationConstants.MAP_SM_CLASS_KEY;
import static com.elitecore.sm.common.constants.MigrationConstants.MEDIATION_SERVER_XML;
import static com.elitecore.sm.common.constants.MigrationConstants.NATFLOW_BINARY_COLLECTION_SERVICE_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.NATFLOW_COLLECTION_SERVICE_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.NATFLOW_COLLECTION_SERVICE_XML;
import static com.elitecore.sm.common.constants.MigrationConstants.PARSING_SERVICE_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.POLICIES_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.PROCESSING_SERVICE_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.RADIUS_COLLECTION_SERVICE_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.SERVICES_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.SFTP_COLLECTION_DRIVER;
import static com.elitecore.sm.common.constants.MigrationConstants.SFTP_COLLECTION_DRIVER_XML;
import static com.elitecore.sm.common.constants.MigrationConstants.SFTP_DISTRIBUTION_DRIVER_XML;
import static com.elitecore.sm.common.constants.MigrationConstants.SYSLOG_COLLECTION_SERVICE_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.SYSTEM_DIR;
import static com.elitecore.sm.common.constants.MigrationConstants.WEBSERVICE_DIR;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.xml.sax.SAXException;

import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.agent.model.AgentType;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.MigrationConstants;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.drivers.model.DriverType;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.drivers.model.FTPCollectionDriver;
import com.elitecore.sm.drivers.model.SFTPCollectionDriver;
import com.elitecore.sm.migration.model.CollectionServiceEntity;
import com.elitecore.sm.migration.model.FTPCollectionDriverEntity;
import com.elitecore.sm.migration.model.LocalCollectionDriverEntity;
import com.elitecore.sm.migration.model.NetflowCollectionServiceEntity;
import com.elitecore.sm.migration.model.SFTPCollectionDriverEntity;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.pathlist.model.CollectionDriverPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.serverinstance.model.LogsDetail;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.CollectionService;
import com.elitecore.sm.services.model.NetflowCollectionService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.test.HibernateUtil;*/

/**//**
 * @author brijesh.soni Nov 4, 2016
 *//*
*/public class MigrationApp {
	/*
	private Logger logger = Logger.getLogger(this.getClass().getName());

	// mapper with mapping files (It will use both xml and annotation mapping)
	private DozerBeanMapper mapper = new DozerBeanMapper();

	// mapper without mapping files (It will use only annotation mapping)
	private Mapper dozerMapper = new DozerBeanMapper();

	private Map<String, Map<String, String>> map = new HashMap<>();

	private static final String ALIAS = "alias";

	private int serverInsancePort;

	
	 * 1) Change ZIP_PATH and TEMP_PATH_FOR_UNZIP, SERVER_INSTANCE_PORT_IN_ZIP
	 * in MigrationConstants.java 2) Change ${catalina.home} in log4j.xml 3)
	 * Change properties in hibernate.cfg.xml 4) Add three jar :
	 * dozer-5.3.2.jar, apache-commons.lang.jar,
	 * org.apache.commons.beanutils.jar
	 
	public static void main(String[] args) {
		MigrationApp app = new MigrationApp();
		app.logger.info("MigrationApp.main() ***");
		app.startMigration(MigrationConstants.ZIP_PATH);
	}

	*//**
	 * Extracts a zip file specified by the zipFilePath to a directory specified
	 * 
	 * @param zipFilePath
	 *//*
	public ResponseObject startMigration(String zipFilePath) {
		logger.info("MigrationApp.startMigration() : zipFilePath : "
				+ zipFilePath);

		// initialize dozer mapping files (ex : StringToEnumConverter)
		initializeDozerMappingFiles();

		// initialize map for xml,jaxb object, sm object
		initializeMapForXmlToClass();

		ResponseObject responseObject = new ResponseObject();

		// destination directory for unzip file
		String destDirectory = MigrationConstants.TEMP_PATH_FOR_UNZIP
				+ File.separator;
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(
				zipFilePath));) {
			ZipEntry entry = zipIn.getNextEntry();
			// iterates over entries in the zip file
			int counter = 0;
			while (entry != null) {
				if (counter == 0) {
					++counter;
				}
				if (counter == 1) {
					serverInsancePort = Integer.parseInt(entry.getName()
							.replace(File.separator, ""));
					++counter;
				}

				String filePath = destDirectory + File.separator
						+ entry.getName();
				if (!entry.isDirectory()) {
					// if the entry is a file, extracts it
					extractFile(zipIn, filePath);
				} else {
					// if the entry is a directory, make the directory
					File dir = new File(filePath);
					dir.mkdir();
				}
				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		executeServerInstanceDirectory(destDirectory + serverInsancePort);
		return responseObject;
	}

	*//**
	 * Extracts a zip entry (file entry)
	 * 
	 * @param zipIn
	 * @param filePath
	 * @throws IOException
	 *//*
	private void extractFile(ZipInputStream zipIn, String filePath) {
		try (BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(filePath));) {
			byte[] bytesIn = new byte[MigrationConstants.BUFFER_SIZE];
			int read;
			while ((read = zipIn.read(bytesIn)) != -1) {
				bos.write(bytesIn, 0, read);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void executeServerInstanceDirectory(String directoryPath) {
		logger.info("MigrationApp.executeServerInstanceDirectory() ***");
		File file = new File(directoryPath);
		String[] names = file.list();
		for (String name : names) {
			File subFile = new File(directoryPath + File.separator + name);
			if (subFile.isDirectory()) {
				switch (name.toUpperCase()) {
				case SERVICES_DIR:
					executeServicesDirectory(directoryPath + File.separator
							+ name);
					break;
				case AGENT_DIR:
					break;
				case POLICIES_DIR:
					break;
				case SYSTEM_DIR:
					break;
				case AGGREGATION_DIR:
					break;
				case ALERT_DIR:
					break;
				case WEBSERVICE_DIR:
					break;
				case DATABASE_DIR:
					break;
				default:
					break;
				}
			} else if (subFile.getName().equalsIgnoreCase(MEDIATION_SERVER_XML)) {
				// logic to execute mediation-server.xml
			}
		}
	}

	private void executeServicesDirectory(String directoryPath) {
		logger.info("MigrationApp.executeServicesDirectory() ***");
		File file = new File(directoryPath);
		String[] names = file.list();
		for (String name : names) {
			if (new File(directoryPath + File.separator + name).isDirectory()) {

				switch (name.toUpperCase()) {
				case AGGREGATION_SERVICE_DIR:
					break;
				case COLLECTION_SERVICE_DIR:
					executeCollectionServiceDirectory(directoryPath
							+ File.separator + name);
					break;
				case CORRELATION_SERVICE_DIR:
					break;
				case DATA_CONSOLIDATION_SERVICE_DIR:
					break;
				case DISTRIBUTION_SERVICE_DIR:
					break;
				case GTP_PRIME_COLLECTION_SERVICE_DIR:
					break;
				case IPLOG_CORRELATION_SERVICE_DIR:
					break;
				case IPLOG_PARSING_SERVICE_DIR:
					break;
				case NATFLOW_BINARY_COLLECTION_SERVICE_DIR:
					break;
				case NATFLOW_COLLECTION_SERVICE_DIR:
					executeNatflowCollectionServiceDirectory(directoryPath
							+ File.separator + name);
					break;
				case PARSING_SERVICE_DIR:
					break;
				case PROCESSING_SERVICE_DIR:
					break;
				case RADIUS_COLLECTION_SERVICE_DIR:
					break;
				case SYSLOG_COLLECTION_SERVICE_DIR:
					break;

				default:
					break;
				}
			}
		}
	}

	private void executeCollectionServiceDirectory(String directoryPath) {
		logger.info("MigrationApp.executeCollectionServiceDirectory() *** ");
		File file = new File(directoryPath);
		String[] names = file.list();
		for (String name : names) {
			if (new File(directoryPath + File.separator + name).isDirectory()) {
				logger.info("MigrationApp.executeCollectionServiceDirectory() : Service Instance ID : "
						+ name);
				executeCollectionServiceInstance(directoryPath + File.separator
						+ name);
			}
		}
	}

	private void executeNatflowCollectionServiceDirectory(String directoryPath) {
		logger.info("MigrationApp.executeNatflowCollectionServiceDirectory() *** ");
		File file = new File(directoryPath);
		String[] names = file.list();
		for (String name : names) {
			if (new File(directoryPath + File.separator + name).isDirectory()) {
				logger.info("MigrationApp.executeNatflowCollectionServiceInstance() : Service Instance ID : "
						+ name);
				executeNatflowCollectionServiceInstance(directoryPath
						+ File.separator + name);
			}
		}
	}

	private void executeCollectionServiceInstance(String directoryPath) {
		logger.info("MigrationApp.executeCollectionServiceInstance() ***");
		CollectionService collectionService = executeCollectionService(directoryPath);

		collectionService = loadDriversInCollectionService(directoryPath
				+ File.separator + DRIVERS_DIR.toLowerCase(), collectionService);

		logger.info("Save collection service to db *** ");

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		collectionService.setName(getDynamicName("Mig_Collection_Service_"));
		session.save(collectionService);
		session.getTransaction().commit();
		session.close();

	}

	private NetflowCollectionService executeNatflowCollectionServiceInstance(
			String directoryPath) {
		logger.info("MigrationApp.executeNatflowCollectionServiceInstance() ***");
		NetflowCollectionService natflowCollectionService = executeNatflowCollectionService(directoryPath);

		logger.info("SAVE NetflowCollectionService IN DB ................");

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		natflowCollectionService
				.setName(getDynamicName("Mig_Natflow_Service_"));
		session.save(natflowCollectionService);
		session.getTransaction().commit();
		session.close();
		return natflowCollectionService;

	}

	private NetflowCollectionService executeNatflowCollectionService(
			String directoryPath) {
		NetflowCollectionService natflowCollectionService = null;
		Map<String, Object> objects = getSMAndJaxbObjectFromXml(directoryPath,
				NATFLOW_COLLECTION_SERVICE_XML);
		Object smObject = objects.get(MAP_SM_CLASS_KEY);
		Object jaxbObject = objects.get(MAP_JAXB_CLASS_KEY);
		if (smObject instanceof NetflowCollectionService
				&& jaxbObject instanceof NetflowCollectionServiceEntity) {
			natflowCollectionService = (NetflowCollectionService) smObject;

			natflowCollectionService = loadClientsInNetflowCollectionService(
					natflowCollectionService,
					(NetflowCollectionServiceEntity) jaxbObject);

			natflowCollectionService
					.setServInstanceId(getParentFolderName(directoryPath
							+ File.separator + NATFLOW_COLLECTION_SERVICE_XML));
			natflowCollectionService
					.setSvctype(getServiceTypeByAlias(EngineConstants.NATFLOW_COLLECTION_SERVICE));
			natflowCollectionService.setServerInstance(getServerInstance(1));

		}
		if (natflowCollectionService != null) {
			displayNatflowCollectionService(natflowCollectionService);
		}
		return natflowCollectionService;
	}

	private CollectionService executeCollectionService(String directoryPath) {
		logger.info("MigrationApp.executeCollectionService() : directoty Path : "
				+ directoryPath + File.separator + COLLECTION_SERVICE_XML);
		CollectionService collectionService = null;
		Class<?> clazz = getClassFromName(map.get(COLLECTION_SERVICE_XML).get(
				MAP_JAXB_CLASS_KEY));
		try {
			ResponseObject responseObject = getUnmarshalObjectFromFile(
					new File(directoryPath + File.separator
							+ COLLECTION_SERVICE_XML), clazz);
			Object object = responseObject.getObject();
			if (object instanceof CollectionServiceEntity) {
				Class<?> destinationClass = getClassFromName(map.get(
						COLLECTION_SERVICE_XML).get(MAP_SM_CLASS_KEY));
				Object convertedSMObject = convertJaxbToSMObjectWithConverter(
						object, destinationClass);
				if (convertedSMObject instanceof CollectionService) {
					collectionService = (CollectionService) convertedSMObject;
					// set parent folder name as service instance id
					collectionService
							.setServInstanceId(getParentFolderName(directoryPath
									+ File.separator + COLLECTION_SERVICE_XML));
					// get service type object
					collectionService
							.setSvctype(getServiceTypeByAlias(EngineConstants.COLLECTION_SERVICE));
					// TODO : Add this to save object in db
					collectionService.setServerInstance(getServerInstance(1));
					displayCollectionService(collectionService);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return collectionService;
	}

	private NetflowCollectionService loadClientsInNetflowCollectionService(
			NetflowCollectionService smNatflowCollectionService,
			NetflowCollectionServiceEntity jaxbNatflowCollectionService) {
		if (jaxbNatflowCollectionService != null
				&& jaxbNatflowCollectionService.getClients() != null
				&& !jaxbNatflowCollectionService.getClients().getClient()
						.isEmpty()) {
			List<NetflowCollectionServiceEntity.Clients.Client> clients = jaxbNatflowCollectionService
					.getClients().getClient();
			int length = clients.size();
			List<NetflowClient> netflowClientList = new ArrayList<>();
			Class<?> destinationClass = getClassFromName("com.elitecore.sm.netflowclient.model.NetflowClient");
			for (int i = length - 1; i >= 0; i--) {
				Object convertedObject = convertJaxbToSMObjectWithConverter(
						clients.get(i), destinationClass);
				if (convertedObject instanceof NetflowClient) {
					NetflowClient natflowClient = (NetflowClient) convertedObject;
					natflowClient
							.setName(getDynamicName("Mig_Natflow_Client_"));
					netflowClientList.add((NetflowClient) convertedObject);
				}
			}
			smNatflowCollectionService.setNetFLowClientList(netflowClientList);
			return smNatflowCollectionService;

		}
		return null;
	}

	private CollectionService loadDriversInCollectionService(
			String directoryPath, CollectionService collectionService) {
		logger.info("MigrationApp.executeCollectionDriverDirectory() *** ");
		// TODO : check both driver name and status (status != 'DELETED')
		List<Drivers> drivers = collectionService.getMyDrivers();
		int driversLength = drivers.size();
		for (int i = driversLength - 1; i >= 0; i--) {
			if (drivers.get(i).getName().equalsIgnoreCase(LOCAL_COLLECTION_DRIVER)) {

				Drivers smDriver = getCollectionDriver(directoryPath,
						collectionService,
						drivers.get(i).getApplicationOrder(),
						LOCAL_COLLECTION_DRIVER_XML, "MIG_LOCAL_COLL_DRIVER_");
				drivers.set(i, smDriver);

			} else if (drivers.get(i).getName().equalsIgnoreCase(FTP_COLLECTION_DRIVER)) {

				Drivers smDriver = getCollectionDriver(directoryPath,
						collectionService,
						drivers.get(i).getApplicationOrder(),
						FTP_COLLECTION_DRIVER_XML, "MIG_FTP_COLL_DRIVER_");
				drivers.set(i, smDriver);

			} else if (drivers.get(i).getName().equalsIgnoreCase(SFTP_COLLECTION_DRIVER)) {

				Drivers smDriver = getCollectionDriver(directoryPath,
						collectionService,
						drivers.get(i).getApplicationOrder(),
						SFTP_COLLECTION_DRIVER_XML, "MIG_SFTP_COLL_DRIVER_");
				drivers.set(i, smDriver);

			}

		}
		logger.info("MigrationApp.executeCollectionDriverDirectory() *** Updated CollectionService ***");
		displayCollectionService(collectionService);

		return collectionService;
	}

	private Drivers getCollectionDriver(String directoryPath, Service service,
			int applicationOrder, String driverXml, String migDriverName) {
		String driverName = getDriverNameFromApplicationOrder(applicationOrder,	driverXml);
		Object collectionDriverObject = executeCollectionDriver(directoryPath,
				driverName.trim(), driverXml);
		return getUpdatedDriverObject(collectionDriverObject, applicationOrder,
				getDynamicName(migDriverName), service);
	}

	private Drivers getUpdatedDriverObject(Object object, int applicationOrder,
			String driverName, Service service) {
		Drivers commonDriver;
		if (object instanceof Drivers) {
			commonDriver = (Drivers) object;
			commonDriver.setName(driverName);
			commonDriver.setApplicationOrder(applicationOrder);
			commonDriver.setService(service);
			return commonDriver;
		}
		return null;
	}

	private String getDynamicName(String name) {
		return name + System.currentTimeMillis();
	}

	private String getDriverNameFromApplicationOrder(int applicationOrder,
			String driverName) {
		String name;
		if (applicationOrder > 0) {
			name = applicationOrder + "-" + driverName;
		} else {
			name = driverName;
		}
		return name.trim();
	}

	private ResponseObject getUnmarshalObjectFromFile(File file, Class<?> clazz)
			throws IOException, JAXBException, SAXException {
		logger.info("MigrationApp.getUnmarshalObjectFromFile() : class : "
				+ clazz + ": File : " + file.getName());
		ResponseObject responseObject = new ResponseObject();

		final JSONArray finaljArray = new JSONArray();
		ValidationEventHandler validator = event -> {
			JSONArray jArray = new JSONArray();
			jArray.put(event.getLocator().getLineNumber());
			jArray.put(event.getMessage());
			finaljArray.put(jArray);
			return true;
		};

		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		jaxbUnmarshaller.setEventHandler(validator);
		Object object = jaxbUnmarshaller.unmarshal(file);

		if (finaljArray.length() > 0) {
			responseObject.setSuccess(false);
			responseObject
					.setResponseCode(ResponseCode.IMPORT_XSD_VALIDATION_FAIL);
			responseObject.setObject(finaljArray);
		} else {
			responseObject.setSuccess(true);
			responseObject.setObject(object);
		}
		return responseObject;
	}

	*//**
	 * This method converts source object to destination object
	 * 
	 * @param sourceObject
	 *            (JAXBObject)
	 * @param destinationClass
	 *            (SMClass)
	 * @return destinationObject (SMObject)
	 *//*
	private Object convertJaxbToSMObject(Object sourceObject,
			Class<?> destinationClass) {
		logger.info("MigrationApp.convertJaxbToSMObject() : sourceObject : "
				+ sourceObject.getClass() + " : destinationClass : "
				+ destinationClass);
		return dozerMapper.map(sourceObject, destinationClass);
	}

	private Object convertJaxbToSMObjectWithConverter(Object sourceObject,
			Class<?> destinationClass) {
		logger.info("MigrationApp.convertJaxbToSMObject() : sourceObject : "
				+ sourceObject.getClass() + " : destinationClass : "
				+ destinationClass);
		return mapper.map(sourceObject, destinationClass);
	}

	*//**
	 * This method returns parent folder from full file path
	 * 
	 * @param filePath
	 * @return parentFolderName
	 *//*
	private String getParentFolderName(String filePath) {
		logger.info("MigrationApp.getParentFolderName() : filePath : "
				+ filePath);
		File file = new File(filePath);
		if (file.getParentFile() != null) {
			return file.getParentFile().getName();
		}
		return null;

	}

	private void displayCollectionService(CollectionService cs) {
		logger.info("startup mode ENUM : "
				+ cs.getSvcExecParams().getStartupMode());
		logger.info("sorting type : " + cs.getSvcExecParams().getSortingType());
		logger.info("Service Execution Params.sorting creteria : "
				+ cs.getSvcExecParams().getSortingCriteria());
		logger.info("Service Execution Params.is execution on startup : "
				+ cs.getSvcExecParams().isExecuteOnStartup());
		logger.info("Service Execution Params.queue size : "
				+ cs.getSvcExecParams().getQueueSize());
		logger.info("Service Execution Params.min thread : "
				+ cs.getSvcExecParams().getMinThread());
		logger.info("Service Execution Params.max thread : "
				+ cs.getSvcExecParams().getMaxThread());
		logger.info("Service Execution Params.file batch size : "
				+ cs.getSvcExecParams().getFileBatchSize());
		logger.info("Service Execution Params.min disk space : "
				+ cs.getServerInstance().getMinDiskSpace());
		logger.info("enable db status : " + cs.isEnableDBStats());
		logger.info("Total driver associated with service : "
				+ cs.getMyDrivers().size());
		for (Drivers d : cs.getMyDrivers()) {
			logger.info("Driver................................................");
			logger.info("application order : " + d.getApplicationOrder());
			logger.info("name : " + d.getName());
			if (d.getDriverPathList() != null) {
				logger.info("driver path list : "
						+ d.getDriverPathList().size());
			}
			for (PathList pathList : d.getDriverPathList()) {
				if (pathList instanceof CollectionDriverPathList) {
					logger.info("pathlist...........................................");
					CollectionDriverPathList l = (CollectionDriverPathList) pathList;
					logger.info("readFilePath : " + l.getReadFilePath());
					logger.info("readFilenamePrefix : "
							+ l.getReadFilenamePrefix());
					logger.info("readFilenameSuffix : "
							+ l.getReadFilenameSuffix());
					logger.info("maxFilesCountAlert : "
							+ l.getMaxFilesCountAlert());
					logger.info("readFilenameContains : "
							+ l.getReadFilenameContains());
					logger.info("writeFilePath : " + l.getWriteFilePath());
					logger.info("writeFilenamePrefix : "
							+ l.getWriteFilenamePrefix());
					logger.info("remoteFileAction : " + l.getRemoteFileAction());
					logger.info("remoteFileActionParamName : "
							+ l.getRemoteFileActionParamName());
					logger.info("remoteFileActionValue : "
							+ l.getRemoteFileActionValue());
					logger.info("dateFormat : " + l.getDateFormat());
					logger.info("position : " + l.getPosition());
					logger.info("startIndex : " + l.getStartIndex());
					logger.info("endIndex : " + l.getEndIndex());
					logger.info("fileGrepDateEnabled : "
							+ l.getFileGrepDateEnabled());
					logger.info("seqStartIndex : " + l.getSeqStartIndex());
					logger.info("seqEndIndex : " + l.getSeqEndIndex());
					logger.info("maxCounterLimit : " + l.getMaxCounterLimit());
					logger.info("fileSeqAlertEnabled : "
							+ l.getFileSeqAlertEnabled());

				}
			}
		}

		logger.info("status of collection service : " + cs.getStatus());
		logger.info("service scheduling params.is scheduling enabled : "
				+ cs.getServiceSchedulingParams().isSchedulingEnabled());
		logger.info("service scheduling params.is scheduling enabled : "
				+ cs.getServiceSchedulingParams().getSchType());
		logger.info("service scheduling params.date : "
				+ cs.getServiceSchedulingParams().getDate());
		logger.info("service scheduling params.day : "
				+ cs.getServiceSchedulingParams().getDay());
		logger.info("service scheduling params.time : "
				+ cs.getServiceSchedulingParams().getTime());
		logger.info("service instance id PARENT FOLDER NAME: "
				+ cs.getServInstanceId());
		logger.info("-----------------------------------------------");
	}

	public void displayLocalCollectionDriver(
			com.elitecore.sm.drivers.model.LocalCollectionDriver driver) {
		logger.info("File sequence order : " + driver.isFileSeqOrder());
		List<PathList> pathList = driver.getDriverPathList();
		for (PathList p : pathList) {
			if (p instanceof CollectionDriverPathList) {
				CollectionDriverPathList l = (CollectionDriverPathList) p;
				displayCollectionDriverPathList(l);
			}
		}
		logger.info("maxRetrycount : " + driver.getMaxRetrycount());
		logger.info("noFileAlert : " + driver.getNoFileAlert());
		logger.info("fileGroupingParameter.groupingType : "
				+ driver.getFileGroupingParameter().getGroupingType());
		logger.info("fileGroupingParameter.enableForDuplicate : "
				+ driver.getFileGroupingParameter().isEnableForDuplicate());
		logger.info("fileGroupingParameter.fileGroupEnable : "
				+ driver.getFileGroupingParameter().isFileGroupEnable());
		logger.info("minFileRange LOCAL : " + driver.getMinFileRange());
		logger.info("maxFileRange LOCAL : " + driver.getMaxFileRange());
	}

	public void displayFTPCollectionDriver(FTPCollectionDriver driver) {
		logger.info("MigrationApp.displayFTPCollectionDriver() ***** start");
		logger.info("status : " + driver.getStatus());
		logger.info("ftpConnectionParams/fileTransferMode : "
				+ driver.getFtpConnectionParams().getFileTransferMode());
		logger.info("minFileRange FTP : " + driver.getMinFileRange());
		logger.info("maxFileRange FTP : " + driver.getMaxFileRange());
		logger.info("file-sequence-order : " + driver.isFileSeqOrder());
		logger.info("host-list . host : "
				+ driver.getFtpConnectionParams().getiPAddressHost());
		logger.info("port : " + driver.getFtpConnectionParams().getPort());
		logger.info("ftp-timeout : "
				+ driver.getFtpConnectionParams().getTimeout());
		logger.info("user-name : "
				+ driver.getFtpConnectionParams().getUsername());
		logger.info("password : "
				+ driver.getFtpConnectionParams().getPassword());
		logger.info("max-retry-count : " + driver.getMaxRetrycount());
		logger.info("remote-system-file-seperator : "
				+ driver.getFtpConnectionParams().getFileSeparator());
		logger.info("no-files-alert-interval : " + driver.getNoFileAlert());
		logger.info("PATHLIST************* start");
		List<PathList> pathList = driver.getDriverPathList();
		for (PathList p : pathList) {
			if (p instanceof CollectionDriverPathList) {
				CollectionDriverPathList l = (CollectionDriverPathList) p;
				displayCollectionDriverPathList(l);
			}
		}
		logger.info("PATHLIST************* end");
		logger.info("file-grouping . enabled : "
				+ driver.getFileGroupingParameter().isFileGroupEnable());
		logger.info("file-grouping . grouping-type : "
				+ driver.getFileGroupingParameter().getGroupingType());
		logger.info("file-grouping . for-duplicate : "
				+ driver.getFileGroupingParameter().isEnableForDuplicate());
		logger.info("file-fetch-rule . enabled : "
				+ driver.getMyFileFetchParams().isFileFetchRuleEnabled());
		logger.info("file-fetch-rule . file-fetch-type : "
				+ driver.getMyFileFetchParams().getFileFetchType());
		logger.info("file-fetch-rule . file-fetch-interval : "
				+ driver.getMyFileFetchParams().getFileFetchIntervalMin());
		logger.info("file-fetch-rule . time-zone : "
				+ driver.getMyFileFetchParams().getTimeZone());
		logger.info("MigrationApp.displayFTPCollectionDriver() ***** end");
	}

	public void displaySFTPCollectionDriver(SFTPCollectionDriver driver) {
		logger.info("MigrationApp.displayFTPCollectionDriver() ***** start");
		logger.info("status : " + driver.getStatus());
		logger.info("ftpConnectionParams/fileTransferMode : "
				+ driver.getFtpConnectionParams().getFileTransferMode());
		logger.info("minFileRange SFTP : " + driver.getMinFileRange());
		logger.info("maxFileRange SFTP : " + driver.getMaxFileRange());
		logger.info("file-sequence-order : " + driver.isFileSeqOrder());
		logger.info("host-list . host : "
				+ driver.getFtpConnectionParams().getiPAddressHost());
		logger.info("port : " + driver.getFtpConnectionParams().getPort());
		logger.info("ftp-timeout : "
				+ driver.getFtpConnectionParams().getTimeout());
		logger.info("user-name : "
				+ driver.getFtpConnectionParams().getUsername());
		logger.info("password : "
				+ driver.getFtpConnectionParams().getPassword());
		logger.info("max-retry-count : " + driver.getMaxRetrycount());
		logger.info("remote-system-file-seperator : "
				+ driver.getFtpConnectionParams().getFileSeparator());
		logger.info("no-files-alert-interval : " + driver.getNoFileAlert());
		logger.info("PATHLIST************* start");
		List<PathList> pathList = driver.getDriverPathList();
		for (PathList p : pathList) {
			if (p instanceof CollectionDriverPathList) {
				CollectionDriverPathList l = (CollectionDriverPathList) p;
				displayCollectionDriverPathList(l);
			}
		}
		logger.info("PATHLIST************* end");
		logger.info("file-grouping . enabled : "
				+ driver.getFileGroupingParameter().isFileGroupEnable());
		logger.info("file-grouping . grouping-type : ENUM * SFTP * "
				+ driver.getFileGroupingParameter().getGroupingType());
		logger.info("file-grouping . for-duplicate : "
				+ driver.getFileGroupingParameter().isEnableForDuplicate());
		logger.info("file-fetch-rule . enabled : "
				+ driver.getMyFileFetchParams().isFileFetchRuleEnabled());
		logger.info("file-fetch-rule . file-fetch-type : "
				+ driver.getMyFileFetchParams().getFileFetchType());
		logger.info("file-fetch-rule . file-fetch-interval : "
				+ driver.getMyFileFetchParams().getFileFetchIntervalMin());
		logger.info("file-fetch-rule . time-zone : "
				+ driver.getMyFileFetchParams().getTimeZone());
		logger.info("MigrationApp.displayFTPCollectionDriver() ***** end");
	}

	public void displayCollectionDriverPathList(CollectionDriverPathList l) {
		logger.info("readFilePath : " + l.getReadFilePath());
		logger.info("readFilenamePrefix : " + l.getReadFilenamePrefix());
		logger.info("readFilenameSuffix : " + l.getReadFilenameSuffix());
		logger.info("maxFilesCountAlert : " + l.getMaxFilesCountAlert());
		logger.info("readFilenameContains : " + l.getReadFilenameContains());
		logger.info("writeFilePath : " + l.getWriteFilePath());
		logger.info("writeFilenamePrefix : " + l.getWriteFilenamePrefix());
		logger.info("remoteFileAction : " + l.getRemoteFileAction());
		logger.info("remoteFileActionParamName : "
				+ l.getRemoteFileActionParamName());
		logger.info("remoteFileActionValue : " + l.getRemoteFileActionValue());
		logger.info("dateFormat : " + l.getDateFormat());
		logger.info("position : " + l.getPosition());
		logger.info("startIndex : " + l.getStartIndex());
		logger.info("endIndex : " + l.getEndIndex());
		logger.info("fileGrepDateEnabled : " + l.getFileGrepDateEnabled());
		logger.info("seqStartIndex : " + l.getSeqStartIndex());
		logger.info("seqEndIndex : " + l.getSeqEndIndex());
		logger.info("maxCounterLimit : " + l.getMaxCounterLimit());
		logger.info("fileSeqAlertEnabled : " + l.getFileSeqAlertEnabled());
	}

	public void displayNatflowCollectionService(NetflowCollectionService service) {
		logger.info("serverIp : " + service.getServerIp());
		logger.info("startup-mode : "
				+ service.getSvcExecParams().getStartupMode());
		logger.info("sktRcvBufferSize : " + service.getSktRcvBufferSize());
		logger.info("sktSendBufferSize : " + service.getSktSendBufferSize());
		logger.info("svcExecParams.queueSize : "
				+ service.getSvcExecParams().getQueueSize());
		logger.info("svcExecParams.minThread : "
				+ service.getSvcExecParams().getMinThread());
		logger.info("svcExecParams.maxThread : "
				+ service.getSvcExecParams().getMaxThread());
		logger.info("parallelFileWriteCount : "
				+ service.getParallelFileWriteCount());
		logger.info("maxPktSize : " + service.getMaxPktSize());
		logger.info("maxWriteBufferSize : " + service.getMaxWriteBufferSize());
		logger.info("readTemplateOnInit : " + service.isReadTemplateOnInit());
		logger.info("optionTemplateId : " + service.getOptionTemplateId());
		logger.info("optionTemplateKey : " + service.getOptionTemplateKey());
		logger.info("optionTemplateValue : " + service.getOptionTemplateValue());
		logger.info("optionCopytoTemplateId : "
				+ service.getOptionCopytoTemplateId());
		logger.info("optionCopyTofield : " + service.getOptionCopyTofield());
		logger.info("optionTemplateEnable : "
				+ service.isOptionTemplateEnable());
		logger.info("enableParallelBinaryWrite : "
				+ service.isEnableParallelBinaryWrite());
		List<NetflowClient> clients = service.getNetFLowClientList();
		int i = 0;
		for (NetflowClient client : clients) {
			++i;
			logger.info("Client : " + i);
			logger.info("clientIpAddress : " + client.getClientIpAddress());
			logger.info("clientPort : " + client.getClientPort());
			logger.info("fileNameFormat : " + client.getFileNameFormat());
			logger.info("appendFileSequenceInFileName : "
					+ client.isAppendFileSequenceInFileName());
			logger.info("minFileSeqValue : " + client.getMinFileSeqValue());
			logger.info("maxFileSeqValue : " + client.getMaxFileSeqValue());
			logger.info("outFileLocation : " + client.getOutFileLocation());
			logger.info("bkpBinaryfileLocation : "
					+ client.getBkpBinaryfileLocation());
			logger.info("timeLogRollingUnit : "
					+ client.getTimeLogRollingUnit());
			logger.info("volLogRollingUnit : " + client.getVolLogRollingUnit());
			logger.info("inputCompressed : " + client.isInputCompressed());
			logger.info("alertInterval : " + client.getAlertInterval());
			logger.info("snmpAlertEnable : " + client.isSnmpAlertEnable());
		}
	}

	*//**
	 * This method initializes map for xml, jaxb object, sm object
	 *//*
	private void initializeMapForXmlToClass() {
		logger.info("MigrationApp.initializeMapForXmlToClass() ****");
		Map<String, String> innerMap = new HashMap<>();
		innerMap.put(MAP_SM_CLASS_KEY,
				"com.elitecore.sm.services.model.CollectionService");
		innerMap.put(MAP_JAXB_CLASS_KEY,
				"com.elitecore.sm.migration.model.CollectionServiceEntity");
		map.put(COLLECTION_SERVICE_XML, innerMap);

		Map<String, String> innerMap1 = new HashMap<>();
		innerMap1.put(MAP_SM_CLASS_KEY,
				"com.elitecore.sm.drivers.model.LocalCollectionDriver");
		innerMap1.put(MAP_JAXB_CLASS_KEY,
				"com.elitecore.sm.migration.model.LocalCollectionDriverEntity");
		map.put(LOCAL_COLLECTION_DRIVER_XML, innerMap1);

		Map<String, String> innerMap2 = new HashMap<>();
		innerMap2.put(MAP_SM_CLASS_KEY,
				"com.elitecore.sm.drivers.model.FTPCollectionDriver");
		innerMap2.put(MAP_JAXB_CLASS_KEY,
				"com.elitecore.sm.migration.model.FTPCollectionDriverEntity");
		map.put(FTP_COLLECTION_DRIVER_XML, innerMap2);

		Map<String, String> innerMap3 = new HashMap<>();
		innerMap3.put(MAP_SM_CLASS_KEY,
				"com.elitecore.sm.drivers.model.SFTPCollectionDriver");
		innerMap3.put(MAP_JAXB_CLASS_KEY,
				"com.elitecore.sm.migration.model.SFTPCollectionDriverEntity");
		map.put(SFTP_COLLECTION_DRIVER_XML, innerMap3);

		Map<String, String> innerMap4 = new HashMap<>();
		innerMap4.put(MAP_SM_CLASS_KEY,
				"com.elitecore.sm.services.model.NetflowCollectionService");
		innerMap4.put(MAP_JAXB_CLASS_KEY,
				"com.elitecore.sm.migration.model.NetflowCollectionServiceEntity");
		map.put(NATFLOW_COLLECTION_SERVICE_XML, innerMap4);

		Map<String, String> innerMap5 = new HashMap<>();
		innerMap5.put(MAP_SM_CLASS_KEY,
				"com.elitecore.sm.drivers.model.SFTPDistributionDriver");
		innerMap5.put(MAP_JAXB_CLASS_KEY,
				"com.elitecore.sm.migration.model.SftpDistributionDriver");
		map.put(SFTP_DISTRIBUTION_DRIVER_XML, innerMap5);

		Map<String, String> asciiComposerPlugin = new HashMap<>();
		asciiComposerPlugin.put(MAP_SM_CLASS_KEY,
				"com.elitecore.sm.composer.model.ASCIIComposerMapping");
		asciiComposerPlugin.put(MAP_JAXB_CLASS_KEY,
				"com.elitecore.sm.migration.model.AsciiComposerPluginEntity");
		map.put(ASCII_COMPOSER_PLUGIN_XML, asciiComposerPlugin);
	}

	*//**
	 * This method returns Class from class name
	 * 
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 *//*
	private Class<?> getClassFromName(String className) {
		logger.info("MigrationApp.getClassFromName() : className : "
				+ className);
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return clazz;
	}

	public ServiceType getServiceTypeByAlias(String serviceAlias) {
		logger.info("MigrationApp.getServiceTypeByAlias() : serviceAlias : "
				+ serviceAlias);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(ServiceType.class);
		criteria.add(Restrictions.eq(ALIAS, serviceAlias));

		@SuppressWarnings("unchecked")
		List<ServiceType> listServiceType = criteria.list();

		session.close();

		return (!listServiceType.isEmpty()) ? listServiceType.get(0) : null;
	}

	public DriverType getDriverTypeByAlias(String driverAlias) {
		logger.info("MigrationApp.getDriverTypeByAlias() : driverAlias : "
				+ driverAlias);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(DriverType.class);
		criteria.add(Restrictions.eq(ALIAS, driverAlias));
		@SuppressWarnings("unchecked")
		List<DriverType> listDriverType = criteria.list();
		session.close();

		return (!listDriverType.isEmpty()) ? listDriverType.get(0) : null;
	}

	public AgentType getAgentTypeByAlias(String alias) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(AgentType.class);
		criteria.add(Restrictions.eq(alias, alias));
		AgentType agentType = (criteria.list() != null && !criteria.list()
				.isEmpty()) ? (AgentType) (criteria.list()).get(0) : null;
		session.close();
		return agentType;
	}

	public ServerInstance getServerInstance(int serverInsId) {
		logger.info("MigrationApp.createServerInstance()");
		Session session = HibernateUtil.getSessionFactory().openSession();

		ServerInstance serverInsFromDB = (ServerInstance) session.get(
				ServerInstance.class, serverInsId);
		if (serverInsFromDB != null) {
			return serverInsFromDB;
		} else {

			session.beginTransaction();

			ServerType cgfType = (ServerType) session.get(ServerType.class, 3);

			Server testServer = new Server();
			testServer.setName("test trng Server");
			try {
				testServer.setIpAddress(InetAddress.getLocalHost()
						.getHostAddress());
			} catch (UnknownHostException e) {
				logger.error(e.getMessage(), e);
			}
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

			ServerInstance mySrvInstance = new ServerInstance("SRVINS", 5001,
					testServer, "/home/group1/crestelsetup/crestelpengine",
					"/home/group1/jrockit-jdk1.6.0_45-R28.2.7-4.1.0", dsConfig,
					logsDetail);

			Agent fileRenameAgent = new Agent();
			// fileRenameAgent.setTypeOfAgent("FILE_RENAME_AGENT");
			fileRenameAgent.setServerInstance(mySrvInstance);
			fileRenameAgent
					.setAgentType(getAgentTypeByAlias("FILE_RENAME_AGENT"));

			List<Agent> agentList = new ArrayList<>();
			agentList.add(fileRenameAgent);
			mySrvInstance.setAgentList(agentList);

			session.save(dsConfig);
			session.save(testServer);

			session.save(mySrvInstance);
			session.save(fileRenameAgent);
			session.getTransaction().commit();
			session.close();
			return mySrvInstance;
		}
	}

	public void initializeDozerMappingFiles() {
		List<String> mappingFiles = new ArrayList<>();
		mappingFiles.add("com/elitecore/sm/migration/mapping/DozerMapping.xml");
		mapper.setMappingFiles(mappingFiles);
	}

	private Object executeCollectionDriver(String directoryPath, String name,
			String xmlName) {
		logger.info("MigrationApp.executeCollectionDriver() ***  collection driver execution starts ***");
		Class<?> clazz = getClassFromName(map.get(xmlName).get(
				MAP_JAXB_CLASS_KEY));
		Object smCollectionDriverObject = null;
		try {
			ResponseObject responseObject = getUnmarshalObjectFromFile(
					new File(directoryPath + File.separator + name), clazz);
			Object jaxbCollectionDriverObject = responseObject.getObject();

			Class<?> destinationClass = getClassFromName(map.get(xmlName).get(
					MAP_SM_CLASS_KEY));
			smCollectionDriverObject = convertJaxbToSMObjectWithConverter(
					jaxbCollectionDriverObject, destinationClass);
			smCollectionDriverObject = loadCollectionDriverPathList(
					jaxbCollectionDriverObject, smCollectionDriverObject);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return smCollectionDriverObject;
	}

	public Object loadCollectionDriverPathList(
			Object jaxbCollectionDriverObject, Object smCollectionDriverObject) {
		if (jaxbCollectionDriverObject instanceof LocalCollectionDriverEntity) {
			LocalCollectionDriverEntity jaxbLocalCollectionDriver = (LocalCollectionDriverEntity) jaxbCollectionDriverObject;
			return getUpdatedDriverWithPathList(jaxbLocalCollectionDriver
					.getPathList().getPath(), smCollectionDriverObject,
					LOCAL_COLLECTION_DRIVER);
		} else if (jaxbCollectionDriverObject instanceof FTPCollectionDriverEntity) {
			FTPCollectionDriverEntity jaxbFtpCollectionDriver = (FTPCollectionDriverEntity) jaxbCollectionDriverObject;
			return getUpdatedDriverWithPathList(jaxbFtpCollectionDriver
					.getPathList().getPath(), smCollectionDriverObject,
					FTP_COLLECTION_DRIVER);
		} else if (jaxbCollectionDriverObject instanceof SFTPCollectionDriverEntity) {
			SFTPCollectionDriverEntity jaxbSftpCollectionDriver = (SFTPCollectionDriverEntity) jaxbCollectionDriverObject;
			return getUpdatedDriverWithPathList(jaxbSftpCollectionDriver
					.getPathList().getPath(), smCollectionDriverObject,
					SFTP_COLLECTION_DRIVER);
		}

		return null;
	}

	private Drivers getUpdatedDriverWithPathList(List<?> jaxbPathList,
			Object smDriverObject, String driverType) {
		List<PathList> driverPathList = getCollectionDriverPathListFromJaxbPathList(jaxbPathList);
		Drivers smDriver;
		if (smDriverObject instanceof Drivers) {
			smDriver = (Drivers) smDriverObject;
			smDriver.setDriverPathList(driverPathList);
			smDriver.setDriverType(getDriverTypeByAlias(driverType));
			return smDriver;
		}
		return null;

	}

	public List<PathList> getCollectionDriverPathListFromJaxbPathList(
			List<?> jaxbPathList) {
		Class<?> destinationClass = getClassFromName("com.elitecore.sm.pathlist.model.CollectionDriverPathList");
		List<PathList> collectionDriverPathList = new ArrayList<>();
		for (Object jaxbPath : jaxbPathList) {
			Object smPathList = convertJaxbToSMObject(jaxbPath,
					destinationClass);
			if (smPathList instanceof CollectionDriverPathList) {
				CollectionDriverPathList smCollectionDriverPathList = (CollectionDriverPathList) smPathList;
				smCollectionDriverPathList
						.setName(getDynamicName("Mig_Collection_PathList_"));

				collectionDriverPathList.add(smCollectionDriverPathList);
				logger.info("MigrationApp.executeLocalCollectionDriver() : ***JAXB PathList to SM PathList ***");
				displayCollectionDriverPathList(smCollectionDriverPathList);
			}
		}
		return collectionDriverPathList;
	}

	private Map<String, Object> getSMAndJaxbObjectFromXml(String directoryPath,
			String xmlName) {
		Map<String, Object> mapOfObjects = new HashMap<>();
		Map<String, Class<?>> mapOfClasses = getClasses(xmlName);
		Object object = null;
		try {
			ResponseObject responseObject = getUnmarshalObjectFromFile(
					new File(directoryPath + File.separator + xmlName),
					mapOfClasses.get(MAP_JAXB_CLASS_KEY));
			if (responseObject.isSuccess()) {
				mapOfObjects
						.put(MAP_JAXB_CLASS_KEY, responseObject.getObject());

				object = convertJaxbToSMObjectWithConverter(
						responseObject.getObject(),
						mapOfClasses.get(MAP_SM_CLASS_KEY));

				mapOfObjects.put(MAP_SM_CLASS_KEY, object);
			} else {
				mapOfObjects.put(MAP_JAXB_CLASS_KEY, null);
				mapOfObjects.put(MAP_SM_CLASS_KEY, null);
			}

		} catch (Exception e) {
			mapOfObjects.put(MAP_JAXB_CLASS_KEY, null);
			mapOfObjects.put(MAP_SM_CLASS_KEY, null);
			logger.error(e.getMessage(), e);
		}
		return mapOfObjects;
	}

	private Map<String, Class<?>> getClasses(String xmlName) {
		Map<String, Class<?>> mapOfClass = new HashMap<>();
		Class<?> jaxbClass = getClassFromName(map.get(xmlName).get(
				MAP_JAXB_CLASS_KEY));
		Class<?> smClass = getClassFromName(map.get(xmlName).get(
				MAP_SM_CLASS_KEY));
		mapOfClass.put(MAP_JAXB_CLASS_KEY, jaxbClass);
		mapOfClass.put(MAP_SM_CLASS_KEY, smClass);
		return mapOfClass;
	}
*/
}
