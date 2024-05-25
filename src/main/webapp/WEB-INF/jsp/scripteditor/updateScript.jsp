<%@page import="com.elitecore.sm.common.constants.LicenseConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<%@page import="com.elitecore.sm.scripteditor.constants.ScriptControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<script>
	var updateAccessStatus = false;
	var deleteAccessStatus = false;
	var ckIntanceSelected = new Array();
	var currentTab = '${REQUEST_ACTION_TYPE}';
	
	
</script>



<jsp:include page="../common/newheader.jsp"></jsp:include>

<body class="skin-blue sidebar-mini sidebar-collapse">

<c:set var="fileId" value="${param.fileId}"></c:set>
	<div>

		<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
		<jsp:include page="../common/newleftMenu.jsp"></jsp:include>

		<div class="tab-content padding0 clearfix" id="server-manager-block">
		<div class="title2">
			<spring:message code="script.page.heading" ></spring:message>
		</div>
       </div>
       
       
       <div class="content-wrapper">
		<%-- 	<section class="content-header"> --%>
				<div class="fullwidth">
					<div style="padding: 0 10px 0 4px;">
						<div id="content-scroll-d" class="content-scroll">
							<div class="fullwidth">
								<h4
									style="margin-top: 10px; margin-bottom: 0px; font-size: 11px;">
									<a href="home.html"><img src="img/tile-icon.png"
										class="vmiddle" alt="Home" /></a> <span class="spanBreadCrumb" style="line-height: 30px;"> <strong>&nbsp; 
									<a href="<%= ScriptControllerConstants.INIT_SCRIPT_MANAGER %>"> 
										 <spring:message code="leftmenu.label.script.manager" ></spring:message>
										   </a>&nbsp;/&nbsp;	
										   <a href="#">
												 <spring:message code="leftmenu.label.update.script" ></spring:message>
											 </a>	  
									</span>
								</h4>
								<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
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
								
								 <div class="form-group">
	             	<spring:message code="script.editor" var="label" ></spring:message>
	         		<div style="content:'';display:table;clear:both;width:100%;">
	         		 <div style="float:left; margin-top: 10px;">${FILENAME_CONTENT_MAP.FILENAME}</div> 
	         		   <c:choose>
	         		   <c:when test="${FILENAME_CONTENT_MAP.SCRIPT_STATUS}">
	         		  <c:set var="statusImg" value="bullet_green.png"></c:set>
	         		  </c:when>
	         		  <c:otherwise> 
	         		  <c:set var="statusImg" value="bullet_red.png"></c:set>
	         		  </c:otherwise>
	         		  </c:choose>
	         		  <div style="float:right;">Execution&nbsp;Status:&nbsp;<img id="statusImage" alt="Not in Running State" src="img/${statusImg}">
	         		  </div>
	         		  </div>
	         		 <c:set var="serverAlias" scope="request" value="${FILENAME_CONTENT_MAP.SERVERALIAS}"></c:set>
	         		 <c:set var="textarea_editable" scope="request" value="${FILENAME_CONTENT_MAP.IS_EDITABLE}"></c:set>
	             	<div class="form-group">
	             	<textarea
	                <c:if test="${textarea_editable eq false}">
				     disabled 
	         		 </c:if>
	             	 rows="15" cols="70"  id="update-script-port" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom">${FILENAME_CONTENT_MAP.FILECONTENT} </textarea>
	             	</div>
	             </div>
	             
	             
	             <c:set var="update_button" scope="request" value="${FILENAME_CONTENT_MAP.IS_EDITABLE}"></c:set>
	             <c:set var="execute_button" scope="request" value="${FILENAME_CONTENT_MAP.IS_EXECUTABLE}"></c:set>
	             <div class="pbottom15">
   					<button 
					<c:if test="${update_button eq false}">
					 <c:out value='disabled'></c:out>
	         		 </c:if>
   					 id="updateScriptBtn" class="btn btn-grey btn-xs" style="align:center" onclick="updateScript();"  ><spring:message code="btn.label.update.change" ></spring:message></button>
   					 
    				<button
					<c:if test="${(execute_button eq false) || (FILENAME_CONTENT_MAP.SCRIPT_STATUS eq true)}">
					<c:out value='disabled'></c:out>
	         	     </c:if>
    				     id="executeScriptBtn" class="btn btn-grey btn-xs" style="align:center" onclick="executeScript();"><spring:message code="btn.label.execute" ></spring:message></button>
    				
    				<button id="viewLogBtn" class="btn btn-grey btn-xs" style="align:center" onclick="viewLog();"><spring:message code="btn.label.log" ></spring:message></button>
    		<label id="execution_progress_status"></label>
   			</div>
   			 <div class="form-group">
	         		<div class="table-cell-label"><spring:message code="script.editor.output" var="label" ></spring:message></div>
	             	<textarea rows="15" cols="70" id="output-textarea" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom"  readonly>
	             	 </textarea>
	             </div>
   		</div>
			                </div>
						</div>
					</div>
				</div>
			<%-- </section> --%>
		</div>
    
       <footer class="main-footer positfix">
			<div class="fooinn">
				<div class="fullwidth">
					<div class="button-set">
						<div class="padleft-right" id="serverListDiv"
							style="display: none;">
		
						</div>

						<div class="padleft-right" id="createServerDiv"
							style="display: none;"></div>
					</div>
				</div>

				<jsp:include page="../common/newfooter.jsp"></jsp:include>
			</div>
		</footer>
		
		<script type="text/javascript">

		function viewLog(){
			
			$.ajax({
			    url:'<%= ScriptControllerConstants.DOWNLOAD_FILE %>',
			    method:'post',
			    dataType: "text",
			    data: { "fileId" : ${fileId}
		  	       },
			    success: function(response, status, xhr) {
			        console.log("In success of file download.");
			        if(response!=""){
			            // check for a filename
			            var filename = "";
			            var disposition = xhr.getResponseHeader('Content-Disposition');
			            if (disposition && disposition.indexOf('attachment') !== -1) {
			                var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
			                var matches = filenameRegex.exec(disposition);
			                if (matches != null && matches[1]) filename = matches[1].replace(/['"]/g, '');
			            }

			            var type = xhr.getResponseHeader('Content-Type');
			            console.log("Content-type :" + type);
			            var blob = new Blob([response], { type: type });

			            if (typeof window.navigator.msSaveBlob !== 'undefined') {
			                // IE workaround for "HTML7007: One or more blob URLs were revoked by closing the blob for which they were created. These URLs will no longer resolve as the data backing the URL has been freed."
			                window.navigator.msSaveBlob(blob, filename);
			            } else {
			                var URL = window.URL || window.webkitURL;
			                var downloadUrl = URL.createObjectURL(blob);

			                if(filename == "null"){
			                	
			                	 alert("Log is not available.");
			                }
			                else if (filename) {
			                    // use HTML5 a[download] attribute to specify filename
			                    var a = document.createElement("a");
			                    // safari doesn't support this yet
			                    if (typeof a.download === 'undefined') {
			                        window.location = downloadUrl;
			                    } else {
			                        a.href = downloadUrl;
			                        a.download = filename;
			                        document.body.appendChild(a);
			                        a.click();
			                    }
			                } else {
			                    window.location = downloadUrl;
			                }

			                setTimeout(function () { URL.revokeObjectURL(downloadUrl); }, 100); // cleanup
			            }
			        } else {
			            alert("file is not available");
			        }
			    },
			    error:function(){
			        alert("error occure here...");
			    }
			});
			
		 }
		
		
		 function executeScript(){
			 
			 $("#executeScriptBtn").attr("disabled", true);
			 $('#execution_progress_status').text('Please wait...');
			 $('#statusImage').attr("src","img/bullet_green.png");
			 
			$.ajax({
				method :"POST",
 			  	url :"<%= ScriptControllerConstants.EXECUTE_FILE %>",
 			  	data: { "fileId" : ${fileId},
 			  	 		"fileName" : "${FILENAME_CONTENT_MAP.FILENAME}",
           	  			"serverAlias" : "${FILENAME_CONTENT_MAP.SERVERALIAS}"
 			  	       },
 			  	       
              	dataType : 'json',
				timeout : 100000,
				success : function(responseObject) {
				
					$('#output-textarea').val(responseObject.object);
					 $("#executeScriptBtn").attr("disabled", false);
					 $('#execution_progress_status').text('');
					 $('#statusImage').attr("src","img/bullet_red.png");
				},
				error : function(e) {
					console.log("ERROR: ", e);
				}
			}); 
			
		 }
		
		
		function updateScript(){
	
		 	$.ajax({
				type :"POST",
 			  	url :"<%= ScriptControllerConstants.UPDATE_FILE %>",
              	data : {
            	  "fileId" : ${fileId},
            	  "fileContent" : $("#update-script-port").val(),
            	  "fileName" : "${FILENAME_CONTENT_MAP.FILENAME}",
            	  "serverAlias" : "${FILENAME_CONTENT_MAP.SERVERALIAS}"
				},
				dataType : 'json',
				timeout : 100000,
				success : function(responseObject){
				
					if(responseObject.object){
						
						$('#output-textarea').val('Update operation performed successfully!');
					}
				},
				error : function(e) {
					console.log("ERROR: ", e);
				}
			}); 
		}
		 
		
		function downloadLog(){
			
			$.ajax({
				type :"POST",
 			  	url :"<%= ScriptControllerConstants.DOWNLOAD_FILE %>",
              	data : {
            	  "fileId" : ${fileId}
				},
				dataType : 'json',
				timeout : 100000,
				success : function(responseObject){
				
				},
				error : function(e) {
					console.log("ERROR: ", e);
				}
			}); 
		}
    	

</script>
		
       </body>
      
</html>
