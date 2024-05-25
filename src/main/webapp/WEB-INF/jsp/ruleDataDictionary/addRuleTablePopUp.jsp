<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
	<div class="modal-content">
					<div class="modal-header padding10">
						<h4 class="modal-title">
							<span id="add_label" style="display: none;"><spring:message
									code="rule.data.mgmt.add.title" ></spring:message></span>
							<span id="update_label" style="display: none;"><spring:message
									code="rule.data.mgmt.update.title" ></spring:message></span>
						</h4>
					</div>
					<input type="hidden" id="ruleDataLookupFile" />
					<input type="hidden" id="ruleDataLookupFileWithData" value="false">
					<form:form modelAttribute="<%=FormBeanConstants.RULE_DATA_TABLE_FORM_BEAN%>" action="<%=ControllerConstants.CREATE_RULE_DATA_CONFIG%>"
						id="rule_data_table_from_bean">
					<div class="modal-body padding10 inline-form">
					<div id="AddPopUpMsg">
						<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
					</div>			
							<form:input path="id"  style="display: none;"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="id" data-toggle="tooltip" data-placement="bottom"></form:input>
						<div class="col-md-6 no-padding">
							<div class="form-group">
									<spring:message code="rule.data.mgmt.table.name"
										var="tooltip" ></spring:message>
									<spring:message code="rule.data.mgmt.table.name"
									var="label" ></spring:message>
									<div class="table-cell-label">${label}<span class="required">*</span></div>
									<div class="input-group">
										<form:input path="viewName"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="viewName" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }"></form:input>
								<spring:bind path="viewName">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
									</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<div class="form-group">
									<spring:message code="rule.data.mgmt.desc"
										var="tooltip" ></spring:message>
									<spring:message code="rule.data.mgmt.desc"
									var="label" ></spring:message>
									<div class="table-cell-label">${label}</div>
									<div class="input-group">
										<form:input path="description"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="description" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }"></form:input>
								<spring:bind path="description">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
									</div>
							</div>
						</div>
					<div id="complete_field_list_div">
					 <div class="col-md-12 no-padding">
						<div class="title2">
							<spring:message code="rule.data.mgmt.field.list" ></spring:message>
							<span class="title2rightfield"> <span
								class="title2rightfield-icon1-text"> 
								<a href="#" onclick="addField();"><i
								class="fa fa-plus-circle"></i></a>	
								<a href="#" id="createField" onclick="addField();">
								<spring:message code="rule.data.mgmt.field.list.add" ></spring:message>
								</a>
							</span>
							</span>
						</div>
					</div>
					<div style="width= calc(); display: none; text-align: center;" class="errorMessage" id="limit_reached">
						<span id = "error_text" class="title" >Cannot add more than 20 fields !!!.</span>
					</div> 
					<div class="col-md-12 inline-form no-padding" style="overflow:scroll;height:280px;width:100%;overflow:auto" id="field_list_grid_div">
						<table class="table table-hover" id="fieldList">
						</table>
					</div>
					</div>
					</div>
				</form:form>
				<div class="modal-footer padding10" id="createTableDiv">
				<div class="col-md-5 no-padding" id="divImport">
					<div class="box-body inline-form accordion-body collapse in" style="padding: 0px;">
							<div class="form-group">
								<div class="input-group" id="importDiv">
									<div class="fullwidth" id="control">
										<input type="file" class="filestyle form-control" tabindex="14" data-buttonText="<spring:message code="serviceMgmt.browse.file"></spring:message>" id="ruledataFile" name="ruledataFile" accept=".csv">
									</div>
									<span class='input-group-addon' style='visibility: visible !important;'>
										<button type="button" id="uploadLookupTable" class="btn btn-grey btn-xs " style="display: none" onclick="uploadRuleLookupDataFile();">
											Upload
										</button>
									</span>
								</div>
							</div>
					</div>
				</div>
		<div class="col-md-7 no-padding">			
					<sec:authorize access="hasAuthority('CREATE_RULE_DATA_CONFIG')">
							<button type="button" id="addNewLookupTable" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateRuleLookupTableWithField('<%=ControllerConstants.CREATE_RULE_DATA_CONFIG%>');">
								<spring:message code="btn.label.add" ></spring:message>
							</button>
					</sec:authorize>
					<sec:authorize access="hasAuthority('UPDATE_RULE_DATA_CONFIG')">
							<button type="button" id="updateLookupTable" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateRuleLookupTableWithField('<%=ControllerConstants.UPDATE_RULE_DATA_CONFIG%>');">
								<spring:message code="btn.label.update" ></spring:message>
							</button>
					</sec:authorize>
					<button id='close_btn' type="button" class="btn btn-grey btn-xs "
							data-dismiss="modal"
							onclick="closeFancyBox();reloadTableGridData();resetCounter();">
							<spring:message code="btn.label.close" ></spring:message>
					</button>
					</div>
				</div>	
				<div id="activate_full_license_processbar_add_table" class="modal-footer padding10 text-left" style="display: none;">
						<label>Processing Request </label> <img src="img/processing-bar.gif">
				</div>	
		</div>
<script>
var i = 0;
var count = 0;

function uploadRuleLookupDataFile(){
	var inputFile = document.getElementById('ruledataFile');
	var file = inputFile.files[0];
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	if (file == null) {
		showErrorMsgPopUp("File is Missing!!");
	}else {
		i = 0;
		count = 0;
		$("#fieldList").empty();
		var fileName = file.name;
		//fileName = fileName.substr(fileName.indexOf("rulelookup_")+1);
		//fileName = fileName.substr(0,fileName.indexOf(".csv"));
		//MED-8288
		fileName = fileName.replace("rulelookup_","");		
		fileName = fileName.replace(".csv","");		
		if(fileName != null && fileName != undefined && fileName != ""){
			var oMyForm = new FormData();
			oMyForm.append("file", file);
			$.ajax({
				dataType : 'json',
				url : "uploadRuleLookupDataFile",
				data : oMyForm,
				type : "POST",
				enctype : 'multipart/form-data',
				processData : false,
				contentType : false,
				success : function(response) {
					var responseObject = eval(response.object);
					var responseCode = response.code;
					if (responseCode == "200" && responseObject != undefined) {
						$("#viewName").val(fileName);
						$("#description").val("Table From Exported File");
						var length = responseObject.length;
						for(var i=0;i < length-1; i++){
							addField(JSON.parse(responseObject[i]));
						}
						var jsonObject = JSON.parse(responseObject[length-1]);
						if(jsonObject != null && jsonObject != undefined){
							$("#ruleDataLookupFile").val(jsonObject["fileName"]);
							var isFileWithData = jsonObject["isFileWithData"];
							$("#ruleDataLookupFileWithData").val(isFileWithData);
							if(isFileWithData == true){
								$("#fieldList input").attr('disabled',true);
								$("#fieldList select").attr('disabled',true);
								$("#fieldList a").attr('onclick','');
								//MED-8284
								//$("#fieldList i").remove();								
								$("#complete_field_list_div a").hide();
								showSuccessMsgPopUp("Table structure with data is uploaded. Click Add to import the table");
							}else{
								showSuccessMsgPopUp("Table structure is created. Click Add to import the table");
							}
						}
					} else if (responseObject == undefined && responseObject == 'undefined') {
						showErrorMsgPopUp("Please upload a valid RuleLookup Data File!");
					} else {
						showErrorMsgPopUp("Please upload a valid RuleLookup Data File!");
					}
				},
				error : function(response) {
					showErrorMsgPopUp("Please upload a valid RuleLookup Data File!");
				}
			});
		}else{
			showErrorMsgPopUp("Please upload a valid RuleLookup Data File!");			
		}
	}
}

function addField(responseObject){
	var field = "";
	var isUnique = "";
	if(responseObject != undefined && responseObject != null){
		field = responseObject["fieldName"];
		isUnique = responseObject["unique"];
	}
	if(count<20){
		$("#limit_reached").hide();
		i += 1;
		count += 1;
		var tableString = '<tr id = "field'+i+'">';
		
		    tableString +='<td>  <div class="form-group" >  <div class="input-group">';
			tableString +='<input id="lookUpFieldDetailData'+i+'fieldName'+i+'" name="ruleLookupData'+i+'.strParam'+i+'" cssClass="form-control table-cell input-sm" '
			tableString +='title="" tabindex="4" data-toggle="tooltip" ' ;
			tableString +='data-placement="bottom" value="'+field+'" data-original-title="" type="text" />'	;	
			tableString +='<span class="input-group-addon add-on last">';
			tableString +='<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title="" data-original-title="" ';
			tableString +='id="lookUpFieldDetailData'+i+'fieldName'+i+'_error"></i></span>'	;
			tableString +='</div>  </div> </td>';
			tableString += '<td><div class="form-group" >  <div class="input-group"><select id="unique'+i+'" cssClass="form-control table-cell input-sm" data-placement="bottom">';
			tableString += '<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">';										
			tableString += '<option value="${trueFalseEnum.name}">${trueFalseEnum.name}</option>';
			tableString += '</c:forEach>';
			tableString += '</td>';
			tableString += '<td><a href="#" onclick="deleteField('+i+')"><i class="fa fa-trash"></i></a>';
			tableString += '</td>';
			tableString += '</tr></div></div>';	
		$("#fieldList").append(tableString);
		if(isUnique === true){
			$("#unique"+i).find("option[value='true']").attr('selected','selected');
		}
	}else{
		$("#limit_reached").html("<span id = 'error_text' class='title' >Cannot add more than 20 fields !!!.</span>");
		$("#limit_reached").show();
	}
}

function deleteField(id){
	$("#limit_reached").hide();
	$("#field"+id).remove();
	count -= 1;
}

function createUpdateRuleLookupTableWithField(actionUrl){
	var index = i;
	createUpdateRuleLookupTable(actionUrl,index);
}

function resetCounter(){
	$("#complete_field_list_div a").show();
	$("#ruleDataLookupFile").val("");
	//$("#ruledataFile").val(null);
	//MED-8284
	$(":file").filestyle('clear');
	$("#ruleDataLookupFileWithData").val(false);
	files = null;
	i=0;
	count = 0;
	resetWarningDisplayPopUp();
}

function fillFieldListTable(a,rowData){
	i=a;
	var trueFalseEnum = "";
	var tableString = '<tr id = "field'+i+'">';
		tableString +='<td>  <div class="form-group" >  <div class="input-group">';
		tableString +='<input id="lookUpFieldDetailData'+i+'fieldName'+i+'" name="ruleLookupData'+i+'.strParam'+i+'" cssClass="form-control table-cell input-sm" '
		tableString +='title="" tabindex="4" data-toggle="tooltip" ' ;
		tableString +='data-placement="bottom" value="'+rowData.viewFieldName+'" data-original-title="" type="text"/>'	;	
		tableString +='<span class="input-group-addon add-on last">';
		tableString +='<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title="" data-original-title="" ';
		tableString +='id="lookUpFieldDetailData'+i+'fieldName'+i+'_error"></i></span>'	;
		tableString +='</div>  </div> </td>';
		tableString += '<td><div class="form-group" >  <div class="input-group"><select id="unique'+i+'" cssClass="form-control table-cell input-sm" data-placement="bottom">';
		tableString += '<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">';	
	 	trueFalseEnum = '${trueFalseEnum.name}';
		trueFalseEnum = trueFalseEnum.toString();
	 
	if( trueFalseEnum == rowData.unique){
		tableString += '<option value="${trueFalseEnum.name}" selected="selected" >${trueFalseEnum.name}</option>';
	}else{
		tableString += '<option value="${trueFalseEnum.name}" >${trueFalseEnum.name}</option>';
	}		
	tableString += '</c:forEach>';
	tableString += '</td>';
	tableString += '<td><a href="#" onclick="deleteField('+i+')"><i class="fa fa-trash"></i></a>';
	tableString += '</td>';
	tableString += '</tr></div></div>';
	
	$("#fieldList").append(tableString);
}
</script>
