<%@page import="com.elitecore.sm.common.model.DistributionDriverTypeEnum"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<!-- <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script> -->
<script src="${pageContext.request.contextPath}/js/bootstrap.js" type="text/javascript"></script>

<script>
	var driverListCounter = -1;
	var distributionDriverListDetail  = [];	
	var countTotalDriversExist = 0;
		
	jsSpringMsg.updateFTPDriver = '<%=ControllerConstants.UPDATE_FTP_DISTRIBUTION_DRIVER%>';
	jsSpringMsg.updateSFTPDriver = '<%=ControllerConstants.UPDATE_SFTP_DISTRIBUTION_DRIVER%>';
	jsSpringMsg.updateLocalDriver = '<%=ControllerConstants.UPDATE_LOCAL_DISTRIBUTION_DRIVER%>';
	jsSpringMsg.updateDatabaseDriver = '<%=ControllerConstants.UPDATE_DATABASE_DISTRIBUTION_DRIVER%>';
	jsSpringMsg.ftpDistributionDriver  = '<%=EngineConstants.FTP_DISTRIBUTION_DRIVER%>' ;
	jsSpringMsg.sftpDistributionDriver  = '<%=EngineConstants.SFTP_DISTRIBUTION_DRIVER%>' ;
	jsSpringMsg.localDistributionDriver  = '<%=EngineConstants.LOCAL_DISTRIBUTION_DRIVER%>' ;
	jsSpringMsg.databaseDistributionDriver  = '<%=EngineConstants.DATABASE_DISTRIBUTION_DRIVER%>' ;
</script>

<script type="text/javascript"> 

function addUpdateDriverDetail(id,driverType,driverAlias,driverName,appOrder,status,actionType){
	
	countTotalDriversExist++;
	driverListCounter++;
	var block = "<form method='POST' id='" + driverListCounter + "_form'> "+
				"<input type='hidden' id='serviceId' name='serviceId' value='${serviceId}'> "+
				"<input type='hidden' id='"+driverListCounter+"_driverId' name='"+driverListCounter+"_driverId' value='"+id+"'> "+
				"<input type='hidden' id='"+driverListCounter+"_driverAlias' name='"+driverListCounter+"_driverAlias' value='"+driverAlias+"'> "+
				"<input type='hidden' id='" + driverListCounter + "_driverCount' id='" + driverListCounter + "_driverCount' value='"+driverListCounter+"'> "+
				"<div class='box box-warning' id='flipbox_"+driverListCounter+"'>"+
			    "	<div class='box-header with-border'> "+
			    "		<h3 class='box-title' id='title_"+driverListCounter+ "'><a href='#' class='title2rightfield-icon1-text' id='link_"+driverListCounter+ "' style='font-size: 12px;text-decoration:none' onclick=redirectToDriverConfig(\'"+driverListCounter+"\')><spring:message code='collectionService.driver.configuration.tab'></spring:message></a></h3>"+
			    "		<div class='box-tools pull-right' id='action_"+driverListCounter+ "'>"+
			    "			<button class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' data-parent='#driverList' href='#" + driverListCounter + "_block'><i class='fa fa-minus'></i></button>	"+
			    "					<sec:authorize access='hasAuthority(\'DELETE_DISTRIBUTION_DRIVER\')'>"+
			    "			&nbsp;&nbsp;<div id='delete_"+driverListCounter+ "' class='ion-ios-close-empty remove-block' style='margin-top:12px;' onclick=deleteDistributionDriverPopup(\'"+driverListCounter+"\');></div>" +
			    "					</sec:authorize>"+
			    "		</div>"+
			    "	</div>	"+
			    "	<div class='box-body inline-form accordion-body collapse in' id='" + driverListCounter + "_block'>"+
			 	"		<div class='col-md-6 no-padding'>"+
			    "			<spring:message code='collectionService.driver.add.driver.name' var='label'></spring:message>"+
			    "			<spring:message code='collectionService.driver.add.driver.name.tooltip' var='tooltip'></spring:message>"+
			    "  			<div class='form-group'>"+
			    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
			    "      			<div class='input-group '>"+
			    "					<input class='form-control table-cell input-sm' id='" + driverListCounter + "_name' tabindex='1' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+driverName+"' />"+
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
			    "       		</div>"+
			    "  	 		</div>"+
			    "		</div>"+
			    "		<div class='col-md-6 no-padding'>"+
			    "			<spring:message code='collectionService.driver.add.driver.type' var='label'></spring:message>"+
			    "			<spring:message code='collectionService.driver.add.driver.type.tooltip' var='tooltip'></spring:message>"+
			    "			<div class='form-group'>" +
			   	"			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
				"     		<div class='input-group'>"+
			    "					<select id='" + driverListCounter + "_driverType' class='form-control table-cell input-sm' data-toggle='tooltip' tabindex='2' data-placement='bottom'  title='${tooltip}'>"+
			    "				        <c:forEach var='driverType' items='${DISTRIBUTION_DRIVER_TYPE_LIST}' >"+
				"							<option value='${driverType.alias}'>${driverType.type}</option>"+
				"				      	</c:forEach>"+
			    "					</select>" +
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
			    "				</div>"+
			    "			</div>"+
			    "		</div>"+
			    "		<div class='col-md-6 no-padding'>"+
			    "			<spring:message code='collectionService.driver.add.driver.app.order' var='label'></spring:message>"+
			  	"			<spring:message code='collectionService.driver.add.driver.app.order.tooltip' var='tooltip'></spring:message>"+
			    "  			<div class='form-group'>"+
			    "   			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
			    "      			<div class='input-group '>"+
			    "      				<input class='form-control table-cell input-sm' value='" + driverListCounter + "' id='" + driverListCounter + "_appOrder' readonly tabindex='3'  data-toggle='tooltip' data-placement='bottom'  title='${tooltip}'/>"+
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
			    "       		</div>"+
			    "     		</div>"+
			    "		</div>"+
			    "		<div class='col-md-6 no-padding'>"+
			    "			<spring:message code='driver.enable.status' var='label'></spring:message>"+
			    "			<spring:message code='driver.enable.status.tooltip' var='tooltip'></spring:message>"+
			    "  			<div class='form-group'>"+
			    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
			    "      			<div class='input-group '>"+
			    "					<select class='form-control table-cell input-sm' id='" + driverListCounter + "_status' tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
			    "				    	<c:forEach var='stateEnum' items='${stateEnum}' >"+
				"							<option value='${stateEnum}'>${stateEnum}</option>"+
				"				    	</c:forEach>"+
				"					</select>"+
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
			    "        		</div>"+
			    "     		</div>"+
			    "		</div> "+
			    "		<div class='col-md-12 no-padding'>"+
			    "			<spring:message code='ftp.driver.mgmt.pathlist.max.count.limit' var='tooltip'></spring:message>"+
			    "  			<div class='form-group'>"+
			    "      			<div id='" + driverListCounter + "_buttons-div' class='input-group '>";
			    					
		block+=	"<sec:authorize access='hasAuthority(\'CREATE_DISTRIBUTION_DRIVER\')'>"+	
	    		"	<button type='button' class='btn btn-grey btn-xs ' id='" + driverListCounter + "_savebtn' onclick=addDistributionDriver(\'"+driverListCounter+"\');><spring:message code='btn.label.save'></spring:message></button>&nbsp;" +
	    		"</sec:authorize>";
	
		block+=	"<sec:authorize access='hasAuthority(\'UPDATE_DISTRIBUTION_DRIVER\')'>"+
				"	<button type='button' class='btn btn-grey btn-xs ' id='" + driverListCounter + "_updatebtn' onclick=updateDistributionDriver(\'"+driverListCounter+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
				"</sec:authorize>";	
			    					
			   
	  block+=   "				<button type='button' class='btn btn-grey btn-xs ' onclick=resetDistributionDriverDetailDiv(\'"+driverListCounter+"\');><spring:message code='btn.label.reset'></spring:message></button>&nbsp;" +
			    "       		</div>"+
			    "					<div id='" + driverListCounter + "_progress-bar-div' class='input-group' style='display: none;'> "+
			    "						<label>Processing Request &nbsp;&nbsp; </label> "+
			    "							<img src='img/processing-bar.gif'>"+
				"					</div> "+
			    "   		</div>" +
			    "		</div>" +
			 	"	</div>"+
				"</div>"+
				"</form>";
				
				$('#driverList').append(block);
				
				$('#' + driverListCounter + '_status option[value="DELETED"]').remove();
				if(actionType == 'UPDATE'){
					$('#' + driverListCounter + '_driverId').val(id);
					$('#' + driverListCounter + '_driverType').val(driverAlias);
					$('#' + driverListCounter + '_status').val(status);
					$("#link_"+driverListCounter).text(appOrder+"-"+driverName);
					$("#link_"+driverListCounter).css('textDecoration','underline');
					$("#" + driverListCounter+"_block").collapse("toggle");
					$("#"+driverListCounter+"_savebtn").hide();
					
				}else{
					$("#"+driverListCounter+"_updatebtn").hide();
				}
				
}

</script>


<div class="tab-pane" id="distribution-driver-configuration">
	<div class="tab-content no-padding  mtop10">
   		<div class="fullwidth">
	   		<div class="title">
	   			<strong><spring:message code="distribution.service.distribution.driver.grid.caption"></spring:message></strong>
	   			<span class="title2rightfield"> 
				    <span class="title2rightfield-icon1-text">
	   				<sec:authorize access="hasAuthority('UPDATE_DISTRIBUTION_DRIVER')">
	   					<a href="#" id="btnShowDriverList" onclick="displayApplicationOrderPopup();"><i class="fa fa-bars" aria-hidden="true"></i></a>
	   					<a href="#" id="btnShowDriverList" onclick="displayApplicationOrderPopup();"><spring:message code="driver.application.order.label" ></spring:message>
	   					</a>&nbsp;
	   				</sec:authorize>	
	   				<sec:authorize access="hasAuthority('CREATE_DISTRIBUTION_DRIVER')">	
	   					<a href="#" onclick="addUpdateDriverDetail();"><i class="fa fa-plus-circle"></i></a>
	          			<a href="#" id="addDriver" onclick="addUpdateDriverDetail('','','','','','','ADD');">
	   					<spring:message code="btn.label.add" ></spring:message></a>
	   				</sec:authorize>
	   				</span> 
	   			</span> 
	   		</div>
	   		<div class="clearfix"></div>
        </div>
       		
        
        <div class="fullwidth">       
       		<div id="driverList">
           	</div>
        	<c:if test="${distributionDriverList != null }">
				 <!-- <script>driverListCounter++;</script> --> 
				<c:forEach var="driver" items="${distributionDriverList }">
					<script type="text/javascript">
						addUpdateDriverDetail('${driver.id}',
											  '${driver.driverType.type}',
											  '${driver.driverType.alias}',
											  '${driver.name}',
											  '${driver.applicationOrder}',
											  '${driver.status}',
											  'UPDATE'
											);
						
						var driver = {'id':'${driver.id}',
								'type':'${driver.driverType.type}',
								'alias':'${driver.driverType.alias}',
								'name':'${driver.name}',
								'order':'${driver.applicationOrder}',
								'status':'${driver.status}'};
						
						distributionDriverListDetail.push(driver);
						
		 			</script> 
				</c:forEach>
			</c:if>
    	</div>
    	
    	
   	</div>
   	
   	<!-- Distribution driver change application order pop-up start here -->
   		<a href="#divDistributionDriverList" class="fancybox" style="display: none;" id="application_popup_link">#</a>
    	<div id="divDistributionDriverList" style="display:none; ">
    		<div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="collection.driver.list.reorder.popup.header"></spring:message></h4>
		    </div>
		    <div class="modal-body padding10 inline-form" >
		    	<ul style="list-style: none;padding-left: 0px;">
		    		<li style='cursor:pointer;'>
						<table class='table table-hover' style="width: 100%">
							<tr>
							<th width='30%'> <spring:message code="collectionService.driver.manage.order.name.label"></spring:message></th>
								<th width='30%'><spring:message code="collectionService.driver.manage.order.type.label"></spring:message></th>
								<th width='30%'> <spring:message code="collectionService.driver.manage.order.alias.label"></spring:message></th>
							</tr>
						</table>
					</li>
		    	</ul>
		    	<ul id="draggableDriverDiv" style="list-style: none;padding-left: 0px;max-height: 400px;overflow: scroll;">
		    	</ul>
		    </div>
		    <div id="reorder-buttons-div" class="modal-footer padding10">
		    	<button type="button" class="btn btn-grey btn-xs " onclick="resetNumbering();" id="reorder_driver_btn"><spring:message code="btn.label.reorder"></spring:message></button>
		    	<button type="button" class="btn btn-grey btn-xs " onclick="closeFancyBox();" id="close_reorder_btn"><spring:message code="btn.label.cancel"></spring:message></button>
		 	</div>
		 	<div id="reorder-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
				<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
			</div>	
    	</div> 
   	
   	
   	<!-- Distribution driver change application order pop-up end here -->
   	
   	
   	<!-- Driver Delete popup code start here -->
    	<div id="divMessage" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10" >
		            <h4 class="modal-title" id="warning-title"><spring:message code="collection.driver.delete.header.label"></spring:message></h4>
		            <h4 class="modal-title" id="status-title" style="display:none;"><spring:message code="serverManagement.warn.popup.header"></spring:message></h4>
		        </div>
		        
		        <div class="modal-body padding10 inline-form">
		        	<span id="deleteDriverResponseMsg">
		        		<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
		        	</span>
			        <p id="warningMessage">
			       		 <spring:message code="driver.enable.delete.warning.message"></spring:message>
			        </p>
			        <p id="deleteWarningMessage">
			       		 <spring:message code="driver.delete.warning.message"></spring:message>
			        </p>
		        </div>
		        
		        <div class="modal-footer padding10" id="active-driver-div">
		            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		        
		        <sec:authorize access="hasAuthority('DELETE_DISTRIBUTION_DRIVER')">
			        <div id="inactive-driver-div" class="modal-footer padding10" style="display:block;">
			            <button id="delete_driver_yes_btn" type="button" class="btn btn-grey btn-xs " onclick="deleteDistributionDriver();"><spring:message code="btn.label.yes"></spring:message></button>
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
			        </div>
			         <div class="modal-footer padding10" id="reaload-driver-details" style="display:none;">
			            <button id="close_delete_driver_success" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="reloadDriverDetails();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
		        </sec:authorize>
		    </div>
		    <!-- /.modal-content --> 
		</div>
    	<a href="#divMessage" class="fancybox" style="display: none;" id="driverMessage">#</a>
    	<!-- Driver Delete popup code end here -->
	</div>
<script>
$(document).ready(function() {
	displayReorderLink();
});
/*Function will  make driver list to the dragable driver list.*/
$(function() {
    $( "#draggableDriverDiv" ).sortable({
      revert: true,
      change:  function (event, ui) {
     	 var id = (ui.item.attr('id'));
       	}
    });
    $( "ul, li" ).disableSelection();
  });


function cancelConfig(divCounter){
	$('#flipbox_'+driverListCounter).remove();
}

function displayReorderLink(){
	if(countTotalDriversExist >= 2){
		$("#btnShowDriverList").show();
		$("#btnShowDriverList_icon").show();
	}else{
		$("#btnShowDriverList").hide();
		$("#btnShowDriverList_icon").hide();
	}	
}

</script>
