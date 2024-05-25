<%@page import="com.elitecore.sm.common.model.DistributionDriverTypeEnum"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<!-- <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script> -->
<script src="${pageContext.request.contextPath}/js/bootstrap.js" type="text/javascript"></script>

<script>
	var driverListCounter=-1;
	var countTotalDriversExist = 0;
	var driverListDetail = [];
</script>
<script>


	function addDriverDetail(id,driverType,driverAlias,driverName,appOrder,timeout,status){
		driverListCounter++;
		countTotalDriversExist++;
		var block = "<form method='POST' id='" + driverListCounter + "_form'> "+
					"<input type='hidden' id='serviceId' name='serviceId' value='${serviceId}'> "+
					"<input type='hidden' id='"+driverListCounter+"_driverId' name='"+driverListCounter+"_driverId' value='"+id+"'> "+
					"<input type='hidden' id='"+driverListCounter+"_driverAlias' name='"+driverListCounter+"_driverAlias' value='"+driverAlias+"'> "+
					"<input type='hidden' id='" + driverListCounter + "_driverCount' name='" + driverListCounter + "_driverCount' value='"+driverListCounter+"'> "+
					"<div class='box box-warning' id='flipbox_"+driverListCounter+"'>"+
				    "	<div class='box-header with-border'> "+
			        "		<h3 class='box-title' id='title_"+driverListCounter+ "'><a href='#' class='title2rightfield-icon1-text' id='link_"+driverListCounter+ "' style='font-size: 12px;' onclick=redirectToDriverConfig(\'"+driverListCounter+"\');>"+appOrder+"-"+driverName+"</a></h3>"+
			        "		<div class='box-tools pull-right'>"+
			        "			<button class='btn btn-box-tool block-collapse-btn' id='collapse_"+driverListCounter+"' data-toggle='collapse' data-parent='#driverList' href='#" + driverListCounter + "_block'><i class='fa fa-minus'></i></button>	"+
			        "					<sec:authorize access='hasAuthority(\'DELETE_COLLECTION_DRIVER\')'>"+
			        "						&nbsp;&nbsp;<a id='delete_"+driverListCounter+"' class='ion-ios-close-empty remove-block' style='margin-top:12px;' onclick=deleteDriverPopup(\'"+driverListCounter+"\');></a>" +
			        "					</sec:authorize>"+
			        "		</div>"+
			        "	</div>	"+
			        "	<div class='box-body inline-form accordion-body collapse' id='" + driverListCounter + "_block'>"+
			     	"		<div class='col-md-6 no-padding'>"+
			        "			<spring:message code='collectionService.driver.add.driver.name' var='label'></spring:message>"+
			        "			<spring:message code='collectionService.driver.add.driver.name.tooltip' var='tooltip'></spring:message>"+
			        "  			<div class='form-group'>"+
			        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
			        "      			<div class='input-group '>"+
			        "					<input class='form-control table-cell input-sm' id='" + driverListCounter + "_name' tabindex='1' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+driverName+"'/>"+
					"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
			        "       		</div>"+
			        "  	 		</div>"+
			        "		</div>"+
			        "		<div class='col-md-6 no-padding'>"+
			        "			<spring:message code='collectionService.driver.add.driver.type' var='label'></spring:message>"+
			        "			<spring:message code='collectionService.driver.add.driver.type' var='tooltip'></spring:message>"+
			        "			<div class='form-group'>"+
			        "				<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
			        "				<div class='input-group'>"+
			        "					<select id='" + driverListCounter + "_driverType' class='form-control table-cell input-sm'  data-toggle='tooltip' data-placement='bottom'   title='${tooltip }'>"+
					"				        <c:forEach var='driverType' items='${COLLECTION_DRIVER_TYPE_LIST}' >"+
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
			        "      				<input class='form-control table-cell input-sm' id='" + driverListCounter + "_appOrder' readOnly data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+appOrder+"'/>"+
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
			        "					<select class='form-control table-cell input-sm' id='" + driverListCounter + "_status' data-toggle='tooltip' data-placement='bottom' tabindex='4' title='${tooltip }'>"+
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
			        "      			<div id='" + driverListCounter + "_buttons-div' class='input-group '>"+
			        "					<sec:authorize access='hasAuthority(\'UPDATE_COLLECTION_DRIVER\')'>"+
			        "      				<button type='button' id='" + driverListCounter + "_updtbtn'  class='btn btn-grey btn-xs ' onclick=updateDriver(\'"+driverListCounter+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
			        "					</sec:authorize>"+
						"					<button type='button' class='btn btn-grey btn-xs ' onclick=resetDriverDetailDiv(\'"+driverListCounter+"\');><spring:message code='btn.label.reset'></spring:message></button>&nbsp;" +
			        "       		</div>"+
			        "					<div id='" + driverListCounter + "_progress-bar-div' class='input-group' style='display: none;'> "+
			        "						<label>Processing Request &nbsp;&nbsp; </label> "+
			        "							<img src='img/processing-bar.gif'>"+
					"					</div> "+
			        "   		</div>" +
			        "		</div>" +
			     	"	</div>"+
					"</div>";
		$('#driverList').append(block);
		$('#' + driverListCounter + '_driverType').val(driverAlias);
		$('#' + driverListCounter + '_status').val(status);
		$('#'+ driverListCounter + '_status option[value="DELETED"]').remove();
		$("#"+driverListCounter+"_block").collapse("toggle");
		
	}
	
	function addNewDriverDetail(){
		driverListCounter++;
		var block = "<form method='POST' id='" + driverListCounter + "_form'> "+
		"<input type='hidden' id='serviceId' name='serviceId' value='${serviceId}'> "+
		"<input type='hidden' id='"+driverListCounter+"_driverId' name='"+driverListCounter+"_driverId''> "+
		"<input type='hidden' id='"+driverListCounter+"_driverAlias' name='"+driverListCounter+"_driverAlias''> "+
		"<input type='hidden' id='" + driverListCounter + "_driverCount' id='" + driverListCounter + "_driverCount' value='"+driverListCounter+"'> "+
		"<div class='box box-warning' id='flipbox_"+driverListCounter+"'>"+
	    "	<div class='box-header with-border'> "+
        "		<h3 class='box-title' id='title_"+driverListCounter+ "'><a href='#' class='title2rightfield-icon1-text' id='link_"+driverListCounter+ "' style='font-size: 12px;text-decoration:none' onclick=redirectToDriverConfig(\'"+driverListCounter+"\')><spring:message code='collectionService.driver.configuration.tab'></spring:message></a></h3>"+
        "		<div class='box-tools pull-right' id='action_"+driverListCounter+ "'>"+
        "			<button class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' data-parent='#driverList' href='#" + driverListCounter + "_block'><i class='fa fa-minus'></i></button>	"+
        "					<sec:authorize access='hasAuthority(\'DELETE_COLLECTION_DRIVER\')'>"+
        "			&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;' onclick=deleteDriverPopup(\'"+driverListCounter+"\');></a>" +
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
        "					<input class='form-control table-cell input-sm' id='" + driverListCounter + "_name' tabindex='1' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}'/>"+
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
        "				        <c:forEach var='driverType' items='${COLLECTION_DRIVER_TYPE_LIST}' >"+
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
        "      				<input class='form-control table-cell input-sm' value='" + driverListCounter + "' id='" + driverListCounter + "_appOrder' readonly  data-toggle='tooltip' data-placement='bottom'  title='${tooltip}'/>"+
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
        "      			<div id='" + driverListCounter + "_buttons-div' class='input-group '>"+
        "					<sec:authorize access='hasAuthority(\'CREATE_COLLECTION_DRIVER\')'>"+
        "					<button type='button' class='btn btn-grey btn-xs ' id='" + driverListCounter + "_savebtn' onclick=addDriver(\'"+driverListCounter+"\');><spring:message code='btn.label.save'></spring:message></button>&nbsp;" +
        "					</sec:authorize>"+
        "					<sec:authorize access='hasAuthority(\'UPDATE_COLLECTION_DRIVER\')'>"+
        "      				<button type='button' id='" + driverListCounter + "_updtbtn' class='btn btn-grey btn-xs ' style='display: none;' id='" + driverListCounter + "_updatebtn' onclick=updateDriver(\'"+driverListCounter+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
        "					</sec:authorize>"+
        "					<button type='button' class='btn btn-grey btn-xs ' onclick=resetDriverDetailDiv(\'"+driverListCounter+"\');><spring:message code='btn.label.reset'></spring:message></button>&nbsp;" +
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
		$('#'+ driverListCounter + '_status option[value="DELETED"]').remove();
		
	}
</script>



<div class="tab-pane" id="collection-driver-configuration">
	<div class="tab-content no-padding">
   		<div class="fullwidth mtop10">
	   		<div class="title2">
	   			<spring:message code="collectionService.driver.grid.caption"></spring:message>
	   			<span class="title2rightfield">
	   				<sec:authorize access="hasAuthority('CREATE_COLLECTION_DRIVER')">
	   				<span class="title2rightfield-icon1-text">
	   					<a href="#" id="btnShowDriverList_icon" onclick="showDriverListPopup();"><i class="fa fa-bars" aria-hidden="true"></i></a>
	   					<a href="#" id="btnShowDriverList" onclick="showDriverListPopup();"><spring:message code="driver.application.order.label" ></spring:message>
	   					</a>&nbsp;
	   					<a href="#" onclick="addNewDriverDetail();"><i class="fa fa-plus-circle"></i></a>
	          			<a href="#" id="addDriver" onclick="addNewDriverDetail();">
	   					<spring:message code="btn.label.add" ></spring:message></a>
	   				</span> 
	   				</sec:authorize>
	   			</span>
	   		</div>
	   		<div class="clearfix"></div>
        </div>
       		
        <!-- Morris chart - Sales -->
        
        <div class="fullwidth">       
       		<div id="driverList">
           	</div>
        	<c:if test="${driverList != null && fn:length(driverList) gt 0}">
				
				<c:forEach var="driver" items="${driverList}">
					<script>
						addDriverDetail('${driver.id}',
										'${driver.driverType.type}',
										'${driver.driverType.alias}',
										'${driver.name}',
										'${driver.applicationOrder}',
										'${driver.timeout}',
										'${driver.status}'
							);
						
						var driver = {'id':'${driver.id}',
								'type':'${driver.driverType.type}',
								'alias':'${driver.driverType.alias}',
								'name':'${driver.name}',
								'order':'${driver.applicationOrder}',
								'timeout':'${driver.timeout}',
								'status':'${driver.status}'};
						
						driverListDetail.push(driver);
						
		 			</script> 
				</c:forEach>
			</c:if>
    	</div>

    	<a href="#divInstanceList" class="fancybox" style="display: none;" id="instanceList">#</a>
    	<div id="divInstanceList" style="display:none; ">
    		<div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="collection.driver.list.reorder.popup.header"></spring:message></h4>
		    </div>
		    <div class="modal-body padding10 inline-form" id="divInstanceList">
		    	<ul style="list-style: none;padding-left: 0px;">
		    		<li style='cursor:pointer;'>
						<table class='table table-hover' style="width: 100%">
							<tr>
							<th width='30%'> <spring:message code="collectionService.driver.manage.order.name.label"></spring:message></th>
								<th width='35%'><spring:message code="collectionService.driver.manage.order.type.label"></spring:message></th>
								<th width='35%'> <spring:message code="collectionService.driver.manage.order.alias.label"></spring:message></th>
								<%-- <th width='10%'> <spring:message code="collectionService.driver.manage.order.timeout.label"></spring:message></th> --%>
							</tr>
						</table>
					</li>
		    	</ul>
		    	<ul id="ulInstanceList" style="list-style: none;padding-left: 0px;max-height: 400px;overflow: scroll;">
		    	</ul>
		    </div>
		    <div id="reorder-buttons-div" class="modal-footer padding10">
		    	<sec:authorize access="hasAuthority('UPDATE_COLLECTION_DRIVER')">
		    		<button type="button" class="btn btn-grey btn-xs " onclick="resetNumbering();" id=""><spring:message code="btn.label.reorder"></spring:message></button>
		    	</sec:authorize>
		    	<button type="button" class="btn btn-grey btn-xs " onclick="closeFancyBox();" id=""><spring:message code="btn.label.cancel"></spring:message></button>
		 	</div>
		 	<div id="reorder-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
				<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
			</div>	
    	</div> 
    	
    	<!-- Driver Delete popup code start here -->
    	<div id="divMessage" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10" >
		            <h4 class="modal-title" id="warning-title"><spring:message code="collection.driver.delete.header.label"></spring:message></h4>
		            <h4 class="modal-title" id="status-title" style="display:none;"><spring:message code="serverManagement.warn.popup.header"></spring:message></h4>
		        </div>
		        
		        <div class="modal-body padding10 inline-form">
		        	<span id="deleteDriverResponseMsg" style="display:none;">
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
		        
			        <div id="inactive-driver-div" class="modal-footer padding10">
			        	<sec:authorize access="hasAuthority('DELETE_COLLECTION_DRIVER')">
			            	<button id="yes_btn" type="button" class="btn btn-grey btn-xs " onclick="deleteCollectionDriver();"><spring:message code="btn.label.yes"></spring:message></button>
			            </sec:authorize>
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
			        </div>
			         <div class="modal-footer padding10" id="reaload-driver-details" style="display:none;">
			         	<sec:authorize access="hasAuthority('VIEW_COLLECTION_DRIVER')">
			            	<button id="close_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="reloadDriverDetails();"><spring:message code="btn.label.close"></spring:message></button>
			        	</sec:authorize>
			        </div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
    	<a href="#divMessage" class="fancybox" style="display: none;" id="driverMessage">#</a>
    	<!-- Driver Delete popup code end here -->
    	
    	<form action="<%= ControllerConstants.INIT_DRIVER_CONFIGURATION %>" method="POST" id="collection-driver-config-form">
    		<input type="hidden" id="driverId" name="driverId"/>
    		<input type="hidden" id="driverTypeAlias" name="driverTypeAlias"/>
    		<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"/>
    		<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}"/>
    		<input type="hidden" id="serverInstanceId" name="serverInstanceId" value="${instanceId}"/>
    		<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
    	</form>
    	
   	</div>
</div>

<script>

$(function() {
    $( "#ulInstanceList" ).sortable({
      revert: true,
      change:  function (event, ui) {
    	 var id = (ui.item.attr('id'));
      	}
    });
    $( "ul, li" ).disableSelection();
  });

function showDriverListPopup(){
	
	$('#ulInstanceList').html('');
	
	var tabstring ="<li>";
	for(var index=0;index<driverListDetail.length;index++){
	    var tabstring ="<li style='cursor:pointer;border:1px solid #ddd;margin-bottom:5px;' name = " + driverListDetail[index].id 
	    + " id=" + driverListDetail[index].name.replace(new RegExp(" ", 'g'), "_") +"_"+driverListDetail[index].alias+ ">"; 
		tabstring +="<table class='table table-hover' style='margin-bottom:10px;border-top:0px;'>";
		tabstring +="<tr id="+driverListDetail[index].id+">";
		tabstring +="<td width='30%'>"+driverListDetail[index].name+"</td>";
		tabstring +="<td width='35%'>"+driverListDetail[index].type+"</td>";
		tabstring +="<td width='35%'>"+driverListDetail[index].alias+"</td>";
		/* tabstring +="<td width='10%'>"+driverListDetail[index].timeout+"</td>"; */
		tabstring += "</table>";
		tabstring += "<li>";
		$('#ulInstanceList').append(tabstring);
	}
	$('#instanceList').click();
}

function resetNumbering(){
	
	var listItems = $("#ulInstanceList li");
	var counter=0;
	listItems.each(function(idx, li) {
	    var ele = $(li);
	    if($(ele).attr('name')==undefined)
	    	$(ele).remove();
	    else
	    	changeDriverOrder($(ele).attr('name'),counter++);
	});
	
	updateDriverOrder();
}

function changeDriverOrder(driverId,order){
	for(var index=0;index<driverListDetail.length;index++){
		if(driverListDetail[index].id==driverId)
			driverListDetail[index].order=order;
	}
}

function resetDriverDetailDiv(divCounter){
	resetWarningDisplay();
	clearAllMessages();
	$('#' + divCounter + '_name').val('');
}

$(document).ready(function() {
	displayReorderLink();
});

function displayReorderLink(){
	if(countTotalDriversExist >= 2){
		$("#btnShowDriverList").show();
		$("#btnShowDriverList_icon").show();
	}else{
		$("#btnShowDriverList").hide();
		$("#btnShowDriverList_icon").hide();
	}	
}

function redirectToDriverConfig(driverListCounter){
	$('#driverTypeAlias').val($('#' + driverListCounter + '_driverAlias').val());
	$('#driverId').val($('#' + driverListCounter + '_driverId').val());
	$('#collection-driver-config-form').submit();
}

function showProgressBar(driverListCounter){
	$('#' + driverListCounter + '_buttons-div').hide();
	$('#' + driverListCounter + '_progress-bar-div').show();
}

function hideProgressBar(driverListCounter){
	$('#' + driverListCounter + '_buttons-div').show();
	$('#' + driverListCounter + '_progress-bar-div').hide();
}

function addDriver(counter){
	
	//ajax call 
	
	//var str = $("#"+counter+"_form").serialize();
	resetWarningDisplay();
	clearAllMessages();
	showProgressBar(counter);
	
	$.ajax({
		url: '<%=ControllerConstants.CREATE_COLLECTION_DRIVER%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"name" 						:	$('#' + counter + '_name').val(),
			"driverType.alias"          :	$('#' + counter + '_driverType').find(":selected").val(),
			"applicationOrder" 			:	$('#' + counter + '_appOrder').val(),
			"timeout" 					:	0,
			"status" 					:   $('#' + counter + '_status').find(":selected").val(),
			"service.id" 				:   $('#serviceId').val(),
			"driverCount"				:	$('#' + counter + '_driverCount').val(),
			
		}, 
		
		success: function(data){
			hideProgressBar(counter);

			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode == "200"){
				
				
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
				$("#link_"+counter).text(responseObject["applicationOrder"]+"-"+responseObject["name"]);
				$("#link_"+counter).css('textDecoration','underline');
				
				$("#"+counter+"_updtbtn").show();
				$("#"+counter+"_savebtn").hide();
				//$('#' + counter + '_driverType').prop("disabled", true);
				$('#'+counter+'_driverId').val(responseObject["id"]);
				$('#'+counter+'_driverAlias').val($('#' + counter + '_driverType').find(":selected").val());
				$("#"+counter+"_block").collapse("toggle");
				
				var driver = {'id':responseObject["id"],
						'type':$('#' + counter + '_driverType').find(":selected").val(),
						'alias':$('#' + counter + '_driverType').find(":selected").val(),
						'name':responseObject["name"],
						'order':responseObject["applicationOrder"],
						'timeout':$('#' + counter + '_timeout').val(),
						'status':$('#' + counter + '_status').val()};
				
				driverListDetail.push(driver);
				countTotalDriversExist++;
				displayReorderLink();
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				
				hideProgressBar(counter);
				addErrorIconAndMsgForAjax(responseObject); 
					
			}else{
				hideProgressBar(counter);
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(counter);
			handleGenericError(xhr,st,err);
		}
	});
}


function updateDriver(driverListCounter){
	
	resetWarningDisplay();
	
	clearAllMessages();
	showProgressBar(driverListCounter);
	parent.resizeFancyBox();
	
	var urlAction;
	var ftpCollectionDriver  = '<%=EngineConstants.FTP_COLLECTION_DRIVER%>' ;
	var sftpCollectionDriver  = '<%=EngineConstants.SFTP_COLLECTION_DRIVER%>' ;
	var localCollectionDriver  = '<%=EngineConstants.LOCAL_COLLECTION_DRIVER%>' ;
	var collectionDriverType  = $('#' + driverListCounter + '_driverType').find(":selected").val();
	if(collectionDriverType ===  ftpCollectionDriver){
		urlAction  = '<%=ControllerConstants.UPDATE_FTP_COLLECTION_DRIVER%>';
	}else if(collectionDriverType ===  sftpCollectionDriver){
		urlAction  = '<%=ControllerConstants.UPDATE_SFTP_COLLECTION_DRIVER%>';
	}else if(collectionDriverType ===  localCollectionDriver){
		urlAction  = '<%=ControllerConstants.UPDATE_LOCAL_COLLECTION_DRIVER%>';
	}
	$.ajax({
		url: urlAction,
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"id"						:	$('#'+driverListCounter+'_driverId').val(),
			"name" 						:	$('#' + driverListCounter + '_name').val(),
			"driverType.alias"          :	collectionDriverType,
			"applicationOrder" 			:	$('#' + driverListCounter + '_appOrder').val(),
			"timeout" 					:	$('#' + driverListCounter + '_timeout').val(),
			"status" 					:   $('#' + driverListCounter + '_status').find(":selected").val(),
			"service.id" 				:   $('#serviceId').val(),
			"driverCount"				:	$('#' + driverListCounter + '_driverCount').val(),
			
		}, 
		
		success: function(data){
			 hideProgressBar(driverListCounter);

			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
				
				$("#link_"+driverListCounter).text(responseObject["applicationOrder"]+"-"+responseObject["name"]);
				$("#link_"+driverListCounter).css('textDecoration','underline');
				$("#"+driverListCounter+"_block").collapse("toggle");
				$('#'+driverListCounter+'_driverAlias').val($('#' + driverListCounter + '_driverType').find(":selected").val());
				var driver = {'id'     :$('#'+driverListCounter+'_driverId').val(),
							  'type'   :$('#' + driverListCounter + '_driverType').find(":selected").val(),
						      'alias'  :$('#' + driverListCounter + '_driverType').find(":selected").val(),
						      'name'   :$('#' + driverListCounter + '_name').val(),
						      'order'  :$('#' + driverListCounter + '_appOrder').val(),
						      'timeout':$('#' + driverListCounter + '_timeout').val(),
						     'status'  :$('#' + driverListCounter + '_status').find(":selected").val()};
				
				for(var index=0;index<driverListDetail.length;index++){
					if(driverListDetail[index].id==$('#'+driverListCounter+'_driverId').val())
						driverListDetail[index] = driver;
				}
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				hideProgressBar(driverListCounter);
				showErrorMsg(responseMsg);
				addErrorIconAndMsgForAjax(responseObject); 
				
				
			}else{
				hideProgressBar(driverListCounter);
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
				 
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(driverListCounter);
			handleGenericError(xhr,st,err);
		}
	});
}
var blockCounter , deleteDriverId;
function deleteDriverPopup(counter){

	blockCounter = counter;

	deleteDriverId= $('#'+counter+'_driverId').val();
	
	if(deleteDriverId == null || deleteDriverId == 'null' || deleteDriverId == ''){
		$("#"+counter+"_form").remove();
	}else{
		var driverStatus = $("#"+blockCounter+"_status").val();
		if(driverStatus == "INACTIVE"){
			$("#deleteWarningMessage").show();
			$("#inactive-driver-div").show();
			$("#warningMessage").hide();
			$("#active-driver-div").hide();
			$("#driverMessage").click();
		}else{
			$("#deleteWarningMessage").hide();
			$("#warningMessage").show();
			$("#inactive-driver-div").hide();
			$("#active-driver-div").show();
			
			$("#driverMessage").click();	
		}
	}

}

function deleteCollectionDriver(){
	resetWarningDisplay();
	clearAllMessages();
	 $.ajax({
		url: '<%=ControllerConstants.DELETE_COLLECTION_DRIVER%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"driverId"   : deleteDriverId,
			"serviceId"  : '${serviceId}',
		}, 
		
		success: function(data){
			hideProgressBar(driverListCounter);
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			$("#deleteWarningMessage").hide();
			$("#inactive-driver-div").hide();
			$("#warningMessage").hide();
			$("#active-driver-div").hide();
		
			$("#warning-title").hide();
			$("#status-title").show();
			
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				$("#deleteDriverResponseMsg").show();
				showSuccessMsgPopUp(responseMsg);
				$("#reaload-driver-details").show();
				parent.resizeFancyBox();
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				showErrorMsgPopUp(responseMsg);
				parent.resizeFancyBox();
				$("#reaload-driver-details").show();

			}else{
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsgPopUp(responseMsg);
				$("#reaload-driver-details").show();
				parent.resizeFancyBox();
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	}); 
	
}

function reloadDriverDetails(){
	closeFancyBox();
	loadCollectionDetails('<%=ControllerConstants.INIT_COLLECTION_DRIVER_MANAGER%>','POST','COLLECTION_DRIVER_CONFIGURATION');
}


function updateDriverOrder(){
	resetWarningDisplay();
	clearAllMessages();
	//showProgressBar(driverListCounter);
	parent.resizeFancyBox();
	
	var orderArray = [];
	
	for(var index=0;index<driverListDetail.length;index++){
		var driver = {};
		driver.driverId = driverListDetail[index].id;
		driver.order = driverListDetail[index].order;
		orderArray.push(driver);
	}
	
	$('#reorder-buttons-div').hide();
	$('#reorder-progress-bar-div').show();
	
	$.ajax({
		url: '<%=ControllerConstants.UPDATE_FTP_COLLECTION_DRIVER_ORDER%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data: {'driverOrderList':JSON.stringify(orderArray)}, 
		
		success: function(data){
			//hideProgressBar(driverListCounter);
			$('#reorder-buttons-div').show();
			$('#reorder-progress-bar-div').hide();
			
			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
				
				$('#driverList').html('');
				closeFancyBox();
				driverListCounter = -1;
				driverListDetail = [];
				
				var driverList = eval(responseObject);
				for(var index=0;index<driverList.length;index++){
					var driver = driverList[index];
					var driverType = JSON.parse(driver.driverType);
					addDriverDetail(driver.id,
							driverType.type,
							driverType.alias,
							driver.name,
							driver.applicationOrder,
							driver.timeout,
							driver.status
					);
					
					var driver = {'id':driver.id,
							'type':driverType.type,
							'alias':driverType.alias,
							'name':driver.name,
							'order':driver.applicationOrder,
							'timeout':driver.timeout,
							'status':driver.status};
					
					driverListDetail.push(driver);
				}
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				hideProgressBar(driverListCounter);
				showErrorMsg(responseMsg);
				addErrorIconAndMsgForAjax(responseObject);
				closeFancyBox();
			}else{
				hideProgressBar(driverListCounter);
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
				closeFancyBox();
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(driverListCounter);
			handleGenericError(xhr,st,err);
		}
	});
}

$(window).keydown(function(event){
    if(event.keyCode == 13) {
      return false;
    }
  });
</script>



	
                                            
