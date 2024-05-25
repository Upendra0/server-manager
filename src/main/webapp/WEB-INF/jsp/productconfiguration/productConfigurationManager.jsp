<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>

<!DOCTYPE html>
<html>
<jsp:include page="../common/newheader.jsp"></jsp:include>
<style>
	.jstree-default .jstree-anchor, .jstree-default .jstree-wholerow {
	    transition: background-color 0.15s ease 0s, box-shadow 0.15s ease 0s;
	}
	.jstree-anchor-view{
		transition: background-color 0.15s ease 0s, box-shadow 0.15s ease 0s;
		text-decoration: none;
		color: inherit;
	}
	a:link {
	    text-decoration: none;
	}
</style>
<body class="skin-blue sidebar-mini">
<div class="wrapper"> 

<!-- Main div start here -->
	
		<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
		<jsp:include page="../common/newleftMenu.jsp"></jsp:include>

		<!-- Content wrapper div start here -->
		<div class="content-wrapper">
			<section class="content-header">
				<div class="fullwidth">
					<div style="padding: 0 10px 0 4px;">
					<div id="content-scroll-d" class="content-scroll"> 
					
							<div class="fullwidth">
								<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
								<a href="home.html"><img src="img/tile-icon.png" class="vmiddle" alt="Title Icon"/></a> 
                                    <span class="spanBreadCrumb" style="line-height: 30px;">
                                   		<strong>&nbsp;
                                   			<spring:message code="product.configuration.top.heading"></spring:message>
                                   		</strong>&nbsp;
                                    </span>
								</h4>
								<jsp:include page="../common/responseMsg.jsp" ></jsp:include>	
								
								<!-- Tab code start here  -->
								<div class="row">
									<div class="col-xs-12">
										<div class="box  martop" style="border: none; box-shadow: none;">
											<div class="box-body table-responsive no-padding">
												<div class="nav-tabs-custom tab-content">
													  
														<ul class="nav nav-tabs pull-right">
														
															<sec:authorize access="hasAnyAuthority('VIEW_PRODUCT_CONFIGURATION')">
																<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'PRODUCT_CONFIGURATION')}">
																	<c:out value="active"></c:out></c:if>"	>
																	<a href="#service-configuration" id="service_configuration"	data-toggle="tab" aria-expanded="true" onclick="showButtons('service_configuration');"> 
																	 	<spring:message code="product.configuration.service.tab.label"></spring:message> 
																	</a>
																</li>
															</sec:authorize>
															
														</ul>
														<div class="fullwidth no-padding tab-content">
														<sec:authorize access="hasAnyAuthority('VIEW_PRODUCT_CONFIGURATION')">
															<div id="view-configuration"
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'PRODUCT_CONFIGURATION')}"><c:out value="active"></c:out></c:if> ">
	 														 		<jsp:include page="productConfiguration.jsp"></jsp:include>  
															</div>
														</sec:authorize>
														
														</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<!-- Tab code end here -->
							</div>
						</div>
					</div>
				</div>

			</section>
			
		</div>
		
		
		
		<!-- Content wrapper div start here -->
		
		<!-- Footer code start here -->
		<footer class="main-footer positfix">
		    	<div class="fooinn">
		    		<div class="fullwidth">
						<div class="button-set">
			            	<div class="padleft-right" id="service_config_btn_set" style="display: none;">
			            		
			            		<sec:authorize access="hasAuthority('UPDATE_PRODUCT_CONFIGURATION')">
			            				<div id ="update-configuration-buttons-div"> 
									  	<button type="button" id="edit_config_btn" class="btn btn-grey btn-xs " onclick="makeCustomServiceTreeEnable();">
											<spring:message code="btn.label.edit"></spring:message>
										</button>
										
									    <button type="button" id="update_config_btn" class="btn btn-grey btn-xs " style="display:none;" onclick="updateConfiguration();">
											<spring:message code="btn.label.update"></spring:message>
										</button>

										<button type="button" id="reset_config_btn" class="btn btn-grey btn-xs"  onclick="resetToDefault();">
											<spring:message code="btn.label.reset.default"></spring:message>
										</button>
										</div>
										<div id="update-configuration-progress-bar-div" class="modal-footer padding10 input-group" style="display: none;">
											<jsp:include page="../common/processing-bar.jsp"></jsp:include>
										</div>
								</sec:authorize>
							</div>	
							
						</div>
					</div>		
		        	<jsp:include page="../common/newfooter.jsp"></jsp:include>
		    	</div>
		    	 <script type="text/javascript">
		    	 			
		    	 			    	 		
			       		$(document).ready(function() {
			       			var activeTab = $(".nav-tabs li.active a");
			    			var id = activeTab.attr("id");
			    			showButtons(id);
			    			$('#reset_config_btn').prop('disabled', true);
			    			
			    			$("#div_service .jstree-anchor").addClass('jstree-default jstree-anchor-view').removeClass('jstree-anchor');
			     		});
			       		
			       		function showButtons(tabType){
			       			if(tabType == 'service_configuration'){
			       				$("#service_config_btn_set").show();
			       			}
			       			
			       			$("#reset_config_btn").prop("disabled", true);
			       			$("#update_config_btn").hide();
			       			$("#edit_config_btn").show();
			       		}
			       		
			       		
			       		 function expandTree(elementId,type,divId){
			       			$("#"+elementId).jstree("open_all");
			       		
			       			console.log("Expand called success");
			       		 	if(isEnabled){
			       				
				       			$("#"+divId +".jstree-anchor-view").addClass('jstree-default jstree-anchor').removeClass('jstree-anchor-view');
				       			
			       			}else{
				       			
				       			$("#"+divId +" .jstree-anchor").addClass('jstree-default jstree-anchor-view').removeClass('jstree-anchor');
				       			
			       			}  
			       			
			       		 }
			       		 
			       		function collapseTree(elementId,type,divId){
			       			$("#"+elementId).jstree("close_all");
			       			
			       		 	if(isEnabled){
			       				
				       			$("#"+divId +" .jstree-anchor-view").addClass('jstree-default jstree-anchor').removeClass('jstree-anchor-view');
				       			
			       			}else{
				       			
				       			$("#"+divId +" .jstree-anchor").addClass('jstree-default jstree-anchor-view').removeClass('jstree-anchor');
				       			
			       			} 
			       			
			       		 }
			       		
			       		
			       		function showProgressBar(div){
			       			
			       			$("#"+div+"-buttons-div").hide();
			       			$("#"+div+"-progress-bar-div").show();
			       			
			       		}

			       		function hideProgressBar(div){
			       			$("#"+div+"-buttons-div").show();
			       			$("#"+div+"-progress-bar-div").hide();	
			       		}
			       		
			       		
			    </script>	
			</footer>
		<!-- Footer code end here -->
	</div>
	<!-- Main div end here -->
</body>
</html>	
