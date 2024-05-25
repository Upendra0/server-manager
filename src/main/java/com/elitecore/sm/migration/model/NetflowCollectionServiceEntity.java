package com.elitecore.sm.migration.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.dozer.Mapping;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "serviceAddress", "startupMode",
		"socketReceiveBufferSize", "socketSendBufferSize", "queueSize",
		"minimumThread", "maximumThread", "redirectionIp",
		"maxIdleCommunicationTimeInterval", "parallelFileWriteCount",
		"maxPacketSizeInByte", "maxWritebufferInMb", "readTemplateInitialy",
		"abnormalTimeAlert", "optionTemplateLookup",
		"parallelBinaryWriteEnable", "clients" })
@XmlRootElement(name = "natflow-collection-service")
public class NetflowCollectionServiceEntity {

	@XmlElement(name = "service-address", required = true)
	protected String serviceAddress;

	@XmlElement(name = "startup-mode", required = true)
	protected String startupMode;

	@XmlElement(name = "socket-receive-buffer-size")
	@Mapping("sktRcvBufferSize")
	protected int socketReceiveBufferSize;

	@XmlElement(name = "socket-send-buffer-size")
	@Mapping("sktSendBufferSize")
	protected int socketSendBufferSize;

	@XmlElement(name = "queue-size")
	@Mapping("svcExecParams.queueSize")
	protected int queueSize;

	@XmlElement(name = "minimum-thread")
	@Mapping("svcExecParams.minThread")
	protected int minimumThread;

	@XmlElement(name = "maximum-thread")
	@Mapping("svcExecParams.maxThread")
	protected int maximumThread;

	@XmlElement(name = "redirection-ip", required = true)
	// TODO : not there in xsl
	@Mapping("this")
	protected String redirectionIp;

	@XmlElement(name = "max-idle-communication-time-interval")
	// TODO : not there in xsl
	@Mapping("this")
	protected int maxIdleCommunicationTimeInterval;

	@XmlElement(name = "parallel-file-write-count")
	@Mapping("parallelFileWriteCount")
	protected int parallelFileWriteCount;

	@XmlElement(name = "max-packet-size-in-byte")
	@Mapping("maxPktSize")
	protected int maxPacketSizeInByte;

	@XmlElement(name = "max-writebuffer-in-mb")
	@Mapping("maxWriteBufferSize")
	protected int maxWritebufferInMb;

	@XmlElement(name = "read-template-initialy", required = true)
	@Mapping("readTemplateOnInit")
	protected boolean readTemplateInitialy;

	@XmlElement(name = "abnormal-time-alert", required = true)
	// @Mapping("this")
	protected NetflowCollectionServiceEntity.AbnormalTimeAlert abnormalTimeAlert;

	@XmlElement(name = "option-template-lookup", required = true)
	@Mapping("this")
	protected NetflowCollectionServiceEntity.OptionTemplateLookup optionTemplateLookup;

	@XmlElement(name = "is-parallel-binary-write-enable", required = true)
	@Mapping("enableParallelBinaryWrite")
	protected boolean parallelBinaryWriteEnable;

	@XmlElement(required = true)
	protected NetflowCollectionServiceEntity.Clients clients;

	public boolean isParallelBinaryWriteEnable() {
		return parallelBinaryWriteEnable;
	}

	public void setParallelBinaryWriteEnable(boolean parallelBinaryWriteEnable) {
		this.parallelBinaryWriteEnable = parallelBinaryWriteEnable;
	}

	public boolean isReadTemplateInitialy() {
		return readTemplateInitialy;
	}

	public void setReadTemplateInitialy(boolean readTemplateInitialy) {
		this.readTemplateInitialy = readTemplateInitialy;
	}

	public String getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(String value) {
		this.serviceAddress = value;
	}

	public String getStartupMode() {
		return startupMode;
	}

	public void setStartupMode(String value) {
		this.startupMode = value;
	}

	public int getSocketReceiveBufferSize() {
		return socketReceiveBufferSize;
	}

	public void setSocketReceiveBufferSize(int value) {
		this.socketReceiveBufferSize = value;
	}

	public int getSocketSendBufferSize() {
		return socketSendBufferSize;
	}

	public void setSocketSendBufferSize(int value) {
		this.socketSendBufferSize = value;
	}

	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int value) {
		this.queueSize = value;
	}

	public int getMinimumThread() {
		return minimumThread;
	}

	public void setMinimumThread(int value) {
		this.minimumThread = value;
	}

	public int getMaximumThread() {
		return maximumThread;
	}

	public void setMaximumThread(int value) {
		this.maximumThread = value;
	}

	public String getRedirectionIp() {
		return redirectionIp;
	}

	public void setRedirectionIp(String value) {
		this.redirectionIp = value;
	}

	public int getMaxIdleCommunicationTimeInterval() {
		return maxIdleCommunicationTimeInterval;
	}

	public void setMaxIdleCommunicationTimeInterval(int value) {
		this.maxIdleCommunicationTimeInterval = value;
	}

	public int getParallelFileWriteCount() {
		return parallelFileWriteCount;
	}

	public void setParallelFileWriteCount(int value) {
		this.parallelFileWriteCount = value;
	}

	public int getMaxPacketSizeInByte() {
		return maxPacketSizeInByte;
	}

	public void setMaxPacketSizeInByte(int value) {
		this.maxPacketSizeInByte = value;
	}

	public int getMaxWritebufferInMb() {
		return maxWritebufferInMb;
	}

	public void setMaxWritebufferInMb(int value) {
		this.maxWritebufferInMb = value;
	}

	public NetflowCollectionServiceEntity.AbnormalTimeAlert getAbnormalTimeAlert() {
		return abnormalTimeAlert;
	}

	public void setAbnormalTimeAlert(
			NetflowCollectionServiceEntity.AbnormalTimeAlert value) {
		this.abnormalTimeAlert = value;
	}

	public NetflowCollectionServiceEntity.OptionTemplateLookup getOptionTemplateLookup() {
		return optionTemplateLookup;
	}

	public void setOptionTemplateLookup(
			NetflowCollectionServiceEntity.OptionTemplateLookup value) {
		this.optionTemplateLookup = value;
	}

	public NetflowCollectionServiceEntity.Clients getClients() {
		return clients;
	}

	public void setClients(NetflowCollectionServiceEntity.Clients value) {
		this.clients = value;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "timeInterval" })
	public static class AbnormalTimeAlert {

		@XmlElement(name = "time-interval")
		@Mapping("alertInterval")
		protected int timeInterval;

		@XmlAttribute(name = "enable")
		@Mapping("snmpAlertEnable")
		protected boolean enable;

		public boolean isEnable() {
			return enable;
		}

		public void setEnable(boolean enable) {
			this.enable = enable;
		}

		public int getTimeInterval() {
			return timeInterval;
		}

		public void setTimeInterval(int value) {
			this.timeInterval = value;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "client" })
	public static class Clients {

		@XmlElement(required = true)
		@Mapping("netFLowClientList")
		protected List<NetflowCollectionServiceEntity.Clients.Client> client;

		public List<NetflowCollectionServiceEntity.Clients.Client> getClient() {
			return client;
		}

		public void setClient(
				List<NetflowCollectionServiceEntity.Clients.Client> value) {
			this.client = value;
		}

		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "clientIp", "clientPort",
				"nodeAliveRequest", "echoRequest", "requestExpiryTime",
				"requestRetry", "redirectionIp", "fileName", "fileSequence",
				"sequenceRange", "fileLocation", "binaryFileLocation",
				"requestBufferCount", "rollingTimeUnit", "rollingVolumeUnit",
				"fileCompression", "clientEnable", "abnormalTimeAlert" })
		public static class Client {

			@XmlElement(name = "client-ip", required = true)
			@Mapping("clientIpAddress")
			protected String clientIp;

			@XmlElement(name = "client-port")
			@Mapping("clientPort")
			protected int clientPort;

			@XmlElement(name = "node-alive-request", required = true)
			// TODO : not there in xsl
			@Mapping("this")
			protected String nodeAliveRequest;

			@XmlElement(name = "echo-request")
			// TODO : not there in xsl
			@Mapping("this")
			protected int echoRequest;

			@XmlElement(name = "request-expiry-time")
			// TODO : not there in xsl
			@Mapping("this")
			protected int requestExpiryTime;

			@XmlElement(name = "request-retry")
			@Mapping("this")
			// TODO : not there in xsl
			protected int requestRetry;

			@XmlElement(name = "redirection-ip", required = true)
			@Mapping("this")
			// TODO : not there in xsl
			protected String redirectionIp;

			@XmlElement(name = "file-name", required = true)
			@Mapping("fileNameFormat")
			protected String fileName;

			@XmlElement(name = "file-sequence", required = true)
			@Mapping("appendFileSequenceInFileName")
			protected boolean fileSequence;

			@XmlElement(name = "sequence-range", required = true)
			// @Mapping("")
			// TODO : converter : minFileSeqValue and maxFileSeqValue
			protected String sequenceRange;

			@XmlElement(name = "file-location", required = true)
			@Mapping("outFileLocation")
			protected String fileLocation;

			@XmlElement(name = "binary-file-location", required = true)
			@Mapping("bkpBinaryfileLocation")
			protected String binaryFileLocation;

			@XmlElement(name = "request-buffer-count")
			// not there in xsl
			@Mapping("this")
			protected int requestBufferCount;

			@XmlElement(name = "rolling-time-unit")
			@Mapping("timeLogRollingUnit")
			protected int rollingTimeUnit;

			@XmlElement(name = "rolling-volume-unit")
			@Mapping("volLogRollingUnit")
			protected int rollingVolumeUnit;

			@XmlElement(name = "file-compression", required = true)
			@Mapping("inputCompressed")
			protected boolean fileCompression;

			@XmlElement(name = "is-client-enable", required = true)
			@Mapping("this")
			// TODO : this field is based on status field : use converter for
			// this
			protected boolean clientEnable;

			@XmlElement(name = "abnormal-time-alert", required = true)
			@Mapping("this")
			protected NetflowCollectionServiceEntity.Clients.Client.AbnormalTimeAlert abnormalTimeAlert;

			public boolean isFileCompression() {
				return fileCompression;
			}

			public void setFileCompression(boolean fileCompression) {
				this.fileCompression = fileCompression;
			}
			
			public boolean isClientEnable() {
				return clientEnable;
			}

			public void setClientEnable(boolean clientEnable) {
				this.clientEnable = clientEnable;
			}

			public boolean isFileSequence() {
				return fileSequence;
			}

			public void setFileSequence(boolean fileSequence) {
				this.fileSequence = fileSequence;
			}

			public String getClientIp() {
				return clientIp;
			}

			public void setClientIp(String value) {
				this.clientIp = value;
			}

			public int getClientPort() {
				return clientPort;
			}

			public void setClientPort(int value) {
				this.clientPort = value;
			}

			public String getNodeAliveRequest() {
				return nodeAliveRequest;
			}

			public void setNodeAliveRequest(String value) {
				this.nodeAliveRequest = value;
			}

			public int getEchoRequest() {
				return echoRequest;
			}

			public void setEchoRequest(int value) {
				this.echoRequest = value;
			}

			public int getRequestExpiryTime() {
				return requestExpiryTime;
			}

			public void setRequestExpiryTime(int value) {
				this.requestExpiryTime = value;
			}

			public int getRequestRetry() {
				return requestRetry;
			}

			public void setRequestRetry(int value) {
				this.requestRetry = value;
			}

			public String getRedirectionIp() {
				return redirectionIp;
			}

			public void setRedirectionIp(String value) {
				this.redirectionIp = value;
			}

			public String getFileName() {
				return fileName;
			}

			public void setFileName(String value) {
				this.fileName = value;
			}

			public String getSequenceRange() {
				return sequenceRange;
			}

			public void setSequenceRange(String value) {
				this.sequenceRange = value;
			}

			public String getFileLocation() {
				return fileLocation;
			}

			public void setFileLocation(String value) {
				this.fileLocation = value;
			}

			public String getBinaryFileLocation() {
				return binaryFileLocation;
			}

			public void setBinaryFileLocation(String value) {
				this.binaryFileLocation = value;
			}

			public int getRequestBufferCount() {
				return requestBufferCount;
			}

			public void setRequestBufferCount(int value) {
				this.requestBufferCount = value;
			}

			public int getRollingTimeUnit() {
				return rollingTimeUnit;
			}

			public void setRollingTimeUnit(int value) {
				this.rollingTimeUnit = value;
			}

			public int getRollingVolumeUnit() {
				return rollingVolumeUnit;
			}

			public void setRollingVolumeUnit(int value) {
				this.rollingVolumeUnit = value;
			}

			public NetflowCollectionServiceEntity.Clients.Client.AbnormalTimeAlert getAbnormalTimeAlert() {
				return abnormalTimeAlert;
			}

			public void setAbnormalTimeAlert(
					NetflowCollectionServiceEntity.Clients.Client.AbnormalTimeAlert value) {
				this.abnormalTimeAlert = value;
			}

			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder = { "timeInterval" })
			public static class AbnormalTimeAlert {

				@XmlElement(name = "time-interval")
				@Mapping("alertInterval")
				protected int timeInterval;

				@XmlAttribute(name = "enable")
				@Mapping("snmpAlertEnable")
				protected boolean enable;

				public boolean isEnable() {
					return enable;
				}

				public void setEnable(boolean enable) {
					this.enable = enable;
				}

				public int getTimeInterval() {
					return timeInterval;
				}

				public void setTimeInterval(int value) {
					this.timeInterval = value;
				}

			}

		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "optionTemplate", "template" })
	public static class OptionTemplateLookup {

		@XmlElement(name = "option-template", required = true)
		@Mapping("this")
		protected NetflowCollectionServiceEntity.OptionTemplateLookup.OptionTemplate optionTemplate;

		@XmlElement(required = true)
		@Mapping("this")
		protected NetflowCollectionServiceEntity.OptionTemplateLookup.Template template;

		@XmlAttribute(name = "enable")
		@Mapping("optionTemplateEnable")
		protected boolean enable;

		public boolean isEnable() {
			return enable;
		}

		public void setEnable(boolean enable) {
			this.enable = enable;
		}

		public NetflowCollectionServiceEntity.OptionTemplateLookup.OptionTemplate getOptionTemplate() {
			return optionTemplate;
		}

		public void setOptionTemplate(
				NetflowCollectionServiceEntity.OptionTemplateLookup.OptionTemplate value) {
			this.optionTemplate = value;
		}

		public NetflowCollectionServiceEntity.OptionTemplateLookup.Template getTemplate() {
			return template;
		}

		public void setTemplate(
				NetflowCollectionServiceEntity.OptionTemplateLookup.Template value) {
			this.template = value;
		}

		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "optionTemplateId", "keyField",
				"valueField" })
		public static class OptionTemplate {

			@XmlElement(name = "option-template-id")
			@Mapping("optionTemplateId")
			protected int optionTemplateId;

			@XmlElement(name = "key-field")
			@Mapping("optionTemplateKey")
			protected int keyField;

			@XmlElement(name = "value-field")
			@Mapping("optionTemplateValue")
			protected int valueField;

			public int getOptionTemplateId() {
				return optionTemplateId;
			}

			public void setOptionTemplateId(int value) {
				this.optionTemplateId = value;
			}

			public int getKeyField() {
				return keyField;
			}

			public void setKeyField(int value) {
				this.keyField = value;
			}

			public int getValueField() {
				return valueField;
			}

			public void setValueField(int value) {
				this.valueField = value;
			}

		}

		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "templateId", "field" })
		public static class Template {

			@XmlElement(name = "template-id", required = true)
			@Mapping("optionCopytoTemplateId")
			protected String templateId;

			@XmlElement(required = true)
			@Mapping("optionCopyTofield")
			protected String field;

			public String getTemplateId() {
				return templateId;
			}

			public void setTemplateId(String value) {
				this.templateId = value;
			}

			public String getField() {
				return field;
			}

			public void setField(String value) {
				this.field = value;
			}

		}

	}

}
