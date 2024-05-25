/**
 * 
 */
package com.elitecore.sm.job.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.errorreprocess.model.AutoErrorReprocessDetail;
import com.elitecore.sm.job.model.CrestelSMJob;


@Repository(value="jobDao")
public class JobDaoImpl extends GenericDAOImpl<CrestelSMJob> implements JobDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<CrestelSMJob> getAllJobList() {
			logger.info(">> getAllJobs ");
			Criteria criteria=getCurrentSession().createCriteria(CrestelSMJob.class);
			criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
			criteria.addOrder(Order.asc("id"));
			logger.info("<< getAllJobs ");
			return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CrestelSMJob> getJobListByTriggerId(int triggerId){
		logger.info(">> getJobListByTriggerId ");
		Criteria criteria=getCurrentSession().createCriteria(CrestelSMJob.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("trigger.id", triggerId));
		criteria.addOrder(Order.asc("id"));
		logger.info("<< getJobListByTriggerId ");
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getAutoJobListByJobId(Class<? extends BaseModel> kclass, int id){
		logger.info(">> getJobListByTriggerId ");
		Criteria criteria=getCurrentSession().createCriteria(kclass);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		if(AutoErrorReprocessDetail.class.equals(kclass)) {
            criteria.add(Restrictions.eq("job.ID", id));
        }else {
            criteria.add(Restrictions.eq("scheduler.ID", id));
        }
		criteria.addOrder(Order.asc("id"));
		logger.info("<< getJobListByTriggerId ");
		return criteria.list();
	}
	
}