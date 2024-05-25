<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page import="com.elitecore.sm.util.MapCache"%>

<style>
 .customLink:hover{
 	color:white;!important
 }
</style>
<script>
	var pathlist = [];
	var consolidationDefanitionList=[];
	jsSpringMsg.createConsolidationListAction = '<%=ControllerConstants.CREATE_CONSOLIDATION_DEFINITION_LIST%>';
	jsSpringMsg.updateConsolidationListAction = '<%=ControllerConstants.UPDATE_CONSOLIDATION_DEFINITION_LIST%>';
</script>
<script src="${pageContext.request.contextPath}/customJS/consolidationManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/customJS/autoSuggestionForUnifiedField.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<div class="tab-pane<c:if test="${(REQUEST_ACTION_TYPE eq 'CONSOLIDATION_DEFINITION')}"><c:out value="active"></c:out></c:if>" id="consolidationDefanitionListConfig">
 	<form:form modelAttribute="<%=FormBeanConstants.DATA_CONSOLIDATION_SERVICE_DEFINITION_FORM_BEAN %>" method="POST" action="createConsolidationDefinitionList" id="<%=FormBeanConstants.DATA_CONSOLIDATION_SERVICE_DEFINITION_FORM_BEAN %>">
		<div class="tab-content no-padding">
			<div class="fullwidth mtop10">
		   		<div class="title">
		   			<strong><spring:message code="consolidation.service.consoli.defi.list.label"></spring:message></strong>
		   			<span class="title2rightfield"> 
					    <span class="title2rightfield-icon1-text">
		   				<sec:authorize access="hasAuthority('UPDATE_SERVICE_INSTANCE')">	
		          			<a href="#" class="customLink" onclick="addConsolidationDefanitionList('','','','','100','ADD','','');">
		          				<i class="fa fa-plus-circle"></i>
		          			</a>
		          			<a href="#" class="customLink" id="addConsolidationDefanitionList" onclick="addConsolidationDefanitionList('','','','','100','ADD','','');">
		   						<spring:message code="btn.label.add" ></spring:message>
		   					</a>
		   				</sec:authorize>
		   				</span>
		   			</span>
		   		</div>
		   		<div class="clearfix"></div>
	        </div>
			<div class="fullwidth">
				<div id="consolidationDefinitionList"></div>
			</div>
		</div>
 	</form:form>
</div>

<%--Add grouping attribute pop-up code start here --%>
<a href="#divAddGroupingAttribute" class="fancybox" style="display: none;" id="add_grouping_attribute_link">#</a>
<form:form modelAttribute="con-grouping-attribute-form_bean" method="POST" action="createConsolidationDefinitionList" id="con-grouping-attribute-form">
<div id="divAddGroupingAttribute" style="width:100%; display: none;"  >
    <div class="modal-content">
    	<input type="hidden" id="pathlist_id"/>
    	<input type="hidden" id="current_block_count"/>
    	<input type="hidden" id="mappingId"/>
    	<input type="hidden" id="composerPluginId"/>
    	
        <div class="modal-header padding10">
            <h4 class="modal-title">
            	<span id="add_group_header_label" style="display:none;"><spring:message code="consolidation.service.consoli.defi.grouping.attribute.list.add" ></spring:message></span>
            	<span id="update_group_header_label" style="display:none;"> <spring:message code="consolidation.service.consoli.defi.grouping.attribute.list.update" ></spring:message></span>
	   		</h4>
        </div>
        <div class="modal-body padding10 inline-form">
        	<div>
        		<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
        	</div>
			
				<div class="col-md-12 no-padding">
		        	<spring:message code="consolidation.service.consoli.defi.grouping.attribute.field" var="label"></spring:message>
		        	<spring:message code="consolidation.service.consoli.defi.grouping.attribute.field.tooltip" var="tooltip"></spring:message>
		           	<div class="form-group">
		        		<div class="table-cell-label">${label}<span class="required">*</span></div>
		           			<jsp:include page="../../common/autocomplete.jsp">
	            				<jsp:param name="url" value="getAllUnifiedField" ></jsp:param>
								<jsp:param name="unifiedField" value="groupingField" ></jsp:param>
							</jsp:include>
		           	</div>
		        </div>
		          	
		        <div class="col-md-12 no-padding box box-warning">
					<div class="box-header with-border">
						<h3 class="box-title">
							<spring:message code="consolidation.service.consoli.defi.grouping.attribute.regex.apply"></spring:message>
						</h3>
						<div class="box-tools pull-right" style="top: -3px;">
							<button class="btn btn-box-tool" data-widget="collapse">
								<i class="fa fa-minus"></i>
							</button>
						</div>
					</div>
					<div class="box-body">
						<div class="fullwidth inline-form">		     		            
				            <div class="col-md-6 no-padding">
				           		<spring:message code="consolidation.service.consoli.defi.grouping.attribute.regex.enable" var="label"></spring:message>
				           		<spring:message code="consolidation.service.consoli.defi.grouping.attribute.regex.enable.tooltip" var="tooltip"></spring:message>
				            	<div class="form-group">
				         			<div class="table-cell-label">${label}</div>
				             		<div class="input-group">
					            		<select id="enableRegex" class="form-control table-cell input-sm" tabindex="3" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" onchange="handleRegexEnableDisabled();">
						            		<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
												<option value='${trueFalseEnum.name}'>${trueFalseEnum}</option>
											</c:forEach > 
										</select>				             			
										<span class="input-group-addon add-on last" > <i id="enableRegex_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
				             		</div>
				            	</div>
				            </div>
		
				            <div class="col-md-6 no-padding">
				           		<spring:message code="consolidation.service.consoli.defi.grouping.attribute.regex.expression" var="label"></spring:message>
				           		<spring:message code="consolidation.service.consoli.defi.grouping.attribute.regex.expression.tooltip" var="tooltip"></spring:message>
				            	<div class="form-group">
				         			<div class="table-cell-label">${label}</div>
				             		<div class="input-group">
				             			<input  id="regExExpression" class="form-control table-cell input-sm" tabindex="3" data-toggle="tooltip" data-placement="top" title="${tooltip}" />
				             			<span class="input-group-addon add-on last" > <i id="regExExpression_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
				             		</div>
				            	</div>
				            </div>
				            
				            <div class="col-md-6 no-padding">
				           		<spring:message code="consolidation.service.consoli.defi.grouping.attribute.regex.mapping" var="label"></spring:message>
				           		<spring:message code="consolidation.service.consoli.defi.grouping.attribute.regex.mapping.tooltip" var="tooltip"></spring:message>
				            	<div class="form-group">
				         			<div class="table-cell-label">${label}</div>
				             		<div class="input-group">
										<jsp:include page="../../common/autocomplete.jsp">
				            				<jsp:param name="url" value="getAllUnifiedField" ></jsp:param>
											<jsp:param name="unifiedField" value="destinationField" ></jsp:param>
										</jsp:include>
				             		</div>
				            	</div>
				            </div>
			           	</div>	
		            </div>
	            </div>		            
		        <div class="col-md-12 no-padding box box-warning">
					<div class="box-header with-border">
						<h3 class="box-title">
					    	<spring:message code="consolidation.service.consoli.defi.grouping.attribute.lookup.extraction"></spring:message>
						</h3>
						<div class="box-tools pull-right" style="top: -3px;">
							<button class="btn btn-box-tool" data-widget="collapse">
								<i class="fa fa-minus"></i>
							</button>
						</div>
					</div>
					<div class="box-body">
						<div class="fullwidth inline-form">	        
				            <div class="col-md-6 no-padding">
				           		<spring:message code="consolidation.service.consoli.defi.grouping.attribute.lookup.enable" var="label"></spring:message>
				           		<spring:message code="consolidation.service.consoli.defi.grouping.attribute.lookup.enable.tooltip" var="tooltip"></spring:message>
				            	<div class="form-group">
				         			<div class="table-cell-label">${label}</div>
				             		<div class="input-group">
					            		<select id="enableLookupDropdown" class="form-control table-cell input-sm" tabindex="3" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" onchange="handleLookupEnableDisabled();">
						            		<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
												<option value='${trueFalseEnum.name}'>${trueFalseEnum}</option>
											</c:forEach > 
										</select>	
										<span class="input-group-addon add-on last" > <i id="enableLookup_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
				             		</div>
				            	</div>
				            </div>
		
				            <div class="col-md-6 no-padding">
				           		<spring:message code="consolidation.service.consoli.defi.grouping.attribute.lookup.tname" var="label"></spring:message>
				           		<spring:message code="consolidation.service.consoli.defi.grouping.attribute.lookup.tname.tooltip" var="tooltip"></spring:message>
				            	<div class="form-group">
				         			<div class="table-cell-label">${label}</div>
				             		<div class="input-group">
				             			<input  id="lookUpTableName" class="form-control table-cell input-sm" tabindex="3" data-toggle="tooltip" data-placement="top" title="${tooltip}" />
				             			<span class="input-group-addon add-on last" > <i id="lookUpTableName_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
				             		</div>
				            	</div>
				            </div>
				            		            
				            <div class="col-md-6 no-padding">
				           		<spring:message code="consolidation.service.consoli.defi.grouping.attribute.lookup.cname" var="label"></spring:message>
				           		<spring:message code="consolidation.service.consoli.defi.grouping.attribute.lookup.cname.tooltip" var="tooltip"></spring:message>
				            	<div class="form-group">
				         			<div class="table-cell-label">${label}</div>
				             		<div class="input-group">
				             			<input  id="lookUpTableColumnName" class="form-control table-cell input-sm" tabindex="3" data-toggle="tooltip" data-placement="top" title="${tooltip}" />
				             			<span class="input-group-addon add-on last" > <i id="lookUpTableColumnName_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
				             		</div>
				            	</div>
				            </div>
				    	</div>
		            </div>
		          </div>			
		          
			      <div id="add_edit_group_button_div" class="modal-footer padding10">
				       
				         <div id="add_group_buttons_div" class="padding10" style="display:none;">
				           <sec:authorize access="hasAuthority('UPDATE_SERVICE_INSTANCE')">
				         		<button id="saveGorupAttrBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="addGroupingAttributeDetails();"><spring:message code="btn.label.save"></spring:message></button>
				           </sec:authorize>	
				         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
				        </div>
				        
				       
				        <div id="update_group_buttons_div" class="padding10" style="display:none;">
				         	 <sec:authorize access="hasAuthority('UPDATE_SERVICE_INSTANCE')">
				         		<button id="updateGroupFieldDetailBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="updateGroupingAttributeDetails();"><spring:message code="btn.label.update"></spring:message></button>
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

<%--Add consolidation attribute pop-up code start here --%>
<a href="#divAddConsolidationAttribute" class="fancybox" style="display: none;" id="add_consolidation_attribute_link">#</a>
<form:form modelAttribute="con-attribute-form_bean" method="POST" action="createConsolidationAttributeList" id="consolidation-attribute-form">
<div id="divAddConsolidationAttribute" style="width:100%; display: none;"  >
    <div class="modal-content">
    	<input type="hidden" id="pathlist_id1"/>
    	<input type="hidden" id="current_block_count1"/>
    	<input type="hidden" id="mappingId1"/>
    	<input type="hidden" id="composerPluginId1"/>
    	
    	
        <div class="modal-header padding10">
            <h4 class="modal-title">
            	<span id="add_cons_header_label" style="display:none;"><spring:message code="consolidation.service.consoli.defi.consolidation.attribute.list.add" ></spring:message></span>
            	<span id="update_cons_header_label" style="display:none;"> <spring:message code="consolidation.service.consoli.defi.consolidation.attribute.list.update" ></spring:message></span>
	   		</h4>
        </div>
        <div class="modal-body padding10 inline-form">
        	<div>
        		<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
        	</div>
			
				<div class="col-md-6 no-padding">
		        	<spring:message code="consolidation.service.consoli.defi.consolidation.attribute.field.name" var="label"></spring:message>
		        	<spring:message code="consolidation.service.consoli.defi.consolidation.attribute.field.name.tooltip" var="tooltip"></spring:message>
		           	<div class="form-group">
		        		<div class="table-cell-label">${label}</div>
		           			<jsp:include page="../../common/autocomplete.jsp">
	            				<jsp:param name="url" value="getAllUnifiedField" ></jsp:param>
								<jsp:param name="unifiedField" value="fieldName" ></jsp:param>
							</jsp:include>
		           	</div>
		        </div>
		          	
				<div class="col-md-6 no-padding">
		        	<spring:message code="consolidation.service.consoli.defi.consolidation.attribute.datatype" var="label"></spring:message>
		        	<spring:message code="consolidation.service.consoli.defi.consolidation.attribute.datatype.tooltip" var="tooltip"></spring:message>
		           	<div class="form-group">
		        		<div class="table-cell-label">${label}</div>
		           		<div class="input-group">		            
		            		<select id="consolidationAttributeDataTypeField" class="form-control table-cell input-sm" tabindex="2" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
 			            		<c:forEach var='dataType' items='${dataType}' >
									<option value='${dataType.value}'>${dataType}</option>
 								</c:forEach > 
							</select>
		           			<span class="input-group-addon add-on last" > <i id="consolidationAttributeDataTypeField_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		           		</div>
		           	</div>
		        </div>		          	
		   		<div class="col-md-6 no-padding">
		        	<spring:message code="consolidation.service.consoli.defi.consolidation.attribute.operation" var="label"></spring:message>
		        	<spring:message code="consolidation.service.consoli.defi.consolidation.attribute.operation.tooltip" var="tooltip"></spring:message>
		           	<div class="form-group">
		        		<div class="table-cell-label">${label}</div>
		           		<div class="input-group">		            
		            		<select id="consolidationAttributeOperationTypeField" class="form-control table-cell input-sm" tabindex="2" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
 			            		<c:forEach var='operationType' items='${operationType}' >
									<option value='${operationType.value}'>${operationType}</option>
								</c:forEach >  
							</select>
		           			<span class="input-group-addon add-on last" > <i id="consolidationAttributeOperationTypeField_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		           		</div>
		           	</div>
		        </div>
			    <div class="col-md-6 no-padding">
	           		<spring:message code="consolidation.service.consoli.defi.consolidation.attribute.description" var="label"></spring:message>
	           		<spring:message code="consolidation.service.consoli.defi.consolidation.attribute.description.tooltip" var="tooltip"></spring:message>
	            	<div class="form-group">
	         			<div class="table-cell-label">${label}</div>
	             		<div class="input-group">
	             			<input  id="consolidationLookUpTableColumnName" class="form-control table-cell input-sm" tabindex="3" data-toggle="tooltip" data-placement="top" title="${tooltip}" />
	             			<span class="input-group-addon add-on last" > <i id="consolidationLookUpTableColumnName_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
	             		</div>
		            	</div>
	            </div>
		          
			      <div id="add_edit_cons_button_div" class="modal-footer padding10">
				         <div id="add_cons_buttons_div" class="padding10" style="display:none;">
				           <sec:authorize access="hasAuthority('UPDATE_SERVICE_INSTANCE')">
				         		<button id="addConsolidationAttrBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="addConsolidationAttributeDetails();"><spring:message code="btn.label.save"></spring:message></button>
				           </sec:authorize>	
				         	<button type="button" class="btn btn-grey btn-xs" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
				        </div>
				        
				       
				        <div id="update_cons_buttons_div" class="padding10" style="display:none;">
				         	 <sec:authorize access="hasAuthority('UPDATE_SERVICE_INSTANCE')">
				         		<button id="updateConAttDetailBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="updateConsolidationAttributeDetails();"><spring:message code="btn.label.update"></spring:message></button>
				         	</sec:authorize>	
				         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
				        </div>
				        
				        <div id="view_cons_buttons_div" class="modal-footer padding10" style="display:none;">
				         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
				        </div>
				        <div id="view_progress-bar-div1" class="modal-footer padding10 text-left" style="display: none;">
							<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
						</div>	
					</div>		
			<%--Content end here --%>
			</div>
    </div>
    <!-- /.modal-content --> 
</div>
</form:form>
<!-- Pathlist Delete popup code start here -->
    	<div id="divMessage" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10" >
		           <h4 class="modal-title" id="status-title" ><spring:message code="consolidation.definition.delete.header"></spring:message></h4>
		        </div>
		        
		        <div class="modal-body padding10 inline-form">
		        	<span id="deleteDriverResponseMsg" style="display:none;">
		        		<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
		        	</span>
		        	<input type="hidden" id="deletepathListId" name="deletepathListId"/>
			        <p id="deleteWarningMessage">
			       		 <spring:message code="consolidation.definition.delete.warn.message"></spring:message>
			        </p>
		        </div>
		        
		       <!-- To Do add role here -->
			        <div id="delete-popup-buttons" class="modal-footer padding10">
		        		<button id="delConDefnBtn" type="button" class="btn btn-grey btn-xs " onclick="deleteConsolidationDefinationPerm();"><spring:message code="btn.label.yes"></spring:message></button>
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
			           <!-- <button type="button" class="btn btn-grey btn-xs " id="closeBtn" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button> --> 
		        	</div>
		        	<div id="delete-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
			</div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
    	<a href="#divMessage" class="fancybox" style="display: none;" id="pathlistMessage">#</a>
    	<!-- Pathlist Delete popup code end here -->
    	
    	
    	<div id="divMessageG" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10" >
		           <h4 class="modal-title" ><spring:message code="consolidation.definition.group.delete.header.label"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        	
		        <input type="hidden" id="deletepathListIdG" name="deletepathListIdG"/>
		        
			        <p id="deleteWarningMessageG">
			       		<spring:message code="consolidation.definition.group.delete.warn.message"></spring:message>
			        </p>
		        </div>
		        
		       <!-- To Do add role here -->
			        <div id="delete-popup-buttonsg" class="modal-footer padding10">
		        		<button id="delYesBtn" type="button" class="btn btn-grey btn-xs " onclick="deleteComposerPlugin();"><spring:message code="btn.label.yes"></spring:message></button>
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
			            <!--  <button type="button" class="btn btn-grey btn-xs " id="closeBtn" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button> -->
		        	</div>
		        	<div id="delete-progress-bar-divg" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
			</div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
    	<a href="#divMessageG" class="fancybox" style="display: none;" id="pathlistMessageG">#</a>
    	
    	<div id="divMessageA" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10" >
		           <h4 class="modal-title" ><spring:message code="consolidation.definition.attribute.delete.header.label"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        	
		        <input type="hidden" id="deletepathListIdA" name="deletepathListIdA"/>
		        
			        <p id="deleteWarningMessageA">
			       		<spring:message code="consolidation.definition.attribute.delete.warn.message"></spring:message>
			        </p>
		        </div>
		        
		       <!-- To Do add role here -->
			        <div id="delete-popup-buttonsa" class="modal-footer padding10">
		        		<button id="delConAttBtn" type="button" class="btn btn-grey btn-xs " onclick="deleteAttribute();"><spring:message code="btn.label.yes"></spring:message></button>
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
			          <!-- <button type="button" class="btn btn-grey btn-xs " id="closeBtn" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button> -->  
		        	</div>
		        	<div id="delete-progress-bar-divg" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
			</div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
    	<a href="#divMessageA" class="fancybox" style="display: none;" id="pathlistMessageA">#</a>
    	
    	
<script type="text/javascript">
var conDefListCnt = -1;
var consolidationAttributeListCounter = -1;
var charRenameOperationCounter = 0;
$(document).ready(function() {
	consolidationDefanitionList = eval('${consolidationDefanitionList}');
	if(consolidationDefanitionList != 'undefined' && consolidationDefanitionList != null  ){
		$.each(consolidationDefanitionList, function(index,consoli){
			addConsolidationDefanitionList(consoli.id,consoli.consName,consoli.dateFieldName,consoli.segregationType,consoli.acrossFilePartition,'UPDATE',consoli.consGrpAttList,consoli.consAttList);
		});	
	}
});
</script>

<script type="text/javascript">
 function addConsolidationDefanitionList(id,name,dateFieldName,segregationType,acrossFilePartition,actionType,consGrpAttList,consAttList){
	 conDefListCnt++;
	 var consolidationDefinitionListHtmlBody = "<div class='box box-warning' id='flipbox_"+conDefListCnt+"'>  "+
							"<input type='hidden' id='"+conDefListCnt+"_serviceId' value='${serviceId}' > "+
							"<input type='hidden' id='"+conDefListCnt+"_consListId' value='"+id+"'> "+ 
							"<div class='box-header with-border'> "+
							"	<h3 class='box-title' id='title_"+conDefListCnt+"'>"+name+" "+ 											
							"	</h3>  "+
							"	<div class='box-tools pull-right' style='top: -3px;' id='"+conDefListCnt+"_action'> "+ 
							"		<button class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' href='#"+conDefListCnt+"_block'> "+ 
							"			<i class='fa fa-minus'></i> "+
							"		</button> "+
							"					<sec:authorize access='hasAuthority(\'DELETE_SERVICE_INSTANCE\')'>"+
						    "						&nbsp;&nbsp;<a id='"+conDefListCnt+"_del' class='ion-ios-close-empty remove-block' style='margin-top:12px;' onclick=deleteConsolidationDefination(\'"+conDefListCnt+"\');></a>&nbsp;" +
						    "					</sec:authorize>"+
							"	</div> "+
							"</div> "+
							"<div class='box-body inline-form accordion-body collapsed in' id='"+conDefListCnt+"_block'> "+ 
							"	<div class='fullwidth inline-form'>  "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='consolidation.service.consoli.defi.name' var='label'></spring:message> "+ 
							"			<spring:message code='consolidation.service.consoli.defi.name.tooltip' var='tooltip'></spring:message> "+ 
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+conDefListCnt+"_consName' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+name+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='consolidation.service.consoli.defi.mappedfield' var='label'></spring:message> "+
							"			<spring:message code='consolidation.service.consoli.defi.mappedfield.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+conDefListCnt+"_dateFieldName' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+name+"'/>"+ 			
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='consolidation.service.consoli.defi.segragationtype' var='label'></spring:message> "+
							"			<spring:message code='consolidation.service.consoli.defi.segragationtype.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<select class='form-control table-cell input-sm' id='"+ conDefListCnt + "_segragationType' tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}'>"+
							"						<c:forEach var='segragationType' items='${segragationType}' >"+
							"								<option value='${segragationType}'>${segragationType}</option>"+
							"				    	</c:forEach>"+
							"					</select>"+
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='consolidation.pathlist.definition.file.partition.label' var='label'></spring:message> "+
							"			<spring:message code='consolidation.pathlist.definition.file.partition.label.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<input id='"+ conDefListCnt + "_acrossFilePartition' tabindex='4' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  onkeydown='isNumericOnKeyDown(event)' title='${tooltip}'/>"+
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-12 no-padding'>"+
						    "  			<div class='form-group'>"+
						    "      			<div id='" + conDefListCnt + "_buttons-div' class='input-group '>"+
							"					<sec:authorize access='hasAuthority(\'UPDATE_SERVICE_INSTANCE\')'>"+	
				    		"						<button type='button' class='btn btn-grey btn-xs ' id='" + conDefListCnt + "_savebtn' onclick=addConsolidationDefination(\'"+conDefListCnt+"\');><spring:message code='btn.label.save'></spring:message></button>&nbsp;" +
				    		"					</sec:authorize>"+
						    "					<sec:authorize access='hasAuthority(\'UPDATE_SERVICE_INSTANCE\')'>"+
							"						<button type='button' class='btn btn-grey btn-xs ' id='" + conDefListCnt + "_updatebtn' onclick=updateConsolidationDefination(\'"+conDefListCnt+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
							"					</sec:authorize>"+	
				            "					<button type='button' class='btn btn-grey btn-xs ' onclick=resetConsolidationDefinationParams(\'"+conDefListCnt+"\');><spring:message code='btn.label.reset'></spring:message></button>&nbsp;" +
						    "       		</div>"+
					        "				<div id='" + conDefListCnt + "_progress-bar-div' class='input-group' style='display: none;'> "+
					        "					<label>Processing Request &nbsp;&nbsp; </label> "+
					        "						<img src='img/processing-bar.gif'>"+
						    "				</div> "+
						    "   		</div>" +
						    "		</div>" +	
						    "		<div class='col-md-12 no-padding' id='"+conDefListCnt+"_group_grid_links_div'> "+ 
							"			<div class='title2'> "+
							"				<spring:message code='consolidation.service.consoli.defi.grouping.attribute.list' ></spring:message>"+ 
							"					<span class='title2rightfield'> "+
							"		          		<span class='title2rightfield-icon1-text'> "+
							"					<sec:authorize access='hasAuthority(\'UPDATE_SERVICE_INSTANCE\')'>"+	
							"		          			<a id='aaad' href='#' onclick=openAddGroupingAttributePopup('"+conDefListCnt+"');><i class='fa fa-plus-circle'></i></a> "+
							"	  						<a id='dssds' href='#'  onclick=openAddGroupingAttributePopup('"+conDefListCnt+"');> "+
							"	  							<spring:message code='btn.label.add' ></spring:message>  "+
							"	  						</a> "+
							"					</sec:authorize>"+
							"	      				</span>	  "+
							"		          		<span class='title2rightfield-icon1-text'> "+
							"					<sec:authorize access='hasAuthority(\'DELETE_SERVICE_INSTANCE\')'>"+	
							"		          			<a href='#' onclick=displayDeletePluginPopup('"+conDefListCnt+"');> <i class='fa fa-trash'></i></a> "+
							"		          			<a href='#' id='deleteComposerPlugin' onclick=displayDeletePluginPopup('"+conDefListCnt+"');><spring:message code='btn.label.delete' ></spring:message></a> "+
							"					</sec:authorize>"+
							"		          		</span> "+
							"		          	</span> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-12 no-padding' id='"+conDefListCnt+"_group_grid_main_div'> "+ 
							"			<div class='box-body table-responsive no-padding box'> "+
							"            	<table class='table table-hover' id='"+conDefListCnt+"_group_grid'></table> "+
							"               	<div id='"+conDefListCnt+"_group_gridPagingDiv'></div> "+
							"	           	<div class='clearfix'></div> "+
							"	           	<div id='divLoading' align='center' style='display: none;'><img src='img/preloaders/Preloader_10.gif' /></div> "+  
							"           </div> "+
							"		</div> "+
						    "		<div class='col-md-12 no-padding' id='"+conDefListCnt+"_cons_grid_links_div'> "+ 
							"			<div class='title2'> "+
							"				<spring:message code='consolidation.service.consoli.defi.consolidation.attribute.list' ></spring:message>"+ 
							"					<span class='title2rightfield'> "+
							"		          		<span class='title2rightfield-icon1-text'> "+
							"					<sec:authorize access='hasAuthority(\'UPDATE_SERVICE_INSTANCE\')'>"+	
							"		          			<a href='#' onclick=openAddConsolidationAttributePopup('"+conDefListCnt+"');><i class='fa fa-plus-circle'></i></a> "+
							"	  						<a href='#' id='dcca' onclick=openAddConsolidationAttributePopup('"+conDefListCnt+"');> "+
							"	  							<spring:message code='btn.label.add' ></spring:message>  "+
							"	  						</a> "+
							"					</sec:authorize>"+
							"	      				</span>	  "+
							"		          		<span class='title2rightfield-icon1-text'> "+
							"					<sec:authorize access='hasAuthority(\'DELETE_SERVICE_INSTANCE\')'>"+	
							"		          			<a href='#' onclick=displayDeleteAttributePopup('"+conDefListCnt+"');> <i class='fa fa-trash'></i></a> "+
							"		          			<a href='#' id='deleteComposerPluginA' onclick=displayDeleteAttributePopup('"+conDefListCnt+"');><spring:message code='btn.label.delete' ></spring:message></a> "+
							"					</sec:authorize>"+
							"		          		</span> "+
							"		          	</span> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-12 no-padding' id='"+conDefListCnt+"_cons_grid_main_div'> "+ 
							"			<div class='box-body table-responsive no-padding box'> "+
							"            	<table class='table table-hover' id='"+conDefListCnt+"_cons_grid'></table> "+
							"               	<div id='"+conDefListCnt+"_cons_gridPagingDiv'></div> "+
							"	           	<div class='clearfix'></div> "+
							"	           	<div id='divLoading' align='center' style='display: none;'><img src='img/preloaders/Preloader_10.gif' /></div> "+  
							"           </div> "+
							"		</div> "+
							"	</div> "+
							"</div> "+
							"</div> ";
							
					$('#consolidationDefinitionList').append(consolidationDefinitionListHtmlBody);
					$("#"+conDefListCnt+"_savebtn").hide();
					$("#"+conDefListCnt+"_updatebtn").hide();
					
					var dateUnifiedField = conDefListCnt + '_dateFieldName'; 
					loadUnfiedAutoComplete( dateUnifiedField, 'getAllUnifiedField' );
					
					if(actionType == 'UPDATE'){
						
						if(dateFieldName){
							$("#"+ conDefListCnt+"_dateFieldName").val(dateFieldName.toString());
						}else{
							$("#"+ conDefListCnt+"_dateFieldName").val('');
						}
						$("#"+ conDefListCnt+"_segragationType").val(segregationType.toString());
						$("#"+ conDefListCnt+"_acrossFilePartition").val(acrossFilePartition);
						loadGroupingAttributeGrid(conDefListCnt,consGrpAttList);
						loadConsolidationAttributeGrid(conDefListCnt,consAttList);
						$("#"+conDefListCnt+"_updatebtn").show();
						$("#"+conDefListCnt+"_group_grid_main_div").show();
						$("#"+conDefListCnt+"_group_grid_links_div").show();
						$("#"+conDefListCnt+"_cons_grid_main_div").show();
						$("#"+conDefListCnt+"_cons_grid_links_div").show();
						$("#title_"+conDefListCnt).html(name);
						$("#"+conDefListCnt+"_block").collapse("toggle");
					}else{
						$("#"+conDefListCnt+"_savebtn").show();
						$("#"+conDefListCnt+"_group_grid_main_div").hide();
						$("#"+conDefListCnt+"_group_grid_links_div").hide();
						$("#"+conDefListCnt+"_cons_grid_main_div").hide();
						$("#"+conDefListCnt+"_cons_grid_links_div").hide();
						$("#title_"+conDefListCnt).html("<spring:message code='consolidation.service.consoli.defi.detail' ></spring:message>");
						loadGroupingAttributeGrid(conDefListCnt,[]);
						loadConsolidationAttributeGrid(conDefListCnt,[]);
					}
					
 }
</script>
