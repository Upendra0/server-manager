<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<div class="modal-content">
					<div class="modal-header padding10">
						<h4 class="modal-title">
							<span id="delete_label" style="display: none;"><spring:message
									code="policymgmt.databaseQueries.delete" ></spring:message></span>
								</h4>
					</div>	
		<!-- Delete Popup code started -->	
 		<div id="divDeleteQueryPopup" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-body padding10 inline-form">
					<div class="fullwidth">
						<input type="hidden" value="" id="deleteQueryId"
							name="deleteQueryId" />
						<div id="delete_selected_query_details"></div>
						<div id="delete_warning" style="display: none;">
							<spring:message code="database.query.delete.warning" ></spring:message>
						</div>
					</div>
				</div>
					<div id="delete_attribute_bts_div" class="modal-footer padding10">
						<button type="button" class="btn btn-grey btn-xs" id="delete_database_query"
							onclick="deleteQuery();">
							<spring:message code="btn.label.delete" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs"
							onclick="closeFancyBox();reloadQueryGridData();">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</div>
					<div class="modal-footer padding10"
						id="delete_close_attribute_buttons_div" style="display: none;">
						<button type="button" class="btn btn-grey btn-xs "
							onclick="closeFancyBox();reloadQueryGridData();">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
					</div>
				
				<div id="delete_attribute_progress_bar_div"
					class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
			</div>
		</div>
		<!-- Delete attribute popup div end here -->
	</div>
