<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%-- <script src="${pageContext.request.contextPath}/customJS/dbConfiguration.js<%= "?v=" + Math.random() %>" type="text/javascript"></script> --%>

<script>
	var kafkaDetailListCounter=-1;
	var kafkaDetailList = [];
</script>
<script type="text/javascript">

	function addKafkaDataSourceDetails(id,name,kafkaServerIpAddress,kafkaServerPort,maxRetryCount,maxResponseWait,kafkaProducerRetryCount,kafkaProducerRequestTimeout,kafkaProducerRetryBackoff,kafkaProducerDeliveryTimeout,kafkaDSName,actionType){
		kafkaDetailListCounter++;
		var block ='<form method="POST" id="' + kafkaDetailListCounter + '_form" > '+
		'<input type="hidden" id="'+kafkaDetailListCounter+'_kafkaDataSourceId" name="'+kafkaDetailListCounter+'_kafkaDataSourceId" value="'+id+'"> '+
		'<div class="box box-warning" id="flipbox_'+kafkaDetailListCounter+'">'+
		'	<div class="box-header with-border"> '+
	    '		<h3 class="box-title" id="title_'+kafkaDetailListCounter+ '">'+kafkaDSName+'</h3>'+
	    '		<div class="box-tools pull-right">'+
	    '			<button class="btn btn-box-tool block-collapse-btn" data-toggle="collapse" data-parent="#pathListConfig" href="#' + kafkaDetailListCounter + '_block"><i class="fa fa-minus"></i></button>	'+
	    '			&nbsp;&nbsp;'+
	    '			<sec:authorize access='hasAuthority("DELETE_KAFKA_DATASOURCE")'>'+
	    '			<a id="'+name+'_delBtn" class="ion-ios-close-empty remove-block" style="margin-top:12px;" 	onclick="deleteKafkaDataSourcePopup(\''+kafkaDetailListCounter+'\')"></a>' +
	    '			</sec:authorize>'+
	    '		</div>'+
	    '	</div>	'+
	    '	<div class="box-body inline-form accordion-body collapse in" id="' + kafkaDetailListCounter + '_block">'+
	 	'		<div class="col-md-6 no-padding">'+
	    '			<spring:message code="kafka.datasource.configuration.add.name.label" var="label"></spring:message>'+
	    '			<spring:message code="kafka.datasource.configuration.add.name.tooltip" var="tooltip"></spring:message>'+
	    '  			<div class="form-group">'+
	    '    			<div class="table-cell-label">${label}<span class="required">*</span></div>'+
	    '      			<div class="input-group ">'+
	    '					<input class="form-control table-cell input-sm" id="' + kafkaDetailListCounter + '_name" tabindex="3" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}" value="'+name+'"/>'+
		'					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="" ></i></span>' +
	    '       		</div>'+
	    '  	 		</div>'+
	    '		</div>'+
	    '		<div class="col-md-6 no-padding">'+
	    '			<spring:message code="kafka.datasource.configuration.add.server.label" var="lable"></spring:message>'+
	    '			<spring:message code="kafka.datasource.configuration.add.server.ip.tooltip" var="tooltipIp"></spring:message>'+
	    '			<spring:message code="kafka.datasource.configuration.add.server.port.tooltip" var="tooltipPort"></spring:message>'+
	    '			<div class="input-group">'+
	    '				<div class="table-cell-label">${lable}<span class="required">*</span></div>'+
	    '				<div class="col-md-6 no-padding">'+
        '					<div class="form-group">'+
        '      					<div class="input-group">'+
	    '							<input class="form-control table-cell input-sm" id="' + kafkaDetailListCounter + '_kafkaServerIpAddress" tabindex="3" data-toggle="tooltip" data-placement="bottom"  title="${tooltipIp}" value="'+kafkaServerIpAddress+'"/>'+
		'							<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="" ></i></span>' +
	    '						</div>'+
	    '					</div>'+
	    '				</div>'+
	    '				<div class="col-md-6 no-padding">'+
        '					<div class="form-group">'+
        '      					<div class="input-group">'+
	    '							<input class="form-control table-cell input-sm" id="' + kafkaDetailListCounter + '_kafkaServerPort" onkeydown="isNumericOnKeyDown(event)" tabindex="3" data-toggle="tooltip" data-placement="bottom"  title="${tooltipPort}" value="'+kafkaServerPort+'"/>'+
		'							<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="" ></i></span>' +
	    '						</div>'+
	    '					</div>'+
	    '				</div>'+
	    '			</div>'+
	    '		</div>'+
	    '		<div class="col-md-6 no-padding" >'+
	    '			<spring:message code="kafka.datasource.configuration.add.max.retry.count.label" var="label"></spring:message>'+
	    '			<spring:message code="kafka.datasource.configuration.add.max.retry.count.tooltip" var="tooltip"></spring:message>'+
	    '  			<div class="form-group">'+
	    '    			<div class="table-cell-label">${label}</div>'+
	    '      			<div class="input-group ">'+
	    '					<input class="form-control table-cell input-sm" id="' + kafkaDetailListCounter + '_maxRetryCount" onkeydown="isNumericOnKeyDown(event)" type="text" tabindex="4" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="'+maxRetryCount+'" />'+
		'					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="" ></i></span>'+
	    '        		</div>'+
	    '     		</div>'+
	    '		</div> '+
	    '		<div class="col-md-6 no-padding" >'+
	    '			<spring:message code="kafka.datasource.configuration.add.max.response.wait.label" var="label"></spring:message>'+
	    '			<spring:message code="kafka.datasource.configuration.add.max.response.wait.tooltip" var="tooltip"></spring:message>'+
	    '  			<div class="form-group">'+
	    '    			<div class="table-cell-label">${label}</div>'+
	    '      			<div class="input-group ">'+
	    '					<input class="form-control table-cell input-sm" id="' + kafkaDetailListCounter + '_maxResponseWait" onkeydown="isNumericOnKeyDown(event)" type="text" tabindex="4" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="'+maxResponseWait+'" />'+
		'					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="" ></i></span>'+
	    '        		</div>'+
	    '     		</div>'+
	    '		</div> '+
	    
	    '		<div class="col-md-6 no-padding" >'+
	    '			<spring:message code="kafka.datasource.configuration.add.producer.retry.count.label" var="label"></spring:message>'+
	    '			<spring:message code="kafka.datasource.configuration.add.producer.retry.count.tooltip" var="tooltip"></spring:message>'+
	    '  			<div class="form-group">'+
	    '    			<div class="table-cell-label">${label}</div>'+
	    '      			<div class="input-group ">'+
	    '					<input class="form-control table-cell input-sm" id="' + kafkaDetailListCounter + '_kafkaProducerRetryCount" onkeydown="isNumericOnKeyDown(event)" type="text" tabindex="4" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="'+kafkaProducerRetryCount+'" />'+
		'					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="" ></i></span>'+
	    '        		</div>'+
	    '     		</div>'+
	    '		</div> '+
	    '		<div class="col-md-6 no-padding" >'+
	    '			<spring:message code="kafka.datasource.configuration.add.producer.request.timeout.label" var="label"></spring:message>'+
	    '			<spring:message code="kafka.datasource.configuration.add.producer.request.timeout.tooltip" var="tooltip"></spring:message>'+
	    '  			<div class="form-group">'+
	    '    			<div class="table-cell-label">${label}</div>'+
	    '      			<div class="input-group ">'+
	    '					<input class="form-control table-cell input-sm" id="' + kafkaDetailListCounter + '_kafkaProducerRequestTimeout" onkeydown="isNumericOnKeyDown(event)" type="text" tabindex="4" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="'+kafkaProducerRequestTimeout+'" />'+
		'					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="" ></i></span>'+
	    '        		</div>'+
	    '     		</div>'+
	    '		</div> '+
	    '		<div class="col-md-6 no-padding" >'+
	    '			<spring:message code="kafka.datasource.configuration.add.producer.retry.backoff.label" var="label"></spring:message>'+
	    '			<spring:message code="kafka.datasource.configuration.add.producer.retry.backoff.tooltip" var="tooltip"></spring:message>'+
	    '  			<div class="form-group">'+
	    '    			<div class="table-cell-label">${label}</div>'+
	    '      			<div class="input-group ">'+
	    '					<input class="form-control table-cell input-sm" id="' + kafkaDetailListCounter + '_kafkaProducerRetryBackoff" onkeydown="isNumericOnKeyDown(event)" type="text" tabindex="4" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="'+kafkaProducerRetryBackoff+'" />'+
		'					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="" ></i></span>'+
	    '        		</div>'+
	    '     		</div>'+
	    '		</div> '+
	    '		<div class="col-md-6 no-padding" >'+
	    '			<spring:message code="kafka.datasource.configuration.add.producer.delivery.timeout.label" var="label"></spring:message>'+
	    '			<spring:message code="kafka.datasource.configuration.add.producer.delivery.timeout.tooltip" var="tooltip"></spring:message>'+
	    '  			<div class="form-group">'+
	    '    			<div class="table-cell-label">${label}</div>'+
	    '      			<div class="input-group ">'+
	    '					<input class="form-control table-cell input-sm" id="' + kafkaDetailListCounter + '_kafkaProducerDeliveryTimeout" onkeydown="isNumericOnKeyDown(event)" type="text" tabindex="4" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="'+kafkaProducerDeliveryTimeout+'" />'+
		'					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="" ></i></span>'+
	    '        		</div>'+
	    '     		</div>'+
	    '		</div> '+

	    '		<div class="col-md-12 no-padding">'+
	    '  			<div class="form-group">'+
	    '      				<div id="' + kafkaDetailListCounter + '_buttons-div" class="input-group" >'+
	    '						<sec:authorize access='hasAuthority("EDIT_KAFKA_DATASOURCE")'>'+
	    '		    				<button type="button" class="btn btn-grey btn-xs " id="' + kafkaDetailListCounter + '_updatebtn" data-parent="#pathListConfig" href="#' + kafkaDetailListCounter + '_updatebtn" onclick="updateKafkaDataSourceConfigurationDetails(\''+kafkaDetailListCounter+'\')";><spring:message code="btn.label.update"></spring:message></button>&nbsp;'+
	    '      				    	<button type="button" class="btn btn-grey btn-xs " id="' + kafkaDetailListCounter + '_savebtn"  data-parent="#pathListConfig" href="#' + kafkaDetailListCounter + '_savebtn" onclick="addKafkaDataSourceDetailsInDB(\''+kafkaDetailListCounter+'\');"><spring:message code="btn.label.save"></spring:message></button>&nbsp;' +
	    '							<button id="resetBtn" type="button" class="btn btn-grey btn-xs " onclick="resetKafkaDataSourceDetailsDiv(\''+kafkaDetailListCounter+'\');"><spring:message code="btn.label.reset"></spring:message></button>' +
	    '						</sec:authorize>'+
	    '       			</div>'+
	    '					<div id="' + kafkaDetailListCounter + '_progress-bar-div" class="input-group" style="display: none;"> '+
	    '						<label>Processing Request &nbsp;&nbsp; </label> '+
	    '						<img src="img/processing-bar.gif">'+
	    '					</div>'
	    '   		</div>' +
	    '		</div>'+
	 	'	</div>'+
		'</div>';
	 	'</div>'+
		'</div>';
		
		$('#kafkaDetailList').prepend(block);
		
		if(actionType == 'UPDATE'){
			$('#'+kafkaDetailListCounter+'_savebtn').hide();
		}else{
			resetWarningDisplay();
			clearAllMessages();	
			$("#"+kafkaDetailListCounter+"_updatebtn").hide();
		}
	
	}
	/* Function will display progress bar for add/edit action in pop-up. */
	 function showProgressBar(kafkaDetailListCounter){
		$('#' + kafkaDetailListCounter + '_buttons-div').hide();
		$('#' + kafkaDetailListCounter + '_progress-bar-div').show();
	}
	 /*Function will hide progress bar for add/edit action in pop-up. */
	function hideProgressBar(kafkaDetailListCounter){
		$('#' + kafkaDetailListCounter + '_buttons-div').show();
		$('#' + kafkaDetailListCounter + '_progress-bar-div').hide();
	} 
	
	

</script>
<div class="tab-content no-padding">
   		<div class="fullwidth mtop10">
	   		<div class="title2">
	   		
	   			<spring:message code="kafka.datasource.configuration.main.title"></spring:message> 
	   			<sec:authorize access="hasAuthority('ADD_KAFKA_DATASOURCE')">
	   			<span class="title2rightfield"> 
	   				<span class="title2rightfield-icon1-text">
	   					<a href="#" id="addDBDetailbtn" onclick="addKafkaDataSourceDetails('','','','-1','-1','-1','-1','30000','100','120000','Kafka DataSource Details','ADD');"><i class="fa fa-plus-circle"></i></a>
	   					<a href="#" id="addDBDetail" onclick="addKafkaDataSourceDetails('','','','-1','-1','-1','-1','30000','100','120000','Kafka DataSource Details','ADD');"><spring:message code="kafka.datasource.configuration.add.link.label"></spring:message> </a>
	   				</span> 
	   			</span> 
	   			</sec:authorize>
	   		</div>
	   		<div class="clearfix"></div>
        </div>
         
       <a href="#view_client_details" class="fancybox"
			style="display: none;" id="view_client_details_link">#</a>
		<div id="view_client_details"
			style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title"> 
						<spring:message code="kafka.datasourceManagement.update.popup.header" ></spring:message>
					</h4>
				</div>
				<div class="modal-body padding10 inline-form">
					<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
					<div class="box-body table-responsive no-padding box" id=clientListDiv>
						<table class="table table-hover" id="clientList"></table>
						<div id=clientListPagingDiv></div>
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
						 <spring:message code="kafka.data.source.update.warning.message"></spring:message>
						</span>
					</div>					
				</div>
				<div id="update-kafka-ds-div" class="modal-footer padding10">
			         
			            <button id="updateYesBtn" type="button" class="btn btn-grey btn-xs " onclick="sendUpdateKafkaDataSourceConfiguration();"><spring:message code="btn.label.yes"></spring:message></button>
			            <button id="updateNoBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
			    </div>
			    <div class="modal-footer padding10" id="update-reload-kafka-ds-details" style="display:none;">
			            <button id="closeBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
			    </div>
			</div>
			<!-- /.modal-content -->
		</div>
		
        <div class="fullwidth" id="dataase_details_blocks">       
       		<div id="kafkaDetailList"></div>
        	<c:if test="${kafkaDetailList != null }">
				<script>kafkaDetailListCounter++;</script>
				<c:forEach var="kafkads" items="${kafkaDetailList}">
					<script>
						addKafkaDataSourceDetails(	'${kafkads.id}',
										'${kafkads.name}',
										'${kafkads.kafkaServerIpAddress}',
										'${kafkads.kafkaServerPort}',
										'${kafkads.maxRetryCount}',
										'${kafkads.maxResponseWait}',
										'${kafkads.kafkaProducerRetryCount}',
										'${kafkads.kafkaProducerRequestTimeout}',
										'${kafkads.kafkaProducerRetryBackoff}',
										'${kafkads.kafkaProducerDeliveryTimeout}',
										'${kafkads.name}',
										'UPDATE'
							);
						var kafkads= {'id':'${kafkads.id}',		
								'name':'${kafkads.name}',		
								'kafkaServerIpAddress':'${kafkads.kafkaServerIpAddress}',		
								'kafkaServerPort':'${kafkads.kafkaServerPort}',		
								'maxRetryCount':'${kafkads.maxRetryCount}',		
								'maxResponseWait':'${kafkads.maxResponseWait}',		
								'kafkaProducerRetryCount':'${kafkads.kafkaProducerRetryCount}',		
								'kafkaProducerRequestTimeout':'${kafkads.kafkaProducerRequestTimeout}',
								'kafkaProducerRetryBackoff':'${kafkads.kafkaProducerRetryBackoff}',
								'kafkaProducerDeliveryTimeout':'${kafkads.kafkaProducerDeliveryTimeout}'
								};
						kafkaDetailList.push(kafkads);
						$("#"+kafkaDetailListCounter+"_block").collapse("data-toggle");
		 			</script> 
				</c:forEach>
			</c:if>
    	</div>
   	</div>
   	
	<div id="divKafkaMessage" style=" width:100%; display: none;" >

		    <div class="modal-content">

		        <div class="modal-header padding10" >
		            <h4 class="modal-title"><spring:message code="kafka.datasourceManagement.delete.popup.header"></spring:message></h4>		            
		        </div>
		        
		        <div class="modal-body padding10 inline-form">
		         	<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		    
		    		<div class="box-body table-responsive no-padding box" id="deleteClientListDiv">
						<table class="table table-hover" id="deleteClientList"></table>
						<div id="deleteClientListPagingDiv"></div>
						<div class="clearfix"></div>
						<div id="divLoading" align="center" style="display: none;">
							<img src="img/preloaders/Preloader_10.gif" />
						</div>
					</div>
					
					<div id = "deleteNote">
						<span class="note">
							<spring:message code="serverManagement.popup.imp.note.lbl" ></spring:message>
						</span>
						<br>
						<span>
						 <spring:message code="kafka.data.source.delete.fail.client.mapping.exists"></spring:message>
						</span>
					</div>	
		        </div>
		        
			    <div class="modal-footer padding10" id="reload-kafka-ds-details">
			        <button id="delCloseBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
			    </div>
		      <%--   </sec:authorize> --%>
		    </div>
		    <!-- /.modal-content --> 
		</div>
				
    	<a href="#divKafkaMessage" class="fancybox" style="display: none;" id="kafkaDSMessage">#</a>   
    	
    	<div id="divKafkaDeleteMessage" style=" width:100%; display: none;" >

		    <div class="modal-content">

		        <div class="modal-header padding10" >
		            <h4 class="modal-title"><spring:message code="kafka.datasourceManagement.delete.popup.header"></spring:message></h4>		            
		        </div>
		        <div class="modal-body padding10 inline-form">
		         	<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		         		<p>
			       			<spring:message code="kafka.datasourceconfig.delete.warning.message"></spring:message>
			        	</p>
		        </div>
		        <div id="kafka-delete-ds-div" class="modal-footer padding10">
			         
			    	<button id="delYesBtn" type="button" class="btn btn-grey btn-xs " onclick="deleteKafkaDataSourceConfigurationDetails();"><spring:message code="btn.label.yes"></spring:message></button>
			        <button id="delNoBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
			    </div>
		     </div>
		</div>
    	<a href="#divKafkaDeleteMessage" class="fancybox" style="display: none;" id="kafkaDeleteDSMessage">#</a> 	
<script>

function resetKafkaDataSourceDetailsDiv(divCounter){
	resetWarningDisplay();
	clearAllMessages();
	$('#' + divCounter + '_name').val('');
	$('#' + divCounter + '_kafkaServerIpAddress').val('');
	$('#' + divCounter + '_kafkaServerPort').val('-1');
	$('#' + divCounter + '_maxRetryCount').val('-1');
	$('#' + divCounter + '_maxResponseWait').val('-1');
	$('#' + divCounter + '_kafkaProducerRetryCount').val('-1');
	$('#' + divCounter + '_kafkaProducerRequestTimeout').val('30000');
	$('#' + divCounter + '_kafkaProducerRetryBackoff').val('100');
	$('#' + divCounter + '_kafkaProducerDeliveryTimeout').val('120000');
 }
function reloadDataSourceDetails(){
	closeFancyBox();
}
function cancelConfig(divCounter){
	$('#flipbox_'+kafkaDetailListCounter).remove();
}
function removeDBDetailBlock(elementId){
	$( "#flipbox_"+elementId ).remove();
}
function addKafkaDataSourceDetailsInDB(counter){
	
	resetWarningDisplay();
	clearAllMessages();
	showProgressBar(counter);
	if($('#' + counter + '_kafkaServerPort').val()=="" || $('#' + counter + '_kafkaServerPort').val()==null){
		$('#' + counter + '_kafkaServerPort').val(-1);
	}
	if($('#' + counter + '_maxRetryCount').val()=="" || $('#' + counter + '_maxRetryCount').val()==null){
		$('#' + counter + '_maxRetryCount').val(-1);
	}
	if($('#' + counter + '_maxResponseWait').val()=="" || $('#' + counter + '_maxResponseWait').val()==null){
		$('#' + counter + '_maxResponseWait').val(-1);
	}
	if($('#' + counter + '_kafkaProducerRetryCount').val()=="" || $('#' + counter + '_kafkaProducerRetryCount').val()==null){
		$('#' + counter + '_kafkaProducerRetryCount').val(-1);
	}
	if($('#' + counter + '_kafkaProducerRequestTimeout').val()=="" || $('#' + counter + '_kafkaProducerRequestTimeout').val()==null){
		$('#' + counter + '_kafkaProducerRequestTimeout').val(-1);
	}
	if($('#' + counter + '_kafkaProducerRetryBackoff').val()=="" || $('#' + counter + '_kafkaProducerRetryBackoff').val()==null){
		$('#' + counter + '_kafkaProducerRetryBackoff').val(-1);
	}
	if($('#' + counter + '_kafkaProducerDeliveryTimeout').val()=="" || $('#' + counter + '_kafkaProducerDeliveryTimeout').val()==null){
		$('#' + counter + '_kafkaProducerDeliveryTimeout').val(-1);
	}
	
	$.ajax({
		url:'<%=ControllerConstants.CREATE_KAFKA_DATASOURCE_CONFIGURATION%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"name" 										:	$('#' + counter + '_name').val(),
			"kafkaServerIpAddress"          			:	$('#' + counter + '_kafkaServerIpAddress').val(),
			"kafkaServerPort" 							:	$('#' + counter + '_kafkaServerPort').val(),
			"maxRetryCount" 							:	$('#' + counter + '_maxRetryCount').val(),
			"maxResponseWait" 							:   $('#' + counter + '_maxResponseWait').val(),
			"kafkaProducerRetryCount" 					:   $('#' + counter + '_kafkaProducerRetryCount').val(),
			"kafkaProducerRequestTimeout"				:	$('#' + counter + '_kafkaProducerRequestTimeout').val(),
			"kafkaProducerRetryBackoff"					:	$('#' + counter + '_kafkaProducerRetryBackoff').val(),
			"kafkaProducerDeliveryTimeout"				:	$('#' + counter + '_kafkaProducerDeliveryTimeout').val(),
			"kafkaDetailListCounter"       				:   counter,
		}, 
		
		success: function(data){
			hideProgressBar(counter);
			resetWarningDisplay();
			clearAllMessages();
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			$("#divKafkaDeleteMessage").hide();
			$("#divKafkaMessage").hide();
			
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessagesPopUp();
				showSuccessMsg(responseMsg);
				$('#'+counter+'_savebtn').hide();
				$("#"+counter+"_updatebtn").show();
				$('#'+counter+'_kafkaDataSourceId').val(responseObject["id"]);
				$("#title_"+counter).text(responseObject["name"]);
				var kafkads = {'id':responseObject["id"],
						'name':responseObject["name"],
						'kafkaServerIpAddress':responseObject["kafkaServerIpAddress"],
						'kafkaServerPort':responseObject["kafkaServerPort"],
						'maxRetryCount':responseObject["maxRetryCount"],
						'maxResponseWait':responseObject["maxResponseWait"],
						'kafkaProducerRetryCount':responseObject["kafkaProducerRetryCount"],
						'kafkaProducerRequestTimeout':responseObject["kafkaProducerRequestTimeout"],
						'kafkaProducerRetryBackoff':responseObject["kafkaProducerRetryBackoff"],
						'kafkaProducerDeliveryTimeout':responseObject["kafkaProducerDeliveryTimeout"]};
				kafkaDetailList.push(kafkads); 
				$("#"+kafkaDetailListCounter+"_block").collapse("toggle");
				
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

var updateBlockCounter , updateKafkaDataSourceId, isServerFound;
function updateKafkaDataSourceConfigurationDetails(counter){	
	clearAllMessages();
	$("#clientList").GridUnload();
	$("#clientListPagingDiv").hide();
	clearAllMessagesPopUp();
	updateBlockCounter = counter;
	$("#update-reload-kafka-ds-details").hide();
	updateKafkaDataSourceId= $('#'+counter+'_kafkaDataSourceId').val();
	getClientList();		
}

function getClientList(){
	$("#clientList").jqGrid({
		url: "<%= ControllerConstants.GET_KAFKA_DATASOURCE_ASSOCIATED_CLIENT_LIST %>",
 	  	postData: {
 		 "id":	$('#' +	updateBlockCounter	+'_kafkaDataSourceId').val(),
 		}, 
    datatype: "json",    
    colNames:[
    		  "<spring:message code='kafka.datasourceManagement.delete.popup.client.id' ></spring:message>",
    		  "<spring:message code='kafka.datasourceManagement.delete.popup.kafka.data.source.name' ></spring:message>",
              "<spring:message code='kafka.datasourceManagement.delete.popup.client.name' ></spring:message>",
              "<spring:message code='kafka.datasourceManagement.delete.popup.service.name'></spring:message>",
              "<spring:message code='kafka.datasourceManagement.delete.popup.server.instance.name'></spring:message>",
             ],
	colModel:[
		{name:'id',index:'id',hidden:true},
		{name:'kafkaDataSourceName',index:'kafkaDataSourceName'},
		{name:'name',index:'name',sortable:true},
		{name:'serviceName',index:'serviceName',sortable:true},
		{name:'serverInstanceName',index:'serverInstanceName',sortable:true},
    ],
    rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
    rowList:[1,10,20,60,100],
    height: 'auto',
	sortname: 'id',
	sortorder: "desc",
    pager: "#clientListPagingDiv",
    viewrecords: true,
    timeout : 120000,
    loadtext: "Loading...",    
    caption: "<spring:message code='kafka.datasourceManagement.delete.popup.client.grid.caption'></spring:message>",
	loadComplete: function(data) {				
		gridDataArr = data.rows;		
		$.each(gridDataArr, function (index, client) {
			clientList[client.id]=client;
		});		
		$(".ui-dialog-titlebar").show();							
	},
	onPaging: function (pgButton) {
		clearResponseMsgDiv();
		//clearClientGrid();
	},
	loadError : function(xhr,st,err) {
		handleGenericError(xhr,st,err);
	},
	gridComplete : function() {
		var rowCount = jQuery("#clientList").jqGrid('getGridParam', 'records');
		if(rowCount==0){		
			sendUpdateKafkaDataSourceConfiguration();
		}else{
			$("#clientListDiv").show();
			$("#clientListPagingDiv").show();
			$("#updateNoteDiv").show();			
			$("#update-kafka-ds-div").show();
			$("#update-reload-kafka-ds-details").hide();
			$("#view_client_details_link").click();
		}
	},
	recordtext: "<spring:message code='serverManagement.grid.pager.total.records.text'></spring:message>",
    emptyrecords: "<spring:message code='serverManagement.grid.empty.records'></spring:message>",
	loadtext: "<spring:message code='serverManagement.grid.loading.text'></spring:message>",
	pgtext : "<spring:message code='serverManagement.grid.pager.text'></spring:message>"
});
	
}
function sendUpdateKafkaDataSourceConfiguration(){
	resetWarningDisplay();
	clearAllMessages();
	clearAllMessagesPopUp();
	showProgressBar(updateBlockCounter);
	//take default values if the values is not passed for min and max Pool size
	if($('#' + updateBlockCounter + '_kafkaServerPort').val()=="" || $('#' + updateBlockCounter + '_kafkaServerPort').val()==null){
		$('#' + updateBlockCounter + '_kafkaServerPort').val(-1);
	}
	if($('#' + updateBlockCounter + '_maxRetryCount').val()=="" || $('#' + updateBlockCounter + '_maxRetryCount').val()==null){
		$('#' + updateBlockCounter + '_maxRetryCount').val(-1);
	}
	if($('#' + updateBlockCounter + '_maxResponseWait').val()=="" || $('#' + updateBlockCounter + '_maxResponseWait').val()==null){
		$('#' + updateBlockCounter + '_maxResponseWait').val(-1);
	}
	if($('#' + updateBlockCounter + '_kafkaProducerRetryCount').val()=="" || $('#' + updateBlockCounter + '_kafkaProducerRetryCount').val()==null){
		$('#' + updateBlockCounter + '_kafkaProducerRetryCount').val(-1);
	}
	if($('#' + updateBlockCounter + '_kafkaProducerRequestTimeout').val()=="" || $('#' + updateBlockCounter + '_kafkaProducerRequestTimeout').val()==null){
		$('#' + updateBlockCounter + '_kafkaProducerRequestTimeout').val(-1);
	}
	if($('#' + updateBlockCounter + '_kafkaProducerRetryBackoff').val()=="" || $('#' + updateBlockCounter + '_kafkaProducerRetryBackoff').val()==null){
		$('#' + updateBlockCounter + '_kafkaProducerRetryBackoff').val(-1);
	}
	if($('#' + updateBlockCounter + '_kafkaProducerDeliveryTimeout').val()=="" || $('#' + updateBlockCounter + '_kafkaProducerDeliveryTimeout').val()==null){
		$('#' + updateBlockCounter + '_kafkaProducerDeliveryTimeout').val(-1);
	}
	
	$.ajax({
		url:'<%=ControllerConstants.UPDATE_KAFKA_DATASOURCE_CONFIGURATION%>',
		cache:false,
		async:true,
		dataType:'json',
		type: "POST",
		data:
		 {
			"id"										:	$('#' +	updateBlockCounter	+'_kafkaDataSourceId').val(),
			"name" 										:	$('#' + updateBlockCounter + '_name').val(),
			"kafkaServerIpAddress"          			:	$('#' + updateBlockCounter + '_kafkaServerIpAddress').val(),
			"kafkaServerPort" 							:	$('#' + updateBlockCounter + '_kafkaServerPort').val(),
			"maxRetryCount" 							:	$('#' + updateBlockCounter + '_maxRetryCount').val(),
			"maxResponseWait" 							:   $('#' + updateBlockCounter + '_maxResponseWait').val(),
			"kafkaProducerRetryCount" 					:   $('#' + updateBlockCounter + '_kafkaProducerRetryCount').val(),
			"kafkaProducerRequestTimeout"				:	$('#' + updateBlockCounter + '_kafkaProducerRequestTimeout').val(),
			"kafkaProducerRetryBackoff"					:	$('#' + updateBlockCounter + '_kafkaProducerRetryBackoff').val(),
			"kafkaProducerDeliveryTimeout"				:	$('#' + updateBlockCounter + '_kafkaProducerDeliveryTimeout').val(),
			"kafkaDetailListCounter"       				:   updateBlockCounter,
		}, 
		success: function(data){
			hideProgressBar(updateBlockCounter);
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			$("#update-kafka-ds-div").hide();
			
			if(responseCode == "200"){		
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
				$("#title_"+updateBlockCounter).text(responseObject["name"]);
				$("#clientListDiv").hide();
				$("#updateNoteDiv").hide();
				$("#update-reload-kafka-ds-details").hide();
				$("#view_client_details").hide();
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

function clearClientGrid(){
	var $grid = $("#clientList");
	var rowIds = $grid.jqGrid('getDataIDs');
	// iterate through the rows and delete each of them
	for(var i=0,len=rowIds.length;i<len;i++){
		var currRow = rowIds[i];
		$grid.jqGrid('delRowData', currRow);
	}
}


var blockCounter , deleteKafkaDataSourceId;

function deleteKafkaDataSourcePopup(counter){
	
	blockCounter = counter;
	clearAllMessagesPopUp();
	clearAllMessages();
	$("#deleteClientList").GridUnload();
	$("#deleteClientListPagingDiv").hide();
	deleteKafkaDataSourceId= $('#'+counter+'_kafkaDataSourceId').val();
	getDeleteClientList();
}

function getDeleteClientList(){
	$("#deleteClientList").jqGrid({
		url: "<%= ControllerConstants.GET_KAFKA_DATASOURCE_ASSOCIATED_CLIENT_LIST %>",
 	  	postData: {
 		 "id":	$('#' +	blockCounter	+'_kafkaDataSourceId').val(),
 		}, 
    datatype: "json",    
    colNames:[
    		  "<spring:message code='kafka.datasourceManagement.delete.popup.client.id' ></spring:message>",
    		  "<spring:message code='kafka.datasourceManagement.delete.popup.kafka.data.source.name' ></spring:message>",
              "<spring:message code='kafka.datasourceManagement.delete.popup.client.name' ></spring:message>",
              "<spring:message code='kafka.datasourceManagement.delete.popup.service.name'></spring:message>",
              "<spring:message code='kafka.datasourceManagement.delete.popup.server.instance.name'></spring:message>",
             ],
	colModel:[
		{name:'id',index:'id',hidden:true},
		{name:'kafkaDataSourceName',index:'kafkaDataSourceName'},
		{name:'name',index:'name',sortable:true},
		{name:'serviceName',index:'serviceName',sortable:true},
		{name:'serverInstanceName',index:'serverInstanceName',sortable:true},
    ],
    rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
    rowList:[1,10,20,60,100],
    height: 'auto',
	sortname: 'id',
	sortorder: "desc",
    pager: "#deleteClientListPagingDiv",
    viewrecords: true,
    timeout : 120000,
    loadtext: "Loading...",    
    caption: "<spring:message code='kafka.datasourceManagement.delete.popup.client.grid.caption'></spring:message>",
	loadComplete: function(data) {				
		gridDataArr = data.rows;		
		$.each(gridDataArr, function (index, client) {
			clientList[client.id]=client;
		});		
		$(".ui-dialog-titlebar").show();							
	},
	onPaging: function (pgButton) {
		clearResponseMsgDiv();
		//clearClientGrid();
	},
	loadError : function(xhr,st,err) {
		handleGenericError(xhr,st,err);
	},
	gridComplete : function() {
		var rowCount = jQuery("#deleteClientList").jqGrid('getGridParam', 'records');
		if(rowCount>0){		
			$("#deleteClientListDiv").show();
			$("#deleteClientListPagingDiv").show();
			$("#divKafkaMessage").show();
			$("#divKafkaDeleteMessage").hide();
			$("#kafkaDSMessage").click();
		}else{
			$("#deleteClientListDiv").hide();
			$("#deleteClientListPagingDiv").hide();
			$("#divKafkaMessage").hide();
			$("#divKafkaDeleteMessage").show();
			$("#kafkaDeleteDSMessage").click();	
		}
	},
	recordtext: "<spring:message code='serverManagement.grid.pager.total.records.text'></spring:message>",
    emptyrecords: "<spring:message code='serverManagement.grid.empty.records'></spring:message>",
	loadtext: "<spring:message code='serverManagement.grid.loading.text'></spring:message>",
	pgtext : "<spring:message code='serverManagement.grid.pager.text'></spring:message>"
});
}

function deleteKafkaDataSourceConfigurationDetails(){
		clearAllMessages();
		resetWarningDisplay();
		$.ajax({
			url: '<%=ControllerConstants.DELETE_KAFKA_DATASOURCE_CONFIGURATION%>',
			cache: false,
			async: true,
			dataType: 'json',
			type: "POST",
			data:
			{
				"id"						:	deleteKafkaDataSourceId,
			}, 
			success: function(data){
				clearAllMessages();
				hideProgressBar(kafkaDetailListCounter);
				resetWarningDisplay();
				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				$("#divKafkaMessage").hide();
				if(responseCode == "200"){
					resetWarningDisplay();
					clearAllMessages();					
					showSuccessMsg(responseMsg);
					closeFancyBox();
					$("#"+blockCounter+"_form").remove();
				}else if(responseCode == "400"){
					showErrorMsgPopUp(responseMsg);
					$("#divKafkaDeleteMessage").show();
					$("#kafkaDeleteDSMessage").click();	
				}else{					
					showErrorMsgPopUp(responseMsg);
					$("#divKafkaDeleteMessage").show();
					$("#kafkaDSMessage").click();
				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		}); 
}
</script>
