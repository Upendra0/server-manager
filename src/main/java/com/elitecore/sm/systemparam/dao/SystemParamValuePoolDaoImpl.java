/**
 * 
 */
package com.elitecore.sm.systemparam.dao;

import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.systemparam.model.SystemParameterValuePoolData;

/**
 * @author vandana.awatramani
 *
 */
@Repository(value = "systemParamValuePoolDao")
public class SystemParamValuePoolDaoImpl extends GenericDAOImpl<SystemParameterValuePoolData> implements SystemParamValuePoolDataDao {
	
}
