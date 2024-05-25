/**
 * 
 */
package com.elitecore.sm.errorreprocess.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.errorreprocess.model.ErrorReprocessingBatch;


/**
 * @author Ranjitsinh Reval
 *
 */

@Repository(value = "errorReprocessBatchDao")
public class ErrorReprocessBatchDaoImpl extends GenericDAOImpl<ErrorReprocessingBatch> implements ErrorReprocessBatchDao {

	
	/**
	 *Method will get all processing details rule and service id.
	 *@param details
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getProcessingServiceDetailsByRule(Integer [] idList, int ruleId, String category) {
		
		StringBuilder hqlQuery = new StringBuilder();
		
		//Please do not change sequence for select column as its used in front end.
		if("Error".equalsIgnoreCase(category) 
				|| "Duplicate".equalsIgnoreCase(category)
				|| "Archive".equalsIgnoreCase(category)
	    		|| "Input".equalsIgnoreCase(category)
	    		|| "Output".equalsIgnoreCase(category)){
			hqlQuery.append(" SELECT DISTINCT s.id,si.id,s.name,si.name,server.ipAddress,si.port,s.svctype.id,path.readFilePath,path.pathId   ");
		}else{
			hqlQuery.append(" SELECT DISTINCT s.id,si.id,s.name,si.name,server.ipAddress,si.port,s.svctype.id,path.readFilePath,path.pathId,pr.id,pr.name   ");
		}
		
		hqlQuery.append(" FROM ProcessingService s  ");
		hqlQuery.append(" INNER JOIN s.fileGroupingParameter fgp ON fgp.status =:fileGroupStatus");
		hqlQuery.append(" INNER JOIN s.serverInstance si ON si.status=:instanceStatus ");
		hqlQuery.append(" INNER JOIN s.svcPathList path ON path.status=:pathStatus ");
		hqlQuery.append(" INNER JOIN si.server server ON server.status=:serverStatus ");

		if(ruleId!=-1) {
			hqlQuery.append(" INNER JOIN path.policy p  ON p.status=:policyStatus ");
			hqlQuery.append(" INNER JOIN p.policyGroupRelSet pgr ");
			hqlQuery.append(" INNER JOIN pgr.group grp ON grp.status=:groupStatus ");
			hqlQuery.append(" INNER JOIN grp.policyGroupRuleRelSet rr ");
			hqlQuery.append(" INNER JOIN rr.policyRule pr  ON pr.status=:ruleStatus ");
			hqlQuery.append(" INNER JOIN pr.policyRuleActionRel par ");
			hqlQuery.append(" INNER JOIN par.action pa ON pa.status=:actionStatus ");
		}
		
	    if("Error".equalsIgnoreCase(category) 
	    		|| "Archive".equalsIgnoreCase(category)
	    		|| "Input".equalsIgnoreCase(category)
	    		|| "Output".equalsIgnoreCase(category)){
	    	hqlQuery.append(" WHERE s.id in (:idList) ");
	    }else if("Duplicate".equalsIgnoreCase(category)){
	    	hqlQuery.append(" WHERE  s.id in (:idList) AND fgp.fileGroupEnable='Y' AND fgp.enableForDuplicate='Y' ");
	    }else if("Filter".equalsIgnoreCase(category) 
	    		|| "Invalid".equalsIgnoreCase(category)) {
	    	hqlQuery.append(" WHERE s.id in (:idList) AND pr.id=:ruleId ");
	    }
		
		Query query = getCurrentSession().createQuery(hqlQuery.toString());
		query.setParameter("fileGroupStatus" , StateEnum.ACTIVE);
		query.setParameter("pathStatus" , StateEnum.ACTIVE);
		if(ruleId!=-1) {
			query.setParameter("policyStatus" , StateEnum.ACTIVE);
			query.setParameter("groupStatus" , StateEnum.ACTIVE);
			query.setParameter("ruleStatus" , StateEnum.ACTIVE);
			query.setParameter("actionStatus" , StateEnum.ACTIVE);
		}
		query.setParameter("instanceStatus" , StateEnum.ACTIVE);
		query.setParameter("serverStatus" , StateEnum.ACTIVE);
		if("Filter".equalsIgnoreCase(category) || "Invalid".equalsIgnoreCase(category)) {
			query.setParameter("ruleId" , ruleId);
	    }
		query.setParameterList("idList",idList);
		return query.list();
	}

	@Override
	public void updateBatchDetails(ErrorReprocessingBatch errorReprocessBatch) {
		getCurrentSession().merge(errorReprocessBatch);
	}
	
	
}
