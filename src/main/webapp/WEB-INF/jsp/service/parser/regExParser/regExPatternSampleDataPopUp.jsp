<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" prefix="sec"  %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>		    
	<div class="modal-content">
			
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code='regex.parser.attr.grid.header' ></spring:message>  
				</h4>
			</div>
				
			<div id="regExAttributeContentDiv" class="modal-body padding10 inline-form">
				<div id="sampleDataMsg">
					<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
					</div>
					<input type="hidden" id="patternresponseObject" name="patternresponseObject"/>
					<input type="hidden" id="patternListCounterPopUp" name="patternListCounterPopUp" />
					<input type="hidden" id="mode" name="mode" />
					<div class="table-responsive " style="height:auto;max-height:300px;overflow: auto;">
					<table class="table table-hover table-bordered " id="tblPatternAttributeList">
					</table>
					
					</div>

			</div>
			
					
				<div id="buttons-div" class="modal-footer padding10 text-left">
			
					<button type="button" id="add-token-select" class="btn btn-grey btn-xs " onclick="addTokenToGrid();">
						<spring:message code="btn.label.select" ></spring:message>
					</button>
			
					<button onclick="javascript:parent.closeFancyBoxFromChildIFrame()" class="btn btn-grey btn-xs ">
						<spring:message code="btn.label.cancel" ></spring:message>
					</button>
				</div>
			
			
	</div>
