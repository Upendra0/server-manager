<%@page import="com.elitecore.sm.util.DateFormatter"%>
<%@page import="java.util.Date"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!-- Footer Code Starts Here -->
	<div class="footopborder">
		
       	<div class="padleft-right" style="border-top: 1px solid #EEEEEE;"> 
      		<div class="pull-right footer-rightlink" style="font-size:10px;"> <spring:message code="footer.bottom.company.copyright" ></spring:message> </div>
			<span>
            	<ul class="cust-icon">
                	<li><a target="_blank" href="<spring:message code="elitecore.web.url" ></spring:message>" class="foolefticon no-underline" style="color:#000;font-weight: normal;"><span style="padding-right: 5px;">
                 	<img src="<%=request.getContextPath()%>/img/sterlite_logo.png" width="80" height="24" />
                 	</span><%-- <spring:message code="footer.title.elitecore" ></spring:message> --%></a> </li>
             	</ul>
            </span>
		</div>
   	</div>
   	
	<script type="text/javascript">

		$( window ).resize(function() {
			toggleSidebar();
			initializeScrollBar();
		});
		
		$(document).ready(function() {
			if(showToggleSidebar){
				toggleSidebar();	
			}
			
			initializeScrollBar();
			initializeTooltip();
			if(showToggleSidebar){
				addErrorClassWhenTitleExist($("#GENERIC_ERROR_MESSAGE").val());
			}
			initializeFancyBox();
		});
		
		$(window).load(function(){
			try {
				$(":file").filestyle({icon: false});
				$.mCustomScrollbar.defaults.axis="yx"; //enable 2 axis scrollbars by default
				$.mCustomScrollbar.defaults.scrollButtons.enable=true; //enable scrolling buttons by default
				$("#content-scroll-d").mCustomScrollbar({theme:"dark"});
				$("#create-server-block").mCustomScrollbar({theme:"dark"});
			} catch(err) {
				console.log("Error : "+err);
			}
			$(".all-themes-switch a").click(function(e){
				e.preventDefault();
				var $this=$(this),
					rel=$this.attr("rel"),
					el=$(".content-scroll");
				switch(rel){
					case "toggle-content-scroll":
						el.toggleClass("expanded-content-scroll");
						break;
				}
			});
		});
		
		$(".nav-tabs li a").click(function(){
			resetWarningDisplay();
		});
		
		function resetSysParamDBValues(){
			
			if(${REQUEST_ACTION_TYPE eq 'EDIT_GEN_SYSTEM_PARAM'}){
				var count = '${generalTotalRowCount}';
				for ( var i = 0; i < count; i++) {
					document.getElementById("genParamListValue[" + i + "]").value =document.getElementById("systemParam["+i+"].hidden").value;
					document.getElementById("genParamListValue[" + i + "]").disabled =true;
				}
				$("#save-btn").hide();
				$("#reset-btn").hide();
				$("#cancel-btn").hide();
				$("#edit-btn").show(); 
				$("#genGroup-tab-a").html("<spring:message code='systemParameter.genGroup.tab' ></spring:message>");
				
			}else if(${REQUEST_ACTION_TYPE eq 'EDIT_PWD_SYSTEM_PARAM'}){
				
				document.getElementById("pwdTypeCombo").value =document.getElementById("systemParam[3].hidden").value;
				document.getElementById("pwdRegex").value =document.getElementById("systemParam[3].hidden").value;
				
				document.getElementById("pwdTypeCombo").disabled=true;
				document.getElementById("pwdRegex").disabled=true;
				
				var count = '${passwordTotalRowCount}';
				for ( var i = 0; i < 2; i++) {
					document.getElementById("pwdParamListValue[" + i + "]").value =document.getElementById("systemParam["+i+"].hidden").value;
					document.getElementById("pwdParamListValue[" + i + "]").disabled =true;
				}
				for ( var i = 4; i < count; i++) {
					document.getElementById("pwdParamListValue[" + i + "]").value =document.getElementById("systemParam["+i+"].hidden").value;
					document.getElementById("pwdParamListValue[" + i + "]").disabled =true;
				}
			
				$("#pwd-save-btn").hide();
				$("#pwd-reset-btn").hide();
				$("#pwd-cancel-btn").hide();
				$("#pwd-edit-btn").show(); 
				$("#pwdGroup-tab-a").html("<spring:message code='systemParameter.pwdGroup.tab' ></spring:message>");
				
			}else if(${REQUEST_ACTION_TYPE eq 'EDIT_CUST_SYSTEM_PARAM'}){
				
				var count = '${customerTotalRowCount}';
				for ( var i = 0; i < count; i++) {
					document.getElementById("custParamListValue[" + i + "]").value =document.getElementById("systemParam["+i+"].hidden").value;
					document.getElementById("custParamListValue[" + i + "]").disabled =true;
				}
				
				
				$("#cust-save-btn").hide();
				$("#cust-reset-btn").hide();
				$("#cust-cancel-btn").hide();
				$("#cust-edit-btn").show(); 
				$("#custGroup-tab-a").html("<spring:message code='systemParameter.custGroup.tab' ></spring:message>");
				$("#custLogofile").parent("div").hide();
				$("#custLargeLogofile").parent("div").hide();
			}else if(${REQUEST_ACTION_TYPE eq 'EDIT_CUST_LOGO_SYSTEM_PARAM'}){
				
				var count = '${customerLogoTotalRowCount}';
				for ( var i = 1; i < count; i++) {
					document.getElementById("custLogoParamListValue[" + i + "]").disabled =true;
				}
				
				$("#custLogofile").hide();
				$("#custLogofile").parent("div").hide();
				$("#custLargeLogofile").hide();
				$("#custLargeLogofile").parent("div").hide();
				$("#small-logo-upload").hide();
				$("#large-logo-upload").hide();
				$("#custLogo-edit-btn").show();
			} 
			document.getElementById("sysParamDBValue").innerHTML='';
		}
		
		function closeFancyBox (){
			parent.jQuery.fancybox.close();
		}
	</script>
	
	<jsp:include page="hiddenValues.jsp" ></jsp:include>
	
	<c:if test="${fn:contains(pageContext.request.servletPath, 'staffManager.jsp')}">
		<jsp:include page="../iam/accessGroup-changeStatusModal.jsp" ></jsp:include>
		<jsp:include page="../iam/staffMgmt-changeStatusModal.jsp" ></jsp:include>
	    <jsp:include page="../iam/staffMgmt-lockUnlockModal.jsp" ></jsp:include>
	</c:if>
	

<!-- Footer Code Ends Here -->
