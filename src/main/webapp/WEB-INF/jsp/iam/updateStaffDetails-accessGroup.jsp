<%@page import="com.elitecore.sm.iam.model.Staff"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<script>
var accessgrouplist = new Array();
</script>

<div class="fullwidth borbot tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_STAFF_ACCESS_GROUP')}"><c:out value="active"></c:out></c:if>" id="update-staff-access-group">
	<div class="fullwidth padding0">
		<div class="col-md-6 no-left-padding">

		<form:form modelAttribute="staff_form_bean" method="POST" action="<%= ControllerConstants.UPDATE_STAFF %>" id="staff-access-group-form" enctype="multipart/form-data">

			<form:input type="hidden" path="id"  id="staff-id" ></form:input>
			<form:input type="hidden" path="username"  id="staff-username" ></form:input>
			<form:input type="hidden" path="password"  id="staff-password" ></form:input>
			<form:input type="hidden" path="staffCode"  id="staff-staffCode" ></form:input>
			<form:input type="hidden" path="firstTimeLogin"  id="staff-firstTimeLogin" ></form:input>

			<form:input type="hidden" path="securityQuestion1"  id="staff-securityQuestion1" ></form:input>
			<form:input type="hidden" path="securityAnswer1"  id="staff-securityAnswer1" ></form:input>
			<form:input type="hidden" path="securityQuestion2"  id="staff-securityQuestion2" ></form:input>
			<form:input type="hidden" path="securityAnswer2"  id="staff-securityAnswer2" ></form:input>
										
			<form:input type="hidden" path="accountState"  id="staff-accountState" ></form:input>
			<form:input type="hidden" path="accountLocked"  id="staff-accountLocked" ></form:input>
			<form:input type="hidden" path="wrongAttempts"  id="staff-wrongAttempts" ></form:input>
			<form:input type="hidden" path="lastWrongAttemptsDate"  id="staff-lastWrongAttemptsDate" ></form:input>
			<form:input type="hidden" path="passwordExpiryDate"  id="staff-passwordExpiryDate" ></form:input>
			<form:input type="hidden" path="lastLoginTime"  id="staff-lastLoginTime" ></form:input>
			<form:input type="hidden" path="createdDate"  id="staff-createdDate" ></form:input>
			<form:input type="hidden" path="lastUpdatedDate"  id="staff-lastUpdatedDate" ></form:input>

			<form:input type="hidden" path="createdByStaffId"  id="staff-createdByStaffId" ></form:input>
			<form:input type="hidden" path="lastUpdatedByStaffId"  id="staff-lastUpdatedByStaffId" ></form:input>
			<form:input type="hidden" path="firstName"  id="staff-firstName" ></form:input>
			<form:input type="hidden" path="lastName"  id="staff-lastName" ></form:input>
			<form:input type="hidden" path="middleName"  id="staff-middleName" ></form:input>
			<form:input type="hidden" path="birthDate" id="staff-birthDate" ></form:input>
			<form:input type="hidden" path="address"  id="staff-address" ></form:input>
			<form:input type="hidden" path="address2"  id="staff-address2" ></form:input>
			<form:input type="hidden" path="city"  id="staff-city" ></form:input>
			<form:input type="hidden" path="state"  id="staff-state" ></form:input>
			<form:input type="hidden" path="country"  id="staff-country" ></form:input>
			<form:input type="hidden" path="pincode"  id="staff-pincode" ></form:input>
			<form:input type="hidden" path="emailId"  id="staff-emailId" ></form:input>
			<form:input type="hidden" path="emailId2"  id="staff-emailId2" ></form:input>
			<form:input type="hidden" path="mobileNo"  id="staff-mobileNo" ></form:input>
			<form:input type="hidden" path="landlineNo"  id="staff-landlineNo" ></form:input>
			<form:input type="hidden" path="loginIPRestriction" id="staff-loginIPRestriction" ></form:input>
			<form:input type="hidden" path="profilePic"  id="staff-profilePic" ></form:input>
			
			<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.UPDATE_STAFF_ACCESS_GROUP %>" />
			
			<c:set var="aglIndex" scope="request" value="0"></c:set>
			<c:forEach items="${staff_form_bean.accessGroupList}" var="accessGroupList">
				<script>
					accessgrouplist.push('${accessGroupList.id}');
				</script>
				<input type="hidden" id="ck_accessGroupList_selected_${accessGroupList.id}" name="ck_accessGroupList_selected_${accessGroupList.id}" value="${accessGroupList.id}" />
				<c:set var="aglIndex" scope="request" value="${aglIndex + 1}"></c:set>
			</c:forEach>

			<div id="accessGroupListDiv" style="display: none;">
			</div>
			<form:errors path="accessGroupList" cssClass="error" ></form:errors>
			
			<div class="fullwidth mtop10">
        		<div class="full-size">
        			<p class="title2 pull-left">&nbsp;&nbsp;</p>
        			<a id="selectAllAccessGroup" class="pull-right black font12" href="javascript: selectAllAccessGroup()" tabindex="1" style="width: 475px; text-align: right; display: inline-block;">
						<spring:message code="addStaff.select.all.access.group" ></spring:message>
					</a>	
        		</div>
        		<div class="clearfix"></div>
            </div>
			
			<div class="box-body table-responsive no-padding box">
				<table id="assignAccessGroupList"></table>
				<div id="assignAccessGroupListPagingDiv"></div>
			</div>
			
            <div class="fullwidth">
				<div class="redtext"><spring:message code="label.important.note" ></spring:message></div>
  				<span><spring:message code="addStaff.important.note.content" ></spring:message></span>
			</div>
			
		</form:form>
	</div>
	</div>
</div>

<script type="text/javascript">
	function deselectAllAccessGroup(){
		var count=jQuery('#assignAccessGroupList').jqGrid('getGridParam', 'records')
		for (i = 0; i < count; i++) {
			$('#' + $('#assignAccessGroupList').jqGrid('getRowData')[i].name.split(" ").join("_")+'_cbx').prop('checked',false);
		}
		resetWarningDisplay();
	}
		
	function selectAllAccessGroup(){
		var count=jQuery('#assignAccessGroupList').jqGrid('getGridParam', 'records')
		for (i = 0; i < count; i++) {
			$('#' + $('#assignAccessGroupList').jqGrid('getRowData')[i].name.split(" ").join("_")+'_cbx').prop('checked',true);
		}
	}
			
	var totalCKIds = new Array();
	$( document ).ready(function() {
		
		$("#assignAccessGroupList").jqGrid({
        	url: "<%= ControllerConstants.GET_ACCESS_GROUP_LIST %>",
        	postData: {
   	    		<%-- created_by_staff_id: function () {
        	        return "<%= new EliteUtils().getLoggedInStaffId(request) %>";
   	    		}, --%>
   	    		search_active_inactive_status:function(){
   	    			return 'Active';
   	    		}
        	},
            datatype: "json",
            colNames:[
				"<spring:message code='addStaffAndAssignAccessGroup.grid.column.id' ></spring:message>",
		        "<spring:message code='addStaffAndAssignAccessGroup.grid.column.name' ></spring:message>",
				"<spring:message code='addStaffAndAssignAccessGroup.grid.column.assign' ></spring:message>",
				"Access Group Type"
			],
			colModel:[
				{name:'id',index:'id',sortable:true,hidden:true},
				{name:'name',index:'name',sortable:true},
				{name:'id',index:'id',formatter:assignColumnFormatter,align:'center'},
				{name:'accessgrouptype',index:'accessgrouptype',hidden:false,align:'center'}
			],
			rowNum:100,
			rowList:[10,20,40,80,100],
			height: 'auto',
			width: "603",
			sortname: 'id',
			sortorder: "desc",
			pager: "#assignAccessGroupListPagingDiv",
			viewrecords: true,
			caption: "<spring:message code="addStaffAndAssignAccessGroup.grid.caption"></spring:message>",
			loadonce:true,
			rownumbers:true,
			loadComplete: function() {
				$("tr.jqgrow:odd").css("background", "#E0E0E0");
				resizeGrid();
				/* var rows = jQuery("#assignAccessGroupList").getRowData();
				var rowsIds = jQuery("#assignAccessGroupList").getDataIDs();
				 for(a=0;a<rows.length;a++){
				    var row=rows[a];
				    if(row.accessgrouptype == 'LDAP'){
				    	$("#"+rowsIds[a]).hide();
				    } 
				 } */
					
			},
			beforeSelectRow: function (rowid, e){
			},
			recordtext: "<spring:message code="addStaffAndAssignAccessGroup.grid.pager.total.records.text"></spring:message>",
	        emptyrecords: "<spring:message code="addStaffAndAssignAccessGroup.grid.empty.records"></spring:message>",
			loadtext: "<spring:message code="addStaffAndAssignAccessGroup.grid.loading.text"></spring:message>",
			pgtext : "<spring:message code="addStaffAndAssignAccessGroup.grid.pager.text"></spring:message>"
		}).navGrid("#pagingDiv",{edit:false,add:false,del:false,search:false});

		resizeGrid();

		submitForm();
	});
			
	function assignColumnFormatter(cellvalue, options, rowObject){
		if(rowObject["accessgrouptype"] == 'LDAP'){
			return '';
		}
		totalCKIds.push(cellvalue);
		var element = $("#ck_accessGroupList_selected_" + cellvalue);
		var elementid=rowObject["name"].split(" ").join("_") + '_cbx'; 
		if(typeof element != 'undefined' && typeof element.val() != 'undefined'){
			if(accessgrouplist.indexOf(cellvalue.toString()) != -1){
				return '<input type="checkbox" checked=checked id="' + elementid + '" name="' + elementid + '" value="' + cellvalue + '">' ;
			}
		}
		return '<input type="checkbox" id="' + elementid + '" name="' + elementid + '" value="' + cellvalue + '">' ;
	}
			
	function resizeGrid(){
		var $grid = $("#accessGroupList"),
	    newWidth = $grid.closest(".ui-jqgrid").parent().width();
 	    if(newWidth < 1000){
	    	newWidth = 1000;
	    }
	    $grid.jqGrid("setGridWidth", newWidth, true);
	}

	$(window).on("resize", function () {
		resizeGrid();
	});
	
	function submitForm(){
		$("#staff-access-group-form input, select, textarea").keypress(function (event) {
		    if (event.which == 13) {
		        event.preventDefault();
		        $(this).blur();
		        validateFieldsForAccessGroup();
		    }
		});
	}
	
	function validateFieldsForAccessGroup(){
		$("#accessGroupListDiv").html('');
		var accessGroupIndex = 0;
		var i;
		var count=jQuery('#assignAccessGroupList').jqGrid('getGridParam', 'records')
		for (i = 0; i < count; i++) {
			if($('#' + $('#assignAccessGroupList').jqGrid('getRowData')[i].name.split(" ").join("_")+'_cbx').is(':checked')){
				$("#accessGroupListDiv").append(
					'<input type="hidden" id="accessGroupList[' + accessGroupIndex + '].id" name="accessGroupList[' + accessGroupIndex + '].id" value="' + $('#' + $('#assignAccessGroupList').jqGrid('getRowData')[i].name.split(" ").join("_")+'_cbx').val() + '"  />'
				);
				accessGroupIndex++;
			}
			
		}
		$("#staff-access-group-form").submit();
	}
</script>
