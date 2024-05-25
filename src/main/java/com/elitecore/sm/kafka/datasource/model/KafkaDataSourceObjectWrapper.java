package com.elitecore.sm.kafka.datasource.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.elitecore.sm.common.model.BaseModel;

@XmlRootElement
public class KafkaDataSourceObjectWrapper extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7014024025639985220L;
	private List<KafkaDataSourceConfig> kafkaDataSourceConfigList;

	@XmlElement
	public List<KafkaDataSourceConfig> getKafkaDataSourceConfigList() {
		return kafkaDataSourceConfigList;
	}
	public void setKafkaDataSourceConfigList(List<KafkaDataSourceConfig> kafkaDataSourceConfigList) {
		this.kafkaDataSourceConfigList = kafkaDataSourceConfigList;
	}

}