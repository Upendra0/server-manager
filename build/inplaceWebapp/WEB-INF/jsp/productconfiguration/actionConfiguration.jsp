<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form:form modelAttribute="access_group_form_bean" method="POST" action="#" id="temp_form_tree_action">
<div class="fullwidth mtop10" >
	
	<div class="fullwidth">
		<div class="title2">
			<label> <spring:message code="product.configuration.product.name.title.label"></spring:message> |&nbsp;<spring:message code="product.configuration.product.name"></spring:message></label>
			
			<span class="title2rightfield">
				<span class="title2rightfield-icon1-text">
					<a href="javascript:;" onclick="viewDefaultConfig('action');" style="cursor: pointer; cursor: hand;text-decoration: underline;">
					<spring:message code="product.configuration.default.link.label" ></spring:message>
					</a>
				</span>
			</span>
			<a href="CREATE_CUSTOM_PROFILE">Apply License</a>
	     </div>
	</div>
	<div class="box-body table-responsive no-padding box">
		<div class="col-sm-12 padding10 ">
			<div class="button-set">
            		<button type="button" class="btn btn-grey btn-xs" tabindex="1" onclick="expandTree('action_list_tree','action');">
						<spring:message code="btn.label.expand.all"></spring:message>
					</button>
					<button type="button" class="btn btn-grey btn-xs" tabindex="2"  onclick="collapseTree('action_list_tree','action');">
						<spring:message code="btn.label.collapse.all"></spring:message> 
					</button>
			</div>
		</div>

		<div class="col-sm-12 padding10 " >
			  <div class="input-group">
				<div class="title2 ">
					<spring:message code="addAccessGroup-access.group.acl.tree.title" ></spring:message>
				</div>
			</div>	
			  <div id="div_service" style="height: 400px;overflow: auto;" >
				<div id="action_list_tree"></div>
			  </div>
		</div>
	</div>
</div>
</form:form>
