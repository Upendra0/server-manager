<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

 <div class="fullwidth mtop10" >
	
	
	<div class="box-body table-responsive no-padding box">
		<div class="col-sm-12 mtop10">
			<div class="button-set">
				<div>
            		<button type="button" class="btn btn-grey btn-xs "   tabindex="1"  onclick="expandTree('profile_list_tree','default','div_service');">
						<spring:message code="btn.label.expand.all"></spring:message>
					</button>
					<button type="button" class="btn btn-grey btn-xs " tabindex="2"  onclick="collapseTree('profile_list_tree','default','div_service');">
						<spring:message code="btn.label.collapse.all"></spring:message> 
					</button>
				</div>	
			</div>
		</div>
		
		<div class="col-sm-12" >
			  <div class="input-group">
				<div class="title2 ">
					<spring:message code="addAccessGroup-access.group.acl.tree.title" ></spring:message>
				</div>
			</div>	
			  <div id="div_profile" style="height: 390px;overflow: auto;" >
				<div id="profile_list_tree"></div>
			  </div>
		</div>
		  
	</div>
</div> 

		
		<form method='POST' id='profile_entity_form'>
			<input type='hidden' id="profileEntityList" name='profileEntityList'>
		</form>
<script type="text/javascript"> 

$(document).ready(function() {
	fetchProfileConfiguration();
	
});

function fetchProfileConfiguration(){
 	
 	$.ajax({
		url: '<%= ControllerConstants.GET_PROFILE_CONFIGURATION %>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",			 
		
		success: function(data){
			createProfileConfigTree(data,'profile_list_tree');
		}
	}); 

}

function createProfileConfigTree(menuTabActionData,treeId){
	
	var pluginsArray = new Array() ;
	pluginsArray.push("checkbox");
	pluginsArray.push("sort");
			
	$('#'+treeId).jstree({
						'plugins':pluginsArray,
						'checkbox' : { 
						    "two_state" : true
						},
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
	isEnabled=false;
	expandTree('profile_list_tree','div_profile');
	$('.jstree-anchor').addClass('jstree-anchor-view').removeClass('jstree-anchor');
	$('#profile_list_tree').on('open_node.jstree', function( key, value ) {
		$('.jstree-anchor').addClass('jstree-anchor-view').removeClass('jstree-anchor');
  
});
	
	$('#profile_list_tree').on('close_node.jstree', function( key, value ) {	
		$('.jstree-anchor').addClass('jstree-anchor-view').removeClass('jstree-anchor');
  
});
}



	function makeProfileTreeEnable(){
		
		
		$("#div_profile .jstree-anchor-view").addClass('jstree-default jstree-anchor').removeClass('jstree-anchor-view');
		
		isEnabled=true;
		$('#profile_list_tree').on('open_node.jstree', function( key, value ) {
			
			$('.jstree-anchor-view').addClass('jstree-anchor').removeClass('jstree-anchor-view');
	  
	});
		
		$('#profile_list_tree').on('close_node.jstree', function( key, value ) {	
		
			$('.jstree-anchor-view').addClass('jstree-anchor').removeClass('jstree-anchor-view');
	  
	});
		$("#update_config_btn").show();
		$("#edit_config_btn").hide();
	}
	
	function updateConfiguration(){
		
		showProgressBar("update-configuration");

		var selectedModules= [];
		var selectedSubModules= [];
		var selectedActions= [];
		var aclSelected = $('#profile_list_tree').jstree(true).get_selected();
		
		var isACLsActionSelected = false;
		var index = 0;
		
		if(aclSelected == null || aclSelected == ''){
			
			showErrorMsg('<spring:message code="product.configuration.no.entity.selected"></spring:message>');
			hideProgressBar("update-configuration");
			
		}else{
			
			$.each( aclSelected, function( key, value ){
				
				if(value.indexOf("ACTION_")>= 0){
					isACLsActionSelected = true;
					var newvalue = value.replace("ACTION_","");
					
					selectedActions.push(newvalue);
					
					 
					index++
				}else if(value.indexOf("SUB_MODULE_")>= 0){
					isACLsActionSelected = true;
					var newvalue = value.replace("SUB_MODULE_","");
					
					selectedSubModules.push(newvalue);
					
					 
					index++
					
				}else if(value.indexOf("MODULE_")>= 0){
					isACLsActionSelected = true;
					var newvalue = value.replace("MODULE_","");
					
					selectedModules.push(newvalue);
					
					 
					index++
					
				}
				
				$('#profile_list_tree').find(".jstree-undetermined").each(function(i, element){
					value=$(element).closest('.jstree-node').attr("id");
					if(value.indexOf("SUB_MODULE_")>= 0){
						isACLsActionSelected = true;
						var newvalue = value.replace("SUB_MODULE_","");
						
						selectedSubModules.push(newvalue);
						
					}else if(value.indexOf("MODULE_")>= 0){
						isACLsActionSelected = true;
						var newvalue = value.replace("MODULE_","");
						
						selectedModules.push(newvalue);
						
					}
				});
				
			});
			
			$.ajax({
				url: '<%=ControllerConstants.UPDATE_PROFILE_CONFIGURATION%>',
				cache: false,
				async: true, 
				dataType: 'json',
				type: "POST",
				data: {
					
						"selectedActionList"          			  :	   JSON.stringify(selectedActions),
						"selectedSubModuleList"          		  :	   JSON.stringify(selectedSubModules),
						"selectedModulesList"          			  :	   JSON.stringify(selectedModules),
						
				},
				success: function(data){
					hideProgressBar("update-configuration");

					var response = eval(data);
					
					var responseCode = response.code; 
					var responseMsg = response.msg; 
					var responseObject = response.object;
					
					if(responseCode == "200"){
						
						$("#edit_config_btn").show();
						$("#update_config_btn").hide();
						$("#div_profile .jstree-anchor").addClass('jstree-default jstree-anchor-view').removeClass('jstree-anchor');
						
						showSuccessMsg(responseMsg);
						
						
					}else{
						
						showErrorMsg(responseMsg);
					}
				},
			    error: function (xhr,st,err){
			    	hideProgressBar("update-configuration");
					handleGenericError(xhr,st,err);
				}

			});
		}
	
	}
		
</script>
