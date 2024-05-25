<%@page import="com.elitecore.sm.common.model.StateEnum"%>
<%@page import="com.elitecore.sm.services.model.PartitionFieldEnum"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@page import="com.elitecore.sm.services.model.HashDataTypeEnum"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>

<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script src="${pageContext.request.contextPath}/customJS/autoSuggestionForUnifiedField.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<style>
  .ui-autocomplete
   {
       z-index:4000 !important;
       max-height: 100px;
       overflow-y: auto;
       overflow-x: hidden;
   }
</style>   

<script>
	var pathlist = [];
</script>

<div class="tab-content no-padding">
	<div class="fullwidth mtop10">	
		<div class="title2">
			<span class="title2rightfield">
				<span class="title2rightfield-icon1-text">
					<sec:authorize access="hasAuthority('CREATE_PATHLIST')">
						<a href="#" id="addPath" onclick="addPathList();"><i class="fa fa-plus-circle">&nbsp;</i><spring:message code="parsing.pathlist.add.pathlist.label"></spring:message></a>
					</sec:authorize>
				</span>
			</span>
		</div>
		<div class="clearfix"></div>
	</div>
	<div class="fullwidth"> 
		<div id="pathList">
		</div>
	</div>
</div>

<a href="#divViewMorePopup" class="fancybox" style="display: none;" id="viewMore">#</a>
<div id="divViewMorePopup" style="width:100%; display: none;" >
    <div class="modal-content">
    	<input type="hidden" id="viewBlockId"/>
    	<input type="hidden" id="viewRowId"/>
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="parsing.pathlist.more.element.popup.header"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
        	<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
        	<jsp:include page="../collection/ftp/deviceDialog.jsp"></jsp:include>
        	<div class="col-md-6 no-padding">
           		<spring:message code="iplog.parsing.service.pathlist.file.pattern" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<input tabindex="4" id="view_fileNamePattern" maxlength="200" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" readonly="readonly"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
			<div class="col-md-6 no-padding">
           		<spring:message code="iplog.parsing.service.pathlist.file.prefix" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<input tabindex="4" id="view_readFilenamePrefix" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" readonly="readonly"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-6 no-padding">
           		<spring:message code="iplog.parsing.service.pathlist.file.suffix" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
             		<div class="input-group">
             			<input tabindex="4" id="view_readFilenameSuffix" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" readonly="readonly"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-6 no-padding">
           		<spring:message code="iplog.parsing.service.pathlist.file.contains" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<input tabindex="4" id="view_readFilenameContains" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" readonly="readonly"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-6 no-padding">
           		<spring:message code="iplog.parsing.service.pathlist.file.type.exclude" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<input tabindex="4" id="view_readFilenameExcludeTypes" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" readonly="readonly"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-6 no-padding">
           		<spring:message code="iplog.parsing.service.pathlist.input.file.compressed" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
             		<div class="input-group">
             			<select id="view_compressInFileEnabled" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" disabled="disabled">
             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
							<option value='${trueFalseEnum.name}' selected>${trueFalseEnum}</option>
						</c:forEach > 
						</select> 
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-6 no-padding">
           		<spring:message code="iplog.parsing.service.pathlist.output.file.compressed" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
             		<div class="input-group">
             			<select id="view_compressOutFileEnabled" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" disabled="disabled">
             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
							<option value='${trueFalseEnum.name}' selected>${trueFalseEnum}</option>
						</c:forEach > 
						</select> 
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
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
           		<spring:message code="iplog.parsing.service.pathlist.write.file.split" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
             		<div class="input-group">
             			<select id="view_writeFileSplit" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="top" title="${tooltip}" disabled="disabled">
             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
							<option value='${trueFalseEnum.name}' selected>${trueFalseEnum}</option>
						</c:forEach > 
						</select> 
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="top" title=""></i></span>
             		</div>
            	</div>
            </div>
                  <div class="col-md-6 no-padding">
           		<spring:message code="iplog.parsing.service.pathlist.max.file.count" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<input tabindex="4" id="view_maxFileCountAlert" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="top" title="${tooltip}" readonly="readonly"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="top" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-6 no-padding">
           		<spring:message code="iplog.parsing.service.pathlist.write.file.cdrheaderfooter" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<select id="view_writeCdrHeaderFooterEnabled" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" disabled="disabled">
             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
							<option value='${trueFalseEnum.name}' selected>${trueFalseEnum}</option>
						</c:forEach > 
						</select> 
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-6 no-padding">
           		<spring:message code="iplog.parsing.service.pathlist.write.file.cdrdefaultattributes" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<select id="view_writeCdrDefaultAttributes" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" disabled="disabled">
             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
							<option value='${trueFalseEnum.name}' selected>${trueFalseEnum}</option>
						</c:forEach > 
						</select> 
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
        </div>
        <div id="view_buttons-div" class="modal-footer padding10">
         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
        </div>
        <div id="view_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
			<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
		</div>		
        
    </div>
    <!-- /.modal-content --> 
</div>

<a href="#divAddPluginPopup" class="fancybox" style="display: none;" id="addMore">#</a>
<div id="divAddPluginPopup" style="width:100%; display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
            <h4 class="modal-title">Plugin Details</h4>
        </div>
        <div class="modal-body padding10 inline-form">
			<div class="nav-tabs-custom tab-content">												
				<ul class="nav nav-tabs pull-right">
					<sec:authorize access="hasAnyAuthority('CREATE_PARSER')">
					<li id="add_existing_parser_tab" class="active" onclick="loadPluginGrid();">
						<a href="#add_existing_parser" id="a-add-existing-parser" data-toggle="tab" aria-expanded="true" > 
							<spring:message	code="parsing.service.clone.plugin.tab.header" ></spring:message>
						</a>
					</li>
					<li id="add_new_parser_tab">
						<a href="#add_new_parser" id="a-add-new-parser"	data-toggle="tab" aria-expanded="false"> 
							<spring:message	code="parsing.service.addNew.plugin.tab.header" ></spring:message>
						</a>
					</li>
					</sec:authorize>														
				</ul>
				<div class="fullwidth no-padding tab-content">
					<div id="add_existing_parser" class="tab-pane active">						 			
						<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
						<div class="box-body table-responsive no-padding box" id=pluginListDiv>
							<table class="table table-hover" id="pluginList"></table>
							<div id="pluginListPagingDiv"></div>
							<div class="clearfix"></div>
							<div id="divLoading" align="center" style="display: none;">
								<img src="img/preloaders/Preloader_10.gif" />
							</div>
						</div>
						
						<div id="create_buttons-div" class="modal-footer padding10">
						
				         	<button id="addParserBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="cloneParser();"><spring:message code="btn.label.add"></spring:message></button>
				         	<button id="closeParserBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
				        </div>
				        <div id="create_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
							<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
						</div>										
					</div>
					<div id="add_new_parser" class="tab-pane">									
							<!-- <div id="divAddPluginPopup_new" style="width:100%; display: none;" >		 -->								    
							        <div class="modal-body padding10 inline-form">	
							        	<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
							        	<input type="hidden" id="addBlockId"/>
							        	<input type="hidden" id="create_parserMappingId"/>
							        	<div class="col-md-6 no-padding">
							           		<spring:message code="iplog.parsing.service.pathlist.plugin.name" var="tooltip"></spring:message>
							            	<div class="form-group">
							         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
							             		<div class="input-group">
							             			<input tabindex="4" id="create_name" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
							             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
							             		</div>
							            	</div>
							            </div>
										<div class="col-md-6 no-padding">
							           		<spring:message code="iplog.parsing.service.pathlist.plugin.type" var="tooltip"></spring:message>
							            	<div class="form-group">
							         			<div class="table-cell-label">${tooltip}</div>
							             		<div class="input-group">
							             			<select id="create_pluginType" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
							             			</select>
							             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
							             		</div>
							            	</div>
							            </div>
							            <div class="col-md-6 no-padding">
							           		<spring:message code="iplog.parsing.service.pathlist.file.pattern" var="tooltip"></spring:message>
							            	<div class="form-group">
							         			<div class="table-cell-label">${tooltip}</div>
							             		<div class="input-group">
							             			<input tabindex="4" id="create_fileNamePattern" maxlength="200" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
							             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
							             		</div>
							            	</div>
							            </div>
										<div class="col-md-6 no-padding">
							           		<spring:message code="iplog.parsing.service.pathlist.file.prefix" var="tooltip"></spring:message>
							            	<div class="form-group">
							         			<div class="table-cell-label">${tooltip}</div>
							             		<div class="input-group">
							             			<input tabindex="4" id="create_readFilenamePrefix" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
							             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
							             		</div>
							            	</div>
							            </div>
							            <div class="col-md-6 no-padding">
							           		<spring:message code="iplog.parsing.service.pathlist.file.suffix" var="tooltip"></spring:message>
							            	<div class="form-group">
							         			<div class="table-cell-label">${tooltip}</div>
							             		<div class="input-group">
							             			<input tabindex="4" id="create_readFilenameSuffix" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
							             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert leftTooltip" data-toggle="tooltip" data-placement="left" title=""></i></span>
							             		</div>
							            	</div>
							            </div>
							            <div class="col-md-6 no-padding">
							           		<spring:message code="iplog.parsing.service.pathlist.file.contains" var="tooltip"></spring:message>
							            	<div class="form-group">
							         			<div class="table-cell-label">${tooltip}</div>
							             		<div class="input-group">
							             			<input tabindex="4" id="create_readFilenameContains" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
							             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
							             		</div>
							            	</div>
							            </div>
							            <div class="col-md-6 no-padding">
							           		<spring:message code="iplog.parsing.service.pathlist.file.type.exclude" var="tooltip"></spring:message>
							            	<div class="form-group">
							         			<div class="table-cell-label">${tooltip}</div>
							             		<div class="input-group">
							             			<input tabindex="4" id="create_readFilenameExcludeTypes" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
							             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert leftTooltip" data-toggle="tooltip" data-placement="left" title=""></i></span>
							             		</div>
							            	</div>
							            </div>
							            <div class="col-md-6 no-padding">
							           		<spring:message code="iplog.parsing.service.pathlist.input.file.compressed" var="tooltip"></spring:message>
							            	<div class="form-group">
							         			<div class="table-cell-label">${tooltip}</div>
							             		<div class="input-group">
							             			<select id="create_compressInFileEnabled" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
							             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
														<option value='${trueFalseEnum.name}' selected>${trueFalseEnum}</option>
													</c:forEach > 
													</select> 
							             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
							             		</div>
							            	</div>
							            </div>
							            <div class="col-md-6 no-padding">
							           		<spring:message code="iplog.parsing.service.pathlist.output.file.compressed" var="tooltip"></spring:message>
							            	<div class="form-group">
							         			<div class="table-cell-label">${tooltip}</div>
							             		<div class="input-group">
							             			<select id="create_compressOutFileEnabled" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
							             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
														<option value='${trueFalseEnum.name}' selected>${trueFalseEnum}</option>
													</c:forEach > 
													</select> 
							             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
							             		</div>
							            	</div>
							            </div>
							             
							            <div class="col-md-6 no-padding">
							           		<spring:message code="iplog.parsing.service.pathlist.output.file.prefix" var="tooltip"></spring:message>
							            	<div class="form-group">
							         			<div class="table-cell-label">${tooltip}</div>
							             		<div class="input-group">
							             			<input tabindex="4" id="create_writeFilenamePrefix" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
							             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
							             		</div>
							            	</div>
							            </div>
							             
							            <div class="col-md-6 no-padding">
							           		<spring:message code="iplog.parsing.service.pathlist.output.file.path" var="tooltip"></spring:message>
							            	<div class="form-group">
							         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
							             		<div class="input-group">
							             			<input tabindex="4" id="create_writeFilePath" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
							             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert leftTooltip" data-toggle="tooltip" data-placement="left" title=""></i></span>
							             		</div>
							            	</div>
							            </div>
							            <div class="col-md-6 no-padding">
							           		<spring:message code="iplog.parsing.service.pathlist.write.file.split" var="tooltip"></spring:message>
							            	<div class="form-group">
							         			<div class="table-cell-label">${tooltip}</div>
							             		<div class="input-group">
							             			<select id="create_writeFileSplit" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="top" title="${tooltip}">
							             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
														<option value='${trueFalseEnum.name}' selected>${trueFalseEnum}</option>
													</c:forEach > 
													</select> 
							             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="top" title=""></i></span>
							             		</div>
							            	</div>
							            </div>
							               <div class="col-md-6 no-padding">
							           		<spring:message code="iplog.parsing.service.pathlist.max.file.count" var="tooltip"></spring:message>
							            	<div class="form-group">
							         			<div class="table-cell-label">${tooltip}</div>
							             		<div class="input-group">
							             			<input tabindex="4" id="create_maxFileCountAlert" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="top" title="${tooltip}" onkeydown="isNumericOnKeyDown(event)"/>
							             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert leftTooltip" data-toggle="tooltip" data-placement="left" title=""></i></span>
							             		</div>
							            	</div>
							            </div>
							            <div class="col-md-6 no-padding">
							           		<spring:message code="iplog.parsing.service.pathlist.write.file.cdrheaderfooter" var="tooltip"></spring:message>
							            	<div class="form-group">
							         			<div class="table-cell-label">${tooltip}</div>
							             		<div class="input-group">
							             			<select id="create_writeCdrHeaderFooterEnabled" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
							             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
														<option value='${trueFalseEnum.name}' selected>${trueFalseEnum}</option>
													</c:forEach > 
													</select> 
							             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
							             		</div>
							            	</div>
							            </div>
							            <div class="col-md-6 no-padding">
							           		<spring:message code="iplog.parsing.service.pathlist.write.file.cdrdefaultattributes" var="tooltip"></spring:message>
							            	<div class="form-group">
							         			<div class="table-cell-label">${tooltip}</div>
							             		<div class="input-group">
							             			<select id="create_writeCdrDefaultAttributes" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
							             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
														<option value='${trueFalseEnum.name}' selected>${trueFalseEnum}</option>
													</c:forEach > 
													</select> 
							             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
							             		</div>
							            	</div>
							            </div>
							        </div>
							        <div id="create_buttons-div" class="modal-footer padding10">
							         	<button id="addParserBtn1" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="addParserDetail();"><spring:message code="btn.label.submit"></spring:message></button>
							         	<button id="closeParserBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="resetcreateParserPopUp();closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
							        </div>
							        <div id="create_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
										<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
									</div>											        
							    <!-- /.modal-content --> 										
						<!-- </div> -->
					</div>
				</div>														
			</div>
		</div>
		<!-- <div id="add_buttons-div" class="modal-footer padding10">
        	<button type="button" id="btndelete" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="openDeletePopup();" style="display: none;">Delete</button>
        	<button type="button" class="btn btn-grey btn-xs " id="delete_close_btn" data-dismiss="modal" onclick="closeFancyBox();">Close</button>
        </div> -->
        <div id="add_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
			<label>Processing Request &nbsp;&nbsp; </label>
			<img src="img/processing-bar.gif" class="mCS_img_loaded">
		</div>
	</div>
</div>		

<a href="#divUpdatePluginPopup" class="fancybox" style="display: none;" id="updatePopup">#</a>
<div id="divUpdatePluginPopup" style="width:100%; display: none;" >
    <div class="modal-content">
    	<input type="hidden" id="updateBlockId"/>
    	<input type="hidden" id="updateRowId"/>
    	<input type="hidden" id="update_parserMappingId"/>
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="iplog.parsing.service.pathlist.plugin.detail.header"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
        	<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
        	<div class="col-md-6 no-padding">
           		<spring:message code="parsing.service.pathlist.plugin.type" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<select id="update_pluginType" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" disabled="disabled">
             			</select>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
        	<div class="col-md-6 no-padding">
           		<spring:message code="parsing.service.pathlist.plugin.id" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
             		<div class="input-group">
             			<input tabindex="4" id="update_pluginId" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" readonly="readonly"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
        	<div class="col-md-6 no-padding">
           		<spring:message code="parsing.service.pathlist.plugin.name" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
             		<div class="input-group">
             			<input tabindex="4" id="update_name" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-6 no-padding">
           		<spring:message code="iplog.parsing.service.pathlist.file.pattern" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<input tabindex="4" id="update_fileNamePattern" maxlength="200" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert leftTooltip" data-toggle="tooltip" data-placement="left" title=""></i></span>
             		</div>
            	</div>
            </div>
			<div class="col-md-6 no-padding">
           		<spring:message code="parsing.service.pathlist.file.prefix" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<input tabindex="4" id="update_readFilenamePrefix" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert leftTooltip" data-toggle="tooltip" data-placement="left" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-6 no-padding">
           		<spring:message code="parsing.service.pathlist.file.suffix" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<input tabindex="4" id="update_readFilenameSuffix" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-6 no-padding">
           		<spring:message code="parsing.service.pathlist.file.contains" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<input tabindex="4" id="update_readFilenameContains" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert leftTooltip" data-toggle="tooltip" data-placement="left" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-6 no-padding">
           		<spring:message code="parsing.service.pathlist.file.type.exclude" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<input tabindex="4" id="update_readFilenameExcludeTypes" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-6 no-padding">
           		<spring:message code="parsing.service.pathlist.input.file.compressed" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<select id="update_compressInFileEnabled" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
							<option value='${trueFalseEnum.name}' selected>${trueFalseEnum}</option>
						</c:forEach > 
						</select> 
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-6 no-padding">
           		<spring:message code="parsing.service.pathlist.output.file.compressed" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<select id="update_compressOutFileEnabled" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
							<option value='${trueFalseEnum.name}' selected>${trueFalseEnum}</option>
						</c:forEach > 
						</select>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-6 no-padding">
           		<spring:message code="parsing.service.pathlist.output.file.prefix" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<input tabindex="4" id="update_writeFilenamePrefix" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert leftTooltip" data-toggle="tooltip" data-placement="left" title=""></i></span>
             		</div>
            	</div>
            </div>

            <div class="col-md-6 no-padding">
           		<spring:message code="parsing.service.pathlist.output.file.path" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
             		<div class="input-group">
             			<input tabindex="4" id="update_writeFilePath" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-6 no-padding">
           		<spring:message code="iplog.parsing.service.pathlist.write.file.split" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
             		<div class="input-group">
             			<select id="update_writeFileSplit" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="top" title="${tooltip}">
             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
							<option value='${trueFalseEnum.name}' selected>${trueFalseEnum}</option>
						</c:forEach > 
						</select> 
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="top" title=""></i></span>
             		</div>
            	</div>
            </div>
             <div class="col-md-6 no-padding">
           		<spring:message code="iplog.parsing.service.pathlist.max.file.count" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<input tabindex="4" id="update_maxFileCountAlert" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="top" title="${tooltip}"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="top" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-6 no-padding">
           		<spring:message code="parsing.service.pathlist.write.file.cdrheaderfooter" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<select id="update_writeCdrHeaderFooterEnabled" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
							<option value='${trueFalseEnum.name}' selected>${trueFalseEnum}</option>
						</c:forEach > 
						</select>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-6 no-padding">
           		<spring:message code="parsing.service.pathlist.write.file.cdrdefaultattributes" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<select id="update_writeCdrDefaultAttributes" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
             			<c:forEach var='trueFalseEnum' items='${trueFalseEnum}' >
							<option value='${trueFalseEnum.name}' selected>${trueFalseEnum}</option>
						</c:forEach > 
						</select>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
             		</div>
            	</div>
            </div>
            
        </div>
        <div id="update_buttons-div" class="modal-footer padding10">
        	<sec:authorize access="hasAuthority('UPDATE_PARSER')">
         		<button id="update_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="updateParserDetail('<%= BaseConstants.UPDATE_MODE %>');"><spring:message code="btn.label.submit"></spring:message></button>
        	</sec:authorize>
			<button id="close_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
        </div>
        <div id="update_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
			<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
		</div>	
        
    </div>
    <!-- /.modal-content --> 
</div>

<a href="#divDeleteComfirmPopup" class="fancybox" style="display: none;" id="deletePopup">#</a>
<div id="divDeleteComfirmPopup" style="width:100%; display: none;" >
    <div class="modal-content">
    	<input type="hidden" id="deleteBlockId"/>
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="device.delete.popup.warning.heading.label"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
        	<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
        </div>
        
		<div class="modal-body padding10 inline-form">
			<span id="warning_msg" style="display: none;"><spring:message code="warning.plugin.message"></spring:message></span>			
			<span id="delete_plugin_note" style="display: none;"><spring:message code="delete.composer.warn.msg"></spring:message></span>
		</div>
        
        <div id="delete_buttons-div" class="modal-footer padding10">
         	<button type="button" id="btndelete" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="openDeletePopup();" style="display: none;"><spring:message code="btn.label.delete"></spring:message></button>
         	<button type="button" class="btn btn-grey btn-xs " id="delete_close_btn" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
        </div>
        <div id="delete_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
			<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
		</div>	
        
    </div>
    <!-- /.modal-content --> 
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
		        	</span>
		        	<input type="hidden" id="deletepathListId" name="deletepathListId"/>
			        <p id="deleteWarningMessage">
			       		 <spring:message code="pahtlist.delete.warn.message"></spring:message>
			        </p>
		        </div>
		        
		       <!-- To Do add role here -->
			        <div id="delete-popup-buttons" class="modal-footer padding10">
		        		<button id="delete_btn" type="button" class="btn btn-grey btn-xs " onclick="deletePathListDetails();"><spring:message code="btn.label.yes"></spring:message></button>
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
			            <button type="button" class="btn btn-grey btn-xs " id="closeBtn" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        	</div>
		        	<div id="delete-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
			</div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
    	<a href="#divMessage" class="fancybox" style="display: none;" id="pathlistMessage">#</a>
    	<!-- Pathlist Delete popup code end here -->

<script>
var pathListCounter=-1;

$(document).ready(function() {
	
	var tempPath = '${pathList}';
	
	console.log("before eval path is :: " + tempPath);
	
	pathlist =   eval('${pathList}');
	//pathlist =  tempPath;  //JSON.parse(JSON.stringify(tempPath)); //JSON.parse('${pathList}');
	
	
	$(function() {
	    $("#create_writeFileSplit").val('false');
	});
	$.each(pathlist, function(index,path){
		console.log("first read file path is :: " + path.readPath);
		addPathListDetail(path.id,path.name,path.readPath,path.archivePath,path.parserWrapper, path.fileGrepDateEnabled, path.dateFormat, path.startIndex, path.endIndex, path.position, path.pathId, path.deviceId, path.deviceName, path.referenceDeviceName,path.circle,path.mandatoryFields);
	});
	
	$(function() {
	    $("#create_writeCdrHeaderFooterEnabled").val('false');
	});
	// making write cdr default attributes as false by default
	$(function() {
	    $("#create_writeCdrDefaultAttributes").val('false');
	});
	
});

$("#123456").tooltip({
    placement: "left"
});

function resetPathList(counter){
	resetWarningDisplay();
	$('#'+ counter+ '_block input').val('');
    $("#"+counter+"_fileGrepDateEnabled").val('false');
    $("#"+counter+"_position").val('left')
    $("#"+counter+"_circle").val('1')
    $("#"+counter+"_startIndex").val('-1')
    $("#"+counter+"_endIndex").val('-1')
    $("#"+counter+"_dateFormat").val('yyyyMMddHHmm');
    changeFileDateParam(counter);
}


function dsiplayGridForPathlist(counter,parserList){
	$.each(parserList,function(index,parser){
		parser.more	='<i class="fa fa-list" onclick="openViewMorePopup('+counter+','+parseInt(parser.id)+')" style="cursor:pointer;"></i>';
		parser.edit	='<a onclick="openEditPopup('+counter+','+parseInt(parser.id)+')" id="editParser_'+parser.name+'"> <i class="fa fa-pencil-square-o" aria-hidden="true" id="img_'+parseInt(parser.id)+'" data-toggle="tooltip" data-placement="bottom" style="cursor:pointer;" ></i> </a>';
	});
	var uniqueGridId = counter+"_grid";
	var selectAllCheckboxId = counter+"_selectAllPlugin";
	var childCheckboxName = counter+"_pluginCheckbox"
	$("#"+uniqueGridId).jqGrid({
    	url: "",
        datatype: "local",
        colNames:[
                  "<input style='display:none' type='checkbox' id='"+selectAllCheckboxId+"' onclick='pluginHeaderCheckbox(event, this, \""+childCheckboxName+"\")'></input>",
				  "<spring:message code='parsing.pathlist.plugin.grid.column.pid' ></spring:message>",
                  "<spring:message code='parsing.pathlist.plugin.grid.column.name' ></spring:message>",
                  "<spring:message code='parsing.pathlist.plugin.grid.column.type.id' ></spring:message>",
                  "<spring:message code='parsing.pathlist.plugin.grid.column.type' ></spring:message>",
                  "",
                  "<spring:message code='iplog.parsing.service.pathlist.file.pattern' ></spring:message>",
                  "<spring:message code='parsing.pathlist.plugin.grid.column.read.prefix' ></spring:message>",
                  "<spring:message code='parsing.pathlist.plugin.grid.column.read.suffix' ></spring:message>",
                  "<spring:message code='parsing.pathlist.plugin.grid.column.read.contains' ></spring:message>",
                  "<spring:message code='parsing.pathlist.plugin.grid.column.read.exclude' ></spring:message>",
                  "<spring:message code='parsing.pathlist.plugin.grid.column.compress.read.enable' ></spring:message>",
                  "<spring:message code='parsing.pathlist.plugin.grid.column.compress.write.enable' ></spring:message>",
                  "<spring:message code='parsing.pathlist.plugin.grid.column.write.path' ></spring:message>",
                  "<spring:message code='parsing.pathlist.plugin.grid.column.write.prefix' ></spring:message>",
                  "<spring:message code='iplog.parsing.service.pathlist.write.file.split' ></spring:message>",
                  "<spring:message code='iplog.parsing.service.pathlist.max.file.count' ></spring:message>",
                  "<spring:message code='iplog.parsing.service.pathlist.write.file.cdrheaderfooter' ></spring:message>",
                  "<spring:message code='iplog.parsing.service.pathlist.write.file.cdrdefaultattributes' ></spring:message>",
                  "<spring:message code='parsing.pathlist.plugin.grid.column.mapping.id' ></spring:message>",
                  "<spring:message code='parsing.pathlist.plugin.grid.column.more' ></spring:message>",
                  "<spring:message code='parsing.pathlist.plugin.grid.column.edit' ></spring:message>"
                 ],
		colModel:[
			{name:'checkbox',index:'checkbox',sortable:false,align:'center', width : '30%', formatter: function(cellvalue, options, rowObject) {
				return pluginCheckboxFormatter(cellvalue, options, rowObject, uniqueGridId, selectAllCheckboxId, childCheckboxName);	
			}},
			{name:'id',index:'id',sortable:true},
        	{name:'name',index:'name',sortable:true,formatter:pluginNameFormatter},
        	{name:'parserTypeId',index:'parserTypeId',sortable:true,hidden: true},
        	{name:'parserType',index:'parserType',sortable:true},
        	{name:'parserTypeAlias',index:'parserType',hidden:true},
        	{name:'fileNamePattern',index:'fileNamePattern',sortable:false,hidden: true},
        	{name:'readFilenamePrefix',index:'readFilenamePrefix',sortable:true,hidden: true},
        	{name:'readFilenameSuffix',index:'readFilenameSuffix',sortable:true,hidden: true},
        	{name:'readFilenameContains',index:'readFilenameContains',sortable:true,hidden: true},
        	{name:'readFilenameExcludeTypes',index:'readFilenameExcludeTypes',sortable:true,hidden: true},
        	{name:'compressInFileEnabled',index:'compressInFileEnabled',sortable:true,hidden: true},
        	{name:'compressOutFileEnabled',index:'compressOutFileEnabled',sortable:true,hidden: true},
        	{name:'writeFilePath',index:'writeFilePath',sortable:true},
            {name:'writeFilenamePrefix',index:'writeFilenamePrefix',sortable:true,hidden: true},
            {name:'writeFileSplit',index:'writeFileSplit',sortable:true,hidden: true},
            {name:'maxFileCountAlert',index:'maxFileCountAlert',sortable:false,hidden: true},
            {name:'writeCdrHeaderFooterEnabled',index:'writeCdrHeaderFooterEnabled',sortable:true,hidden: true},
            {name:'writeCdrDefaultAttributes',index:'writeCdrDefaultAttributes',sortable:true,hidden: true},
            {name:'parserMappingId',index:'parserMappingId',sortable:true,hidden: true},
        	{name:'more',index:'more',sortable:false,align:'center'},
        	{name:'edit',index:'edit',sortable:false,align:'center'}
        ],
        data:parserList,
        rowNum: <%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
        rowList:[10,20,60,100],
        height: 'auto',
        mtype:'POST',
		sortname: 'id',
 		sortorder: "desc",
        pager: "#"+counter+"_gridPagingDiv",
        contentType: "application/json; charset=utf-8",
        viewrecords: true,
        //multiselect: true,
        timeout : 120000,
        loadtext: "Loading...",
        caption: "<spring:message code='parsing.pathlist.plugin.grid.header'></spring:message>",
        beforeRequest:function(){
            $(".ui-dialog-titlebar").hide();
        }, 
        beforeSend: function(xhr) {
        	xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
 		 loadComplete: function(data) {
			$(".ui-dialog-titlebar").show();
		}, 
		onPaging: function (pgButton) {
			
		},
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		onSelectAll:function(id,status){
		},
		recordtext: "<spring:message code='parsing.pathlist.plugin.grid.pager.total.records.text'></spring:message>",
        emptyrecords: "<spring:message code='parsing.pathlist.plugin.grid.empty.records'></spring:message>",
		loadtext: "<spring:message code='parsing.pathlist.plugin.grid.loading.text'></spring:message>",
		pgtext : "<spring:message code='parsing.pathlist.plugin.grid.pager.text'></spring:message>",
	}).navGrid("#gridPagingDiv",{edit:true,add:false,del:false,search:false});
	
	$(".ui-jqgrid-titlebar").hide();
	if (parserList.length >= 1)
		addLinkHideShow(counter,false);
}

function addPathList(){
	pathListCounter++;
	pathlist[pathListCounter] = {};
	
	var html = "<div class='box box-warning' id='flipbox_"+pathListCounter+"'>  "+
				"<input type='hidden' id='"+pathListCounter+"_pluginId' > "+   
				"<input type='hidden' id='"+pathListCounter+"_pathListId'> "+ 
				"<div class='box-header with-border'> "+
				"	<h3 class='box-title' id='title_"+pathListCounter+"'><spring:message code='parsing.service.pathlist.name'></spring:message> "+ 											
				"	</h3>  "+
				"	<div class='box-tools pull-right' style='top: -3px;' id='action_"+pathListCounter+"'> "+ 
				"		<button class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' href='#"+pathListCounter+"_block'> "+ 
				"			<i class='fa fa-minus'></i> "+
				"		</button> "+
				"					<sec:authorize access='hasAuthority(\'DELETE_PATHLIST\')'>"+
			    "			&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;' id='deletePath' onclick=deletePathListPopup(\'"+pathListCounter+"\');></a>&nbsp;" +
			    "					</sec:authorize>"+
				"	</div> "+
				"</div> "+
				"<div class='box-body inline-form accordion-body collapsed' id='"+pathListCounter+"_block'> "+ 
				"	<div class='fullwidth inline-form'>  "+
				"		<div class='col-md-6 no-padding'> "+
				"			<spring:message code='parsing.service.pathlist.name' var='tooltip' ></spring:message> "+ 
				"			<div class='form-group'>  "+
				"				<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+ 
				"				<div class='input-group '> "+
				"					<input type='text' id='"+pathListCounter+"_name' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+ 
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title='' ></i></span> "+
				"				</div> "+
				"			</div> "+
				"		</div> "+
				"		<div class='col-md-6 no-padding'> "+ 
				"			<spring:message code='iplog.parsing.service.pathlist.source.path' var='tooltip' ></spring:message> "+ 
				"			<div class='form-group'>  "+
				"				<div class='table-cell-label'>${tooltip}<span class='required'>*</span><i class='fa fa-square'></i></div> "+ 
				"				<div class='input-group '> "+
				"					<input type='text' id='"+pathListCounter+"_readFilePath' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/> "+ 	
				"					<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
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
				"			<input type='hidden' class='form-control table-cell input-sm' id='"+pathListCounter+"_deviceId'  data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
				"			</input>"+
				"           <a href='#' onclick='selectDevicePopUp("+pathListCounter+");' tabindex='9' style='vertical-align:middle;' id='select_popup_"+ pathListCounter +"_lnk'><i style='margin-top: 8px;margin-left: 8px;' class='glyphicon glyphicon-search'></i></a>" +
				"			<input type='text' class='form-control table-cell input-sm' id='"+pathListCounter+"_parentDevice'  data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' style='width:94%;' readonly=true >"+
				"			</input>"+
				"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
				"		</div> "+
				"	</div> "+
				"</div> "+


			    "				<div class='col-md-6 no-padding'>"+
			    "					<spring:message code='parsingService.plugin.popup.circle.label' var='label'></spring:message>"+
			    "					<spring:message code='parsingService.plugin.popup.circle.label' var='tooltip'></spring:message>"+
			    "  				<div class='form-group'>"+
				"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
			    "      				<div class='input-group '>"+
				"  						<select class='form-control table-cell input-sm' id='"+pathListCounter+"_circle' tabindex='14' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
			    "   						<c:forEach var='circle' items='${circleList}'>"+
			    "    							<option value='${circle.id}'>${circle.name}</option>" +
			    "    						</c:forEach>" +
			    "   						</select>" +
				"							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
			    "        				</div>"+
			    "    				</div>"+
			    "				</div>"+
				
				//mandatory field
			    "				<div class='col-md-6 no-padding'>"+
			    "					<spring:message code='parsingService.plugin.popup.mandatory.field.label' var='label'></spring:message>"+
			    "					<spring:message code='parsingService.plugin.popup.mandatory.field.label' var='tooltip'></spring:message>"+
			    "  				<div class='form-group'>"+
				"				<div class='table-cell-label'>${label}</div> "+ 
			    "      				<div class='input-group '>"+
				"					<textarea id='"+pathListCounter+"_mandatoryFields' data-toggle='tooltip' data-placement='bottom' title='${tooltipDesc}' class='md-textarea form-control' rows='3' onkeypress=triggerAutoSuggest(\'#"+pathListCounter+"_mandatoryFields\','getAllUnifiedField') onBlur=validateMandatoryFields("+pathListCounter+")></textarea>"+
				"					<input type='hidden' id='"+pathListCounter+"_mandatoryFields'>" +
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
				"					</div> "+
			    "    				</div>"+
			    "				</div>"+
				
				
				
				
				// add New Pathlist : reference devicename
				/* "<div class='col-md-6 no-padding'>"+
		        "	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.reference.devicename' var='label'></spring:message>"+
		        "	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.reference.devicename.tooltip' var='tooltip'></spring:message>"+
		        "  	<div class='form-group'>"+
		        "    	<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
		        "      	<div class='input-group '>"+
		        "      		<input class='form-control table-cell input-sm' id='" + pathListCounter + "_referenceDevice'  tabindex='10' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' />"+
				"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
		        "        </div>"+
		        "     </div>"+
		        "</div>"+ */
				
				
				
				
				
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
				    "      					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_dateFormat' tabindex='13' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='yyyyMMddHHmm'/>"+
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
				    "      					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_startIndex' onkeydown=isNumericOnKeyDown(event); tabindex='15' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='-1' />"+
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
				    "      					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_endIndex' onkeydown=isNumericOnKeyDown(event); tabindex='16' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='-1' />"+
				    "							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
				    "       				</div>"+
				    "   				</div>"+
				    "				</div>"+

				"		<div class='col-md-12 no-padding'> "+ 
				"			<div class='form-group'> "+
				"				<div id='"+pathListCounter+"_buttons-div' class='input-group '> "+ 
				"					<sec:authorize access='hasAuthority(\'UPDATE_PATHLIST\')'> "+
				"						<button type='button' style='display:none;' class='btn btn-grey btn-xs' id='"+pathListCounter+"_updatebtn' onclick=updatePathDetail(\'"+pathListCounter+"\')><spring:message code='btn.label.update' ></spring:message></button>&nbsp;"+
				"					</sec:authorize> "+
				"					<sec:authorize access='hasAuthority(\'CREATE_PATHLIST\')'> "+
				"						<button type='button' class='btn btn-grey btn-xs' id='"+pathListCounter+"_addbtn' onclick=savePathDetail(\'"+pathListCounter+"\')><spring:message code='btn.label.add'></spring:message></button>&nbsp;"+ 
				"						<button type='button' class='btn btn-grey btn-xs' id='"+pathListCounter+"_resetbtn' onclick=resetPathList(\'"+pathListCounter+"\')><spring:message code='btn.label.reset'></spring:message></button>&nbsp;"+
				"					</sec:authorize> "+
				"				</div> "+
				"				<div id='"+pathListCounter+"_progress-bar-div' class='input-group' style='display: none;'>"+  
				"					<label>Processing Request &nbsp;&nbsp; </label>  "+
				"					<img src='img/processing-bar.gif'> "+
				"				</div> "+
				"			</div>  "+
				"		</div>  "+
				"		<div class='col-md-12 no-padding gridBlock' > "+ 
				"			<div class='title2'> "+
				"				<spring:message code='parsing.pathlist.plugin.grid.header' ></spring:message>"+ 
				"					<span class='title2rightfield'> "+
				"		          		<span class='title2rightfield-icon1-text'> "+
				"							<sec:authorize access='hasAnyAuthority(\'CREATE_PARSER\')'> "+
				"		          			<a href='#' name='" + pathListCounter +  "_addlinkicon' onclick=addPlugin('"+pathListCounter+"');><i class='fa fa-plus-circle'></i></a> "+
				"	  						<a href='#' name='" + pathListCounter +  "_addlink' id='addPlugin' onclick=addPlugin('"+pathListCounter+"');> "+
				"	  							<spring:message code='btn.label.add' ></spring:message>  "+
				"	  						</a> "+
				"							</sec:authorize> "+
				"	      				</span>	  "+
				"		          		<span class='title2rightfield-icon1-text'> "+
				"							<sec:authorize access='hasAuthority(\'DELETE_PARSER\')'> "+
				"		          			<a href='#' onclick=openDeletePopup('"+pathListCounter+"');> <i class='fa fa-trash'></i></a> "+
				"		          			<a href='#' id='deleteService' onclick=openDeletePopup('"+pathListCounter+"');><spring:message code='btn.label.delete' ></spring:message></a> "+
				"							</sec:authorize> "+
				"		          		</span> "+
				"		          	</span> "+
				"			</div> "+
				"		</div> "+
				"		<div class='col-md-12 no-padding gridBlock'> "+ 
				"			<div class='box-body table-responsive no-padding box'> "+
				"            	<table class='table table-hover' id='"+pathListCounter+"_grid'></table> "+
				"               	<div id='"+pathListCounter+"_gridPagingDiv' style='display:none'></div> "+
				"	           	<div class='clearfix'></div> "+
				"	           	<div id='divLoading' align='center' style='display: none;'><img src='img/preloaders/Preloader_10.gif' /></div> "+  
				"           </div> "+
				"		</div> "+
				"	</div> "+
				"</div> "+
				"</div> ";
				
	$('#pathList').prepend(html);
	$("#"+pathListCounter+"_block").collapse("toggle");
	$('#'+pathListCounter+'_block .gridBlock').hide();
	changeArchivePathField(pathListCounter);
	changeFileDateParam(pathListCounter);
	setDefaultCircle(pathListCounter);
}

function addPathListDetail(pathListId, pathListName, readPath, archivePath, wrapperList, fileGrepDateEnabled, dateFormat, startIndex, endIndex, position, pathId, deviceId, deviceName, referenceDeviceName,circleId,mandatoryFields){
	console.log("Read file path is :: " + readPath);
	
	pathListCounter++;
	if(archivePath == undefined || archivePath == null){
		archivePath = '';
	}
	if(readPath == undefined || readPath == null){
		readPath = '';
	}
	if(pathListName == undefined || pathListName == null){
		pathListName = '';
	}
	var html = "<div class='box box-warning' id='flipbox_"+pathListCounter+"'>  "+
				"<input type='hidden' id='"+pathListCounter+"_pluginId' > "+   
				"<input type='hidden' id='"+pathListCounter+"_pathListId' value='"+pathListId+"'> "+
				"<input type='hidden' id='"+pathListCounter+"_pathId' value='"+pathId+"'> "+ 
				"<div class='box-header with-border'> "+
				"	<h3 class='box-title' id='title_"+pathListCounter+"'>"+pathListName+" - "+pathId+ 											
				"	</h3>  "+
				"	<div class='box-tools pull-right' style='top: -3px;' id='action_"+pathListCounter+"'> "+ 
				"		<button class='btn btn-box-tool block-collapse-btn' id='collapseBtn_"+pathListName+"' data-toggle='collapse' href='#"+pathListCounter+"_block'> "+ 
				"			<i class='fa fa-minus'></i> "+
				"		</button> "+
				"					<sec:authorize access='hasAuthority(\'DELETE_PATHLIST\')'>"+
			    "			&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;' id='deletePath_"+pathListName+"' onclick=deletePathListPopup(\'"+pathListCounter+"\');></a>&nbsp;" +
			    "					</sec:authorize>"+
				"	</div> "+
				"</div> "+
				"<div class='box-body inline-form accordion-body collapsed in' id='"+pathListCounter+"_block'> "+ 
				"	<div class='fullwidth inline-form'>  "+
				"		<div class='col-md-6 no-padding'> "+
				"			<spring:message code='parsing.service.pathlist.name' var='tooltip' ></spring:message> "+ 
				"			<div class='form-group'>  "+
				"				<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+ 
				"				<div class='input-group '> "+
				"					<input type='text' id='"+pathListCounter+"_name' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+pathListName+"'/>"+ 
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title='' ></i></span> "+
				"				</div> "+
				"			</div> "+
				"		</div> "+
				"		<div class='col-md-6 no-padding'> "+ 
				"			<spring:message code='iplog.parsing.service.pathlist.source.path' var='tooltip' ></spring:message> "+ 
				"			<div class='form-group'>  "+
				"				<div class='table-cell-label'>${tooltip}<span class='required'>*</span><i class='fa fa-square'></i></div> "+ 
				"				<div class='input-group '> "+
				"					<input type='text' id='"+pathListCounter+"_readFilePath' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readPath+"'/> "+ 	
				"					<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span> "+
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

				//circle Name

			    "				<div class='col-md-6 no-padding'>"+
			    "					<spring:message code='parsingService.plugin.popup.circle.label' var='label'></spring:message>"+
			    "					<spring:message code='parsingService.plugin.popup.circle.label' var='tooltip'></spring:message>"+
			    "  				<div class='form-group'>"+
				"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
			    "      				<div class='input-group '>"+
				"  						<select class='form-control table-cell input-sm' id='"+pathListCounter+"_circle' tabindex='14' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
			    "   						<c:forEach var='circle' items='${circleList}'>"+
			    "    							<option value='${circle.id}'>${circle.name}</option>" +
			    "    						</c:forEach>" +
			    "   						</select>" +
				"							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
			    "        				</div>"+
			    "    				</div>"+
			    "				</div>"+
			    
			      //mandatory field
			    "				<div class='col-md-6 no-padding'>"+
			    "					<spring:message code='parsingService.plugin.popup.mandatory.field.label' var='label'></spring:message>"+
			    "					<spring:message code='parsingService.plugin.popup.mandatory.field.label' var='tooltip'></spring:message>"+
			    "  				<div class='form-group'>"+
				"				<div class='table-cell-label'>${label}</div> "+ 
			    "      				<div class='input-group '>"+
				"					<textarea id='"+pathListCounter+"_mandatoryFields' data-toggle='tooltip' data-placement='bottom' title='${tooltipDesc}' class='md-textarea form-control' rows='3' onkeypress=triggerAutoSuggest(\'#"+pathListCounter+"_mandatoryFields\','getAllUnifiedField') onBlur=validateMandatoryFields("+pathListCounter+")></textarea>"+
				"					<input type='hidden' id='"+pathListCounter+"_mandatoryFields'>" +
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
				"					</div> "+
			    "    				</div>"+
			    "				</div>"+
			    
				//reference devicename
				/* "<div class='col-md-6 no-padding'>"+
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

				
				"		<div class='col-md-12 no-padding'> "+ 
				"			<div class='form-group'> "+
				"				<div id='"+pathListCounter+"_buttons-div' class='input-group '> "+ 
				"					<sec:authorize access='hasAuthority(\'UPDATE_PATHLIST\')'> "+
				"						<button type='button' class='btn btn-grey btn-xs' id='"+pathListCounter+"_updatebtn' onclick=updatePathDetail(\'"+pathListCounter+"\')><spring:message code='btn.label.update'></spring:message></button>&nbsp;"+ 
				"						<button type='button' class='btn btn-grey btn-xs' id='"+pathListCounter+"_resetbtn' onclick=resetPathList(\'"+pathListCounter+"\')><spring:message code='btn.label.reset'></spring:message></button>&nbsp;"+
				"					</sec:authorize> "+
				"				</div> "+
				"				<div id='"+pathListCounter+"_progress-bar-div' class='input-group' style='display: none;'>"+  
				"					<label>Processing Request &nbsp;&nbsp; </label>  "+
				"					<img src='img/processing-bar.gif'> "+
				"				</div> "+
				"			</div>  "+
				"		</div>  "+
				"		<div class='col-md-12 no-padding'> "+ 
				"			<div class='title2'> "+
				"				<spring:message code='parsing.pathlist.plugin.grid.header' ></spring:message>"+ 
				"					<span class='title2rightfield'> "+
				"		          		<span class='title2rightfield-icon1-text'> "+
				"							<sec:authorize access='hasAnyAuthority(\'CREATE_PARSER\')'> "+
				"		          				<a href='#' name='" + pathListCounter +  "_addlinkicon' onclick=addPlugin('"+pathListCounter+"');><i class='fa fa-plus-circle'></i></a> "+
				"	  							<a href='#' name='" + pathListCounter +  "_addlink' id='"+pathListName+"_addPlugin' onclick=addPlugin('"+pathListCounter+"');> "+
				"	  								<spring:message code='btn.label.add' ></spring:message>  "+
				"	  							</a> "+
				"							</sec:authorize> "+
				"	      				</span>	  "+
				"		          		<span class='title2rightfield-icon1-text'> "+
				"							<sec:authorize access='hasAuthority(\'DELETE_PARSER\')'> "+
				"		          				<a href='#' onclick=openDeletePopup('"+pathListCounter+"');> <i class='fa fa-trash'></i></a> "+
				"		          				<a href='#' id='deleteService' onclick=openDeletePopup('"+pathListCounter+"');><spring:message code='btn.label.delete' ></spring:message></a> "+
				"							</sec:authorize> "+
				"		          		</span> "+
				"		          	</span> "+
				"			</div> "+
				"		</div> "+
				"		<div class='col-md-12 no-padding'> "+ 
				"			<div class='box-body table-responsive no-padding box'> "+
				"            	<table class='table table-hover' id='"+pathListCounter+"_grid'></table> "+
				"               	<div id='"+pathListCounter+"_gridPagingDiv' style='display:none'></div> "+
				"	           	<div class='clearfix'></div> "+
				"	           	<div id='divLoading' align='center' style='display: none;'><img src='img/preloaders/Preloader_10.gif' /></div> "+  
				"           </div> "+
				"		</div> "+
				"	</div> "+
				"</div> "+
				"</div> ";
	$('#pathList').prepend(html);
	$("#"+ pathListCounter+"_fileGrepDateEnabled").val(fileGrepDateEnabled.toString());
	$("#"+ pathListCounter+"_position").val(position.toString());
	$("#"+ pathListCounter+"_circle").val(circleId);
	$("#"+pathListCounter+"_block").collapse("toggle");
	$("#"+pathListCounter+"_mandatoryFields").val(mandatoryFields);
	changeFileDateParam(pathListCounter);
	dsiplayGridForPathlist(pathListCounter,wrapperList);
	changeArchivePathField(pathListCounter);
}

function setDefaultCircle(pathListCounter){
		
	$("#"+pathListCounter+"_circle [value=1]").attr('selected', 'true');
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

function validateMandatoryFields(counter){
	 var unifiedFields=$("#"+counter+"_mandatoryFields").val();
	 unifiedFields = unifiedFields.replace(/ +/g, "");
		var lastChar = unifiedFields[unifiedFields.length -1];
		if(lastChar==','){
		 unifiedFields=unifiedFields.substring(0, unifiedFields.length - 1);
		}
		var myArray=unifiedFields.split(',');
		var myNewArray = myArray.filter(function(elem, index, self) {
		     return index === self.indexOf(elem);
		 });
     $("#"+counter+"_mandatoryFields").val(myNewArray);		
}

function savePathDetail(counter){
	
	showProgressBar(counter);
	
	var startIndex = $("#"+counter+"_startIndex").val();
	var endIndex = $("#"+counter+"_endIndex").val();
	if(startIndex === null || startIndex === '' || startIndex === 'undefined'){
		$("#"+counter+"_startIndex").val('-1')
	}
	if(endIndex === null || endIndex === '' || endIndex === 'undefined'){
		$("#"+counter+"_endIndex").val('-1')
	}
	var mandatoryFields = $("#"+counter+"_mandatoryFields").val();
		if (mandatoryFields === null || mandatoryFields === '' || mandatoryFields === 'undefined')
				mandatoryFields = "";
	
	$.ajax({
		url: '<%=ControllerConstants.CREATE_PARSING_PATHLIST%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"readFilePath" 			   : $('#'+counter+'_readFilePath').val(),
			"archivePath"              : $('#'+counter+'_archivePath').val(),
			"name"					   : $('#'+counter+'_name').val(),
			"service.id"			   : parseInt('${serviceId}'),
			"service.serverInstance.id": parseInt('${instanceId}'),
			"fileGrepDateEnabled"      : $("#"+counter+"_fileGrepDateEnabled").val(),
			"position"			       : $("#"+counter+"_position").val(),
			"startIndex"		       : parseInt($("#"+counter+"_startIndex").val()),
			"endIndex"			       : parseInt($("#"+counter+"_endIndex").val()),
			"dateFormat"               : $("#"+counter+"_dateFormat").val(),
			"pathListCount"			   : counter,
			"referenceDevice"		   : $('#' + counter + '_parentDevice').val(),
			"parentDevice.id"		   : $('#' + counter + '_deviceId').val(),
			"mandatoryFields"		   : mandatoryFields,
			"circle.id"		   : $('#' + counter + '_circle	').val()		
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
				
				var respObj = eval(responseObject);
				
				var pathListId = respObj['id'];
				
				// set pathlist id hidden for all pathlist block
				$('#'+counter+'_pathListId').val(pathListId);
				
				// update block title with path list name
				$('#title_'+counter).html(respObj['name'] +"-" + respObj['pathId']);
				
				$('#'+counter+'_block .gridBlock').show();
				dsiplayGridForPathlist(counter,[]);
 				
	 			$('#'+counter+'_updatebtn').show();
	 			$('#'+counter+'_addbtn').hide();
	 			$('#'+counter+'_pathId').val(respObj['pathId']);
	 			
	 			showSuccessMsg(responseMsg);
 				closeFancyBox();
				
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

function updatePathDetail(counter){
	resetWarningDisplay();
	clearAllMessages();
	showProgressBar(counter);
	var startIndex = $("#"+counter+"_startIndex").val();
	var endIndex = $("#"+counter+"_endIndex").val();
	if(startIndex === null || startIndex === '' || startIndex === 'undefined'){
		$("#"+counter+"_startIndex").val('-1')
	}
	if(endIndex === null || endIndex === '' || endIndex === 'undefined'){
		$("#"+counter+"_endIndex").val('-1')
	}
	var pathId = $('#'+counter+'_pathId').val();
	if(pathId == null || pathId == ''){
		pathId = '000';
	}
	var mandatoryFields = $("#"+counter+"_mandatoryFields").val();
		if (mandatoryFields === null || mandatoryFields === '' || mandatoryFields === 'undefined')
				mandatoryFields = "";
	
	$.ajax({
		url: '<%=ControllerConstants.UPDATE_PARSING_PATHLIST%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"id" 			   		   : $('#'+counter+'_pathListId').val(),
			"readFilePath" 			   : $('#'+counter+'_readFilePath').val(),
			"archivePath"              : $('#'+counter+'_archivePath').val(),
			"name"					   : $('#'+counter+'_name').val(),
			"service.id"			   : parseInt('${serviceId}'),
			"service.serverInstance.id": parseInt('${instanceId}'),
			"fileGrepDateEnabled"      : $("#"+counter+"_fileGrepDateEnabled").val(),
			"position"			       : $("#"+counter+"_position").val(),
			"startIndex"		       : parseInt($("#"+counter+"_startIndex").val()),
			"endIndex"			       : parseInt($("#"+counter+"_endIndex").val()),
			"dateFormat"               : $("#"+counter+"_dateFormat").val(),
			"pathListCount"			   : counter,
			"pathId"				   : pathId,
			"referenceDevice"		   : $('#' + counter + '_parentDevice').val(),
			"parentDevice.id"		   : $('#' + counter + '_deviceId').val(),
			"mandatoryFields"		   : mandatoryFields,
			"circle.id"		   : $('#' + counter + '_circle	').val()
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
				
				var respObj = eval(responseObject);
				
				var pathListId = respObj['id'];
				
				// set pathlist id hidden for all pathlist block
				$('#'+counter+'_pathListId').val(pathListId);
				
				// update block title with path list name
				$('#title_'+counter).html(respObj['name'] +"-" + respObj['pathId']);
 				
	 			$('#'+counter+'_updatebtn').show();
	 			$('#'+counter+'_addbtn').hide();
	 			
	 			showSuccessMsg(responseMsg);
 				closeFancyBox();
				
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

// save new parser in database
function addParserDetail(){
	resetWarningDisplay();
	clearAllMessages();
	showProgressBar('<%= BaseConstants.CREATE_MODE %>');
	var maxFileCountAlert=$('#create_maxFileCountAlert').val();
	if(maxFileCountAlert=="" || maxFileCountAlert==null)
	{
		maxFileCountAlert='-1';
	}
	var counter = $('#addBlockId').val();
	var parserObj = {};	
	parserObj['parsingPathList'] 					=	$('#'+counter+'_pathListId').val();
	parserObj['pluginId'] 							=	-1;
	parserObj['name'] 								=	$('#create_name').val();
	parserObj['parserType.id']      				=	$('#create_pluginType').val();
	parserObj['pluginType']      					=	$('#create_pluginType option:selected').text();
	parserObj['fileNamePattern'] 				=	$('#create_fileNamePattern').val();
	parserObj['readFilenamePrefix'] 				=	$('#create_readFilenamePrefix').val();
	parserObj['readFilenameSuffix'] 				=	$('#create_readFilenameSuffix').val();
	parserObj['readFilenameContains'] 				=   $('#create_readFilenameContains').val();
	parserObj['readFilenameExcludeTypes']			=	$('#create_readFilenameExcludeTypes').val();
	parserObj['compressInFileEnabled']				=   $('#create_compressInFileEnabled').val();
	parserObj['compressOutFileEnabled']			    =   $('#create_compressOutFileEnabled').val();
	parserObj['writeFilePath'] 					    =	$('#create_writeFilePath').val();
	parserObj['writeFilenamePrefix']				=   $('#create_writeFilenamePrefix').val();
	parserObj['writeFileSplit']						= 	$('#create_writeFileSplit').val();
	parserObj['maxFileCountAlert']					= 	maxFileCountAlert;
	parserObj['writeCdrHeaderFooterEnabled']		=   $('#create_writeCdrHeaderFooterEnabled').val();
	parserObj['writeCdrDefaultAttributes']		=   $('#create_writeCdrDefaultAttributes').val();
	parserObj['parserMapping.id']                   =   '0';    
	parserObj['serviceId']							= 	'${serviceId}';
	parserObj['mode']								=   '<%= BaseConstants.CREATE_MODE %>';
	
	
	$.ajax({
		url: '<%=ControllerConstants.CREATE_PARSER%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"pathListId"						:	parserObj['parsingPathList'],
			"parsingPathList.id"				:   parserObj['parsingPathList'],
			"name" 								:	parserObj['name'] ,
			"parserType.id"      				:	parserObj['parserType.id'] ,
			"writeFilePath" 					:	parserObj['writeFilePath'] ,
			"fileNamePattern" 				:	parserObj['fileNamePattern'],
			"readFilenamePrefix" 				:	parserObj['readFilenamePrefix'],
			"readFilenameSuffix" 				:	parserObj['readFilenameSuffix'] ,
			"readFilenameContains" 				:   parserObj['readFilenameContains'] 	,
			"readFilenameExcludeTypes"			:	parserObj['readFilenameExcludeTypes'],
			"compressInFileEnabled"				:   parserObj['compressInFileEnabled'],
			"compressOutFileEnabled"			:   parserObj['compressOutFileEnabled']	,
			"writeFilenamePrefix"				: 	parserObj['writeFilenamePrefix']	,
			"writeFileSplit"					:	parserObj['writeFileSplit']	,
			"maxFileCountAlert"                 :	parserObj['maxFileCountAlert']	,
			"writeCdrHeaderFooterEnabled"		:	parserObj['writeCdrHeaderFooterEnabled'],
			"writeCdrDefaultAttributes"		:	parserObj['writeCdrDefaultAttributes'],
			"parserMapping.id"                  :	parserObj['parserMapping.id'],   
			"serviceId"							: 	parserObj['serviceId']	,
			"pathListCount"						: 	parserObj['pathListCount']	,
			"mode"								:   parserObj['mode']
		}, 
		
		success: function(data){
			hideProgressBar('<%= BaseConstants.CREATE_MODE %>');

			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);

				var parser = eval(responseObject);
				var dbmaxFileCountAlert=parser.maxFileCountAlert;
				
				
 				var rowData = {};
 				rowData.id						= parser.id;
 				//rowData.checkbox				= '<input type="checkbox"></input>';
 				rowData.name					= parser.name;
 				rowData.parserTypeId			= parserObj['parserType.id'];
 				rowData.parserType				= parserObj['pluginType'];
  				rowData.parserTypeAlias         = parser.pluginTypeAlias;
  				rowData.fileNamePattern		= parser.fileNamePattern;
  				rowData.readFilenamePrefix		= parser.readFilenamePrefix;
 				rowData.readFilenameSuffix		= parser.readFilenameSuffix;
 				rowData.readFilenameContains	= parser.readFilenameContains;
 				rowData.readFilenameExcludeTypes= parser.readFilenameExcludeTypes;
 				rowData.compressInFileEnabled	= parser.compressInFileEnabled;
 				rowData.compressOutFileEnabled	= parser.compressOutFileEnabled;
 				rowData.writeFilePath			= parser.writeFilePath;
 				rowData.writeFilenamePrefix		= parser.writeFilenamePrefix;
 				rowData.writeFileSplit			= parser.writeFileSplit;
 				rowData.maxFileCountAlert		= dbmaxFileCountAlert;
 				rowData.writeCdrHeaderFooterEnabled = parser.writeCdrHeaderFooterEnabled;
 				rowData.writeCdrDefaultAttributes = parser.writeCdrDefaultAttributes;
 				rowData.parserMappingId         = parserObj['parserMapping.id'];
 				rowData.more					='<i class="fa fa-list" onclick="openViewMorePopup('+counter+','+rowData.id+')" style="cursor:pointer;"></i>';
 				rowData.edit					='<a onclick="openEditPopup('+counter+','+rowData.id+')" id="editParser_'+parser.name+'"> <i class="fa fa-pencil-square-o" aria-hidden="true" id="img_'+rowData.id+'" width="10px" data-toggle="tooltip" data-placement="bottom" style="cursor:pointer;"></i> </a>';
 				jQuery("#"+counter+"_grid").jqGrid('addRowData',rowData.id,rowData);
 				
 				addLinkHideShow(counter,false);
 				closeFancyBox();
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				hideProgressBar('<%= BaseConstants.CREATE_MODE %>');
				addErrorIconAndMsgForAjaxPopUp(responseObject); 
			}else{
				hideProgressBar('<%= BaseConstants.CREATE_MODE %>');
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar('<%= BaseConstants.CREATE_MODE %>');
			handleGenericError(xhr,st,err);
		}
	});
}

// open view more link popup
function  openViewMorePopup(counter,rowId){
	
	var dataFromTheRow = jQuery("#"+counter+"_grid").jqGrid ('getRowData', rowId);
	$('#view_fileNamePattern').val(dataFromTheRow.fileNamePattern);
	$('#view_readFilenamePrefix').val(dataFromTheRow.readFilenamePrefix);
	$('#view_readFilenameSuffix').val(dataFromTheRow.readFilenameSuffix);
	$('#view_readFilenameContains').val(dataFromTheRow.readFilenameContains);
	$('#view_readFilenameExcludeTypes').val(dataFromTheRow.readFilenameExcludeTypes);
	$('#view_compressInFileEnabled').val(dataFromTheRow.compressInFileEnabled+"");
	$('#view_compressOutFileEnabled').val(dataFromTheRow.compressOutFileEnabled+"");
	$('#view_writeFilenamePrefix').val(dataFromTheRow.writeFilenamePrefix);
	$('#view_writeFileSplit').val(dataFromTheRow.writeFileSplit+"");
	$('#view_maxFileCountAlert').val(dataFromTheRow.maxFileCountAlert);
	$('#view_writeCdrHeaderFooterEnabled').val(dataFromTheRow.writeCdrHeaderFooterEnabled+"");
	$('#view_writeCdrDefaultAttributes').val(dataFromTheRow.writeCdrDefaultAttributes+"");
	$("#viewRowId").val(dataFromTheRow.id);
	$("#viewBlockId").val(counter);
	clearAllMessagesPopUp();
	resetWarningDisplay();
	$('#viewMore').click();
		
}

//open delete popup
function  openDeletePopup(counter){
	clearAllMessagesPopUp();
	resetWarningDisplay();
	
	//var myGrid = $("#"+counter+"_grid");
	//var selectedRowId = myGrid.jqGrid ('getGridParam', 'selarrrow');
	var childCheckboxName = counter+"_pluginCheckbox"
	var selectedPluginCheckboxes = [];
    $.each($("input[name='"+childCheckboxName+"']:checked"), function(){            
    	selectedPluginCheckboxes.push($(this).val());
    });
    var selectedRowId = "";
    if(selectedPluginCheckboxes.length > 0) {
    	selectedRowId = selectedPluginCheckboxes[0];
    }
	
	$("#warning_msg").hide();
	$("#delete_plugin_note").hide();
	$("#delete_progress-bar-div").hide();
	$("#btndelete").hide();
	$("#delete_close_btn").show();
	$("#delete_buttons-div").show();
	
	if(selectedRowId != null && selectedRowId != ''){
		$("#delete_plugin_note").show();
		$("#btndelete").show();
		$("#delete_close_btn").show();		
		$('#delete_buttons-div #btndelete').attr('onClick','deleteParser("'+counter+'")');
		$('#deleteBlockId').val(counter);	
	}else{
		$("#warning_msg").show();	
		$("#delete_close_btn").show();		
	}
	$('#deletePopup').click();
}


// update parser detail for view more popup & update popup
function updateParserDetail(mode){
	
	var counter = 0;
	var rowId = 0;
	var popupType = '';
	
	if(mode=='<%= BaseConstants.VIEW_MODE %>'){
		showProgressBar('<%= BaseConstants.VIEW_MODE %>');	
		counter = $('#viewBlockId').val();
		rowId = $('#viewRowId').val();
		popupType = '<%= BaseConstants.VIEW_MODE %>';
	} else if (mode=='<%= BaseConstants.UPDATE_MODE %>'){
		showProgressBar('<%= BaseConstants.UPDATE_MODE %>');
		counter = $('#updateBlockId').val();
		rowId = $('#updateRowId').val();
		popupType = '<%= BaseConstants.UPDATE_MODE %>';
	} 
	
	var dataFromTheRow = jQuery("#"+counter+"_grid").jqGrid ('getRowData', rowId);
	var parserObj = {};
	
	parserObj['id'] 								=	rowId;
	parserObj['parsingPathList'] 					=	$('#'+counter+'_pathListId').val();
	var maxFileCountAlert=$('#'+popupType+'_maxFileCountAlert').val();
	if(mode=='<%= BaseConstants.VIEW_MODE %>'){
		parserObj['name'] 								=	dataFromTheRow['name'];
		parserObj['writeFilePath'] 					    =	dataFromTheRow['writeFilePath'];
		parserObj['parserType.id']      				=	dataFromTheRow['parserTypeId']; 
		parserObj['pluginType']      					=	dataFromTheRow['parserType'];
		parserObj['writeFilenamePrefix']				=   $('#'+popupType+'_writeFilenamePrefix').val();
	} else if (mode=='<%= BaseConstants.UPDATE_MODE %>'){
		parserObj['name'] 								=	$('#'+popupType+'_name').val();
		parserObj['writeFilePath'] 					    =	$('#'+popupType+'_writeFilePath').val();
		parserObj['writeFilenamePrefix']				=   $('#'+popupType+'_writeFilenamePrefix').val();		
		parserObj['parserType.id']      				=	$('#'+popupType+'_pluginType').val();
		parserObj['pluginType']      					=	$('#'+popupType+'_pluginType option:selected').text();
		
		if(maxFileCountAlert=="" || maxFileCountAlert==null)
		{
			maxFileCountAlert='-1';
		}
	}
	parserObj['fileNamePattern'] 				=	$('#'+popupType+'_fileNamePattern').val();
	parserObj['readFilenamePrefix'] 				=	$('#'+popupType+'_readFilenamePrefix').val();
	parserObj['readFilenameSuffix'] 				=	$('#'+popupType+'_readFilenameSuffix').val();
	parserObj['readFilenameContains'] 				=   $('#'+popupType+'_readFilenameContains').val();
	parserObj['readFilenameExcludeTypes']			=	$('#'+popupType+'_readFilenameExcludeTypes').val();
	parserObj['compressInFileEnabled']				=   $('#'+popupType+'_compressInFileEnabled').val();
	parserObj['compressOutFileEnabled']			    =   $('#'+popupType+'_compressOutFileEnabled').val();
	parserObj['writeFilenamePrefix']				=   $('#'+popupType+'_writeFilenamePrefix').val();
	parserObj['writeFileSplit']				        =   $('#'+popupType+'_writeFileSplit').val();
	parserObj['writeCdrHeaderFooterEnabled']	    =   $('#'+popupType+'_writeCdrHeaderFooterEnabled').val();
	parserObj['writeCdrDefaultAttributes']	    =   $('#'+popupType+'_writeCdrDefaultAttributes').val();
	
	parserObj['serviceId']							= 	'${serviceId}';
	parserObj['parserMapping.id']					= 	$('#'+popupType+'_parserMappingId').val();
	parserObj['mode']								=   popupType;
	
	$.ajax({
		url: '<%=ControllerConstants.UPDATE_PARSER%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"id"								:   parserObj['id'],
			"name" 								:	parserObj['name'] ,
			"parsingPathList.id"				:   parserObj['parsingPathList'],
			"parserType.id"      				:	parserObj['parserType.id'] ,
			"writeFilePath" 					:	parserObj['writeFilePath'] ,
			"fileNamePattern" 				:	parserObj['fileNamePattern'],
			"readFilenamePrefix" 				:	parserObj['readFilenamePrefix'],
			"readFilenameSuffix" 				:	parserObj['readFilenameSuffix'] ,
			"readFilenameContains" 				:   parserObj['readFilenameContains'] 	,
			"readFilenameExcludeTypes"			:	parserObj['readFilenameExcludeTypes'],
			"compressInFileEnabled"				:   parserObj['compressInFileEnabled'],
			"compressOutFileEnabled"			:   parserObj['compressOutFileEnabled']	,
			"writeFilenamePrefix"				: 	parserObj['writeFilenamePrefix'],
			"writeFileSplit"					: 	parserObj['writeFileSplit']	,
			"maxFileCountAlert"					: 	maxFileCountAlert	,
			"writeCdrHeaderFooterEnabled"		:   parserObj['writeCdrHeaderFooterEnabled']	,
			"writeCdrDefaultAttributes"		:   parserObj['writeCdrDefaultAttributes']	,
			"serviceId"							: 	parserObj['serviceId']	,
			"parserMapping.id"					: 	parserObj['parserMapping.id']	,
			"mode"								:   parserObj['mode']
		}, 
		
		success: function(data){
			
			if(mode=='<%= BaseConstants.VIEW_MODE %>'){
				hideProgressBar('<%= BaseConstants.VIEW_MODE %>');	
			} else if (mode=='<%= BaseConstants.UPDATE_MODE %>'){
				hideProgressBar('<%= BaseConstants.UPDATE_MODE %>');
			}

			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);

				var parser = eval(responseObject);
				var dbmaxFileCountAlert=parser.maxFileCountAlert;
				
 				var rowData = {};
 				rowData.id						= parser.id;
 				//rowData.checkbox				= '<input type="checkbox"></input>';
 				rowData.name					= parser.name;
 				rowData.parserTypeId			= parserObj['parserType.id'];
 				rowData.parserType				= parserObj['pluginType'];
 				rowData.parserTypeAlias			= parser.pluginTypeAlias;
 				rowData.fileNamePattern			= parser.fileNamePattern;
 				rowData.readFilenamePrefix		= parser.readFilenamePrefix;
 				rowData.readFilenameSuffix		= parser.readFilenameSuffix;
 				rowData.readFilenameContains	= parser.readFilenameContains;
 				rowData.readFilenameExcludeTypes= parser.readFilenameExcludeTypes;
 				rowData.compressInFileEnabled	= parser.compressInFileEnabled;
 				rowData.compressOutFileEnabled	= parser.compressOutFileEnabled;
 				rowData.writeFilePath			= parser.writeFilePath;
 				rowData.writeFilenamePrefix		= parser.writeFilenamePrefix;
 				rowData.writeFileSplit			= parser.writeFileSplit;
 				rowData.maxFileCountAlert		= dbmaxFileCountAlert;
 				rowData.writeCdrHeaderFooterEnabled = parser.writeCdrHeaderFooterEnabled;
 				rowData.writeCdrDefaultAttributes = parser.writeCdrDefaultAttributes;
 				rowData.parserMappingId         = parserObj['parserMapping.id'];
 				rowData.more					='<i class="fa fa-list" onclick="openViewMorePopup('+counter+','+rowData.id+')" style="cursor:pointer;"></i>';
 				rowData.edit					='<a id="editParser_'+parser.name+'" onclick="openEditPopup('+counter+','+rowData.id+')" ><i class="fa fa-pencil-square-o" aria-hidden="true" id="img_'+rowData.id+'" data-toggle="tooltip" data-placement="bottom" style="cursor:pointer;" ></i> </a>';
 				
 				$("#"+counter+"_grid").jqGrid('setRowData', rowData.id, rowData);

 				closeFancyBox();
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				if(mode=='<%= BaseConstants.VIEW_MODE %>'){
					hideProgressBar('<%= BaseConstants.VIEW_MODE %>');	
				} else if (mode=='<%= BaseConstants.UPDATE_MODE %>'){
					hideProgressBar('<%= BaseConstants.UPDATE_MODE %>');
				}
				addErrorIconAndMsgForAjaxPopUp(responseObject); 
			}else{
				if(mode=='<%= BaseConstants.VIEW_MODE %>'){
					hideProgressBar('<%= BaseConstants.VIEW_MODE %>');	
				} else if (mode=='<%= BaseConstants.UPDATE_MODE %>'){
					hideProgressBar('<%= BaseConstants.UPDATE_MODE %>');
				}
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar('<%= BaseConstants.CREATE_MODE %>');
			handleGenericError(xhr,st,err);
		}
	});
}

// open edit plugin popup
function  openEditPopup(counter,rowId){
	
	var dataFromTheRow = jQuery("#"+counter+"_grid").jqGrid ('getRowData', rowId);
	var pluginName = $(jQuery("#"+counter+"_grid").jqGrid ('getCell', rowId, 'name')).closest("a").html();
	$('#update_pluginId').val(dataFromTheRow.id);
	$('#update_name').val(pluginName);
	$('#update_pluginType').val(dataFromTheRow.parserTypeId);
	$('#update_fileNamePattern').val(dataFromTheRow.fileNamePattern);
	$('#update_readFilenamePrefix').val(dataFromTheRow.readFilenamePrefix);
	$('#update_readFilenameSuffix').val(dataFromTheRow.readFilenameSuffix);
	$('#update_readFilenameContains').val(dataFromTheRow.readFilenameContains);
	$('#update_readFilenameExcludeTypes').val(dataFromTheRow.readFilenameExcludeTypes);
	$('#update_compressInFileEnabled').val(dataFromTheRow.compressInFileEnabled+"");
	$('#update_compressOutFileEnabled').val(dataFromTheRow.compressOutFileEnabled+"");
	$('#update_writeFilenamePrefix').val(dataFromTheRow.writeFilenamePrefix);
	$('#update_writeFilePath').val(dataFromTheRow.writeFilePath);
	$('#update_writeFileSplit').val(dataFromTheRow.writeFileSplit+"");
	$('#update_maxFileCountAlert').val(dataFromTheRow.maxFileCountAlert);
	$('#update_parserMappingId').val(dataFromTheRow.parserMappingId);
	$("#updateRowId").val(dataFromTheRow.id);
	$("#updateBlockId").val(counter);
	$('#update_writeCdrHeaderFooterEnabled').val(dataFromTheRow.writeCdrHeaderFooterEnabled+"");
	$('#update_writeCdrDefaultAttributes').val(dataFromTheRow.writeCdrDefaultAttributes+"");
	
	clearAllMessagesPopUp();
	resetWarningDisplay();
	$('#updatePopup').click();
}

// open add plugin popup
function addPlugin(blockId){
	
	$('#addBlockId').val(blockId);
	clearAllMessagesPopUp();
	resetWarningDisplay();
	
	$('#create_buttons-div').show();
	$('#create_progress-bar-div').hide();
	$('#create_name').val('');
	$('#create_fileNamePattern').val('');
	$('#create_readFilenamePrefix').val('');
	$('#create_readFilenameSuffix').val('');
	$('#create_readFilenameContains').val('');
	$('#create_readFilenameExcludeTypes').val('');
	$('#create_writeFilenamePrefix').val('');
	$('#create_parserMappingId').val('0');
	$('#create_writeFilePath').val('');
	
	$('#addMore').click();	
	$('#add_existing_parser_tab').addClass('active');
	$('#add_existing_parser').addClass('tab-pane active');
	$('#add_new_parser_tab').removeClass('active');
	$('#add_new_parser').removeClass('tab-pane active');
	$('#add_new_parser').addClass('tab-pane');
	loadPluginGrid();
}

// delete all plugin selected in grid
function deleteParser(blockId){	

	showProgressBar('<%= BaseConstants.DELETE_MODE %>');
	//var myGrid = $("#"+blockId+"_grid");
    //var selectedRowId = myGrid.jqGrid ('getGridParam', 'selarrrow');
    //var rowString = selectedRowId.join(",");
    
    var childCheckboxName = blockId+"_pluginCheckbox";
	var selectedPluginCheckboxes = [];
    $.each($("input[name='"+childCheckboxName+"']:checked"), function(){            
    	selectedPluginCheckboxes.push($(this).val());
    });
    var rowString = selectedPluginCheckboxes.join(",");
    
    var rowIds = new Array();
    rowIds = rowString.split(",");
    
    $.ajax({
		url: '<%=ControllerConstants.DELETE_PARSER%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		{
			"parserIdList" :  rowString
		}, 
		
		success: function(data){

			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode == "200"){
				var selectAllCheckboxId = blockId+"_selectAllPlugin";
				$('input:checkbox[id="'+selectAllCheckboxId+'"]').prop('checked',false);
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
				
				for(var i=0;i<rowIds.length;i++){
					var rowid=rowIds[i];
					$("#"+blockId+"_grid").jqGrid('delRowData',rowid);
				}
				
				addLinkHideShow (blockId,true);
 				closeFancyBox();
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				hideProgressBar('<%= BaseConstants.DELETE_MODE %>')
				addErrorIconAndMsgForAjaxPopUp(responseObject); 
			}else{
				hideProgressBar('<%= BaseConstants.DELETE_MODE %>');
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar('<%= BaseConstants.DELETE_MODE %>');
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

function resetcreateParserPopUp(){
	
	$('#create_name').val('');
	$('#create_fileNamePattern').val('');
	$('#create_readFilenamePrefix').val('');
	$('#create_readFilenameSuffix').val('');
	$('#create_readFilenameContains').val('');
	$('#create_readFilenameExcludeTypes').val('');
	$('#create_writeFilenamePrefix').val('');
	$('#create_writeFilePath').val('');
	$('#create_maxFileCountAlert').val('');
	clearResponseMsgDiv();
}

$(document).ready(function() {
	
	//set plugin type for update & create plugin popup
	var pluginType = eval('${pluginType}');
	$.each(pluginType,function(index,plugin){
		$('#update_pluginType').append($('<option>', {
		    value: plugin.id,
		    text: plugin.type
		}));
		$('#create_pluginType').append($('<option>', {
		    value: plugin.id,
		    text: plugin.type
		}));
	});
});

function changeArchivePathField(pathListCounter){
	if('${fileGroupEnable}' == 'false'){
		$('#'+pathListCounter+'_archivePath').prop("readOnly",true);
		
	}else{
		$('#'+pathListCounter+'_archivePath').prop("readOnly",false);
	}
}

var deleteBlockId='';
function deletePathListPopup(counter){
	
	var pathListId=$('#'+counter+'_pathListId').val();
	deleteBlockId=counter;
	
	if(pathListId == null || pathListId == 'null' || pathListId==''){
		$("#flipbox_"+counter).remove();
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
		url: '<%=ControllerConstants.DELETE_PARSING_PATHLIST%>',
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

function pluginNameFormatter(cellvalue, options, rowObject){
	<sec:authorize access="hasAuthority('VIEW_PARSER')">
		return '<a class="link" onclick="redirectParserConfig('+"'"+rowObject["id"]+"','"+cellvalue+"','"+rowObject['parserTypeAlias']+"'"+');">' + cellvalue + '</a>' ;
	</sec:authorize>
	<sec:authorize access="!hasAuthority('VIEW_PARSER')">
		return cellvalue;
	</sec:authorize>
}

function pluginCheckboxFormatter(cellvalue, options, rowObject, uniqueGridId, rootCheckboxId, childCheckboxName) {
	var uniqueId = "checkbox";
	if(rowObject["name"] !== "") {
		uniqueId += "_" + rowObject["name"]; 
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

/*
 * this function will handle select all/ de select all event
 */
function pluginHeaderCheckbox(e, element, childCheckboxName) {
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

function loadPluginGrid(){
	reloadPluginGrid();
	getPluginsList();
}

function getPluginsList(){
	<%-- url: "<%= ControllerConstants.GET_SERVICE_PARSER_LIST %>", --%>
	$("#pluginList").jqGrid({		
		url: "<%= ControllerConstants.GET_PARSER_LIST %>",
		cache: false,
		async: true, 	  	 
    	datatype: "json",
    colNames:[
        "#",
		"<spring:message code='iplog.parsing.summary.parser.grid.column.id' ></spring:message>",
	  	"<spring:message code='iplog.parsing.summary.parser.grid.column.plugin.name' ></spring:message>",
		"<spring:message code='iplog.parsing.summary.parser.grid.column.plugin.type' ></spring:message>",
		"<spring:message code='iplog.parsing.summary.parser.grid.column.plugin.alias' ></spring:message>",
             ],
	colModel:[
		{name : '',	index : '', sortable:false, formatter : radBtnFormatterForAddParser, width : "15px", align:'center'},
    	{name:'id',index:'id'},
        {name:'name',index:'name',sortable:true},
    	{name:'type',index:'type',sortable:true},
    	{name:'alias',index:'alias',align:'center',sortable:true},
    ],
    rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
    rowList:[10,20,60,100],
    height: 'auto',
	sortname: 'id',
	sortorder: "desc",
    pager: "#pluginListPagingDiv",
    viewrecords: true,
    timeout : 120000,
    loadtext: "Loading...",
    caption: "<spring:message code='iplog.parsing.service.summary.plugin.grid.caption'></spring:message>",    
	contentType : "application/json; charset=utf-8",
		loadComplete: function(data) {
			
		gridDataArr = data.rows;
		
		$.each(gridDataArr, function (index, plugin) {
			pluginList[plugin.id]=plugin;
		});
		
		$(".ui-dialog-titlebar").show();
		if ($('#pluginList').getGridParam('records') === 0) {
            $('#pluginList tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code='serverManagement.grid.empty.records'></spring:message></div>");
        }else{
        	$("#pluginListPagingDiv").show();        	
        }
		$('.checkboxbg').bootstrapToggle();			
	},
	onPaging: function (pgButton) {
		clearResponseMsgDiv();		
	},
	loadError : function(xhr,st,err) {
		handleGenericError(xhr,st,err);
	},
	recordtext: "<spring:message code='serverManagement.grid.pager.total.records.text'></spring:message>",
    emptyrecords: "<spring:message code='serverManagement.grid.empty.records'></spring:message>",
	loadtext: "<spring:message code='serverManagement.grid.loading.text'></spring:message>",
	pgtext : "<spring:message code='serverManagement.grid.pager.text'></spring:message>"
	});	
}

function checkBoxFormatter(cellvalue, options, rowObject) {
	var parserName = rowObject["parserName"].replace(/ /g, "_");
	return "<input type='checkbox' name='"+parserName+"_client_chkbox"+"' id='"+parserName+"_client_chkbox"+"' onclick=\"addRowId(\'"+parserName+"_client_chkbox"+"\', \'"+rowObject["id"]+"\')\"; />";
}
function addRowId(elementId,parserId)
{
	var deviceElement = document.getElementById(elementId);
	if (deviceElement.checked) {
		if((ckparserSelected.indexOf(parserId)) == -1){
			ckparserSelected.push(parserId);
		}		
	}else{
		if(ckparserSelected.indexOf(parserId) !== -1){
			ckparserSelected.splice(ckparserSelected.indexOf(parserId), 1);
		}
	}
}

function radBtnFormatterForAddParser(cellvalue, options, rowObject) {
	var radId = rowObject["name"].split(" ").join("_")+'_'+rowObject["type"].split(" ").join("_")+ '_rad'; 
    return "<input type='radio' name='pluginGridRad'  id='"+ radId + "' value='"+rowObject['id']+"'/>";    
}

function cloneParser(){	
	resetWarningDisplay();
	clearAllMessages();
	showProgressBar('<%= BaseConstants.CREATE_MODE %>');
	var maxFileCountAlert=$('#create_maxFileCountAlert').val();
	if(maxFileCountAlert=="" || maxFileCountAlert==null)
	{
		maxFileCountAlert='-1';
	}
	var counter = $('#addBlockId').val();
	var parserObj = {};
	parserObj['parsingPathList'] 					=	$('#'+counter+'_pathListId').val();
	parserObj['pluginId'] 							=	$('input[name=pluginGridRad]:checked').val();		   
	parserObj['serviceId']							= 	'${serviceId}';
	parserObj['mode']								=   '<%= BaseConstants.CREATE_MODE %>';
		
	$.ajax({
		url: '<%=ControllerConstants.CLONE_PARSER%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {												 
			"parserId"						: 	parserObj['pluginId'],
			"serviceId"						: 	parserObj['serviceId'],			
			"pathListId"					:   parserObj['parsingPathList']
		},		
		success: function(data){
			hideProgressBar('<%= BaseConstants.CREATE_MODE %>');

			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
				var parser = eval(responseObject);
				var dbmaxFileCountAlert=parser.maxFileCountAlert;
				
 				var rowData = {};
 				rowData.id						= parser.id; 				
 				rowData.name					= parser.name;
 				rowData.parserTypeId			= parser['parserType.id'];
 				rowData.parserType				= parser.pluginType;
  				rowData.parserTypeAlias         = parser.pluginTypeAlias;
  				rowData.fileNamePattern		= parser.fileNamePattern;
  				rowData.readFilenamePrefix		= parser.readFilenamePrefix;
 				rowData.readFilenameSuffix		= parser.readFilenameSuffix;
 				rowData.readFilenameContains	= parser.readFilenameContains;
 				rowData.readFilenameExcludeTypes= parser.readFilenameExcludeTypes;
 				rowData.compressInFileEnabled	= parser.compressInFileEnabled;
 				rowData.compressOutFileEnabled	= parser.compressOutFileEnabled;
 				rowData.writeFilePath			= parser.writeFilePath;
 				rowData.writeFilenamePrefix		= parser.writeFilenamePrefix;
 				rowData.writeFileSplit			= parser.writeFileSplit;
 				rowData.maxFileCountAlert		= dbmaxFileCountAlert;
 				rowData.writeCdrHeaderFooterEnabled = parser.writeCdrHeaderFooterEnabled;
 				rowData.writeCdrDefaultAttributes = parser.writeCdrDefaultAttributes;
 				rowData.parserMappingId         = parser['parserMapping.id'];
 				rowData.more					='<i class="fa fa-list" onclick="openViewMorePopup('+counter+','+rowData.id+')" style="cursor:pointer;"></i>';
 				rowData.edit					='<a onclick="openEditPopup('+counter+','+rowData.id+')" id="editParser_'+parser.name+'"> <i class="fa fa-pencil-square-o" aria-hidden="true" id="img_'+rowData.id+'" width="10px" data-toggle="tooltip" data-placement="bottom" style="cursor:pointer;"></i> </a>';
 				jQuery("#"+counter+"_grid").jqGrid('addRowData',rowData.id,rowData);
 				addLinkHideShow(counter,false);
 				closeFancyBox();
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				hideProgressBar('<%= BaseConstants.CREATE_MODE %>');
				addErrorIconAndMsgForAjaxPopUp(responseObject); 
			}else{
				hideProgressBar('<%= BaseConstants.CREATE_MODE %>');
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar('<%= BaseConstants.CREATE_MODE %>');
			handleGenericError(xhr,st,err);
		}
	});	
}

function clearPluginGrid() {
	var $grid = $("#pluginList");
	var rowIds = $grid.jqGrid('getDataIDs');
	// iterate through the rows and delete each of them
	for (var i = 0, len = rowIds.length; i < len; i++) {
		var currRow = rowIds[i];
		$grid.jqGrid('delRowData', currRow);
	}
}

function reloadPluginGrid() {
	
	var $grid = $("#pluginList");
	jQuery('#pluginList').jqGrid('clearGridData');
	clearPluginGrid();

	$grid.jqGrid('setGridParam', {
		datatype : 'json',
		page : 1,
		sortname : 'id',
		sortorder : "desc"		
	}).trigger('reloadGrid');
}

function addLinkHideShow(counter,flag)
{
	if (flag == true)
	{
		$('[name='+counter+'_addlinkicon]').show();
		$('[name='+counter+'_addlink]').show();
	}
	else
	{
		$('[name='+counter+'_addlinkicon]').hide();
		$('[name='+counter+'_addlink]').hide();
	}
}
</script>
