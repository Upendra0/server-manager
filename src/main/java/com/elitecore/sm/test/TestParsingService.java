/**
 * 
 */
package com.elitecore.sm.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.common.model.FileGroupEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.parser.model.NATFlowParserMapping;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.pathlist.model.FileGroupingParameter;
import com.elitecore.sm.pathlist.model.FileGroupingParameterParsing;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.serverinstance.model.LogsDetail;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.CDRDateSummaryTypeEnum;
import com.elitecore.sm.services.model.CDRFileDateTypeEnum;
import com.elitecore.sm.services.model.ParsingService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceExecutionParams;
import com.elitecore.sm.services.model.ServiceType;

/**
 * @author vandana.awatramani
 *
 */
@SuppressWarnings( {"unused","unchecked"})
public class TestParsingService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			int serverInsId = 1;
	//		 serverInsId = createServerInstance(serverInsId);
			System.out.println("TestParsingService.main()" +serverInsId);

	//		 int serviceID = createParsingPage1(serverInsId);
//			 System.out.println(" created service id is " + serviceID);
			 
	//		 boolean status = createParsingServiceConfigPage2(serviceID);
	//		 System.out.println(" Page 2 params status " + status);
			 
	//		 int parserId = createParsingPathListPage3(serviceID);
	//		 System.out.println(" created parser id is " + parserId);
			 
			 int parserMappingId=createParserMappingPage4(104,3);
	//		 System.out.println(" created parser Mapping id is " + parserMappingId);
			 
	//		 createParserAttributePage5(104, parserMappingId);
			 
			 readParser();
			
		} catch (Exception e) {
			System.out.println("App.main() caught ex ");
			e.printStackTrace();
		} finally {
			// HibernateUtil.shutdown();
			System.exit(1);
		}

	}

	public static int createParsingPage1(int serverInsId) {
		System.out.println("TestParsingService.createParsingPage1()");
		int svcId = 0;
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		ServerInstance serverInsFromDB = (ServerInstance) session.get(ServerInstance.class, serverInsId);

		ServiceType parsingSvcType = (ServiceType) session.get(ServiceType.class, 2);

		ParsingService parsingService = new ParsingService();
	
		parsingService.setSvctype(parsingSvcType);
		parsingService.setName("parsing svc 1");
		parsingService.setServerInstance(serverInsFromDB);

		List<Service> svcList = null;
		if (serverInsFromDB.getServices() != null) {
			svcList = serverInsFromDB.getServices();

		} else
			svcList = new ArrayList<Service>();

		svcList.add(parsingService);
		serverInsFromDB.setServices(svcList);
		serverInsFromDB.setSyncChildStatus(false);
		serverInsFromDB.setSyncSIStatus(false);
		session.save(serverInsFromDB);
		session.save(parsingService);
		session.getTransaction().commit();
		session.close();
		svcId = parsingService.getId();

		return svcId;
	}

	public static boolean createParsingServiceConfigPage2(int parsingSvcId) {
		boolean executionStatus = true;
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			ParsingService parsingSvc = (ParsingService) session.get(ParsingService.class, parsingSvcId);

			ServiceExecutionParams svcExecParams = new ServiceExecutionParams();
			svcExecParams.setMinThread(13);
			svcExecParams.setMaxThread(15);

			parsingSvc.setSvcExecParams(svcExecParams);

			FileGroupingParameterParsing parseFilegrpingParam = new FileGroupingParameterParsing();
			parseFilegrpingParam.setFileGroupEnable(true);
			parseFilegrpingParam.setGroupingType(FileGroupEnum.DAY.getFileGroupEnum());

			parsingSvc.setFileGroupingParameter(parseFilegrpingParam);
	
			// parameters for file date summary
			parsingSvc.setDateFieldForSummary("General1");
			parsingSvc.setEqualCheckValue("123");
			parsingSvc.setEqualCheckField("456");
			//parsingSvc.setFileCopyFolders("/home/elitecore");
			parsingSvc.setOverrideFileDateType(CDRFileDateTypeEnum.MAXIMUM.getValue());
			parsingSvc.setTypeForSummary(CDRDateSummaryTypeEnum.DAY);
			parsingSvc.setOverrideFileDateEnabled(false);

			session.save(parseFilegrpingParam);
			session.save(parsingSvc);

			session.getTransaction().commit();

			session.close();

		} catch (Exception e) {
			e.printStackTrace();
			executionStatus = false;
		}
		return executionStatus;

	}

	public static int createParsingPathListPage3(int parsingSvcId) {
		System.out.println("TestParsingService.createParsingPage3()");
		
		int parserId=0;
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			ParsingService parsingSvc = (ParsingService) session.get(ParsingService.class, parsingSvcId);

			// add multiple path lists for the selected parser.
			
			  ParsingPathList path1 = new ParsingPathList();
					  
			  path1.setName("VaPL 1");;
			  path1.setReadFilePath("/opt");
			  path1.setStatus(StateEnum.ACTIVE);
			  //path1.setArchivePath("/home");
			 
			 path1.setService(parsingSvc);
			 
			 List<PathList> svcList =parsingSvc.getSvcPathList();
			 
			 if (svcList == null) { 
				 svcList = new ArrayList<PathList>();
			 }
			 
			 svcList.add(path1); 
			 parsingSvc.setSvcPathList(svcList);
			 
			 session.save(path1);
			 session.save(parsingSvc);
		
			PluginTypeMaster pluginType = (PluginTypeMaster) session.get(PluginTypeMaster.class, 7);
					
			Parser testParserWrapper= new Parser();
			
			testParserWrapper.setParsingPathList(path1);
			testParserWrapper.setName("VA Test Wrapper 1");
			testParserWrapper.setParserType(pluginType);
			testParserWrapper.setWriteFilePath("/data");
			testParserWrapper.setReadFilenamePrefix("abc");
			testParserWrapper.setReadFilenameSuffix("abc");
			testParserWrapper.setReadFilenameContains("abc");
			testParserWrapper.setReadFilenameExcludeTypes("abc");
			
			List<Parser> parserList=path1.getParserWrappers();
			if(parserList==null){
				parserList=new ArrayList<Parser>();
			}
			parserList.add(testParserWrapper);
			path1.setParserWrappers(parserList);
					
			session.save(testParserWrapper);
			session.save(path1);
			session.getTransaction().commit();
			parserId=testParserWrapper.getId();
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return parserId;
	}

	public static int createParserMappingPage4(int parsingSvcId,int parserId) {
		System.out.println("TestParsingService.createParsingPage4()");
		
		int parserMappingid=0;
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			 ParsingService parsingSvc = (ParsingService) session.get(ParsingService.class, parsingSvcId);
			 PluginTypeMaster pluginType = (PluginTypeMaster) session.get(PluginTypeMaster.class, 7);
			 Device d= (Device) session.load(Device.class, 1);
				
			 Parser parser=(Parser)session.get(Parser.class,parserId);
				
			 NATFlowParserMapping natFlowParser = new NATFlowParserMapping();
			 
		//	 ParserMapping natFlowParser=new RegexParserMapping();

			natFlowParser.setCreatedDate(new Date());
			natFlowParser.setLastUpdatedDate(new Date());
			natFlowParser.setLastUpdatedByStaffId(1);
			natFlowParser.setCreatedByStaffId(1);
			natFlowParser.setName("Natflow Plugin");
			natFlowParser.setStatus(StateEnum.ACTIVE);
		natFlowParser.setReadTemplatesInitially(true);
			natFlowParser.setParserType(pluginType);
			natFlowParser.setOptionTemplateEnable(false);
			natFlowParser.setOptionCopyTofield("12");
			natFlowParser.setOptionCopytoTemplateId("271");
			
			List<Parser> parserList =natFlowParser.getParserWrapper();
			
			 if (parserList == null) { 
				 parserList = new ArrayList<Parser>(); 
			}
			parserList.add(parser);
			natFlowParser.setParserWrapper(parserList);
			natFlowParser.setDevice(d);			
			
			parser.setParserMapping(natFlowParser);

			session.save(natFlowParser);
			session.save(parser);
			session.getTransaction().commit();
			parserMappingid=natFlowParser.getId();
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return parserMappingid;
	}
	
	
	public static boolean createParserAttributePage5(int parsingSvcId,int parserMappingid) {
		System.out.println("TestParsingService.createParsingPage5()");
		boolean executionStatus = true;
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			NATFlowParserMapping natFlowParser=(NATFlowParserMapping)session.get(NATFlowParserMapping.class, parserMappingid);
				
				if(natFlowParser instanceof NATFlowParserMapping){
					System.out.println("NATFlowParserMapping found");
				}
			
			 ParserAttribute parseAttr2 = new ParserAttribute(1,"general-2",
			  "enddate", "", " ", "enddate","");
			 parseAttr2.setParserMapping(natFlowParser);
			  
			  ParserAttribute parseAttr3 = new ParserAttribute(2,"general-3",
			  "areaid", "", " ","areaid","");
			  parseAttr3.setParserMapping(natFlowParser);
			  
			  ParserAttribute parseAttr4 = new ParserAttribute(3,"general-4",
			  "publicip", "", " ","publicip","");
			  parseAttr4.setParserMapping(natFlowParser);
			  
			  ParserAttribute parseAttr5 = new ParserAttribute(4,"general-5",
			  "privateip", "", " ","privateip","");
			  parseAttr5.setParserMapping(natFlowParser);
			  
			  ParserAttribute parseAttr6 = new ParserAttribute(5,"general-6",
			  "url", "", " ", "url","");
			  parseAttr6.setParserMapping(natFlowParser);
			  
			  ParserAttribute parseAttr7 = new ParserAttribute(6,"general-7",
			  "callduration", "", " ","callduration","");
			  parseAttr7.setParserMapping(natFlowParser);
			  
			  ParserAttribute parseAttr8 = new ParserAttribute(7,"general-8",
			  "datausage", "", " ","datausage","");
			  parseAttr8.setParserMapping(natFlowParser);
			  
			  ParserAttribute parseAttr9 = new ParserAttribute(8,"general-9",
			  "circlename", "", " ","circlename","");
			  parseAttr9.setParserMapping(natFlowParser);
			 

			List<ParserAttribute> attrList =natFlowParser.getParserAttributes();
			
			  if (attrList == null) { 
				  attrList = new ArrayList<ParserAttribute>(); 
				  }
			
			  attrList.add(parseAttr2); attrList.add(parseAttr3);
			  attrList.add(parseAttr4); attrList.add(parseAttr5);
			  attrList.add(parseAttr6); attrList.add(parseAttr7);
			  attrList.add(parseAttr8); attrList.add(parseAttr9);
			 

			  natFlowParser.setParserAttributes(attrList);

			
			  session.save(parseAttr2); session.save(parseAttr3);
			  session.save(parseAttr4); session.save(parseAttr5);
			  session.save(parseAttr6); session.save(parseAttr7);
			  session.save(parseAttr8); session.save(parseAttr9);
			 

			session.save(natFlowParser);
			session.getTransaction().commit();
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
			executionStatus = false;
		}
		return executionStatus;
	}

	public static int createServerInstance(int serverInsId) {
		System.out.println("TestEntities.createServerInstance()");
		Session session = HibernateUtil.getSessionFactory().openSession();

		ServerInstance serverInsFromDB = (ServerInstance) session.get(
				ServerInstance.class, serverInsId);
		if (serverInsFromDB != null) {
			return serverInsId;
		} else {

			session.beginTransaction();

			ServerType cgfType = new ServerType();
			cgfType.setAlias("alias");
			cgfType.setName("cgf");
			cgfType.setDescription("serverTypeDescription");

			session.save(cgfType);

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
			// before
			// instance creation.

			ServerInstance mySrvInstance = new ServerInstance(/*"SRVINS",
					 5556, testServer,
					"/home/group1/crestelsetup/crestelpengine",
					"/home/group1/jrockit-jdk1.6.0_45-R28.2.7-4.1.0", dsConfig,
					logsDetail*/);

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

	 public static void readParser() {

		  Session session = HibernateUtil.getSessionFactory().openSession();

		  try {
		   File inputxml = new File("F:\\ip.xml");
		   JAXBContext jaxbContext;
		   
		   System.out.println("-------------------------------------Fetch pathlist---------------------");
		   Query qry1 = session.createQuery(" from PathList where id=4");
		   
		   List<PathList> path=qry1.list();
		   
		   for(PathList p:path){
			   ParsingPathList parsingp=(ParsingPathList)p;
		   }
		   

		   jaxbContext = JAXBContext.newInstance(Parser.class);

		   Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		   jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		   //Query qry = session.createQuery(" from Parser where id=3");
		   
		   Criteria criteria=session.createCriteria(Parser.class);
		   criteria.add(Restrictions.eq("id", 3));

		   List<Parser> parserList = criteria.list();

		   for (Parser p : parserList) {
		    System.out.println("Testing  " + p.getClass());
		    System.out.println("Testing  class " + p);
		//    Hibernate.initialize(p);	
		    NATFlowParserMapping natflow=(NATFlowParserMapping)p.getParserMapping();

		    	
		    //	System.out.println("netflow paroperty " +natflow.getOptionCopyTofield());
		    

		    jaxbMarshaller.marshal(p, inputxml);
		    jaxbMarshaller.marshal(p, System.out);

		    /*BufferedReader br = new BufferedReader(new FileReader(inputxml));
		    String fileContent = null;
		    StringBuffer sb = new StringBuffer();
		    while ((fileContent = br.readLine()) != null) {
		     sb.append(fileContent);
		    }
		    System.out.println("JAXB XML is" + sb.toString()); */

		   }
		  } catch (Exception e) {
		   
		   e.printStackTrace();
		  }

		 }

}
