<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%-- <script src="${pageContext.request.contextPath}/customJS/dbConfiguration.js<%= "?v=" + Math.random() %>" type="text/javascript"></script> --%>

<script>
	var dbDetailListCounter=-1;
	var dbDetailList = [];
</script>
<script type="text/javascript">

	function handleOnchange(counter){
		var val = document.getElementById(counter+"_type").value;
		if(val == 1 || val == 2){
			document.getElementById(counter+"_failTimeout").disabled = false;
			document.getElementById(counter+"_failTimeout").readOnly = false;
		}else{
			document.getElementById(counter+"_failTimeout").value='';
			document.getElementById(counter+"_failTimeout").disabled = true;
			document.getElementById(counter+"_failTimeout").readOnly = true;
		}
	}
	
	function addDBDetails(id,name,type,connURL,username,password,minPoolSize,maxPoolSize,failTimeout,actionType){
	dbDetailListCounter++;
	var block ='<form method="POST" id="' + dbDetailListCounter + '_form" > '+
	'<input type="hidden" id="'+dbDetailListCounter+'_dataSourceId" name="'+dbDetailListCounter+'_dataSourceId" value="'+id+'"> '+
	'<div class="box box-warning" id="flipbox_'+dbDetailListCounter+'">'+
	'	<div class="box-header with-border"> '+
    '		<h3 class="box-title" id="title_'+dbDetailListCounter+ '"><spring:message code="dbconfig.database.configuration.tab.section.heading" ></spring:message></h3>'+
    '		<div class="box-tools pull-right">'+
    '			<button class="btn btn-box-tool block-collapse-btn" data-toggle="collapse" data-parent="#pathListConfig" href="#' + dbDetailListCounter + '_block"><i class="fa fa-minus"></i></button>	'+
    '			&nbsp;&nbsp;'+
    '			<sec:authorize access='hasAuthority("DELETE_DATASOURCE")'>'+
    '			<a id="'+name+'_delBtn" class="ion-ios-close-empty remove-block" style="margin-top:12px;" 	onclick="deleteDBPopup(\''+dbDetailListCounter+'\')"></a>' +
    '			</sec:authorize>'+
    '		</div>'+
    '	</div>	'+
    '	<div class="box-body inline-form accordion-body collapse in" id="' + dbDetailListCounter + '_block">'+
 	'		<div class="col-md-6 no-padding">'+
    '			<spring:message code="dbconfig.database.configuration.add.name.label" var="label"></spring:message>'+
    '			<spring:message code="dbconfig.database.configuration.add.name.tooltip" var="tooltip"></spring:message>'+
    '  			<div class="form-group">'+
    '    			<div class="table-cell-label">${label}<span class="required">*</span></div>'+
    '      			<div class="input-group ">'+
    '					<input class="form-control table-cell input-sm" id="' + dbDetailListCounter + '_name" tabindex="3" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}" value="'+name+'"/>'+
	'					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="" ></i></span>' +
    '       		</div>'+
    '  	 		</div>'+
    '		</div>'+
    '		<div class="col-md-6 no-padding">'+
    '			<spring:message code="dbconfig.database.configuration.add.connection.label" var="lable"></spring:message>'+
    '			<spring:message code="dbconfig.database.configuration.add.connection.tooltip" var="tooltip"></spring:message>'+
    '			<div class="form-group">'+
    '				<div class="table-cell-label">${lable}<span class="required">*</span></div>'+
    '				<div class="input-group">'+
    '					<input class="form-control table-cell input-sm" id="' + dbDetailListCounter + '_connURL" tabindex="3" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}" value="'+connURL+'"/>'+
	'					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="" ></i></span>' +
    '				</div>'+
    '			</div>'+
    '		</div>'+
    '		<div class="col-md-6 no-padding">'+
    '			<spring:message code="dbconfig.database.configuration.add.type.label" var="lable"></spring:message>'+
    '			<spring:message code="dbconfig.database.configuration.add.type.tooltip" var="tooltip"></spring:message>'+
    '  			<div class="form-group">'+
    '   			<div class="table-cell-label">${lable}<span class="required">*</span></div>'+
    '      			<div class="input-group ">'+
    '      				<select id="' + dbDetailListCounter + '_type" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" onchange="handleOnchange(' + dbDetailListCounter + ')" title="${tooltip }" value="'+type+'" >'+
	'				        <c:forEach var="type" items="${dbSourceType}" >'+
	'							<option value="${type.value}">${type}</option>'+
	'				      	</c:forEach>'+
    '					</select>' +
	'					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="" ></i></span>'+
    '       		</div>'+
    '     		</div>'+
    '		</div>'+
     '			<div class="col-md-6 no-padding">'+
	'				<spring:message code="dbconfig.database.configuration.add.minpool.size.tooltip" var="minTooltip"></spring:message>'+
	'				<spring:message code="dbconfig.database.configuration.add.maxpool.size.tooltip" var="maxTooltip"></spring:message>'+
     '			 	<div class="form-group" >'+ 
	'					<div class="table-cell-label"><spring:message code="dbconfig.database.configuration.add.pool.size.label"></spring:message></div>'+
	'					<div class="input-group">'+
	
	'					<div class="col-md-6 no-padding">'+
	'					<div class="form-group">'+
    '					<div class="input-group">'+
	
	'						<input class="form-control table-cell input-sm" id="' + dbDetailListCounter + '_minPoolSize" tabindex="3" data-toggle="tooltip" data-placement="bottom" title="${minTooltip }" value="'+minPoolSize+'"/>'+
	'						<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="" ></i></span>'+
	'					</div>'+
	'					</div>'+
	'					</div>'+
	'					<div class="col-md-6 no-padding">'+
	'					<div class="form-group">'+
    '					<div class="input-group">'+
	'						<input class="form-control table-cell input-sm" id="' + dbDetailListCounter + '_maxPoolsize" tabindex="3" data-toggle="tooltip" data-placement="bottom" title="${maxTooltip }" value="'+maxPoolSize+'"//>'+
	'						<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title="" ></i></span>'+
	'					</div>'+
    '			</div>'+
    '		</div>'+
    '			</div>'+
    '		</div>'+
    '</div>'+
    '<div class="clearfix"> </div>'+
    
    '		<div class="col-md-6 no-padding">'+
    '			<spring:message code="dbconfig.database.configuration.add.username.label" var="label"></spring:message>'+
    '			<spring:message code="dbconfig.database.configuration.add.username.tooltip" var="tooltip"></spring:message>'+
    '  			<div class="form-group">'+
    '    			<div class="table-cell-label">${label}<span class="required">*</span></div>'+
    '      			<div class="input-group ">'+
    '					<input class="form-control table-cell input-sm" id="' + dbDetailListCounter + '_username" tabindex="3" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="'+username+'"/>'+
	'					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="" ></i></span>'+
    '        		</div>'+
    '     		</div>'+
    '		</div> '+
    '		<div class="col-md-6 no-padding" >'+
    '			<spring:message code="dbconfig.database.configuration.add.password.label" var="label"></spring:message>'+
    '			<spring:message code="dbconfig.database.configuration.add.password.tooltip" var="tooltip"></spring:message>'+
    '  			<div class="form-group">'+
    '    			<div class="table-cell-label">${label}<span class="required">*</span></div>'+
    '      			<div class="input-group ">'+
    '					<input class="form-control table-cell input-sm" id="' + dbDetailListCounter + '_password" type="password" tabindex="3" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="'+password+'"/>'+
	'					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="" ></i></span>'+
    '        		</div>'+
    '     		</div>'+
    '		</div> '+
    
    
    
    '		<div class="col-md-6 no-padding" >'+
    '			<spring:message code="dbconfig.database.configuration.add.failover.timeout.label" var="label"></spring:message>'+
    '			<spring:message code="dbconfig.database.configuration.add.failover.timeout.tooltip" var="tooltip"></spring:message>'+
    '  			<div class="form-group">'+
    '    			<div class="table-cell-label">${label}</div>'+
    '      			<div class="input-group ">'+
    '					<input class="form-control table-cell input-sm" id="' + dbDetailListCounter + '_failTimeout" onkeydown="isNumericOnKeyDown(event)" type="text" tabindex="4" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="'+failTimeout+'" />'+
	'					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="" ></i></span>'+
    '        		</div>'+
    '     		</div>'+
    '		</div> '+
    
    
    
	'		<div class="col-md-12 no-padding">'+
    '			<spring:message code="ftp.driver.mgmt.pathlist.max.count.limit" var="tooltip"></spring:message>'+
    '  			<div class="form-group">'+
    '      				<div id="' + dbDetailListCounter + '_buttons-div" class="input-group" >'+
    '						<sec:authorize access='hasAuthority("EDIT_DATASOURCE")'>'+
    '		    				<button type="button" class="btn btn-grey btn-xs " id="' + dbDetailListCounter + '_testbtn" data-parent="#pathListConfig" href="#' + dbDetailListCounter + '_testbtn" onclick="testDataBaseConfigurationDetails(\''+dbDetailListCounter+'\')";><spring:message code="btn.label.test"></spring:message></button>&nbsp;'+
    '		    				<button type="button" class="btn btn-grey btn-xs " id="' + dbDetailListCounter + '_updatebtn" data-parent="#pathListConfig" href="#' + dbDetailListCounter + '_updatebtn" onclick="updateDataBaseConfigurationDetails(\''+dbDetailListCounter+'\')";><spring:message code="btn.label.update"></spring:message></button>&nbsp;'+
    '      				    	<button type="button" class="btn btn-grey btn-xs " id="' + dbDetailListCounter + '_savebtn"  data-parent="#pathListConfig" href="#' + dbDetailListCounter + '_savebtn" onclick="addDataSourceDetails(\''+dbDetailListCounter+'\');"><spring:message code="btn.label.save"></spring:message></button>&nbsp;' +
    '							<button id="resetBtn" type="button" class="btn btn-grey btn-xs " onclick="resetDriverDetailDiv(\''+dbDetailListCounter+'\');"><spring:message code="btn.label.reset"></spring:message></button>' +
    '						</sec:authorize>'+
    '       			</div>'+
    '					<div id="' + dbDetailListCounter + '_progress-bar-div" class="input-group" style="display: none;"> '+
    '						<label>Processing Request &nbsp;&nbsp; </label> '+
    '						<img src="img/processing-bar.gif">'+
    '					</div>'
    '   		</div>' +
    '		</div>'+
 	'	</div>'+
	'</div>';
 	'</div>'+
	'</div>';
	
	$('#dbDetailList').prepend(block);
	
	
	if(actionType == 'UPDATE'){
		$('#'+dbDetailListCounter+'_savebtn').hide();
		$('#'+dbDetailListCounter+'_type').val(type);
	}else{
		resetWarningDisplay();
		clearAllMessages();	
		$("#"+dbDetailListCounter+"_updatebtn").hide();
	}
	handleOnchange(dbDetailListCounter);
	
}
	/* Function will display progress bar for add/edit action in pop-up. */
	 function showProgressBar(dbDetailListCounter){
		$('#' + dbDetailListCounter + '_buttons-div').hide();
		$('#' + dbDetailListCounter + '_progress-bar-div').show();
	}
	 /*Function will hide progress bar for add/edit action in pop-up. */
	function hideProgressBar(dbDetailListCounter){
		$('#' + dbDetailListCounter + '_buttons-div').show();
		$('#' + dbDetailListCounter + '_progress-bar-div').hide();
	} 
	
	

</script>
<div class="tab-content no-padding">
   		<div class="fullwidth mtop10">
	   		<div class="title2">
	   		
	   			<spring:message code="dbconfig.database.configuration.main.title"></spring:message> 
	   			<sec:authorize access="hasAuthority('ADD_DATASOURCE')">
	   			<span class="title2rightfield"> 
	   				<span class="title2rightfield-icon1-text">
	   					<a href="#" id="addDBDetailbtn" onclick="addDBDetails('','','','','','','','','10','ADD');"><i class="fa fa-plus-circle"></i></a>
	   					<a href="#" id="addDBDetail" onclick="addDBDetails('','','','','','','','','10','ADD');"><spring:message code="dbconfig.database.configuration.add.link.label"></spring:message> </a>
	   				</span> 
	   			</span> 
	   			</sec:authorize>
	   		</div>
	   		<div class="clearfix"></div>
        </div>
         
       <a href="#view_server_instance_details" class="fancybox"
			style="display: none;" id="view_server_instance_details_link">#</a>
		<div id="view_server_instance_details"
			style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title"> 
						<spring:message code="datasourceManagement.update.popup.header" ></spring:message>
					</h4>
				</div>
				<div class="modal-body padding10 inline-form">
					<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
					<div class="box-body table-responsive no-padding box" id=instanceListDiv>
						<table class="table table-hover" id="instanceList"></table>
						<div id=instanceListPagingDiv></div>
						<div class="clearfix"></div>
						<div id="divLoading" align="center" style="display: none;">
							<img src="img/preloaders/Preloader_10.gif" />
						</div>
					</div>
					<div id = "updateNoteDiv">
						<span class="note">
							<spring:message code="serverManagement.popup.imp.note.lbl" ></spring:message>
						</span>
						<br>
						<span>
						 <spring:message code="DataSourceConfig.update.warning.message"></spring:message>
						</span>
					</div>					
				</div>
				<div id="update-ds-div" class="modal-footer padding10">
			         
			            <button id="updateYesBtn" type="button" class="btn btn-grey btn-xs " onclick="sendUpdateDataBaseConfiguration();"><spring:message code="btn.label.yes"></spring:message></button>
			            <button id="updateNoBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
			    </div>
			    <div class="modal-footer padding10" id="update-reload-ds-details" style="display:none;">
			            <button id="closeBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
			    </div>
			</div>
			<!-- /.modal-content -->
		</div>
		
        <div class="fullwidth" id="dataase_details_blocks">       
       		<div id="dbDetailList"></div>
        	<c:if test="${dbDetailList != null }">
				<script>dbDetailListCounter++;</script>
				<c:forEach var="db" items="${dbDetailList}">
					<script>
						addDBDetails(	'${db.id}',
										'${db.name}',
										'${db.type}',
										'${db.connURL}',
										'${db.username}',
										'${db.password}',
										'${db.minPoolSize}',
										'${db.maxPoolsize}',
										'${db.failTimeout}',
										'UPDATE'
							);
						var db= {'id':'${db.id}',		
								'name':'${db.name}',		
								'connURL':'${db.type}',		
								'type':'${db.connURL}',		
								'username':'${db.username}',		
								'password':'${db.password}',		
								'minPoolSize':'${db.minPoolSize}',		
								' maxPoolsize':'${db.maxPoolsize}',
								'failTimeout':'${db.failTimeout}'
								};
						dbDetailList.push(db);
						$("#"+dbDetailListCounter+"_block").collapse("data-toggle");
		 			</script> 
				</c:forEach>
			</c:if>
    	</div>
   	</div>
   	
	<div id="divMessage" style=" width:100%; display: none;" >

		    <div class="modal-content">

		        <div class="modal-header padding10" >
		            <h4 class="modal-title" id="status-title" style="display:none;"><spring:message code="datasourceManagement.delete.popup.header"></spring:message></h4>		            
		        </div>
		        
		        <div class="modal-body padding10 inline-form">
		         	<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		    
 			        <p id="deleteWarningMessage">
			       		<spring:message code="DataSourceConfig.delete.warning.message"></spring:message>
			        </p>
		        </div>
		        
			    <div id="delete-ds-div" class="modal-footer padding10">
			         
			    	<button id="delYesBtn" type="button" class="btn btn-grey btn-xs " onclick="deleteDataBaseConfigurationDetails();"><spring:message code="btn.label.yes"></spring:message></button>
			        <button id="delNoBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
			    </div>
			    <div class="modal-footer padding10" id="reload-ds-details" style="display:none;">
			        <button id="delCloseBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
			    </div>
		      <%--   </sec:authorize> --%>
		    </div>
		    <!-- /.modal-content --> 
		</div>
				
    	<a href="#divMessage" class="fancybox" style="display: none;" id="dsMessage">#</a>    	
<script>

function resetDriverDetailDiv(divCounter){
	resetWarningDisplay();
	clearAllMessages();
	$('#' + divCounter + '_name').val('');
	//$('#' + divCounter + '_type').val('');
	$('#' + divCounter + '_connURL').val('');
	$('#' + divCounter + '_username').val('');
	$('#' + divCounter + '_password').val('');
	$('#' + divCounter + '_minPoolSize').val('');
	$('#' + divCounter + '_maxPoolsize').val('');
	$('#' + divCounter + '_failTimeout').val('');
	
 }
function reloadDataSourceDetails(){
	closeFancyBox();
}
function cancelConfig(divCounter){
	$('#flipbox_'+dbDetailListCounter).remove();
}
function removeDBDetailBlock(elementId){
	$( "#flipbox_"+elementId ).remove();
}
function addDataSourceDetails(counter){
	
	resetWarningDisplay();
	clearAllMessages();
	showProgressBar(counter);
	if($('#' + counter + '_minPoolSize').val()=="" || $('#' + counter + '_minPoolSize').val()==null){
		$('#' + counter + '_minPoolSize').val(10);
	}
	if($('#' + counter + '_maxPoolsize').val()=="" || $('#' + counter + '_maxPoolsize').val()==null){
		$('#' + counter + '_maxPoolsize').val(15);
	}
	if($('#' + counter + '_failTimeout').val()==null){
		$('#' + counter + '_failTimeout').val('');
	}
	$.ajax({
		url:'<%=ControllerConstants.CREATE_DATASOURCE_CONFIGURATION%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"name" 						:	$('#' + counter + '_name').val(),
			"connURL"          			:	$('#' + counter + '_connURL').val(),
			"type" 						:	$('#' + counter + '_type').find(":selected").val(),
			"username" 					:	$('#' + counter + '_username').val(),
			"password" 					:   $('#' + counter + '_password').val(),
			"minPoolSize" 				:   $('#' + counter + '_minPoolSize').val(),
			"maxPoolsize"				:	$('#' + counter + '_maxPoolsize').val(),
			"failTimeout"				:	$('#' + counter + '_failTimeout').val(),
			"dbDetailListCounter"       :   counter,
		}, 
		
		success: function(data){
			hideProgressBar(counter);
			resetWarningDisplay();
			clearAllMessages();
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			$("#deleteWarningMessage").hide();
			$("#delete-ds-div").hide();
			$("#status-title").hide();
			
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessagesPopUp();
				showSuccessMsg(responseMsg);
				$('#'+counter+'_savebtn').hide();
				$("#"+counter+"_updatebtn").show();
				$('#'+counter+'_dataSourceId').val(responseObject["id"]);
				var db = {'id':responseObject["id"],
						'name':responseObject["name"],
						'connURL':responseObject["connURL"],
						'type':responseObject["type"],
						'username':responseObject["username"],
						'password':responseObject["password"],
						'minPoolSize':responseObject["minPoolSize"],
						'maxPoolsize':responseObject["maxPoolsize"],
						'failTimeout':responseObject["failTimeout"]};
				dbDetailList.push(db); 
				$("#"+dbDetailListCounter+"_block").collapse("toggle");
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				hideProgressBar(counter);
				showErrorMsg(responseMsg);
				addErrorIconAndMsgForAjax(responseObject); 
					
			}else{
				hideProgressBar(counter);
				resetWarningDisplay();
				clearAllMessagesPopUp();
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(counter);
			handleGenericError(xhr,st,err);
		}
	});
}

var updateBlockCounter , updateDataSourceId, isServerFound;
function updateDataBaseConfigurationDetails(counter){	
	clearAllMessages();
	$("#instanceList").GridUnload();
	$("#instanceListPagingDiv").hide();
	clearAllMessagesPopUp();
	updateBlockCounter = counter;
	$("#update-reload-ds-details").hide();
	updateDataSourceId= $('#'+counter+'_dataSourceId').val();	
	getServerInstanceList();		
}

function getServerInstanceList(){
	$("#instanceList").jqGrid({
		url: "<%= ControllerConstants.GET_SERVER_INSTANCE_LIST %>",
 	  	postData: {
 		 "id":	$('#' +	updateBlockCounter	+'_dataSourceId').val(),
 		}, 
    datatype: "json",    
    colNames:[
              "<spring:message code='serverManagement.grid.column.server.name' ></spring:message>",
              "<spring:message code='serverManagement.grid.column.server.instance' ></spring:message>",
              "<spring:message code='serverManagement.grid.column.server.hostIp' ></spring:message>",
              "<spring:message code='serverManagement.grid.column.server.port' ></spring:message>",
             ],
	colModel:[
    	{name:'servername',index:'server.name'},
        {name:'instanceName',index:'name',sortable:true},
    	{name:'host',index:'server.ipAddress',sortable:true},
    	{name:'port',index:'port',align:'center',sortable:true},
    ],
    rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
    rowList:[10,20,60,100],
    height: 'auto',
	sortname: 'id',
	sortorder: "desc",
    pager: "#instanceListPagingDiv",
    viewrecords: true,
    timeout : 120000,
    loadtext: "Loading...",    
    caption: "<spring:message code='serverManagement.grid.caption'></spring:message>",
	loadComplete: function(data) {				
		gridDataArr = data.rows;		
		$.each(gridDataArr, function (index, instance) {
			instanceList[instance.id]=instance;
		});		
		$(".ui-dialog-titlebar").show();							
	},
	onPaging: function (pgButton) {
		clearResponseMsgDiv();
		clearInstanceGrid();
	},
	loadError : function(xhr,st,err) {
		handleGenericError(xhr,st,err);
	},
	gridComplete : function() {
		var rowCount = jQuery("#instanceList").jqGrid('getGridParam', 'records');
		if(rowCount==0){		
			sendUpdateDataBaseConfiguration();
		}else{
			$("#instanceListDiv").show();
			$("#instanceListPagingDiv").show();
			$("#updateNoteDiv").show();			
			$("#update-ds-div").show();
			$("#update-reload-ds-details").hide();
			$("#view_server_instance_details_link").click();
		}
	},
	recordtext: "<spring:message code='serverManagement.grid.pager.total.records.text'></spring:message>",
    emptyrecords: "<spring:message code='serverManagement.grid.empty.records'></spring:message>",
	loadtext: "<spring:message code='serverManagement.grid.loading.text'></spring:message>",
	pgtext : "<spring:message code='serverManagement.grid.pager.text'></spring:message>"
});
	
}
function sendUpdateDataBaseConfiguration(){
	resetWarningDisplay();
	clearAllMessages();
	clearAllMessagesPopUp();
	showProgressBar(updateBlockCounter);
	//take default values if the values is not passed for min and max Pool size
	if($('#' + updateBlockCounter + '_minPoolSize').val()=="" || $('#' + updateBlockCounter + '_minPoolSize').val()==null){
		$('#' + updateBlockCounter + '_minPoolSize').val(10);
	}
	if($('#' + updateBlockCounter + '_maxPoolsize').val()=="" || $('#' + updateBlockCounter + '_maxPoolsize').val()==null){
		$('#' + updateBlockCounter + '_maxPoolsize').val(15);
	}
	if($('#' + updateBlockCounter + '_failTimeout').val()==null){
		$('#' + updateBlockCounter + '_failTimeout').val('');
	}
	
	var type = $('#' + updateBlockCounter + '_type').find(":selected").val()
	$.ajax({
		url:'<%=ControllerConstants.UPDATE_DATASOURCE_CONFIGURATION%>',
		cache:false,
		async:true,
		dataType:'json',
		type: "POST",
		data:
		{
			"id"						:	$('#' +	updateBlockCounter	+'_dataSourceId').val(),
			"name" 						:	$('#' + updateBlockCounter + '_name').val(),
			"connURL"          			:	$('#' + updateBlockCounter + '_connURL').val(),
			"type" 						:	type,
			"username" 					:	$('#' + updateBlockCounter + '_username').val(),
			"password" 					:   $('#' + updateBlockCounter + '_password').val(),
			"minPoolSize" 				:   $('#' + updateBlockCounter + '_minPoolSize').val(),
			"maxPoolsize"				:	$('#' + updateBlockCounter + '_maxPoolsize').val(),
			"failTimeout"				:	$('#' + updateBlockCounter + '_failTimeout').val(),
			"dbDetailListCounter"       :   updateBlockCounter,
		}, 
		success: function(data){
			hideProgressBar(updateBlockCounter);
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			$("#update-ds-div").hide();
			if(responseCode == "200"){		
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
				$("#instanceListDiv").hide();
				$("#updateNoteDiv").hide();
				$("#update-reload-ds-details").hide();
				$("#view_server_instance_details").hide();
				closeFancyBox();
				$("#"+updateBlockCounter+"_block").collapse("toggle");
				$('#'+updateBlockCounter+'_savebtn').hide();
				$("#"+updateBlockCounter+"_updatebtn").show();
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				hideProgressBar(updateBlockCounter);
				closeFancyBox();
				clearAllMessages();
				showErrorMsg(responseMsg);
				addErrorIconAndMsgForAjax(responseObject); 
			}else{
				hideProgressBar(updateBlockCounter);
				closeFancyBox();
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(updateBlockCounter);
			handleGenericError(xhr,st,err);
		}
	}); 
}

function clearInstanceGrid(){
	var $grid = $("#instanceList");
	var rowIds = $grid.jqGrid('getDataIDs');
	// iterate through the rows and delete each of them
	for(var i=0,len=rowIds.length;i<len;i++){
		var currRow = rowIds[i];
		$grid.jqGrid('delRowData', currRow);
	}
}


var blockCounter , deleteDataSourceId;

function deleteDBPopup(counter){
	
	blockCounter = counter;
	clearAllMessagesPopUp();
	clearAllMessages();
	$("#reload-ds-details").hide();
	deleteDataSourceId= $('#'+counter+'_dataSourceId').val();
	
	if(deleteDataSourceId == null || deleteDataSourceId == 'null' || deleteDataSourceId == ''){
		$("#"+counter+"_form").remove();
	}else{					
		$("#deleteWarningMessage").show();
		$("#status-title").show();	
		$("#delete-ds-div").show();
		$("#dsMessage").click();				
	}
}
function deleteDataBaseConfigurationDetails(){
		clearAllMessages();
		resetWarningDisplay();
		$.ajax({
			url: '<%=ControllerConstants.DELETE_DATASOURCE_CONFIGURATION%>',
			cache: false,
			async: true,
			dataType: 'json',
			type: "POST",
			data:
			{
				"id"						:	deleteDataSourceId,
			}, 
			success: function(data){
				clearAllMessages();
				hideProgressBar(dbDetailListCounter);
				resetWarningDisplay();
				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				$("#deleteWarningMessage").hide();
				$("#status-title").hide();	
				$("#delete-ds-div").hide();
				if(responseCode == "200"){
					resetWarningDisplay();
					clearAllMessages();					
					showSuccessMsg(responseMsg);
					$("#divMessage").hide();
					$("#reload-ds-details").hide();					
					closeFancyBox();
					$("#"+blockCounter+"_form").remove();
				}else if(responseCode == "400"){
					showErrorMsgPopUp(responseMsg);
					$("#divMessage").show();	
					$("#status-title").show();
					$("#reload-ds-details").show();
					$("#dsMessage").click();
				}else{					
					showErrorMsgPopUp(responseMsg);
					$("#divMessage").show();	
					$("#status-title").show();
					$("#reload-ds-details").show();
					$("#dsMessage").click();
				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		}); 
}
		function testDataBaseConfigurationDetails(counter){
			resetWarningDisplay();
			clearAllMessages();
			showProgressBar(counter);
			deleteDataSourceId= $('#'+counter+'_dataSourceId').val();
			if($('#' + counter + '_minPoolSize').val()=="" || $('#' + counter + '_minPoolSize').val()==null){
				$('#' + counter + '_minPoolSize').val(10);
			}
			if($('#' + counter + '_maxPoolsize').val()=="" || $('#' + counter + '_maxPoolsize').val()==null){
				$('#' + counter + '_maxPoolsize').val(15);
			}
			$.ajax({
				url:'<%=ControllerConstants.TEST_DATASOURCE_CONFIGURATION%>',
				cache: false,
				async: true,
				dataType: 'json',
				type: "POST",
				data:
				 {
					"name" 						:	$('#' + counter + '_name').val(),
					"connURL"          			:	$('#' + counter + '_connURL').val(),
					"type" 						:	$('#' + counter + '_type').find(":selected").val(),
					"username" 					:	$('#' + counter + '_username').val(),
					"password" 					:   $('#' + counter + '_password').val(),
					"minPoolSize" 				:   $('#' + counter + '_minPoolSize').val(),
					"maxPoolsize"				:	$('#' + counter + '_maxPoolsize').val(),
					"failTimeout"				:	$('#' + counter + '_failTimeout').val(),
					"dbDetailListCounter"       :   counter,
				},
				success: function(data){
					hideProgressBar(counter);
					resetWarningDisplay();
					clearAllMessages();
					var response = eval(data);
					var responseCode = response.code; 
					var responseMsg = response.msg; 
					var responseObject = response.object;
					
					$("#deleteWarningMessage").hide();
					$("#delete-ds-div").hide();
					$("#status-title").hide();
					
					if(responseCode == "200"){
						resetWarningDisplay();
						clearAllMessagesPopUp();
						showSuccessMsg(responseMsg);
					}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
						hideProgressBar(counter);
						showErrorMsg(responseMsg);
						addErrorIconAndMsgForAjax(responseObject); 
							
					}else{
						hideProgressBar(counter);
						resetWarningDisplay();
						clearAllMessagesPopUp();
						showErrorMsg(responseMsg);
					}
				},
			    error: function (xhr,st,err){
			    	hideProgressBar(counter);
					handleGenericError(xhr,st,err);
				}
			
			});
		}
</script>
