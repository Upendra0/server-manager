/**
 * 
 */
package com.elitecore.sm.migration.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.migration.model.MigrationTrackDetails;
import com.elitecore.sm.services.model.MigrationStatusEnum;


/**
 * @author Ranjitsinh Reval
 *
 */
@Repository(value = "migrationTrackDao")
public class MigrationTrackDaoImpl  extends	GenericDAOImpl<MigrationTrackDetails> implements MigrationTrackDao {


	/**
	 * Method will get all open status instances for migration process. 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MigrationTrackDetails> getAllInstancesByStatus(MigrationStatusEnum migrationStatus) {
		logger.debug("Fetching all open status instances for migration process.");
		
		Criteria criteria = getCurrentSession().createCriteria(MigrationTrackDetails.class);
		criteria.add(Restrictions.eq(BaseConstants.MIGRATION_STATUS, migrationStatus));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		return criteria.list();
	}
	
	@Override
	public Map<String, Object> getMigrationTrackPaginatedList() {

		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditions = new ArrayList<>();

		conditions.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditions);

		return returnMap;
	}
	
	@Override
	public void clearSession() {
		getCurrentSession().flush();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MigrationTrackDetails> getMigrationTrackPaginatedList(Class<MigrationTrackDetails> classMigrationTrackDetails, List<Criterion> conditions,
			Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder) {

		List<MigrationTrackDetails> resultList;
		Criteria criteria = getCurrentSession().createCriteria(classMigrationTrackDetails);

		logger.debug("Sort column =" + sortColumn);
		
		if (("desc").equalsIgnoreCase(sortOrder)) {
			if (("id").equalsIgnoreCase(sortColumn)) {
				criteria.addOrder(Order.desc("id"));
			} else if (("Attempts").equalsIgnoreCase(sortColumn)) {
				criteria.addOrder(Order.desc("reprocessAttemptNumber"));
			} else if ((BaseConstants.MIGRATION_STATUS).equalsIgnoreCase(sortColumn)) {
				criteria.addOrder(Order.desc(BaseConstants.MIGRATION_STATUS));
			} else {
				criteria.addOrder(Order.desc(sortColumn)); 
			} 
		} else if (("asc").equalsIgnoreCase(sortOrder)) {
			if (("id").equalsIgnoreCase(sortColumn)) {
				criteria.addOrder(Order.asc("id"));
			} else if (("Attempts").equalsIgnoreCase(sortColumn)) {
				criteria.addOrder(Order.asc("reprocessAttemptNumber"));
			} else if ((BaseConstants.MIGRATION_STATUS).equalsIgnoreCase(sortColumn)) {
				criteria.addOrder(Order.asc(BaseConstants.MIGRATION_STATUS));
			} else {
				criteria.addOrder(Order.asc(sortColumn));
			}
		}
		//criteria.addOrder(Order.desc("migrationStartDate"));
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

		criteria.setFirstResult(offset);// first record is 2
		criteria.setMaxResults(limit);// records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MigrationTrackDetails> getMigrationTrackDetailsByIpAndPort(int serverId, int port) {
		logger.debug("Fetching all instances for migration process for ip and port.");
		Criteria criteria = getCurrentSession().createCriteria(MigrationTrackDetails.class);
		criteria.add(Restrictions.eq(BaseConstants.MIGRATION_STATUS, MigrationStatusEnum.OPEN));
		criteria.add(Restrictions.eq("server.id", serverId));
		criteria.add(Restrictions.eq("serverInstancePort", port));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		return criteria.list();
	}
}
