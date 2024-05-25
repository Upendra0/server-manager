package com.elitecore.sm.iam.service;

import java.util.Map;

public interface RSAAuthenticationService {
	
	boolean verify() throws Exception;//NOSONAR
	public Map<String, String> initialize(String userName, String token) throws Exception;//NOSONAR
	boolean status();
}
