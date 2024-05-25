package com.elitecore.sm.iam.dao;

import java.util.Collection;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

@Component
public class CustomLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

	@Override
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData,String username) {
		userData.getAttributes();
		//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//authentication.getPrincipal();
		// TODO Auto-generated method stub
		return null;
	}
}