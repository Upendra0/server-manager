<%@page import="com.elitecore.sm.common.model.StateEnum"%>
<%@page import="com.elitecore.sm.services.model.PartitionFieldEnum"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@page import="com.elitecore.sm.services.model.HashDataTypeEnum"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<script src="${pageContext.request.contextPath}/customJS/autoSuggestionForUnifiedField.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<script>
var partitionParamArr = [];
</script>

<div class="fullwidth mtop10">
	<form:form
		modelAttribute="<%= FormBeanConstants.IPLOG_PARSING_SERVICE_CONFIGURATION_FORM_BEAN %>"
		method="POST"
		action="<%=ControllerConstants.UPDATE_PARSING_HASH_CONFIGURATION %>"
		id="iplog-parsing-service-hash-configuration-form">
		<form:hidden path="id" id="serviceId" value="${serviceId}" ></form:hidden>
		<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
		<form:hidden path="svctype.id" id="svctypeId" value="${svctype.id}" ></form:hidden>
		<form:hidden path="svctype.type" id="svctypetype"
			value="${svctype.type}" ></form:hidden>
		<form:hidden path="svctype.serviceCategory" id="svctypecategory"
			value="${svctype.serviceCategory}" ></form:hidden>
		<form:hidden path="svctype.typeOfService" id="svctypeOfService"
			value="${svctype.typeOfService}" ></form:hidden>
		<form:hidden path="svctype.description" id="svctypedescription"
			value="${svctype.description}" ></form:hidden>
		<form:hidden path="svctype.alias" id="svctypealias"
			value="${svctype.alias}" ></form:hidden>
		<form:hidden path="svctype.serviceFullClassName"
			id="svctypeserviceFullClassName"
			value="${svctype.serviceFullClassName}" ></form:hidden>
		<form:hidden path="fileGroupingParameter.id" id="id" ></form:hidden>
		<form:hidden path="serverInstance.id" id="serverInstanceId"
			value="${instanceId}" ></form:hidden>
		<form:hidden path="servInstanceId" id="servInstanceId"
			value="${servInstanceId}" ></form:hidden>
		<form:hidden path="name" id="name" ></form:hidden>
		<form:hidden path="description" id="description" ></form:hidden>
		<form:hidden path="svcExecParams.startupMode"
			id="svcExecParams.startupMode" ></form:hidden>
		<form:hidden path="svcExecParams.executionInterval"
			id="svcExecParams.executionInterval" ></form:hidden>
		<form:hidden path="svcExecParams.executeOnStartup"
			id="svcExecParams.executeOnStartup" ></form:hidden>
		<form:hidden path="svcExecParams.sortingType"
			id="svcExecParams.sortingType" ></form:hidden>
		<form:hidden path="svcExecParams.sortingCriteria"
			id="svcExecParams.sortingCriteria" ></form:hidden>
		<form:hidden path="svcExecParams.minThread"
			id="svcExecParams.minThread" ></form:hidden>
		<form:hidden path="svcExecParams.maxThread"
			id="svcExecParams.maxThread" ></form:hidden>
		<form:hidden path="svcExecParams.fileBatchSize"
			id="svcExecParams.fileBatchSize" ></form:hidden>
		<form:hidden path="svcExecParams.queueSize"
			id="svcExecParams.queueSize" ></form:hidden>
		<form:hidden path="recordBatchSize" id="recordBatchSize" ></form:hidden>
		<form:hidden path="outputFileHeader" id="outputFileHeader" ></form:hidden>
		<form:hidden path="equalCheckField" id="equalCheckField" ></form:hidden>
		<form:hidden path="equalCheckFunction" id="equalCheckFunction" ></form:hidden>
		<form:hidden path="equalCheckValue" id="equalCheckValue" ></form:hidden>
		<form:hidden path="fileGroupingParameter.fileGroupEnable"
			id="fileGroupParam.fileGroupEnable" ></form:hidden>
		<form:hidden path="fileGroupingParameter.groupingType"
			id="fileGroupParam.groupingType" ></form:hidden>
		<form:hidden path="fileGroupingParameter.enableForArchive"
			id="fileGroupParam.enableForArchive" ></form:hidden>
		<form:hidden path="fileGroupingParameter.archivePath"
			id="fileGroupParam.archivePath" ></form:hidden>

		<form:hidden path="fileStatsEnabled" id="fileStatsEnabled" ></form:hidden>
		<form:hidden path="fileStatsLoc" id="fileStatsLoc" ></form:hidden>
		<form:hidden path="purgeInterval" id="purgeInterval" ></form:hidden>
		<form:hidden path="purgeDelayInterval" id="purgeDelayInterval" ></form:hidden>
		<form:hidden path="correlEnabled" id="correlEnabled" ></form:hidden>
		<form:hidden path="mappedSourceField" id="mappedSourceField" ></form:hidden>
		<form:hidden path="destPortField" id="destPortField" ></form:hidden>
		<form:hidden path="destPortFilter" id="destPortFilter" ></form:hidden>
		<form:hidden path="createRecDestPath" id="createRecDestPath" ></form:hidden>
		<form:hidden path="deleteRecDestPath" id="deleteRecDestPath" ></form:hidden>

		<input type="hidden" id="partitionParamList" name="partitionParamList" />

		<!-- section-1 start here -->
		<div class="box box-warning">
			<div class="box-header with-border">
				<h3 class="box-title">
					<spring:message
						code="iplog.parsing.service.config.index.config.popup.header" ></spring:message>
				</h3>
				<div class="box-tools pull-right" style="top: -3px;">
					<button class="btn btn-box-tool" data-widget="collapse">
						<i class="fa fa-minus"></i>
					</button>
				</div>
				<!-- /.box-tools -->
			</div>
			<!-- /.box-header end here -->
			<div class="box-body">
				<div class="fullwidth inline-form">
					<div class="col-md-6 no-padding">
						<spring:message code="iplog.parsing.service.config.index.base"
							var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group ">
								<form:select path="indexType"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="service-startupMode" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip }" readonly="true"
									disabled="true">
									<c:forEach items="${indexBaseEnum}" var="indexBaseEnum">
										<form:option value="${indexBaseEnum.value}">${indexBaseEnum}</form:option>
									</c:forEach>
								</form:select>
								<spring:bind path="indexType">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
					</div>

					<div class="col-md-6 no-padding">
						<spring:message code="iplog.parsing.service.config.hash.separator"
							var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group ">
								<form:select path="hashSeparator"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="service-startupMode" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip }">
									<c:forEach items="${hashSeparatorEnum}" var="hashSeparatorEnum">
										<form:option value="${hashSeparatorEnum}">${hashSeparatorEnum}</form:option>
									</c:forEach>
								</form:select>
								<spring:bind path="hashSeparator">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
					</div>
					<div class="col-md-6 no-padding">
						<spring:message code="iplog.parsing.service.config.data.type"
							var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip}<span
									class="required">*</span><i class='fa fa-square'></i>
							</div>
							<div class="input-group ">
								<form:select path="dataType"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="service-dataType" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip }"
									onchange="setDefaultDataTypeValue();">
									<c:forEach items="${dataTypeEnum}" var="dataTypeEnum">
										<form:option value="${dataTypeEnum}">${dataTypeEnum}</form:option>
									</c:forEach>
								</form:select>
								<spring:bind path="dataType">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
					</div>
					<div class="clearfix"></div>
					<div class="box-body table-responsive no-padding box">
						<table class="table table-hover" id="paramList"></table>
						<div id="paramListPagingDiv"></div>
						<div class="clearfix"></div>
						<div id="divLoading" align="center" style="display: none;">
							<img src="img/preloaders/Preloader_10.gif" />
						</div>
					</div>
				</div>
			</div>
			<!-- Form content end here  -->
		</div>
		<!-- section-1 end here -->
	</form:form>
</div>

<!-- Client state popup code start here -->
<div id="divEditPartitionParam" style="width: 100%; display: none;">

	<input type="hidden" id="paramId" />
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title" id="edit-title"></h4>
		</div>

		<div class="modal-body padding10 inline-form">
			<jsp:include page="../../../common/responseMsgPopUp.jsp"></jsp:include>
			<div class="col-md-12  no-padding">
				<div class="form-group">
					<div class="table-cell-label">
						<spring:message
							code="iplog.parsing.service.config.hash.param.grid.column.idx.lvl" ></spring:message>
					</div>
					<div class="input-group ">
						<input class="form-control table-cell input-sm" tabindex="4"
							id="param-level" data-toggle="tooltip" data-placement="bottom"
							title="${tooltip }" disabled="disabled" /> <span
							class="input-group-addon add-on last"> <i title=""
							data-placement="left" data-toggle="tooltip"
							class="glyphicon glyphicon-alert" data-original-title=""></i>
						</span>
					</div>
				</div>
			</div>
			<div class="col-md-12  no-padding">
				<div class="form-group">
					<div class="table-cell-label">
						<spring:message
							code="iplog.parsing.service.config.hash.param.grid.column.index.field" ></spring:message>
					</div>
					<div class="input-group ">
						<input class="form-control table-cell input-sm" tabindex="4"
							id="param-partitionField" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip }" disabled="disabled" />
						<span class="input-group-addon add-on last"> <i title=""
							data-placement="left" data-toggle="tooltip"
							class="glyphicon glyphicon-alert" data-original-title=""></i>
						</span>
					</div>
				</div>
			</div>
			<div class="col-md-12  no-padding">
				<spring:message
					code="iplog.parsing.service.config.hash.param.grid.column.unified.field.tooltip"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">
						<spring:message
							code="iplog.parsing.service.config.hash.param.grid.column.unified.field" ></spring:message>
						<span class="required">*</span>
					</div>
					<jsp:include page="../../../common/autocomplete.jsp">
						<jsp:param name="unifiedField" value="unifiedField" ></jsp:param>
					</jsp:include>
					<%-- <div class="input-group ">
						<select class="form-control table-cell input-sm" tabindex="4"
							id="param-unifiedField" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip}">
							<c:forEach items="${unifiedFieldEnum}" var="unifiedFieldEnum">
								<option value="${unifiedFieldEnum}">${unifiedFieldEnum}</option>
							</c:forEach>
						</select> <span class="input-group-addon add-on last"> <i title=""
							data-placement="left" data-toggle="tooltip"
							class="glyphicon glyphicon-alert" data-original-title=""></i>
						</span>
					</div> --%>
				</div>
			</div>
			<div class="col-md-12  no-padding" id="divRangeInput">
			<spring:message
					code="iplog.parsing.service.config.hash.param.grid.column.partion.range.tooltip"
					var="tooltip" ></spring:message>
			
				<div class="form-group">
					<div class="table-cell-label">
						<spring:message
							code="iplog.parsing.service.config.hash.param.grid.column.partion.range" ></spring:message>
						<span class="required">*</span><i class='fa fa-square'></i>
					</div>
					<div class="input-group">
						<input class="form-control table-cell input-sm" tabindex="4"
							id="partitionRange" data-toggle="tooltip"
							data-placement="top" title="${tooltip }" /> <span
							class="input-group-addon add-on last"> <i title=""
							data-placement="left" data-toggle="tooltip"
							class="glyphicon glyphicon-alert" data-original-title=""></i>
						</span>
					</div>
				</div>
			</div>
			
			<div class="col-md-12  no-padding" id="divRangeSelect">
				<div class="form-group">
					<div class="table-cell-label">
						<spring:message
							code="iplog.parsing.service.config.hash.param.grid.column.partion.range" ></spring:message>
						<span class="required">*</span><i class='fa fa-square'></i>
					</div>
					<div class="input-group">
						<select class="form-control table-cell input-sm" tabindex="4"
							id="param-partitionRangeDropDown" data-toggle="tooltip"
							data-placement="top" title="${tooltip }"
							style="display: none;">
							<c:forEach items="${dateRangeEnum}" var="dateRangeEnum">
								<option value="${dateRangeEnum}">${dateRangeEnum}</option>
							</c:forEach>
						</select> <span class="input-group-addon add-on last"> <i title=""
							data-placement="left" data-toggle="tooltip"
							class="glyphicon glyphicon-alert" data-original-title=""></i>
						</span>
					</div>
				</div>
			</div>
			
			<div class="col-md-12  no-padding">
				<spring:message code="iplog.parsing.service.base.unified.field.label"	var="label" ></spring:message>
				<spring:message code="iplog.parsing.service.base.unified.field.label"	var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">
						${label}
					</div>
					<jsp:include page="../../../common/autocomplete.jsp">
						<jsp:param name="unifiedField" value="baseUnifiedField" ></jsp:param>
					</jsp:include>
					<%-- <div class="input-group ">
					<!-- MED-8505 -->
						<select class="form-control table-cell input-sm" tabindex="5" 	id="param-base-unifiedField" data-toggle="tooltip"	data-placement="bottom" title="${tooltip}">
							<option  value="NA" selected="selected"><spring:message code="iplog.parsing.service.base.unified.option.label" ></spring:message></option>
							<c:forEach items="${unifiedFieldEnum}" var="unifiedFieldEnum">
								<option value="${unifiedFieldEnum}">${unifiedFieldEnum}</option>
							</c:forEach>
						</select> 
						<span class="input-group-addon add-on last"> <i title="" 	data-placement="left" data-toggle="tooltip"			class="glyphicon glyphicon-alert" data-original-title=""></i>
						</span>
					</div> --%>
				</div>
			</div>
			
			<div class="col-md-12  no-padding">
				<spring:message code="iplog.parsing.service.net.mask.field.label"	var="label" ></spring:message>
				<spring:message code="iplog.parsing.service.net.mask.field.label"	var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">
						${label}
					</div>
					<div class="input-group ">
						<input class="form-control table-cell input-sm" tabindex="6" 	id="netMask" data-toggle="tooltip" data-placement="top" title="${tooltip }"  /> 
						<span class="input-group-addon add-on last"> 
							<i title="" data-placement="left" data-toggle="tooltip"	class="glyphicon glyphicon-alert" data-original-title=""></i>
						</span>	
					</div>
				</div>
			</div>
			
		</div>
		<div class="modal-footer padding10">
			<button type="button" class="btn btn-grey btn-xs "
				data-dismiss="modal" id="updtBtn"
				onclick="validatePartitionParam();">
				<spring:message code="btn.label.update" ></spring:message>
			</button>
			<button type="button" class="btn btn-grey btn-xs "
				data-dismiss="modal" id="noBtn" onclick="closeFancyBox();">
				<spring:message code="btn.label.cancel" ></spring:message>
			</button>
		</div>
	</div>
	<!-- /.modal-content -->
</div>
<a href="#divEditPartitionParam" class="fancybox" style="display: none;"
	id="editPartitionParam">#</a>
<!-- Client state popup code end here -->

<script type="text/javascript">

	$(document).ready(function() {

		jQuery("#paramList").showCol("edit");
		
		$('#baseUnifiedField').val('');
		
 		var partitionParamList = eval('${iplog_parsing_service_configuration_form_bean.partionParamList}');
		var indexLevel=0;
 		
 		$.each(partitionParamList, function (index, paramObj) {
	  	    var partitionObj = {};
	  	  	indexLevel++;
	  	  	partitionObj.id=paramObj.id;
	  	  	partitionObj.level=indexLevel;
 			partitionObj.partitionField=paramObj.partitionField;
 			partitionObj.unifiedField=paramObj.unifiedField;
 			partitionObj.partitionRange=paramObj.partitionRange;
 			
 			var baseUnified =  paramObj.baseUnifiedField;
 			/* if(baseUnified == '' || baseUnified == null ){
 				baseUnified = 'NA';
 			} */
 			partitionObj.baseUnifiedField= baseUnified;
 			partitionObj.netMask = paramObj.netMask;
 			
 			partitionObj.edit='<div><img src="img/edit-24.png" id="img_'+paramObj.partitionField+'" width="10px" data-toggle="tooltip" data-placement="bottom" height="10px" class="chkStatus" style="z-index:100px;cursor:pointer;" onclick="editParam(\''+paramObj.id+'\');"></div>';
 			partitionObj.status=paramObj.status;
 			partitionParamArr.push(partitionObj);
 		});
		
		$("#paramList").jqGrid({
        	url: "",
        	data: partitionParamArr,
            datatype: "local",
            colNames:[
					  "<spring:message code='iplog.parsing.service.config.hash.param.grid.column.id' ></spring:message>",
                      "<spring:message code='iplog.parsing.service.config.hash.param.grid.column.idx.lvl' ></spring:message>",
                      "<spring:message code='iplog.parsing.service.config.hash.param.grid.column.index.field' ></spring:message>",
                      "<spring:message code='iplog.parsing.service.config.hash.param.grid.column.unified.field' ></spring:message>",
                      "<spring:message code='iplog.parsing.service.config.hash.param.grid.column.partion.range' ></spring:message>",
                      "<spring:message code='iplog.parsing.service.config.hash.param.grid.column.partion.baseunified' ></spring:message>",
                      "<spring:message code='iplog.parsing.service.config.hash.param.grid.column.partion.net.mask' ></spring:message>",
                      "<spring:message code='iplog.parsing.service.config.hash.param.grid.column.edit' ></spring:message>"
                     ],
			colModel:[
				{name:'id',index:'id',sortable:false,hidden: true},
            	{name:'level',index:'level',sortable:false},
            	{name:'partitionField',index:'partitionField',sortable:false},
                {name:'unifiedField',index:'unifiedField',sortable:false},
            	{name:'partitionRange',index:'partitionRange',sortable:false},
            	{name:'baseUnifiedField',index:'baseUnifiedField',sortable:false},
            	{name:'netMask',index:'netMask',sortable:false,formatter:netMaskFormatter},
            	{name:'edit',index:'',sortable:false,align:'center',hidden:false}
            ],
            rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
            rowList:[10,20,60,100],
            height: 'auto',
			sortname: 'id',
     		sortorder: "asc",
            pager: "#paramListPagingDiv",
            viewrecords: true,
            multiselect: false,
            timeout : 120000,
            loadtext: "Loading...",
            beforeRequest:function(){
                $(".ui-dialog-titlebar").hide();
            },
     		loadComplete: function(data) {
     			gridDataArr = data.rows;
			},
			onPaging: function (pgButton) {
				clearResponseMsgDiv();
				clearInstanceGrid();
			},
			loadError : function(xhr,st,err) {
				handleGenericError(xhr,st,err);
			},
			beforeSelectRow: function (rowid, e){
				
			},
			onSelectAll:function(id,status){
				
			},
			recordtext: "<spring:message code='iplog.parsing.service.config.hash.param.grid.pager.total.records.text'></spring:message>",
	        emptyrecords: "<spring:message code='iplog.parsing.service.config.hash.param.grid.empty.records'></spring:message>",
			loadtext: "<spring:message code='iplog.parsing.service.config.hash.param.grid.loading.text'></spring:message>",
			pgtext : "<spring:message code='iplog.parsing.service.config.hash.param.grid.pager.text'></spring:message>"
		}).navGrid("#paramListPagingDiv",{edit:false,add:false,del:false,search:false});
	});

	function reloadGridData(){
		var $grid = $("#paramList");
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
	}
	
	
	 function baseUnifiedFieldFormatter(cellvalue, options, rowObject){
		 if(cellvalue != null && cellvalue != '' && cellvalue != 'null' ){
			return cellvalue;	
		}else{
			return '';
		} 
	}
	
	function netMaskFormatter(cellvalue, options, rowObject){
		 if(cellvalue != '' && cellvalue != '-1'){
			return cellvalue ;	
		}else{
			return '';
		}
	} 
	
	function editFormatter(cellvalue, options, rowObject){
		return '<div><img src="img/edit-24.png" id="img_'+rowObject['id']+'" width="10px" data-toggle="tooltip" data-placement="bottom" height="10px" class="chkStatus" style="z-index:100px;cursor:pointer;"></div>';
	}
	
	function editParam(id){
		var grid = $("#paramList");
        var rowData = jQuery('#paramList').jqGrid('getRowData',id);
        var partitionFieldEleId = "";
        
        if(rowData['partitionField'] == '<%= PartitionFieldEnum.Date %>'){
        	partitionRangeEleId = "param-partitionRangeDropDown";
        	$('#param-partitionRangeDropDown').show();
        	$('#param-partitionRange').hide();
        	$('#divRangeSelect').show();
        	$('#divRangeInput').hide();
        } else {
        	partitionRangeEleId = "partitionRange";
        	$('#param-partitionRangeDropDown').hide();
        	$('#param-partitionRange').show();
        	$('#divRangeSelect').hide();
        	$('#divRangeInput').show();
        }
        // set edit popup default values
        $('#edit-title').html($('#service-dataType').val());
        $('#param-level').val(rowData['level']);
        $('#param-partitionField').val(rowData['partitionField']);
        $("#unifiedField").val(rowData['unifiedField']);
        $('#'+partitionRangeEleId).val(rowData['partitionRange']);

        if(rowData['partitionField'] == '<%=PartitionFieldEnum.Date%>'){
        	$('#baseUnifiedField').attr('readonly',true);
			$('#baseUnifiedField').attr('disabled','disabled');
			
			$('#netMask').attr('readonly',true);
			$('#netMask').attr('disabled',true);
        	
        }else{
        	$('#baseUnifiedField').attr('readonly',false);
			$('#baseUnifiedField').removeAttr('disabled');
			
			$('#netMask').attr('readonly',false);
			$('#netMask').attr('disabled',false);
			/* MED-8505 */
			//if(rowData['baseUnifiedField'] == 'NA')
			//	$("#netMask").attr('disabled',true);
        }
        
        var selectedBaseVal = rowData['baseUnifiedField'];
        
        if(selectedBaseVal == '' || selectedBaseVal == null || selectedBaseVal == 'undefined'){
        	 $('#baseUnifiedField').val(''); 	
        }else{
        	$('#baseUnifiedField').val(rowData['baseUnifiedField']);
        }
        
       
        $("#netMask").val(rowData['netMask']);
      	$('#paramId').val(rowData['id']);
        
      	// open edit partition param popup
      	resetWarningDisplayPopUp();
        $('#editPartitionParam').click();
		
        
	}
	
	var systemDataType = '<%= HashDataTypeEnum.NAT_DATA_RECORD %>';
    var publicIp = '<%=PartitionFieldEnum.PUBLIC_IP%>';
	
	// if data type is usege data than disable private ip param for service
	function setDefaultDataTypeValue(){
		
		var dataType = $('#service-dataType').val();
		var ids = $("#paramList").jqGrid('getDataIDs');
    	var rowData = jQuery("#paramList").getRowData(ids[1]);
        
        $.each(partitionParamArr,function(index,param){
        		
        		if( publicIp == param.partitionField){
        			
        			if(param.partitionRange == null || param.partitionRange==''){
        				if(dataType ==  systemDataType ){ // its net data
            				param.partitionRange = '100';
        		        }else { // its usage data
        		        	param.partitionRange = '1';
        		        }
        			}
        			partitionParamArr[index] = param;
        			jQuery("#paramList").jqGrid('setCell', param.id, 'partitionRange', param.partitionRange);
        		}
		});
        
	}
	
	// on click of update button in edit partition param popup
	function changeParam(){
		var id = $("#paramId").val();
		
		if(id == ''){
			var ids = $("#paramList").jqGrid('getDataIDs');
	    	var rowData = jQuery("#paramList").getRowData(ids[2]);
	    	id = rowData['id'];
		}	
		
		$.each(partitionParamArr,function(index,param){
			if(param.id==id){

				param.level = $('#param-level').val();
				param.partitionField = $('#param-partitionField').val();
				param.unifiedField = $('#unifiedField').val();

				/* if(baseUnifiedField == '' || baseUnifiedField == null){
					baseUnifiedField = 'NA';
				} */
				param.baseUnifiedField = $('#baseUnifiedField').val();
				
				var baseUnifiedField = $('#baseUnifiedField').val();
				var netMask  = $('#netMask').val();
				if(netMask == '' || netMask == 'undefined'|| baseUnifiedField == '' || baseUnifiedField == 'undefined'){
					netMask = -1;
				}
				param.netMask = netMask;
				
				var dataType = $('#service-dataType').val();
		    	var rowData = jQuery("#paramList").getRowData(id);
		    	 var partitionRangeEleId = "";
		    	
	    	 	if(rowData['partitionField'] == '<%= PartitionFieldEnum.Date%>'){
	         		partitionRangeEleId = "param-partitionRangeDropDown";
	         	} else {
	         		partitionRangeEleId = "partitionRange";
	         	}
	    	 	
		    	 //console.log("baseUnifiedField is :: " + baseUnifiedField);
		    	param.partitionRange = $('#'+partitionRangeEleId).val();
		    	partitionParamArr[index]=param;
		    	
		    	jQuery("#paramList").jqGrid('setCell', id, 'level', $('#param-level').val());
		    	jQuery("#paramList").jqGrid('setCell', id, 'partitionField', $('#param-partitionField').val());
		    	jQuery("#paramList").jqGrid('setCell', id, 'unifiedField', $('#unifiedField').val());
		    	jQuery("#paramList").jqGrid('setCell', id, 'partitionRange', $('#'+partitionRangeEleId).val());
		    	
		    	jQuery("#paramList").jqGrid('setCell', id, 'baseUnifiedField', $('#baseUnifiedField').val());
		    	jQuery("#paramList").jqGrid('setCell', id, 'netMask', netMask);
		    	
		    	//var $grid = $("#paramList");
		    	
		    	//jQuery("#paramList").jqGrid('setGridParam').trigger("reloadGrid");
				
			}
		});
	}
	
	function updateHashConfigForm(){
		// here we are manually setting partition param grid list to be saved with service update
		//console.log("json object is :: " + JSON.stringify(partitionParamArr));
		$('#partitionParamList').val(JSON.stringify(partitionParamArr));
		$('#iplog-parsing-service-hash-configuration-form').submit();
	}
	
	function validatePartitionParam(){
		resetWarningDisplay();
		
		var id = $("#paramId").val();
		var partitionField = $('#param-partitionField').val();
		var partitionRangeEleId;
		if(partitionField == '<%= PartitionFieldEnum.Date %>'){
     		partitionRangeEleId = "param-partitionRangeDropDown";
     	} else {
     		partitionRangeEleId = "partitionRange";
     	}

		var netMask = $("#netMask").val();
		if(($("#baseUnifiedField").val() == '') || netMask == null || netMask == 'undefined' || netMask == ''){
			netMask = -1 ;
			//$("#netMask").val(netMask);
		} 
		
		$.ajax({
			url: '<%= ControllerConstants.VALIDATE_PARTITION_PARAM %>',
			cache: false,
			async: true,
			dataType: 'json',
			type: "POST",
			data: {
				"id": id,
				"partitionField": $('#param-partitionField').val(),
				"unifiedField": $('#unifiedField').val(),
				"partitionRange": $('#'+partitionRangeEleId).val(),
				"baseUnifiedField": $('#baseUnifiedField').val(),
				"netMask":netMask
			},
			success: function(data){
				var response = eval(data);
				
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				
				if(responseCode == "200"){
					closeFancyBox();
					changeParam(); // if validation success than change grid param
				} else if(responseCode === "400"){
					addErrorIconAndMsgForAjaxPopUp(responseObject);
				} else if(responseObject != undefined && responseObject != 'undefined' || responseCode === "400"){
					resetWarningDisplayPopUp();
					
					if(responseObject["partitionRange"] != undefined){
						addErrorMsgForCreateServer(partitionRangeEleId,responseObject["partitionRange"]);
					}
					
					if(responseObject["netMask"] != undefined){
						addErrorMsgForCreateServer('netMask',responseObject["netMask"]);
					}
					
					if(responseCode === "400") {
						addErrorIconAndMsgForAjaxPopUp(responseObject);
					}
					
					addErrorClassWhenTitleExistPopUp($("#GENERIC_ERROR_MESSAGE", window.parent.document).val());
					parent.resizeFancyBox();
				}else{
					resetWarningDisplayPopUp();
					showErrorMsgPopUp(responseMsg);
					parent.resizeFancyBox();
				}
			},
		    error: function (xhr,st,err){
		    	hideProgressBar();
				handleGenericError(xhr,st,err);
			}
		});
	}
	
	function enableNetMaskfield(){
		if($("#baseUnifiedField").val() == '')
			$("#netMask").prop('disabled',true);
		else
			$("#netMask").prop('disabled',false);
	}
	
</script>
