package com.elitecore.sm.iam.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.iam.model.AccessGroup;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;

public class LDAPUserDetailsContextMapper implements UserDetailsContextMapper {
	
	@Autowired
	private StaffService staffService;
			
	@Autowired
	@Qualifier(value="eliteUtilsQualifier")
	protected EliteUtils eliteUtils ;
	
	private Logger loggerLogin = Logger.getLogger(this.getClass().getName());
	
	@SuppressWarnings("serial")
	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String userName,
			Collection<? extends GrantedAuthority> authorities)throws AuthenticationException {
		loggerLogin.info("Inside mapUserFromContext method for validating login information with LDAP.");
		List<SimpleGrantedAuthority> new_authorities = new ArrayList<SimpleGrantedAuthority>();	
		Staff staff = new Staff();
		staff.setUsername(userName);
		String groupRoleAttributeName = null;
		boolean isAccessGrpAvailable = false;
		try {			 
			String systemGrpRoleAttr=(String)MapCache.getConfigValueAsString(SystemParametersConstant.GROUP_ROLE_ATTRIBUTE_NAME,"DEFAULT");			
			if (systemGrpRoleAttr!=null && systemGrpRoleAttr.equalsIgnoreCase("memberOf")) {						
				Object[] objArr = ctx.getObjectAttributes(systemGrpRoleAttr);
				for (Object obj : objArr) {				
					groupRoleAttributeName = getGroupRoleAttributeName((String) obj);
					loggerLogin.info("groupRoleAttributeName : "+groupRoleAttributeName);
					AccessGroup accessGroup = staffService.getAccessGroupForLDAPStaff(groupRoleAttributeName);				
					if (accessGroup != null) {
						loggerLogin.info("AccessGroup found in database named : "+groupRoleAttributeName);
						isAccessGrpAvailable = true;
						break;
					}
				}
				if (!isAccessGrpAvailable) {
					groupRoleAttributeName = (String)MapCache.getConfigValueAsString(SystemParametersConstant.DEFAULT_ACCESS_GROUP,"");
					loggerLogin.info("Access group coming from LDAP is not found in Database, Assigning DEFAULT access group : "+groupRoleAttributeName);
				}
			} else {
				groupRoleAttributeName = ctx.getStringAttribute((String)MapCache.getConfigValueAsString(SystemParametersConstant.GROUP_ROLE_ATTRIBUTE_NAME,"DEFAULT"));
			}
			ResponseObject responseObject = staffService.createLdapStaff(staff,groupRoleAttributeName);
			if (responseObject.isSuccess()) {
				new_authorities = getGrantedAuthorities(staff, new_authorities);
				loggerLogin.info("LDAP user created successfully with assigned authorities.");
				eliteUtils.addLoginAuditDetails(staff.getId(), staff.getUsername(),AuditConstants.UPDATE_STAFF_DETAILS, BaseConstants.UPDATE_ACTION );
				eliteUtils.addLoginAuditDetails(staff.getId(), staff.getUsername(),AuditConstants.LOGIN_ACTION,null);
			} else {
				loggerLogin.info("Staff with username : " + userName + " is already created in database.");
				loggerLogin.info("Login denied!!!");
				eliteUtils.addLoginAuditDetails(0, userName,AuditConstants.LOGIN_AS_LDAP_DENIED,null);
				throw new AuthenticationException(userName + " login denied. Username already created in Server Manager.") {};
			}			
			new_authorities.add(new SimpleGrantedAuthority("HOME"));
		} catch (Exception e) {
			eliteUtils.addLoginAuditDetails(0, userName,AuditConstants.ERROR_LOGIN_AS_LDAP,null);
			loggerLogin.error(e);
		}		 
		return new User(userName, "NA", true, true, true, true, new_authorities);
	}

	private List<SimpleGrantedAuthority> getGrantedAuthorities(Staff staff, List<SimpleGrantedAuthority> new_authorities){
		loggerLogin.info("Inside getGrantefAuthorities method for for fetching user's authorities from database.");
		if (null != staff && new_authorities != null) {
			staff = staffService.getFullStaffDetails(staff.getUsername());		
			if (null != staff.getAccessGroupList()) {
				for (AccessGroup accessGroup : staff.getAccessGroupList()) {
					if (null != accessGroup) {
						for (Action action : accessGroup.getActions()) {
							new_authorities.add(new SimpleGrantedAuthority(action.getAlias()));
							new_authorities.add(new SimpleGrantedAuthority(action.getParentBusinessSubModule().getAlias()));
							new_authorities.add(new SimpleGrantedAuthority(action.getParentBusinessSubModule().getParentBusinessModule().getAlias()));
						}
					}
				}
			}
		}
		return new_authorities;
	}

	@Override
	public void mapUserToContext(UserDetails arg0, DirContextAdapter arg1) {
		// TODO Auto-generated method stub
	}
	
	private String getGroupRoleAttributeName(String attributeStr) {
		String group = null;
		StringTokenizer st = new StringTokenizer(attributeStr,",");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if(token.contains("CN=")) {
				group = token.split("=")[1];
				loggerLogin.info(group);
			}
		}
		return group;
	}
	
}