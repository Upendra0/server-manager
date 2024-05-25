package com.elitecore.sm.iam.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

public final class TokenInfo {

	private final String token;
	private final UserDetails userDetails;
	private Date expiredDate;
	private Date createdDate = new Date();

	public TokenInfo(String token, UserDetails userDetails) {
		this.token = token;
		this.userDetails = userDetails;
	}

	public String getToken() {
		return token;
	}

	public Date getExpiredDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.createdDate);
		//expired time of token is after one hour of created time of the token
		cal.add(Calendar.HOUR, 1);
		this.expiredDate = cal.getTime();
		return this.expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}
	
	@Override
	public String toString() {
		return "TokenInfo [token=" + token + ", userDetails=" + userDetails
				+ ", expiredDate=" + expiredDate + ", createdDate=" + createdDate + "]";
	}

}
