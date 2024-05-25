<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Save Action field form start here -->
		<%-- <div class="box box-warning" id="action_field_section" style="display:none;">
				<div class="box-header witd-border">
					<h3 class="box-title">
						<spring:message code="business.policy.create.action.form.heading" ></spring:message>
					</h3>
					<div class="box-tools pull-right" style="top: -3px;">
						<button class="btn btn-box-tool" data-widget="collapse">
							<i class="fa fa-minus"></i>
						</button>
					</div>
					<!-- /.box-tools -->
				</div>
				<!-- /.box-header end here -->
				
				<div class="box-body">
					<div class="fullwidtd inline-form">
						<!-- Form content start here  -->
							 <div class="col-md-9 no-padding">
								<div class="form-group">
									<spring:message code="business.policy.create.action.name.label"  var="tooltip"></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group ">
										<input class="form-control table-cell input-sm" tabindex="4"
											id="actionName" name="actionName" data-toggle="tooltip"
											data-placement="bottom" 
											title="${tooltip }" />
											<span class="input-group-addon add-on last "> 
									      	  	<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i>
									      	</span>
									</div>
								</div>
							</div>
							<div class="col-md-9 no-padding">
								<div class="form-group">
									<spring:message code="business.policy.create.action.description.label"  var="tooltip"></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group ">
										 <textarea class="form-control  " rows="4" cols="15" id="actionDescription" name="actionDescription"  data-toggle="tooltip"
											data-placement="bottom" 
											title="${tooltip }"style="resize: none;"></textarea>
										<span class="input-group-addon add-on last "> 
								      	  	<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i>
								      	</span>
									</div>
								</div>
							</div>
							<div class="col-md-9 no-padding">
								<div class="form-group">
									<div class="table-cell-label"></div>
									<div class="input-group ">
										 <div class="button-set">
											<button type="button" class="btn btn-grey btn-xs " tabindex="21" onclick="saveActionDetails();" >
												 <spring:message code="btn.label.save" ></spring:message>
											</button>
											<button type="button" class="btn btn-grey btn-xs " tabindex="22" onclick="closeActionDetails();" >
											    <spring:message code="btn.label.cancel"  ></spring:message>
											</button>
										</div>			 
									</div>
								</div>
							</div>
						<!-- Form content end here -->
					</div>
				</div>	
		</div> --%>
		<!-- Save Action field form end here -->
		
		<script type="text/javascript">
		
			/* function openSaveActionForm(){
				clearResponseMsg();
				$('#action_field_section').show();
				
				$('#action_expression_next_btn').prop('disabled', true);
				$('#action_expression_save_next_btn').prop('disabled', true);
			}
		
			function saveActionDetails(){
				showSuccessMsg("Action details saved successfully!");
				
				if(currentPage == "actionPage"){
					$('#action_field_section').hide();
					gotoSaveRuleDetails();
				}
			}
			
			function closeActionDetails(){
				$('#action_field_section').hide();
			} */
		
		</script>
		
