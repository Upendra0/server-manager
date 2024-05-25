<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7 lt-ie10"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8 lt-ie10"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9 lt-ie10"> <![endif]-->
<!--[if IE 9]>         <html class="no-js lt-ie10"> <![endif]-->
<html>
	<jsp:include page="../common/newheader.jsp" ></jsp:include>

    <body class="error-page">
        <jsp:include page="../common/browserCompatibleCheck.jsp" ></jsp:include>

        <section class="wrapper scrollable">
            <div class="panel panel-default panel-block panel-error-block">
                <div class="panel-heading">
                    <div>
                        <i class="icon-frown"></i>
                        <h1>
                            <small>
                            	<spring:message code="error.title" ></spring:message>
                            </small>
                            	<spring:message code="page.not.found" ></spring:message>
                            <i class="icon-info-sign uses-tooltip" data-toggle="tooltip" data-placement="top" data-original-title="The requested resource could not be found but may be available again in the future."></i>
                        </h1>
                        <div class="error-code">
                            404
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <a href="${pageContext.request.contextPath}/welcome">
                    	<i class="icon-home"></i> &nbsp;&nbsp; 
                    	<spring:message code="go.to.home" ></spring:message> 
                    </a>
                </div>
            </div>
        </section>
        
		<jsp:include page="../common/newfooter.jsp" ></jsp:include>

    </body>
</html>
