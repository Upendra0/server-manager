<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page import="com.elitecore.sm.util.MapCache"%>

<style>
 .customLink:hover{
 	color:white;!important
 }
</style>
<script src="${pageContext.request.contextPath}/customJS/consolidationManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/customJS/autoSuggestionForUnifiedField.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<script>
	var pathlist = [];
</script>
<jsp:include page="../collection/ftp/deviceDialog.jsp"></jsp:include>
<div class="tab-pane<c:if test="${(REQUEST_ACTION_TYPE eq 'CONSOLIDATION_SOURCE_PATH_MAPPING')}"><c:out value="active"></c:out></c:if>" id="pathListConfig">
	<div class="tab-content no-padding">
		<div class="fullwidth mtop10">
	   		<div class="title">
	   			<strong><spring:message code="consolidation.pathlist.tab.header"></spring:message></strong>
	   			<span class="title2rightfield"> 
				    <span class="title2rightfield-icon1-text">
	   				<sec:authorize access="hasAuthority('CREATE_PATHLIST')">	
	   					<a href="#" class="customLink" onclick="addPathlistAndConsolidationDetails('','','','','','','','','','','ADD');"><i class="fa fa-plus-circle"></i></a>
	          			<a href="#" class="customLink" id="addPathList" onclick="addPathlistAndConsolidationDetails('','','','','','','','','','','ADD');">
	   					<spring:message code="consolidation.pathlist.add.sourcepathmapping" ></spring:message></a>
	   				</sec:authorize>
	   				</span> 
	   			</span> 
	   		</div>
	   		<div class="clearfix"></div>
        </div>
		<div class="fullwidth">
			<div id="pathList"></div>
		</div>
	</div>
</div>

<a href="#divAddConsolidationGrid" class="fancybox" style="display: none;" id="add_consolidation_grid_link">#</a>
<form:form modelAttribute="consolidation_mapping_form_bean" method="POST" action="javascript:;" id="composer-plugin-form">
<div id="divAddConsolidationGrid" style="width:100%; display:none;"  >
    <div class="modal-content">
    	<input type="hidden" id="pathlist_id"/>
    	<input type="hidden" id="current_block_count"/>
    	<input type="hidden" id="mappingId"/>
    	<input type="hidden" id="mappingAction"/>
    	
        <div class="modal-header padding10">
            <h4 class="modal-title">
            	<span id="add_consolidation_pathlist_header_label" style="display:none;"><spring:message code="consolidation.pathlist.definition.add.header.label" ></spring:message></span>
            	<span id="update_consolidation_pathlist_header_label" style="display:none;"> <spring:message code="consolidation.pathlist.definition.update.header.label" ></spring:message></span>
	   		</h4>
        </div>
        
        <div class="modal-body padding10 inline-form">
        	<div>
        		<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
        	</div>
			
		            <div class="col-md-6 no-padding">
		           		<spring:message code="consolidation.pathlist.definition.consolidation.name.label" var="label"></spring:message>
		           		<spring:message code="consolidation.pathlist.definition.consolidation.name.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}<span class="required">*</span></div>
		             		<div class="input-group">
		             			<select id="mappingName" class="form-control table-cell input-sm" tabindex="2" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
			             			<c:forEach var='definitionList' items='${definitionList}' >
										<option value='${definitionList}'>${definitionList}</option>
									</c:forEach >
								</select>
		             			<!-- <input  id="mappingName" class="form-control table-cell input-sm" tabindex="5" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" />  -->
		             			<span class="input-group-addon add-on last" > <i id="mappingName_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		           
		            <div class="col-md-6 no-padding">
		           		<spring:message code="consolidation.pathlist.definition.write.path.label" var="label"></spring:message>
		           		<spring:message code="consolidation.pathlist.definition.write.path.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}<span class="required">*</span></div>
		             		<div class="input-group">
		             			<input  id="destPath" class="form-control table-cell input-sm" tabindex="5" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" />
		             			<span class="input-group-addon add-on last" > <i id="destPath_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		        
		            <div class="col-md-6 no-padding">
		           		<spring:message code="consolidation.pathlist.definition.condition.list.label" var="label"></spring:message>
		           		<spring:message code="consolidation.pathlist.definition.condition.list.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		             		<div class="input-group">
		             			<input  id="conditionList" class="form-control table-cell input-sm" tabindex="5" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" />
		             			<span class="input-group-addon add-on last" > <i id="conditionList_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		            
		            <div class="col-md-6 no-padding">
		           		<spring:message code="consolidation.pathlist.definition.logical.operator.label" var="label"></spring:message>
		           		<spring:message code="consolidation.pathlist.definition.logical.operator.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		             		<div class="input-group">
		             			<select id="logicalOperator" class="form-control table-cell input-sm" tabindex="2" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
		             			<c:forEach var='logicalOperator' items='${logicalOperator}' >
									<option value='${logicalOperator.value}'>${logicalOperator}</option>
								</c:forEach > 
								</select>
		             			<span class="input-group-addon add-on last" > <i id="logicalOperator_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		            
		            <div class="col-md-6 no-padding">
		           		<spring:message code="consolidation.pathlist.definition.process.record.limit.label" var="label"></spring:message>
		           		<spring:message code="consolidation.pathlist.definition.process.record.limit.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		             		<div class="input-group">
		             			<input  id="processRecordLimit" class="form-control table-cell input-sm" tabindex="5" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" />
		             			<span class="input-group-addon add-on last" > <i id="processRecordLimit_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		            

		            <div class="col-md-6 no-padding">
		           		<spring:message code="consolidation.pathlist.definition.compress.mode.write.label" var="label"></spring:message>
		           		<spring:message code="consolidation.pathlist.definition.compress.mode.write.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		             		<div class="input-group">
		             			<select id="compressModeWrite" class="form-control table-cell input-sm" tabindex="2" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
		             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
									<option value='${trueFalseEnum.name}'>${trueFalseEnum}</option>
								</c:forEach > 
								</select> 
		             			<span class="input-group-addon add-on last" > <i id="compressModeWrite_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		            
		            <div class="col-md-6 no-padding">
		           		<spring:message code="consolidation.pathlist.definition.write.configured.field.label" var="label"></spring:message>
		           		<spring:message code="consolidation.pathlist.definition.write.configured.field.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		             		<div class="input-group">
		             			<select id="writeConfField" class="form-control table-cell input-sm" tabindex="2" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
		             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
									<option value='${trueFalseEnum.name}'>${trueFalseEnum}</option>
								</c:forEach > 
								</select> 
		             			<span class="input-group-addon add-on last" > <i id="writeConfField_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		            
		            
		            <div class="col-md-6 no-padding">
		           		<spring:message code="consolidation.pathlist.definition.field.for.count.label" var="label"></spring:message>
		           		<spring:message code="consolidation.pathlist.definition.field.for.count.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}<span class="required">*</span></div>
		             		<div class="input-group">	
								<jsp:include page="../../common/autocomplete.jsp">
	            					<jsp:param name="url" value="getAllUnifiedField" ></jsp:param>
									<jsp:param name="unifiedField" value="fieldNameForCount" ></jsp:param>
								</jsp:include>
		             		</div>
		            	</div>
		            </div>
		            
	 	            <div class="col-md-6 no-padding">
		           		<spring:message code="consolidation.pathlist.definition.sorting.type.label" var="label"></spring:message>
		           		<spring:message code="consolidation.pathlist.definition.sorting.type.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		             		<div class="input-group">
		             			<select id="sortingType" class="form-control table-cell input-sm" tabindex="2" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
		             			<c:forEach var='sortingTypeEnum' items='${sortingTypeEnum}' >
									<option value='${sortingTypeEnum.value}' selected>${sortingTypeEnum}</option>
								</c:forEach > 
								</select> 
		             			<span class="input-group-addon add-on last" > <i id="sortingType_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div> 
		            
		            <div class="col-md-6 no-padding">
		           		<spring:message code="consolidation.pathlist.definition.sorting.field.label" var="label"></spring:message>
		           		<spring:message code="consolidation.pathlist.definition.sorting.field.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		         			<div class="input-group">
		             			<jsp:include page="../../common/autocomplete.jsp">
	            					<jsp:param name="url" value="getAllUnifiedField" ></jsp:param>
									<jsp:param name="unifiedField" value="recordSortingField" ></jsp:param>
								</jsp:include>
			               </div>
		            	</div>
		            </div>
		            
		             <div class="col-md-6 no-padding">
		           		<spring:message code="consolidation.pathlist.definition.sorting.field.type.label" var="label"></spring:message>
		           		<spring:message code="consolidation.pathlist.definition.sorting.field.type.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		             		<div class="input-group">
		             			<select id="sortingFieldType" class="form-control table-cell input-sm" tabindex="2" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
		             			<c:forEach var='sortingRecordFieldType' items='${sortingRecordFieldType}' >
									<option value='${sortingRecordFieldType.value}' >${sortingRecordFieldType}</option>
								</c:forEach > 
								</select> 
		             			<span class="input-group-addon add-on last" > <i id="sortingFieldType_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div> 
					<div class="clearfix"></div>		            
		       	 	<div class="no-padding inline-form">
			           	<spring:message code="consolidation.pathlist.definition.file.name.convention.label" var="label"></spring:message>
			           	<div class="table-cell-label"><strong>${label}</strong></div>
			          	<div class="clearfix"></div>
			          	<div><hr /></div>
			            <div class="col-md-6 no-padding">
			           		<spring:message code="consolidation.pathlist.definition.file.name.label" var="label"></spring:message>
			           		<spring:message code="consolidation.pathlist.definition.file.name.label.tooltip" var="tooltip"></spring:message>
			            	<div class="form-group">
			         			<div class="table-cell-label">${label}</div>
			             		<div class="input-group">
			             			<input  id="fileName" class="form-control table-cell input-sm" tabindex="5" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" />
			             			<span class="input-group-addon add-on last" > <i id="fileName_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
			             		</div>
			            	</div>
			            </div>
			            
			            <div class="col-md-6 no-padding">
			           		<spring:message code="consolidation.pathlist.definition.file.sequence.label" var="label"></spring:message>
			           		<spring:message code="consolidation.pathlist.definition.file.sequence.label.tooltip" var="tooltip"></spring:message>
			            	<div class="form-group">
			         			<div class="table-cell-label">${label}</div>
			             		<div class="input-group">
			             			<select id="fileSequence" class="form-control table-cell input-sm" tabindex="2" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" onchange="toggleFileRange();">
			             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
										<option value='${trueFalseEnum.name}' >${trueFalseEnum}</option>
									</c:forEach > 
									</select> 
			             			<span class="input-group-addon add-on last" > <i id="fileSequence_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
			             		</div>
			            	</div>
			            </div>
			            
			            <div class="col-md-6 no-padding">
			           		<spring:message code="consolidation.pathlist.definition.file.sequence.minrange.label" var="label"></spring:message>
			           		<spring:message code="consolidation.pathlist.definition.file.sequence.minrange.label.tooltip" var="tooltip"></spring:message>
			            	<div class="form-group">
			         			<div class="table-cell-label">${label}</div>
			             		<div class="input-group">
			             			<input  id="minSeqRange" class="form-control table-cell input-sm" tabindex="5" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" />
			             			<span class="input-group-addon add-on last" > <i id="minSeqRange_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
			             		</div>
			            	</div>
			            </div>
			            
			            <div class="col-md-6 no-padding">
			           		<spring:message code="consolidation.pathlist.definition.file.sequence.maxrange.label" var="label"></spring:message>
			           		<spring:message code="consolidation.pathlist.definition.file.sequence.maxrange.label.tooltip" var="tooltip"></spring:message>
			            	<div class="form-group">
			         			<div class="table-cell-label">${label}</div>
			             		<div class="input-group">
			             			<input  id="maxSeqRange" class="form-control table-cell input-sm" tabindex="5" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" />
			             			<span class="input-group-addon add-on last" > <i id="maxSeqRange_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
			             		</div>
			            	</div>
			            </div>
		        	</div>
		             
		             <div class="clearfix"></div>
			         <div id="add_edit_mapping_button_div" class="modal-footer padding10">
				         <div id="add_mapping_buttons_div" class="padding10" style="display:none;">
				         		<button id="addConsDefnDetail" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="addUpdateConsolidationDefinitionDetails();"><spring:message code="btn.label.save"></spring:message></button>
				         	    <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
				         </div>
				        <div id="update_mapping_buttons_div" class="padding10" style="display:none;">
				         	    <button id="updateConsDefnDetail" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="addUpdateConsolidationDefinitionDetails();"><spring:message code="btn.label.update"></spring:message></button>
				         	    <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
				        </div>
				        <div id="close_button_div" class="modal-footer padding10" style="display:none;">
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
 
 
     	<div id="divMessagedeletepathlist" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10" >
		           <h4 class="modal-title" id="status-title" ><spring:message code="ftp.driver.pathlist.delete.header.label"></spring:message></h4>
		        </div>
		        
		        <div class="modal-body padding10 inline-form">
		        	<span id="deleteDriverResponseMsg" style="display:none;">
		        		<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
		        	</span>
			        <p id="deleteWarningMessage">
			       		 <spring:message code="pahtlist.delete.warn.message"></spring:message>
			        </p>
		        </div>
		        
		       <!-- To Do add role here -->
		       <sec:authorize access="hasAuthority('DELETE_PATHLIST')">
			        <div id="inactive-driver-div" class="modal-footer padding10">
			            <button id="delConPathBtn" type="button" class="btn btn-grey btn-xs " onclick="deleteConsolidationPathList();"><spring:message code="btn.label.yes"></spring:message></button>
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
			        </div>
			     </sec:authorize>   
			         <div class="modal-footer padding10" id="reaload-driver-details" style="display:none;">
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox()();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
    	<a href="#divMessagedeletepathlist" class="fancybox" style="display: none;" id="pathlist_delete_link">#</a>
    	<!-- Pathlist Delete popup code end here -->
		<!-- ConsolidationMapping delete popup code start here  -->
		<a href="#divDeletemPopup" class="fancybox" style="display: none;" id="delete_consolidationmapping_link">#</a>
		<div id="divDeletemPopup" style="width:100%; display: none;" >
		    <div class="modal-content">
		    	<input type="hidden" id="deleteBlockId"/>
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="consolidation.mapping.delete.label"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        	<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
		        	
		        	<span id="warning_msg" style="display: none;"><spring:message code="consolidation.mapping.delete.warning.message"></spring:message></span>			
					<span id="delete_consolidationmapping_note" style="display: none;"><spring:message code="consolidation.mapping.delete.warn.message"></spring:message></span>
		        	
		        </div>
		        <sec:authorize access="hasAuthority('DELETE_PATHLIST')">
			        <div id="delete_buttons-div" class="modal-footer padding10">
			         	<button type="button" id="btndelete" class="btn btn-grey btn-xs " data-dismiss="modal" style="display: none;" onclick="deleteConsolidationMapping();"><spring:message code="btn.label.delete"></spring:message></button>
			         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
		        </sec:authorize>
		        <div id="delete_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
				</div>	
		        
		    </div>
		    <!-- /.modal-content --> 
		</div>
		
		
<script type="text/javascript">
var pathListCounter = -1;
var charRenameOperationCounter = 0;
 $(document).ready(function() {
	pathlist = eval('${pathList}');
	if(pathlist != 'undefined' && pathlist != null  ){
		$.each(pathlist, function(index,path){
			addPathlistAndConsolidationDetails(path.id, path.name, path.compressedInput, path.readPath, path.readFilePrefix, path.readFileSuffix, path.maxCountAlert, path.consolidationMappingList, path.deviceName, path.deviceId, 'UPDATE',path.pathId);
		});	
	}
	
}); 
</script> 

<script type="text/javascript">
function addPathlistAndConsolidationDetails(id, name, compressedInput, readPath, readFilePrefix, readFileSuffix, maxCountAlert, consolidationMappingList, parentDevice, parentDeviceId, actionType,pathId){
	 pathListCounter++;

	 if(String(readFilePrefix) === 'null' || String(readFilePrefix) === 'undefined'){
		 readFilePrefix = '';
	 }
	
	 if(String(readFileSuffix) === 'null' || String(readFileSuffix) === 'undefined'){
		 readFileSuffix = '';
	 }
	
	 
	 var pahtlistHtmlBody = "<div class='box box-warning' id='flipbox_"+pathListCounter+"'>  "+
							"<input type='hidden' id='"+pathListCounter+"_pluginId' > "+
							"<input type='hidden' id='"+pathListCounter+"_pathId' value='"+pathId+"'> "+
							"<input type='hidden' id='"+pathListCounter+"_driverId' value='${driverId}' > "+
							"<input type='hidden' id='"+pathListCounter+"_pathListId' value='"+id+"'> "+ 
							"<div class='box-header with-border'> "+
							"	<h3 class='box-title' id='title_"+pathListCounter+"'>"+name+" "+ 											
							"	</h3>  "+
							"	<div class='box-tools pull-right' style='top: -3px;' id='"+pathListCounter+"_action'> "+ 
							"		<button class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' href='#"+pathListCounter+"_block'> "+ 
							"			<i class='fa fa-minus'></i> "+
							"		</button> "+
							"					<sec:authorize access='hasAuthority(\'DELETE_PATHLIST\')'>"+
						    "			&nbsp;&nbsp;<a id='"+pathListCounter+"_delPath'class='ion-ios-close-empty remove-block' style='margin-top:12px;' onclick=deletePathListDetails(\'"+pathListCounter+"\');></a>&nbsp;" +
						    "					</sec:authorize>"+
							"	</div> "+
							"</div> "+
							"<div class='box-body inline-form accordion-body collapsed in' id='"+pathListCounter+"_block'> "+ 
							"	<div class='fullwidth inline-form'>  "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='consolidation.pathlist.source.path.name.label' var='label'></spring:message> "+ 
							"			<spring:message code='consolidation.pathlist.source.path.name.label.tooltip' var='tooltip'></spring:message> "+ 
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_name' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+name+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='consolidation.pathlist.compressed.label' var='label'></spring:message> "+
							"			<spring:message code='distribution.service.pathlist.file.read.mode.label.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<select class='form-control table-cell input-sm' id='" + pathListCounter + "_compressedInput' tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
						    "				    	<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >"+
							"							<option value='${trueFalseEnum.name}'>${trueFalseEnum}</option>"+
							"				    	</c:forEach>"+
							"					</select>"+
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='consolidation.pathlist.source.path.readpath.label' var='label'></spring:message> "+
							"			<spring:message code='consolidation.pathlist.source.path.readpath.label.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_readFilePath' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readPath+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='consolidation.pathlist.file.prefix.r' var='label'></spring:message> "+
							"			<spring:message code='consolidation.pathlist.file.prefix.r.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_readFilenamePrefix' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readFilePrefix+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+ 
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='consolidation.pathlist.file.suffix.r' var='label'></spring:message> "+
							"			<spring:message code='consolidation.pathlist.file.suffix.r.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_readFilenameSuffix' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readFileSuffix+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='consolidation.pathlist.max.file.cnt.alert' var='label'></spring:message> "+
							"			<spring:message code='consolidation.pathlist.max.file.cnt.alert.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_maxCounterLimit' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' 	onkeydown='isNumericOnKeyDown(event)'  value='"+maxCountAlert+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							
							// 	Device Name
							"	<div class='col-md-6 no-padding'> "+
							"		<spring:message code='device.list.grid.device.name.label' var='label'></spring:message> "+ 
							"		<spring:message code='device.list.grid.device.name.label' var='tooltip'></spring:message> "+ 
							"		<div class='form-group'>  "+
							"			<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
							"			<div class='input-group '> "+
							"				<input type='hidden' class='form-control table-cell input-sm' id='"+pathListCounter+"_deviceId'  data-toggle='tooltip' data-placement='bottom' value='"+parentDeviceId+"' title='${tooltip }' />"+
							"          		<a href='#' onclick='selectDevicePopUp("+pathListCounter+");' tabindex='9' style='vertical-align:middle;' id='select_popup_"+ pathListCounter +"_lnk'><i style='margin-top: 8px;margin-left: 8px;' class='glyphicon glyphicon-search'></i></a>" +
							"				<input type='text' value='"+parentDevice+"' class='form-control table-cell input-sm' id='"+pathListCounter+"_parentDevice'  data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' style='width:94%;' readonly=true />"+
							"				<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							"			</div> "+
							"		</div> "+
							"	</div> "+
							
						 	"       <div class='clearfix'></div>  "+
							"		<div class='col-md-12 no-padding'>"+
						    "  			<div class='form-group'>"+
						    "      			<div id='" + pathListCounter + "_buttons-div' class='input-group '>"+
							"					<sec:authorize access='hasAuthority(\'CREATE_PATHLIST\')'>"+	
				    		"						<button type='button' class='btn btn-grey btn-xs ' id='" + pathListCounter + "_savebtn' onclick=createConsolidationPathlist(\'"+pathListCounter+"\');><spring:message code='btn.label.save'></spring:message></button>&nbsp;" +
				    		"					</sec:authorize>"+
						    "					<sec:authorize access='hasAuthority(\'UPDATE_PATHLIST\')'>"+
							"						<button type='button' class='btn btn-grey btn-xs ' id='" + pathListCounter + "_updatebtn' onclick=updateConsolidationPathList(\'"+pathListCounter+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
							"					</sec:authorize>"+	
				            "					<button type='button' class='btn btn-grey btn-xs ' onclick=resetPathlistParams(\'"+pathListCounter+"\');><spring:message code='btn.label.reset'></spring:message></button>&nbsp;" +
						    "       		</div>"+
					        "				<div id='" + pathListCounter + "_progress-bar-div' class='input-group' style='display: none;'> "+
					        "					<label>Processing Request &nbsp;&nbsp; </label> "+
					        "						<img src='img/processing-bar.gif'>"+
						    "				</div> "+
						    "   		</div>" +
						    "		</div>" +	
						    "		<div class='col-md-12 no-padding' id='"+pathListCounter+"_grid_links_div'> "+ 
							"			<div class='title2'> "+
							"				<spring:message code='consolidation.pathlist.definition.grid.header' ></spring:message>"+ 
							"					<span class='title2rightfield'> "+
							"		          		<span class='title2rightfield-icon1-text'> "+
							"					<sec:authorize access='hasAuthority(\'CREATE_PATHLIST\')'>"+	
							"		          			<a href='#' onclick=openAddEditConsolidationMappingPopup('ADD','"+-1+"','"+pathListCounter+"');><i class='fa fa-plus-circle'></i></a> "+
							"	  						<a href='#' id='addConsolidationMapping' onclick=openAddEditConsolidationMappingPopup('ADD','"+-1+"','"+pathListCounter+"');> "+
							"	  							<spring:message code='btn.label.add' ></spring:message>  "+
							"	  						</a> "+
							"					</sec:authorize>"+
							"	      				</span>	  "+
							"		          		<span class='title2rightfield-icon1-text'> "+
							"					<sec:authorize access='hasAuthority(\'DELETE_PATHLIST\')'>"+	
							"		          			<a href='#' onclick=displayDeleteConsolidationPopup('"+pathListCounter+"');> <i class='fa fa-trash'></i></a> "+
							"		          			<a href='#' id='deleteConsolidationMapping' onclick=displayDeleteConsolidationPopup('"+pathListCounter+"');><spring:message code='btn.label.delete' ></spring:message></a> "+
							"					</sec:authorize>"+
							"		          		</span> "+
							"		          	</span> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-12 no-padding' id='"+pathListCounter+"_grid_main_div'> "+ 
							"			<div class='box-body table-responsive no-padding box'> "+
							"            	<table class='table table-hover' id='"+pathListCounter+"_grid'></table> "+
							"               	<div id='"+pathListCounter+"_gridPagingDiv'></div> "+
							"	           	<div class='clearfix'></div> "+
							"	           	<div id='divLoading' align='center' style='display: none;'><img src='img/preloaders/Preloader_10.gif' /></div> "+  
							"           </div> "+
							"		</div> "+
							"	</div> "+ 
							"</div> "+
							"</div> ";
							
					$('#pathList').prepend(pahtlistHtmlBody);
					
					$("#"+pathListCounter+"_savebtn").hide();
					$("#"+pathListCounter+"_updatebtn").hide();
					if(actionType == 'UPDATE'){
						$("#"+ pathListCounter+"_compressedInput").val(compressedInput.toString());
						if(!consolidationMappingList || consolidationMappingList.legnth == 0){
							consolidationMappingList = [];
						}
						loadConsolidationGrid(pathListCounter, consolidationMappingList);
						$("#"+pathListCounter+"_updatebtn").show();
						$("#"+pathListCounter+"_grid_main_div").show();
						$("#"+pathListCounter+"_grid_links_div").show();
						$("#title_"+pathListCounter).html(name);
						$("#"+pathListCounter+"_block").collapse("toggle");
					}else{
						$("#"+pathListCounter+"_savebtn").show();
						$("#"+pathListCounter+"_grid_main_div").hide();
						$("#"+pathListCounter+"_grid_links_div").hide();
						$("#"+pathListCounter+"_maxCounterLimit").val('${pathlist_form_bean.maxCountAlert}');
						//$("#"+ pathListCounter+"_compressInFileEnabled").val('${pathlist_form_bean.compressInFileEnabled}');
						$("#title_"+pathListCounter).html("<spring:message code='consolidation.pathlist.block.header.label' ></spring:message>");
					}
					
 }
 
 
function loadConsolidationGrid(counter, consolidationMappingList) {
	$.each(consolidationMappingList,function(index,consolidationMapping){
		consolidationMapping.edit = '<a href="#" id="editConsolidationMapping_'+consolidationMapping.mappingName+'" onclick="openAddEditConsolidationMappingPopup(\'UPDATE\',' + parseInt(consolidationMapping.id) + ',' + counter  + ')"><i class="fa fa-pencil-square-o" aria-hidden="true" id="img_'+parseInt(consolidationMapping.id)+'" data-toggle="tooltip" data-placement="bottom" style="cursor:pointer;"></i></a>';
	});
	
	var uniqueGridId = counter+"_grid";
	var selectAllCheckboxId = counter+"_selectAllConsolidationMapping";
	var childCheckboxName = counter+"_consolidationMappingCheckbox";
	
	$("#" + counter + "_grid")
	.jqGrid({
				url : "",
				datatype : "local",
				colNames : [
							"id",
							"<input type='checkbox' id='"+selectAllCheckboxId+"' onclick='consolidationMappingHeaderCheckbox(event, this, \""+childCheckboxName+"\")'></input>",
				            jsSpringMsg.consolidationName,
						    jsSpringMsg.writePath,
						    jsSpringMsg.conditionList,
						    jsSpringMsg.recordSorting,
						    jsSpringMsg.filenameConvention,
						    jsSpringMsg.edit,
						    'logicalOperator',
						    'processRecordLimit',
						    'compressedOutput',
						    'writeOnlyConfiguredAttribute',
						    'fieldNameForCount',
						    'recordSortingField',
						    'recordSortingFieldType',
						    'fileSequence',
						    'minSeqRange',
						    'maxSeqRange'
			   ],
			   colModel: [	{name:'id',index:'id',hidden:true,sortable:false},
			              	{name:'checkbox',index:'checkbox',sortable:false,align:'center', width : '30%', formatter: function(cellvalue, options, rowObject) {
								return consolidationMappingCheckboxFormatter(cellvalue, options, rowObject, uniqueGridId, selectAllCheckboxId, childCheckboxName);	
				            }},
			              	{name:'mappingName',index:'mappingName',sortable:false},
			            	{name:'destPath',index:'destPath',sortable:false},
			            	{name:'conditionList',index:'conditionList',sortable:false},
			            	{name:'recordSortingType',index:'recordSortingType',sortable:false},
			            	{name:'fileName',index:'fileName',sortable:false},
			            	{name:'edit',index:'edit',sortable:false},
			            	{name:'logicalOperator',index:'logicalOperator',sortable:false,hidden:true},
			            	{name:'processRecordLimit',index:'processRecordLimit',sortable:false,hidden:true},
			            	{name:'compressedOutput',index:'compressModeWrite',sortable:false,hidden:true},
			            	{name:'writeOnlyConfiguredAttribute',index:'writeOnlyConfiguredAttribute',sortable:false,hidden:true},
			            	{name:'fieldNameForCount',index:'fieldNameForCount',sortable:false,hidden:true},
			            	{name:'recordSortingField',index:'recordSortingField',sortable:false,hidden:true},
			            	{name:'recordSortingFieldType',index:'recordSortingFieldType',sortable:false,hidden:true},
			            	{name:'fileSequence',index:'fileSequence',sortable:false,hidden:true},
			            	{name:'minSeqRange',index:'minSeqRange',sortable:false,hidden:true},
			            	{name:'maxSeqRange',index:'maxSeqRange',sortable:false,hidden:true}
			    ],
			    data: consolidationMappingList,		
				rowNum : <%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
				rowList : [ 10, 20, 60, 100 ],
				height : 'auto',
				mtype : 'POST',
				sortname : 'id',
				sortorder : "desc",
				pager : "#" + counter + "_gridPagingDiv",
				contentType : "application/json; charset=utf-8",
				viewrecords : true,
				//multiselect : true,
				timeout : 120000,
				loadtext : jsSpringMsg.loadingText,
				caption : "",
				beforeRequest : function() {
					$(".ui-dialog-titlebar").hide();
				},
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type","application/json");
				},
				beforeSelectRow: function(rowid, e){
		        	var $myGrid = $(this),
		            i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),
		            cm = $("#" + counter + "_grid").jqGrid('getGridParam', 'colModel');
		        	return (cm[i].name === 'cb');
		        },
		        onSelectRow: function(rowid, status) {
		            $('#'+rowid).removeClass('ui-state-highlight');
		        },
				onPaging : function() {
				},
				loadError : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				},
				onSelectAll : function() {
				},
				recordtext : jsSpringMsg.totalRecordText,
				emptyrecords : jsSpringMsg.emptyRecordText,
				loadtext : jsSpringMsg.loadingText,
				pgtext : jsSpringMsg.pagerText,
		}).navGrid("#gridPagingDiv", {edit : true,	add : false, del : false, search : false
	});

	$(".ui-jqgrid-titlebar").hide();
}

/*
 * this function will handle select all/ de select all event
 */
function consolidationMappingHeaderCheckbox(e, element, childCheckboxName) {
    e = e || event;/* get IE event ( not passed ) */
    e.stopPropagation ? e.stopPropagation() : e.cancelBubble = false;

    //if root checkbox is checked :-> all child checkbox should ne checked
    if(element.checked) {
    	$('input:checkbox[name="'+childCheckboxName+'"]').prop('checked',true);
    } else {
    	//if root checkbox is unchecked :-> all child checkbox should be unchecked
    	$('input:checkbox[name="'+childCheckboxName+'"]').prop('checked',false);
    }
}

function consolidationMappingCheckboxFormatter(cellvalue, options, rowObject, uniqueGridId, rootCheckboxId, childCheckboxName) {
	var uniqueId = "checkbox";
	if(rowObject["mappingName"] !== "") {
		uniqueId += "_" + rowObject["mappingName"]; 
	}
	return '<input type="checkbox" id="'+uniqueId+'" name="'+childCheckboxName+'" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \''+uniqueGridId+'\', \''+rootCheckboxId+'\', \''+childCheckboxName+'\')"/>';
}

/*
 * this function will handle root checkbox from child checkbox
 */
function updateRootCheckbox(element, gridId, rootCheckboxId, childCheckboxName) {
	if(!element.checked){
		// if current child checkbox is not checked : uncheck root checkbox
		$('input:checkbox[id="'+rootCheckboxId+'"]').prop('checked',false);
	} else {
		//if current child checkboox is checked and all checkbox are checked then check root checkbox
		var count = jQuery("#"+gridId).jqGrid('getGridParam', 'records');
		if ($('input:checkbox[name="'+childCheckboxName+'"]:checked').length === count) {
			$('input:checkbox[id="'+rootCheckboxId+'"]').prop('checked',true);
	    }
	}
}

function openAddEditConsolidationMappingPopup(actionType, rowId, counter) {
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	$("#pathlist_id").val($("#" + counter + "_pathListId").val());
	$("#current_block_count").val(counter);
	$("#mappingId").val('0');
	$("#mappingAction").val('');
	$("#recordSortingField").val('');
	resetConsolidationMappingParams();
	$("#add_mapping_buttons_div").hide();
	$("#update_mapping_buttons_div").hide();
	if(actionType == 'UPDATE'){
		
		$("#add_consolidation_pathlist_header_label").hide();
		$("#update_consolidation_pathlist_header_label").show();
		
		$("#update_mapping_buttons_div").show();
		$("#mappingAction").val('UPDATE');
		$("#mappingId").val(rowId);
		var rowData = jQuery("#" + counter + "_grid").jqGrid('getRowData', rowId);
		
		$('#mappingName').val(rowData.mappingName);
		$('#destPath').val(rowData.destPath);
		$('#logicalOperator').val(rowData.logicalOperator);
		$('#processRecordLimit').val(rowData.processRecordLimit);
		$('#conditionList').val(rowData.conditionList);
		$('#compressModeWrite').val(rowData.compressedOutput);
		$('#writeConfField').val(rowData.writeOnlyConfiguredAttribute);
		$('#fieldNameForCount').val(rowData.fieldNameForCount);
		$('#sortingType').val(rowData.recordSortingType);
		$('#recordSortingField').val(rowData.recordSortingField);
		$('#sortingFieldType').val(rowData.recordSortingFieldType);
		$('#fileName').val(rowData.fileName);
		$('#fileSequence').val(rowData.fileSequence);
		$('#minSeqRange').val(rowData.minSeqRange);
		$('#maxSeqRange').val(rowData.maxSeqRange);	
	}else{
		$("#add_mapping_buttons_div").show()
		$("#add_consolidation_pathlist_header_label").show();
		$("#update_consolidation_pathlist_header_label").hide();
	}
	toggleFileRange();
	$("#close_button_div").hide();
	$("#view_progress-bar-div").hide();
	
	$("#add_consolidation_grid_link").click();
	/* $('#composerPluginType').attr('disabled', false);
	$("#charRename_div").html('');
	$("#char_rename_add_link").hide(); */
}

function resetConsolidationMappingParams(){
	//$('#mappingName').val('');
	$('#destPath').val('');
	$('#logicalOperator').val('and');
	$('#compressModeWrite').val('true');
	$('#processRecordLimit').val('-1');
	$('#fieldNameForCount').val('General80');
	$('#sortingFieldType').val('String');
	$('#conditionList').val('');
	$('#fileName').val('data_cons{yyyyMMddHHmmssSSS}.gz');
	$('#fileSequence').val('false');
	$('#minSeqRange').val('1');
	$('#maxSeqRange').val('100');
	toggleFileRange();
}

function toggleFileRange(){
	if($('#fileSequence').val() == 'true'){
		$('#minSeqRange').prop("disabled", false);
		$('#maxSeqRange').prop("disabled", false);
	}else{
		$('#minSeqRange').prop("disabled", true);
		$('#maxSeqRange').prop("disabled", true);
	}
}

function showProgressBar(pathListCounter){
	$('#' + pathListCounter + '_buttons-div').hide();
	$('#' + pathListCounter + '_progress-bar-div').show();
 }
 
 function hideProgressBar(pathListCounter){
	$('#' + pathListCounter + '_buttons-div').show();
	$('#' + pathListCounter + '_progress-bar-div').hide();
 }
 
 function createConsolidationPathlist(counter){
	 createUpdateConsolidationPathList(counter, 'ADD', '<%=ControllerConstants.CREATE_CONSOLIDATION_SERVICE_PATH_LIST%>');
 }
 
 function updateConsolidationPathList(counter){
	 createUpdateConsolidationPathList(counter, 'UPDATE', '<%=ControllerConstants.UPDATE_CONSOLIDATION_SERVICE_PATH_LIST%>');
 }
 
 function createUpdateConsolidationPathList(counter, action, actionURL) {
	resetWarningDisplay();
	clearAllMessages();
	showProgressBar(counter);
	var pathId = '0';
	if(action == 'UPDATE'){
		pathId = $('#' + counter + '_pathListId').val();
	}
	var maxFileCountAlert = $("#" + counter + "_maxCounterLimit").val();
	if (maxFileCountAlert === null || maxFileCountAlert === ''
			|| maxFileCountAlert === 'undefined ') {
		maxFileCountAlert = -1;
		$("#" + counter + "_maxCounterLimit").val(maxFileCountAlert);
	}
	$.ajax({	url      : 	actionURL,
				cache    :  false,
				async    :  true,
				dataType :  'json',
				type     :  "POST",
				data 	 :  {	"id" : pathId,
						 		"name" 						:	$("#" + counter + "_name").val(),
								"maxCounterLimit" 			: 	maxFileCountAlert,
								"compressInFileEnabled" 	: 	$("#" + counter + "_compressedInput").val(),
								"readFilenamePrefix" 		: 	$("#" + counter + "_readFilenamePrefix").val(),
								"readFilenameSuffix" 		: 	$("#" + counter + "_readFilenameSuffix").val(),
								"readFilePath"				:	$("#" + counter + "_readFilePath").val(),		
								"pathListCount" 			: 	counter,
								"service.id"				:  	parseInt('${serviceId}'),
								"referenceDevice"			  : $('#' + counter + '_parentDevice').val(),
								"parentDevice.id"			  : $('#' + counter + '_deviceId').val(),
								"service.serverInstance.id"	: 	parseInt('${instanceId}'),
								"pathId"			 		: $('#' + counter + '_pathId').val(),
				},
				success : function(data) {
					hideProgressBar(counter);
					var responseCode = data.code;
					var responseMsg = data.msg;
					var responseObject = data.object;
					if (responseCode === "200") {
						resetWarningDisplay();
						clearAllMessages();
						var respObj = responseObject;
						var pathListId = respObj['id'];
						// set pathlist id hidden for all pathlist block
						$('#' + counter + '_pathListId').val(pathListId);
						// update block title with path list name
						$('#title_' + counter).html(respObj['name']);
						$('#' + counter + '_grid_links_div').show();
						$('#' + counter + '_grid_main_div').show();
						loadConsolidationGrid(counter, []);
						$('#' + counter + '_updatebtn').show();
						$('#' + counter + '_savebtn').hide();
						$('#'+counter+'_pathId').val(respObj['pathId']);
						showSuccessMsg(responseMsg);
					} else if (responseObject !== undefined	&& responseObject !== 'undefined' && responseCode === "400") {
						hideProgressBar(counter);
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
 
 
 function addUpdateConsolidationDefinitionDetails() {
		resetWarningDisplay();
		clearAllMessages();
		$("#view_progress-bar-div").show();
		$("#add_mapping_buttons_div").hide();
		$("#update_mapping_buttons_div").hide();
		if($('#processRecordLimit').val()==''){
			$('#processRecordLimit').val('-1');
		}
		var counter = $('#current_block_count').val();
		var mappingObj = {};
		var mappingId = 0;
		var actionURL = '<%=ControllerConstants.ADD_CONSOLIDATION_MAPPING%>';
		var mappingAction = $("#mappingAction").val();
		if(mappingAction == 'UPDATE'){
			mappingId = $("#mappingId").val();
			actionURL = '<%=ControllerConstants.UPDATE_CONSOLIDATION_MAPPING%>';
		}
		
		mappingObj['dataConsPathList.id'] 				= 	$('#pathlist_id').val();
		mappingObj['id'] 								=	mappingId;
		mappingObj['mappingName'] 						=	$('#mappingName').val();
		mappingObj['destPath'] 							= 	$('#destPath').val();
		mappingObj['logicalOperator'] 					= 	$('#logicalOperator').val();
		mappingObj['processRecordLimit'] 				= 	$('#processRecordLimit').val();
		mappingObj['conditionList'] 					= 	$('#conditionList').val();
		mappingObj['compressedOutput'] 					=	$('#compressModeWrite').val();
		mappingObj['writeOnlyConfiguredAttribute'] 		= 	$('#writeConfField').val();
		mappingObj['fieldNameForCount'] 				= 	$('#fieldNameForCount').val();
		mappingObj['recordSortingType'] 				= 	$('#sortingType').val();
		mappingObj['recordSortingField'] 				= 	$('#recordSortingField').val();
		mappingObj['recordSortingFieldType'] 			= 	$('#sortingFieldType').val();
		mappingObj['fileName'] 							= 	$('#fileName').val();
		mappingObj['fileSequence'] 						= 	$('#fileSequence').val();
		mappingObj['minSeqRange'] 						= 	$('#minSeqRange').val();
		mappingObj['maxSeqRange'] 						= 	$('#maxSeqRange').val(); 

		$.ajax({  	url : actionURL,
					cache : false,
					async : true,
					dataType : 'json',
					type : "POST",
					data : {
							"id" 								: mappingObj['id'],
							"dataConsPathList.id" 				: mappingObj['dataConsPathList.id'], 
							"mappingName" 						: mappingObj['mappingName'],
							"destPath" 							: mappingObj['destPath'],
							"logicalOperator" 					: mappingObj['logicalOperator'],
							"processRecordLimit" 				: mappingObj['processRecordLimit'],
							"conditionList" 					: mappingObj['conditionList'],
							"compressedOutput" 					: mappingObj['compressedOutput'],
							"writeOnlyConfiguredAttribute" 		: mappingObj['writeOnlyConfiguredAttribute'],
							"fieldNameForCount" 				: mappingObj['fieldNameForCount'],
							"recordSortingType" 				: mappingObj['recordSortingType'],
							"recordSortingField"				: mappingObj['recordSortingField'],
							"recordSortingFieldType"			: mappingObj['recordSortingFieldType'],
							"fileName"							: mappingObj['fileName'],
							"fileSequence"						: mappingObj['fileSequence'],
							"minSeqRange"						: mappingObj['minSeqRange'],
							"maxSeqRange"						: mappingObj['maxSeqRange'],
					},

					success : function(data) {
						hideProgressBar(counter);
						var response = data;
						var responseCode = response.code;
						var responseMsg = response.msg;
						var consolidationMapping = response.object;
						console.log(response);
						if (responseCode === "200") {
							resetWarningDisplay();
							clearAllMessages();
							showSuccessMsg(responseMsg);
							var rowData = {};
							rowData.id = parseInt(consolidationMapping.id);
							rowData.mappingName = consolidationMapping.mappingName;
							rowData.destPath = consolidationMapping.destPath;
							rowData.conditionList = consolidationMapping.conditionList;
							rowData.recordSortingType = consolidationMapping.recordSortingType;
							rowData.fileName = consolidationMapping.fileName;
							rowData.edit = '<a href="#" id="editConsolidationMapping_'+consolidationMapping.mappingName+'" onclick="openAddEditConsolidationMappingPopup(\'UPDATE\',' + rowData.id + ',' + counter  + ')"><i class="fa fa-pencil-square-o" aria-hidden="true" id="img_'+parseInt(rowData.id)+'" data-toggle="tooltip" data-placement="bottom" style="cursor:pointer;"></i></a>';
							rowData.logicalOperator = consolidationMapping.logicalOperator;
							rowData.processRecordLimit = consolidationMapping.processRecordLimit;
							rowData.compressedOutput = consolidationMapping.compressedOutput;
							rowData.writeOnlyConfiguredAttribute = consolidationMapping.writeOnlyConfiguredAttribute;
							rowData.fieldNameForCount = consolidationMapping.fieldNameForCount;
							rowData.recordSortingField = consolidationMapping.recordSortingField;
							rowData.recordSortingFieldType = consolidationMapping.recordSortingFieldType;
							rowData.fileSequence = consolidationMapping.fileSequence;
							rowData.minSeqRange = consolidationMapping.minSeqRange;
							rowData.maxSeqRange = consolidationMapping.maxSeqRange;
							if(mappingAction == 'UPDATE'){
								jQuery("#" + counter + "_grid").jqGrid('setRowData', rowData.id, rowData);
							}else{
								jQuery("#" + counter + "_grid").jqGrid('addRowData', rowData.id, rowData);
							}
							closeFancyBox();
							
							$("#add_mapping_buttons_div").hide();
							$("#update_mapping_buttons_div").hide();
							$("#close_button_div").show();
							$("#view_progress-bar-div").hide();maxSeqRange

						} else if (consolidationMapping !== undefined	&& consolidationMapping !== 'undefined' && responseCode === "400") {
							if(mappingAction == 'UPDATE'){
								$("#update_mapping_buttons_div").show();
							}else{
								$("#add_mapping_buttons_div").show();
							}
							$("#close_button_div").hide();
							$("#view_progress-bar-div").hide();
							addErrorIconAndMsgForAjaxPopUp(consolidationMapping);
						} else {
							if(mappingAction == 'UPDATE'){
								$("#update_mapping_buttons_div").show();
							}else{
								$("#add_mapping_buttons_div").show();
							}
							$("#close_button_div").hide();
							$("#view_progress-bar-div").hide();
							resetWarningDisplay();
							clearAllMessages();
							showErrorMsgPopUp(responseMsg);
						}
					},
					error : function(xhr, st, err) {
						hideProgressBar(counter);
						handleGenericError(xhr, st, err);
					}
			});
	}
 
 function displayDeleteConsolidationPopup(counter) {
 	clearAllMessagesPopUp();
 	resetWarningDisplay();
 	//var myGrid = $("#" + counter + "_grid");
 	//var selectedRowId = myGrid.jqGrid('getGridParam', 'selarrrow');
 	
 	var childCheckboxName = counter+"_consolidationMappingCheckbox";
	var selectedMappingCheckboxes = [];
    $.each($("input[name='"+childCheckboxName+"']:checked"), function(){            
    	selectedMappingCheckboxes.push($(this).val());
    });
    var selectedRowId = "";
    if(selectedMappingCheckboxes.length > 0) {
    	selectedRowId = selectedMappingCheckboxes[0];
    }
 	
 	$("#warning_msg").hide();
 	$("#delete_consolidationmapping_note").hide();
 	$("#btndelete").hide();
 	$("#delete_buttons-div").show();
 	if (selectedRowId != null && selectedRowId != '') {
 		$("#delete_consolidationmapping_note").show();
 		$("#btndelete").show();
 		$('#delete_buttons-div #btndelete').attr('onClick',	'deleteConsolidationMapping("' + counter + '")');
 		$('#deleteBlockId').val(counter);
 	} else {
 		$("#warning_msg").show();
 	}
 	$('#delete_consolidationmapping_link').click();
 }
 
 function deleteConsolidationMapping() {
		var blockId = $('#deleteBlockId').val();
		
		var childCheckboxName = blockId+"_consolidationMappingCheckbox";
		var selectedMappingCheckboxes = [];
    	$.each($("input[name='"+childCheckboxName+"']:checked"), function(){            
    		selectedMappingCheckboxes.push($(this).val());
    	});
    	var rowString = selectedMappingCheckboxes.join(",");
    
    	var rowIds = new Array();
    	rowIds = rowString.split(",");
    
		$.ajax({
					url : '<%=ControllerConstants.DELETE_CONSOLIDATION_MAPPING%>',
					cache : false,
					async : true,
					dataType : 'json',
					type : "POST",
					data : {
						"mappingId" : rowString
					},
					success : function(data) {
						var response = data;
						var responseCode = response.code;
						var responseMsg = response.msg;
						var responseObject = response.object;
						if (responseCode === "200") {
							resetWarningDisplay();
							clearAllMessages();
							showSuccessMsg(responseMsg);
							for (var i = 0; i < rowIds.length; i++) {
								var rowid = rowIds[i];
								$("#" + blockId + "_grid").jqGrid('delRowData',rowid);
							}
							closeFancyBox();
						} else if (responseObject !== undefined
								&& responseObject !== 'undefined'
								&& responseCode === "400") {
							addErrorIconAndMsgForAjaxPopUp(responseObject);
						} else {
							resetWarningDisplay();
							clearAllMessages();
							showErrorMsgPopUp(responseMsg);
						}
					},
					error : function(xhr, st, err) {
						handleGenericError(xhr, st, err);
					}
				});
	}

 var deleteBlockId = '';
 function deletePathListDetails(counter) {

 	var pathListId = $('#' + counter + '_pathListId').val();
 	deleteBlockId = counter;

 	if (pathListId === null || pathListId === 'null' || pathListId === '') {
 		$("#flipbox_" + counter).remove();
 	} else {
 		$("#pathlist_delete_link").click();
 	}
 }

 function deleteConsolidationPathList() {
 	resetWarningDisplay();
 	clearAllMessages();
 	$.ajax({
			url : '<%=ControllerConstants.DELETE_CONSOLIDATION_PATHLIST%>',
			cache : false,
			async : true,
			dataType : 'json',
			type : "POST",
			data : {
				"pathlistId" : $('#' + deleteBlockId + '_pathListId').val()
			},
			success : function(data) {
				var response = data;
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;
				if (responseCode === "200") {
					resetWarningDisplay();
					clearAllMessages();
					showSuccessMsg(responseMsg);
					$("#flipbox_" + deleteBlockId).remove();
					closeFancyBox();
				} else if (responseObject !== undefined
						&& responseObject !== 'undefined'
						&& responseCode === "400") {
					showErrorMsg(responseMsg);
					closeFancyBox();
				} else {
					resetWarningDisplay();
					clearAllMessages();
					showErrorMsg(responseMsg);
					closeFancyBox();
				}
			},
			error : function(xhr, st, err) {
				handleGenericError(xhr, st, err);
			}
		});
   }

 </script>
