/**
 * 
 */
package com.elitecore.sm.test;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.agent.model.PacketStatisticsAgent;
import com.elitecore.sm.agent.model.ServicePacketStatsConfig;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.serverinstance.model.LogsDetail;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.CollectionService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceType;

/**
 * @author vandana.awatramani
 *
 */
public class TestAgent {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			
			Server testServer = new Server();
			testServer.setName("test trng Server");
			testServer.setIpAddress("127.0.0.1");
			testServer.setDescription("testing entity defs");
			

			System.out.println(testServer);
			
			
			// int srvInsId=createServerInstance(1);
			//associatePacketStats(1);
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			System.exit(1);
		}
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

			ServerType cgfType = (ServerType) session.get(ServerType.class, 2);

			Server testServer = new Server();
			testServer.setName("test trng Server");
			testServer.setIpAddress("127.0.0.1");
			testServer.setDescription("testing entity defs");
			testServer.setServerType(cgfType);

			System.out.println(testServer);
			
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

			ServiceType collSvcType = (ServiceType) session.get(
					ServiceType.class, 1);

			Service collService = new CollectionService();
			// below are the properties first used in making a service, rest all
			// config is done later
			collService.setSvctype(collSvcType);
			collService.setName("test netflow collection service ddd1");
			collService.setServerInstance(mySrvInstance);

			List<Service> svcList = null;
			if (mySrvInstance.getServices() != null) {
				svcList = mySrvInstance.getServices();

			} else
				svcList = new ArrayList<Service>();

			svcList.add(collService);
			mySrvInstance.setServices(svcList);
			mySrvInstance.setSyncChildStatus(false);
			mySrvInstance.setSyncSIStatus(false);

			mySrvInstance
					.getLogsDetail()
					.setLogPathLocation(
							"/home/group1/crestelsetup/crestelpengine/modules/mediation/si_test_logs");

			session.save(dsConfig);
			session.save(testServer);
			session.save(mySrvInstance);
			session.save(collService);

			PacketStatisticsAgent pktAgent = new PacketStatisticsAgent();

			pktAgent.setInitialDelay(30);
			pktAgent.setServerInstance(mySrvInstance);
		//	pktAgent.setTypeOfAgent("Packet Agent");
			pktAgent.setExecutionInterval(60);

			session.save(mySrvInstance);
			session.save(pktAgent);
			session.getTransaction().commit();
			session.close();
			return mySrvInstance.getId();
		}
	}

	public static void associatePacketStats(int serverInsId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		ServerInstance serverInsFromDB = (ServerInstance) session.get(
				ServerInstance.class, serverInsId);

		List<Agent> agentList = serverInsFromDB.getAgentList();
		PacketStatisticsAgent pktAgent = (PacketStatisticsAgent) agentList
				.get(0);

		List<Service> serviceList = serverInsFromDB.getServices();
		List<ServicePacketStatsConfig> servicePktConfigList = new ArrayList<>();

		for (Service svc : serviceList) {
			System.out.println(svc.getName());
			ServicePacketStatsConfig svcPktConfig = new ServicePacketStatsConfig();
			svcPktConfig.setAgent(pktAgent);
			svcPktConfig.setService(svc);
			svcPktConfig.setEnable(true);
			servicePktConfigList.add(svcPktConfig);
			session.save(svcPktConfig);
		}

		pktAgent.setServiceList(servicePktConfigList);

		agentList.add(pktAgent);
		serverInsFromDB.setAgentList(agentList);

		session.update(serverInsFromDB);
		session.update(pktAgent);
		session.getTransaction().commit();
		session.close();

	}

}
