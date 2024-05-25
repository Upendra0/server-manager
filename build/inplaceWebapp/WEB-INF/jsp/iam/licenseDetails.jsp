<%@page import="com.elitecore.sm.common.constants.LicenseConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"  %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%  String user =(String) session.getAttribute("userName"); %>

<div class="table-responsive no-padding" id="licenseDiv">
	<div class="title2">
			<spring:message code="licenseManagement.grid.heading" ></spring:message>
		</div>
	<form id="apply_license_form" method="POST" action="<%= ControllerConstants.LICENSE_MANAGER %>">
				<input type="hidden" id="componentType" name="componentType"  value="<%=LicenseConstants.LICENSE_ENGINE%>">
				<input type="hidden" id="license_intance_id" name="license_server_instance_id"  value="">
				<input type="hidden" id="license_action" name="licenseAction"  value="">
	</form>
	
	<form id="apply_SM_License_form" method="POST" action="<%= ControllerConstants.LICENSE_MANAGER %>">
				<input type="hidden" id="componentType" name="componentType"  value="<%=LicenseConstants.LICENSE_SM_CONTAINER%>">
				<input type="hidden" id="license_intance_id" name="license_server_instance_id"  value="0">
				<input type="hidden" id="license_action_sm" name="licenseAction"  value="">
				<input type="hidden" id="hostIP" name="hostIP"  value="">
	</form>
	
		<table class="table table-hover" id="licenseDetailsList"></table>
		<div id="licenseDetailsListPagingDiv"></div>
		<div class="clearfix"></div>
		<div id="divLoading" align="center" style="display: none;">
			<img src="img/preloaders/Preloader_10.gif" />
		</div>
</div>

<!-- service details popup -->
		<a href="#view_services_license_details" class="fancybox"
			style="display: none;" id="view_services_license_details_link">#</a>
		<div id="view_services_license_details" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title"> 
						<spring:message code="license.popup.service.tps.heading.label" ></spring:message>
					</h4>
				</div>
				<div class="modal-body padding10 inline-form">

					<div class="box-body table-responsive no-padding box">
						<table class="table table-hover" id="servicesDetailsList"></table>
						<div id="servicesDetailsListPagingDiv"></div>
						<div class="clearfix"></div>
						<div id="divLoading" align="center" style="display: none;">
							<img src="img/preloaders/Preloader_10.gif" />
						</div>
					</div>
				</div>

				<div class="modal-footer padding10">
					<button type="button" class="btn btn-grey btn-xs "
						data-dismiss="modal" onclick="closeFancyBox();clearGridData();">
						<spring:message code="btn.label.close" ></spring:message>
					</button>
				</div>

			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /service details popup -->
		
<!-- Container details Pop Up Start-->
		<a href="#view_container_details" class="fancybox"
			style="display: none;" id="view_container_details_link">#</a>
		<div id="view_container_details" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title"> 
						<spring:message code="license.popup.container.heading.label" ></spring:message>
					</h4>
				</div>
				<div class="modal-body padding10 inline-form">

					<div class="box-body table-responsive no-padding box">
						<table class="table table-hover" id="containerDetailsList"></table>
						<!-- <div id="containerDetailsListPagingDiv"></div> -->
						<div class="clearfix"></div>
						<div id="divLoading" align="center" style="display: none;">
							<img src="img/preloaders/Preloader_10.gif" />
						</div>
					</div>
				</div>

				<div class="modal-footer padding10">
					<button type="button" class="btn btn-grey btn-xs "
						data-dismiss="modal" onclick="closeFancyBox();clearGridData();">
						<spring:message code="btn.label.close" ></spring:message>
					</button>
				</div>

			</div>
			<!-- /.modal-content -->
		</div>
<!--/ Container details Pop Up End-->
		
		
<script type="text/javascript">
		var instanceList = {};
		var oldGrid = '';
		var prevCellVal = { cellId: undefined, value: undefined };
		var utilityCellVal = { cellId: undefined, value: undefined };
		var upgradeCellVal = { cellId: undefined, value: undefined };
		
		function getLicenseDetailList(){
			<sec:authorize access="hasAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')">	
			$('#licenseDiv').show();
			$("#licenseDetailsList").jqGrid({
	        	url: "<%=ControllerConstants.LICENSE_DETAILS%>",
									datatype : "json",
									colNames : [
											"<spring:message code='license.grid.column.id' ></spring:message>",
											"<spring:message code='license.grid.column.hostName' ></spring:message>",
											"<spring:message code='license.grid.column.utility' ></spring:message>",
											"<spring:message code='license.grid.column.server.type' ></spring:message>",
											"<spring:message code='license.grid.column.serverInstanceName' ></spring:message>",
 											"<spring:message code='license.grid.column.startDate' ></spring:message>",  											
											"<spring:message code='license.grid.column.endDate' ></spring:message>", 
 											"<spring:message code='license.grid.column.status' ></spring:message>"
 											/* "<spring:message code='license.grid.column.upgrade' ></spring:message>" */
											],
											
									colModel:[
											{name:'id',index:'id',sortable:false,hidden: true,align:'center'},
							            	{name: 'hostName',index:'hostName',sortable:false,align:'center',formatter:hostNameFormatter,cellattr:hostNameRowSpan,width:'180'},
							            	{name:'serverUtility',index:'serverUtility',sortable:false,align:'center',cellattr:utilityRowSpan},
							            	{name:'serverType',index:'serverType',sortable:false},
							            	{name:'serverInstanceName',index:'serverInstanceName',sortable:false,align:'center',formatter:serverInstanceFormatter},
 											{name:'startDate',index:'startDate',sortable:false,hidden: false,align:'center'}, 		
							            	{name:'endDate',index:'endDate',sortable:false,align:'center'}, 
							            	{name:'status',index:'status',sortable:false,hidden: false,align:'center'}
							            	/* {name :'upgradeLic',index : '',formatter : upgradeFormatter,cellattr:upgradeRowSpan,hidden : false}, */
								            ],
								
							rowNum :<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>,
									rowList : [ 10, 20, 60, 100 ],
									height : 'auto',
									mtype : 'POST',	
									sortname : 'id',
									sortorder : "desc",
									pager : "#licenseDetailsListPagingDiv",
									contentType : "application/json; charset=utf-8",
									viewrecords : true,
									multiselect : false,
									timeout : 120000,
									loadtext : "Loading...",
									caption : "<spring:message code='serviceManagement.grid.caption'></spring:message>",
									beforeRequest : function() {
										$(".ui-dialog-titlebar").hide();
									},
									beforeSend : function(xhr) {
										xhr.setRequestHeader("Accept","application/json");
										xhr.setRequestHeader("Content-Type","application/json");
									},
									loadComplete : function(data) {
										$(".ui-dialog-titlebar").show();
										if ($('#licenseDetailsList').getGridParam('records') === 0) {
											$('#licenseDetailsList tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code="serviceManagement.grid.empty.records"></spring:message></div>");
											$("#licenseDetailsListPagingDiv").hide();
										} else {
											$("#licenseDetailsListPagingDiv").show();
										}
									},
									onPaging : function(pgButton) {
										clearResponseMsgDiv();
									},
									loadError : function(xhr, st, err) {
										handleGenericError(xhr, st, err);
									},
									
									gridComplete: function () {
										<sec:authorize access="!hasAnyAuthority('FULL_LICENSE_MANAGEMENT')">
											jQuery("#licenseDetailsList").jqGrid('hideCol',["upgradeLic"]);
										</sec:authorize>
									        var grid = this;
									        $('td[rowspan="1"]', grid).each(function () {
									            var spans = $('td[rowspanid="' + this.id + '"]', grid).length + 1;
												
									            if (spans > 1) {
									                $(this).attr('rowspan', spans);
									            }
									        });
									        prevCellVal = { cellId: undefined, value: undefined };
											utilityCellVal = { cellId: undefined, value: undefined };
											upgradeCellVal = { cellId: undefined, value: undefined };
									 },
									recordtext : "<spring:message code="serviceManagement.grid.pager.total.records.text"></spring:message>",
									emptyrecords : "<spring:message code="serviceManagement.grid.empty.records"></spring:message>",
									loadtext : "<spring:message code="serviceManagement.grid.loading.text"></spring:message>",
									pgtext : "<spring:message code="serviceManagement.grid.pager.text"></spring:message>",
								}).navGrid("#licenseDetailsListPagingDiv", {
							edit : false,
							add : false,
							del : false,
							search : false
						});

				$(".ui-jqgrid-titlebar").hide();
				</sec:authorize>
			}		
		
		function goToApplyLicense(serverId){
			 $('#license_intance_id').val(serverId);
			 $('#license_action').val('apply');
			 $("#apply_license_form").submit();
		}
		
		function utilityRowSpan(rowId, val, rawObject, cm, rdata) {
			var result;
			val = val + "_" + rawObject["hostName"];
            if (utilityCellVal.value == val) {
                result = ' style="display: none" rowspanid="' + utilityCellVal.cellId + '"';
            }
            else {
                var cellId = this.id + '_' + rowId + '_' + cm.name;
                result = ' rowspan="1" id="' + cellId + '"';
                utilityCellVal = { cellId: cellId, value: val };
            }
            return result;
    	}
		
		function hostNameRowSpan(rowId, val, rawObject, cm, rdata) {
           var result;
            if (prevCellVal.value == val) {
                result = ' style="display: none" rowspanid="' + prevCellVal.cellId + '"';
            }
            else {
                var cellId = this.id + '_' + rowId + '_' + cm.name;
                result = ' rowspan="1" id="' + cellId + '"';
                prevCellVal = { cellId: cellId, value: val };
            }
            return result;
    	}
		
		function upgradeRowSpan(rowId, val, rawObject, cm, rdata) {
	           var result;
	           val = rawObject["hostName"] + "_" + rawObject["serverUtility"];
	            if (upgradeCellVal.value == val) {
	                result = ' style="display: none" rowspanid="' + upgradeCellVal.cellId + '"';
	            }
	            else {
	                var cellId = this.id + '_' + rowId + '_' + cm.name;
	                result = ' rowspan="1" id="' + cellId + '"';
	                upgradeCellVal = { cellId: cellId, value: val };
	            }
	            return result;
	    	}
		
		function upgradeFormatter(cellvalue, options, rowObject){
			return '<a class="link" id="' + rowObject["serverId"] + '" href="#" onclick="goToApplyLicense('+"'" + rowObject["serverId"]+ "'"+')"> Upgrade </a>'
		}
		
		function utilityFormatter(cellvalue, options, rowObject){
			return rowObject["serverUtility"] + ' - ' + rowObject["serverType"];
		}
		
		function hostNameFormatter(cellvalue, options, rowObject){
			return '<a class="link" id="hostName_' + rowObject["hostName"] + '" href="#" onclick="openContainersfromPopUp('+"'" + rowObject["hostName"]+ "'"+')"> '+ rowObject["hostName"] +' </a>'
		}
		
		$(document).ready(function() {
			$('#licenseDiv').hide();
			<%if(!BaseConstants.PROFILE_ADMIN_USERNAME.equalsIgnoreCase(user) && !BaseConstants.MODULE_ADMIN_USERNAME.equalsIgnoreCase(user)){%>
				getLicenseDetailList();
			<%}%>
		});
		
		function serverInstanceFormatter(cellvalue, options, rowObject){
			return '<a class="link" id="serverInstance_' + rowObject["serverInstanceId"] + '" href="#" onclick="getServicesAndTPS('+"'" + rowObject["serverInstanceId"]+ "'"+')"> '+ rowObject["serverInstanceName"] +' </a>'
		}
		
		function getServicesAndTPS(serverInstanceId)
		{
			$("#view_services_license_details_link").click();
			getServicesDetailList(serverInstanceId);
		}
		
		function getServicesDetailList(serverInstanceId) {
			var $grid = $("#servicesDetailsList");
			
			$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", postData:{
				'serverInstanceId': function () {
					return serverInstanceId;
				}
			}}).clearGridData(true).trigger('reloadGrid');

	        $("#servicesDetailsList").jqGrid({
	        	url: "<%=ControllerConstants.SERVICES_TPS_DETAILS%>",
						        	postData: {
						        		serverInstanceId: function () {
						        	        return serverInstanceId;
						   	    		},
						        	},
									datatype : "json",
									colNames : [
											"<spring:message code='license.grid.column.service.name' ></spring:message>",
											"<spring:message code='license.grid.column.license.tps' ></spring:message>",
											"<spring:message code='license.grid.column.service.tps' ></spring:message>"
											],
											
									colModel:[
											{name:'serviceName',index:'serviceName',sortable:false,align:'center'},
							            	{name: 'licenseTps',index:'licenseTps',sortable:false,align:'center'},
							            	{name: 'serviceTps',index:'',sortable:false,align:'center', formatter:instanceStateColumnFormatter}
								            ],
								
							rowNum :<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>,
		
									rowList : [ 10, 20, 60, 100 ],
									height : 'auto',
									mtype : 'POST',
									sortname : 'id',
									sortorder : "desc",
									pager : "#servicesDetailsListPagingDiv",
									contentType : "application/json; charset=utf-8",
									viewrecords : true,
									multiselect : false,
									timeout : 120000,
									loadtext : "Loading...",
									caption : "<spring:message code='serviceManagement.grid.caption'></spring:message>",
									beforeRequest : function() {
										$(".ui-dialog-titlebar").hide();
									},
									beforeSend : function(xhr) {
										xhr.setRequestHeader("Accept",
												"application/json");
										xhr.setRequestHeader("Content-Type",
												"application/json");
									},
									loadComplete : function(data) {
										$(".ui-dialog-titlebar").show();
										if ($('#servicesDetailsList').getGridParam('records') === 0) {
											$('#servicesDetailsList tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code="serviceManagement.grid.empty.records"></spring:message></div>");
											$("#servicesDetailsListPagingDiv").hide();
										} else {
											$("#servicesDetailsListPagingDiv").show();
										}

									},
									onPaging : function(pgButton) {
										clearResponseMsgDiv();
									},
									loadError : function(xhr, st, err) {
										handleGenericError(xhr, st, err);
									},
									
									recordtext : "<spring:message code="serviceManagement.grid.pager.total.records.text"></spring:message>",
									emptyrecords : "<spring:message code="serviceManagement.grid.empty.records"></spring:message>",
									loadtext : "<spring:message code="serviceManagement.grid.loading.text"></spring:message>",
									pgtext : "<spring:message code="serviceManagement.grid.pager.text"></spring:message>",
								}).navGrid("#servicesDetailsListPagingDiv", {
							edit : false,
							add : false,
							del : false,
							search : false
						});
		}
		
		function instanceStateColumnFormatter(cellvalue, options, rowObject){
			getServiceCounterStatus(rowObject);
			return '<div id="loader_'+rowObject["serviceId"]+'"><img src="img/preloaders/circle-red.gif" width="20px" data-toggle="tooltip" data-placement="bottom" title="Loading ..." height="20px" style="z-index:100px;"></div>';
		}
		
		function getServiceCounterStatus(rowObject){
			var serviceId = rowObject["serviceId"];
			$.ajax({
				url: "<%=ControllerConstants.GET_COLLECTION_SERVICE_COUNTER_STATUS%>",
				cache: false,
				async: true,
				dataType: 'json',
				type: "POST",
				data:
				 {
					"serviceId"     : serviceId
				 }, 
				success: function(data){ 
					var response = eval(data);
					var responseCode = response.code; 
					var responseMsg = response.msg; 
					var responseObject = response.object;
					if(responseCode == "200"){
						var tps;
						var couterJson = responseObject["COUNTER_STATUS"];
						var serviceInstanceId = rowObject["serviceInstanceId"];
						$.each(couterJson,function(index,counterStatus){
							var counterObject = counterStatus[serviceInstanceId];
							tps = counterObject["AVG_TPS"]; //CURRENT_TPS
							$('#loader_'+serviceId).html(tps);
				 		});
						
						var licenseTps = rowObject["licenseTps"];
						if (licenseTps !== "-") {
							if (tps > licenseTps) {
								$('#loader_'+serviceId).parents('td').css('background-color','red').css('color','white').css('font-weight','bold');
							}
						}
					}else{
						$('#loader_'+serviceId).html("-");
					}
				},
			    error: function (xhr,st,err){
					handleGenericError(xhr,st,err);
				}
			});
		}
		
		function clearGridData() {
			$("#servicesDetailsList").jqGrid('clearGridData');
			$("#servicesDetailsList").jqGrid("GridUnload");
		}
		
		function openContainersfromPopUp(host) {
			$("#view_container_details_link").click();
			getContainersfromHost(host);
		}
		
		function getContainersfromHost(hostIp) {
			
			var $grid = $("#containerDetailsList");
			
			$grid.jqGrid('setGridParam',{datatype:'json',postData:{
				'hostIp': function () {
					return hostIp;
				}
			}}).clearGridData(true).trigger('reloadGrid');
			
 			$("#containerDetailsList").jqGrid({
       			url: "<%=ControllerConstants.CONTAINER_DETAILS%>",
				        	postData: {
				        		hostIp: function () {
				        	        return hostIp;
				   	    		},
				        	},
							datatype : "json",
							colNames : [
									"<spring:message code='license.grid.container.ip.addr' ></spring:message>",
									"<spring:message code='license.grid.container.licensed.container' ></spring:message>",
									"<spring:message code='license.grid.container.created.container' ></spring:message>",
									"<spring:message code='license.grid.column.upgrade' ></spring:message>"
									],
									
							colModel:[
									{name:'hostIP',index:'hostIP',sortable:false,align:'center'},
									{name:'licenseCount',index:'licenseCount',sortable:false,align:'center'},
									{name:'containerCount',index:'containerCount',sortable:false,align:'center'},
									{name:'upgrade',index:'',sortable:false,align:'center',formatter : smUpgradeFormatter}
						            ],
						
					rowNum :<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>,
							rowList : [ 10, 20, 60, 100 ],
							height : 'auto',
							mtype : 'POST',
							sortname : 'id',
							sortorder : "desc",
							contentType : "application/json; charset=utf-8",
							viewrecords : true,
							multiselect : false,
							timeout : 120000,
							loadtext : "Loading...",
							caption : "<spring:message code="license.popup.container.heading.label"></spring:message>",
							beforeRequest : function() {
								$(".ui-dialog-titlebar").hide();
							},
							beforeSend : function(xhr) {
								xhr.setRequestHeader("Accept",
										"application/json");
								xhr.setRequestHeader("Content-Type",
										"application/json");
							},
							loadComplete : function(data) {
								$(".ui-dialog-titlebar").show();
								if ($('#containerDetailsList').getGridParam('records') === 0) {
									$('#containerDetailsList tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code="serviceManagement.grid.empty.records"></spring:message></div>");
								}

							},
							onPaging : function(pgButton) {
								clearResponseMsgDiv();
							},
							loadError : function(xhr, st, err) {
								handleGenericError(xhr, st, err);
							},
							recordtext : "<spring:message code="serviceManagement.grid.pager.total.records.text"></spring:message>",
							emptyrecords : "<spring:message code="serviceManagement.grid.empty.records"></spring:message>",
							loadtext : "<spring:message code="serviceManagement.grid.loading.text"></spring:message>",
							pgtext : "<spring:message code="serviceManagement.grid.pager.text"></spring:message>",
						}).navGrid("#servicesDetailsListPagingDiv", {
					edit : false,
					add : false,
					del : false,
					search : false
				});	
		}
		
		function smUpgradeFormatter(cellvalue, options, rowObject){
			var licenseCount = rowObject["licenseCount"];
			var containerCount = rowObject["containerCount"]; 
			
			if (containerCount < licenseCount){
				return '-';
			} else if (containerCount > licenseCount) {
				return '<a class="link" id="lnk_' + rowObject["hostIP"] + '" href="#" onclick="goToApplySMLicense('+"'" + rowObject["hostIP"]+ "'"+')" > Container limit exceeds. <br> Please Upgrade License </a>'
			} else {
				return '<span style="color:red">Container limit reached.</span>';
			}
		}
		
		function goToApplySMLicense(hostIP) {
			 $('#hostIP').val(hostIP);
			 $('#license_action_sm').val('apply');
			 $("#apply_SM_License_form").submit();
		}

</script>
