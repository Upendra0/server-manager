 	function validate()
		{		
			var checked = $("input[type=checkbox]:checked").length;
			      if(checked===0) {
			    	  $("#checkboxServer").click();
			        return false;
			      }
			      else
			      {
			    	  var selected = [];
			    	  $('input[type=checkbox]:checked').each(function() {
			    		  selected.push($(this).attr('value'));
			    	   });
			    	  createServerManagerTrialLicense(selected); ;
			     }
		}
		
function createServerManagerTrialLicense(productType) {
	progrssAndButtonDisable();
	$.ajax({
		url : 'activateTrialLicense',
		cache : false,
		async : true,
		dataType : 'json',
		type : "POST",
		data : {
			"id" : '0',
			"location" : '',
			"customer" : '',
			"licenceType" : 'TRIAL',
			"productType" : productType.toString()
		},
		success : function(data) {
			var response = data;
			var responseCode = response.code;
			var responseMsg = response.msg;
			if (responseCode === "200") {
				stopProgressBar();
				displayLicenseSuccessMessage(responseMsg);
			} else {
				progressAndButtonEnable();
				showErrorMsg(responseMsg);
			}
		},
		error : function(xhr, st, err) {
			stopProgressBar();
			handleGenericError(xhr, st, err);
		}
	});
}
function progrssAndButtonDisable() {
	$("#trialLicenseNextBtnDiv").hide();
	$("#trial-license-add-progress-bar-div").show();
}

function progressAndButtonEnable() {
	$("#trialLicenseNextBtnDiv").show();
	$("#trial-license-add-progress-bar-div").hide();
}

function stopProgressBar() {
	$("#trial-license-add-progress-bar-div").hide();
}

function downloadLicenseRegistrationForm() {
	$("#license_download_form").submit();
}



var files;
$(document).on("change", "#licenseActivationKey", function(event) {
	files = event.target.files;
	$('#licenseActivationKey').html(files[0].name);
});
	
function clearFileContent(){
	 $("#licenseActivationKey").val('');
	 $("#licenseActivationKey").html('');
}	

function activateFullLicense(urlAction,componentType){
	clearAllMessages();
	var selectedProdct = $("input[type=checkbox]:checked").length;
	var file = $("#licenseActivationKey").val();
	var hostIP = $("#hostIP").val();
	
	if (selectedProdct === 0) {
		$("#checkboxServer").click();
		return false;
	}else if(file === ''){
		showErrorMsg(jsSpringMsg.fileNotSelectMsg);
		return false;
	}else{
		var productList = [];
		$('input[type=checkbox]:checked').each(function() {
			productList.push($(this).attr('value'));
		});
		
		var oMyForm = new FormData();
	    oMyForm.append("file", files[0]);
	    oMyForm.append("productTypes", productList.toString());
	    oMyForm.append("hostIP", hostIP);
	    
	    $('#activate_full_license_processbar').show();
	    $('#activate_full_license_btn').hide();
		  $.ajax({dataType : 'json',
	          url : urlAction,
	          data : oMyForm ,
	          type : "POST",
	          enctype: 'multipart/form-data',
	          processData: false,
	          contentType:false,
	          success : function(response) {
	  			var responseCode = response.code;
	  			var responseMsg = response.msg;
	  			if(responseCode === "200"){
				    $('#activate_full_license_processbar').hide();
	  			    $('#activate_full_license_btn').show();
	  			    var successMsg;
	  			    if (componentType == 'SERVER_MANAGER') {
	  			    	successMsg = jsSpringMsg.fullLicenseSuccessMsg;
	  			    } else {
	  			    	successMsg = responseMsg;
	  			    	showSuccessMsg(successMsg);
	  			    }
	  			    displayLicenseSuccessMessage(successMsg);
				}else{
					 $('#activate_full_license_processbar').hide();
		  			 $('#activate_full_license_btn').show();
					showErrorMsg(responseMsg);
				}
	          },
	          error : function(){
	        	  $('#activate_full_license_processbar').hide();
	  			  $('#activate_full_license_btn').show();
	          }
	      });	
	}
}

function activateEngineFullLicense(urlAction, serverInstanceId){
	clearAllMessages();
	var selectedProdct = $("input[type=checkbox]:checked").length;
	var file = $("#licenseActivationKey").val();
	
	if (selectedProdct === 0) {
		$("#checkboxServer").click();
		return false;
	}else if(file === ''){
		showErrorMsg(jsSpringMsg.fileNotSelectMsg);
		return false;
	}else{
		var productList = [];
		$('input[type=checkbox]:checked').each(function() {
			productList.push($(this).attr('value'));
		});
		
		var oMyForm = new FormData();
	    oMyForm.append("file", files[0]);
	    oMyForm.append("productTypes", productList.toString());
	    oMyForm.append("serverInstanceId", serverInstanceId);
	    oMyForm.append("serverId", serverInstanceId);
	    
	    $('#activate_full_license_processbar').show();
	    $('#activate_full_license_btn').hide();
		  $.ajax({dataType : 'json',
	          url : urlAction,
	          data : oMyForm ,
	          type : "POST",
	          enctype: 'multipart/form-data',
	          processData: false,
	          contentType:false,
	          success : function(response) {
	  			var responseCode = response.code;
	  			var responseMsg = response.msg;
	  			if(responseCode === "200"){
				    $('#activate_full_license_processbar').hide();
	  			    $('#activate_full_license_btn').show();
	  			    showSuccessMsg(jsSpringMsg.engineFullLicenseSuccessMsg);
	  			    $("#activate_full_license_btn").attr('disabled','disabled');
	  			    $('#activate_full_license_btn_apply').attr('disabled',true);
	  			  
				}else{
					 $('#activate_full_license_processbar').hide();
		  			 $('#activate_full_license_btn').show();
					showErrorMsg(responseMsg);
				}
	          },
	          error : function(){
	        	  $('#activate_full_license_processbar').hide();
	  			  $('#activate_full_license_btn').show();
	          }
	      });	
	}
}

function activateLicenseContainer(urlAction,componentType){
	clearAllMessages();
	var file = $("#licenseActivationKey").val();
	var hostIP = $("#hostIP").val();
	
	if(file === ''){
		showErrorMsg(jsSpringMsg.fileNotSelectMsg);
		return false;
	}else{
		var oMyForm = new FormData();
	    oMyForm.append("file", files[0]);
	    oMyForm.append("hostIP", hostIP);
	    
	    $('#activate_full_license_processbar').show();
	    $('#activate_full_license_btn').hide();
		  $.ajax({dataType : 'json',
	          url : urlAction,
	          data : oMyForm ,
	          type : "POST",
	          enctype: 'multipart/form-data',
	          processData: false,
	          contentType:false,
	          success : function(response) {
	  			var responseCode = response.code;
	  			var responseMsg = response.msg;
	  			if(responseCode === "200"){
				    $('#activate_full_license_processbar').hide();
	  			    $('#activate_full_license_btn').show();
	  			    var successMsg;
	  			    successMsg = responseMsg;
	  			    showSuccessMsg(successMsg);
	  			    displayLicenseSuccessMessage(successMsg);
				}else{
					 $('#activate_full_license_processbar').hide();
		  			 $('#activate_full_license_btn').show();
					showErrorMsg(responseMsg);
				}
	          },
	          error : function(){
	        	  $('#activate_full_license_processbar').hide();
	  			  $('#activate_full_license_btn').show();
	          }
	      });	
	}
}

function displayLicenseSuccessMessage(licenseMsg){
	$("#license_sucess_message").html(licenseMsg);
	$("#checkboxServerSuccess").click();
}

function downloadEngineLicenseRegistrationForm(){
	$("#license_engine_download_form").submit();
}

function redirectLogIn(){
	$("#login_page").submit();
	clearAllMessages();
}