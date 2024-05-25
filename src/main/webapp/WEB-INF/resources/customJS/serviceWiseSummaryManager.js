
function generateReport(){
	$("#instanceList").GridUnload();
	if(validateReportParameters()){
		if(isServicePacketBased($("#serviceInstanceId").val())){
			getServerInstanceListForPacketBasedService();
		}else{
			getServerInstanceListForFileBasedService();
		}
	}
}

function validateOtherReportParameters(){
	if($("#reporttypeid").val() === '-1'){
		showErrorMsg(jsSpringMsg.invalidreporttypeid);
		return false;
	}else{
		var type = $("#reporttypeid").val();
		if(type.toUpperCase() === "HOURLY"){
			if($("#hourlyDate").val() === ''){
				showErrorMsg(jsSpringMsg.invalidhourly_date);
				return false;
			}
		}else if(type.toUpperCase() === "DAILY"){
			if($("#dailyOptionsid").val() === '-1'){
				showErrorMsg(jsSpringMsg.invaliddailyOptionsid);
				return false;
			}else{
				var dailyType = $("#dailyOptionsid").val();
				if(dailyType==1){
					if($("#dailyMonthid").val() === '-1'){
						showErrorMsg(jsSpringMsg.invaliddailyMonthid);
						return false;
					}
					if($("#dailyyearid").val() === '-1'){
						showErrorMsg(jsSpringMsg.invaliddailyyearid);
						return false;
					}
				}else{
					if($("#dailyCustomDateFrom").val() === ''){
						showErrorMsg(jsSpringMsg.invaliddaily_custom_date_from);
						return false;
					}
					if($("#dailyCustomDateTo").val() === ''){
						showErrorMsg(jsSpringMsg.invaliddaily_custom_date_to);
						return false;
					}
				}
			}
		}else if(type.toUpperCase() === "MONTHLY"){
			if($("#monthlyoptionsid").val() === '-1'){
				showErrorMsg(jsSpringMsg.invalidmonthlyoptionsid);
				return false;
			}else{
				var monthType = $("#monthlyoptionsid").val();
				if(monthType==1){
					if($("#monthlyyearid").val() === '-1'){
						showErrorMsg(jsSpringMsg.invalidmonthlyyearid);
						return false;
					}
				}else{
					if($("#monthlymonth1id").val() === '-1'){
						showErrorMsg(jsSpringMsg.invalidmonthlymonth1id);
						return false;
					}
					if($("#monthlyyear1id").val() === '-1'){
						showErrorMsg(jsSpringMsg.invalidmonthlyyear1id);
						return false;
					}
					if($("#monthlymonth2id").val() === '-1'){
						showErrorMsg(jsSpringMsg.invalidmonthlymonth2id);
						return false;
					}
					if($("#monthlyyear2id").val() === '-1'){
						showErrorMsg(jsSpringMsg.invalidmonthlyyear2id);
						return false;
					}
				}
			}
		}
	}
	 resetWarningDisplay();
	 clearAllMessages();
	return true;
}
function dateDiff(d2, d1)
{
	    var d1year = d1.getFullYear();
	    var d2Year = d2.getFullYear();
	    var age = d2Year - d1year;
	    return age;
}


function validateDateParametersForDownload(){
	var type = $("#reporttypeid").val();
	if(type.toUpperCase() === "DAILY"){
		var dailyType = $("#dailyOptionsid").val();
		if(dailyType==2){
			var startDate =  Date.parse($('#dailyCustomDateFrom').val());
			var endDate =  Date.parse($('#dailyCustomDateTo').val());
			if(endDate < startDate){
				showErrorMsg(jsSpringMsg.startDateGreaterThanEndDate);
				return false;
			}else{
				//check if diff between the dates is greater than one year
				if(dateDiff(new Date(endDate),new Date(startDate)) > 1){
					showErrorMsg(jsSpringMsg.startDateGreaterThanEndDate);
					return false;
				}
			}
		}
	}else if(type.toUpperCase() === "MONTHLY"){
		var monthType = $('#monthlyoptionsid').val();
		if(monthType==2){
			if (($('#monthlyyear1id').val()) > ($('#monthlyyear2id').val())){
				showErrorMsg(jsSpringMsg.startYearIsGreater);
				return false;
			}else if(($('#monthlyyear1id').val() !== -1) && ($('#monthlyyear2id').val()!== -1) && ($('#monthlyyear1id').val() === ($('#monthlyyear2id').val()))){
				if($('#monthlymonth1id').val() > $('#monthlymonth2id').val()) {
					showErrorMsg(jsSpringMsg.startMonthIsGreater);
					return false;
				}
			}else{
				//check if diff between the dates is greater than one year
				var Date1 = new Date($('#monthlyyear1id').val(), $('#monthlymonth1id').val(), 1);
		        var Date2 = new Date($('#monthlyyear2id').val(), $('#monthlymonth2id').val() + 1, 0);
				if(dateDiff(new Date(Date2),new Date(Date1)) > 1){
					showErrorMsg(jsSpringMsg.startDateGreaterThanEndDate);
					return false;
				}
			}
	}
	}	
	return true;
}


function isServicePacketBased(serviceInstanceId){
	var trueVal = 1;
	var falseVal = 0;
	if(serviceInstanceId == "RADIUS_COLLECTION_SERVICE" ||
	serviceInstanceId == "GTPPRIME_COLLECTION_SERVICE" ||
	serviceInstanceId == "NATFLOW_COLLECTION_SERVICE" ||
	serviceInstanceId == "NATFLOWBINARY_COLLECTION_SERVICE" ||
	serviceInstanceId == "SYSLOG_COLLECTION_SERVICE") {
		return Boolean(trueVal);
	} else {
		return Boolean(falseVal);
	}
}

function getDurationForMonthly(durationType){
	resetAllFieldsSetPreviously();
	var type = durationType.value;
	if(type==1){
		$('#monthly_month1').hide();
		$('#monthly_year1').hide();
		$('#monthly_month2').hide();
		$('#monthly_year2').hide();
		$('#monthly_year').show();
		
	}else if(type==2){
		$('#monthly_month1').show();
		$('#monthly_year1').show();
		$('#monthly_month2').show();
		$('#monthly_year2').show();
		$('#monthly_year').hide();
	}
}
function getDurationForDaily(durationType){
	resetAllFieldsSetPreviously();
	var type = durationType.value;
	if(type==1){
		$('#daily_month').show();
		$('#daily_year').show();
		$('#daily_custom_date_to').hide();
		$('#daily_custom_date_from').hide();
		
	}else if(type==2){
		$('#daily_month').hide();
		$('#daily_year').hide();
		$('#daily_custom_date_to').show();
		$('#daily_custom_date_from').show();
	}
}
function resetAllFieldsSetPreviously(){
	$('#dailyMonthid').val(-1);
	
	$('#dailyyearid').val(-1);
	$('#dailyCustomDateTo').val('');
	$('#dailyCustomDateFrom').val('');
	$('#monthlyyear1id').val(-1);
	$('#monthlymonth1id').val(-1);
	$('#monthlyyear2id').val(-1);
	$('#monthlymonth2id').val(-1);
	$('#monthlyyearid').val(-1);
	$('#hourlyDate').val('');
}
function getFields(reportType){
	var type = reportType.value;
	resetAllFieldsSetPreviously();
	document.getElementById("daily_options").value = -1;
	document.getElementById("monthly_options").value =-1;
	if(type.toUpperCase() === "HOURLY"){
		$('#hourlyDate').val('');
		$('#hourly_date').show();
		$('#daily_options').hide();
		$('#monthly_options').hide();
		$('#daily_month').hide();
		$('#daily_year').hide();
		$('#daily_custom_date_to').hide();
		$('#daily_custom_date_from').hide();
		$('#monthly_year1').hide();
		$('#monthly_month1').hide();
		$('#monthly_year2').hide();
		$('#monthly_month2').hide();
		$('#monthly_year').hide();
	}else if(type.toUpperCase() === "DAILY"){
		$('#hourly_date').hide();
		$('#daily_options').show();
		$('#dailyOptionsid').val(-1);
		$('#daily_month').hide();
		$('#daily_year').hide();
		$('#daily_custom_date_to').hide();
		$('#daily_custom_date_from').hide();
		$('#monthly_options').hide();
		$('#monthly_year1').hide();
		$('#monthly_month1').hide();
		$('#monthly_year2').hide();
		$('#monthly_month2').hide();
		$('#monthly_year').hide();
	}else if(type.toUpperCase() === "MONTHLY"){
		$('#hourly_date').hide();
		$('#daily_options').hide();
		$('#daily_month').hide();
		$('#daily_year').hide();
		$('#daily_custom_date_to').hide();
		$('#daily_custom_date_from').hide();
		$('#monthlyoptionsid').val(-1);
		$('#monthly_options').show();
		$('#monthly_year1').hide();
		$('#monthly_month1').hide();
		$('#monthly_year2').hide();
		$('#monthly_month2').hide();
		$('#monthly_year').hide();
	}	
}
 
 function exportData(cols,file){
     var serverInstanceSelectedList = $('#serverInstanceId').val()+'';
     document.misReport_download_form.fileType.value=file;
     document.misReport_download_form.reportType.value=$("#reporttypeid").val();
     document.misReport_download_form.serverInstancelst.value=serverInstanceSelectedList;
     document.misReport_download_form.serviceInstanceId.value=$("#serviceInstanceId").val();
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
     document.misReport_download_form.loggedInStaffName.value='<%= new EliteUtils().getUserNameOfUser(request) %>';
     document.misReport_download_form.method='POST';
     document.misReport_download_form.action='<%=request.getContextPath()%>/<%= ControllerConstants.DOWNLOAD_MIS_REPORT %>';  // send it to server which will open this contents in excel file
     document.misReport_download_form.submit();  
 }
 
function clearInstanceGrid(){
	var $grid = $("#instanceList");
	var rowIds = $grid.jqGrid('getDataIDs');
	// iterate through the rows and delete each of them
	for(var i=0,len=rowIds.length;i<len;i++){
		var currRow = rowIds[i];
		$grid.jqGrid('delRowData', currRow);
	}
}

function getServerList(serviceInstanceId){
	
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
				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				}else{
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