/**
 * 
 */
package com.elitecore.sm.iam.dao;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.iam.model.AccessGroup;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.util.DateFormatter;

/**
 * @author Sunil Gulabani
 * Apr 6, 2015
 */
@Repository(value = "staffDAO")
public class StaffDAOImpl extends GenericDAOImpl<Staff> implements StaffDAO{
	
	/**
	 * Get's the Staff's profile pic as Blob
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Blob getStaffProfilePicAsBlob(int staffId){
		Blob profilePic = null;
		Criteria criteria = getCurrentSession().createCriteria(Staff.class);
		criteria.add(Restrictions.eq("id",staffId));
		criteria.add(Restrictions.ne(BaseConstants.ACCOUNTSTATE,StateEnum.DELETED));
		criteria.setProjection(Projections.property("profilePic"));
		List list = criteria.list();
		if(list!=null){
			Iterator iterator = list.iterator();
			while(iterator.hasNext()){
				profilePic = (Blob)iterator.next();
			}
		}
		return profilePic;
	}

	/**
	 * Provide Staff Id by email id
	 */

	@SuppressWarnings("unchecked")
	@Override
	public int getStaffIdByEmailId(String emailId){
		
		
		Criteria criteria = getCurrentSession().createCriteria(Staff.class);
		criteria.add(Restrictions.eq(BaseConstants.EMAIL_ID,emailId));
		criteria.setProjection(Projections.distinct(Projections.property("id")));
		
		List<Integer> staffIdList=criteria.list();

		return (staffIdList!=null && !staffIdList.isEmpty()) ? (int)staffIdList.get(0):0;
	}

	/**
	 * Provide Staff based on Username.
	 * 
	 * extracted to support method to case insensitive
	 * 
	 */
	@Override
	public Staff getStaffDetails(String username, boolean iscasesensitive) {
		Criteria criteria = getCurrentSession().createCriteria(Staff.class);
		if (iscasesensitive)
			criteria.add(Restrictions.eq(BaseConstants.USERNAME ,username));
		else
			criteria.add(Restrictions.eq(BaseConstants.USERNAME ,username).ignoreCase());
		criteria.add(Restrictions.ne(BaseConstants.ACCOUNTSTATE,StateEnum.DELETED));
		return (Staff) criteria.uniqueResult();
	}
	
	/**
	 * Provide Staff based on Username.
	 */
	@Override
	public Staff getStaffDetailsByType(String username, String type) {
		Criteria criteria = getCurrentSession().createCriteria(Staff.class);
		Criterion ldapStaff = Restrictions.eq(BaseConstants.STAFF_TYPE, BaseConstants.LDAP_STAFF);
		Criterion localStaff = Restrictions.eq(BaseConstants.STAFF_TYPE, BaseConstants.LOCAL_STAFF);
		LogicalExpression orExp = Restrictions.or(ldapStaff, localStaff);
		criteria.add(Restrictions.eq(BaseConstants.USERNAME ,username));
		criteria.add(orExp);
		criteria.add(Restrictions.ne(BaseConstants.ACCOUNTSTATE,StateEnum.DELETED));
		return (Staff) criteria.uniqueResult();
	}
	
	/**
	 * Provides the Staff based on Email Id.
	 */
	@Override
	public Staff getStaffDetailsByEmailId(String emailId) {
		Criteria criteria = getCurrentSession().createCriteria(Staff.class);
		
		criteria.add(Restrictions.eq(BaseConstants.EMAIL_ID,emailId));
		criteria.add(Restrictions.ne(BaseConstants.ACCOUNTSTATE,StateEnum.DELETED));
		return (Staff) criteria.uniqueResult();
	}
	
	/**
	 * Provides Staff based on staff id.
	 */
	@Override
	public Staff getStaffDetailsById(int staffId) {
		Criteria criteria = getCurrentSession().createCriteria(Staff.class);
		criteria.add(Restrictions.eq("id",staffId));
		criteria.add(Restrictions.ne(BaseConstants.ACCOUNTSTATE,StateEnum.DELETED));
		return (Staff) criteria.uniqueResult();
	}
	
	/**
	 * Provides All staff id and username from database.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String,String> getAllStaffIdAndUsername(){
		Map<String,String> staffIdsAndUsername = new HashMap<>();
		
		Criteria criteria = getCurrentSession().createCriteria(Staff.class);
		criteria.add(Restrictions.or(Restrictions.eq(BaseConstants.ACCOUNTSTATE,StateEnum.ACTIVE),Restrictions.eq(BaseConstants.ACCOUNTSTATE,StateEnum.INACTIVE)));	
		criteria.add(Restrictions.ne("id", 2));
		criteria.add(Restrictions.ne("id", 3));
		criteria.setProjection(Projections.projectionList()
	            .add(Projections.property("id"))
	            .add(Projections.property(BaseConstants.USERNAME ))   
				);
		List staffList=criteria.list();
		
		if(staffList!=null){
			Object[] staffRow; 			
			for (Iterator it = staffList.iterator(); it.hasNext(); ) {
				staffRow = (Object[]) it.next();
				staffIdsAndUsername.put(String.valueOf(staffRow[0]), (String) staffRow[1]);
			}
		}
		
		return staffIdsAndUsername;
	}
	
	/**
	 * Provides complete hierarchy of Staff based on Username.
	 */
	@Override
	public Staff getFullStaffDetails(String username) {
		Criteria criteria = getCurrentSession().createCriteria(Staff.class);
		criteria.add(Restrictions.eq(BaseConstants.USERNAME ,username));
		criteria.add(Restrictions.ne(BaseConstants.ACCOUNTSTATE,StateEnum.DELETED));
		Staff user = (Staff) criteria.uniqueResult();
		logger.info(BaseConstants.USER + user);
		iterateFullStaffSubModels(user);
		return user;
	}
	
	/**
	 * Provides the complete staff hierarchy based on email id.
	 */
	@Override
	public Staff getFullStaffDetailsByEmailId(String emailId) {
		Criteria criteria = getCurrentSession().createCriteria(Staff.class);
		criteria.add(Restrictions.eq(BaseConstants.EMAIL_ID,emailId));
		criteria.add(Restrictions.ne(BaseConstants.ACCOUNTSTATE,StateEnum.DELETED));
		Staff user = (Staff) criteria.uniqueResult();
		logger.info(BaseConstants.USER + user);
		iterateFullStaffSubModels(user);
		return user;
	}
	
	/**
	 * Provides the complete hierarchy of Staff based on Staff Id.
	 */
	@Override
	public Staff getFullStaffDetailsById(int staffId) {
		Criteria criteria = getCurrentSession().createCriteria(Staff.class);
		criteria.add(Restrictions.eq("id",staffId));
		criteria.add(Restrictions.ne(BaseConstants.ACCOUNTSTATE,StateEnum.DELETED));
		Staff user = (Staff) criteria.uniqueResult();
		logger.info(BaseConstants.USER + user);
		iterateFullStaffSubModels(user);
		return user;
	}
	
	/**
	 * Iterates over the Staff hierarchy for fetching complete hierarchy of Staff.
	 * @param user
	 */
	private void iterateFullStaffSubModels(Staff user){
		if(user!=null){
			logger.debug(BaseConstants.USER + user);
			if(user.getAccessGroupList()!=null){
				for(AccessGroup accessGroup : user.getAccessGroupList()){
					logger.debug("accessGroup: " + accessGroup);
					if(accessGroup!=null && accessGroup.getActions()!=null){
						logger.debug("accessGroup.getActions(): " + accessGroup.getActions());
						for(Action action : accessGroup.getActions()){
							logger.debug("action: " + action);
							if(action.getParentBusinessSubModule()!=null){
								logger.debug("action.getParentBusinessSubModule(): " + action.getParentBusinessSubModule());
								action.getParentBusinessSubModule();
								
								logger.debug("action.getParentBusinessSubModule(): " + action.getParentBusinessSubModule().getParentBusinessModule());
								action.getParentBusinessSubModule().getParentBusinessModule();
							}
						}
					}
				}
			}
			

		}
	}
	
	/**
	 * Release the Staff Lock
	 */
	@Override
	public int releaseStaffLock(){
		
		return 0;
	}
	
	/**
	 * Creates the criteria conditions based on the input.
	 */
	@Override
	public Map<String,Object> createCriteriaConditions(int searchCreatedByStaffId, String searchUsername,
			String searchFirstName, String searchLastName, String searchEmployeeId, Date startDate,
			Date endDate, String searchAccountState,
			String searchEmailId, String searchAccessGroupId, String searchLockStatus, String excludeStaffUsernames) {
		Map<String,Object> returnMap = new HashMap<>();
		
		HashMap<String,String> aliases = new HashMap<>();
		
		List<Criterion> conditions = new ArrayList<>();
		
		int isearchAccessGroupId;
		
		if(searchCreatedByStaffId != 0){
			logger.info("searchCreatedByStaffId: " + searchCreatedByStaffId);
			conditions.add(Restrictions.eq("createdByStaffId",searchCreatedByStaffId));
		}
		
		if(!StringUtils.isEmpty(searchUsername)){
			logger.info("searchUsername: " + searchUsername);
			String searchUsernameNew = searchUsername.trim();
			conditions.add(Restrictions.like(BaseConstants.USERNAME , "%" + searchUsernameNew + "%"));
		}
		
		if(!StringUtils.isEmpty(searchEmailId)){
			logger.info("searchEmailId: " + searchEmailId);
			String searchEmailIdNew = searchEmailId.trim();
			conditions.add(Restrictions.like(BaseConstants.EMAIL_ID, "%" + searchEmailIdNew + "%"));
		}
		
		if(!StringUtils.isEmpty(searchFirstName)){
			logger.info("searchFirstName: " + searchFirstName);
			String searchFirstNameNew = searchFirstName.trim();
			conditions.add(Restrictions.like(BaseConstants.FIRST_NAME , "%" + searchFirstNameNew + "%"));
		}
		
		if(!StringUtils.isEmpty(searchLastName)){
			logger.info("searchLastName: " + searchLastName);
			String searchLastNameNew = searchLastName.trim();
			conditions.add(Restrictions.like(BaseConstants.LAST_NAME, "%" + searchLastNameNew + "%"));
		}
		
		if(!StringUtils.isEmpty(searchEmployeeId)){
			String searchEmployeeIdNew = searchEmployeeId.trim();
			logger.info("searchEmployeeId: " + searchEmployeeIdNew);
			conditions.add(Restrictions.like("staffCode", "%" + searchEmployeeIdNew + "%"));
		}

		if(!StringUtils.isEmpty(searchAccountState)){
			String searchAccountStateNew = searchAccountState.trim();
			logger.info("searchAccountState: " + searchAccountStateNew);
			if(("ACTIVE").equals(searchAccountStateNew))
				conditions.add(Restrictions.eq(BaseConstants.ACCOUNTSTATE, StateEnum.ACTIVE));
			else if(("INACTIVE").equals(searchAccountStateNew))
				conditions.add(Restrictions.eq(BaseConstants.ACCOUNTSTATE, StateEnum.INACTIVE));
		}
		
		if(!StringUtils.isEmpty(searchLockStatus)){
			String searchLockStatusNew = searchLockStatus.trim();
			logger.info("searchLockStatus: " + searchLockStatusNew);
			if(("LOCK").equals(searchLockStatusNew))
				conditions.add(Restrictions.eq("accountLocked", true));
			else if(("UNLOCK").equals(searchLockStatusNew))
				conditions.add(Restrictions.eq("accountLocked", false));
		}
		
		if(!StringUtils.isEmpty(startDate)){
			Date startDateNew = DateFormatter.getStartOfDay(startDate);
			logger.info("startDate: " + startDateNew);
			
			conditions.add(Restrictions.ge(
							"createdDate", 
							startDateNew)
						);
		}

		if(!StringUtils.isEmpty(endDate)){
		Date endDateNew = DateFormatter.getEndOfDay(endDate);
			logger.info("endDate: " + endDateNew);

			conditions.add(Restrictions.le(
							"createdDate", 
							endDateNew)
						);
		}
		
		if(("ALL").equals(searchAccessGroupId)){
			isearchAccessGroupId=0;
		}else{
			isearchAccessGroupId=Integer.parseInt(searchAccessGroupId);
		}
		if(isearchAccessGroupId!=0){
			logger.info("searchAccessGroupId: " + isearchAccessGroupId);

			aliases.put("accessGroupList", "ag");
			conditions.add(Restrictions.eq("ag.id",isearchAccessGroupId));
		}
		
		conditions.add(Restrictions.ne(BaseConstants.ACCOUNTSTATE,StateEnum.DELETED));
		
		if(!StringUtils.isEmpty(excludeStaffUsernames)){
			logger.info("excludeStaffUsernames: " + excludeStaffUsernames);
			String excludeStaffUsernamesNew = excludeStaffUsernames.trim();
			String [] excludeStaffUsernamesArray = excludeStaffUsernamesNew.split(",");
			for(String staffUsername : excludeStaffUsernamesArray){
				conditions.add(Restrictions.ne(BaseConstants.USERNAME , staffUsername));
			}
		}

		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditions);

		return returnMap;
	}
	
	
	/**
	 * Verify staff by username or emailid and fetch question detail
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map<String,Object> verifyStaffUserByCredential(String username,String emailId){
		Map<String,Object> staffIdsAndUsername = new HashMap<>();
		List<String> questionList;
		
		List staffList ;
		Criteria staffCri = getCurrentSession().createCriteria(Staff.class);
		
		Criterion usrname = Restrictions.eq(BaseConstants.USERNAME , username);
		Criterion email = Restrictions.eq(BaseConstants.EMAIL_ID,emailId);
		
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property(BaseConstants.USERNAME ));
		proList.add(Projections.property(BaseConstants.EMAIL_ID));
		proList.add(Projections.property(BaseConstants.STAFF_TYPE));
		proList.add(Projections.property("securityQuestion1"));
		proList.add(Projections.property("securityQuestion2"));
		
		staffCri.setProjection(proList);
		
		LogicalExpression orExp = Restrictions.or(usrname,email);
		staffCri.add(orExp);
		
		staffList = staffCri.list();
		
		if(staffList!=null && !staffList.isEmpty()){
			Object[] staffRow ; 			
			for (Iterator it = staffList.iterator(); it.hasNext(); ) {
				staffRow = (Object[]) it.next();
				
				staffIdsAndUsername.put(BaseConstants.USERNAME ,staffRow[0].toString());
				staffIdsAndUsername.put(BaseConstants.EMAILID,staffRow[1].toString());
				staffIdsAndUsername.put(BaseConstants.STAFF_TYPE,staffRow[2].toString());
				
				if(staffRow[3]!=null){
					questionList=new ArrayList<>();
					questionList.add(staffRow[3].toString());
					questionList.add(staffRow[4].toString());
					staffIdsAndUsername.put("QUESTION_LIST",questionList);
				}
			}
		}
		return staffIdsAndUsername;
	}
	
	/**
	 * Get staff by username or emailid and fetch question and answer detail
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map<String,JSONObject> getStaffUserByCredential(String username,String emailId){
		Map<String,JSONObject> staffIdsAndUsername = new HashMap<>();
		
		List staffList ;
		Criteria staffCri = getCurrentSession().createCriteria(Staff.class);
		
		Criterion usrname = Restrictions.eq(BaseConstants.USERNAME, username);
		Criterion email = Restrictions.eq(BaseConstants.EMAIL_ID,emailId);
		
		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.property(BaseConstants.USERNAME));
		proList.add(Projections.property("securityQuestion1"));
		proList.add(Projections.property("securityAnswer1"));
		proList.add(Projections.property("securityQuestion2"));
		proList.add(Projections.property("securityAnswer2"));
		proList.add(Projections.property(BaseConstants.EMAIL_ID));
		
		staffCri.setProjection(proList);
		
		LogicalExpression orExp = Restrictions.or(usrname,email);
		staffCri.add(orExp);
		
		staffList = staffCri.list();
		
		if(staffList!=null && !staffList.isEmpty()){
			Object[] staffRow ; 			
			for (Iterator it = staffList.iterator(); it.hasNext(); ) {
				staffRow = (Object[]) it.next();
				JSONObject staffObj =  new JSONObject();
				if(staffRow[1]!=null){
					staffObj.put(staffRow[1].toString(),staffRow[2].toString());
					staffObj.put(staffRow[3].toString(),staffRow[4].toString());
					staffIdsAndUsername.put((String) staffRow[0], staffObj);
					staffObj.put(BaseConstants.EMAILID, (String) staffRow[5]);
					staffIdsAndUsername.put(BaseConstants.EMAILID,staffObj);
				}else{
					staffIdsAndUsername.put((String) staffRow[0], null);	
				}
			}
		}
		return staffIdsAndUsername;
	}
	
	@Override
	public List<Staff> getPaginatedList(Class<Staff> klass, List<Criterion> conditions, Map<String,String> aliases, int offset,int limit,String sortColumn,String sortOrder){
		List<Staff> resultList;// sonar L416
		Criteria criteria = getCurrentSession().createCriteria(klass);
		
		
		
		if(BaseConstants.DESC.equals(sortOrder) && sortColumn != null){
			if(("name").equalsIgnoreCase(sortColumn)){
				criteria.addOrder(Order.desc(BaseConstants.FIRST_NAME )).addOrder(Order.desc(BaseConstants.LAST_NAME));
			}else{
				criteria.addOrder(Order.desc(sortColumn));
			}
		}
		else if(BaseConstants.ASC.equals(sortOrder) && sortColumn != null){
			if(("name").equalsIgnoreCase(sortColumn)){
				criteria.addOrder(Order.asc(BaseConstants.FIRST_NAME )).addOrder(Order.asc(BaseConstants.LAST_NAME));
			}else{
				criteria.addOrder(Order.asc(sortColumn));
			}
		}

		if(conditions!=null){
			for(Criterion condition : conditions){
				criteria.add(condition);
			}
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS,StateEnum.DELETED));
		
		if(aliases!=null){
			for (Entry<String, String> entry : aliases.entrySet()){
				criteria.createAlias(entry.getKey(), entry.getValue());
			}
		}

		criteria.setFirstResult(offset);//first record is 2
		criteria.setMaxResults(limit);//records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Staff getStaffByUsernameOrEmail(String username) {
		Criteria staffCri = getCurrentSession().createCriteria(Staff.class);
		Criterion user = Restrictions.eq("username", username);
		Criterion email = Restrictions.eq("emailId",username);
		LogicalExpression orExp = Restrictions.or(user, email);
		staffCri.add(orExp);
		List<Staff> staffList = staffCri.list();
		return staffList != null && !staffList.isEmpty() ? staffList.get(0) : null;
	}
	
	/**
	 * Provides All staff id and username from database.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Staff> getAllStaffDetails(){
		Criteria criteria = getCurrentSession().createCriteria(Staff.class);
		criteria.add(Restrictions.or(Restrictions.eq(BaseConstants.ACCOUNTSTATE,StateEnum.ACTIVE),Restrictions.eq(BaseConstants.ACCOUNTSTATE,StateEnum.INACTIVE)));	
		criteria.add(Restrictions.ne("id", 2));
		criteria.add(Restrictions.ne("id", 3));
		//criteria.setProjection(Projections.projectionList().add(Projections.property("id")).add(Projections.property(BaseConstants.USERNAME )));
		return (List<Staff>)criteria.list();
	}
	
	/**
	 * Provide SSO Staff based on Username.
	 */
	@Override
	public Staff getStaffDetailsBySSOType(String username, String type) {
		Criteria criteria = getCurrentSession().createCriteria(Staff.class);
		Criterion ssoStaff = Restrictions.eq(BaseConstants.STAFF_TYPE, BaseConstants.SSO_STAFF);
		Criterion localStaff = Restrictions.eq(BaseConstants.STAFF_TYPE, BaseConstants.LOCAL_STAFF);
		LogicalExpression orExp = Restrictions.or(ssoStaff, localStaff);
		criteria.add(Restrictions.eq(BaseConstants.USERNAME ,username));
		criteria.add(orExp);
		criteria.add(Restrictions.ne(BaseConstants.ACCOUNTSTATE,StateEnum.DELETED));
		return (Staff) criteria.uniqueResult();
	}
}