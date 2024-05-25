<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<head>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
<script type='text/javascript' src="${pageContext.request.contextPath}/js/jquery.ajaxfileupload.js"></script>
<script src="${pageContext.request.contextPath}/customJS/serviceWiseSummaryManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
</head>

<div class="fullwidth ">
             <div class="box box-warning">
                 <div class="box-header with-border">
                     <h3 class="box-title"><spring:message code="mis.service.summary.tab.header1"></spring:message></h3>
                     <div class="box-tools pull-right">
                         <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                     </div>
                     <!-- /.box-tools --> 
                 </div>
             <div class="box-body inline-form " >
             	<div class="col-md-6 no-padding">
	         		<spring:message code="service.wise.summary.serviceInstance.id" var="label" ></spring:message>
	         		<spring:message code="service.wise.summary.serviceInstance.id.tooltip" var="tooltip" ></spring:message>
	         		<spring:message code="service.wise.summary.selectServieType" var="selectServiceType" ></spring:message>
	         		<div class="form-group">
	         		<div class="table-cell-label">${label }</div>
	             	<div class="input-group ">
	             		<select  name = "serviceInstanceId" class="form-control table-cell input-sm"  tabindex="1" id="serviceInstanceId" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="getServerList(this);">
	             			<option  value="-1" selected="selected">${selectServiceType}</option>
	             			<c:if test="${SERVICE_TYPE_LIST !=null && fn:length(SERVICE_TYPE_LIST) gt 0}">
			             		<c:forEach items="${SERVICE_TYPE_LIST}" var="serviceType">
			             			<option value="${serviceType.alias}">${serviceType.type}</option>
			             		</c:forEach>
		             		</c:if>
	             		</select>
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span> 
	             	</div>
	             	</div>
	             </div>
	             
             	<div class="col-md-6 no-padding">
	             	<spring:message code="service.wise.summary.serverInstance.id" var="label" ></spring:message>
	             	<spring:message code="service.wise.summary.serverInstance.id.tooltip" var="tooltip" ></spring:message>
	             	<div class="form-group">
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group ">
	             		<select  name = "serverInstanceId" class="form-control table-cell input-sm"  tabindex="1" id="serverInstanceId" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" multiple="multiple">
	             		<%-- <input type="text" id="serverInstanceId" name="id" maxlength="100" class="form-control table-cell input-sm" tabindex="2" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> --%>
	             		</select>
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span> 
	             	</div>
	             </div>	
	             </div>
	             
             </div>    
                 
   </div>
   </div>
   
   
   <div class="fullwidth">
             <div class="box box-warning">
                 <div class="box-header with-border">
                     <h3 class="box-title"><spring:message code="mis.service.summary.tab.header2"></spring:message></h3>
                     <div class="box-tools pull-right">
                         <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                     </div>
                     <!-- /.box-tools --> 
                 </div>  
             	    <div class="box-body inline-form" >
             		<div class="col-md-6 no-padding">
	         		<spring:message code="service.wise.summary.reporttype.id" var="label" ></spring:message>
	         		<spring:message code="service.wise.summary.reporttype.id.tooltip" var="tooltip" ></spring:message>
	         		<spring:message code="service.wise.summary.selectReportType" var="selectReportType" ></spring:message>
	         		<div class="form-group">
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group " style="display: table;">
	             		<select  name = "reporttypeid" class="form-control table-cell input-sm"  tabindex="3" id="reporttypeid" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="getFields(this);">
	             			<option  value="-1" selected="selected">${selectReportType}</option>
	             			<c:if test="${misReportType !=null && fn:length(misReportType) gt 0}">
			             		 <c:forEach var="reporttype" items="${misReportType}" >
									<option value="${reporttype}">${reporttype}</option>
	'				      		</c:forEach>
		             		</c:if>
	             		</select>
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span> 
	             	</div>
	             </div>
	           </div>
	           
             	<div class="col-md-6 no-padding" id="hourly_date" style="display: none;">
	             <spring:message code="service.wise.summary.hourly_date.id" var="label" ></spring:message>
				 <spring:message code="service.wise.summary.hourly_date.id.tooltip" var="tooltip" ></spring:message>
				 <div class="form-group">
					<div class="table-cell-label">${label}</div>
							<div class="input-group input-append date" id="dateRangePicker" >
								<span class="input-group-addon add-on"> <i class="fa fa-calendar"></i></span>
								<input id="hourlyDate" tabindex="7" title="${tooltip}"   data-toggle="tooltip" data-placement="bottom" /> 
								
							</div>
				 </div>
     	  		</div>
     	  		
             		<div class="col-md-6 no-padding" id="daily_options" style="display: none;">
	         		<spring:message code="service.wise.summary.daily_options.id" var="label" ></spring:message>
	         		<spring:message code="service.wise.summary.daily_options.id.tooltip" var="tooltip" ></spring:message>
	         		<spring:message code="service.wise.summary.selectDurationType" var="selectDurationType" ></spring:message>
	         		<div class="form-group">
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group " style="display: table;">
	             		<select  name = "dailyOptionsid" class="form-control table-cell input-sm"  tabindex="3" id="dailyOptionsid" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="getDurationForDaily(this);">
	             			<option  value="-1" selected="selected">${selectDurationType}</option>
	             			<option value="1">Select Month</option>
    						<option value="2">Custom</option>
	             		</select>
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span> 
	             	</div>
	             </div>
	           </div>
	           
	           <div class="col-md-6 no-padding" id="daily_month" style="display: none;">
	         		<spring:message code="service.wise.summary.daily_month.id" var="label" ></spring:message>
	         		<spring:message code="service.wise.summary.daily_month.id.tooltip" var="tooltip" ></spring:message>
	         		<spring:message code="service.wise.summary.selectMonth" var="selectMonth" ></spring:message>
	         		<div class="form-group">
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group " style="display: table;">
	             		<select  name = "dailyMonthid" class="form-control table-cell input-sm"  tabindex="3" id="dailyMonthid" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
	             			<option  value="-1" selected="selected">${selectMonth}</option>
	             			<%-- <c:if test="${SERVICE_TYPE_LIST !=null && fn:length(SERVICE_TYPE_LIST) gt 0}"> --%>
			             		 <c:forEach var="month" items="${months}" >
									<option value="${month.value}">${month}</option>
	'				      		</c:forEach>
		             		<%-- </c:if> --%>
	             		</select>
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span> 
	             	</div>
	             </div>
	           </div>
	           
	           <div class="col-md-6 no-padding" id="daily_year" style="display: none;">
	         		<spring:message code="service.wise.summary.daily_year.id" var="label" ></spring:message>
	         		<spring:message code="service.wise.summary.daily_year.id.tooltip" var="tooltip" ></spring:message>
	         		<spring:message code="service.wise.summary.selectYear" var="selectYear" ></spring:message>
	         		<div class="form-group">
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group " style="display: table;">
	             		<select  name = "dailyyearid" class="form-control table-cell input-sm"  tabindex="3" id="dailyyearid" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
	             			<option  value="-1" selected="selected">${selectYear}</option>
	             			<%-- <c:if test="${SERVICE_TYPE_LIST !=null && fn:length(SERVICE_TYPE_LIST) gt 0}"> --%>
			             		 <c:forEach var="year" items="${years}" >
									<option value="${year}">${year}</option>
	'				      		</c:forEach>
		             		<%-- </c:if> --%>
	             		</select>
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span> 
	             	</div>
	             </div>
	           </div>
	          
	           <div class="col-md-6 no-padding" id="daily_custom_date_from" style="display: none;">
	             <spring:message code="service.wise.summary.daily_custom_date_from.id" var="label" ></spring:message>
				 <spring:message code="service.wise.summary.daily_custom_date_from.id.tooltip" var="tooltip" ></spring:message>
				 <div class="form-group">
					<div class="table-cell-label">${label}</div>
							<div class="input-group input-append date" id="dateRangePicker1" >
								<span class="input-group-addon add-on"> <i class="fa fa-calendar"></i></span>
								<input id="dailyCustomDateFrom" tabindex="7" title="${tooltip}"   data-toggle="tooltip" data-placement="bottom" />
								<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span> 
							</div>
				 </div>
     	  		</div>
     	  		
     	  		<div class="col-md-6 no-padding" id="daily_custom_date_to" style="display: none;">
	             <spring:message code="service.wise.summary.daily_custom_date_to.id" var="label" ></spring:message>
				 <spring:message code="service.wise.summary.daily_custom_date_to.id.tooltip" var="tooltip" ></spring:message>
				 <div class="form-group">
					<div class="table-cell-label">${label}</div>
							<div class="input-group input-append date" id="dateRangePicker2" >
								<span class="input-group-addon add-on"> <i class="fa fa-calendar"></i></span>
								<input id="dailyCustomDateTo" tabindex="7" title="${tooltip}"   data-toggle="tooltip" data-placement="bottom" />
								<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span> 
								 
							</div>
				 </div>
     	  		</div>
     	  		
     	  		
     	  		<div class="col-md-6 no-padding" id="monthly_options" style="display: none;">
	         		<spring:message code="service.wise.summary.daily_options.id" var="label" ></spring:message>
	         		<spring:message code="service.wise.summary.daily_options.id.tooltip" var="tooltip" ></spring:message>
	         		<spring:message code="service.wise.summary.selectDurationType" var="selectDurationType" ></spring:message>
	         		<div class="form-group">
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group " style="display: table;">
	             		<select  name = "monthlyoptionsid" class="form-control table-cell input-sm"  tabindex="3" id="monthlyoptionsid" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="getDurationForMonthly(this);">
	             			<option  value="-1" selected="selected">${selectDurationType}</option>
	             			<option value="1">Select Year</option>
    						<option value="2">Custom</option>
	             		</select>
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span> 
	             	</div>
	             </div>
	           </div>
	           
	           
	           <div class="col-md-6 no-padding" id="monthly_year" style="display: none;">
	         		<spring:message code="service.wise.summary.daily_year.id" var="label" ></spring:message>
	         		<spring:message code="service.wise.summary.daily_year.id.tooltip" var="tooltip" ></spring:message>
	         		<spring:message code="service.wise.summary.selectYear" var="selectYear" ></spring:message>
	         		<div class="form-group">
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group " style="display: table;">
	             		<select  name = "monthlyyearid" class="form-control table-cell input-sm"  tabindex="3" id="monthlyyearid" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
	             			<option  value="-1" selected="selected">${selectYear}</option>
	             			<%-- <c:if test="${SERVICE_TYPE_LIST !=null && fn:length(SERVICE_TYPE_LIST) gt 0}"> --%>
			             		 <c:forEach var="year" items="${years}" >
									<option value="${year}">${year}</option>
	'				      		</c:forEach>
		             		<%-- </c:if> --%>
	             		</select>
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span> 
	             	</div>
	             </div>
	           </div>
	           
	           <div class="col-md-6 no-padding" id="monthly_month1" style="display: none;">
	         		<spring:message code="service.wise.summary.monthly_month1.id" var="label" ></spring:message>
	         		<spring:message code="service.wise.summary.monthly_month1.id.tooltip" var="tooltip" ></spring:message>
	         		<spring:message code="service.wise.summary.selectMonth" var="selectMonth" ></spring:message>
	         		<div class="form-group">
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group " style="display: table;">
	             		<select  name = "monthlymonth1id" class="form-control table-cell input-sm"  tabindex="3" id="monthlymonth1id" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
	             			<option  value="-1" selected="selected">${selectMonth}</option>
	             			<%-- <c:if test="${SERVICE_TYPE_LIST !=null && fn:length(SERVICE_TYPE_LIST) gt 0}"> --%>
			             		 <c:forEach var="month" items="${months}" >
									<option value="${month.value}">${month}</option>
	'				      		</c:forEach>
		             		<%-- </c:if> --%>
	             		</select>
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>  
	             	</div>
	             	
	             </div>
	           </div>
	           
	           <div class="col-md-6 no-padding" id="monthly_year1" style="display: none;">
	         		<spring:message code="service.wise.summary.monthly_year1.id" var="label" ></spring:message>
	         		<spring:message code="service.wise.summary.monthly_year1.id.tooltip" var="tooltip" ></spring:message>
	         		<spring:message code="service.wise.summary.selectYear" var="selectYear" ></spring:message>
	         		<div class="form-group">
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group " style="display: table;">
	             		<select  name = "monthlyyear1id" class="form-control table-cell input-sm"  tabindex="3" id="monthlyyear1id" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
	             			<option  value="-1" selected="selected">${selectYear}</option>
	             			<%-- <c:if test="${SERVICE_TYPE_LIST !=null && fn:length(SERVICE_TYPE_LIST) gt 0}"> --%>
			             		 <c:forEach var="year" items="${years}" >
									<option value="${year}">${year}</option>
	'				      		</c:forEach>
		             		<%-- </c:if> --%>
	             		</select>
	             	    <span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span> 
	             	</div>
	             </div>
	           </div>
	           
	           <div class="col-md-6 no-padding" id="monthly_month2" style="display: none;">
	         		<spring:message code="service.wise.summary.monthly_month2.id" var="label" ></spring:message>
	         		<spring:message code="service.wise.summary.monthly_month2.id.tooltip" var="tooltip" ></spring:message>
	         		<spring:message code="service.wise.summary.selectMonth" var="selectMonth" ></spring:message>
	         		<div class="form-group">
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group " style="display: table;">
	             		<select  name = "monthlymonth2id" class="form-control table-cell input-sm"  tabindex="3" id="monthlymonth2id" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
	             			<option  value="-1" selected="selected">${selectMonth}</option>
	             			<%-- <c:if test="${SERVICE_TYPE_LIST !=null && fn:length(SERVICE_TYPE_LIST) gt 0}"> --%>
			             		 <c:forEach var="month" items="${months}" >
									<option value="${month.value}">${month}</option>
	'				      		</c:forEach>
		             		<%-- </c:if> --%>
	             		</select>
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span> 
	             	</div>
	             </div>
	           </div>
	           
	           <div class="col-md-6 no-padding" id="monthly_year2" style="display: none;">
	         		<spring:message code="service.wise.summary.monthly_year2.id" var="label" ></spring:message>
	         		<spring:message code="service.wise.summary.monthly_year2.id.tooltip" var="tooltip" ></spring:message>
	         		<spring:message code="service.wise.summary.selectYear" var="selectYear" ></spring:message>
	         		<div class="form-group">
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group " style="display: table;">
	             		<select  name = "monthlyyear2id" class="form-control table-cell input-sm"  tabindex="3" id="monthlyyear2id" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
	             			<option  value="-1" selected="selected">${selectYear}</option>
	             			<%-- <c:if test="${SERVICE_TYPE_LIST !=null && fn:length(SERVICE_TYPE_LIST) gt 0}"> --%>
			             		 <c:forEach var="year" items="${years}" >
									<option value="${year}">${year}</option>
	'				      		</c:forEach>
		             		<%-- </c:if> --%>
	             		</select>
	             	<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span> 
	             	</div>
	             </div>
	           </div>
           
   </div>
   </div>
   
   	
     	  	<div class="clearfix"></div>
     		<div class="pbottom15 form-group ">
     			<sec:authorize access="hasAuthority('GENERATE_SERVICE_WISE_SUMMARY_REPORT')">
   					<button id="generateReportBtn" class="btn btn-grey btn-xs" onclick="generateReport();"><spring:message code="btn.label.generatereport" ></spring:message></button>
    				<button id="resetBtn" class="btn btn-grey btn-xs" onclick="resetReportParams();"><spring:message code="btn.label.reset" ></spring:message></button>
    			</sec:authorize>
   			</div>   			
   		</div>	
   		
   		
   		
   		 <!--Below link calls for PDF Generation param '4' excludes column in PDF-->
      <!--   <a href="#" onclick="exportData('pdf');">Save as PDF</a>
        <a href="#" onclick="exportData('xls');">Save as Excel</a> -->
        
      <!--   <div class="fullwidth mtop10"> -->
      <sec:authorize access="hasAuthority('DOWNLOAD_SERVICE_WISE_SUMMARY_REPORT')">
	   		<div class="title2">
	   			<%-- <spring:message code="dbconfig.database.configuration.main.title"></spring:message> --%> 
	   			
	   			<span class="title2rightfield"> 
	   				<span class="title2rightfield-icon1-text">
	   					<sec:authorize access="hasAuthority('DOWNLOAD_SERVICE_WISE_SUMMARY_REPORT')">
	   					<a href="#" id="exportDataPdf" onclick="exportData('pdf');"><spring:message code="service.wise.summary.generate.pdf"></spring:message></a>
	   					</sec:authorize>
	   					<sec:authorize access="hasAuthority('DOWNLOAD_SERVICE_WISE_SUMMARY_REPORT')">
	   					<a href="#" id="exportDataXls" onclick="exportData('xls');"><spring:message code="service.wise.summary.generate.excel"></spring:message> </a>
	   					</sec:authorize>
	   				</span> 
	   			</span> 
	   			
	   		</div>
	   		</sec:authorize>
	   		<div class="clearfix"></div>
       <!--  </div> -->
        
 
        <div id="jQGrid" align="center">
        </div>
        <form id="misReport_download_form" action="" method="POST" name="misReport_download_form">
            <input type="hidden" name="reportType" id="reportType" value="" />
            <input type="hidden" name="serverInstancelst" id="serverInstancelst" value="" />
            <input type="hidden" name="serviceInstanceId" id="serviceInstanceId" value="" />
            <input type="hidden" name="hourlyDate" id="hourlyDate" value="" />
            <input type="hidden" name="dailyMonth" id="dailyMonth" value="" />
            <input type="hidden" name="dailyYear" id="dailyYear" value="" />
            <input type="hidden" name="dailyDuration" id="dailyDuration" value="" />
            <input type="hidden" name="dailyCustomToDate" id="dailyCustomToDate" value="" />
            <input type="hidden" name="dailyCustomFromDate" id="dailyCustomFromDate" value="" />
            <input type="hidden" name="monthlyDuration" id="monthlyoptionsid" value="" />
            <input type="hidden" name="monthlyYear" id="monthlyyearid" value="" />
            <input type="hidden" name="monthlyStartMonth" id="monthlymonth1id" value="" />
            <input type="hidden" name="monthlyEndMonth" id="monthlymonth2id" value="" />
            <input type="hidden" name="monthlyStartYear" id="monthlyyear1id" value="" />
            <input type="hidden" name="monthlyEndYear" id="monthlyyear2id" value="" />
            <input type="hidden" name="fileType" id="fileType" value="" />
        </form>
  <div class="box-body table-responsive no-padding box" id=instanceListDiv>
						<table class="table table-hover" id="instanceList"></table>
						<div id=instanceListPagingDiv></div>
						<div class="clearfix"></div>
						<div id="divLoading" align="center" style="display: none;">
							<img src="img/preloaders/Preloader_10.gif" />
						</div>
</div>	                        
                 
<script type="text/javascript">
var jsSpringMsg = {};
jsSpringMsg.invalidserverInstanceId = "<spring:message code='mis.invalidserverInstanceId.message'></spring:message>";
jsSpringMsg.invalidserviceInstanceId = "<spring:message code='mis.invalidserviceInstanceId.message'></spring:message>";
jsSpringMsg.invalidreporttypeid = "<spring:message code='mis.invalidreporttypeid.message'></spring:message>";
jsSpringMsg.invalidhourly_date = "<spring:message code='mis.invalidhourly_date.message'></spring:message>";
jsSpringMsg.invaliddailyOptionsid = "<spring:message code='mis.invaliddailyOptionsid.message'></spring:message>";
jsSpringMsg.invaliddailyMonthid = "<spring:message code='mis.invaliddailyMonthid.message'></spring:message>";
jsSpringMsg.invaliddailyyearid = "<spring:message code='mis.invaliddailyyearid.message'></spring:message>";
jsSpringMsg.invaliddaily_custom_date_to = "<spring:message code='mis.invaliddaily_custom_date_to.message'></spring:message>";
jsSpringMsg.invaliddaily_custom_date_from = "<spring:message code='mis.invaliddaily_custom_date_from.message'></spring:message>";
jsSpringMsg.invalidmonthlyoptionsid = "<spring:message code='mis.invalidmonthlyoptionsid.message'></spring:message>";
jsSpringMsg.invalidmonthlyyearid = "<spring:message code='mis.invalidmonthlyyearid.message'></spring:message>";
jsSpringMsg.invalidmonthlymonth1id = "<spring:message code='mis.invalidmonthlymonth1id.message'></spring:message>";
jsSpringMsg.invalidmonthlyyear1id = "<spring:message code='mis.invalidmonthlyyear1id.message'></spring:message>";
jsSpringMsg.invalidmonthlymonth2id = "<spring:message code='mis.invalidmonthlymonth2id.message'></spring:message>";
jsSpringMsg.invalidmonthlyyear2id = "<spring:message code='mis.invalidmonthlyyear2id.message'></spring:message>";
jsSpringMsg.startDateGreaterThanEndDate="<spring:message code='mis.startDateGreaterThanEndDate.message'></spring:message>"
jsSpringMsg.startYearIsGreater="<spring:message code='mis.startYearIsGreater.message'></spring:message>"
jsSpringMsg.startMonthIsGreater="<spring:message code='mis.startMonthIsGreater.message'></spring:message>"
jsSpringMsg.startDateGreaterThanEndDate="<spring:message code='mis.InvalidDiffStartEndDate.message'></spring:message>"

$(function () {
	$('#dateRangePicker').datepicker({
		format : 'mm/dd/yyyy',
		autoclose: true,
		onSelect:function(){
			$("input[tabindex="+(this.tabIndex+1)+"]").focus();
	        }
});
	$('#dateRangePicker1').datepicker({
		format : 'mm/dd/yyyy',
		autoclose: true,
		onSelect:function(){
			$("input[tabindex="+(this.tabIndex+1)+"]").focus();
	        }
});
	$('#dateRangePicker2').datepicker({
		format : 'mm/dd/yyyy',
		autoclose: true,
		onSelect:function(){
			$("input[tabindex="+(this.tabIndex+1)+"]").focus();
	        }
});
})

function generateReport(){
	clearAllMessages();
	if(validateServiceAndServerValues()){
		if(validateOtherReportParameters()){
			if(isServicePacketBased($("#serviceInstanceId").val())){
				getServerInstanceListForPacketBasedService();
			}else{
				getServerInstanceListForFileBasedService();
			}
		}
	}
}

function resetReportParams()
{
	$("#serviceInstanceId").val(-1);
	$("#reporttypeid").val(-1);
	$("#hourlyDate").val('');
	$("#dailyOptionsid").val(-1);
	$("#dailyMonthid").val(-1);
	$("#dailyyearid").val(-1);
	$("#monthlyoptionsid").val(-1);
	$("#monthlyyearid").val(-1);
	$("#monthlymonth1id").val(-1);
	$("#monthlyyear1id").val(-1);
	$("#monthlyyear2id").val(-1);
	$("#monthlymonth2id").val(-1);
	$("#dailyCustomDateFrom").val('');
	$("#dailyCustomDateTo").val('');
	$('#serverInstanceId option').each(function(index, option) {
	    $(option).remove();
	});
}
function validateServiceAndServerValues(){
	if($("#serviceInstanceId").val() === '-1'){
		showErrorMsg(jsSpringMsg.invalidserviceInstanceId);
		return false;
	}else if( (!$("#serverInstanceId").val())){
		showErrorMsg(jsSpringMsg.invalidserverInstanceId);
		return false;
	}
	return true;
}

 function exportData(file){
	 clearAllMessages();
     if(validateServiceAndServerValues()){
     if(validateOtherReportParameters()){
     if(validateDateParametersForDownload()){
	     var serverInstanceSelectedList = $('#serverInstanceId').val()+'';
	     document.misReport_download_form.fileType.value=file;
	     document.misReport_download_form.reportType.value=$("#reporttypeid").val();
	     document.misReport_download_form.serverInstancelst.value=serverInstanceSelectedList;
	     document.misReport_download_form.serviceInstanceId.value=$("#serviceInstanceId").val();/* $("#serviceInstanceId").val(); */
	     document.misReport_download_form.hourlyDate.value=$("#hourlyDate").val();
	     document.misReport_download_form.dailyMonth.value=$("#dailyMonthid").val();
	     document.misReport_download_form.dailyYear.value=$("#dailyyearid").val();
	     document.misReport_download_form.dailyDuration.value=$("#dailyOptionsid").val();
	     document.misReport_download_form.dailyCustomToDate.value=$("#dailyCustomDateTo").val();
	     document.misReport_download_form.dailyCustomFromDate.value=$("#dailyCustomDateFrom").val();
	     document.misReport_download_form.monthlyDuration.value=$("#monthlyoptionsid").val();
	     document.misReport_download_form.monthlyYear.value=$("#monthlyyearid").val();
	     document.misReport_download_form.monthlyStartMonth.value=$("#monthlymonth1id").val();
	     document.misReport_download_form.monthlyEndMonth.value=$("#monthlymonth2id").val();
	     document.misReport_download_form.monthlyStartYear.value=$("#monthlyyear1id").val();
	     document.misReport_download_form.monthlyEndYear.value=$("#monthlyyear2id").val();
	     document.misReport_download_form.method='POST';
	     document.misReport_download_form.action='<%=request.getContextPath()%>/<%= ControllerConstants.DOWNLOAD_MIS_REPORT %>';  // send it to server which will open this contents in excel file
	     document.misReport_download_form.submit();
     }
     }
     }
 }
function getServerInstanceListForPacketBasedService(){
	var serverInstanceSelectedList = $('#serverInstanceId').val()+'';
	$.ajax({
		url: '<%=ControllerConstants.MIS_GET_SERVER_SUMMARY_LIST_FOR_PACKET_BASED_SERVICE%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		{
			'reportType'			: 		$("#reporttypeid").val(),
	    	'serverInstancelst'		: 		serverInstanceSelectedList,
	    	'serviceInstanceId'		: 		$("#serviceInstanceId").val(),
	    	'hourlyDate'			:  		$("#hourlyDate").val(),
	    	'dailyMonth'			:  		$("#dailyMonthid").val(),
	    	'dailyYear'				:   	$("#dailyyearid").val(),
		    'dailyDuration'			: 		$("#dailyOptionsid").val(),
	    	'dailyCustomToDate'		: 		$("#dailyCustomDateTo").val(),
		    'dailyCustomFromDate'	: 		$("#dailyCustomDateFrom").val(),
	    	'monthlyDuration'		:  		$("#monthlyoptionsid").val(),
		    'monthlyYear'			: 		$("#monthlyyearid").val(),
			'monthlyStartMonth'		: 		$("#monthlymonth1id").val(),
			'monthlyEndMonth'		: 		$("#monthlymonth2id").val(),
			'monthlyStartYear'		: 		$("#monthlyyear1id").val(),
			'monthlyEndYear'		: 		$("#monthlyyear2id").val(),
		}, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			$("#instanceList").GridUnload();
			if(responseCode == "200"){
				populateResultDataForPacketBasedServiceInGrid(responseObject);
			}else if(responseCode == "400"){
				addErrorIconAndMsgForAjaxPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
		}
	}); 
	
	function populateResultDataForPacketBasedServiceInGrid(reportData) {
		$("#instanceList").jqGrid({
		url: "",
	    datatype: "local",
	    colNames:[
	             "<spring:message code='serverManagement.grid.column.srno' ></spring:message>",
	             "<spring:message code='serverManagement.grid.column.server.instance' ></spring:message>",
	              "<spring:message code='serviceManagement.grid.column.service.type' ></spring:message>",
	              "<spring:message code='serverManagement.grid.column.service.totalPackets' ></spring:message>",
	              "<spring:message code='serverManagement.grid.column.service.successPackets' ></spring:message>",
	              "<spring:message code='serverManagement.grid.column.service.malformPackets' ></spring:message>",
	              "<spring:message code='serverManagement.grid.column.service.malformPackets%' ></spring:message>",
	              "<spring:message code='serverManagement.grid.column.service.totalRecords' ></spring:message>",
	              "<spring:message code='serverManagement.grid.column.service.successRecords' ></spring:message>",
	              "<spring:message code='serverManagement.grid.column.service.failRecords' ></spring:message>",
	              "<spring:message code='serverManagement.grid.column.service.failRecords%' ></spring:message>",
	             ],
		colModel:[
		    {name:'srno',sorttype:'int'},
		    {name:'serverName'},
	        {name:'serviceName'},
	        {name:'totalPackets'},
	    	{name:'successPackets'},
	    	{name:'droppedPackets'},
	    	{name:'droppedPacketsPercentage'},
	    	{name:'totalRecords'},
	    	{name:'successRecords'},
	    	{name:'failRecords'},
	    	{name:'failedRecordsPercentage'},
	    ],
	    data:reportData,
	    rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
	    rowList:[10,20,60,100],
	    height: 'auto',
		sortname: 'id',
		sortorder: "desc",
	    pager: "#instanceListPagingDiv",
	    viewrecords: true,
	    timeout : 120000,
	    loadtext: "Loading...",
	    caption: "<spring:message code='mis.report.grid.caption'></spring:message>",
			loadComplete: function(data) {
				
				gridDataArr = data.rows;
				
				$.each(gridDataArr, function (index, instance) {
					instanceList[instance.id]=instance;
				});
				
				$(".ui-dialog-titlebar").show();
				if ($('#instanceList').getGridParam('records') === 0) {
	            $('#instanceList tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code='serverManagement.grid.empty.records'></spring:message></div>");
	            $("#instanceListPagingDiv").hide();
	        }else{
	        	$("#instanceListPagingDiv").show();
	        	ckIntanceSelected = new Array();
	        }

				 $('.checkboxbg').bootstrapToggle();
		},
		 beforeSend: function(xhr) {
	        	xhr.setRequestHeader("Accept", "application/json");
	            xhr.setRequestHeader("Content-Type", "application/json");
	        },
		onPaging: function (pgButton) {
		},
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		recordtext: "<spring:message code='serverManagement.grid.pager.total.records.text'></spring:message>",
	    emptyrecords: "<spring:message code='serverManagement.grid.empty.records'></spring:message>",
		loadtext: "<spring:message code='serverManagement.grid.loading.text'></spring:message>",
		pgtext : "<spring:message code='serverManagement.grid.pager.text'></spring:message>"
	});
	}
}

function getServerInstanceListForFileBasedService(){
	var serverInstanceSelectedList = $('#serverInstanceId').val()+'';
		$.ajax({
			url: '<%=ControllerConstants.MIS_GET_SERVER_SUMMARY_LIST_FOR_FILE_BASED_SERVICE%>',
			cache: false,
			async: true,
			dataType: 'json',
			type: "POST",
			data:
			{
				'reportType'			: 		$("#reporttypeid").val(),
		    	'serverInstancelst'		: 		serverInstanceSelectedList,
		    	'serviceInstanceId'		: 		$("#serviceInstanceId").val(),
		    	'hourlyDate'			:  		$("#hourlyDate").val(),
		    	'dailyMonth'			:  		$("#dailyMonthid").val(),
		    	'dailyYear'				:   	$("#dailyyearid").val(),
			    'dailyDuration'			: 		$("#dailyOptionsid").val(),
		    	'dailyCustomToDate'		: 		$("#dailyCustomDateTo").val(),
			    'dailyCustomFromDate'	: 		$("#dailyCustomDateFrom").val(),
		    	'monthlyDuration'		:  		$("#monthlyoptionsid").val(),
			    'monthlyYear'			: 		$("#monthlyyearid").val(),
				'monthlyStartMonth'		: 		$("#monthlymonth1id").val(),
				'monthlyEndMonth'		: 		$("#monthlymonth2id").val(),
				'monthlyStartYear'		: 		$("#monthlyyear1id").val(),
				'monthlyEndYear'		: 		$("#monthlyyear2id").val(),
			}, 
			success: function(data){
				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				$("#instanceList").GridUnload();
				if(responseCode == "200"){
					populateResultDataForFileBasedServiceInGrid(responseObject);
				}else if(responseCode == "400"){
					addErrorIconAndMsgForAjaxPopUp(responseMsg);
				}
			},
		    error: function (xhr,st,err){
			}
		}); 
	
		function populateResultDataForFileBasedServiceInGrid(reportData){
			$("#instanceList").GridUnload();
			$("#instanceList").jqGrid({
			url: "",
		    datatype: "local",
		    colNames:[
		              "<spring:message code='serverManagement.grid.column.srno' ></spring:message>",
		              "<spring:message code='serverManagement.grid.column.server.instance' ></spring:message>",
		              "<spring:message code='serviceManagement.grid.column.service.type' ></spring:message>",
		              "<spring:message code='serverManagement.grid.column.service.totalFiles' ></spring:message>",
		              "<spring:message code='serverManagement.grid.column.service.successFiles' ></spring:message>",
		              "<spring:message code='serverManagement.grid.column.service.failFiles' ></spring:message>",
		              "<spring:message code='serverManagement.grid.column.service.failFiles%' ></spring:message>",
		              "<spring:message code='serverManagement.grid.column.service.totalRecords' ></spring:message>",
		              "<spring:message code='serverManagement.grid.column.service.successRecords' ></spring:message>",
		              "<spring:message code='serverManagement.grid.column.service.failRecords' ></spring:message>",
		              "<spring:message code='serverManagement.grid.column.service.failRecords%' ></spring:message>",
		             ],
			colModel:[
		    	{name:'srno',sorttype:'int'},
		    	{name:'serverName'},
		        {name:'serviceName'},
		        {name:'totalFiles'},
		    	{name:'successFiles'},
		    	{name:'failFiles'},
		    	{name:'failFilesPercentage'},
		    	{name:'totalRecords'},
		    	{name:'successRecords'},
		    	{name:'failRecords'},
		    	{name:'failedRecordsPercentage'},
		    ],
		    data:reportData,
		    rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		    rowList:[10,20,60,100],
		    height: 'auto',
			sortname: 'id',
			sortorder: "desc",
		    pager: "#instanceListPagingDiv",
		    viewrecords: true,
		    timeout : 120000,
		    loadtext: "Loading...",
		    caption: "<spring:message code='mis.report.grid.caption'></spring:message>",
				loadComplete: function(data) {
					
					gridDataArr = data.rows;
					
					$.each(gridDataArr, function (index, instance) {
						instanceList[instance.id]=instance;
					});
					
					$(".ui-dialog-titlebar").show();
					if ($('#instanceList').getGridParam('records') === 0) {
		            $('#instanceList tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code='serverManagement.grid.empty.records'></spring:message></div>");
		            $("#instanceListPagingDiv").hide();
		        }else{
		        	$("#instanceListPagingDiv").show();
		        	ckIntanceSelected = new Array();
		        }

					 $('.checkboxbg').bootstrapToggle();
			},
			onPaging: function (pgButton) {
			},
			loadError : function(xhr,st,err) {
				handleGenericError(xhr,st,err);
			},
			recordtext: "<spring:message code='serverManagement.grid.pager.total.records.text'></spring:message>",
		    emptyrecords: "<spring:message code='serverManagement.grid.empty.records'></spring:message>",
			loadtext: "<spring:message code='serverManagement.grid.loading.text'></spring:message>",
			pgtext : "<spring:message code='serverManagement.grid.pager.text'></spring:message>"
		});	
}

}

function getServerList(serviceInstanceId){
		clearAllMessages();
		document.getElementById("serverInstanceId").options.length = 0;
		$.ajax({
			url: '<%=ControllerConstants.MIS_GET_SERVER_LIST_FOR_SERVICE%>',
			cache: false,
			async: true,
			dataType: 'json',
			type: "POST",
			data:
			{
				"serviceAlias"	:	serviceInstanceId.value,
			}, 
			success: function(data){
				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				if(responseCode == "200"){
					 showUsers(responseObject);
				}else if(responseCode == "400"){
					showErrorMsg(responseMsg);
				}
			},
		    error: function (xhr,st,err){
			}
		}); 
		
		function showUsers(data) {
		        for ( var i = 0, len = data.length; i < len; ++i) {
		            var user = data[i];
		            $('#serverInstanceId').append("<option value=\"" + user + "\">" + user + "</option>");
		    } 
		}	        
}


</script>


</html>
