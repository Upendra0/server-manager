<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="error" %>



<div class="fullwidth">
	<div class="input-group">
		<div class="title2 ">
			<spring:message code="addAccessGroup-access.group.acl.tree.title" ></spring:message>
		</div>
		 <spring:bind path="actions">
			<c:if test="${not empty status.errorMessage}">
				<error:showError errorMessage="${status.errorMessage}"></error:showError>
			</c:if>
		</spring:bind> 
	</div>
	<div style="height: 350px;overflow: auto;">
		<div id="acls-tree"></div>
	</div>
</div>


<script type="text/javascript">
function loadMenuTabAndActionList(){
		var actionIdsSelected = new Array();
	
		 <c:forEach var="action" items="${access_group_form_bean.actions}"> //add the model attribute of list in items
			actionIdsSelected.push('<c:out value="${action.id}"></c:out>');
		</c:forEach> 
	
		$.ajax({
			url: '<%= ControllerConstants.GET_MENU_TAB_ACTION_LIST %>',
			cache: false,
			async: true,
			dataType: 'json',
			type: "POST",			 
			data: {
				actionIdsSelected : actionIdsSelected.join()
			},
			success: function(data){
				createMenuTabActionTree(data);
			}
		}); 
	}
									
	function createMenuTabActionTree(menuTabActionData){
		var pluginsArray = new Array() ;
		pluginsArray.push("checkbox");
		pluginsArray.push("sort");			
		$('#acls-tree').jstree({
							'plugins':pluginsArray, 
							'core' : {
										'data' : menuTabActionData
									}
						}).on('changed.jstree', function (e, data) {
							    var i, j, r = [];
							    for(i = 0, j = data.selected.length; i < j; i++) {
									r.push(data.instance.get_node(data.selected[i]).id);
							    }
							    $('#event_result').html('Selected: ' + r.join(', '));
	  						});
	
		expandAll();
		$( ".jstree-anchor" ).each(function( index ) {
			  $(this).attr("id",$(this).html().replace(/(<([^>]+)>)/ig,"").split(" ").join("_")+"_cbx");
			});
		$( ".jstree-anchor-view" ).each(function( index ) {
			  $(this).attr("id",$(this).html().replace(/(<([^>]+)>)/ig,"").split(" ").join("_")+"_cbx");
			});
		<c:choose>
			<c:when test="${REQUEST_ACTION_TYPE == 'VIEW_ACCESS_GROUP'}">
				$('.jstree-anchor').addClass('jstree-anchor-view').removeClass('jstree-anchor');
				$('#acls-tree').on('open_node.jstree', function( key, value ) {
					   
					$('.jstree-anchor').addClass('jstree-anchor-view').removeClass('jstree-anchor');
			  
			});
				
				$('#acls-tree').on('close_node.jstree', function( key, value ) {
					   
					$('.jstree-anchor').addClass('jstree-anchor-view').removeClass('jstree-anchor');
			  
			});

			</c:when>
		</c:choose>
	}
		
	
	 
	
	function expandAll(){
		$("#acls-tree").jstree("open_all");
		<c:choose>
		<c:when test="${REQUEST_ACTION_TYPE == 'VIEW_ACCESS_GROUP'}">
			$('.jstree-anchor').addClass('jstree-anchor-view').removeClass('jstree-anchor');
		</c:when>
	</c:choose>
	}
								
	function collapseAll(){
		$("#acls-tree").jstree("close_all");
		<c:choose>
		<c:when test="${REQUEST_ACTION_TYPE == 'VIEW_ACCESS_GROUP'}">
			$('.jstree-anchor').addClass('jstree-anchor-view').removeClass('jstree-anchor');
		</c:when>
	</c:choose>
	}
</script>
