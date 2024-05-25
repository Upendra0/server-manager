<%@page import="com.elitecore.core.util.mbean.data.filereprocess.FileReprocessStatusEnum"%>
<%@page import="com.elitecore.sm.services.model.MigrationStatusEnum"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<form:form modelAttribute="rule_condition_form_bean" method="POST" action="#" id="rule-condition-reprocess-form">
<div id="modify-file-details-div" style="display:none;">
	<div class="tab-content padding0 clearfix" id="modify-file-details-block">
		<div class="title">
	   			<strong><spring:message code="file.reprocess.modify.file.label" ></spring:message></strong>
	   			<span class="title2rightfield"> 
				    <span class="title2rightfield-icon1-text">
				    		<sec:authorize access="hasAuthority('PREVIEW_FILE')">
	   						<button type="button" id="preview_original_file_btn" class="btn btn-grey btn-xs " onclick="viewOriginalFile();">
								<spring:message code="btn.label.file.original.preview" ></spring:message>
							</button>
							</sec:authorize>
	   				</span> 
	   			</span> 
	   		</div>
		<div class="fullwidth ">
			<div class="col-md-12 inline-form" style="padding-left: 0px !important; ">
			
				<input type="hidden" value="0" id="condition_action_application_order" />
				<div class="col-md-6 padding10" >
					<%@ include file="../policy/commonConditionExpression.jsp" %>  
				</div>
				 <div class="col-md-6 padding10" >
					<%@ include file="../policy/commonActionExpression.jsp" %>  
				</div> 
				<div class="clearfix"></div>
				<div class="form-group">
					<div class="table-cell-label">
					<input type="hidden" value="0" id="rule_condition_id" />
					<input type="hidden" value="0" id="rule_action_condition_appplication_order" />
					</div>
					<div class="table-cell  no-border no-shadow inline-form">
						<sec:authorize access="hasAuthority('MODIFY_FILES')">
						<button type="button" id="action_condition_add_btn" class="btn btn-grey btn-xs" onclick="createActionCondition();">
							<spring:message code="btn.label.add" ></spring:message>
						</button>
						<button type="button" id="action_condition_update_btn" style="display:none;" class="btn btn-grey btn-xs " onclick="updateActionConditionDetails();">
								<spring:message code="btn.label.update" ></spring:message>
						</button>
						</sec:authorize>
						<button type="button" id="action_condition_cancel_btn" style="display:none;" class="btn btn-grey btn-xs " onclick="cancelEdit();">
								<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</div>
				</div>	
			</div>
		</div>	
	</div>
	
	<div class="tab-content no-padding clearfix">
		
		<div class="fullwidth">
			<div class="title2">
				<spring:message code="error.reprocess.rule.list.header" ></spring:message>
			</div>
			<div class="box-body table-responsive no-padding box">
             	<table class="table table-hover" id="actionConditionList"></table>
               	<div id="actionConditionListPagingDiv"></div> 
	           	<div class="clearfix"></div> 
	           	<div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>  
            </div>
		</div>	
		
		<div class="clearfix"></div>
		<div class="fullwidth">
		
			
			<button type="button" class="btn btn-grey btn-xs " id = "reprocess_modified_files_btn" onclick="applyRules();" > <spring:message code="btn.label.apply.rule.save.file" ></spring:message></button>
            <button type="button" class="btn btn-grey btn-xs " id="cancel_modified_files_btn" data-dismiss="modal" onclick="goToSearchScreen();"><spring:message code="btn.label.cancel" ></spring:message></button>
		</div>
	</div>
	
	
<!-- Delete confirmation pop-up start here -->
<a href="#div_rule_action_condition_delete" class="fancybox" style="display: none;" id="div_rule_action_condition_delete_lnk">#</a>
<div id="div_rule_action_condition_delete" style=" width:100%; display: none;" >
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="label.confirmation.header"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
        	<input type="hidden" id="delete_action_condition_id" value="0" />
            <p> <spring:message code="file.reprocess.delete.rule.msg"></spring:message></p>
        </div>
        <div class="modal-footer padding10">
        	<sec:authorize access="hasAuthority('MODIFY_FILES')">
            	<button type="button" class="btn btn-grey btn-xs " id = "delete_action_condition_btn" onclick="deleteActionConditionDetails();" > <spring:message code="btn.label.yes" ></spring:message></button>
            </sec:authorize>
            <button type="button" class="btn btn-grey btn-xs " id="delete_action_condition_cancel_btn" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no" ></spring:message></button>
        </div>
    </div>
    <!-- /.modal-content --> 
</div>
<!-- Delete confirmation pop-up end here -->

</div>
</form:form>


<div id="preview-modified-file-details-div" style="display: none;">
	<div class="tab-content padding0 clearfix"	id="preview-modified-file-details-block">
		<div class="title">
			<strong><spring:message code="file.reprocess.modify.file.label"></spring:message></strong> 
		</div>
	</div>
	<div class="tab-content no-padding clearfix">
		<div class="fullwidth">
			<div class="title2 mtop10">
				<spring:message code="file.reprocess.file.list.label"></spring:message>
				 <span class="title2rightfield">
		          	<span class="title2rightfield-icon1-text">
		          		<button type="button" class="btn btn-grey btn-xs " id = "refresh_modify_files" onclick="realoadModifyDataGrid();" ><spring:message code="btn.label.refresh"></spring:message></button>
          			</span>	
		          	
		          </span>
			</div>
			<div class="box-body table-responsive no-padding box">
             	<table class="table table-hover" id="modifiedFileDetailsList"></table>
               	<div id="modifiedFileDetailsListPagingDiv"></div> 
	           	<div class="clearfix"></div> 
            </div>
		</div>	
		
		<div class="clearfix"></div>	
		<div class="fullwidth">
			<sec:authorize access="hasAuthority('DIRECT_REPROCESS')">
				<button type="button" class="btn btn-grey btn-xs " id = "reprocess_modified_file_btn" onclick="reprocessModifiedFiles();" ><spring:message code="btn.label.reprocess"></spring:message></button>
			</sec:authorize>
			<sec:authorize access="hasAuthority('MODIFY_FILES')">
				<button type="button" class="btn btn-grey btn-xs " id = "revert_modified_file_btn" onclick="revertModifiedFiles();" > <spring:message code="btn.label.rever.to.original"></spring:message></button>
			</sec:authorize>
            <button type="button" class="btn btn-grey btn-xs " id = "cancel_modified_preview_files_btn" data-dismiss="modal" onclick="goToSearchScreen();"><spring:message code="btn.label.cancel" ></spring:message></button>
		</div>
	</div>
	
</div>

<script type="text/javascript">
	function modifyProcessingFiles() {
		if (selectRowIdList.length == 0) {
			$("#reprocessMessage").click();
			return;
		}

		$("#processing_service_div").hide();
		$("#reprocessFileSearchArea").hide();

		$("#modify-file-details-div").show();
		$("#condition-info-block").hide();
		$("#action-info-block").hide();

		$("#action_syntax").val('');
		$("#condition_syntax").val('');

		$("#action_condition_add_btn").show();
		$("#action_condition_update_btn").hide();
		$("#action_condition_cancel_btn").hide();

		var ruleActionCondtionJson = {};
		var gridData = [];
		$('#actionConditionList').jqGrid('clearGridData');
		loadActionConditionDetailsGrid(gridData); // load grid details with rule action and condition.
	}

	function reloadActionConditionGrid() {
		var $grid = $("#actionConditionList");
		$grid.jqGrid('setGridParam', {
            datatype : "local",
		}).trigger('reloadGrid');
	}

	function loadActionConditionDetailsGrid(gridData) {
		
		 if(count && count > 0) {
			jQuery("#actionConditionList").jqGrid('setGridParam',
		        { 
		            datatype: 'local',
		            data:gridData
		        })
		    .trigger("reloadGrid");
		} else { 
			$("#actionConditionList")
			.jqGrid({
				url:'local',
				data : gridData,
				datatype : "local",
	            colNames:[
	                      "<spring:message code='file.reprocess.detail.list.grid.column.id' ></spring:message>",
	                      "<spring:message code='error.reprocess.rule.condition.header'></spring:message>",
	                      "<spring:message code='error.reprocess.rule.action.header'></spring:message>",
	                      "<spring:message code='error.reprocess.rule.order.header'></spring:message>", 
	                      "<spring:message code='error.reprocess.rule.edit.action.header'></spring:message>",
	                      "<spring:message code='error.reprocess.rule.delete.action.header'></spring:message>"
	                     ],
				colModel:[
					{name :'id',index : 'id',sortable : false, hidden:false},
					 {name:'conditionExpression',index:'conditionExpression',sortable:true},
	            	{name:'actionExpression',index:'actionExpression',sortable:true },
	            	{name:'applicationOrder',index:'applicationOrder',sortable:true }, 
	            	{name:'Edit',index:'Edit',formatter : editRuleConditionFormatter},
	            	{name:'Delete',index:'Delete',formatter :deleteRuleConditionFormatter}
	            ],
	            rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
	            rowList:[10,20,60,100],
	            height: 'auto',
	            mtype:'POST',
				sortname: 'applicationOrder',
	     		sortorder: "desc",
	            pager: "#actionConditionListPagingDiv",
	            contentType: "application/json; charset=utf-8",
	            viewrecords: true,
	            multiselect: false,
	            timeout : 120000,
	            loadonce: true,
	            loadtext:"<spring:message code='serviceManagement.grid.loading.text'></spring:message>",
	            caption: "<spring:message code='serviceManagement.grid.caption'></spring:message>",
	            beforeRequest:function(){
	                $(".ui-dialog-titlebar").hide();
	            }, 
	            beforeSend: function(xhr) {
	                xhr.setRequestHeader("Accept", "application/json");
	                xhr.setRequestHeader("Content-Type", "application/json");
	            },
	     		 loadComplete: function(data) {
				}, 
				onPaging: function (pgButton) {
				},
				gridComplete: function () { 
					 var rowCount = $('#actionConditionList').getGridParam('records');
		             $("#condition_action_application_order").val(rowCount);
					
				},
				loadError : function(xhr,st,err) {
					handleGenericError(xhr,st,err);
				},
				recordtext: "<spring:message code='serviceManagement.grid.pager.total.records.text'></spring:message>",
		        emptyrecords: "<spring:message code='serviceManagement.grid.empty.records'></spring:message>",
				loadtext: "<spring:message code='serviceManagement.grid.loading.text'></spring:message>",
				pgtext : "<spring:message code='serviceManagement.grid.pager.text'></spring:message>",
			}).navGrid("#actionConditionListPagingDiv",{edit:false,add:false,del:false,search:false});
			
			$(".ui-jqgrid-titlebar").hide();
			
			$("#actionConditionList").sortableRows();   
			$("#actionConditionList").disableSelection();
		    $("#actionConditionList").sortable({
		    	items: 'tr:not(:first)'
		    });
				
			 $("#actionConditionList").jqGrid("sortableRows", {
		        update: function () {
		        	changeGridApplicationOrder();
		            // the data of the column col_sortorder will contain
		            // now sequensial values 1,2,3...
		            // even the display values are still old
		            // reload grid to display updated data
		            var p = $("#actionConditionList").jqGrid("getGridParam");
		            // we reset sortname to "col_sortorder" only to reload
		            // with minimal visual changes for the user
		            p.sortname = "applicationOrder";
		            p.sortorder = "desc";
		           // setTimeout(function () { $("#actionConditionList").trigger("reloadGrid");}, 0);
		        }
		    }); 
			 jQuery("#actionConditionList").jqGrid('setGridParam',
 		        { 
 		            datatype: 'local',
 		            data:gridData
 		        })
 		    .trigger("reloadGrid"); 
		}
	}
	
	
	function changeGridApplicationOrder() {
		var rows = $("#actionConditionList")[0].rows, localRow, i;
		for (i = 0; i < rows.length; i++) {
			
			if ($(rows[i]).hasClass("jqgrow")) {
				// row is a row with data. row.id is the rowid
				localRow = $("#actionConditionList").jqGrid("getRowData", rows[i].id);
				localRow.applicationOrder = i;
				$('#actionConditionList').jqGrid('setRowData', localRow.id, localRow);
			}
		}
	}
	
	function upActionFormatter(cellvalue, options, rowObject) {
		return "<div align='center'><a href='#' align='center' onclick=\"updateApplicationOrder('UP')\"; > <i class='fa fa-arrow-up' aria-hidden='true'></i></a></div>";
	}

	function downActionFormatter(cellvalue, options, rowObject) {
		return "<div align='center'><a href='#' align='center' onclick=\"updateApplicationOrder('DOWN')\"; > <i class='fa fa-arrow-down' aria-hidden='true'></i></a></div>";
	}

	function editRuleConditionFormatter(cellvalue, options, rowObject) {
		return "<div align='center'><a href='#' align='center' onclick=\"editRuleActionConditionDetails('"
				+ rowObject["id"]
				+ "')\"; > <i class='fa fa-pencil-square-o' aria-hidden='true'></i></a></div>";
	}

	function deleteRuleConditionFormatter(cellvalue, options, rowObject) {
		return "<div align='center'><a href='#' align='center' onclick=\"displayDeleteConfirmPopup('"+ rowObject["id"]	+ "')\"; > <i class='fa fa-trash-o' aria-hidden='true'></i></a></div>";
	}

	function displayDeleteConfirmPopup(rowId) {
		var rows = $("#actionConditionList")[0].rows, localRow, i;
		for (i = 0; i < rows.length; i++) {
			console.log(rows);
			if ($(rows[i]).hasClass("jqgrow")) {
				// row is a row with data. row.id is the rowid
				localRow = $("#actionConditionList").jqGrid("getRowData", rows[i].id);
				var currentRowId =  localRow.id ;
				if(currentRowId > rowId){
					var updatedId = currentRowId -1;
					localRow.id = updatedId;
					localRow.applicationOrder = updatedId;
					$('#actionConditionList').jqGrid('setRowData', currentRowId, localRow);
				}else{
					localRow.applicationOrder = currentRowId 
					$('#actionConditionList').jqGrid('setRowData', currentRowId, localRow);
				}
			}
		}
		$('#actionConditionList').jqGrid('delRowData', rowId);
		reloadActionConditionGrid();
	}

	function editRuleActionConditionDetails(rowId) {

		$("#action_condition_add_btn").hide();
		$("#action_condition_update_btn").show();
		$("#action_condition_cancel_btn").show();

		var rowData = $("#actionConditionList").jqGrid('getRowData', rowId);
		$("#rule_condition_id").val(rowData.id);
		$("#rule_action_condition_appplication_order").val(rowData.applicationOrder);
		$("#action_syntax").val(rowData.actionExpression);
		$("#condition_syntax").val(rowData.conditionExpression);
	}
	
	function createActionCondition() {
		var rowCount = $('#actionConditionList').getGridParam('records');
		if(rowCount >= 5){
			showErrorMsg("<spring:message code='max.rule.validation.msg'></spring:message>");
			return ;
		}else{
			rowCount++;
			var ruleActionCondtionJson = {};
			ruleActionCondtionJson.id = rowCount;
			ruleActionCondtionJson.actionExpression = $("#action_syntax").val();
			ruleActionCondtionJson.conditionExpression = $("#condition_syntax").val();
			ruleActionCondtionJson.applicationOrder = rowCount; 
			ruleActionCondtionJson.Edit = '';
			ruleActionCondtionJson.Delete = '';
			
			 $.ajax({
				url : '<%=ControllerConstants.CRAETE_RULE_ACTION_CONDITION%>',
				cache: false,
				async: true, 
				dataType: 'json',
				headers: {
				    'Content-Type': 'application/json'
				},
				type: "POST",
				data: JSON.stringify(ruleActionCondtionJson),
				success: function(data){
					var responseCode = data.code;
					var responseMsg = data.msg;
					var responseObject = data.object;
					
					if(responseCode === "200") { 
						clearAllMessages();
						clearResponseMsgDiv();
						clearResponseMsg();
						clearErrorMsg();
	
						$("#action_syntax").val('');
						$("#condition_syntax").val('');
						var gridData = ruleActionCondtionJson;
						var rowId = gridData.id;
						 $("#actionConditionList").jqGrid('addRowData', rowId, gridData);
							reloadActionConditionGrid();
					}else if (responseObject != undefined	&& responseObject != 'undefined' && responseCode == "400") {
						$.each(responseObject, function(key, val) {
							if (key == 'actionExpression') {
								$("#action_syntax").next().children().first().attr("data-original-title", val);
							} else if (key == 'conditionExpression') {
								$("#condition_syntax").next().children().first().attr("data-original-title", val);
							} 
							addErrorClassWhenTitleExistPopUp($("#GENERIC_ERROR_MESSAGE", window.parent.document).val());
						});
					} 
				 },
			    error: function (xhr,st,err){
					handleGenericError(xhr,st,err);
				} 
			});
		}
	}
	
	
	function updateActionConditionDetails(){
		var ruleActionCondtionJson = {};
		var rowId = $("#rule_condition_id").val();
		var rowData = $("#actionConditionList").jqGrid('getRowData', rowId); 
		
		ruleActionCondtionJson.id = $("#rule_condition_id").val();
		ruleActionCondtionJson.applicationOrder =rowData.applicationOrder; 
		ruleActionCondtionJson.actionExpression = $("#action_syntax").val();
		ruleActionCondtionJson.conditionExpression = $("#condition_syntax").val();
		
		$.ajax({
			url: '<%=ControllerConstants.CRAETE_RULE_ACTION_CONDITION%>',
			cache: false,
			async: true, 
			dataType: 'json',
			headers: {
			    'Content-Type': 'application/json'
			},
			type: "POST",
			data: JSON.stringify(ruleActionCondtionJson),
			success: function(data){
				var responseCode = data.code;
				var responseMsg = data.msg;
				var responseObject = data.object;
			
				if(responseCode === "200") { 
					clearAllMessages();
					clearResponseMsgDiv();
					clearResponseMsg();
					clearErrorMsg();
					rowData.actionExpression = $("#action_syntax").val();
					rowData.conditionExpression = $("#condition_syntax").val();
					$('#actionConditionList').jqGrid('setRowData', rowData.id, rowData);
					reloadActionConditionGrid();
					
					$("#action_syntax").val('');
					$("#condition_syntax").val('');
					
					$("#action_condition_add_btn").show();
					$("#action_condition_update_btn").hide();
					$("#action_condition_cancel_btn").hide();
					
				}else if (responseObject != undefined	&& responseObject != 'undefined' && responseCode == "400") {
					$.each(responseObject, function(key, val) {
						if (key == 'actionExpression') {
							$("#action_syntax").next().children().first().attr("data-original-title", val);
						} else if (key == 'conditionExpression') {
							$("#condition_syntax").next().children().first().attr("data-original-title", val);
						} 
						addErrorClassWhenTitleExistPopUp($("#GENERIC_ERROR_MESSAGE", window.parent.document).val());
					});
			} 
		 },
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
		}); 
	}
	
	function displayAjaxResponse(data){
		var responseCode = data.code;
		var responseMsg = data.msg;
		var responseObject = data.object;
		
		if(responseCode === "200") {
			showSuccessMsg(responseMsg);
		}else if (responseObject != undefined	&& responseObject != 'undefined' && responseCode == "400") {
			$.each(responseObject, function(key, val) {
				if (key == 'actionExpression') {
					$("#action_syntax").next().children().first().attr("data-original-title", val);
				} else if (key == 'conditionExpression') {
					$("#condition_syntax").next().children().first().attr("data-original-title", val);
				} 
				addErrorClassWhenTitleExistPopUp($("#GENERIC_ERROR_MESSAGE", window.parent.document).val());
			});
		}else{
			showErrorMsg(responseMsg);
		}
	}
	
	function cancelEdit(){
		$("#action_condition_add_btn").show();
		$("#action_condition_update_btn").hide();
		$("#action_condition_cancel_btn").hide();
		$("#rule_condition_id").val(0);
		$("#action_syntax").val('');
		$("#condition_syntax").val('');
	}

	
	function  checkValidFileSize(fileSize,compress){
		fileSize = getFileSizeInKb(fileSize);
		if(compress == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
			if(parseFloat(fileSize) > maxCompressFileSize){
				return false;
			}else{
				return true;
			}	
		}else{
			if(parseFloat(fileSize) > maxCsvFileSize){
				return false;
			}else{
				return true;				
			}
		}
	} 
	
	
	function viewOriginalFile(){
		
		var rowData = $("#processingServiceGrid").jqGrid('getRowData', selectRowIdList[0]);
		var searchGridRowId = rowData["serviceId"]+"_"+rowData["pathId"];;
		var fileDetailsId = "fileDetails_"+ searchGridRowId;
		var errorPathId = "errorPath_"+ searchGridRowId;
		var detailElementJson = $("#"+fileDetailsId).html();
		
		detailElementJson = JSON.parse(detailElementJson);
		
		var errorPath = $("#"+errorPathId).html()
		var serverInstanceId = rowData["serverInstanceId"];
		var fileName ;
		var fileSize ;
		var compress ;
		var absolutePath;
		
		var isValidSize = false;
		$.each(detailElementJson, function(key,value){
			fileName = value.fileName;
			fileSize = value.fileSize;
			compress = value.fileType;
			absolutePath = value.absoluteFileName;
			
			isValidSize = checkValidFileSize(fileSize, compress);
			if(isValidSize){
			    return false;
			}
		});
		
		absolutePath = encodeURIComponent(absolutePath);
		
		if(isValidSize){
			setDynamicGridParamsAndFetchGridData(serverInstanceId, fileName, absolutePath,compress,true);
		}else{
			showErrorMsg("All files size are greater then configured system paramter size");
		}
	}
	
	
	function setDynamicGridParamsAndFetchGridData(serverInstanceId,fileName,fileErrorPath,fileType,isOverite){
		
		waitingDialog.show();
		$("#fileRecordDetailsGrid").GridUnload();
		
		$.ajax({
			url: 'getFileRecordDetails',
			cache: false,
			async: true, 
			dataType: 'json',
			type: "POST",
			data : {
				'serverInstanceId' : function() {
					return serverInstanceId;
				},
				'fileName' : function() {
					return fileName;
				},
				'filePath' : function() {
					return decodeURIComponent(fileErrorPath);
				},
				'fileType' : function() {
					return fileType;
				},
				'isOverite' : function() {
					return isOverite;
				},
				'isDisplayLimitedRecord' : function() {
					return true;
				}
			},
			success: function(data){
				var responseCode = data.code; 
				var responseMsg = data.msg;
				var responseObject = data.object; 
				
				waitingDialog.hide();
				
				if (responseCode === "200") {
					var colModel = responseObject["columnModel"];
					var colNames =  responseObject["columnName"] ; 
					var gridData = responseObject["gridData"];
					
					$("#processing_file_name_header").html(fileName);
					loadOriginalFileGridData(colModel, colNames, gridData);//loading dynamic grid from file detail records.
					
					$("#editable-grid-btn-div").hide();
					$("#view-origine-file-btn-div").show();
					
					$("#file_detail_grid_lnk").click();
					
				}else{
					showErrorMsg(responseMsg);
				}
		   },
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	}
	
	function loadOriginalFileGridData(colModel,colNames,gridData){
		jQuery("#fileRecordDetailsGrid").jqGrid({
			data : gridData,
			datatype : "local",
			colNames:colNames,
		   	colModel : colModel,
		   	loadonce: false,
		   	rowNum:10,
		   	rowList:[10,20,30],
		   	pager: jQuery('#fileRecordDetailsGridPagingDiv'),
		   	sortname: 'id',
		    viewrecords: true,
		    sortorder: "desc",
			caption: ""
		}).navGrid("#fileRecordDetailsGridPagingDiv",{edit:false,add:false,del:false}); 
	 }
	
	
	function goToSearchScreen(){
		
		$('input[name="procesing_search"]').each(function() {
			this.checked = false;
		});
		
		selectRowIdList = new Array();
		
		$("#modify-file-details-div").hide();
		
		$("#processing_service_div").show();
		$("#reprocessFileSearchArea").show();
		$("#preview-modified-file-details-div").hide();
		$("#continue_btn_go_to_back").hide();
		$("#continue_btn").show();
	}
	
	function applyRules(){
		var rowCount = $("#condition_action_application_order").val();
		if(selectRowIdList.length == 0){
			$("#processing_file_message").click();
			return;
		}else if(rowCount == 0){
			showErrorMsg("<spring:message code='create.rule.validation.msg'></spring:message>"); 
			return
		}else {
			finalProcessingBatchObj = {};
			createDynamicBatchDetailObj('BULK_EDIT');
			createBatchAndApplyRule(finalProcessingBatchObj,selectRowIdList,'Search');
		}
	}
	
	function createBatchAndApplyRule(batchObj,selectedRowId,actionFrom){
		
		waitingDialog.show();
		
		var rowData = jQuery("#actionConditionList").jqGrid('getRowData')
		 $.ajax({
			url: '<%=ControllerConstants.APPLY_RULE_TO_FILE%>',
			cache: false,
			async: true, 
			dataType: 'json',
			type: "POST",
			data: {
				"errorReprocessBatch":JSON.stringify(batchObj),
				"ruleList":JSON.stringify(rowData)
			},
			success: function(data){
				var response = eval(data);
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;
				
				waitingDialog.hide();
				
				if (responseCode === "200") {
					
					var object = JSON.parse(data.object);
					
					$("#processing_batch_success_lnk").click();
					
					$("#batch-view-status-response-div").hide();
					$("#batch-preview-response-div").show();
	 				$("#batchId").empty();
	 				$("#batchId").html(object.id);
	 				$("#batchIdForViewStatus").val(parseInt(object.id));
	 				$("#response_batch_id").val(object.id);
	 				
				}else{
					showErrorMsg(responseMsg);
				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	}
	
	function previewAppliedRuleFiles(){
		$("#preview-modified-file-details-div").show();
		$("#modify-file-details-div").hide();
		var batchId = $("#response_batch_id").val();
		loadModifiedFilesGridData(batchId);
		$("#continue_btn").click();
	}
	
	function realoadModifyDataGrid(){
		var batchId = $("#response_batch_id").val();
		 $("#modifiedFileDetailsList").trigger("reloadGrid"); 
	}
	
	function loadModifiedFilesGridData(batchId){
		
		$("#modifiedFileDetailsList").jqGrid(
				{
					url: "getAllBatchDetails",
		        	postData:{
		        		'batchId': function () {
		        			return batchId;
		   	    		}
		    		},
					datatype : "json",
					colNames : ["#",
					            "<spring:message code='processing.jqgrid.server.instance.header'></spring:message>",
					            "",
					            "<spring:message code='processing.jqgrid.service.instance.header'></spring:message>",
					            "<spring:message code='processing.jqgrid.server.file.name.header'></spring:message>",
					            "<spring:message code='processing.jqgrid.server.file.size.header'></spring:message>",
					            "",
					            "",
					            "",
					            "",
					            "<spring:message code='processing.jqgrid.server.file.preview.header'></spring:message>",
					            "<spring:message code='processing.jqgrid.server.file.download.header'></spring:message>",
					            ],
					colModel : [
									{name : 'id',	index : 'id', sortable:false,hidden:true },
					            	{name : 'serverInstance',index: 'serverInstance'},
					            	{name : 'serverInstanceId',index: 'serverInstanceId',hidden:true},
					            	{name : 'serviceInstance',index : 'serviceInstance'},
					            	{name : 'fileName',index : 'fileName'},
					            	{name : 'fileSize',index : 'fileSize',formatter:displayFileSize},
					            	{name : 'filePath',index : 'filePath',hidden:true},
					            	{name : 'compress',index : 'compress',hidden:true},
					            	{name : 'status',index : 'status',hidden:true},
					            	{name : 'absoluteFilePath',index : 'absoluteFilePath',hidden:true},
					            	{name : '',index : '',formatter:previewFileFormatter},
					            	{name : '',index : '',formatter:downloadAppliedRuleFile}
					            	
					            	
							   ],
					rowNum : 20,
					rowList : [],
					height : 'auto',
					mtype : 'POST',
					sortname : 'id',
					sortorder : "asc",
					pager : "#modifiedFileDetailsListPagingDiv",
					contentType : "application/json; charset=utf-8",
					viewrecords : false,
					multiselect : false,
					timeout : 120000,
					loadtext : "Loading...",
					caption : "Loading",
					beforeRequest : function() {
						$(".ui-dialog-titlebar").hide();
					},
					beforeSend : function(xhr) {
						xhr.setRequestHeader("Accept", "application/json");
						xhr.setRequestHeader("Content-Type", "application/json");
					},
					loadComplete : function(data) {
						$(".ui-dialog-titlebar").show();
						if ($('#modifiedFileDetailsList').getGridParam('records') === 0) {
							$('#modifiedFileDetailsList tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'>No Record found</div>");
							$("#modifiedFileDetailsListPagingDiv").hide();
						} else {
							$("#modifiedFileDetailsListPagingDiv").show();
							ckIntanceSelected = new Array();
						}
					},
					loadError : function(xhr, st, err) {
						handleGenericError(xhr, st, err);
					},
					beforeSelectRow : function(rowid, e) {
						return false;
					},
					onSelectAll : function(id, status) {
					},
					recordtext: "<spring:message code="serviceManagement.grid.pager.total.records.text"></spring:message>",
			        emptyrecords: "<spring:message code="serviceManagement.grid.empty.records"></spring:message>",
					loadtext: "<spring:message code="serviceManagement.grid.loading.text"></spring:message>",
					pgtext : "<spring:message code="serviceManagement.grid.pager.text"></spring:message>",
				}).navGrid("#modifiedFileDetailsListPagingDiv", {
			edit : false,
			add : false,
			del : false,
			search : false
		});
		$(".ui-jqgrid-titlebar").hide();
	}
	
	function downloadAppliedRuleFile(cellvalue, options, rowObject) {
		var serverInstanceId = rowObject["serverInstanceId"];
		var fileName = rowObject["fileName"];
		var filePath = rowObject["filePath"];
		var serverInstanceId = rowObject["serverInstanceId"];
		var fileType = rowObject["compress"];
		var absoluteFileName = rowObject["absoluteFilePath"];
		
		absoluteFileName = encodeURIComponent(absoluteFileName);
		
		
		if(fileType == 'true'){
			fileType = '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>';
		}else{
			fileType = '<%=BaseConstants.FILE_CSV_COMPRESS_EXT%>';
		}
		
		if(isAccessDownloadFile){
			return "<div align='center'><a href='javascript:;'   onclick=downloadErrorFile(\'"+serverInstanceId+"\',\'"+fileName+"\',\'"+absoluteFileName+"\',\'"+fileType+"\');><i class='fa fa-download' aria-hidden='true'></i> </a><div>";
		}else{
			return "<div align='center'><a href='javascript:;'><i class='fa fa-download' aria-hidden='true'></i> </a><div>";	
		}
	}
	
	function previewFileFormatter(cellvalue, options, rowObject) {

		if(isAccessPreviewFile){
			var fileSize = rowObject["fileSize"];
			var isCompress =  rowObject["compress"];
			var fileName = rowObject["fileName"];
			var filePath = rowObject["absoluteFilePath"];
			
			filePath = encodeURIComponent(filePath);
			
			var serverInstanceId = rowObject["serverInstanceId"];
			
			if(isCompress == 'true'){
				isCompress = '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>';
			}else{
				isCompress = '<%=BaseConstants.FILE_CSV_COMPRESS_EXT%>';
			}

			var status = rowObject["status"]; 
			var completed =  '<%=FileReprocessStatusEnum.COMPLETED%>';
			
			var reverted = '<%=FileReprocessStatusEnum.REVERTED%>';
			
			if(status == completed  || status == reverted){
				
				fileSize = getFileSizeInKb(fileSize);
				
				if(isCompress == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
					
					if(parseFloat(fileSize) < maxCompressFileSize){
						return "<div align='center'> <a href='#' onclick=previewModifiedFile(\'"+serverInstanceId+"\',\'"+fileName+"\',\'"+filePath+"\',\'"+isCompress+"\'); align='center' > <i class='fa fa-file' aria-hidden='true'></i></a></div>";
					}else{
						return "<div align='center'> <i class='fa fa-file' aria-hidden='true'></i></div>";
					}
				}else{
					if(parseFloat(fileSize) < maxCsvFileSize){
						return "<div align='center'> <a href='#' onclick=previewModifiedFile(\'"+serverInstanceId+"\',\'"+fileName+"\',\'"+filePath+"\',\'"+isCompress+"\'); align='center' > <i class='fa fa-file' aria-hidden='true'></i></a></div>";
					}else{
						return "<div align='center'><i class='fa fa-file' aria-hidden='true'></i></div>";		
					}
				}	
			}else{
				return "<div align='center'><i class='fa fa-file' aria-hidden='true'></i></div>";
			}
		}else{
			return "<div align='center'><i class='fa fa-file' aria-hidden='true'></i></div>";
		}
	}
	
	
	function reprocessModifiedFiles(){
		reprocessProcessingFiles('true');
	}
	
	function previewModifiedFile(serverInstanceId, fileName, fileErrorPath,compress){
		setDynamicGridParamsAndFetchGridData(serverInstanceId, fileName, fileErrorPath,compress,true);
	}
	
	
	function revertModifiedFiles(){
		waitingDialog.show();
		
		 $.ajax({
			 url : '<%=ControllerConstants.REVERT_MODIFIED_FILES%>',
				cache: false,
				async: true, 
				dataType: 'json',
				type: "POST",
				data: { 'batchId':$("#response_batch_id").val()},
				success: function(data){
					waitingDialog.hide();
					displayAjaxResponse(data);
				},
			    error: function (xhr,st,err){
					handleGenericError(xhr,st,err);
				}
			}); 
	}
	
</script>
