
<%@ taglib uri="http://www.springframework.org/security/tags"	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>

<script>
var files = null;
var parserMapping= null;
</script>

<div class="box-body padding0 mtop10">
	<div class="fullwidth" id="attribute_details_div">
		<form:form modelAttribute="<%=FormBeanConstants.REGEX_PARSER_MAPPING_FORM_BEAN %>" method="POST" id="regex_attribute_configuration_form_bean">
		
		<input type="hidden" id="id" name="id" value="${mappingId}"/>
		<input type="hidden" id="parserType_alias" name="parserType_alias" value="${plugInType}"/>

<!-- section-1 start here -->
			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message
							code="service.configuration.basic.detail.section.heading" ></spring:message>
					</h3>
					<div class="box-tools pull-right" style="top: -3px;">
						<button class="btn btn-box-tool" data-widget="collapse">
							<i class="fa fa-minus"></i>
						</button>
					</div>
					<!-- /.box-tools -->
				</div>
				<!-- /.box-header end here -->
				<!-- Form content start here  -->
				<div class="box-body">
					<div class="fullwidth inline-form">

						<div class="col-md-6 no-padding">
							<spring:message code="device.list.grid.device.name.label" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="device.name"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="device-name" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }"></form:input>
									<spring:bind path="device.name">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>

						<div class="col-md-6 no-padding">
							<spring:message
								code="device.list.grid.device.mapping.name.label"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="name"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="mapping-name" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }"></form:input>
									<spring:bind path="name">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code="regex.parser.source.date.format" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group ">
								<form:select path="dateFormat"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="dateFormat" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip }">
									<c:forEach items="${sourceDateFormatEnum}" var="sourceDateFormatEnum">
										<form:option value="${sourceDateFormatEnum.name}">${sourceDateFormatEnum.name}</form:option>
									</c:forEach>
								</form:select>
								<spring:bind path="dateFormat">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
								 <form:input path="srcDateFormat" cssClass="form-control table-cell input-sm" tabindex="4" id="srcDateFormat" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
									<spring:bind path="srcDateFormat">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code="regex.parser.source.charset.name" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group ">
									<form:select path="srcCharSetName"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="srcCharSetName"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach items="${sourceCharsetName}" var="sourceCharsetName">
											<form:option value="${sourceCharsetName}">${sourceCharsetName}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="srcCharSetName">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding" style="height: 45px">
							<spring:message code="regex.parser.upload.sample.file" var="tooltip" ></spring:message>
							<div class="form-group">
							<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
						
		                          	<div class="input-group">
		                          	<div class="fullwidth">
		                          		<input type="file" class="filestyle form-control" tabindex="14" data-buttonText="<spring:message code="serviceMgmt.browse.file"></spring:message>"
		             		 id="sampleFile" name="sampleFile" onChange="uploadSampleData(event);">
		             		 </div>
		             				<span class='input-group-addon' style='visibility: visible !important;'> 
		             				<a onclick="downloadSampleFile();"><img src="img/download-file.png" height="20" width="20"></a>
		             				</span>
		                          	</div>
		                         </div> 	
							</div>
						
		         	
		         	<div class="col-md-6 no-padding">
							<spring:message code="regex.parser.log.pattern.regex" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="logPatternRegex"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="logPatternRegex" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }"></form:input>
									<spring:bind path="logPatternRegex">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						
						<div class="col-md-6 no-padding">
							<spring:message code="regex.parser.log.pattern.regexid" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
								<form:textarea rows="3" id="avilablelogPatternRegexId" path="avilablelogPatternRegexId" class="form-control input-sm" title="${tooltip}" data-toggle="tooltip" data-placement="bottom"></form:textarea>
								<spring:bind path="avilablelogPatternRegexId">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code="regex.parser.configured.log.pattern.regexid" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group ">
								<form:textarea rows="3" id="logPatternRegexId" path="logPatternRegexId" class="form-control input-sm" title="${tooltip}" data-toggle="tooltip" data-placement="bottom"></form:textarea>
								<spring:bind path="logPatternRegexId">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
					
					
						<div class='col-md-12 no-padding'> 
							<div class='form-group'> 
								<div id='buttons-div' class='input-group '> 
									<sec:authorize access='hasAuthority(\'UPDATE_PARSER\')'>
										<button type='button' class='btn btn-grey btn-xs' id='submitbtn' onclick='addBasicDetail();'><spring:message code='btn.label.update'></spring:message></button>&nbsp;
									</sec:authorize> 
									<%-- <button type='button' class='btn btn-grey btn-xs' id='resetbtn' onclick=""><spring:message code='btn.label.reset'></spring:message></button>&nbsp; --%>  
								</div> 
								<div id="progress-bar-div" class="input-group" style="display: none;">
									<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
								</div>
							</div>  
						</div> 
				</div>
				<!-- Form content end here  -->
			</div>
			</div>
			<!-- section-1 end here -->
			
			</form:form>
			
		 	<jsp:include page="regExParserAttrPatternList.jsp"></jsp:include> 
			</div>
			</div>
			
<script type="text/javascript">

$(document).ready(function() {
	
	$("#device-name").val('${deviceName}');
	$("#mapping-name").val('${mappingName}');
	$("#device-name").prop('disabled',true);
	$("#mapping-name").prop('disabled',true);
	$("#logPatternRegexId").prop('disabled',true);
	$("#avilablelogPatternRegexId").prop('disabled',true);	
	
	var dateFormat=$("#dateFormat").find(":selected").val();
	if(dateFormat != 'Other'){
		$("#srcDateFormat").val('');
		$("#srcDateFormat").attr('readOnly',true);
	}
	
	$("#sampleFile").next().children().first().val('${uploadedFileName}');
	
	var flag = '${readOnlyFlag}';
	
	if(flag =='true'){
		disableAttributeDetail();
	}
	
	if($("#logPatternRegex").val() != ''){
		$("#patternListDiv").show();
	}
	
});

$(document).on("change","#dateFormat",function(event) {
	resetWarningDisplay();
	clearAllMessages();
	var dateFormat = $('#dateFormat').val();
    	if(dateFormat == 'Other'){
    		$("#srcDateFormat").val('');
		    $("#srcDateFormat").attr('readOnly',false);
	   }else{
		   $("#srcDateFormat").val('');
		   $("#srcDateFormat").attr('readOnly',true);
	   }
});

function uploadSampleData(event){
	resetWarningDisplay();
	clearAllMessages();
    files=event.target.files;
    $('#sampleFile').html(files[0].name);
}

function addBasicDetail(){
	resetWarningDisplay();
	clearAllMessages();
	$("#buttons-div").hide();
	$("#progress-bar-div").show();
	
	var file = $("#sampleFile").val();
	console.log("File object is : " + file);
	
	var uploadedFileName='${uploadedFileName}';
	if(uploadedFileName == null || uploadedFileName=='' || uploadedFileName == undefined || uploadedFileName== 'undefined'){
		
		if (file == '') {
			showErrorMsg("<spring:message code='regEx.parser.no.sample.file.select' ></spring:message>");
			$("#buttons-div").show();
			$("#progress-bar-div").hide();
			
			return;
		}else if((files[0].size/ 1024)>1024){
			
	    	showErrorMsg("<spring:message code='failed.file.size.message'></spring:message>");
	    	$("#buttons-div").show();
			$("#progress-bar-div").hide();
	    	return;
		}else{
		    	
			addBasicDetailDB();
		}
		    		
	}else{
		
		addBasicDetailDB();
			
	}
	
	
}

function addBasicDetailDB(){
	var file = $("#sampleFile").val();
	var deviceId='${deviceId}';
	var parserMappingId='${mappingId}';
	var parserType='${plugInType}';
	var oMyForm = new FormData();
	if(file !=''){
		oMyForm.append("sampleFile",files[0]);	
	}
	
	var dateFormat=$("#dateFormat").find(":selected").val();
	if(dateFormat == 'Other'){
		oMyForm.append("srcDateFormat", $("#srcDateFormat").val());
	}else{
		oMyForm.append("srcDateFormat", dateFormat);
	}
	
	oMyForm.append("dateFormat", $("#dateFormat").find(":selected").val());
    oMyForm.append("logPatternRegex", $("#logPatternRegex").val());
    oMyForm.append("logPatternRegexId", $("#logPatternRegexId").val());
    oMyForm.append("srcCharSetName", $("#srcCharSetName").find(":selected").val());
    oMyForm.append("avilablelogPatternRegexId", $("#avilablelogPatternRegexId").val());
    oMyForm.append("id",parserMappingId);  
    oMyForm.append("name",$("#mapping-name").val());
    oMyForm.append("device.id",deviceId);
    oMyForm.append("parserType.alias", parserType);
    
    
    $.ajax({
		url: '<%=ControllerConstants.ADD_REGEX_ATTR_BASIC_DETAIL%>',
		data : oMyForm ,
        type : "POST",
        enctype: 'multipart/form-data',
        processData: false, 
        contentType:false,
		 
		success: function(data){
			resetWarningDisplay();
			clearAllMessages();
			
			$("#buttons-div").show();
	    	$("#progress-bar-div").hide();

			var response = $.parseJSON(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
	 		
			if(responseCode == "200"){
				resetWarningDisplay();
    			clearAllMessages();
				showSuccessMsg(responseMsg);
				$("#avilablelogPatternRegexId").val(responseObject);
				$("#patternListDiv").show();
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				
				resetWarningDisplay();
				clearAllMessages();
				addErrorIconAndMsgForAjax(responseObject);
			}
			
		 },
	    error: function (xhr,st,err){
	    	resetWarningDisplay();
			clearAllMessages();
	    	hideProgressBar(counter);
			handleGenericError(xhr,st,err);
		}
	});


}

function downloadSampleFile(){
	
	resetWarningDisplay();
	clearAllMessages();
	$("#regex-parser-form").attr("action",'<%= ControllerConstants.DOWNLOAD_REGEX_SAMPLE_DATA_FILE%>');
	$("#regex-parser-form").attr("method","POST");
	$("#regex-parser-form #deviceName").val('${deviceName}');
	$("#regex-parser-form #mappingName").val('${mappingName}');
	$("#regex-parser-form #mappingId").val('${mappingId}');
	$("#regex-parser-form #deviceId").val('${deviceId}');
	$("#regex-parser-form").submit();
}
</script>			
