/**
 * 
 */
package com.elitecore.sm.systemparam.dao;

import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.systemparam.model.SystemParameterGroupData;

/**
 * @author vandana.awatramani
 *
 */
@Repository(value = "systemParamGroupDataDao")
public class SystemParamGroupDataDaoImpl extends GenericDAOImpl<SystemParameterGroupData> implements SystemParamGroupDataDao{

}
