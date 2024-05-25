<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page contentType="text/html; charset=UTF-8" %>

	 <a href="#divPartnerList" class="fancybox" style="display: none;" id="partnerList"></a> 
	 <div id="divPartnerList" style=" width:100%; display:none;" > 
		    <div class="modal-content" style="overflow:hidden">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<i class="icon-hdd"></i> 
						<spring:message code="partner.tab.heading.label" ></spring:message>
					</h4>
				</div>
				<div id="partnerContentDiv" class="modal-body" >
					
<div >
					<label id="lblCounter" style="display:none;"></label>
					<label id="lblPartnerid" style="display:none;"></label>
					<span class="title2rightfield">
					 	<span class="title2rightfield-icon1-text" style="font-size: 12px;"> 
					 		<!-- <a href="#" onclick="createPolicy();"  ><spring:message code="business.policy.create.label" ></spring:message></a>  --> 
						</span>
					</span>
				</div>
					<div class="fullwidth table-responsive " style="height:auto;max-height:300px;overflow: auto;">
					<table class="table table-hover" id="partnerlistgrid"></table>
					<div id="partnerGridPagingDiv"></div>
					</div>
				
			</div>
			<div id="buttons-div" class="modal-footer padding10 text-left">
					<div id="buttons-div" class="modal-footer padding10 text-left">
					<button type="button" class="btn btn-grey btn-xs " id ="selectPartnerbtn" onclick="onSelectPartner();">
						<spring:message code="btn.label.select" ></spring:message>
					</button>
					<button onClick="closeFancyBoxFromChildIFrame();" class="btn btn-grey btn-xs ">
						<spring:message code="btn.label.cancel" ></spring:message>
					</button>
				</div>
				</div>
			</div>
			<!-- /.modal-content -->
	</div>
	
	

	</script>
