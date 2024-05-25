package com.elitecore.sm.kafka.datasource.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.kafka.datasource.model.KafkaDataSourceConfig;

@Repository(value="kafkaDataSourceDao")
public class KafkaDataSourceDaoImpl extends GenericDAOImpl<KafkaDataSourceConfig> implements KafkaDataSourceDao {
	
	/**
	 * Fetch All KafkaDataSource Configuration
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<KafkaDataSourceConfig> getKafkaDataSourceConfigList() {
		Criteria criteria =getCurrentSession().createCriteria(KafkaDataSourceConfig.class);
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		return (List<KafkaDataSourceConfig>)criteria.list();
	}
	
	/**
	 * Fetch KafkaDataSource Configuration by name
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<KafkaDataSourceConfig> getKafkaDataSourceByName(String name) {
		Criteria criteria=getCurrentSession().createCriteria(KafkaDataSourceConfig.class);
		criteria.add(Restrictions.eq("name", name).ignoreCase());
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		return (List<KafkaDataSourceConfig>)criteria.list();
	}
	
	/**
	 * Fetch KafkaDataSourceConfig based on kafka server ip and server port
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<KafkaDataSourceConfig> getKafkaDSConfigByIPandPort(String kafkaSeverIP, int kafkaServerPort) {
		Criteria criteria =getCurrentSession().createCriteria(KafkaDataSourceConfig.class);
		criteria.add(Restrictions.eq("kafkaServerIpAddress", kafkaSeverIP));
		criteria.add(Restrictions.eq("kafkaServerPort", kafkaServerPort));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		return (List<KafkaDataSourceConfig>)criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public KafkaDataSourceConfig getKafkaDataSourceConfigById(Integer kafkaDSId) {
		Criteria criteria =getCurrentSession().createCriteria(KafkaDataSourceConfig.class);
		criteria.add(Restrictions.eq("id", kafkaDSId));
		KafkaDataSourceConfig kafkaDataSourceConfig = null;
		List<KafkaDataSourceConfig> kafkaDSList=(List<KafkaDataSourceConfig>)criteria.list();
		if(kafkaDSList!=null && !kafkaDSList.isEmpty()){
			kafkaDataSourceConfig=kafkaDSList.get(0);
		}
		return kafkaDataSourceConfig;
	}

}
