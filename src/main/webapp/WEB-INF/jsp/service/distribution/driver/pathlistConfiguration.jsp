<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.model.TrueFalseEnum"%>
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
<script>
	var pathlist = [];
	var charRenameList  = [];
	var fileGroupEnable = '${fileGroupEnable}';
	var fileMergeEnable='${fileMergeEnable}';
</script>
<jsp:include page="../../collection/ftp/deviceDialog.jsp"></jsp:include>
<div class="tab-pane<c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_DRIVER_PATHLIST_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" id="pathListConfig">
	<div class="tab-content no-padding">
		<div class="fullwidth mtop10">
	   		<div class="title">
	   			<strong><spring:message code="distribution.service.ftp.pathlist.tab.header"></spring:message></strong>
	   			<span class="title2rightfield"> 
				    <span class="title2rightfield-icon1-text">
	   				<sec:authorize access="hasAuthority('CREATE_PATHLIST')">	
	   					<a href="#" class="customLink" onclick="addPathlistAndPluginDetails('','','','','','','','','','','','','','','ADD',false, 'yyyyMMddHHmm', '-1', '-1', '', '', '', '');"><i class="fa fa-plus-circle"></i></a>
	          			<a href="#" class="customLink" id="addPathList" onclick="addPathlistAndPluginDetails('','','','','','','','','','','','','','','ADD',false, 'yyyyMMddHHmm', '-1', '-1', '', '', '', '');">
	   					<spring:message code="parsing.pathlist.add.pathlist.label"></spring:message></a>
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

<c:if test="${(driverTypeAlias ne 'DATABASE_DISTRIBUTION_DRIVER')}">
<%--Add composer plug-in pop-up code start here --%>
<a href="#divAddComposerPlugin" class="fancybox" style="display: none;" id="add_composer_plugin_link">#</a>
<form:form modelAttribute="composer_plugin_form_bean" method="POST" action="javascript:;" id="composer-plugin-form">
<div id="divAddComposerPlugin" style="width:100%; display: none;"  >
    <div class="modal-content">
    	<input type="hidden" id="pathlist_id"/>
    	<input type="hidden" id="current_block_count"/>
    	<input type="hidden" id="current_block_path_name"/>
    	<input type="hidden" id="mappingId"/>
    	<input type="hidden" id="composerPluginId"/>
    	
        <div class="modal-header padding10">
            <h4 class="modal-title">
            	<span id="add_plugin_header_label" style="display:none;"><spring:message code="distributionService.pathlist.plugin.add.header.label" ></spring:message></span>
            	<span id="update_plugin_header_label" style="display:none;"> <spring:message code="distributionService.pathlist.plugin.update.header.label" ></spring:message></span>
	   		</h4>
        </div>
        <div class="modal-body padding10 inline-form">
        	<div>
        		<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>        		
        	</div>
			
				<div class="col-md-6 no-padding">
		           		<spring:message code="distributionService.pathlist.plugin.name.label" var="label"></spring:message>
		           		<spring:message code="distributionService.pathlist.plugin.name.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}<span class="required">*</span></div>
		             		<div class="input-group">
		             			<input id="name" class="form-control table-cell input-sm" tabindex="1" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" />
		             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		           
		            <div class="col-md-6 no-padding">
		           		<spring:message code="distributionService.pathlist.plugin.type.label" var="label"></spring:message>
		           		<spring:message code="distributionService.pathlist.plugin.type.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}<span class="required">*</span></div>
		             		<div class="input-group">
		             			<select id="composerPluginType" class="form-control table-cell input-sm" tabindex="2" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
		             			<c:forEach var='composer' items='${composerTypeList}' >
									<option value='${composer.id}' selected>${composer.type}</option>
								</c:forEach > 
								</select> 
		             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		             <div class="col-md-6 no-padding">
		           		<spring:message code="distributionService.pathlist.plugin.file.destination.path.label" var="label"></spring:message>
		           		<c:if test="${(driverTypeAlias eq 'FTP_DISTRIBUTION_DRIVER')}">
		           			<spring:message code="distributionService.pathlist.plugin.file.ftp.destination.path.label.tooltip" var="tooltip"></spring:message>
		           		</c:if>
		           		<c:if test="${(driverTypeAlias ne 'FTP_DISTRIBUTION_DRIVER')}">
		           			<spring:message code="distributionService.pathlist.plugin.file.destination.path.label.tooltip" var="tooltip"></spring:message>
		           		</c:if>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}<span class="required">*</span></div>
		             		<div class="input-group">
		             			<input  id="destPath" class="form-control table-cell input-sm" tabindex="3" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" />
		             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		            
		             <div class="col-md-6 no-padding">
		           		<spring:message code="distributionService.pathlist.plugin.file.prefix.label" var="label"></spring:message>
		           		<spring:message code="distributionService.pathlist.plugin.file.prefix.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		             		<div class="input-group">
		             			<input  id="writeFilenamePrefix" class="form-control table-cell input-sm" tabindex="4" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" />
		             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		            <div class="col-md-6 no-padding">
		           		<spring:message code="distributionService.pathlist.plugin.file.suffix.label" var="label"></spring:message>
		           		<spring:message code="distributionService.pathlist.plugin.file.suffix.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		             		<div class="input-group">
		             			<input  id="writeFilenameSuffix" class="form-control table-cell input-sm" tabindex="5" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" />
		             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		      		
			  		<c:choose>
			 			<c:when test="${driverTypeAlias eq 'FTP_DISTRIBUTION_DRIVER' or driverTypeAlias eq 'SFTP_DISTRIBUTION_DRIVER' }">
			 				<div class="col-md-6 no-padding">
					           		<spring:message code="distributionService.pathlist.plugin.file.backup.path.label" var="label"></spring:message>
					           		<spring:message code="distributionService.pathlist.plugin.file.backup.path.label.tooltip" var="tooltip"></spring:message>
					            	<div class="form-group">
					         			<div class="table-cell-label">${label}</div>
					             		<div class="input-group">
					             			<input  id="fileBackupPath" class="form-control table-cell input-sm" tabindex="6" data-toggle="tooltip" data-placement="top" title="${tooltip}" />
					             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
					             		</div>
					            	</div>
					            </div>
			  			</c:when>
					</c:choose> 		      		  
		            
		            <div class="col-md-6 no-padding">
		           		<spring:message code="distributionService.pathlist.plugin.file.extension.label" var="label"></spring:message>
		           		<spring:message code="distributionService.pathlist.plugin.file.extension.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		             		<div class="input-group">
		             			<input  id="fileExtension" class="form-control table-cell input-sm" tabindex="6" data-toggle="tooltip" data-placement="top" title="${tooltip}" />
		             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		            <div class="col-md-6 no-padding">
		           		<spring:message code="distribution.service.pathlist.default.file.extension.remove.enabled.label" var="label"></spring:message>
		           		<spring:message code="distribution.service.pathlist.default.file.extension.remove.enabled.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		             		<div class="input-group">
		             		    <select class='form-control table-cell input-sm' id='defaultFileExtensionRemoveEnabled' tabindex='5' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' >
							    	<c:forEach var='truefalseEnum' items='${truefalseEnum}' >
										<option value='${truefalseEnum.name}'>${truefalseEnum}</option>
									</c:forEach>
								</select>
		             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		            <div class="col-md-6 no-padding">
		           		<spring:message code="distribution.service.pathlist.file.split.enabled.label" var="label"></spring:message>
		           		<spring:message code="distribution.service.pathlist.file.split.enabled.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		             		<div class="input-group">
		             		    <select class='form-control table-cell input-sm' id='fileSplitEnabled' tabindex='5' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' >
							    	<c:forEach var='truefalseEnum' items='${truefalseEnum}' >
										<option value='${truefalseEnum.name}'>${truefalseEnum}</option>
									</c:forEach>
								</select>
		             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		            
		             <div class="clearfix"></div>
		             
			         <div id="add_edit_plugin_button_div" class="modal-footer padding10">
				         
				       
				         <div id="add_plugin_buttons_div" class="padding10" style="display:none;">
				           <sec:authorize access="hasAuthority('CREATE_COMPOSER')">
				         		<button id="saveComposerPluginDetails_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="addComposerPluginDetails();"><spring:message code="btn.label.save"></spring:message></button>
				           </sec:authorize>	
				         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
				        </div>
				        
				       
				        <div id="update_plugin_buttons_div" class="padding10" style="display:none;">
				         	 <sec:authorize access="hasAuthority('UPDATE_COMPOSER')">
				         		<button id="updateComposerPlugin_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="updateComposerPluginDetails();"><spring:message code="btn.label.update"></spring:message></button>
				         	</sec:authorize>	
				         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
				        </div>
				        
				        <div id="view_plugin_buttons_div" class="modal-footer padding10" style="display:none;">
				         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
				        </div>
				        <div id="view_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
							<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
						</div>	
					</div>		
			<%--Content end here --%>
					</div>
    </div>
    <!-- /.modal-content --> 
</div>

 </form:form>
</c:if>
<%--Add composer plug-in pop-up code start here --%>
<!-- Pathlist Delete popup code start here -->
    	<div id="divMessage" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10" >
		           <h4 class="modal-title" id="status-title" ><spring:message code="ftp.driver.pathlist.delete.header.label"></spring:message></h4>
		        </div>
		        
		        <div class="modal-body padding10 inline-form">
		        	<span id="deleteDriverResponseMsg" style="display:none;">
		        		<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
		        	</span>
			        <p id="deleteWarningMessage">
			       		 <spring:message code="pahtlist.delete.warn.message"></spring:message>
			        </p>
		        </div>
		        
		       <!-- To Do add role here -->
		       <sec:authorize access="hasAuthority('DELETE_PATHLIST')">
			        <div id="inactive-driver-div" class="modal-footer padding10">
			            <button id="delete_pathList_btn" type="button" class="btn btn-grey btn-xs " onclick="deleteDistributionDriverPathList();"><spring:message code="btn.label.yes"></spring:message></button>
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
			        </div>
			     </sec:authorize>   
			         <div class="modal-footer padding10" id="reaload-driver-details" style="display:none;">
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox()();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
    	<a href="#divMessage" class="fancybox" style="display: none;" id="pathlist_delete_link">#</a>
    	<!-- Pathlist Delete popup code end here -->
<c:if test="${(driverTypeAlias ne 'DATABASE_DISTRIBUTION_DRIVER')}">
		<!-- Plugin delete popup code start here  -->
		<a href="#divDeletemPopup" class="fancybox" style="display: none;" id="delete_plugin_link">#</a>
		<div id="divDeletemPopup" style="width:100%; display: none;" >
		    <div class="modal-content">
		    	<input type="hidden" id="deleteBlockId"/>
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="delete.composer.plugin.label"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        	<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
		        	
		        	<span id="warning_msg" style="display: none;"><spring:message code="warning.plugin.message"></spring:message></span>			
					<span id="delete_plugin_note" style="display: none;"><spring:message code="delete.composer.warn.msg"></spring:message></span>
		        	
		        </div>
		         <sec:authorize access="hasAuthority('DELETE_COMPOSER')">
		        <div id="delete_buttons-div" class="modal-footer padding10">
		         	<button type="button" id="btndelete" class="btn btn-grey btn-xs " data-dismiss="modal" style="display: none;" onclick="deleteComposerPlugin();"><spring:message code="btn.label.delete"></spring:message></button>
		         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		        </sec:authorize>
		        <div id="delete_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
				</div>	
		        
		    </div>
		    <!-- /.modal-content --> 
		</div>
		<!-- Plugin delete popup code end here -->
		
		
		<a href="#divCharRenameDeletemPopup" class="fancybox" style="display: none;" id="delete_char_rename_param_link">#</a>
		
		<!-- Delete Char Rename operaton parameter pop up code start here -->
			<div id="divCharRenameDeletemPopup" style="width:100%; display: none;" >
		    <div class="modal-content">
		    	<input type="hidden" id="deleteCharRenameBlockId"/>
		    	<input type="hidden" id="deleteCharRenameId"/>
		    	
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="delete.composer.plugin.label"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        	<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
		        	
		        	<div>
		        		<spring:message code="delete.characterRename.warn.msg"></spring:message>
		        	</div>
		        </div>
		        <div id="delete_buttons-div" class="modal-footer padding10">
		         	<button type="button" id="btndelete" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="deleteCharRenameOperationParams();"><spring:message code="btn.label.delete"></spring:message></button>
		         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		        <div id="delete_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
				</div>	
		        
		    </div>
		    <!-- /.modal-content --> 
		</div>
		
		<!-- Delete char Rename operation parameter pop up code end here -->
		
		<!-- View additional parameters popup code start here -->
		<a href="#divAdditionalParams" class="fancybox" style="display: none;" id="additional_param_link">#</a>
			<div id="divAdditionalParams" style="width:100%; display: none;" >
			    <div class="modal-content">
			        <div class="modal-header padding10">
			            <h4 class="modal-title"><spring:message code="parsing.pathlist.more.element.popup.header"></spring:message></h4>
			        </div>
			        <div class="modal-body padding10 inline-form">
						
			            <div class="col-md-6 no-padding">
			           		<spring:message code="iplog.parsing.service.pathlist.output.file.prefix" var="tooltip"></spring:message>
			            	<div class="form-group">
			         			<div class="table-cell-label">${tooltip}</div>
			             		<div class="input-group">
			             			<input tabindex="4" id="view_writeFilenamePrefix" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" readonly="readonly"/>
			             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
			             		</div>
			            	</div>
			            </div>
			            <div class="col-md-6 no-padding">
		           		<spring:message code="distributionService.pathlist.plugin.file.suffix.label" var="label"></spring:message>
		           		<spring:message code="distributionService.pathlist.plugin.file.suffix.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		             		<div class="input-group">
		             			<input  id="view_writeFilenameSuffix" class="form-control table-cell input-sm" tabindex="5" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"  readonly="readonly"/>
		             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
			            <div class="col-md-6 no-padding">
			           		<spring:message code="distributionService.pathlist.plugin.file.destination.path.label" var="tooltip"></spring:message>
			            	<div class="form-group">
			         			<div class="table-cell-label">${tooltip}</div>
			             		<div class="input-group">
			             			<input tabindex="4" id="view_destPath" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" readonly="readonly"/>
			             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
			             		</div>
			            	</div>
			            </div>
			           <c:if test="${driverTypeAlias ne 'LOCAL_DISTRIBUTION_DRIVER'}"> 
			            <div class="col-md-6 no-padding">
			           			<spring:message code="distributionService.pathlist.plugin.file.backup.path.label" var="tooltip"></spring:message>
			            	<div class="form-group">
			         			<div class="table-cell-label">${tooltip}</div>
			             		<div class="input-group">
			             			<input tabindex="4" id="view_fileBackupPath" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" readonly="readonly"/>
			             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
			             		</div>
			            	</div>
			            </div>
			           </c:if> 
			         <div class="col-md-6 no-padding">
		           		<spring:message code="distributionService.pathlist.plugin.file.extension.label" var="label"></spring:message>
		           		<spring:message code="distributionService.pathlist.plugin.file.extension.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		             		<div class="input-group">
		             			<input  id="view_fileExtension" class="form-control table-cell input-sm" tabindex="6" data-toggle="tooltip" data-placement="top" title="${tooltip}" readonly="readonly"/>
		             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		            </div>
					<div class="col-md-6 no-padding">
		           		<spring:message code="distribution.service.pathlist.file.split.enabled.label" var="label"></spring:message>
		           		<spring:message code="distribution.service.pathlist.file.split.enabled.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		             		<div class="input-group">
		             		    <select class='form-control table-cell input-sm' id='view_fileSplitEnabled' tabindex='5' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' >
							    	<c:forEach var='truefalseEnum' items='${truefalseEnum}' >
										<option value='${truefalseEnum.name}'>${truefalseEnum}</option>
									</c:forEach>
								</select>
		             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
			        
			        <div id="view_additional_params_buttons-div" class="modal-footer padding10">
			         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
			    </div>
			    <!-- /.modal-content --> 
			</div>
		
		<!-- View addintional paramters popup code end here -->
		<!-- View char rename operation parameters popup code start here-->
		<a href="#divCharRenameParams" class="fancybox" style="display: none;" id="char_rename_param_link">#</a>
		<div id="divCharRenameParams" style="width:100%; display: none;" >
			    <div class="modal-content">
			        <div class="modal-header padding10">
			            <h4 class="modal-title"><spring:message code="distributionService.pathlist.char.rename.sub.header"></spring:message></h4>
			        </div>
			        <div class="modal-body padding10 inline-form">
			        	<div style="width= calc(); display: none; text-align: center; padding-bottom: 0px;" class="errorMessage" id="errorMessageCharacterRename">
							<span id = "error_text" class="title" >Given input values may not be as per defined guideline. Get more detail on hover of error icon.</span>
						</div> 
		        		<div class="fullwidth">
		        			<span class="title2rightfield"> 
							    <span class="title2rightfield-icon1-text" id="char_rename_add_link" >
				   				<sec:authorize access="hasAuthority('CREATE_COMPOSER')">	
				   					<a href="#" onclick="addCharRenameOperation('','','','','','','','','','','','ADD');" ><i class="fa fa-plus-circle"></i></a>
				          			<a href="#" id="addCharacterRenameOperations" onclick="addCharRenameOperation('','','','','','','','','','','','ADD');" >
				   					<spring:message code="btn.label.add" ></spring:message></a>
				   				</sec:authorize>
				   				</span> 
				   			</span> 
		        		</div>
						<div class="fullwidth" id="plugin_char_rename_details" style="overflow: auto;height:300px;">
						</div>
			        </div>
			        <div id="view_char_rename_params_buttons-div" class="modal-footer padding10">
			         	<button id="closePopup_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox(); resetWarningMsg();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
			    </div>
			    <!-- /.modal-content --> 
			</div>
		<!-- View char rename operation parameters popup code end here-->
		
</c:if>		
<script type="text/javascript">
var pathListCounter = -1;
var charRenameOperationCounter = 0;
$(document).ready(function() {
	pathlist = eval('${pathList}');
	if(pathlist != 'undefined' && pathlist != null  ){
		$.each(pathlist, function(index,path){
			addPathlistAndPluginDetails(path.id, path.name, path.fileNamePattern, path.readPath, path.maxFileCountAlert, path.isReadCompressEnable, path.readFilePrefix, path.readFileSuffix, path.dbReadFileNameExtraSuffix, path.fileNameContains, path.excludeFileType, path.writePath, path.isWriteCompressEnable, path.composerWrapper,'UPDATE', path.fileGrepDateEnabled, path.dateFormat, path.startIndex, path.endIndex, path.position, path.pathId, path.deviceName, path.deviceId, path.referenceDeviceName );
		});	
	}
	if(fileMergeEnable == 'true'){
		$("#"+ "fileSplitEnabled").prop('disabled',true);
	}else{
		$("#"+ "fileSplitEnabled").prop('disabled',false);
	}
	
});
</script>

<script type="text/javascript">

function resetWarningMsg() {
	$("#errorMessageCharacterRename").hide();
}

function addCharRenameOperation(id,seqNumber,renameQuery,position,startIndex,endIndex,padding,paddingValue,defaultValue,length,isCacheEnable,actionType){
	charRenameOperationCounter++
	
	 if(String(defaultValue) === 'null' || String(defaultValue) === 'undefined'){
		 defaultValue = '';
	 }

	 if(String(renameQuery) === 'null' || String(renameQuery) === 'undefined'){
		 renameQuery = '';
	 }
	
	 if(String(paddingValue) === 'null' || String(paddingValue) === 'undefined'){
		 paddingValue = '';
	 }
	
	 if(startIndex==-1){
		 startIndex='';
	 }
	 if(endIndex==-1){
		 endIndex='';
	 }
	 if(length==-1){
		 length='';
	 }
	var charHtmlBody =  "<form method='POST' id='" + charRenameOperationCounter + "_char_form'> "+
						"<div class='box box-warning' id='flipbox_char_"+charRenameOperationCounter+"'>  "+
					    "<input type='hidden' id='"+charRenameOperationCounter+"_char_rename_id'  value='"+id+"'> "+
						"<div class='box-header with-border'> "+
						"	<h3 class='box-title' id='title_"+charRenameOperationCounter+"'>"+
						"       	<spring:message code='distributionService.pathlist.char.rename.sub.header'></spring:message>"+
						"	</h3>  "+
						"	<div class='box-tools pull-right' style='top: -3px;' id='action_"+charRenameOperationCounter+"'> "+ 
						"		<button id='"+charRenameOperationCounter+"_collapse_block' class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' href='#"+charRenameOperationCounter+"_char_operation_block'> "+ 
						"			<i class='fa fa-minus'></i> "+
						"		</button> "+
						"		<sec:authorize access='hasAuthority(\'DELETE_COMPOSER\')'>"+
					    "					&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;' onclick=displayDeleteCharRenamePopup(\'"+charRenameOperationCounter+"\');></a>&nbsp;" +
					    "		</sec:authorize>"+
						"	</div> "+
						"</div> "+
						"<div class='box-body inline-form accordion-body collapsed in' id='"+charRenameOperationCounter+"_char_operation_block'> "+ 
						"	<div class='fullwidth inline-form'>  "+
						"		 <div class='col-md-12 no-padding'>"+
					    "       	<spring:message code='distributionService.pathlist.plugin.sequence.label.tooltip' var='tooltip'></spring:message>"+
					    "       	<spring:message code='distributionService.pathlist.plugin.sequence.label' var='label'></spring:message>"+
					    "        	<div class='form-group'>"+
					    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
					    "         		<div class='input-group' style = 'width:40%;'>"+
					    "         			<input  id='" + charRenameOperationCounter + "_sequenceNo' class='form-control table-cell input-sm' tabindex='1' data-toggle='tooltip' data-placement='bottom' title='${tooltip}' />"+
					    "         			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
					    "         		</div>"+
					    "        	</div>"+
					    "        </div>"+
					    "        <div class='col-md-12 no-padding'>"+
					    "       	<spring:message code='distributionService.pathlist.plugin.default.label.tooltip' var='tooltip'></spring:message>"+
					    "       	<spring:message code='distributionService.pathlist.plugin.default.label' var='label'></spring:message>"+
					    "        	<div class='form-group'>"+
					    "    			<div class='table-cell-label'>${label}</div>"+
					    "         		<div class='input-group' style='width:40%'>"+
					    "         			<input  id='" + charRenameOperationCounter + "_defaultValue' class='form-control table-cell input-sm' tabindex='2'  data-toggle='tooltip' data-placement='bottom' title='${tooltip}' value='"+defaultValue+"' onchange='disableDefaultValue("+charRenameOperationCounter+");'/>"+
					    "         			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
					    "         		</div>"+
					    "        	</div>"+
					    "        </div>"+
					    "		 <div class='col-md-12 no-padding'> "+
						"			<spring:message code='collectionService.pathlist.plugin.extractFromOriginal.label' var='label'></spring:message> "+
						"			<div class='form-group'>  "+
						"				<div class='table-cell-label'><strong>${label}</strong></div> "+ 
						"			</div> "+
						"		 </div> "+
						"		 <div class='col-md-6 no-padding'>"+
						"		 <div class='form-group'>"+
							"				<div class='col-md-4 no-padding'>"+
							"       			<spring:message code='distributionService.pathlist.plugin.end.index.label.tooltip' var='maxIndex'></spring:message>"+
							"       			<spring:message code='distributionService.pathlist.plugin.start.index.label.tooltip' var='minIndex'></spring:message>"+
							"       			<spring:message code='collectionService.pathlist.plugin.index.label' var='label'></spring:message>"+
							"					<div class='table-cell-label'>${label}</div>"+
							"				</div>"+
							"				<div class='col-md-4 no-padding'>"+
							"					<div class='form-group'>"+
							"                         	<div class='input-group'>"+
							"                         		<input tabindex='3' id='" + charRenameOperationCounter + "_char_startIndex' class='form-control table-cell input-sm'  data-toggle='tooltip' data-placement='bottom' title='${minIndex}' value='"+startIndex+"' onkeydown='isNumericOnKeyDown(event)' onchange='disableIndex("+charRenameOperationCounter+");'/>"+
						    "         						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
							"                         	</div>"+
							"                        </div> "+	
							"				</div>"+
							"				<div class='col-md-4 no-padding'>"+
							"					<div class='form-group'>"+
							"                        	<div class='input-group'>"+ 
							"                         		<input tabindex='4' id='" + charRenameOperationCounter + "_char_endIndex' class='form-control table-cell input-sm'  data-toggle='tooltip' data-placement='bottom' title='${maxIndex}' value='"+endIndex+"'  onkeydown='isNumericOnKeyDown(event)'/>"+
						    "         						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
							"                         	</div>"+
							"                        </div> "+	
							"				</div>"+
						    " 		</div>"+	
						    "		</div>"+
						    "		 <div class='col-md-6 no-padding'> "+
							"			<spring:message code='distributionService.pathlist.plugin.position.label' var='label'></spring:message> "+
							"			<spring:message code='distributionService.pathlist.plugin.position.label.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<select class='form-control table-cell input-sm' id='" + charRenameOperationCounter + "_position' tabindex='5' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
						    "				    	<c:forEach var='positionEnum' items='${positionEnum}' >"+
							"							<option value='${positionEnum.value}'>${positionEnum}</option>"+
							"				    	</c:forEach>"+
							"					</select>"+
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							"				</div> "+
							"			</div> "+
							"		 </div> "+
							"		 <div class='col-md-12 no-padding'> "+
							"			<spring:message code='collectionService.pathlist.plugin.padding.label' var='label'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'><strong>${label}</strong></div> "+ 
							"			</div> "+
							"		 </div> "+
						    "		 <div class='col-md-12 no-padding'>"+
						    "       	<spring:message code='distributionService.pathlist.plugin.length.label.tooltip' var='tooltip'></spring:message>"+
						    "       	<spring:message code='distributionService.pathlist.plugin.length.label' var='label'></spring:message>"+
						    "        	<div class='form-group'>"+
						    "    			<div class='table-cell-label'>${label}</div>"+
						    "         		<div class='input-group' style = 'width:40%;'>"+
						    "         			<input  id='" + charRenameOperationCounter + "_length' class='form-control table-cell input-sm' tabindex='6'  data-toggle='tooltip' data-placement='bottom' title='${tooltip}' value='"+length+"' onkeydown='isNumericOnKeyDown(event)' onchange='disablePadding("+charRenameOperationCounter+");'/>"+
						    "         			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
						    "         		</div>"+
						    "        	</div>"+
						    "        </div>"+
					    "		 <div class='col-md-6 no-padding'> "+
						"			<spring:message code='distributionService.pathlist.plugin.padding.label' var='label'></spring:message> "+
						"			<spring:message code='distributionService.pathlist.plugin.padding.label.tooltip' var='tooltip'></spring:message> "+
						"			<div class='form-group'>  "+
						"				<div class='table-cell-label'>${label}</div> "+ 
						"				<div class='input-group '> "+
						"					<select class='form-control table-cell input-sm' id='" + charRenameOperationCounter + "_paddingType' tabindex='7' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' >"+
					    "				    	<c:forEach var='positionEnum' items='${positionEnum}' >"+
						"							<option value='${positionEnum.value}'>${positionEnum}</option>"+
						"				    	</c:forEach>"+
						"					</select>"+
						"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
						"				</div> "+
						"			</div> "+
						"		 </div> "+
						"		 <div class='col-md-6 no-padding'>"+
					    "       	<spring:message code='distributionService.pathlist.plugin.paddingValue.label' var='label'></spring:message>"+
					    "       	<spring:message code='distributionService.pathlist.plugin.paddingValue.label.tooltip' var='tooltip'></spring:message>"+
					    "        	<div class='form-group'>"+
					    "    			<div class='table-cell-label'>${label}</div>"+
					    "         		<div class='input-group'>"+
					    "         			<input  id='" + charRenameOperationCounter + "_paddingValue' class='form-control table-cell input-sm' tabindex='8'  data-toggle='tooltip' data-placement='bottom' title='${tooltip}' value='"+paddingValue+"' maxlength = '1' />"+
					    "         			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
					    "         		</div>"+
					    "        	</div>"+
					    "        </div>"+
					    "		 <div class='col-md-12 no-padding'> "+
						"			<spring:message code='collectionService.pathlist.plugin.renameQuery.label' var='label'></spring:message> "+
						"			<div class='form-group'>  "+
						"				<div class='table-cell-label'><strong>${label}</strong></div> "+ 
						"			</div> "+
						"		 </div> "+
						"		 <div class='col-md-12 no-padding'> "+
						"			<spring:message code='distributionService.pathlist.plugin.query.label' var='label'></spring:message> "+
						"			<spring:message code='distributionService.pathlist.plugin.query.label.tooltip' var='tooltip'></spring:message> "+
						"			<div class='form-group'>  "+
						"				<div class='table-cell-label'>${label}</div> "+ 
						"				<div class='input-group '> "+
						"         			<input  id='" + charRenameOperationCounter + "_query' class='form-control table-cell input-sm' tabindex='9'  data-toggle='tooltip' data-placement='bottom' title='${tooltip}' onchange='disableQuery("+charRenameOperationCounter+");'/>"+
					    "         			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
						"				</div> "+
						"			</div> "+
						"		 <div class='col-md-12 no-padding'> "+
						"			<div class='form-group'>  "+
						"				<div class='table-cell-label'>DB Cache Enable</div> "+ 
						"				<div class='input-group ' style = 'width:40%;'>"+
						"					<select class='form-control table-cell input-sm' id='" + charRenameOperationCounter + "_cacheEnable' tabindex='10' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' >"+
						"				    	<c:forEach var='truefalseEnum' items='${truefalseEnum}' >"+
						"							<option value='${truefalseEnum.name}'>${truefalseEnum}</option>"+
						"				    	</c:forEach>"+
						"					</select>"+
						"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
						"				</div> "+
						"			</div> "+
						"		 </div> "+
						"		 </div> "+ 
					    "		<div class='col-md-12 no-padding'>"+
					    "  			<div class='form-group'>"+
					    "      			<div id='" + charRenameOperationCounter + "_char_buttons-div' class='input-group '>"+
						"						<sec:authorize access='hasAuthority(\'CREATE_COMPOSER\')'>"+	
			    		"								<button type='button' class='btn btn-grey btn-xs ' id='" + charRenameOperationCounter + "_char_savebtn' onclick=addCharRenamOperationParams(\'"+charRenameOperationCounter+"\');><spring:message code='btn.label.save'></spring:message></button>&nbsp;" +
			    		"						</sec:authorize>"+
						"						<sec:authorize access='hasAuthority(\'UPDATE_COMPOSER\')'>"+
						"								<button type='button' class='btn btn-grey btn-xs ' id='" + charRenameOperationCounter + "_char_updatebtn' onclick=updateCharRenamOperationParams(\'"+charRenameOperationCounter+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
						"						</sec:authorize>"+	
					   
			     		"				<button type='button' class='btn btn-grey btn-xs ' onclick=resetCharRenameParameters(\'"+charRenameOperationCounter+"\');><spring:message code='btn.label.reset'></spring:message></button>&nbsp;" +
					    "       		</div>"+
					    "					<div id='" + charRenameOperationCounter + "_char_progress-bar-div' class='input-group' style='display: none;'> "+
					    "						<label>Processing Request &nbsp;&nbsp; </label> "+
					    "							<img src='img/processing-bar.gif'>"+
						"					</div> "+
					    "   		</div>" +
					    "		</div>" +
					    
					    
						"	</div> "+
						"</div> "+
						"</div> "+
						"</form>";
						
		$('#plugin_char_rename_details').append(charHtmlBody);		
		
		if(actionType == 'UPDATE'){
			 if(startIndex!='' && startIndex>=0){
				 $('#' + charRenameOperationCounter + '_char_startIndex').val(startIndex);
			 }
			 if(endIndex!='' && endIndex>=0){
				 $('#' + charRenameOperationCounter + '_char_endIndex').val(endIndex);
			 }
			$('#' + charRenameOperationCounter + '_cacheEnable').val(isCacheEnable.toString());
			$('#' + charRenameOperationCounter + '_position').val(position.toString());
			$('#' + charRenameOperationCounter + '_paddingType').val(padding.toString());
			$('#' + charRenameOperationCounter + '_query').val(renameQuery.toString());
			
			$("#"+charRenameOperationCounter+"_char_savebtn").hide();
			$("#"+charRenameOperationCounter+"_char_updatebtn").show();
			
			$("#"+charRenameOperationCounter+"_char_operation_block").collapse("toggle");
			
			$("#"+charRenameOperationCounter+"_sequenceNo").val(seqNumber);
			if(defaultValue!=null && defaultValue!=undefined && defaultValue!=""){
				disableDefaultValue(charRenameOperationCounter);
			} else if(startIndex>-1 || length>-1){
				disableIndex(charRenameOperationCounter);
			}else if(renameQuery!=null && renameQuery!=undefined && renameQuery!=""){
				disableQuery(charRenameOperationCounter);
			}   
		}else{
			$("#"+charRenameOperationCounter+"_char_updatebtn").hide();
			$("#"+charRenameOperationCounter+"_char_savebtn").show();
		}
}



function addPathlistAndPluginDetails(id,name,fileNamePattern,readFilePath,maxCountAlert,readFileMode,readFilePrefix,readFileSuffix, dbReadFileNameExtraSuffix, fileContain,fileExcludeType,writePath,writeFileMode,composerList, actionType, fileGrepDateEnabled, dateFormat, startIndex, endIndex, position, pathId, parentDevice, parentDeviceId, referenceDevice){

	 pathListCounter++;

	 if(String(readFilePrefix) === 'null' || String(readFilePrefix) === 'undefined'){
		 readFilePrefix = '';
	 }
	
	 if(String(readFileSuffix) === 'null' || String(readFileSuffix) === 'undefined'){
		 readFileSuffix = '';
	 }
	 if(String(fileContain) === 'null' || String(fileContain) === 'undefined'){
		 fileContain = '';
	 }
	 if(String(fileExcludeType) === 'null' || String(fileExcludeType) === 'undefined'){
		 fileExcludeType = '';
	 }
	 if(String(writePath) === 'null' || String(writePath) === 'undefined'){
		 writePath = '';
	 }
	 
	 if(String(referenceDevice) === 'null' || String(referenceDevice) === 'undefined'){
		 referenceDevice = '';
	 }

	 if(String(fileNamePattern) === 'null' || String(fileNamePattern) === 'undefined'){
		 fileNamePattern = '';
	 }

	 if(String(dbReadFileNameExtraSuffix) === 'null' || String(dbReadFileNameExtraSuffix) === 'undefined'){
		 dbReadFileNameExtraSuffix = '';
	 }
	 
	 var pahtlistHtmlBody = "<div class='box box-warning' id='flipbox_"+pathListCounter+"'>  "+
							"<input type='hidden' id='"+pathListCounter+"_pluginId' > "+   
							"<input type='hidden' id='"+pathListCounter+"_driverId' value='${driverId}' > "+
							"<input type='hidden' id='"+pathListCounter+"_pathListId' value='"+id+"'> "+ 
							"<input type='hidden' id='"+pathListCounter+"_pathId' value='"+pathId+"'> "+ 
							"<div class='box-header with-border'> "+
							"	<h3 class='box-title' id='title_"+pathListCounter+"'>"+name+" - "+ pathId +											
							"	</h3>  "+
							"	<div class='box-tools pull-right' style='top: -3px;' id='action_"+pathListCounter+"'> "+ 
							"		<button id='"+name+"_collapse_btn' class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' href='#"+pathListCounter+"_block'> "+ 
							"			<i class='fa fa-minus'></i> "+
							"		</button> "+
							"					<sec:authorize access='hasAuthority(\'DELETE_PATHLIST\')'>"+
						    "			&nbsp;&nbsp;<a id='"+name+"_delete_btn' class='ion-ios-close-empty remove-block' style='margin-top:12px;' onclick=deletePathListDetails(\'"+pathListCounter+"\');></a>&nbsp;" +
						    "					</sec:authorize>"+
							"	</div> "+
							"</div> "+
							"<div class='box-body inline-form accordion-body collapsed in' id='"+pathListCounter+"_block'> "+ 
							"	<div class='fullwidth inline-form'>  "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='distribution.service.ftp.pathlist.name' var='label'></spring:message> "+ 
							"			<spring:message code='distribution.service.ftp.pathlist.name.tooltip' var='tooltip'></spring:message> "+ 
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_name' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+name+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='distribution.service.ftp.pathlist.read.path' var='label'></spring:message> "+ 
							"			<spring:message code='distribution.service.ftp.pathlist.read.path.tooltip' var='tooltip'></spring:message> "+ 
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_readFilePath' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readFilePath+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='distribution.service.ftp.pathlist.max.file.cnt.alert' var='label'></spring:message> "+
							"			<spring:message code='distribution.service.ftp.pathlist.max.file.cnt.alert.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_maxFilesCountAlert' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+maxCountAlert+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='distribution.service.pathlist.file.read.mode.label' var='label'></spring:message> "+
							"			<spring:message code='distribution.service.pathlist.file.read.mode.label.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
							"				<div class='input-group '> "+
							"					<select class='form-control table-cell input-sm' id='" + pathListCounter + "_compressInFileEnabled' tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
						    "				    	<c:forEach var='trueFalseEnum' items='${truefalseEnum}' >"+
							"							<option value='${trueFalseEnum.name}'>${trueFalseEnum}</option>"+
							"				    	</c:forEach>"+
							"					</select>"+
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							"				</div> "+
							"			</div> "+
							"		</div> "+
						 	"       <div class='clearfix'></div>  "+
						 	"<div class='col-md-6 no-padding'>"+ 
							"		     <spring:message code='iplog.parsing.service.pathlist.file.pattern' var='tooltip'></spring:message>"+ 
							"		     <div class='form-group'>" +
							"			     <div class='table-cell-label'>${tooltip}</div>"+ 
							"			     <div class='input-group'>" +
							"				      <input tabindex='4' id='" + pathListCounter + "_fileNamePattern' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom' title='${tooltip}' value='"+fileNamePattern+"'/>"+ 
							"				 	  <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>" +
							"			     </div>" +
							"            </div>" +
							"       </div>" +
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='distribution.service.ftp.pathlist.file.prefix.r' var='label'></spring:message> "+
							"			<spring:message code='distribution.service.ftp.pathlist.file.prefix.r.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_readFilenamePrefix' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readFilePrefix+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+ 
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='distribution.service.ftp.pathlist.file.suffix.r' var='label'></spring:message> "+
							"			<spring:message code='distribution.service.ftp.pathlist.file.suffix.r.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_readFilenameSuffix' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readFileSuffix+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='distribution.service.ftp.pathlist.file.suffix.for.db.stats' var='label'></spring:message> "+
							"			<spring:message code='distribution.service.ftp.pathlist.file.suffix.for.db.stats.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_dbReadFileNameExtraSuffix' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+dbReadFileNameExtraSuffix+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='distribution.service.ftp.pathlist.file.contain' var='label'></spring:message> "+
							"			<spring:message code='distribution.service.ftp.pathlist.file.contain.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_readFilenameContains' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+fileContain+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='distribution.service.ftp.pathlist.exc.file.type' var='label'></spring:message> "+
							"			<spring:message code='distribution.service.ftp.pathlist.exc.file.type.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_readFilenameExcludeTypes' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+fileExcludeType+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
								
									// add New Pathlist : device Name
							        "<div class='col-md-6 no-padding'> "+
									"	<spring:message code='device.list.grid.device.name.label' var='label'></spring:message> "+ 
									"	<spring:message code='device.list.grid.device.name.label' var='tooltip'></spring:message> "+ 
									"	<div class='form-group'>  "+
									"		<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
									"		<div class='input-group '> "+
									"			<input type='hidden' class='form-control table-cell input-sm' id='"+pathListCounter+"_deviceId'  data-toggle='tooltip' data-placement='bottom' value='"+parentDeviceId+"' title='${tooltip }'>"+
									"			</input>"+
									"           <a href='#' onclick='selectDevicePopUp("+pathListCounter+");' tabindex='9' style='vertical-align:middle;' id='select_popup_"+ pathListCounter +"_lnk'><i style='margin-top: 8px;margin-left: 8px;' class='glyphicon glyphicon-search'></i></a>" +
									"			<input type='text' value='"+parentDevice+"' class='form-control table-cell input-sm' id='"+pathListCounter+"_parentDevice'  data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' style='width:94%;' readonly=true >"+
									"			</input>"+
									"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
									"		</div> "+
									"	</div> "+
									"</div> "+
									// add New Pathlist : reference devicename
									/* "<div class='col-md-6 no-padding'>"+
							        "	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.reference.devicename' var='label'></spring:message>"+
							        "	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.reference.devicename.tooltip' var='tooltip'></spring:message>"+
							        "  	<div class='form-group'>"+
							        "    	<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
							        "      	<div class='input-group '>"+
							        "      		<input class='form-control table-cell input-sm' id='" + pathListCounter + "_referenceDevice' value='"+referenceDevice+"' tabindex='10' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' />"+
									"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							        "        </div>"+
							        "     </div>"+
							        "</div>"+ */
							
							
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='distribution.service.ftp.pathlist.write.path.loc' var='label'></spring:message> "+ 
							"			<spring:message code='distribution.service.ftp.pathlist.write.path.loc.tooltip' var='tooltip'></spring:message> "+ 
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_writeFilePath' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+writePath+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							" <c:if test='${driverTypeAlias ne "DATABASE_DISTRIBUTION_DRIVER"}'>"+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='distribution.service.pathlist.file.write.mode.label' var='label'></spring:message> "+
							"			<spring:message code='distribution.service.pathlist.file.write.mode.label.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<select class='form-control table-cell input-sm' id='" + pathListCounter + "_compressOutFileEnabled' tabindex='4' data-toggle='tooltip' data-placement='top'  title='${tooltip }'>"+
						    "				    	<c:forEach var='trueFalseEnum' items='${truefalseEnum}' >"+
							"							<option value='${trueFalseEnum.name}'>${trueFalseEnum}</option>"+
							"				    	</c:forEach>"+
							"					</select>"+
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							"				</div> "+
							"			</div> "+
							"		</div> "+ 
							"       <div class='clearfix'></div>  "+
							"</c:if>"+
							" <c:if test='${driverTypeAlias ne "DATABASE_DISTRIBUTION_DRIVER"}'>"+
							"			<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
						    "					<spring:message code='ftp.driver.mgmt.pathlist.file.date.header' var='tooltip'></spring:message>"+
						    "					<label style='width: 100%;background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
						    " 				</div>"+
						    "				<div class='col-md-6 no-padding'>"+
						    "					<spring:message code='ftp.driver.mgmt.pathlist.filed.date.enable.label' var='label'></spring:message>"+
						    "					<spring:message code='ftp.driver.mgmt.pathlist.filed.date.enable.tooltip' var='tooltip'></spring:message>"+
						    "  				<div class='form-group'>"+
						    "    					<div class='table-cell-label'>${label}</div>"+
						    "      				<div class='input-group '>"+
							"		        			<select class='form-control table-cell input-sm' id='"+pathListCounter+"_fileGrepDateEnabled' onchange=changeFileDateParam(\'"+pathListCounter+"\'); tabindex='12' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
							"				    			<c:forEach var='trueFalseEnum' items='${truefalseEnum}' >"+
							"									<option value='${trueFalseEnum.name}'>${trueFalseEnum}</option>"+
							"				    			</c:forEach>"+
						    "   						</select>" +
							 "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
						    "        				</div>"+
						    "     				</div>"+
						    "				</div>"+
						    "   			<div class='col-md-6 no-padding'>"+
						    "					<spring:message code='ftp.driver.mgmt.pathlist.date.format.label' var='label'></spring:message>"+
						    "					<spring:message code='ftp.driver.mgmt.pathlist.date.format.tooltip' var='tooltip'></spring:message>"+
						    "  				<div class='form-group'>"+
						    "    					<div class='table-cell-label'>${label}</div>"+
						    "      				<div class='input-group '>"+
						    "      					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_dateFormat' tabindex='13' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+dateFormat+"'/>"+
							 "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
						    "        				</div>"+
						    "     				</div>"+
						    "				</div>"+
						    "				<div class='col-md-6 no-padding'>"+
						    "					<spring:message code='ftp.driver.mgmt.pathlist.position.label' var='label'></spring:message>"+
						    "					<spring:message code='ftp.driver.mgmt.pathlist.position.tooltip' var='tooltip'></spring:message>"+
						    "  				<div class='form-group'>"+
						    "    					<div class='table-cell-label'>${label}</div>"+
						    "      				<div class='input-group '>"+
							 "  						<select class='form-control table-cell input-sm' id='"+pathListCounter+"_position' tabindex='14' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
						    "   						<c:forEach var='positionEnum' items='${positionEnum}'>"+
						    "    							<option value='${positionEnum.value}'>${positionEnum}</option>" +
						    "    						</c:forEach>" +
						    "   						</select>" +
							 "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
						    "        				</div>"+
						    "    				</div>"+
						    "				</div>"+
						    "				<div class='col-md-6 no-padding'>"+
						    "					<spring:message code='ftp.driver.mgmt.pathlist.start.idx.label' var='label'></spring:message>"+
						    "					<spring:message code='ftp.driver.mgmt.pathlist.start.idx.tooltip' var='tooltip'></spring:message>"+
						    "  				<div class='form-group'>"+
						    "    					<div class='table-cell-label'>${label}</div>"+
						    "      				<div class='input-group '>"+
						    "      					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_startIndex' onkeydown=isNumericOnKeyDown(event); tabindex='15' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+startIndex+"' />"+
							 "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
						    "       				</div>"+
						    "   				</div>"+
						    "				</div>"+
						    "				<div class='col-md-6 no-padding'>"+
						    "					<spring:message code='ftp.driver.mgmt.pathlist.end.idx.label' var='label'></spring:message>"+
						    "					<spring:message code='ftp.driver.mgmt.pathlist.end.idx.tooltip' var='tooltip'></spring:message>"+
						    "  				<div class='form-group'>"+
						    "    					<div class='table-cell-label'>${label}</div>"+
						    "      				<div class='input-group '>"+
						    "      					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_endIndex' onkeydown=isNumericOnKeyDown(event); tabindex='16' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+endIndex+"' />"+
						    "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
						    "       				</div>"+
						    "   				</div>"+
						    "				</div>"+
						    "</c:if>"+
							"		<div class='col-md-12 no-padding'>"+
						    "  			<div class='form-group'>"+
						    "      			<div id='" + pathListCounter + "_buttons-div' class='input-group '>"+
							"					<sec:authorize access='hasAuthority(\'CREATE_PATHLIST\')'>"+	
				    		"						<button type='button' class='btn btn-grey btn-xs ' id='" + pathListCounter + "_savebtn' onclick=addDistributionPathlist(\'"+pathListCounter+"\');><spring:message code='btn.label.save'></spring:message></button>&nbsp;" +
				    		"					</sec:authorize>"+
						    "					<sec:authorize access='hasAuthority(\'UPDATE_PATHLIST\')'>"+
							"						<button type='button' class='btn btn-grey btn-xs ' id='" + pathListCounter + "_updatebtn' onclick=updateDistributionPathList(\'"+pathListCounter+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
							"					</sec:authorize>"+	
				            "					<button type='button' class='btn btn-grey btn-xs ' id='" + pathListCounter + "_resetbtn' onclick=resetPathlistParams(\'"+pathListCounter+"\');><spring:message code='btn.label.reset'></spring:message></button>&nbsp;" +
						    "       		</div>"+
					        "				<div id='" + pathListCounter + "_progress-bar-div' class='input-group' style='display: none;'> "+
					        "					<label>Processing Request &nbsp;&nbsp; </label> "+
					        "						<img src='img/processing-bar.gif'>"+
						    "				</div> "+
						    "   		</div>" +
						    "		</div>" +	
						    " <c:if test='${driverTypeAlias ne "DATABASE_DISTRIBUTION_DRIVER"}'>"+
						    "		<div class='col-md-12 no-padding' id='"+pathListCounter+"_grid_links_div'> "+ 
							"			<div class='title2'> "+
							"				<spring:message code='parsing.pathlist.plugin.grid.header' ></spring:message>"+ 
							"					<span class='title2rightfield'> "+
							"		          		<span class='title2rightfield-icon1-text'> "+
							"					<sec:authorize access='hasAuthority(\'CREATE_COMPOSER\')'>"+	
							"		          			<a href='#' onclick=openAddComposerPluginPopup('"+pathListCounter+"');><i class='fa fa-plus-circle'></i></a> "+
							"	  						<a href='#' id='addComposerPlugin_btn' onclick=openAddComposerPluginPopup('"+pathListCounter+"');> "+
							"	  							<spring:message code='btn.label.add' ></spring:message>  "+
							"	  						</a> "+
							"					</sec:authorize>"+
							"	      				</span>	  "+
							"		          		<span class='title2rightfield-icon1-text'> "+
							"					<sec:authorize access='hasAuthority(\'DELETE_COMPOSER\')'>"+	
							"		          			<a href='#' onclick=displayDeletePluginPopup('"+pathListCounter+"');> <i class='fa fa-trash'></i></a> "+
							"		          			<a href='#' id='deleteComposerPlugin' onclick=displayDeletePluginPopup('"+pathListCounter+"');><spring:message code='btn.label.delete' ></spring:message></a> "+
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
							"</c:if>"+
							"	</div> "+
							"</div> "+
							"</div> ";
							
					$('#pathList').prepend(pahtlistHtmlBody);
					
					$("#"+pathListCounter+"_savebtn").hide();
					$("#"+pathListCounter+"_updatebtn").hide();
					
					
					if(fileGroupEnable.toString() == 'true'){
						$("#"+pathListCounter+"_archivePath").prop('readonly', false);
					}else{
						$("#"+pathListCounter+"_archivePath").prop('readonly', true);
					}
					
					if(actionType == 'UPDATE'){

						$("#"+ pathListCounter+"_compressInFileEnabled").val(readFileMode.toString());
						$("#"+ pathListCounter+"_compressOutFileEnabled").val(writeFileMode.toString());
						loadComposerPluginGrid(pathListCounter,composerList, name);
						$("#"+pathListCounter+"_updatebtn").show();
						$("#"+pathListCounter+"_grid_main_div").show();
						$("#"+pathListCounter+"_grid_links_div").show();
						$("#title_"+pathListCounter).html(name + ' - ' + pathId);
						$("#"+pathListCounter+"_block").collapse("toggle");
						$("#"+ pathListCounter+"_fileGrepDateEnabled").val(fileGrepDateEnabled.toString());
						$("#"+ pathListCounter+"_position").val(position.toString());
						
					}else{
						$("#"+pathListCounter+"_savebtn").show();
						$("#"+pathListCounter+"_grid_main_div").hide();
						$("#"+pathListCounter+"_grid_links_div").hide();
						
						$("#"+pathListCounter+"_maxFilesCountAlert").val('${pathlist_form_bean.maxFilesCountAlert}');
						$("#"+ pathListCounter+"_compressInFileEnabled").val('${pathlist_form_bean.compressInFileEnabled}');
						$("#"+ pathListCounter+"_compressOutFileEnabled").val('${pathlist_form_bean.compressOutFileEnabled}');
						
						$("#title_"+pathListCounter).html("<spring:message code='distribution.pathlist.block.header.label' ></spring:message>");
						
					}
					changeFileDateParam(pathListCounter);
 }
 function disableDefaultValue(charRenameOperationCounter){
		if ($("#"+ charRenameOperationCounter + "_defaultValue").val() != "" || $("#"+ charRenameOperationCounter + "_defaultValue").val().length > 0) {
		 
		   $("#"+ charRenameOperationCounter + "_char_startIndex").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_char_endIndex").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_position").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_positionCharForm").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_length").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_paddingType").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_paddingValue").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_query").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_cacheEnable").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_char_startIndex").val('');
		   $("#"+ charRenameOperationCounter + "_char_endIndex").val('');
		   $("#"+ charRenameOperationCounter + "_length").val('');
		   $("#"+ charRenameOperationCounter + "_paddingValue").val('');
		   $("#"+ charRenameOperationCounter + "_query").val('');
		   
		} else {
		   $("#"+ charRenameOperationCounter + "_char_startIndex").prop('disabled',false);
		   $("#"+ charRenameOperationCounter + "_char_endIndex").prop('disabled',false);
		   $("#"+ charRenameOperationCounter + "_positionCharForm").prop('disabled',false);
		   $("#"+ charRenameOperationCounter + "_length").prop('disabled',false);
		   $("#"+ charRenameOperationCounter + "_paddingType").prop('disabled',false);
		   $("#"+ charRenameOperationCounter + "_paddingValue").prop('disabled',false);
		   $("#"+ charRenameOperationCounter + "_query").prop('disabled',false);
		   $("#"+ charRenameOperationCounter + "_cacheEnable").prop('disabled',false);
		   $("#"+ charRenameOperationCounter + "_position").prop('disabled',false);
		}	   
	}
 function disableIndex(charRenameOperationCounter){
		if ($("#"+ charRenameOperationCounter + "_char_startIndex").val() != "" || $("#"+ charRenameOperationCounter + "_char_startIndex").val().length > 0 || $("#"+ charRenameOperationCounter + "_length").val() != "" || $("#"+ charRenameOperationCounter + "_defaultValue").val().length > 0) {
		   $("#"+ charRenameOperationCounter + "_defaultValue").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_defaultValue").val('');
		} else {
		   $("#"+ charRenameOperationCounter + "_defaultValue").prop('disabled',false);	
		}
}
 function disableQuery(charRenameOperationCounter){
		if ($("#"+ charRenameOperationCounter + "_query").val() != "" || $("#"+ charRenameOperationCounter + "_defaultValue").val().length > 0) {
			   $("#"+ charRenameOperationCounter + "_defaultValue").prop('disabled',true);   
			   $("#"+ charRenameOperationCounter + "_positionCharForm").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_defaultValue").val('');
			   $("#"+ charRenameOperationCounter + "_dateFormatCharRename").val('');
			} else {
			   $("#"+ charRenameOperationCounter + "_defaultValue").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_positionCharForm").prop('disabled',false);
			}
	}
 function disablePadding(charRenameOperationCounter){
		if ($("#"+ charRenameOperationCounter + "_char_startIndex").val() != "" || $("#"+ charRenameOperationCounter + "_char_startIndex").val().length > 0 || $("#"+ charRenameOperationCounter + "_length").val() != "" || $("#"+ charRenameOperationCounter + "_defaultValue").val().length > 0) {
		   $("#"+ charRenameOperationCounter + "_defaultValue").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_defaultValue").val('');
		} else {
		   $("#"+ charRenameOperationCounter + "_defaultValue").prop('disabled',false);	
		}
}
</script>
