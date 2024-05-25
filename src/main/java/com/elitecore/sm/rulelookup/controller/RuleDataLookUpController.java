package com.elitecore.sm.rulelookup.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.core.util.Constant;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.AutoOperationStatusEnum;
import com.elitecore.sm.common.model.AutoProcessEnum;
import com.elitecore.sm.common.model.JobActionTypeEnum;
import com.elitecore.sm.common.model.JobTypeEnum;
import com.elitecore.sm.common.model.TriggerTypeEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.job.service.JobService;
import com.elitecore.sm.policy.model.DatabaseQuery;
import com.elitecore.sm.policy.service.IDatabaseQueryService;
import com.elitecore.sm.rulelookup.model.AutoJobStatistic;
import com.elitecore.sm.rulelookup.model.AutoReloadJobDetail;
import com.elitecore.sm.rulelookup.model.LookupFieldDetailData;
import com.elitecore.sm.rulelookup.model.ReloadOptionEnum;
import com.elitecore.sm.rulelookup.model.RuleLookupTableData;
import com.elitecore.sm.rulelookup.model.ScheduleTypeEnum;
import com.elitecore.sm.rulelookup.model.SearchAutoUploadReloadDetail;
import com.elitecore.sm.rulelookup.service.IAutoJobStatisticsService;
import com.elitecore.sm.rulelookup.service.IAutoUploadConfigService;
import com.elitecore.sm.rulelookup.service.IRuleDataLookUpService;
import com.elitecore.sm.rulelookup.validator.AutoReloadCacheConfigValidator;
import com.elitecore.sm.rulelookup.validator.RuleDataLookUpValidator;
import com.elitecore.sm.scheduler.QuartJobSchedulingListener;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;
import com.elitecore.sm.trigger.service.TriggerService;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.MapCache;
import com.google.gson.JsonObject;

/**
 * 
 * @author Sagar Ghetiya
 *
 */
@Controller
public class RuleDataLookUpController extends BaseController {
	
	private final String STRPARAMCONSTANT = "strparam";
	
	private final String KEYCONSTANT = "key";
	
	protected Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private RuleDataLookUpValidator ruleLookupTableValidator;
	
	@Autowired
	private IRuleDataLookUpService ruleDataLookUpService;
	
	@Autowired
	private ServerInstanceService serverInstanceService;
	
	@Autowired
	JobService jobService;
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	private AutoReloadCacheConfigValidator autoReloadCacheConfigValidator;
	
	@Autowired
	private TriggerService triggerService;
	
	/*@Autowired
	JobService jobService;*/
	
	@Autowired
	IDatabaseQueryService databaseQueryService;
	
	@Autowired
	IAutoJobStatisticsService autoJobStatisticsService;
	
	@Autowired
	QuartJobSchedulingListener quartJobSchedulingListener;
	
	@Autowired
	private IAutoUploadConfigService autoUploadConfigService;
	
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
	

	@PreAuthorize("hasAnyAuthority('VIEW_RULE_DATA_CONFIG','UPDATE_RULE_DATA_CONFIG')")
	@RequestMapping(value = ControllerConstants.INIT_RULE_DATA_CONFIG, method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initRuleDataConfig(
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = false,defaultValue = BaseConstants.UPDATE_RULE_DATA_CONFIG)String requestActionType
			){
		ModelAndView model = new ModelAndView(
				ViewNameConstants.UPDATE_RULE_DATA_CONFIG);
			model.addObject(FormBeanConstants.RULE_DATA_TABLE_FORM_BEAN,
					(RuleLookupTableData) SpringApplicationContext
							.getBean(RuleLookupTableData.class));
			model.addObject("trueFalseEnum",
					Arrays.asList(TrueFalseEnum.values()));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
		return model;
	}
	
	
	@PreAuthorize("hasAnyAuthority('VIEW_RULE_DATA_CONFIG','UPDATE_RULE_DATA_CONFIG')")
	@RequestMapping(value = ControllerConstants.INIT_RULE_TABLE_DATA_LIST, method = RequestMethod.GET)
	@ResponseBody
	public String initRuleTableList(
			@RequestParam(value = "rows", defaultValue = "5") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = true) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			@RequestParam(value = "isSearch" ,required=false) boolean isSearch,
			@RequestParam(value = "searchName", required=false) String searchName,
			@RequestParam(value = "searchDesc", required=false) String searchDesc){
		List<RuleLookupTableData> resultList;
		Map<String, Object> row;
		List<Map<String, Object>> rowList = new ArrayList<>();
		Map<Integer, Object> viewDataSizeList = new HashMap<>();
		
		long count = ruleDataLookUpService.getAllTableListCount(isSearch,searchName,searchDesc);				
		resultList  = this.ruleDataLookUpService.getPaginatedList( eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord,searchName,searchDesc,isSearch);
		
		if (resultList != null) {
			 long viewRecordsSize = 0;			
			for (RuleLookupTableData ruleLookupTableData : resultList) {
				viewRecordsSize = ruleDataLookUpService.getSearchLookupDataListCountById(ruleLookupTableData.getViewName(), ruleLookupTableData.getId(), null);
				viewDataSizeList.put(ruleLookupTableData.getId(), viewRecordsSize );
			}
		}
		if (resultList != null) { 
			for (RuleLookupTableData ruleLookupTableData : resultList) {
				row = new HashMap<>();
				row.put("id", ruleLookupTableData.getId());
				row.put("viewName",ruleLookupTableData.getViewName());
				row.put("description",ruleLookupTableData.getDescription());
				row.put("viewDataSize", viewDataSizeList.get(ruleLookupTableData.getId()));
				row.put("viewDataDownloadLimit", MapCache.getConfigValueAsObject("LOOKUP_DOWNLOAD_LIMIT") );				
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}
	
	
	@PreAuthorize("hasAnyAuthority('CREATE_RULE_DATA_CONFIG')")
	@RequestMapping(value = ControllerConstants.CREATE_RULE_DATA_CONFIG, method = RequestMethod.POST)
	@ResponseBody
	public String createRuleDataConfig(
			@ModelAttribute(value=FormBeanConstants.RULE_DATA_TABLE_FORM_BEAN)RuleLookupTableData ruleLookupTableData,//NOSONAR
			@RequestParam(value = "fieldList", required=false)String fieldList,
			@RequestParam(value = "fileName", required=false)String fileName,
			@RequestParam(value = "isFileWithData", required=false)boolean isFileWithData,
			BindingResult result,
			HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		AjaxResponse ajaxResponse = new AjaxResponse();
		ruleLookupTableData = addFieldList(fieldList,staffId,ruleLookupTableData);
		ruleLookupTableValidator.validateRuleLookupTable(ruleLookupTableData, result, null);
		ResponseObject responseObject;	
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			responseObject = ruleDataLookUpService.createRuleLookUpTable(ruleLookupTableData,staffId);
			if(responseObject.isSuccess() && isFileWithData && fileName != null && !fileName.isEmpty()) {
				try {
					byte[] content  = Files.readAllBytes(Paths.get(servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT) + File.separator + fileName));
					MultipartFile multiPartFile = new MockMultipartFile(fileName, fileName, "text/plain", content);
					RuleLookupTableData  ruleLookupTableData2 =  (RuleLookupTableData )responseObject.getObject();
					logger.info("Table Created Successfully Now inserting data into table ");
					return uploadRuleData(ruleLookupTableData2.getId()+"", ruleLookupTableData2.getViewName(), "append", multiPartFile, request);
				} catch (Exception e) {
					logger.trace("There is some error while inserting data in table "+e);
				}
			} else if(!responseObject.isSuccess() && isFileWithData) {
				ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
				JSONArray jsonArray = new JSONArray( "["+ajaxResponse.toString() +"]" );
				return jsonArray.toString();
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	
	public RuleLookupTableData addFieldList(String fieldList,int staffId,RuleLookupTableData ruleLookupTableData){
		List<LookupFieldDetailData> lookUpFieldList = new ArrayList<>();
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(fieldList)) {
			JSONArray jsonArray = new JSONArray(fieldList);
			int strParamCount = 2;
			int keyCount = 1;
			LookupFieldDetailData lookupFieldDetailData;
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				if(!jsonObj.getBoolean("unique")){
					lookupFieldDetailData = convertToFieldDetailData(jsonObj,strParamCount++,staffId,ruleLookupTableData,false);
					lookUpFieldList.add(lookupFieldDetailData);
				} else {
					lookupFieldDetailData = convertToFieldDetailData(jsonObj,keyCount++,staffId,ruleLookupTableData,true);
					lookUpFieldList.add(lookupFieldDetailData);
				}
			}
			ruleLookupTableData.setLookUpFieldDetailData(lookUpFieldList);
		}
		return ruleLookupTableData;
	}
	
	
	public LookupFieldDetailData convertToFieldDetailData(JSONObject jsonObj,int index,int staffId,RuleLookupTableData ruleLookupTableData, boolean iskey ){
		String fieldName;
		if(!iskey){
			fieldName = STRPARAMCONSTANT+index;
		} else {
			if(index == 1){
				fieldName = STRPARAMCONSTANT+index;
			} else {
				fieldName = KEYCONSTANT+(index-1);
			}
		}
		String viewFieldName = jsonObj.getString("viewFieldName");
		String displayName = jsonObj.getString("displayName");
		boolean unique = jsonObj.getBoolean("unique");
		LookupFieldDetailData lookupFieldDetailData = new LookupFieldDetailData();
		lookupFieldDetailData.setCreatedByStaffId(staffId);
		lookupFieldDetailData.setCreatedDate(new Date());
		lookupFieldDetailData.setId(0);
		lookupFieldDetailData.setLastUpdatedByStaffId(staffId);
		lookupFieldDetailData.setLastUpdatedDate(new Date());
		lookupFieldDetailData.setFieldName(fieldName);
		lookupFieldDetailData.setIsUnique(unique);
		lookupFieldDetailData.setViewFieldName(viewFieldName);
		lookupFieldDetailData.setDisplayName(displayName);
		lookupFieldDetailData.setRuleLookUpTable(ruleLookupTableData);
		return lookupFieldDetailData;
	}
	
	
	@PreAuthorize("hasAnyAuthority('VIEW_RULE_DATA_CONFIG','UPDATE_RULE_DATA_CONFIG')")
	@RequestMapping(value = ControllerConstants.INIT_RULE_FIELD_LIST, method = RequestMethod.GET)
	@ResponseBody
	public String initRuleFieldList(@RequestParam(value = "rows", defaultValue = "5") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = true) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			@RequestParam(value = "tableId" , required=true)String tableId){
		List<LookupFieldDetailData> resultList;
		Map<String, Object> row;
		List<Map<String, Object>> rowList = new ArrayList<>();
		long count = ruleDataLookUpService.getFieldListCountByTableId(Integer.parseInt(tableId));
		resultList  = this.ruleDataLookUpService.getFieldListPaginatedList(Integer.parseInt(tableId), eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord);
		if (resultList != null) { 
			for(LookupFieldDetailData lookupFieldDetailData : resultList){
				row = new HashMap<>();
				row.put("viewFieldName",lookupFieldDetailData.getViewFieldName());
				row.put("displayName",lookupFieldDetailData.getDisplayName());
				row.put("unique",lookupFieldDetailData.getIsUnique());
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}
	
	
	@PreAuthorize("hasAnyAuthority('DELETE_RULE_DATA_CONFIG')")
	@RequestMapping(value = ControllerConstants.DELETE_RULE_LOOKUP_TABLE, method = RequestMethod.POST)
	@ResponseBody
	public String deleteRuleLookupTable(@RequestParam(value = "tableId",required = true)String tableIds,
			HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = ruleDataLookUpService.deleteRuleLookupTables(tableIds, staffId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	
	@PreAuthorize("hasAnyAuthority('UPDATE_RULE_DATA_CONFIG')")
	@RequestMapping(value = ControllerConstants.UPDATE_RULE_DATA_CONFIG, method = RequestMethod.POST)
	@ResponseBody
	public String updateRuleDataConfig(@ModelAttribute(value=FormBeanConstants.RULE_DATA_TABLE_FORM_BEAN)RuleLookupTableData ruleLookupTableData,//NOSONARs
			@RequestParam(value = "fieldList", required=false)String fieldList,
			BindingResult result,
			HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		
		ruleLookupTableData = addFieldList(fieldList,staffId,ruleLookupTableData);
		ruleLookupTableValidator.validateRuleLookupTable(ruleLookupTableData, result, null);
		
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			/** ruleLookupTableData = addFieldList(fieldList,staffId,ruleLookupTableData); **/
			responseObject = ruleDataLookUpService.updateRuleLookupTable(ruleLookupTableData,staffId);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	
	@PreAuthorize("hasAnyAuthority('UPDATE_RULE_DATA_CONFIG')")
	@RequestMapping(value = ControllerConstants.DOWNLOAD_SAMPLE_CSV_FILE, method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView downloadSampleFile(@RequestParam(value = "uploadTableId",required = true)String tableId,
									 @RequestParam(value = "uploadViewName",required = true)String tableName,
									 HttpServletResponse response){
		response = getSampleFile(Integer.parseInt(tableId),tableName,response);
		return null;
	}
	
	
	public HttpServletResponse getSampleFile(int tableId,String tableName,HttpServletResponse response){
		String fileName = "rulelookup_"+tableName+".csv";
		response.setContentType("application/csv");
		response.reset();
		String headerKey = "Content-Disposition";
	    String headerValue = String.format("attachment; filename=\"%s\"",fileName);
	    response.setHeader(headerKey, headerValue);
	    try{
	    	ServletOutputStream outputStream = response.getOutputStream();
	    	String header = ruleDataLookUpService.getCsvHeader(tableId);
	    	outputStream.print(header);
	    	outputStream.flush();
	    	outputStream.close();
	    	return response;
	    }catch(Exception e){
	    	logger.trace("Error occured while downloading sample file", e);
	    	return null;
	    }
	}
	
	
	@PreAuthorize("hasAnyAuthority('UPDATE_RULE_DATA_CONFIG')")
	@RequestMapping(value = ControllerConstants.UPLOAD_RULE_DATA, method = RequestMethod.POST)
	@ResponseBody
	public  String uploadRuleData(
			@RequestParam(value = "uploadTableId",required = true)String tableId,
			@RequestParam(value = "uploadViewName",required = true)String tableName,
			@RequestParam(value = "datawrittenmode",required = true)String mode,
			@RequestParam(value = "file", required = true) MultipartFile multipartFile,
			HttpServletRequest request ) throws SMException{
			AjaxResponse ajaxResponse = new AjaxResponse();
			ResponseObject responseObject;
			String duplicatedata =""; 
			String responseStr ="";
			Timestamp lookupDataReloadFromDate = new Timestamp(new Date().getTime());
			
			int staffId = eliteUtils.getLoggedInStaffId(request);
			
			List<AutoReloadJobDetail> autoReloadJobDetailList = new ArrayList<AutoReloadJobDetail>();
			 
			if (!multipartFile.isEmpty() ){
				logger.debug("File object found.Going to insert data");
				// check if file is a valid csv file
				if(BaseConstants.CSV_FILE_CONTENT.equalsIgnoreCase(multipartFile.getContentType()) || "application/octet-stream".equalsIgnoreCase(multipartFile.getContentType()) || multipartFile.getOriginalFilename().endsWith(".csv")){	
					logger.debug("Valid CSV file found");
					// Check if the file headers match
					String repositoryPath = servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT)+File.separator;
					File lookupDataFile = new File(repositoryPath + multipartFile.getOriginalFilename());
					try{
						multipartFile.transferTo(lookupDataFile);
						/** responseObject = this.ruleDataLookUpService.insertDataIntoLookupTable(lookupDataFile,repositoryPath,tableId , mode ); **/
						
						RuleLookupTableData lookUpData = ruleDataLookUpService.getLookUpTableData( tableId );
						String header = ruleDataLookUpService.getCsvHeader(Integer.parseInt(tableId));
						
						responseObject = this.ruleDataLookUpService.insertDataIntoLookupView( header, lookUpData , lookupDataFile,repositoryPath,tableId , mode , staffId,null);
						
						if(responseObject.isSuccess()){
							ruleDataLookUpService.reloadImmediateUploadDataInView(tableName,lookupDataReloadFromDate);
						}
						
						Object duplicateSummary[] = responseObject.getArgs();
						 
						 if( duplicateSummary!=null && duplicateSummary.length > 0){
						   duplicatedata =  ( String ) duplicateSummary[0];
						 }
						ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
						
						JSONArray jsonArray = new JSONArray( "["+ajaxResponse.toString() +"]" );
						JSONArray jsonArrayDupliate = new JSONArray("["+ duplicatedata +"]");
						for(int i = 0; i < jsonArrayDupliate.length(); i++) { 
							JSONObject jsobj = new JSONObject();
					  		jsobj = jsonArrayDupliate.getJSONObject( i );							
							jsonArray.put( jsobj );
						}
						responseStr = jsonArray.toString();
						
					}catch(Exception e){
						logger.trace("Problem occured while uploading lookup data file",e);
					}
				}else{
					ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
					ajaxResponse.setResponseMsg("Please upload a valid CSV file");
				}
			}
		/** return ajaxResponse.toString() + duplicatedata.toString() ; **/
			return responseStr;
	}
	
	
	@PreAuthorize("hasAnyAuthority('VIEW_RULE_DATA_CONFIG')")
	@RequestMapping(value = ControllerConstants.DOWNLOAD_LOOKUP_DATA_FILE, method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView downloadRuleLookupFile(@RequestParam(value = "downloadTableId",required = true)String tableId,
			@RequestParam(value = "downloadTableName",required = true)String tableName,
			@RequestParam(value = "includeData",required = false)boolean includeData,
			 HttpServletResponse response){
		String fileName = "rulelookup_"+tableName+".csv";
		response.setContentType("application/csv");
		response.reset();
		String headerKey = "Content-Disposition";
	    String headerValue = String.format("attachment; filename=\"%s\"",fileName);
	    response.setHeader(headerKey, headerValue);
	    try{
	    	ServletOutputStream outputStream = response.getOutputStream();
	    	/** String header = ruleDataLookUpService.getCsvHeader(Integer.parseInt(tableId));
	    	outputStream.println(header); **/
	    	List<String> tableFieldList = ruleDataLookUpService.getLookupTableFields(Integer.parseInt(tableId));
	    	int fieldListLength = 0;
	    	if(!CollectionUtils.isEmpty(tableFieldList)) {
	    		fieldListLength = tableFieldList.size();
	    		for(int i = 0; i < fieldListLength; i++) {
	    			String field = tableFieldList.get(i);
	    			if(field != null) {
	    				outputStream.print(field);
	    				if(!(i == fieldListLength-1)) {
	    					outputStream.print(",");
	    				}
	    			}
	    		}
	    		outputStream.println();
	    	}
	    	if(includeData) {
	    		List<List<String>> fieldDataList = ruleDataLookUpService.getLookupTableData(Integer.parseInt(tableId));
		    	if(!CollectionUtils.isEmpty(fieldDataList)){
		    		StringBuffer sb = new StringBuffer();
		    		for(List<String> fieldData : fieldDataList){
		    			if(!CollectionUtils.isEmpty(fieldData)) {
		    				int fieldDataLength = fieldData.size();
		    				if(fieldDataLength >= fieldListLength) {
		    					for(int i = 0; i < fieldListLength; i++) {
		    						String myData = fieldData.get(i);
		    						if (myData != null && !myData.isEmpty() && myData.contains(",")) {
		    							sb.append('"').append(myData).append('"');
		    							myData = sb.toString();
		    							sb.setLength(0);
		    						}
		    						outputStream.print(myData);
		    						if(!(i == fieldListLength-1)) {
		    							outputStream.print(",");
		    						}
		    					}
		    					outputStream.println();
		    				}
		    			}
		    		}
		    	}
	    	}
	    	outputStream.flush();
	    	outputStream.close();
	    }catch(Exception e){
	    	logger.trace("Error occured while downloading sample file", e);
	    }
	    return null;
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_RULE_DATA_CONFIG')")
	@RequestMapping(value = ControllerConstants.INIT_LOOKUP_DATA_CONFIG, method = RequestMethod.POST)
	public ModelAndView initRuleLookupDataConfig(
			@RequestParam(value = "viewLookupTableId",required = true)String tableId, 
			@RequestParam(value = "viewLookupTablename",required = true)String tablename,
			@RequestParam(value = "viewLookupTableDescription",required = false)String tableDescription,
			HttpServletResponse response){
		ModelAndView model = new ModelAndView(ViewNameConstants.UPDATE_RULE_DATA_CONFIG);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.VIEW_RULE_DATA_RECORDS);
		List<String> tableFieldList = ruleDataLookUpService.getLookupTableFields(Integer.parseInt(tableId));
		JSONArray jAllGrpAttrArr = new JSONArray();
		if (tableFieldList != null) {
			for (String tableField : tableFieldList) {
				String viewTableField =tableField.endsWith(BaseConstants.TABLE_FIELD_UNIQUE_SEPERATOR) ?tableField.substring(0,tableField.indexOf(BaseConstants.TABLE_FIELD_UNIQUE_SEPERATOR)).toUpperCase():tableField.toUpperCase();
				jAllGrpAttrArr.put(viewTableField);
			}
		}
		
		String uniquetableFieldList = ruleDataLookUpService.getUniqueFieldsByTableId(Integer.parseInt(tableId));
		
		model.addObject("tableId", tableId );
		model.addObject("tablename", tablename );
		model.addObject("tableDescription", tableDescription );
		model.addObject("tableFieldListJson", jAllGrpAttrArr);
		model.addObject("uniqueFields", uniquetableFieldList);
	    return model;
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_RULE_DATA_CONFIG')")
	@RequestMapping(value = ControllerConstants.INIT_LOOKUP_DATA_LIST, method = RequestMethod.GET)
	@ResponseBody
	public String initRuleLookupDataList(
			@RequestParam(value = "isSearch" ,required=false) boolean isSearch,
			@RequestParam(value = "searchQuery" , required=false) String searchQuery,
			@RequestParam(value = "rows", defaultValue = "5") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = false) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			@RequestParam(value = "id",required = false)int tableId,
			@RequestParam(value = "tableviewname" , required=false) String tableviewname) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject = new ResponseObject();
		List<Map<String, Object>> searchrowList ;
		long count = ruleDataLookUpService.getSearchLookupDataListCountById( tableviewname ,tableId ,searchQuery);
		
		searchrowList = this.ruleDataLookUpService.getSearchLookupDataPaginatedList(eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), 
				limit, sidx, sord, tableId, tableviewname , searchQuery , responseObject);
		
		
		if(!responseObject.isSuccess()){			
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			ajaxResponse.setResponseMsg("Enter valid input search text");
			return ajaxResponse.toString();
		}
		
		
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, searchrowList).getJsonString();
	}
		
	@PreAuthorize("hasAnyAuthority('UPDATE_RULE_DATA_CONFIG')")
	@RequestMapping(value = "downloadlookupduplicatedata", method = RequestMethod.POST)
	@ResponseBody
	public  ModelAndView downloadDuplicateLookUpData(		//NOSONAR	
			@RequestParam(value = "downloadFileName",required = true)String downloadFileName ,
			@RequestParam(value = "downloadFileType",required = true)String downloadFileType ,
			HttpServletResponse response ) throws SMException{//NOSONAR	
		
			String filepath=null;
			try{
				filepath = downloadFileName.split(downloadFileType + File.separator)[1];
			}
			catch(Exception e){
				logger.trace("Invalid File Name ", e);
				return null;
			}
			response.setContentType("application/csv");
			response.reset();
			String headerKey = "Content-Disposition";
		    String headerValue = String.format("attachment; filename=\"%s\"", filepath );
		    response.setHeader(headerKey, headerValue);
		    
		    try( BufferedReader br = new BufferedReader(new FileReader(  downloadFileName )) ){
		    	
		    	ServletOutputStream outputStream = response.getOutputStream();		    	
		    	
			    String	line = br.readLine();
			     
			    	while(line != null) {		    						 
		    	 			outputStream.println( line );
		    	 			line = br.readLine();		 
		    	 	}
			    
		    	outputStream.flush();
		    	outputStream.close();
		    }catch(Exception e){
		    	logger.trace("Error occured while downloading duplicate data file", e);
		    }
		    return null;
	}
	
	@PreAuthorize("hasAnyAuthority('DELETE_RULE_DATA_CONFIG')")
	@RequestMapping(value = ControllerConstants.DELETE_RULE_LOOKUP_TABLE_RECORDS, method = RequestMethod.POST)
	@ResponseBody
	public String deleteRuleLookupTableRecords(
			@RequestParam(value = "recordIds",required = true)String recordIds,
			@RequestParam(value = "viewName",required = true)String viewName,
			HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = ruleDataLookUpService.deleteRuleLookupTableRecords(recordIds, viewName);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_RULE_DATA_CONFIG')")
	@RequestMapping(value = ControllerConstants.CREATE_RULE_LOOKUP_RECORD, method = RequestMethod.POST)
	@ResponseBody
	public String createRuleLookupRecord(
			@RequestParam(value = "lookupRecord",required = true)String lookupRecord,
			@RequestParam(value = "viewName",required = true)String viewName,
			@RequestParam(value = "tableId",required = true)String tableId,
			HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = new ResponseObject();
		Timestamp lookupDataReloadFromDate = new Timestamp(new Date().getTime());
		JSONObject jsonObj= new JSONObject( lookupRecord );
		Iterator keys = jsonObj.keys();
		Map<String,String> lookUpdata = new HashMap<>();
		while (keys.hasNext()){
		    String key = (String) keys.next();
		    lookUpdata.put(key, jsonObj.get(key).toString() );
		}
		responseObject = ruleDataLookUpService.createRuleLookupRecord(viewName, lookUpdata, staffId, Integer.parseInt(tableId));
		if(responseObject.isSuccess()){
			ruleDataLookUpService.reloadImmediateUploadDataInView(viewName,lookupDataReloadFromDate);
		}	
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();	
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_RULE_DATA_CONFIG')")
	@RequestMapping(value = ControllerConstants.UPDATE_RULE_LOOKUP_RECORD, method = RequestMethod.POST)
	@ResponseBody
	public String updateRuleLookupRecord(
			@RequestParam(value = "RECORDROWID",required = true)String recordRowId,
			@RequestParam(value = "lookupRecord",required = true)String lookupRecord,
			@RequestParam(value = "viewName",required = true)String viewName,
			HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = new ResponseObject();
		Timestamp lookupDataReloadFromDate = new Timestamp(new Date().getTime());
		JSONObject jsonObj= new JSONObject( lookupRecord );
		Iterator keys = jsonObj.keys();
		Map<String,String> lookUpdata = new HashMap<>();
		while (keys.hasNext()){
		    String key = (String) keys.next();
		    lookUpdata.put(key, jsonObj.get(key).toString() );
		}
		lookUpdata.put("RECORDROWID", recordRowId);
		responseObject = ruleDataLookUpService.updateRuleLookupRecord(viewName, lookUpdata, staffId);
		if(responseObject.isSuccess()){
			ruleDataLookUpService.reloadImmediateUploadDataInView(viewName,lookupDataReloadFromDate);
		}
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();	
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_RULE_DATA_CONFIG')")
	@RequestMapping(value = "downloadviewlookuptabledata", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView downloadRuleLookupViewFile(@RequestParam(value = "downloadTableId",required = true)String tableId,
			@RequestParam(value = "downloadTableName",required = true)String tableName,
			@RequestParam(value = "viewdataidlist",required = true)String searchQuery,
			 HttpServletResponse response){
		String fileName = "rulelookup_"+tableName+".csv";
		response.setContentType("application/csv");
		response.reset();
		String headerKey = "Content-Disposition";
	    String headerValue = String.format("attachment; filename=\"%s\"",fileName);
	    response.setHeader(headerKey, headerValue);
	    try{
	    	ServletOutputStream outputStream = response.getOutputStream();
	    	
	    	List<String> tableFieldList = ruleDataLookUpService.getLookupTableFields(Integer.parseInt(tableId));
	    	List<String> tableViewFieldList = new ArrayList<>();
	    	
	    	int fieldListLength = 0;
	    	if(!CollectionUtils.isEmpty(tableFieldList)) {
	    		fieldListLength = tableFieldList.size();
	    		for(int i = 0; i < fieldListLength; i++) {
	    			String field = tableFieldList.get(i);
	    			tableViewFieldList.add( field.toUpperCase() );
	    			if(field != null) {
	    				outputStream.print(field);
	    				if(!(i == fieldListLength-1)) {
	    					outputStream.print(",");
	    				}
	    			}
	    		}
	    		outputStream.println();
	    	}
	    	
	    	ResponseObject responseObject = new ResponseObject();
	    	List<Map<String,Object>> searchLookUpDataList =null;
	    	searchLookUpDataList = ruleDataLookUpService.getLookupViewData(Integer.parseInt(tableId), tableName, searchQuery , responseObject);
	    	Iterator<Map<String,Object>> it = searchLookUpDataList.iterator();
			while (it.hasNext()) {
				Map<String,Object> map = it.next();				 				
				int columnCount = 0;
				StringBuffer sb = new StringBuffer();
					for(int i = 0; i < tableViewFieldList.size(); i++) {
		    			String field = tableViewFieldList.get(i);
				    	++columnCount;
				    	if(field.endsWith(BaseConstants.TABLE_FIELD_UNIQUE_SEPERATOR))
				    		field = field.replace(BaseConstants.TABLE_FIELD_UNIQUE_SEPERATOR, "");
				    	String myData = map.get(field).toString();
						if (myData != null && !myData.isEmpty() && myData.contains(",")) {
							sb.append('"').append(myData).append('"');
							myData = sb.toString();
							sb.setLength(0);
						}
				    	outputStream.print(myData);
				    	if(!( columnCount == tableFieldList.size())) {
							outputStream.print(",");
						}				    
				}
				outputStream.println();				 
			}	    	
	    	outputStream.flush();
	    	outputStream.close();
	    }catch(Exception e){
	    	logger.trace("Error occured while downloading sample file", e);
	    }
	    return null;
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_AUTO_RELOAD_CACHE')")
	@RequestMapping(value = ControllerConstants.INIT_AUTO_RELOAD_CACHE_CONFIG, method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initAutoReloadCacheConfig(
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = false,defaultValue = BaseConstants.AUTO_RELOAD_CACHE_CONFIG)String requestActionType
			){
		ModelAndView model = new ModelAndView(ViewNameConstants.UPDATE_RULE_DATA_CONFIG);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
		List<ServerInstance> serverInstanceList = serverInstanceService.getServerInstanceList();
		model.addObject(BaseConstants.SERVERINSTANCE_LIST, serverInstanceList);
		model.addObject("SCHEDULE_TYPE", ScheduleTypeEnum.values());
		model.addObject("RELOAD_OPTIONS", ReloadOptionEnum.values());
		return model;
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_AUTO_RELOAD_CACHE')")
	@RequestMapping(value = ControllerConstants.INIT_AUTO_RELOAD_CACHE_CONFIG_LIST, method = RequestMethod.GET)
	@ResponseBody
	public String initAutoReloadCacheConfigList(
			@RequestParam(value = "rows", defaultValue = "5") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = true) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			@RequestParam(value = "isSearch" , required=false) boolean isSearch,
			@RequestParam(value = "searchName" , required=false) String searchName,
			@RequestParam(value = "searchServerInstance" , required=false) String searchServerInstance,
			@RequestParam(value = "searchDBQuery" , required=false) String searchDBQuery){
		List<AutoReloadJobDetail> resultList;
		Map<String, Object> row;
		List<Map<String, Object>> rowList = new ArrayList<>();
		long count = ruleDataLookUpService.getAutoReloadCacheCount(isSearch,searchName,searchServerInstance,searchDBQuery);
		resultList  = this.ruleDataLookUpService.getAutoReloadCachePaginatedList(eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord,isSearch,searchName,searchServerInstance,searchDBQuery);
		if (resultList != null) { 
			for(AutoReloadJobDetail lookupFieldDetailData : resultList){
				row = new HashMap<>();
				row.put("id",lookupFieldDetailData.getId());
				row.put("viewName",ruleDataLookUpService.getLookUpTableData(lookupFieldDetailData.getRuleLookupTableData().getId()+"").getViewName());
				row.put("serverInstance",serverInstanceService.getServerInstance(lookupFieldDetailData.getServerInstance().getId()).getName());
				String dbQueryIdList[] = lookupFieldDetailData.getDatabaseQueryList().split(",");
				StringBuffer dbQueryList = new StringBuffer();
				List <DatabaseQuery> databaseQueryList = databaseQueryService.getAllQueriesByServerId(lookupFieldDetailData.getServerInstance().getId());
				for(String dbQueryName : dbQueryIdList){
					for(DatabaseQuery databaseQuery : databaseQueryList){
						if(String.valueOf(databaseQuery.getAlias()).equals(dbQueryName)){
							if(org.apache.commons.lang3.StringUtils.isNotBlank(dbQueryList)){
								dbQueryList.append(",");
							}
							dbQueryList = dbQueryList.append(databaseQuery.getAlias());//NOSONAR
						}
					}
				}
				row.put("dbQuery",dbQueryList.toString());
				row.put("reloadSchedule",lookupFieldDetailData.getScheduleType().toString());
				row.put("reloadOptions",lookupFieldDetailData.getReloadOptions().toString());
				row.put("tableId",lookupFieldDetailData.getRuleLookupTableData().getId());
				CrestelSMTrigger trigger = ruleDataLookUpService.getScheduler(lookupFieldDetailData.getId());
				if(trigger!=null){
					row.put("scheduler",trigger.getTriggerName());
					row.put("triggerId",trigger.getID());
				}
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_AUTO_RELOAD_CACHE')")
	@RequestMapping(value = "createAutoReloadCacheConfig", method = RequestMethod.POST)
	@ResponseBody public  String createAutoReloadCacheConfig(
			@RequestParam(value="triggerId") String triggerId,
			@ModelAttribute AutoReloadJobDetail autoReloadCache,//NOSONAR
			BindingResult result,HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject;
		responseObject = triggerService.getTriggerById(Integer.parseInt(triggerId));
		CrestelSMJob job = new CrestelSMJob();
		CrestelSMTrigger trigger = (CrestelSMTrigger) responseObject.getObject();
		job.setTrigger(trigger);
		autoReloadCache.setScheduler(job);
		autoReloadCacheConfigValidator.validateAutoReloadCache(autoReloadCache, result, null, null, false);
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			Map<String, String> errorMsgs = new HashMap<>();
			for(FieldError error:result.getFieldErrors()){
				errorMsgs.put(error.getField(),error.getDefaultMessage());
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			responseObject = ruleDataLookUpService.createAutoReloadCache(autoReloadCache,staffId);
			if(job!=null && job.getJobType()!=null && !job.getJobType().equals(ScheduleTypeEnum.Immediate.toString())){
				quartJobSchedulingListener.createAndScheduleJob(job);
				job.setLastRunTime(null);
				jobService.merge(job);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_AUTO_RELOAD_CACHE')")
	@RequestMapping(value = "updateAutoReloadCacheConfig", method = RequestMethod.POST)
	@ResponseBody public  String updateAutoReloadCacheConfig(
			@RequestParam(value="triggerId") String triggerId,
			@ModelAttribute AutoReloadJobDetail autoReloadCache,//NOSONAR
			BindingResult result,HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject = triggerService.getTriggerById(Integer.parseInt(triggerId));
		CrestelSMJob job = null;
		if(responseObject!=null && autoReloadCache.getScheduleType().toString().equalsIgnoreCase(ScheduleTypeEnum.Schedule.toString())){
			job = ruleDataLookUpService.getJobByJobDetailId(autoReloadCache.getId());
			if(job==null){
				job = new CrestelSMJob();
				CrestelSMTrigger trigger = (CrestelSMTrigger) responseObject.getObject();
				job.setTrigger(trigger);
				job.setJobName("AUTOREPROCESS_"+autoReloadCache.getRuleLookupTableData().getId()+"_job_"+UUID.randomUUID());
				job.setDescription("AUTOREPROCESS_"+autoReloadCache.getRuleLookupTableData().getId()+"_job");
				job.setParentTrigger("T"+job.getTrigger().getID());
				job.setOriginalTrigger("JT");
				job.setJobType(JobTypeEnum.AutoReloadCache.name());
				jobService.save(job);
				job.setOriginalTrigger("JT"+job.getID());
				jobService.update(job);
				autoReloadCache.setScheduler(job);
			}else{
				CrestelSMTrigger trigger = (CrestelSMTrigger) responseObject.getObject();
				job.setTrigger(trigger);
				autoReloadCache.setScheduler(job);
			}
			
		}
		autoReloadCacheConfigValidator.validateAutoReloadCache(autoReloadCache, result, null, null, false);
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			Map<String, String> errorMsgs = new HashMap<>();
			for(FieldError error:result.getFieldErrors()){
				errorMsgs.put(error.getField(),error.getDefaultMessage());
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			responseObject = ruleDataLookUpService.updateAutoReloadCache(autoReloadCache,staffId);
			if(job!=null && job.getJobType()!=null && !job.getJobType().equals(ScheduleTypeEnum.Immediate.toString())){
				quartJobSchedulingListener.updateAndRescheduleJob(job);
				jobService.merge(job);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_AUTO_RELOAD_CACHE')")
	@RequestMapping(value = "deleteAutoReloadCacheConfig", method = RequestMethod.POST)
	@ResponseBody public  String deleteAutoReloadCacheConfig(
			@RequestParam(value="id") String ids,HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = ruleDataLookUpService.deleteMultipleAutoReloadCache(ids, staffId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_AUTO_RELOAD_CACHE')")
	@RequestMapping(value = ControllerConstants.GET_RULE_LOOKUP_TABLE_LIST, method = RequestMethod.POST)
	@ResponseBody public  String getRuleLookupDataTableName(HttpServletRequest request){
		ResponseObject	responseObject = ruleDataLookUpService.getRuleLookUpDataTableNameList();
		if(responseObject.isSuccess()){
			Map<String, Object> row;
			List<Map<String, Object>> rowList = new ArrayList<>();
			@SuppressWarnings("unchecked")
			List<RuleLookupTableData> ruleLookupTableDataList = (List<RuleLookupTableData>) responseObject.getObject(); 
			if(ruleLookupTableDataList != null){
				for(RuleLookupTableData ruleLookupTableData : ruleLookupTableDataList){
					row = new HashMap<String, Object>();
					row.put("viewName",ruleLookupTableData.getViewName());
					row.put("id",ruleLookupTableData.getId());
					rowList.add(row);
				}
				responseObject.setObject(rowList);
				responseObject.setSuccess(true);
			}
		}
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_AUTO_UPLOAD_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.INIT_AUTO_UPLOAD_CONFIG, method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initAutoUploadConfig(
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = false,defaultValue = BaseConstants.AUTO_UPLOAD_CONFIG)String requestActionType
			){
		ModelAndView model = new ModelAndView(ViewNameConstants.UPDATE_RULE_DATA_CONFIG);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
		List<ServerInstance> serverInstanceList = serverInstanceService.getServerInstanceList();
		model.addObject(BaseConstants.SERVERINSTANCE_LIST, serverInstanceList);
		model.addObject("SCHEDULE_TYPE", ScheduleTypeEnum.values());
		model.addObject("RELOAD_OPTIONS", ReloadOptionEnum.values());
		return model;
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_AUTO_JOB_PROCESS')")
	@RequestMapping(value = ControllerConstants.INIT_VIEW_AUTO_UPLOAD_RELOAD_STATUS, method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView viewAutoUploadReloadStatus(
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = false,defaultValue = BaseConstants.AUTO_UPLOAD_RELOAD_STATUS)String requestActionType ){
		ModelAndView model = new ModelAndView(ViewNameConstants.UPDATE_RULE_DATA_CONFIG);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
		List<ServerInstance> serverInstanceList = serverInstanceService.getServerInstanceList();
		model.addObject(BaseConstants.SERVERINSTANCE_LIST, serverInstanceList);
		model.addObject("SCHEDULE_TYPE", ScheduleTypeEnum.values());
		model.addObject("RELOAD_OPTIONS", ReloadOptionEnum.values());
		model.addObject("AUTO_OPERATION_STATUS_ENUM", AutoOperationStatusEnum.values());
		model.addObject("PROCESS_ENUM",AutoProcessEnum.values());
		model.addObject("SCHEDULE_TYPE_ENUM",ScheduleTypeEnum.values());
		model.addObject("TRIGGER_TYPE_ENUM",TriggerTypeEnum.values());
		model.addObject("schedulerNameList",triggerService.getAllTriggerList());
		model.addObject("jobActionTypeEnum",JobActionTypeEnum.values());
		model.addObject("tableList", (List<RuleLookupTableData>)ruleDataLookUpService.getRuleLookUpDataTableNameList().getObject());
		return model;
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_AUTO_JOB_PROCESS')")
	@RequestMapping(value = ControllerConstants.VIEW_AUTO_JOB_LIST , method = RequestMethod.GET)
	@ResponseBody
	public String viewAutoJobStatastics(SearchAutoUploadReloadDetail searchAutoUploadReloadDetail, 
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = false,defaultValue = BaseConstants.AUTO_UPLOAD_RELOAD_STATUS)String requestActionType,
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = true) String sortColumn, 
			@RequestParam(value = "sord", required = true) String sortOrder,
			@RequestParam(value = "isSearch", required = false) boolean isSearch){
		List<AutoJobStatistic> resultList;
		Map<String, Object> row;
		List<Map<String, Object>> rowList = new ArrayList<>();
		Map<String,Object> conditionListAndCount = null;
		try {
			conditionListAndCount = autoJobStatisticsService.getAutoJobProcessCount(isSearch, searchAutoUploadReloadDetail);
			resultList  = this.autoJobStatisticsService.getAutoJobProcessPaginatedList(eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount((long)conditionListAndCount.get("count"), limit)), limit, sortColumn, sortOrder,isSearch,searchAutoUploadReloadDetail,(List<Criterion>)conditionListAndCount.get("conditions"));
			if (resultList != null) { 
				for(AutoJobStatistic autoJobStatistic : resultList){
				row = new HashMap<>();
				row.put("id",autoJobStatistic.getId());
				row.put("batchId",autoJobStatistic.getId());
				row.put("viewName",autoJobStatistic.getTableName());
				row.put("jobProcess", autoJobStatistic.getProcessType());
				row.put("scheduleType", autoJobStatistic.getSchedulerType());
				row.put("schedulerName", autoJobStatistic.getSchedulerName());
				row.put("scheduler", autoJobStatistic.getSchedulerName());
				row.put("jobStatus", autoJobStatistic.getJobStatus());
				row.put("fromDate", DateFormatter.dateToString(autoJobStatistic.getExecutionStart(), BaseConstants.DATE_FORMAT_FOR_JOBSTATISTIC));
				row.put("toDate", DateFormatter.dateToString(autoJobStatistic.getExecutionEnd(), BaseConstants.DATE_FORMAT_FOR_JOBSTATISTIC));
				if(autoJobStatistic.getReason() != null && ! autoJobStatistic.getReason().isEmpty())
					row.put("reason",autoJobStatistic.getReason());					
				if(autoJobStatistic.getProcessType().equals(JobTypeEnum.AutoUpload.name())){
					//AutoUploadJobDetail autoUploadJobDetail = autoUploadConfigService.getAutoUploadByJobId(autoJobStatistic.getJobId());
					row.put("sourceDirectory", autoJobStatistic.getSourceDirectory());
					row.put("jobAction", autoJobStatistic.getAction());
					row.put("fileTotal", autoJobStatistic.getSuccessFileCount() + autoJobStatistic.getFailedFileCount());
					row.put("fileSuccess", autoJobStatistic.getSuccessFileCount());
					row.put("fileFailed", autoJobStatistic.getFailedFileCount());
					row.put("recordTotal", autoJobStatistic.getSuccessRecordCount() + autoJobStatistic.getFailedRecordCount() + autoJobStatistic.getDuplicateRecordCount());
					row.put("recordSuccess", autoJobStatistic.getSuccessRecordCount());
					row.put("duplicateRecord", autoJobStatistic.getDuplicateRecordCount());
					row.put("recordFailed", autoJobStatistic.getFailedRecordCount());
					row.put("filePrefix", autoJobStatistic.getFilePrefix());
					row.put("fileContains", autoJobStatistic.getFileContains());
				}
				if(autoJobStatistic.getProcessType().equals(JobTypeEnum.AutoReloadCache.name())){
					row.put("reloadQueryStatus", autoJobStatistic.getReloadDbQueryStatus());
					row.put("reloadRecordCount", autoJobStatistic.getReloadRecordCount());
					row.put("reloadQuery", autoJobStatistic.getDbQuery());
					row.put("serverInstance",autoJobStatistic.getServerInstance());
				}
				rowList.add(row);
				}
			}
		} catch (Exception e) {
			logger.trace("Error occured while getting record", e);
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount((long)conditionListAndCount.get("count"), limit), currentPage, (int)(long)conditionListAndCount.get("count"), rowList).getJsonString();	
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_RULE_DATA_CONFIG')")
	@RequestMapping(value = ControllerConstants.UPLOAD_RULELOOKUP_DATA_FILE, method = RequestMethod.POST)
	@ResponseBody
	public String uploadRuleLookupDataFile(@RequestParam(value = "file", required = true) MultipartFile multipartFile,
			HttpServletRequest request) throws SMException {
		logger.info("Uploaded file name is "+multipartFile.getName());
		ResponseObject responseObject = new ResponseObject();
		responseObject.setSuccess(true);
		JSONArray jsonArray = new JSONArray();
		if (!multipartFile.isEmpty()) {
			String repositoryPath = servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT) + File.separator;
			File lookupDataFile = new File(repositoryPath + multipartFile.getOriginalFilename());
			try {
				multipartFile.transferTo(lookupDataFile);
			} catch (IllegalStateException | IOException e) {
				logger.trace("Problem occured while uploading lookup data file", e);
				responseObject.setObject(null);
				responseObject.setSuccess(false);
			}
			if (responseObject.isSuccess()) {
				try (BufferedReader br = new BufferedReader(new FileReader(lookupDataFile))) {
					String line = br.readLine();
					if (line != null && !line.isEmpty()) {
						logger.info("File Header Found :: "+line);
						String[] headerArray = line.split(",");
						for (String field : headerArray) {
							JsonObject jsonObject = new JsonObject();
							String actualFieldName = field.endsWith(BaseConstants.TABLE_FIELD_UNIQUE_SEPERATOR) ? field.substring(0, field.indexOf(BaseConstants.TABLE_FIELD_UNIQUE_SEPERATOR)) : field;
							jsonObject.addProperty("fieldName", actualFieldName);
							jsonObject.addProperty("unique", field.endsWith(BaseConstants.TABLE_FIELD_UNIQUE_SEPERATOR));
							jsonArray.put(jsonObject);
						}
						JsonObject jsonObject = new JsonObject();
						jsonObject.addProperty("fileName", lookupDataFile.getName());
						jsonObject.addProperty("isFileWithData", (br.readLine() != null));//NOSONAR
						jsonArray.put(jsonObject);
						responseObject.setObject(jsonArray);
						responseObject.setSuccess(true);
					}
				} catch (Exception e) {
					logger.trace("Problem occured while uploading lookup data file", e);
					responseObject.setObject(null);
					responseObject.setSuccess(false);
				}
			}
		}
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
}