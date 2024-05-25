package com.elitecore.sm.iam.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


import javax.ws.rs.core.Context;
import org.apache.log4j.Logger;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.KeycloakRole;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.iam.model.AccessGroup;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;



public class KeycloakAuthenticationProviderImpl extends KeycloakAuthenticationProvider{
	@Autowired
	private StaffService staffService;
	
	@Context
	SecurityContext sc;
	
    private GrantedAuthoritiesMapper grantedAuthoritiesMapper;
    
    private Logger loggerLogin = Logger.getLogger(this.getClass().getName());
    
    @Autowired
	@Qualifier(value="eliteUtilsQualifier")
	protected EliteUtils eliteUtils ;
    
    public void setGrantedAuthoritiesMapper(GrantedAuthoritiesMapper grantedAuthoritiesMapper) {
    	this.grantedAuthoritiesMapper = grantedAuthoritiesMapper;     
    }
    
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {  	
    	KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;

    	UserDetails userDetails;
        List<String> groupNames = new ArrayList<String>();
        Staff staff = new Staff();
        KeycloakPrincipal<KeycloakSecurityContext> kp =(KeycloakPrincipal<KeycloakSecurityContext>) token.getPrincipal();
        String userName= kp.getKeycloakSecurityContext().getIdToken().getPreferredUsername();
		staff.setUsername(userName);
		if(kp.getKeycloakSecurityContext().getIdToken().getEmail() != null){
			staff.setEmailId(kp.getKeycloakSecurityContext().getIdToken().getEmail());
		}
		if(kp.getKeycloakSecurityContext().getIdToken().getGivenName()!=null)
			staff.setFirstName(kp.getKeycloakSecurityContext().getIdToken().getGivenName());
		
		List<GrantedAuthority> new_authorities = new ArrayList<GrantedAuthority>();	
		List<GrantedAuthority> rolesfromKeycloak= new ArrayList<GrantedAuthority>();	
		try {		
			 //get the roles(access group) names from keycloak 	
			 if(kp.getKeycloakSecurityContext()!=null && kp.getKeycloakSecurityContext().getToken().getRealmAccess()!=null){ 	
				 Set<String> roles = kp.getKeycloakSecurityContext().getToken().getRealmAccess().getRoles();
	        	 for (String role : roles) {      		 
	                 groupNames.add(role);
	                 rolesfromKeycloak.add(new KeycloakRole(role));
	             } 
			 }
			
			if(!staff.getUsername().equals("admin") && !staff.getUsername().equals("profileadmin") && !staff.getUsername().equals("moduleadmin"))
			{	
				//create or update sso staff  
				ResponseObject responseObject = staffService.createSSOStaff(staff,groupNames);
				if (responseObject.isSuccess()) {		
					 if(responseObject.getResponseCode().equals(ResponseCode.STAFF_INSERT_SUCCESS)) {
						 eliteUtils.addLoginAuditDetails(staff.getId(), staff.getUsername(),AuditConstants.CREATE_STAFF, BaseConstants.CREATE_ACTION );
						 loggerLogin.info("Keycloack user created successfully with assigned authorities.");
					 }
					 if(responseObject.getResponseCode().equals(ResponseCode.STAFF_UPDATE_SUCCESS)) {
						 eliteUtils.addLoginAuditDetails(staff.getId(), staff.getUsername(),AuditConstants.UPDATE_STAFF_DETAILS, BaseConstants.UPDATE_ACTION );
						 loggerLogin.info("Keycloack user access group updated successfully with assigned authorities.");
					 }	
					 eliteUtils.addLoginAuditDetails(staff.getId(), staff.getUsername(),AuditConstants.LOGIN_ACTION,null);	
				} else {
					loggerLogin.info("Staff with username : " + userName + " is already created in database.");
					loggerLogin.info("Login denied!!!");
					eliteUtils.addLoginAuditDetails(0, userName,AuditConstants.LOGIN_AS_SSO_DENIED,null);
				}
			}
			new_authorities = getGrantedAuthorities(staff, new_authorities); 
			new_authorities.add(new KeycloakRole("HOME"));
			new_authorities.add(new KeycloakRole("ROLE_ADMIN"));
		} catch (Exception e) {
			loggerLogin.error(e);
		}	
		 userDetails = new User(userName, "NA", true, true, true, true, new_authorities);
		 return new KeycloakUserDetailsAuthenticationTokenMapper(userDetails, token.getAccount(), mapAuthorities(new_authorities));
    }

    private Collection<? extends GrantedAuthority> mapAuthorities(
            Collection<? extends GrantedAuthority> authorities) {
        return grantedAuthoritiesMapper != null
            ? grantedAuthoritiesMapper.mapAuthorities(authorities)
            : authorities;
    }

    @Override
    public boolean supports(Class<?> aClass) {
    	return Boolean.parseBoolean(MapCache.getConfigValueAsObject(SystemParametersConstant.SSO_ENABLE).toString());
    }
    
    private List<GrantedAuthority> getGrantedAuthorities(Staff staff, List<GrantedAuthority> new_authorities){
		loggerLogin.info("Inside getGrantefAuthorities method for for fetching user's authorities from database.");
		if (null != staff && new_authorities != null) {
			staff = staffService.getFullStaffDetails(staff.getUsername());		
			if (null != staff.getAccessGroupList()) {
				for (AccessGroup accessGroup : staff.getAccessGroupList()) {
					if (null != accessGroup) {
						for (Action action : accessGroup.getActions()) {
							new_authorities.add(new KeycloakRole(action.getAlias()));
							new_authorities.add(new KeycloakRole(action.getParentBusinessSubModule().getAlias()));
							new_authorities.add(new KeycloakRole(action.getParentBusinessSubModule().getParentBusinessModule().getAlias()));
						}
					}
				}
			}
		}
		return new_authorities;
	}
   
}
