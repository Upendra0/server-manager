<%@page import="com.elitecore.sm.common.model.StateEnum"%>
<%@page import="com.elitecore.sm.services.model.PartitionFieldEnum"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@page import="com.elitecore.sm.services.model.HashDataTypeEnum"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script>
var pathListCounter=-1;
var blockAdded='false';



function addPathlistDetail(wrapperId,pathListId,pathlistName,sourcePath,writePath,readFilePrefix,readFileSuffix,readExclude,compressedIn,compressedOut,writeFilePrefix,pluginName,pluginType,indexType,parserMappingId){
	pathListCounter++;
	var block = "	<input type='hidden' id='"+pathListCounter+"_parserId' value='"+wrapperId+"'>   "+
				"	<input type='hidden' id='"+pathListCounter+"_pathListId' value='"+pathListId+"'>   "+
				"	<input type='hidden' id='"+pathListCounter+"_parserMappingId' value='"+parserMappingId+"'>   "+
				"	<div class='box box-warning' id='flipbox_"+pathListCounter+"'>   "+ 														
				"	<div class='box-header with-border'> "+
				"		<h3 class='box-title' id='title_"+pathListCounter+ "'><a href='#' class='title2rightfield-icon1-text' style='font-size: 12px;text-decoration:none' id='Pathlink_"+pathListCounter+ "'>"+pathlistName+"&nbsp;|</a>"+
				"		<a href='#' class='title2rightfield-icon1-text' id='link_"+pathListCounter+ "' style='font-size: 12px;' onclick=redirectPluginConfig(\'"+pathListCounter+"\');>"+pluginName +"</a></h3>"+
				"		<div class='box-tools pull-right' style='top: -3px;' id='action_"+pathListCounter+ "'> "+
				"			<button class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' href='#" + pathListCounter + "_block'> "+
				"				<i class='fa fa-minus'></i> "+
				"			</button> "+
				"					<sec:authorize access='hasAuthority(\'DELETE_PATHLIST\')'>"+
			    "			&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;' id='deletePath_"+pathlistName+"' onclick=deletePathListPopup(\'"+pathListCounter+"\');></a>&nbsp;" +
			    "					</sec:authorize>"+
				"		</div> "+
				"		<!-- /.box-tools --> "+
				"	</div> "+
				"	<!-- /.box-header end here --> "+
				"	<div class='box-body inline-form accordion-body' id='" + pathListCounter + "_block'> "+
				"		<div class='fullwidth inline-form'> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.name' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+
				"					<div class='input-group '> "+
				"						<input type='text' id='" + pathListCounter + "_parsingPathList_name' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+pathlistName+"'/> "+
				"						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title='' ></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.source.path' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span><i class='fa fa-square'></i></div> "+
				"					<div class='input-group '> "+
				"						<input type='text' id='" + pathListCounter + "_parsingPathList_readFilePath' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+sourcePath+"'/> "+	
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.output.file.path' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+ 
				"					<div class='input-group '> "+
				"						<input type='text' id='" + pathListCounter + "_writeFilePath' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+writePath+"'/> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.file.prefix' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}</div> "+
				"					<div class='input-group '> "+
				"						<input type='text' id='" + pathListCounter + "_readFilenamePrefix' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readFilePrefix+"'/> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.file.suffix' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+
				"					<div class='input-group '> "+
				"						<input type='text' id='" + pathListCounter + "_readFilenameSuffix' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readFileSuffix+"'/> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.file.type.exclude' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+ 
				"					<div class='table-cell-label'>${tooltip}</div> "+
				"					<div class='input-group '> "+
				"						<input type='text' id='" + pathListCounter + "_readFilenameExcludeTypes' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readExclude+"'/> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.input.file.compressed' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+
				"					<div class='input-group '> "+
				"						<select id='" + pathListCounter + "_compressInFileEnabled' class='form-control table-cell input-sm' tabindex='4' id='service-dataType' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+compressedIn+"'> "+
				"							<c:forEach items='${truefalseEnum}' var='truefalseEnum' > "+
				"	                          			<option value='${truefalseEnum.name}'>${truefalseEnum}</option> "+
				"							</c:forEach> "+
 				"						</select> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.output.file.compressed' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+
				"					<div class='input-group '> "+
				"						<select  disabled id='" + pathListCounter + "_compressOutFileEnabled' class='form-control table-cell input-sm' tabindex='4' id='service-dataType' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+compressedOut+"'> "+
				"							<c:forEach items='${truefalseEnum}' var='truefalseEnum' > "+
				"								<c:if test='${type == True}'>" +	
				"	                          		<option value='${truefalseEnum.name}' selected>${truefalseEnum}</option> "+
				"								</c:if>" +
				"								<c:if test='${type != True}'>" +	
				"	                          		<option value='${truefalseEnum.name}'>${truefalseEnum}</option> "+
				"								</c:if>" +	
				"							</c:forEach> "+
				"						</select> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+ 
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<!-- <div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.output.file.prefix' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}</div> "+
				"					<div class='input-group '> "+
				"						<input type='text' id='" + pathListCounter + "_writeFilenamePrefix' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+writeFilePrefix+"'/> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> -->"+
				"			<div class='col-md-12 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.plugin.detail.header' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'><strong>${tooltip}</strong></div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.plugin.name' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+
				"					<div class='input-group '> "+
				"						<input type='text' id='" + pathListCounter + "_name' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+pluginName+"'/> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.plugin.type' var='tooltip' ></spring:message> "+ 
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span><i class='fa fa-square'></i></div> "+
				"					<div class='input-group '> "+
				"						<select id='" + pathListCounter + "_parserType' class='form-control table-cell input-sm' tabindex='4' id='service-dataType' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' disabled='disabled'> "+
				"							<c:if test='${pluginType != null }'>"+
				"							<c:forEach var='pluginType' items='${pluginType}'>"+
				"	                          	<option value='${pluginType.alias}'>${pluginType.type}</option> "+
				"							</c:forEach></c:if> "+
				"						</select> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.config.data.type' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span><i class='fa fa-square'></i></div> "+
				"					<div class='input-group '> "+
				"						<select id='" + pathListCounter + "_parsingPathList_service_dataType' class='form-control table-cell input-sm' tabindex='4' id='service-dataType' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' disabled='disabled' value='"+indexType+"'>"+
				"							<c:forEach items='${dataTypeEnum}' var='dataTypeEnum' > "+
				"	                          			<option value='${dataTypeEnum}'>${dataTypeEnum}</option> "+
				"							</c:forEach> "+
				"						</select> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-12 no-padding'> "+
				"					<div class='form-group'> "+
				" 					<div id='" + pathListCounter + "_buttons-div' class='input-group '> "+
				"						<sec:authorize access='hasAuthority(\'UPDATE_PATHLIST\')'>"+
				" 						<button type='button' class='btn btn-grey btn-xs' id='" + pathListCounter + "_updatebtn' onclick=updatePathlistDB(\'"+pathListCounter+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp; "+
				"						</sec:authorize> "+
				"						<button type='button' class='btn btn-grey btn-xs' id='" + pathListCounter + "_resetbtn' onclick=resetPathList(\'"+pathListCounter+"\');><spring:message code='btn.label.reset'></spring:message></button>&nbsp;  "+
				"  					</div> "+
				"					<div id='" + pathListCounter + "_progress-bar-div' class='input-group' style='display: none;'> "+ 
				"						<label>Processing Request &nbsp;&nbsp; </label>  "+
				"						<img src='img/processing-bar.gif'> "+
				"					</div> "+
				"					</div> "+ 
				"			</div>  "+
				"		</div> "+
				"	</div> "+
				"</div>";
	$('#pathList').append(block);
	$('#'+ pathListCounter + '_compressOutFileEnabled').val(compressedOut);
	$('#'+ pathListCounter + '_compressInFileEnabled').val(compressedIn);
	$('#'+ pathListCounter + '_parserType').val(pluginType);
	$('#'+ pathListCounter + '_parsingPathList_service_dataType').val(indexType);
	
}

function addPathlist(indexType){
	pathListCounter++;
	var block = "	<input type='hidden' id='"+pathListCounter+"_parserId'>   "+
				"	<input type='hidden' id='"+pathListCounter+"_pathListId'>   "+
				"	<div class='box box-warning' id='flipbox_"+pathListCounter+"'>   "+ 														
				"	<div class='box-header with-border'> "+
				"		<h3 class='box-title' id='title_"+pathListCounter+ "'> "+
				"		<a href='#' class='title2rightfield-icon1-text' style='font-size: 12px;text-decoration:none' id='Pathlink_"+pathListCounter+ "'><spring:message code='iplog.parsing.service.pathlist.popup.header'></spring:message>&nbsp;</a>"+
				"		<a href='#' class='title2rightfield-icon1-text' id='link_"+pathListCounter+ "' onclick=redirectPluginConfig(\'"+pathListCounter+"\'); style='font-size: 12px;text-decoration:none'></a></h3>"+												
				"		</h3>  "+
				"		<div class='box-tools pull-right' style='top: -3px;' id='action_"+pathListCounter+ "'> "+
				"			<button class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' href='#" + pathListCounter + "_block'> "+
				"				<i class='fa fa-minus'></i> "+
				"			</button> "+
				"					<sec:authorize access='hasAuthority(\'DELETE_PATHLIST\')'>"+
			    "			&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;display:none' id='delete_"+pathListCounter+"' onclick=deletePathListPopup(\'"+pathListCounter+"\');></a>&nbsp;" +
			    "					</sec:authorize>"+
				"		</div> "+
				"	</div> "+
				"	<div class='box-body inline-form accordion-body collapse-out' id='" + pathListCounter + "_block'> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.name' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+
				"					<div class='input-group '> "+
				"						<input type='text' id='" + pathListCounter + "_parsingPathList_name' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/> "+
				"						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title='' ></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.source.path' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span><i class='fa fa-square'></i></div> "+
				"					<div class='input-group '> "+
				"						<input type='text' id='" + pathListCounter + "_parsingPathList_readFilePath' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/> "+	
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.output.file.path' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+ 
				"					<div class='input-group '> "+
				"						<input type='text' id='" + pathListCounter + "_writeFilePath' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.file.prefix' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}</div> "+
				"					<div class='input-group '> "+
				"						<input type='text' id='" + pathListCounter + "_readFilenamePrefix' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.file.suffix' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+
				"					<div class='input-group '> "+
				"						<input type='text' id='" + pathListCounter + "_readFilenameSuffix' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.file.type.exclude' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+ 
				"					<div class='table-cell-label'>${tooltip}</div> "+
				"					<div class='input-group '> "+
				"						<input type='text' id='" + pathListCounter + "_readFilenameExcludeTypes' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.input.file.compressed' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+
				"					<div class='input-group '> "+
				"						<select id='" + pathListCounter + "_compressInFileEnabled' class='form-control table-cell input-sm' tabindex='4' id='service-dataType' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'> "+
				"							<c:forEach items='${truefalseEnum}' var='truefalseEnum' > "+
				"	                          			<option value='${truefalseEnum.name}'>${truefalseEnum}</option> "+
				"							</c:forEach> "+
 				"						</select> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.output.file.compressed' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+
				"					<div class='input-group '> "+
				"						<select disabled id='" + pathListCounter + "_compressOutFileEnabled' class='form-control table-cell input-sm' tabindex='4' id='service-dataType' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'> "+
				"							<c:forEach items='${truefalseEnum}' var='truefalseEnum' > "+
				"								<c:if test='${type == True}'>" +	
				"	                          		<option value='${truefalseEnum.name}' selected>${truefalseEnum}</option> "+
				"								</c:if>" +
				"								<c:if test='${type != True}'>" +	
				"	                          		<option value='${truefalseEnum.name}'>${truefalseEnum}</option> "+
				"								</c:if>" +	
				"							</c:forEach> "+
				"						</select> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+ 
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<!-- <div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.output.file.prefix' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}</div> "+
				"					<div class='input-group '> "+
				"						<input type='text' id='" + pathListCounter + "_writeFilenamePrefix' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> -->"+
				"			<div class='col-md-12 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.plugin.detail.header' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'><strong>${tooltip}</strong></div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.plugin.name' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+
				"					<div class='input-group '> "+
				"						<input type='text' id='" + pathListCounter + "_name' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.pathlist.plugin.type' var='tooltip' ></spring:message> "+ 
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span><i class='fa fa-square'></i></div> "+
				"					<div class='input-group '> "+
				"						<select id='" + pathListCounter + "_parserType' class='form-control table-cell input-sm' tabindex='4' id='service-dataType' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'> "+
				"							<c:if test='${pluginType != null }'>"+
				"							<c:forEach var='pluginType' items='${pluginType}'>"+
				"	                          	<option value='${pluginType.alias}'>${pluginType.type}</option> "+
				"							</c:forEach></c:if> "+
				"						</select> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-6 no-padding'> "+
				"				<spring:message code='iplog.parsing.service.config.data.type' var='tooltip' ></spring:message> "+
				"				<div class='form-group'> "+
				"					<div class='table-cell-label'>${tooltip}<span class='required'>*</span><i class='fa fa-square'></i></div> "+
				"					<div class='input-group '> "+
				"						<select id='" + pathListCounter + "_parsingPathList_service_dataType' class='form-control table-cell input-sm' tabindex='4' id='service-dataType' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' disabled='disabled'>"+
				"							<c:forEach items='${dataTypeEnum}' var='dataTypeEnum' > "+
				"	                          			<option value='${dataTypeEnum}'>${dataTypeEnum}</option> "+
				"							</c:forEach> "+
				"						</select> "+
				"						<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
				"					</div> "+
				"				</div> "+
				"			</div> "+
				"			<div class='col-md-12 no-padding'> "+
				"					<div class='form-group'> "+
				" 					<div id='" + pathListCounter + "_buttons-div' class='input-group '> "+
				"  						<button type='button' class='btn btn-grey btn-xs ' id='" + pathListCounter + "_updatebtn' style='display:none;'><spring:message code='btn.label.update'></spring:message></button>&nbsp; "+
				"  						<button type='button' class='btn btn-grey btn-xs ' id='" + pathListCounter + "_savebtn' onclick=addPathlistDB(\'"+pathListCounter+"\');><spring:message code='btn.label.save'></spring:message></button>&nbsp; "+
				"						<button type='button' class='btn btn-grey btn-xs ' id='" + pathListCounter + "_resetbtn' onclick=resetPathList(\'"+pathListCounter+"\')><spring:message code='btn.label.reset'></spring:message></button>&nbsp;  "+
				"  					</div> "+
				"					<div id='" + pathListCounter + "_progress-bar-div' class='input-group' style='display: none;'> "+ 
				"						<label>Processing Request &nbsp;&nbsp; </label>  "+
				"						<img src='img/processing-bar.gif'> "+
				"					</div> "+
				"					</div> "+ 
				"			</div>  "+
				"		</div> "+
				"</div>";
	$('#pathList').append(block);
	$('#'+ pathListCounter + '_parsingPathList_service_dataType').val(indexType);
}

</script>

<div class="fullwidth mtop10">	
		<div id="pathList">
        </div>
        
        <c:choose>
			<c:when test="${parsingPathList != null && fn:length(parsingPathList) gt 0}">
			<c:forEach var="pathList" items="${parsingPathList}">
				<c:forEach var="parserWrappers" items="${pathList.parserWrappers}">
					<c:choose>
						<c:when test="${parserWrappers.parsingPathList.status ne 'DELETED'}">
						
						<script>
						blockAdded='true';
						addPathlistDetail(
								'${parserWrappers.id}',
								'${parserWrappers.parsingPathList.id}',
								'${parserWrappers.parsingPathList.name}',
								'${parserWrappers.parsingPathList.readFilePath}',
								'${parserWrappers.writeFilePath}',
								'${parserWrappers.readFilenamePrefix}',
								'${parserWrappers.readFilenameSuffix}',
								'${parserWrappers.readFilenameExcludeTypes}',
								'${parserWrappers.compressInFileEnabled}',
								'${parserWrappers.compressOutFileEnabled}',
								'${parserWrappers.writeFilenamePrefix}',
		 						'${parserWrappers.name}',
		 						'${parserWrappers.parserType.alias}',
		 						'${parserWrappers.parsingPathList.service.dataType}',
		 						'${parserWrappers.parserMapping.id}'
								);				
						</script>
						</c:when>
					</c:choose>
					
				</c:forEach>
			</c:forEach>
			</c:when>
			<c:otherwise>
			   <!-- if user has add pathlist access right than only add empty pathlist block -->
			   <sec:authorize access="hasAuthority('CREATE_PATHLIST')">
		       <script>
		       		// if no pathlist configured than add default one pathlist block
		       		blockAdded='true';
		       		addPathlist('${iplog_parsing_service_configuration_form_bean.dataType}');
		       </script>
		       </sec:authorize>
		    </c:otherwise>
		</c:choose>
		<sec:authorize access="hasAuthority('CREATE_PATHLIST')">
		       <script>
		       		// if only deleted pathlist configured than add default one pathlist block
		       		if(blockAdded != 'true'){
		       			blockAdded='true';
		       			addPathlist('${iplog_parsing_service_configuration_form_bean.dataType}');
		       		}
		       </script>
		 </sec:authorize>
		
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
		        	<input type="hidden" id="deletepathListId" name="deletepathListId"/>
			        <p id="deleteWarningMessage">
			       		 <spring:message code="pahtlist.delete.warn.message"></spring:message>
			        </p>
		        </div>
		        
		       <!-- To Do add role here -->
			        <div id="delete-popup-buttons" class="modal-footer padding10">
		        		<button type="button" class="btn btn-grey btn-xs " id="deletePathList_btn" onclick="deletePathListDetails();"><spring:message code="btn.label.yes"></spring:message></button>
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
			            <button type="button" class="btn btn-grey btn-xs " id="closeBtn" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        	</div>
		        	<div id="delete-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
			</div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
    	<a href="#divMessage" class="fancybox" style="display: none;" id="pathlistMessage">#</a>
    	<!-- Pathlist Delete popup code end here -->
		
</div>

<script>

function resetPathList(counter){
	$('#'+ counter+ '_block input').val('');
}

function addPathlistDB(counter){
	
	resetWarningDisplay();
	clearAllMessages();
	showProgressBar(counter);
	
	$.ajax({
		url: '<%=ControllerConstants.CREATE_IPLOG_PARSING_PATHLIST%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"parsingPathList.name" 				:	$('#' + counter + '_parsingPathList_name').val(),
			"parsingPathList.readFilePath"      :	$('#' + counter + '_parsingPathList_readFilePath').val(),
			"writeFilePath" 					:	$('#' + counter + '_writeFilePath').val(),
			"readFilenamePrefix" 				:	$('#' + counter + '_readFilenamePrefix').val(),
			"readFilenameSuffix" 				:	$('#' + counter + '_readFilenameSuffix').val(),
			"readFilenameExcludeTypes"			:	$('#' + counter + '_readFilenameExcludeTypes').val(),
			"compressInFileEnabled"				:   $('#' + counter + '_compressInFileEnabled').val(),
			"compressOutFileEnabled"			:   $('#' + counter + '_compressOutFileEnabled').val(),
			"writeFilenamePrefix"				:   $('#' + counter + '_writeFilenamePrefix').val(),
			"serviceId"							: 	'${serviceId}',
			"name"								:	$('#' + counter + '_name').val(),
			"parserType.alias"						:	$('#' + counter + '_parserType').val(),
			"pathListCount"						: 	counter
		}, 
		
		success: function(data){
			hideProgressBar(counter);

			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
				
				var pathList = JSON.parse(responseObject["parsingPathList"]);
				
				$("#"+counter+"_savebtn").hide();
				<sec:authorize access='hasAuthority(\'UPDATE_PATHLIST\')'>
					$("#"+counter+"_updatebtn").show();
					$("#"+counter+"_updatebtn").attr("onclick","updatePathlistDB('"+counter+"')");
				</sec:authorize>
				$("#delete_"+counter).show();
				jQuery("#delete_"+counter).attr("id","deletePath_"+pathList.name);
				
				$('#' + counter + '_parserType').attr('disabled',true);
				
				// set block header
				$("#Pathlink_"+counter).text(pathList.name+" |");
				$("#link_"+counter).text(responseObject["name"]);
				<sec:authorize access='hasAuthority(\'UPDATE_PARSER\')'>
				$("#link_"+counter).css('textDecoration','underline');
				</sec:authorize>
				
				<sec:authorize access='!hasAuthority(\'UPDATE_PARSER\')'>
				$("#link_"+counter).css('textDecoration','none');
				</sec:authorize>
				
				$("#"+counter+"_parserId").val(responseObject["id"]);
				$("#"+counter+"_pathListId").val(pathList.id);
				$("#"+counter+"_parserMappingId").val('0');
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				
				hideProgressBar(counter);
				addErrorIconAndMsgForAjax(responseObject); 
					
			}else{
				hideProgressBar(counter);
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(counter);
			handleGenericError(xhr,st,err);
		}
	});
}

function updatePathlistDB(counter){
	
	resetWarningDisplay();
	clearAllMessages();
	showProgressBar(counter);
	var mappingId=$('#' + counter + '_parserMappingId').val();
	if(mappingId == '' || mappingId == null){
		mappingId='0';
	}
	
	$.ajax({
		url: '<%=ControllerConstants.UPDATE_IPLOG_PARSING_PATHLIST%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"id" 								:	$('#' + counter + '_parserId').val(), // wrapper id
			"parsingPathList.id" 				:	$('#' + counter + '_pathListId').val(),
			"parsingPathList.name" 				:	$('#' + counter + '_parsingPathList_name').val(),
			"parsingPathList.readFilePath"      :	$('#' + counter + '_parsingPathList_readFilePath').val(),
			"writeFilePath" 					:	$('#' + counter + '_writeFilePath').val(),
			"readFilenamePrefix" 				:	$('#' + counter + '_readFilenamePrefix').val(),
			"readFilenameSuffix" 				:	$('#' + counter + '_readFilenameSuffix').val(),
			"readFilenameExcludeTypes"			:	$('#' + counter + '_readFilenameExcludeTypes').val(),
			"compressInFileEnabled"				:   $('#' + counter + '_compressInFileEnabled').val(),
			"compressOutFileEnabled"			:   $('#' + counter + '_compressOutFileEnabled').val(),
			"writeFilenamePrefix"				:   $('#' + counter + '_writeFilenamePrefix').val(),
			"serviceId"							: 	'${serviceId}',
			"name"								:	$('#' + counter + '_name').val(),
			"parserType.alias"					:	$('#' + counter + '_parserType').val(),
			"parserMapping.id"					:   mappingId,
			"pathListCount"						: 	counter
		}, 
		
		success: function(data){
			hideProgressBar(counter);

			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
				
				$("#"+counter+"_updatebtn").show();
				$("#"+counter+"_savebtn").hide();
				
				var pathList = JSON.parse(responseObject["parsingPathList"]);
				$("#Pathlink_"+counter).text(pathList.name+" |");
				$("#link_"+counter).text(responseObject["name"]);
				$("#link_"+counter).css('textDecoration','underline');
				
			//	$("#title_"+counter).text(pathList.name + " | " +responseObject["name"]);
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				
				hideProgressBar(counter);
				addErrorIconAndMsgForAjax(responseObject); 
					
			}else{
				hideProgressBar(counter);
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(counter);
			handleGenericError(xhr,st,err);
		}
	});
}

function showProgressBar(pathListCounter){
	$('#' + pathListCounter + '_buttons-div').hide();
	$('#' + pathListCounter + '_progress-bar-div').show();
}

function hideProgressBar(pathListCounter){
	$('#' + pathListCounter + '_buttons-div').show();
	$('#' + pathListCounter + '_progress-bar-div').hide();
}

var deleteBlockId='';
function deletePathListPopup(counter){
	var pathListId=$('#'+counter+'_pathListId').val();
	deleteBlockId=counter;
	
	if(pathListId == null || pathListId == 'null' || pathListId==''){
		$("#flipbox_"+counter).remove();
		addPathlist('${iplog_parsing_service_configuration_form_bean.dataType}');
	}else{
		$("#deletepathListId").val(pathListId);
		$("#closeBtn").hide();
		$("#pathlistMessage").click();
	}
	
}

function deletePathListDetails(){
	resetWarningDisplay();
	clearAllMessages();
	$("#delete-popup-buttons").hide();
	$("#delete-progress-bar-div").show();
	 $.ajax({
		url: '<%=ControllerConstants.DELETE_IPLOG_PARSING_PATHLIST%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"pathlistId"   : $("#deletepathListId").val(),
		 }, 
		
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				$("#delete-popup-buttons").show();
				$("#delete-progress-bar-div").hide();
				closeFancyBox();
				showSuccessMsg(responseMsg);
				$("#flipbox_"+deleteBlockId).remove();
				
				addPathlist('${iplog_parsing_service_configuration_form_bean.dataType}');
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				showErrorMsgPopup(responseMsg);
				$("#delete-progress-bar-div").hide();
				$("#closeBtn").show();
			}else{
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsgPopup(responseMsg);
				$("#delete-progress-bar-div").hide();
				$("#closeBtn").show();
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

function redirectPluginConfig(pathListCounter){
	
	var parserId = $('#'+pathListCounter+"_parserId").val();
	var parserName = $('#'+pathListCounter+"_name").val();
	var parserType =	$('#'+pathListCounter+"_parserType").val();
	console.log("Parser type value is :: " + parserType);
	
	redirectParserConfig(parserId,parserName,parserType);
	
}

</script>
    		


