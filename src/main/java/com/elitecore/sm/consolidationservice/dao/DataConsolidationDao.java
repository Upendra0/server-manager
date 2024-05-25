/**
 * 
 */
package com.elitecore.sm.consolidationservice.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;


/**
 * @author Ranjitsinh Reval
 *
 */
public interface DataConsolidationDao extends GenericDAO<DataConsolidation>{

	
	/**
	 * Method will return data consolidation service wise count. 
	 * @param name
	 * @param serviceId
	 * @return
	 */
	public List<DataConsolidation> getDataConsolidationServicewiseCount(String name, int serviceId);
		
}
