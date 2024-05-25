package com.elitecore.sm.services.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.services.model.PartitionParam;

/**
 * @author vishal.lakhyani
 *
 */
@Repository(value="paramDao")
public class PartitionParamDaoImpl extends GenericDAOImpl<PartitionParam> implements  PartitionParamDao {

	
	/**
	 * Method will fetch all Partition parameters by service id.
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PartitionParam> getAllParamByServiceId(int id) {
		logger.debug("Method will fetch all partition parameters by service id " + id);
		Criteria criteria = getCurrentSession().createCriteria(PartitionParam.class);
		criteria.add(Restrictions.eq("parsingService.id", id));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		List<PartitionParam> partitionParamList = criteria.list();
		return  ((partitionParamList !=null) &&  !partitionParamList.isEmpty()) ?  partitionParamList : null;
	}
	
}
