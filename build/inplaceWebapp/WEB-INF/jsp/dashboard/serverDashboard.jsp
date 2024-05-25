<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="${pageContext.request.contextPath}/js/highchart/highcharts.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/highchart/drilldown.js" type="text/javascript"></script>

<div class="box-body padding0 mtop10">
	<div class="fullwidth">
			<!-- High Alert sections  start here -->
			<div class="col-md-6 padding0" id="high_alert_block">
					<div class="box box-warning">
						<div class="box-header with-border">
							<h3 class="box-title"><spring:message code="dashboard.chart.high.alert.heading" ></spring:message></h3>
							<div class="box-tools pull-right" style="top: -3px;">
								<button class="btn btn-box-tool" data-widget="collapse">
									<i class="fa fa-minus"></i>
								</button>
							</div>
							<!-- /.box-tools -->
						</div>
						<!-- /.box-header end here -->
						<!-- Form content start here  -->
						<div class="box-body">
							<div class="fullwidth inline-form" style="overflow: auto;height:255px;min-height:255px;">
								<table class="table table-hover table-bordered" border="1"  style="width:100%;min-height: 300px;">
									<tr>
										<th><spring:message code="dashboard.chart.high.alert.column.stage" ></spring:message></th>
										<th><spring:message code="dashboard.chart.file.statistic.column.instance.name" ></spring:message></th>
										<th><spring:message code="dashboard.chart.high.alert.column.alert" ></spring:message></th>
										<th><spring:message code="dashboard.chart.high.alert.column.time" ></spring:message></th>
									</tr>
									<c:forEach items="${alertList}" var="alert">
										
				   							<tr>	
				   								 
				   								 <td  class="padleft-right" align="center"> 
					   							<c:choose>
											        <c:when test="${(alert.stage eq 'critical')}">
												         <img src="img/alert.png" width="22" height="22" data-placement="bottom" data-toggle="tooltip" title="Critical"/>
											        </c:when>
											        <c:when test="${(alert.stage eq 'info')}">
											             <img src="img/info.png" width="22" height="22" data-placement="bottom" data-toggle="tooltip" title="Info"/>
											        </c:when>
											       <c:when test="${(alert.stage eq 'normal')}">
											             <img src="img/warning.png" width="22" height="22" data-placement="bottom" data-toggle="tooltip" title="Warning"/>
											        </c:when>
											    </c:choose>
											    </td>
											    <td class="padleft-right">${alert.intanceName}</td>
											    <td class="padleft-right">${alert.alert}</td>
				   								<td class="padleft-right">${alert.time}</td> 
											</tr>    
								   	</c:forEach> 
								</table>
							</div>
						</div>	
					</div>
			</div>
		<!-- High Alert sections end here -->
		<!-- File Statistic Details sections  start here -->
			<div class="col-md-6 padding0" id="file_statistic_block" style="padding-left: 10px;">
					<div class="box box-warning">
						<div class="box-header with-border">
							<h3 class="box-title"><spring:message code="dashboard.chart.file.statistic.heading" ></spring:message></h3>
							<div class="box-tools pull-right" style="top: -3px;">
								<button class="btn btn-box-tool" data-widget="collapse">
									<i class="fa fa-minus"></i>
								</button>
							</div>
							<!-- /.box-tools -->
						</div>
						<!-- /.box-header end here -->
						<!-- Form content start here  -->
						<div class="box-body">
							<div class="fullwidth inline-form" style="overflow: auto;height:255px;min-height:255px;">
								
								<table class="table table-hover table-bordered" border="1"  style="widtd:100%;min-height: 300px;">
									<tr>
										<th><spring:message code="dashboard.chart.file.statistic.column.instance.name" ></spring:message></th>
										<th><spring:message code="dashboard.chart.file.statistic.column.raw.cdr" ></spring:message></th>
										<th><spring:message code="dashboard.chart.file.statistic.column.processed.cdr" ></spring:message></th>
										<th><spring:message code="dashboard.chart.file.statistic.column.deviation" ></spring:message></th>
										<th><spring:message code="dashboard.chart.file.statistic.column.raw.file.size" ></spring:message></th>
										<th><spring:message code="dashboard.chart.file.statistic.column.process.file.size" ></spring:message></th>
									</tr>
									<c:forEach items="${statisticList}" var="file">
										<tr>
				   							<td style="padding-left: 10px;">${file.intanceName}</td>
				   							<td style="padding-left: 10px;">${file.rawCDR}</td>
				   							<td style="padding-left: 10px;">${file.processedCDR}</td>
				   							<td  style="padding-left: 10px;" >
				   								
				   								<c:choose>
											        <c:when test="${file.deviation <= 4}">
												      <span style="color:green;"><label>${file.deviation} %</label></span>
											        </c:when>
											        <c:when test="${file.deviation <= 6}">
											           <span style="color:orange;"><label>${file.deviation} %</label></span>
											        </c:when>
											       <c:when test="${file.deviation >= 6}">
											             <span style="color:red;"><label>${file.deviation} %</label></span>
											        </c:when>
											    </c:choose>
											    </td>
				   							<td style="padding-left: 10px;">${file.rawFileSize}</td>
				   							<td style="padding-left: 10px;">${file.processedFileSize}</td>
				   						</tr>
								   	</c:forEach> 
								</table>
								</div>
							</div>
						</div>	
					</div>
			</div>
		<!--File Statistic Details sections end here -->
		
		<!-- Backlog Status sections  start here -->
			<div class="col-md-12 padding0" id="backlog_status_block">
					<div class="box box-warning">
						<div class="box-header with-border">
							<h3 class="box-title"><spring:message code="dashboard.chart.backlog.status.heading" ></spring:message></h3>
							<div class="box-tools pull-right" style="top: -3px;">
								<button class="btn btn-box-tool" data-widget="collapse">
									<i class="fa fa-minus"></i>
								</button>
							</div>
							<!-- /.box-tools -->
						</div>
						<!-- /.box-header end here -->
						<!-- Form content start here  -->
						<div class="box-body">
							<div class="fullwidth inline-form">
								<div id="server_backlog_status" style="width:95%;">
        						</div>
							</div>
						</div>	
					</div>
			</div>
		<!-- Backlog Status sections end here -->
		<!-- File Processing Status sections  start here -->
			<div class="col-md-12 padding0" id="file_process_block">
					<div class="box box-warning">
						<div class="box-header with-border">
							<h3 class="box-title"><spring:message code="dashboard.chart.file.process.heading" ></spring:message></h3>
							<div class="box-tools pull-right" style="top: -3px;">
								<button class="btn btn-box-tool" data-widget="collapse">
									<i class="fa fa-minus"></i>
								</button>
							</div>
							<!-- /.box-tools -->
						</div>
						<!-- /.box-header end here -->
						<!-- Form content start here  -->
						<div class="box-body">
							<div class="fullwidth inline-form">
								<div id="server_file_process_status" style="width:95%;">
        						</div>
							</div>
						</div>	
					</div>
			</div>
		<!-- File Processing Status sections end here -->
		<!-- Server health details sections  start here -->
			<div class="col-md-12 padding0" id="server_health_block">
					<div class="box box-warning">
						<div class="box-header with-border">
							<h3 class="box-title"><spring:message code="dashboard.chart.server.health.heading" ></spring:message></h3>
							<div class="box-tools pull-right" style="top: -3px;">
								<button class="btn btn-box-tool" data-widget="collapse">
									<i class="fa fa-minus"></i>
								</button>
							</div>
							<!-- /.box-tools -->
						</div>
						<!-- /.box-header end here -->
						<!-- Form content start here  -->
						<div class="box-body">
							<div class="fullwidth inline-form">
								<div id="server_health" style="width:95%;">
        						</div>
							</div>
						</div>	
					</div>
			</div>
		<!--Server health details sections end here -->
		
	</div>
<script lang="javascript" type="text/javascript">
$(document).ready(function () {
	
	//backlog status Chart Code start here
	  $("#server_backlog_status").highcharts({
        chart: {
            type: 'column'
        },
        title: {
            text: 'Backlog Status'
        },
       
        xAxis: {
            type: 'category'
        },
        yAxis: {
        	 title: {
                 text: 'Number of Files'
             }
        },
        legend: {
            enabled: false
        },
        tooltip: {
            headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}</b> Files<br/>'
        },

        series: [{
            colorByPoint: true,
            data: [{
                name: 'Distribution Servie Path 1 <br/> Mediation Server-192.168.0.146 ',
                y: 10000,
                drilldown: null,
                color: 'blue'
            }, {
                name: 'Collection Path 2 <br/> CGF Server-192.168.1.19',
                y: 2000,
                drilldown: null,
                color: 'blue'
            }, {
                name: 'Collection Path 1 <br/> Mediation Server-192.168.0.146',
                y: 1000,
                drilldown: null,
                color: 'blue'
            }, {
                name: 'Processing Path 1 <br/> IPLMS_Server - 192.168.1.168',
                y: 100,
                drilldown: null,
                color: 'blue'
            }, {
                name: 'Parsing Path 1 <br/> Mediation Server-192.168.4.20',
                y: 75,
                drilldown: null,
                color: 'blue'
            }]
        }]
    });
	
	//backlog status Chart Code end here
	// File Processing Status code start here
		$('#server_file_process_status').highcharts({
	        chart: {
	            type: 'column'
	        },
	        title: {
	            text: 'File Processing Status'
	        },
	        xAxis: {
	            categories: ['Distribution Service <br/> Mediation Server-192.168.0.146', 'Parsing Service <br/> CGF Server-192.168.1.19', 'Processing Service <br/> IPLMS_Server-192.168.1.168', 'Collection Service <br/> Mediation Server-192.168.1.198']
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: 'Number of Files'
	            },
	            stackLabels: {
	                enabled: true,
	                style: {
	                    fontWeight: 'bold',
	                    color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
	                }
	            }
	        },
	        legend: {
	            align: 'right',
	            x: -30,
	            verticalAlign: 'top',
	            y: 25,
	            floating: true,
	            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
	            borderColor: '#CCC',
	            borderWidth: 1,
	            shadow: false
	        },
	        tooltip: {
	            headerFormat: '<b>{point.x}</b><br/>',
	            pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
	        },
	        plotOptions: {
	            column: {
	                stacking: 'normal',
	                dataLabels: {
	                    enabled: true,
	                    color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
	                    style: {
	                        textShadow: '0 0 3px black'
	                    }
	                }
	            }
	        },
	        series: [{
	            name: 'File In Queue',
	            data: [2334, 2200, 2000, 300]
	        }, {
	            name: 'In-Process Files',
	            data: [29994, 29934, 23900, 10000],
	            color: '#339900'
	        }, {
	            name: 'Marked as Malform /Error Files',
	            data: [2345,2330, 1023,1000],
	            color: '#990000'
	        }]
	    });
	
	// File Processing status code start here
	// Server Health  Details chart start here
	 $('#server_health').highcharts({
        chart: {
            type: 'column'
        },
        title: {
            text: 'Server Health'
        },
       
        xAxis: {
            categories: [
                'Mediation Server-192.168.0.146',
                'Mediation Server-192.168.1.198',
                'Mediation Server-192.168.4.20',
                'IPLMS_Server - 192.168.1.168',
                'CGF Server-192.168.1.19',
                'IPLMS_Server - 192.168.1.1',
                'IPLMS_Server - 192.168.1.50',
                'CGF Server-192.168.1.7',
                'CGF Server-192.168.1.9'
            ],
            crosshair: true
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Percentage(%)'
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y:.1f} %</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: [{
            name: 'Memory Utilization',
            data: [49,71,80,70,45,95,54,56,25]
        }, {
            name: 'Disk Utilization',
            data: [65,48,75,49,63,50,55,67,66]
        }, {
            name: 'CPU Utilization',
            data: [40, 50, 45, 39,60, 24, 65, 70,75]
        }]
    });
	// Server Health  Details chart end here
	
	
	
});


</script>
