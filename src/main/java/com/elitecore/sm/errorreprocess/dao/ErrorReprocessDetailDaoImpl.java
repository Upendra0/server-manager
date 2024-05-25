/**
 * 
 */
package com.elitecore.sm.errorreprocess.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.core.util.mbean.data.filereprocess.FileReprocessStatusEnum;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.errorreprocess.model.ErrorReprocessDetails;
import com.elitecore.sm.errorreprocess.model.SearchErrorReprocessDetails;
import com.elitecore.sm.services.model.ErrorReprocessingActionEnum;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;


/**
 * @author Ranjitsinh Reval
 *
 */

@Repository(value = "errorReprocessDetailDao")
public class ErrorReprocessDetailDaoImpl  extends GenericDAOImpl<ErrorReprocessDetails> implements ErrorReprocessDetailDao {

	/**
	 * Method will make hibernate condition list for search the error re-process batch details.
	 * @param searchErrorReprocessDetails
	 * @return
	 */
	@Override
	public Map<String, Object> getBatchDetailsBySearchParameters(SearchErrorReprocessDetails searchErrorReprocessDetails) {
		
		logger.debug(">> getBatchDetailsBySearchParameters in ErrorReprocessBatchDaoImpl "  +searchErrorReprocessDetails);
		
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		
		aliases.put("reprocessingBatch", "batch");
		
		if (searchErrorReprocessDetails.getBatchId() > 0) {
			conditionList.add(Restrictions.eq("batch.id", searchErrorReprocessDetails.getBatchId()));
		}
		if (!StringUtils.isEmpty(searchErrorReprocessDetails.getServiceType()) && !"-1".equals(searchErrorReprocessDetails.getServiceType())) {
			aliases.put("svctype", "serviceType");
			conditionList.add(Restrictions.eq("serviceType.alias", searchErrorReprocessDetails.getServiceType()));
		}
		
		if(!StringUtils.isEmpty(searchErrorReprocessDetails.getServiceInstanceIds()) && !"0".equalsIgnoreCase(searchErrorReprocessDetails.getServiceInstanceIds().trim())) {
			Integer[] serviceIds = EliteUtils.convertStringArrayToInt(searchErrorReprocessDetails.getServiceInstanceIds());
			if(serviceIds != null && serviceIds.length > 0){
				conditionList.add(Restrictions.in("service.id", serviceIds));
			}
		}
		
		/*if (searchErrorReprocessDetails.getServiceId() >  0) {
			aliases.put("batch.service", "service");
			conditionList.add(Restrictions.eq("service.id", searchErrorReprocessDetails.getServiceId()));
		}*/
		
		if (!StringUtils.isEmpty(searchErrorReprocessDetails.getFileNameContains()) && !"undefined".equals(searchErrorReprocessDetails.getFileNameContains())) {
			conditionList.add(Restrictions.like("fileName", "%"+searchErrorReprocessDetails.getFileNameContains()+"%"));
		}
		
		if (!StringUtils.isEmpty(searchErrorReprocessDetails.getReprocessStatus()) && !"-1".equals(searchErrorReprocessDetails.getReprocessStatus())) {
			conditionList.add(Restrictions.eq("errorReprocessStatus", FileReprocessStatusEnum.valueOf(searchErrorReprocessDetails.getReprocessStatus())));
		}
		
		if (!StringUtils.isEmpty(searchErrorReprocessDetails.getFileAction()) && !"-1".equals(searchErrorReprocessDetails.getFileAction())) {
			conditionList.add(Restrictions.eq("batch.errorProcessAction", ErrorReprocessingActionEnum.valueOf(searchErrorReprocessDetails.getFileAction())));
		}

		if(!StringUtils.isEmpty(searchErrorReprocessDetails.getFromDate()) ){
			Date startDate = DateFormatter.formatDate(searchErrorReprocessDetails.getFromDate(), MapCache.getConfigValueAsString(SystemParametersConstant.DATE_FORMAT,"dd-MM-yyyy HH:mm:ss"));
			if(startDate != null){
				LogicalExpression orExp = Restrictions.or(Restrictions.ge("reprocessStartTime", startDate), Restrictions.ge("createdDate", startDate));
				conditionList.add(orExp);
			}
		}
			
		if(!StringUtils.isEmpty(searchErrorReprocessDetails.getToDate()) ){
			Date endDate = DateFormatter.formatDate(searchErrorReprocessDetails.getToDate(), MapCache.getConfigValueAsString(SystemParametersConstant.DATE_FORMAT,"dd-MM-yyyy HH:mm:ss"));
			if(endDate != null){
				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.set(Calendar.HOUR, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.HOUR, 59);
				LogicalExpression orExp = Restrictions.or(Restrictions.le("reprocessEndTime", cal.getTime()), Restrictions.le("createdDate", cal.getTime()));
				conditionList.add(orExp);
			}
		}
		
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		
		logger.debug("<< getBatchDetailsBySearchParameters in ErrorReprocessDetailDaoImpl ");
		
		return returnMap;
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ErrorReprocessDetails> getAllFileDetailsByBatchId(int batchId) {
		Criteria criteria = getCurrentSession().createCriteria(ErrorReprocessDetails.class);
		criteria.add(Restrictions.eq("reprocessingBatch.id",batchId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		return criteria.list();
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ErrorReprocessDetails> getErrorReprocessDetailsPaginatedList(Class<ErrorReprocessDetails> errorReprocessDetails, List<Criterion> conditions,
			Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder) {

		List<ErrorReprocessDetails> resultList;
		Criteria criteria = getCurrentSession().createCriteria(errorReprocessDetails);

		logger.debug("Sort column =" + sortColumn);
		
		
		criteria.addOrder(Order.desc("id"));
			
		
		if (conditions != null) {
			for (Criterion condition : conditions) {
				criteria.add(condition);
			}
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		

		if (aliases != null) {
			for (Entry<String, String> entry : aliases.entrySet()) {
				criteria.createAlias(entry.getKey(), entry.getValue());
			}
		}

		if(offset > 0) {
			criteria.setFirstResult(offset);// first record is 2
		}
		criteria.setMaxResults(limit);// records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ErrorReprocessDetails> getAllErrorReprocessDetailByIds(Integer[] ids) {
		logger.debug(">>  getAllErrorReprocessDetailByIds ");
		Criteria criteria = getCurrentSession().createCriteria(ErrorReprocessDetails.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		List<ErrorReprocessDetails> errorReprocessDetailList = null;
		if(ids != null && ids.length > 0){
			criteria.add(Restrictions.in("id", ids));
			errorReprocessDetailList = criteria.list();
		}
		return errorReprocessDetailList;
	}
	
}
