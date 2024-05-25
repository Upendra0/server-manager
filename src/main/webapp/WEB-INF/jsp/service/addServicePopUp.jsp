<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>


			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<i class="icon-hdd"></i> 
						<spring:message code="serviceManager.create.service" ></spring:message>
					</h4>
				</div>
				<form:form modelAttribute="service_form_bean" method="POST" action="addService" id="create-service_form">
				<div id="serviceContentDiv" class="modal-body padding10 inline-form">
				
					<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
					<form:hidden path="serverInstance.id"  id="serverInstanceID" value="" ></form:hidden>
					<form:hidden path="svctype.alias"  id="serviceTypeAlias" value="" ></form:hidden>
					<input type="hidden" id="servicelabel" name="servicelabel" value="" />
					
					<div class="fullwidth">
							<spring:message
								code="serviceManager.add.service.name"
								var="name" ></spring:message>
							
							<spring:message
								code="serviceManager.add.service.name.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="name"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="name" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip}" ></form:input>
									<spring:bind path="name">
										<elitecoreError:showError
											errorId="name_err"
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
							
						</div>
						<div class="fullwidth">
							<spring:message
								code="serviceManager.add.service.description"
								var="description" ></spring:message>
							<spring:message
								code="serviceManager.add.service.description.tooltip"
								var="tooltip" ></spring:message>
						
							<div class="form-group">
								<div class="table-cell-label">${description}</div>
								<div class="input-group ">
									<form:textarea path="description"
										cssClass="form-control input-sm" rows="4" tabindex="4"
										id="description" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip}" ></form:textarea>
									<spring:bind path="description">
										<elitecoreError:showError
											errorId="description_err"
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
					
					
					<%-- <elitecore:textAreaHTML 
									labelClass="table-cell-label"
									isMandatoryField="false"
									id="serviceDescription" 
									name="serviceDescription" 
									rows="3"
									inputClassName="form-control input-sm"
									i18NCode="serviceManager.add.service.description">
					</elitecore:textAreaHTML> --%>
					
				
				</div>
				</form:form>
				<div id="create-service-buttons-div" class="modal-footer padding10">
					<button id="createServiceInstanceBtn" type="button" class="btn btn-grey btn-xs " onclick="createService()">
						<spring:message code="btn.label.save" ></spring:message>
					</button>
					<button id="cancelServiceInstanceBtn" type="button" onclick="javascript:parent.closeFancyBoxFromChildIFrame()" class="btn btn-grey btn-xs ">
						<spring:message code="btn.label.cancel" ></spring:message>
					</button>
				</div>
				<div id="progress-bar-div" class="modal-footer padding10" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
				<script type="text/javascript">
					$(document).ready(function() {
						$("#addServiceAnchor").fancybox({
							   "afterLoad": function(){
									$("#name").val('');
									$("#description").val('');
									$("#create-service-buttons-div").show();
									resetWarningDisplayPopUp();
									clearAllMessagesPopUp();
							   },
							});
						
						

						$(window).keydown(function(event){
			 				
						    if(event.keyCode == 13) {
						    	event.preventDefault();
						        $(this).blur();
						        createService();
						    }
						  });
						
					});
					
					function createService(){
						resetWarningDisplayPopUp();
						clearAllMessagesPopUp();
						addNewServiceToDB(parent.updateServiceAccessStatus,parent.deleteServiceAccessRights);
					}
				
				</script>
			</div>
			<!-- /.modal-content -->
			
		
