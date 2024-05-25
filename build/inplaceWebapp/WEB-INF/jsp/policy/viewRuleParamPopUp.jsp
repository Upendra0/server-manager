 <%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
 <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="policymgmt.rule.grid.column.additionalparam"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
	        		<div id="viewRuleParamMsg" style="color:red;">
	        			<span class="title">
						</span>
					</div>
			        <p>
			        	
			        	<table style='width:100%'>
			        		<tr>
			        			<td width="100%">
									<div id="tblRuleParamList">
									</div>
			        			</td>
			        		</tr>
			        	</table>
			        </p>
			        </div>
		        <div id="delete-buttons-div" class="modal-footer padding10">
		            <button type="button" class="btn btn-grey btn-xs " id="btnViewRuleParamClose" data-dismiss="modal" onclick="closeFancyBox();clearSelection();"><spring:message code="btn.label.close"></spring:message></button>
		            
		        </div>
		    </div>
	
		    <!-- /.modal-content -->
		  
		  
		    
