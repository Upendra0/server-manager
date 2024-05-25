package com.elitecore.sm.pathlist.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.pathlist.model.PathListHistory;

@Repository(value = "pathListHistoryDao")
public class PathListHistoryDaoImpl extends GenericDAOImpl<PathListHistory> implements PathListHistoryDao {
	
	@Override
	public PathListHistory getLatestHistoryFromPath(int pathId) {
		Criteria criteria = getCurrentSession().createCriteria(PathListHistory.class);
		criteria.add(Restrictions.eq("pathList.id", pathId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.addOrder(Order.desc("id"));
		criteria.setMaxResults(1);
		return (PathListHistory)criteria.uniqueResult();
	}
}
