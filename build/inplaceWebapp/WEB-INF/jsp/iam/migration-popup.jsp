<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<!--start version migration popup -->
<a href="#divVersionMigration" class="fancybox" style="display:none ;" id="version_migration_link">#</a>

 <div id="divVersionMigration" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="serverManagement.versionMigration.popup.heading"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
	    			<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
			     	<div class="modal-body padding10 inline-form">
		        		<p id="lessWarn">
					   <!--      <span class="successMessage"></span><br/><br/> -->
		        			<span class="proccessingMessage"><spring:message code="serverManagement.versionMigration.waiting.message"></spring:message></span>
		        		</p>
		        	</div>
		        </div>
		        <div id="migration-buttons-div" class="modal-footer padding10">
		            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="upgradeVersion()"><spring:message code="btn.label.yes"></spring:message></button>
            		<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
                </div>
		        <div id="migration-buttons-close-div" style="display: none" class="modal-footer padding10">
		    		<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();">Close</button>    
		        </div>
		        <div id="migration-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>	
		    </div>
		    <!-- /.modal-content --> 
		</div>

 <script type="text/javascript">			
	$(document).ready(function() {
		<% if(session.getAttribute("userName").equals("admin") &&  Boolean.FALSE.toString().equals((String)MapCache.getConfigValueAsObject(SystemParametersConstant.VERSION_MIGRATE))){  %> 
			$("#version_migration_link").click();	
			upgradeVersion();
		<%} %> 
	});
	
	
	function upgradeVersion(){		
		$("#migration-buttons-div").hide();
		$("#migration-progress-bar-div").show();
		clearAllMessages();
		$.ajax({
			url: '<%= ControllerConstants.MIGRATEALLSERVERCONFIG %>',
			    cache: false,
				async: true,
				dataType: 'json',
				type: "GET",
				success: function(data){
					
					$("#migration-buttons-close-div").show();
					$("#migration-progress-bar-div").hide();					
					
					
					var response = eval(data);
			    	response.msg = decodeMessage(response.msg);
			    	response.msg = replaceAll("+"," ",response.msg);
			    	/* if(response.success ==='true') { */
			    	$('.proccessingMessage').html('');
			    	$('.proccessingMessage').html("Configuration are loaded successfully.");
			    	closeFancyBox();
					/* } */
				},
			    error: function (xhr,st,err){
			    	$("#migration-buttons-div").show();
					$("#migration-progress-bar-div").hide();
			    	handleGenericError(xhr,st,err);
				}
			});
	}
</script>	
<!--End version migration popup -->
