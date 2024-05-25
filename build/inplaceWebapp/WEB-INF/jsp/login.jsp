<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title><spring:message code="header.server.manager.title" ></spring:message></title>
	<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
	
	<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">
	<!--  	Bootstrap 3.3.4  -->
	<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<!-- 	Font Awesome Icons  -->
	<link href="css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	<!-- 	 Theme style  -->
	<link href="css/styles.css" rel="stylesheet" type="text/css" />
	
	<link href="css/theme-color.css" rel="stylesheet">
	<link href="css/theme-font.css" rel="stylesheet">
	<link href="css/custome.css" rel="stylesheet">
	
	<!-- REQUIRED JS SCRIPTS --> 
	
	<!-- jQuery 3.5.1 --> 
	<script src="jquery/3.5.1/jquery.min.js"></script> 
	<script src="js/jquery-migrate-3.4.0.min.js"></script> 
	
	<!-- Bootstrap 3.3.2 JS --> 
	<script src="js/bootstrap.min.js" type="text/javascript"></script> 
	<!-- AdminLTE App --> 
	<script src="js/app.min.js" type="text/javascript"></script> 
	<!--login page css-->
	<link href="css/login.css" rel="stylesheet" type="text/css" />
	
	
	<!--scroll bar css and js-->
	<link rel="stylesheet" href="css/jquery.mCustomScrollbar.css">
	<script src="js/jquery.mCustomScrollbar.concat.min.js"></script> 
	
	<script src="js/jquery.blockUI.js" type="text/javascript"></script> 
<script src="${pageContext.request.contextPath}/customJS/footer.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>

</head>
<style>
	.left{float: left;}
	.language-header{
		color: #FFFFFF;
	    display: inline-block;
	    margin-bottom: 5px;
	    margin-top: 12px;
	    position: relative;
	    float: left;
	}
	/* .col-lg-offset-2 {
	    margin-left: -5.333%;
	} */
</style>

<script>
function showMsg(message) 
{ 
	var topPixel = "11%";
	
	if ($(window).width() < 767) {
		topPixel="17%";
	}
	
  	$.blockUI({ 
       message: '<h5>'+message+'</h5>', 
       fadeIn: 700, 
       fadeOut: 700, 
       timeout: 10000,
       showOverlay: false, 
       centerY: true, 
       css: { 
           width: 'auto',
           weight:'bold',
           top: topPixel, 
           left: '', 
           right: '10px', 
           border: 'none', 
           padding: '2px 5px 2px 5px', 
           backgroundColor: '#000', 
           '-webkit-border-radius': '10px', 
           '-moz-border-radius': '10px', 
           opacity: .6, 
           color: ' #FE7B26' 
       } 
   	}); 
} 
</script>

<%-- <jsp:include page="common/newheader.jsp" ></jsp:include> --%>
<body class="login-page">

	 <sec:authorize access="isAuthenticated()">
        	<script type="text/javascript">
        		window.location.href="home";
        	</script>
     </sec:authorize>

<!-- Header Start -->
<div class="fullwidth pull-left login-header">
  <div class="fullwidth pull-left logobox logocenter">
    <div class="login-logo" style="float:left;"> <a href="#"><img src="img/logo-1.png"></a> </div>
  </div>
  
  <form role="form" id="loginForm" action="<c:url value="/j_spring_security_check" ></c:url>" method="post">
    <div class="col-xs-12 col-sm-8 col-lg-6 col-lg-offset-1 login-details">
      <div class="col-xs-6 col-sm-5 col-md-5 col-lg-5 fieldpad">

        <div class="form-group" id="divUsername">
        	<spring:message code="index.page.username" var="tooltip"></spring:message>
        	<div class="input-group ">
              <input type="text" class="form-control" autocomplete="off" data-toggle="tooltip" id="username_txt" name="username" autofocus="autofocus" data-placement="bottom" title="<spring:message code="index.page.username.tooltip.lable" ></spring:message>" />
              <span class="input-group-addon add-on last" > 
              	<i id="username_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="index.page.username" ></spring:message>">
              	</i>
              </span>
          </div>
        </div>
      </div>
      <div class="col-xs-6 col-sm-5 col-md-5 col-lg-5 col-lg-offset-3 fieldpad">
      	<div class="form-group" id="divPassword">
        	<div class="input-group ">
              <input type="password" class="form-control" autocomplete="off" data-toggle="tooltip" id="password_txt" name="password" data-placement="bottom" title="<spring:message code="index.page.password.tooltip.lable" ></spring:message>" />
              <span class="input-group-addon add-on last" >
              	<i id="password_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="index.page.password" ></spring:message>">
              	</i>
              </span>
          </div>
        </div>
      </div>
      <div class="lastlogbut col-md-2 col-lg-offset-2 fieldpad">
      	<div class="form-group">
      		<div class="input-group error">
	        	<input id="login_btn" type="submit" onclick="return validateLoginForm();" class="btn btn-primary btn-block btn-flat" value="<spring:message code="btn.label.login"></spring:message>">
	        	
         		<c:if test="${not empty error}">
         			<spring:message code="index.page.username.password.mandatory" var="username_password_error" ></spring:message>
         			<spring:message code="index.page.password.mandatory" var="password_error" ></spring:message>
         			<spring:message code="index.page.username.mandatory" var="username_error" ></spring:message>
         			
         			<c:choose>
					    <c:when test="${error == username_error}">
					    	<script>
					       	$("#divUsername").addClass('error');
         					$("#divUsername span i").attr('title','${error}');
         					</script>
					    </c:when>
					    <c:when test="${error == password_error}">
					    	<script>
					        $("#divPassword").addClass('error');
         					$("#divPassword span i").attr('title','${error}');
         					</script>
					    </c:when>
					    <c:when test="${error == username_password_error}">
					    	<script>
					    	$("#divUsername").addClass('error');
         					$("#divUsername span i").attr('title','${username_err}');
					        $("#divPassword").addClass('error');
         					$("#divPassword span i").attr('title','${password_err}');
         					</script>
					    </c:when>
					    <c:otherwise>
					    	<span class="input-group-addon add-on last">
								<i id="login_err" class="glyphicon glyphicon-alert" title="" data-placement="bottom" data-toggle="tooltip" data-original-title="${error}"></i>
							</span>
					    </c:otherwise>
					</c:choose>	
				</c:if>
				<c:if test="${not empty ERROR_MSG}">
					<span class="input-group-addon add-on last">
						<i id="login_err" class="glyphicon glyphicon-alert" title="" data-placement="bottom" data-toggle="tooltip" data-original-title="${ERROR_MSG}"></i>
					</span>
				</c:if>
				<c:if test="${not empty RESPONSE_MSG}">
					<script>
						showMsg("${RESPONSE_MSG}");
					</script>
	           	</c:if>  
	        </div>
         </div>
      </div>
    </div>
    
    <div class="language-header" >
    	
    <c:if test="${languagePropsList != null && fn:length(languagePropsList) gt 0}">
	    	<select class="form-control table-cell input-sm" id="language_combo" onchange="changeLanguage();">
		    	<c:forEach var="propList" items="${languagePropsList}">
				  	<c:if test="${(propList.key ne 'default.lang')}">
		  				<c:choose>
				        	  <c:when test="${propList.key eq currentLocale}">
							    	<option value="${propList.key}" selected="selected">${propList.value}</option>
							  </c:when>
							  <c:otherwise>
							 	 	<option value="${propList.key}">${propList.value}</option>
							  </c:otherwise>
				    	</c:choose>
				  	</c:if>
				</c:forEach>
	    	</select> 
	    	<a id="selectedLang" href="" style="display: none;">#</a>
   	 </c:if>
    </div>
    <div class="loginright">
      <div class="checkbox icheck">
        <label>
          <input type="checkbox" class="left" name="remember" id="remember_cbx"><spring:message code="index.page.label.remember.me" ></spring:message>
          </label>
      </div>
      <div style="padding-left: 25px;">
      	<a id=forgotpassword_lnk href="<%= ControllerConstants.INIT_FORGOT_PASSWORD %>" class="forget_p"><spring:message code="index.page.forgot.password.link" htmlEscape="false" ></spring:message></a>
      </div>
      <!-- /.col --> 
    </div>
  </form>
</div>
<!-- Header End -->

<!-- Contains page Start -->
<div class="content"> </div>
<!-- Contains page End -->

<!-- Form to change the languages -->
 <form action="<%= ControllerConstants.CHANGE_LANGUAGE %>" id="form-language" method="POST">
  	<input type="hidden" value="" name="currentLang" id="currentLang">
  </form>

<!-- Footer Start -->
<footer class="foo-positfix"> 
  <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
    <div class="col-xs-12 col-sm-5 col-md-4 col-lg-4 text-left foo-left-left">
      <ul class="cust-icon">
       <li><a target="_blank" href="<spring:message code="elitecore.web.url" ></spring:message>" class="no-underline"><span><img src="<%=request.getContextPath()%>/img/sterlite_logo.png" width="80" height="24" /></span><%-- <spring:message code="footer.title.elitecore" ></spring:message> --%></a> </li>
      </ul>
    </div>
    <div class="col-xs-12 col-sm-3 col-md-4 col-lg-4 text-center copyright foo-cent-copy"><spring:message code="footer.bottom.company.copyright" ></spring:message>  </div>
    <div class="col-xs-12 col-sm-4 col-md-4 col-lg-4 text-right foo-right-icon">
      <ul class="social-icon">
        <li><a target="_blank" href="<spring:message code="elitecore.facebook.web.url" ></spring:message>"><i class="fa fa-fw fa-facebook"></i></a></li>
<!--         <li><a href="#"><i class="fa fa-fw fa-google-plus"></i></a></li> -->
        <li><a target="_blank" href="<spring:message code="elitecore.linkedin.web.url" ></spring:message>"><i class="fa fa-fw fa-linkedin"></i></a></li>
        <li><a target="_blank" href="<spring:message code="elitecore.twitter.web.url" ></spring:message>"><i class="fa fa-fw fa-twitter"></i></a></li>
        <li><a target="_blank" href="<spring:message code="elitecore.rss.web.url" ></spring:message>"><i class="fa fa-fw fa-rss"></i></a></li>
      </ul>
    </div>
    
  </div>
</footer>
 <!-- Footer End --> 
        
    
<script>
		(function($){
			
			var hederHeight = $(".main-header").outerHeight();
					var footerHeight = $(".main-footer").outerHeight();
					$('#content-scroll-d').css({'height':(($(window).height())-footerHeight-hederHeight-10)+'px'});
				
					$(window).resize(function(){
						var hederHeight = $(".main-header").outerHeight();
						var footerHeight = $(".main-footer").outerHeight();
						$('#content-scroll-d').css({'height':(($(window).height())-footerHeight-hederHeight-10)+'px'});
						
					});
			$(window).load(function(){
			
				try{
					$.mCustomScrollbar.defaults.scrollButtons.enable=true; //enable scrolling buttons by default
					$.mCustomScrollbar.defaults.axis="yx"; //enable 2 axis scrollbars by default
				} catch (err) {
					console.log("Error : "+err);
				}
				$("#content-scroll-d").mCustomScrollbar({theme:"dark"});
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
		})(jQuery);
		
		
</script>
<script type="text/javascript">
			

	        $("#password").keypress(function(e) {
            	// Enter pressed?
            	if(e.which == 10 || e.which == 13) {
            		validateLoginForm();
            	}
        	});
	        
        	function validateLoginForm(){
        		$("#username_err").attr('data-original-title','');
        		$("#username_err").parent().parent().parent().removeClass('error');

        		$("#password_err").attr('data-original-title','');
        		$("#password_err").parent().parent().parent().removeClass('error');
        		
        		$("#login_err").parent().parent().removeClass('error');
        		$("#login_err").attr('data-original-title','');
        		
         		var isValid = true;
        		if($("#username_txt").val() == ''){
        			$("#username_txt").blur();
        			addErrorMsgToInputTag('username_txt' , "<spring:message code='index.page.username.mandatory' ></spring:message>");
					isValid = false;
        		}
        		if($("#password_txt").val() == ''){
        			if(isValid){
	        			$("#password_txt").blur();
        			}
        			addErrorMsgToInputTag('password_txt' , "<spring:message code='index.page.password.mandatory' ></spring:message>");
        			isValid = false;
        		}

        		if(isValid){
					var username = $("#username_txt").val();
	        		username = username;
	        		$("#username_txt").val(username);
	        		//processRememberMe();
	        		$('#loginForm').submit();
        		}else{
        			addErrorClassWhenTitleExist('');
        			return false;
        		}
        	}
        	
        	function processRememberMe(){
        		if($('#remember_cbx').is(':checked')){
        			setCookie('sm-username',$("#username_txt").val(),365);
        			setCookie('sm-password',$("#password_txt").val(),365);
        		}else{
        			setCookie('sm-username','',0);
        			setCookie('sm-password','',0);
        		}
        	}
        	
        	function checkIfCredentialsInCookieExists(){
        		if(getCookie('sm-username') != ""){
        			$("#username_txt").val(getCookie('sm-username'));
        			$('#remember_cbx').prop('checked',true);
        		}
        		
        		if(getCookie('sm-password') != ""){
        			$("#password_txt").val(getCookie('sm-password'));
        			$('#remember_cbx').prop('checked',true);
        		}
        	}
        	
        	$(document).ready(function() {
        		//checkIfCredentialsInCookieExists();
        	});
        </script>
        
		<%
        	session.removeAttribute(BaseConstants.VERIFIED_USERNAME_FOR_FORGOT_PASSWORD);
        %>
        
         <script type="text/javascript">
	        function replaceAll(find, replace, str) 
	        {
	        	while( str.indexOf(find) > -1)
	        	{
	        		str = str.replace(find, replace);
	        	}
	        	return str;
	        }
	
	        function decodeMessage(value){
	        	var tempValue = decodeURIComponent(value);
	        	tempValue = replaceAll("+"," ",tempValue);
	        	return tempValue;	
	        }
	        
	        function handleGenericError(xhr,st,err){
	        	if(xhr.responseText == 'SESSION_TIMEOUT'){
			    	window.location.href = 'login-fail?error-timeout' ;
			    }else if(xhr.responseText == 'ACCESS_DENIED'){
			    	window.location.href = 'error403' ;
			    }
	        }
	        
	        function setCookie(cname, cvalue, exdays) {
				var d = new Date();
				d.setTime(d.getTime() + (exdays*24*60*60*1000));
				var expires = "expires="+d.toUTCString();
				document.cookie = cname + "=" + cvalue + "; " + expires;
	        }
	        
			function getCookie(cname) {
	            var name = cname + "=";
	            var ca = document.cookie.split(';');
	            for(var i=0; i<ca.length; i++) {
	                var c = ca[i];
	                while (c.charAt(0)==' ') c = c.substring(1);
	                if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
	            }
	            return "";
	        }
			
			
			function changeLanguage(){
        	   $("#currentLang").val($("#language_combo").val());
  			   $("#form-language").submit();
        	}
        </script>
        
        <%
        	session.removeAttribute(BaseConstants.VERIFIED_USERNAME_FOR_FORGOT_PASSWORD);
        %>


<!-- iCheck --> 
<script src="js/icheck.min.js" type="text/javascript"></script>
 
<script>
      $(function () {
        $('input').iCheck({
          checkboxClass: 'icheckbox_square-blue',
          radioClass: 'iradio_square-blue',
          increaseArea: '20%' // optional
        });
        checkIfCredentialsInCookieExists(); // check whether cookie is set or not
        
       	// Checks for the login page open in child frame. 
       	// If yes, then we will reload the page parent frame so that it takes user to login page.
		if( window != top ){
			parent.location.reload();
       	}
      });
      
      $(function (){
    	  $('i')
			.each(
				function() {
					if ($(this).attr('data-original-title') != ''
							&& $(this).attr('data-original-title') != 'undefined'
							&& $(this).attr('data-original-title') != undefined) {
						$(this).parent().parent().parent()
								.addClass('error');
						errorExists = true;
					}
				}); 
      });
      
</script> 
</body>
</html>
