package com.elitecore.sm.iam.service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Base64;

/**
 * Implements simple token manager, that keeps a single token for each user. If user logs in again,
 * older token is invalidated.
 */
public class TokenManagerSingle implements TokenManager {
	
	private static Logger logger = Logger.getLogger(TokenManagerSingle.class);

	private static Map<String, UserDetails> validUsers = new ConcurrentHashMap<>();

	/**
	 * This maps system users to tokens because equals/hashCode is delegated to User entity.
	 * This can store either one token or list of them for each user, depending on what you want to do.
	 * Here we store single token, which means, that any older tokens are invalidated.
	 */
	private static Map<UserDetails, TokenInfo> tokens = new ConcurrentHashMap<>();
	
	private static Map<String, TokenInfo> tokenMap = new ConcurrentHashMap<>();

	@Override
	public TokenInfo createNewToken(UserDetails userDetails) {
		String token;
		do {
			token = generateToken();
		} while (validUsers.containsKey(token));

		TokenInfo tokenInfo = new TokenInfo(token, userDetails);
		removeUserDetails(userDetails);
		UserDetails previous = validUsers.put(token, userDetails);
		if (previous != null) {
			logger.info(" *** SERIOUS PROBLEM HERE - we generated the same token (randomly?)!");
			return null;
		}
		tokens.put(userDetails, tokenInfo);
		tokenMap.put(token, tokenInfo);
		return tokenInfo;
	}

	private String generateToken() {
		byte[] tokenBytes = new byte[32];
		new SecureRandom().nextBytes(tokenBytes);
		return new String(Base64.encode(tokenBytes), StandardCharsets.UTF_8);
	}

	@Override
	public void removeUserDetails(UserDetails userDetails) {
		TokenInfo token = tokens.remove(userDetails);
		if (token != null) {
			validUsers.remove(token.getToken());
		}
	}

	@Override
	public UserDetails removeToken(String token) {
		tokenMap.remove(token);
		UserDetails userDetails = validUsers.remove(token);
		if (userDetails != null) {
			tokens.remove(userDetails);
		}
		return userDetails;
	}

	@Override
	public UserDetails getUserDetails(String token) {
		return validUsers.get(token);
	}

	@Override
	public Collection<TokenInfo> getUserTokens(UserDetails userDetails) {
		return Arrays.asList(tokens.get(userDetails));
	}

	@Override
	public Map<String, UserDetails> getValidUsers() {
		return Collections.unmodifiableMap(validUsers);
	}

	@Override
	public TokenInfo getTokenInfo(String token) {
		return tokenMap.get(token);
	}
}
