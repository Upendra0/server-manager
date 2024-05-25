/**
 * 
 */
package com.elitecore.sm.iam.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.StateEnum;

/**
 * @author Sunil Gulabani
 * Mar 31, 2015
 */

@Component(value="staffObject")
@Scope(value="prototype")
@Entity
@Table(name="TBLMSTAFF")
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="staffCache")
public class Staff extends BaseModel implements Serializable{
	
	private static final long serialVersionUID = 4195998773961436052L;

	private int id;
	
	private String username;
	
	private String password;
	
	private transient String confirmPassword;

	private String staffCode;
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private Date birthDate;
	
	private String emailId;
	
	private String emailId2;
	
	private String mobileNo;
	
	private String landlineNo;
	
	private String address;
	
	private String address2;
	
	private String city;
	
	private String pincode;
	
	private String state;
	
	private String country;
	
	private String loginIPRestriction;
	
	private StateEnum accountState = StateEnum.ACTIVE;

	private boolean accountLocked = false;

	private List<AccessGroup> accessGroupList;
	
	private int wrongAttempts = 0;
	
	private Date lastWrongAttemptsDate;
	
	private Date passwordExpiryDate;
	
	private Date lastLoginTime;
	
	private List<PasswordHistory> passwordHistoryList;
	
    private String profilePic;
    
    private String securityQuestion1;
    
    private String securityAnswer1;
    
    private String securityQuestion2;
    
    private String securityAnswer2;
    
    private boolean firstTimeLogin=true;
    
    private String stafftype = BaseConstants.LOCAL_STAFF;

	public Staff(){
		//default no-arg constructor
	}
	
	public Staff(int id){
		this.id = id;
	}
	
	public void copyStaffProperties(Staff staff){
		if(staff!=null){
			createdByStaffId = staff.getId();
			lastUpdatedByStaffId = staff.getId();
		}

		createdDate = new Date();
		lastUpdatedDate = new Date();
		wrongAttempts = 0;
		accountState = StateEnum.ACTIVE;
			
		loginIPRestriction = "";
	}
	
	public Staff(Staff staff){
		if(staff!=null){
			createdByStaffId = staff.getId();
			lastUpdatedByStaffId = staff.getId();
		}

		createdDate = new Date();
		lastUpdatedDate = new Date();
		wrongAttempts = 0;
		accountState = StateEnum.ACTIVE;
			
		loginIPRestriction = "*.*.*.*";
	}
	
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							pkColumnValue="Staff",allocationSize=1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name="username", nullable = false, length = 255, unique=true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name="STF_PASSWORD", nullable = true, length = 255)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name="staffcode", nullable = true, length = 255)
	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	@Column(name="emailid", nullable = false, unique=true)
	public String getEmailId() {
		if(emailId!=null)
			emailId = emailId.toLowerCase();
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@Column(name="mobileno", nullable = false, length = 20)
	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	@Column(name="address", nullable = false, length = 500)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name="city", nullable = false, length = 255)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name="pincode", nullable = false, length = 255)
	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	@Column(name="state", nullable = true, length = 255)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name="country", nullable = true, length = 255)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="accountstate")
	public StateEnum getAccountState() {
		return accountState;
	}

	public void setAccountState(StateEnum accountState) {
		this.accountState = accountState;
	}

	@Type(type = "yes_no")
	@Column(name="isaccountlocked")
	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	@ManyToMany(targetEntity=AccessGroup.class)
	@JoinTable(name="TBLTSTAFFACCESSGROUPREL",joinColumns=@JoinColumn (name="staffid",referencedColumnName="id"),inverseJoinColumns=@JoinColumn(name="accessgroupid",referencedColumnName="id"))
	@DiffIgnore
	public List<AccessGroup> getAccessGroupList() {
		return accessGroupList;
	}

	public void setAccessGroupList(List<AccessGroup> accessGroupList) {
		this.accessGroupList = accessGroupList;
	}

	@Column(name="wrongattempts")
	@DiffIgnore
	public int getWrongAttempts() {
		return wrongAttempts;
	}

	public void setWrongAttempts(int wrongAttempts) {
		this.wrongAttempts = wrongAttempts;
	}

	@Column(name="lastwrongattemptsdate")
	@Type(type="timestamp")
	@DiffIgnore
	public Date getLastWrongAttemptsDate() {
		return lastWrongAttemptsDate;
	}

	public void setLastWrongAttemptsDate(Date lastWrongAttemptsDate) {
		this.lastWrongAttemptsDate = lastWrongAttemptsDate;
	}

	@Column(name="passwordexpirydate")
	@Type(type="timestamp")
	@DiffIgnore
	public Date getPasswordExpiryDate() {
		return passwordExpiryDate;
	}

	public void setPasswordExpiryDate(Date passwordExpiryDate) {
		this.passwordExpiryDate = passwordExpiryDate;
	}

	@Column(name="lastlogintime")
	@Type(type="timestamp")
	@DiffIgnore
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "staff",cascade=CascadeType.ALL)
	@DiffIgnore
	public List<PasswordHistory> getPasswordHistoryList() {
		return passwordHistoryList;
	}

	public void setPasswordHistoryList(List<PasswordHistory> passwordHistoryList) {
		this.passwordHistoryList = passwordHistoryList;
	}

	@Column(name="firstname", nullable = false, length = 255)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name="lastname", nullable = true, length = 255)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Transient
	public String getName() {
		if(
				!StringUtils.isEmpty(firstName)
				&& !StringUtils.isEmpty(lastName)
				){
			return firstName + " " + lastName;
		}else if(!StringUtils.isEmpty(firstName)){
			return firstName;
		}else if(!StringUtils.isEmpty(lastName)){
			return lastName;
		}else{
			return username;
		}
	}

	@Column(name="loginiprestriction", nullable = true, length = 500)
	public String getLoginIPRestriction() {
		return loginIPRestriction;
	}

	public void setLoginIPRestriction(String loginIPRestriction) {
		this.loginIPRestriction = loginIPRestriction;
	}

	@Transient
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Column(name="middlename", nullable = true, length = 255)
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Column(name="birthdate")
	@Type(type = "timestamp")
	//@DiffIgnore
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	@Column(name="address2", nullable = true, length = 500)
	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	@Column(name="emailid2", nullable = true)
	public String getEmailId2() {
		return emailId2;
	}

	public void setEmailId2(String emailId2) {
		this.emailId2 = emailId2;
	}

	@Column(name="landlineno", nullable = true, length = 20)
	public String getLandlineNo() {
		return landlineNo;
	}

	public void setLandlineNo(String landlineNo) {
		this.landlineNo = landlineNo;
	}

	@Column(name="profilepic", nullable = true)
	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	@Column(name="securityquestion1", nullable = true)
	public String getSecurityQuestion1() {
		return securityQuestion1;
	}

	public void setSecurityQuestion1(String securityQuestion1) {
		this.securityQuestion1 = securityQuestion1;
	}

	@Column(name="securityanswer1", nullable = true)
	public String getSecurityAnswer1() {
		return securityAnswer1;
	}

	public void setSecurityAnswer1(String securityAnswer1) {
		this.securityAnswer1 = securityAnswer1;
	}

	@Column(name="securityquestion2", nullable = true)
	public String getSecurityQuestion2() {
		return securityQuestion2;
	}

	public void setSecurityQuestion2(String securityQuestion2) {
		this.securityQuestion2 = securityQuestion2;
	}

	@Column(name="securityanswer2", nullable = true)
	public String getSecurityAnswer2() {
		return securityAnswer2;
	}

	public void setSecurityAnswer2(String securityAnswer2) {
		this.securityAnswer2 = securityAnswer2;
	}
	
	@Type(type = "yes_no")
	@Column(name="ISFIRSTTIMELOGIN")
    public boolean isFirstTimeLogin() {
		return firstTimeLogin;
	}

	public void setFirstTimeLogin(boolean firstTimeLogin) {
		this.firstTimeLogin = firstTimeLogin;
	}

	@Column(name="stafftype")
	public String getStafftype() {
		return stafftype;
	}

	public void setStafftype(String stafftype) {
		this.stafftype = stafftype;
	}
	
}