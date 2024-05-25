<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<div id="input-fields-horizontal" class="tab-content no-padding clearfix">
		<!-- Search Staff -->
		<div class="fullwidth borbot tab-pane active">
				<div class="fullwidth">
					<div class="title2"><spring:message code="staffMgmt.staff.mgmt.title" ></spring:message></div>
				</div>
<!-- 				<form class="form-horizontal" role="form"> -->
				<!-- new  -->
					<div class="col-md-6 inline-form" style="padding-left: 0px !important;"> 
						<div class="form-group"> 
	    	            	<div class="table-cell-label"><spring:message code="staffMgmt.search.staff.first.name" ></spring:message></div>	
	        	            <div class="input-group">
	        	            	<spring:message code="staffMgmt.search.staff.first.name" var="tooltip"></spring:message>
	            	            <input type="text" id="search-staff-first-name" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
	                	        <span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" ></i></span>
	                   		</div>
	                	</div>
	                	
	                	<div class="form-group">
							<spring:message code="staffMgmt.search.emailId" var="tooltip"></spring:message> 
	    	            	<div class="table-cell-label">${tooltip}</div>	
	        	            <div class="input-group ">
	            	            <input type="text" id="search-emailId" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
	                	        <span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" ></i></span>
	                   		</div>
                		</div>
		                
		                <div class="form-group">
		                	<spring:message code="staffMgmt.search.staff.status" var="tooltip"></spring:message>
		                	<div class="table-cell-label">${tooltip}</div>	
		                    <div class="input-group ">
		                        <select id="search-staff-status" class="form-control" title="${tooltip}" data-toggle="tooltip" data-placement="bottom">
						        	<option value="ALL">
						        		<spring:message code="staffMgmt.search.staff.status.all" ></spring:message>
						        	</option>
						        	<option value="ACTIVE">
						        		<spring:message code="staffMgmt.search.staff.status.active" ></spring:message>
						        	</option>
						        	<option value="INACTIVE">
						        		<spring:message code="staffMgmt.search.staff.status.inactive" ></spring:message>
						        	</option>
					       	 	</select>
		                        <span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" ></i></span>
		                    </div>
		                </div>
		                
		                <div class="form-group">
		                	<spring:message code="staffMgmt.search.staff.access.group" var="tooltip"></spring:message> 
		                	<div class="table-cell-label">${tooltip}</div>	
		                    <div class="input-group ">
		                        <select id="search-staff-access-group" class="form-control" title="${tooltip}" data-toggle="tooltip" data-placement="bottom">
						        	<option value="ALL">
						        		<spring:message code="staffMgmt.search.lock.status.all" ></spring:message>
						        	</option>
					        	</select>
		                        <span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" ></i></span>
		                    </div>
		                </div>
	            	</div>
            
            	
            	<div class="col-md-6 inline-form"> 
            		<div class="form-group">
            		<spring:message code="staffMgmt.search.staff.last.name" var="tooltip"></spring:message>
						<div class="table-cell-label">${tooltip}</div>	
	            		<div class="input-group">
	           	            <input type="text" id="search-staff-last-name" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
	               	        <span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" ></i></span>
	                   	</div>
	                 </div>
	                 
	                 <div class="form-group">
		                	<spring:message code="staffMgmt.search.staff.code" var="tooltip"></spring:message>
		                	<div class="table-cell-label">${tooltip}</div>	
		                    <div class="input-group ">
		                        <input type="text"  id="search-staff-code" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"   />
		                        <span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom"></i></span>
		                    </div>
		             </div>
					
	                <div class="form-group">
						<spring:message code="staffMgmt.grid.column.lock.status" var="tooltip"></spring:message> 
    	            	<div class="table-cell-label">${tooltip}</div>	
        	            <div class="input-group ">
            	            <select id="search-lock-status" class="form-control" title="${tooltip}" data-toggle="tooltip" data-placement="bottom">
					        	<option value="ALL">
					        		<spring:message code="staffMgmt.search.lock.status.all" ></spring:message>
					        	</option>
					        	<option value="LOCK">
					        		<spring:message code="staffMgmt.search.lock.status.lock" ></spring:message>
					        	</option>
					        	<option value="UNLOCK">
					        		<spring:message code="staffMgmt.search.lock.status.unlock" ></spring:message>
					        	</option>
				        	</select>
                	        <span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" ></i></span>
                   		</div>
                	</div>
            	</div>
				<!-- end new -->
				
				<div class="clearfix"></div>
				<div class="pbottom15 mtop10">
					<button id="searchStaff_btn" class="btn btn-grey btn-xs" onclick="searchStaffCriteria();"><spring:message code="btn.label.search" ></spring:message></button>
					<button id="resetStaf_btn" class="btn btn-grey btn-xs" onclick="resetSearchStaffCriteria();"><spring:message code="btn.label.reset" ></spring:message></button>
				</div>
<!-- 			</form> -->
		</div>
		

		<div class="tab-content no-padding clearfix">
			<div class="fullwidth">
		       	<div class="title2"><spring:message code="staffMgmt.grid.caption"></spring:message>
		          <span class="title2rightfield">
		          	<span class="title2rightfield-icon1-text">
		          	<sec:authorize access="hasAnyAuthority('ADD_STAFF')">
		          		<c:if test="${Boolean.parseBoolean(MapCache.getConfigValueAsObject(SystemParametersConstant.SSO_ENABLE) eq 'false' )}">	
				          	<a id="addStaff_img" href="<%= ControllerConstants.INIT_ADD_STAFF  %>" ><i class="fa fa-plus-circle"></i></a>
				          	<a id="addStaff_lnk" href="<%= ControllerConstants.INIT_ADD_STAFF  %>">
				          		<spring:message code="btn.label.add" ></spring:message>
				          	</a>
			          	</c:if>
			        </sec:authorize>
			        </span>
			        <span class="title2rightfield-icon1-text">
			        <sec:authorize access="hasAuthority('DELETE_STAFF')">
			        	<c:if test="${Boolean.parseBoolean(MapCache.getConfigValueAsObject(SystemParametersConstant.SSO_ENABLE) eq 'false' )}">	
				          	<a id="delStaff_img" href="javascript: deleteStaff();" ><i class="fa fa-trash"></i></a>
				          	<a id="delStaff_lnk" href="#" onclick="deleteStaff();">
				          		<spring:message code="btn.label.delete" ></spring:message>
				          	</a>
				        </c:if> 	
				          	<a href="#divconfirmation" class="fancybox" style="display: none;" id="confirmation">
				          		<spring:message code="btn.label.delete" ></spring:message>
				          	</a>
							<a href="#divmessage" class="fancybox" style="display: none;" id="message">
				          		<spring:message code="btn.label.delete" ></spring:message>
				          	</a>
			        </sec:authorize> 
			        </span>
		          </span>
		      	</div>
		    </div>
		    <!-- Morris chart - Sales -->
	        <div class="box-body table-responsive no-padding box" style="padding-top: 5px;">
	        		<table class="table table-hover" id="staffList"></table>
	            	<div id="staffListPagingDiv"></div> 
	           		<div class="clearfix"></div>   
		    </div>
	        <div class="chart tab-pane" id="sales-chart" style="position: relative; height: 250px;"><spring:message code="select.chart.label.content"></spring:message></div>
        </div>

	</div>
	
	<div style="display: none;">
		<form id="staff_form" method="POST" action="<%= ControllerConstants.VIEW_STAFF_DETAILS %>">
			<input type="hidden" id="staffId" name="staffId">
		</form>
	</div>
	
	<div id="divconfirmation" style=" width:100%; display: none;" >
        <div class="modal-content">
            <div class="modal-header padding10">
                <h4 class="modal-title"><spring:message code="label.confirmation.header"></spring:message></h4>
            </div>
            <div class="modal-body padding10 inline-form">
                <p><spring:message code="staff.grid.checkbox.confirmation.on.delete"></spring:message></p>
            </div>
            <div class="modal-footer padding10">
                <button id="delstaff_btn" type="button" class="btn btn-grey btn-xs " onclick="permanentRemoveStaff();"><spring:message code="btn.label.yes" ></spring:message></button>
                <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no" ></spring:message></button>
            </div>
        </div>
        <!-- /.modal-content --> 
    </div>
    
    <div id="divmessage" style=" width:100%; display: none;" >
        <div class="modal-content">
            <div class="modal-header padding10">
                <h4 class="modal-title"><spring:message code="label.confirmation.header"></spring:message></h4>
            </div>
            <div class="modal-body padding10 inline-form">
                <p> <spring:message code="staff.grid.checkbox.validation.on.delete"></spring:message></p>
            </div>
            <div class="modal-footer padding10">
                <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close" ></spring:message></button>
            </div>
        </div>
        <!-- /.modal-content --> 
    </div>
	
	<a href="#staffMgmt-activeInActive" class='fancybox black' id="staffMgmtActiveInActive-modal" style="display: none;" ></a>
	<a href="#staffMgmt-lockUnlock" class="fancybox black" id="staffMgmtLockUnlock-modal" style="display: none;" ></a>
	
	<script type="text/javascript">
		var ckStaffSelected = new Array();
		$(document).ready(function() {
			<!-- Search Staff -->
			<sec:authorize access="hasAuthority('VIEW_STAFF')">

				$("#staffList").jqGrid({
		        	url: "<%= ControllerConstants.GET_STAFF_LIST %>",
		        	postData: {
		        		firstName: function () {
		        	        return $("#search-staff-first-name").val();
		   	    		},
		   	    		lastName: function () {
		        	        return $("#search-staff-last-name").val();
		   	    		},
		   	    		email_id: function(){
		   	    			return $("#search-emailId").val();
		   	    		},
		   	    		staff_code: function () {
		        	        return $("#search-staff-code").val();
		   	    		},
		   	    		account_status: function () {
		        	        return $("#search-staff-status").val();
		   	    		},
		   	    		access_group_id: function () {
		        	        return $("#search-staff-access-group").val();
		   	    		},
		   	    		lock_status: function () {
		        	        return $("#search-lock-status").val();
		   	    		}
		        	},
		            datatype: "json",
		            colNames:["#",
		                      "<spring:message code='staffMgmt.grid.column.id' ></spring:message>",
		                      "<spring:message code='staffMgmt.grid.column.username' ></spring:message>",
		                      "<spring:message code='staffMgmt.grid.column.staff.code' ></spring:message>",
		                      "<spring:message code='staffMgmt.grid.column.name' ></spring:message>",
		                      "<spring:message code='staffMgmt.grid.column.emailId' ></spring:message>",
		                      "<spring:message code='staffMgmt.grid.column.access.group.name' ></spring:message>",
		                      "<spring:message code='staffMgmt.grid.column.lock.status' ></spring:message>",
		                      "<spring:message code='staffMgmt.grid.column.status' ></spring:message>",
		                      "Staff Type"
		                     ],
					colModel:[
						{name : '',	index : '', sortable:false, formatter : checkBoxFormatterForViewStatus, width : "15px", align:'center'},
		            	{name:'id',index:'id',sortable:true,hidden: true},
		            	{name:'username',index:'username',hidden:true},
		                {name:'staffCode',index:'staffCode',sortable:true},
		            	{name:'name',index:'name',sortable:true, formatter: nameFormatter},
		            	{name:'emailId',index:'emailId',sortable:false},
		            	{name:'accessGroupName',index:'accessGroupName',sortable:false},
		            	{name:'isAccountLock',index:'isAccountLock',sortable:false, align:'center', formatter: lockColumnFormatter},
		            	{name:'accountState',index:'accountState',sortable:false, align:'center', formatter: accountStateColumnFormatter},
		            	{name:'stafftype',index:'stafftype',sortable:false, align:'center'}
		            ],
		            rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		            rowList:[10,20,60,100],
		            height: 'auto',
		            width: "1000px",
					sortname: 'id',
		     		sortorder: "desc",
		            pager: "#staffListPagingDiv",
		            viewrecords: true,
		            //multiselect: true,
		            caption: "<spring:message code="staffMgmt.grid.caption"></spring:message>",
		     		loadComplete: function(data) {
		     			if ($('#staffList').getGridParam('records') === 0) {
		                    $('#staffList tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code="staffMgmt.grid.empty.records"></spring:message></div>");
		                    $("#staffListPagingDiv").hide();
		                }else{
		                	$("#staffListPagingDiv").show();
			    			//$("tr.jqgrow:odd").css("background", "#E0E0E0");
			    			resizeStaffGrid();
			    		
		                }
		     			
		     			$("#cb_staffList").hide();
		     			
		     			$('.checkboxbg').bootstrapToggle();
		    			//$('.toggle-group').parent().css('width','30%');
		    			
		    			$('.checkboxbg').change(function(){
		    				staffActiveInactiveToggleChanged(this);
		    			});
		    			
					},
					onPaging: function (pgButton) {
						clearResponseMsgDiv();
						clearStaffGrid();
					},
					loadError : function(xhr,st,err) {
						handleGenericError(xhr,st,err);
					},
					recordtext: "<spring:message code="staffMgmt.grid.pager.total.records.text"></spring:message>",
			        emptyrecords: "<spring:message code="staffMgmt.grid.empty.records"></spring:message>",
					loadtext: "<spring:message code="staffMgmt.grid.loading.text"></spring:message>",
					pgtext : "<spring:message code="staffMgmt.grid.pager.text"></spring:message>"
				}).navGrid("#staffListPagingDiv",{edit:false,add:false,del:false,search:false});
				$(".ui-jqgrid-titlebar").hide();
				resizeStaffGrid();
				
				<sec:authorize access="hasAuthority('VIEW_ACCESS_GROUP')">
					loadAccessGroupList();
				</sec:authorize>
			</sec:authorize>
		});
		
		function checkBoxFormatterForViewStatus(cellvalue, options, rowObject) {
			var cbxId = rowObject["name"].split(" ").join("_")+'_'+rowObject["accessGroupName"].split(" ").join("_")+ '_cbx'; 
			if(rowObject['username'] == '<%= BaseConstants.ADMIN_USERNAME %>'){
				return "<input type='checkbox' name='staffGridCbx'  id='"+ cbxId + "' value='"+rowObject['id']+"' disabled/>";
	        } else {
	        	return "<input type='checkbox' name='staffGridCbx'  id='"+ cbxId + "' value='"+rowObject['id']+"'/>";
	        }
			
		}

		function isEnabledFormatter(cellvalue, options, rowObject){
			if(cellvalue == 'false'){
				return '<input id="switch-create-destroy" type="checkbox" data-switch-no-init="" class="checkboxbg" checked="">';
			}else{
				return '<input id="switch-create-destroy" type="checkbox" data-switch-no-init="" class="checkboxbg">';
			}
		}
		
		function staffActiveInactiveToggleChanged(element){
			var toggleName = $(element).prop('name');
			var id_status  = toggleName.split("_");
			var id = id_status[0];
			var status = id_status[1];

			$("#staff-id").val(id);
			$("#staff-current-status").html(status);
			
			if(status == 'ACTIVE'){
				$("#staff-state").val('INACTIVE');
				$("#staff-active-inactive-btn").html("<spring:message code='updateStaffMgmt-change.status-inactive'></spring:message>");
				$("#staff-active-inactive-btn").css('background','#FD7B25');
				$("#staff-current-status").css('background','#FD7B25');
				
			}else{
				$("#staff-state").val('ACTIVE');
				$("#staff-active-inactive-btn").html("<spring:message code='updateStaffMgmt-change.status-active'></spring:message>");
				$("#staff-active-inactive-btn").css('background','#D7D7D7');
				$("#staff-current-status").css('background','#D7D7D7');
			}
			
			$("#staff-reason-for-change-errors").html('');
			$("#staff-reason-for-change").val('');
			
			$("#staffMgmtActiveInActive-modal").click();
		}
		
		function loadAccessGroupList(){
			$.ajax({
				url: '<%= ControllerConstants.GET_ACCESS_GROUP_LIST %>?sidx=name&sord=desc&rows=100',
			    cache: false,
				async: true,
				dataType: 'json',
				type: "GET",
				data: {
				},
				success: function(data){
					var response = eval(data);
					var totalRecords = parseInt(response.records);
			    	if(totalRecords > 0) {
						$.each(response.rows, function(key,value) {
			    			$('#search-staff-access-group').append($('<option>', { 
			    		        value: value.id,
			    		        text : value.name
			    		    }));
						});
			    	}
				},
			    error: function (xhr,st,err){
					handleGenericError(xhr,st,err);
				}
			});
		}
		
		function accountStateColumnFormatter(cellvalue, options, rowObject){
			
			<sec:authorize access="hasAuthority('EDIT_STAFF')" var="staffHasEditStaffRight">
	 		</sec:authorize>
	 		if(rowObject["stafftype"] == '<%= BaseConstants.LDAP_STAFF %>' || rowObject["stafftype"] == '<%= BaseConstants.SSO_STAFF %>')
				return '';
	 		if(${staffHasEditStaffRight}){
				var toggleName = rowObject["id"] + "_" + cellvalue;
				var toggleId = rowObject["name"].split(" ").join("_")+'_'+rowObject["accessGroupName"].split(" ").join("_")+ '_tgl';
				if(cellvalue == 'ACTIVE'){
					return '<input class="checkboxbg" id=' + toggleId + ' name=' + toggleName + ' data-on="Active" data-off="Inactive" checked type="checkbox" data-size="mini">';
				}else{
					return '<input class="checkboxbg" id=' + toggleId + ' name=' + toggleName + ' data-on="Active" data-off="Inactive" type="checkbox" data-size="mini">';
				}
	 		}else{
	 			if(cellvalue == 'ACTIVE'){
	 				return '<label style="color:#FD7B25;font-weight: bold;">' + cellvalue + '</label>';
				}else{
					return '<label style="color:#D7D7D7;font-weight: bold;">' + cellvalue + '</label>';
				}
	 		}
		}
		
		function nameFormatter(cellvalue, options, rowObject){
			if(rowObject["stafftype"] == '<%= BaseConstants.LDAP_STAFF %>')
				return cellvalue;
			var linkId = rowObject["name"].split(" ").join("_")+'_'+rowObject["accessGroupName"].split(" ").join("_")+ '_lnk';
			return '<a id='+linkId+' class="link" onclick="viewStaff(\'' + rowObject["id"] + '\')">' + cellvalue + '</a>' ;
		}
		
		function viewStaff(staffId){
			$("#staffId").val(staffId);
			$("#staff_form").submit();
		}
		
		function lockColumnFormatter(cellvalue, options, rowObject){
			<sec:authorize access="hasAuthority('LOCK_UNLOCK_STAFF')" var="staffHasLockUnlockStaffRight">
	 		</sec:authorize>
	 		if(rowObject["stafftype"] == '<%= BaseConstants.LDAP_STAFF %>' || rowObject["stafftype"] == '<%= BaseConstants.SSO_STAFF %>')
				return '';
	 		var lockStatusId = rowObject["name"].split(" ").join("_")+'_'+rowObject["accessGroupName"].split(" ").join("_");
			if(${staffHasLockUnlockStaffRight}){
				if(rowObject["isAccountLock"] == true)
					return '<img id="'+lockStatusId+'_lock" onclick="unlockStaff(\'' + rowObject["id"] + '\',\'' + rowObject["username"] + '\');" src="img/lock-24.png" style="cursor: pointer;" />';
				else if(rowObject["isAccountLock"] == false)
					return '<img id="'+lockStatusId+'_unlock" onclick="lockStaff(\'' + rowObject["id"] + '\',\'' + rowObject["username"] +  '\');" src="img/unlock-24.png" style="cursor: pointer;" />';
			}else{
				if(rowObject["isAccountLock"] == true)
					return '<img id="'+lockStatusId+'_lock" src="img/lock-24.png"/>';
				else if(rowObject["isAccountLock"] == false)
					return '<img id="'+lockStatusId+'_unlock" src="img/unlock-24.png"/>';
			}
			return "" ;
		}
		
		function lockStaff(staffId, username){
			$("#staff-id-for-lock-unlock").val(staffId);
			$("#staff-new-account-locked-status").val('true');
			
			$("#staff-current-lock-unlock").html('Unlock');
			$("#staff-current-lock-unlock").css('background','#D7D7D7');
			
			$("#staffMgmtLockUnlock-modal").click();
		}
		
		function unlockStaff(staffId, username){
			$("#staff-id-for-lock-unlock").val(staffId);
			$("#staff-new-account-locked-status").val('false');
			
			$("#staff-current-lock-unlock").html('Lock');
			$("#staff-current-lock-unlock").css('background','#FD7B25');
			
			$("#staffMgmtLockUnlock-modal").click();
		}
		
		function deleteStaff(){
			ckStaffSelected = new Array();
			$.each($("input[name='staffGridCbx']:checked"), function(){
				ckStaffSelected.push($(this).val());
			});
			if(ckStaffSelected.length == 0){
				$("#message").click();
				return;
			}
			$("#confirmation").click();
		}		
		
		function permanentRemoveStaff(){
			$.ajax({
					url: '<%= ControllerConstants.DELETE_STAFF %>',
 				    cache: false,
 					async: true,
 					dataType: 'json',
 					type: "POST",
 					data: {
 						staffIds: ckStaffSelected.join()
 					},
 					success: function(data){
 						var response = eval(data);
 				    	response.msg = decodeMessage(response.msg);
 				    	response.msg = replaceAll("+"," ",response.msg);
				    	
 				    	if(response.code == 200 || response.code == "200") {
 				    		resetSearchStaffCriteria();
 				    		clearResponseMsgDiv();
 				    		clearResponseMsg();
 				    		clearErrorMsg();
 				    		showSuccessMsg(response.msg);
 				    		closeFancyBox();
 				    	}else{
 				    		showErrorMsg(response.msg);
 				    		//alert(response.msg);
 				    	}
 					},
 				    error: function (xhr,st,err){
 				    	handleGenericError(xhr,st,err);
 					}
 				});
		}
		
		function searchStaffCriteria(){
			clearResponseMsgDiv();
			clearStaffGrid();
			var $grid = $("#staffList");
			$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
		}

		function resetSearchStaffCriteria(){
			$("#search-staff-first-name").val('');
			$("#search-staff-last-name").val('');
			$("#search-emailId").val('');
			$("#search-staff-code").val('');
		    $("#search-staff-status").val("ALL");
		    $("#search-staff-access-group").val("ALL");
		    $("#search-lock-status").val("ALL");

		    searchStaffCriteria();
		}
		
		function clearStaffGrid(){
			var $grid = $("#staffList");
			var rowIds = $grid.jqGrid('getDataIDs');
			// iterate through the rows and delete each of them
			for(var i=0,len=rowIds.length;i<len;i++){
				var currRow = rowIds[i];
				$grid.jqGrid('delRowData', currRow);
			}
		}
		
		function resizeStaffGrid(){
			var $grid = $("#staffList"),
		    newWidth = $grid.closest(".ui-jqgrid").parent().width();
	 	    if(newWidth < 1000){
		    	newWidth = 800;
		    }
		    //$grid.jqGrid("setGridWidth", newWidth, true);
		}
		
		$(window).on("resize", function () {
			resizeStaffGrid();
		});
	</script>
	
	<style>
		.ui-jqgrid-btable .btn {
		    font-size: 10px;
		}
	</style>
