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

<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-multiselect.js"></script>
<script src="${pageContext.request.contextPath}/customJS/autoSuggestionForUnifiedField.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-multiselect.css" type="text/css"/>

<style>
  .ui-autocomplete
   {
       z-index:4000 !important;
       max-height: 100px;
       overflow-y: auto;
       overflow-x: hidden;
   }
 .customLink:hover{
 	color:white;!important
 }
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
</style>
<script>
	var pathlist = [];
	var charRenameList  = [];
	var fileGroupEnable = '${fileGroupEnable}';
	
</script>

<div class="tab-pane<c:if test="${(REQUEST_ACTION_TYPE eq 'PROCESSING_PATHLIST_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" id="pathListConfig">
	<div class="tab-content no-padding">
		<div class="fullwidth mtop10">
	   		<div class="title">
	   			<strong><spring:message code="distribution.service.ftp.pathlist.tab.header"></spring:message></strong>
	   			<span class="title2rightfield"> 
				    <span class="title2rightfield-icon1-text">
	   				<sec:authorize access="hasAuthority('CREATE_PATHLIST')">	
	   					<a href="#" class="customLink" onclick="addPathlist('','','','','','','','','','','','','','ADD', -1, '','',true ,'yyyyMMddHHmm', '-1', '-1', '', '', '', '','',false,'','',5000,'','','1','');"><i class="fa fa-plus-circle"></i></a>
	          			<a href="#" class="customLink" id="addPathList" onclick="addPathlist('','','','','','','','','','','','','','ADD', -1, '', '', true,'yyyyMMddHHmm', '-1', '-1', '', '', '', '','',false,'','',5000,'','','1','');">
	   					<spring:message code="parsing.pathlist.add.pathlist.label"></spring:message></a>
	   				</sec:authorize>
	   				</span> 
	   			</span> 
	   		</div>
	   		<div class="clearfix"></div>
        </div>
        <jsp:include page="policydialog.jsp"></jsp:include>
		<div class="fullwidth">
			<div id="pathList"></div>
		</div>
	</div>
</div>

<!-- Pathlist Delete popup code start here -->
    	<div id="divMessage" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10" >
		           <h4 class="modal-title" id="status-title" ><spring:message code="ftp.driver.pathlist.delete.header.label"></spring:message></h4>
		        </div>
		        
		        <div class="modal-body padding10 inline-form">
		        	<span id="deleteDriverResponseMsg" style="display:none;">
		        		<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
		        		<jsp:include page="../collection/ftp/deviceDialog.jsp"></jsp:include>
		        	</span>
			        <p id="deleteWarningMessage">
			       		 <spring:message code="pahtlist.delete.warn.message"></spring:message>
			        </p>
		        </div>
		        
		       <!-- To Do add role here -->
		       <sec:authorize access="hasAuthority('DELETE_PATHLIST')">
			        <div id="inactive-driver-div" class="modal-footer padding10">
			            <button type="button" class="btn btn-grey btn-xs " id = "delete_pathlist" onclick="deleteProcessingPathList();"><spring:message code="btn.label.yes"></spring:message></button>
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
		
<script type="text/javascript">
var pathListCounter = -1;
$(document).ready(function() {
	pathlist = eval('${pathList}');
	if(pathlist != 'undefined' && pathlist != null  ){
		$.each(pathlist, function(index,path){
			console.log(path);
			console.log(path.pathId);
			
			addPathlist(path.id, path.name, path.readPath, path.maxFileCountAlert, path.isReadCompressEnable,
					path.readFilePrefix, path.readFileSuffix, path.fileNameContains, path.excludeFileType, 
					path.writePath, path.isWriteCompressEnable, path.archivePath, path.composerWrapper,'UPDATE',
					path.policyId, path.policyName, path.fileGrepDateEnabled, path.writeCdrHeaderFooterEnabled ,path.dateFormat, path.startIndex, 
					path.endIndex, path.position, path.pathId, path.deviceName, path.deviceId, path.referenceDeviceName,
					path.duplicateRecordPolicyEnabled,path.unifiedFields,
					path.duplicateRecordPolicyType,path.acrossFileDuplicateCDRCacheLimit,path.acrossFileDuplicateDateField,
					path.acrossFileDuplicateDateIntervalType,path.acrossFileDuplicateDateInterval,path.acrossFileDuplicateDateFieldFormat);
		});	
	}
	
});
</script>

<script type="text/javascript">
 function addPathlist(id,name,readFilePath,maxCountAlert,readFileMode,readFilePrefix,readFileSuffix,
		 fileContain,fileExcludeType,writePath,writeFileMode,archivePath,composerList, actionType, 
		 policyId, policyName, fileGrepDateEnabled, writeCdrHeaderFooterEnabled,dateFormat, startIndex, endIndex, position, pathId, deviceName, deviceId, referenceDeviceName,
		 duplicateRecordPolicyEnabled,unifiedFields,
		 duplicateRecordPolicyType,acrossFileDuplicateCDRCacheLimit,acrossFileDuplicateDateField,
		 acrossFileDuplicateDateIntervalType,acrossFileDuplicateDateInterval,acrossFileDuplicateDateFieldFormat){
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
	 if(String(archivePath) === 'null' || String(archivePath) === 'undefined'){
		 archivePath = '';
	 } 
	 
	 if(String(referenceDeviceName) === 'null' || String(referenceDeviceName) === 'undefined'){
		 referenceDeviceName = '';
	 } 
	 
	 
	 var pahtlistHtmlBody = "<div class='box box-warning' id='flipbox_"+pathListCounter+"'>  "+
							"<input type='hidden' id='"+pathListCounter+"_pathListId' value='"+id+"'> "+
							"<input type='hidden' id='"+pathListCounter+"_pathId' value='"+pathId+"'> "+ 
							"<div class='box-header with-border'> "+
							"	<h3 class='box-title' id='title_"+pathListCounter+"'>" +name+ " - " + pathId +											
							"	</h3>  "+
							"	<div class='box-tools pull-right' style='top: -3px;' id='action_"+pathListCounter+"'> "+ 
							"		<button class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' id = 'expand_"+pathListCounter+"' href='#"+pathListCounter+"_block'> "+ 
							"			<i class='fa fa-minus'></i> "+
							"		</button> "+
							"					<sec:authorize access='hasAuthority(\'DELETE_PATHLIST\')'>"+
						    "			&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;' id = 'delete_pathlist_"+pathListCounter+"' onclick=deletePathListDetails(\'"+pathListCounter+"\');></a>&nbsp;" +
						    "					</sec:authorize>"+
							"	</div> "+
							"</div> "+
							"<div class='box-body inline-form accordion-body collapsed in' id='"+pathListCounter+"_block'> "+ 
							"	<div class='fullwidth inline-form'>  "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='processingService.pathList.name.label' var='label'></spring:message> "+ 
							"			<spring:message code='processingService.pathList.name.tooltip' var='tooltip'></spring:message> "+ 
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_name' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+name+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='processingService.pathList.policy.name.label' var='label'></spring:message> "+ 
							"			<spring:message code='processingService.pathList.policy.name.tooltip' var='tooltip'></spring:message> "+ 
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<input type='hidden' class='form-control table-cell input-sm' id='"+pathListCounter+"_policyId' tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+policyId+"'>"+
							"					</input>"+
							"					<input type='text' class='form-control table-cell input-sm' id='"+pathListCounter+"_policy' tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' style='width:95%;' readonly=true value='"+policyName+"'>"+
							"					</input>"+
							"                   <a href='#' onclick='selectPolicyPopUp("+pathListCounter+");' style='vertical-align:middle;' id='select_popup_"+ pathListCounter +"_lnk'><i class='fa fa-list-ul'></i></a>" +
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='processingService.pathList.readFilePath.label' var='label'></spring:message> "+ 
							"			<spring:message code='processingService.pathList.readFilePath.tooltip' var='tooltip'></spring:message> "+ 
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_readFilePath' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readFilePath+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='processingService.pathlist.max.file.cnt.alert.label' var='label'></spring:message> "+
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
							"			<spring:message code='processingService.pathlist.file.read.mode.label' var='label'></spring:message> "+
							"			<spring:message code='processingService.pathlist.file.read.mode.tooltip' var='tooltip'></spring:message> "+
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
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='processingService.pathlist.file.prefix.r.label' var='label'></spring:message> "+
							"			<spring:message code='processingService.pathlist.file.prefix.r.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_readFilenamePrefix' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readFilePrefix+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+ 
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='processingService.pathlist.file.suffix.r.label' var='label'></spring:message> "+
							"			<spring:message code='processingService.pathlist.file.suffix.r.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_readFilenameSuffix' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readFileSuffix+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='processingService.pathlist.file.contain.label' var='label'></spring:message> "+
							"			<spring:message code='processingService.pathlist.file.contain.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_readFilenameContains' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+fileContain+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='processingService.pathlist.exc.file.type.label' var='label'></spring:message> "+
							"			<spring:message code='processingService.pathlist.exc.file.type.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_readFilenameExcludeTypes' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+fileExcludeType+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='processingService.pathlist.write.path.loc.label' var='label'></spring:message> "+ 
							"			<spring:message code='processingService.pathlist.write.path.loc.tooltip' var='tooltip'></spring:message> "+ 
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_writeFilePath' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+writePath+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							
							// device Name
					        "		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='device.list.grid.device.name.label' var='label'></spring:message> "+ 
							"			<spring:message code='device.list.grid.device.name.label' var='tooltip'></spring:message> "+ 
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
							"				<div class='input-group '> "+
							"					<input type='hidden' class='form-control table-cell input-sm' value='"+deviceId+"' id='"+pathListCounter+"_deviceId'  data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
							"					</input>"+
							"                   <a href='#' onclick='selectDevicePopUp("+pathListCounter+");' tabindex='9' style='vertical-align:bottom;' id='select_popup_"+ pathListCounter +"_lnk'><i style='margin-top: 8px;margin-left: 8px;' class='glyphicon glyphicon-search'></i></a>" +
							"					<input type='text' class='form-control table-cell input-sm' value='"+deviceName+"' id='"+pathListCounter+"_parentDevice' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' style='width:94%;' readonly=true >"+
							"					</input>"+
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							"				</div> "+
							"			</div> "+
							"		</div> "+
							//reference devicename
							/* "		<div class='col-md-6 no-padding'>"+
					        "			<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.reference.devicename' var='label'></spring:message>"+
					        "			<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.reference.devicename.tooltip' var='tooltip'></spring:message>"+
					        "  			<div class='form-group'>"+
					        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
					        "      			<div class='input-group '>"+
					        "      				<input class='form-control table-cell input-sm' value='"+referenceDeviceName+"' id='" + pathListCounter + "_referenceDevice'  tabindex='10' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' />"+
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					        "       		</div>"+
					        "     		</div>"+
					        "		</div>"+ */
							
							
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='processingService.pathlist.file.write.mode.label' var='label'></spring:message> "+
							"			<spring:message code='processingService.pathlist.file.write.mode.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<select class='form-control table-cell input-sm' id='" + pathListCounter + "_compressOutFileEnabled' tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
						    "				    	<c:forEach var='trueFalseEnum' items='${truefalseEnum}' >"+
							"							<option value='${trueFalseEnum.name}'>${trueFalseEnum}</option>"+
							"				    	</c:forEach>"+
							"					</select>"+
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							"				</div> "+
							"			</div> "+
							"		</div> "+ 
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
							    "			<div class='col-md-6 no-padding'>"+
							    "					<spring:message code='processingService.pathlist.enable.cdr.summary.label' var='label'></spring:message>"+
							    "					<spring:message code='processingService.pathlist.enable.cdr.summary.tooltip' var='tooltip'></spring:message>"+
							    "  				<div class='form-group'>"+
							    "    					<div class='table-cell-label'>${label}</div>"+
							    "      				<div class='input-group '>"+
								"		        			<select class='form-control table-cell input-sm' id='"+pathListCounter+"_writeCdrHeaderFooterEnabled' ; tabindex='12' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
								"				    			<c:forEach var='trueFalseEnum' items='${truefalseEnum}' >"+
								"									<option value='${trueFalseEnum.name}'>${trueFalseEnum}</option>"+
								"				    			</c:forEach>"+
							    "   						</select>" +
								 "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							    "        				</div>"+
							    "     				</div>"+
							    "				</div>"+
							    // Duplicate Record Detection Start
								"	<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
								"		<spring:message code='processingService.configuration.duplicate.record.detection' var='tooltip'></spring:message>"+
								"		<label style='width: 100%;background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
								" 	</div>" +
								
								"   <input type='hidden' value='"+unifiedFields+"' id='"+pathListCounter+"_hiddenUnifiedFields'/>" +
								
								"	<div class='col-md-6 no-padding'>"+
							    "		<spring:message code='processingService.configuration.duplicate.record.detection.enable' var='tooltip' ></spring:message> " +
							    "  		<div class='form-group'>"+
							    "   		<div class='table-cell-label'>${tooltip}</div>"+
							    "   		<div class='input-group '> "+
								"					<select class='form-control table-cell input-sm' id='" + pathListCounter + "_duplicatecheck-duplicateRecordPolicyEnabled' " + 
								"							tabindex='17' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' " + 
								"							onchange='enableDuplicateParams(this.value,"+pathListCounter+");'>"+
							    "				    	<c:forEach var='trueFalseEnum' items='${truefalseEnum}' >"+
								"							<option value='${trueFalseEnum.name}'>${trueFalseEnum}</option>"+
								"				    	</c:forEach>"+
								"					</select>"+
								"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
								"				</div> "+
							    "   	</div>"+
							    "	</div>"+
							    
								/* "	<div class='col-md-6 no-padding'>"+
							    "		<spring:message code='processingService.configuration.alert.id' var='tooltip' ></spring:message> " +
							    "  		<div class='form-group'>"+
							    "   		<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div>"+
							    "   		<div class='input-group '> "+
								"					<select class='form-control table-cell input-sm' id='" + pathListCounter + "_alertId' tabindex='18' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
							    "				    	<c:forEach var='alert' items='${alertList}' >"+
								"							<option value='${alert.alertId}'>${alert.name}</option>"+
								"				    	</c:forEach>"+
								"					</select>"+
								"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
								"				</div> "+
							    "   	</div>"+
							    "	</div>"+
							    
								"	<div class='col-md-6 no-padding'>"+
							    "		<spring:message code='processingService.configuration.alert.des' var='tooltip' ></spring:message> " +
							    "  		<div class='form-group'>"+
							    "   		<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div>"+
							    "   		<div class='input-group '> "+
								"					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_alertDescription' tabindex='19' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value= '"+alertDescription+"'/>"+
								"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
								"				</div> "+
							    "   	</div>"+
							    "	</div>"+  */
							    
								"	<div class='col-md-6 no-padding'>"+
							    "		<spring:message code='processingService.configuration.duplicate.check.on' var='tooltip' ></spring:message> " +
							    "		<spring:message code='processingService.configuration.duplicate.check.tooltipDesc' var='tooltipDesc' ></spring:message> " +
							    "  		<div class='form-group'>"+
							    "   		<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div>"+
							    "   		<div class='input-group '> "+
								/* "					<select tabindex='20' class='form-control table-cell input-sm' " +
								"						id='"+pathListCounter+"_unifiedFields_dropdown' data-toggle='tooltip' data-placement='bottom' " +
								"	 					title='${tooltip}' multiple='multiple' value='${unifiedFields}'> " +
								"							<c:forEach items='${unifiedFieldEnum}' var='unifiedField'> " +
								"								<option value='${unifiedField}' >${unifiedField}</option> " +
								"							</c:forEach> " +
								"					</select> " + */
								"<textarea id='"+pathListCounter+"_duplicateCheckUnifiedFields' data-toggle='tooltip' data-placement='bottom' title='${tooltipDesc}' class='md-textarea form-control' rows='3' onkeypress=triggerAutoSuggest(\'#"+pathListCounter+"_duplicateCheckUnifiedFields\','getAllUnifiedField') onBlur=validateDuplicateCheckUnifiedFields("+pathListCounter+")></textarea>"+
								"					<input type='hidden' id='"+pathListCounter+"_unifiedFields'>" +
								"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
								"			</div> "+
							    "   	</div>"+
							    "	</div>"+
							    
							    "	<div class='col-md-6 no-padding'>"+
							    "		<spring:message code='processingService.configuration.detection.type' var='tooltip' ></spring:message> " +
							    "  		<div class='form-group'>"+
							    "   		<div class='table-cell-label'>${tooltip}</div>"+
							    "   		<div class='input-group '> "+
								"					<select class='form-control table-cell input-sm' id='" + pathListCounter + "_duplicatecheck-duplicateRecordPolicyType' " +  
								"							tabindex='21' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' "+
								"							onchange='enableAcrossFileParams(this.value,"+pathListCounter+");'>"+
							    "				    	<c:forEach var='duplicateRecordPolicyType' items='${duplicateRecordPolicyType}' >"+
								"							<option value='${duplicateRecordPolicyType}'>${duplicateRecordPolicyType}</option>"+
								"				    	</c:forEach>"+
								"					</select>"+
								"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
								"			</div> "+
							    "   	</div>"+
							    "	</div>"+
								//Across fileParam Start
							"	<div id='"+pathListCounter+"_acrossFileParams'> " +
 								"	<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
								"		<spring:message code='processingService.configuration.duplicate.check.across.file.params' var='tooltip'></spring:message>"+
								"		<label style='width: 100%;background: none repeat scroll 0 0;height:25px;padding-top:4px;'>${tooltip}</label>"+
								" 	</div>" +
								
								"	<div class='col-md-6 no-padding'>"+
							    "		<spring:message code='processingService.configuration.cache.limit' var='tooltip' ></spring:message> " +
							    "  		<div class='form-group'>"+
							    "   		<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div>"+
							    "   		<div class='input-group '> "+
								"					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_acrossFileDuplicateCDRCacheLimit' tabindex='22' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+acrossFileDuplicateCDRCacheLimit+"'/>"+
								"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
								"				</div> "+
							    "   	</div>"+
							    "	</div>"+
							    
							    "	<div class='col-md-6 no-padding'>"+
							    "		<spring:message code='processingService.configuration.mapped.unified.field' var='tooltip' ></spring:message> " +
							    "  		<div class='form-group'>"+
							    "   		<div class='table-cell-label'>${tooltip}</div>"+
							    "   		<div class='input-group '> "+
							   /*  "					<select class='form-control table-cell input-sm' id='" + pathListCounter + "_duplicatecheck-acrossFileDuplicateDateField' tabindex='24' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
							    "				    	<c:forEach var='unifiedField' items='${unifiedField}' >"+
								"							<option value='${unifiedField}'>${unifiedField}</option>"+
								"				    	</c:forEach>"+
								"					</select>"+ */
								"					<input class='form-control table-cell input-sm' onkeydown=loadUnfiedAutoComplete('"+pathListCounter+"_duplicatecheck-acrossFileDuplicateDateField\','getAllUnifiedField') id='" + pathListCounter + "_duplicatecheck-acrossFileDuplicateDateField' tabindex='22' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' maxlength='100'/>"+
								"					<input type='hidden' id='"+pathListCounter+"_acrossFileDuplicateDateField'>" +
								"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
								"			</div> "+
							    "   	</div>"+
							    "	</div>"+
							    
							    "	<div class='col-md-6 no-padding'>"+
							    "		<spring:message code='processingService.configuration.date.field.format' var='tooltip' ></spring:message> " +
							    "  		<div class='form-group'>"+
							    "   		<div class='table-cell-label'>${tooltip}</div>"+
							    "   		<div class='input-group '> "+
								"					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_acrossFileDuplicateDateFieldFormat' tabindex='26' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+acrossFileDuplicateDateFieldFormat+"'/>"+
								"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
								"			</div> "+
							    "   	</div>"+
							    "	</div>"+
							    
							    
							    "	<div class='col-md-6 no-padding'>"+
							    "		<spring:message code='processingService.configuration.interval.type' var='tooltip' ></spring:message> " +
							    "  		<div class='form-group'>"+
							    "   		<div class='table-cell-label'>${tooltip}</div>"+
							    "   		<div class='input-group '> "+
								"					<select class='form-control table-cell input-sm' id='" + pathListCounter + "_duplicatecheck-acrossFileDuplicateDateIntervalType' tabindex='25' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
							    "				    	<c:forEach var='acrossFileDuplicateDateIntervalType' items='${acrossFileDuplicateDateIntervalType}' >"+
								"							<option value='${acrossFileDuplicateDateIntervalType}'>${acrossFileDuplicateDateIntervalType}</option>"+
								"				    	</c:forEach>"+
								"					</select>"+
								"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
								"			</div> "+
							    "   	</div>"+
							    "	</div>"+
							    
							    "	<div class='col-md-6 no-padding'>"+
							    "		<spring:message code='processingService.configuration.check.interval' var='tooltip' ></spring:message> " +
							    "  		<div class='form-group'>"+
							    "   		<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div>"+
							    "   		<div class='input-group '> "+
								"					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_acrossFileDuplicateDateInterval' tabindex='26' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+acrossFileDuplicateDateInterval+"'/>"+
								"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
								"				</div> "+
							    "   	</div>"+
							    "	</div>"+
							    
							"	</div> " + //Across fileParam End
							    
								// Duplicate Record Detection End
								
							
							"		<div class='col-md-12 no-padding'>"+
						    "  			<div class='form-group'>"+
						    "      			<div id='" + pathListCounter + "_buttons-div' class='input-group '>"+
							"					<sec:authorize access='hasAuthority(\'CREATE_PATHLIST\')'>"+	
				    		"						<button type='button' class='btn btn-grey btn-xs ' id='" + pathListCounter + "_savebtn' onclick=createProcessingPathList(\'"+pathListCounter+"\');><spring:message code='btn.label.save'></spring:message></button>&nbsp;" +
				    		"					</sec:authorize>"+
						    "					<sec:authorize access='hasAuthority(\'UPDATE_PATHLIST\')'>"+
							"						<button type='button' class='btn btn-grey btn-xs ' id='" + pathListCounter + "_updatebtn' onclick=updateProcessingPathList(\'"+pathListCounter+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
							"					</sec:authorize>"+	
				            "					<button type='button' class='btn btn-grey btn-xs ' onclick=resetPathlistParams(\'"+pathListCounter+"\');><spring:message code='btn.label.reset'></spring:message></button>&nbsp;" +
						    "       		</div>"+
					        "				<div id='" + pathListCounter + "_progress-bar-div' class='input-group' style='display: none;'> "+
					        "					<label>Processing Request &nbsp;&nbsp; </label> "+
					        "						<img src='img/processing-bar.gif'>"+
						    "				</div> "+
						    "   		</div>" +
						    "		</div>" +
							"	</div> "+
							"</div> "+
							"</div> " ;
							
							
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
						$("#"+ pathListCounter+"_fileGrepDateEnabled").val(fileGrepDateEnabled.toString());
						$("#"+ pathListCounter+"_writeCdrHeaderFooterEnabled").val(writeCdrHeaderFooterEnabled.toString());
						$("#"+ pathListCounter+"_position").val(position.toString());
						$("#"+pathListCounter+"_updatebtn").show();
						$("#"+pathListCounter+"_grid_main_div").show();
						$("#"+pathListCounter+"_grid_links_div").show();
						$("#title_"+pathListCounter).html(name + " - " + pathId);
						$("#"+pathListCounter+"_block").collapse("toggle");
						$("#"+pathListCounter+"_policyId").val(policyId);
						$("#"+pathListCounter + "_duplicatecheck-duplicateRecordPolicyEnabled").val(duplicateRecordPolicyEnabled.toString());
						//$("#"+pathListCounter + "_alertId").val(alertId.toString());
						$("#"+pathListCounter + "_duplicatecheck-duplicateRecordPolicyType").val(duplicateRecordPolicyType.toString());
						if (acrossFileDuplicateDateField === "")
							acrossFileDuplicateDateField = $("#"+pathListCounter + "_duplicatecheck-acrossFileDuplicateDateField option:first").val();
						if (acrossFileDuplicateDateIntervalType === "")
							acrossFileDuplicateDateIntervalType = $("#"+pathListCounter + "_duplicatecheck-acrossFileDuplicateDateIntervalType option:first").val();
						$("#"+pathListCounter + "_duplicatecheck-acrossFileDuplicateDateField").val(acrossFileDuplicateDateField);
						$("#"+pathListCounter + "_duplicatecheck-acrossFileDuplicateDateIntervalType").val(acrossFileDuplicateDateIntervalType.toString());
						$("#"+pathListCounter + "_duplicatecheck-acrossFileDuplicateDateFieldFormat").val(acrossFileDuplicateDateFieldFormat);
						
					}else{
						$("#"+pathListCounter+"_savebtn").show();
						$("#"+pathListCounter+"_grid_main_div").hide();
						$("#"+pathListCounter+"_grid_links_div").hide();
						$("#"+ pathListCounter+"_compressInFileEnabled").val('true');
						$("#"+ pathListCounter+"_writeCdrHeaderFooterEnabled").val('true');
						$("#"+ pathListCounter+"_readFilenameSuffix").val('.gz');
						$("#title_"+pathListCounter).html("<spring:message code='distribution.pathlist.block.header.label' ></spring:message>");
					}
					changeFileDateParam(pathListCounter);
					$("#"+pathListCounter+"_duplicateCheckUnifiedFields").val(unifiedFields);
					//setDuplicateCheckOnUnifiedFields(pathListCounter);
					enableDuplicateParams($("#"+pathListCounter+"_duplicatecheck-duplicateRecordPolicyEnabled").find(":selected").val(),pathListCounter);
 }
 
 function setDuplicateCheckOnUnifiedFields(counter)
 {
	 $('#'+counter+'_unifiedFields_dropdown').multiselect({
	    	maxHeight: '200',
	        buttonWidth: 'auto',
	        nonSelectedText: 'Select',
	        nSelectedText: 'selected',
	        numberDisplayed: 4,
	        enableCaseInsensitiveFiltering: true
	    });
		var unifiedFields = $('#'+counter+'_hiddenUnifiedFields').val();
		var unifiedFieldsArr = unifiedFields.split(",");
		$('#'+counter+'_unifiedFields_dropdown').val(unifiedFieldsArr);
		$('#'+counter+'_unifiedFields_dropdown').multiselect("refresh");
	}
 
 function enableDuplicateParams(value,counter){
	 if(value=='false'){
			$('#'+counter+'_duplicateCheckUnifiedFields').attr('disabled',true);
			//$('#'+counter+'_unifiedFields_dropdown').multiselect('disable');
			//$('#'+counter+'_alertId').attr('disabled',true);
			//$('#'+counter+'_alertDescription').attr('disabled',true);
			$('#'+counter+'_duplicatecheck-duplicateRecordPolicyType option[value="IN_FILE"]').prop("selected", "selected");
			$('#'+counter+'_duplicatecheck-duplicateRecordPolicyType').attr('disabled',true);
			$('#'+counter+'_acrossFileParams').hide();
		} else {
			$('#'+counter+'_duplicateCheckUnifiedFields').attr('disabled',false);
			//$('#'+counter+'_alertId').attr('disabled',false);
			//$('#'+counter+'_alertDescription').attr('disabled',false);
			$('#'+counter+'_unifiedFields_dropdown').multiselect('enable');;
			$('#'+counter+'_duplicatecheck-duplicateRecordPolicyType').attr('disabled',false);
			enableAcrossFileParams($('#'+counter+'_duplicatecheck-duplicateRecordPolicyType').find(':selected').val(),counter);
		}
	}
 
 function enableAcrossFileParams(value,counter){
	if(value == 'ACROSS_FILE'){
		$('#'+counter+'_acrossFileParams').show();		
	}else{
		$('#'+counter+'_acrossFileParams').hide();
		}
 }
 
 function createProcessingPathList(counter){
	 createUpdateProcessingPathList(counter, 'ADD', '<%=ControllerConstants.CREATE_PROCESSING_SERVICE_PATH_LIST%>');
 }
 
 function updateProcessingPathList(counter){
	 createUpdateProcessingPathList(counter, 'UPDATE', '<%=ControllerConstants.UPDATE_PROCESSING_SERVICE_PATH_LIST%>');
 }
 
 function showProgressBar(pathListCounter){
	$('#' + pathListCounter + '_buttons-div').hide();
	$('#' + pathListCounter + '_progress-bar-div').show();
 }
 
 function hideProgressBar(pathListCounter){
		$('#' + pathListCounter + '_buttons-div').show();
		$('#' + pathListCounter + '_progress-bar-div').hide();
 }
 
 function validateDuplicateCheckUnifiedFields(counter){
	 var unifiedFields=$("#"+counter+"_duplicateCheckUnifiedFields").val();
	 unifiedFields = unifiedFields.replace(/ +/g, "");
		var lastChar = unifiedFields[unifiedFields.length -1];
		if(lastChar==','){
		 unifiedFields=unifiedFields.substring(0, unifiedFields.length - 1);
		}
		var myArray=unifiedFields.split(',');
		var myNewArray = myArray.filter(function(elem, index, self) {
		     return index === self.indexOf(elem);
		 });
     $("#"+counter+"_duplicateCheckUnifiedFields").val(myNewArray);		
}

 function createUpdateProcessingPathList(counter, action, actionURL) {
		resetWarningDisplay();
		clearAllMessages();
		showProgressBar(counter);
		var pathListId = '0';
		if(action == 'UPDATE'){
			pathListId = $('#' + counter + '_pathListId').val();
		}
		var maxFileCountAlert = $("#" + counter + "_maxFilesCountAlert").val();
		if (maxFileCountAlert === null || maxFileCountAlert === ''
				|| maxFileCountAlert === 'undefined ') {
			maxFileCountAlert = -1;
			$("#" + counter + "_maxFilesCountAlert").val(maxFileCountAlert);
		}
		
		var startIndex = $("#"+counter+"_startIndex").val();
		var endIndex = $("#"+counter+"_endIndex").val();
		if(startIndex === null || startIndex === '' || startIndex === 'undefined'){
			$("#"+counter+"_startIndex").val('-1')
		}
		if(endIndex === null || endIndex === '' || endIndex === 'undefined'){
			$("#"+counter+"_endIndex").val('-1')
		}
		if(isNaN($("#" + counter + "_policyId").val()) || $("#" + counter + "_policyId").val() === null || $("#"+counter + "_policy").val() === 'undefined' || $("#"+counter + "_policy").val() === ''){
			$("#" + counter + "_policyId").val('0');
		}
		var unifiedFields = $("#"+counter+"_duplicateCheckUnifiedFields").val();
		if (unifiedFields === null || unifiedFields === '' || unifiedFields === 'undefined')
				unifiedFields = "";
		
		var acrossFileDuplicateCDRCacheLimit = $("#"+counter+"_acrossFileDuplicateCDRCacheLimit").val();
		var acrossFileDuplicateDateInterval = $("#"+counter+"_acrossFileDuplicateDateInterval").val();
		if(acrossFileDuplicateCDRCacheLimit === null || acrossFileDuplicateCDRCacheLimit === '' || acrossFileDuplicateCDRCacheLimit === 'undefined'){
			$("#"+counter+"_acrossFileDuplicateCDRCacheLimit").val('-1')
		}
		if(acrossFileDuplicateDateInterval === null || acrossFileDuplicateDateInterval === '' || acrossFileDuplicateDateInterval === 'undefined'){
			$("#"+counter+"_acrossFileDuplicateDateInterval").val('-1')
		}
		var acrossFileDuplicateDateFieldFormat = $("#"+counter+"_acrossFileDuplicateDateFieldFormat").val();
		if (acrossFileDuplicateDateFieldFormat === null || acrossFileDuplicateDateFieldFormat === '' || acrossFileDuplicateDateFieldFormat === 'undefined')
			acrossFileDuplicateDateFieldFormat = "";
		
		$.ajax({	url : actionURL,
					cache : false,
					async : true,
					dataType : 'json',
					type : "POST",
					data : {
						"id"                       : pathListId,
						"name"                     : $("#" + counter + "_name").val(),
						"readFilePath"             : $("#" + counter + "_readFilePath").val(),
						"maxFilesCountAlert"       : maxFileCountAlert,
						"compressInFileEnabled"    : $("#" + counter + "_compressInFileEnabled").val(),
						"readFilenamePrefix" 	   : $("#" + counter + "_readFilenamePrefix").val(),
						"readFilenameSuffix"       : $("#" + counter + "_readFilenameSuffix").val(),
						"readFilenameContains"     : $("#" + counter + "_readFilenameContains").val(),
						"readFilenameExcludeTypes" : $("#" + counter + "_readFilenameExcludeTypes").val(),
						"writeFilePath"            : $("#" + counter + "_writeFilePath").val(),
						"compressOutFileEnabled"   : $("#" + counter + "_compressOutFileEnabled").val(),
						"archivePath"              : $("#" + counter + "_archivePath").val(),
						"service.id"			   : parseInt('${serviceId}'),
						"service.serverInstance.id": parseInt('${instanceId}'),
						"policy.id" 		       : parseInt($("#" + counter + "_policyId").val()),
						"fileGrepDateEnabled"      : $("#"+counter+"_fileGrepDateEnabled").val(),
						"writeCdrHeaderFooterEnabled"      : $("#"+counter+"_writeCdrHeaderFooterEnabled").val(),
						"position"			       : $("#"+counter+"_position").val(),
						"startIndex"		       : parseInt($("#"+counter+"_startIndex").val()),
						"endIndex"			       : parseInt($("#"+counter+"_endIndex").val()),
						"dateFormat"               : $("#"+counter+"_dateFormat").val(),
						"pathListCount"            : counter,
						"referenceDevice"          : $('#' + pathListCounter + '_parentDevice').val(),
						"parentDevice.id"          : $('#' + pathListCounter + '_deviceId').val(),
						"pathId"			       				: $('#'+counter+'_pathId').val(),
						"duplicateRecordPolicyEnabled"			: $("#"+counter+"_duplicatecheck-duplicateRecordPolicyEnabled").val(),
						//"alertId"			       				: $("#"+counter+"_alertId").val(),
						//"alertDescription"			       		: $("#"+counter+"_alertDescription").val(),
						"unifiedFields"			       			: unifiedFields,
						"duplicateRecordPolicyType"				: $("#"+counter+"_duplicatecheck-duplicateRecordPolicyType").val(),
						"acrossFileDuplicateCDRCacheLimit"		: $("#"+counter+"_acrossFileDuplicateCDRCacheLimit").val(),
						"acrossFileDuplicateDateField"			: $("#"+counter+"_duplicatecheck-acrossFileDuplicateDateField").val(),
						"acrossFileDuplicateDateFieldFormat"	: acrossFileDuplicateDateFieldFormat,
						"acrossFileDuplicateDateIntervalType"	: $("#"+counter+"_duplicatecheck-acrossFileDuplicateDateIntervalType").val(),
						"acrossFileDuplicateDateInterval"		: $("#"+counter+"_acrossFileDuplicateDateInterval").val()
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
							$('#title_' + counter).html(respObj['name'] +"-" + respObj['pathId']);

							$('#' + counter + '_grid_links_div').show();
							$('#' + counter + '_grid_main_div').show();

							//loadComposerPluginGrid(counter, []);

							$('#' + counter + '_updatebtn').show();
							$('#' + counter + '_savebtn').hide();
				 			$('#'+counter+'_pathId').val(respObj['pathId']);
				 			
							showSuccessMsg(responseMsg);

						} else if (responseObject !== undefined
								&& responseObject !== 'undefined'
								&& responseCode === "400") {
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
 
 
   function resetPathlistParams(counter){
	   resetWarningDisplay();
	   $("#" + counter + "_maxFilesCountAlert").val('');
	   $("#" + counter + "_name").val('');
	   $("#" + counter + "_readFilePath").val('');
	   $("#" + counter + "_readFilenamePrefix").val('');
	   $("#" + counter + "_readFilenameSuffix").val('');
	   $("#" + counter + "_readFilenameContains").val('');
	   $("#" + counter + "_readFilenameExcludeTypes").val('');
	   $("#" + counter + "_writeFilePath").val('');
	   $("#" + counter + "_archivePath").val('');
	   $("#" + counter + "_policyId").val('');
	   $("#" + counter + "_policy").val('');
	   $("#"+counter+"_fileGrepDateEnabled").val('false');
	   $("#"+counter+"_writeCdrHeaderFooterEnabled").val('true');
	   $("#"+counter+"_position").val('left')
	   $("#"+counter+"_startIndex").val('-1')
	   $("#"+counter+"_endIndex").val('-1')
	   $("#"+counter+"_dateFormat").val('yyyyMMddHHmm');
	   changeFileDateParam(counter);
	   $("#"+counter+"_duplicatecheck-duplicateRecordPolicyEnabled").val('false');
	   //$("#"+counter+"_alertId").val('');
	   //$("#"+counter+"_alertDescription").val('');
	   $('#'+counter+'_hiddenUnifiedFields').val('');
	   $('#'+counter+'_duplicateCheckUnifiedFields').val('');
	   $('#'+counter+'_duplicateCheckUnifiedFields').val('');
	   $('#'+counter+'_duplicatecheck-acrossFileDuplicateDateField').val('');
	   $('#'+counter+'_duplicatecheck-acrossFileDuplicateDateFieldFormat').val('');
	   //setDuplicateCheckOnUnifiedFields(counter);
	   enableDuplicateParams('false',counter);
   }
   
   function changeFileDateParam(pathListCounter){
		
		var fileDateEnable=$("#"+pathListCounter+"_fileGrepDateEnabled").find(":selected").val();
		
		if(fileDateEnable == 'false'){
			$("#"+pathListCounter+"_dateFormat").prop('readonly', true);
			$("#"+pathListCounter+"_position").prop('disabled', true);
			$("#"+pathListCounter+"_startIndex").prop('readonly', true);
			$("#"+pathListCounter+"_endIndex").prop('readonly', true);
		}else{
			$("#"+pathListCounter+"_dateFormat").prop('readonly', false);
			$("#"+pathListCounter+"_position").prop('disabled', false);
			$("#"+pathListCounter+"_startIndex").prop('readonly', false);
			$("#"+pathListCounter+"_endIndex").prop('readonly', false);
		}
		
	}
   
   function deletePathListDetails(counter) {

		var pathListId = $('#' + counter + '_pathListId').val();
		deleteBlockId = counter;

		if (pathListId === null || pathListId === 'null' || pathListId === '') {
			$("#flipbox_" + counter).remove();
		} else {
			$("#pathlist_delete_link").click();
		}
	}
   
   /* Function will remove processing pathlist */
   function deleteProcessingPathList() {
   	resetWarningDisplay();
   	clearAllMessages();
   	$
   			.ajax({
   				url : '<%=ControllerConstants.DELETE_PROCESSING_PATHLIST%>',
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
