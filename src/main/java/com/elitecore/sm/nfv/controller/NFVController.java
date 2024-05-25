package com.elitecore.sm.nfv.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.iam.service.AuthenticationService;
import com.elitecore.sm.nfv.commons.constants.NFVResponseCode;
import com.elitecore.sm.nfv.commons.utils.NFVUtils;
import com.elitecore.sm.nfv.model.NFVAddServer;
import com.elitecore.sm.nfv.model.NFVClient;
import com.elitecore.sm.nfv.model.NFVCopyServer;
import com.elitecore.sm.nfv.model.NFVImportServer;
import com.elitecore.sm.nfv.model.NFVLicense;
import com.elitecore.sm.nfv.model.NFVSyncServer;
import com.elitecore.sm.nfv.service.NFVService;
import com.elitecore.sm.nfv.validator.NFVValidator;

/**
 * The Class NFVController.
 * 
 * @author sagar shah
 * July 13, 2017
 */
@Controller
public class NFVController extends BaseController {

	/** The validator. */
	@Autowired
	private NFVValidator validator;

	/** The authentication service. */
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	NFVService nfvService;
	
	public static final int checkStatusCount=20;
	
	/**
	 * Method will apply full license.
	 *
	 * @param license the license
	 * @param httpRequest the http request
	 * @return the string
	 * @throws SMException the SM exception
	 */
	@RequestMapping(value = ControllerConstants.NFV_ACTIVATE_ENGINE_FULL_LICENSE, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public  String activateEngineFullLicense(	@RequestBody @Validated NFVLicense license, HttpServletRequest httpRequest ) 
			throws SMException {
		ResponseObject responseObject = new ResponseObject();
		validator.validateLicenseActivationParams(license, httpRequest, responseObject);
		if(responseObject.isSuccess() && authenticateLoginCredentials(httpRequest,responseObject)){
			responseObject = nfvService.activateEngineFullLicense(license);
		}
		return NFVUtils.createAjaxResponse(responseObject,NFVResponseCode.LICENSE_CREATION_FAIL).toString();
	}
	
	@RequestMapping(value = ControllerConstants.NFV_CHECK_STATUS, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public String checkStatus(@RequestBody @Validated NFVAddServer serverModel, HttpServletRequest httpRequest) throws SMException, InterruptedException {
		int checkStatusCall=0;
		ResponseObject responseObject = new ResponseObject();
		if(authenticateLoginCredentials(httpRequest, responseObject)) {
			responseObject = nfvService.checkStatus(serverModel);
			while(responseObject.isSuccess()!=true){
				checkStatusCall++;
				logger.info("checkStatusCall Counter : " + checkStatusCall);
				if(checkStatusCall<=checkStatusCount){
				      responseObject = nfvService.checkStatus(serverModel);
				      if(responseObject.isSuccess()==true){
						   logger.info("======= NFV_CHECK_STATUS ====="+ responseObject.isSuccess());
						   break;
				      }
				}else{
					logger.info("Limit exceed for making checkStatusCall. Limit is 15 !!");
					responseObject.setSuccess(false);
					responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_CHECK_STATUS_TRY_COUNT_FAIL);
					break;
				}
			   Thread.sleep(2000);
			}
			
		}
		return NFVUtils.createAjaxResponse(responseObject,NFVResponseCode.SERVER_INSTANCE_CHECK_STATUS_FAIL).toString();
	}
	
	@RequestMapping(value = ControllerConstants.NFV_ADD_SERVER, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public  String addServer(	@RequestBody @Validated NFVAddServer serverModel, HttpServletRequest httpRequest) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		if(authenticateLoginCredentials(httpRequest,responseObject)){
			responseObject = nfvService.addServer(serverModel);
		}
	    return NFVUtils.createAjaxResponse(responseObject,NFVResponseCode.SERVER_INSERT_FAIL).toString();
	}
	
	
	
	/**
	 * Method will apply full license.
	 *
	 * @param serverModel the server model
	 * @param httpRequest the http request
	 * @return the string
	 * @throws SMException the SM exception
	 */
	@RequestMapping(value = ControllerConstants.NFV_ADD_SERVER_INSTANCE, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public  String addServerInstance(	@RequestBody @Validated NFVAddServer serverModel, HttpServletRequest httpRequest) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		if(authenticateLoginCredentials(httpRequest,responseObject)){
			responseObject = nfvService.addServerInstance(serverModel);
		}
	    return NFVUtils.createAjaxResponse(responseObject,NFVResponseCode.SERVER_INSTANCE_INSERT_FAIL).toString();
	}
	
	/**
	 * @see api will sync given server
	 * @author brijesh.soni
	 * @param serverModel
	 * @param httpRequest
	 * @return NFVResponseObject json string
	 * @throws SMException
	 */
	@RequestMapping(value = ControllerConstants.NFV_SYNC_SERVER_INSTANCE, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public String syncServer(@RequestBody @Validated NFVSyncServer serverModel, HttpServletRequest httpRequest) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		validator.validateSyncServerParams(serverModel, httpRequest, responseObject);
		if(responseObject.isSuccess() && authenticateLoginCredentials(httpRequest, responseObject)) {
			responseObject = nfvService.syncServer(serverModel);
		}
		return NFVUtils.createAjaxResponse(responseObject, NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE).toString();
	}
	
	/**
	 * @see api will copy server configuration from(copyFromIp) server to(copyToIp) server
	 * @author brijesh.soni
	 * @param serverModel
	 * @param httpRequest
	 * @return NFVResponseObject json string
	 * @throws SMException
	 */
	@RequestMapping(value = ControllerConstants.NFV_COPY_SERVER_INSTANCE, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public String copyServer(@RequestBody @Validated NFVCopyServer serverModel, HttpServletRequest httpRequest) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		validator.validateCopyServerParams(serverModel, httpRequest, responseObject);
		if(responseObject.isSuccess() && authenticateLoginCredentials(httpRequest, responseObject)) {
			responseObject = nfvService.copyServer(serverModel, eliteUtils.getLoggedInStaffId(httpRequest));
		}
		return NFVUtils.createAjaxResponse(responseObject, NFVResponseCode.SERVER_INSTANCE_COPY_CONFIG_FAIL).toString();
	}
	/**
	 * @see api will import server configuration 
	 * @author brijesh.soni
	 * @param serverModel
	 * @param httpRequest
	 * @return NFVResponseObject json string
	 * @throws SMException
	 */
	@RequestMapping(value = ControllerConstants.NFV_IMPORT_SERVER_INSTANCE, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public String importServer(@RequestBody @Validated NFVImportServer serverModel,HttpServletRequest httpRequest) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		if( authenticateLoginCredentials(httpRequest, responseObject)) {
			responseObject = nfvService.importServer(serverModel,eliteUtils.getLoggedInStaffId(httpRequest));
		}
		return NFVUtils.createAjaxResponse(responseObject, NFVResponseCode.SERVER_INSTANCE_COPY_CONFIG_FAIL).toString();
	}
	
	
	/**
	 * @see api will restart given server
	 * @author brijesh.soni
	 * @param serverModel
	 * @param httpRequest
	 * @return NFVResponseObject json string
	 * @throws SMException
	 */
	@RequestMapping(value = ControllerConstants.NFV_RESTART_SERVER_INSTANCE, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public String restartServer(@RequestBody @Validated NFVSyncServer serverModel, HttpServletRequest httpRequest) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		validator.validateSyncServerParams(serverModel, httpRequest, responseObject);
		if(responseObject.isSuccess() && authenticateLoginCredentials(httpRequest, responseObject)) {
			responseObject = nfvService.restartServer(serverModel);
		}
		return NFVUtils.createAjaxResponse(responseObject, NFVResponseCode.SERVER_DELETE_FAIL).toString();
	}
	
	
	@RequestMapping(value = ControllerConstants.NFV_DELETE_SERVER_INSTANCE, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public String deleteServerInstance(@RequestBody @Validated NFVAddServer serverModel, HttpServletRequest httpRequest) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		if(authenticateLoginCredentials(httpRequest, responseObject)) {
			responseObject = nfvService.deleteServerInstance(serverModel);
		}
		return NFVUtils.createAjaxResponse(responseObject, NFVResponseCode.SERVER_INSTANCE_DELETE_FAIL).toString();
	}
	
	@RequestMapping(value = ControllerConstants.NFV_DELETE_SERVER_INSTANCE_ONLY_IN_SM, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public String deleteServerInstanceOnlyInSM(@RequestBody @Validated NFVAddServer serverModel, HttpServletRequest httpRequest) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		if(authenticateLoginCredentials(httpRequest, responseObject)) {
			responseObject = nfvService.deleteServerInstanceOnlyInSM(serverModel);
		}
		return NFVUtils.createAjaxResponse(responseObject, NFVResponseCode.SERVER_INSTANCE_DELETE_FAIL).toString();
	}
	
	@RequestMapping(value = ControllerConstants.NFV_DELETE_SERVER, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public String deleteServer(@RequestBody @Validated NFVAddServer serverModel, HttpServletRequest httpRequest) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		if(authenticateLoginCredentials(httpRequest, responseObject)) {
			responseObject = nfvService.deleteServer(serverModel);
		}
		return NFVUtils.createAjaxResponse(responseObject, NFVResponseCode.SERVER_INSTANCE_RESTART_FAIL).toString();
	}
	
	/**
	 * @see api will start given server
	 * @author brijesh.soni
	 * @param serverModel
	 * @param httpRequest
	 * @return NFVResponseObject json string
	 * @throws SMException
	 */
	@RequestMapping(value = ControllerConstants.NFV_START_SERVER_INSTANCE, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public String startServer(@RequestBody @Validated NFVSyncServer serverModel, HttpServletRequest httpRequest) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		validator.validateSyncServerParams(serverModel, httpRequest, responseObject);
		if(responseObject.isSuccess() && authenticateLoginCredentials(httpRequest, responseObject)) {
			responseObject = nfvService.startServer(serverModel);
		}
		return NFVUtils.createAjaxResponse(responseObject, NFVResponseCode.SERVER_INSTANCE_START_FAIL).toString();
	}
	
	/**
	 * @see api will stop given server
	 * @author brijesh.soni
	 * @param serverModel
	 * @param httpRequest
	 * @return NFVResponseObject json string
	 * @throws SMException
	 */
	@RequestMapping(value = ControllerConstants.NFV_STOP_SERVER_INSTANCE, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public String stopServer(@RequestBody @Validated NFVSyncServer serverModel, HttpServletRequest httpRequest) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		validator.validateSyncServerParams(serverModel, httpRequest, responseObject);
		if(responseObject.isSuccess() && authenticateLoginCredentials(httpRequest, responseObject)) {
			responseObject = nfvService.stopServer(serverModel);
		}
		return NFVUtils.createAjaxResponse(responseObject, NFVResponseCode.SERVER_INSTANCE_STOP_FAIL).toString();
	}
	
	/**
	 * @see api will add client to given server in given service type
	 * @author brijesh.soni
	 * @param client
	 * @param httpRequest
	 * @return NFVResponseObject json string
	 * @throws SMException
	 */
	@RequestMapping(value = ControllerConstants.NFV_ADD_CLIENT, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public String addClient(@RequestBody @Validated NFVClient client, HttpServletRequest httpRequest) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		validator.validateClientParams(client, httpRequest, responseObject);
		if(responseObject.isSuccess() && authenticateLoginCredentials(httpRequest, responseObject)) {
			responseObject = nfvService.addClient(client);
		}
		return NFVUtils.createAjaxResponse(responseObject, NFVResponseCode.ADD_CLIENT_FAIL).toString();
	}
	
	/**
	 * Authenticate login credentials.
	 *
	 * @param httpRequest the http request
	 * @param responseObject the response object
	 * @return true, if successful
	 * @throws SMException the SM exception
	 */
	private boolean authenticateLoginCredentials(HttpServletRequest httpRequest, ResponseObject responseObject) throws SMException{
		JSONObject jsonObj = getLoginResponse(httpRequest);
	    boolean bSuccess = jsonObj.getBoolean("success");
	    if(!bSuccess){
	    	responseObject.setSuccess(bSuccess);
	    	responseObject.setResponseCodeNFV(NFVResponseCode.INVALID_LOGIN_CREDENTIAL);
	    	logger.info("NFV login failed!");
	    }
	    return bSuccess; 
	}
	
	/**
	 * Gets the login response.
	 *
	 * @param httpRequest the http request
	 * @return the login response
	 * @throws SMException the SM exception
	 */
	public JSONObject getLoginResponse(HttpServletRequest httpRequest) throws SMException {
		logger.debug("login using rest api ");
		String username = httpRequest.getHeader(BaseConstants.HEADER_USERNAME);
		String password = httpRequest.getHeader(BaseConstants.HEADER_PASS_PARAM);
		return authenticationService.authenticate(username, password);
	}
	
	/**
	 * Login.
	 *
	 * @param httpRequest the http request
	 * @return the string
	 * @throws SMException the SM exception
	 */
	@RequestMapping(value = ControllerConstants.NFV_LOGIN, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public String login(HttpServletRequest httpRequest) throws SMException {
		logger.debug("login using rest api ");
		ResponseObject responseObject = new ResponseObject();
		if(validator.validateLoginParams(httpRequest, responseObject) && authenticateLoginCredentials(httpRequest, responseObject)){
			responseObject.setSuccess(true);
			responseObject.setResponseCodeNFV(NFVResponseCode.SUCCESS);
		}
		return NFVUtils.createAjaxResponse(responseObject,NFVResponseCode.INVALID_LOGIN_CREDENTIAL).toString();
	}
}