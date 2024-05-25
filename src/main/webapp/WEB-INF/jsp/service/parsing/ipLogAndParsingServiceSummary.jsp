<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<script>
var jsLocaleMsg = {};
jsLocaleMsg.serviceId = "<spring:message code='serviceManagement.grid.column.service.id'></spring:message>";
jsLocaleMsg.serviceNm = "<spring:message code='serviceManagement.grid.column.service.name'></spring:message>";
jsLocaleMsg.moduleName="<spring:message code='server.instance.import.header.label.module.name'></spring:message>";
jsLocaleMsg.entityName = "<spring:message code='server.instance.import.header.label.entity.name'></spring:message>";
jsLocaleMsg.propertyName = "<spring:message code='server.instance.import.header.label.property.name'></spring:message>";
jsLocaleMsg.propertyValue = "<spring:message code='server.instance.import.header.label.property.value'></spring:message>";
jsLocaleMsg.errorMessage = "<spring:message code='server.instance.import.header.label.error.message'></spring:message>";
jsLocaleMsg.LineNo = "<spring:message code='server.instance.import.header.label.line.number'></spring:message>";
</script>

<div class="fullwidth mtop10">	

		<!-- Counter section start here -->
		
		<jsp:include page="../serviceCounterSummary.jsp"></jsp:include>
		
		<!-- Counter section end here -->			
			
		<!-- Jquery Driver Grid start here -->
		<div class="fullwidth" id="parserGridHeadDiv">
			<div class="title2">
       			<spring:message code="iplog.parsing.service.summary.plugin.grid.caption"></spring:message>
      		</div>
      	</div>
      	
      	<div class="box-body table-responsive no-padding box">
			<table class="table table-hover" id="parserGrid"></table>
	        <div id="parserGridPagingDiv"></div> 
	    </div>
	    
	    <!-- Jquery Partition Param Grid start here -->
	    <c:if test="${serviceType eq 'IPLOG_PARSING_SERVICE'}">
	    <jsp:include page="iplogparsingservice/ipLogParsingPartitionParamGrid.jsp"></jsp:include>
	    </c:if>
	    <!-- Jquery Partition Param Grid end here -->

		<!-- Jquery Agent Grid start here -->		
		<jsp:include page="../serviceAgentList.jsp"></jsp:include>		
		<!-- Jquery Agent Grid end here -->	
		
		<!--  Jqeury All footer pop up start here -->
		<div id="divSynchronize" style=" width:100%; display: none;" >
		    <jsp:include page="../synchronizationPopUp.jsp"></jsp:include>
		</div>
		<jsp:include page="../commonServiceSyncPublish.jsp"></jsp:include>
		<div id="divImportConfig" style=" width:100%; display: none;" >
		    <jsp:include page="../importServiceWiseConfigPopUp.jsp"></jsp:include>
		</div>
		<div id="divStartService" style=" width:100%; display: none;" >
		    <jsp:include page="../startServicePopUp.jsp"></jsp:include>
		</div>
		<div id="divStopService" style=" width:100%; display: none;" >
		     <jsp:include page="../stopServicePopUp.jsp"></jsp:include>
		</div>
		
		<a href="#divSynchronize" class="fancybox" style="display: none;" id="synchronize">#</a>
       	<a href="#divImportConfig" class="fancybox" style="display: none;" id="importconfig">#</a>
       	<a href="#divStopService" class="fancybox" style="display: none;" id="stop_service">#</a>
       	<a href="#divStartService" class="fancybox" style="display: none;" id="start_service">#</a>
		
		<form action="<%=ControllerConstants.EXPORT_SERVICE_INSTANCE_CONFIG%>" id="export-service-instance-config-form" method="POST">
        	<input type="hidden" id="exportServiceInstanceId" name="exportServiceInstanceId"  value=""/>
        	<input type="hidden" id="isExportForDelete" name="isExportForDelete"  value=""/>
        	<input type="hidden" id="<%=BaseConstants.REQUEST_ACTION_TYPE %>" name="<%=BaseConstants.REQUEST_ACTION_TYPE %>" value="<%=BaseConstants.SERVICE_MANAGEMENT%>"/>
       </form>
       

</div>

	
<script type="text/javascript">
	var serviceTypeVal = '${serviceType}';
	$(document).ready(function() {
		loadParserGrid();		
		loadJqueryAgentGrid();		
		if(serviceTypeVal == 'IPLOG_PARSING_SERVICE'){
			getServiceCounterDetails('<%=ControllerConstants.GET_COLLECTION_SERVICE_COUNTER_STATUS%>','${serviceId}','<%=EngineConstants.IPLOG_PARSING_SERVICE%>','ONLOAD');
		}
		else if(serviceTypeVal =='PARSING_SERVICE'){
			getServiceCounterDetails('<%=ControllerConstants.GET_COLLECTION_SERVICE_COUNTER_STATUS%>','${serviceId}','<%=EngineConstants.PARSING_SERVICE%>','ONLOAD');
		}
	});

	function reloadGridData(){
		var $grid = $("#parserGrid");
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
	}

	
	function exportConfigPopup(){
		$("#exportServiceInstanceId").val($("#serviceId").val()); // set service instance id which is selected for export to submit with form.
		$("#isExportForDelete").val(false);
		$("#REQUEST_ACTION_TYPE").val('<%=BaseConstants.IPLOG_SERVICE_SUMMARY%>');
		$("#export-service-instance-config-form").submit();
	}

	var ckIntanceSelected = new Array();
	
	function loadParserGrid(){
		$("#parserGrid").jqGrid({
			url: "<%= ControllerConstants.GET_SERVICE_PARSER_LIST%>",
			postData:
					{'serviceId':'${serviceId}' 
					},
			datatype: "json",
			colNames: [
						  "<spring:message code='iplog.parsing.summary.parser.grid.column.id' ></spring:message>",
			           	  "<spring:message code='iplog.parsing.summary.parser.grid.column.plugin.name' ></spring:message>",
			              "<spring:message code='iplog.parsing.summary.parser.grid.column.plugin.type' ></spring:message>",
			              ""
				],
			colModel: [
				{name:'id',index:'id',sortable:false},
				{name:'name',index:'name',sortable:true,formatter:pluginNameFormatter},
				{name:'type',index:'parserType.type',sortable:true},
				{name:'alias',index:'parserType.alias',hidden:true},
				
			],
			rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
			rowList:[10,20,60,100],
			height: 'auto',
			sortname: 'createdDate',
			sortorder: "asc",
			pager: "#parserGridPagingDiv",
			viewrecords: true,
			multiselect: false,
			timeout : 120000,
		    loadtext: "Loading...",
			caption: "<spring:message code="iplog.parsing.summary.parser.grid.caption"></spring:message>",
			beforeRequest:function(){
		    },
			loadComplete: function(data) {
				
			},
			loadError : function(xhr,st,err) {
				handleGenericError(xhr,st,err);
			},
			recordtext: "<spring:message code="iplog.parsing.summary.parser.grid.pager.total.records.text"></spring:message>",
		    emptyrecords: "<spring:message code="iplog.parsing.summary.parser.grid.empty.records"></spring:message>",
			loadtext: "<spring:message code="iplog.parsing.summary.parser.grid.loading.text"></spring:message>",
			pgtext : "<spring:message code="iplog.parsing.summary.parser.grid.pager.text"></spring:message>",
			beforeSelectRow: function (rowid, e){
			}
			}).navGrid("#parserGridPagingDiv",{edit:false,add:false,del:false,search:false});
			
			$(".ui-jqgrid-titlebar").hide();
	}
	
	
	function pluginNameFormatter(cellvalue, options, rowObject){
		<sec:authorize access="hasAuthority('VIEW_PARSER')">
			return '<a class="link" onclick="redirectParserConfig('+"'"+rowObject["id"]+"','"+cellvalue+"','"+rowObject['alias']+"'"+');">' + cellvalue + '</a>' ;
 		</sec:authorize>
 		<sec:authorize access="!hasAuthority('VIEW_PARSER')">
			return cellvalue;
		</sec:authorize>
	}
	

</script>

