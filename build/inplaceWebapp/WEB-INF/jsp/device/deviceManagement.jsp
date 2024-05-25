<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!-- Main device list and search device list div start here -->
<div class="tab-content no-padding clearfix" id="device-manager-block">

<!-- Search form Content start here -->
	<form name="search-device-mgmt-form"  action="javascript:;" id="search-device-mgmt-form">
		<sec:authorize access="hasAuthority('VIEW_DEVICE')">
			<div class="title2">
				<spring:message code="device.list.search.heading.label" ></spring:message>
			</div>
			<div class="fullwidth borbot">
				 <div class="col-md-6 inline-form" style="padding-left: 0px !important;">
		         	<div class="form-group">
		         		<spring:message code="device.list.grid.device.type.label"  var="tooltip"></spring:message>
		         		<div class="table-cell-label">${tooltip }</div>
		             	<div class="input-group ">
		             		<input type="text" id="search_device_type" name="search_device_type" maxlength="100" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
		             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             	</div>
		             </div>
		             
		             <div class="form-group">
		             	<spring:message code="device.list.grid.device.name.label" var="tooltip" ></spring:message>
		         		<div class="table-cell-label">${tooltip }</div>
		             	<div class="input-group ">
		             		<input type="text" id="search_device_name" name="search_device_name" maxlength="100" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
		             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
		             	</div>
		             </div>
		           	  <div class="form-group">
		             	<spring:message code="device.list.grid.device.mapping.name.label" var="tooltip" ></spring:message>
		         		<div class="table-cell-label">${tooltip }</div>
		             	<div class="input-group ">
		             		<input type="text" id="search_mapping_name" name="search_mapping_name" maxlength="100" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
		             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
		             	</div>
		             </div>
	     	  	</div> 
				
				<div class="col-md-6 inline-form">
		         	<div class="form-group">
		         		<spring:message code="device.list.grid.device.vendor.type.label" var="tooltip" ></spring:message>
		         		<div class="table-cell-label">${tooltip }</div>
		             	<div class="input-group " style="display: table;">
		             		<input type="text" id="search_vendor_type" name="search_vendor_type" maxlength="100" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
		             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             	</div>
									
		             </div>
		             <div class="form-group">
		             	<spring:message code="device.list.search.device.decode.type.label" var="tooltip" ></spring:message>
		         		<div class="table-cell-label">${tooltip }</div>
		             	<div class="input-group ">
		             		<select  name = "search_decode_type" class="form-control table-cell input-sm"  tabindex="3" id="search_decode_type" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
		             			<%-- <option  value="-1" selected="selected"><spring:message code="decode.type.dropdown.default.selection.label" ></spring:message> </option> --%>
				             		<c:forEach items="${decodeTypeEnum}" var="decodeTypeEnum">
				             			<option value="${decodeTypeEnum}">${decodeTypeEnum}</option>
				             		</c:forEach>
		             		</select>
		             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             	</div>
		             </div>
		             
		              <div class="form-group">
		         		<div class="table-cell-label">&nbsp;</div>
		             	<div class="input-group ">
		             		&nbsp;&nbsp;&nbsp;
		             	</div>
		             </div> 
	         	</div>
				<div class="clearfix"></div>
	     		<div class="pbottom15 form-group ">
	     			<sec:authorize access="hasAuthority('VIEW_DEVICE')">
	   					<button id="searchDevice_btn" class="btn btn-grey btn-xs" onclick="reloadGridData();"><spring:message code="btn.label.search" ></spring:message></button>
	    				<button id="ResetDevice_btn" class="btn btn-grey btn-xs" onclick="resetSearchParams();"><spring:message code="btn.label.reset" ></spring:message></button>
	    			</sec:authorize>
	   			</div>
			</div>
		</sec:authorize>
	</form>
	<!-- Search form Content end here -->
	
	<!-- Grid content start here -->
	<div class="tab-content no-padding clearfix">
         	<div class="fullwidth">
   				<div class="title2">
   					<spring:message code="device.tab.heading.label" ></spring:message>
	   				 <span class="title2rightfield">
				          
				          	<sec:authorize access="hasAuthority('ADD_DEVICE')">
					          	<span class="title2rightfield-icon1-text">
					          		<a href="#" onclick="addDevicePopup();"><i class="fa fa-plus-circle"></i></a>
		          					<a href="#" id="addDevice" onclick="addDevicePopup();">
		          						<spring:message code="btn.label.add" ></spring:message>
		          					</a>
			          			</span>	
				          	</sec:authorize>
				          	<sec:authorize access="hasAuthority('DELETE_DEVICE')">
				          		<span class="title2rightfield-icon1-text">
				          		<a href="#" onclick="displayDeleteDevicePopup();"> <i class="fa fa-trash"></i></a>
				          		<a href="#" id="deleteDevice" onclick="displayDeleteDevicePopup();"><spring:message code="btn.label.delete" ></spring:message></a>
				          		</span>
				          	</sec:authorize>
				          	<a href="#divAddDevice" class="fancybox" style="display: none;" id="addDevice_link">#</a>
				          	<a href="#divDeleteDevice" class="fancybox" style="display: none;" id="deleteDevice_link">#</a>
				          </span>
		          </div>
   			</div>
           	<!-- Morris chart - Sales -->
           	<sec:authorize access="hasAuthority('VIEW_DEVICE')">
            <div class="box-body table-responsive no-padding box">
             	<table class="table table-hover" id="deviceAndMappingList"></table>
               	<div id="deviceAndMappingListPagingDiv"></div> 
	           	<div class="clearfix"></div> 
	           	<div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>  
            </div>
            </sec:authorize>
        </div>
	<!-- Grid content end here -->
	<!-- Device Popoup code start here  -->
	<form:form modelAttribute="device_form_bean" method="POST" action="javascript:;" id="device-form">
	<div id="divAddDevice" style=" width:100%; display: none;" >
	    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title">
		            	<span style="display:none;" id="create_device_span">
		            		<spring:message code="device.create.popup.heading.label" ></spring:message>
		            	</span>
		            	<span style="display:none;" id="update_device_span">
		            		<spring:message code="device.update.popup.heading.label" ></spring:message>
		            	</span>
		            </h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        	<div class=fullwidth>
		        		<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		        	</div>
		        	<div class="fullwidth">
		        		<input type="hidden" value="ADD" id="deviceCurrentAction" name="deviceCurrentAction"/>
		        		 <form:hidden path="id" id="id" value="0"></form:hidden> 
		        		<div class="form-group">
	               			<spring:message code="device.list.grid.device.type.label" var="tooltip"></spring:message>
	            			<div class="table-cell-label">${tooltip}</div>
	                		<div class="input-group">
	                			
	                			<form:select path="deviceType.id" cssClass="form-control table-cell input-sm" id="deviceType_id" tabindex="1"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="getVendorByDeviceType('getVendorListByDeviceType',this.value,'vendorType_id',' ','form');displayDevicetypeandVendorTypeElement(this.value,'create_device_type_name_div');" >
			             			<c:choose>
				             			<c:when test="${deviceTypeList !=null && fn:length(deviceTypeList) gt 0}">
									        <c:forEach items="${deviceTypeList}" var="deviceType">
						             			<form:option value="${deviceType.id}" >${deviceType.name}</form:option>
						             		</c:forEach>
					             			 <form:option value="0"><spring:message code="device.dropdown.other.option.label" ></spring:message></form:option>
									    </c:when>
									    <c:otherwise>
									        <form:option value="0"><spring:message code="device.dropdown.other.option.label" ></spring:message></form:option>
									    </c:otherwise>
			             			</c:choose>
	                			</form:select>
	                				                			
								<spring:bind path="deviceType.id">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>  
	                		</div>
	               		</div>
	               		
	               		<div class="form-group" id="create_device_type_name_div" style="display:none;">
	               			<spring:message code="device.list.grid.device.type.name.label" var="tooltip"></spring:message>
	            			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
	                		<div class="input-group">
	                			<form:input path="deviceType.name" cssClass="form-control table-cell input-sm" id="deviceType_name" tabindex="1"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
								<spring:bind path="deviceType.name">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind> 	
	                		</div>
	               		</div>
	               		<div class="form-group">
	               			<spring:message code="device.list.grid.device.vendor.type.label" var="tooltip"></spring:message>
	            			<div class="table-cell-label">${tooltip}</div>
	                		<div class="input-group">
	                			<form:select path="vendorType.id" cssClass="form-control table-cell input-sm" id="vendorType_id" tabindex="1"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"  onchange="displayDevicetypeandVendorTypeElement(this.value,'create_vendor_type_name_div');">
	                			</form:select>
								<spring:bind path="vendorType.id">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind> 	
	                		</div>
	               		</div>
	               		<div class="form-group" id="create_vendor_type_name_div" style="display:none;">
	               			<spring:message code="device.list.grid.vendor.type.name.label" var="tooltip"></spring:message>
	            			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
	                		<div class="input-group">
	                			<form:input path="vendorType.name" cssClass="form-control table-cell input-sm" id="vendorType_name" tabindex="1"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" ></form:input>
								<spring:bind path="vendorType.name">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind> 	
	                		</div>
	               		</div>
	               		<div class="form-group">
	               			<spring:message code="device.list.search.device.decode.type.label" var="tooltip"></spring:message>
	            			<div class="table-cell-label">${tooltip}</div>
	                		<div class="input-group">
	                			<form:select path="decodeType" cssClass="form-control table-cell input-sm" id="decodeType" tabindex="1"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" >
		             					<%-- <form:option  value="-1" selected="selected"><spring:message code="decode.type.dropdown.default.selection.label" ></spring:message> </form:option> --%>
					             		<c:forEach items="${decodeTypeEnum}" var="decodeTypeEnum">
					             			<form:option value="${decodeTypeEnum}">${decodeTypeEnum}</form:option>
					             		</c:forEach>
		             			</form:select>
								<spring:bind path="decodeType">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind> 	
	                		</div>
	               		</div>
	               		<div class="form-group">
	               			<spring:message code="device.list.grid.create.device.name.label" var="tooltip"></spring:message>
	            			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
	                		<div class="input-group">
	                			<form:input path="name" cssClass="form-control table-cell input-sm" id="name" tabindex="1"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
								<spring:bind path="name">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind> 	
	                		</div>
	               		</div>
		        	</div>
		        </div>
		        <sec:authorize access="hasAuthority('ADD_DEVICE')">
		        <div class="modal-footer padding10" id="add-device-buttons-div">
		            <button type="button" id="create_device_btn" class="btn btn-grey btn-xs " onclick="createDevice();"><spring:message code="btn.label.save"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
		        </div>
		        <div class="modal-footer padding10" id="close-device-buttons-div" style="display:none;">
		            <button type="button" class="btn btn-grey btn-xs " onclick="closeFancyBox();reloadGridData();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		        </sec:authorize>
		        <div id="device_proccess_bar_div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
		    </div>
		    <!-- /.modal-content -->
	</div>
	</form:form>	
		<a href="#divDeleteDevice" class="fancybox" style="display: none;" id="delete_device_and_mapping_link">#</a>
	
	<div id="divDeleteDevice" style=" width:100%; display: none;" >
	   <div class="modal-content">
	        <div class="modal-header padding10">
	            <h4 class="modal-title"><spring:message code="device.delete.popup.heading.label"></spring:message></h4>
	        </div>
	        <div class="modal-body padding10 inline-form">
	        	<div class=fullwidth>
	        		<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
	        	</div>
	        	<div class="modal-body padding10 inline-form">
	        	<div class="nav-tabs-custom tab-content">
	        	 <ul class="nav nav-tabs pull-right">
		        	<li id="mapping_selection_to_delete_tab" class="active" onclick="reloadPopUpMappingData();">
			        	<a href="#mapping_selection_to_delete" id="mapping_list_to_select" data-toggle="tab" aria-expanded="false"> 
							<spring:message	code="device.list.grid.heading.mappinglist.label" ></spring:message>
						</a>
		        	</li>
		        	<li id="device_selection_to_delete_tab" onclick="reloadPopUpDeviceData();">
			        	<a href="#device_selection_to_delete" id="device_list_to_select" data-toggle="tab" aria-expanded="true"> 
							<spring:message	code="device.list.grid.heading.label" ></spring:message>
						</a>
		        	</li>
	        	</ul> 
	        	<div id="tabbedPopUpDivToDelete" class="fullwidth no-padding tab-content">
					<div id="mapping_selection_to_delete" class="tab-pane active">
						<div class="box-body table-responsive no-padding box">
             				<table class="table table-hover" id="mappingList"></table>
               				<div id="mappingListPagingDiv"></div> 
	           				<div class="clearfix"></div> 
	           				<div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>  
            			</div>
            			<sec:authorize access="hasAuthority('DELETE_DEVICE')">
            			<div id="delete_mapping_bts_div" class="modal-footer padding10">
				         	<button id="delMappingBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="getSelectedMappingChkVal('<%=ControllerConstants.DELETE_MAPPINGS%>');"><spring:message code="btn.label.delete"></spring:message></button>
				         	<button type="button" class="btn btn-grey btn-xs " onclick="closeFancyBox();reloadGridData();"><spring:message code="btn.label.cancel"></spring:message></button>
				        </div>
				        <div class="modal-footer padding10" id="close-device-buttons-div" style="display:none;">
		            		<button type="button" class="btn btn-grey btn-xs " onclick="closeFancyBox();reloadGridData();"><spring:message code="btn.label.close"></spring:message></button>
		        		</div>
				        </sec:authorize>
				        <div id="delete_mapping_progress_bar_div" class="modal-footer padding10 text-left" style="display: none;">
							<jsp:include page="../common/processing-bar.jsp"></jsp:include>
						</div>
					</div>
					<div id="device_selection_to_delete" class="tab-pane">
						<div class="box-body table-responsive no-padding box">
							<table class="table table-hover" id="deviceList"></table>
               				<div id="deviceListPagingDiv"></div> 
	           				<div class="clearfix"></div> 
	           				<div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>
	           			</div>
	           			<sec:authorize access="hasAuthority('DELETE_DEVICE')">
            			<div class="fullwidth">
	               		<div id="delete_all_deviceMapping_div">
	               			<input type="checkbox" checked="checked" disabled='disabled' id="delete_deviceMapping_id" value="" > &nbsp;&nbsp;&nbsp;<spring:message code="device.delete.mappingdetails.label"></spring:message> 
	               		</div>	
	               		</div>
            			<div id="delete_device_bts_div" class="modal-footer padding10">
				         	<button id="delDeviceBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="getSelectedDeviceChkVal('<%=ControllerConstants.DELETE_DEVICES_MAPPINGS%>');"><spring:message code="btn.label.delete"></spring:message></button>
				         	<button type="button" class="btn btn-grey btn-xs " onclick="closeFancyBox();reloadGridData();"><spring:message code="btn.label.cancel"></spring:message></button>
				        </div>
				        <!-- div class="modal-footer padding10" id="close-device-buttons-div" style="display: none;">
		            		<button type="button" class="btn btn-grey btn-xs " onclick="closeFancyBox();reloadGridData();"><spring:message code="btn.label.close"></spring:message></button>
		        		</div -->
				        </sec:authorize>
				        <div id="delete_device_progress_bar_div" class="modal-footer padding10 text-left" style="display: none;">
							<jsp:include page="../common/processing-bar.jsp"></jsp:include>
						</div>
					</div>
				</div>	
	        	</div>
	        	</div>
	    </div>
		</div>
	</div>
	<a href="#delete_msg_div" class="fancybox" style="display: none;" id="delete_warn_msg_link">#</a>
   	<div id="delete_msg_div" style=" width:100%; display: none;" >
	    <div class="modal-content">
	        <div class="modal-header padding10">
	            <h4 class="modal-title"><spring:message code="device.delete.popup.warning.heading.label"></spring:message></h4>
	        </div>
	        <div class="modal-body padding10 inline-form">
		        <p id="moreWarn">
		        	<spring:message code="device.multiple.selection.warning.label"></spring:message>
		        </p>
		        <p id="lessWarn">
		        	<spring:message code="device.no.selection.warning.label"></spring:message>    
		        </p>
	        </div>
	        <div class="modal-footer padding10">
	            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
	        </div>
	    </div>
	    <!-- /.modal-content --> 
	</div>
	
	<a href="#divAttributeList" class="fancybox" style="display: none;" id="view_attribute_link">#</a>
	<div id="divAttributeList" style=" width:100%; display: none;" >
	   <div class="modal-content">
	        <div class="modal-header padding10">
	            <h4 class="modal-title"> <span id="attribute_name_label"></span> </h4>
	        </div>
	        <div class="modal-body padding10 inline-form">
					<div id="attribute_list_div" style="overflow: auto;height: 350px;"></div>	        	
	        </div>

	        <div id="start-buttons-div" class="modal-footer padding10">
	            <button type="button" class="btn btn-grey btn-xs " id="server-start-close-btn" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
	        </div>
	    </div>
	</div>
	
	
	<a href="#divMappingAssociationList" class="fancybox" style="display: none;" id="view_mapping_association_link">#</a>
	<div id="divMappingAssociationList" style=" width:100%; display: none;" >
	   <div class="modal-content">
	        <div class="modal-header padding10">
	            <h4 class="modal-title"><spring:message code="device.association.popup.header.label"></spring:message> </h4>
	        </div>
	        <div class="modal-body padding10 inline-form">
	        		<div>
	        			<p><spring:message code="device.association.popup.description.label"></spring:message></p>
	        			<p><b><spring:message code="device.association.association.label"></spring:message></b></p>
	        		</div>
					<div id="mapping_association_list_div"></div>	        	
	        </div>
	        <div id="start-buttons-div" class="modal-footer padding10">
	            <button type="button" class="btn btn-grey btn-xs " id="server-start-close-btn" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
	        </div>
	    </div>
	</div>
	<!-- Device Popup code end here -->
</div>
<!-- Main device list and search device list div end here -->
<script type="text/javascript">

$(document).ready(function() {
	getDeviceListBySearchParams('<%=ControllerConstants.GET_DEVICE_MAPPING_LIST%>','<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>');
	
	var dTypeId = $("#deviceType_id").val();
	if(dTypeId == 'undefined' || dTypeId == '' || dTypeId == null ){
		dTypeId = 0
	}
	
	getVendorByDeviceType('getVendorListByDeviceType',dTypeId,'vendorType_id',' ','form');
});

<%-- $(document).on("change","#search_vendor_type",function(event) {
	   var deviceTypeId = $('#search_device_type').val();
	   var vendorId = $('#search_vendor_type').val();
	getDeviceByVendorName('<%=ControllerConstants.GET_DEVICE_LIST_BY_VENDOR%>', deviceTypeId, vendorId);
});	 --%>
</script>
