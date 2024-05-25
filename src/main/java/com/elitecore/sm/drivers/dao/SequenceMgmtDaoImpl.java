package com.elitecore.sm.drivers.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.ControlFileResetFrequency;
import com.elitecore.sm.common.model.SequenceManagement;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.drivers.model.DistributionDriver;

@Repository(value="SequenceMgmtDao")
public class SequenceMgmtDaoImpl extends GenericDAOImpl<SequenceManagement> implements SequenceMgmtDao {

	/*@Override
	public void update(SequenceManagement seqMgmt) {
		getCurrentSession().merge(seqMgmt);
	}
	*/
	@Override
	@SuppressWarnings("unchecked")
	public SequenceManagement getControlFileSequenceBySequenceId(int sequenceId) {
		logger.debug("Fetching Control File Sequence from Sequence id : " + sequenceId);
		SequenceManagement sequenceManagement = null;
		
		Criteria criteria=getCurrentSession().createCriteria(SequenceManagement.class);
		criteria.add(Restrictions.eq("id", sequenceId));
		
		List<SequenceManagement> sequenceManagementList = (List<SequenceManagement>)criteria.list();
		
		if (sequenceManagementList != null && !sequenceManagementList.isEmpty())
		{
			sequenceManagement = sequenceManagementList.get(0);
		}
		return sequenceManagement;
	}
}
