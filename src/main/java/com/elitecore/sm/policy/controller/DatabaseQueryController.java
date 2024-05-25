package com.elitecore.sm.policy.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.policy.model.DatabaseQuery;
import com.elitecore.sm.policy.model.DatabaseQueryAction;
import com.elitecore.sm.policy.model.DatabaseQueryCondition;
import com.elitecore.sm.policy.model.SearchDatabaseQuery;
import com.elitecore.sm.policy.service.IDatabaseQueryService;
import com.elitecore.sm.policy.service.IPolicyService;
import com.elitecore.sm.policy.validator.DatabaseQueryValidator;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.util.DateFormatter;

/**
 * The controller for database Query
 * @author Sagar Ghetiya
 *
 */

@Controller
public class DatabaseQueryController extends BaseController{
		
	@Autowired
	ServerInstanceService serverInstanceService;
	
	@Autowired
	IPolicyService policyService;
	
	@Autowired
	IDatabaseQueryService databaseQueryService;
	
	@Autowired
	DatabaseQueryValidator databaseQueryValidator;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));

	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_POLICY_CONFIGURATION','UPDATE_POLICY_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.INIT_DATABASE_QUERY_LIST, method = RequestMethod.GET)
	@ResponseBody
	public String initDatabaseQueryList(
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = true) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			@RequestParam(value = BaseConstants.QUERY_NAME, required = false) String queryName,
			@RequestParam(value = BaseConstants.DESCRIPTION, required = false) String searchDescription,
			@RequestParam(value = BaseConstants.ASSOCIATION_STATUS, required = false) String searchAssociationStatus,
			@RequestParam(value = BaseConstants.SERVER_INSTANCES_ID) int serverInstanceId,
			@RequestParam(value = BaseConstants.IS_SEARCH, required = false) boolean isSearch) {
		SearchDatabaseQuery searchQuery = new SearchDatabaseQuery(queryName,searchDescription,searchAssociationStatus,serverInstanceId);
		List<DatabaseQuery> resultList;
		Map<String, Object> row;
		databaseQueryService.setAssociationStatus(serverInstanceId);
		long count = databaseQueryService.getQueryListCountByServerId(searchQuery);
		resultList  = this.databaseQueryService.getPaginatedList(serverInstanceId, eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord,isSearch,searchQuery);
		List<Map<String, Object>> rowList = new ArrayList<>();
		if (resultList != null) { 
			for (DatabaseQuery query : resultList) {
				row = new HashMap<>();
				row.put(BaseConstants.ID, query.getId());
				row.put(BaseConstants.QUERY_NAME, query.getQueryName());
				row.put(BaseConstants.DESCRIPTION, StringUtils.isNotEmpty(query.getDescription()) ? query.getDescription() : StringUtils.EMPTY);
				row.put("delete", query.getAssociationStatus()!= null && 
						query.getAssociationStatus().equalsIgnoreCase(BaseConstants.ASSOCIATED) ? "Associated" : "");
				row.put("queryValue", query.getQueryValue());
				row.put("outputDbField", StringUtils.isNotEmpty(query.getOutputDbField()) ? query.getOutputDbField() : StringUtils.EMPTY);
				row.put("conditionExpression", StringUtils.isNotEmpty(query.getConditionExpression()) ? query.getConditionExpression() : StringUtils.EMPTY);
				row.put("logicalOperator", query.getLogicalOperator());
				row.put("returnMultipleRowsEnable", query.isReturnMultipleRowsEnable());
				row.put("cacheEnable", query.getCacheEnable());
				row.put("conditionExpressionEnable", query.getConditionExpressionEnable());
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_POLICY_CONFIGURATION','UPDATE_POLICY_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.GET_DATABASE_QUERY_LIST, method = RequestMethod.GET)
	@ResponseBody
	public String getDatabaseQueryList(@RequestParam(value = BaseConstants.SERVER_INSTANCES_ID) int serverInstanceId,@RequestParam(value = "tableName",required=false)String tableName) {
		ResponseObject responseObject = new ResponseObject();
		List<DatabaseQuery> dbQueryList = databaseQueryService.getAllQueriesByServerIdAndtableName(serverInstanceId,tableName);
		JSONObject jAttributeList = new JSONObject();
		JSONObject jAttrObj;
		JSONArray jAllAttrArr = new JSONArray();
		if (dbQueryList != null) {
			for (DatabaseQuery dbQuery : dbQueryList) {
				jAttrObj = new JSONObject();
				jAttrObj.put("id", dbQuery.getId());
				jAttrObj.put("queryName", dbQuery.getQueryName());
				jAllAttrArr.put(jAttrObj);
			}
		}
		jAttributeList.put("dbQueryList", jAllAttrArr);
		responseObject.setObject(jAttributeList);
		responseObject.setSuccess(true);
				
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_POLICY_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.CREATE_DATABASE_QUERY ,method = RequestMethod.POST)
	@ResponseBody
	public String createDatabaseQuery(
			@ModelAttribute (value=FormBeanConstants.DATABASE_QUERY_FORM_BEAN) DatabaseQuery databaseQuery,//NOSONAR
			@RequestParam(value = "serverInstanceId",required=true) String serverInstanceId,
			@RequestParam(value = "actionList",required=false)String actionList,
			@RequestParam(value = "conditionList",required=false)String conditionList,
			BindingResult result,
			HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		databaseQueryValidator.validateDatabaseQuery(databaseQuery, result, null, false,null);
		databaseQuery = addActionsAndConditions(databaseQuery,actionList,conditionList,staffId);
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			ServerInstance serverInstance = serverInstanceService.getServerInstance(Integer.parseInt(serverInstanceId));
			databaseQuery.setServerInstance(serverInstance);
			responseObject = databaseQueryService.createDatabaseQuery(databaseQuery,serverInstanceId,staffId);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_POLICY_CONFIGURATION','CREATE_POLICY_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.UPDATE_DATABASE_QUERY, method=RequestMethod.POST)
	@ResponseBody
	public String updateDatabaseQuery(
			@ModelAttribute (value=FormBeanConstants.DATABASE_QUERY_FORM_BEAN) DatabaseQuery databaseQuery,//NOSONAR
			@RequestParam(value = "serverInstanceId",required=true) String serverInstanceId,
			@RequestParam(value = "actionList",required=false)String actionList,
			@RequestParam(value = "conditionList",required=false)String conditionList,
			BindingResult result,
			HttpServletRequest request
			){
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		databaseQueryValidator.validateDatabaseQuery(databaseQuery, result, null, false,null);
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			databaseQuery = addActionsAndConditions(databaseQuery,actionList,conditionList,staffId);
			ServerInstance serverInstance = serverInstanceService.getServerInstance(Integer.parseInt(serverInstanceId));
			databaseQuery.setServerInstance(serverInstance);
			Hibernate.initialize(databaseQuery);
			Hibernate.initialize(databaseQuery.getDatabaseQueryActions());
			Hibernate.initialize(databaseQuery.getDatabaseQueryConditions());
			responseObject = databaseQueryService.updateDatabaseQuery(databaseQuery,serverInstanceId,staffId);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_POLICY_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.DELETE_DATABASE_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public String deleteQuery(@RequestParam(value = "deleteQueryId",required = true)String queryId,
			HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = databaseQueryService.deleteQuery(queryId, staffId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	public DatabaseQuery addActionsAndConditions(DatabaseQuery databaseQuery,String actionString,String conditionString, int staffId){
		List<DatabaseQueryAction> databaseQueryActions = new ArrayList<>();
		if(StringUtils.isNotEmpty(actionString)) {
			JSONArray jsonArray = new JSONArray(actionString);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				DatabaseQueryAction databaseQueryAction = convertToDatabaseQueryAction(jsonObj,staffId);
				databaseQueryActions.add(databaseQueryAction);
			}
			databaseQuery.setDatabaseQueryActions(databaseQueryActions);
		}
		List<DatabaseQueryCondition> databaseQueryConditions = new ArrayList<>();
		if(StringUtils.isNotEmpty(conditionString)){
			JSONArray jsonArray = new JSONArray(conditionString);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				DatabaseQueryCondition databaseQueryCondition = convertToDatabaseQueryCondition(jsonObj,staffId);
				databaseQueryConditions.add(databaseQueryCondition);
			}
			databaseQuery.setDatabaseQueryConditions(databaseQueryConditions);
		}
		return databaseQuery;
	}
	
	public DatabaseQueryCondition convertToDatabaseQueryCondition(JSONObject jsonObj,int staffId){
		String fieldName = jsonObj.getString("databaseFieldName");
		String unifiedField = jsonObj.getString("unifiedField");
		String operator = jsonObj.getString("policyOperator");
		boolean databaseKey = jsonObj.getBoolean("databaseKey");
		DatabaseQueryCondition databaseQueryCondition = new DatabaseQueryCondition();
		databaseQueryCondition.setCreatedByStaffId(staffId);
		databaseQueryCondition.setCreatedDate(new Date());
		databaseQueryCondition.setLastUpdatedByStaffId(staffId);
		databaseQueryCondition.setLastUpdatedDate(new Date());
		databaseQueryCondition.setStatus(StateEnum.ACTIVE);
		databaseQueryCondition.setDatabaseFieldName(fieldName);
		databaseQueryCondition.setUnifiedField(unifiedField);
		databaseQueryCondition.setPolicyConditionOperatorEnum(operator);
		databaseQueryCondition.setDatabaseKey(databaseKey);
		return databaseQueryCondition;
	}
	
	
	
	
	public DatabaseQueryAction convertToDatabaseQueryAction(JSONObject jsonObject, int staffId){
		String fieldName = jsonObject.getString("databaseFieldName");
		String unifiedField = jsonObject.getString("unifiedField");
		DatabaseQueryAction databaseQueryAction = new DatabaseQueryAction();
		databaseQueryAction.setCreatedByStaffId(staffId);
		databaseQueryAction.setCreatedDate(new Date());
		databaseQueryAction.setLastUpdatedByStaffId(staffId);
		databaseQueryAction.setLastUpdatedDate(new Date());
		databaseQueryAction.setStatus(StateEnum.ACTIVE);
		databaseQueryAction.setDatabaseFieldName(fieldName);
		databaseQueryAction.setUnifiedField(unifiedField);
		return databaseQueryAction;
	}

	
	@PreAuthorize("hasAnyAuthority('VIEW_POLICY_CONFIGURATION','UPDATE_POLICY_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.INIT_DATABASE_QUERY_CONDITIONS, method = RequestMethod.GET)
	@ResponseBody
	public String initDatabaseQueryConditions(
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = true) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			@RequestParam(value = "queryId" , required=true)String queryId){
		List<DatabaseQueryCondition> resultList;
		Map<String, Object> row;
		List<Map<String, Object>> rowList = new ArrayList<>();
		long count = databaseQueryService.getConditionListCountByQueryId(Integer.parseInt(queryId));
		resultList  = this.databaseQueryService.getConditionPaginatedList(Integer.parseInt(queryId), eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord);
		if (resultList != null) { 
			for (DatabaseQueryCondition queryCondition : resultList) {
				row = new HashMap<>();
				row.put("fieldName",queryCondition.getDatabaseFieldName());
				row.put("operator",queryCondition.getPolicyConditionOperatorEnum());
				row.put("unifiedField",queryCondition.getUnifiedField());
				row.put("databaseKey",queryCondition.isDatabaseKey());
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_POLICY_CONFIGURATION','UPDATE_POLICY_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.INIT_DATABASE_QUERY_ACTIONS, method = RequestMethod.GET)
	@ResponseBody
	public String initDatabaseQueryActions(
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = true) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			@RequestParam(value = "queryId" , required=true)String queryId){
		List<DatabaseQueryAction> resultList;
		Map<String, Object> row;
		List<Map<String, Object>> rowList = new ArrayList<>();
		long count = databaseQueryService.getActionListCountByQueryId(Integer.parseInt(queryId));
		resultList = databaseQueryService.getActionPaginatedList(Integer.parseInt(queryId), eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord);
		if (resultList != null) { 
			for (DatabaseQueryAction queryCondition : resultList) {
				row = new HashMap<>();
				row.put("fieldName",queryCondition.getDatabaseFieldName());
				row.put("unifiedField",queryCondition.getUnifiedField());
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}
}
