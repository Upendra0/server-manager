/**
 * 
 */
package com.elitecore.sm.iam.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.iam.model.AccessGroup;

/**
 * @author Sunil Gulabani Apr 7, 2015
 */
@Repository(value = "accessGroupDAO")
public class AccessGroupDAOImpl extends GenericDAOImpl<AccessGroup> implements AccessGroupDAO {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * This method provide custom native criteria map based on the search
	 * parameters provided.
	 */
	@Override
	public Map<String, Object> createCriteriaConditionsForPagination(int searchCreatedByStaffId, String searchAccessGroupName,
			String searchAssignedUnassignedStatus, String searchActiveInactiveStatus) {

		Map<String, Object> conditions = new HashMap<>();

		if (searchCreatedByStaffId != 0 && searchCreatedByStaffId != 1) {
			logger.info("searchCreatedByStaffId: " + searchCreatedByStaffId);
			conditions.put("createdbystaffid", searchCreatedByStaffId);
		}

		if (!StringUtils.isEmpty(searchAccessGroupName)) {
			conditions.put("name", "%" + searchAccessGroupName.trim().toLowerCase() + "%");
		} else {
			conditions.put("name", "%%");
		}

		if (!StringUtils.isEmpty(searchAssignedUnassignedStatus) && !"ALL".equals(searchAssignedUnassignedStatus)) {
			logger.info("searchStatus: " + searchAssignedUnassignedStatus);
			String searchAssignedUnassignedStatusTrim = searchAssignedUnassignedStatus.trim();
			if ("Assigned".equals(searchAssignedUnassignedStatusTrim))
				conditions.put(BaseConstants.ASSIGNEDUNASSIGNEDSTATUS, "Assigned");
			else if ("Unassigned".equals(searchAssignedUnassignedStatusTrim))
				conditions.put(BaseConstants.ASSIGNEDUNASSIGNEDSTATUS, "Unassigned");
			else
				conditions.put(BaseConstants.ASSIGNEDUNASSIGNEDSTATUS, "%%");
		} else {
			conditions.put(BaseConstants.ASSIGNEDUNASSIGNEDSTATUS, "%%");
		}

		if (!StringUtils.isEmpty(searchActiveInactiveStatus)) {
			String searchActiveInactiveStatusTrim = searchActiveInactiveStatus.trim();
			if ("Active".equals(searchActiveInactiveStatusTrim))
				conditions.put(BaseConstants.STATE, StateEnum.ACTIVE.toString());
			else if ("Inactive".equals(searchActiveInactiveStatusTrim))
				conditions.put(BaseConstants.STATE, StateEnum.INACTIVE.toString());
			else
				conditions.put(BaseConstants.STATE, "%%");
		} else {
			conditions.put(BaseConstants.STATE, "%%");
		}
		
		return conditions;
	}

	/**
	 * Gets the Access Group Staff Relation Unique Ids to check for the Assigned
	 * Access Groups to Staff.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getAccessGroupStaffRelUniqueIds(int accessGroupId) {
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery("select  distinct accessgroupid from TBLTSTAFFACCESSGROUPREL");
		if (accessGroupId != 0) {
			sqlQuery = getCurrentSession().createSQLQuery(
					"select  distinct accessgroupid from TBLTSTAFFACCESSGROUPREL where accessgroupid = :accessgroupid");
			sqlQuery.setParameter("accessgroupid", accessGroupId);
		}

		return sqlQuery.list();
	}

	/**
	 * Get Access Group Id by access group name.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int getAccessGroupIdByName(String accessGroupName) {

		Criteria criteria = getCurrentSession().createCriteria(AccessGroup.class);
		criteria.add(Restrictions.eq("name", accessGroupName));
		criteria.setProjection(Projections.distinct(Projections.property("id")));

		List<Integer> accessGroupIdList = criteria.list();

		return (accessGroupIdList != null && !accessGroupIdList.isEmpty()) ? (int) accessGroupIdList.get(0) : 0;
	}

	/**
	 * Get Access Group Model by access group name.
	 */
	@Override
	public AccessGroup getAccessGroup(String accessGroupName) {
		Criteria criteria = getCurrentSession().createCriteria(AccessGroup.class);
		criteria.add(Restrictions.eq("name", accessGroupName));
		criteria.add(Restrictions.ne("accessGroupState", StateEnum.DELETED));

		return (AccessGroup) criteria.uniqueResult();
	}

	@Override
	public String getDynamicNativeQueryForAccessGroupSearchCount(int searchCreatedByStaffId) {
		StringBuilder sqlBuilder = new StringBuilder(
				" select count(*) as count from ( "
						+ " 	select    "
						+ " 		ag.id, ag.name, ag.description,  ag.state as accessGroupState,   ag.accessGroupType,	 ag.lastupdatedate, staff.username,   "
						+ " 		(select  CASE         WHEN(count(*) > 0 ) THEN  'Assigned'      ELSE 'Unassigned'  END  from TBLTSTAFFACCESSGROUPREL rel, TBLMSTAFF ts where rel.accessgroupid = ag.id and rel.staffid = ts.id and ts.ACCOUNTSTATE <> 'DELETED') as assignedUnassignedStatus "
						+ " 	from   " + " 		TBLMACCESSGROUP ag " + " 			join  " + " 		TBLMSTAFF staff " + " 			on  "
						+ " 		ag.createdbystaffid = staff.id " + " 	where            " + " 		lower(ag.name) like :name  " + " and"
						+ " 		(ag.name) not in ('" + BaseConstants.MODULE_ADMIN_ACCCESS_GROUP + "','" + BaseConstants.PROFILE_ADMIN_ACCCESS_GROUP
						+ "') " + " 			and            " + " 		ag.state like :state  " + " 	and      " + " 		ag.state <> 'DELETED' ");
		if (searchCreatedByStaffId != 0 && searchCreatedByStaffId != 1) {
			sqlBuilder.append(" and  ag.createdbystaffid = :createdbystaffid and ag.accessGroupType = 'LOCAL' ");
		}
		sqlBuilder.append(" )  a where a.assignedUnassignedStatus like :assignedUnassignedStatus");
		return sqlBuilder.toString();
	}

	@Override
	public String getDynamicNativeQueryForAccessGroupSearchResult(int searchCreatedByStaffId) {
		StringBuilder sqlBuilder = new StringBuilder(
				"select * from ( "
						+ " 	select    "
						+ " 		ag.id, ag.name, ag.description,  ag.state as accessGroupState,   ag.accessGroupType,		 ag.lastupdatedate, staff.username,   "
						+ " 		(select  CASE         WHEN(count(*) > 0 ) THEN  'Assigned'      ELSE 'Unassigned'  END  from TBLTSTAFFACCESSGROUPREL rel, TBLMSTAFF ts where rel.accessgroupid = ag.id and rel.staffid = ts.id and ts.ACCOUNTSTATE <> 'DELETED') as assignedUnassignedStatus "
						+ " 	from   " + " 		TBLMACCESSGROUP ag " + " 			join  " + " 		TBLMSTAFF staff " + " 			on  "
						+ " 		ag.createdbystaffid = staff.id " + " 	where            " + " 		lower(ag.name) like :name  " + " 			and            "
						+ " 		(ag.name) not in ('" + BaseConstants.MODULE_ADMIN_ACCCESS_GROUP + "','" + BaseConstants.PROFILE_ADMIN_ACCCESS_GROUP
						+ "') " + " 			and           " + " 		ag.state like :state  " + " 			and           " + " 		ag.state <> 'DELETED' ");

		if (searchCreatedByStaffId != 0 && searchCreatedByStaffId != 1) {
			sqlBuilder.append(" and  ag.createdbystaffid = :createdbystaffid and ag.accessGroupType = 'LOCAL' ");
		}
		sqlBuilder.append(" )  a where a.assignedUnassignedStatus like :assignedUnassignedStatus ");
		return sqlBuilder.toString();
	}
	
	/**
	 * Fetch all active access group
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AccessGroup> getAllAccessGroup() {

		Criteria criteria = getCurrentSession().createCriteria(AccessGroup.class);

		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.ne("accessGroupState", StateEnum.DELETED));
		criteria.add(Restrictions.ne("id", 2));
		criteria.add(Restrictions.ne("id", 3));

		List<AccessGroup> accessGroupList = criteria.list();

		for (AccessGroup accessGroup : accessGroupList) {
			Hibernate.initialize(accessGroup.getActions());
		}
		return accessGroupList;
	}

	/**
	 * Fetch all groups name except admin, profileadmin and moduleadmin
	 * */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllAccessGroupName() {
		Criteria criteria = getCurrentSession().createCriteria(AccessGroup.class).setProjection(Projections.distinct(Projections.property("name")));
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("accessGroupState", StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("accessGroupType", BaseConstants.LDAP_STAFF));
		criteria.add(Restrictions.ne("id", 1));
		criteria.add(Restrictions.ne("id", 2));
		criteria.add(Restrictions.ne("id", 3));
		List<String> accessGroupList = criteria.list();
		return accessGroupList;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllAccessGroupByStaffType(String staffType) {
		Criteria criteria = getCurrentSession().createCriteria(AccessGroup.class).setProjection(Projections.distinct(Projections.property("name")));
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("accessGroupState", StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("accessGroupType", staffType));
		criteria.add(Restrictions.ne("id", 1));
		criteria.add(Restrictions.ne("id", 2));
		criteria.add(Restrictions.ne("id", 3));
		List<String> accessGroupList = criteria.list();
		return accessGroupList;
	}
	
	@Override
	public AccessGroup getAccessGroupForLDAPStaff(String accessGroupName) {
		Criteria criteria = getCurrentSession().createCriteria(AccessGroup.class);
		criteria.add(Restrictions.eq("name", accessGroupName));
		criteria.add(Restrictions.ne("accessGroupState", StateEnum.DELETED));
		criteria.add(Restrictions.eq("accessGroupType", BaseConstants.LDAP_STAFF));
		return (AccessGroup) criteria.uniqueResult();
	}

	@Override
	public AccessGroup getAccessGroupForSSOStaff(String accessGroupName) {
		Criteria criteria = getCurrentSession().createCriteria(AccessGroup.class);
		criteria.add(Restrictions.eq("name", accessGroupName));
		criteria.add(Restrictions.ne("accessGroupState", StateEnum.DELETED));
		criteria.add(Restrictions.eq("accessGroupType", BaseConstants.SSO_STAFF));
		return (AccessGroup) criteria.uniqueResult();
	}
}