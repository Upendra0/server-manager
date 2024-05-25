package com.elitecore.sm.iam.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.iam.exceptions.LoginIPUnauthorizedException;
import com.elitecore.sm.iam.model.AccessGroup;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.systemaudit.service.SystemAuditService;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;

public class SpringLoginServiceImpl implements UserDetailsService {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	@Qualifier(value = "eliteUtilsQualifier")
	protected EliteUtils eliteUtils;

	@Autowired
	private StaffService staffService;

	@Autowired
	private SystemAuditService systemAuditService;

	/**
	 * Loads the user by username during login request.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		Staff user = null;

		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		logger.info(" loadUserByUsername username: " + username);

		if (!"".equals(username)) {
			user = staffService.getFullStaffDetails(username);
			
			// we allow login by either username or emailid.
			if (user == null) {
				// not found by username so find by email id.
				user = staffService.getFullStaffDetailsByEmailId(username);
			}

			if (null != user) {

				boolean isAccLocked=checkAndReleaseAccountLock(user);
				isStaffMachineIPAuthorised(user);
				boolean enabled=user.getAccountState() == StateEnum.INACTIVE ? false : true;
				boolean accNONExpired = user.getAccountState() == StateEnum.DELETED ? false : true ;
				logger.debug("isAccLocked:" + isAccLocked);
				logger.debug("enabled:" + enabled);
				logger.debug("accNONExpired:" + accNONExpired);
				
				if (!BaseConstants.MODULE_ADMIN_USERNAME.equalsIgnoreCase(user.getUsername())
						&& !BaseConstants.PROFILE_ADMIN_USERNAME.equalsIgnoreCase(user.getUsername())
						&& (user.isFirstTimeLogin() || (user.getPasswordExpiryDate().compareTo(new Date()) <= 0))) {
					logger.info("Staff either login for 1st time or password have expired.");
					// For Home Page
					authorities.add(new SimpleGrantedAuthority("HOME"));
				
					if (user.getPasswordExpiryDate() != null && user.getPasswordExpiryDate().compareTo(new Date()) <= 0) {
						authorities.add(new SimpleGrantedAuthority("RESET_PASSWORD_MENU_VIEW"));
						authorities.add(new SimpleGrantedAuthority("RESET_PASSWORD")); 
						// For Reset Password screen on Password expiry date completion.
					} else {
						authorities.add(new SimpleGrantedAuthority("CHANGE_PASSWORD_MENU_VIEW"));
						authorities.add(new SimpleGrantedAuthority("CHANGE_PASSWORD"));
					}
					// spring user constructor uses accountnonLocked so need to add not operator to our account lock logic
					return new User(user.getUsername(), user.getPassword(),enabled, accNONExpired
							, true, !isAccLocked, authorities);
				} else {
					// load all authorities of staff.
					logger.info("Staff access group list: " + user.getAccessGroupList());

					if (user.getAccessGroupList() != null) {
						for (AccessGroup accessGroup : user.getAccessGroupList()) {
							if (null != accessGroup) {
								for (Action action : accessGroup.getActions()) {
									authorities.add(new SimpleGrantedAuthority(action.getAlias()));
									authorities.add(new SimpleGrantedAuthority(action.getParentBusinessSubModule().getAlias()));
									authorities.add(new SimpleGrantedAuthority(action.getParentBusinessSubModule().getParentBusinessModule()
											.getAlias()));
								}
							}
						}
					}
					authorities.add(new SimpleGrantedAuthority("HOME")); 
					// For Home Page

					logger.info("authorities: " + authorities);
					boolean credsExpired=eliteUtils.isCredentialsExpired(
							user.getPasswordExpiryDate(),user.getUsername());
					logger.debug("Value of credsExpired is :" + credsExpired);
					//spring user constructor uses accountnonLocked so need to add not operator to our account lock logic
					return new User(user.getUsername(), user.getPassword(), enabled,
							accNONExpired, credsExpired , ! isAccLocked, authorities);
				}
			} else {
				logger.info("Staff Object is null...");
				return new User(username, "NA", true, true, true, true, authorities);
			}
		} else {
			logger.info("username is blank...");
			return new User(username, "NA", true, true, true, true, authorities);
		}
	}

	/**
	 * Checks for the Staff current IP Address and Staff assigned login ip
	 * address.
	 * 
	 * @param staff
	 * @throws LoginIPUnauthorizedException
	 */
	private void isStaffMachineIPAuthorised(Staff staff) throws LoginIPUnauthorizedException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String currentRequestIPAddress = eliteUtils.getFramedIpAddress(request);

		if ( !StringUtils.isEmpty(staff.getLoginIPRestriction())) {
			boolean isIPAuthorized = false;
			String loginIPRestrictions[] = staff.getLoginIPRestriction().split(",");

			for (String ip : loginIPRestrictions) {
				if (ip.equals(currentRequestIPAddress)) {
					isIPAuthorized = true;
				}
			}

			if (!isIPAuthorized) {
				throw new LoginIPUnauthorizedException();
			}
		}
	}

	

	

	/**
	 * Checks and releases the account lock if release lock time is over.
	 * 
	 * @param user
	 */
	private boolean checkAndReleaseAccountLock(Staff staff) {
		boolean isAccLocked = false;
		if (staff.isAccountLocked() )
		{
			logger.debug("Staff Account is locked  by admin.");
			isAccLocked = true;
		}
		
		if (staff.isAccountLocked() && staff.getLastWrongAttemptsDate() != null && staff.getWrongAttempts() >= MapCache.getConfigValueAsInteger(
						SystemParametersConstant.MAX_WRONG_PASSWORD_ATTEMPTS,3)) {
			// staff has exceeded its wrong login attempts so effectively he's locked
			isAccLocked = true;
			Calendar cal = Calendar.getInstance();
			cal.setTime(staff.getLastWrongAttemptsDate());
			cal.add(Calendar.MINUTE, MapCache.getConfigValueAsInteger(SystemParametersConstant.RELEASE_LOCK_ON_WRONG_ATTEMPTS_IN_MINUTES, 5));
			// if auto reset time has passed since his last wrong login he can attempt again
			logger.debug("user exceeded wrong login attempts, last wrong login attempt + grace period is " + cal.getTime());
			if (cal.getTime().compareTo(new Date()) < 0) {
				isAccLocked=false;
				logger.info("auto rest time is complete so allowing staff to attempt login.");
			}
		}
		return isAccLocked;
	}

	public StaffService getStaffService() {
		return staffService;
	}

	public void setStaffService(StaffService userService) {
		this.staffService = userService;
	}
}