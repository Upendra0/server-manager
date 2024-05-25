<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

 <div class="fullwidth mtop10" >
	
	<p class="title2">
		<label> <spring:message code="product.configuration.product.name.title.label"></spring:message> :&nbsp; </label>
	<label><select id="selCustomProductType" class='form-control table-cell input-sm'  data-toggle='tooltip' data-placement='bottom' title='${tooltip }' onChange='fetchCustomProfile();'>
						<c:forEach items="${activeServerTypeList}" var="serverType">
							<option value="${serverType.id}">${serverType.name}</option>
						</c:forEach>
			       </select></label>	
			      
			       <span style="float: right;" class="title2rightfield title2rightfield-icon1-text">
			       <a href="javascript:;" onclick="viewDefaultConfig('service');" style="cursor: pointer; cursor: hand;text-decoration: underline;">
						<spring:message code="product.configuration.default.link.label" ></spring:message>
						</a>
			       </span>
	</p>
	<div class="box-body table-responsive no-padding box">
		<div class="col-sm-12 mtop10">
			<div class="button-set">
				<div>
            		<button type="button" class="btn btn-grey btn-xs "   tabindex="1"  onclick="expandTree('service_list_tree','default','div_service');">
						<spring:message code="btn.label.expand.all"></spring:message>
					</button>
					<button type="button" class="btn btn-grey btn-xs " tabindex="2"  onclick="collapseTree('service_list_tree','default','div_service');">
						<spring:message code="btn.label.collapse.all"></spring:message> 
					</button>
				</div>	
			</div>
		</div>
		
		<div class="col-sm-12" >
			  <div class="input-group">
				<div class="title2 ">
					<spring:message code="product.configuration.service.tree.label" ></spring:message>
				</div>
			</div>	
			  <div id="div_service" style="height: 400px;overflow: auto;" >
				<div id="service_list_tree"></div>
			  </div>
		</div>
		  
	</div>
</div> 


<div id="view_default_config_popup" style=" width:100%; display: none;" >
		     <div class="modal-content">
		        <div class="modal-header padding10" >
		            <span class="modal-title">
		            	<spring:message code="product.configuration.default.tab.label"></spring:message>
		            </span>
		        </div>
		        <div class="modal-body padding10 inline-form" style="min-height:450px;" id="default_config_actions">
		        <div class="col-md-6 no-padding">
		        <div class='form-group'>
					<div class='table-cell-label'><spring:message code="product.configuration.product.name.title.label"></spring:message><span class='required'>*</span></div>
					<div class='input-group'>
					<select id="selProductType" class='form-control table-cell input-sm'  data-toggle='tooltip' data-placement='bottom' title='${tooltip }' onChange='loadDefaultProfileConfiguration();'>
						<c:forEach items="${activeServerTypeList}" var="serverType">
							<option value="${serverType.id}">${serverType.name}</option>
						</c:forEach>
			       </select>
		        </div>
		        </div>
				</div>
				
				 	 <div class="col-sm-12">
						<div class="button-set">
							<div  id="default_service_btn_set">
			            		<button class="btn btn-grey btn-xs "   tabindex="1"  onclick="expandTree('default_service_tree','default','div_default_service_tree');">
									<spring:message code="btn.label.expand.all"></spring:message>
								</button>
								<button class="btn btn-grey btn-xs " tabindex="2"  onclick="collapseTree('default_service_tree','default','div_default_service_tree');">
									<spring:message code="btn.label.collapse.all"></spring:message> 
								</button>
							</div>
						</div>
					</div>	
					 <div class="col-sm-12">
					 	 <div class="input-group">
							<div class="title2 " id="service_default_title" style="display:none;">
								<spring:message code="product.configuration.service.tree.label" ></spring:message>
							</div>
						</div>	
						
						
			        	 <div id="div_default_service_tree" style="height: 350px;overflow: auto;display:none;">
							 <div id="default_service_tree">
							 
							 </div> 
						</div>
					</div>
		        </div>
		        <div id="start-buttons-div" class="modal-footer padding10">
		            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		    </div>
		</div>
		<a href="#view_default_config_popup" class="fancybox" style="display: none;" id="view_default_link">#</a>
		
		<div id="reset_default_config_popup" style=" width:100%; display: none;" >
		     <div class="modal-content">
		        <div class="modal-header padding10">
		            <span class="modal-title">
		            	<spring:message code="product.configuration.reset.default.popup.header"></spring:message>
		            </span>
		        </div>
		        <div class="modal-body padding10 inline-form" style="min-height:40px;">
		        <input type="hidden" id="selectedProductTypeforReset" name="selectedProductTypeforReset" />
		        	 <div class="col-sm-12">
						<spring:message code="product.configuration.reset.default.note"></spring:message><br/><br/>
						<span class="note"><spring:message code="label.important.note"></spring:message></span><br/><br/>
						<b><spring:message code="product.configuration.reset.default.important.note"></spring:message></b>
					</div>	
					
		        </div>
		        <div id="start-buttons-div"  class="modal-footer padding10">
		        <sec:authorize access="hasAuthority('UPDATE_PRODUCT_CONFIGURATION')">
		        	<div id ="reset-configuration-buttons-div">
		        	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="resetConfigurationAction();">
		        		<spring:message code="btn.label.yes"></spring:message>
		        	</button>
		            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();">
		            
		            <spring:message code="btn.label.no"></spring:message>
		            </button>
		            </div>
		            <div id="reset-configuration-progress-bar-div" class="modal-footer padding10" style="display: none;">
							<jsp:include page="../common/processing-bar.jsp"></jsp:include>
					</div>
		            </sec:authorize>
		        </div>
		        
		    </div>
		</div>
			<a href="#reset_default_config_popup" class="fancybox" style="display: none;" id="reset_default_link">#</a>
		
		<form method='POST' id='profile_entity_form'>
			<input type='hidden' id="profileEntityList" name='profileEntityList'>
		</form>
<script type="text/javascript"> 

$(document).ready(function() {
	fetchCustomProfile();
});

function fetchCustomProfile(){
	
	var selectedServerTypeId=$("#selCustomProductType").find(":selected").val();
 	$("#service_list_tree").html('');
 	$("#div_service").html('<div id="service_list_tree"></div> ');
 	$("#reset_config_btn").prop("disabled", true);
	$("#update_config_btn").hide();
	$("#edit_config_btn").show();
 	generateJSTree(selectedServerTypeId,'service_list_tree','custom','<%= ControllerConstants.GET_CUSTOM_PRODUCT_CONFIGURATION %>');

}

function viewDefaultConfig(type){
	loadDefaultProfileConfiguration();
		
		$("#view_default_link").click();
	
		$("#service_default_title").show();
	
		$("#div_default_service_tree").show();
	
		$("#default_service_btn_set").show();
			
		
		jQuery('#div_default_service_tree .jstree a').click(function() {return false;});

	}
	
 function loadDefaultProfileConfiguration(){
 
 	var selectedServerTypeId=$("#selProductType").find(":selected").val();
 	$("#default_service_tree").html('');
 	$("#div_default_service_tree").html('<div id="default_service_tree"></div> ');
 	
 	generateJSTree(selectedServerTypeId,'default_service_tree','default','<%= ControllerConstants.GET_DEFAULT_PRODUCT_CONFIGURATION %>');
	
} 
 
	
function generateJSTree(selectedServerTypeId,treeId,type,action){
		
		$.ajax({
			url: action,
			cache: false,
			async: true,
			dataType: 'json',
			type: "POST",			 
			data: {
				selectedServerTypeId : selectedServerTypeId
			},
			success: function(data){
				createServicesConfigTree(data,treeId,type);
			}
		}); 
	}
	
function createServicesConfigTree(menuTabActionData,treeId,type){
		
		var pluginsArray = new Array() ;
		pluginsArray.push("checkbox");
				
		$('#'+treeId).jstree({
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
		isEnabled=false;
		expandTree(treeId,type);
		$('.jstree-anchor').addClass('jstree-anchor-view').removeClass('jstree-anchor');
		if(isEnabled==false){
		$('#service_list_tree').on('open_node.jstree', function( key, value ) {
			$('.jstree-anchor').addClass('jstree-anchor-view').removeClass('jstree-anchor');
	  
	});
		
		$('#service_list_tree').on('close_node.jstree', function( key, value ) {	
			$('.jstree-anchor').addClass('jstree-anchor-view').removeClass('jstree-anchor');
	  
	});
		}			
	}
	
	function makeCustomServiceTreeEnable(){
		
		$("#div_service .jstree-anchor-view").addClass('jstree-default jstree-anchor').removeClass('jstree-anchor-view');
		
		isEnabled=true;
		
		$('#service_list_tree').on('open_node.jstree', function( key, value ) {
			$('.jstree-anchor-view').addClass('jstree-anchor').removeClass('jstree-anchor-view');
		});
		
		$('#service_list_tree').on('close_node.jstree', function( key, value ) {	
			$('.jstree-anchor-view').addClass('jstree-anchor').removeClass('jstree-anchor-view');
		});
		
		$("#reset_config_btn").prop("disabled", false);
		$("#update_config_btn").show();
		$("#edit_config_btn").hide();
	}
	
	function makeCustomServiceTreeDisable(){
		$("#div_service .jstree-anchor").addClass('jstree-anchor-view').removeClass('jstree-anchor');
		
		isEnabled=false;
		
		$('#service_list_tree').on('open_node.jstree', function( key, value ) {
			$('.jstree-anchor').addClass('jstree-anchor-view').removeClass('jstree-anchor');
		});
		
		$('#service_list_tree').on('close_node.jstree', function( key, value ) {	
			$('.jstree-anchor').addClass('jstree-anchor-view').removeClass('jstree-anchor');
		});
	}
	
	function updateConfiguration(){
		
		showProgressBar("update-configuration");
		var selectedServerTypeId=$("#selCustomProductType").find(":selected").val();
	
		var selectedEntities= [];
		var entitySelected = $('#service_list_tree').jstree(true).get_selected();
		
		var isProfileEntitySelected = false;
		var index = 0;
		
		if(entitySelected == null || entitySelected == ''){
			
			showErrorMsg('<spring:message code="product.configuration.no.entity.selected"></spring:message>');
			hideProgressBar("update-configuration");
			
		}else{
			$.each( entitySelected, function( key, value ){
				if(value.indexOf("ENTITY_ALIAS_")>= 0){
					isProfileEntitySelected = true;
					var newvalue = value.replace("ENTITY_ALIAS_","");
					
					selectedEntities.push(newvalue);
					
					 
					index++
				}
			});
			
			$.ajax({
				url: '<%=ControllerConstants.UPDATE_PRODUCT_CONFIGURATION%>',
				cache: false,
				async: true, 
				dataType: 'json',
				type: "POST",
				data: {
					
						"serverTypeId"        					  :	   selectedServerTypeId,
						"profileEntityList"          			  :	   JSON.stringify(selectedEntities),
						
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
						$("#reset_config_btn").prop("disabled",true);
						//$("#div_service .jstree-anchor-view").addClass('jstree-default jstree-anchor').removeClass('jstree-anchor-view');
						makeCustomServiceTreeDisable();
						
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
	
	function resetToDefault(){
			$("#selectedProductTypeforReset").val($("#selCustomProductType").find(":selected").val());
			$("#reset_default_link").click();
		}
		
	function resetConfigurationAction(){
			
			showProgressBar("reset-configuration");
			var selectedServerTypeId=$("#selectedProductTypeforReset").val();
			
			$.ajax({
				url: '<%=ControllerConstants.RESET_TO_DEFAULT_PRODUCT_CONFIGURATION%>',
				cache: false,
				async: true, 
				dataType: 'json',
				type: "POST",
				data: {
					
						"selectedProductTypeforReset"        	  : 	 selectedServerTypeId,
						
				},
				success: function(data){
					hideProgressBar("reset-configuration");

					var response = eval(data);
					
					var responseCode = response.code; 
					var responseMsg = response.msg; 
					var responseObject = response.object;
					
					if(responseCode == "200"){
						
						$("#edit_config_btn").show();
						$("#update_config_btn").hide();
						$("#reset_config_btn").prop("disabled",true);
						$("#div_service .jstree-anchor-view").addClass('jstree-default jstree-anchor').removeClass('jstree-anchor-view');
						
						fetchCustomProfile();
						showSuccessMsg(responseMsg);
						closeFancyBox();
						
						
					}else{
						
						showErrorMsg(responseMsg);
						closeFancyBox();
					}
				},
			    error: function (xhr,st,err){
			    	hideProgressBar("update-configuration");
					handleGenericError(xhr,st,err);
				}

			});

			
		}

	
</script>
