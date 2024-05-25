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
<script>
	var pathlist = [];
	var charRenameList  = [];
	var fileGroupEnable = '${fileGroupEnable}';
	
</script>
<input type="hidden" name="serviceId" id="serviceId" value="${serviceId}"/>
			
<div class="tab-pane<c:if test="${(REQUEST_ACTION_TYPE eq 'AGGREGATION_PATHLIST_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" id="pathListConfig">
	<div class="tab-content no-padding">
		<div class="fullwidth mtop10">
	   		<div class="title">
	   			<strong><spring:message code="distribution.service.ftp.pathlist.tab.header"></spring:message></strong>
	   			<span class="title2rightfield"> 
				    <span class="title2rightfield-icon1-text">
	   				<%-- <sec:authorize access="hasAuthority('CREATE_PATHLIST')">	
	   					<a href="#" class="customLink" onclick="addPathlist('','','','','','','','','','','','ADD','yyyyMMddHHmm','-1','0','5','Agg{DDMMMYYYY_hh:mm:ss}','','-1','-1','');"><i class="fa fa-plus-circle"></i></a>
	          			<a href="#" class="customLink" id="addPathList" onclick="addPathlist('','','','','','','','','','','','ADD','yyyyMMddHHmm','-1','0','5','Agg{DDMMMYYYY_hh:mm:ss}','','-1','-1','');">
	   					<spring:message code="btn.label.add" ></spring:message></a>
	   				</sec:authorize> --%>
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
			            <button type="button" class="btn btn-grey btn-xs " id = "delete_pathlist" onclick="deleteAggregationPathList();"><spring:message code="btn.label.yes"></spring:message></button>
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
		if(pathlist.length == 0){
			addPathlist('','','','','','','','','','','','ADD','yyyyMMddHHmm','-1','1','12','Agg{yyyyMMddHHmmssSSS}','','1','100','Non_Agg{yyyyMMddHHmmssSSS}','','1','100','Agg_error{yyyyMMddHHmmssSSS}','','1','100','', '', '', '','false');
			changeFileDateParam(pathListCounter);
		}else{
			$.each(pathlist, function(index,path){
				console.log(path);
				console.log(path.pathId);
				
				addPathlist(path.id, path.name, path.readPath, path.isReadCompressEnable, 
						path.readFilePrefix, path.readFileSuffix,path.maxFileCountAlert,path.isWriteCompressEnable, 
						path.writePathAggregated,path.writePathNonAggregated, path.writePathAggregatedError,
						'UPDATE',
						path.dateFormat, path.position, path.startIndex, path.endIndex,
						path.outputfilename, path.outputfileseq, path.outputfileminrange, path.outputfilemaxrange,
						path.outputfilenamefornonagg,path.outputfileseqfornonagg,path.outputfileminrangefornonagg, path.outputfilemaxrangefornonagg,
						path.outputfilenameforerr, path.outputfileseqforerr, path.outputfileminrangeforerr,
						path.outputfilemaxrangeforerr,path.pathId, path.deviceName, path.deviceId, path.referenceDeviceName,path.fileGrepDateEnabled);
				changeFileDateParam(pathListCounter);
			});
		}
			
	}else{
		addPathlist('','','','','','','','','','','','ADD','yyyyMMddHHmm','-1','1','12','Agg{yyyyMMddHHmmssSSS}','','1','100','Non_Agg_{yyyyMMddHHmmssSSS}','','1','100','Agg_error{yyyyMMddHHmmssSSS}','','1','100','', '', '', '','false');
	}
});
</script>

<script type="text/javascript">

 function addPathlist(id,name,readFilePath,readFileMode,
		 readFilePrefix,readFileSuffix, maxCountAlert,writeFileMode,
		 writePathAggregation,writePathNonAggregation,writePathAggregationError, 
		 actionType, 
		 dateFormat,position, startIndex, endIndex, 
		 outputfilename,outputfileseq,outputfileminrange,outputfilemaxrange,
		 outputfilenamefornonagg,outputfileseqfornonagg,outputfileminrangefornonagg,outputfilemaxrangefornonagg,
		 outputfilenameforerr,outputfileseqforerr,outputfileminrangeforerr,outputfilemaxrangeforerr,
		 pathId, deviceName, deviceId, referenceDeviceName,fileGrepDateEnabled){
	 pathListCounter++;
	 if(String(readFilePrefix) === 'null' || String(readFilePrefix) === 'undefined'){
		 readFilePrefix = '';
	 }
	
	 if(String(readFileSuffix) === 'null' || String(readFileSuffix) === 'undefined'){
		 readFileSuffix = '';
	 }
	 if(String(writePathAggregation) === 'null' || String(writePathAggregation) === 'undefined'){
		 writePathAggregation = '';
	 }
	 if(String(writePathNonAggregation) === 'null' || String(writePathNonAggregation) === 'undefined'){
		 writePathNonAggregation = '';
	 }
	 if(String(writePathAggregationError) === 'null' || String(writePathAggregationError) === 'undefined'){
		 writePathAggregationError = '';
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
							"			<spring:message code='aggregationService.pathList.readFilePath.label' var='label'></spring:message> "+ 
							"			<spring:message code='aggregationService.pathList.readFilePath.tooltip' var='tooltip'></spring:message> "+ 
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_readFilePath' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readFilePath+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+							
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='aggregationService.pathlist.file.read.mode.label' var='label'></spring:message> "+
							"			<spring:message code='aggregationService.pathlist.file.read.mode.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
							"				<div class='input-group '>"+
							"					<select class='form-control table-cell input-sm' id='" + pathListCounter + "_compressInFileEnabled' tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
						    "				    	<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >"+
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
							"			<spring:message code='processingService.pathlist.file.write.mode.label' var='label'></spring:message> "+
							"			<spring:message code='processingService.pathlist.file.write.mode.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
							"				<div class='input-group '> "+
							"					<select class='form-control table-cell input-sm' id='" + pathListCounter + "_compressOutFileEnabled' tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
						    "				    	<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >"+
							"							<option value='${trueFalseEnum.name}'>${trueFalseEnum}</option>"+
							"				    	</c:forEach>"+
							"					</select>"+
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							"				</div> "+
							"			</div> "+
							"		</div> "+ 							
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='aggregationService.pathlist.write.path.aggregated.loc.label' var='label'></spring:message> "+ 
							"			<spring:message code='aggregationService.pathlist.write.path.aggregated.loc.tooltip' var='tooltip'></spring:message> "+ 
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_writeFilePath' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+writePathAggregation+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+	
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='aggregationService.pathlist.write.path.nonaggregated.loc.label' var='label'></spring:message> "+ 
							"			<spring:message code='aggregationService.pathlist.write.path.nonaggregated.loc.tooltip' var='tooltip'></spring:message> "+ 
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_wPathNonAggregate' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+writePathNonAggregation+"'/>"+ 
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
							"				</div> "+
							"			</div> "+
							"		</div> "+	
							"		<div class='col-md-6 no-padding'> "+
							"			<spring:message code='aggregationService.pathlist.write.path.aggregatederror.loc.label' var='label'></spring:message> "+ 
							"			<spring:message code='aggregationService.pathlist.write.path.aggregatederror.loc.tooltip' var='tooltip'></spring:message> "+ 
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<input type='text' id='"+pathListCounter+"_wPathAggregateError' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+writePathAggregationError+"'/>"+ 
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
					 		/*"<div class='col-md-6 no-padding'>"+
					        "	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.reference.devicename' var='label'></spring:message>"+
					        "	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.reference.devicename.tooltip' var='tooltip'></spring:message>"+
					        "  	<div class='form-group'>"+
					        "    	<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
					        "      	<div class='input-group '>"+
					        "      		<input class='form-control table-cell input-sm' value='"+referenceDeviceName+"' id='" + pathListCounter + "_referenceDevice'  tabindex='10' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' />"+
							"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					        "        </div>"+
					        "     </div>"+
					        "</div>"+ */
							
							
							    "			<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
							    "					<spring:message code='aggregationService.pathlist.file.date.header' var='tooltip'></spring:message>"+
							    "					<label style='width: 100%;background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
							    " 			</div>"+
							    
							    "				<div class='col-md-6 no-padding'>"+
							    "					<spring:message code='ftp.driver.mgmt.pathlist.filed.date.enable.label' var='label'></spring:message>"+
							    "					<spring:message code='ftp.driver.mgmt.pathlist.filed.date.enable.tooltip' var='tooltip'></spring:message>"+
							    "  					<div class='form-group'>"+
							    "    					<div class='table-cell-label'>${label}</div>"+
							    "      					<div class='input-group '>"+
								"		        			<select class='form-control table-cell input-sm' id='"+pathListCounter+"_fileGrepDateEnabled' onchange=changeFileDateParam(\'"+pathListCounter+"\'); tabindex='12' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
								"				    			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >"+
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
							    "  					<div class='form-group'>"+
							    "    					<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
							    "      					<div class='input-group '>"+
							    "      					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_dateFormat' tabindex='13' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+dateFormat+"'/>"+
								 "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							    "        				</div>"+
							    "     				</div>"+
							    "				</div>"+
							    "				<div class='col-md-6 no-padding'>"+
							    "					<spring:message code='ftp.driver.mgmt.pathlist.position.label' var='label'></spring:message>"+
							    "					<spring:message code='ftp.driver.mgmt.pathlist.position.tooltip' var='tooltip'></spring:message>"+
							    "  					<div class='form-group'>"+
							    "    					<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
							    "      					<div class='input-group '>"+
								 "  						<select class='form-control table-cell input-sm' id='"+pathListCounter+"_position' tabindex='14' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
							    "   							<c:forEach var='positionEnum' items='${positionEnum}'>"+
							    "    								<option value='${positionEnum.value}'>${positionEnum}</option>" +
							    "    							</c:forEach>" +
							    "   						</select>" +
								 "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							    "        				</div>"+
							    "    				</div>"+
							    "				</div>"+
							    "				<div class='col-md-6 no-padding'>"+
							    "					<spring:message code='ftp.driver.mgmt.pathlist.start.idx.label' var='label'></spring:message>"+
							    "					<spring:message code='ftp.driver.mgmt.pathlist.start.idx.tooltip' var='tooltip'></spring:message>"+
							    "  					<div class='form-group'>"+
							    "    					<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
							    "      					<div class='input-group '>"+
							    "      						<input class='form-control table-cell input-sm' id='"+pathListCounter+"_startIndex' onkeydown=isNumericOnKeyDown(event); tabindex='15' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+startIndex+"' />"+
								 "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							    "       				</div>"+
							    "   				</div>"+
							    "				</div>"+
							    "				<div class='col-md-6 no-padding'>"+
							    "					<spring:message code='ftp.driver.mgmt.pathlist.end.idx.label' var='label'></spring:message>"+
							    "					<spring:message code='ftp.driver.mgmt.pathlist.end.idx.tooltip' var='tooltip'></spring:message>"+
							    "  					<div class='form-group'>"+
							    "    					<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
							    "      					<div class='input-group '>"+
							    "      					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_endIndex' onkeydown=isNumericOnKeyDown(event); tabindex='16' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+endIndex+"' />"+
							    "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							    "       				</div>"+
							    "   				</div>"+
							    "				</div>"+ 
							    "			<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.header' var='tooltip'></spring:message>"+
							    "					<label style='width: 100%;background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
							    " 			</div>"+
							    "   			<div class='col-md-6 no-padding'>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.label' var='label'></spring:message>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.tooltip' var='tooltip'></spring:message>"+
							    "  					<div class='form-group'>"+
							    "    					<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
							    "      					<div class='input-group '>"+
							    "      						<input class='form-control table-cell input-sm' id='"+pathListCounter+"_oFilePathName' tabindex='15' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+outputfilename+"' />"+
								 "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							    "       				</div>"+
							    "   				</div>"+
							    "				</div>"+
							    "				<div class='col-md-6 no-padding'>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.fileseq.label' var='label'></spring:message>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.fileseq.tooltip' var='tooltip'></spring:message>"+
							    "  					<div class='form-group'>"+
							    "    					<div class='table-cell-label'>${label}</div>"+
							    "      					<div class='input-group '>"+			
							    /*"      						<input class='form-control table-cell input-sm' id='"+pathListCounter+"_outputfileseq' tabindex='15' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+_outputfileseq+"' />"+ */
							    "							<select class='form-control table-cell input-sm' id='" + pathListCounter + "_outputfileseq' tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' onchange=toggleOutputFileSeq(\'"+pathListCounter+"\'); value='"+outputfileseq+"' >"+
							    "				    			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >"+
								"									<option value='${trueFalseEnum.name}'>${trueFalseEnum}</option>"+
								"				    			</c:forEach>"+
								"							</select>"+ 
							    "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							    "       				</div>"+
							    "   				</div>"+
							    "				</div>"+
							    "				<div class='col-md-6 no-padding'>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.minrange.label' var='label'></spring:message>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.minrange.tooltip' var='tooltip'></spring:message>"+
							    "  					<div class='form-group'>"+
							    "    				<div class='table-cell-label'>${label}</div>"+
							    "      					<div class='input-group '>"+
							    "      						<input class='form-control table-cell input-sm' id='"+pathListCounter+"_oFileMinRange' onkeydown=isNumericOnKeyDown(event); tabindex='15' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+outputfileminrange+"' />"+
								 "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							    "       				</div>"+
							    "   				</div>"+
							    "				</div>"+
							    "				<div class='col-md-6 no-padding'>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.maxrange.label' var='label'></spring:message>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.maxrange.tooltip' var='tooltip'></spring:message>"+
							    "  					<div class='form-group'>"+
							    "    					<div class='table-cell-label'>${label}</div>"+
							    "      					<div class='input-group '>"+
							    "      						<input class='form-control table-cell input-sm' id='"+pathListCounter+"_oFileMaxRange' onkeydown=isNumericOnKeyDown(event); tabindex='16' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+outputfilemaxrange+"' />"+
							    "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							    "       				</div>"+
							    "   				</div>"+
							    "				</div>"+
							    /* nonagg*/
							    "			<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.nonaggregate.header' var='tooltip'></spring:message>"+
							    "					<label style='width: 100%;background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
							    " 			</div>"+
							    "   			<div class='col-md-6 no-padding'>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.label' var='label'></spring:message>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.tooltip' var='tooltip'></spring:message>"+
							    "  					<div class='form-group'>"+
							    "    					<div class='table-cell-label'>${label}</div>"+
							    "      					<div class='input-group '>"+
							    "      						<input class='form-control table-cell input-sm' id='"+pathListCounter+"_oFilePathNameForNonAgg' tabindex='15' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+outputfilenamefornonagg+"' />"+
								 "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							    "       				</div>"+
							    "   				</div>"+
							    "				</div>"+
							    "				<div class='col-md-6 no-padding'>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.fileseq.label' var='label'></spring:message>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.fileseq.tooltip' var='tooltip'></spring:message>"+
							    "  					<div class='form-group'>"+
							    "    					<div class='table-cell-label'>${label}</div>"+
							    "      					<div class='input-group '>"+			
							    "							<select class='form-control table-cell input-sm' id='" + pathListCounter + "_outputfileseqnonagg' tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' onchange=toggleOutputFileSeq(\'"+pathListCounter+"\'); value='"+outputfileseqfornonagg+"' >"+
							    "				    			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >"+
								"									<option value='${trueFalseEnum.name}'>${trueFalseEnum}</option>"+
								"				    			</c:forEach>"+
								"							</select>"+ 
							    "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							    "       				</div>"+
							    "   				</div>"+
							    "				</div>"+
							    "				<div class='col-md-6 no-padding'>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.minrange.label' var='label'></spring:message>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.minrange.tooltip' var='tooltip'></spring:message>"+
							    "  					<div class='form-group'>"+
							    "    				<div class='table-cell-label'>${label}</div>"+
							    "      					<div class='input-group '>"+
							    "      						<input class='form-control table-cell input-sm' id='"+pathListCounter+"_oFileMinRangeForNonAgg' onkeydown=isNumericOnKeyDown(event); tabindex='15' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+outputfileminrangefornonagg+"' />"+
								 "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							    "       				</div>"+
							    "   				</div>"+
							    "				</div>"+
							    "				<div class='col-md-6 no-padding'>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.maxrange.label' var='label'></spring:message>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.maxrange.tooltip' var='tooltip'></spring:message>"+
							    "  					<div class='form-group'>"+
							    "    					<div class='table-cell-label'>${label}</div>"+
							    "      					<div class='input-group '>"+
							    "      						<input class='form-control table-cell input-sm' id='"+pathListCounter+"_oFileMaxRangeForNonAgg' onkeydown=isNumericOnKeyDown(event); tabindex='16' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+outputfilemaxrangefornonagg+"' />"+
							    "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							    "       				</div>"+
							    "   				</div>"+
							    "				</div>"+
							    /* error*/
							    "			<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.aggregateerror.header' var='tooltip'></spring:message>"+
							    "					<label style='width: 100%;background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
							    " 			</div>"+
							    "   			<div class='col-md-6 no-padding'>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.label' var='label'></spring:message>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.tooltip' var='tooltip'></spring:message>"+
							    "  					<div class='form-group'>"+
							    "    					<div class='table-cell-label'>${label}</div>"+
							    "      					<div class='input-group '>"+
							    "      						<input class='form-control table-cell input-sm' id='"+pathListCounter+"_oFilePathNameForError' tabindex='15' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+outputfilenameforerr+"' />"+
								 "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							    "       				</div>"+
							    "   				</div>"+
							    "				</div>"+
							    "				<div class='col-md-6 no-padding'>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.fileseq.label' var='label'></spring:message>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.fileseq.tooltip' var='tooltip'></spring:message>"+
							    "  					<div class='form-group'>"+
							    "    					<div class='table-cell-label'>${label}</div>"+
							    "      					<div class='input-group '>"+			
							    "							<select class='form-control table-cell input-sm' id='" + pathListCounter + "_outputfileseqaggerror' tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' onchange=toggleOutputFileSeq(\'"+pathListCounter+"\'); value='"+outputfileseqforerr+"' >"+
							    "				    			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >"+
								"									<option value='${trueFalseEnum.name}'>${trueFalseEnum}</option>"+
								"				    			</c:forEach>"+
								"							</select>"+ 
							    "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							    "       				</div>"+
							    "   				</div>"+
							    "				</div>"+
							    "				<div class='col-md-6 no-padding'>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.minrange.label' var='label'></spring:message>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.minrange.tooltip' var='tooltip'></spring:message>"+
							    "  					<div class='form-group'>"+
							    "    				<div class='table-cell-label'>${label}</div>"+
							    "      					<div class='input-group '>"+
							    "      						<input class='form-control table-cell input-sm' id='"+pathListCounter+"_oFileMinRangeForError' onkeydown=isNumericOnKeyDown(event); tabindex='15' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+outputfileminrangeforerr+"' />"+
								 "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							    "       				</div>"+
							    "   				</div>"+
							    "				</div>"+
							    "				<div class='col-md-6 no-padding'>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.maxrange.label' var='label'></spring:message>"+
							    "					<spring:message code='aggregationService.pathlist.outputfile.filename.maxrange.tooltip' var='tooltip'></spring:message>"+
							    "  					<div class='form-group'>"+
							    "    					<div class='table-cell-label'>${label}</div>"+
							    "      					<div class='input-group '>"+
							    "      						<input class='form-control table-cell input-sm' id='"+pathListCounter+"_oFileMaxRangeForError' onkeydown=isNumericOnKeyDown(event); tabindex='16' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+outputfilemaxrangeforerr+"' />"+
							    "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							    "       				</div>"+
							    "   				</div>"+
							    "				</div>"+
							 
						 	"		<div class='col-md-12 no-padding'>"+
						    "  			<div class='form-group'>"+
						    "      			<div id='" + pathListCounter + "_buttons-div' class='input-group '>"+
							"					<sec:authorize access='hasAuthority(\'CREATE_PATHLIST\')'>"+	
				    		"						<button type='button' class='btn btn-grey btn-xs ' id='" + pathListCounter + "_savebtn' onclick=createAggregationPathList(\'"+pathListCounter+"\');><spring:message code='btn.label.save'></spring:message></button>&nbsp;" +
				    		"					</sec:authorize>"+
						    "					<sec:authorize access='hasAuthority(\'UPDATE_PATHLIST\')'>"+
							"						<button type='button' class='btn btn-grey btn-xs ' id='" + pathListCounter + "_updatebtn' onclick=updateAggregationPathList(\'"+pathListCounter+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
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
							"</div> ";
							
					$('#pathList').prepend(pahtlistHtmlBody);
					$("#"+ pathListCounter+"_fileGrepDateEnabled").val(fileGrepDateEnabled.toString());
					$('#' + pathListCounter + '_outputfileseq option[value="'+ outputfileseq + '"]').attr('selected', true);
					$('#' + pathListCounter + '_outputfileseqnonagg option[value="'+ outputfileseqfornonagg + '"]').attr('selected', true);
					$('#' + pathListCounter + '_outputfileseqaggerror option[value="'+ outputfileseqforerr + '"]').attr('selected', true);
					toggleOutputFileSeq(pathListCounter);
					$("#"+pathListCounter+"_savebtn").hide();
					$("#"+pathListCounter+"_updatebtn").hide();

					if(actionType == 'UPDATE'){
						$("#"+ pathListCounter+"_compressInFileEnabled").val(readFileMode.toString());
						$("#"+ pathListCounter+"_compressOutFileEnabled").val(writeFileMode.toString()); 
						$("#"+ pathListCounter+"_position").val(position.toString());
						$("#"+pathListCounter+"_updatebtn").show();
						$("#"+pathListCounter+"_grid_main_div").show();
						$("#"+pathListCounter+"_grid_links_div").show();
						$("#title_"+pathListCounter).html(name + " - " + pathId);
						$("#"+pathListCounter+"_block").collapse("toggle");
					}else{
						$("#"+pathListCounter+"_savebtn").show();
						$("#"+pathListCounter+"_grid_main_div").hide();
						$("#"+pathListCounter+"_grid_links_div").hide();
						$("#"+ pathListCounter+"_compressInFileEnabled").val('true');
						$("#"+ pathListCounter+"_readFilenameSuffix").val('.gz');
						$("#title_"+pathListCounter).html("<spring:message code='distribution.pathlist.block.header.label' ></spring:message>");
					}
 }
 
 function toggleOutputFileSeq(pathListCounter){
		if($('#'+pathListCounter+'_outputfileseq').val() == 'true'){
			$('#'+pathListCounter+'_oFileMinRange').prop("disabled", false);
			$('#'+pathListCounter+'_oFileMaxRange').prop("disabled", false);
		}else{
			$('#'+pathListCounter+'_oFileMinRange').prop("disabled", true);
			$('#'+pathListCounter+'_oFileMaxRange').prop("disabled", true);
		}
		
		if($('#'+pathListCounter+'_outputfileseqnonagg').val() == 'true'){
			$('#'+pathListCounter+'_oFileMinRangeForNonAgg').prop("disabled", false);
			$('#'+pathListCounter+'_oFileMaxRangeForNonAgg').prop("disabled", false);
		}else{
			$('#'+pathListCounter+'_oFileMinRangeForNonAgg').prop("disabled", true);
			$('#'+pathListCounter+'_oFileMaxRangeForNonAgg').prop("disabled", true);
		}
		
		if($('#'+pathListCounter+'_outputfileseqaggerror').val() == 'true'){
			$('#'+pathListCounter+'_oFileMinRangeForError').prop("disabled", false);
			$('#'+pathListCounter+'_oFileMaxRangeForError').prop("disabled", false);
		}else{
			$('#'+pathListCounter+'_oFileMinRangeForError').prop("disabled", true);
			$('#'+pathListCounter+'_oFileMaxRangeForError').prop("disabled", true);
		}
 }
 
 function createAggregationPathList(counter){
	 createUpdateAggregationPathList(counter, 'ADD', '<%=ControllerConstants.CREATE_AGGREGATION_SERVICE_PATH_LIST%>');
 }
 
 function updateAggregationPathList(counter){
	 createUpdateAggregationPathList(counter, 'UPDATE', '<%=ControllerConstants.UPDATE_AGGREGATION_SERVICE_PATH_LIST%>');
 }
 
 function showProgressBar(pathListCounter){
	$('#' + pathListCounter + '_buttons-div').hide();
	$('#' + pathListCounter + '_progress-bar-div').show();
 }
 
 function hideProgressBar(pathListCounter){
		$('#' + pathListCounter + '_buttons-div').show();
		$('#' + pathListCounter + '_progress-bar-div').hide();
 }
 
 function createUpdateAggregationPathList(counter, action, actionURL) {
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
		$.ajax({	url : actionURL,
					cache : false,
					async : true,
					dataType : 'json',
					type : "POST",
					data : {
						"id" : pathListId,
						"name" : $("#" + counter + "_name").val(),
						"readFilePath" : $("#" + counter + "_readFilePath").val(),
						"maxFilesCountAlert" : maxFileCountAlert,
						"compressInFileEnabled" : $(
								"#" + counter + "_compressInFileEnabled").val(),
						"readFilenamePrefix" : $(
								"#" + counter + "_readFilenamePrefix").val(),
						"readFilenameSuffix" : $(
								"#" + counter + "_readFilenameSuffix").val(),
						"writeFilePath" : $("#" + counter + "_writeFilePath").val(),
						"wPathNonAggregate"	: $('#'+counter+'_wPathNonAggregate').val(),
						"wPathAggregateError" : $('#'+counter+'_wPathAggregateError').val(),
						"compressOutFileEnabled" : $("#" + counter + "_compressOutFileEnabled").val(),
						"service.id"			   :   	parseInt('${serviceId}'),
						"service.serverInstance.id": 	parseInt('${instanceId}'),
						"position"			  : $("#"+counter+"_position").val(),
						"startIndex"		  : parseInt($("#"+counter+"_startIndex").val()),
						"endIndex"			  : parseInt($("#"+counter+"_endIndex").val()),
						"dateFormat" : $("#"+counter+"_dateFormat").val(),
						"pathListCount" : counter,
						"pathId"				   :	$('#'+counter+'_pathId').val(),
						"oFilePathName"				   :	$('#'+counter+'_oFilePathName').val(),
						"oFileMinRange"				   :	$('#'+counter+'_oFileMinRange').val(),
						"oFileMaxRange"				   :	$('#'+counter+'_oFileMaxRange').val(),
						"oFileSeqEnables"				   :	$('#'+counter+'_outputfileseq').val(),
						"oFilePathNameForNonAgg"        :	$('#'+counter+'_oFilePathNameForNonAgg').val(),
						"oFileMinRangeForNonAgg"        :	$('#'+counter+'_oFileMinRangeForNonAgg').val(),
						"oFileMaxRangeForNonAgg"        :	$('#'+counter+'_oFileMaxRangeForNonAgg').val(),
						"oFileSeqEnablesForNonAgg"      :	$('#'+counter+'_outputfileseqnonagg').val(),
						"oFilePathNameForError"         :	$('#'+counter+'_oFilePathNameForError').val(),
						"oFileSeqEnablesForError"       :	$('#'+counter+'_outputfileseqaggerror').val(),   
						"oFileMinRangeForError"         :	$('#'+counter+'_oFileMinRangeForError').val(),
						"oFileMaxRangeForError"         :	$('#'+counter+'_oFileMaxRangeForError').val(),
						"referenceDevice"		    	:   $('#' + counter + '_parentDevice').val(),
						"parentDevice.id"				:   $('#' + counter + '_deviceId').val(),
						"fileGrepDateEnabled"      : $("#"+counter+"_fileGrepDateEnabled").val(),
						"serviceId"						: $('#serviceId').val(), 
						
					},
					success : function(data) {
						hideProgressBar(counter);
						var responseCode = data.code;
						var responseMsg = data.msg;
						var responseObject = data.object;
						scrollTo("top");
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
 
   function resetPathlistParams(counter){
	   resetWarningDisplay();
	   $("#" + counter + "_maxFilesCountAlert").val('');
	   $("#" + counter + "_name").val('');
	   $("#" + counter + "_readFilePath").val('');
	   $("#" + counter + "_readFilenamePrefix").val('');
	   $("#" + counter + "_readFilenameSuffix").val('.gz');
	   $("#" + counter + "_writeFilePath").val('');
	   $("#" + counter + "_wPathNonAggregate").val();
	   $("#" + counter + "_wPathAggregateError").val();
	   $("#" + counter + "_position").val('left');
	   $("#" + counter + "_startIndex").val('0');
	   $("#" + counter + "_endIndex").val('5');
	   $("#" + counter + "_dateFormat").val('yyyyMMddHHmm');	   
	   $("#" + counter + "_oFileMinRange").val('-1');
	   $("#" + counter + "_oFileMaxRange").val('-1');
	   $("#" + counter + "_oFilePathName").val('Agg{DDMMMYYYY_hh:mm:ss}');
	   
	   $("#" + counter + "_oFilePathNameForNonAgg").val('Non_Agg_{DDMMMYYYY_hh:mm:ss}');
	   $("#" + counter + "_oFileMinRange").val('-1');
	   $("#" + counter + "_oFileMaxRange").val('-1');
	   
	   $("#" + counter + "_oFilePathNameForError").val('Agg_error{DDMMMYYYY_hh:mm:ss}');
	   $("#" + counter + "_oFileMinRange").val('-1');
	   $("#" + counter + "_oFileMaxRange").val('-1');
	   $("#" + counter + "_fileGrepDateEnabled").val('false');
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
   function deleteAggregationPathList() {
   	resetWarningDisplay();
   	clearAllMessages();
   	$
   			.ajax({
   				url : '<%=ControllerConstants.DELETE_AGGREGATION_PATHLIST%>',
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
