package com.elitecore.sm.kafka.datasource.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.kafka.datasource.model.KafkaDataSourceConfig;

public interface KafkaDataSourceDao extends GenericDAO<KafkaDataSourceConfig> {
	
	public List<KafkaDataSourceConfig> getKafkaDataSourceConfigList();
	
	public List<KafkaDataSourceConfig> getKafkaDataSourceByName(String name);
	
	public List<KafkaDataSourceConfig> getKafkaDSConfigByIPandPort(String kafkaSeverIP, int kafkaServerPort);
	
	public KafkaDataSourceConfig getKafkaDataSourceConfigById(Integer kafkaDSId);

}
