package com.elitecore.sm.services.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.services.model.PartitionParam;

public interface PartitionParamDao extends GenericDAO<PartitionParam>{

	public List<PartitionParam> getAllParamByServiceId(int id);
}
