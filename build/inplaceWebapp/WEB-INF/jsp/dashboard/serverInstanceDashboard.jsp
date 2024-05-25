<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<script src="${pageContext.request.contextPath}/js/highchart/highcharts.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/highchart/drilldown.js" type="text/javascript"></script>

<div class="box-body padding0 mtop10">
	<div class="fullwidth">
	
			<!--Server health details sections end here -->
			<!-- TPS sections  start here -->
			 <div class="col-md-12 padding0" >
					<div class="box box-warning">
						<div class="box-header with-border">
							<h3 class="box-title"><spring:message	code="dashboard.chart.tps.heading" ></spring:message></h3>
							<div class="box-tools pull-right" style="top: -3px;">
								<button class="btn btn-box-tool" data-widget="collapse">
									<i class="fa fa-minus"></i>
								</button>
							</div>
						</div>
						<div class="box-body">
							<div class="fullwidth inline-form"> 
									<div id="server_instance_tps_chart" style="width:90%;">
        							</div>
							</div>
						</div>	
					</div>
			</div> 
		<!-- TPS sections end here -->
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
							<div class="fullwidth inline-form"  style="overflow: auto;height:255px;">
									<table class="table table-hover table-bordered" border="1" style="widtd:100%;min-height: 300px;">
										<tr>
											<th><spring:message code="dashboard.chart.high.alert.column.stage" ></spring:message></th>
											<th><spring:message code="dashboard.chart.high.alert.column.alert" ></spring:message></th>
											<th><spring:message code="dashboard.chart.high.alert.column.time" ></spring:message></th>
										</tr>
										<c:forEach items="${alertList}" var="alert">
											
					   							<tr>	
					   								 <td  style="padding-left: 10px;" > 
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
												    <td style="padding-left: 10px;">${alert.alert}</td>
					   								<td style="padding-left: 10px;">${alert.time}</td> 
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
								<table class="table table-hover table-bordered" border="1" style="width:100%;min-height: 300px;">
									<tr>
										<th><spring:message code="dashboard.chart.file.statistic.column.instance.name" ></spring:message></th>
										<th><spring:message code="dashboard.chart.file.statistic.column.raw.cdr" ></spring:message></th>
										<th><spring:message code="dashboard.chart.file.statistic.column.processed.cdr" ></spring:message></th>
										<th><spring:message code="dashboard.chart.file.statistic.column.deviation" ></spring:message></th>
										<th><spring:message code="dashboard.chart.file.statistic.column.raw.file.size" ></spring:message></th>
										<th><spring:message code="dashboard.chart.file.statistic.column.process.file.size" ></spring:message></th>
										
									</tr>
									 <c:forEach items="${iStatisticList}" var="file">
									 <tr>
				   							<td class="padleft-right">${file.intanceName}</td>
				   							<td class="padleft-right">${file.rawCDR}</td>
				   							<td class="padleft-right">${file.processedCDR}</td>
				   							<td  class="padleft-right" >
				   								
				   								<c:choose>
											        <c:when test="${file.deviation <= 5}">
												      <span style="color:green;"><label>${file.deviation} %</label></span>
											        </c:when>
											        <c:when test="${(file.deviation > 5)  && (file.deviation <= 7)}">
											           <span style="color:orange;"><label>${file.deviation} %</label></span>
											        </c:when>
											       <c:when test="${file.deviation > 7}">
											             <span style="color:red;"><label>${file.deviation} %</label></span>
											        </c:when>
											    </c:choose>
											    </td>
				   							<td class="padleft-right">${file.rawFileSize}</td>
				   							<td class="padleft-right">${file.processedFileSize}</td>
				   						</tr>
									
								   	 </c:forEach> 
								</table>
							</div>
						</div>	
					</div>
			</div>
		<!--File Statistic Details sections end here -->
		<!-- Backlog Status sections  start here -->
			 <div class="col-md-6 padding0" >
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
								<div id="server_instance_backlog_status" >
        						</div>
							</div>
						</div>	
					</div>
			</div> 
		<!-- Backlog Status sections end here -->
		<!-- File Processing Status sections  start here -->
			<div class="col-md-6 padding0" style="padding-left: 10px;">
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
								<div id="server_instance_file_process_status" >
        						</div>
							</div>
						</div>	
					</div>
			</div> 
		<!-- File Processing Status sections end here -->
		<!-- Server health details sections  start here -->
			<%-- <div class="col-md-12 padding0" >
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
								<div id="server_instance_health" style="width:90%;">
        						</div>
							</div>
						</div>	
					</div>
			</div> --%>
		
	</div>
</div>	
<script lang="javascript" type="text/javascript">
  
  function loadAllChart(){
		//backlog status Chart Code start here
	  $("#server_instance_backlog_status").highcharts({
        chart: {
            type: 'column'
        },
        title: {
            text: selectedInstanceName
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
                name: 'Distribution Servie Path 1 ',
                y: 10000,
                drilldown: null,
                color: '#330033'
            }, {
                name: 'Collection Path 2',
                y: 2000,
                drilldown: null,
                color: '#330033'
            }, {
                name: 'Collection Path 1 ',
                y: 1000,
                drilldown: null,
                color: '#330033'
            }, {
                name: 'Processing Path 1',
                y: 100,
                drilldown: null,
                color: '#330033'
            }, {
                name: 'Parsing Path 1',
                y: 75,
                drilldown: null,
                color: '#330033'
            }]
        }]
    });
	
	//backlog status Chart Code end here
	// File Processing Status code start here
		$('#server_instance_file_process_status').highcharts({
	        chart: {
	            type: 'column'
	        },
	        title: {
	            text: selectedInstanceName
	        },
	        xAxis: {
	            categories: ['Distribution Service', 'Parsing Service', 'Processing Service', 'Collection Service']
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
	
	// TPS Line chart start here
$('#server_instance_tps_chart').highcharts({
    chart: {
        type: 'spline'
    },
    title: {
        text: selectedInstanceName
    },
    xAxis: {
        type: 'datetime',
        dateTimeLabelFormats: { // don't display the dummy year
        	hour: '%I %p',
            minute: '%I:%M %p'
           
        },
        title: {
            text: 'Date'
        }
    },
    yAxis: {
        title: {
            text: 'TPS'
        },
        min: 0
    },
    tooltip: {
        headerFormat: '<b>{series.name}</b><br>',
        pointFormat: '{point.x:%e. %b}: {point.y:.2f}'
    },

    plotOptions: {
        spline: {
            marker: {
                enabled: true
            }
        }
    },

    series: [{
        name: "TPS Observed",
        // Define the data points. All series have a dummy year
        // of 1970/71 in order to be compared on the same x axis. Note
        // that in JavaScript, months start at 0 for January, 1 for February etc.
        data: [
				[Date.UTC(2015, 11, 1, 1, 15), 12030], [Date.UTC(2015, 11, 1, 2, 15), 9870],
				[Date.UTC(2015, 11, 1, 3, 15), 12300], [Date.UTC(2015, 11, 1, 4, 15), 15000],
				[Date.UTC(2015, 11, 1, 5, 15), 12500], [Date.UTC(2015, 11, 1, 6, 15), 11600],
				[Date.UTC(2015, 11, 1, 7, 15), 10500], [Date.UTC(2015, 11, 1, 8, 15), 10000],
				[Date.UTC(2015, 11, 1, 9, 15), 8900], [Date.UTC(2015, 11, 1, 10, 15), 7900],
				[Date.UTC(2015, 11, 1, 11, 15), 6599], [Date.UTC(2015, 11, 1, 12, 15), 5406]
        ]
    }, {
        name: "TPS Committed ",
        data: [
				[Date.UTC(2015, 11, 1, 1, 15), 10000], [Date.UTC(2015, 11, 1, 2, 15), 10000],
				[Date.UTC(2015, 11, 1, 3, 15), 10000], [Date.UTC(2015, 11, 1, 4, 15), 10000],
				[Date.UTC(2015, 11, 1, 5, 15), 10000], [Date.UTC(2015, 11, 1, 6, 15), 10000],
				[Date.UTC(2015, 11, 1, 7, 15), 10000], [Date.UTC(2015, 11, 1, 8, 15), 10000],
				[Date.UTC(2015, 11, 1, 9, 15), 10000], [Date.UTC(2015, 11, 1, 10, 15), 10000],
				[Date.UTC(2015, 11, 1, 11, 15), 10000], [Date.UTC(2015, 11, 1, 12, 15), 10000]
        ]
    }]
});
// TPS Line chart end here
  }
    

	$(document).ready(function () {
		loadAllChart();
	});

	
	
	
</script>
