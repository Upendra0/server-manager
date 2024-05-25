package com.elitecore.sm.kafka.datasource.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

@Component(value = "kafkaDataSourceConfigObject")
@Scope(value = "prototype")
@Entity()
@Table(name = "TBLTKAFKADSCONFIG")
@DynamicUpdate
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "datasourceConfigCache")
@XmlType(propOrder = { "id","name","kafkaServerIpAddress","kafkaServerPort","maxRetryCount","maxResponseWait","kafkaProducerRetryCount","kafkaProducerRequestTimeout","kafkaProducerRetryBackoff","kafkaProducerDeliveryTimeout"})
public class KafkaDataSourceConfig extends BaseModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String kafkaServerIpAddress;
	private int kafkaServerPort = -1;
	private int maxRetryCount = -1;
	private int maxResponseWait = -1;
	private int kafkaProducerRetryCount = -1;
	private int kafkaProducerRequestTimeout = -1;
	private int kafkaProducerRetryBackoff = -1;
	private int kafkaProducerDeliveryTimeout = -1;
	
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="KafkaDataSourceConfig",allocationSize=1)

	@XmlElement
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@XmlElement
	@Column(name = "NAME", length=250, unique= true, nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	@Column(name = "KAFKASERVERIP", nullable = true,length=100)
	public String getKafkaServerIpAddress() {
		return kafkaServerIpAddress;
	}

	public void setKafkaServerIpAddress(String kafkaServerIpAddress) {
		this.kafkaServerIpAddress = kafkaServerIpAddress;
	}

	@XmlElement
	@Column(name = "KAFKASERVERPORT", nullable = true)
	public int getKafkaServerPort() {
		return kafkaServerPort;
	}

	public void setKafkaServerPort(int kafkaServerPort) {
		this.kafkaServerPort = kafkaServerPort;
	}

	@XmlElement
	@Column(name = "MAXRETRYCOUNT", nullable = true)
	public int getMaxRetryCount() {
		return maxRetryCount;
	}

	public void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}

	@XmlElement
	@Column(name = "MAXRESPONSEWAIT", nullable = true)
	public int getMaxResponseWait() {
		return maxResponseWait;
	}

	public void setMaxResponseWait(int maxResponseWait) {
		this.maxResponseWait = maxResponseWait;
	}

	@XmlElement
	@Column(name = "KPRETRYCOUNT", nullable = true)
	public int getKafkaProducerRetryCount() {
		return kafkaProducerRetryCount;
	}

	public void setKafkaProducerRetryCount(int kafkaProducerRetryCount) {
		this.kafkaProducerRetryCount = kafkaProducerRetryCount;
	}

	@XmlElement
	@Column(name = "KPREQUESTTIMEOUT", nullable = true)
	public int getKafkaProducerRequestTimeout() {
		return kafkaProducerRequestTimeout;
	}

	public void setKafkaProducerRequestTimeout(int kafkaProducerRequestTimeout) {
		this.kafkaProducerRequestTimeout = kafkaProducerRequestTimeout;
	}

	@XmlElement
	@Column(name = "KPRETRYBACKOFF", nullable = true)
	public int getKafkaProducerRetryBackoff() {
		return kafkaProducerRetryBackoff;
	}

	public void setKafkaProducerRetryBackoff(int kafkaProducerRetryBackoff) {
		this.kafkaProducerRetryBackoff = kafkaProducerRetryBackoff;
	}

	@XmlElement
	@Column(name = "KPDELIVERYTIMEOUT", nullable = true)
	public int getKafkaProducerDeliveryTimeout() {
		return kafkaProducerDeliveryTimeout;
	}

	public void setKafkaProducerDeliveryTimeout(int kafkaProducerDeliveryTimeout) {
		this.kafkaProducerDeliveryTimeout = kafkaProducerDeliveryTimeout;
	}
	

}
