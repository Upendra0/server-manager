package com.elitecore.sm.parser.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.TrimPositionEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.parser.model.HtmlParserAttribute;
import com.elitecore.sm.parser.model.HtmlParserMapping;
import com.elitecore.sm.parser.model.PDFParserAttribute;
import com.elitecore.sm.parser.model.PDFParserMapping;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.ParserGroupAttribute;
import com.elitecore.sm.parser.model.ParserPageConfiguration;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.parser.model.XlsParserAttribute;
import com.elitecore.sm.parser.model.XlsParserMapping;
import com.elitecore.sm.parser.service.HtmlParserService;
import com.elitecore.sm.parser.service.PDFParserService;
import com.elitecore.sm.parser.service.ParserAttributeService;
import com.elitecore.sm.parser.service.ParserGroupAttributeService;
import com.elitecore.sm.parser.service.XlsParserService;
import com.elitecore.sm.parser.validator.ParserMappingValidator;
import com.elitecore.sm.util.DateFormatter;
/**
 * @author ELITECOREADS\kiran.kagithapu
 *
 */
@Controller
public class ParserGroupAttributeController extends BaseController {
	
	@Autowired
	private ParserAttributeService parserAttributeService;
	
	@Autowired
	ParserGroupAttributeService parserGroupAttributeService;
	
	@Autowired
	HtmlParserService htmlParserService;
	
	@Autowired
	ParserMappingValidator parserMappingValidator;
	
	@Autowired
	PDFParserService pdfParserService;
	
	@Autowired
	XlsParserService xlsParserService;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_PARSER_GROUP_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public String addEditGroupAttributeByMappingId(
			@ModelAttribute(value = FormBeanConstants.GROUP_ATTRIBUTE_FORM_BEAN) ParserGroupAttribute parserGroupAttribute,//NOSONAR
			@RequestParam(value = "actionType",required=true) String actionType,
		 	@RequestParam(value = "plugInType",required=true) String plugInType,
			@RequestParam(value = "mappingId",required=true) int mappingId,
			@RequestParam(value = "groupAttrLists",required=false) String groupAttrLists,
			@RequestParam(value = "attrLists",required=true) String attrLists,BindingResult result,HttpServletRequest request) throws SMException{
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject = new ResponseObject();

		parserMappingValidator.validateParserGroupAttributeName(parserGroupAttribute, result, null, false);
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			if(attrLists!=null&&!attrLists.isEmpty()){
				int staffId = eliteUtils.getLoggedInStaffId(request);
				if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
					responseObject=parserGroupAttributeService.createGroupAttribute(parserGroupAttribute, groupAttrLists, attrLists, mappingId, plugInType, staffId);
				}else{
					responseObject=parserGroupAttributeService.updateGroupAttribute(parserGroupAttribute, groupAttrLists, attrLists, mappingId, plugInType, staffId);
				}
			}else{
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.PARSER_GROUP_ATTRIBUTE_ADD_FAIL);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.GET_ADD_PARSER_GROUP_ATTRIBUTE_GRID_LIST_BY_GROUP_ID,method=RequestMethod.POST)
	@ResponseBody public String getAddGroupAttributeListByGroupId(@RequestParam(value = "plugInType" , required = true) String parserType,
			@RequestParam(value = "mappingId" , required = true) int mappingId){
    	logger.debug("getAttributeListBymappingId in ParserGroupAttributeController " + mappingId); 
		ResponseObject responseObject = new ResponseObject();
		List<ParserGroupAttribute> resultList = new ArrayList<>();
		responseObject=parserGroupAttributeService.getGroupAttributeListEligibleToAttachWithGroupByMappingId(mappingId);
		if(responseObject.isSuccess()&&responseObject.getObject()!=null){
			resultList=(List<ParserGroupAttribute>)responseObject.getObject();
		}

		JSONObject jGrpAttributeList = new JSONObject();
		JSONObject jGrpAttrObj;
		JSONArray jGrpAllAttrArr = new JSONArray();
		if(resultList!=null){
			for(ParserGroupAttribute parserGroupAttribute:resultList){
				jGrpAttrObj=new JSONObject();
				jGrpAttrObj.put("id", parserGroupAttribute.getId());
				jGrpAttrObj.put("groupName", parserGroupAttribute.getName());
				jGrpAllAttrArr.put(jGrpAttrObj);
			}
		}
		jGrpAttributeList.put("groupAttributeList", jGrpAllAttrArr);
		responseObject.setObject(jGrpAttributeList);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.GET_ADD_PARSER_ATTRIBUTE_GRID_LIST_BY_GROUP_ID,method=RequestMethod.POST)
	@ResponseBody public String getAddAttributeListByGroupId(@RequestParam(value = "plugInType" , required = true) String parserType,
			@RequestParam(value = "mappingId" , required = true) int mappingId){
    	logger.debug("getAttributeListBymappingId in ComposerGroupAttributeController " + mappingId); 
		ResponseObject responseObject = new ResponseObject();
		List<ParserAttribute> resultList = new ArrayList<>();
        responseObject=parserGroupAttributeService.getAttributeListEligibleToAttachWithGroup(mappingId);
        if(responseObject.isSuccess() && responseObject.getObject()!=null){
        	resultList=(List<ParserAttribute>)responseObject.getObject();
        }
        JSONObject jAttributeList=new JSONObject();
        JSONObject jAttrObj;
        JSONArray jAllAttrArr=new JSONArray();
        if(resultList !=null){
        	for (ParserAttribute parserAttribute:resultList){
        		jAttrObj=new JSONObject();
        		jAttrObj.put("id", parserAttribute.getId());
        		jAttrObj.put("sourceField",parserAttribute.getSourceField());
        		jAttrObj.put("unifiedField", parserAttribute.getUnifiedField());
        		jAllAttrArr.put(jAttrObj);
			}
        }
        jAttributeList.put("attributeList", jAllAttrArr);
        responseObject.setObject(jAttributeList);
        responseObject.setSuccess(true);
        responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value=ControllerConstants.GET_UPDATE_GROUP_PARSER_ATTRIBUTE_GRID_LIST_BY_GROUP_ID,method=RequestMethod.POST)
	@ResponseBody public String getUpdateGroupAttributeListByGroupId(@RequestParam(value = "plugInType" , required = true) String parserType,
			@RequestParam(value = "mappingId" , required = true) int mappingId,
			@RequestParam(value = "groupId" , required = true) int groupId){
    	logger.debug("getGroupAttributeListByGroupId in ComposerGroupAttributeController " + groupId); 
    	ResponseObject responseObject = new ResponseObject();
		List<ParserGroupAttribute> resultList = new ArrayList<>();
		JSONObject jGrpAttributeList = new JSONObject();
		JSONObject jGrpAttrObj;
		/* getting all attribute list attached with the group and setting it on json object*/
		JSONArray jAllAtchGrpAttrArr = new JSONArray();
          responseObject=parserGroupAttributeService.getAttachedGroupAttributeListByGroupId(groupId,mappingId);
          if(responseObject.isSuccess() && responseObject.getObject()!= null){
        	  resultList=(List<ParserGroupAttribute>)responseObject.getObject();
          }
          if(resultList!=null){
        	  for(ParserGroupAttribute parserGroupAttribute:resultList){
        		  jGrpAttrObj=new JSONObject();
        		  jGrpAttrObj.put("id", parserGroupAttribute.getId());
        		  jGrpAttrObj.put("groupName", parserGroupAttribute.getName());
        		  jAllAtchGrpAttrArr.put(jGrpAttrObj);
        	  }
          }
          jGrpAttributeList.put("attachedGroupAttributeList", jAllAtchGrpAttrArr);
          
        /* getting all attribute list which are eligible to attached with the group and setting it on json object*/
  		JSONArray jAllEllGrpAttrArr = new JSONArray();
  		responseObject = null;
  		resultList = null;
  		responseObject =parserGroupAttributeService.getGroupAttributeListEligibleToAttachWithGroupByGroupId(groupId, mappingId);
  		if(responseObject.isSuccess()&& responseObject.getObject()!=null){
  			resultList=(List<ParserGroupAttribute>)responseObject.getObject();
  		}
  		if(resultList!=null){
  			for(ParserGroupAttribute parserGroupAttribute:resultList){
  				jGrpAttrObj=new JSONObject();
  				jGrpAttrObj.put("id", parserGroupAttribute.getId());
  				jGrpAttrObj.put("groupName", parserGroupAttribute.getName());
  				jAllEllGrpAttrArr.put(jGrpAttrObj);
  				
  			}
  		}
  		jGrpAttributeList.put("eligibleGroupAttributeList", jAllEllGrpAttrArr);
  		responseObject.setObject(jGrpAttributeList);
  		responseObject.setSuccess(true);
  		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
  		AjaxResponse ajaxResponse=eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.GET_UPDATE_PARSER_ATTRIBUTE_GRID_LIST_BY_GROUP_ID,method=RequestMethod.POST)
	@ResponseBody public String getUpdateAttributeListByGroupId(@RequestParam(value = "plugInType" , required = true) String parserType,
			@RequestParam(value = "mappingId" , required = true) int mappingId,
			@RequestParam(value = "groupId" , required = true) int groupId){
		
		logger.debug("getAttributeListByGroupId in ParserGroupAttributeController " + groupId); 
		ResponseObject responseObject = new ResponseObject();
		List<ParserAttribute> resultList = new ArrayList<>();
		JSONObject jAttributeList = new JSONObject();
		JSONObject jAttrObj;
		/* getting all attribute list attached with the group and setting it on json object*/
		JSONArray jAllAtchAttrArr = new JSONArray();
         responseObject=parserGroupAttributeService.getAttachedAttributeListByGroupId(groupId, mappingId);
         if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
 			resultList = (List<ParserAttribute>) responseObject.getObject();
 		}
         if (resultList != null) {
 			for (ParserAttribute parserAttribute : resultList) {
 				jAttrObj = new JSONObject();
 				jAttrObj.put("id", parserAttribute.getId());
 				jAttrObj.put("sourceField", parserAttribute.getSourceField());
 				jAttrObj.put("unifiedField", parserAttribute.getUnifiedField());
 								
 				jAllAtchAttrArr.put(jAttrObj);
 			}
 		}
 		jAttributeList.put("attachedAttributeList", jAllAtchAttrArr);
 		
 		/* getting all attribute list which are eligible to attached with the group and setting it on json object*/
		JSONArray jAllEllAttrArr = new JSONArray();
		responseObject = null;
		resultList = null;
	    responseObject=parserGroupAttributeService.getAttributeListEligibleToAttachWithGroup(mappingId);
	    if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
			resultList = (List<ParserAttribute>) responseObject.getObject();
		}
	    if (resultList != null) {
			for (ParserAttribute parserAttribute : resultList) {
				jAttrObj = new JSONObject();
				jAttrObj.put("id", parserAttribute.getId());
				jAttrObj.put("sourceField", parserAttribute.getSourceField());
				jAttrObj.put("unifiedField", parserAttribute.getUnifiedField());
				jAllEllAttrArr.put(jAttrObj);
			}
		}
         jAttributeList.put("eligibleAttributeList", jAllEllAttrArr);
		
		responseObject.setObject(jAttributeList);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER')")
	@RequestMapping(value=ControllerConstants.GET_VIEW_GROUP_PARSER_ATTRIBUTE_GRID_LIST_BY_GROUP_ID,method=RequestMethod.POST)
	@ResponseBody public String getViewGroupAttributeListByGroupId(
			@RequestParam(value = "plugInType" , required = true) String parserType,
			@RequestParam(value = "mappingId" , required = true) int mappingId,
			@RequestParam(value = "groupId" , required = true) int groupId){
		logger.debug("getGroupAttributeListByGroupId in ParserGroupAttributeController " + groupId); 
		ResponseObject responseObject = new ResponseObject();
		List<ParserGroupAttribute> resultList = new ArrayList<>();
		responseObject = parserGroupAttributeService.getGroupAttributeListByGroupId(groupId, mappingId);
		if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
			resultList = (List<ParserGroupAttribute>) responseObject.getObject();
		}
		JSONObject jAttributeList = new JSONObject();
		JSONObject jAttrObj;
		JSONArray jAllAttrArr = new JSONArray();
		if (resultList != null) {
			for (ParserGroupAttribute parserGroupAttribute : resultList) {
				jAttrObj = new JSONObject();
				jAttrObj.put("id", parserGroupAttribute.getId());
				jAttrObj.put("groupName", parserGroupAttribute.getName());
				jAllAttrArr.put(jAttrObj);
			}
		}
		jAttributeList.put("groupAttributeList", jAllAttrArr);
		responseObject.setObject(jAttributeList);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
				
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
				
	}
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER')")
	@RequestMapping(value=ControllerConstants.GET_VIEW_PARSER_ATTRIBUTE_GRID_LIST_BY_GROUP_ID,method=RequestMethod.POST)
	@ResponseBody public String getViewAttributeListByGroupId(
			@RequestParam(value = "plugInType" , required = true) String parserType,
			@RequestParam(value = "mappingId" , required = true) int mappingId,
			@RequestParam(value = "groupId" , required = true) int groupId){
		logger.debug("getAttributeListByGroupId in ParserGroupAttributeController " + groupId); 
		ResponseObject responseObject = new ResponseObject();
		List<ParserAttribute> resultList = new ArrayList<>();
		responseObject=parserGroupAttributeService.getAttachedAttributeListByGroupId(groupId, mappingId);
		if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
			resultList = (List<ParserAttribute>)responseObject.getObject();
		}
		JSONObject jAttributeList = new JSONObject();
		JSONObject jAttrObj;
		JSONArray jAllAttrArr = new JSONArray();
		if (resultList != null) {
			for (ParserAttribute composerAttribute : resultList) {
				jAttrObj = new JSONObject();
				jAttrObj.put("id", composerAttribute.getId());
				jAttrObj.put("sourceField", composerAttribute.getSourceField());
				jAttrObj.put("unifiedField", composerAttribute.getUnifiedField());
								
				jAllAttrArr.put(jAttrObj);
			}
		}
		jAttributeList.put("attributeList", jAllAttrArr);
		responseObject.setObject(jAttributeList);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
				
	}
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER')")
	@RequestMapping(value=ControllerConstants.GET_GROUP_PARSER_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID,method=RequestMethod.POST)
    @ResponseBody public String getGroupAttributeGridListByMappingId(
		@RequestParam(value = "plugInType" , required = true) String parserType,
		@RequestParam(value = "mappingId" , required = true) int mappingId){
    	logger.debug("getGroupAttributeListByMappingId in TapParserController " + mappingId);
    	ResponseObject responseObject = new ResponseObject();
		List<ParserGroupAttribute> resultList = new ArrayList<>();
		responseObject=parserGroupAttributeService.getGroupAttributeListByMappingId(mappingId);
		if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
			resultList = (List<ParserGroupAttribute>)responseObject.getObject();
		}
		JSONObject jGrpAttributeList = new JSONObject();
		JSONObject jGrpAttrObj;
		JSONArray jAllGrpAttrArr = new JSONArray();
		if (resultList != null) {
			for (ParserGroupAttribute gruopAttribute : resultList) {
				jGrpAttrObj = new JSONObject();
				jGrpAttrObj.put("id", gruopAttribute.getId());
				jGrpAttrObj.put("name", gruopAttribute.getName());
								
				jAllGrpAttrArr.put(jGrpAttrObj);
			}
		}
		jGrpAttributeList.put("groupAttributeList", jAllGrpAttrArr);
		responseObject.setObject(jGrpAttributeList);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
				
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.DELETE_GROUP_PARSER_ATTRIBTE, method = RequestMethod.POST)
	@ResponseBody public String deleteGroupAttribute(
			@RequestParam(value = "mappingId" , required = true) int mappingId,
			@RequestParam(value = "attributeId", required=true) String groupAttributeIds,
			HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
    ResponseObject responseObject   =parserGroupAttributeService.deleteGroupAttributes(groupAttributeIds, staffId, mappingId);
    AjaxResponse ajaxResponse=eliteUtils.convertToAjaxResponse(responseObject);
	return ajaxResponse.toString();
	}
	
	/**
	 * Redirect to PDF parser gruop attribute page
	 * @param requestParamMap
	 * @return ModelAndView
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.INIT_PDF_PARSER_GROUP_ATTRIBUTE,method=RequestMethod.POST)
	public ModelAndView initPDFParserGroupAttribute(@RequestParam Map<String,String> requestParamMap){
		ModelAndView model =new ModelAndView(ViewNameConstants.PDF_PARSER_CONFIGURATION);
		int pdfParserMappingId;
		ResponseObject responseObject = null;
		List<ParserGroupAttribute> groupList = null;
		if(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID)!=null){
			pdfParserMappingId=Integer.parseInt(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID));
			
			responseObject=pdfParserService.getPDFParserMappingById(pdfParserMappingId);
			if(responseObject.isSuccess()){
				PDFParserMapping pdfParserMapping=(PDFParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.PDF_PARSER_MAPPING_FORM_BEAN,pdfParserMapping);
				groupList = pdfParserMapping.getGroupAttributeList();
			}else{
				model.addObject(FormBeanConstants.PDF_PARSER_MAPPING_FORM_BEAN, (PDFParserMapping)SpringApplicationContext.getBean(PDFParserMapping.class));
				groupList = new ArrayList<ParserGroupAttribute>();
			}
		}
		addCommonParamToModel(requestParamMap, model);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.PDF_PARSER_GROUP_ATTRIBUTE);
		model.addObject("mappingId", requestParamMap.get("mappingId"));
		model.addObject("plugInType", requestParamMap.get("plugInType"));
		model.addObject("readOnlyFlagGroup", requestParamMap.get("readOnlyFlagGroup"));
		model.addObject("unifiedField", java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("trimPosition",java.util.Arrays.asList(TrimPositionEnum.values()));
		model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject("isGroupConfiguration", true);
		JSONArray jGroupList = new JSONArray();
		JSONObject jGroupDetail= null ;
		
		if(groupList != null && !groupList.isEmpty()){
				
			for(ParserGroupAttribute pga : groupList){
				JSONArray jAttributeList = new JSONArray();
				JSONObject jAttribute= null ;
				
				JSONArray jPageConfigList = new JSONArray();
				JSONObject jPageConfig= null ;
				if(!pga.getStatus().equals(StateEnum.DELETED)){
				
					jGroupDetail = new JSONObject();
					jGroupDetail.put("name", pga.getName());
					jGroupDetail.put("id", pga.getId());
					jGroupDetail.put("tableStartIdentifier", pga.getTableStartIdentifier());
					jGroupDetail.put("tableEndIdentifier", pga.getTableEndIdentifier());
					jGroupDetail.put("tableStartIdentifierCol", pga.getTableStartIdentifierCol());
					jGroupDetail.put("tableEndIdentifierCol", pga.getTableEndIdentifierCol());
					jGroupDetail.put("tableEndIdentifierOccurence", pga.getTableEndIdentifierOccurence());
					jGroupDetail.put("tableEndIdentifierRowLocation", pga.getTableEndIdentifierRowLocation());
					jGroupDetail.put("tableRowIdentifier", pga.getTableRowIdentifier());
				
					List<ParserAttribute> attributeList = pga.getAttributeList();
					
					if(attributeList != null && !attributeList.isEmpty()){
						
						for(ParserAttribute pa : attributeList){
							PDFParserAttribute pdfAtt = (PDFParserAttribute)pa;
							
							if(!pa.getStatus().equals(StateEnum.DELETED)){
								jAttribute = new JSONObject();
								jAttribute.put("unifiedField", pdfAtt.getUnifiedField());
								jAttribute.put("id", pdfAtt.getId());
								jAttribute.put("sourceField", pdfAtt.getSourceField());
								jAttribute.put("referenceRow", pdfAtt.getReferenceRow());
								jAttribute.put("referenceCol", pdfAtt.getReferenceCol());
								jAttribute.put("description", pdfAtt.getDescription());
								jAttribute.put("columnStartsWith", pdfAtt.getColumnStartsWith());
								jAttribute.put("tableFooter", pdfAtt.isTableFooter());
								jAttribute.put("defaultValue", pdfAtt.getDefaultValue());
								jAttribute.put("trimChars", pdfAtt.getTrimChars());
								jAttribute.put("mandatory", pdfAtt.getMandatory());
								jAttribute.put("multiLineAttribute", pdfAtt.isMultiLineAttribute());
								jAttribute.put("multipleValues", pdfAtt.isMultipleValues());
								jAttribute.put("rowTextAlignment", pdfAtt.getRowTextAlignment());
								jAttributeList.put(jAttribute);
							}
							
						}
						jGroupDetail.put("attributeList",jAttributeList);
					}
					
					List<ParserPageConfiguration> pageList = pga.getParserPageConfigurationList();
					
					if(pageList != null && !pageList.isEmpty()){
						
						for(ParserPageConfiguration pg : pageList){
							if(!pg.getStatus().equals(StateEnum.DELETED)){
								jPageConfig = new JSONObject();
								jPageConfig.put("pageSize", pg.getPageSize());
								jPageConfig.put("id", pg.getId());
								jPageConfig.put("tableLocation", pg.getTableLocation());
								jPageConfig.put("tableCols", pg.getTableCols());
								jPageConfig.put("pageNumber", pg.getPageNumber());
								jPageConfig.put("extractionMethod", pg.getExtractionMethod());
								
								jPageConfigList.put(jPageConfig);
							}
						}
						jGroupDetail.put("parserPageConfigurationList",jPageConfigList);
					}
					
					jGroupList.put(jGroupDetail);
				}
			}
		}
		model.addObject("groupAttributeList", jGroupList);
		return model;
	}
	
	/**
	 * Adds the edit attribute by mapping id.
	 * @param parserGroupAttribute the parser attribute
	 * @param actionType the action type
	 * @param plugInType the plug in type
	 * @param mappingId the mapping id
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.ADD_EDIT_PARSER_GROUP_BASIC_DETAILS_ATTRIBUTE,method=RequestMethod.POST)
	@ResponseBody public String addEditGroupAttributeBasicDetailsByMappingId(
			@ModelAttribute(value=FormBeanConstants.PARSER_GROUP_ATTRIBUTE_FORM_BEAN) ParserGroupAttribute parserGroupAttribute,//NOSONAR
			@RequestParam(value = "actionType",required=true) String actionType,
			@RequestParam(value = "plugInType",required=true) String plugInType,
			@RequestParam(value = "mappingId",required=true) int mappingId,
			BindingResult result, HttpServletRequest request){
		
    	AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		
		int staffId = eliteUtils.getLoggedInStaffId(request);
		parserMappingValidator.validateParserGroupAttributeName(parserGroupAttribute, result, null, false);
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
		if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
			responseObject = parserGroupAttributeService.createParserGroupAttributes(parserGroupAttribute, mappingId, plugInType, staffId);
		}else{
			responseObject = parserGroupAttributeService.updateParserGroupAttributes(parserGroupAttribute, mappingId, plugInType,staffId,false);
		}
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Redirect to Html parser group attribute page
	 * @param requestParamMap
	 * @return ModelAndView
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_HTML_PARSER_GROUP_ATTRIBUTE, method= RequestMethod.POST)
	public ModelAndView initHtmlGroupParserAttribute(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.HTML_PARSER_CONFIGURATION);
		//List<ParserGroupAttribute> groupList = null;
		ParserGroupAttribute groupAttribute=null;
		int htmlParserMappingId=0;
		//HtmlParserMapping htmlParsermapping;
		if(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID) !=null){			
			htmlParserMappingId=Integer.parseInt(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID));
			ResponseObject responseObject=htmlParserService.getHtmlParserMappingById(htmlParserMappingId); 
			if(responseObject.isSuccess()){				
				HtmlParserMapping htmlParserMapping=(HtmlParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.HTML_PARSER_MAPPING_FORM_BEAN,htmlParserMapping);
				if(htmlParserMapping.getGroupAttributeList()!=null && htmlParserMapping.getGroupAttributeList().size()>0){
					groupAttribute = htmlParserMapping.getGroupAttributeList().get(0);
				}
			}else{
				model.addObject(FormBeanConstants.HTML_PARSER_MAPPING_FORM_BEAN,(HtmlParserMapping) SpringApplicationContext.getBean(HtmlParserMapping.class));
			}		
		}	
		addCommonParamToModel(requestParamMap, model);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.HTML_PARSER_GROUP_ATTRIBUTE);
		model.addObject("mappingId", requestParamMap.get("mappingId"));
		model.addObject("plugInType", requestParamMap.get("plugInType"));
		model.addObject("readOnlyFlagGroup",requestParamMap.get("readOnlyFlagGroup"));
		model.addObject("unifiedField",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		JSONObject jGroupDetail= null ;
		
		if( groupAttribute!=null && !groupAttribute.getStatus().equals(StateEnum.DELETED)){			
			jGroupDetail = new JSONObject();
			jGroupDetail.put("name", groupAttribute.getName());
			jGroupDetail.put("id", groupAttribute.getId());
			jGroupDetail.put("tableStartIdentifier", groupAttribute.getTableStartIdentifier());
			jGroupDetail.put("tableEndIdentifier", groupAttribute.getTableEndIdentifier());
			jGroupDetail.put("tableStartIdentifierTdNo", groupAttribute.getTableStartIdentifierTdNo());
			jGroupDetail.put("tableEndIdentifierTdNo", groupAttribute.getTableEndIdentifierTdNo());
			jGroupDetail.put("tableRowIgnore", groupAttribute.getTableRowsToIgnore());
		}
		model.addObject("groupAttribute", jGroupDetail);	
		return model;
		
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_XLS_PARSER_GROUP_ATTRIBUTE, method = RequestMethod.POST)
	public ModelAndView initXlsGroupParserAttribute(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.XLS_PARSER_CONFIGURATION);
		ParserGroupAttribute groupAttribute=null;
		int xlsParserMappingId=0;
		if(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID) !=null){			
			xlsParserMappingId=Integer.parseInt(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID));
			ResponseObject responseObject=xlsParserService.getXlsParserMappingById(xlsParserMappingId); 
			if(responseObject.isSuccess()){				
				XlsParserMapping xlsParserMapping=(XlsParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.XLS_PARSER_MAPPING_FORM_BEAN,xlsParserMapping);
				if(xlsParserMapping.getGroupAttributeList()!=null && !xlsParserMapping.getGroupAttributeList().isEmpty()){
					groupAttribute = xlsParserMapping.getGroupAttributeList().get(0);
				}
			}else{
				model.addObject(FormBeanConstants.XLS_PARSER_MAPPING_FORM_BEAN,(XlsParserMapping) SpringApplicationContext.getBean(XlsParserMapping.class));
			}		
		}	
		addCommonParamToModel(requestParamMap, model);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.XLS_PARSER_GROUP_ATTRIBUTE);
		model.addObject(BaseConstants.PARSER_MAPPING_ID, requestParamMap.get(BaseConstants.PARSER_MAPPING_ID));
		model.addObject(BaseConstants.PLUGIN_TYPE, requestParamMap.get(BaseConstants.PLUGIN_TYPE));
		model.addObject(BaseConstants.READ_ONLY_FLAG,BaseConstants.FALSE);
		model.addObject(BaseConstants.READ_ONLY_FLAG_GROUP,requestParamMap.get(BaseConstants.READ_ONLY_FLAG_GROUP));
		model.addObject(BaseConstants.UNIFIED_FIELD,java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject(BaseConstants.TRUE_FALSE_ENUM, java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject(BaseConstants.TRIM_POSITION, java.util.Arrays.asList(TrimPositionEnum.values()));
		JSONObject jGroupDetail= null ;
		
		if( groupAttribute!=null && !groupAttribute.getStatus().equals(StateEnum.DELETED)){			
			jGroupDetail = new JSONObject();
			jGroupDetail.put(BaseConstants.NAME, groupAttribute.getName());
			jGroupDetail.put(BaseConstants.ID, groupAttribute.getId());
			jGroupDetail.put(BaseConstants.TABLE_START_IDENTIFIER, groupAttribute.getTableStartIdentifier());
			jGroupDetail.put(BaseConstants.TABLE_END_IDENTIFIER, groupAttribute.getTableEndIdentifier());
			jGroupDetail.put(BaseConstants.TABLE_START_IDENTIFIER_COL, groupAttribute.getTableStartIdentifierCol());
			jGroupDetail.put(BaseConstants.TABLE_END_IDENTIFIER_COL, groupAttribute.getTableEndIdentifierCol());
		}
		model.addObject(BaseConstants.GROUP_ATTRIBUTE, jGroupDetail);	
		return model;
		
	}

	/**
	 * Gets the attribute grid list by mapping id.
	 *
	 * @param parserType the parser type
	 * @param mappingId the mapping id
	 * @param limit the limit
	 * @param currentPage the current page
	 * @param sidx the sidx
	 * @param sord the sord
	 * @param skipPaging the skip paging
	 * @param attributeType the attribute type
	 * @return the attribute grid list by mapping id
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER')")
	@RequestMapping(value = ControllerConstants.GET_PARSER_GROUP_ATTRIBUTE_GRID_LIST, method = RequestMethod.POST)
	@ResponseBody public String getParserGroupAttributeGridList(
				@RequestParam(value="plugInType",required=true) String parserType,
				@RequestParam(value = "mappingId" , required = true) int mappingId, 				
				@RequestParam(value = "skipPaging", defaultValue = "false") boolean skipPaging,
				@RequestParam(value = "attributeType", required = false) String attributeType) {
		logger.debug(">> getGroupAttributeListByMappingId in ParserAttributeController " + mappingId); 
		if(attributeType != null){
			logger.debug("Attribute Type ::"+attributeType);
		}
		
		long count =  this.parserAttributeService.getAttributeListCountByMappingId(mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ParserAttribute> resultList = new ArrayList<>();
		List<ParserAttribute> newResultList = new ArrayList<>();
		if(count > 0){
			logger.debug("<< getAttributeListByMappingId in ParserAttributeController ");
			if(skipPaging){
				if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
					resultList = (List<ParserAttribute>) responseObject.getObject();
				}
			}else{
				
				responseObject = this.parserAttributeService.getAttributeListByMappingId(mappingId,BaseConstants.UPSTREAM);
				if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
					resultList = (List<ParserAttribute>) responseObject.getObject();
					
					for(ParserAttribute attribute : resultList){
						if(attribute.getParserGroupAttribute()!=null){
							newResultList.add(attribute);
						}
					}
				}				
			}
		}
		JSONObject jAttributeList = new JSONObject();
		JSONObject jAttrObj;
		JSONArray jAllAttrArr = new JSONArray();
		
		if (resultList != null) {
			for (ParserAttribute attribute : newResultList) {
				jAttrObj = new JSONObject();
				jAttrObj.put(BaseConstants.ID, attribute.getId());
				jAttrObj.put(BaseConstants.SOURCE_FIELD, attribute.getSourceField());
				jAttrObj.put(BaseConstants.UNIFIED_FIELD, attribute.getUnifiedField());
				jAttrObj.put(BaseConstants.DEFAULT_TEXT,attribute.getDefaultValue() );				
				jAttrObj.put(BaseConstants.TRIM_CHAR, attribute.getTrimChars());
				jAttrObj.put(BaseConstants.TRIM_POSITION, attribute.getTrimPosition());
				
				jAttrObj.put(BaseConstants.ATTRIBUTE_ORDER, attribute.getAttributeOrder());
				
				if(EngineConstants.HTML_PARSING_PLUGIN.equals(parserType)){
					HtmlParserAttribute htmlParserAttribute = (HtmlParserAttribute)attribute;
					jAttrObj.put("tdNo", htmlParserAttribute.getTdNo());
				}else if(EngineConstants.XLS_PARSING_PLUGIN.equals(parserType)){
					XlsParserAttribute xlsParserAttribute = (XlsParserAttribute)attribute;
					jAttrObj.put(BaseConstants.COLUMN_STARTS_WITH , xlsParserAttribute.getColumnStartsWith());
					jAttrObj.put(BaseConstants.TABLE_FOOTER, xlsParserAttribute.isTableFooter());
					jAttrObj.put(BaseConstants.EXCEL_ROW, xlsParserAttribute.getExcelRow());
					jAttrObj.put(BaseConstants.EXCEL_COL , xlsParserAttribute.getExcelCol());
					jAttrObj.put(BaseConstants.RELATIVE_EXCEL_ROW, xlsParserAttribute.getRelativeExcelRow());
					jAttrObj.put(BaseConstants.STARTS_WITH, xlsParserAttribute.getStartsWith());
					jAttrObj.put(BaseConstants.COLUMN_CONTAINS, xlsParserAttribute.getColumnContains());
					jAttrObj.put(BaseConstants.TABLE_ROW_ATTRIBUTE, xlsParserAttribute.isTableRowAttribute());
				}
				
				jAllAttrArr.put(jAttrObj);
			}
		}
		
		jAttributeList.put(BaseConstants.ATTRIBUTE_LIST, jAllAttrArr);
		responseObject.setObject(jAttributeList);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
				
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	public void addCommonParamToModel(Map<String, String> requestParamMap, ModelAndView model) {

		model.addObject(BaseConstants.SERVICE_ID, requestParamMap.get(BaseConstants.SERVICE_ID));
		model.addObject(BaseConstants.SERVICE_NAME, requestParamMap.get(BaseConstants.SERVICE_NAME));
		model.addObject(BaseConstants.SERVICE_TYPE, requestParamMap.get(BaseConstants.SERVICE_TYPE));
		model.addObject(BaseConstants.SERVER_INSTANCE_ID, requestParamMap.get(BaseConstants.SERVER_INSTANCE_ID));
		model.addObject(BaseConstants.SERVICE_INST_ID, requestParamMap.get(BaseConstants.SERVICE_INST_ID));
		model.addObject(BaseConstants.PLUGIN_ID, requestParamMap.get(BaseConstants.PLUGIN_ID));
		model.addObject(BaseConstants.PLUGIN_NAME, requestParamMap.get(BaseConstants.PLUGIN_NAME));
		model.addObject(BaseConstants.PLUGIN_TYPE, requestParamMap.get(BaseConstants.PLUGIN_TYPE));
	}
	
	/**
	 * Gets the attribute grid list by mapping id.
	 *
	 * @param parserType the parser type
	 * @param mappingId the mapping id
	 * @param limit the limit
	 * @param currentPage the current page
	 * @param sidx the sidx
	 * @param sord the sord
	 * @param skipPaging the skip paging
	 * @param attributeType the attribute type
	 * @return the attribute grid list by mapping id
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER')")
	@RequestMapping(value = ControllerConstants.GET_ATTRIBUTE_GRID_LIST_BY_GROUP_ID, method = RequestMethod.POST)
	@ResponseBody public String getAttributeGridListByGroupId(
				@RequestParam(value = "groupId" , required = true) int groupId,
				@RequestParam(value = "mappingId" , required = true) int mappingId) {
		ResponseObject responseObject = new ResponseObject();
		List<ParserAttribute> resultList = new ArrayList<>();

		responseObject = this.parserGroupAttributeService.getAttachedAttributeListByGroupId(groupId, mappingId);
		
		if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
			resultList = (List<ParserAttribute>) responseObject.getObject();
		}
		
		JSONObject jAttributeList = new JSONObject();
		JSONObject jAttrObj;
		JSONArray jAllAttrArr = new JSONArray();
		
		if (resultList != null) {
			for (ParserAttribute attribute : resultList) {
				if(attribute.getStatus() != null && attribute.getStatus().equals(StateEnum.ACTIVE)){
				jAttrObj = new JSONObject();
				jAttrObj.put("id", attribute.getId());
				jAttrObj.put("sourceField", attribute.getSourceField());
				jAttrObj.put("unifiedField", attribute.getUnifiedField());
				jAttrObj.put("trimPosition", attribute.getTrimPosition());
				jAttrObj.put("description", attribute.getDescription());
				jAttrObj.put("defaultText",attribute.getDefaultValue() );
				jAttrObj.put("trimChar", attribute.getTrimChars());
				
				jAttrObj.put("attributeOrder", attribute.getAttributeOrder());
				
				PDFParserAttribute pdfParserAttribute = (PDFParserAttribute)attribute;
				jAttrObj.put("location", pdfParserAttribute.getLocation());
				jAttrObj.put("columnStartLocation", pdfParserAttribute.getColumnStartLocation());
				jAttrObj.put("columnIdentifier", pdfParserAttribute.getColumnIdentifier());
				jAttrObj.put("referenceRow", pdfParserAttribute.getReferenceRow());
				jAttrObj.put("referenceCol", pdfParserAttribute.getReferenceCol());
				jAttrObj.put("columnStartsWith", pdfParserAttribute.getColumnStartsWith());
				jAttrObj.put("tableFooter", pdfParserAttribute.isTableFooter());
				jAttrObj.put("mandatory", pdfParserAttribute.getMandatory());
				jAttrObj.put("multiLineAttribute", pdfParserAttribute.isMultiLineAttribute());
				jAttrObj.put("multipleValues", pdfParserAttribute.isMultipleValues());
				jAttrObj.put("rowTextAlignment", pdfParserAttribute.getRowTextAlignment());
				jAllAttrArr.put(jAttrObj);
				}
			}
		}
		
		jAttributeList.put("attributeList", jAllAttrArr);
		responseObject.setObject(jAttributeList);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
				
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will delete Group Config with nested elements  
	 * @param groupId
	 * @param mappingId
	 * @return AJAX responseObj
	 * @throws CloneNotSupportedException 
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_PARSER')")
	@RequestMapping(value = ControllerConstants.DELETE_GROUP_ATTRIBUTE_WITH_HIERARCHY, method = RequestMethod.POST)
	@ResponseBody public String deleteGroupConfigWithHierarchyByGroupId(
						@RequestParam(value = "groupId",required=true) int groupId,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						HttpServletRequest request) throws SMException {
		return deleteGroupConfigWithHierarchyByGroupId(request, groupId, mappingId);
	}
	
	public String deleteGroupConfigWithHierarchyByGroupId(HttpServletRequest request,  int groupId, int mappingId) throws SMException {
		AjaxResponse  ajaxResponse = new AjaxResponse();
		if(groupId > 0 && mappingId > 0){
			ResponseObject responseObject;
			responseObject = parserGroupAttributeService.deleteGroupAttributeWithHierarchy(groupId, eliteUtils.getLoggedInStaffId(request), mappingId);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Adds the edit page configuration by page id.
	 * @param pageConfiguration the parser page
	 * @param actionType the action type
	 * @param groupId the group Id
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.ADD_EDIT_PAGE_CONFIG_DETAILS,method=RequestMethod.POST)
	@ResponseBody public String addEditParserPageConfigurationDetails(
			@ModelAttribute(value=FormBeanConstants.PARSER_PAGE_CONFIG_FORM_BEAN) ParserPageConfiguration pageConfiguration,//NOSONAR
			@RequestParam(value = "actionType",required=true) String actionType,
			@RequestParam(value = "groupId",required=true) int groupId,
			BindingResult result, HttpServletRequest request){
		
    	AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		
		int staffId = eliteUtils.getLoggedInStaffId(request);
		if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
			
			responseObject = parserGroupAttributeService.createParserPageConfiguration(pageConfiguration, staffId, groupId);
		}else{
			responseObject = parserGroupAttributeService.updateParserPageConfiguration(pageConfiguration, staffId, groupId);
		}
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 * Gets the page config list by mapping id.
	 *
	 * @param mappingId the mapping id
	 * @return the page config list by mapping id
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.GET_PAGE_CONFIG_LIST_BY_GROUP_ID, method = RequestMethod.POST)
	@ResponseBody public String getPageConfigListByGroupId(
				@RequestParam(value = "groupId" , required = true) int groupId){
		ResponseObject responseObject = new ResponseObject();
		List<ParserPageConfiguration> resultList = new ArrayList<>();

		responseObject = this.parserGroupAttributeService.getPageConfigListGroupId(groupId);
		
		if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
			resultList = (List<ParserPageConfiguration>) responseObject.getObject();
		}
		
		JSONObject jPageConfigList = new JSONObject();
		JSONObject jPageObj;
		JSONArray jAllPageArr = new JSONArray();
		
		if (resultList != null) {
			for (ParserPageConfiguration page : resultList) {
				if(page.getStatus() != null && page.getStatus().equals(StateEnum.ACTIVE)){
					jPageObj = new JSONObject();
					jPageObj.put("id", page.getId());
					jPageObj.put("pageSize", page.getPageSize());
					jPageObj.put("tableLocation", page.getTableLocation());
					jPageObj.put("tableCols", page.getTableCols());
					jPageObj.put("pageNumber", page.getPageNumber());
					jPageObj.put("extractionMethod", page.getExtractionMethod());
					jAllPageArr.put(jPageObj);
				}
			}
		}
		
		jPageConfigList.put("pageConfigList", jAllPageArr);
		responseObject.setObject(jPageConfigList);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
				
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 * Delete page.
	 *
	 * @param pageIds the page ids
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.DELETE_PAGE, method = RequestMethod.POST)
	@ResponseBody public  String deletePage(@RequestParam(value = "pageId", required=true) 
		String pageIds, HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = parserGroupAttributeService.deletePageConfiguration(pageIds, staffId);	
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
}
