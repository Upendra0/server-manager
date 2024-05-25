/**
 * 
 */
package com.elitecore.sm.integration.engine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.elitecore.core.util.mbean.data.config.CrestelNetConfigurationData;
import com.elitecore.core.util.mbean.data.config.CrestelNetServerData;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.datasource.model.DataSourceConfig;

/**
 * @author Sunil Gulabani Jul 29, 2015
 */
public class ParseCrestelNetServerData {

	protected Logger logger = Logger.getLogger(this.getClass().getName());

	private CrestelNetServerData engineServerData;

	/**
	 * Default Constructor
	 */
	public ParseCrestelNetServerData(CrestelNetServerData engineServerData) {
		this.engineServerData = engineServerData;
	}

	/**
	 * Provides Data Source Config Details Map
	 * 
	 * @return
	 */
	public List<DataSourceConfig> getDataSourceConfig() {
		// DataSourceConfig dsConfig = null;
		List<DataSourceConfig> dsConfigList = null;
		String mediationXML = null;
		String databaseXML = null;
		if (engineServerData != null && engineServerData.getNetConfigurationList() != null) {
			for (CrestelNetConfigurationData configuation : engineServerData.getNetConfigurationList()) {
				if (configuation.getNetConfigurationKey().equals(EngineConstants.MEDIATION_SERVER)) {
					mediationXML = new String(configuation.getNetConfigurationData());
				} else if (configuation.getNetConfigurationKey().equals(EngineConstants.ORACLE_DATABASE_CONFIGURATION)) {
					databaseXML = new String(configuation.getNetConfigurationData());
				}

				if (mediationXML != null && !mediationXML.equals("") && databaseXML != null && !databaseXML.equals("")) {
					logger.debug("mediationXML:\n" + mediationXML);
					logger.debug("-----------------------------------------------------");
					logger.debug("databaseXML:\n" + databaseXML);
					dsConfigList = parseDataSourceConfigXML(mediationXML, databaseXML);
					break;
				}
			}
		}
		return dsConfigList;
	}

	/**
	 * Parses the Mediation Server XML and Datasource XML to find default
	 * Datasource configured.
	 * 
	 * @param mediationXMLData
	 * @param dataSourceXMLData
	 * @return
	 */
	public List<DataSourceConfig> parseDataSourceConfigXML(String mediationXMLData, String dataSourceXMLData) {
		DataSourceConfig dsConfig = (DataSourceConfig) SpringApplicationContext.getBean(DataSourceConfig.class);
		List<DataSourceConfig> dsConfigList = new ArrayList<>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//NOSONAR
			DocumentBuilder builder = factory.newDocumentBuilder();
			ByteArrayInputStream input = new ByteArrayInputStream(mediationXMLData.getBytes("UTF-8"));
			Document doc = builder.parse(input);
			XPath xPath = XPathFactory.newInstance().newXPath();

			String expression = "/server-configuration/database-ds";
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
			String engineDatasourceName = null;
			String smDatasourceName = null;
			String iploggerDatasourceName = null;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nNode = nodeList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					engineDatasourceName = eElement.getElementsByTagName("engine-datasource-name").item(0).getFirstChild().getNodeValue();
					smDatasourceName = eElement.getElementsByTagName("servermanager-datasource-name").item(0).getFirstChild().getNodeValue();
					iploggerDatasourceName = eElement.getElementsByTagName("iplogger-datasource-name").item(0).getFirstChild().getNodeValue();
					break;
				}
			}
			logger.debug("engineDatasourceName: " + engineDatasourceName);
			// find datasourceName in dataSourceXMLData
			input = new ByteArrayInputStream(dataSourceXMLData.getBytes("UTF-8"));
			doc = builder.parse(input);
			expression = "/datasources/datasource";
			nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nNode = nodeList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					int length = eElement.getElementsByTagName("datasource-name").getLength();
					for (int j = 0; j < length; j++) {
						if (eElement.getElementsByTagName("datasource-name").item(j).getFirstChild().getNodeValue().equals(engineDatasourceName)) {
							dsConfig.setName(eElement.getElementsByTagName("datasource-name").item(j).getFirstChild().getNodeValue());
							dsConfig.setType(eElement.getElementsByTagName("datasource-type").item(j).getFirstChild().getNodeValue());
							dsConfig.setConnURL(eElement.getElementsByTagName("connection-url").item(j).getFirstChild().getNodeValue());
							dsConfig.setUsername(eElement.getElementsByTagName("user-name").item(j).getFirstChild().getNodeValue());
							dsConfig.setPassword(eElement.getElementsByTagName("password").item(j).getFirstChild().getNodeValue());
							dsConfig.setMinPoolSize(Integer.parseInt(eElement.getElementsByTagName("minimum-pool-size").item(j).getFirstChild()
									.getNodeValue()));
							dsConfig.setMaxPoolsize(Integer.parseInt(eElement.getElementsByTagName("maximum-pool-size").item(j).getFirstChild()
									.getNodeValue()));
							dsConfig.setFailTimeout(eElement.getElementsByTagName("failover-timeout").item(j).getFirstChild().getNodeValue());
							dsConfigList.add(0,dsConfig);
						}
					}
					for (int j = 0; j < length; j++) {
						if (eElement.getElementsByTagName("datasource-name").item(j).getFirstChild().getNodeValue().equals(smDatasourceName)) {
							dsConfig = (DataSourceConfig) SpringApplicationContext.getBean(DataSourceConfig.class);
							dsConfig.setName(eElement.getElementsByTagName("datasource-name").item(j).getFirstChild().getNodeValue());
							dsConfig.setType(eElement.getElementsByTagName("datasource-type").item(j).getFirstChild().getNodeValue());
							dsConfig.setConnURL(eElement.getElementsByTagName("connection-url").item(j).getFirstChild().getNodeValue());
							dsConfig.setUsername(eElement.getElementsByTagName("user-name").item(j).getFirstChild().getNodeValue());
							dsConfig.setPassword(eElement.getElementsByTagName("password").item(j).getFirstChild().getNodeValue());
							dsConfig.setMinPoolSize(Integer.parseInt(eElement.getElementsByTagName("minimum-pool-size").item(j).getFirstChild()
									.getNodeValue()));
							dsConfig.setMaxPoolsize(Integer.parseInt(eElement.getElementsByTagName("maximum-pool-size").item(j).getFirstChild()
									.getNodeValue()));
							dsConfig.setFailTimeout(eElement.getElementsByTagName("failover-timeout").item(j).getFirstChild().getNodeValue());
							dsConfigList.add(1,dsConfig);
						}
					}
					for (int j = 0; j < length; j++) {
						if (eElement.getElementsByTagName("datasource-name").item(j).getFirstChild().getNodeValue().equals(iploggerDatasourceName)) {
							dsConfig = (DataSourceConfig) SpringApplicationContext.getBean(DataSourceConfig.class);
							dsConfig.setName(eElement.getElementsByTagName("datasource-name").item(j).getFirstChild().getNodeValue());
							dsConfig.setType(eElement.getElementsByTagName("datasource-type").item(j).getFirstChild().getNodeValue());
							dsConfig.setConnURL(eElement.getElementsByTagName("connection-url").item(j).getFirstChild().getNodeValue());
							dsConfig.setUsername(eElement.getElementsByTagName("user-name").item(j).getFirstChild().getNodeValue());
							dsConfig.setPassword(eElement.getElementsByTagName("password").item(j).getFirstChild().getNodeValue());
							dsConfig.setMinPoolSize(Integer.parseInt(eElement.getElementsByTagName("minimum-pool-size").item(j).getFirstChild()
									.getNodeValue()));
							dsConfig.setMaxPoolsize(Integer.parseInt(eElement.getElementsByTagName("maximum-pool-size").item(j).getFirstChild()
									.getNodeValue()));
							dsConfig.setFailTimeout(eElement.getElementsByTagName("failover-timeout").item(j).getFirstChild().getNodeValue());
							dsConfigList.add(2,dsConfig);
						}
					}
					for (int j = 0; j < length; j++) {
						if (!(eElement.getElementsByTagName("datasource-name").item(j).getFirstChild().getNodeValue().equals(engineDatasourceName))
								&& !(eElement.getElementsByTagName("datasource-name").item(j).getFirstChild().getNodeValue().equals(smDatasourceName)) 
								&& !(eElement.getElementsByTagName("datasource-name").item(j).getFirstChild().getNodeValue().equals(iploggerDatasourceName))) {
							dsConfig = (DataSourceConfig) SpringApplicationContext.getBean(DataSourceConfig.class);
							dsConfig.setName(eElement.getElementsByTagName("datasource-name").item(j).getFirstChild().getNodeValue());
							dsConfig.setType(eElement.getElementsByTagName("datasource-type").item(j).getFirstChild().getNodeValue());
							dsConfig.setConnURL(eElement.getElementsByTagName("connection-url").item(j).getFirstChild().getNodeValue());
							dsConfig.setUsername(eElement.getElementsByTagName("user-name").item(j).getFirstChild().getNodeValue());
							dsConfig.setPassword(eElement.getElementsByTagName("password").item(j).getFirstChild().getNodeValue());
							dsConfig.setMinPoolSize(Integer.parseInt(eElement.getElementsByTagName("minimum-pool-size").item(j).getFirstChild()
									.getNodeValue()));
							dsConfig.setMaxPoolsize(Integer.parseInt(eElement.getElementsByTagName("maximum-pool-size").item(j).getFirstChild()
									.getNodeValue()));
							dsConfig.setFailTimeout(eElement.getElementsByTagName("failover-timeout").item(j).getFirstChild().getNodeValue());
							dsConfigList.add(dsConfig);
						}
					}
				}
			}

		} catch (ParserConfigurationException e) {
			logger.error("Error :" + e, e);
		} catch (UnsupportedEncodingException e) {
			logger.error("Error :" + e, e);
		} catch (SAXException e) {
			logger.error("Error :" + e, e);
		} catch (IOException e) {
			logger.error("Error :" + e, e);
		} catch (XPathExpressionException e) {
			logger.error("Error :" + e, e);
		}
		return dsConfigList;
	}
}