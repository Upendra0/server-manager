<%@taglib 	uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib 	uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/multiple-select.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/multiple-select.css" type="text/css"/>

<style>
.ms-drop ul {
    height : 90px !important;
}
</style>

<script>
    $(function() {
        $('#DatabaseQueryList').change(function() {
            console.log($(this).val());
        }).multipleSelect({
            width: '70%',
            placeholder : 'Please select Database Query',
			filter: true
        });
    });
</script>

 <script type="text/javascript">
 	$(document).ready(function(){
 		manageReloadCache();	
 	});
 	
 	function manageReloadCache()
 	{
 		var cacheType = $('input[name=reloadType]:radio:checked').val();
 		if (cacheType == "static") {
 			$("#staticCacheMsg").show();
 			$("#dynamicCacheMsg").hide();
 			$("#dynamicDistCacheMsg").hide();
 		} else if (cacheType == "dynamic") {
 			$("#staticCacheMsg").hide();
 			$("#dynamicCacheMsg").show();
 			$("#dynamicDistCacheMsg").hide();
 		} else if (cacheType == "distDynamic") {
 			$("#staticCacheMsg").hide();
 			$("#dynamicCacheMsg").hide();
 			$("#dynamicDistCacheMsg").show();
 		}
 	}
 	
 	function resetReloadCacheOptions()
 	{
 		$("#reloadTypeStatic").prop('checked', 'checked');
 		$("#DatabaseQueryList").multipleSelect("uncheckAll");
 		clearResponseMsgDivPopUp();
 		manageReloadCache();
 	}
 </script>


 	<div id="divReloadCache" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="serverMgmt.reload.cache.popup.header"></spring:message></h4>
		        </div>
				 	
		        <div class="modal-body padding10 inline-form" style="height: 260px;overflow: auto;">
			        <jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
					<div class="col-md-12 inline-form" style="padding-left: 0px !important;">
						<div class="form-group">
							<spring:message code="serverMgmt.reload.cache.select.cache" var="tooltip" ></spring:message>
							<div class="table-cell" style="width:33%!important;">${tooltip }</div>
							<div class="table-cell" style="width:3%!important;text-align: left;">:</div>
							<div class="input-group ">
								<input type="radio" name="reloadType" id="reloadTypeStatic" style="padding-right: 15px;" value="static" checked="checked" onchange="manageReloadCache();"/>&nbsp;&nbsp;
								<spring:message code="serverMgmt.reload.cache.static.cache" ></spring:message>
								&nbsp;&nbsp;&nbsp;
								<input type="radio" name="reloadType" id="reloadTypeDynamic" style="padding-right: 15px;" value="dynamic" onchange="manageReloadCache();" />&nbsp;&nbsp;
								<spring:message code="serverMgmt.reload.cache.dynamic.cache" ></spring:message>
								&nbsp;&nbsp;&nbsp;
								<input type="radio" name="reloadType" id="reloadTypeDistDynamic" style="padding-right: 15px;" value="distDynamic" onchange="manageReloadCache();" />&nbsp;&nbsp;
								<spring:message code="serverMgmt.reload.cache.distribution.dynamic.cache" ></spring:message>
							</div>
						</div>
					</div>
	
					<p id="staticCacheMsg" style="display: none;">
						<spring:message code="serverMgmt.reload.cache.popup.static.cache.msg" ></spring:message>
					</p>
					<p id="dynamicDistCacheMsg" style="display: none;">
						<spring:message code="serverMgmt.reload.cache.popup.dynamic.distribution.cache.msg" ></spring:message>
					</p>
					
					<div id="dynamicCacheMsg" style="display: none;">
						<p>
							<spring:message code="serverMgmt.reload.cache.popup.dynamic.cache.msg" ></spring:message>
						</p>
					 
	        			<div class="col-md-12 no-padding">
							<spring:message code="serverMgmt.reload.cache.select.query" var="tooltip" ></spring:message>
							<div class="table-cell" style="width: 50%;float: left;">${tooltip} : </div>
							<div class="form-group"  style="width: 50%;float: left;">
								<select tabindex='9' id='DatabaseQueryList' data-toggle='tooltip'
									data-placement='bottom' title='${tooltip}' multiple="multiple">
									<c:forEach var="dbQuery" items="${databaseQueryList}">
										<option value='${dbQuery}'>${dbQuery}</option>
									</c:forEach>
								</select> 
							</div>
						</div>
					</div>
					
					<p id="stopNote" style="display: none;">	           	
			           	<span class="note"><spring:message code="label.important.note"></spring:message></span><br/><br/>
			        </p>
			        <p id="reload-cache-msgMismatch" style="display: none;">	  
						<strong><spring:message code="server.manager.and.server.detail.mismatch"></spring:message></strong>         	
			        </p>
			        <p> </p>
			        <p>
			        	<strong><spring:message code="serverManagement.popup.wish.continue.lbl"></spring:message></strong>
			        </p>
			        
		        </div>
		        
		        <div id="reload-cache-buttons-div" class="modal-footer padding10">
		        	<sec:authorize access="hasAuthority('SERVER_INSTANCE_RELOAD_CACHE')">
		        		<button type="button" id = "btn-yes_cache" class="btn btn-grey btn-xs " onclick="reloadCache();"><spring:message code="btn.label.yes"></spring:message></button>
		        		<button type="button" id="btn-no_cache" class="btn btn-grey btn-xs " onclick="closeFancyBox();resetReloadCacheOptions();"><spring:message code="btn.label.no"></spring:message></button>
		        	</sec:authorize>
		        </div>
		        <div id="reload-cache-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>	
		        
		    </div>
		    
		    <!-- /.modal-content --> 
		</div>
