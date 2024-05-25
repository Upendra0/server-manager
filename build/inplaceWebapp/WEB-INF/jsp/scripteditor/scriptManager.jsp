<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.scripteditor.constants.ScriptControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<script>
	var currentTab = '${REQUEST_ACTION_TYPE}';
</script>

<jsp:include page="../common/newheader.jsp"></jsp:include>

<body class="skin-blue sidebar-mini sidebar-collapse">
	<div>

		<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
		<jsp:include page="../common/newleftMenu.jsp"></jsp:include>

       <div class="content-wrapper">
			<section class="content-header">
				<div class="fullwidth">
					<div style="padding: 0 10px 0 4px;">
						<div id="content-scroll-d" class="content-scroll">
							<div class="fullwidth">
								<h4
									style="margin-top: 10px; margin-bottom: 0px; font-size: 11px;">
									<a href="home.html"><img src="img/tile-icon.png"
										class="vmiddle" alt="Home" /></a> 
										<span class="spanBreadCrumb" style="line-height: 30px;"> 
										<strong>&nbsp; <spring:message code="leftmenu.label.script.manager" ></spring:message></strong>
									</span>
								</h4>
							
								<div class="row">
									<div class="col-xs-12">
										<div class="box  martop"
											style="border: none; box-shadow: none;">
											<div class="box-body table-responsive no-padding">
												<div class="nav-tabs-custom tab-content">
												
												</div>
											</div>
										</div>
									</div>
								</div>

<div class="tab-content padding0 clearfix" id="server-manager-block">
		
        <div class="fullwidth borbot">
        	<div class="col-md-6 inline-form" style="padding-left: 0px !important;">

			<div class="form-group">
				
				<spring:message code="script.search.server" var="label" ></spring:message>
				
				<div class="table-cell-label">${label}&nbsp;<span class="required">*</span></div>
				<div class="input-group ">
					<select name="serverId" class="form-control table-cell input-sm" tabindex="1" id="search-server" data-toggle="tooltip" data-placement="bottom" title="${search-server.value}" >
						<option value="-1" selected="selected"><spring:message
								code="scriptManagement.search.server.type.allServerType.option"  ></spring:message></option>
						<c:forEach var="server" items="${serverList}">
							<option value="${server.key}">${server.value}</option>
						</c:forEach>
						
					</select> <span class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="left" title=""></i></span>
				</div>
			</div>

		<div class="form-group">
				
				<spring:message code="script.file.name" var="label" ></spring:message>
				
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					<select name="fileId" id="search-filename" class="form-control table-cell input-sm" tabindex="1" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
						<option value="-1" selected="selected"><spring:message code="serverManagement.search.server.type.allServerType.option" ></spring:message></option>
						<c:forEach var="serverType" items="${SERVER_TYPE_LIST}">
							<option value="${serverType.id}">${serverType.name}</option>
						</c:forEach>
						
					</select> <span class="input-group-addon add-on last"> <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
				</div>
			</div>
			   
         	</div>
	             	<div class="table-cell-label">&nbsp;</div>
	             	<div class="input-group ">
	             		&nbsp;
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
	             	</div>
           
           <div class="clearfix"></div>
     		<div class="pbottom15 form-group">
   					<button type="button" id="searchServerBtn" class="btn btn-grey btn-xs" onclick="searchScriptFiles();"><spring:message code="btn.label.search" ></spring:message></button>
   					
   			</div>
   		</div>
			
		<div class="box-body table-responsive no-padding box" style="padding-top: 5px;">
	        		<table class="table table-hover" id="fileList"></table>
	            	<div id="fileListPagingDiv"></div> 
	           		<div class="clearfix"></div>   
		</div>
</div>  



<script type="text/javascript">

		var ckIntanceSelected = new Array();
		var gridDataArr = [];
		var instanceList = {};
		var oldGrid = '';
		
		
		$(document).ready(function() {
			$("#fileList").jqGrid({
	        	url: "<%= ScriptControllerConstants.SEARCH_SCRIPT_MANAGER %>",
	        	postData: {
	        		serverId: function () {
	        	        return $("#search-server").val();
	   	    		},
	   	    		fileId: function () {
	        	        return $("#search-filename").val();
	   	    		}
	        	},
	            datatype: "json",
	            colNames:[
	            		  "File Id",
	                      "File Name",
	                       "File Path",
	                       "Alias(Server IP)",
	                       "Execution Command",
	                       "File Description"
	                     ],
				colModel:[
					{name:'fileid',index:'fileid',sortable:true,hidden: true},
	            	{name:'filename',index:'filename',hidden:false,formatter: nameFormatter},
	                {name:'filepath',index:'filepath',sortable:false},
	            	{name:'alias',index:'alias',sortable:false},
	            	{name:'excommand',index:'excommand',sortable:false},
	            	{name:'filedesc',index:'filedesc',sortable:false}
	            ],
	            rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
	            rowList:[10,20,10,30,20],
	            height: 'auto',
	            width: "1000px",
	        	sortname: 'fileid',
	     		sortorder: "desc",
	            pager: "#fileListPagingDiv",
	            viewrecords: true,
	     		loadComplete: function(data) {
	     			if ($('#fileList').getGridParam('records') === 0) {
	                    $('#fileList tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code="staffMgmt.grid.empty.records"></spring:message></div>");
	                    $("#fileListPagingDiv").hide();
	                }else{
	                	$("#fileListPagingDiv").show();
		    			resizeSearchGrid();
	                }
				},
				onPaging: function (pgButton) {
				
					clearResponseMsgDiv();
					clearSearchGrid();
				},
				loadError : function(xhr,st,err) {
					handleGenericError(xhr,st,err);
				},
				recordtext: "<spring:message code="staffMgmt.grid.pager.total.records.text"></spring:message>",
		        emptyrecords: "<spring:message code="staffMgmt.grid.empty.records"></spring:message>",
				loadtext: "<spring:message code="staffMgmt.grid.loading.text"></spring:message>",
				pgtext : "<spring:message code="staffMgmt.grid.pager.text"></spring:message>"
			}).navGrid("#fileListPagingDiv",{edit:false,add:false,del:false,search:false});
			$(".ui-jqgrid-titlebar").hide();
			resizeSearchGrid();
			

			$("#refresh_fileList").hide();
			
			$('#search-server').change(function(e) {
			
				   var search = {}
					search["serverId"] = $("#search-server").val();
				
				   $.ajax({
						type : "GET",
						url : "<%= ScriptControllerConstants.FILE_LIST_FOR_SERVER %>",
						data : "serverId="+search["serverId"],
						dataType : 'json',
						timeout : 100000,
						success : function(data) {
							if(data.code=='200'){ 
						
								$('#search-filename').empty()
								 .append('<option selected="selected" value="-1">All</option>');
								
								$.each(data.object, function (key, value) {
									$('#search-filename').append('<option value="' + key + '">' + value + '</option>');
								});
							}
	    					
						},
						error : function(e) {
							console.log("ERROR: ", e);
						},
						done : function(e) {
							console.log("DONE");
							enableSearchButton(true);
						}
					});
				  
			   });
			 
		});
		
		function clearSearchGrid(){
			var $grid = $("#fileList");
			var rowIds = $grid.jqGrid('getDataIDs');
			// iterate through the rows and delete each of them
			for(var i=0,len=rowIds.length;i<len;i++){
				var currRow = rowIds[i];
				$grid.jqGrid('delRowData', currRow);
			}
		}
		
		function resizeSearchGrid(){
			var $grid = $("#fileList"),
		    newWidth = $grid.closest(".ui-jqgrid").parent().width();
	 	    if(newWidth < 1000){
		    	newWidth = 800;
		    }
		}
		
		function nameFormatter(cellvalue, options, rowObject) {
		
 			var flid = rowObject['fileid'];
 			var action = "<%= ScriptControllerConstants.EDIT_FILE %>";
	        return '<a href='+action+'?fileId='+flid+'>'+cellvalue+' </a>';
		}
		
		//Rohit 
		function searchScriptFiles(){
			
			//clearResponseMsgDiv();
			clearSearchGrid();
			var $grid = $("#fileList");
			$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
		}
			
	function loadScriptTableDetail(tableId,data){
		
				$(tableId).bootstrapTable({
					data : data, 
					pagination : false,
					sortable : true,
					cookie : true,
					iconsPrefix :'glyphicon glyphicon-download-alt',
					mobileResponsive : true
			});
		}
	
</script>
							</div>
						</div>
					</div>
				</div>
			</section>
		</div>
  
       <footer class="main-footer positfix">
			<div class="fooinn">
				<jsp:include page="../common/newfooter.jsp"></jsp:include>
			</div>
		</footer>
		</div>
       </body>
</html>
