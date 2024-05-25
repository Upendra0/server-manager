package com.elitecore.sm.mis.controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.core.commons.util.report.MISExcelReportUtils;
import com.elitecore.core.commons.util.report.MISPDFReportsUtils;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.mis.model.DynamicReportData;
import com.elitecore.sm.mis.service.IDynamicReportService;
import com.elitecore.sm.rulelookup.model.RuleLookupTableData;
import com.elitecore.sm.rulelookup.service.IRuleDataLookUpService;
import com.elitecore.sm.util.DateFormatter;

/**
 * 
 * @author chetan.kaila
 *
 */

@Controller
public class DynamicReportController extends BaseController {
	
	protected Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private IRuleDataLookUpService ruleDataLookUpService;
	
	@Autowired
	private IDynamicReportService dynamicReportService;
	
	@Autowired
	ServletContext servletContext;
	
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

	/**
	 * 
	 * @param requestActionType
	 * @return
	 * 
	 * Load reportMgmt further include dynamicReportMgmt page on click on Dynamic Report tab
	 * 
	 */

	@PreAuthorize("hasAnyAuthority('VIEW_DYNAMIC_REPORT')")
	@RequestMapping(value = ControllerConstants.INIT_DYNAMIC_REPORT_CONFIG, method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initDynamicReportConfig(
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = false,defaultValue = BaseConstants.VIEW_DYNAMIC_REPORT)String requestActionType
			){
		ModelAndView model = new ModelAndView(
				ViewNameConstants.INIT_MIS_REPORT_MANAGER);
			model.addObject(FormBeanConstants.RULE_DATA_TABLE_FORM_BEAN,
					(RuleLookupTableData) SpringApplicationContext
							.getBean(RuleLookupTableData.class));
			model.addObject("trueFalseEnum",
					Arrays.asList(TrueFalseEnum.values()));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
		return model;
	}
	
	/**
	 * 
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param reportName
	 * @param reportDesc
	 * @return
	 * 
	 * search dynamic report based on input like report name or report description
	 * 
	 */
	
	@PreAuthorize("hasAnyAuthority('VIEW_DYNAMIC_REPORT')")
	@RequestMapping(value = ControllerConstants.INIT_DYNAMIC_REPORT_LIST, method = RequestMethod.GET)
	@ResponseBody
	public String initDynamicReportList(
			@RequestParam(value = "rows", defaultValue = "5") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = true) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			@RequestParam(value = "reportName", required=false) String reportName,
			@RequestParam(value = "reportDesc", required=false) String reportDesc){
		Map<String, Object> row;
		List<Map<String, Object>> rowList = new ArrayList<>();
		
		long count = dynamicReportService.getAllTableListCount(reportName,reportName);
		List<DynamicReportData> resultList  = this.dynamicReportService.getPaginatedList(eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord,reportName,reportDesc);
		if (resultList != null) { 
			count = resultList.size();
			for (DynamicReportData dynamicReportData : resultList) {
				row = new HashMap<>();
				row.put("id", dynamicReportData.getId());
				row.put("reportName",dynamicReportData.getReportName());
				row.put("description",dynamicReportData.getDescription());
				row.put("viewName",dynamicReportData.getViewName());
				row.put("displayfields",dynamicReportData.getDisplayFields().toUpperCase());
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}

	/**
	 * 
	 * @param tableId
	 * @param reportName
	 * @param description
	 * @param viewName
	 * @param displayFields
	 * @param response
	 * @return
	 * 
	 * Load header of detail report data on click on view button 
	 * 
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_DYNAMIC_REPORT')")
	@RequestMapping(value = ControllerConstants.INIT_DYNAMIC_REPORT_HEADER_LIST, method = RequestMethod.POST)
	public ModelAndView initRuleLookupDataConfig(
			@RequestParam(value = "viewDynamicReportTableId",required = true)String tableId, 
			@RequestParam(value = "viewDynamicReportName",required = true)String reportName,
			@RequestParam(value = "viewDynamicReportDescription",required = false)String description,
			@RequestParam(value = "viewDynamicReportTableViewName",required = true)String viewName,
			@RequestParam(value = "viewDynamicReportTableDisplayfields",required = true)String displayFields,
			HttpServletResponse response){
		ModelAndView model = new ModelAndView(ViewNameConstants.INIT_MIS_REPORT_MANAGER);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.VIEW_DYNAMIC_REPORT_TABLE_RECORDS);
		
		String displayFieldsArr [] = displayFields.split(";");
		
		JSONArray jAllGrpAttrArr = new JSONArray();
		if (displayFieldsArr != null) {
			for (String tableField : displayFieldsArr) {
				jAllGrpAttrArr.put(tableField);
			}
		}
		model.addObject("tableId", tableId );
		model.addObject("reportName", reportName );
		model.addObject("description", description );
		model.addObject("viewName", viewName);
		model.addObject("tableFieldListJson", jAllGrpAttrArr);
	    return model;
	}

	
	/**
	 * 
	 * @param searchQuery
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param tableId
	 * @param viewName
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 
	 * Load actual report data of configured view when click on view button 
	 * 
	 */
	
	@PreAuthorize("hasAnyAuthority('VIEW_DYNAMIC_REPORT')")
	@RequestMapping(value = ControllerConstants.INIT_DYNAMIC_REPORT_RECORD_LIST, method = RequestMethod.GET)
	@ResponseBody
	public String initRuleLookupDataList(
			@RequestParam(value = "searchQuery" , required=false) String searchQuery,
			@RequestParam(value = "rows", defaultValue = "5") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = false) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			@RequestParam(value = "id",required = false)int tableId,
			@RequestParam(value = "viewName" , required=false) String viewName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject = new ResponseObject();
		List<Map<String, Object>> searchrowList ;
		long count = ruleDataLookUpService.getSearchLookupDataListCountById( viewName ,tableId ,searchQuery);
		searchrowList =  this.ruleDataLookUpService.getSearchLookupDataPaginatedList(eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), 
				limit, sidx, sord, tableId, viewName , searchQuery , responseObject);
		
		
		if(!responseObject.isSuccess()){			
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			ajaxResponse.setResponseMsg("Enter valid input search text");
			return ajaxResponse.toString();
		}
		
		
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, searchrowList).getJsonString();
	}
	
	//Controller for getting records count from MIS table - MEDSUP 2202
	//GET  ControllerConstants.GET_DYNAMIC_REPORT_COUNT RES: AJAX OBJ with MSG as count and STATUS CODE
	@PreAuthorize("hasAnyAuthority('VIEW_DYNAMIC_REPORT')")
	@RequestMapping(value = ControllerConstants.GET_DYNAMIC_REPORT_COUNT, method = RequestMethod.GET)
	@ResponseBody
	public String getDynamicReportCount(
			@RequestParam(value = "searchQuery" , required=false) String searchQuery,		
			@RequestParam(value = "viewName" , required=false) String viewName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		long count = ruleDataLookUpService.getSearchLookupDataListCountById( viewName ,0,searchQuery);
		System.out.print(count);
		logger.debug("Count is: " + count);
		if(count == -1){			
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
		}
		else {
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_SUCCESS);
			ajaxResponse.setResponseMsg(Long.toString(count));
			ajaxResponse.setObject(count);
		}
		return ajaxResponse.toString();	
	}
	
	
	@PreAuthorize("hasAnyAuthority('DOWNLOAD_DYNAMIC_REPORT')")
	@RequestMapping(value = ControllerConstants.DOWNLOAD_DYNAMIC_REPORT, method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView downloadDynamicReport(@RequestParam(value = "reportName",required = true)String reportName,
			@RequestParam(value = "searchQuery",required = false)String searchQuery,
			@RequestParam(value = "viewName",required = true)String viewName,
			@RequestParam(value = "fileType",required = true)String fileType,
			 HttpServletResponse response,
			 HttpServletRequest request){
	    try{
	    	DynamicReportData reportData = dynamicReportService.getDynamicReportDataByReportName(reportName);
	    	if(reportData!=null){
	    		ResponseObject responseObject = new ResponseObject();
	    		String displayFields=reportData.getDisplayFields();
		    	List<String> tableFieldList = Arrays.asList(displayFields.split(";"));
		    	List<String> tableViewFieldList = new ArrayList<>();
		    	List<Map<String,Object>> searchLookUpDataList = null;
		    	List<List<String>> rowList= new ArrayList<>();		    	
		    	if(!CollectionUtils.isEmpty(tableFieldList)) {
		    		for(int i = 0; i < tableFieldList.size(); i++) {
		    			String field = tableFieldList.get(i);
		    			tableViewFieldList.add(field.toUpperCase());	    			
		    		}	    		
		    	}		    			    	    	
		    	searchLookUpDataList = ruleDataLookUpService.getLookupViewData(0, viewName, searchQuery, responseObject);
		    	getMapDataAsRowList(searchLookUpDataList, tableViewFieldList, rowList);
		    	
				boolean isPDF = StringUtils.equals(BaseConstants.PDF_FILE_TYPE,fileType);
			    File file=null;
				if (isPDF) {
					file = MISPDFReportsUtils.generateDynamicPDFReport(rowList, tableViewFieldList, reportName);
					logger.info(StringUtils.replace(reportName, " ", StringUtils.EMPTY) + "_" + System.currentTimeMillis() + ".pdf");
					if (file != null) {
						response.setHeader(BaseConstants.MIS_DOWNLOAD_HEADER_STRING_1,BaseConstants.MIS_DOWNLOAD_HEADER_STRING_2+ reportName+ "_" + System.currentTimeMillis() + ".pdf");
						response.setContentType(BaseConstants.PDF_APPLICATION_CONTENT_TYPE);
						response.setContentLength((int) file.length());
					}
				}else{
					file = MISExcelReportUtils.generateDynamicExcelReport(reportName, tableViewFieldList, rowList);
					logger.info(StringUtils.replace(reportName, " ", StringUtils.EMPTY) + "_" + System.currentTimeMillis() + ".xls");
					if (file != null) {
						response.setHeader(BaseConstants.MIS_DOWNLOAD_HEADER_STRING_1,BaseConstants.MIS_DOWNLOAD_HEADER_STRING_2+ reportName+ "_" + System.currentTimeMillis() + ".xls");
						response.setContentType(BaseConstants.EXCEL_APPLICATION_CONTENT_TYPE);
						response.setContentLength((int) file.length());
					}
				}
				try(FileInputStream fileInputStream = new FileInputStream(file);DataInputStream in = new DataInputStream(fileInputStream);ServletOutputStream outStream = response.getOutputStream())
				{
					byte[] byteBuffer = new byte[4096];
					int length;
					while ((length = in.read(byteBuffer)) != -1)  {
						outStream.write(byteBuffer, 0, length);
					}
				}catch (Exception e) {
					logger.error("Exception Occured during download file " + e);
				}
	    	}
	    }catch(Exception e){
	    	logger.error("Error occured while downloading file", e);
	    }
	    return null;
	}
	
	private List<List<String>> getMapDataAsRowList(List<Map<String,Object>> searchLookUpDataList, List<String> tableViewFieldList, List<List<String>> rowList){
		if(searchLookUpDataList!=null && searchLookUpDataList.size()>0){
			List<String> dataList = null;
			Iterator<Map<String,Object>> it = searchLookUpDataList.iterator();
			while (it.hasNext()) {
				Map<String,Object> map = it.next();				 				
				dataList = new ArrayList<>();
				for(int i = 0; i < tableViewFieldList.size(); i++) {
	    			String field = tableViewFieldList.get(i);
	    			String value = BaseConstants.EMPTY_STRING;
	    			if(map.get(field)!=null)
	    				value = map.get(field).toString();
			    	dataList.add(value);
				}
				rowList.add(dataList);								 
			}
		}
		return rowList;
	}
	
}