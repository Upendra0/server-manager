package com.elitecore.sm.kafka.datasource.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.kafka.datasource.model.KafkaDataSourceConfig;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.services.model.NetflowBinaryCollectionService;

public interface KafkaDataSourceService {

	public ResponseObject getKafkaDataSourceConfigList();
	
	public ResponseObject updateKafkaDataSourceConfig(KafkaDataSourceConfig kafkaDataSourceConfig);
	
	public ResponseObject deleteKafkaDataSourceConfig(int kafkaDataSourceId);

	public ResponseObject addKafkaDataSourceConfig(KafkaDataSourceConfig defaultKafkaDataSource);
	
	public KafkaDataSourceConfig getKafkaDataSourceConfigById(Integer kafkaDSId);
	
	public List<NetflowClient> getClientListByKafkaDSId(int kafkaDSId, int startIndex, int limit);
	
	public int getClientCountByKafkaDSId(int kafkaDSId);
	
	public ResponseObject createKafkaDataSourceForImport(KafkaDataSourceConfig exportedKafkaDataSource,NetflowBinaryCollectionService exportedService);
	
}
