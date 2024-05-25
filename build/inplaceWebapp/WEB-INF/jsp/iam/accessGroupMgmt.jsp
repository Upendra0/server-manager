<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Search Access Group -->
<div id="input-fields-horizontal"
	class="tab-content no-padding clearfix">
	<sec:authorize access="hasAuthority('VIEW_ACCESS_GROUP')">
		<div class="fullwidth borbot tab-pane active">
			<div class="fullwidth">
				<div class="title2">
					<spring:message code="searchAccessGroup.search.access.group.title" ></spring:message>
				</div>
			</div>

			<!-- 			<form class="form-horizontal" role="form"> -->
			<div class="col-md-6 inline-form">
				<div class="form-group">
					<div class="table-cell-label">
						<spring:message code="searchAccessGroup.access.group.name" ></spring:message>
					</div>
					<div class="input-group">
						<spring:message code="searchAccessGroup.access.group.name"
							var="tooltip" ></spring:message>
						<input type="text" class="form-control"
							id="search-access-group-name" title="${tooltip}"
							data-toggle="tooltip" data-placement="bottom"> <span
							class="input-group-addon add-on last"> <i
							class="glyphicon glyphicon-alert" data-toggle="tooltip"
							data-placement="bottom"></i></span>
					</div>
				</div>
				<div class="form-group">
					<div class="table-cell-label">
						<spring:message code="searchAccessGroup.created.by" ></spring:message>
					</div>
					<div class="input-group">
						<spring:message code="searchAccessGroup.created.by" var="tooltip" ></spring:message>
						<select id="searchCreatedBy" class="form-control"
							title="${tooltip}" data-toggle="tooltip" data-placement="bottom">
							<option value="ALL">All</option>
						</select> <span class="input-group-addon add-on last"> <i
							class="glyphicon glyphicon-alert" data-toggle="tooltip"
							data-placement="bottom"></i></span>
					</div>
				</div>
			</div>

			<div class="col-md-6 inline-form">
				<div class="form-group">
					<div class="table-cell-label">
						<spring:message code="searchAccessGroup.status" ></spring:message>
					</div>
					<div class="input-group">
						<spring:message code="searchAccessGroup.status" var="tooltip" ></spring:message>
						<select id="searchStatus" class="form-control" title="${tooltip}"
							data-toggle="tooltip" data-placement="bottom">
							<option value="ALL">
								<spring:message code="searchAccessGroup.status.all" ></spring:message>
							</option>
							<option value="Assigned">
								<spring:message code="searchAccessGroup.status.assigned" ></spring:message>
							</option>
							<option value="Unassigned">
								<spring:message code="searchAccessGroup.status.unassigned" ></spring:message>
							</option>
						</select> <span class="input-group-addon add-on last"> <i
							class="glyphicon glyphicon-alert" data-toggle="tooltip"
							data-placement="bottom"></i></span>
					</div>
				</div>
				<div class="form-group">
					<div class="table-cell-label">
						<spring:message code="searchAccessGroup.activeInactiveStatus" ></spring:message>
					</div>
					<div class="input-group">
						<spring:message code="searchAccessGroup.activeInactiveStatus"
							var="tooltip" ></spring:message>
						<select id="searchActiveInactive" class="form-control"
							title="${tooltip}" data-toggle="tooltip" data-placement="bottom">
							<option value="ALL">
								<spring:message
									code="searchAccessGroup.activeInactiveStatus.all" ></spring:message>
							</option>
							<option value="Active">
								<spring:message
									code="searchAccessGroup.activeInactiveStatus.active" ></spring:message>
							</option>
							<option value="Inactive">
								<spring:message
									code="searchAccessGroup.activeInactiveStatus.inactive" ></spring:message>
							</option>
						</select> <span class="input-group-addon add-on last"> <i
							class="glyphicon glyphicon-alert" data-toggle="tooltip"
							data-placement="bottom"></i></span>
					</div>
				</div>
			</div>


			<div class="form-group" style="display: none;">
				<label for="searchCreatedDate" class="col-lg-2 control-label">
					<spring:message code="searchAccessGroup.created.date" ></spring:message>
				</label>
				<div class="col-lg-2">
					<spring:message code="searchAccessGroup.created.date" var="tooltip" ></spring:message>
					<input type="text" class="datetimepicker-month form-control"
						id="start-date" title="${tooltip}">
				</div>
				<div class="col-lg-2">
					<spring:message code="searchAccessGroup.created.date" var="tooltip" ></spring:message>
					<input type="text" class="datetimepicker-month form-control"
						id="end-date" title="${tooltip}">
				</div>
			</div>

			<div class="clearfix"></div>
			<div class="pbottom15 mtop10">
				<button id="searchAccessGroup_btn" class="btn btn-grey btn-xs" onclick="searchCriteria();">
					<spring:message code="btn.label.search" ></spring:message>
				</button>
				<button id="resetAccessGroup_btn" class="btn btn-grey btn-xs" onclick="resetSearchCriteria();">
					<spring:message code="btn.label.reset" ></spring:message>
				</button>
			</div>
			<!-- 		</form> -->
		</div>
	</sec:authorize>
	<sec:authorize
		access="hasAuthority('ADD_ACCESS_GROUP') or hasAuthority('DELETE_ACCESS_GROUP')">
		<div class="tab-content no-padding clearfix">
			<div class="fullwidth">
				<div class="title2" style="float: right;">
					<span class="title2rightfield"> <span
						class="title2rightfield-icon1-text"> 
							<sec:authorize	access="hasAuthority('ADD_ACCESS_GROUP')">									
									<a href="<%= ControllerConstants.INIT_ADD_ACCESS_GROUP  %>"><i
										class="fa fa-plus-circle"></i></a>
	
									<a id="createAccessGroup" class="fancybox"
										href="<%= ControllerConstants.INIT_ADD_ACCESS_GROUP  %>"> <spring:message
											code="accessGroupMgmt.add.access.group.btn" ></spring:message>
									</a>
							</sec:authorize>
					</span> <span class="title2rightfield-icon1-text"> <sec:authorize
								access="hasAuthority('VIEW_ACCESS_GROUP') and hasAuthority('DELETE_ACCESS_GROUP')">
									<a id="delAccessGroup_lnk" href="javascript: deleteAccessGroup();"><i
										class="fa fa-trash"></i></a>
	
									<a class="fancybox" href="javascript: deleteAccessGroup();">
										<spring:message code="accessGroupMgmt.delete.access.group.btn" ></spring:message>
									</a>
									<a href="#divAccessGroupConfirmation" class="fancybox"
										style="display: none;" id="accessGrpConfirmation"></a>
									<a href="#divAccessGroupMessage" class="fancybox"
										style="display: none;" id="accessGrpMessage"></a>
							</sec:authorize>
					</span>
					</span>
				</div>
			</div>
		</div>
		<div id="divAccessGroupConfirmation"
			style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">Confirmation</h4>
				</div>
				<div class="modal-body padding10 inline-form">
					<p>
						<spring:message
							code="searchAccessGroup.grid.checkbox.confirmation.on.delete" ></spring:message>
					</p>
				</div>
				<div class="modal-footer padding10">
					<button id="delAG_btn" type="button" class="btn btn-grey btn-xs "
						onclick="permanentRemoveAccessGroup();">Ok</button>
					<button type="button" class="btn btn-grey btn-xs "
						data-dismiss="modal" onclick="closeFancyBox();">Cancel</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>

		<div id="divAccessGroupMessage" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">Confirmation</h4>
				</div>
				<div class="modal-body padding10 inline-form">
					<p>
						<spring:message
							code="searchAccessGroup.grid.checkbox.validation.on.delete" ></spring:message>
					</p>
				</div>
				<div class="modal-footer padding10">
					<button type="button" class="btn btn-grey btn-xs "
						data-dismiss="modal" onclick="closeFancyBox();">Close</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
	</sec:authorize>

	<sec:authorize access="hasAuthority('VIEW_ACCESS_GROUP')">
		<div class="box-body table-responsive no-padding box"
			style="padding-top: 5px;">
			<table class="table table-hover" id="accessGroupList"></table>
			<div id="pagingDiv"></div>
			<div style="display: none;">
				<form id="access_group_form" method="POST"
					action="<%= ControllerConstants.VIEW_ACCESS_GROUP %>">
					<input type="hidden" id="accessGroupId" name="accessGroupId">
				</form>
			</div>
			<div class="clearfix"></div>
		</div>
		<a href="#accessGrpMgmt-activeInActive" class='fancybox black'
			id="activeInActive-modal" style="display: none;"></a>
	</sec:authorize>
</div>

<sec:authorize access="hasAuthority('VIEW_ACCESS_GROUP')">

	<script type="text/javascript">
		var ckAccessGroupSelected = new Array();
		var assignedAccessGroupIds = new Array();
		var oldGrid = '';
		$(document).ready(function() {
			$("#accessGroupList").jqGrid({
	        	url: "<%= ControllerConstants.GET_ACCESS_GROUP_LIST %>",
	        	postData: {
	        		created_by_staff_id: function () {
						var searchCreatedBy = $("#searchCreatedBy").val();
						if(searchCreatedBy == 'ALL')
							searchCreatedBy = "0";
						
						return searchCreatedBy;
	   	    		},
	   	    		search_status: function () {
	        	        return $('#searchStatus').val();
	        	    },
	        		access_group_name: function () {
	        	        return $("#search-access-group-name").val();
	        	    },
	        	    start_date:function () {
	        	        return $("#start-date").val();
	        	    },
	        	    end_date:function () {
	        	        return $("#end-date").val();
	        	    },
	        	    search_active_inactive_status :function () {
	        	        return $('#searchActiveInactive').val();
	        	    },
	        	},
	            datatype: "json",
	            colNames:[
	                      "#",
	                      "<spring:message code='searchAccessGroup.grid.column.id' ></spring:message>",
	                      "<spring:message code='searchAccessGroup.grid.column.name' ></spring:message>",
	                      "<spring:message code='searchAccessGroup.grid.column.description' ></spring:message>",
	                      "<spring:message code='searchAccessGroup.grid.column.status' ></spring:message>",
	                      "<spring:message code='searchAccessGroup.grid.column.state' ></spring:message>",
	                      "<spring:message code='searchAccessGroup.grid.column.last.update' ></spring:message>",
	                      "<spring:message code='searchAccessGroup.grid.column.created.by' ></spring:message>",
	                      "Access Group Type"
	                     ],
				colModel:[
				    {name:'',index:'',formatter:checkBoxFormatter,sortable:false,width:'1%'},   
	            	{name:'id',index:'id',sortable:true,hidden:true},
	                {name:'name',index:'name',sortable:true,width:'17%',formatter:nameColumnFormatter},
	            	{name:'description',index:'description',sortable:false,width:'27%'},
	            	{name:'assignedunassignedstatus',index:'assignedunassignedstatus',sortable:false,width:'15%',formatter:assignedUnassignedColumnFormatter},
	            	{name:'accessgroupstate',index:'accessgroupstate',sortable:false,width:'15%', align:'center',formatter:stateColumnFormatter},
	            	{name:'lastupdatedate',index:'lastupdatedate',sortable:true,width:'15%'},
	            	{name:'username',index:'username',sortable:true, align:'center',width:'15%'},
	            	{name:'accessgrouptype',index:'accessgrouptype',hidden:false,align:'center',width:'15%'}
	            ],
	            rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
	            rowList:[10,20,60,100],
	            height: 'auto',
	         	//width: "900px",
				sortname: 'lastupdatedate',
	     		sortorder: "desc",
	            pager: "#pagingDiv",
	            viewrecords: true,
	            multiselect: false,
	            caption: "<spring:message code="searchAccessGroup.grid.caption"></spring:message>",
	            beforeRequest : function() {
					if(oldGrid != ''){
	            		$('#accessGroupList tbody').html(oldGrid);
	            	}
				},
	     		loadComplete: function() {
	     			
	     			if ($('#accessGroupList').getGridParam('records') === 0) {
	     				oldGrid = $('#accessGroupList tbody').html();
	                    $('#accessGroupList tbody').html("<div style='padding:6px;background:#D8D8D8;font-weight:bolder;text-align:center;'><spring:message code="searchAccessGroup.grid.empty.records"></spring:message></div>");
	                    $("#pagingDiv").hide();
	                }else{
	                	$("#pagingDiv").show();

		    			ckAccessGroupSelected = new Array();

						}
	    		    	
	
	    			resizeGrid();
	    			

	    			$('.checkboxbg2').bootstrapToggle();
	    			$('.checkboxbg2').parent().css('width','30%');
	    			
	    			$('.checkboxbg2').change(function(){
	    				activeInactiveAccessGroupToggleChanged(this);
	    			});
				},
				loadError : function(xhr,st,err) {
					handleGenericError(xhr,st,err);
				},
				onPaging: function (pgButton) {
					clearResponseMsgDiv();
					clearAccessGroupGrid();
				},
				beforeSelectRow: function (rowid, e){
			
				},
				recordtext: "<spring:message code="searchAccessGroup.grid.pager.total.records.text"></spring:message>",
		        emptyrecords: "<spring:message code="searchAccessGroup.grid.empty.records"></spring:message>",
				loadtext: "<spring:message code="searchAccessGroup.grid.loading.text"></spring:message>",
				pgtext : "<spring:message code="searchAccessGroup.grid.pager.text"></spring:message>"
			}).navGrid("#pagingDiv",{edit:false,add:false,del:false,search:false});
			
			resizeGrid();
			
			loadStaffForCreatedBySearchCriteria();
				
			$(window).on("resize", function () {
				resizeGrid();
			});
			
			$("#search-access-group-name").keypress(function(e) {
            	// Enter pressed?
            	if(e.which == 10 || e.which == 13) {
            		searchCriteria();
            	}
        	});
			
			$("#searchCreatedBy").keypress(function(e) {
            	// Enter pressed?
            	if(e.which == 10 || e.which == 13) {
            		searchCriteria();
            	}
        	});
			
			$("#searchStatus").keypress(function(e) {
            	// Enter pressed?
            	if(e.which == 10 || e.which == 13) {
            		searchCriteria();
            	}
        	});
			
			$("#searchActiveInactive").keypress(function(e) {
            	// Enter pressed?
            	if(e.which == 10 || e.which == 13) {
            		searchCriteria();
            	}
        	});
	});
		
	function activeInactiveAccessGroupToggleChanged(element){
		$("#ag-reason-for-change-errors").html('');
		$("#ag-reason-for-change").val('');
		
		$("#ag-reason-for-change-title").hide();
		$("#ag-reason-for-change-content").hide();
		$("#ag-reason-for-change-content").hide();
		$("#ag-important-note-title").hide();
		
		var toggleName = $(element).prop('name');

		var id_Status_State = toggleName.split("_");
		
		var id = id_Status_State[0];
		var status = id_Status_State[1];
		var state = id_Status_State[2];
		
		$("#ag-access-group-id").val(id);

		$("#ag-current-assign-status").html(status);
		if(status == 'Unassigned'){
			$("#ag-current-assign-status").css('color','9E9A9A');
			$("#ag-reason-for-change-title").show();
			$("#ag-reason-for-change-content").show();
		}else{
			$("#ag-current-assign-status").css('color','#FD7B25');
		}

		$("#ag-current-status").html(state);
		
		if(state == 'ACTIVE'){
			$("#ag-access-group-state").val('INACTIVE');
			
			
			$("#ag-active-inactive-btn").html("<spring:message code='accessGroupMgmt.change.status-inactive'></spring:message>");
			
			$("#ag-active-inactive-btn").css('background','#FD7B25');
			$("#ag-current-status").css('background','#FD7B25');
		}else{
			$("#ag-access-group-state").val('ACTIVE');
			
			$("#ag-active-inactive-btn").html("<spring:message code='accessGroupMgmt.change.status-active'></spring:message>");
			$("#ag-active-inactive-btn").css('background','#D7D7D7');
			$("#ag-current-status").css('background','#D7D7D7');
			
		}

		var toggleStatus = $(element).prop('checked');
		$("#activeInActive-modal").click();
	}
		
		function loadStaffForCreatedBySearchCriteria(){
			$.ajax({
				url: '<%= ControllerConstants.GET_STAFF_ID_AND_USERNAME %>',
			    cache: false,
				async: true,
				dataType: 'json',
				type: "POST",
				data: {
				},
				success: function(data){
					var response = eval(data);

			    	if(response.code == 200 || response.code == "200") {
						$.each(response.object, function(key,value) {
			    			$('#searchCreatedBy').append($('<option>', { 
			    		        value: value.id,
			    		        text : value.username
			    		    }));
						});
			    	}else{
				    	response.msg = decodeMessage(response.msg);
				    	response.msg = replaceAll("+"," ",response.msg);
			    	}
				},
			    error: function (xhr,st,err){
					handleGenericError(xhr,st,err);
				}
			});
		}
		
		function searchCriteria(){
			clearResponseMsgDiv();
			clearAccessGroupGrid();
			var $grid = $("#accessGroupList");
			$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
		}
		
		function resetSearchCriteria(){
			$("#search-access-group-name").val('');
		    $("#start-date").val('');
		    $("#end-date").val('');
		    $("#searchStatus").val("ALL");
		    $("#searchActiveInactive").val("ALL");
		    $("#searchCreatedBy").val("ALL");

		    searchCriteria();
		}
		
		function clearAccessGroupGrid(){
			var $grid = $("#accessGroupList");
			var rowIds = $grid.jqGrid('getDataIDs');
			// iterate through the rows and delete each of them
			for(var i=0,len=rowIds.length;i<len;i++){
				var currRow = rowIds[i];
				$grid.jqGrid('delRowData', currRow);
			}
		}
		
 		function viewAccessGroup(accessGroupId){
			$("#accessGroupId").val(accessGroupId);
			$("#access_group_form").attr("action","viewAccessGroup");
			$("#access_group_form").submit();
		}
		
		function stateColumnFormatter(cellvalue, options, rowObject) {
			
			<sec:authorize access="hasAuthority('EDIT_ACCESS_GROUP')" var="staffHasEditAccessGroupRight">
	 		</sec:authorize>
	 		
	 		if(${staffHasEditAccessGroupRight}){
				var toggleName = rowObject["id"] + "_" + rowObject["assignedunassignedstatus"] + "_" + cellvalue;
				var toggleId = rowObject["name"].split(" ").join("_")+ '_tgl';
		 		if(cellvalue == 'ACTIVE'){
	 				if(rowObject["assignedunassignedstatus"] == 'Assigned'){
	 					jQuery("#jqg_accessGroupList_"+rowObject["id"]).attr("disabled", "disabled");
	 					return '<input disabled class="checkboxbg2" id=' + toggleId + ' name="'+toggleName+'" checked data-on="Active" data-off="Inactive" type="checkbox" data-size="mini">';
	 				}else{
						return '<input class="checkboxbg2" id=' + toggleId + ' name="'+toggleName+'" checked data-on="Active" data-off="Inactive" type="checkbox" data-size="mini">';
	 				}
				}else if(cellvalue == 'INACTIVE'){
					if(rowObject["assignedunassignedstatus"] == 'Assigned'){
						jQuery("#jqg_accessGroupList_"+rowObject["id"]).attr("disabled", "disabled");
						return '<input disabled class="checkboxbg2" id=' + toggleId + ' name="'+toggleName+'" data-on="Active" data-off="Inactive" type="checkbox" data-size="mini">';
					}else{
						return '<input class="checkboxbg2" id=' + toggleId + ' name="'+toggleName+'" data-on="Active" data-off="Inactive" type="checkbox" data-size="mini">';
					}
				}
	 		}else{
	 			if(cellvalue == 'ACTIVE'){
	 				return '<label style="color:#FD7B25;font-weight: bold;">' + cellvalue + '</label>';
				}else if(cellvalue == 'INACTIVE'){
					return '<label style="color:#D7D7D7;font-weight: bold;">' + cellvalue + '</label>';
				}
	 		}
		}
		 
		function assignedUnassignedColumnFormatter(cellvalue, options, rowObject) {
			if(cellvalue == 'Assigned'){
				assignedAccessGroupIds.push(rowObject["id"]);
				return '<label style="color:#FD7B25;font-weight: bold;">' + cellvalue + '</label>';
			}else{
				return '<label style="color:#9E9A9A;font-weight: bold;">' + cellvalue + '</label>';	
			}
		}
		
		function nameColumnFormatter(cellvalue, options, rowObject) {
			var linkId = rowObject["name"].split(" ").join("_")+ '_lnk';
			return '<a id='+linkId+' class="link" onclick="viewAccessGroup(\'' + rowObject["id"] + '\')">' + cellvalue + '</a>';
		}
		
		function deleteAccessGroup(){
			if(ckAccessGroupSelected.length == 0){
				$("#accessGrpMessage").click();
				//alert("<spring:message code="searchAccessGroup.grid.checkbox.validation.on.delete"></spring:message>");
				return;
			}
			$("#accessGrpConfirmation").click();
		}
		
		function permanentRemoveAccessGroup(){
			$.ajax({
				url: '<%= ControllerConstants.DELETE_ACCESS_GROUP %>',
			    cache: false,
				async: true,
				dataType: 'json',
				type: "POST",
				data: {
					accessGroupIds: ckAccessGroupSelected.join()
				},
				success: function(data){
					var response = eval(data);
			    	response.msg = decodeMessage(response.msg);
			    	response.msg = replaceAll("+"," ",response.msg);
			    	
			    	if(response.code == 200 || response.code == "200") {
			    		resetSearchCriteria();
			    		clearResponseMsgDiv();
			    		clearResponseMsg();
			    		clearErrorMsg();
			    		showSuccessMsg(response.msg);
			    		ckAccessGroupSelected = new Array();
			    		closeFancyBox();
			    	}else{
			    		showErrorMsg(response.msg);
			    	}
				},
			    error: function (xhr,st,err){
			    	handleGenericError(xhr,st,err);
				}
			});
		}
	function checkBoxFormatter(cellvalue, options, rowObject){
			
			var accessgroupName = rowObject["name"];
			accessgroupName = accessgroupName.replace(/[^\w\s]/gi, '')
			var checkboxId = rowObject["name"].split(" ").join("_")+ '_cbx';
			if(rowObject["assignedunassignedstatus"] === 'Assigned'  || String(rowObject["mappingType"]) === '0'){
				return "<input type='checkbox' name='accessGroup"+accessgroupName+"_"+rowObject["id"]+"' disabled='disabled' id='"+checkboxId+"' onclick=\"addAccessGroupId(\'"+checkboxId+"\', \'"+rowObject["id"]+"\')\"; />";
			}else{
				return "<input type='checkbox' name='accessgroup_"+accessgroupName+"_"+rowObject["id"]+"' id='"+checkboxId+"' onclick=\"addAccessGroupId(\'"+checkboxId+"\', \'"+rowObject["id"]+"\')\"; />";
				
			}
		}
		
	function addAccessGroupId(elementId,accessgroupId){
			if($("#"+elementId).is(':checked')){
				
				if(ckAccessGroupSelected.indexOf(accessgroupId) === -1){
					
					ckAccessGroupSelected.push(accessgroupId);
					
				}		
				
			}else{
				if(ckAccessGroupSelected.indexOf(accessgroupId) !== -1){
					ckAccessGroupSelected.splice(ckAccessGroupSelected.indexOf(accessgroupId), 1);
				}
			}
		}
		function resizeGrid(){
			var $grid = $("#accessGroupList");
		    newWidth = $grid.closest(".ui-jqgrid").parent().width();
	 	    if(newWidth < 1000){
		    	newWidth = 1000;
		    }
		    //$grid.jqGrid("setGridWidth", newWidth, true);
		}
		
		
		
	
		
		
	</script>
</sec:authorize>
<!-- End of Search Access Group -->
