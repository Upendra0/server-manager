/**
 * 
 */
package com.elitecore.sm.services.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.netflowclient.model.NatFlowProxyClient;
import com.elitecore.sm.netflowclient.model.NetflowClient;

/**
 * @author vandana.awatramani
 *
 */
@Component(value="netflowBinaryCollectionService")
@Entity
@Table(name = "TBLTNETFLOWBINARYCOLLSVC")
@Scope(value = "prototype")
@DynamicUpdate
@XmlRootElement
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="serviceCache")
@XmlType(propOrder = { "serverIp", "serverPort", "netFlowPort", "sktRcvBufferSize", "sktSendBufferSize",
						"newLineCharAvailable","bulkWriteLimit","maxPktSize","maxWriteBufferSize","isTCPProtocol",
						"parallelFileWriteCount","snmpAlertEnable","snmpTimeInterval","maxIdelCommuTime","redirectionIP","maxReadRate",
						"receiverBufferSize","connectAttemptsMax","reconnectAttemptsMax","reconnectDelay",
						"netFLowClientList","proxyClientEnable","proxyServicePort","natFlowProxyClients", "packetThreshold", "bitThreshold"})
public class NetflowBinaryCollectionService extends CollectionService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * Port on which the Netflow collection service listens for incoming Netflow
	 * packets.
	 */
	private String serverIp;
	private int serverPort=5778;
	private int netFlowPort = 5140;
	// Main thread priority and worker thread priority are being deprecated
	private boolean newLineCharAvailable = true;
	private int bulkWriteLimit = 1000;
	private long maxPktSize = 8192;
	private long maxWriteBufferSize = 4000;

	private int parallelFileWriteCount = 3;
	private boolean isTCPProtocol = false;
	private boolean snmpAlertEnable = false;
	private int snmpTimeInterval;
	private int maxIdelCommuTime = 60;
	private String redirectionIP;
	
	private long sktRcvBufferSize;
	private long sktSendBufferSize;

	private List<NetflowClient> netFLowClientList = new ArrayList<>();
	private boolean proxyClientEnable = false;
	private int proxyServicePort = 5145;
	private List<NatFlowProxyClient> natFlowProxyClients = new ArrayList<>();
	
	private long receiverBufferSize=-1;
	private int maxReadRate=-1;
	private int connectAttemptsMax=-1;
	private int reconnectAttemptsMax=-1;
	private int reconnectDelay=-1;
	
	private long packetThreshold = -1;
	private long bitThreshold = -1;
	
	public long getReceiverBufferSize() {
		return receiverBufferSize;
	}

	public void setReceiverBufferSize(long receiverBufferSize) {
		this.receiverBufferSize = receiverBufferSize;
	}

	public int getMaxReadRate() {
		return maxReadRate;
	}

	public void setMaxReadRate(int maxReadRate) {
		this.maxReadRate = maxReadRate;
	}

	public int getConnectAttemptsMax() {
		return connectAttemptsMax;
	}

	public void setConnectAttemptsMax(int connectAttemptsMax) {
		this.connectAttemptsMax = connectAttemptsMax;
	}

	public int getReconnectAttemptsMax() {
		return reconnectAttemptsMax;
	}

	public void setReconnectAttemptsMax(int reconnectAttemptsMax) {
		this.reconnectAttemptsMax = reconnectAttemptsMax;
	}

	public int getReconnectDelay() {
		return reconnectDelay;
	}

	public void setReconnectDelay(int reconnectDelay) {
		this.reconnectDelay = reconnectDelay;
	}

	@XmlElement
	@Column(name = "NETFLOWPORT", nullable = false,length=5)
	public int getNetFlowPort() {
		return netFlowPort;
	}

	public void setNetFlowPort(int netFlowPort) {
		this.netFlowPort = netFlowPort;
	}

	@XmlElement
	@Column(name = "NEWLINECHARAVAILABLE", nullable = true,length=1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isNewLineCharAvailable() {
		return newLineCharAvailable;
	}

	public void setNewLineCharAvailable(boolean newLineCharAvailable) {
		this.newLineCharAvailable = newLineCharAvailable;
	}

	@XmlElement
	@Column(name = "BULKWRITELIMIT", nullable = false,length=5)
	public int getBulkWriteLimit() {
		return bulkWriteLimit;
	}

	public void setBulkWriteLimit(int bulkWriteLimit) {
		this.bulkWriteLimit = bulkWriteLimit;
	}

	@XmlElement
	@Column(name = "MAXPKTSIZE", nullable = false,length=10)
	public long getMaxPktSize() {
		return maxPktSize;
	}

	public void setMaxPktSize(long maxPktSize) {
		this.maxPktSize = maxPktSize;
	}

	@XmlElement
	@Column(name = "MAXWRITEBUFFSIZE", nullable = false,length=10)
	public long getMaxWriteBufferSize() {
		return maxWriteBufferSize;
	}

	public void setMaxWriteBufferSize(long maxWriteBufferSize) {
		this.maxWriteBufferSize = maxWriteBufferSize;
	}

	@XmlElement
	@Column(name = "PARRLWRITECOUNT", nullable = false,length=5)
	public int getParallelFileWriteCount() {
		return parallelFileWriteCount;
	}

	public void setParallelFileWriteCount(int parallelFileWriteCount) {
		this.parallelFileWriteCount = parallelFileWriteCount;
	}
	
	@XmlElement
	@Column(name = "TCPPROTOCOLENABLED", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean getIsTCPProtocol() {
		return isTCPProtocol;
	}

	public void setIsTCPProtocol(boolean isTCPProtocol) {
		this.isTCPProtocol = isTCPProtocol;
	}

	@XmlElement
	@Column(name = "SNMPALERTENABLED", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isSnmpAlertEnable() {
		return snmpAlertEnable;
	}

	public void setSnmpAlertEnable(boolean snmpAlertEnable) {
		this.snmpAlertEnable = snmpAlertEnable;
	}

	@XmlElement
	@Column(name = "SNMPINTERVAL", nullable = true,length=5)
	public int getSnmpTimeInterval() {
		return snmpTimeInterval;
	}

	public void setSnmpTimeInterval(int snmpTimeInterval) {
		this.snmpTimeInterval = snmpTimeInterval;
	}

	@XmlElement
	@OneToMany(mappedBy = "service",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@DiffIgnore
	public List<NetflowClient> getNetFLowClientList() {
		return netFLowClientList;
	}

	public void setNetFLowClientList(List<NetflowClient> netFLowClientList) {
		this.netFLowClientList = netFLowClientList;
	}

	@XmlElement
	@Column(name = "MAXIDELCOMMTIME", nullable = true)
	public int getMaxIdelCommuTime() {
		return maxIdelCommuTime;
	}

	public void setMaxIdelCommuTime(int maxIdelCommuTime) {
		this.maxIdelCommuTime = maxIdelCommuTime;
	}

	@Column(name = "SERVERIP", nullable = false, length = 255)
	@XmlElement(nillable=true)
	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	@Column(name = "SERVERPORT", nullable = false, length = 6)
	@XmlElement
	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	@Column(name = "REDIRECTIONIP", nullable = true, length = 255)
	@XmlElement
	public String getRedirectionIP() {
		return redirectionIP;
	}

	public void setRedirectionIP(String redirectionIP) {
		this.redirectionIP = redirectionIP;
	}

	@Transient
	public long getSktRcvBufferSize() {
		return sktRcvBufferSize;
	}

	public void setSktRcvBufferSize(long sktRcvBufferSize) {
		this.sktRcvBufferSize = sktRcvBufferSize;
	}

	@Transient
	public long getSktSendBufferSize() {
		return sktSendBufferSize;
	}

	public void setSktSendBufferSize(long sktSendBufferSize) {
		this.sktSendBufferSize = sktSendBufferSize;
	}

	@XmlElement
	@OneToMany(mappedBy = "service",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@DiffIgnore
	public List<NatFlowProxyClient> getNatFlowProxyClients() {
		return natFlowProxyClients;
	}

	public void setNatFlowProxyClients(List<NatFlowProxyClient> natFlowProxyClients) {
		this.natFlowProxyClients = natFlowProxyClients;
	}

	@Column(name = "PROXYCLIENTENABLE", nullable = true, length = 255)
	@XmlElement
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isProxyClientEnable() {
		return proxyClientEnable;
	}

	public void setProxyClientEnable(boolean proxyClientEnable) {
		this.proxyClientEnable = proxyClientEnable;
	}

	@Column(name = "PROXYSERVICEPORT", nullable = true, length = 255)
	@XmlElement
	public int getProxyServicePort() {
		return proxyServicePort;
	}

	public void setProxyServicePort(int proxyServicePort) {
		this.proxyServicePort = proxyServicePort;
	}

	@Column(name = "PKTTHRESHOLD", nullable = false, length = 20)
	@XmlElement
	public long getPacketThreshold() {
		return packetThreshold;
	}

	public void setPacketThreshold(long packetThreshold) {
		this.packetThreshold = packetThreshold;
	}

	@Column(name = "BITTHRESHOLD", nullable = false, length = 20)
	@XmlElement
	public long getBitThreshold() {
		return bitThreshold;
	}

	public void setBitThreshold(long bitThreshold) {
		this.bitThreshold = bitThreshold;
	}	
}
