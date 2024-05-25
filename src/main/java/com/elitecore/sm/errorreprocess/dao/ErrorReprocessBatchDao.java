/**
 * 
 */
package com.elitecore.sm.errorreprocess.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.errorreprocess.model.ErrorReprocessingBatch;


/**
 * @author Ranjitsinh Reval
 *
 */
public interface ErrorReprocessBatchDao extends GenericDAO<ErrorReprocessingBatch> {

	public List<Object[]> getProcessingServiceDetailsByRule(Integer [] idList, int ruleId, String category);
	
	public void updateBatchDetails(ErrorReprocessingBatch errorReprocessBatch);
	
}
