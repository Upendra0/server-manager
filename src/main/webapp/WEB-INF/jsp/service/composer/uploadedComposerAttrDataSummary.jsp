<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<script src="${pageContext.request.contextPath}/customJS/composerManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title">Upload Composer Attribute Status</h4>
		        </div>
			<div class="col-md-12"
							id="divImporttotalNoRecords">
							<div class="box-body inline-form accordion-body collapse in"
								style="padding: 0px;">
								<!-- <div class="col-md-6 no-padding" style="height: 45px"> -->
								
								<div class="form-group">													
			                          	<div class="input-group">
				                          	<div class="fullwidth" style="margin-top:7px;">				                          	
				                          		<label  id="TotalRecord" style="color: red;" ></label></div>
				                          		<label  id="ObjectMessage" style="color: red;" ></label></div>
				                          		<div class="table-cell-label">
				             		 		</div>			             				 
			                          	</div>
			                         </div> 	
								<!-- </div> -->
							</div>
			</div> 
			
			<div class="col-md-12"
							id="divImportValidRecords">
							<div class="box-body inline-form accordion-body collapse in"
								style="padding: 0px;">
							<!-- 	<div class="col-md-6 no-padding" style="height: 45px"> -->
								<div class="form-group">							
			                          	<div class="input-group">
				                          	<div class="fullwidth" style="margin-top:7px;">
				                          	    <label  id="ValidRecord" ></label>
				                          		<div class="table-cell-label"></div>	
				             		 		</div>			             				 
			                          	</div>
			                         </div> 	
								<!-- </div> -->
							</div>
			</div> 
	        
     <div id="closeDiv" class="modal-footer padding10">
         <button type="button" class="btn btn-grey btn-xs " id="uploadedSummaryClose" data-dismiss="modal" onclick="closeFancyBox();">
         	<spring:message code="btn.label.close"></spring:message>
         </button>
     </div>
</div>

<script type="text/javascript">
function setSummaryData( code,messsge,objectData){
	$("#ObjectMessage").text('');
	var ValidRecord = "";
	var TotalRecord = "";
		if(code == 200){
		$("#divImporttotalNoRecords").hide();
		$("#ValidRecord").text(messsge);
		$("#divImportValidRecords").show();
		$("#slashUpdate").hide();
	} else if (code == 400) {
        if(objectData !== undefined || objectData !== null){
        	$("#ObjectMessage").text(objectData);
        }
  		$("#TotalRecord").text(messsge);
		$("#divImporttotalNoRecords").show();
		$("#divImportValidRecords").hide();
	} 
}
</script>
