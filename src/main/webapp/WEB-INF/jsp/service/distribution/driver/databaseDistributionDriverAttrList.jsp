<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page import="com.elitecore.sm.util.MapCache"%>

<style>
 .customLink:hover{
 	color:white;!important
 }
</style>
<div class="tab-pane<c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_DRIVER_ATTRLIST_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" id="attrListConfig">

	<div class="tab-content no-padding">
		<!-- Attribute div code start here -->
		<div class="fullwidth" id="add_delete_attribute_link_div">
			<div class="title2">
				<spring:message code="parserConfiguration.attribute.grid.heading.label" ></spring:message>
				<span class="title2rightfield"> 
				<sec:authorize access='hasAuthority(\'CREATE_PARSER\')'>
				<span class="title2rightfield-icon1-text">
				     	<a href="#" onclick="displayAddEditPopup('ADD','0');">
						<i class="fa fa-plus-circle"></i>
					</a> 
					<a href="#" onclick="displayAddEditPopup('ADD','0');"> 
						<spring:message code="parserConfiguration.attribute.grid.addattribute.label" ></spring:message>
					</a> 
					<a href="#divAddAttribute" class="fancybox" style="display: none;" id="add_edit_attribute">#</a>
				</span> 
				</sec:authorize>
				<sec:authorize access='hasAuthority(\'DELETE_PARSER\')'>
				<span class="title2rightfield-icon1-text"> 
					<a href="#" onclick="displayDeleteAttributePopup();">
						<i class="fa fa-trash"></i>
					</a> 
					<a href="#" onclick="displayDeleteAttributePopup();"> 
						<spring:message code="parserConfiguration.attribute.grid.deleteattribute.label" ></spring:message>
					</a> 
					<a href="#divDeleteAttribute" class="fancybox" style="display: none;" id="delete_attribute">#</a>
				</span>
				</sec:authorize>
				<span class="title2rightfield-icon1-text">
					<a onclick="addColNames('#attributeList');downloadCSVFile();">
						<i class="fa fa-download"></i>
					</a> 
					<a href="#" onclick="addColNames('#attributeList');downloadCSVFile();">
						<spring:message code="parserConfiguration.attribute.grid.exportattribute.label" ></spring:message>
					</a>
				</span> 
				<sec:authorize access='hasAuthority(\'CREATE_PARSER\')'>
				<span class="title2rightfield-icon1-text"> 
					<a href="#" onclick="openUploadAttrPopup('#attributeList');">
						<i class="fa fa-upload"></i>
					</a> 
				    <a href="#" onclick="openUploadAttrPopup('#attributeList');">
						<spring:message code="parserConfiguration.attribute.grid.uploadattribute.label" ></spring:message>
					</a> 
				</span>
				</sec:authorize> 
				</span>
			</div>
		</div>
		<div class="col-md-12 inline-form no-padding" id="attribute_grid_div">
			<div class="box-body table-responsive no-padding box"
				id="parser_attribute">
				<table class="table table-hover" id="attributeList"></table>
				<div id="attributeListDiv"></div>
				<div class="clearfix"></div>
				<div id="divLoading" align="center" style="display: none;">
					<img src="img/preloaders/Preloader_10.gif" />
				</div>
			</div>
		</div>
 		<!-- Attribute div code start here -->
	</div>
</div>
<%--Add composer plug-in pop-up code start here --%>
<a href="#divAddAttribute" class="fancybox" style="display: none;" id="add_driver_attr_link">#</a>

<form:form modelAttribute="driver_attribute_form_bean" method="POST" action="<%=ControllerConstants.ADD_EDIT_DATABASE_DRIVER_ATTRIBUTE%>" id="driver-attribute-form">
			<input type="hidden" value="0" id="id" name="id" />
	<div id="divAddAttribute" style="width:100%; display: none;"  >
					<div class="modal-content">
					<div class="modal-header padding10">
						<h4 class="modal-title">
							<span id="add_label" style="display: none;"><spring:message
									code="parser.attribute.create.heading.label" ></spring:message></span> 
							<span id="update_label" style="display: none;"><spring:message
									code="parser.attribute.update.heading.label" ></spring:message></span>
							<span id="view_label" style="display: none;"><spring:message
									code="parser.attribute.view.heading.label" ></spring:message></span>
						</h4>
					</div>
					<div class="modal-body padding10 inline-form">
					<div id="AddPopUpMsg">
						<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
					</div>
					
						<div class="col-md-6 no-padding">
						<div class="form-group">
							<spring:message code="database.driver.attrlist.database.field.name.label" var="label" ></spring:message>
							<spring:message code="database.driver.attrlist.database.field.name.label.tooltip" var="tooltip" ></spring:message>
							<div class="table-cell-label">${label}<span class="required">*</span></div>
							<div class="input-group">
								<form:input path="databaseFieldName"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="databaseFieldName" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }"></form:input>
								<spring:bind path="databaseFieldName">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="databaseFieldName_error"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
						</div>
						
						<div class="col-md-6 no-padding">
						<div class="form-group">
							<spring:message code="parsing.service.add.attr.uni.field" var="label" ></spring:message>
							<spring:message code="parsing.service.add.attr.uni.field.tooltip" var="tooltip" ></spring:message>
							<div class="table-cell-label">${label}</div>
							<div class="input-group">
								<jsp:include page="../../../common/formAutocomplete.jsp">
										<jsp:param name="unifiedField" value="unifiedFieldName" ></jsp:param>
										<jsp:param name="unifiedFieldPath" value="unifiedFieldName" ></jsp:param>
								</jsp:include> 
								<spring:bind path="unifiedFieldName">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="unifiedFieldName_error"></elitecoreError:showError>
								</spring:bind> 
							</div>
						</div>
						</div>
						
						<div class="col-md-6 no-padding">
						<div class="form-group">
							<spring:message code="composer.attr.grid.data.type" var="label" ></spring:message>
							<spring:message code="composer.attr.grid.data.type.tooltip" var="tooltip" ></spring:message>
							<div class="table-cell-label">${label}<span class="required">*</span></div>
							<div class="input-group">
									<form:select path="dataType"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="dataType" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip }">
									  <option value="" >Select Data Type</option>  
									<c:forEach items="${driverDataTypeEnum}" var="driverDataTypeEnum">
										<form:option value="${driverDataTypeEnum}">${driverDataTypeEnum}</form:option>
									</c:forEach>
								</form:select>
								<spring:bind path="dataType">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="dataType_error"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
						</div>
						
						<div class="col-md-6 no-padding">
						<div class="form-group">
							<spring:message code="parsing.service.add.attr.default.val" var="label" ></spring:message>
							<spring:message code="parsing.service.add.attr.default.val.tooltip" var="tooltip" ></spring:message>
							<div class="table-cell-label">${label}</div>
							<div class="input-group">
								<form:input path="defualtValue"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="defualtValue" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }"></form:input>
								<spring:bind path="defualtValue">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="defualtValue_error"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
						</div>
						
						<div class="title2">
								<spring:message code="composer.attribute.enable.padding"></spring:message>
						</div>
							
						<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="composer.attribute.enable.padding" var="label" ></spring:message>
								<spring:message code="composer.attribute.enable.padding.tooltip" var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}</div>
								<div class="input-group">
									<form:select path="paddingEnable"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="paddingEnable" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip }" onChange="changePaddingParamter()">
										
										<c:forEach items="${trueFalseEnum}" var="trueFalseEnum">
												<option value="${trueFalseEnum.name}" >${trueFalseEnum}</option>
										</c:forEach>
													
									</form:select>
									<spring:bind path="paddingEnable">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="composer.attribute.padding.length" var="label" ></spring:message>
								<spring:message code="composer.attribute.padding.length.tooltip" var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}</div>
								<div class="input-group">
									<form:input path="length"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="length" data-toggle="tooltip" data-placement="bottom"
										title="${tooltip }" onkeydown="isNumericOnKeyDown(event)"></form:input>
									<spring:bind path="length">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
							</div>
						<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="composer.attribute.padding.type" var="label" ></spring:message>
								<spring:message code="composer.attribute.padding.type.tooltip" var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}</div>
								<div class="input-group">
									<form:select path="paddingType"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="paddingType" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip }">
										<c:forEach items="${paddingTypeEnum}" var="paddingTypeEnum">
											<form:option value="${paddingTypeEnum}">${paddingTypeEnum}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="paddingType">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
									
								</div>
							</div>
							</div>
						<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="composer.attribute.padding.character" var="label" ></spring:message>
								<spring:message code="composer.attribute.padding.character.tooltip" var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}</div>
								<div class="input-group">
									<form:input path="paddingChar"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="paddingChar" data-toggle="tooltip" data-placement="bottom"
										title="${tooltip }"></form:input>
									<spring:bind path="paddingChar">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
							</div>
						<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="composer.attribute.padding.prefix" var="label" ></spring:message>
								<spring:message code="composer.attribute.padding.prefix.tooltip" var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}</div>
								<div class="input-group">
									<form:input path="prefix"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="prefix" data-toggle="tooltip" data-placement="top"
										title="${tooltip }"></form:input>
									<spring:bind path="prefix">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
							</div>
							<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="composer.attribute.padding.suffix" var="label" ></spring:message>
								<spring:message code="composer.attribute.padding.suffix.tooltip" var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}</div>
								<div class="input-group">
									<form:input path="suffix"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="suffix" data-toggle="tooltip" data-placement="top"
										title="${tooltip }"></form:input>
									<spring:bind path="suffix">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
							</div>
							
					</div>
					<div class="modal-footer padding10">
						<button type="button" id="addNewAttribute"
							class="btn btn-grey btn-xs " style="display: none"
							onclick="createUpdateDriverAttribute('<%=ControllerConstants.ADD_EDIT_DATABASE_DRIVER_ATTRIBUTE%>',0,'ADD');">
							<spring:message code="btn.label.add" ></spring:message>
						</button>
						<button type="button" id="editAttribute"
							class="btn btn-grey btn-xs " style="display: none"
							onclick="createUpdateDriverAttribute('<%=ControllerConstants.ADD_EDIT_DATABASE_DRIVER_ATTRIBUTE%>',0,'EDIT');">
							<spring:message code="btn.label.edit" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs "
							data-dismiss="modal" id="closebtn"
							onclick="closeFancyBox();reloadAttributeGridData();">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs "
							data-dismiss="modal" id="viewClose"
							onclick="closeFancyBox();">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
					</div>
				</div>
	</div>
</form:form> 

		<!-- Delete warning message popup div start here -->
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
		<div id="divDeleteAttribute" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<spring:message code="parser.attribute.delete.heading.label" ></spring:message>
					</h4>
				</div>
				<div class="modal-body padding10 inline-form">
					<div id="deletePopUpMsg" class=fullwidth>
						<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
					</div>
					<div class="fullwidth">
						<input type="hidden" value="" id="deleteAttributeId"
							name="deleteAttributeId" />
						<div id="delete_selected_attribute_details"></div>
						<div>
							<spring:message code="attribute.delete.warning.message" ></spring:message>
						</div>
					</div>
				</div>
					<div id="delete_attribute_bts_div" class="modal-footer padding10">
						<button type="button" class="btn btn-grey btn-xs"
							onclick="deleteAttribute();">
							<spring:message code="btn.label.delete" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs"
							onclick="closeFancyBox();">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</div>
					<div class="modal-footer padding10"
						id="delete_close_attribute_buttons_div" style="display: none;">
						<button type="button" class="btn btn-grey btn-xs "
							onclick="closeFancyBox();reloadAttributeGridData();">
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
<!-- Attribute upload screen -->
<a href="#divuploadAttrData" class="fancybox" style="display: none;" id="uploadAttrData">#</a>
 		<div id="divuploadAttrData" style=" width:100%; display: none;" >
 		  <jsp:include page="uploadDbDistributionDriverAttrPopup.jsp"></jsp:include>
       </div>	
<script src="${pageContext.request.contextPath}/customJS/distributionDriverManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function() {
	
	selDriverId = $("#driverId").val();
	urlAction = '<%=ControllerConstants.GET_DRIVER_ATTRIBUTE_GRID_LIST%>';
	
    createDatabaseAttributeGrid('<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>')
    getDriverAttributeListByDriverId(urlAction, selDriverId);

    changePaddingParamter();
});

/*Function will create JQGRID for database attribute list */
function createDatabaseAttributeGrid(defaultRowNum){				
	 
	$("#attributeList").jqGrid({	    	
		datatype: "local",
        colNames:[
				  	"#",
					"<spring:message code='database.driver.attrlist.database.field.name.label' ></spring:message>",
					"<spring:message code='parsing.service.add.attr.uni.field' ></spring:message>",
					"<spring:message code='composer.attr.grid.data.type'></spring:message>", 
					"<spring:message code='parsing.service.add.attr.default.val' ></spring:message>",
					"<spring:message code='composer.attribute.enable.padding' ></spring:message>",	
					"<spring:message code='composer.attribute.padding.length' ></spring:message>",	
					"<spring:message code='composer.attribute.padding.type' ></spring:message>",	
					"<spring:message code='composer.attribute.padding.character' ></spring:message>",	
					"<spring:message code='composer.attribute.padding.prefix' ></spring:message>",	
					"<spring:message code='composer.attribute.padding.suffix' ></spring:message>",
					"<spring:message code='regex.parser.attr.grid.edit' ></spring:message>",
                 ],
		colModel:[
					{name:'id',index:'id',sortable:true,hidden: true,align:'center',search:false},
	            	{name:'databaseFieldName',index:'databaseFieldName',sortable:true,align:'center'},
	            	{name:'unifiedFieldName',index:'unifiedFieldName',sortable:true,align:'center'},
					{name:'dataType',index:'dataType',hidden: false,align:'center',sortable:true,search:false}, 
					{name:'defualtValue',index:'defaultValue',sortable:false,hidden: false,align:'center',search:false}, 
					{name:'paddingEnable',index:'paddingEnable',sortable:false,hidden: true,align:'center',search:false},	
					{name:'length',index:'length',sortable:false,hidden: true,align:'center',search:false},	
					{name:'paddingType',index:'paddingType',sortable:false,hidden: true,align:'center',search:false},	
					{name:'paddingChar',index:'paddingChar',sortable:false,hidden: true,align:'center',search:false},	
					{name:'prefix',index:'prefix',sortable:false,hidden: true,align:'center',search:false},	
					{name:'suffix',index:'suffix',sortable:false,hidden: true,align:'center',search:false},
					{name:'update',index: 'update', align:'center',sortable:false,formatter: updateFormatter,search:false},
					
        ],
        rowNum:defaultRowNum,
        rowList:[10,20,60,100],
        height: 'auto',
        pager: '#attributeListDiv',
        viewrecords: true,
        sortname: 'id',
 		sortorder: "asc",
        search:true,
        ignoreCase: true,
        multiselect: true,
        timeout : 120000,
        loadtext: 'Loading...',
        /* caption: jsSpringMsg.caption, */
        beforeRequest:function(){
            $(".ui-dialog-titlebar").hide();
        }, 
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        loadComplete: function(data) { 
 			/* $(".ui-dialog-titlebar").show();
 			if ($('#attributeList').getGridParam('records') === 0) {
                $('#attributeList tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'>"+jsSpringMsg.emptyRecordText+"</div>");
                $("#attributeListDiv").hide();
            }else{
            	$("#attributeListDiv").show();
            	ckIntanceSelected = new Array();
            } */ 
        	ckIntanceSelected = new Array();
		},
		onPaging: function (pgButton) {
			clearResponseMsgDiv();
		},
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		beforeSelectRow: function (rowid, e){
			// this blank function will not select the entire row. Only checkbox can be selectable.
			
			var $grid = $("#attributeList");
			if($("#jqg_attributeList_" + rowid).is(':checked')){
				if(ckIntanceSelected.indexOf(rowid) == -1){
					ckIntanceSelected.push(rowid);
				}
			}else{
				if(ckIntanceSelected.indexOf(rowid) != -1){
					ckIntanceSelected.splice(ckIntanceSelected.indexOf(rowid), 1);
				}
			}
		    return false;
		},
		onSelectAll:function(id,status){
			if(status==true){
				ckIntanceSelected = new Array();
				for(i=0;i<id.length;i++){
					ckIntanceSelected.push(id[i]);
	         	}
			} else {
				ckIntanceSelected = new Array();
			} 

		},			
		recordtext: jsSpringMsg.recordtext,
        emptyrecords: jsSpringMsg.emptyrecords,
		loadtext: jsSpringMsg.loadingText,
		pgtext : jsSpringMsg.pgtext,
 	}).navGrid("#attributeListDiv",{edit:false,add:false,del:false,search:false});
	$(".ui-jqgrid-titlebar").hide();
	$("#attributeList").sortableRows();   
       $("#attributeList").sortable({
            items: 'tr:not(:first)'
       });
       $("#attributeList").bind('sortstop', function(event, ui) { 
           });
       $("#attributeList").jqGrid('gridDnD');
	$('#attributeList').jqGrid('filterToolbar',{
		stringResult: true,
		searchOperators: false,
		searchOnEnter: false, 
		defaultSearch: "cn"
	});
}

/*Function will load driver attribute to JQGRID */
function getDriverAttributeListByDriverId(urlAction, selDriverId){
	
	$.ajax({
		url: urlAction,
		cache: false,
		async: false,
		data:
		{
		   'driverId'   : selDriverId,
		},
        datatype: "json",
        type: "POST",
		success: function(data){					
			var response = JSON.parse(data);			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;			
			if(responseCode == "200"){				
				$("#attributeListDiv").show();				
				var attributes=eval(responseObject);				
				var attributeList=attributes['attributeList'];
				
				if(attributeList!=null && attributeList != 'undefined' && attributeList != undefined){					
					var gridArray = [];
					$.each(attributeList,function(index,attribute){		 				
		 				var rowData = {};
		 				rowData.id					= parseInt(attribute.id);
		 				rowData.databaseFieldName   = attribute.databaseFieldName;
		 				rowData.unifiedFieldName	= attribute.unifiedFieldName;
		 				rowData.defualtValue		= attribute.defualtValue;
		 				rowData.dataType			= attribute.dataType;
		 				rowData.paddingEnable		= attribute.paddingEnable;	
		 				rowData.length				= parseInt(attribute.length);	
		 				rowData.paddingType			= attribute.paddingType;	
		 				rowData.paddingChar			= attribute.paddingChar;	
		 				rowData.prefix				= attribute.prefix;	
		 				rowData.suffix				= attribute.suffix;
		 				gridArray.push(rowData);
		 			});
					jQuery("#attributeList").jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
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
function downloadCSVFile() {
	$("#sampleRequired").val('NO');
	$("#sample_lookup_table_form").submit();
}

function changePaddingParamter(){
	var paddingEnable=$("#paddingEnable").find(":selected").val();
		if(paddingEnable == 'true'){
			
			$("#length").attr("readOnly",false)
			$("#paddingType").prop("disabled",false)
			$("#paddingChar").attr("readOnly",false)
			$("#prefix").attr("readOnly",false)
			$("#suffix").attr("readOnly",false)
			
		}else{
			$("#length").attr("readOnly",true)
			$("#paddingType").prop("disabled",true)
			$("#paddingChar").attr("readOnly",true)
			$("#prefix").attr("readOnly",true)
			$("#suffix").attr("readOnly",true)
		}
}
</script>
