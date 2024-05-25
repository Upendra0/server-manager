/**
 * 
 */
package com.elitecore.sm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.configmanager.model.VersionConfig;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.iam.service.StaffService;
import com.elitecore.sm.systemaudit.model.AuditActivity;
import com.elitecore.sm.systemaudit.model.AuditUserDetails;
import com.elitecore.sm.systemaudit.service.SystemAuditService;

/**
 * Utility class
 * 
 * @author Sunil Gulabani
 * Mar 17, 2015
 */
public class EliteUtils {
	
	private static Logger logger = Logger.getLogger(EliteUtils.class);
	
	private static final Pattern HEXADECIMAL_PATTERN = Pattern.compile("\\p{XDigit}+");
	
	@Autowired
    public MessageSource messageSource;
	
	@Autowired(required=true)
	@Qualifier(value="staffService")
	private StaffService staffService;
	
	@Autowired
	private SystemAuditService systemAuditService;
	
	private static Date dateForImport = null;
	
	public EliteUtils() {
		// this is instantiated by spring
	}
	
	/**
	 * Used to get the IP address from the request.
	 * 
	 * @param request HttpServletRequest 
	 * @return IP Address
	 */
	public String getFramedIpAddress(HttpServletRequest request){
		String framedIp ;
		String xFF = request.getHeader("x-forwarded-for");
		if(xFF == null){
			xFF = request.getHeader("X-FORWARDED-FOR");
		}
		if(xFF == null){
			framedIp = request.getRemoteAddr();
			logger.debug("Request from host with ip :"+framedIp);
		}else{
			StringTokenizer tokens = new StringTokenizer(xFF,",");
			framedIp = tokens.nextToken();
		}
		return framedIp;
	}
	
	public int getStartIndex(int limit, int currentPage, int totalPages){
		int cPage=currentPage ;
		if (currentPage > totalPages)
			cPage = totalPages;
		return (limit * cPage) - limit;
	}
	
	public int getTotalPagesCount(double recordCount, int limit){
		logger.info("recordCount: " + recordCount);
		logger.info("limit: " + limit);
		double pages = Math.ceil(recordCount/limit);
		logger.info("Math.ceil(recordCount/limit): " + pages);
		if (recordCount > 0)
			return (int) pages;
		return 0;
	}
	
	public boolean isCredentialsExpired(Date passwordExpiryDate,String username){
		logger.info("passwordExpiryDate: " + passwordExpiryDate);
		logger.info("new Date(): " + new Date());
		if(passwordExpiryDate!=null){
			logger.info("passwordExpiryDate.before(new Date()): " + !passwordExpiryDate.before(new Date()));
			return !passwordExpiryDate.before(new Date());
		}else{
			//Allow login for moduleadmin and profile admin user
			logger.info("Password expiry date is null");
			if(BaseConstants.MODULE_ADMIN_USERNAME.equalsIgnoreCase(username) || BaseConstants.PROFILE_ADMIN_USERNAME.equalsIgnoreCase(username)){
				return true;
			}
		}
		return false;
	}
	
	public Staff getLoggedInUser(HttpServletRequest request){
		return (Staff)request.getSession().getAttribute(BaseConstants.STAFF_DETAILS);
	}
	
	public String getFirstNameAndLastNameOfUser(HttpServletRequest request){
		Staff staff = getLoggedInUser(request);
		if(staff!=null){
			return staff.getName();
		}
		return "Guest";
	}
	
	public String getUserNameOfUser(HttpServletRequest request){
		Staff staff = getLoggedInUser(request);
		if(staff!=null){
			return staff.getUsername();
		}
		return "Guest";
	}
	
	public int getLoggedInStaffId(HttpServletRequest request){
		Staff staff = getLoggedInUser(request);
		if(staff!=null && staff.getId()!= 0 /*&& !staff.getUsername().equals(BaseConstants.ADMIN_USERNAME)*/){
			return staff.getId();
		}
		return 0;
	}
	
	/**
	 * Fetch customer logo
	 * @param alias
	 * @return
	 * @throws SMException
	 */
	public String getCustomerLogo(String alias,String defaultValue) {
		return MapCache.getConfigValueAsString(alias, defaultValue);
	}
	
	/**
	 * Fetch customer logo
	 * @param alias
	 * @return
	 * @throws SMException
	 */
	public String getStaffLogo(HttpServletRequest request) throws SMException{
		int staffId=getLoggedInStaffId(request);
		if(staffService!=null){
			return staffService.getStaffProfilePicPath(staffId);	
		}else{
			logger.debug("staff service is null");
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	public boolean isAuthorityGranted(String searchKey){
		try {
			List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
			if(Boolean.parseBoolean(MapCache.getConfigValueAsObject(SystemParametersConstant.SSO_ENABLE).toString())){
				List<SimpleGrantedAuthority> new_authorities = new ArrayList<SimpleGrantedAuthority>();	
				for (GrantedAuthority role  : authorities) {	
					new_authorities.add(new SimpleGrantedAuthority(role.getAuthority().toString()));
				}
				if(new_authorities!=null){
					return new_authorities.contains(new SimpleGrantedAuthority(searchKey));
				}
			}else{
				if(authorities!=null){
					return authorities.contains(new SimpleGrantedAuthority(searchKey));
				}
			}
			
		} catch (Exception e) {
			logger.error(" Error :"+e,e);
		}
		return false;
	}
	
	public AjaxResponse convertToAjaxResponse(ResponseObject responseObj){
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		if(responseObj.isSuccess()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_SUCCESS);
		} else {
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
		}
		
		if(responseObj.getArgs()==null){
			if(responseObj.getResponseCode()!=null){
				ajaxResponse.setResponseMsg(getMessage(String.valueOf(responseObj.getResponseCode()),null));
			}
		} else {
			if(responseObj.getResponseCode()!=null){
				ajaxResponse.setResponseMsg(getMessage(String.valueOf(responseObj.getResponseCode()),responseObj.getArgs()));
			}
		}
		
		
		if(responseObj.getObject() != null){
			ajaxResponse.setObject(responseObj.getObject());	
		}
		
		if(!StringUtils.isEmpty(responseObj.getModuleName())){
			ajaxResponse.setModuleName(responseObj.getModuleName());
		}
		return ajaxResponse;
	}
	
	public void transferErrorsToAjaxResponse(AjaxResponse ajaxResponse,BindingResult result){
		
		ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

		Map<String, String> errorMsgs = new HashMap<>();

		for(FieldError error:result.getFieldErrors()){
			errorMsgs.put(error.getField(), error.getDefaultMessage());
		}
		
		ajaxResponse.setObject(errorMsgs);

	}
	
	protected String getMessage(String key,Object[]arguments){
		try {
			return messageSource.getMessage(key, arguments, LocaleContextHolder.getLocale());
		} catch (Exception e) {
			logger.error(" Error :"+ e.getMessage(), e);
		}
		return "";
	}
	
	/**
	 * Fetch Entity Type List from cache by using server type
	 * @param serverTypeId
	 * @param entityType
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List fetchProfileEntityStatusFromCache(int serverTypeId,String entityType){
		
		logger.debug("inside fetchProfileEntityStatusFromCache Key for fetch from MAP::  " + serverTypeId  + " And" +entityType);
		Map<String, List<BaseModel>> entityStatusMap=(Map<String, List<BaseModel>>)MapCache.getConfigCollectionAsObject(SystemParametersConstant.CUSTOM_PRODUCT_CONFIGURATION_ENTITY_STATUS, 
																																											String.valueOf(serverTypeId), null);
		List entityList=null;
		logger.debug("entityStatusMap found " +entityStatusMap);
		if(entityStatusMap !=null && !entityStatusMap.isEmpty() && !StringUtils.isEmpty(entityType)){
		
				entityList=entityStatusMap.get(entityType);
		
		}
		return entityList;
	}
	
	public StaffService getStaffService() {
		return staffService;
	}

	public void setStaffService(StaffService userService) {
		this.staffService = userService;
	}
	
	public static String checkForNames(String action,String nameInConfig){
		String updatedName= nameInConfig;
		String[] temp;
		if(nameInConfig != null){
		if("import".equalsIgnoreCase(action)){
			
			if(nameInConfig.contains(BaseConstants.SUFFIX_FOR_IMPORT)){
				temp=nameInConfig.split(BaseConstants.SUFFIX_FOR_IMPORT);
				if(temp[0]!=null)
					updatedName = temp[0] + BaseConstants.SUFFIX_FOR_IMPORT + new Date().getTime();
				else
					updatedName = nameInConfig + BaseConstants.SUFFIX_FOR_IMPORT + new Date().getTime();
			}else{
				updatedName = nameInConfig + BaseConstants.SUFFIX_FOR_IMPORT + new Date().getTime();
			}
			
			
		}else if("delete".equalsIgnoreCase(action)){
			
			if(nameInConfig.contains(BaseConstants.SUFFIX_FOR_DELETE)){
				temp=nameInConfig.split(BaseConstants.SUFFIX_FOR_DELETE);
				if(temp[0]!=null)
					updatedName = temp[0] + BaseConstants.SUFFIX_FOR_DELETE + new Date().getTime();
				else
					updatedName = nameInConfig + BaseConstants.SUFFIX_FOR_DELETE + new Date().getTime();
			}else{
				updatedName = nameInConfig + BaseConstants.SUFFIX_FOR_DELETE + new Date().getTime();
			}
			
		}
		}
		return updatedName;
		
	}
	
	/**
	 * Get CSV String from List of Strings
	 * @param list the input list of strings
	 * @return CSV String output
	 */
	public static String getCSVString(List<String> list) {
		String returnString = org.apache.commons.lang3.StringUtils.EMPTY;
		if(list != null && !list.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < list.size(); i++) {
				sb.append(list.get(i));
				if(i != list.size() - 1) {
					sb.append(",");
				}
			}
			returnString = sb.toString();
		}
		return returnString;
	}
	
	public static String[] getStringArrayFromEnum(Class<?> enumClass) {
		String[] stringArray = null;
		Object[] array = enumClass.getEnumConstants();
		if(array != null) {
			int length = array.length;
			stringArray = new String[length];
			for(int i = length-1; i >=0; i--) {
				stringArray[i] = array[i].toString();
			}
			return stringArray;
		}
		return stringArray;
	}
	
	/** Method will convert comma separated string to integer array.
	 * @param stringIds
	 * @return
	 */
	public static Integer[] convertStringArrayToInt(String stringIds){
		Integer[] numbers = null;
		if(!StringUtils.isEmpty(stringIds)){
			String [] ids = stringIds.split(",");
			if(!StringUtils.isEmpty(ids)) {
				int length = ids.length;
				numbers = new Integer[length];
				for(int i = 0; i < length; i++){
					try {
						numbers[i] = Integer.parseInt(ids[i]);
					} catch (NumberFormatException e) {
						numbers[i] = -1;
						logger.error("NumberFormatException convertStringArrayToInt : "+ e.getMessage(), e);
					}
				}
			}
		}
		return numbers;
	}
	
	/**
	 * @param baseModelList
	 * @see This method will return the list which contains not deleted objects / active objects
	 * @return List<BaseModel>
	 */
	public static List<BaseModel> getActiveListFromGivenList(List<? extends BaseModel> baseModelList) {
		return baseModelList.stream().filter(object -> !object.getStatus().equals(StateEnum.DELETED)).collect(Collectors.toList());
	}
	
	/**
	 * @param isInitialize
	 * @see This method is used in import functionality. In import -> newly created entry will fetch created date and updated date 
	 * from this method. singleton date object will be returned by this method for every import.
	 * @return
	 */
	public static Date getDateForImport(boolean isRequired) {
		if(isRequired || dateForImport == null) {
			dateForImport = new Date();
		}
		return dateForImport;
	}
	
	public static Date getDateForSyncPublish(Date date) {
		DateFormat dateFormatter = new SimpleDateFormat(BaseConstants.SYNC_PUBLISH_DATE_TIME_FORMATTER);
		String outDate = null;
		try {
			outDate = dateFormatter.format(date);
			date = dateFormatter.parse(outDate);
		} catch (ParseException ex) {
		}
	    return date;
	}
	
	/**
	 * @param s1
	 * @param s2
	 * @return true if two string are equal
	 */
	public static boolean compare(String s1, String s2) {
		if(s1 == null && s2 == null) {
			return true;
		}
		if(s1 == null && s2 != null) {
			return false;
		}
		if(s1 != null && s2 == null) {
			return false;
		}
		if(s1 != null && s2 != null && s1.equals(s2)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param inDate
	 * @return String
	 */
	public static String formatDate(String inDate) {
		SimpleDateFormat inSDF = new SimpleDateFormat("dd/mm/yyyy");
		SimpleDateFormat outSDF = new SimpleDateFormat("yy-mm-dd");
		String outDate = null;
		try {
			Date date = outSDF.parse(inDate);
			outDate = inSDF.format(date);
		} catch (ParseException ex) {
		}
	    return outDate;
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	public static String addPaddingToString(String input){
		if(input!=null && !"".equals(input) && input.length()<2){
			input="0"+input;
			return input;
		}
		return input;
	}
	
	@SuppressWarnings("unchecked")
	public void addLoginAuditDetails(int staffId, String name,String currentAuditAction, String auditActionIp){
		logger.debug("auditing loging action.");
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		HashMap<String, AuditActivity> auditActivityMap  =  (HashMap<String, AuditActivity>) MapCache.getConfigValueAsObject(SystemParametersConstant.AUDIT_ACTIVITY_ALIAS_LIST);
		AuditUserDetails auditUserDetails = new AuditUserDetails(request.getRemoteAddr(), staffId, name, null);
		AuditActivity auditActivityObj = auditActivityMap.get(currentAuditAction);
		String remarkMsg = "";
		if(auditActivityObj != null){
			remarkMsg = auditActivityObj.getMessage();
			if (remarkMsg != null && remarkMsg.indexOf("$entityName") >= 0) {
				remarkMsg = remarkMsg.replace("$entityName", "<b>"  + name + "</b>");
			}
			if (remarkMsg != null && remarkMsg.indexOf("$staffName") >= 0) {
				remarkMsg = remarkMsg.replace("$staffName", "<b>"  + name + "</b>");
			}
		}
		// use audit action from input if not passed default to SM_ACTION
		String auditAction=auditActionIp;
		if (StringUtils.isEmpty(auditAction))
			auditAction=BaseConstants.SM_ACTION;
		
		systemAuditService.addAuditDetails(auditUserDetails, auditActivityMap.get(currentAuditAction), null,auditAction ,remarkMsg);
	}
	
	public static byte[] convertFileContentToBlob(File file) throws IOException {
		byte[] fileContent = null;
	        // initialize string buffer to hold contents of file
		StringBuffer fileContentStr = new StringBuffer(""); //NOSONAR
		BufferedReader reader = null;
		FileReader fr=null;
		try { //NOSONAR
			fr=new FileReader(file);//NOSONAR
	                // initialize buffered reader  
			reader = new BufferedReader(new FileReader(file));//NOSONAR
			String line = null;
	                // read lines of file
			while ((line = reader.readLine()) != null) {
	                        //append line to string buffer
				fileContentStr.append(line).append("\n");
			}
	                // convert string to byte array
			fileContent = fileContentStr.toString().trim().getBytes();
		} catch (IOException e) {//NOSONAR
			throw new IOException("Unable to convert file to byte array. " + e.getMessage());//NOSONAR
		} finally {
			if (reader != null) {
				reader.close();
			}
			if(fr!=null)
				fr.close();
		}
		return fileContent;
	}
	
	public static File convertBlobToFileContent(VersionConfig versionConfig, String tempPathForSyncPublish) throws IOException {
		File file = null;
		FileOutputStream os = null;
		InputStream is = null;
		try{
			Blob blob = versionConfig.getFile();
			String fileName = tempPathForSyncPublish + File.separator + versionConfig.getName() + ".xml";
	        file = new File(fileName);
        	os = new FileOutputStream(file); //NOSONAR
	        is = blob.getBinaryStream();
	        byte[] buffer = new byte[(int) blob.length()];
			while(is.read(buffer)!=-1){
				os.write(buffer);
			}
			os.flush();
        	os.close();
        	return file;
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if(is!=null){
				is.close();
			}
			if(os!=null){
				os.flush();
				os.close();
			}
		}
		return null;
	}
	
	public static String compareXmlFile(File actualOutputFile, File expectedOutputFile,String absolutePath) {

		Scanner input1 = null;
		Scanner input2 = null;
		StringBuilder stringBuilder2 = new StringBuilder();
		try {
		    input1 = new Scanner(actualOutputFile);
			input2 = new Scanner(expectedOutputFile);
			
			boolean flag = false;
			StringBuilder builder = new StringBuilder();
			String first = "";
			String second = "";
			while (!flag) {
				if(input1.hasNextLine()){
					first = input1.nextLine();
				} else {
					first = null;
				}
				if(input2.hasNextLine()){
					second = input2.nextLine();
				} else if(!input2.hasNextLine()) {
					second = null;
				}
				if(!input1.hasNextLine() && !input2.hasNextLine()){
					flag= true;
				}
				if (first!=null && second!=null && !first.trim().equals(second.trim())) {
					stringBuilder2.append(
							"<div style='font-family: Times New Roman, Times, serif;Color:red;display: inline;'><xmp>"
									+ first + "</xmp></div>");
					builder.append(
							"<div style='font-family: Times New Roman, Times, serif;Color:green;display: inline;'><xmp>"
									+ second +"</xmp></div>");
					
				} else if(first==null && second!=null) {
					builder.append(
							"<div style='font-family: Times New Roman, Times, serif;Color:green;display: inline;'><xmp>"
									+ second +"</xmp></div>");
				} else if(first!=null && second==null) {
					stringBuilder2.append(
							"<div style='font-family: Times New Roman, Times, serif;Color:red;display: inline;'><xmp>"
									+ first + "</xmp></div>");
				} else {
					if(!builder.toString().isEmpty()){
						stringBuilder2.append(builder);
						builder.delete(0,builder.toString().length());
					}
					stringBuilder2.append(
							"<div style='font-family: Times New Roman, Times, serif;display: inline;'><xmp>"
									+ first +"</xmp></div>");
				}
			}
			if(!builder.toString().isEmpty()){
				stringBuilder2.append(builder);
				builder.delete(0,builder.toString().length());
			}
			
		} catch (Exception e) {
			logger.error(e);
		}finally {
			if(input1!=null)
				input1.close();
			if(input2!=null)
				input2.close();
			if(actualOutputFile.exists())
				actualOutputFile.delete();
			if(expectedOutputFile.exists())
				expectedOutputFile.delete();
		}
		return stringBuilder2.toString();
	}
	
	/**
	 * To encrypt the password to hexadecimal
	 * 
	 * @param pwd
	 * @return
	 */
	public static String encryptData(String pwd) {

		char[] chars = pwd.toCharArray();

		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			hex.append(Integer.toHexString((int) chars[i]));
		}

		return hex.toString();

	}

	/**
	 * Decrypt password to ASCCII
	 * 
	 * @param pwd
	 * @return
	 */
	public static String decryptData(String pwd) {
		byte[] bytes = DatatypeConverter.parseHexBinary(pwd.trim());
		String decodedPwd = new String(bytes);
		return decodedPwd;
	}
	
	public static boolean isHexadecimal(String input) {
		final Matcher matcher = HEXADECIMAL_PATTERN.matcher(input);
		return matcher.matches();
	}
	
	public static byte[] convertBlobToByteArray(Blob blob) {
		
		int blobLength;
		byte[] blobAsBytes = null ;
		try {
			blobLength = (int) blob.length();
			blobAsBytes = blob.getBytes(1, blobLength);
		} catch (SQLException e) {
			logger.error(e);
		}  
		
		return blobAsBytes;
		
	}
	
	public static byte[] convertFileToByteArray(File file) throws IOException  {
		return Files.readAllBytes(file.toPath());
	}
	
	public static boolean isJSONValid(String jsonString) {
	    try {
	        new JSONObject(jsonString);
	    } catch (JSONException ex) { //NOSONAR
	        try {
	            new JSONArray(jsonString);
	        } catch (JSONException ex1) { //NOSONAR
	            return false;
	        }
	    }
	    return true;
	}
	
 }