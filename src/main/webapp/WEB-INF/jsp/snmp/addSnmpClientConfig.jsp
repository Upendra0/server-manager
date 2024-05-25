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

			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<i class="icon-hdd"></i> 
						
						 <span id="c_add_label" style="display: none;"><spring:message
									code="snmpConfiguration.add.clientList.label" ></spring:message></span> 
						<span id="c_update_label" style="display: none;"><spring:message
									code="snmpConfiguration.update.clientList.label" ></spring:message></span>
									
						
					</h4>
				</div>
				<form:form modelAttribute="snmp_config_form_bean" method="POST" action="#" id="create-snmp-server-list">
				<div id="snmpServerListC0ntentDiv" class="modal-body padding10 inline-form">
				
					<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
					
 					<input type="hidden" id="server_Instance_Id" name="server_Instance_Id" value='${serverInstanceId}' /> 
 					
 					<input type="hidden" id="snmpClientId" name="snmpClientId" /> 
 					
 					<input type="hidden" id="SnmpClientType" name="SnmpClientType" value="client" /> 
<!-- 					<input type="hidden" id="servicelabel" name="servicelabel" value="" /> -->
											
					<div class="col-md-6 no-padding">
							<spring:message
								code="snmpConfiguration.add.snmpClientList.name"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpConfiguration.add.snmpClientList.name.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="name"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="client_name" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip}" ></form:input>
										
									<spring:bind path="name">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message
								code="snmpConfiguration.add.snmpClientList.ipaddress"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpConfiguration.add.snmpClientList.ipaddress.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="hostIP"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="client_hostIP" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip}"  ></form:input>
									<spring:bind path="hostIP">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message
								code="snmpConfiguration.add.snmpClientList.port"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpConfiguration.add.snmpClientList.port.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="port"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="client_port" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip}" ></form:input>
									<spring:bind path="port">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message
								code="snmpConfiguration.add.snmpClientList.version"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpConfiguration.add.snmpClientList.version.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}<span class="required">*</span></div>
								<div class="input-group ">
									<form:select path="version" cssClass="form-control table-cell input-sm" tabindex="4" name="client_version" id="client_version" data-toggle="tooltip"
									 onchange="handleV3Params('version',this.options[this.selectedIndex].value)" data-placement="bottom"  title="${tooltip }">
	                             		<c:forEach items="${versionEnum}" var="versionEnum">
	                             			<%-- <c:if test="${versionEnum!='V3'}">
	                             			 	<form:option value="${versionEnum}" >${versionEnum}</form:option>
	                             			</c:if> --%>
                             			 	<form:option value="${versionEnum}" >${versionEnum}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="version">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						
						
						
						
						
						
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="snmpConfiguration.add.snmpClientList.v3AuthAlgo"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpConfiguration.add.snmpClientList.v3AuthAlgo.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}</div>
								<div class="input-group ">
									<form:select path="snmpV3AuthAlgorithm" cssClass="form-control table-cell input-sm" tabindex="5" name="client_snmpV3AuthAlgorithm"
									 id="client_snmpV3AuthAlgorithm" data-toggle="tooltip" data-placement="bottom" onchange="handleV3Params('Algo',this.options[this.selectedIndex].value)" title="${tooltip }">
	                             		<c:forEach items="${authAlgo}" var="authAlgo">
	                             			 	<form:option value="${authAlgo}" >${authAlgo}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="snmpV3AuthAlgorithm">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message
								code="snmpConfiguration.add.snmpClientList.v3AuthPass"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpConfiguration.add.snmpClientList.v3AuthPass.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}</div>
								<div class="input-group ">
									<form:password path="snmpV3AuthPassword"
										cssClass="form-control table-cell input-sm" tabindex="6"
										id="client_snmpV3AuthPassword"  data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip}" ></form:password>
										
									<spring:bind path="snmpV3AuthPassword">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="snmpConfiguration.add.snmpClientList.v3PrivAlgo"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpConfiguration.add.snmpClientList.v3PrivAlgo.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}</div>
								<div class="input-group ">
									<form:select path="snmpV3PrivAlgorithm" cssClass="form-control table-cell input-sm" tabindex="7" name="client_snmpV3PrivAlgorithm" id="client_snmpV3PrivAlgorithm" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
	                             		<c:forEach items="${privAlgo}" var="privAlgo">
	                             			<c:if test="${privAlgo!='AES192' and privAlgo!='_3DES' and privAlgo!='AES256'}">
	                             				<form:option value="${privAlgo}" >${privAlgo}</form:option>	
	                             			</c:if>
										</c:forEach>
									</form:select>
									<spring:bind path="snmpV3PrivAlgorithm">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message
								code="snmpConfiguration.add.snmpClientList.v3PrivPass"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpConfiguration.add.snmpClientList.v3PrivPass.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}</div>
								<div class="input-group ">
									<form:password path="snmpV3PrivPassword"
										cssClass="form-control table-cell input-sm" tabindex="8"
										id="client_snmpV3PrivPassword" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip}" ></form:password>
										
									<spring:bind path="snmpV3PrivPassword">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						
						
						
						
						
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="snmpConfiguration.add.snmpClientList.community"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpConfiguration.add.snmpClientList.community.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}<span class="required">*</span></div>
								<div class="input-group ">
									<%-- <form:select path="community" cssClass="form-control table-cell input-sm" tabindex="9" name="client_community" id="client_community" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
	                             		<c:forEach items="${communityEnum}" var="communityEnum">
	                             			<c:if test="${communityEnum!='Private'}">
	                             			 	<form:option value="${communityEnum}" >${communityEnum}</form:option>
	                             			</c:if>
										</c:forEach>
									</form:select> --%>
									
									<form:input path="community" cssClass="form-control table-cell input-sm" tabindex="9" name="client_community" id="client_community" data-toggle="tooltip" data-placement="top"  title="${tooltip }"></form:input>
									<spring:bind path="community">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message
								code="snmpConfiguration.add.snmpClientList.advance"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpConfiguration.add.snmpClientList.advance.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}<span class="required">*</span></div>
								<div class="input-group ">
									<form:select path="advance" cssClass="form-control table-cell input-sm" tabindex="10" name="client_advance" id="client_advance" data-toggle="tooltip" data-placement="top"  title="${tooltip }">
		                              	
 													<c:forEach items="${advanceEnum}" var="advanceEnum">
 													<form:option value="${advanceEnum.name}">${advanceEnum}</form:option>
												</c:forEach>
 													
									</form:select>
									<spring:bind path="advance">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
								
							</div>
						</div>
				</div>
				</form:form>
				<div id="add-snmp-client-buttons-div" class="modal-footer padding10">
					<button type="button" id="caddNewAttribute" class="btn btn-grey btn-xs " onclick="addNewClientToDB()">
						<spring:message code="btn.label.save" ></spring:message>
					</button>
					
					<sec:authorize access="hasAuthority('EDIT_SNMP_SERVER')">
					<button type="button" id="ceditAttribute"
							class="btn btn-grey btn-xs " style="display: none"
							onclick="updateClientToDB();">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
					</sec:authorize>
					
					<button id="snmp_client_cancel_btn" onclick="javascript:parent.closeFancyBoxFromChildIFrame()" class="btn btn-grey btn-xs ">
						<spring:message code="btn.label.cancel" ></spring:message>
					</button>
					
				</div>
								
				<div id="addSnmpClientAlertListC0ntentDiv" class="modal-body padding10 inline-form">								
					<div class="box-body table-responsive no-padding box">
						<table class="table table-hover" id="snmpClientAlertgrid"></table>
	           			<div id="snmpClientAlertgridPagingDiv"></div> 
	         		</div>		
				</div>				
					
				<div id="add-snmp-client-alert-buttons-div" class="modal-footer padding10">
					<button type="button" id="calert_addNewAttribute" class="btn btn-grey btn-xs " onclick="addAlertsOfClientToDB()">
						<spring:message code="btn.label.save" ></spring:message>
					</button>
			
						<sec:authorize access="hasAuthority('EDIT_SNMP_SERVER')">
					<button type="button" id="calert_editAttribute"
							class="btn btn-grey btn-xs " style="display: none"
							onclick="updateAlertsToDB();">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
					</sec:authorize>
					
					<button id="alert_mapping_cancel_btn" onclick="javascript:parent.closeFancyBoxFromChildIFrame();reloadClientGridData()" class="btn btn-grey btn-xs ">
						<spring:message code="btn.label.cancel" ></spring:message>
					</button>
					
				</div>
				<div id="add-snmp-client-progress-bar-div" class="modal-footer padding10" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
				<div id="add-snmp-client-alert-progress-bar-div" class="modal-footer padding10" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
 				<script type="text/javascript"> 
						$(document).ready(function() {
							/* $("#addServiceAnchor").fancybox({
								   "afterLoad": function(){
										$("#name").val('');
										$("#description").val('');
										$("#create-service-buttons-div").show();
										resetWarningDisplayPopUp();
										clearAllMessagesPopUp();
								   },
								}); */
									console.log("Called");	
									$("#add-snmp-client-alert-buttons-div").hide();
									$("#addSnmpClientAlertListC0ntentDiv").hide();
					     });
						
					
					 
						function displayGridForClientAlert(alertList){
							
							$("#snmpClientAlertgrid").jqGrid({  
							  
							    datatype: "local",
						        colNames:["#",
										  "<spring:message code='snmpClientList.grid.column.alertList.id' ></spring:message>",
										  "<spring:message code='snmpClientList.grid.column.alertList.id' ></spring:message>",
										  "<spring:message code='snmpClientList.grid.column.alertList.name' ></spring:message>",
						                  "<spring:message code='snmpClientList.grid.column.alertList.category' ></spring:message>",
						                  
						                 ],
								colModel:[
									{name : '',index : '',sortable : false,width : '20%',formatter : alertcheckBoxFormatter,search : false},
									{name:'id',index:'id',hidden:true},
									{name:'alertId',index:'alertId',sortable:true},
									{name:'name',index:'name',sortable:true},
						        	{name:'alertType',index:'alertType',sortable:true},
						        	
						        ],
						        
						        rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
						        rowList:[10,20,60,100],
						        height: 'auto',
						        mtype:'POST',
								sortname: 'id',
						 		sortorder: "desc",
						 		search:true,
						 		ignoreCase: true,
						        pager: "#snmpClientAlertgridPagingDiv",
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
						        	
									$(".ui-dialog-titlebar").show();
									$.each(ckSnmpClientAlertSelected,function(index,id){
						 				
										var rowId1= parseInt(id);
						 				
						 			//	var data = $('#' + gridid).jqGrid('getGridParam', 'data');
						 			//	jQuery("#snmpClientAlertgrid").jqGrid('setSelection',rowData,true);
						 			 var rowData = jQuery('#snmpClientAlertgrid').jqGrid ('getRowData', rowId1);
						 				$("#"+rowData.name+"_alert_chkbox").prop('checked', true);
						 			
						 			});
						 			
						 			
								},
							/* beforeSelectRow: function (rowid, e){
									// this blank function will not select the entire row. Only checkbox can be selectable.
									var $grid = $("#snmpClientAlertgrid");
							
									if($("#jqg_snmpClientAlertgrid_" + $grid.jqGrid ('getCell', rowid, 'id')).is(':checked')){
										if(ckSnmpClientAlertSelected.indexOf($grid.jqGrid ('getCell', rowid, 'id')) == -1){
											ckSnmpClientAlertSelected.push($grid.jqGrid ('getCell', rowid, 'id'));
										}
									}else{
																				
										if(ckSnmpClientAlertSelected.indexOf($grid.jqGrid ('getCell', rowid, 'id')) != -1){
 											
											ckSnmpClientAlertSelected.splice(ckSnmpClientAlertSelected.indexOf($grid.jqGrid ('getCell', rowid, 'id')), 1);
										}
									}
								    return false;
								}, */
								
								/* onSelectAll: function(id,status){
								
									var $grid = $("#snmpClientAlertgrid");
									if(status==true){
										
										for(i=0;i<id.length;i++){
											if(ckSnmpClientAlertSelected.indexOf($grid.jqGrid ('getCell', id[i], 'id')) == -1){
												ckSnmpClientAlertSelected.push($grid.jqGrid ('getCell', id[i], 'id'));
											}
							         	}
										
									} else {
										for(i=0;i<id.length;i++){
											
											if(ckSnmpClientAlertSelected.indexOf($grid.jqGrid ('getCell', id[i], 'id')) != -1){
												
	 											
												ckSnmpClientAlertSelected.splice(ckSnmpClientAlertSelected.indexOf($grid.jqGrid ('getCell', id[i], 'id')), 1);
											}
											
							         	}
									} 
									

								}, */
								
								loadError : function(xhr,st,err) {
									handleGenericError(xhr,st,err);
								},
								
								recordtext: "<spring:message code='regex.parser.attr.grid.pager.total.records.text'></spring:message>",
						        emptyrecords: "<spring:message code='regex.parser.attr.grid.empty.records'></spring:message>",
								loadtext: "<spring:message code='regex.parser.attr.grid.loading.text'></spring:message>",
								pgtext : "<spring:message code='regex.parser.attr.grid.pager.text'></spring:message>",
							}).navGrid("#snmpClientAlertgridPagingDiv",{edit:false,add:false,del:false,search:false,refresh:false});
							
							 $('#snmpClientAlertgrid').jqGrid('filterToolbar',{
					                // JSON stringify all data from search, including search toolbar operators
					                stringResult: true,
					                // instuct the grid toolbar to show the search options
					                searchOperators: false,
					                searchOnEnter: false
					            });
							
						//	$(".ui-jqgrid-titlebar").hide();
						}
				
						function alertcheckBoxFormatter(cellvalue, options, rowObject) {
							var snmpAlertName = rowObject["name"].replace(/ /g, "_");
							//alert("id="+snmpAlertName+"_alert_chkbox");
							return "<input type='checkbox' name='"+snmpAlertName+"_alert_chkbox"+"' id='"+snmpAlertName+"_alert_chkbox"+"' onclick=\"addRowIdForAlert(\'"+snmpAlertName+"_alert_chkbox"+"\', \'"+rowObject["id"]+"\')\"; />";
						}

						function addRowIdForAlert(elementId,snmpAlertId)
						{	
							var deviceElement = document.getElementById(elementId);
							if (deviceElement.checked) {
								if((ckSnmpClientAlertSelected.indexOf(snmpAlertId)) == -1){
									ckSnmpClientAlertSelected.push(snmpAlertId);
								}		
							}else{
								if(ckSnmpClientAlertSelected.indexOf(snmpAlertId) !== -1){
									ckSnmpClientAlertSelected.splice(ckSnmpClientAlertSelected.indexOf(snmpAlertId), 1);
								}
							}
						}	
						
						
					function addNewClientToDB(){
						
						console.log("Function Called ");
						resetWarningDisplayPopUp();
						clearAllMessagesPopUp();
						
						
						$("#create-server-buttons-div").hide();
						$("calert_editAttribute").hide();
						
						showProgressBar("add-snmp-client");
						parent.resizeFancyBox();
						$.ajax({
							url: '<%=ControllerConstants.CREATE_SNMP_CLIENT%>',
							cache: false,
							async: true, 
							dataType: 'json',
							type: "POST",
							data: 
							{
								"serverInstance.id"			:   $("#server_Instance_Id").val(),
								"name" 						:	$("#client_name").val(),
								"hostIP" 					:	$("#client_hostIP").val(),
								"port" 						:	$("#client_port").val(),
								"version" 					:	$("#client_version").find(":selected").text(),
								"advance" 					:	$("#client_advance").find(":selected").text(),
								//"community" 				:	$("#client_community").find(":selected").text(),
								"community" 				:	$("#client_community").val(),
								"SnmpClientType"			:   $("#SnmpClientType").val(),
								
								"snmpV3AuthAlgorithm"		:   $("#client_snmpV3AuthAlgorithm").val(),
								"snmpV3AuthPassword"		:   $("#client_snmpV3AuthPassword").val(),
								"snmpV3PrivAlgorithm"		:   $("#client_snmpV3PrivAlgorithm").val(),
								"snmpV3PrivPassword"		:   $("#client_snmpV3PrivPassword").val(),
							},
							success: function(data){
								hideProgressBar("add-snmp-client");
								ckSnmpClientAlertSelected = new Array();
								var response = eval(data);
								
								var responseCode = response.code; 
								var responseMsg = response.msg; 
								var responseObject = response.object;
								 console.log("Response object is ::  " + responseObject);
								if(responseCode == "200"){
									//parent.getSnmpServerList();
									$("#client_name").prop('readonly', true);
									$("#client_hostIP").prop('readonly', true);
									$("#client_port").prop('readonly', true);
									$("#client_version").prop('disabled', true);
									$("#client_advance").prop('disabled', true);
									$("#client_community").prop('disabled', true);
									
									$("#client_snmpV3AuthAlgorithm").prop('disabled', true);
									$("#client_snmpV3AuthPassword").prop('disabled', true);
									$("#client_snmpV3PrivAlgorithm").prop('disabled', true);
									$("#client_snmpV3PrivPassword").prop('disabled', true);
									
									$("#add-snmp-client-buttons-div").hide();
									$("#add-snmp-client-alert-buttons-div").show();
									$("#calert_addNewAttribute").show();
									$("#add-snmp-client-buttons-div").hide();
									$("#addSnmpClientAlertListC0ntentDiv").show();
									
									showSuccessMsgPopUp(responseMsg);
									reloadClientGridData();
									var alerts=eval(responseObject);
									var clientId=alerts['clientId'];
// 									
									$("#snmpClientId").val(clientId);
									var alertList=alerts['alertList'];
									
									$("#snmpClientAlertgrid").jqGrid('clearGridData');
										if(alertList!=null && alertList != 'undefined' && alertList != undefined){
											$("#gs_alertId").val('');
											displayGridForClientAlert(alertList);
											$.each(alertList,function(index,attribute){
								 				
								 				var rowData = {};
								 				rowData.id							= parseInt(attribute.id);
								 				rowData.alertId						= attribute.alertId;
								 				rowData.name						= attribute.name;
								 				rowData.alertType					= attribute.alertType;
								 				
								 				
								 				
								 				jQuery("#snmpClientAlertgrid").jqGrid('addRowData',rowData.id,rowData);
								 				jQuery("#snmpClientAlertgrid").jqGrid('setSelection',rowData.id,true);

								 				
								 			
								 			});
											
											jQuery("#snmpClientAlertgrid").setGridParam({ rowNum: 10 }).trigger("reloadGrid");
										} 
										$('.fancybox-wrap').css('top','10px');
										
									
									//closeFancyBox();
									
									
								}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
									console.log("got 400 response.");
								//	$("#create-server-buttons-div").show();
									
									addErrorIconAndMsgForAjaxPopUp(responseObject);
									
								}else{
									
									resetWarningDisplayPopUp();
									$("#create-server-buttons-div").show();
									showErrorMsgPopUp(responseMsg);
									reloadClientGridData();
								}
							},
						    error: function (xhr,st,err){
						    	hideProgressBar("add-snmp-client");
						    	$("#create-server-buttons-div").show();
								handleGenericError(xhr,st,err);
							}
						});
					}
					
				function updateClientToDB(){
						
						console.log("Function Called ");
						resetWarningDisplayPopUp();
						clearAllMessagesPopUp();
						
						
						$("#add-snmp-client-buttons-div").show();
						$("#calert_editAttribute").show();
						$("#calert_addNewAttribute").hide();
						$("ceditAttribute").show();
						
						showProgressBar("add-snmp-client");
						parent.resizeFancyBox();
						$.ajax({
							url: '<%=ControllerConstants.UPDATE_SNMP_CLIENT%>',
							cache: false,
							async: true, 
							dataType: 'json',
							type: "POST",
							data: 
							{
								"serverInstance.id"			:   $("#server_Instance_Id").val(),
								"id"						:   $("#snmpClientId").val(),
								"name" 						:	$("#client_name").val(),
								"hostIP" 					:	$("#client_hostIP").val(),
								"port" 						:	$("#client_port").val(),
								"version" 					:	$("#client_version").find(":selected").text(),
								"advance" 					:	$("#client_advance").find(":selected").text(),
								//"community" 				:	$("#client_community").find(":selected").text(),
								"community" 				:	$("#client_community").val(),
								"SnmpType"					:   $("#SnmpClientType").val(),
								
								"snmpV3AuthAlgorithm"		:   $("#client_snmpV3AuthAlgorithm").val(),
								"snmpV3AuthPassword"		:   $("#client_snmpV3AuthPassword").val(),
								"snmpV3PrivAlgorithm"		:   $("#client_snmpV3PrivAlgorithm").val(),
								"snmpV3PrivPassword"		:   $("#client_snmpV3PrivPassword").val(),
							},
							success: function(data){
								hideProgressBar("add-snmp-client");
								
								var response = eval(data);
								
								var responseCode = response.code; 
								var responseMsg = response.msg; 
								var responseObject = response.object;
								 console.log("Response object is ::  " + responseObject);
								if(responseCode == "200"){
									
									showSuccessMsgPopUp(responseMsg);
// 									var alerts=eval(responseObject);
// 									var clientId=alerts['clientId'];
// 								
// 									$("#snmpClientId").val(clientId);
// 									var alertList=alerts['alertList'];
// 									reloadClientGridData();
									
// 										if(alertList!=null && alertList != 'undefined' && alertList != undefined){
											
// 											displayGridForClientAlert(alertList);
// 											$.each(alertList,function(index,attribute){
								 				
// 								 				var rowData = {};
// 								 				rowData.id							= parseInt(attribute.id);
// 								 				rowData.alertId						= attribute.alertId;
// 								 				rowData.name						= attribute.name;
// 								 				rowData.alertType					= attribute.alertType;
								 				
								 				
								 				
// 								 				jQuery("#snmpClientAlertgrid").jqGrid('addRowData',rowData.id,rowData);
								 				
								 			
// 								 			});
											
											
// 											jQuery("#snmpClientAlertgrid").setGridParam({ rowNum: 10 }).trigger("reloadGrid");
// 										} 
									
// 									//reloadClientGridData()
// 									//closeFancyBox();
									reloadClientGridData();
									
								}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
									console.log("got 400 response.");
								//	$("#create-server-buttons-div").show();
									
									addErrorIconAndMsgForAjaxPopUp(responseObject);
									
								}else{
									
									resetWarningDisplayPopUp();
									$("#create-server-buttons-div").show();
									showErrorMsgPopUp(responseMsg);
									reloadClientGridData();
								}
							},
						    error: function (xhr,st,err){
						    	hideProgressBar("add-snmp-client");
						    	$("#add-snmp-client-buttons-div").show();
						    	hideProgressBar("add-snmp-client");
								handleGenericError(xhr,st,err);
							}
						});
					}
					
					//Add selected Alerts from grid to db 
					function addAlertsOfClientToDB(){
						
						
						
							clearAllMessagesPopUp();
							resetWarningDisplay();

							showProgressBar("add-snmp-client-alert");
								
								var myGrid = $("#snmpClientAlertgrid");
							    var selectedRowId = myGrid.jqGrid ('getGridParam', 'selarrrow');
							  
							    var rowString = selectedRowId.join(",");
							    var rowIds = new Array();
							    rowIds=rowString.split(",");
							  
							    var selectedAlertId;
							    
							    $.each(rowIds,function (index,rowId){
							    	selectedAlertId= myGrid.jqGrid('getCell', rowId, 'id');
							    	
// // 							    	if(patternId == null || patternId == ''){
// // 							    		$("#"+deleteblockId+"_grid").jqGrid('delRowData',rowId);
// // 							    	}
							    	
							    });
							 
							    if(ckSnmpClientAlertSelected.join() != null ){
							    	
							    	 $.ajax({
							 			url: '<%=ControllerConstants.ADD_ALERTS_TO_CLIENT%>',
							 			cache: false,
							 			async: true,
							 			dataType: 'json',
							 			type: "POST",
							 			data:
							 			{
							 				"selectedAlertIdList" :  ckSnmpClientAlertSelected.join(),
							 				"clientId"			  :  $("#snmpClientId").val(),
							 			}, 
							 			
							 			success: function(data){

							 				var response = eval(data);
							 				
							 				var responseCode = response.code; 
							 				var responseMsg = response.msg; 
							 				hideProgressBar("add-snmp-client-alert");
							 				
							 				if(responseCode == "200"){
							 					resetWarningDisplay();
							 					clearAllMessagesPopUp();
							 					showSuccessMsg(responseMsg);
							 					/* reloadAlertGridData(); */							 					
							 					console.log("selectedRowId " +rowIds);							 					
							 					reloadClientGridData();
							 					getSnmpAlertList();
							 	 				closeFancyBox();							 	 				
							 				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
							 					
							 					resetWarningDisplay();
							 					clearAllMessagesPopUp();
							 					addErrorIconAndMsgForAjaxPopUp(responseObject); 
							 				}else{
							 					
							 					resetWarningDisplay();
							 					clearAllMessagesPopUp();
							 					showErrorMsgPopUp(responseMsg);
							 				}
							 			},
							 		    error: function (xhr,st,err){
							 		    	hideProgressBar("add-snmp-client-alert");
							 				handleGenericError(xhr,st,err);
							 			}
							 		});
							    }else{
							    	closeFancyBox();
							    	hideProgressBar("add-snmp-client-alert");
							   }
		

					}
					
		function updateAlertsToDB(){
						
// 						
						
							clearAllMessagesPopUp();
							resetWarningDisplay();

							showProgressBar("add-snmp-client-alert");
								
								
							    if(ckSnmpClientAlertSelected.join() != null ){
							    	
							    	 $.ajax({
							 			url: '<%=ControllerConstants.UPDATE_ALERTS_TO_CLIENT%>',
							 			cache: false,
							 			async: true,
							 			dataType: 'json',
							 			type: "POST",
							 			data:
							 			{
							 				"selectedAlertIdList" :  ckSnmpClientAlertSelected.join(),
							 				"clientId"			  :  $("#snmpClientId").val(),
							 			}, 
							 			
							 			success: function(data){

							 				var response = eval(data);
							 				
							 				var responseCode = response.code; 
							 				var responseMsg = response.msg; 
							 				var responseObject = response.object;
							 				hideProgressBar("add-snmp-client-alert");
							 				
							 				if(responseCode == "200"){
							 					resetWarningDisplay();
							 					clearAllMessagesPopUp();
							 					showSuccessMsg(responseMsg);
							 					/* reloadAlertGridData(); */
							 					getSnmpAlertList();
							 					
							 					
							 					
							 	 				closeFancyBox();
							 				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
							 					
							 					resetWarningDisplay();
							 					clearAllMessagesPopUp();
							 					addErrorIconAndMsgForAjaxPopUp(responseObject); 
							 				}else{
							 					
							 					resetWarningDisplay();
							 					clearAllMessagesPopUp();
							 					showErrorMsgPopUp(responseMsg);
							 				}
							 			},
							 		    error: function (xhr,st,err){
							 		    	hideProgressBar("add-snmp-client-alert");
							 				handleGenericError(xhr,st,err);
							 			}
							 		});
							    }else{
							    	hideProgressBar("add-snmp-client-alert");
							    	closeFancyBox();
							   }
		

					}
					
					
					function handleV3Params(type,value){
						if(type == 'version'){
							if(value == 'V3'){
								$("#client_snmpV3AuthAlgorithm").prop('disabled', false);
								$("#client_snmpV3AuthPassword").prop('disabled', false);
								$("#client_snmpV3PrivAlgorithm").prop('disabled', false);
								$("#client_snmpV3PrivPassword").prop('disabled', false);
							}else{
								$("#client_snmpV3AuthAlgorithm").prop('disabled', true);
								$("#client_snmpV3AuthPassword").prop('disabled', true);
								$("#client_snmpV3PrivAlgorithm").prop('disabled', true);
								$("#client_snmpV3PrivPassword").prop('disabled', true);
							}
						}else{
							if(value == 'NOAUTH'){
								$("#client_snmpV3AuthAlgorithm").prop('disabled', false);
								$("#client_snmpV3AuthPassword").prop('disabled', true);
								$("#client_snmpV3PrivAlgorithm").prop('disabled', true);
								$("#client_snmpV3PrivPassword").prop('disabled', true);
							}else{
								$("#client_snmpV3AuthAlgorithm").prop('disabled', false);
								$("#client_snmpV3AuthPassword").prop('disabled', false);
								$("#client_snmpV3PrivAlgorithm").prop('disabled', false);
								$("#client_snmpV3PrivPassword").prop('disabled', false);
							}	
						}
						
					}
				</script> 
			</div>
			<!-- /.modal-content -->
			
		
