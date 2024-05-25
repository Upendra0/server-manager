<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>

<html>
<script>
var accessgrouplist = new Array();
</script>

<style>
	.error{
		color: red;
	}
</style>

  	<!-- topnavigation Start -->

	<jsp:include page="../common/newheader.jsp"></jsp:include>
 
 <body class="skin-blue sidebar-mini">
    <div class="wrapper">

   	<!-- Header Start -->
    
   	<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
    	 
   	<!-- Header End -->
   	
   	 <jsp:include page="../common/newleftMenu.jsp"></jsp:include>
    	 
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
        	<div class="fullwidth">
        		<div style="padding:0 10px 0 4px;">
        			<div id="content-scroll-d" class="content-scroll">
          
          				<!-- Content Wrapper. Contains page content Start -->
         				<div class="fullwidth">
        
        	 				<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
                            	<img src="img/tile-icon.png" class="vmiddle" alt="Title Icon"/> 
                            	<span class="spanBreadCrumb" style="line-height: 30px;font-weight: bold;">
                            		<a href="<%= ControllerConstants.STAFF_MANAGER %>">
				                    	<spring:message code="staffManager.page.heading" ></spring:message>
				                    </a>&nbsp;/&nbsp;	
	                    			 <a href="<%= ControllerConstants.STAFF_MANAGER %>?REQUEST_ACTION_TYPE=<%= BaseConstants.ADD_STAFF %>">
				                    	<spring:message code="staffManager.staff.mgmt.tab" ></spring:message>
				                    </a>&nbsp;&nbsp;
                            	</span>
	                        </h4>
	                        
          					<div class="row">
            					<div class="col-xs-12">
              						<div class="box  martop" style="border:none; box-shadow: none;">
                						<!-- /.box-header -->
                						<div class="box-body table-responsive no-padding">
                							<div class="nav-tabs-custom">
                								<sec:authorize access="hasAnyAuthority('ADD_STAFF') or hasAnyAuthority('EDIT_STAFF')">
                									<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
                								<!-- Tabs within a box -->
								               <ul class="nav nav-tabs pull-right">
								                  <li class="disabled"><a href="#"><spring:message code="addStaff.add.staff.page.heading" ></spring:message></a></li>
								                  <li class="active" ><a href="#assign-access-group" data-toggle="tab"><spring:message code="addStaff.assign.access.group" ></spring:message></a></li>
								                  <li class="disabled" id="change-profile-pic-a"><a href="#"><spring:message code="addStaff.change.profile.pic.tab.heading" ></spring:message></a></li>
								                </ul>
                
               									<div class="col-md-6 no-left-padding">
       			  									<div class="tab-content no-padding clearfix">
                  										<div role="tabpanel" class="tab-pane active" id="create-staff">
                  											
                  										</div>
                  										<div class="chart tab-pane active" id="assign-access-group">
                  										<form:form modelAttribute="staff_form_bean" method="post" action="addStaffAndAssignAccessGroup" id="staff-form" encType='multipart/form-data'>
								
															<form:input type="hidden" path="id"  	id="id" ></form:input>
															
													        
													        <c:set var="aglIndex" scope="request" value="0"></c:set>
															<c:forEach items="${staff_form_bean.accessGroupList}" var="accessGroupList">
															<script>
																accessgrouplist.push('${accessGroupList.id}');
															</script>
															<c:set var="aglIndex" scope="request" value="${aglIndex + 1}"></c:set>
															</c:forEach>
                  											
                  											<div id="accessGroupListDiv" style="display: none;">
															</div>
															<form:errors path="accessGroupList" cssClass="error"></form:errors>
									
                  											<div class="fullwidth">
                      											<div class="full-size">
                      												<p class="title2 pull-left">&nbsp;&nbsp;</p>
                      												<a id="selectAllAccessGroup_div" class="pull-right black font12 mtop10" href="javascript: selectAllAccessGroup()">
																		<spring:message code="addStaff.select.all.access.group" ></spring:message>
																	</a>	
                      											</div>
                      											<div class="clearfix"></div>
										                    </div>
                  											<!-- Morris chart - Sales -->
                  											<div class="box-body table-responsive no-padding box">
                  												<table id="assignAccessGroupList"></table>
																<div id="assignAccessGroupListPagingDiv"></div>
                  											</div>
                  											
                  											</form:form>
                  										</div>
                									</div>
               									</div>
              									</sec:authorize>
              								</div>
                							
                						</div>
                						<!-- /.box-body -->
              						</div><!-- /.box -->
                          
                					<div class="fullwidth">
                						<div class="redtext"><spring:message code="label.important.note" ></spring:message></div>
                    					<span><spring:message code="addStaff.important.note.content" ></spring:message></span>
                					</div>
                        		</div>
            				</div>
            
          				</div>
         				<!-- Content Wrapper. Contains page content End --> 
        			</div>
          		</div>
          </div>
       </section>
    </div><!-- /.content-wrapper -->

    <!-- Footer Start -->
	<footer class="main-footer positfix">
    	<div class="fooinn">
        	<div class="fullwidth">
        		<div class="button-set">
	            	<div class="padleft-right" id="staffCreateDiv">
	            		<button id="staffAssignAG_btn" class="btn btn-grey btn-xs " tabindex="21" onclick="validateFields();">
							<spring:message code="btn.label.save" ></spring:message>
						</button>
						<button class="btn btn-grey btn-xs " tabindex="23" onclick="deselectAllAccessGroup();">
							<spring:message code="btn.label.reset" ></spring:message>
						</button>
						<a class="btn btn-grey btn-xs " style="text-decoration:none;" tabindex="23" href="<%= ControllerConstants.STAFF_MANAGER %>?REQUEST_ACTION_TYPE=<%= BaseConstants.ADD_STAFF %>">
							<spring:message code="btn.label.cancel" ></spring:message>
						</a>
					</div>	
				</div>
        	</div>
        	
     		<jsp:include page="../common/newfooter.jsp"></jsp:include>
     	</div>
	</footer>
    <!-- Footer End -->			

    </div><!-- ./wrapper -->

	<script type="text/javascript">
		function deselectAllAccessGroup(){
			var count=jQuery('#assignAccessGroupList').jqGrid('getGridParam', 'records')
			for (i = 0; i < count; i++) {
				$('#' + $('#assignAccessGroupList').jqGrid('getRowData')[i].name.split(" ").join("_")+'_cbx').prop('checked',false);
			}
		}
		
		function selectAllAccessGroup(){
			var count=jQuery('#assignAccessGroupList').jqGrid('getGridParam', 'records')
			for (i = 0; i < count; i++) {
				$('#' + $('#assignAccessGroupList').jqGrid('getRowData')[i].name.split(" ").join("_")+'_cbx').prop('checked',true);
			}
		}
			
			var totalCKIds = new Array();
			$( document ).ready(function() {
				
				$(".datetimepicker").hide();
				
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
		                {name:'id',index:'id',align:'center',formatter:assignColumnFormatter},
		                {name:'accessgrouptype',index:'accessgrouptype',hidden:false,align:'center'}
		            ],
		            rowNum:100,
		            rowList:[10,20,40,80,160],
		            height: 'auto',
		            width: "618",
					sortname: 'id',
		     		sortorder: "desc",
		            pager: "#assignAccessGroupListPagingDiv",
		            viewrecords: true,
		            caption: "<spring:message code="addStaffAndAssignAccessGroup.grid.caption"></spring:message>",
		            loadonce:true,
		            rownumbers:true,
		     		loadComplete: function() {
// 		    			$("tr.jqgrow:odd").css("background", "#E0E0E0");
		    			resizeGrid();
					},
					beforeSelectRow: function (rowid, e){
					},
					recordtext: "<spring:message code="addStaffAndAssignAccessGroup.grid.pager.total.records.text"></spring:message>",
			        emptyrecords: "<spring:message code="addStaffAndAssignAccessGroup.grid.empty.records"></spring:message>",
					loadtext: "<spring:message code="addStaffAndAssignAccessGroup.grid.loading.text"></spring:message>",
					pgtext : "<spring:message code="addStaffAndAssignAccessGroup.grid.pager.text"></spring:message>"
				}).navGrid("#pagingDiv",{edit:false,add:false,del:false,search:false});

				resizeGrid();
			});
			
			function assignColumnFormatter(cellvalue, options, rowObject){
				if(rowObject["accessgrouptype"] == 'LDAP' || rowObject["accessgrouptype"] == 'SSO'){
					return '';
				}
				totalCKIds.push(cellvalue);
				var elementid=rowObject["name"].split(" ").join("_") + '_cbx';
				if(accessgrouplist.indexOf(cellvalue) != -1)
					return '<input type="checkbox" id="' + elementid + '" name="' + elementid + '" value="' + cellvalue + '" checked="checked">' ;
				else
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
			
			function goBackToAddStaff(){
				$("#staff-form").attr("action",'<%= ControllerConstants.GO_BACK_TO_ADD_STAFF_FROM_ASSIGN_ACCESS_GROUP %>');
				$("#staff-form").submit();
			}
			
			function validateFields(){
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
				$("#staff-form").submit();
			}
			
		</script>
		
</body>
</html>
