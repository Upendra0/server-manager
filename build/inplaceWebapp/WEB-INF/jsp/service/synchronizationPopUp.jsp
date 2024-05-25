<%@taglib 	uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="serviceMgmt.synchronize.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
	        		<div id="divSyncMsg">
					</div>
					
					<div id="divSyncAllMsg" >
					</div>
			        <p>
			        	<input type='hidden' id="syncServiceId" value=""/>
			        	<input type='hidden' id="service_status" value="" />
			        	<input type='hidden' id="syncServiceName" value="" />
			        	<input type='hidden' id="syncServiceInstanceId" value="" />
			        	
			        	<table style='width:100%'>
			        		<tr>
			        			<td width="100%">
									<div id="divServiceList">
									</div>
			        			</td>
			        		</tr>
			        	</table>
			        </p>
					<p id="synchronizeSvcPopUpMsg" class="synchronizeSvcPopUpMsgClass">	           	
			           	<span class="note"><spring:message code="label.important.note"></spring:message></span><br/><br/>
			           	<spring:message code="serviceMgmt.synchronize.popup.note.content"></spring:message>
			        </p>
			        <p id="synchronizeSvcPopUpNote" style="display:none">	           	
			           	<span class="note"><spring:message code="label.important.note"></spring:message></span><br/><br/>
			           	<spring:message code="serviceMgmt.synchronize.popup.note.kafka.data.source.content"></spring:message>
			        </p>
		        </div>
		        <div id="synch-popup-buttons-div" class="modal-footer padding10">
		        	<sec:authorize access="hasAuthority('SYNCHRONIZE_SERVICE_INSTANCE')">
		            	<button type="button" class="btn btn-grey btn-xs" id="syncPopupBtn" onclick="syncServiceInstances();"><spring:message code="btn.label.synchronize"></spring:message></button>
		            </sec:authorize>
		            <button type="button" class="btn btn-grey btn-xs " id="cancelSyncBtn" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
		           
		            <button type="button" class="btn btn-grey btn-xs " id="service-synchCloseBtn" data-dismiss="modal" onclick="closeFancyBox();searchInstanceCriteria();" style="display: none;"><spring:message code="btn.label.close"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " id="server-synchCloseBtn" data-dismiss="modal" onclick="closeFancyBox();clearServiceSummaryInstanceGrid();" style="display: none;"><spring:message code="btn.label.close"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " id="summary-synchCloseBtn" data-dismiss="modal" onclick="closeFancyBox();loadServiceSummaryPage();" style="display: none;"><spring:message code="btn.label.close"></spring:message></button>
		            
		            
		        </div>
		        <div id="synch-popup-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>	
		    </div>
		    <!-- /.modal-content -->

<script type="text/javascript">

 function syncServiceInstanceById(id){
	
	clearAllMessages();
	$("#syncPopupBtn").show();
	$("#cancelSyncBtn").show();
	$("#server-synchCloseBtn").hide();
	$("#service-synchCloseBtn").hide();
	$("#summary-synchCloseBtn").hide();
	$("#synchronizeSvcPopUpNote").hide();
   
	var rowId = id;
	var serviceId , serviceName,serviceInstanceId;
  	if(currentTab == 'SERVICE_MANAGEMENT'){
  		var rowData = jQuery("#serviceInstanceList").getRowData(id); 
  	    var ch = jQuery("#serviceInstanceList").find('#'+id+' input[type=checkbox]').prop('checked',true);
		serviceId= jQuery('#serviceInstanceList').jqGrid ('getCell',rowId, 'servInstanceId' );
		serviceInstanceId=jQuery('#serviceInstanceList').jqGrid ('getCell',rowId, 'servInstanceId' );
		parent.ckIntanceSelected[0] = id;
  	  	
  	    serviceName = $(jQuery('#serviceInstanceList').jqGrid ('getCell', id, 'serviceName')).closest("a").html();
  	}

  	if(currentTab == 'COLLECTION_SERVICE_SUMMARY'){
		 serviceId = '${serviceId}';
		 serviceName ='${serviceName}';
		 serviceInstanceId='${serviceInstanceId}';
	}
	
  	// same variable used for Netflow Collection & Netflow Binary Collection & Syslog Collection service
  	if(currentTab == 'NETFLOW_COLLECTION_SERVICE_SUMMARY'){
		 serviceId = '${serviceId}';
		 serviceName ='${serviceName}';
		 serviceInstanceId='${serviceInstanceId}';
	}
  	
  	if(currentTab == 'UPDATE_INSTANCE_SUMMARY'){
  		serviceId = id ;
  		serviceName = $("#syncServiceName").val();
  		serviceInstanceId=$("#syncServiceInstanceId").val();
	}
  	
  	if(currentTab == 'IPLOG_SERVICE_SUMMARY'|| currentTab == 'PARSING_SERVICE_SUMMARY' || currentTab == 'CONSOLIDATION_SERVICE_SUMMARY'){
  		serviceId = '${serviceId}';
		serviceName ='${serviceName}';
		serviceInstanceId='${serviceInstanceId}';
	}
  	
  	if(currentTab == 'PROCESSING_SERVICE_SUMMARY'){
  		serviceId = '${serviceId}';
		serviceName ='${serviceName}';
		serviceInstanceId='${serviceInstanceId}';
  	}
  	
  	if(currentTab == 'DISTRIBUTION_SERVICE_SUMMARY'){
  		serviceId = id ;
  		serviceName ='${serviceName}';
  		serviceInstanceId='${serviceInstanceId}';
	}
  	
  	if(currentTab == 'AGGREGATION_SERVICE_SUMMARY'){
  		serviceId = '${serviceId}';
		serviceName ='${serviceName}';
		serviceInstanceId='${serviceInstanceId}';
  	}
  	
	if(currentTab == 'DIAMETER_COLLECTION_SERVICE_SUMMARY'){
		 serviceId = '${serviceId}';
		 serviceName ='${serviceName}';
		 serviceInstanceId='${serviceInstanceId}';
	}
  	
	$("#divServiceList").html('');
	$('#divSyncAllMsg').html('');
	var tableString ='<table class="table table-hover" style="width:100%">';
	tableString += "<tr>";
	tableString += "<th><spring:message code='serviceManagement.grid.column.service.id'></spring:message></th>";
	tableString += "<th><spring:message code='serviceManager.add.service.name' ></spring:message></th>";
	tableString += "<th class='status'><spring:message code='serviceMgmt.synchronize.popup.status'></spring:message></th>";
	tableString += "</tr>";
	tableString += "<tr>";
	tableString += "<td hidden='true'>"+serviceId+"</td>";
	tableString += "<td>"+serviceInstanceId+"</td>";
	tableString += "<td>"+serviceName+"</td>";
	
	tableString += "<td class='status' id='res_"+rowId+"'></td>";
	tableString += "</tr>";
	tableString += "</table>"
	$("#divServiceList").html(tableString);
	$('.status').hide();
	
	$('#divSyncMsg').html('');
	if(currentTab == 'UPDATE_INSTANCE_SUMMARY'){
		$("#syncServiceInstance").click();
	}else if(currentTab == 'DISTRIBUTION_SERVICE_SUMMARY'){
		$("#synchronize_service_link").click();	
	}else{
		$("#synchronize").click();	
	}
	// set instnce id to synchronize
	$("#syncServiceId").val(serviceId);
}

function syncServiceInstances(){
	
	$("#synch-popup-buttons-div").hide();
	$("#synch-popup-progress-bar-div").show();
	clearAllMessages();
	var serviceInstanceStatus = "" ;
	
	var rowId;
	if(currentTab == 'SERVICE_MANAGEMENT'){
		rowId = $("#syncServiceId").val();
		$.each(parent.ckIntanceSelected, function(key,val){
			serviceInstanceStatus += checkInstanceState(val) + ",";
		});
	}
	
	if(currentTab == 'COLLECTION_SERVICE_SUMMARY'){
		serviceInstanceStatus+= $("#serviceStatus").val();
		parent.ckIntanceSelected[0] = $("#serviceId").val();
	}
	
	if(currentTab == 'NETFLOW_COLLECTION_SERVICE_SUMMARY'){
		serviceInstanceStatus+= $("#serviceStatus").val();
		parent.ckIntanceSelected[0] = $("#serviceId").val();
	}
	
	if(currentTab == 'IPLOG_SERVICE_SUMMARY' || currentTab == 'PARSING_SERVICE_SUMMARY'){
		serviceInstanceStatus+= $("#serviceStatus").val();
		parent.ckIntanceSelected[0] = $("#serviceId").val();
	}
	
	
	if(currentTab == 'PROCESSING_SERVICE_SUMMARY'){
		serviceInstanceStatus+= $("#serviceStatus").val();
		parent.ckIntanceSelected[0] = $("#serviceId").val();
	}
	
	if(currentTab == 'CONSOLIDATION_SERVICE_SUMMARY'){
		serviceInstanceStatus+= $("#serviceStatus").val();
		parent.ckIntanceSelected[0] = $("#serviceId").val();
	}
	
	if(currentTab == 'UPDATE_INSTANCE_SUMMARY'){
		ckIntanceSelected[0] = $("#syncServiceId").val();
		serviceInstanceStatus+= checkServiceInstanceStatus(ckIntanceSelected[0]);
	}
	
	if(currentTab == 'DISTRIBUTION_SERVICE_SUMMARY'){
		parent.ckIntanceSelected[0] = $("#syncServiceId").val();
		serviceInstanceStatus = $("#serviceStatus").val();
	}
	
	if(currentTab == 'AGGREGATION_SERVICE_SUMMARY'){
		parent.ckIntanceSelected[0] = $("#syncServiceId").val();
		serviceInstanceStatus = $("#serviceStatus").val();
	}
	
	if(currentTab == 'DIAMETER_COLLECTION_SERVICE_SUMMARY'){
		serviceInstanceStatus+= $("#serviceStatus").val();
		parent.ckIntanceSelected[0] = $("#serviceId").val();
	}

	$(".status").show();
	$("#syncPopupBtn").show();
	$("#cancelSyncBtn").show();
	$("#synchCloseBtn").hide();
	
	$.ajax({
			url: '<%= ControllerConstants.SYNC_SERVICE_INSTANCE %>',
			    cache: false,
				async: true,
				dataType: 'json',
				type: "POST",
				data: {
					'serviceInstanceId': parent.ckIntanceSelected.join(),
					'serviceInstancesStatus':serviceInstanceStatus
				},
				success: function(data){
					//$("#synch-popup-buttons-div").hide();
					//$("#synch-popup-progress-bar-div").show();
					var response = eval(data);
			    	response.msg = decodeMessage(response.msg);
			    	response.msg = replaceAll("+"," ",response.msg);
	
			    	$('#divSyncMsg').find('.title').html(response.msg);
			    	if(response.code == 200 || response.code == "200") {
			    		
			    		clearResponseMsgDiv();
			    		clearResponseMsg();
			    		clearErrorMsg();
			    		$("#synchronizeSvcPopUpMsg").hide();
			    		parent.ckIntanceSelected = new Array();

			    		var responseCode = data.code;
			    		if(currentTab == 'SERVICE_MANAGEMENT' || currentTab == 'COLLECTION_SERVICE_SUMMARY' || currentTab == 'NETFLOW_COLLECTION_SERVICE_SUMMARY' || currentTab == 'IPLOG_SERVICE_SUMMARY' || currentTab == 'PARSING_SERVICE_SUMMARY'|| currentTab == 'DISTRIBUTION_SERVICE_SUMMARY' || currentTab == 'PROCESSING_SERVICE_SUMMARY' || currentTab == 'CONSOLIDATION_SERVICE_SUMMARY' || currentTab == 'DIAMETER_COLLECTION_SERVICE_SUMMARY'){
			    			$('#divSyncMsg').html('<span class="title" style="color:black;font-weight:bolder">'+response.msg+'</span>');	
			    		}else if(currentTab == 'UPDATE_INSTANCE_SUMMARY'){
			    			$('#divSyncAllMsg').html('<span class="title" style="color:black;font-weight:bolder">'+response.msg+'</span>');	
		    			}
			    		
		    			var response = data.object;
		    			$.each(response, function(key,val){
		    				var resObj = val;
		    				if(resObj.code == "702") {
		    					$('#res_'+key).text(resObj.msg+" Click the link to Configure License Circle ");
		    					$('#res_'+key).append("<form method='get' action='initCircleConfigurationManager'><a href='#' onclick='parentNode.submit()' >Configure</a></form>");
		    				}else {
		    					$('#res_'+key).text(resObj.msg);
		    				}		    			    
		    			});
		    			$(".status").show();
		    			
		    			
		    			$("#synch-popup-progress-bar-div").hide();
		    			$("#synch-popup-buttons-div").show();
		    			$("#syncPopupBtn").hide();
		    			$("#cancelSyncBtn").hide();
		    			$("#synchronizeSvcPopUpNote").show();
		    			if(currentTab == 'SERVICE_MANAGEMENT'){
		    				$("#service-synchCloseBtn").show(); 
		    			}else if(currentTab == 'UPDATE_INSTANCE_SUMMARY'){
		    				$("#server-synchCloseBtn").show();
		    			}else if (currentTab == 'COLLECTION_SERVICE_SUMMARY' ||  currentTab == 'DISTRIBUTION_SERVICE_SUMMARY'){
		    				$("#summary-synchCloseBtn").show();
		    			} else if (currentTab == 'NETFLOW_COLLECTION_SERVICE_SUMMARY'){
		    				$("#summary-synchCloseBtn").show();
		    			} else if (currentTab == 'IPLOG_SERVICE_SUMMARY'|| currentTab == 'PARSING_SERVICE_SUMMARY' || currentTab == 'PROCESSING_SERVICE_SUMMARY' || currentTab == 'CONSOLIDATION_SERVICE_SUMMARY'
		    					|| currentTab == 'AGGREGATION_SERVICE_SUMMARY' || currentTab == 'DIAMETER_COLLECTION_SERVICE_SUMMARY'){
		    				$("#summary-synchCloseBtn").show();
		    			}
			    	}else{
			    		showErrorMsg(response.msg);
			    	}
			    
				},
			    error: function (xhr,st,err){
			    	$("#synch-popup-buttons-div").show();
					$("#synch-popup-progress-bar-div").hide();
			    	handleGenericError(xhr,st,err);
				}
			}); 
}

function loadServiceSummaryPage(){
	$("#synchronizeSvcPopUpMsg").show();
	$("#synchronizeSvcPopUpNote").hide();
	if(currentTab == 'COLLECTION_SERVICE_SUMMARY'){
		$("#collection_service_summary").click();
	}
}
</script>	    
