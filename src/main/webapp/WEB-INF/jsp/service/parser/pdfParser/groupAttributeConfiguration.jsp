<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script>
	var groupAttributeList = [];
</script>


<!-- main Div for hide/show mask start -->
<div class="tab-content no-padding" id = "group_attribute_details_div"> 
	
	<!-- Group List Div start -->
	<div class="tab-content no-padding">
		<%-- <div id="deletePopUpMsg" class=fullwidth>
			<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
		</div> --%>
		<div class="fullwidth mtop10">	
			<div class="title2">
				<span class="title2rightfield">
					<span class="title2rightfield-icon1-text">
	
					<a href="#" id="addGroupConfiguration" onclick="addGroupConfiguration();"><i class="fa fa-plus-circle">&nbsp;</i><spring:message code="parsing.group.configuration.add.group"></spring:message></a>
	
					</span>
				</span>
			</div>
			<div class="clearfix"></div>
		</div>
		<div class="fullwidth"> 
			<div id="groupAttributeList">
			</div>
		</div>
		<%-- <div id="deletePopUpMsg" class=fullwidth>
			<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
		</div> --%>
	</div>
	<!-- Group List Div end -->

	<!-- Add Attribute Div code start -->
	<form:form modelAttribute="parser_attribute_form_bean" method="POST"
				action="<%=ControllerConstants.ADD_EDIT_PDF_PARSER_ATTRIBUTE%>"
				id="add-edit-attribute-form">
		<!-- Add/Edit Attribute from grid  pop-up code start here-->
    	<input type="hidden" id="groupId" name="groupId" />
		<input type="hidden" value="0" id="id" name="id" />
		<input type="hidden" value="${plugInType}" id="selplugInType"
			name="selplugInType" />
		<input type="hidden" value="${mappingId}" id="selConfigMappingId"
			name="selConfigMappingId" />
	
	
		<div id="divAddAttribute" style="display: none;">
			<jsp:include page="../addEditParserAttributePopUp.jsp"></jsp:include>
		</div>
		<div id="divAdditionalInfoAttribute" style="display: none;">
			<jsp:include page="displayAdditionalPDFInfoPopUp.jsp"></jsp:include>
		</div>
	</form:form>
	<a href='#divAddAttribute' class='fancybox' style='display: none;' id='add_edit_attribute'>#</a>
	<a href='#divAdditionalInfoAttribute' class='fancybox' style='display: none;' id='view_attribute'>#</a>
	<!-- Add Attribute Div code end -->
	
	
	<!-- delete Attribute Div code start -->
	<a href="#delete_msg_div" class="fancybox" style="display: none;"
		id="delete_warn_msg_link">#</a>
	<div id="delete_msg_div" style="width: 100%; display: none;">
		<div class="modal-content">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="device.delete.popup.warning.heading.label" ></spring:message>
				</h4>
			</div>
			<div class="modal-body padding10 inline-form">
				<p id="moreWarn">
					<spring:message code="attribute.multiple.selection.warning.label" ></spring:message>
				</p>
				<p id="lessWarn">
					<spring:message code="attribute.no.selection.warning.label" ></spring:message>
				</p>
			</div>
			<div class="modal-footer padding10">
				<button type="button" class="btn btn-grey btn-xs "
					data-dismiss="modal" onclick="closeFancyBox();">
					<spring:message code="btn.label.close" ></spring:message>
				</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- Delete warning message popup  div end here-->

	<!-- Delete attribute popup div start here -->
	<a href='#divDeleteAttribute' class='fancybox' style='display: none;' id='delete_attribute'>#</a>
	
	<div id="divDeleteAttribute" style="width: 100%; display: none;">
		<div class="modal-content">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="parser.attribute.delete.heading.label" ></spring:message>
				</h4>
			</div>
			<div class="modal-body padding10 inline-form">
				<%-- <div id="deletePopUpMsg" class=fullwidth>
					<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
				</div> --%>
				<div class="fullwidth">
					<input type="hidden" value="" id="deleteAttributeId"
						name="deleteAttributeId" />
					<div id="delete_selected_attribute_details"></div>
					<div>
						<spring:message code="attribute.delete.warning.message" ></spring:message>
					</div>
				</div>
			</div>
			<sec:authorize access="hasAuthority('DELETE_PARSER')">
				<div id="delete_attribute_bts_div" class="modal-footer padding10">
					<button id="pdf_del_btn" type="button" class="btn btn-grey btn-xs"
						onclick="deleteAttribute();">
						<spring:message code="btn.label.delete" ></spring:message>
					</button>
					<button type="button" class="btn btn-grey btn-xs"
						onclick="closeFancyBox();">
						<spring:message code="btn.label.cancel" ></spring:message>
					</button>
				</div>
				</sec:authorize>
				<div class="modal-footer padding10"
					id="delete_close_attribute_buttons_div" style="display: none;">
					<button type="button" class="btn btn-grey btn-xs " id="closeDeleteAttributePopup"
						onclick="closeFancyBox();">
						<spring:message code="btn.label.close" ></spring:message>
					</button>
				</div>
			
			<div id="delete_attribute_progress_bar_div"
				class="modal-footer padding10 text-left" style="display: none;">
				<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
			</div>
		</div>
	</div>
	<!-- Delete attribute popup div end here -->
		
	<!-- Group Config Delete start -->
	<a href="#divGroupMessage" class="fancybox" style="display: none;" id="groupDelMessage">#</a>
	
	<!-- Group Delete popup code start here -->
   	<div id="divGroupMessage" style=" width:100%; display: none;" >
	    <div class="modal-content">
	        <div class="modal-header padding10" >
	           <h4 class="modal-title" id="status-title" ><spring:message code="parser.group.delete.label"></spring:message></h4>
	        </div>
	        
	        <div class="modal-body padding10 inline-form">
	        	<input type="hidden" id="groupId" name="groupId"/>
		        <p id="deleteWarningMessage">
		       		 <spring:message code="group.config.delete.warn.message"></spring:message>
		        </p>
	        </div>
	        
	       <!-- To Do add role here -->
		        <div id="delete-popup-buttons" class="modal-footer padding10">
	        		<button id="delete_btn" type="button" class="btn btn-grey btn-xs " onclick="deleteGroupDetails();"><spring:message code="btn.label.yes"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " id="closeBtn" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
	        	</div>
	        	<div id="delete-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
				<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
		</div>
	    </div>
	    <!-- /.modal-content --> 
	</div>
	<!-- Group Config Delete end -->
		
	<!-- Page add/edit div start-->
	<form:form modelAttribute="parser_page_config_form_bean" method="POST"
			action="<%=ControllerConstants.ADD_EDIT_PAGE_CONFIG_DETAILS%>"
			id="add-edit-page-form">
			
		<input type="hidden" value="0" id="pageConfigId" name="pageConfigId" />
		<input type="hidden" value="${plugInType}" id="selplugInType" name="selplugInType" />
		<input type="hidden" value="${mappingId}" id="selConfigMappingId"
			name="selConfigMappingId" />
		<a href="#divAddPageConfig" class="fancybox" style="display: none;" id="add_edit_page">#</a>
		<div id="divAddPageConfig" style="display: none;">
			<jsp:include page="addEditPageConfigPopUp.jsp"></jsp:include>
		</div>
		
	</form:form>
	<!-- Page add/edit div end-->
	
	<form action="<%= ControllerConstants.DOWNLOAD_SAMPLE_ATTRIBUTE%>"
			method="POST" id="sample_lookup_table_form">
			<input type="hidden" id="parserType" name="parserType" value='${plugInType}' /> 
			<input type="hidden" id="mappingId" name="mappingId" value='${mappingId}' /> 
			<input type="hidden" id="sampleRequired" name="sampleRequired" /> 
			<input type="hidden" id="groupAttrId" name="groupAttrId" /> 
			<input type="hidden" id="colNamesFromGrid" name="colNamesFromGrid" />
		</form>
	
	
	<!-- Delete page popup div start here -->
	<a href='#divDeletePage' class='fancybox' style='display: none;' id='delete_page'>#</a>
	<div id="divDeletePage" style="width: 100%; display: none;">
		<div class="modal-content">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="parser.group.page.delete.heading.label" ></spring:message>
				</h4>
			</div>
			<div class="modal-body padding10 inline-form">
				<%-- <div id="deletePopUpMsg" class=fullwidth>
					<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
				</div> --%>
				<div class="fullwidth">
					<input type="hidden" value="" id="deletePageId"
						name="deletePageId" />
					<div id="delete_selected_page_details"></div>
					<div>
						<spring:message code="group.config.page.delete.warn.message" ></spring:message>
					</div>
				</div>
			</div>
			<sec:authorize access="hasAuthority('DELETE_PARSER')">
				<div id="delete_page_bts_div" class="modal-footer padding10">
					<button id="pdf_del_btn" type="button" class="btn btn-grey btn-xs"
						onclick="deletePage();">
						<spring:message code="btn.label.delete" ></spring:message>
					</button>
					<button type="button" class="btn btn-grey btn-xs"
						onclick="closeFancyBox();">
						<spring:message code="btn.label.cancel" ></spring:message>
					</button>
				</div>
				</sec:authorize>
				<div class="modal-footer padding10"
					id="delete_close_attribute_buttons_div" style="display: none;">
					<button type="button" class="btn btn-grey btn-xs " id="closeDeletePagePopup"
						onclick="closeFancyBox();">
						<spring:message code="btn.label.close" ></spring:message>
					</button>
				</div>
			
			<div id="delete_page_progress_bar_div"
				class="modal-footer padding10 text-left" style="display: none;">
				<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
			</div>
		</div>
	</div>
	<!-- Delete page popup div end here -->
			
<!-- main Div for hide/show mask end -->
</div>
<!-- MED-8969 START : Attribute upload screen -->
<a href="#divuploadAttrData" class="fancybox" style="display: none;"
	id="uploadAttrData">#</a>
<div id="divuploadAttrData" style="width: 100%; display: none;">
<jsp:include page="../uploadParserAttrPopup.jsp"></jsp:include>
</div> 
<script type="text/javascript">
var groupAttributeListCounter=-1;
$(document).ready(function() {
	loadGroupAttGrid();	
	disableGroupAttributeDetail();
});

function loadGroupAttGrid() {
	groupAttributeList =  eval(${groupAttributeList});
	
	$.each(groupAttributeList, function(index,groupAttributeConfig){
		
		addGroupAttributeListDetail(groupAttributeConfig.id, groupAttributeConfig.name,
			groupAttributeConfig.parserPageConfigurationList,groupAttributeConfig.attributeList, groupAttributeConfig.tableStartIdentifier,
			groupAttributeConfig.tableStartIdentifierCol, groupAttributeConfig.tableEndIdentifier, groupAttributeConfig.tableEndIdentifierCol,
			groupAttributeConfig.tableEndIdentifierRowLocation,groupAttributeConfig.tableEndIdentifierOccurence,groupAttributeConfig.tableRowIdentifier);
	});
}

function addGroupAttributeListDetail(groupAttributeId, groupAttributeConfigName, pageConfigurationList, attributeList, tableStartIdentifier,
		tableStartIdentifierCol, tableEndIdentifier, tableEndIdentifierCol, tableEndIdentifierRowLocation,tableEndIdentifierOccurence,tableRowIdentifier){
	
	groupAttributeListCounter++;
	
	if(tableStartIdentifier == undefined || tableStartIdentifier == null){
		tableStartIdentifier = '';
	}
	if(tableStartIdentifierCol == undefined || tableStartIdentifierCol == null){
		tableStartIdentifierCol = '';
	}
	
	if(tableEndIdentifier == undefined || tableEndIdentifier == null){
		tableEndIdentifier = '';
	}
	if(tableEndIdentifierCol == undefined || tableEndIdentifierCol == null){
		tableEndIdentifierCol = '';
	}
	
	if(tableEndIdentifierRowLocation == undefined || tableEndIdentifierRowLocation == null){
		tableEndIdentifierRowLocation = '';
	}
	
	if(tableEndIdentifierOccurence == undefined || tableEndIdentifierOccurence == null){
		tableEndIdentifierOccurence = '';
	}
	
	if(tableRowIdentifier == undefined || tableRowIdentifier == null){
		tableRowIdentifier = '';
	}
	
	if(groupAttributeConfigName == undefined || groupAttributeConfigName == null){
		groupAttributeConfigName = '';
	}
	var html = "<div class='box box-warning' id='flipbox_"+groupAttributeListCounter+"'>  "+
				"<input type='hidden' id='"+groupAttributeListCounter+"_groupAttributeId' value='"+groupAttributeId+"'> "+
				"<div class='box-header with-border'> "+
				"	<h3 class='box-title' id='title_"+groupAttributeListCounter+"'>"+groupAttributeConfigName+ 											
				"	</h3>  "+
				"	<div class='box-tools pull-right' style='top: -3px;' id='action_"+groupAttributeListCounter+"'> "+ 
				"		<button class='btn btn-box-tool block-collapse-btn' id='collapseBtn_"+groupAttributeConfigName+"' data-toggle='collapse' href='#"+groupAttributeListCounter+"_block'> "+ 
				"			<i class='fa fa-minus'></i> "+
				"		</button> "+
				"					<sec:authorize access='hasAuthority(\'DELETE_PARSER\')'>"+
			    "			&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;' id='deleteGroup_"+groupAttributeListCounter+"' onClick=deleteGroupAttributeConfig('"+groupAttributeListCounter+"',\'"+groupAttributeId+"\');></a>&nbsp;" +
			    "					</sec:authorize>"+
				"	</div> "+
				"</div> "+
				
				//GROUP NAME
				
				"<div class='box-body inline-form accordion-body collapsed in' id='"+groupAttributeListCounter+"_block'> "+ 
				"	<div class='fullwidth inline-form'>  "+
				
				"		<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
			    "			<spring:message code='service.configuration.basic.detail.section.heading' var='tooltip'></spring:message>"+
			    "			<label style='width: 100%;background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
			    " 		</div>"+
				
				"		<div class='col-md-6 no-padding'> "+
				"			<spring:message code='parsing.service.group.attribute.name' var='tooltip' ></spring:message> "+ 
				"			<div class='form-group'>  "+
				"				<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+ 
				"				<div class='input-group '> "+
				"					<input type='text' id='"+groupAttributeListCounter+"_name' tabindex='1' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+groupAttributeConfigName+"'/>"+ 
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title='' ></i></span> "+
				"				</div> "+
				"			</div> "+
				"		</div> "+
				
				
				
				//Table Configurations
				"		<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
			    "			<spring:message code='parsing.service.group.attribute.table.configurations' var='tooltip'></spring:message>"+
			    "			<label style='width: 100%;background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
			    " 		</div>"+
			    
	    		"		<div class='col-md-6 no-padding'>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableStartIdentifier' var='label'></spring:message>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableStartIdentifier.tooltip' var='tooltip'></spring:message>"+
	    		"  			<div class='form-group'>"+
	    		"    			<div class='table-cell-label'>${label}</div>"+
	    		"      			<div class='input-group '>"+
	    		"      				<input class='form-control table-cell input-sm' id='"+groupAttributeListCounter+"_tableStartIdentifier' tabindex='2' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+tableStartIdentifier+"'/>"+
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    		"        		</div>"+
	    		"     		</div>"+
	    		"		</div>"+
	    		"		<div class='col-md-6 no-padding'>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableEndIdentifier' var='label'></spring:message>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableEndIdentifier.tooltip' var='tooltip'></spring:message>"+
	    		"  			<div class='form-group'>"+
	    		"    			<div class='table-cell-label'>${label}</div>"+
	    		"      			<div class='input-group '>"+
	    		"      				<input class='form-control table-cell input-sm' id='"+groupAttributeListCounter+"_tableEndIdentifier' tabindex='3' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+tableEndIdentifier+"'/>"+
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    		"        		</div>"+
	    		"     		</div>"+
	    		"		</div>"+
	    		"		<div class='col-md-6 no-padding'>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableStartIdentifierCol' var='label'></spring:message>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableStartIdentifierCol.tooltip' var='tooltip'></spring:message>"+
	    		"  			<div class='form-group'>"+
	    		"    			<div class='table-cell-label'>${label}</div>"+
	    		"      			<div class='input-group '>"+
	    		"      				<input class='form-control table-cell input-sm' id='"+groupAttributeListCounter+"_tableStartIdentifierCol' tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+tableStartIdentifierCol+"'/>"+
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    		"        		</div>"+
	    		"     		</div>"+
	    		"		</div>"+
	    		"		<div class='col-md-6 no-padding'>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableEndIdentifierCol' var='label'></spring:message>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableEndIdentifierCol.tooltip' var='tooltip'></spring:message>"+
	    		"  			<div class='form-group'>"+
	    		"    			<div class='table-cell-label'>${label}</div>"+
	    		"      			<div class='input-group '>"+
	    		"      				<input class='form-control table-cell input-sm' id='"+groupAttributeListCounter+"_tableEndIdentifierCol' tabindex='5' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+tableEndIdentifierCol+"'/>"+
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    		"        		</div>"+
	    		"     		</div>"+
	    		"		</div>"+
	    		
	    		
	    		"		<div class='col-md-6 no-padding'>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableEndIdentifierRowLocation' var='label'></spring:message>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableEndIdentifierRowLocation.tooltip' var='tooltip'></spring:message>"+
	    		"  			<div class='form-group'>"+
	    		"    			<div class='table-cell-label'>${label}</div>"+
	    		"      			<div class='input-group '>"+
	    		"      				<input class='form-control table-cell input-sm' id='"+groupAttributeListCounter+"_tableEndIdentifierRowLocation' tabindex='6' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+tableEndIdentifierRowLocation+"'/>"+
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    		"        		</div>"+
	    		"     		</div>"+
	    		"		</div>"+
	    		"		<div class='col-md-6 no-padding'>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableEndIdentifierOccurrence' var='label'></spring:message>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableEndIdentifierOccurrence.tooltip' var='tooltip'></spring:message>"+
	    		"  			<div class='form-group'>"+
	    		"    			<div class='table-cell-label'>${label}</div>"+
	    		"      			<div class='input-group '>"+
	    		"      				<input class='form-control table-cell input-sm' id='"+groupAttributeListCounter+"_tableEndIdentifierOccurence' tabindex='5' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+tableEndIdentifierOccurence+"'/>"+
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    		"        		</div>"+
	    		"     		</div>"+
	    		"		</div>"+
	    		"		<div class='col-md-6 no-padding'>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableRowIdentifier' var='label'></spring:message>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableRowIdentifier.tooltip' var='tooltip'></spring:message>"+
	    		"  			<div class='form-group'>"+
	    		"    			<div class='table-cell-label'>${label}</div>"+
	    		"      			<div class='input-group '>"+
	    		"      				<input class='form-control table-cell input-sm' id='"+groupAttributeListCounter+"_tableRowIdentifier' tabindex='5' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+tableRowIdentifier+"'/>"+
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    		"        		</div>"+
	    		"     		</div>"+
	    		"		</div>"+
	    		
	    		
	    		//Save Reset Buttons
				"		<div class='col-md-12 no-padding'> "+ 
				"			<div class='form-group'> "+
				"				<div id='"+groupAttributeListCounter+"_buttons-div' class='input-group '> "+ 
				"					<sec:authorize access='hasAuthority(\'UPDATE_PARSER\')'> "+
				"						<button type='button' class='btn btn-grey btn-xs' id='"+groupAttributeListCounter+"_updatebtn' tabindex='6' onclick=saveGroupAttrirbuteConfigDetailDetail(\'"+groupAttributeListCounter+"\','UPDATE','"+groupAttributeId+"')><spring:message code='btn.label.update'></spring:message></button>&nbsp;"+ 
				"					</sec:authorize> "+
				"					<button type='button' class='btn btn-grey btn-xs' id='"+groupAttributeListCounter+"_resetbtn' tabindex='7' onclick=resetGroupAttribute(\'"+groupAttributeListCounter+"\')><spring:message code='btn.label.reset'></spring:message></button>&nbsp;"+  
				"				</div> "+
				"				<div id='"+groupAttributeListCounter+"_progress-bar-div' class='input-group' style='display: none;'>"+  
				"					<label>Processing Request &nbsp;&nbsp; </label>  "+
				"					<img src='img/processing-bar.gif'> "+
				"				</div> "+
				"			</div>  "+
				"		</div>  "+
				
				
				
				//PAGE_LIST GRID
				"		<div class='col-md-12 no-padding gridBlock' > "+ 
				"			<div class='title2'> "+
				"				<spring:message code='parserConfiguration.page.config.grid.heading.label' ></spring:message>"+ 
				"					<span class='title2rightfield'> "+
				"						<sec:authorize access='hasAuthority(\'CREATE_PARSER\')'>"+
				"		          			<span class='title2rightfield-icon1-text'> "+
				"		          				<a href='#' onClick=displayAddEditPagePopup('ADD','0',\'"+groupAttributeListCounter+"\',\'"+groupAttributeId+"');><i class='fa fa-plus-circle'></i></a>"+
				"	  							<a href='#' name='" + groupAttributeListCounter +  "_addlink' onClick=displayAddEditPagePopup('ADD','0',\'"+groupAttributeListCounter+"\',\'"+groupAttributeId+"');> "+
				"	  								<spring:message code='btn.label.add' ></spring:message>  "+
				"	  							</a> "+
				"	      					</span>	  "+
				"						</sec:authorize>"+
				"						<sec:authorize access='hasAuthority(\'DELETE_PARSER\')'>"+
				"		          			<span class='title2rightfield-icon1-text'> "+
				
				"								<a href='#' onclick=displayDeletePagePopup('"+groupAttributeListCounter+"','"+groupAttributeId+"');"+
				"									<i class='fa fa-trash'></i>"+
				"								</a> "+
				"								<a href='#' onclick=displayDeletePagePopup('"+groupAttributeListCounter+"','"+groupAttributeId+"');>"+ 
				"									<spring:message code='btn.label.delete' ></spring:message>"+
				"								</a> "+
				
				
				
				"		          			</span> "+
				"						</sec:authorize>"+
				"		          	</span> "+
				"			</div> "+
				"		</div> "+
				"		<div class='col-md-12 no-padding gridBlock'> "+ 
				"			<div class='box-body table-responsive no-padding box'> "+
				"            	<table class='table table-hover' id='"+groupAttributeListCounter+"_pdfPageList'></table> "+
				"               	<div id='"+groupAttributeListCounter+"_pdfPageListDiv' ></div> "+
				"	           	<div class='clearfix'></div> "+
				"	           	<div id='divLoading' align='center' style='display: none;'><img src='img/preloaders/Preloader_10.gif' /></div> "+  
				"           </div> "+
				"		</div> "+
				
				
				
				//ATTRIBUTE_LIST GRID
				"		<div class='col-md-12 no-padding gridBlock' > "+ 
				"			<div class='title2'> "+
				"				<spring:message code='parserConfiguration.attribute.grid.heading.label' ></spring:message>"+ 
				"					<span class='title2rightfield'> "+
				"						<sec:authorize access='hasAuthority(\'CREATE_PARSER\')'>"+
				"							<span class='title2rightfield-icon1-text'>"+
				"						     	<a href='#' onclick=displayAddEditPopupForAL_In_Group('ADD','0',\'"+groupAttributeListCounter+"');>"+
				"									<i class='fa fa-plus-circle'></i>"+
				"								</a>"+ 
				"								<a href='#' onclick=displayAddEditPopupForAL_In_Group('ADD','0','"+groupAttributeListCounter+"');>"+ 
				"									<spring:message code='parserConfiguration.attribute.grid.addattribute.label' ></spring:message>"+
				"								</a> "+
													
				"							</span> "+
				"						</sec:authorize>"+
				"						<sec:authorize access='hasAuthority(\'DELETE_PARSER\')'>"+
				"							<span class='title2rightfield-icon1-text'> "+
				"								<a href='#' onclick=displayDeleteAttributePopupGA('"+groupAttributeListCounter+"');"+
				"									<i class='fa fa-trash'></i>"+
				"								</a> "+
				"								<a href='#' onclick=displayDeleteAttributePopupGA('"+groupAttributeListCounter+"');>"+ 
				"									<spring:message code='parserConfiguration.attribute.grid.deleteattribute.label' ></spring:message>"+
				"								</a> "+
				"							</span>"+
				"						</sec:authorize>"+
				"                     <span class='title2rightfield-icon1-text'>"+
				"					  <a onclick=addColNames(\'#"+groupAttributeListCounter+"_pdfAttributeList\');downloadCSVFile('"+groupAttributeId+"');>" + 
				"  					    <i class='fa fa-download'></i>"+ 
				"                     </a>" +
				"                     <a href='#' onclick=addColNames(\'#"+groupAttributeListCounter+"_pdfAttributeList\');downloadCSVFile('"+groupAttributeId+"');> "+
				"                     <spring:message code='parserConfiguration.attribute.grid.exportattribute.label'></spring:message>"+
				"                      </a>"+
				"                    </span>"+
				"                    <sec:authorize access='hasAuthority(\'CREATE_PARSER\')'>"+
				"                    <span class='title2rightfield-icon1-text'>"+
			    "	                 <a href='#' onclick=setUploadPopUpStyle();openUploadAttrPopupData(\'#"+groupAttributeListCounter+"_pdfAttributeList\','"+groupAttributeId+"','"+groupAttributeListCounter+"');>"+
				"	                 <i class='fa fa-upload'></i>"+
				"	                 </a>"+ 
				"                    <a href='#' onclick=setUploadPopUpStyle();openUploadAttrPopupData(\'#"+groupAttributeListCounter+"_pdfAttributeList\','"+groupAttributeId+"','"+groupAttributeListCounter+"');>"+
				"                    <spring:message code='parserConfiguration.attribute.grid.uploadattribute.label' ></spring:message>"+
				"                    </a>"+ 
				"                    </span>"+
				"                   </sec:authorize>"+ 
				"		          	</span> "+
				"			</div> "+
				"		</div> "+
				"		<div class='col-md-12 no-padding gridBlock'> "+ 
				"			<div class='box-body table-responsive no-padding box'> "+
				"            	<table class='table table-hover' id='"+groupAttributeListCounter+"_pdfAttributeList'></table> "+
				"               	<div id='"+groupAttributeListCounter+"_pdfAttributeListDiv' ></div> "+
				"	           	<div class='clearfix'></div> "+
				"	           	<div id='divLoading' align='center' style='display: none;'><img src='img/preloaders/Preloader_10.gif' /></div> "+  
				"           </div> "+
				"		</div> "+
				
				
				
				
				
				"	</div> "+
				"</div> "+
				"</div> ";
	$('#groupAttributeList').prepend(html);
	createPDFAttributeGrid('<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>', groupAttributeListCounter);
	getAllPDFAttributeListByMappingId(attributeList,groupAttributeListCounter);
	createPDFPageConfigGrid('<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>', groupAttributeListCounter);
	getAllPDFPageConfig(pageConfigurationList,groupAttributeListCounter);
	$("#"+groupAttributeListCounter+"_block").collapse("toggle");
}

function addGroupConfiguration(){
	
	groupAttributeListCounter++;
	groupAttributeList[groupAttributeListCounter] = {};
	var html = "<div class='box box-warning' id='flipbox_"+groupAttributeListCounter+"'>  "+
				" <input type='hidden' value='0' id='newGroupAttributeId_"+groupAttributeListCounter+"' name='newGroupAttributeId_"+groupAttributeListCounter+"' />"+
				" <input type='hidden' value='0' id='"+groupAttributeListCounter+"_groupAttributeId' name='"+groupAttributeListCounter+"_groupAttributeId' />"+
				"<div class='box-header with-border'> "+
				"	<h3 class='box-title' id='title_"+groupAttributeListCounter+"'>"+'Group Configuration'	+										
				"	</h3>  "+
				"	<div class='box-tools pull-right' style='top: -3px;' id='action_"+groupAttributeListCounter+"'> "+ 
				"		<button class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' href='#"+groupAttributeListCounter+"_block'> "+ 
				"			<i class='fa fa-minus'></i> "+
				"		</button> "+
				"					<sec:authorize access='hasAuthority(\'DELETE_PARSER\')'>"+
			    "			&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;' id='deleteGroup_"+groupAttributeListCounter+"' onClick=deleteGroupAttributeConfig('"+groupAttributeListCounter+"','-1'); ></a>&nbsp;" +
			    "					</sec:authorize>"+
				"	</div> "+
				"</div> "+
				"<div class='box-body inline-form accordion-body collapsed in' id='"+groupAttributeListCounter+"_block'> "+ 
				"	<div class='fullwidth inline-form'>  "+
				
				"		<div class='col-md-6 no-padding'> "+
				"			<spring:message code='parsing.service.group.attribute.name' var='tooltip' ></spring:message> "+ 
				"			<div class='form-group'>  "+
				"				<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+ 
				"				<div class='input-group '> "+
				"					<input type='text' id='"+groupAttributeListCounter+"_name' tabindex='1' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+ 
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title='' ></i></span> "+
				"				</div> "+
				"			</div> "+
				"		</div> "+
				
				
				
				
				
				
				"		<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
			    "			<spring:message code='parsing.service.group.attribute.table.configurations' var='tooltip'></spring:message>"+
			    "			<label style='width: 100%;background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
			    " 		</div>"+
			    
	    		"		<div class='col-md-6 no-padding'>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableStartIdentifier' var='label'></spring:message>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableStartIdentifier.tooltip' var='tooltip'></spring:message>"+
	    		"  			<div class='form-group'>"+
	    		"    			<div class='table-cell-label'>${label}</div>"+
	    		"      			<div class='input-group '>"+
	    		"      				<input class='form-control table-cell input-sm' id='"+groupAttributeListCounter+"_tableStartIdentifier' tabindex='2' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    		"        		</div>"+
	    		"     		</div>"+
	    		"		</div>"+
	    		"		<div class='col-md-6 no-padding'>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableEndIdentifier' var='label'></spring:message>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableEndIdentifier.tooltip' var='tooltip'></spring:message>"+
	    		"  			<div class='form-group'>"+
	    		"    			<div class='table-cell-label'>${label}</div>"+
	    		"      			<div class='input-group '>"+
	    		"      				<input class='form-control table-cell input-sm' id='"+groupAttributeListCounter+"_tableEndIdentifier' tabindex='3' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' />"+
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    		"        		</div>"+
	    		"     		</div>"+
	    		"		</div>"+
	    		"		<div class='col-md-6 no-padding'>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableStartIdentifierCol' var='label'></spring:message>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableStartIdentifierCol.tooltip' var='tooltip'></spring:message>"+
	    		"  			<div class='form-group'>"+
	    		"    			<div class='table-cell-label'>${label}</div>"+
	    		"      			<div class='input-group '>"+
	    		"      				<input class='form-control table-cell input-sm' id='"+groupAttributeListCounter+"_tableStartIdentifierCol' tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' />"+
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    		"        		</div>"+
	    		"     		</div>"+
	    		"		</div>"+
	    		"		<div class='col-md-6 no-padding'>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableEndIdentifierCol' var='label'></spring:message>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableEndIdentifierCol.tooltip' var='tooltip'></spring:message>"+
	    		"  			<div class='form-group'>"+
	    		"    			<div class='table-cell-label'>${label}</div>"+
	    		"      			<div class='input-group '>"+
	    		"      				<input class='form-control table-cell input-sm' id='"+groupAttributeListCounter+"_tableEndIdentifierCol' tabindex='5' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' />"+
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    		"        		</div>"+
	    		"     		</div>"+
	    		"		</div>"+
	    		
	    		"		<div class='col-md-6 no-padding'>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableEndIdentifierRowLocation' var='label'></spring:message>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableEndIdentifierRowLocation.tooltip' var='tooltip'></spring:message>"+
	    		"  			<div class='form-group'>"+
	    		"    			<div class='table-cell-label'>${label}</div>"+
	    		"      			<div class='input-group '>"+
	    		"      				<input class='form-control table-cell input-sm' id='"+groupAttributeListCounter+"_tableEndIdentifierRowLocation' tabindex='5' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' />"+
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    		"        		</div>"+
	    		"     		</div>"+
	    		"		</div>"+
	    		"		<div class='col-md-6 no-padding'>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableEndIdentifierOccurrence' var='label'></spring:message>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableEndIdentifierOccurrence.tooltip' var='tooltip'></spring:message>"+
	    		"  			<div class='form-group'>"+
	    		"    			<div class='table-cell-label'>${label}</div>"+
	    		"      			<div class='input-group '>"+
	    		"      				<input class='form-control table-cell input-sm' id='"+groupAttributeListCounter+"_tableEndIdentifierOccurence' tabindex='5' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' />"+
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    		"        		</div>"+
	    		"     		</div>"+
	    		"		</div>"+
	    		"		<div class='col-md-6 no-padding'>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableRowIdentifier' var='label'></spring:message>"+
	    		"			<spring:message code='parsing.service.group.attribute.tableRowIdentifier.tooltip' var='tooltip'></spring:message>"+
	    		"  			<div class='form-group'>"+
	    		"    			<div class='table-cell-label'>${label}</div>"+
	    		"      			<div class='input-group '>"+
	    		"      				<input class='form-control table-cell input-sm' id='"+groupAttributeListCounter+"_tableRowIdentifier' tabindex='5' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' />"+
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    		"        		</div>"+
	    		"     		</div>"+
	    		"		</div>"+
	    		
	    		
	    		//Save Reset Buttons
				"		<div class='col-md-12 no-padding'> "+ 
				"			<div class='form-group'> "+
				"				<div id='"+groupAttributeListCounter+"_buttons-div' class='input-group '> "+ 
				"					<sec:authorize access='hasAuthority(\'UPDATE_PARSER\')'> "+
				"						<button type='button' class='btn btn-grey btn-xs' id='"+groupAttributeListCounter+"_updatebtn' tabindex='6' onclick=saveGroupAttrirbuteConfigDetailDetail(\'"+groupAttributeListCounter+"\','ADD','"+0+"')><spring:message code='btn.label.update'></spring:message></button>&nbsp;"+ 
				"					</sec:authorize> "+
				"					<button type='button' class='btn btn-grey btn-xs' id='"+groupAttributeListCounter+"_resetbtn' tabindex='7' onclick=resetGroupAttribute(\'"+groupAttributeListCounter+"\')><spring:message code='btn.label.reset'></spring:message></button>&nbsp;"+  
				"				</div> "+
				"				<div id='"+groupAttributeListCounter+"_progress-bar-div' class='input-group' style='display: none;'>"+  
				"					<label>Processing Request &nbsp;&nbsp; </label>  "+
				"					<img src='img/processing-bar.gif'> "+
				"				</div> "+
				"			</div>  "+
				"		</div>  "+
				
				
				
				//PAGE_LIST GRID
				"	<div id='"+groupAttributeListCounter+"_listDivs' style='display:none'>"+
				"		<div class='col-md-12 no-padding gridBlock' > "+ 
				"			<div class='title2'> "+
				"				<spring:message code='parserConfiguration.page.config.grid.heading.label' ></spring:message>"+ 
				"					<span class='title2rightfield'> "+
				"						<sec:authorize access='hasAuthority(\'CREATE_PARSER\')'>"+
				"		          			<span class='title2rightfield-icon1-text'> "+
				"		          				<a href='#' onClick=displayAddEditPagePopup('ADD','0',\'"+groupAttributeListCounter+"\',retValOfElementById('newGroupAttributeId_"+groupAttributeListCounter+"'));><i class='fa fa-plus-circle'></i></a>"+
				"	  							<a href='#' name='" + groupAttributeListCounter +  "_addlink' onClick=displayAddEditPagePopup('ADD','0',\'"+groupAttributeListCounter+"\',retValOfElementById('newGroupAttributeId_"+groupAttributeListCounter+"'));> "+
				"	  								<spring:message code='btn.label.add' ></spring:message>  "+
				"	  							</a> "+
				"	      					</span>	  "+
				"						</sec:authorize>"+
				"						<sec:authorize access='hasAuthority(\'DELETE_PARSER\')'>"+
				"		          			<span class='title2rightfield-icon1-text'> "+
				
				"								<a href='#' onclick=displayDeletePagePopup('"+groupAttributeListCounter+"',retValOfElementById('newGroupAttributeId_"+groupAttributeListCounter+"'));"+
				"									<i class='fa fa-trash'></i>"+
				"								</a> "+
				"								<a href='#' onclick=displayDeletePagePopup('"+groupAttributeListCounter+"',retValOfElementById('newGroupAttributeId_"+groupAttributeListCounter+"'));>"+ 
				"									<spring:message code='btn.label.delete' ></spring:message>"+
				"								</a> "+
				
				
				
				"		          			</span> "+
				"						</sec:authorize>"+
				"		          	</span> "+
				"			</div> "+
				"		</div> "+
				"		<div class='col-md-12 no-padding gridBlock'> "+ 
				"			<div class='box-body table-responsive no-padding box'> "+
				"            	<table class='table table-hover' id='"+groupAttributeListCounter+"_pdfPageList'></table> "+
				"               	<div id='"+groupAttributeListCounter+"_pdfPageListDiv' ></div> "+
				"	           	<div class='clearfix'></div> "+
				"	           	<div id='divLoading' align='center' style='display: none;'><img src='img/preloaders/Preloader_10.gif' /></div> "+  
				"           </div> "+
				"		</div> "+
				
				
				
				
				//ATTRIBUTE_LIST GRID
				"		<div class='col-md-12 no-padding gridBlock' > "+ 
				"			<div class='title2'> "+
				"				<spring:message code='parserConfiguration.attribute.grid.heading.label' ></spring:message>"+ 
				"					<span class='title2rightfield'> "+
				"						<sec:authorize access='hasAuthority(\'CREATE_PARSER\')'>"+
				"							<span class='title2rightfield-icon1-text'>"+
				"						     	<a href='#' onclick=displayAddEditPopupForAL_In_Group('ADD','0',\'"+groupAttributeListCounter+"');>"+
				"									<i class='fa fa-plus-circle'></i>"+
				"								</a>"+ 
				"								<a href='#' onclick=displayAddEditPopupForAL_In_Group('ADD','0','"+groupAttributeListCounter+"');>"+ 
				"									<spring:message code='parserConfiguration.attribute.grid.addattribute.label' ></spring:message>"+
				"								</a> "+
													
				"							</span> "+
				"						</sec:authorize>"+
				"						<sec:authorize access='hasAuthority(\'DELETE_PARSER\')'>"+
				"							<span class='title2rightfield-icon1-text'> "+
				"								<a href='#' onclick=displayDeleteAttributePopupGA('"+groupAttributeListCounter+"');"+
				"									<i class='fa fa-trash'></i>"+
				"								</a> "+
				"								<a href='#' onclick=displayDeleteAttributePopupGA('"+groupAttributeListCounter+"');>"+ 
				"									<spring:message code='parserConfiguration.attribute.grid.deleteattribute.label' ></spring:message>"+
				"								</a> "+
				"							</span>"+
				"						</sec:authorize>"+
				"                     <span class='title2rightfield-icon1-text'>"+
				"					  <a id='"+groupAttributeListCounter+"_downloadCSVLink' >" + 
				"  					    <i class='fa fa-download'></i>"+ 
				"                     </a>" +
				"                     <a href='#' id='"+groupAttributeListCounter+"_downloadCSVLinkLabel'> "+
				"                     <spring:message code='parserConfiguration.attribute.grid.exportattribute.label'></spring:message>"+
				"                      </a>"+
				"                    </span>"+
				"                    <sec:authorize access='hasAuthority(\'CREATE_PARSER\')'>"+
				"                    <span class='title2rightfield-icon1-text'>"+
			    "	                 <a href='#' id='"+groupAttributeListCounter+"_uploadCSVLink'>"+
				"	                 <i class='fa fa-upload'></i>"+
				"	                 </a>"+ 
				"                    <a href='#' id='"+groupAttributeListCounter+"_uploadCSVLinkLabel'>"+
				"                    <spring:message code='parserConfiguration.attribute.grid.uploadattribute.label' ></spring:message>"+
				"                    </a>"+ 
				"                    </span>"+
				"                   </sec:authorize>"+ 				
				"		          	</span> "+
				"			</div> "+
				"		</div> "+
				"		<div class='col-md-12 no-padding gridBlock'> "+ 
				"			<div class='box-body table-responsive no-padding box'> "+
				"            	<table class='table table-hover' id='"+groupAttributeListCounter+"_pdfAttributeList'></table> "+
				"               	<div id='"+groupAttributeListCounter+"_pdfAttributeListDiv'></div> "+
				"	           	<div class='clearfix'></div> "+
				"	           	<div id='divLoading' align='center' style='display: none;'><img src='img/preloaders/Preloader_10.gif' /></div> "+  
				"           </div> "+
				"		</div> "+
				"	</div >"+
				
				
				
				
				"	</div> "+
				"</div> "+
				"</div> ";
	$('#groupAttributeList').prepend(html);
	createPDFAttributeGrid('<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>', groupAttributeListCounter);
	createPDFPageConfigGrid('<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>', groupAttributeListCounter);
	//$("#"+groupAttributeListCounter+"_block").collapse("toggle");
	//$('#'+groupAttributeListCounter+'_block .gridBlock').hide();
	
}


function saveGroupAttrirbuteConfigDetailDetail(counter,actionType, id){
	
	showProgressBar(counter);
	$.ajax({
		url: '<%=ControllerConstants.ADD_EDIT_PARSER_GROUP_BASIC_DETAILS_ATTRIBUTE%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"id" 			   : id,
			"name"					   : $('#'+counter+'_name').val(),
			"tableStartIdentifier"			   : $('#'+counter+'_tableStartIdentifier').val(),
			"tableEndIdentifier"			   : $('#'+counter+'_tableEndIdentifier').val(),
			"tableStartIdentifierCol"			   : $('#'+counter+'_tableStartIdentifierCol').val(),
			"tableEndIdentifierCol"			   : $('#'+counter+'_tableEndIdentifierCol').val(),
			
			"tableEndIdentifierRowLocation"	   : $('#'+counter+'_tableEndIdentifierRowLocation').val(),
			"tableEndIdentifierOccurence"			   : $('#'+counter+'_tableEndIdentifierOccurence').val(),
			"tableRowIdentifier"			   : $('#'+counter+'_tableRowIdentifier').val(),
			
			"actionType" 			: actionType,
			"plugInType"    		: '${plugInType}',
			"mappingId"    			: parseInt('${mappingId}'),
			
		}, 
		success: function(data){
			hideProgressBar(counter);

			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				
				var respObj = eval(responseObject);
				
				var groupAttributeId = respObj['id'];
				
				// set pathlist id hidden for all pathlist block
				
				var updateOnlick = 'saveGroupAttrirbuteConfigDetailDetail(';
				updateOnlick = updateOnlick+groupAttributeListCounter+','+'"UPDATE"'+','+groupAttributeId+');'
				$('#'+counter+'_updatebtn').attr('onclick', updateOnlick);
				
				//changing onclick of downloadMappingList
				var updateOnlickDownload = 'addColNames("#';
				updateOnlickDownload = updateOnlickDownload+groupAttributeListCounter+'_pdfAttributeList");downloadCSVFile('+groupAttributeId+');';
				$('#'+counter+'_downloadCSVLink').attr('onclick', updateOnlickDownload);
				$('#'+counter+'_downloadCSVLinkLabel').attr('onclick', updateOnlickDownload);
				
				//changing onclick of uploadMappingList
				var updateOnlickUpload = 'setUploadPopUpStyle();openUploadAttrPopupData("#';
				updateOnlickUpload = updateOnlickUpload+groupAttributeListCounter+'_pdfAttributeList",'+groupAttributeId+','+groupAttributeListCounter+');';
				$('#'+counter+'_uploadCSVLink').attr('onclick', updateOnlickUpload);
				$('#'+counter+'_uploadCSVLinkLabel').attr('onclick', updateOnlickUpload);
				
				$('#'+counter+'_groupAttributeId').val(groupAttributeId);
				$('#newGroupAttributeId_'+counter).val(groupAttributeId);
				$('#'+counter+'_listDivs').show();
				// update block title with path list name
				$('#title_'+counter).html(respObj['name']);
				
				$('#'+counter+'_block .gridBlock').show();
				//dsiplayGridForPathlist(counter,[]);
 				
	 			$('#'+counter+'_updatebtn').show();
	 			$('#'+counter+'_addbtn').hide();
	 			
	 			showSuccessMsg(responseMsg);
 				closeFancyBox();
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				hideProgressBar(counter);
				addErrorIconAndMsgForAjaxGA(responseObject,counter); 
			}else{
				hideProgressBar(counter);
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(counter);
			handleGenericError(xhr,st,err);
		}
	});
}

function createPDFAttributeGrid(defaultRowNum, counter){				
	$("#"+counter+'_pdfAttributeList').jqGrid({	    	
        datatype: "local",
        colNames:[
                  "#",
                  "<input type='checkbox' id='"+counter+"_selectAllAttribute' onclick='attributeHeaderCheckbox(event, this)'></input>",
                  jsSpringMsg.unifiedFieldName,
                  jsSpringMsg.sourceField,
                  jsSpringMsg.description,
                  jsSpringMsg.referenceRow,
                  jsSpringMsg.referenceCol,
                  jsSpringMsg.columnStartsWith,
                  jsSpringMsg.defaultVal,
                  jsSpringMsg.trimChar,
                  jsSpringMsg.trimPosition,
                  jsSpringMsg.tableFooter,
                  jsSpringMsg.multiLineAttribute,
                  jsSpringMsg.rowTextAlignment,
                  jsSpringMsg.mandatory,
                  jsSpringMsg.additionalinfo,
                  jsSpringMsg.updateAction,
                 ],
		colModel:[
		          {name: 'id', index: 'id',hidden:true,sortable:false,search : false},
		          {name: 'checkbox', index: 'checkbox',sortable:false,formatter:pdfAttributeCheckboxFormatter, align : 'center', width:'30%',search : false},
				  {name: 'unifiedField',index: 'unifiedField',sortable:true},
				  {name: 'sourceField',index: 'sourceField',sortable:false,search : false},
				  {name: 'description',index: 'description',hidden:false,sortable:false,search : false},
				  {name: 'referenceRow',index: 'referenceRow',hidden:false,search : false},
				  {name: 'referenceCol',index: 'referenceCol',hidden:false,search : false},
				  {name: 'columnStartsWith',index: 'columnStartsWith',hidden:false,search : false},
				  {name: 'defaultText',index: 'defaultText',hidden:false,search : false},
				  {name: 'trimChar',index: 'trimChar', hidden:false,sortable:false,search : false},
				  {name: 'trimPosition',index: 'trimPosition', sortable:false , hidden:false,search : false},
				  {name: 'tableFooter',index: 'tableFooter',hidden:false,search : false},
				  {name: 'multiLineAttribute',index: 'multiLineAttribute',hidden:false,search : false},
				  {name: 'rowTextAlignment',index: 'rowTextAlignment',hidden:false,search : false},
				  {name: 'mandatory',index: 'mandatory',hidden:false,search : false},
				  {name: 'info',index: 'info', hidden:true, formatter: function (cellvalue, options, rowObject) {
					    return additionInfoFormatterGA(cellvalue, options, rowObject,counter);},
					    align:'center',sortable:false,search : false},
				  {name: 'update',index: 'update', align:'center',sortable:false,formatter:function (cellvalue, options, rowObject) {
					    return pdfUpdateFormatter(cellvalue, options, rowObject,counter);},search : false}, 
				        	
        ],
        rowNum:defaultRowNum,
        rowList:[10,20,50,100,200],
        height: 'auto',
        pager: '#'+counter+'_pdfAttributeListDiv',
        viewrecords: true,
        search:true,
        ignoreCase: true,
        timeout : 120000,
        loadtext: 'Loading...',
        caption: jsSpringMsg.caption,
        beforeRequest:function(){
            $(".ui-dialog-titlebar").hide();
        }, 
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
 		loadComplete: function(data) {
		}, 
		onPaging: function (pgButton) {
			clearResponseMsgDiv();
		},
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},			
		recordtext: jsSpringMsg.recordtext,
        emptyrecords: jsSpringMsg.emptyrecords,
		loadtext: jsSpringMsg.loadtext,
		pgtext : jsSpringMsg.pgtext,
 	}).navGrid("#"+counter+"_pdfAttributeListDiv",{edit:false,add:false,del:false,search:true});
	$(".ui-jqgrid-titlebar").hide();
	$("#"+counter+'_pdfAttributeList').jqGrid('filterToolbar',{
		stringResult: true,
		searchOperators: false,
		searchOnEnter: false, 
		defaultSearch: "cn"
    });	
	
	$("#"+counter+'_pdfAttributeList').sortableRows();   
	$("#"+counter+'_pdfAttributeList').disableSelection();
    $("#"+counter+'_pdfAttributeList').sortable({
    	items: 'tr:not(:first)'
    });
    /* $("#"+counter+'_pdfAttributeList').navButtonAdd("#" +counter+"_pdfAttributeListDiv", {
       title: "Update Order",
       caption: "Update Order",
       position: "last",
       onClickButton: function(){updateOrderGa(counter)}
    }); */
}
	
function getAllPDFAttributeListByMappingId(attributeList,counter){
	
			
	if(attributeList!=null && attributeList != 'undefined' && attributeList != undefined){					
		var gridArray = [];
		$.each(attributeList,function(index,attribute){	
				var rowData = {};
				rowData.id					= parseInt(attribute.id);
				rowData.checkbox			= attribute.checkbox;
				rowData.unifiedField		= attribute.unifiedField;
				rowData.sourceField			= attribute.sourceField;
				rowData.description			= attribute.description;
				rowData.referenceRow   		= attribute.referenceRow;
				rowData.referenceCol   		= attribute.referenceCol;
				rowData.multiLineAttribute  = attribute.multiLineAttribute;
				rowData.rowTextAlignment   	= attribute.rowTextAlignment;
				rowData.mandatory   		= attribute.mandatory;
				rowData.columnStartsWith   	= attribute.columnStartsWith;
				rowData.tableFooter   		= attribute.tableFooter;
				rowData.defaultText   		= attribute.defaultValue;
				rowData.trimChar			= attribute.trimChars;
				rowData.trimPosition		= attribute.trimPosition;
				rowData.info				= attribute.info;
				rowData.updateAction		= attribute.updateAction;
 				gridArray.push(rowData);
		});
		jQuery("#"+counter+'_pdfAttributeList').jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
	} 
	$('.fancybox-wrap').css('top','10px');
			
}
function pdfAttributeCheckboxFormatter(cellvalue, options, rowObject, counter) {
	var uniqueId = "#"+counter+"_checkbox";
	if(rowObject["id"] && rowObject["id"] !== 'null' && rowObject["id"] !== "") {
		uniqueId += "_" + rowObject["id"];
	}
	return '<input type="checkbox" id="'+uniqueId+'" name="attributeCheckbox" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \'pdfAttributeList\')"/>';
}

/* Function will add edit link in attribute link */
function pdfUpdateFormatter(cellvalue, options, rowObject,counter){
	
	var uniqueId = "#"+counter+"_editAttribute";
	if(rowObject["id"] && rowObject["id"] !== 'null' && rowObject["id"] !== "") {
		uniqueId += "_" + rowObject["id"];
	}
	return "<a href='#' id='"+uniqueId+"' class='link' onclick=displayAddEditPopupForAL_In_Group(\'EDIT\','"+rowObject["id"]+"',\'"+counter+"\'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
}


function pdfUpdatePageFormatter(cellvalue, options, rowObject,counter){
	var gId=$("#"+counter+"_groupAttributeId").val();
	var uniqueId = "#"+counter+"_editPage";
	if(rowObject["id"] && rowObject["id"] !== 'null' && rowObject["id"] !== "") {
		uniqueId += "_" + rowObject["id"];
	}
	return "<a href='#' id='"+uniqueId+"' class='link' onclick=displayAddEditPagePopup(\'EDIT\','"+rowObject["id"]+"',\'"+counter+"\',\'"+gId+"\'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
}


function createPDFPageConfigGrid(defaultRowNum, counter){				
	 
	$("#"+counter+'_pdfPageList').jqGrid({	    	
        datatype: "local",
        colNames:[
                  "#",
                  "<input type='checkbox' id='"+counter+"_selectAllPage' onclick='attributeHeaderCheckbox(event, this)'></input>",
                  jsSpringMsg.pageNumber,
                  jsSpringMsg.pageSize,
                  jsSpringMsg.tableLocation,
                  jsSpringMsg.tableCols,
                  jsSpringMsg.extractionMethod,
                  jsSpringMsg.updateAction,
                 ],
		colModel:[
		          {name: 'id', index: 'id',hidden:true,sortable:false,search : false},
		          {name: 'checkbox', index: 'checkbox',sortable:false, formatter:pdfAttributeCheckboxFormatter,align : 'center', width:'30%',search : false},
		          {name: 'pageNumber',index: 'pageNumber',sortable:false,search:false},
		          {name: 'pageSize',index: 'pageSize',sortable:false,search:false},
				  {name: 'tableLocation',index: 'tableLocation',sortable:false,search : false},
				  {name: 'tableCols',index: 'tableCols',sortable:false,search : false},
				  {name: 'extractionMethod',index: 'extractionMethod',sortable:false,search : false},
				  {name: 'update',index: 'update', align:'center',sortable:false,formatter:function (cellvalue, options, rowObject) {
					    return pdfUpdatePageFormatter(cellvalue, options, rowObject,counter);},search : false},
				        	
        ],
        rowNum:defaultRowNum,
        rowList:[10,20,50,100,200],
        height: 'auto',
        pager: '#'+counter+'_pdfPageListDiv',
        viewrecords: true,
        search:false,
        ignoreCase: false,
        timeout : 120000,
        loadtext: 'Loading...',
        caption: jsSpringMsg.caption,
        beforeRequest:function(){
            $(".ui-dialog-titlebar").hide();
        }, 
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
 		loadComplete: function(data) {
		}, 
		onPaging: function (pgButton) {
			clearResponseMsgDiv();
		},
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},			
		recordtext: jsSpringMsg.recordtext,
        emptyrecords: jsSpringMsg.emptyrecords,
		loadtext: jsSpringMsg.loadtext,
		pgtext : jsSpringMsg.pgtext,
 	}).navGrid("#" +counter+"_pdfPageListDiv",{edit:false,add:false,del:false,search:false});
	$(".ui-jqgrid-titlebar").hide();
	/* $('#'+counter+'_pdfPageList').jqGrid('filterToolbar',{
		stringResult: true,
		searchOperators: false,
		searchOnEnter: false, 
		defaultSearch: "cn"
    }); */
	
	$("#"+counter+'_pdfPageList').sortableRows();   
	$("#"+counter+'_pdfPageList').disableSelection();
    $("#"+counter+'_pdfPageList').sortable({
    	items: 'tr:not(:first)'
    });
    
  /*   $("#"+counter+'_pdfPageList').navButtonAdd("#" +counter+"_pdfPageListDiv", {
       title: "Update Order",
       caption: "Update Order",
       position: "last",
       onClickButton: updateOrder
    }); */
}
	
	
function getAllPDFPageConfig(pageList,counter){
	
	if(pageList!=null && pageList != 'undefined' && pageList != undefined){					
		var gridArray = [];
		var gridGroupArray = [];
		$.each(pageList,function(index,page){	
				var rowData = {};
				rowData.id					= parseInt(page.id);
				rowData.checkbox			= page.checkbox;
				rowData.pageSize   			= page.pageSize;
				rowData.pageNumber   		= page.pageNumber;
				rowData.extractionMethod 	= page.extractionMethod;
				rowData.tableLocation		= page.tableLocation;
				rowData.tableCols			= page.tableCols;
				rowData.updateAction		= page.updateAction;
				gridArray.push(rowData);
		});
		jQuery("#"+counter+'_pdfPageList').jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
	}
	$('.fancybox-wrap').css('top','10px');
			
}


function additionInfoFormatterGA(cellvalue, options, rowObject,counter){
	return "<a href='#' class='link' onclick=displayAdditionalInfoGA('"+rowObject["id"]+"',\'"+counter+"\'); ><i class='fa fa-info-circle' aria-hidden='true'></i></a>";
}

function displayAdditionalInfoGA(attributeId,counter){
	var pdfResponseObject = jQuery("#"+counter+"_pdfAttributeList").jqGrid('getRowData', attributeId);
	$("#locationPopUp").val(pdfResponseObject.location);
	$("#columnStartLocationPopUp").val(pdfResponseObject.columnStartLocation);
	$("#columnIdentifierPopUp").val(pdfResponseObject.columnIdentifier);
	$("#referenceRowPopUp").val(pdfResponseObject.referenceRow);
	$("#referenceColPopUp").val(pdfResponseObject.referenceCol);
	$("#columnStartsWithPopUp").val(pdfResponseObject.columnStartsWith);
	
	$("#tableFooterPopUp").val(pdfResponseObject.tableFooter);
	$("#multiLineAttributePopUp").val(pdfResponseObject.multiLineAttribute);
	$("#rowTextAlignmentPopUp").val(pdfResponseObject.rowTextAlignment);
	$("#mandatoryPopUp").val(pdfResponseObject.mandatory);
	
	$("#view_attribute").click();
}
function displayAddEditPopupForAL_In_Group(actionType,attributeId,counter){
	 clearAllMessagesPopUp();
	 resetWarningDisplayPopUp();
	 resetParserAttributeParams();
	 setGroupAttCounter(counter);
	$("#"+counter+"_selectAllAttribute").prop('checked',false);
	if(actionType === 'ADD'){
		
		$("#addNewAttribute").show();
		$("#"+editAttributeBtnIdForParser).hide();
		$("#add_label").show();
		$("#update_label").hide();
		$("#sourceFieldFormat").val($("#sourceFieldFormat option:first").val());
		$("#id").val(0);
		$("#add_edit_attribute").click();
	}else{
	
		if(attributeId > 0){

			var responseObject="";
			
			responseObject = jQuery("#"+counter+"_pdfAttributeList").jqGrid('getRowData', attributeId);

			$("#addNewAttribute").hide();
			$("#update_label").show();
			$("#add_label").hide();
			
			$("#id").val(responseObject.id);
			$("#sourceField").val(responseObject.sourceField);
			$("#unifiedField").val(responseObject.unifiedField);
			$("#description").val(responseObject.description);
			$("#trimChars").val(responseObject.trimChar);
			$("#trimPosition").val(responseObject.trimPosition);
			$("#defaultValue").val(responseObject.defaultText);
			$("#sourceField").val(responseObject.sourceField);
			$("#location").val(responseObject.location);
			$("#columnStartLocation").val(responseObject.columnStartLocation);
			$("#columnIdentifier").val(responseObject.columnIdentifier);
			$("#referenceRow").val(responseObject.referenceRow);
			$("#referenceCol").val(responseObject.referenceCol);
			$("#columnStartsWith").val(responseObject.columnStartsWith);
			$("#tableFooter").val(responseObject.tableFooter);
			
			$("#multiLineAttribute").val(responseObject.multiLineAttribute);
			$("#rowTextAlignment").val(responseObject.rowTextAlignment);
			$("#mandatory").val(responseObject.mandatory);
			
			$("#"+editAttributeBtnIdForParser).show();
			$("#add_edit_attribute").click();
			
		}else{
			showErrorMsg(jsSpringMsg.failUpdateAtributeMsg);
		}
	}
}

function disableGroupAttributeDetail(){
	
	var flag='${readOnlyFlagGroup}';
	if(flag =='true' ){
		$("#group_attribute_details_div_mask").remove(); 
		
		$('#group_attribute_details_div').fadeTo('slow',.6);
		$element = $('#group_attribute_details_div');

		var top = Math.ceil($element.position().top);
		var elementStr = "<div id='group_attribute_details_div_mask' style='position: absolute;top:"+top+"px;left:0;width: 100%;height:100%;z-index:2;opacity:0.6;filter: alpha(opacity = 50)'></div>";
		$element.append(elementStr);
	}
	 
} 


function getAttributeListByGroupId(urlAction, groupId, mappingId, counter){ //get and reload
	
	$grid = $("#"+counter+"_pdfAttributeList");
	jQuery("#"+counter+"_pdfAttributeList").jqGrid('clearGridData');
	clearAttributeGrid();
	
	$.ajax({
		url: urlAction,
		cache: false,
		async: false,
    	data:
    		{
    		   'groupId':groupId,
			   'mappingId':mappingId
    		},
		dataType: 'json',
		type: "POST",
		success: function(data){						
			var response = eval(data);			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;			
			if(responseCode == "200"){				
				$("#"+counter+"_pdfAttributeListDiv").show();				
				var attributes=eval(responseObject);				
				var attributeList=attributes['attributeList'];
				
				if(attributeList!=null && attributeList != 'undefined' && attributeList != undefined){					
					var gridArray = [];
					var gridGroupArray = [];
					$.each(attributeList,function(index,attribute){	
		 				var rowData = {};
		 				rowData.id					= parseInt(attribute.id);
		 				rowData.checkbox			= attribute.checkbox;
		 				rowData.unifiedField		= attribute.unifiedField;
		 				rowData.sourceField		= 	attribute.sourceField;
		 				rowData.description			= attribute.description;
		 				rowData.trimChar			= attribute.trimChar;
		 				rowData.trimPosition		= attribute.trimPosition;
		 				rowData.info				= attribute.info;
		 				rowData.updateAction		= attribute.updateAction;
		 				rowData.defaultText   		= attribute.defaultText;
		 				
		 				rowData.location   		    = attribute.location;
		 				rowData.columnStartLocation = attribute.columnStartLocation;
		 				rowData.columnIdentifier   	= attribute.columnIdentifier;
		 				rowData.referenceRow   		= attribute.referenceRow;
		 				rowData.referenceCol   		= attribute.referenceCol;
		 				rowData.columnStartsWith   	= attribute.columnStartsWith;
		 				rowData.tableFooter   		= attribute.tableFooter;
		 				
		 				rowData.multiLineAttribute  = attribute.multiLineAttribute;
		 				rowData.rowTextAlignment   	= attribute.rowTextAlignment;
		 				rowData.mandatory   		= attribute.mandatory;
		 				
			 			gridArray.push(rowData);
		 			});
					jQuery("#"+counter+"_pdfAttributeList").jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
				} 
				$('.fancybox-wrap').css('top','10px');
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				console.log("got 400 response.");
			}
		},
	    error: function (xhr,st,err){	    		    	
			handleGenericError(xhr,st,err);
		}
	});
}

function resetGroupAttribute(counter){
	$("#"+counter+"_name").val('');
	$("#"+counter+"_tableStartIdentifier").val('');
	$("#"+counter+"_tableEndIdentifier").val('');
	$("#"+counter+"_tableStartIdentifierCol").val('');
	$("#"+counter+"_tableEndIdentifierCol").val('');
	
	$("#"+counter+"_tableEndIdentifierRowLocation").val('');
	$("#"+counter+"_tableEndIdentifierOccurence").val('');
	$("#"+counter+"_tableRowIdentifier").val('');
}

/*Function will display delete attribute popup */
function displayDeleteAttributePopupGA(counter){
	setGroupAttCounter(counter);
	ckIntanceSelected = [];
    $("#"+counter+"_pdfAttributeList").find('input[name=attributeCheckbox]:checked').each( function () {
    	ckIntanceSelected.push($(this).val());
    });
	
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearAllMessages();
	
	if(ckIntanceSelected.length === 0){
		$("#lessWarn").show();
		$("#moreWarn").hide();
		$("#delete_warn_msg_link").click();
		return;
	}else{
		
		var tableString ="<table class='table table-hover table-bordered'  border='1' >";
		tableString+="<th>#</th>"
		tableString+="<th>"+jsSpringMsg.attributeName+"</th>";
		tableString+="<th>"+jsSpringMsg.unifiedFieldName+"</th><th>"+jsSpringMsg.description+"</th>";

		for(var i = 0 ; i< ckIntanceSelected.length;i++){
			var rowData='';
			rowData = $("#"+counter+"_pdfAttributeList").jqGrid('getRowData', ckIntanceSelected[i]);
			tableString += "<tr>";
			tableString += "<td><input type='checkbox' name='attribute_delete' id='attribute_"+ckIntanceSelected[i]+"' checked  onclick=getSelecteAttributeList('"+ckIntanceSelected[i]+"')  value='"+ckIntanceSelected[i]+"' /> </td>";
			tableString += "<td>"+rowData.sourceField+" </td>";
			tableString += "<td>"+rowData.unifiedField+"</td>";
			tableString += "<td>"+rowData.description+"</td>";
			tableString += "</tr>";
		}	
		tableString+="</table>";
		
		$("#delete_selected_attribute_details").html(tableString);
		$("#delete_attribute_bts_div").show();
		$("#delete_attribute_progress_bar_div").hide();
		$("#delete_close_attribute_buttons_div").hide();
		$("#deleteAttributeId").val(ckIntanceSelected.toString());
		$("#delete_attribute").click();
	}
}

var groupConfigCnt='';
function deleteGroupAttributeConfig(counter,groupId){
	groupConfigCnt = counter;

	if(groupId == -1){
		var element = document.getElementById('flipbox_'+counter);
	    element.parentNode.removeChild(element);
	}else{
		$("#groupId").val(groupId);
		$("#closeBtn").hide();
		$("#groupDelMessage").click();
	}
}

function deleteGroupDetails(){
	resetWarningDisplay();
	clearAllMessages();
	$("#delete-popup-buttons").hide();
	$("#delete-progress-bar-div").show();
	 $.ajax({
		url: '<%=ControllerConstants.DELETE_GROUP_ATTRIBUTE_WITH_HIERARCHY%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"groupId"   : $("#groupId").val(),
			"mappingId"   : parseInt('${mappingId}'),
		 }, 
		
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				$("#delete-popup-buttons").show();
				$("#delete-progress-bar-div").hide();
				closeFancyBox();
				showSuccessMsg(responseMsg);
				$("#flipbox_"+groupConfigCnt).remove();
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				showErrorMsgPopup(responseMsg);
				$("#delete-progress-bar-div").hide();
				$("#closeBtn").show();
			}else{
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsgPopup(responseMsg);
				$("#delete-progress-bar-div").hide();
				$("#closeBtn").show();
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

var cnt = '';
var pageGroupId = '';
function displayAddEditPagePopup(actionType,pageId, counter, groupId){
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	resetParserAttributeParams();
	$("#"+counter+"_selectAllPage").prop('checked',false);
	cnt = counter;
	pageGroupId = groupId;
	if(actionType === 'ADD'){
		
		$("#addNewPage").show();
		$("#editPageForParser").hide();
		$("#add_page_label").show();
		$("#update_page_label").hide();
		
		$("#pageConfigId").val('0');
		$("#pageSize").val('');
		$("#tableLocation").val('');
		$("#tableCols").val('');
		$("#extractionMethod").val('');
		$("#pageNumber").val('');
		
		$("#add_edit_page").click();
	}else{
		if(pageId > 0){

			var responseObject="";
			responseObject = jQuery("#"+counter+"_pdfPageList").jqGrid('getRowData', pageId);

			$("#addNewPage").hide();
			$("#update_page_label").show();
			$("#editPageForParser").show();
			$("#add_page_label").hide();
			
			$("#pageConfigId").val(responseObject.id);
			$("#pageSize").val(responseObject.pageSize);
			$("#pageNumber").val(responseObject.pageNumber);
			$("#extractionMethod").val(responseObject.extractionMethod);
			$("#tableLocation").val(responseObject.tableLocation);
			$("#tableCols").val(responseObject.tableCols);
			$("#"+counter+"_selectAllPage").show();
			$("#add_edit_page").click();
			
		}else{
			showErrorMsg(jsSpringMsg.failUpdateAtributeMsg);
		}
	}
}

function createUpdateParserPageConfig(inputUrl, pageId, actionType){
	var dateFormat='';
	var sourceFieldFormat='';
	var ASN1DataType = '';
	
	$.ajax({
		url: inputUrl,
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:  {
			"id" 					: parseInt($("#pageConfigId").val()),
			"groupId"		   		: parseInt(pageGroupId),
			"pageSize"  			: $("#pageSize").val() ,
			"pageNumber"  			: $("#pageNumber").val() ,
			"extractionMethod"  			: $("#extractionMethod").val() ,
			"tableLocation"  		: $("#tableLocation").val() ,
			"tableCols"   			: $("#tableCols").val(),
			"actionType" 			: actionType
		}, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			if(responseCode === "200"){
				closeFancyBox();
				closeFancyBoxFromChildIFrame();
				var url = 'getPageConfigListByGroupId';
				getAndReloadPageConfigListByGroupId(url,pageGroupId, cnt );
				showSuccessMsg(responseMsg);
			}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
				addErrorIconAndMsgForAjaxPopUp(responseObject);
			}else{
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err); 
		}
	});
}

function getAndReloadPageConfigListByGroupId(urlAction, groupId, counter){ //get and reload
	
	$grid = $("#"+counter+"_pdfPageList");
	jQuery("#"+counter+"_pdfPageList").jqGrid('clearGridData');
	clearAttributeGrid();
	$.ajax({
		url: urlAction,
		cache: false,
		async: false,
    	data:
    		{
    		   'groupId':groupId,
    		},
		dataType: 'json',
		type: "POST",
		success: function(data){						
			var response = eval(data);			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;			
			if(responseCode == "200"){				
				$("#"+counter+"_pdfPageListDiv").show();				
				var pageList=eval(responseObject);				
				var pageConfigList=pageList['pageConfigList'];
				
				if(pageConfigList!=null && pageConfigList != 'undefined' && pageConfigList != undefined){					
					var gridArray = [];
					$.each(pageConfigList,function(index,page){	
		 				var rowData = {};
		 				rowData.id					= parseInt(page.id);
		 				rowData.pageSize			= page.pageSize;
		 				rowData.extractionMethod  	= page.extractionMethod;
		 				rowData.pageNumber		  	= page.pageNumber;
		 				rowData.tableLocation		= page.tableLocation;
		 				rowData.tableCols			= page.tableCols;
		 				rowData.updateAction		= page.updateAction;
			 			gridArray.push(rowData);
		 			});
					jQuery("#"+counter+"_pdfPageList").jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
				} 
				$('.fancybox-wrap').css('top','10px');
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				console.log("got 400 response.");
			}
		},
	    error: function (xhr,st,err){	    		    	
			handleGenericError(xhr,st,err);
		}
	});
}

/*Function will display delete page popup */
function displayDeletePagePopup(counter, groupId){
	setGroupAttCounter(counter);
	cnt = counter;
	pageGroupId = groupId;
	ckIntanceSelected = [];
    $("#"+counter+"_pdfPageList").find('input[name=attributeCheckbox]:checked').each( function () {
    	ckIntanceSelected.push($(this).val());
    });
	
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearAllMessages();
	
	if(ckIntanceSelected.length === 0){
		$("#lessWarn").show();
		$("#moreWarn").hide();
		$("#delete_warn_msg_link").click();
		return;
	}else{
		
		var tableString ="<table class='table table-hover table-bordered'  border='1' >";
		tableString+="<th>#</th>"
		tableString+="<th>"+jsSpringMsg.pageSize+"</th>";
		tableString+="<th>"+jsSpringMsg.tableLocation+"</th><th>"+jsSpringMsg.tableCols+"</th><th>"+jsSpringMsg.pageNumber+"</th><th>"+jsSpringMsg.extractionMethod+"</th>";

		for(var i = 0 ; i< ckIntanceSelected.length;i++){
			var rowData='';
			rowData = $("#"+counter+"_pdfPageList").jqGrid('getRowData', ckIntanceSelected[i]);
			tableString += "<tr>";
			tableString += "<td><input type='checkbox' name='page_delete' id='page_"+ckIntanceSelected[i]+"' checked  onclick=getSelectePageList('"+ckIntanceSelected[i]+"')  value='"+ckIntanceSelected[i]+"' /> </td>";
			tableString += "<td>"+rowData.pageSize+" </td>";
			tableString += "<td>"+rowData.tableLocation+"</td>";
			tableString += "<td>"+rowData.tableCols+"</td>";
			tableString += "<td>"+rowData.pageNumber+"</td>";
			tableString += "<td>"+rowData.extractionMethod+"</td>";
			tableString += "</tr>";
		}	
		tableString+="</table>";
		
		$("#delete_selected_page_details").html(tableString);
		$("#delete_page_bts_div").show();
		$("#delete_page_progress_bar_div").hide();
		$("#delete_close_page_buttons_div").hide();
		$("#deletePageId").val(ckIntanceSelected.toString());
		$("#delete_page").click();
	}
}

/*Function will delete attribute  */
function deletePage(){
	
	$("#delete_page_bts_div").hide();
	$("#delete_page_progress_bar_div").show();
	$("#delete_close_page_buttons_div").hide();
	
	$.ajax({
		url: 'deletePage',
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:
		 {
			"pageId" : $("#deletePageId").val()
		 }, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
		
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			 
			if(responseCode === "200"){
				showSuccessMsgPopUp(responseMsg);
				$("#delete_page_bts_div").hide();
				$("#delete_page_progress_bar_div").hide();
				var url = 'getPageConfigListByGroupId';
				getAndReloadPageConfigListByGroupId(url,pageGroupId,cnt);
				closeFancyBox();
				$("#selectAllAttribute").prop('checked',false);
				ckIntanceSelected = new Array();
			}else{
				showErrorMsgPopUp(responseMsg);
				$("#delete_attribute_bts_div").show();
				$("#delete_attribute_progress_bar_div").hide();
				$("#delete_close_attribute_buttons_div").hide();
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

function getSelectePageList(elementId){
	
	if( document.getElementById("page_"+elementId).checked === true){
		if(ckIntanceSelected.indexOf(elementId) === -1){
			ckIntanceSelected.push(elementId);
		}
	}else{
		if(ckIntanceSelected.indexOf(elementId) !== -1){
			ckIntanceSelected.splice(ckIntanceSelected.indexOf(elementId), 1);
		}
	}
	$("#deletePageId").val(ckIntanceSelected.toString());
	
}

function retValOfElementById(id){
	var val = $("#"+id).val();
	if(val == undefined || val == '' || val == null){
		return '';
	}else{
		return val;
	}
}
function downloadCSVFile(groupId) {
	$("#sampleRequired").val('NO');
	$("#groupAttrId").val(groupId);
	$("#sample_lookup_table_form").submit();
}
function openUploadAttrPopupData(gridName,groupId,counter){
	addColNames(gridName);
	$('#groupId').val(groupId);
	setGroupAttCounter(counter);
	$("#uploadAttrData").click();
}

function setUploadPopUpStyle(){
	document.getElementById("UploadPopUpMsg").style.padding = "8px";
}

function addErrorIconAndMsgForAjaxGA(responseObject, counter){
	
	$.each(responseObject, function(key,val){
		if (key.indexOf(".") >= 0)
			key = key.replace('.','_');
		
		$('#'+counter+'_'+ key).next().children().first().attr("data-original-title",val);

		addErrorClassWhenTitleExist($("#GENERIC_ERROR_MESSAGE", window.parent.document).val());
	});

}
</script>
