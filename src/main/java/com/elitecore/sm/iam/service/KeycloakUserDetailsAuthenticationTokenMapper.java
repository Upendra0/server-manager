package com.elitecore.sm.iam.service;


import org.keycloak.adapters.OidcKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;

public class KeycloakUserDetailsAuthenticationTokenMapper extends KeycloakAuthenticationToken
{

    private UserDetails userDetails;

    public KeycloakUserDetailsAuthenticationTokenMapper(UserDetails userDetails, OidcKeycloakAccount account,
            Collection<? extends GrantedAuthority> authorities) {
    	
    		super(account,true, authorities);
    
        Assert.notNull(userDetails, "UserDetails required");
        this.userDetails = userDetails;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }
}