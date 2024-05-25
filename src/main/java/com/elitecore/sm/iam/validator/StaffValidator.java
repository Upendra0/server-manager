/**
 * 
 */
package com.elitecore.sm.iam.validator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.systemparam.service.SystemParameterService;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.Regex;

/**
 * @author Sunil Gulabani Apr 21, 2015
 * 
 *         Reference:
 *         http://www.beingjavaguys.com/2013/05/spring-form-validation.html
 */
@Component
public class StaffValidator extends BaseValidator {

	private Staff staff;

	@Autowired
	private SystemParameterService systemParameterService;

	@Override
	public boolean supports(@SuppressWarnings("rawtypes") Class clazz) {
		return Staff.class.isAssignableFrom(clazz);
	}

	public void validateAccessGroup(Object target, Errors errors) {
		this.errors = errors;
		staff = (Staff) target;

		logger.debug("staff: " + staff);

		if (staff.getAccessGroupList() == null || staff.getAccessGroupList().isEmpty()) {
			errors.rejectValue("accessGroupList", "error.staff.access.group.list.is.blank", getMessage("staff.access.group.list.is.blank"));
		}
	}

	@Override
	public void validate(Object target, Errors errors) {
		this.errors = errors;
		staff = (Staff) target;

		logger.debug("staff: " + staff);

		if (staff.getId() != 0) {
			logger.debug("Validating for Staff Update");
		} else {
			logger.debug("Validating for Staff Save");
			validatePasswordAndConfirmPassword(staff.getPassword(), staff.getConfirmPassword(), errors);
		}

		validateUsername(staff.getUsername());

		validateEmailAddress(staff.getEmailId());

		validateEmail2Address(staff.getEmailId2());

		validateStaffCode(staff.getStaffCode());

		validateFirstName(staff.getFirstName());

		validateLastName(staff.getLastName());
		
		validateDOB(staff.getBirthDate());

		validateMiddleName(staff.getMiddleName());

		validateAddress(staff.getAddress());

		validateAddress2(staff.getAddress2());

		validateCity(staff.getCity());

		validateState(staff.getState());

		validateCountry(staff.getCountry());

		validatePincode(staff.getPincode());

		validateMobileNo(staff.getMobileNo());

		validateLandline(staff.getLandlineNo());

		validateLoginIPRestriction(staff.getLoginIPRestriction());

	}

	private void validateLoginIPRestriction(String value) {

		if (!("").equals(value) && !("*").equals(value)) {
			InetAddressValidator ipValidator = new InetAddressValidator();
			String[] ipAddresses = value.split(",");
			String errorMsgValue;
			for (String ipAddress : ipAddresses) {
				ipAddress = ipAddress.trim();
				if (!(ipValidator.isValidInet4Address(ipAddress) || ipValidator.isValidInet6Address(ipAddress))) {
					errorMsgValue = getMessage("ip.address.invalid");
					errorMsgValue = errorMsgValue.replace("[IP_ADDRESS]", ipAddress);
					errors.rejectValue("loginIPRestriction", "error.ip.address.invalid", errorMsgValue);
				}
			}
		}
	}

	private void validateMobileNo(String value) {
		isValidate(SystemParametersConstant.STAFF_MOBILENO, value, "mobileNo", null, null, false);

		isValidate(SystemParametersConstant.STAFF_MOBILENO, value, "mobileNo", null, null, false);
	}

	private void validateLandline(String value) {
		isValidate(SystemParametersConstant.STAFF_LANDLINENO, value, "landlineNo", null, null, false);
	}

	private void validatePincode(String value) {
		isValidate(SystemParametersConstant.STAFF_PINCODE, value, "pincode", null, null, false);
	}

	private void validateCity(String value) {
		isValidate(SystemParametersConstant.STAFF_CITY, value, "city", null, null, false);
	}

	private void validateState(String value) {
		isValidate(SystemParametersConstant.STAFF_STATE, value, "state", null, null, false);
	}

	private void validateCountry(String value) {
		isValidate(SystemParametersConstant.STAFF_COUNTRY, value, "country", null, null, false);
	}

	private void validateAddress(String value) {
		isValidate(SystemParametersConstant.STAFF_ADDRESS, value, "address", null, null, false);
	}

	private void validateAddress2(String value) {
		if(!StringUtils.isEmpty(value)){
		isValidate(SystemParametersConstant.STAFF_ADDRESS2, value, "address2", null, null, false);
		}
		
	}

	private void validateFirstName(String firstName) {
		isValidate(SystemParametersConstant.STAFF_FIRSTNAME, firstName, "firstName", null, null, false);
	}

	private void validateLastName(String value) {
		isValidate(SystemParametersConstant.STAFF_LASTNAME, value, "lastName", null, null, false);
	}
	
	private void validateDOB(Date value) {
		boolean isValid = true;
		if(value == null){
			isValid = false;
		}else{
			if(value.compareTo(new Date()) >= 0){
				isValid = false;
			}
		}
		
		if(!isValid){
			String errorMsgValue = getMessage("Staff.birthDate.invalid");
			errors.rejectValue("birthDate", "error.Staff.birthDate.invalid", errorMsgValue);
		}
	}

	private void validateMiddleName(String value) {
		isValidate(SystemParametersConstant.STAFF_MIDDLENAME, value, "middleName", null, null, false);
	}

	private void validateStaffCode(String staffCode) {
		isValidate(SystemParametersConstant.STAFF_STAFFCODE, staffCode, "staffCode", null, null, false);
	}

	private void validateUsername(String username) {
		isValidate(SystemParametersConstant.STAFF_USERNAME, username, "username", null, null, false);
	}

	private void validateEmailAddress(String emailAddress) {
		isEmailValidate(emailAddress, "staff.email.invalid", "emailId");
	}

	private void validateEmail2Address(String emailAddress2) {
		if (!StringUtils.isEmpty(emailAddress2))
			isEmailValidate(emailAddress2, "staff.email.invalid", "emailId2");
	}

	private boolean validatePassword(String password) {
		String[] passwordPolicy = systemParameterService.parsePasswordType(MapCache.getConfigValueAsString(SystemParametersConstant.STAFF_PASSWORD,
				""));
		logger.debug("regex :: passwordPolicy in validatePassword::" + passwordPolicy[0]);
		logger.debug("PolicyName :: passwordPolicy::" + passwordPolicy[1]);
		return isValidate(SystemParametersConstant.STAFF_PASSWORD, password, "password", new Object[] { passwordPolicy[1] }, null, null, false);

	}

	private boolean validateConfirmPassword(String confirmPassword) {

		String[] passwordPolicy = systemParameterService.parsePasswordType(MapCache.getConfigValueAsString(SystemParametersConstant.STAFF_PASSWORD,
				""));
		logger.debug("regex-passwordPolicy::" + passwordPolicy[0]);
		logger.debug("Policy Name-passwordPolicy::" + passwordPolicy[1]);

		return isValidate(SystemParametersConstant.STAFF_PASSWORD, confirmPassword, "confirmPassword", new Object[] { passwordPolicy[1] }, null,
				null, false);
	}

	public Map<String, String> validateForForgotPassword(String username, String emailId, String forgotPWdOption) {
		Map<String, String> validationMap = new HashMap<>();

		if (forgotPWdOption != null && ("username").equals(forgotPWdOption)) {
			if (StringUtils.isEmpty(username)) {
				validationMap.put("USERNAME_ERROR", getMessage("username.is.missing"));
			}
		} else if (forgotPWdOption != null && "email".equals(forgotPWdOption) && StringUtils.isEmpty(emailId)) {
			validationMap.put("EMAIL_ID_ERROR", getMessage("emailid.is.missing"));
		}

		return validationMap;
	}

	public Map<String, String> validateQuestionForForgotPwd(String question1, String answer1) {

		Map<String, String> validationMap = new HashMap<>();

		if (StringUtils.isEmpty(question1))
			validationMap.put("QUESTION1_ERROR", getMessage("question1.is.missing"));

		if (StringUtils.isEmpty(answer1))
			validationMap.put("ANSWER1_ERROR", getMessage("forgotPassword.answer1.is.missing"));

		return validationMap;

	}

	public Map<String, String> validateForResetPassword(String newPassword, String confirmNewPassword) {
		Map<String, String> validationMap = new HashMap<>();
		String[] passwordPolicy = systemParameterService.parsePasswordType(MapCache.getConfigValueAsString(SystemParametersConstant.STAFF_PASSWORD,
				""));
		logger.debug("rege-- :passwordPolicy::" + passwordPolicy[0]);
		logger.debug("PolicyName -:passwordPolicy::" + passwordPolicy[1]);
		if (match(passwordPolicy[0], newPassword)) {
			validationMap.put(BaseConstants.PASS_ERROR, getMessage(BaseConstants.PASS_INVALID_MSG ).replace(BaseConstants.REGEX, passwordPolicy[1]));
		}

		if (match(passwordPolicy[0], confirmNewPassword)) {
			validationMap.put(BaseConstants.CONFORM_PASS_ERROR,
					getMessage(BaseConstants.CHANGE_PASS_ERROR ).replace(BaseConstants.REGEX, passwordPolicy[1]));
		} else if (validationMap.size() == 0 && !newPassword.equals(confirmNewPassword)) {
			validationMap.put(BaseConstants.CONFORM_PASS_ERROR, getMessage("changePassword.new.password.cnf.new.password.different"));
		}
		return validationMap;
	}

	public Map<String, String> validateForChangePassword(String oldPassword, String newPassword, String confirmNewPassword,
			Object isLoginForFirstTime, String question1, String answer1, String question2, String answer2) {
		Map<String, String> validationMap = new HashMap<>();

		String[] passwordPolicy = systemParameterService.parsePasswordType(MapCache.getConfigValueAsString(SystemParametersConstant.STAFF_PASSWORD,
				""));
		logger.debug("regex::passwordPolicy::" + passwordPolicy[0]);
		logger.debug("PolicyName::passwordPolicy::" + passwordPolicy[1]);
		
		if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(confirmNewPassword))
		{
			if (StringUtils.isEmpty(oldPassword))
				validationMap.put(BaseConstants.OLD_PASS_ERROR, getMessage("changePassword.old.password.missing"));
			if (StringUtils.isEmpty(newPassword))
				validationMap.put(BaseConstants.PASS_ERROR, getMessage("changePassword.new.password.missing"));
			if (StringUtils.isEmpty(confirmNewPassword))
				validationMap.put(BaseConstants.CONFORM_PASS_ERROR, getMessage("changePassword.confirm.new.password.missing"));
			return validationMap;
		}
		if (match(passwordPolicy[0], newPassword)) {

			if (passwordPolicy != null && passwordPolicy.length == 2) {
				validationMap.put(BaseConstants.PASS_ERROR, getMessage(BaseConstants.PASS_INVALID_MSG ).replace(BaseConstants.REGEX, passwordPolicy[1]));
			} else {
				validationMap.put(BaseConstants.PASS_ERROR,
						getMessage(BaseConstants.PASS_INVALID_MSG).replace(BaseConstants.REGEX, Regex.get(SystemParametersConstant.STAFF_PASSWORD)));
			}
		}

		if (match(passwordPolicy[0], confirmNewPassword)) {

			if (passwordPolicy != null && passwordPolicy.length == 2) {
				validationMap.put(BaseConstants.CONFORM_PASS_ERROR,
						getMessage(BaseConstants.CHANGE_PASS_ERROR ).replace(BaseConstants.REGEX, passwordPolicy[1]));
			} else {
				validationMap.put(
						BaseConstants.CONFORM_PASS_ERROR,
						getMessage(BaseConstants.CHANGE_PASS_ERROR ).replace(BaseConstants.REGEX,
								Regex.get(SystemParametersConstant.STAFF_PASSWORD)));
			}

		} else if (validationMap.size() == 0) {

			if (oldPassword.equals(newPassword)) {
				validationMap.put(BaseConstants.PASS_ERROR, getMessage("changePassword.old.password.new.password.different"));
			}

			if (!newPassword.equals(confirmNewPassword)) {
				validationMap.put(BaseConstants.CONFORM_PASS_ERROR, getMessage("changePassword.new.password.cnf.new.password.different"));
			}
		}

		if (!StringUtils.isEmpty(isLoginForFirstTime)) {
			if (StringUtils.isEmpty(question1))
				validationMap.put("QUESTION1_ERROR", getMessage("question1.is.missing"));

			if (StringUtils.isEmpty(question2))
				validationMap.put("QUESTION2_ERROR", getMessage("question2.is.missing"));
			else if (question1.equals(question2))
				validationMap.put("QUESTION2_ERROR", getMessage("security.question1.question2.are.same"));

			if (StringUtils.isEmpty(answer1))
				validationMap.put("ANSWER1_ERROR", getMessage("answer1.is.missing"));

			if (StringUtils.isEmpty(answer2))
				validationMap.put("ANSWER2_ERROR", getMessage("answer2.is.missing"));
		}
		return validationMap;
	}

	private void validatePasswordAndConfirmPassword(String password, String confirmPassword, Errors errors) {

		boolean isPasswordValid = validatePassword(password);

		if (staff.getId() == 0) {
			boolean isConfirmPasswordValid = validateConfirmPassword(confirmPassword);

			if (isPasswordValid && isConfirmPasswordValid && !password.equals(confirmPassword)) {
				errors.rejectValue("confirmPassword", "error.password.and.cnf.password.mismatch", getMessage("password.and.cnf.password.mismatch"));
			}
		}
	}

	public boolean validateReasonForChange(String value) {
		logger.debug("Regex for change::"+Regex.get(SystemParametersConstant.STAFF_REASON_FOR_CHANGE_REGEX));
		
		if(!StringUtils.isEmpty(value)){
		return match("^[\\p{L} .'-]+{1,255}$", value);
			
		}
		else{
			return true;
		}
	}

	/**
	 * Validate staff profile pic
	 * 
	 * @param profilePicContentType
	 * @param errors
	 */
	public void validateStaffProfilePic(MultipartFile file, Errors errors) {
		this.errors = errors;
		String errorMsgValue;
		String profilePicContentType = file.getContentType();	
		String[] supportedTypes = new String[] { SystemParametersConstant.PNG, SystemParametersConstant.JPG};
		final long FOUR_MB_IN_BYTES = 4 * 1024 * 1024L;
		long uploadedFileSizeInBytes = file.getSize();
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		errorMsgValue = getMessage("systemParameter.customerLogo.error");
		logger.debug("checking Staff Profile pic content type");
		
		if (profilePicContentType != null && !(profilePicContentType.startsWith(SystemParametersConstant.IMAGE_CONTENT_TYPE))) {			
			logger.debug("errorMsgValue: " + errorMsgValue);
			errors.rejectValue("profilePic", "systemParameter.customerLogo.error", errorMsgValue);
			return;
		}
		
		logger.debug("checking Staff Profile pic extension");
		if(extension!=null && !(Arrays.asList(supportedTypes).contains(extension.toLowerCase()))) {	
			logger.debug("errorMsgValue: " + errorMsgValue);
			errors.rejectValue("profilePic", "systemParameter.customerLogo.error", errorMsgValue);
			return;
		}
		
		logger.debug("checking Staff Profile pic multiple extensions in filename");
		String fileName = file.getOriginalFilename();
		if(fileName!=null && fileName.split("\\.").length>2) {	
			logger.debug("errorMsgValue: " + errorMsgValue);
			errors.rejectValue("profilePic", "systemParameter.customerLogo.error", errorMsgValue);
			return;
		}
		
		logger.debug("Staff profile pics file type validation with magic number");
		try {
			FileType fileType = getType(file);
			if(fileType == null) {
				logger.debug("Staff profile pics file type not found, since it is not in JPG,JPEG,PNG types");
				logger.debug("errorMsgValue: " + errorMsgValue);
				errors.rejectValue("profilePic", "systemParameter.customerLogo.error", errorMsgValue);
				return;
			}else if(!(fileType.name().equalsIgnoreCase(extension))){
				logger.debug("Staff profile pics's extension is not matched with magic number is type of  "+ fileType.name() + ","
						+ " but found uploaded file's extension is type of " + extension);
				logger.debug("errorMsgValue: " + errorMsgValue);
				errors.rejectValue("profilePic", "systemParameter.customerLogo.error", errorMsgValue);
				return;
			}
		} catch (IOException e) {
			logger.error(e);
			logger.debug("errorMsgValue: " + errorMsgValue);
			errors.rejectValue("profilePic", "systemParameter.customerLogo.error", errorMsgValue);
			return;
		}
		
        logger.debug("checking Staff Profile pic size limit");
	    if (uploadedFileSizeInBytes > FOUR_MB_IN_BYTES) {
	    	errorMsgValue = getMessage("systemParameter.customerLogo.size.error");
	    	errors.rejectValue("profilePic", "systemParameter.customerLogo.size.error", errorMsgValue);	    	
	    }
	    
	}
	
    public FileType getType(MultipartFile file) throws IOException {
        String fileHead = getFileHeader(file);
 
        if (fileHead != null && fileHead.length() > 0) {
            fileHead = fileHead.toUpperCase();
            FileType[] fileTypes = FileType.values();
 
            for (FileType type : fileTypes) {
                if (fileHead.startsWith(type.getValue())) {
                    return type;
                }
            }
        }
 
        return null;
    }
 
    private static String getFileHeader(MultipartFile file) throws IOException {
        byte[] b = new byte[28];
        InputStream inputStream = null;
 
        try {
        	if(file!=null && file.getInputStream()!=null) {
        		inputStream = file.getInputStream();
            	inputStream.read(b, 0, 28);
        	}
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return bytesToHex(b);
    }
 
    public static String bytesToHex(byte[] src){  
        StringBuilder stringBuilder = new StringBuilder("");  
        if (src == null || src.length <= 0) {  
            return null;  
        }
        for (int i = 0; i < src.length; i++) {  
            int v = src[i] & 0xFF;  
            String hv = Integer.toHexString(v);  
            if (hv.length() < 2) {  
                stringBuilder.append(0);  
            }  
            stringBuilder.append(hv);  
        }  
        return stringBuilder.toString();  
    } 
    
     enum FileType {
    	              
        /** JPG */
        JPG("FFD8FF"),
     
        /** PNG */
        PNG("89504E47");
        
        private String value = "";
        
        private FileType(String value) {
            this.value = value;
        }
     
        public String getValue() {
            return value;
        }
     
        public void setValue(String value) {
            this.value = value;
        }
     }

}