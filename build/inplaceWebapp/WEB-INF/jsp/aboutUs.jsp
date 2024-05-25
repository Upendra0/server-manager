<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>

<html>

<jsp:include page="common/newheader.jsp"></jsp:include>

<body class="skin-blue sidebar-mini">
	<div class="wrapper">

		<!-- Header Start -->

		<jsp:include page="common/newtopNavigationPanel.jsp"></jsp:include>

		<!-- Header End -->
		<jsp:include page="common/newleftMenu.jsp"></jsp:include>

		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper">
			<!-- Content Header (Page header) -->
			<section class="content-header">
				<div class="fullwidth">
					<div style="padding: 0 10px 0 4px;">
						<div id="content-scroll-d" class="content-scroll">

							<!-- Content Wrapper. Contains page content Start -->

							<div class="fullwidth">
								<h4 style="margin-top: 10px; margin-bottom: 0px; font-size: 11px;">
									<a href="home.html"><img src="img/tile-icon.png" class="vmiddle" alt="Home" /></a> 
									<span class="spanBreadCrumb" style="line-height: 30px;"> 
										<strong> <spring:message code="about.us.bread.scrum.header" ></spring:message></strong>
									</span>
								</h4>
								<br />
								<jsp:include page="common/responseMsg.jsp" ></jsp:include>
								
								<div class="row">
									<div class="col-xs-9">
										<div class="box  martop"
											style="border: none; box-shadow: none;">
											<!-- /.box-header -->
											<div class="box-border borderwidth">
												<div class="clearfix"></div>
												<div class="col-md-12" style="font-weight: 400;">
													<h5>
														<strong><spring:message
																code="about.us.server.manager.detail.header" ></spring:message></strong>
													</h5>
												</div>
												<div class="inline-form mtop10"
													style="padding: 15px 15px 15px 15px;">
													<div class="clearfix"></div>
													<hr width="100%" style="margin: 0 0 5px 0;" />
													<div class="col-md-12">
														<div class="form-group">
															<div class="table-cell-label" style="width: 25%;">
																<spring:message code="about.us.product.nm" ></spring:message>
															</div>
															<div class="input-group">
																: &nbsp;&nbsp;&nbsp;<label id="lblProductName" style="font-weight: normal;">
																	${versionData.productType} </label>
															</div>
														</div>
													</div>
													<div class="clearfix"></div>
													<div class="col-md-12">
														<div class="form-group">
															<div class="table-cell-label" style="width: 25%;">
																<spring:message code="about.us.installed.version" ></spring:message>
															</div>
															<div class="input-group">
																: &nbsp;&nbsp;&nbsp;<label id="lblProductVersion" style="font-weight: normal;">${versionData.versionNumber}</label>
															</div>
														</div>
													</div>
													<%-- <div class="clearfix"></div>
													<div class="col-md-12">
														<div class="form-group">
															<div class="table-cell-label" style="width: 25%;">
																<spring:message code="about.us.install.dt" ></spring:message>
															</div>
															<div class="input-group">
																: &nbsp;&nbsp;&nbsp;<label id="lblInstallDt"
																	style="font-weight: normal;">${versionData.installationDate}</label>
															</div>
														</div>
													</div>
													<div class="clearfix"></div>
													<div class="col-md-12">
														<div class="form-group">
															<div class="table-cell-label" style="width: 25%;">
																<spring:message code="about.us.applied.hotfix" ></spring:message>
															</div>
															<div class="input-group">
																: &nbsp;&nbsp;&nbsp;<label id="lblAppHotfix"
																	style="font-weight: normal;">${versionData.hotfixNumber}</label>
															</div>
														</div>
													</div> --%>

													<div class="col-md-12">
														<div class="form-group">
															<div class="table-cell-label" style="width: 25%;">
																<spring:message code="about.us.license.type" ></spring:message>
															</div>
															<div class="input-group">
																: &nbsp;&nbsp;&nbsp;<label id="lblLicenseType"
																	style="font-weight: normal;">${versionData.licenceType}</label>
															</div>
														</div>
													</div>
													<div class="clearfix"></div>
													<div class="col-md-12">
														<div class="form-group">
															<div class="table-cell-label" style="width: 25%;">
																<spring:message code="about.us.licence.start.dt" ></spring:message>
															</div>
															<div class="input-group">
																: &nbsp;&nbsp;&nbsp;<label id="lblLicenseStartDt"
																	style="font-weight: normal;">${versionData.startDate}</label>
															</div>
														</div>
													</div>
													<div class="clearfix"></div>
													<div class="col-md-12">
														<div class="form-group">
															<div class="table-cell-label" style="width: 25%;">
																<spring:message code="about.us.license.end.dt" ></spring:message>
															</div>
															<div class="input-group">
																: &nbsp;&nbsp;&nbsp;<label id="lblLicenseEndDt"
																	style="font-weight: normal;">${versionData.endDate}</label>
															</div>
														</div>
													</div>
													<div class="clearfix"></div>
													<%-- <div class="col-md-12">
														<div class="form-group">
															<div class="table-cell-label" style="width: 25%;">
																<spring:message code="about.us.crestel.p.eng.license" ></spring:message>
															</div>
															<div class="input-group">
																: &nbsp;&nbsp;&nbsp;<label id="lblEngineLicense">
																
 																<a style="cursor: pointer" class="link" onClick="licenseDetailsPopUp();"> View License Details</a> 
																
																</label>

															</div>
														</div>
													</div> --%>
													<div class="clearfix"></div>
													<br /> <br />
													<div class="col-md-9">
														<spring:message code="about.us.note" ></spring:message>
													</div>
													<div class="col-md-3"
														style="margin-top: -50px; float: right;">
														<img src="img/sterlite_logo.png" align="right" width="80px"
															height="25px" />
													</div>
													<br />
												</div>
											</div>

											<!-- /.box-body -->
										</div>
										<!-- /.box -->

									</div>
								</div>
							</div>

							<!-- Content Wrapper. Contains page content End -->
						</div>
					</div>
				</div>
			</section>
		</div>
		<!-- /.content-wrapper -->

<%  if(System.getenv("KUBERNETES_ENV") !=null){ %>
		<!-- license details popup -->
		<a href="#view_server_license_details" class="fancybox"
			style="display: none;" id="view_server_license_details_link">#</a>
		<div id="view_server_license_details"
			style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title"> 
						<spring:message code="license.popup.heading.label" ></spring:message>
					</h4>
				</div>
				<div class="modal-body padding10 inline-form">

					<div class="box-body table-responsive no-padding box">
						<table class="table table-hover" id="licenseDetailsList"></table>
						<div id="licenseDetailsListPagingDiv"></div>
						<div class="clearfix"></div>
						<div id="divLoading" align="center" style="display: none;">
							<img src="img/preloaders/Preloader_10.gif" />
						</div>
					</div>

				</div>

				<div class="modal-footer padding10">
					<button type="button" class="btn btn-grey btn-xs "
						data-dismiss="modal" onclick="closeFancyBox();">
						<spring:message code="btn.label.close" ></spring:message>
					</button>
				</div>

			</div>
			<!-- /.modal-content -->
		</div>

		<!-- /license details popup -->
<% } %>
		<script type="text/javascript">
		
		function licenseDetailsPopUp() {
			$("#view_server_license_details_link").click();
			getLicenseDetailList();  
		}
		
		function loadGridData(){
			clearAllMessages();
			clearResponseMsgDiv();
    		clearResponseMsg();
    		clearErrorMsg();
    		clearSelection();
    		
			var $grid = $("#licenseDetailsList");
			$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc"}).trigger('reloadGrid');
			
		}
		
		function getLicenseDetailList(){
			$("#licenseDetailsList").jqGrid({
	        	url: "<%=ControllerConstants.LICENSE_DETAILS%>",
									datatype : "json",
									
									colNames : [
											"<spring:message code='license.grid.column.id' ></spring:message>",
											"<spring:message code='license.grid.column.hostName' ></spring:message>",
											"<spring:message code='license.grid.column.serverInstanceName' ></spring:message>",
	   										"<spring:message code='license.grid.column.licenceType'></spring:message>", 
 											"<spring:message code='license.grid.column.startDate' ></spring:message>",  											
											"<spring:message code='license.grid.column.endDate' ></spring:message>", 
 											"<spring:message code='license.grid.column.status' ></spring:message>",    
											],
											
									colModel:[
											{name:'id',index:'id',sortable:false,hidden: true,align:'center'},
							            	{name:'hostName',index:'hostName',sortable:false,align:'center'},
							            	{name:'serverInstanceName',index:'serverInstanceName',sortable:false,align:'center'},
	   										{name:'licenceType',index:'licenceType',hidden: false,align:'center',sortable:false}, 
 											{name:'startDate',index:'startDate',sortable:false,hidden: false,align:'center'}, 		
							            	{name:'endDate',index:'endDate',sortable:false,align:'center'}, 
							            	{name:'status',index:'status',sortable:false,hidden: false,align:'center'},  
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
										xhr.setRequestHeader("Accept",
												"application/json");
										xhr.setRequestHeader("Content-Type",
												"application/json");
									},
									loadComplete : function(data) {
										$(".ui-dialog-titlebar").show();
										if ($('#licenseDetailsList')
												.getGridParam('records') === 0) {
											$('#licenseDetailsList tbody')
												.html(
												"<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code="serviceManagement.grid.empty.records"></spring:message></div>");
											$("#licenseDetailsListPagingDiv")
												.hide();
										} else {
											$("#licenseDetailsListPagingDiv")
													.show();
											ckIntanceSelected = new Array();
										}
										$('.checkboxbg').bootstrapToggle();

										$('.checkboxbg')
												.change(
														function() {
															serviceActiveInactiveToggleChanged(this);
														});

										var $jqgrid = $("#licenseDetailsList");
										$(".jqgrow", $jqgrid)
												.each(
														function(index, row) {
															var $row = $(row);
															//Find the checkbox of the row and set it disabled
															$row
																	.find(
																			"input:checkbox")
																	.attr(
																			"disabled",
																			"disabled");
														});

									},
									onPaging : function(pgButton) {
										clearResponseMsgDiv();
									},
									loadError : function(xhr, st, err) {
										handleGenericError(xhr, st, err);
									},
									beforeSelectRow : function(rowid, e) {
										// this blank function will not select the entire row. Only checkbox can be selectable.

										var $grid = $("#licenseDetailsList");

										if ($( "#jqg_licenseDetailsList_"
														+ $grid.jqGrid(
																'getCell',
																rowid, 'id'))
												.is(':checked')) {

											if (ckIntanceSelected.indexOf($grid
													.jqGrid('getCell', rowid,
															'id')) == -1) {
												ckIntanceSelected.push($grid
														.jqGrid('getCell',
																rowid, 'id'));
											}
										} else {
											if (ckIntanceSelected.indexOf($grid
													.jqGrid('getCell', rowid,'id')) != -1) {
												ckIntanceSelected.splice(ckIntanceSelected.indexOf($grid.jqGrid('getCell',rowid,'id')),1);
											}
										}
										return false;

									},
									onSelectAll : function(id, status) {

										if (status == true) {
											ckIntanceSelected = new Array();
											for (i = 0; i < id.length; i++) {
												ckIntanceSelected.push(id[i]);
											}
										} else {
											ckIntanceSelected = new Array();
										}

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
			}		
		
		</script>


		<!-- Footer Start -->
		<footer class="main-footer positfix">
			<div class="fooinn">
				<div class="fullwidth"></div>
				<jsp:include page="common/newfooter.jsp"></jsp:include>
			</div>
		</footer>
		<!-- Footer End -->

	</div>
	<!-- ./wrapper -->

</body>

</html>
