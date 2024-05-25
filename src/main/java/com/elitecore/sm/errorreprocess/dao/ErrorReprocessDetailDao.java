/**
 * 
 */
package com.elitecore.sm.errorreprocess.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.errorreprocess.model.ErrorReprocessDetails;
import com.elitecore.sm.errorreprocess.model.SearchErrorReprocessDetails;


/**
 * @author Ranjitsinh Reval
 *
 */
public interface ErrorReprocessDetailDao  extends GenericDAO<ErrorReprocessDetails> {

	public Map<String, Object>  getBatchDetailsBySearchParameters(SearchErrorReprocessDetails searchErrorReprocessDetails);

	public List<ErrorReprocessDetails> getAllFileDetailsByBatchId(int batchId);
	public List<ErrorReprocessDetails> getErrorReprocessDetailsPaginatedList(Class<ErrorReprocessDetails> errorReprocessDetails, List<Criterion> conditions,
			Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder);
	
	public List<ErrorReprocessDetails> getAllErrorReprocessDetailByIds(Integer[] ids);
	
	
}
