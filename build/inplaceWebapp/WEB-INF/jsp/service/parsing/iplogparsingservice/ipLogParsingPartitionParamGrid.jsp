<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>


    <div class="fullwidth" id="hashParamGridHeadDiv">
		<div class="title2">
    		<spring:message code="iplog.parsing.summary.partition.param.grid.caption"></spring:message>
   		</div>
   	</div>
     	
     <div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="hashParamGrid"></table>
       	<div id="hashParamGridPagingDiv"></div> 
    </div>


<script>
	function loadHashParamGrid(){
		$("#hashParamGrid").jqGrid({
			url: "<%= ControllerConstants.GET_SERVICE_HASH_CONFIG_LIST%>",
			postData:
					{'serviceId':'${serviceId}' 
					},
			datatype: "json",
			colNames: [
						  "<spring:message code='iplog.parsing.summary.partition.param.grid.column.id' ></spring:message>",
			           	  "<spring:message code='iplog.parsing.summary.partition.param.grid.column.partition.field' ></spring:message>",
			              "<spring:message code='iplog.parsing.summary.partition.param.grid.column.unified.field' ></spring:message>",
			              "<spring:message code='iplog.parsing.summary.partition.param.grid.column.range' ></spring:message>",
			              "<spring:message code='iplog.parsing.service.config.hash.param.grid.column.partion.baseunified' ></spring:message>",
	                      "<spring:message code='iplog.parsing.service.config.hash.param.grid.column.partion.net.mask' ></spring:message>"
				],
			colModel: [
				{name:'id',index:'id',sortable:true},
				{name:'partitionField',index:'partitionField',sortable:true},
				{name:'unifiedField',index:'unifiedField',sortable:true},
				{name:'partitionRange',index:'partitionRange',sortable:true},
				{name:'baseUnifiedField',index:'baseUnifiedField',sortable:false,formatter:baseUnifiedFieldFormatter},
            	{name:'netMask',index:'netMask',sortable:false,formatter:netMaskFormatter},
			],
			rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
			rowList:[10,20,60,100],
			height: 'auto',
			sortname: 'createdDate',
			sortorder: "asc",
			pager: "#hashParamGridPagingDiv",
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
			recordtext: "<spring:message code="iplog.parsing.summary.partition.param.grid.pager.total.records.text"></spring:message>",
		    emptyrecords: "<spring:message code="iplog.parsing.summary.partition.param.grid.empty.records"></spring:message>",
			loadtext: "<spring:message code="iplog.parsing.summary.partition.param.grid.loading.text"></spring:message>",
			pgtext : "<spring:message code="iplog.parsing.summary.partition.param.grid.pager.text"></spring:message>",
			beforeSelectRow: function (rowid, e){
			}
			}).navGrid("#hashParamGridPagingDiv",{edit:false,add:false,del:false,search:false});
			
			$(".ui-jqgrid-titlebar").hide();
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
	
	$(document).ready(function() {
		loadHashParamGrid();
	});
	
</script>
	
