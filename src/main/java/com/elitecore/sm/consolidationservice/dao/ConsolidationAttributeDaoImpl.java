package com.elitecore.sm.consolidationservice.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.consolidationservice.model.DataConsolidationAttribute;
import com.elitecore.sm.services.dao.ServicesDao;

@Repository(value = "consolidationAttributeDao")
public class ConsolidationAttributeDaoImpl extends GenericDAOImpl<DataConsolidationAttribute>
		implements IConsolidationAttributeDao {

	@Autowired
	ServicesDao servicesDao;

}
