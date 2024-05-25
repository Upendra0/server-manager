<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
  <%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
    <%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
    <%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<script>

function displayGridForMappedClientMappedAlert(alertList){
	//alert("grid call");
	
	$("#snmpClientMappedAlertgrid").jqGrid({  
	  
	    datatype: "local",
        colNames:[
				  "<spring:message code='snmpClientList.grid.column.alertList.id' ></spring:message>",
				  "<spring:message code='snmpClientList.grid.column.alertList.id' ></spring:message>",
				  "<spring:message code='snmpClientList.grid.column.alertList.name' ></spring:message>",
                 
                 ],
		colModel:[
			{name:'id',index:'id',hidden:true},
			{name:'alertId',index:'alertId',sortable:true},
			{name:'name',index:'name',sortable:true},
        	
        	
        ],
        
        rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
        rowList:[10,20,60,100],
        height: 'auto',
        mtype:'POST',
		sortname: 'id',
 		sortorder: "desc",
        pager: "#snmpClientMappedAlertgridPagingDiv",
        contentType: "application/json; charset=utf-8",
        viewrecords: true,
        multiselect: false,
        timeout : 120000,
        loadtext: "Loading...",
        caption: "<spring:message code='snmpClientList.grid.column.alertList.lable'></spring:message>",
        beforeRequest:function(){
            $(".ui-dialog-titlebar").hide();
        }, 
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
       
        loadComplete: function(data) {
        	//alert("load comlete call");
			$(".ui-dialog-titlebar").show();
 			
 			
		},
		onPaging: function (pgButton) {
		
			
		},
		beforeSelectRow: function (rowid, e){
			// this blank function will not select the entire row. Only checkbox can be selectable.
			
		},
		
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		onSelectAll:function(id,status){


		},
		recordtext: "<spring:message code='regex.parser.attr.grid.pager.total.records.text'></spring:message>",
        emptyrecords: "<spring:message code='regex.parser.attr.grid.empty.records'></spring:message>",
		loadtext: "<spring:message code='regex.parser.attr.grid.loading.text'></spring:message>",
		pgtext : "<spring:message code='regex.parser.attr.grid.pager.text'></spring:message>",
	}).navGrid("#snmpClientMappedAlertgridPagingDiv",{edit:false,add:false,del:false,search:false});
	
	$(".ui-jqgrid-titlebar").hide();
}

</script>

		<div class="modal-content">
			<div class="modal-header padding10">
					<h4 class="modal-title">
						<i class="icon-hdd"></i> 
						
						 <span id="mappedAlertLabel" style="display: none;"><spring:message
									code="snmpClientList.grid.column.mapped.alertList.label" ></spring:message></span> 
											</h4>
				
</div>	
				<div class="modal-body padding10 inline-form">
					<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
					
						<div class="box-body table-responsive no-padding box">
							<table class="table table-hover" id="snmpClientMappedAlertgrid"></table>
	           			<div id="snmpClientMappedAlertgridPagingDiv"></div> 
	         </div>	
	         </div>
	         
	         	<div id="client-mapped-alerts-buttons-div" class="modal-footer padding10">
					
					<button onclick="javascript:parent.closeFancyBoxFromChildIFrame()" class="btn btn-grey btn-xs ">
						<spring:message code="btn.label.cancel" ></spring:message>
					</button>
					
				</div>	
				
					
</div>
