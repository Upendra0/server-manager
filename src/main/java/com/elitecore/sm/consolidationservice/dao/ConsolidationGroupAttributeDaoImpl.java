package com.elitecore.sm.consolidationservice.dao;

import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.model.DataConsolidationGroupAttribute;

@Repository(value = "consolidationGroupAttributeDao")
public class ConsolidationGroupAttributeDaoImpl extends GenericDAOImpl<DataConsolidationGroupAttribute>
		implements IConsolidationGroupAttributeDao {

	/**
	 * Get data consolidation by Id
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DataConsolidation getDataConsolidationById(int id) {
		return (DataConsolidation)getCurrentSession().get(DataConsolidation.class, id);
	}
}
