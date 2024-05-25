package com.elitecore.sm.nfv.commons.utils;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.nfv.commons.constants.NFVResponseCode;

/**
 * The Class NFVUtils.
 * 
 * @author sagar shah
 * July 13, 2017
 */
public class NFVUtils {

	/**
	 * Creates the ajax response.
	 *
	 * @param responseObject the response object
	 * @param defaultResponseCode the default response code
	 * @return the NFV ajax response
	 */
	public static NFVAjaxResponse createAjaxResponse(ResponseObject responseObject, NFVResponseCode defaultResponseCode){
		NFVAjaxResponse ajaxResponse = new NFVAjaxResponse();
		if(responseObject == null){
			responseObject = new ResponseObject();
			responseObject.setResponseCodeNFV(defaultResponseCode);
		}
		if(!responseObject.isSuccess() && responseObject.getResponseCodeNFV() == null){
			responseObject.setResponseCodeNFV(defaultResponseCode);
		}
		if(responseObject.isSuccess()){
			responseObject.setResponseCodeNFV(NFVResponseCode.SUCCESS);
		}
		ajaxResponse.setResponseCode(String.valueOf(responseObject.getResponseCodeNFV().getCode()));
		ajaxResponse.setResponseMsg(responseObject.getResponseCodeNFV().getDescription());
		
		return ajaxResponse;
	}
}
