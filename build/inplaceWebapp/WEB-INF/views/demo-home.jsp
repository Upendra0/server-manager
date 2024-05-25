<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<!-- <script type='text/javascript' src='http://code.jquery.com/jquery-1.6.2.js'></script> -->
		
		<script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
		<script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>

		<link rel="stylesheet" type="text/css" href="/css/normalize.css">
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.14/jquery-ui.js"></script>
		
		
		<link rel="stylesheet" type="text/css" href="/css/result-light.css">
		<link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.14/themes/base/jquery-ui.css">
		<link rel="stylesheet" type="text/css" href="http://trirand.com/blog/jqgrid/themes/ui.jqgrid.css">
		<script type='text/javascript' src="http://trirand.com/blog/jqgrid/js/i18n/grid.locale-en.js"></script>
		<script type='text/javascript' src="http://trirand.com/blog/jqgrid/js/jquery.jqGrid.min.js"></script>
		<style type="text/css">
			.ui-jqgrid .ui-pg-input{
				height: 22px;
			}
		</style>
		<script type='text/javascript'>
			jQuery(document).ready(function () {
				$("#grid").jqGrid({
	            	url: "demo-get-ajax",
	                datatype: "json",
	                colNames:['Id','Name'],
					colModel:[
	                	{name:'id',index:'id'},
	                    {name:'name',index:'name'}
	                ],
	                rowNum:10,
	                rowList:[10,20,60,100],
	                height: 'auto',
	                width: 700,
					sortname: 'id',
             		sortorder: "desc",
	                pager: "#pagingDiv",
	                viewrecords: true,
	                caption: "<spring:message code="demo.jqgrid.caption"></spring:message>",
	                editurl: 'demo-delete-ajax',
	         		loadComplete: function() {
	        			$("tr.jqgrow:odd").css("background", "#E0E0E0");

	        			$("#update-Id").val('0');
						$("#update-Name").val('');
					}
				}).navGrid("#pagingDiv",{edit:false,add:false,del:true,search:false});
				
				
				$("#grid").jqGrid('setGridParam', {
					onSelectRow: function(rowid, status,e){
						var id = $('#grid').getCell(rowid, 'id');
						var name = $('#grid').getCell(rowid, 'name');
						$("#update-Id").val(id);
						$("#update-Name").val(name);
						
					}
				});
			});
			$(window).on("resize", function () {
			    var $grid = $("#grid"),
			    newWidth = $grid.closest(".ui-jqgrid").parent().width();
			    console.log("newWidth: " + newWidth);
			    if(newWidth > 700){
			    	newWidth = 700;
			    }
			    $grid.jqGrid("setGridWidth", newWidth, true);
			});
		</script>
	</head>
	<body>
		<table style="width: 100%;">
			<tr>
				<td>
					<h1>
						<spring:message code="demo.page.title" ></spring:message>
					</h1>
				</td>
				<td style="width: 20%;">
					<h5>
						Language:
						<a href="demo-home?<%= BaseConstants.USER_SELECTED_LANGUAGE %>=en">English</a> &nbsp;&nbsp; | &nbsp;&nbsp;
						<a href="demo-home?<%= BaseConstants.USER_SELECTED_LANGUAGE %>=fr">French</a>
					</h5>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					${RESPONSE_MSG}
				</td>
			</tr>
		</table>
		<hr>
		
		<h3>
			<spring:message code="demo.save" ></spring:message>
		</h3>
		<form:form modelAttribute="demo-object-command" method="POST" action="demo-save">
			<table>
				<tr>
					<td>
						<form:label path="name">
							<spring:message code="demo.name" ></spring:message>
						</form:label>
					</td>
					<td>
						<form:input path="name" ></form:input>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="submit" value="<spring:message code="demo.submit-btn"></spring:message>" />
					</td>
				</tr>
			</table>
		</form:form>
		<hr>
		
		<table>
			<tr>
				<td>
					<h3>
						<spring:message code="demo.get.label" ></spring:message>
					</h3>
				</td>
				<td style="width: 50px;"></td>
				<td>
					<h3>
						<spring:message code="demo.update" ></spring:message>
					</h3>
				</td>
			</tr>
			<tr>
				<td>
					<table id="grid"></table>
					<div id="pagingDiv"></div>
				</td>
				<td></td>
				<td valign="top">
					<form:form modelAttribute="demo-object-command" method="POST" action="demo-update">
						<table>
							<tr>
								<td>
									<form:label path="name">
										<spring:message code="demo.name" ></spring:message> 
									</form:label>
								</td>
								<td>
									<form:input id="update-Id" path="id" cssStyle="display:none;"></form:input>
									<form:input  id="update-Name" path="name" ></form:input>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<input type="submit" value="<spring:message code="demo.update-btn"></spring:message>" />
								</td>
							</tr>
						</table>
					</form:form>
				</td>
			</tr>
		</table>
	</body>
</html>
