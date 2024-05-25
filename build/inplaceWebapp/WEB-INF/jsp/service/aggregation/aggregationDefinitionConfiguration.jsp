<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@ taglib uri="http://www.springframework.org/security/tags"	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<style>
.checkbox{
	color: #000 !important;
}
.btn .caret {
    margin-left: -10px;
}
.btn-group{
	width: 100% !important;
}
.btn-group button{
	width: 100% !important;
}
.btn-default span{
width: 100% !important;
background:none !important ;
}
</style>
<script>
var defConditionList;
var defKeyAttributeList;
var defAggAttributeList;
var conditionRowCount = 1;
var keyAttributeRowCount = 1;
var aggAttributeRowCount = 1;
var oldAggDefName;
</script>
<script src="${pageContext.request.contextPath}/customJS/aggregationManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<div class="box-body padding0 mtop10">
	<div class="fullwidth">
		<form:form modelAttribute="<%=FormBeanConstants.AGGREGATION_SERVICE_DEFINITION_FORM_BEAN%>" method="POST" action="<%= ControllerConstants.CREATE_AGGREGATION_DEFINITION_LIST %>" id="aggregation-service-definition-form" name="aggregation-service-definition-form">
			<form:hidden path="id" id="aggDefId"></form:hidden>
			<input type="hidden" name="instanceId" id="instanceId" value="${instanceId}"/>
			<input type="hidden" name="serviceId" id="serviceId" value="${serviceId}"/>
			<input type="hidden" name="aggregation-def-conditions" id="aggregation-def-conditions"/>
			<input type="hidden" name="aggregation-def-aggattribute" id="aggregation-def-aggattribute"/>
			<input type="hidden" name="aggregation-def-keyattribute" id="aggregation-def-keyattribute"/>
			
			<div class="box-body">
				<div class="fullwidth inline-form">
				<!-- fullwidth inline-form start -->
					<div class="col-md-6 no-padding">
						<spring:message code='aggregationService.service.definition.select.label' var='label'></spring:message>  
						<spring:message code='aggregationService.service.definition.select.tooltip' var='tooltip'></spring:message>  
						<div class="form-group">
							<div class='table-cell-label'>${label}</div> 
							<div class="input-group ">
								<form:select path="" cssClass="form-control table-cell input-sm" tabindex="1"
									id="aggSelectedDefinition" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"
									onchange="loadOtherAggDefinition()">
									<c:forEach var='aggDefinitionNameList' items='${aggDefinitionNameList}' >
										<option id='${aggDefinitionNameList}' value='${aggDefinitionNameList}'>${aggDefinitionNameList}</option>
									</c:forEach>
								</form:select>									
							</div>
						</div>
					</div> 
				</div>
			</div>
           			
			<!-- section-1 start here -->
			<div class="box box-warning" id="service_basic_details_div">
				<div class="box-header with-border">
					<h3 class="box-title">
							<spring:message code='aggregationService.defList.heading.section.label' ></spring:message>
					</h3>
				</div>
				<!-- /.box-header end here -->
				<!-- Form content start here  -->
				<div class="box-body">
					<div class="fullwidth inline-form">
					<!-- fullwidth inline-form start -->
						<div class="col-md-6 no-padding">
							<spring:message code='aggregationService.defList.name.label' var='label'></spring:message>  
							<spring:message code='aggregationService.defList.name.tooltip' var='tooltip'></spring:message>  
							<div class="form-group">
								<div class='table-cell-label'>${label}<span class='required'>*</span></div> 
								<div class="input-group ">
									<form:input path="aggDefName" cssClass="form-control table-cell input-sm" tabindex="2"
										id="aggDefName" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
									<spring:bind path="aggDefName">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="aggDefName_error"></elitecoreError:showError>
									</spring:bind>										
								</div>
							</div>
						</div> 
						
						<div class="col-md-6 no-padding">
							<spring:message code='aggregationService.defList.noofpartition.label' var='label'></spring:message>  
							<spring:message code='aggregationService.defList.noofpartition.tooltip' var='tooltip'></spring:message>  
							<div class="form-group">
								<div class='table-cell-label'>${label}</div> 
								<div class="input-group ">
									<form:input path="noOfPartition" 
										cssClass="form-control table-cell input-sm" tabindex="3"
										id="noOfPartition" data-toggle="tooltip"
										data-placement="bottom"  onkeydown='isNumericOnKeyDown(event)'
										title="${tooltip }"></form:input>
									<spring:bind path="noOfPartition">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="name_error"></elitecoreError:showError>
									</spring:bind>										
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code='aggregationService.defList.partCDRField.label' var='label'></spring:message> 
							<spring:message code='aggregationService.defList.partCDRField.tooltip' var='tooltip'></spring:message> 
							<div class="form-group">
								<div class='table-cell-label'>${label}</div>  
								<div class="input-group ">
									<jsp:include page="../../common/formAutocomplete.jsp">
										<jsp:param name="unifiedField" value="partCDRField" ></jsp:param>
										<jsp:param name="unifiedFieldPath" value="partCDRField" ></jsp:param>
									</jsp:include>
									<spring:bind path="partCDRField">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>	
						
						<div class="col-md-6 no-padding">
							<spring:message code='aggregationService.defList.fleg.label' var='label'></spring:message>  
							<spring:message code='aggregationService.defList.fleg.tooltip' var='tooltip'></spring:message> 
							<div class="form-group">
								<div class='table-cell-label'>${label}</div> 
								<div class="input-group ">
									<form:input path="fLegVal" maxlength="30"
										cssClass="form-control table-cell input-sm" tabindex="5"
										id="fLegVal" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }"></form:input>
									<spring:bind path="fLegVal">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="name_error"></elitecoreError:showError>
									</spring:bind>										
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code='aggregationService.defList.lleg.label' var='label'></spring:message>  
							<spring:message code='aggregationService.defList.lleg.tooltip' var='tooltip'></spring:message>  
							<div class="form-group">
								<div class='table-cell-label'>${label}</div> 
								<div class="input-group ">
									<form:input path="lLegVal" maxlength="30"
										cssClass="form-control table-cell input-sm" tabindex="6"
										id="lLegVal" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }"></form:input>
									<spring:bind path="lLegVal">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="name_error"></elitecoreError:showError>
									</spring:bind>										
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code='aggregationService.defList.unifiedDateField.label' var='label'></spring:message> 
							<spring:message code='aggregationService.defList.unifiedDateField.tooltip' var='tooltip'></spring:message> 
							<div class="form-group">
								<div class='table-cell-label'>${label}<span class='required'>*</span></div>  
								<div class="input-group ">
									<jsp:include page="../../common/formAutocomplete.jsp">
										<jsp:param name="unifiedField" value="unifiedDateFiled" ></jsp:param>
										<jsp:param name="unifiedFieldPath" value="unifiedDateFiled" ></jsp:param>
									</jsp:include>								
									<spring:bind path="unifiedDateFiled">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>	
												
						<div class="col-md-6 no-padding">
							<spring:message code='aggregationService.defList.aggInterval.label' var='label'></spring:message>  
							<spring:message code='aggregationService.defList.aggInterval.tooltip' var='tooltip'></spring:message>  
							<div class="form-group">
								<div class='table-cell-label'>${label}</div> 
								<div class="input-group ">
									<form:input path="aggInterval"
										cssClass="form-control table-cell input-sm" tabindex="9"
										id="aggInterval" data-toggle="tooltip"
										data-placement="bottom" onkeydown='isNumericOnKeyDown(event)'
										title="${tooltip }"></form:input>
									<spring:bind path="aggInterval">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="name_error"></elitecoreError:showError>
									</spring:bind>										
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code='aggregationService.defList.outputFileField.label' var='label'></spring:message> 
							<spring:message code='aggregationService.defList.outputFileField.tooltip' var='tooltip'></spring:message> 
							<div class="form-group">
								<div class='table-cell-label'>${label}</div>  
								<div class="input-group ">								
									<form:select path="outputFileField"
										cssClass="form-control table-cell input-sm" tabindex="10"
										id="outputFileField"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach var="FieldForOutputFileEnum" items="${FieldForOutputFileEnum}" >
											<option ${FieldForOutputFileEnum.value eq aggregation_service_definition_form_bean.outputFileField ? 'selected' : ''} id ="${FieldForOutputFileEnum.name}" value ="${FieldForOutputFileEnum.value}">${FieldForOutputFileEnum.name}</option>
										</c:forEach>
									</form:select>
									<spring:bind path="unifiedDateFiled">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>	
					
						<!-- Grid 0 start -->	
						<!-- heading start -->		
					    <div class='col-md-12 no-padding' id='condition_grid_links_div'>
							<div class='title2'>
								<spring:message code='aggregationService.defList.condition.section.label' ></spring:message>
								<span class='title2rightfield'>
					          		<span class='title2rightfield-icon1-text'>
										<sec:authorize access='hasAuthority(\'UPDATE_SERVICE_INSTANCE\')'>
						          			<a id='addCondLinkSym' href='#' onclick=openAddConditionPopup();><i class='fa fa-plus-circle'></i></a>
					  						<a id='addCondLinkText' href='#'  onclick=openAddConditionPopup();> 
												<spring:message code='btn.label.add' ></spring:message> 
					  						</a> 
										</sec:authorize>
					   				</span>	 
					          		<span class='title2rightfield-icon1-text'> 
										<sec:authorize access='hasAuthority(\'DELETE_SERVICE_INSTANCE\')'>
						          			<a href='#' onclick=deleteConditionDetails();> <i class='fa fa-trash'></i></a>
						          			<a href='#' id='deleteCondition' onclick=deleteConditionDetails();><spring:message code='btn.label.delete' ></spring:message></a>
										</sec:authorize>
					          		</span>
					          	</span> 
							</div>
						</div>
						<!-- heading end -->						
						
						<div class='col-md-12 no-padding' id='condition_grid_main_div'>
							<div class='box-body table-responsive no-padding box'>
						       	<table class='table table-hover' id='condition_grid'></table>
					           	<div id='condition_gridPagingDiv'></div>
					          	<div class='clearfix'></div>
					           	<div id='divLoading' align='center' style='display: none;'><img src='img/preloaders/Preloader_10.gif' /></div>
						    </div>
						</div>
						<!-- Grid 0 end -->
						
						<!-- Grid 1 start -->	
						<!-- heading start -->		
					    <div class='col-md-12 no-padding' id='keyattribute_grid_links_div'>
							<div class='title2'>
								<spring:message code='aggregationService.service.definition.keyattribute.section.label' ></spring:message>
								<span class='title2rightfield'>
					          		<span class='title2rightfield-icon1-text'>
										<sec:authorize access='hasAuthority(\'UPDATE_SERVICE_INSTANCE\')'>
						          			<a id='addKeyAttributeLinkSym' href='#' onclick=openAddKeyAttributePopup();><i class='fa fa-plus-circle'></i></a>
					  						<a id='addKeyAttributeLinkText' href='#'  onclick=openAddKeyAttributePopup();> 
												<spring:message code='btn.label.add' ></spring:message> 
					  						</a> 
										</sec:authorize>
					   				</span>	 
					          		<span class='title2rightfield-icon1-text'> 
										<sec:authorize access='hasAuthority(\'DELETE_SERVICE_INSTANCE\')'>
						          			<a href='#' onclick=deleteKeyAttributeDetails();> <i class='fa fa-trash'></i></a>
						          			<a href='#' id='deleteKeyAttribute' onclick=deleteKeyAttributeDetails();><spring:message code='btn.label.delete' ></spring:message></a>
										</sec:authorize>
					          		</span>
					          	</span> 
							</div>
						</div>
						<!-- heading end -->						
						
						<div class='col-md-12 no-padding' id='keyattribute_grid_main_div'>
							<div class='box-body table-responsive no-padding box'>
						       	<table class='table table-hover' id='keyattribute_grid'></table>
					           	<div id='keyattribute_gridPagingDiv'></div>
					          	<div class='clearfix'></div>
					           	<div id='divLoading1' align='center' style='display: none;'><img src='img/preloaders/Preloader_10.gif' /></div>
						    </div>
						</div>
						<!-- Grid 1 end -->
						
						
						<!-- Grid 2 start -->	
						<!-- heading start -->		
					    <div class='col-md-12 no-padding' id='aggattribute_grid_links_div'>
							<div class='title2'>
								<spring:message code='aggregationService.service.definition.aggattribute.section.label' ></spring:message>
								<span class='title2rightfield'>
					          		<span class='title2rightfield-icon1-text'>
										<sec:authorize access='hasAuthority(\'UPDATE_SERVICE_INSTANCE\')'>
						          			<a id='addAggAttributeLinkSym' href='#' onclick=openAddAggAttributePopup();><i class='fa fa-plus-circle'></i></a>
					  						<a id='addAggAttributeLinkText' href='#'  onclick=openAddAggAttributePopup();> 
												<spring:message code='btn.label.add' ></spring:message> 
					  						</a> 
										</sec:authorize>
					   				</span>	 					   				
					          		<span class='title2rightfield-icon1-text'> 
										<sec:authorize access='hasAuthority(\'DELETE_SERVICE_INSTANCE\')'>
						          			<a href='#' onclick=deleteAggAttributeDetails();> <i class='fa fa-trash'></i></a>
						          			<a href='#' id='deleteAggAttribute' onclick=deleteAggAttributeDetails();><spring:message code='btn.label.delete' ></spring:message></a>
										</sec:authorize>
					          		</span>
					          	</span> 
							</div>
						</div>
						<!-- heading end -->						
						
						<div class='col-md-12 no-padding' id='aggattribute_grid_main_div'>
							<div class='box-body table-responsive no-padding box'>
						       	<table class='table table-hover' id='aggattribute_grid'></table>
					           	<div id='aggattribute_gridPagingDiv'></div>
					          	<div class='clearfix'></div>
					           	<div id='divLoading' align='center' style='display: none;'><img src='img/preloaders/Preloader_10.gif' /></div>
						    </div>
						</div>
						<!-- Grid 2 end -->
						
						
					<!-- fullwidth inline-form end -->
					</div>
				</div>
				<!-- Form content end here  -->
			</div>
			<!-- section-1 end here -->
			
			
		</form:form>
		
		<script type="text/javascript" >
			$(document).ready(function() {
				var condList = '${defConditionList}';
				if(condList != 'undefined' && condList != null && condList.length > 0){
					defConditionList = JSON.parse(condList);
					loadConditionGrid(defConditionList);
				}
				
				
				var keyAttributeList = '${defKeyAttributeList}';
				if(keyAttributeList != 'undefined' && keyAttributeList != null && keyAttributeList.length > 0){
					defKeyAttributeList = JSON.parse(keyAttributeList);
					loadKeyAttributeGrid(defKeyAttributeList);
				}
				
				
				var aggAttributeList = '${defAggAttributeList}';
				if(aggAttributeList != 'undefined' && aggAttributeList != null && aggAttributeList.length > 0){
					defAggAttributeList = JSON.parse(aggAttributeList);
					loadAggAttributeGrid(defAggAttributeList);
				}
				
				managePopupValuesAfterLoad(defConditionList,defKeyAttributeList,defAggAttributeList);
				
				$('#aggSelectedDefinition').append($('<option>',{id: "Select",value: "Select",text: 'Select',selected: true}));
				changePartCDRField();
				manageConditionButtonVisibility();
			});
			
			$(window).on('load', function () {
				changeDefinitionName();
			});
			
			function changeDefinitionName(){
				var defName = $('#aggDefName').val();
				if(defName != ''){
					$('#aggSelectedDefinition option[value=\''+defName+'\']').attr("selected", "selected");
					oldAggDefName = defName;
				}
			}
			function loadOtherAggDefinition(){
				var selectedDefinitionName = $("#aggSelectedDefinition option:selected").text();
				deleteAllConditionDetails();
				deleteAllKeyAttributeDetails();
				deleteAllAggAttributeDetails();
				if(selectedDefinitionName == 'Select'){
					$("#noOfPartition").val("1");		
					$("#fLegVal").val("");
					$("#lLegVal").val("");
					$("#aggInterval").val("30");	
					//$("#partCDRField option[value='NONE']").attr("selected", "selected");
					changePartCDRField();
				}else{
					
					$.ajax({
						url : "loadAggregationDefinitionData",
						cache : false,
						async : true,
						dataType : 'json',
						type : "POST",
						data : {"aggDefName" : selectedDefinitionName},
						success : function(data) {
							hideProgressBar();
							var responseCode = data.code;
							var responseMsg = data.msg;
							var responseObject = data.object;
							if (responseCode === "200") {
								resetWarningDisplay();
								clearAllMessages();
								defConditionList = responseObject.jsonConditionArray;
								defKeyAttributeList = responseObject.jsonAggKeyArray;
								defAggAttributeList = responseObject.jsonAggAttributeArray;
								managePopupValuesAfterLoad(defConditionList,defKeyAttributeList,defAggAttributeList);
								jQuery("#condition_grid").jqGrid('setGridParam',{datatype:'local',data:defConditionList}).trigger("reloadGrid");
								jQuery("#keyattribute_grid").jqGrid('setGridParam',{datatype:'local',data:defKeyAttributeList}).trigger("reloadGrid");
								jQuery("#aggattribute_grid").jqGrid('setGridParam',{datatype:'local',data:defAggAttributeList}).trigger("reloadGrid");
								
								var aggDefinition = responseObject.aggDefinition;
								$("#noOfPartition").val(aggDefinition.noOfPartition);
								$("#partCDRField").val(aggDefinition.partCDRField);
								$("#aggInterval").val(aggDefinition.aggInterval);
								$("#fLegVal").val(aggDefinition.fLegVal);
								$("#lLegVal").val(aggDefinition.lLegVal);
								$("#aggType").val(aggDefinition.aggType);
								$("#unifiedDateFiled").val(aggDefinition.unifiedDateFiled);
								$("#outputFileField").val(aggDefinition.outputFileField);
								changePartCDRField();

							} else if (responseObject !== undefined
									&& responseObject !== 'undefined'
									&& responseCode === "400") {
								hideProgressBar();
								addErrorIconAndMsgForAjax(responseObject);
							} else {
								hideProgressBar(counter);
								resetWarningDisplay();
								clearAllMessages();
								showErrorMsg(responseMsg);
							}
						},
						error : function(xhr, st, err) {
							hideProgressBar(counter);
							handleGenericError(xhr, st, err);
						}
					});					
				}							
			}		
			
			function changePartCDRField(){
				var partCDRVal = $("#partCDRField").val();
				if(partCDRVal == 'NONE'){
					$('#condition_grid').jqGrid('clearGridData'); 
					$('#addCondLinkSym').hide();
					$('#addCondLinkText').hide();
					$('#fLegVal').attr('disabled',true);
					$('#lLegVal').attr('disabled',true);
					$('#noOfPartition').attr('disabled',true);
					$('#aggInterval').attr('disabled',true);
					$("#fLegVal").val('');
					$("#lLegVal").val('');
				}else{
					$('#addCondLinkSym').show();
					$('#addCondLinkText').show();
					$('#addCondLinkText').attr('disabled',false);
					$('#fLegVal').attr('disabled',false);
					$('#lLegVal').attr('disabled',false);
					$('#noOfPartition').attr('disabled',false);
					$('#aggInterval').attr('disabled',false);
				}
				
			}
			function openAddConditionPopup(){
				clearAllMessagesPopUp();
			 	resetWarningDisplayPopUp();
			 	$("#add_condition_buttons_div").show();
			 	$("#add_condition_header_label").show();
			 	$("#add_condition_link").click();	
			 	manageCondActionOptions();
			 }
			
			function addConditionDetails(){
				var uniqueGridId = "condition_grid";
				var newDefination = {};
				var condAction = $("#aggregationConditionActionField option:selected").val();	
				var condActionDisplay = $("#aggregationConditionActionField option:selected").text();	
				var conditionExpression = $("#aggregationConditionField option:selected").text();
				var conditionExpressionValue = $("#aggregationConditionField option:selected").val();
				if(conditionExpression == ""){
					return;			
				}

				newDefination.id = conditionRowCount;
				conditionRowCount = conditionRowCount + 1;
				newDefination.conditionExpression = conditionExpression;
				newDefination.conditionAction = condActionDisplay;
				newDefination.conditionExpressionValue = conditionExpressionValue;
				defConditionList.push(newDefination); 
				
				jQuery("#"+uniqueGridId).jqGrid('setGridParam',{datatype: 'local',data:defConditionList}).trigger("reloadGrid");
				removeConditionPopupValues(conditionExpression,condActionDisplay);
				closeFancyBox();
				sortAttributeList();
				manageConditionButtonVisibility();
			}
			
			function manageConditionButtonVisibility(){
				var uniqueGridId = "condition_grid";
				var addedConditionCount = $("#"+uniqueGridId).getGridParam("reccount");
				var totalConditionCount = ${fn:length(aggConditionExpression)};
				var partCDRVal = $("#partCDRField").val();
				if(partCDRVal != 'NONE'){
					if(addedConditionCount == totalConditionCount){
						$('#addCondLinkSym').hide();
						$('#addCondLinkText').hide(); 
					}else{
						$('#addCondLinkSym').show();
						$('#addCondLinkText').show(); 
					}
				}
			}
			
			function managePopupValuesAfterLoad(defConditionJSONArray,defKeyAttributeJSONArray,defAggAttributeJSONArray){
				if(defConditionJSONArray != 'undefined' && defConditionJSONArray != null){
					jQuery.each(defConditionJSONArray, function() {
						removeConditionPopupValues(this.conditionExpression,this.conditionAction)
					});
				}
				
				if(defKeyAttributeJSONArray != 'undefined' && defKeyAttributeJSONArray != null){
					jQuery.each(defKeyAttributeJSONArray, function() {
						$("#aggregationKeyAttributeField option[id='"+ this.aggregationFieldName+"']").remove();
						$("#aggregationOutputField option[id='"+ this.aggregationFieldName+"']").remove();
					});
				}
				
				if(defAggAttributeJSONArray != 'undefined' && defAggAttributeJSONArray != null){
					jQuery.each(defAggAttributeJSONArray, function() {
						$("#aggregationOutputField option[id='"+ this.outputfieldname+"']").remove(); 
						$("#aggregationKeyAttributeField option[id='"+ this.outputfieldname+"']").remove();
					});
				}
			}
			
			function manageCondActionOptions(){
				var aggCondVal = $("#aggregationConditionField option:selected").val();
				
				if(aggCondVal == '1' || aggCondVal == '2' || aggCondVal == '4'){
					$("#aggregationConditionActionField option[id='AGGTIMEOUT']").remove();
				}else{
					if($("#aggregationConditionActionField option[id='AGGTIMEOUT']").length == 0)
						addPopupListValues('aggregationConditionActionField','AGGTIMEOUT','AGGTIMEOUT','Aggregate on timeout');
				}
				
				if(!(aggCondVal == '1' || aggCondVal == '2' || aggCondVal == '4' || aggCondVal == '7')){
					$("#aggregationConditionActionField option[id='AGGREGATE']").remove();
				}else{
					if($("#aggregationConditionActionField option[id='AGGREGATE']").length == 0)
						addPopupListValues('aggregationConditionActionField','AGGREGATE','AGGREGATE','Aggregate');
				}
			}
			
			function removeConditionPopupValues(conditionExpression,condAction){
				$("#aggregationConditionField option[id='"+ conditionExpression+"']").remove();
			}
			
			function deleteAllConditionDetails(){
				$('input:checkbox[name="conditionAttributeCheckbox"]').prop('checked',true);
				deleteConditionDetails();
			}
			function deleteAllKeyAttributeDetails(){
				$('input:checkbox[name="keyAttributeCheckbox"]').prop('checked',true);
				deleteKeyAttributeDetails();
			}
			function deleteAllAggAttributeDetails(){
				$('input:checkbox[name="aggAttributeCheckbox"]').prop('checked',true);
				deleteAggAttributeDetails();
			}
			function deleteConditionDetails(){
				var uniqueGridId = "condition_grid";
				var childCheckboxName = "conditionAttributeCheckbox";	
				var selectAllCheckboxId = "selectAllConditionAttribute";
				var selectedAttributeCheckboxes = [];
				clearAllMessagesPopUp();
				resetWarningDisplay();		
			    $.each($("input[name='"+childCheckboxName+"']:checked"), function(){            
			    	selectedAttributeCheckboxes.push($(this).val());
			    });
				for(var i=0;i<selectedAttributeCheckboxes.length;i++){
					var rowid=selectedAttributeCheckboxes[i];
					$("#"+uniqueGridId).jqGrid('delRowData',rowid);
				    defConditionList.forEach( function (arr) {
					    if(arr.id == rowid) {
					    	var index = defConditionList.indexOf(arr);
					    	if (index > -1) {
					    		if(arr.conditionAction == "Aggregate"){
					    			if($("#aggregationConditionActionField option[id='AGGREGATE']").length == 0)
										addPopupListValues('aggregationConditionActionField','AGGREGATE','AGGREGATE','Aggregate');
					    		}	
				    			addPopupListValues('aggregationConditionField',arr.conditionExpression,arr.conditionExpressionValue,arr.conditionExpression);
				    			defConditionList.splice(index, 1);
					    	}
					    }
					});					    
				    //console.log("defConditionList : "+JSON.stringify(defConditionList));						
				}
				$('input:checkbox[id="'+selectAllCheckboxId+'"]').prop('checked',false);
				sortAttributeList();
				manageConditionButtonVisibility();
			}

			function cancelConfiguration(){
				showButtons('aggregation_service_configuration');
				$("#aggregation_service_configuration").click();
			}
			
			function openAddKeyAttributePopup(){
				clearAllMessagesPopUp();
			 	resetWarningDisplayPopUp();
			 	$("#aggregationKeyAttributeField").val('');
			 	$("#add_keyattribute_buttons_div").show();
			 	$("#add_keyattribute_header_label").show();
			 	$("#add_keyattribute_link").click();			 	
			 }
			
			function addKeyAttributeDetails(){
				var uniqueGridId = "keyattribute_grid";
				var newKeyAttribute = {};
				var aggFieldName = $("#aggregationKeyAttributeField").val();
				if(aggFieldName == ""){
					return;			
				}
				if(aggFieldName!=""){
					if(!validateUnifiedFieldsAlphaNumeric(aggFieldName)){
						var msg="<spring:message code='aggregation.unifiedfield'></spring:message>";
						showErrorMsgPopUp(msg);
						return;
					}	
				}
				newKeyAttribute.id = keyAttributeRowCount;
				keyAttributeRowCount = keyAttributeRowCount + 1;
				newKeyAttribute.aggregationFieldName = aggFieldName;
				defKeyAttributeList.push(newKeyAttribute); 
				
				jQuery("#"+uniqueGridId).jqGrid('setGridParam',{datatype: 'local',data:defKeyAttributeList}).trigger("reloadGrid");
				
				$("#aggregationKeyAttributeField option[id='"+ aggFieldName+"']").remove();	
				$("#aggregationOutputField option[id='"+ aggFieldName+"']").remove();
				closeFancyBox();
				sortAttributeList();
			}
			
			function deleteKeyAttributeDetails(){
				var uniqueGridId = "keyattribute_grid";
				var selectAllCheckboxId = "selectAllKeyAttribute";
				var childCheckboxName = "keyAttributeCheckbox";
				
				var selectedKeyAttributeCheckboxes = [];
				clearAllMessagesPopUp();
				resetWarningDisplay();									
				
			    $.each($("input[name='"+childCheckboxName+"']:checked"), function(){            
			    	selectedKeyAttributeCheckboxes.push($(this).val());
			    });
				for(var i=0;i<selectedKeyAttributeCheckboxes.length;i++){
					var rowid=selectedKeyAttributeCheckboxes[i];
					$("#"+uniqueGridId).jqGrid('delRowData',rowid);
					defKeyAttributeList.forEach( function (arr) {
					    if(arr.id == rowid) {
					    	var index = defKeyAttributeList.indexOf(arr);
					    	if (index > -1) {
					    		addPopupListValues('aggregationKeyAttributeField',arr.aggregationFieldName,arr.aggregationFieldName,arr.aggregationFieldName);
					    		addPopupListValues('aggregationOutputField',arr.aggregationFieldName,arr.aggregationFieldName,arr.aggregationFieldName);				    		
					    		defKeyAttributeList.splice(index, 1);
					    	}
					    }
					});						
				}
				$('input:checkbox[id="'+selectAllCheckboxId+'"]').prop('checked',false);
				sortAttributeList();
			}
			
			function sortAttributeList(){
				var aggregationOutputFieldList = $('#aggregationOutputField option');
				var aggregationKeyAttributeFieldList = $('#aggregationKeyAttributeField option');
				if(aggregationOutputFieldList.length > 0){
					aggregationOutputFieldList.sort(function(a,b){  if (a.text > b.text) return 1;if (a.text < b.text) return -1;return 0});
					$("#aggregationOutputField").empty().append( aggregationOutputFieldList);
					$('#aggregationOutputField option')[0].selected = true;	
				}
				if(aggregationKeyAttributeFieldList.length > 0){
					aggregationKeyAttributeFieldList.sort(function(a,b){  if (a.text > b.text) return 1;if (a.text < b.text) return -1;return 0});
					$("#aggregationKeyAttributeField").empty().append( aggregationKeyAttributeFieldList);
					$('#aggregationKeyAttributeField option')[0].selected = true;
				}		
			}
			
			function openAddAggAttributePopup(){
				clearAllMessagesPopUp();
			 	resetWarningDisplayPopUp();
			 	$("#view_aggAttribute_buttons_div").show();
			 	$("#aggregationOutputField").val('');
			 	$("#add_aggattribute_header_label").show();
			 	$("#add_aggattribute_link").click(); 				
			}
			function validateUnifiedFieldsAlphaNumeric(unifiedFieldValue){
				var regex = new RegExp("^[a-zA-Z]+[a-zA-Z0-9_]+$");
				if (regex.test(unifiedFieldValue)) {
			        return true;
			    }
				return false;
			}

			function addAggAttributeDetails(){
				var uniqueGridId = "aggattribute_grid";
				var newAggAttribute = {};
				var aggregationOutputFieldVal = $("#aggregationOutputField").val();
				var aggOutputFieldDataTypeFieldVal = $("#aggOutputFieldDataTypeField option:selected").text();
				var aggOperationExpressionVal = $("#aggOperationExpression option:selected").val();
				var aggOperationExpressionText = $("#aggOperationExpression option:selected").text();
				if(aggregationOutputFieldVal == "" || aggOutputFieldDataTypeFieldVal == "" || aggOperationExpressionVal == ""){
					showErrorMsgPopUp('Please provide appropriate value for the operation expression');
					return;			
				}
				if(aggregationOutputFieldVal!=""){
					if(!validateUnifiedFieldsAlphaNumeric(aggregationOutputFieldVal)){
						var msg="<spring:message code='aggregation.unifiedfield'></spring:message>";
						showErrorMsgPopUp(msg);
						return;
					}	
				}
				newAggAttribute.id = aggAttributeRowCount;
				aggAttributeRowCount = aggAttributeRowCount + 1;
				newAggAttribute.outputfieldname = aggregationOutputFieldVal;
				newAggAttribute.operationexpression = aggOperationExpressionVal;
				newAggAttribute.aggoutputfielddatatypefield = aggOutputFieldDataTypeFieldVal;
				newAggAttribute.operationexpressionText = aggOperationExpressionText;
				
				defAggAttributeList.push(newAggAttribute); 
				
				jQuery("#"+uniqueGridId).jqGrid('setGridParam',{datatype: 'local',data:defAggAttributeList}).trigger("reloadGrid");
				
				$("#aggregationOutputField option[id='"+ aggregationOutputFieldVal+"']").remove();	
				$("#aggregationKeyAttributeField option[id='"+ aggregationOutputFieldVal+"']").remove();	
				closeFancyBox();
				sortAttributeList();
			}
			
			function deleteAggAttributeDetails(){
				var uniqueGridId = "aggattribute_grid";
				var selectAllCheckboxId = "selectAllAggAttribute";
				var childCheckboxName = "aggAttributeCheckbox";
				
				var selectedAggAttributeCheckboxes = [];
				clearAllMessagesPopUp();
				resetWarningDisplay();									
				
			    $.each($("input[name='"+childCheckboxName+"']:checked"), function(){            
			    	selectedAggAttributeCheckboxes.push($(this).val());
			    });
				for(var i=0;i<selectedAggAttributeCheckboxes.length;i++){
					var rowid=selectedAggAttributeCheckboxes[i];
					$("#"+uniqueGridId).jqGrid('delRowData',rowid);
					defAggAttributeList.forEach( function (arr) {
					    if(arr.id == rowid) {
					    	var index = defAggAttributeList.indexOf(arr);
					    	if (index > -1) {	
					    		addPopupListValues('aggregationOutputField',arr.outputfieldname,arr.outputfieldname,arr.outputfieldname);
					    		addPopupListValues('aggregationKeyAttributeField',arr.outputfieldname,arr.outputfieldname,arr.outputfieldname);
					    		defAggAttributeList.splice(index, 1);
					    	}
					    }
					});						
				}
				$('input:checkbox[id="'+selectAllCheckboxId+'"]').prop('checked',false);
				sortAttributeList();
			}

			function saveDefinition(){
				
				var noOfPartitionVal  =  $('#noOfPartition').val();
   				if(noOfPartitionVal == '' || noOfPartitionVal == null){
   					$("#noOfPartition").val('0');
   				}
				var keyAttribRowCount = $("#keyattribute_grid").getGridParam("reccount");
				if(keyAttribRowCount == 0){
					showErrorMsg('Atleast one key attribute needs to be added');	
					scrollTo("top");
					return;
				}
				$("#aggregation-definition-progress-bar-div").show();
				$("#aggdefinitionbtnDiv").hide();
				var aggAttributeFullData = jQuery("#aggattribute_grid").jqGrid('getRowData');
				var conditionFullData = jQuery("#condition_grid").jqGrid('getRowData');
				console.log("defConditionList : "+JSON.stringify(conditionFullData));
				var keyAttributeFullData = jQuery("#keyattribute_grid").jqGrid('getRowData');
				$('#aggregation-def-conditions').val(JSON.stringify(conditionFullData));
				$('#aggregation-def-aggattribute').val(JSON.stringify(aggAttributeFullData));
				$('#aggregation-def-keyattribute').val(JSON.stringify(keyAttributeFullData));
				var defId = $('#aggDefId').val();
				var actionURL;
				if(defId == 0){
					 actionURL = "<%= ControllerConstants.CREATE_AGGREGATION_DEFINITION_LIST %>";
				}else{
					 actionURL = "<%= ControllerConstants.UPDATE_AGGREGATION_DEFINITION_LIST %>";
				}
				resetWarningDisplay();
				clearAllMessages();
				showProgressBar();
				
				$.ajax({
					url: actionURL,
					cache: false,
					async: true,
					dataType: 'json',
					type: "POST",
					data:
					 {
						"id" 				    :	$('#aggDefId').val(),
						"aggDefName" 			:	$('#aggDefName').val(),
						"noOfPartition" 		:	$('#noOfPartition').val(),
						"partCDRField" 			:	$('#partCDRField').val(),
						"fLegVal" 				:	$('#fLegVal').val(),
						"lLegVal" 				:	$('#lLegVal').val(),
						"unifiedDateFiled" 		:	$('#unifiedDateFiled').val(),
						"aggInterval" 			:	$('#aggInterval').val(),
						"outputFileField" 		:	$('#outputFileField').val(),
						"aggregation-def-conditions"   :   JSON.stringify(conditionFullData),
						"aggregation-def-aggattribute" :   JSON.stringify(aggAttributeFullData),
						"aggregation-def-keyattribute" :   JSON.stringify(keyAttributeFullData),
						"aggregationService.id" : $('#serviceId').val(),
						
					}, 
					
					success: function(data){
						$("#aggregation-definition-progress-bar-div").hide();
						$("#aggdefinitionbtnDiv").show();
						var response = eval(data);
						var responseCode = response.code; 
						var responseMsg = response.msg; 
						var responseObject = response.object;
						scrollTo("top");
						if(responseCode == "200"){
							resetWarningDisplay();
							clearAllMessages();
							showSuccessMsg(responseMsg);
							var aDefName = $('#aggDefName').val();
							$("#aggSelectedDefinition option[id='"+ oldAggDefName+"']").remove();	
							oldAggDefName = aDefName;
							$('#aggSelectedDefinition').append($('<option>',{id: aDefName,value: aDefName,text: aDefName,selected: true}));
							$('#aggDefId').val(responseObject.id);
						}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
							hideProgressBar();
							addErrorIconAndMsgForAjax(responseObject); 
						}else{
							hideProgressBar();
							resetWarningDisplay();
							clearAllMessages();
							showErrorMsg(responseMsg);
						}
						
		 			},
				    error: function (xhr,st,err){
				    	$("#aggregation-definition-progress-bar-div").hide();
						handleGenericError(xhr,st,err);
					}
				});
			}
		</script>
	</div>
</div>
<%--Add condition pop-up code start here --%>
<a href="#divAddGroupingAttribute" class="fancybox" style="display: none;" id="add_condition_link">#</a>
<form:form modelAttribute="con-grouping-attribute-form_bean" method="POST" action="createConsolidationDefinitionList" id="con-grouping-attribute-form">
<div id="divAddGroupingAttribute" style="width:100%; display: none;"  >
    <div class="modal-content">
    	
        <div class="modal-header padding10">
            <h4 class="modal-title">
            	<span id="add_condition_header_label" style="display:none;"><spring:message code="aggregationService.service.definition.condition.header.add" ></spring:message></span>
	   		</h4>
        </div>
        <div class="modal-body padding10 inline-form">
        	<div>
        		<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
        	</div>
			
			<div class="col-md-12 no-padding">
	        	<spring:message code="aggregationService.service.definition.condition.expression.label" var="label"></spring:message>
	        	<spring:message code="aggregationService.service.definition.condition.expression.tooltip" var="tooltip"></spring:message>
	           	<div class="form-group">
	        		<div class="table-cell-label">${label}<span class="required">*</span></div>
	           		<div class="input-group col-md-6">		            
	            		<select id="aggregationConditionField" class="form-control table-cell input-sm" tabindex="1" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" onchange="manageCondActionOptions();" >
		            		<c:forEach var='AggregationConditionEnum' items='${aggConditionExpression}' >
								<option id='${AggregationConditionEnum.displayValue}' value='${AggregationConditionEnum.value}'>${AggregationConditionEnum.displayValue}</option>
							</c:forEach > 
						</select>
	           			<span class="input-group-addon add-on last" > <i id="conditionAttributeField_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
	           		</div>
	           	</div>
	        </div>		
		    
		    <div class="col-md-12 no-padding">
	        	<spring:message code="aggregationService.service.definition.condition.action.label" var="label"></spring:message>
	        	<spring:message code="aggregationService.service.definition.condition.action.tooltip" var="tooltip"></spring:message>
	           	<div class="form-group">
	        		<div class="table-cell-label">${label}<span class="required">*</span></div>
	           		<div class="input-group col-md-6">		            
	            		<select id="aggregationConditionActionField" class="form-control table-cell input-sm" tabindex="2" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
		            		<c:forEach var='AggregationConditionActionEnum' items='${aggConditionAction}' >
								<option id='${AggregationConditionActionEnum}' value='${AggregationConditionActionEnum}'>${AggregationConditionActionEnum.displayValue}</option>
							</c:forEach > 
						</select>
	           			<span class="input-group-addon add-on last" > <i id="conditionAttributeAction_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
	           		</div>
	           	</div>
	        </div>	   
		      <div id="add_edit_group_button_div" class="modal-footer padding10">
			       
			         <div id="add_condition_buttons_div" class="padding10" style="display:none;">
			           <sec:authorize access="hasAuthority('UPDATE_SERVICE_INSTANCE')">
			         		<button id="saveGorupAttrBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="addConditionDetails();"><spring:message code="btn.label.add"></spring:message></button>
			           </sec:authorize>	
			         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
			        </div>
			        
			        <div id="view_group_buttons_div" class="modal-footer padding10" style="display:none;">
			         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
			        <div id="view_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
						<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
					</div>	
				</div>		
			<%--Content end here --%>
			</div>
    </div>
    <!-- /.modal-content --> 
</div>
</form:form>
<%--Add condition pop-up code end here --%>


<%--Add Key Attribute pop-up code start here --%>
<a href="#divAddKeyAttribute" class="fancybox" style="display: none;" id="add_keyattribute_link">#</a>
<form:form modelAttribute="con-grouping-attribute-form_bean" method="POST" action="createConsolidationDefinitionList" id="con-grouping-attribute-form">
<div id="divAddKeyAttribute" style="width:100%; display: none;"  >
    <div class="modal-content">
    	
        <div class="modal-header padding10">
            <h4 class="modal-title">
            	<span id="add_keyattribute_header_label" style="display:none;"><spring:message code="aggregationService.service.definition.keyattribute.header.add" ></spring:message></span>
	   		</h4>
        </div>
        <div class="modal-body padding10 inline-form">
        	<div>
        		<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
        	</div>
			
			<div class="col-md-12 no-padding">
	        	<spring:message code="aggregationService.service.definition.keyattribute.aggfield.label" var="label"></spring:message>
	        	<spring:message code="aggregationService.service.definition.keyattribute.aggfield.tooltip" var="tooltip"></spring:message>
	           	<div class="form-group">
	        		<div class="table-cell-label">${label}<span class="required">*</span></div>
	           		<div class="input-group col-md-6">		            
	            		<jsp:include page="../../common/autocomplete.jsp">
	            			<jsp:param name="url" value="getAllUnifiedField" ></jsp:param>
							<jsp:param name="unifiedField" value="aggregationKeyAttributeField" ></jsp:param>
						</jsp:include>
	           		</div>
	           	</div>
	        </div>		
		      
	      	<div id="add_edit_group_button_div" class="modal-footer padding10">
		       
		         <div id="add_keyattribute_buttons_div" class="padding10" style="display:none;">
		           <sec:authorize access="hasAuthority('UPDATE_SERVICE_INSTANCE')">
		         		<button id="saveKeyAttributeBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="addKeyAttributeDetails();"><spring:message code="btn.label.add"></spring:message></button>
		           </sec:authorize>	
		         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
		        </div>
		        
		        <div id="view_group_buttons_div" class="modal-footer padding10" style="display:none;">
		         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		        <div id="view_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
				</div>	
			</div>		
			<%--Content end here --%>
			</div>
    </div>
    <!-- /.modal-content --> 
</div>
</form:form>
<%--Add Key Attribute pop-up code end here --%>

<%--Add Aggregation Attribute pop-up code start here --%>
<a href="#divAddAggregationAttribute" class="fancybox" style="display: none;" id="add_aggattribute_link">#</a>
<form:form modelAttribute="con-grouping-attribute-form_bean" method="POST" action="createConsolidationDefinitionList" id="con-grouping-attribute-form">
<div id="divAddAggregationAttribute" style="width:100%; display: none;"  >
    <div class="modal-content">
    	
        <div class="modal-header padding10">
            <h4 class="modal-title">
            	<span id="add_aggattribute_header_label" style="display:none;"><spring:message code="aggregationService.service.definition.aggattribute.header.add" ></spring:message></span>
	   		</h4>
        </div>
        <div class="modal-body padding10 inline-form">
        	<div>
        		<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
        	</div>
			
			<div class="col-md-12 no-padding">
	        	<spring:message code="aggregationService.service.definition.aggattribute.outputfieldname.label" var="label"></spring:message>
	        	<spring:message code="aggregationService.service.definition.aggattribute.outputfieldname.tooltip" var="tooltip"></spring:message>
	           	<div class="form-group">
	        		<div class="table-cell-label">${label}<span class="required">*</span></div>
	           		<div class="input-group col-md-6">		            
	            		<jsp:include page="../../common/autocomplete.jsp">
							<jsp:param name="url" value="getAllUnifiedField" ></jsp:param>
							<jsp:param name="unifiedField" value="aggregationOutputField" ></jsp:param>
						</jsp:include>
	           		</div>
	           	</div>
	        </div>		
	        
	        <div class="col-md-12 no-padding">
	        	<spring:message code="aggregationService.service.definition.aggattribute.aggoutputfielddatatypefield.label" var="label"></spring:message>
	        	<spring:message code="aggregationService.service.definition.aggattribute.aggoutputfielddatatypefield.tooltip" var="tooltip"></spring:message>
	           	<div class="form-group">
	        		<div class="table-cell-label">${label}<span class="required">*</span></div>
	           		<div class="input-group col-md-6">		            
	            		<select id="aggOutputFieldDataTypeField" class="form-control table-cell input-sm" tabindex="3" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
		            		<c:forEach var='aggOutputFieldDataTypeEnum' items='${aggOutputFieldDataTypeEnum}' >
								<option id='${aggOutputFieldDataTypeEnum.displayValue}'>${aggOutputFieldDataTypeEnum.displayValue}</option>
							</c:forEach > 
						</select>
	           			<span class="input-group-addon add-on last" > <i id="aggregationAttributeFieldDataType_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
	           		</div>
	           	</div>
	        </div>	
	        
	         <div class="col-md-12 no-padding">
	        	<spring:message code="aggregationService.service.definition.aggattribute.operationexpression.label" var="label"></spring:message>
	        	<spring:message code="aggregationService.service.definition.aggattribute.operationexpression.tooltip" var="tooltip"></spring:message>
	           	<div class="form-group">
	        		<div class="table-cell-label">${label}<span class="required">*</span></div>
	           		<div class="input-group col-md-6">
	           			<select id="aggOperationExpression" class="form-control table-cell input-sm" tabindex="3" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
		            		<c:forEach var='aggOprExEnum' items='${aggOprExEnum}' >
								<option id='${aggOprExEnum.displayValue}' value='${aggOprExEnum.value}'>${aggOprExEnum.displayValue}</option>
							</c:forEach > 
						</select>	
	           			<span class="input-group-addon add-on last" > <i id="aggOperationExpression_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>	            
	           		</div>
	           	</div>
	        </div>
	        
		      <div id="add_edit_group_button_div" class="modal-footer padding10">
			       
			        <div id="view_aggAttribute_buttons_div" class="padding10" style="display:none;">
			           <sec:authorize access="hasAuthority('UPDATE_SERVICE_INSTANCE')">
			         		<button id="saveAggAttrBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="addAggAttributeDetails();"><spring:message code="btn.label.add"></spring:message></button>
			           </sec:authorize>	
			         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
			        </div>
			        <div id="view_group_buttons_div" class="modal-footer padding10" style="display:none;">
			         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
			        <div id="view_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
						<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
					</div>	
				</div>		
			<!-- Content end here -->
			</div>
    </div>
    <!-- /.modal-content --> 
</div>
</form:form>
<%--Add Aggregation Attribute pop-up code end here --%>





