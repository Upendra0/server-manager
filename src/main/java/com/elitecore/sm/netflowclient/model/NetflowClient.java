/**
 * 
 */
package com.elitecore.sm.netflowclient.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.ContentFormatTypeEnum;
import com.elitecore.sm.common.model.ContentTypeEnum;
import com.elitecore.sm.common.model.MessageTypeEnum;
import com.elitecore.sm.common.model.ProxySchemaTypeEnum;
import com.elitecore.sm.common.model.RequestTypeEnum;
import com.elitecore.sm.common.model.RollingTypeEnum;
import com.elitecore.sm.common.model.SecurityTypeEnum;
import com.elitecore.sm.kafka.datasource.model.KafkaDataSourceConfig;
import com.elitecore.sm.services.model.NetflowBinaryCollectionService;

/**
 * @author vandana.awatramani
 *
 */

/**
 * @author elitecore
 *
 */
@Component("netflowClient")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "TBLTNETFLOWCLIENT")
@Scope(value = "prototype")
@DynamicUpdate
@XmlRootElement
@XmlType(propOrder = { "id","name","clientIpAddress", "clientPort","fileNameFormat","appendFileSequenceInFileName",
		"appendFilePaddingInFileName","minFileSeqValue","maxFileSeqValue","outFileLocation","rollingType",
		"volLogRollingUnit","timeLogRollingUnit","inputCompressed","bkpBinaryfileLocation","snmpAlertEnable",
		"alertInterval","nodeAliveRequest","echoRequest","requestExpiryTime","requestRetry","redirectionIp","requestBufferCount","sharedSecretKey",
		"topicName","resourcesName","registerObserver","observerTimeout","requestType","messageType","requestTimeout","requestRetryCount","reqExecutionInterval","reqExecutionFreq", "exchangeLifeTime",
		"enableSecurity","securityType","securityIdentity","securityKey","secCerLocation","secCerPasswd","enableProxy","proxySchema","proxyResources","proxyServerIp","proxyServerPort","jsonValidate","contentType", "uri","enableKafka","kafkaDataSourceConfig"})
public class NetflowClient extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2252086629078034849L;
	
	private int id;
	private String name;
	private String clientIpAddress;
	private int clientPort;
	private String fileNameFormat="Natflow{yyyyMMddHHmmssSSS}.log";
	private boolean appendFileSequenceInFileName = false;
	private boolean appendFilePaddingInFileName = false;
	private int minFileSeqValue=-1;
	private int maxFileSeqValue=-1;
	private String outFileLocation;
	private int volLogRollingUnit=1024;
	private int timeLogRollingUnit=2;
	private boolean inputCompressed=true; 
	//This parameter refers an absolute path where the service copies the collected binary files for back-up
	// purpose. This parameter has NO relevance for Netflow Binary Collection Service.
	private String bkpBinaryfileLocation;
	private NetflowBinaryCollectionService service;
	private boolean snmpAlertEnable = true;
	private int alertInterval = 5;
	private RollingTypeEnum rollingType = RollingTypeEnum.BOTH;
	
	private boolean nodeAliveRequest = true;
	private int echoRequest=5;
	private int requestExpiryTime=3000;
	private int requestRetry=5;
	private String redirectionIp;
	private int requestBufferCount=5;
	private String sharedSecretKey;
	private String topicName;
	
	private String resourcesName;
	private boolean registerObserver=false;
	private int observerTimeout=-1;
	private RequestTypeEnum requestType=RequestTypeEnum.GET;
	private MessageTypeEnum messageType=MessageTypeEnum.Confirmable;
	private int requestTimeout=-1;
	private int requestRetryCount=-1;
	private int reqExecutionInterval=-1;
	private int reqExecutionFreq=-1;
	private long exchangeLifeTime = 247000;
	private ContentFormatTypeEnum contentFormat=ContentFormatTypeEnum.TEXT_PLAIN;
	private String payload;
	private boolean enableSecurity=false;
	private SecurityTypeEnum securityType=SecurityTypeEnum.PSK;
	private String securityIdentity;
	private String securityKey;
	private String secCerLocation;
	private String secCerPasswd;
	private boolean enableProxy=false;
	private ProxySchemaTypeEnum proxySchema=ProxySchemaTypeEnum.HTTP;
	private String proxyResources;
	private String proxyServerIp;
	private int proxyServerPort;
	
	private boolean jsonValidate = false;
	private String contentType = ContentTypeEnum.JSON.getName();
	private String uri = "/";
	
	private boolean enableKafka = false;
	private KafkaDataSourceConfig kafkaDataSourceConfig = null;
	
	
	@XmlElement
	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="NetflowClient",allocationSize=1)
	@XmlElement
	public int getId() {
		return id;
	}
	
	@XmlElement
	@Column(name = "CLIENTIP", nullable = true,length=20)
	public String getClientIpAddress() {
		return clientIpAddress;
	}
	public void setClientIpAddress(String clientIpAddress) {
		this.clientIpAddress = clientIpAddress;
	}
	
	@XmlElement
	@Column(name = "CLIENTPORT", nullable = false,length=5)
	public int getClientPort() {
		return clientPort;
	}
	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}
	
	@XmlElement
	@Column(name = "FILENAMEFORMAT", nullable = false,length=50)
	public String getFileNameFormat() {
		return fileNameFormat;
	}
	public void setFileNameFormat(String fileNameFormat) {
		this.fileNameFormat = fileNameFormat;
	}
	
	@XmlElement
	@Column(name = "APPENDFILESEQ", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isAppendFileSequenceInFileName() {
		return appendFileSequenceInFileName;
	}
	public void setAppendFileSequenceInFileName(boolean appendFileSequenceInFileName) {
		this.appendFileSequenceInFileName = appendFileSequenceInFileName;
	}
	
	@XmlElement
	@Column(name = "APPENDPADDINGINFILESEQ", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isAppendFilePaddingInFileName() {
		return appendFilePaddingInFileName;
	}

	public void setAppendFilePaddingInFileName(boolean appendFilePaddingInFileName) {
		this.appendFilePaddingInFileName = appendFilePaddingInFileName;
	}

	@XmlElement
	@Column(name = "MINFILESEQ", nullable = false)
	public int getMinFileSeqValue() {
		return minFileSeqValue;
	}
	public void setMinFileSeqValue(int minFileSeqValue) {
		this.minFileSeqValue = minFileSeqValue;
	}
	
	@XmlElement
	@Column(name = "MAXFILESEQ", nullable = false)
	public int getMaxFileSeqValue() {
		return maxFileSeqValue;
	}
	public void setMaxFileSeqValue(int maxFileSeqValue) {
		this.maxFileSeqValue = maxFileSeqValue;
	}
	
	@XmlElement
	@Column(name = "OUTFILELOC", nullable = false,length=500)
	public String getOutFileLocation() {
		return outFileLocation;
	}
	public void setOutFileLocation(String outFileLocation) {
		this.outFileLocation = outFileLocation;
	}
	
	@XmlElement
	@Column(name = "LOGROLLINGUNITVOL", nullable = false,length=5)
	public int getVolLogRollingUnit() {
		return volLogRollingUnit;
	}
	public void setVolLogRollingUnit(int volLogRollingUnit) {
		this.volLogRollingUnit = volLogRollingUnit;
	}
	
	@XmlElement
	@Column(name = "LOGROLLINGUNITTIME", nullable = false,length=5)
	public int getTimeLogRollingUnit() {
		return timeLogRollingUnit;
	}
	public void setTimeLogRollingUnit(int timeLogRollingUnit) {
		this.timeLogRollingUnit = timeLogRollingUnit;
	}
	
	@XmlElement
	@Column(name = "INPUTCOMPRESSED", nullable = false,length=1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isInputCompressed() {
		return inputCompressed;
	}
	public void setInputCompressed(boolean inputCompressed) {
		this.inputCompressed = inputCompressed;
	}
	
	@XmlElement
	@Column(name = "BAKFILLOC", nullable = true,length=500)
	public String getBkpBinaryfileLocation() {
		return bkpBinaryfileLocation;
	}
	public void setBkpBinaryfileLocation(String bkpBinaryfileLocation) {
		this.bkpBinaryfileLocation = bkpBinaryfileLocation;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVID", nullable = true, foreignKey = @ForeignKey(name = "FK_SERVICE_NETFLOW_CLIENT"))
	@XmlTransient
	@DiffIgnore
	public NetflowBinaryCollectionService getService() {
		return service;
	}
	
	public void setService(NetflowBinaryCollectionService service) {
		this.service = service;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement
	@Column(name = "NAME", nullable = false,length=250)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	@Column(name = "SNMPALERTENABLE", nullable = false,length=1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isSnmpAlertEnable() {
		return snmpAlertEnable;
	}

	public void setSnmpAlertEnable(boolean snmpAlertEnable) {
		this.snmpAlertEnable = snmpAlertEnable;
	}

	@XmlElement
	@Column(name = "ALERTINTERVAL", nullable = true,length=7)
	public int getAlertInterval() {
		return alertInterval;
	}

	public void setAlertInterval(int alertInterval) {
		this.alertInterval = alertInterval;
	}

	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name = "ROLLINGTYPE" ,nullable=false)
	public RollingTypeEnum getRollingType() {
		return rollingType;
	}

	public void setRollingType(RollingTypeEnum rollingType) {
		this.rollingType = rollingType;
	}

	
	@XmlElement
	@Column(name = "NODEALIVEREQUEST", nullable = true, length = 1)
	public boolean isNodeAliveRequest() {
		return nodeAliveRequest;
	}

	public void setNodeAliveRequest(boolean nodeAliveRequest) {
		this.nodeAliveRequest = nodeAliveRequest;
	}

	@XmlElement
	@Column(name = "ECHOREQUEST", nullable = true, length = 10)
	public int getEchoRequest() {
		return echoRequest;
	}

	public void setEchoRequest(int echoRequest) {
		this.echoRequest = echoRequest;
	}
	
	@XmlElement
	@Column(name = "REQUESTEXPIRYTIME", nullable = true, length = 10)
	public int getRequestExpiryTime() {
		return requestExpiryTime;
	}

	public void setRequestExpiryTime(int requestExpiryTime) {
		this.requestExpiryTime = requestExpiryTime;
	}

	@XmlElement
	@Column(name = "REQUESTRETRY", nullable = true, length = 10)
	public int getRequestRetry() {
		return requestRetry;
	}

	public void setRequestRetry(int requestRetry) {
		this.requestRetry = requestRetry;
	}

	@XmlElement
	@Column(name = "REDIRECTIONIP", nullable = true, length = 255)
	public String getRedirectionIp() {
		return redirectionIp;
	}

	public void setRedirectionIp(String redirectionIp) {
		this.redirectionIp = redirectionIp;
	}
	
	@XmlElement
	@Column(name = "REQUESTBUFFERCOUNT", nullable = true, length = 10)
	public int getRequestBufferCount() {
		return requestBufferCount;
	}

	public void setRequestBufferCount(int requestBufferCount) {
		this.requestBufferCount = requestBufferCount;
	}
	
	@XmlElement
	@Column(name = "SHAREDSECRETKEY", nullable = true)
	public String getSharedSecretKey() {
		return sharedSecretKey;
	}

	public void setSharedSecretKey(String sharedSecretKey) {
		this.sharedSecretKey = sharedSecretKey;
	}
	

	@XmlElement
	@Column(name = "RESOURCENAME", nullable = true, length = 500)
	public String getResourcesName() {
		return resourcesName;
	}

	public void setResourcesName(String resourcesName) {
		this.resourcesName = resourcesName;
	}

	@XmlElement
	@org.hibernate.annotations.Type(type = "yes_no")
	@Column(name = "REGISTEROBSERVER", nullable = true, length = 1)
	public boolean isRegisterObserver() {
		return registerObserver;
	}

	public void setRegisterObserver(boolean registerObserver) {
		this.registerObserver = registerObserver;
	}

	@XmlElement
	@Column(name = "OBSERVERTIMEOUT", nullable = true, length = 10)
	public int getObserverTimeout() {
		return observerTimeout;
	}

	public void setObserverTimeout(int observerTimeout) {
		this.observerTimeout = observerTimeout;
	}

	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name = "REQUESTTYPE", nullable = true, length = 10)
	public RequestTypeEnum getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestTypeEnum requestType) {
		this.requestType = requestType;
	}

	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name = "MESSAGETYPE", nullable = true, length = 10)
	public MessageTypeEnum getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageTypeEnum messageType) {
		this.messageType = messageType;
	}

	@XmlElement
	@Column(name = "REQTIMEOUT", nullable = true, length = 10)
	public int getRequestTimeout() {
		return requestTimeout;
	}

	public void setRequestTimeout(int requestTimeout) {
		this.requestTimeout = requestTimeout;
	}
	
	
	@XmlElement
	@Column(name = "REQRETRYCOUNT", nullable = true, length = 10)
	public int getRequestRetryCount() {
		return requestRetryCount;
	}

	public void setRequestRetryCount(int requestRetryCount) {
		this.requestRetryCount = requestRetryCount;
	}

	@XmlElement
	@Column(name = "REQEXECUTIONINTERVAL", nullable = true, length = 10)
	public int getReqExecutionInterval() {
		return reqExecutionInterval;
	}

	public void setReqExecutionInterval(int reqExecutionInterval) {
		this.reqExecutionInterval = reqExecutionInterval;
	}

	@XmlElement
	@Column(name = "REQEXECUTIONFREQ", nullable = true, length = 10)
	public int getReqExecutionFreq() {
		return reqExecutionFreq;
	}

	public void setReqExecutionFreq(int reqExecutionFreq) {
		this.reqExecutionFreq = reqExecutionFreq;
	}
	
	@XmlElement
	@Column(name = "EXCHANGELIFETIME", nullable = true)
	public long getExchangeLifeTime() {
		return exchangeLifeTime;
	}

	public void setExchangeLifeTime(long exchangeLifeTime) {
		this.exchangeLifeTime = exchangeLifeTime;
	}

	@XmlTransient
	@Enumerated(EnumType.STRING)
	@Column(name = "CONTENTFORMAT", nullable = true, length = 100)
	public ContentFormatTypeEnum getContentFormat() {
		return contentFormat;
	}

	public void setContentFormat(ContentFormatTypeEnum contentFormat) {
		this.contentFormat = contentFormat;
	}
	
	
	@XmlTransient
	@Column(name = "PAYLOAD", nullable = true, length = 500)
	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@XmlElement
	@org.hibernate.annotations.Type(type = "yes_no")
	@Column(name = "ENABLESECURITY", nullable = true, length = 1)
	public boolean isEnableSecurity() {
		return enableSecurity;
	}

	public void setEnableSecurity(boolean enableSecurity) {
		this.enableSecurity = enableSecurity;
	}

	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name = "SECURITYTYPE", nullable = true, length = 20)
	public SecurityTypeEnum getSecurityType() {
		return securityType;
	}

	public void setSecurityType(SecurityTypeEnum securityType) {
		this.securityType = securityType;
	}

	@XmlElement
	@Column(name = "SECURITYIDENTITY", nullable = true, length = 100)
	public String getSecurityIdentity() {
		return securityIdentity;
	}

	public void setSecurityIdentity(String securityIdentity) {
		this.securityIdentity = securityIdentity;
	}

	@XmlElement
	@Column(name = "SECURITYKEY", nullable = true, length = 100)
	public String getSecurityKey() {
		return securityKey;
	}

	public void setSecurityKey(String securityKey) {
		this.securityKey = securityKey;
	}
	
	@XmlElement
	@Column(name = "SECCERLOCATION", nullable = true, length = 500)
	public String getSecCerLocation() {
		return secCerLocation;
	}

	public void setSecCerLocation(String secCerLocation) {
		this.secCerLocation = secCerLocation;
	}

	@XmlElement
	@Column(name = "SECCERPASSWD", nullable = true, length = 100)
	public String getSecCerPasswd() {
		return secCerPasswd;
	}

	public void setSecCerPasswd(String secCerPasswd) {
		this.secCerPasswd = secCerPasswd;
	}

	@XmlElement
	@org.hibernate.annotations.Type(type = "yes_no")
	@Column(name = "ENABLEPROXY", nullable = true, length = 1)
	public boolean isEnableProxy() {
		return enableProxy;
	}

	public void setEnableProxy(boolean enableProxy) {
		this.enableProxy = enableProxy;
	}

	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name = "PROXYSCHEMA", nullable = true, length = 20)
	public ProxySchemaTypeEnum getProxySchema() {
		return proxySchema;
	}

	public void setProxySchema(ProxySchemaTypeEnum proxySchema) {
		this.proxySchema = proxySchema;
	}

	@XmlElement
	@Column(name = "PROXYRESOURCES", nullable = true, length = 500)
	public String getProxyResources() {
		return proxyResources;
	}

	public void setProxyResources(String proxyResources) {
		this.proxyResources = proxyResources;
	}

	@XmlElement
	@Column(name = "PROXYSERVERIP", nullable = true, length = 100)
	public String getProxyServerIp() {
		return proxyServerIp;
	}

	public void setProxyServerIp(String proxyServerIp) {
		this.proxyServerIp = proxyServerIp;
	}

	@XmlElement
	@Column(name = "PROXYSERVERPORT", nullable = true, length = 10)
	public int getProxyServerPort() {
		return proxyServerPort;
	}

	public void setProxyServerPort(int proxyServerPort) {
		this.proxyServerPort = proxyServerPort;
	}
	
	@XmlElement
	@org.hibernate.annotations.Type(type = "yes_no")
	@Column(name = "JSONVALIDATE", nullable = true, length = 1)
	public boolean isJsonValidate() {
		return jsonValidate;
	}

	public void setJsonValidate(boolean jsonValidate) {
		this.jsonValidate = jsonValidate;
	}

	@XmlElement
	@Column(name = "CONTENTTYPE", nullable = true, length = 100)
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	@XmlElement
	@Column(name = "URI", nullable = true, length = 255)
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	@XmlElement
	@org.hibernate.annotations.Type(type = "yes_no")
	@Column(name = "ENABLEKAFKA", nullable = true, length = 1)
	public boolean isEnableKafka() {
		return enableKafka;
	}

	public void setEnableKafka(boolean enableKafka) {
		this.enableKafka = enableKafka;
	}

	@XmlElement
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "KAFKADATASOURCEID", nullable = true, foreignKey = @ForeignKey(name = "FK_TBLTKAFKADSCONFIG_KAFKADATASOURCE_ID"))
	@DiffIgnore
	public KafkaDataSourceConfig getKafkaDataSourceConfig() {
		return kafkaDataSourceConfig;
	}

	public void setKafkaDataSourceConfig(KafkaDataSourceConfig kafkaDataSourceConfig) {
		this.kafkaDataSourceConfig = kafkaDataSourceConfig;
	}
	
	@Override
	public Object clone() {
		NetflowClient client = null;
		try {
			Date date = new Date();
			client = (NetflowClient) super.clone();
			client.setId(0);
			client.setCreatedDate(date);
			client.setLastUpdatedDate(date);
			return client;
		} catch (CloneNotSupportedException e) {
			logger.error("Clone not supported", e);
		}
		return client;
	}
}
