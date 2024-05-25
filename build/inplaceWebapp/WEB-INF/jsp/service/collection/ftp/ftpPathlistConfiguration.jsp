<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
.checkbox{
	color: #000 !important;
}
.btn .caret {
    margin-left: -10px;
}
.btn-group{
	width: 100% !important;
}
.btn-group button{
	width: 100% !important;
}
.btn-default span{
	width: 100% !important;
	background:none !important ;
}
.multiselect-clear-filter {
	line-height: 1.9;
}
.ui-jqgrid .ui-jqgrid-bdiv { overflow-y: scroll;  max-height: 270px; }
</style>
<script>
    var currentSelectedPathListID;
	var pathListCounter=-1;
	var serviceDbStats='${serviceDbStats}';
	var forDuplicateEnabled = '${forDuplicatEnabled}';
</script>

<script>
function formatDuplicateCheckDropdown(counter){

	var ddId = counter+"_duplicateCheckParamName";
	var inpId = counter+"_timeInterval";
	$('#'+inpId).prop("disabled", false);
	var x = $('#'+ddId);
	x.multiselect({
		enableFiltering: true,
		enableHTML : true,
		maxHeight: 200,
		dropDown: true
	});
	
	if((serviceDbStats == 'true' || serviceDbStats == true) && 
			(forDuplicateEnabled == 'true' || forDuplicateEnabled == true)){
		$('#'+ddId).multiselect("enable");
	}else{
		$('#'+ddId).multiselect("disable");
	}
	
};

function setValueInMultiSelect(counter,value){
	
	if(value === null || value === '' || value === 'undefined '){
		value = 'name';
	}
	var element = $('#'+counter+'_duplicateCheckParamName');
    var valArr = value.split(",");
    var i = 0, size = valArr.length;
    for (i; i < size; i++) {
    	element.multiselect('select', valArr[i]);
    	if(valArr[i] == 'name' || valArr[i] == 'NAME' ){
    		$("#NAME").prop("disabled", true);
    	}
    	
    }
    element.multiselect('select', 'name');
    
   
    /** Edited for dropdown button ID,Duplicate Check Criteria **/
    var multiselectbtn = document.querySelector(".multiselect");
    multiselectbtn.setAttribute("id", counter + "_btn_duplicateCheck");    
    $('#' + counter + '_btn_duplicateCheck').parent().children(".multiselect-container").attr("id", counter + "_multiselect_menu");
    $( '#' + counter + '_btn_duplicateCheck' ).click(function(){
    	var showhide = $( '#' + counter + '_multiselect_menu').css("display");  // $(".multiselect-container").css("display");
	    	if(showhide == "none" || showhide == null){
	    		$('#' + counter + '_multiselect_menu').show();
	    	}else {
	    		$('#' + counter + '_multiselect_menu').hide();
	    	}
    	});
    /**End of Edited for dropdown button ID **/
}

var prevFilterActionArray = new Array();

function changeFilterAction(pathListCounter){

	resetWarningDisplay();
	$("#"+pathListCounter+"_remoteFileActionParamName").prop('disabled', true);
	$("#"+pathListCounter+"_remoteFileActionParamNameTwo").prop('disabled', true);
	$("#"+pathListCounter+"_renamediv").hide();
	var filterActionEnable=$("#"+pathListCounter+"_remoteFileAction").find(":selected").text();
	prevFilterActionArray.push(filterActionEnable);
	if(prevFilterActionArray.length > 1){
		var lengthOfArray = prevFilterActionArray.length;
		var valueOne = $('#' + pathListCounter + '_remoteFileActionValue').val();
		var valueTwo = $('#' + pathListCounter + '_remoteFileActionValueTwo').val();
		if(prevFilterActionArray[lengthOfArray - 2] == 'MOVE' && !filterActionEnable == 'MOVE'){
			if(filterActionEnable == 'RENAME'){
				$('#' + pathListCounter + '_remoteFileActionValue').val('')
			} else if(filterActionEnable == 'MOVEANDRENAME'){
				$('#' + pathListCounter + '_remoteFileActionValue').val(valueOne);
			}
		} else if (prevFilterActionArray[lengthOfArray - 2] == 'RENAME' && !filterActionEnable == 'RENAME'){
			$('#' + pathListCounter + '_remoteFileActionValue').val('');
			if(filterActionEnable == 'MOVEANDRENAME'){
				$('#' + pathListCounter + '_remoteFileActionValueTwo').val(valueOne);
			}
		} else if(prevFilterActionArray[lengthOfArray - 2] == 'MOVEANDRENAME' && !filterActionEnable == 'MOVEANDRENAME'){
			if(filterActionEnable == 'RENAME'){
				$('#' + pathListCounter + '_remoteFileActionValue').val(valueTwo);
			} else if(filterActionEnable == 'MOVE'){
				$('#' + pathListCounter + '_remoteFileActionValue').val(valueOne);
			}
		}
	}
	if (filterActionEnable == 'MOVE'){
		$('#' + pathListCounter + '_remoteFileActionParamName').val('destpath');
		$('#' + pathListCounter + '_remoteFileActionValueTwo').val('');
		$('#' + pathListCounter + '_remoteFileActionParamNameTwo').val('');
		$('#' + pathListCounter + '_remoteFileActionValue').prop('readonly', false);
		$('#' + pathListCounter + '_destionationpathicon').show();
		$('#' + pathListCounter + '_dBstatsDisableMessage').hide();
		$('#' + pathListCounter + '_validFileTimeInterval').prop('readonly', true);

	}
	else if (filterActionEnable == 'RENAME'){
		$('#' + pathListCounter + '_remoteFileActionValueTwo').val('');
		$('#' + pathListCounter + '_remoteFileActionParamNameTwo').val('');
		$('#' + pathListCounter + '_remoteFileActionParamName').val('ext');
		$('#' + pathListCounter + '_remoteFileActionValue').prop('readonly', false);
		$('#' + pathListCounter + '_destionationpathicon').hide();
		$('#' + pathListCounter + '_dBstatsDisableMessage').hide();
		$('#' + pathListCounter + '_validFileTimeInterval').prop('readonly', true);

	}
	else if (filterActionEnable == 'NA'){
	    $('#' + pathListCounter + '_validFileTimeInterval').prop('readonly', false);
		$('#' + pathListCounter + '_remoteFileActionParamName').val('na');
		$('#' + pathListCounter + '_remoteFileActionValue').val('');
		$('#' + pathListCounter + '_remoteFileActionValueTwo').val('');
		$('#' + pathListCounter + '_remoteFileActionParamNameTwo').val('');
		$('#' + pathListCounter + '_remoteFileActionValue').prop('readonly', true);
		$('#' + pathListCounter + '_remoteFileActionValueTwo').prop('readonly', true);
		$('#' + pathListCounter + '_destionationpathicon').hide();
		var serviceDbStats='${serviceDbStats}';
		
		if(serviceDbStats == 'false'){
			$('#' + pathListCounter + '_dBstatsDisableMessage').show();
		}
		
	}else if(filterActionEnable == 'MOVEANDRENAME'){
		
		$("#"+pathListCounter+'_renamediv').show();
		$('#' + pathListCounter + '_remoteFileActionParamName').val('destpath');
		$('#' + pathListCounter + '_remoteFileActionParamNameTwo').val('ext');
		$('#' + pathListCounter + '_destionationpathicon').show();
		$('#' + pathListCounter + '_remoteFileActionValue').prop('readonly', false);
		$('#' + pathListCounter + '_remoteFileActionValueTwo').prop('readonly', false);
		$('#' + pathListCounter + '_dBstatsDisableMessage').hide();
		$('#' + pathListCounter + '_validFileTimeInterval').prop('readonly', true);
		
	} else if(filterActionEnable == 'DELETE'){
		
		$('#' + pathListCounter + '_remoteFileActionParamName').val('delete');
		$('#' + pathListCounter + '_remoteFileActionValue').val('');
		$('#' + pathListCounter + '_remoteFileActionValueTwo').val('');
		$('#' + pathListCounter + '_remoteFileActionValue').prop('readonly', true);
		$('#' + pathListCounter + '_remoteFileActionValueTwo').prop('readonly', true);
		$('#' + pathListCounter + '_destionationpathicon').hide();
		$('#' + pathListCounter + '_validFileTimeInterval').prop('readonly', true);

	}
}

function changeFileSeqParam(pathListCounter){

	var fileSeqEnable=$("#"+pathListCounter+"_fileSeqAlertEnabled").find(":selected").val();
	
	if(fileSeqEnable == 'false'){
		$("#"+pathListCounter+"_seqStartIndex").prop('readonly', true);
		$("#"+pathListCounter+"_seqEndIndex").prop('readonly', true);
		//$("#"+pathListCounter+"_maxCounterLimit").prop('readonly', true);
	}else{
		$("#"+pathListCounter+"_seqStartIndex").prop('readonly', false);
		$("#"+pathListCounter+"_seqEndIndex").prop('readonly', false);
		//$("#"+pathListCounter+"_maxCounterLimit").prop('readonly', false);
	}
}
function changeFileSizeCheck(pathListCounter){

	var fileSeqEnable=$("#"+pathListCounter+"_fileSizeCheckEnabled").find(":selected").val();
	
	if(fileSeqEnable == 'false'){
		$("#"+pathListCounter+"_fileSizeCheckMinValue").prop('readonly', true);
		$("#"+pathListCounter+"_fileSizeCheckMaxValue").prop('readonly', true);
	}else{
		$("#"+pathListCounter+"_fileSizeCheckMinValue").prop('readonly', false);
		$("#"+pathListCounter+"_fileSizeCheckMaxValue").prop('readonly', false);
	}
}
function changeFileDateParam(pathListCounter){
	
	var fileDateEnable=$("#"+pathListCounter+"_fileGrepDateEnabled").find(":selected").val();
	
	if(fileDateEnable == 'false'){
		$("#"+pathListCounter+"_dateFormat").prop('readonly', true);
		$("#"+pathListCounter+"_position").prop('disabled', true);
		$("#"+pathListCounter+"_startIndex").prop('readonly', true);
		$("#"+pathListCounter+"_endIndex").prop('readonly', true);
	}else{
		$("#"+pathListCounter+"_dateFormat").prop('readonly', false);
		$("#"+pathListCounter+"_position").prop('disabled', false);
		$("#"+pathListCounter+"_startIndex").prop('readonly', false);
		$("#"+pathListCounter+"_endIndex").prop('readonly', false);
	}
	
}

function changeMissingFileSequenceParams(pathListCounter){
	
	var missingFileSequenceEnabled = $("#"+pathListCounter+"_fileSeqAlertEnabled").find(":selected").val();
	if(missingFileSequenceEnabled == 'false'){

		$("#"+pathListCounter+"_missingFileSequenceId_minValue").prop('readonly', true);
		$("#"+pathListCounter+"_missingFileSequenceId_maxValue").prop('readonly', true);
		$("#"+pathListCounter+"_resetFrequency").prop('disabled', true); 
		$("#"+pathListCounter+"_seqStartIndex").prop('readonly', true);
		$("#"+pathListCounter+"_seqEndIndex").prop('readonly', true);
	}else {

		$("#"+pathListCounter+"_missingFileSequenceId_minValue").prop('readonly', false);
		$("#"+pathListCounter+"_missingFileSequenceId_maxValue").prop('readonly', false);
		$("#"+pathListCounter+"_resetFrequency").prop('disabled', false); 
		$("#"+pathListCounter+"_seqStartIndex").prop('readonly', false);
		$("#"+pathListCounter+"_seqEndIndex").prop('readonly', false);
	}
}
	
function addNewPathList(){
	pathListCounter++;
	
	var pathListTable = "<form method='POST' id='" + pathListCounter + "_form'> "+ 
						 "<input type='hidden' id='driverId' name='driverId' value='${driverId}'/>"+
						 "<input type='hidden' id='"+pathListCounter+"_id' name='"+pathListCounter+"_id'> "+
						 "<input type='hidden' id='"+pathListCounter+"_pathId' name='"+pathListCounter+"_pathId'> "+
						 "<input type='hidden' id='"+pathListCounter+"_pathListCount' name='pathListCount' value='"+pathListCounter+"'/>"+
						 "<div class='box box-warning' id='"+ pathListCounter + "_block'>"+
					    "	<div class='box-header with-border'> "+
					    "	<h3 class='box-title' id='title_"+pathListCounter+ "'><spring:message code='ftp.driver.mgmt.pathlist.path.dtl.header'></spring:message></h3>"+
					    "		<div class='box-tools pull-right' id='action_"+pathListCounter+ "'>"+
					    "			<button class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' data-parent='#pathListConfig' href='#pathList_" + pathListCounter + "_block'><i class='fa fa-minus'></i></button>	"+
					    "			<sec:authorize access='hasAuthority(\'DELETE_PATHLIST\')'>"+
					    "				&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;' onclick=deletePathListPopup(\'"+pathListCounter+"\');></a>&nbsp;" +
					    "			</sec:authorize>"+
					    "		</div>"+
					    "	</div>	"+
					    "	<div class='box-body inline-form accordion-body collapse in' id='pathList_"+pathListCounter +"_block'>"+
					    "		<div class='col-md-6 no-padding'>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.path.dtl.alias' var='label'></spring:message>"+
				        "	<spring:message code='ftp.driver.mgmt.pathlist.path.dtl.alias.tooltip' var='tooltip'></spring:message>"+
					    "  		<div class='form-group'>"+
					    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
					    "      		<div class='input-group '>"+
					    "					<input class='form-control table-cell input-sm' id='" + pathListCounter + "_name' tabindex='1' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}'/>"+
						"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
					    "       		</div>"+
					    "   		</div>"+
					    "		</div>"+
					    
 

					    
						" <div class='col-md-6 no-padding'>"+ 
						"		 <spring:message code='iplog.parsing.service.pathlist.file.pattern' var='tooltip'></spring:message>"+ 
						"		 <div class='form-group'>" +
						"			 <div class='table-cell-label'>${tooltip}</div>"+ 
						"			 <div class='input-group'>" +
						"				 <input tabindex='4' id='" + pathListCounter + "_fileNamePattern' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom' title='${tooltip}'/>"+ 
						"				 <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>" +
						"			 </div>" +
						"        </div>" +
						"    </div>" +					    

						
					    "		<div class='col-md-6 no-padding'>"+
					    "			<spring:message code='ftp.driver.mgmt.pathlist.read.file.loc.label' var='label'></spring:message>"+
					    "			<spring:message code='ftp.driver.mgmt.pathlist.read.file.loc.tooltip' var='tooltip'></spring:message>"+
					    "			<div class='form-group'>"+
					    "				<div class='table-cell-label'>${label}<span class='required'>*</span>&nbsp;<i class='fa fa-square'></i></div>"+
					    "				<div class='input-group'>"+
					    "					<input class='form-control table-cell input-sm' id='" + pathListCounter + "_readFilePath' tabindex='2' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+
						"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
					    "				</div>"+
					    "			</div>"+
					    "		</div>"+
					    "		<div class='col-md-6 no-padding'>"+
					    "			<spring:message code='ftp.driver.mgmt.pathlist.write.file.loc' var='tooltip'></spring:message>"+
					    "  		<div class='form-group'>"+
					    "   			<div class='table-cell-label'>${tooltip}<span class='required'>*</span>&nbsp;<i class='fa fa-square'></i></div>"+
					    "      		<div class='input-group '>"+
					    "      			<input class='form-control table-cell input-sm' id='" + pathListCounter + "_writeFilePath' tabindex='3' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+
						"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "       		</div>"+
					    "     		</div>"+
					    "		</div>"+
					    "		<div class='col-md-6 no-padding'>"+
					    "			<spring:message code='ftp.driver.mgmt.pathlist.file.prefix.r' var='label'></spring:message>"+
					    "			<spring:message code='ftp.driver.mgmt.pathlist.file.prefix.r.tooltip' var='tooltip'></spring:message>"+
					    "  		<div class='form-group'>"+
					    "    			<div class='table-cell-label'>${label}</div>"+
					    "      		<div class='input-group '>"+
					    "      			<input class='form-control table-cell input-sm' id='" + pathListCounter + "_readFilenamePrefix' tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+
					    "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "     			</div>"+
					    "     		</div>"+
					    "		</div>"+
					    "		<div class='col-md-6 no-padding'>"+
					    "			<spring:message code='ftp.driver.mgmt.pathlist.file.suffix.r' var='label'></spring:message>"+
					    "			<spring:message code='ftp.driver.mgmt.pathlist.file.suffix.r.tooltip' var='tooltip'></spring:message>"+
					    "  		<div class='form-group'>"+
					    "    			<div class='table-cell-label'>${label}</div>"+
					    "      		<div class='input-group '>"+
					    "      			<input class='form-control table-cell input-sm' id='" + pathListCounter + "_readFilenameSuffix' tabindex='5' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+
					    "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "       		</div>"+
					    "     		</div>"+
					    "		</div>"+
					    "		<div class='col-md-6 no-padding'>"+
					    "			<spring:message code='ftp.driver.mgmt.pathlist.file.contain.r' var='label'></spring:message>"+
					    "			<spring:message code='ftp.driver.mgmt.pathlist.file.contain.r.tooltip' var='tooltip'></spring:message>"+
					    "  		<div class='form-group'>"+
					    "    			<div class='table-cell-label'>${label}</div>"+
					    "      		<div class='input-group '>"+
					    "      			<input class='form-control table-cell input-sm' id='" + pathListCounter + "_readFilenameContains' tabindex='6' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+
					    "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "        		</div>"+
					    "     		</div>"+
					    "		</div>"+
					    "		<div class='col-md-6 no-padding'>"+
					    "			<spring:message code='ftp.driver.mgmt.pathlist.file.prefix.w' var='label'></spring:message>"+
					    "			<spring:message code='ftp.driver.mgmt.pathlist.file.prefix.w.tooltip' var='tooltip'></spring:message>"+
					    "  		<div class='form-group'>"+
					    "    			<div class='table-cell-label'>${label}</div>"+
					    "      		<div class='input-group '>"+
					    "      			<input class='form-control table-cell input-sm' id='" + pathListCounter + "_writeFilenamePrefix'  tabindex='7' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+
					    "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "        		</div>"+
					    "     		</div>"+
					    "		</div>"+
					    "<div class='col-md-6 no-padding'>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.file.max.count.alert' var='label'></spring:message>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.file.max.count.alert.tooltip' var='tooltip'></spring:message>"+
					    "  	<div class='form-group'>"+
					    "   	<div class='table-cell-label'>${label}</div>"+
					    "   	<div class='input-group '>"+
					    "   		<input class='form-control table-cell input-sm'  id='" + pathListCounter + "_maxFilesCountAlert' tabindex='8' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' onkeydown='isNumericOnKeyDown(event)' value='${maxFilesCountAlert}'/>"+
						"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "      	</div>"+
					    "   </div>"+
					    "</div>"+
					     // add New Pathlist : device Name
				        "<div class='col-md-6 no-padding'> "+
						"	<spring:message code='device.list.grid.device.name.label' var='label'></spring:message> "+ 
						"	<spring:message code='device.list.grid.device.name.label' var='tooltip'></spring:message> "+ 
						"	<div class='form-group'>  "+
						"		<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
						"		<div class='input-group '> "+
						"			<input type='hidden' class='form-control table-cell input-sm' id='"+pathListCounter+"_deviceId'  data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
						"			</input>"+
						"           <a href='#' onclick='selectDevicePopUp("+pathListCounter+");' tabindex='9' style='vertical-align:middle;' id='select_popup_"+ pathListCounter +"_lnk'><i style='margin-top: 8px;margin-left: 8px;' class='glyphicon glyphicon-search'></i></a>" +
						"			<input type='text' class='form-control table-cell input-sm' id='"+pathListCounter+"_parentDevice'  data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' style='width:94%;' readonly=true >"+
						"			</input>"+
						"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
						"		</div> "+
						"	</div> "+
						"</div> "+
						// add New Pathlist : reference devicename
						"<div class='col-md-6 no-padding'>"+
				        "	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.reference.devicename' var='label'></spring:message>"+
				        "	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.reference.devicename.tooltip' var='tooltip'></spring:message>"+
				        "  	<div class='form-group'>"+
				        "    	<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
				        "      	<div class='input-group '>"+
				        "      		<input class='form-control table-cell input-sm' id='" + pathListCounter + "_referenceDevice'  tabindex='10' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' />"+
						"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
				        "        </div>"+
				        "     </div>"+
				        "</div>"+
						
					    
					    //add New Pathlist - missing file seq
					    "<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
			         	"	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.header' var='tooltip'></spring:message>"+
			        	"	<label style='width: 100%;background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
			         	"</div>"+
			         	"<div class='col-md-6 no-padding'>"+
			  	        "	<spring:message code='ftp.driver.mgmt.pathlist.file.seq.alert.enable.label' var='label'></spring:message>"+
			  	        "	<spring:message code='ftp.driver.mgmt.pathlist.file.seq.alert.enable.tooltip' var='tooltip'></spring:message>"+
			  	        "	<div class='form-group'>"+
			  	        "		<div class='table-cell-label'>${label}</div>"+
			  	        "		<div class='input-group '>"+
			  	        " 			<select class='form-control table-cell input-sm' id='"+pathListCounter+"_fileSeqAlertEnabled' onChange='changeMissingFileSequenceParams("+pathListCounter+");'  tabindex='11' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
			 	        "				<c:forEach var='trueFaluseEnum' items='${trueFalseEnum}'>"+
			            " 					<option value='${trueFaluseEnum.name}'>${trueFaluseEnum}</option>" +
			            "	 			</c:forEach>" +
			 	        "			</select>" +
			  			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
			  	        "       </div>"+
			  	        "     </div>"+
			  	        "</div>"+
  	        
			  	        //min max val
			  	        "<div class='col-md-6 no-padding'>"+
			  	      	"	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.range' var='label'></spring:message>"+
			  	     	"	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.range.min' var='minTooltip'></spring:message>"+
			  	     	"	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.range.max' var='maxTooltip'></spring:message>"+
			  	   		"	<div class='form-group'>"+
			  	     	"		<div class='table-cell-label'>${label}</div>"+
			  	     	"		<div class='input-group'>"+
			  	 		"			<div class='col-md-6 no-padding'>"+
			  	 		"				<div class='input-group'>"+
			  	 		"					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_missingFileSequenceId_minValue' tabindex='12' data-toggle='tooltip' type='text' data-placement='bottom' title='${minTooltip}' onkeydown='isNumericOnKeyDown(event)'/>"+
			  	 		"					<span class='input-group-addon add-on last'> <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
			  	 		"				</div>"+
			  	 		"			</div>"+
			
			  	 		"			<div class='col-md-6 no-padding'>"+
			  	 		"				<div class='input-group'>"+ 
			  	 		"					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_missingFileSequenceId_maxValue' tabindex='13' data-toggle='tooltip' type='text' data-placement='bottom' title='${maxTooltip}' onkeydown='isNumericOnKeyDown(event)' />"+
			  	 		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
			  	 		"				</div>"+
			  	 		"			</div>"+
			  	 		"		</div>"+
			  	   		"	</div>"+  
			  	   		"</div>"+ 
						// Reset frequency
						"<div class='col-md-6 no-padding'>"+
			 	 		"	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.frequency' var='label'></spring:message>"+
			 	        "	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.frequency.tooltip' var='tooltip'></spring:message>"+
			 	        "  	<div class='form-group'>"+
			 	        "   	<div class='table-cell-label'>${label}</div>"+
			 	        "   	<div class='input-group'>"+
			 	        " 			<select class='form-control table-cell input-sm' id='"+pathListCounter+"_resetFrequency'  tabindex='14' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
				        "				<c:forEach var='resetFrequency' items='${missingFileFrequency}'>"+
			           	" 					<option value='${resetFrequency.value}'>${resetFrequency}</option>" +
			           	" 				</c:forEach>" +
				        "			</select>" +
			 			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
			 	        "       </div>"+
			 	        "    </div>"+
			 	        "</div>"+
		
			 	        //Seq Start-End Index
				     	"<div class='col-md-6 no-padding'>"+
				     	"	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.startEndIndex' var='label'></spring:message>"+
				     	"	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.startIndex.tooltip' var='startIndexTooltip'></spring:message>"+
				    	"	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.endIndex.tooltip' var='endIndexTooltip'></spring:message>"+
				  	    "	<div class='form-group'>"+
				  	    "		<div class='table-cell-label'>${label}</div>"+
				  	    "		<div class='input-group'>"+
				  		"			<div class='col-md-6 no-padding'>"+
				  	 	"				<div class='input-group'>"+
				  	 	"					<input class='form-control table-cell input-sm' id='" + pathListCounter + "_seqStartIndex' tabindex='15' data-toggle='tooltip' type='text' data-placement='bottom' title='${startIndexTooltip}' onkeydown='isNumericOnKeyDown(event)'/>"+
				  	 	"					<span class='input-group-addon add-on last'> <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
				  	 	"				</div>"+
				  	 	"			</div>"+
				  		"			<div class='col-md-6 no-padding'>"+
				  	 	"				<div class='input-group'>"+ 
				  	 	"					<input class='form-control table-cell input-sm' id='" + pathListCounter + "_seqEndIndex' tabindex='16' data-toggle='tooltip' type='text' data-placement='bottom' title='${endIndexTooltip}' onkeydown='isNumericOnKeyDown(event)' />"+
				  	 	"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
				  	 	"				</div>"+
				  	 	"			</div>"+
				  	 	"		</div>"+
				  	    "	</div>"+  
				  	    "</div>"+ 
				  	    "<div class='col-md-6 no-padding'>"+
				  	    "</div>"+ 
				  	  	"<div class='col-md-6 no-padding'>"+
				  	  	"<span class='title2rightfield' style='text-decoration:none;'><spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.note'></spring:message></span>"+
				  	    "</div>"+
	     
  	 					// Duplicate Check 
					    "<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
				  		"	<spring:message code='ftp.driver.mgmt.pathlist.duplicate.file.check.header' var='tooltip'></spring:message>"+
					    " 	<label style='width: 100%; background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
					    "</div>"+
					    "<div class='col-md-6 no-padding'>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.duplicate.file.check.duplicate.check.criteria' var='label'></spring:message>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.duplicate.file.check.duplicate.check.criteria.tooltip' var='tooltip'></spring:message>"+
					    "  	<div class='form-group'>"+
					    "   	<div class='table-cell-label'>${label}</div>"+
					    "      	<div class='input-group '>"+
					    "			<select  class='form-control table-cell input-sm'  tabindex='17' multiple='multiple' id='"+pathListCounter+"_duplicateCheckParamName' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
						" 				<c:forEach var='checkParam' items='${duplicateCheckParamEnum}'>"+
						"					<option value='${checkParam.value}' ${checkParam.value == 'name' ? 'selected' : ''}>${checkParam}</option>" +
			    		"				</c:forEach>" +
					    "			</select>"+
						"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "       </div>"+
					    "   </div>"+
					    "</div>"+
					    
					    "<div class='col-md-6 no-padding'>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.duplicate.file.check.time.interval' var='label'></spring:message>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.duplicate.file.check.time.interval.tooltip' var='tooltip'></spring:message>"+
					    "  	<div class='form-group'>"+
					    "   	<div class='table-cell-label'>${label}</div>"+
					    "      	<div class='input-group '>"+
						"	    	<input class='form-control table-cell input-sm' id='"+pathListCounter+"_timeInterval' onkeydown='isNumericOnKeyDown(event)' tabindex='18' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+
						"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "       </div>"+
					    "   </div>"+
					    "</div>"+
					    
					    "<div class='col-md-6 no-padding'>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.duplicate.file.suffix' var='label'></spring:message>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.duplicate.file.suffix.tooltip' var='tooltip'></spring:message>"+
					    "  	<div class='form-group'>"+
					    "   	<div class='table-cell-label'>${label}</div>"+
					    "      	<div class='input-group '>"+
			  	        " 			<select class='form-control table-cell input-sm' id='"+pathListCounter+"_duplicateFileSuffix' tabindex='11' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
			 	        "				<c:forEach var='trueFaluseEnum' items='${trueFalseEnum}'>"+
			            " 					<option value='${trueFaluseEnum.name}'>${trueFaluseEnum}</option>" +
			            " 				</c:forEach>" +
			 	        "			</select>" +
			  			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
			  	        "       </div>"+
					    "   </div>"+
					    "</div>"+
					    
					    //Additional Parameters
					    "<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.file.date.header' var='tooltip'></spring:message>"+
					    "	<label style='width: 100%;background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
					    "</div>"+
					    "<div class='col-md-6 no-padding'>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.filed.date.enable.label' var='label'></spring:message>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.filed.date.enable.tooltip' var='tooltip'></spring:message>"+
					    "  	<div class='form-group'>"+
					    "   	<div class='table-cell-label'>${label}</div>"+
					    "      	<div class='input-group '>"+
						"			<select class='form-control table-cell input-sm' id='"+pathListCounter+"_fileGrepDateEnabled' onchange=changeFileDateParam(\'"+pathListCounter+"\'); tabindex='19' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
					    "   			<c:forEach var='trueFaluseEnum' items='${trueFalseEnum}'>"+
					    "   				<option value='${trueFaluseEnum.name}'>${trueFaluseEnum}</option>" +
					    "    			</c:forEach>" +
					    "   		</select>" +
						"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "       </div>"+
					    "   </div>"+
					    "</div>"+
					    
					    "<div class='col-md-6 no-padding'>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.date.format.label' var='label'></spring:message>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.date.format.tooltip' var='tooltip'></spring:message>"+
					    "  	<div class='form-group'>"+
					    "   	<div class='table-cell-label'>${label}</div>"+
					    "   	<div class='input-group '>"+
					    "   		<input class='form-control table-cell input-sm' id='"+pathListCounter+"_dateFormat' tabindex='20' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+
						"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "       </div>"+
					    "   </div>"+
					    "</div>"+
					    
					    "<div class='col-md-6 no-padding'>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.position.label' var='label'></spring:message>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.position.tooltip' var='tooltip'></spring:message>"+
					    "  	<div class='form-group'>"+
					    "   	<div class='table-cell-label'>${label}</div>"+
					    "   	<div class='input-group '>"+
						"  			<select class='form-control table-cell input-sm' id='"+pathListCounter+"_position' tabindex='21' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
						"   			<c:forEach var='positionEnum' items='${positionEnum}'>"+
						"   				<option value='${positionEnum.value}'>${positionEnum}</option>" +
						"    			</c:forEach>" +
						"		   	</select>" +
						"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "      </div>"+
					    "  </div>"+
					    "</div>"+
					    
					    "<div class='col-md-6 no-padding'>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.start.idx.label' var='label'></spring:message>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.start.idx.tooltip' var='tooltip'></spring:message>"+
					    "	<div class='form-group'>"+
					    "		<div class='table-cell-label'>${label}</div>"+
					    "   	<div class='input-group '>"+
					    "      		<input class='form-control table-cell input-sm' id='"+pathListCounter+"_startIndex' onkeydown=isNumericOnKeyDown(event); tabindex='22' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+
						 "			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "       </div>"+
					    "   </div>"+
					    "</div>"+
					    
					    "<div class='col-md-6 no-padding'>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.end.idx.label' var='label'></spring:message>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.end.idx.tooltip' var='tooltip'></spring:message>"+
					    "	<div class='form-group'>"+
					    "		<div class='table-cell-label'>${label}</div>"+
					    "   	<div class='input-group '>"+
					    "      		<input class='form-control table-cell input-sm' id='"+pathListCounter+"_endIndex' onkeydown=isNumericOnKeyDown(event); tabindex='23' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+
					    "			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "       </div>"+
					    "   </div>"+
					    "</div>"+
					    
					    
						// file filter rule
					    
					    "      		<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
					    "					<spring:message code='ftp.driver.mgmt.pathlist.file.filter.rule.header' var='tooltip'></spring:message>"+
					    " 					<label style='width: 100%; background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
					    "				</div>"+
					    "				<div class='col-md-6 no-padding'>"+
					    "					<spring:message code='ftp.driver.mgmt.pathlist.file.filter.action' var='label'></spring:message>"+
					    "					<spring:message code='ftp.driver.mgmt.pathlist.file.filter.action.tooltip' var='tooltip'></spring:message>"+
					    "  				<div class='form-group' >"+
					    "    					<div class='table-cell-label'>${label}</div>"+
					    "      				<div class='input-group'>"+
						"	        				<select class='form-control table-cell input-sm' id='"+pathListCounter+"_remoteFileAction' onchange=changeFilterAction(\'"+pathListCounter+"\'); tabindex='24' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
					    "  						<c:forEach var='filterAction' items='${fileFilterActionEnum}'>"+
					    "    							<option value='${filterAction.value}'>${filterAction}</option>" +
					    "   						</c:forEach>" +
					    "   						</select>" +
						"							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "       				</div>"+
					    "     				</div>"+
					    " 				</div>"+
					    "			<div class='col-md-6 no-padding'>"+
					    "				<spring:message code='ftp.driver.mgmt.pathlist.valid.file.check.time.interval' var='label'></spring:message>"+
					    "				<spring:message code='ftp.driver.mgmt.pathlist.valid.file.check.time.interval.tooltip' var='tooltip'></spring:message>"+
					    "  				<div class='form-group'>"+
					    "   				<div class='table-cell-label'>${label}</div>"+
					    "      				<div class='input-group '>"+
						"	    				<input class='form-control table-cell input-sm' id='"+pathListCounter+"_validFileTimeInterval' tabindex='18' data-toggle='tooltip' data-placement='bottom' onkeydown=isNumericOnKeyDown(event); title='${tooltip }' value = '1' />"+
						"						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "       			</div>"+
					    "   			</div>"+
					    "			</div>"+
					    " 				<div class='col-md-6 no-padding'>"+
					    "					<spring:message code='ftp.driver.mgmt.pathlist.filter.action.type' var='label'></spring:message>"+
					    "					<spring:message code='ftp.driver.mgmt.pathlist.filter.action.type.tooltip' var='tooltip'></spring:message>"+
					    "  				<div class='form-group'>"+
					    "    					<div class='table-cell-label'>${label}</div>"+
					    "      				<div class='input-group '>"+
						"  						<select class='form-control table-cell input-sm' id='"+pathListCounter+"_remoteFileActionParamName' tabindex='25' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
					    "   						<c:forEach var='filterActionType' items='${fileFilterActionTypeEnum}'>"+
					    "    							<option value='${filterActionType.value}'>${filterActionType}</option>" +
					    "    						</c:forEach>" +
					    "   						</select>" +
						"							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "        				</div>"+
					    "     				</div>"+
					    "				</div>"+
					    "				<div class='col-md-6 no-padding'>"+
					    "					<spring:message code='ftp.driver.mgmt.pathlist.filter.val' var='label'></spring:message>"+
					    "					<spring:message code='ftp.driver.mgmt.pathlist.filter.val' var='tooltip'></spring:message>"+
					    "  				<div class='form-group'>"+
					    "    					<div class='table-cell-label'>${label}&nbsp;<i style='display:none;' id='"+pathListCounter+"_destionationpathicon' class='fa fa-square'></i></div>"+
					    "      				<div class='input-group '>"+
					    "      					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_remoteFileActionValue' tabindex='26' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+
						"							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "        				</div>"+
					    "     				</div>"+
					    "				</div>"+
					    "				<div id='"+pathListCounter+"_renamediv' style = 'display:none;'>"+
					    "				<div class='col-md-6 no-padding'>"+
					    "					<spring:message code='ftp.driver.mgmt.pathlist.filter.action.type' var='label'></spring:message>"+
					    "					<spring:message code='ftp.driver.mgmt.pathlist.filter.action.type.tooltip' var='tooltip'></spring:message>"+
					    "  				<div class='form-group'>"+
					    "    					<div class='table-cell-label'>${label}</div>"+
					    "      				<div class='input-group '>"+
						"  						<select class='form-control table-cell input-sm' id='"+pathListCounter+"_remoteFileActionParamNameTwo' tabindex='25' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
					    "   						<c:forEach var='filterActionType' items='${fileFilterActionTypeEnum}'>"+
					    "    							<option value='${filterActionType.value}'>${filterActionType}</option>" +
					    "    						</c:forEach>" +
					    "   						</select>" +
						"							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "        				</div>"+
					    "     				</div>"+
					    "				</div>"+
					    "				<div class='col-md-6 no-padding'>"+
					    "					<spring:message code='ftp.driver.mgmt.pathlist.filter.val' var='label'></spring:message>"+
					    "					<spring:message code='ftp.driver.mgmt.pathlist.filter.val' var='tooltip'></spring:message>"+
					    "  				<div class='form-group'>"+
					    "    					<div class='table-cell-label'>${label}</div>"+
					    "      				<div class='input-group '>"+
					    "      					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_remoteFileActionValueTwo' tabindex='26' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+
						"							<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "        				</div>"+
					    "     				</div>"+
					    "				</div>"+
					    "				</div>"+
					   
					    
					    // File Size Check
					    "<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
				  		"	<spring:message code='ftp.driver.mgmt.pathlist.file.size.check.header' var='tooltip'></spring:message>"+
					    " 	<label style='width: 100%; background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
					    "</div>"+
					    "<div class='col-md-6 no-padding'>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.file.size.check.enable' var='label'></spring:message>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.file.size.check.enable.tooltip' var='tooltip'></spring:message>"+
					    "  	<div class='form-group'>"+
					    "   	<div class='table-cell-label'>${label}</div>"+
					    "      	<div class='input-group '>"+
						"			<select class='form-control table-cell input-sm' id='"+pathListCounter+"_fileSizeCheckEnabled' onchange=changeFileSizeCheck(\'"+pathListCounter+"\'); tabindex='27' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
					    "   			<c:forEach var='trueFaluseEnum' items='${trueFalseEnum}'>"+
					    "   				<option value='${trueFaluseEnum.name}'>${trueFaluseEnum}</option>" +
					    "    			</c:forEach>" +
					    "   		</select>" +
						"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "       </div>"+
					    "   </div>"+
					    "</div>"+
					    "<div class='col-md-6 no-padding'>"+
			  	      	"	<spring:message code='ftp.driver.mgmt.pathlist.file.size.check.range' var='label'></spring:message>"+
			  	        "	<spring:message code='ftp.driver.mgmt.pathlist.file.size.check.min.tooltip' var='minTooltip'></spring:message>"+
			  	      	"	<spring:message code='ftp.driver.mgmt.pathlist.file.size.check.max.tooltip' var='maxTooltip'></spring:message>"+
			  	   		"	<div class='form-group'>"+
			  	     	"		<div class='table-cell-label'>${label}</div>"+
			  	     	"		<div class='input-group'>"+
			  	 		"			<div class='col-md-6 no-padding'>"+
			  	 		"				<div class='input-group'>"+
			  	 		"	    			<input class='form-control table-cell input-sm' id='"+pathListCounter+"_fileSizeCheckMinValue' onkeydown='isNumericOnKeyDown(event)' tabindex='28' data-toggle='tooltip' data-placement='bottom'  title='${minTooltip}'/>"+
						"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
			  	 		"				</div>"+
			  	 		"			</div>"+
			
			  	 		"			<div class='col-md-6 no-padding'>"+
			  	 		"				<div class='input-group'>"+ 
			  	 		"	    			<input class='form-control table-cell input-sm' id='"+pathListCounter+"_fileSizeCheckMaxValue' onkeydown='isNumericOnKeyDown(event)' tabindex='29' data-toggle='tooltip' data-placement='bottom'  title='${maxTooltip}'/>"+
						"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
					    "				</div>"+
			  	 		"			</div>"+
			  	 		"		</div>"+
			  	   		"	</div>"+  
			  	   		"</div>"+ 
					    
					    
					    
					    "				<div class='col-md-12 no-padding'>"+
					    "				<div id='"+pathListCounter+"_dBstatsDisableMessage' style='display:none ; color: red;'><spring:message code='pathlist.service.destats.disable.message' ></spring:message> </div> "+
					    "				</div>"+
					    
					    "				<div class='col-md-12 no-padding'>"+
					    
					    "				</div>"+
					    "				<div class='col-md-12 no-padding title2' style='margin-bottom: 40px;display:none;' id='"+pathListCounter+"_destFileRenamingDiv' >"+
					    "				<span class='title2leftfield' id='char_rename_add_link'>"+
					    "				<span class='title2rightfield-icon1-text'>"+
					    "				<a href='#' onclick='viewFileCharRenameOperation("+pathListCounter+");'><i class='fa fa-plus-circle'></i></a>"+
					    "				<a href='#' onclick='viewFileCharRenameOperation("+pathListCounter+");' id=''>"+
					    "				<strong>Destination File Naming Convention</strong>"+
					    "				</a>"+
					    "				</div>"+
					    
					    "<div class='col-md-6 no-padding'>"+
					    "	<spring:message code='ftp.driver.mgmt.pathlist.max.count.limit.label' var='tooltip'></spring:message>"+
					    "	<div class='form-group'>"+
					    "  		<div class='input-group ' id='" + pathListCounter + "_buttons-div'>"+
					    "			<sec:authorize access='hasAuthority(\'CREATE_PATHLIST\')'>"+
					    "  				<button type='button' class='btn btn-grey btn-xs ' id='" + pathListCounter + "_savebtn' tabindex='41' onclick=createPathList(\'"+pathListCounter+"\');closeFancyBox();><spring:message code='btn.label.submit'></spring:message></button>&nbsp;" +
					    "			</sec:authorize>"+
					    "			<sec:authorize access='hasAuthority(\'UPDATE_PATHLIST\')'>"+
					    "      			<button type='button' class='btn btn-grey btn-xs ' tabindex='42' style='display:none' id='" + pathListCounter + "_updatebtn'  onclick=updatePathList(\'"+pathListCounter+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
					    "			    <button type='button' class='btn btn-grey btn-xs ' id='" + pathListCounter + "_resetbtn' onclick=resetPathListDiv(\'"+pathListCounter+"\');><spring:message code='btn.label.reset'></spring:message></button>" +
					    "			</sec:authorize>"+
					    "       </div>"+
					    "		<div id='" + pathListCounter + "_progress-bar-div' class='input-group' style='display: none;'> "+
					    "			<label>Processing Request &nbsp;&nbsp; </label> "+
					    "			<img src='img/processing-bar.gif'>"+
						"		</div> "+
					    "   </div>" +
					    "</div>" +
					    
					    "<div class='col-md-6 no-padding' style='vertical-align:bottom;'>"+
						"	<span class='title2rightfield' style='text-decoration:none;'>" +
						"		<spring:message code='ftp.driver.mgmt.pathlist.r.w.note'></spring:message><br/>" +
						"		<div class='pull-left mtop5' style='font-size:10px;'><i class='fa fa-square' style='font-size: 9px'></i>&nbsp;&nbsp;<spring:message code='restart.operation.require.message' ></spring:message> </div> "+
						"	</span>" +
				        "</div>" +
					    "	</div>"+
						"</div>"+
						"</form>";
					 
		$('#divPathList').prepend(pathListTable);
		//$('#' + pathListCounter + '_maxCounterLimit').val('${maxCounterLimit}');
		$("#"+pathListCounter+"_remoteFileAction").val('na');
		$("#"+pathListCounter+"_fileGrepDateEnabled").val('false');
		$("#"+pathListCounter+"_position").val('left');
		$("#"+pathListCounter+"_startIndex").val('-1');
		$("#"+pathListCounter+"_endIndex").val('-1');
		$("#"+pathListCounter+"_fileSeqAlertEnabled").val('false');
		$("#"+pathListCounter+"_fileSizeCheckEnabled").val('false');
		$("#"+pathListCounter+"_fileSizeCheckMinValue").val('-1');
		$("#"+pathListCounter+"_fileSizeCheckMaxValue").val('-1');
		
		changeFileSizeCheck(pathListCounter);
		changeFilterAction(pathListCounter);
		changeFileDateParam(pathListCounter);
		changeMissingFileSequenceParams(pathListCounter);
		changeFileSeqParam(pathListCounter);
		$("#"+pathListCounter+"_seqStartIndex").val('0');
		$("#"+pathListCounter+"_seqEndIndex").val('0');
		$("#"+pathListCounter+"_timeInterval").val('1');
		$("#"+pathListCounter+"_missingFileSequenceId_minValue").val('0');
		$("#"+pathListCounter+"_missingFileSequenceId_maxValue").val('0');
		$("#"+pathListCounter+"_maxFilesCountAlert").val('${maxFilesCountAlert}');
		$("#"+pathListCounter+"_validFileTimeInterval").val('1');
		formatDuplicateCheckDropdown(pathListCounter);
		setValueInMultiSelect(pathListCounter, 'name');
		
	 }
	 
	 function addPathDetail(id,name,fileNamePattern,readLocation,writeLocation,readPrefix,readSuffix,readContains,readExclude,writePrefix,maxFilesCountAlert,
			 				remoteFileAction,remoteFileActionParamName,remoteActionValue,remoteFileActionParamNameTwo,remoteActionValue1,fileGrepDateEnabled,position,dateFormat,startIdx,endIdx,
			 				fileSeqAlertEnabled,seqStartIdx,seqEndIdx,duplicateFileSuffix,timeInterval,minValue, maxValue, deviceId, deviceName, referenceDeviceName,
			 				resetFrequency, missingFileSequenceIdVal, duplcateCheckParamsVal, pathId,fileSizeCheckMinValue, fileSizeCheckMaxValue,fileSizeCheckEnabled, validFileTimeInterval){
		 pathListCounter++;
		 if(minValue === null || minValue === '' || minValue === 'undefined '){
			 minValue = 0;
		 }
		 if(maxValue === null || maxValue === '' || maxValue === 'undefined '){
			 maxValue = 0;
		 }
		 var pathListTable ="<form method='POST' id='" + pathListCounter + "_form'> "+
		 "<input type='hidden' id='driverId' name='driverId' value='${driverId}'/>"+
		 "<input type='hidden' id='"+pathListCounter+"_missingFileSequenceIdVal' name='"+pathListCounter+"_missingFileSequenceIdVal' value='"+missingFileSequenceIdVal+"'> "+
		 "<input type='hidden' id='"+pathListCounter+"_pathListCount' name='pathListCount' value='"+pathListCounter+"'/>"+
		 "<input type='hidden' id='"+pathListCounter+"_id' name='"+pathListCounter+"_id' value='"+id+"'> "+
		 "<input type='hidden' id='"+pathListCounter+"_pathId' value='"+pathId+"'> "+
		 
			 "<div class='box box-warning' id='"+ pathListCounter + "_block'>"+
	     "<div class='box-header with-border'> "+
	         "<h3 class='box-title' id='title_"+pathListCounter+ "'>"+name+"-"+pathId+"</h3>"+
	         "<div class='box-tools pull-right'>"+
	             "<button class='btn btn-box-tool block-collapse-btn' id='collapse_"+pathListCounter+"' data-toggle='collapse' data-parent='#pathListConfig' href='#pathList_" + pathListCounter + "_block'><i class='fa fa-minus'></i></button>	"+
	"	<sec:authorize access='hasAuthority(\'DELETE_PATHLIST\')'>"+				             
	             "						&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' id='delete_"+pathListCounter+"' style='margin-top:12px;' onclick=deletePathListPopup(\'"+pathListCounter+"\');></a>&nbsp;" +
	             "					</sec:authorize>"+
	             "</div>"+
	     		 "</div>	"+
	     		 "<div class='box-body inline-form accordion-body collapse' id='pathList_" + pathListCounter + "_block'>"+
	     	"<div class='col-md-6 no-padding'>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.path.dtl.alias' var='label'></spring:message>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.path.dtl.alias.tooltip' var='tooltip'></spring:message>"+
	        "  	<div class='form-group'>"+
	        "    	<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
	        "      	<div class='input-group '>"+
	        "			<input class='form-control table-cell input-sm' id='" + pathListCounter + "_name' tabindex='1' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+name+"'/>"+
			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
	        "       </div>"+
	        "   </div>"+
	        "</div>"+
	        "<div class='col-md-6 no-padding'>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.read.file.loc.label' var='label'></spring:message>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.read.file.loc.tooltip' var='tooltip'></spring:message>"+
	        "	<div class='form-group'>"+
	        "		<div class='table-cell-label'>${label}<span class='required'>*</span>&nbsp;<i class='fa fa-square'></i></div>"+
	        "		<div class='input-group'>"+
	        "			<input class='form-control table-cell input-sm' id='" + pathListCounter + "_readFilePath' tabindex='2' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readLocation+"'/>"+
			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
	        "		</div>"+
	        "	</div>"+
	        "</div>"+
	     	" <div class='col-md-6 no-padding'>"+ 
			"		 <spring:message code='iplog.parsing.service.pathlist.file.pattern' var='tooltip'></spring:message>"+ 
			"		 <div class='form-group'>" +
			"			 <div class='table-cell-label'>${tooltip}</div>"+ 
			"			 <div class='input-group'>" +
			"				 <input tabindex='4' id='" + pathListCounter + "_fileNamePattern' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom' title='${tooltip}' value='"+fileNamePattern+"'/>"+ 
			"				 <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>" +
			"			 </div>" +
			"        </div>" +
			"    </div>" +
	        "<div class='col-md-6 no-padding'>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.write.file.loc' var='label'></spring:message>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.write.file.loc.tooltip' var='tooltip'></spring:message>"+
	        "  	<div class='form-group'>"+
	        "   	<div class='table-cell-label'>${label}<span class='required'>*</span>&nbsp;<i class='fa fa-square'></i></div>"+
	        "      	<div class='input-group '>"+
	        "      		<input class='form-control table-cell input-sm' id='" + pathListCounter + "_writeFilePath' tabindex='3' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+writeLocation+"'/>"+
			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	        "       </div>"+
	        "     </div>"+
	        "</div>"+
	        "<div class='col-md-6 no-padding'>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.file.prefix.r' var='label'></spring:message>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.file.prefix.r.tooltip' var='tooltip'></spring:message>"+
	        "  	<div class='form-group'>"+
	        "    	<div class='table-cell-label'>${label}</div>"+
	        "      	<div class='input-group '>"+
	        "      		<input class='form-control table-cell input-sm' id='" + pathListCounter + "_readFilenamePrefix' tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readPrefix+"'/>"+
			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	        "     	</div>"+
	        "     </div>"+
	        "</div>"+
	        "<div class='col-md-6 no-padding'>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.file.suffix.r' var='label'></spring:message>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.file.suffix.r.tooltip' var='tooltip'></spring:message>"+
	        "  	<div class='form-group'>"+
	        "    	<div class='table-cell-label'>${label}</div>"+
	        "      	<div class='input-group '>"+
	        "      		<input class='form-control table-cell input-sm' id='" + pathListCounter + "_readFilenameSuffix' tabindex='5' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readSuffix+"'/>"+
			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	        "       </div>"+
	        "     </div>"+
	        "</div>"+
	        
	        
	        "<div class='col-md-6 no-padding'>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.file.contain.r' var='label'></spring:message>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.file.contain.r.tooltip' var='tooltip'></spring:message>"+
	        "  	<div class='form-group'>"+
	        "    	<div class='table-cell-label'>${label}</div>"+
	        "      	<div class='input-group '>"+
	        "      		<input class='form-control table-cell input-sm' id='" + pathListCounter + "_readFilenameContains'  tabindex='6' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+readContains+"'/>"+
			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	        "        </div>"+
	        "     </div>"+
	        "</div>"+
	        "<div class='col-md-6 no-padding'>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.file.prefix.w' var='label'></spring:message>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.file.prefix.w.tooltip' var='tooltip'></spring:message>"+
	        "  	<div class='form-group'>"+
	        "    	<div class='table-cell-label'>${label}</div>"+
	        "      	<div class='input-group '>"+
	        "      		<input class='form-control table-cell input-sm' id='" + pathListCounter + "_writeFilenamePrefix'  tabindex='7' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+writePrefix+"'/>"+
			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	        "        </div>"+
	        "     </div>"+
	        "</div>"+
	        "<div class='col-md-6 no-padding'>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.file.max.count.alert' var='label'></spring:message>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.file.max.count.alert.tooltip' var='tooltip'></spring:message>"+
	        "  	<div class='form-group'>"+
	        "    	<div class='table-cell-label'>${label}</div>"+
	        "      	<div class='input-group '>"+
	        "      		<input class='form-control table-cell input-sm' id='" + pathListCounter + "_maxFilesCountAlert' onkeydown='isNumericOnKeyDown(event)' tabindex='8' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+maxFilesCountAlert+"'/>"+
			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	        "        </div>"+
	        "     </div>"+
	        "</div>"+
	        // device Name
	        "		<div class='col-md-6 no-padding'> "+
			"			<spring:message code='device.list.grid.device.name.label' var='label'></spring:message> "+ 
			"			<spring:message code='device.list.grid.device.name.label' var='tooltip'></spring:message> "+ 
			"			<div class='form-group'>  "+
			"				<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+ 
			"				<div class='input-group '> "+
			"					<input type='hidden' class='form-control table-cell input-sm' value='"+deviceId+"' id='"+pathListCounter+"_deviceId'  data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
			"					</input>"+
			"                   <a href='#' onclick='selectDevicePopUp("+pathListCounter+");' tabindex='9' style='vertical-align:bottom;' id='select_popup_"+ pathListCounter +"_lnk'><i style='margin-top: 8px;margin-left: 8px;' class='glyphicon glyphicon-search'></i></a>" +
			"					<input type='text' class='form-control table-cell input-sm' value='"+deviceName+"' id='"+pathListCounter+"_parentDevice' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' style='width:94%;' readonly=true >"+
			"					</input>"+
			"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
			"				</div> "+
			"			</div> "+
			"		</div> "+
			//reference devicename
			"<div class='col-md-6 no-padding'>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.reference.devicename' var='label'></spring:message>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.reference.devicename.tooltip' var='tooltip'></spring:message>"+
	        "  	<div class='form-group'>"+
	        "    	<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
	        "      	<div class='input-group '>"+
	        "      		<input class='form-control table-cell input-sm' value='"+referenceDeviceName+"' id='" + pathListCounter + "_referenceDevice'  tabindex='10' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' />"+
			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	        "        </div>"+
	        "     </div>"+
	        "</div>"+
			
			
	        //add Path Detail - missing file seq
	        "      		<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
         	"				<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.header' var='tooltip'></spring:message>"+
        	"				<label style='width: 100%;background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
         	"			</div>"+
         	 "			<div class='col-md-6 no-padding'>"+
  	        "				<spring:message code='ftp.driver.mgmt.pathlist.file.seq.alert.enable.label' var='label'></spring:message>"+
  	        "				<spring:message code='ftp.driver.mgmt.pathlist.file.seq.alert.enable.tooltip' var='tooltip'></spring:message>"+
  	        "  				<div class='form-group'>"+
  	        "    				<div class='table-cell-label'>${label}</div>"+
  	        "      				<div class='input-group '>"+
  	        " 						<select class='form-control table-cell input-sm' id='"+pathListCounter+"_fileSeqAlertEnabled' onChange='changeMissingFileSequenceParams("+pathListCounter+");' tabindex='11' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
 	        "						<c:forEach var='trueFaluseEnum' items='${trueFalseEnum}'>"+
            " 							<option value='${trueFaluseEnum.name}'>${trueFaluseEnum}</option>" +
            " 						</c:forEach>" +
 	        "						</select>" +
  			"						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
  	        "       			</div>"+
  	        "     			</div>"+
  	        "			</div>"+
  	        
  	        //min max val
  	        "<div class='col-md-6 no-padding'>"+
  	       	"	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.range' var='label'></spring:message>"+
  	     	"	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.range.min' var='minTooltip'></spring:message>"+
  	     	"	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.range.max' var='maxTooltip'></spring:message>"+
  	   		"	<div class='form-group'>"+
  	     	"		<div class='table-cell-label'>${label}</div>"+
  	     	"		<div class='input-group'>"+
  	 		"			<div class='col-md-6 no-padding'>"+
  	 		"				<div class='input-group'>"+
  	 		"					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_missingFileSequenceId_minValue' value='"+minValue+"' tabindex='12' data-toggle='tooltip' type='text' data-placement='bottom' title='${minTooltip}' onkeydown='isNumericOnKeyDown(event)'/>"+
  	 		"					<span class='input-group-addon add-on last'> <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
  	 		"				</div>"+
  	 		"			</div>"+
		  	"			<div class='col-md-6 no-padding'>"+
  	 		"				<div class='input-group'>"+ 
  	 		"					<input class='form-control table-cell input-sm' value='"+maxValue+"' id='"+pathListCounter+"_missingFileSequenceId_maxValue' tabindex='13' data-toggle='tooltip' type='text' data-placement='bottom' title='${maxTooltip}' onkeydown='isNumericOnKeyDown(event)' />"+
  	 		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
  	 		"				</div>"+
  	 		"			</div>"+
  	 		"		</div>"+
  	   		"	</div>"+  
  	   		"</div>"+ 
			// Reset Frequency
			"<div class='col-md-6 no-padding'>"+
 	 		"	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.frequency' var='label'></spring:message>"+
 	        "	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.frequency.tooltip' var='tooltip'></spring:message>"+
 	        "  	<div class='form-group'>"+
 	        "   	<div class='table-cell-label'>${label}</div>"+
 	        "   	<div class='input-group'>"+
 	        " 			<select class='form-control table-cell input-sm' id='"+pathListCounter+"_resetFrequency'  tabindex='14' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
	        "				<c:forEach var='resetFrequency' items='${missingFileFrequency}'>"+
            " 					<option value='${resetFrequency.value}'>${resetFrequency}</option>" +
            "				</c:forEach>" +
	        "			</select>" +
 			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
 	        "       </div>"+
 	        "   </div>"+
 	        "</div>"+
			//Start End Index
     		"<div class='col-md-6 no-padding'>"+
  	        "	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.startEndIndex' var='label'></spring:message>"+
  	      	"	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.startIndex.tooltip' var='startIndexTooltip'></spring:message>"+
  	     	"	<spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.endIndex.tooltip' var='endIndexTooltip'></spring:message>"+
  	   		"	<div class='form-group'>"+
  	     	"		<div class='table-cell-label'>${label}</div>"+
  	     	"		<div class='input-group'>"+
  			"			<div class='col-md-6 no-padding'>"+
  	 		"				<div class='input-group'>"+
  	 		"					<input class='form-control table-cell input-sm' id='" + pathListCounter + "_seqStartIndex' value='"+seqStartIdx+"' tabindex='15' data-toggle='tooltip' type='text' data-placement='bottom' title='${startIndexTooltip}' onkeydown='isNumericOnKeyDown(event)'/>"+
  	 		"					<span class='input-group-addon add-on last'> <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
  	 		"				</div>"+
  	 		"			</div>"+
		 	"			<div class='col-md-6 no-padding'>"+
  	 		"				<div class='input-group'>"+ 
  	 		"					<input class='form-control table-cell input-sm' id='" + pathListCounter + "_seqEndIndex' value='"+seqEndIdx+"' tabindex='16' data-toggle='tooltip' type='text' data-placement='bottom' title='${endIndexTooltip}' onkeydown='isNumericOnKeyDown(event)' />"+
  	 		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
  	 		"				</div>"+
  	 		"			</div>"+
  	 		"		</div>"+
	  		"	</div>"+  
  	   		"</div>"+ 
	  	   	"<div class='col-md-6 no-padding'>"+
	  	    "</div>"+ 
	  	  	"<div class='col-md-6 no-padding'>"+
	  	  	"<span class='title2rightfield' style='text-decoration:none;'><spring:message code='ftp.driver.mgmt.pathlist.missing.file.sequence.note'></spring:message></span>"+
	  	    "</div>"+
  	   
	  		// Duplicate Check 
		    "<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
	 		"	<spring:message code='ftp.driver.mgmt.pathlist.duplicate.file.check.header' var='tooltip'></spring:message>"+
		    " 	<label style='width: 100%; background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
		    "</div>"+
		    "<div class='col-md-6 no-padding'>"+
		    "	<spring:message code='ftp.driver.mgmt.pathlist.duplicate.file.check.duplicate.check.criteria' var='label'></spring:message>"+
		    "	<spring:message code='ftp.driver.mgmt.pathlist.duplicate.file.check.duplicate.check.criteria.tooltip' var='tooltip'></spring:message>"+
		    "  	<div class='form-group'>"+
		    "   	<div class='table-cell-label'>${label}</div>"+
		    "      	<div class='input-group '>"+
		    "			<select  class='form-control table-cell input-sm'  tabindex='17' multiple='multiple' id='"+pathListCounter+"_duplicateCheckParamName' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
			" 				<c:forEach var='checkParam' items='${duplicateCheckParamEnum}'>"+
			"					<option value='${checkParam.value}'>${checkParam}</option>" +
			"				</c:forEach>" +
		    "			</select>"+
			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
		    "       </div>"+
		    "   </div>"+
		    "</div>"+
		    
		    "<div class='col-md-6 no-padding'>"+
		    "	<spring:message code='ftp.driver.mgmt.pathlist.duplicate.file.check.time.interval' var='label'></spring:message>"+
		    "	<spring:message code='ftp.driver.mgmt.pathlist.duplicate.file.check.time.interval.tooltip' var='tooltip'></spring:message>"+
		    "  	<div class='form-group'>"+
		    "   	<div class='table-cell-label'>${label}</div>"+
		    "      	<div class='input-group '>"+
			"	    	<input class='form-control table-cell input-sm' id='"+pathListCounter+"_timeInterval' tabindex='18' data-toggle='tooltip' data-placement='bottom' onkeydown=isNumericOnKeyDown(event); title='${tooltip }' value = '"+timeInterval+"' />"+
			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
		    "       </div>"+
		    "   </div>"+
		    "</div>"+
		    
		    "<div class='col-md-6 no-padding'>"+
		    "	<spring:message code='ftp.driver.mgmt.pathlist.duplicate.file.suffix' var='label'></spring:message>"+
		    "	<spring:message code='ftp.driver.mgmt.pathlist.duplicate.file.suffix.tooltip' var='tooltip'></spring:message>"+
		    "  	<div class='form-group'>"+
		    "   	<div class='table-cell-label'>${label}</div>"+
		    "      	<div class='input-group '>"+
  	        " 			<select class='form-control table-cell input-sm' id='"+pathListCounter+"_duplicateFileSuffix' tabindex='11' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
 	        "				<c:forEach var='trueFaluseEnum' items='${trueFalseEnum}'>"+
            " 					<option value='${trueFaluseEnum.name}'>${trueFaluseEnum}</option>" +
            " 				</c:forEach>" +
 	        "			</select>" +
  			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
  	        "       </div>"+
		    "   </div>"+
		    "</div>"+
  	   
		    //Additional Parameters
  	   
		    "<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
         	"	<spring:message code='ftp.driver.mgmt.pathlist.file.date.header' var='tooltip'></spring:message>"+
        	"		<label style='width: 100%;background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
         	"</div>"+
	        "<div class='col-md-6 no-padding'>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.filed.date.enable.label' var='label'></spring:message>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.filed.date.enable.tooltip' var='tooltip'></spring:message>"+
	        "	<div class='form-group'>"+
	        "		<div class='table-cell-label'>${label}</div>"+
	        "		<div class='input-group '>"+
			"			<select class='form-control table-cell input-sm' id='"+pathListCounter+"_fileGrepDateEnabled' onchange=changeFileDateParam(\'"+pathListCounter+"\'); tabindex='19' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
	        "				<c:forEach var='trueFaluseEnum' items='${trueFalseEnum}'>"+
            "					<option value='${trueFaluseEnum.name}'>${trueFaluseEnum}</option>" +
            "				</c:forEach>" +
	        "			</select>" +
			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	        "	   </div>"+
	        "   </div>"+
	        "</div>"+
	        
	        "<div class='col-md-6 no-padding'>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.date.format.label' var='label'></spring:message>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.date.format.tooltip' var='tooltip'></spring:message>"+
	        "	<div class='form-group'>"+
	        "		<div class='table-cell-label'>${label}</div>"+
	        "   	<div class='input-group '>"+
	        "      		<input class='form-control table-cell input-sm' id='"+pathListCounter+"_dateFormat' tabindex='20' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+dateFormat+"'/>"+
			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	        "       </div>"+
	        "   </div>"+
	        "</div>"+
	        
	        "<div class='col-md-6 no-padding'>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.position.label' var='label'></spring:message>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.position.tooltip' var='tooltip'></spring:message>"+
	        "	<div class='form-group'>"+
	        "		<div class='table-cell-label'>${label}</div>"+
	        "		<div class='input-group '>"+
			"			<select class='form-control table-cell input-sm' id='"+pathListCounter+"_position' tabindex='21' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
	        "				<c:forEach var='positionEnum' items='${positionEnum}'>"+
            "					<option value='${positionEnum.value}'>${positionEnum}</option>" +
            " 				</c:forEach>" +
	        "			</select>" +
			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	        "       </div>"+
	        "   </div>"+
	       	"</div>"+
	        
	       	"<div class='col-md-6 no-padding'>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.start.idx.label' var='label'></spring:message>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.start.idx.tooltip' var='tooltip'></spring:message>"+
	        "	<div class='form-group'>"+
	        "		<div class='table-cell-label'>${label}</div>"+
	        "		<div class='input-group '>"+
	        "			<input class='form-control table-cell input-sm' id='"+pathListCounter+"_startIndex' onkeydown=isNumericOnKeyDown(event); tabindex='22' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+startIdx+"'/>"+
			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	        "		</div>"+
	        "	</div>"+
	        "</div>"+
	        
	        "<div class='col-md-6 no-padding'>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.end.idx.label' var='label'></spring:message>"+
	        "	<spring:message code='ftp.driver.mgmt.pathlist.end.idx.tooltip' var='tooltip'></spring:message>"+
	        "	<div class='form-group'>"+
	        "		<div class='table-cell-label'>${label}</div>"+
	        "      	<div class='input-group '>"+
	        "      		<input class='form-control table-cell input-sm' id='"+pathListCounter+"_endIndex' onkeydown=isNumericOnKeyDown(event); tabindex='23' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+endIdx+"'/>"+
			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	        "       </div>"+
	        "   </div>"+
	        "</div>"+
  	   
  	   
  	   
  	   
 	        // filter rule 
	        "      		<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
         	"				<spring:message code='ftp.driver.mgmt.pathlist.file.filter.rule.header' var='tooltip'></spring:message>"+
        	"				<label style='width: 100%;background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
         	"			</div>"+
	        "			<div class='col-md-6 no-padding'>"+
	        "				<spring:message code='ftp.driver.mgmt.pathlist.file.filter.action' var='label'></spring:message>"+
	        "				<spring:message code='ftp.driver.mgmt.pathlist.file.filter.action.tooltip' var='tooltip'></spring:message>"+
	        "  				<div class='form-group'>"+
	        "    				<div class='table-cell-label'>${label}</div>"+
	        "      				<div class='input-group'>"+
	        "						<select class='form-control table-cell input-sm' id='"+pathListCounter+"_remoteFileAction' onChange=changeFilterAction(\'"+pathListCounter+"\'); tabindex='24' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
	        "						<c:forEach var='filterAction' items='${fileFilterActionEnum}'>"+
            " 							<option value='${filterAction.value}'>${filterAction}</option>" +
            " 							</c:forEach>" +
	        " 						</select>" +
			"						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	        "       			</div>"+
	        "     			</div>"+
	        "			</div>"+
	        "			<div class='col-md-6 no-padding'>"+
	        "				<spring:message code='ftp.driver.mgmt.pathlist.valid.file.check.time.interval' var='label'></spring:message>"+
		    "				<spring:message code='ftp.driver.mgmt.pathlist.valid.file.check.time.interval.tooltip' var='tooltip'></spring:message>"+
		    "  				<div class='form-group'>"+
		    "   				<div class='table-cell-label'>${label}</div>"+
		    "      				<div class='input-group '>"+
			"	    				<input class='form-control table-cell input-sm' id='"+pathListCounter+"_validFileTimeInterval' tabindex='18' data-toggle='tooltip' data-placement='bottom' onkeydown=isNumericOnKeyDown(event); title='${tooltip }' value = '"+validFileTimeInterval+"' />"+
			"						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
		    "       			</div>"+
		    "   			</div>"+
		    "			</div>"+
	        "			<div class='col-md-6 no-padding'>"+
	        "				<spring:message code='ftp.driver.mgmt.pathlist.filter.action.type' var='label'></spring:message>"+
	        "				<spring:message code='ftp.driver.mgmt.pathlist.filter.action.type.tooltip' var='tooltip'></spring:message>"+
	        "  				<div class='form-group'>"+
	        "    				<div class='table-cell-label'>${label}</div>"+
	        "      				<div class='input-group '>"+
			"	        			<select class='form-control table-cell input-sm' id='"+pathListCounter+"_remoteFileActionParamName' tabindex='25' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
	        "						<c:forEach var='filterActionType' items='${fileFilterActionTypeEnum}'>"+
            " 							<option value='${filterActionType.value}'>${filterActionType}</option>" +
            " 						</c:forEach>" +
	        " 						</select>" +
			"						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	        "        			</div>"+
	        "     			</div>"+
	        "			</div>"+
	        "			<div class='col-md-6 no-padding'>"+
	        "				<spring:message code='ftp.driver.mgmt.pathlist.filter.val' var='label'></spring:message>"+
	        "				<spring:message code='ftp.driver.mgmt.pathlist.filter.val.tooltip' var='tooltip'></spring:message>"+
	        "  				<div class='form-group'>"+
	        "    				<div class='table-cell-label'>${label}&nbsp;<i style='display:none;' id='"+pathListCounter+"_destionationpathicon' class='fa fa-square'></i></div>"+
	        "      				<div class='input-group'>"+
	        "      					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_remoteFileActionValue' value='"+remoteActionValue+"'    />"+
			"						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	        "        			</div>"+
	        "     			</div>"+
	        "			</div>"+
	        "			<div id='"+pathListCounter+"_renamediv' style = 'display:none;'>"+
	        "			<div class='col-md-6 no-padding'>"+
	        "				<spring:message code='ftp.driver.mgmt.pathlist.filter.action.type' var='label'></spring:message>"+
	        "				<spring:message code='ftp.driver.mgmt.pathlist.filter.action.type.tooltip' var='tooltip'></spring:message>"+
	        "  				<div class='form-group'>"+
	        "    				<div class='table-cell-label'>${label}</div>"+
	        "      				<div class='input-group '>"+
			"	        			<select class='form-control table-cell input-sm' id='"+pathListCounter+"_remoteFileActionParamNameTwo' tabindex='25' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
	        "						<c:forEach var='filterActionType' items='${fileFilterActionTypeEnum}'>"+
            " 							<option value='${filterActionType.value}'>${filterActionType}</option>" +
            " 						</c:forEach>" +
	        " 						</select>" +
			"						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	        "        			</div>"+
	        "     			</div>"+
	        "			</div>"+
	        "			<div class='col-md-6 no-padding' id='renamevalue'>"+
	        "				<spring:message code='ftp.driver.mgmt.pathlist.filter.val' var='label'></spring:message>"+
	        "				<spring:message code='ftp.driver.mgmt.pathlist.filter.val.tooltip' var='tooltip'></spring:message>"+
	        "  				<div class='form-group'>"+
	        "    				<div class='table-cell-label'>${label}</div>"+
	        "      				<div class='input-group '>"+
	        "      					<input class='form-control table-cell input-sm' id='"+pathListCounter+"_remoteFileActionValueTwo' tabindex='26' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+remoteActionValue1+"'/>"+
			"						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	        "        			</div>"+
	        "     			</div>"+
	        "			</div>"+
	        "			</div>"+
	        
	        
	        // File Size Check
		    "<div class='col-md-12 no-padding' style='height: 25px;margin-bottom: 5px;'>"+
	  		"	<spring:message code='ftp.driver.mgmt.pathlist.file.size.check.header' var='tooltip'></spring:message>"+
		    " 	<label style='width: 100%; background: none repeat scroll 0 0 #E9E9E7;height:25px;padding-top:4px;'>${tooltip}</label>"+
		    "</div>"+
		    "<div class='col-md-6 no-padding'>"+
		    "	<spring:message code='ftp.driver.mgmt.pathlist.file.size.check.enable' var='label'></spring:message>"+
		    "	<spring:message code='ftp.driver.mgmt.pathlist.file.size.check.enable.tooltip' var='tooltip'></spring:message>"+
		    "  	<div class='form-group'>"+
		    "   	<div class='table-cell-label'>${label}</div>"+
		    "      	<div class='input-group '>"+
			"			<select class='form-control table-cell input-sm' id='"+pathListCounter+"_fileSizeCheckEnabled' onchange=changeFileSizeCheck(\'"+pathListCounter+"\'); tabindex='27' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
		    "   			<c:forEach var='trueFaluseEnum' items='${trueFalseEnum}'>"+
		    "   				<option value='${trueFaluseEnum.name}'>${trueFaluseEnum}</option>" +
		    "    			</c:forEach>" +
		    "   		</select>" +
			"			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
		    "       </div>"+
		    "   </div>"+
		    "</div>"+
		    
		    "<div class='col-md-6 no-padding'>"+
		    "	<spring:message code='ftp.driver.mgmt.pathlist.file.size.check.range' var='label'></spring:message>"+
  	      	"	<spring:message code='ftp.driver.mgmt.pathlist.file.size.check.min.tooltip' var='minTooltip'></spring:message>"+
	  	    "	<spring:message code='ftp.driver.mgmt.pathlist.file.size.check.max.tooltip' var='maxTooltip'></spring:message>"+
  	   		"	<div class='form-group'>"+
  	     	"		<div class='table-cell-label'>${label}</div>"+
  	     	"		<div class='input-group'>"+
  	 		"			<div class='col-md-6 no-padding'>"+
  	 		"				<div class='input-group'>"+
	    	"	    			<input class='form-control table-cell input-sm' id='"+pathListCounter+"_fileSizeCheckMinValue' value='"+fileSizeCheckMinValue+"' onkeydown='isNumericOnKeyDown(event)' tabindex='28' data-toggle='tooltip' data-placement='bottom'  title='${minTooltip}'/>"+
			"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
  	 		"				</div>"+
  	 		"			</div>"+
		  	"			<div class='col-md-6 no-padding'>"+
  	 		"				<div class='input-group'>"+ 
  	 		"	    			<input class='form-control table-cell input-sm' id='"+pathListCounter+"_fileSizeCheckMaxValue' value='"+fileSizeCheckMaxValue+"' onkeydown='isNumericOnKeyDown(event)' tabindex='29' data-toggle='tooltip' data-placement='bottom'  title='${maxTooltip}'/>"+
			"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
			"				</div>"+
  	 		"			</div>"+
  	 		"		</div>"+
  	   		"	</div>"+  
  	   		"</div>"+ 
  	   		
		    
		    
	        
	        "				<div class='col-md-12 no-padding'>"+
		    "				<div id='"+pathListCounter+"_dBstatsDisableMessage' style='display:none ; color: red;'><spring:message code='pathlist.service.destats.disable.message' ></spring:message> </div> "+
		    "				</div>"+
	       
		    "				<div class='col-md-12 no-padding'>"+
		    
		    "				</div>"+
		    "				<div class='col-md-12 no-padding title2' style='margin-bottom: 40px;' id='"+pathListCounter+"_destFileRenamingDiv' >"+
		    "				<span class='title2leftfield' id='char_rename_add_link'>"+
		    "				<span class='title2rightfield-icon1-text'>"+
		    "				<a href='#' onclick='viewFileCharRenameOperation("+pathListCounter+");'><i class='fa fa-plus-circle'></i></a>"+
		    "				<a href='#' onclick='viewFileCharRenameOperation("+pathListCounter+");' id=''>"+
		    "				<strong>Destination File Naming Convention</strong>"+
		    "				</a>"+
		    "				</div>"+
	        //Update/Reset Button
	        "	<div class='col-md-6 no-padding'>"+
	        "  		<div class='form-group'>"+
	        "      		<div class='input-group ' id='" + pathListCounter + "_buttons-div'>"+
	"				<sec:authorize access='hasAuthority(\'UPDATE_PATHLIST\')'>"+
	"      				<button type='button' class='btn btn-grey btn-xs ' tabindex='41' id='" + pathListCounter + "_updatebtn' onclick=updatePathList(\'"+pathListCounter+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
	"					<button type='button' class='btn btn-grey btn-xs ' tabindex='42' id='" + pathListCounter + "_resetbtn' onclick=resetPathListDiv(\'"+pathListCounter+"\');><spring:message code='btn.label.reset'></spring:message></button>" +
	"				</sec:authorize>"+
	        "       	</div>"+
	        "					<div id='" + pathListCounter + "_progress-bar-div' class='input-group' style='display: none;'> "+
	        "						<label>Processing Request &nbsp;&nbsp; </label> "+
	        "							<img src='img/processing-bar.gif'>"+
			"					</div> "+
	        "   	</div>" +
	        "	</div>" +
	        "	<div class='col-md-6 no-padding' style='vertical-align:bottom;'>"+
			"		<span class='title2rightfield' style='text-decoration:none;'>" +
			" 			<spring:message code='ftp.driver.mgmt.pathlist.r.w.note'></spring:message><br/>" +
			"				<div class='pull-left mtop5' style='font-size:10px;'><i class='fa fa-square' style='font-size: 9px'></i>&nbsp;&nbsp;<spring:message code='restart.operation.require.message' ></spring:message> </div> "+
			"		</span>" +
	        "	</div>" +
	     	"</div>"+
			"</div>";
 			"</form>";
			
			$('#divPathList').prepend(pathListTable);
			$("#"+pathListCounter+"_remoteFileAction").val(remoteFileAction);
			$("#"+pathListCounter+"_fileGrepDateEnabled").val(fileGrepDateEnabled);
			$("#"+pathListCounter+"_position").val(position);
			$("#"+pathListCounter+"_fileSeqAlertEnabled").val(fileSeqAlertEnabled);
			$("#"+pathListCounter+"_resetFrequency").val(resetFrequency);
			$("#"+pathListCounter+"_fileSizeCheckEnabled").val(fileSizeCheckEnabled);
			$("#"+pathListCounter+"_duplicateFileSuffix").val(duplicateFileSuffix);
			changeFileSizeCheck(pathListCounter);
			changeFilterAction(pathListCounter);
			changeFileDateParam(pathListCounter); 
			changeMissingFileSequenceParams(pathListCounter); 
			changeFileSeqParam(pathListCounter);
			$("#pathList_"+pathListCounter+"_block").collapse("toggle");
			formatDuplicateCheckDropdown(pathListCounter);
			setValueInMultiSelect(pathListCounter, duplcateCheckParamsVal);
			
	 }
	 
	var duplicateCheckParamArray = [];

	function getDuplicateCheckParamList(counter){
		; 
		duplicateCheckParamArray = [];
		$.each($('#' + counter + '_duplicateCheckParamName').val(), function(i, item) {
			;
			duplicateCheckParamArray.push(item);
			
		});
	}
	 
	function createPathList(pathListCounter){
		
		var maxFileCountAlert = $("#"+pathListCounter+"_maxFilesCountAlert").val();
		 
		if(maxFileCountAlert === null || maxFileCountAlert === '' || maxFileCountAlert === 'undefined '){
			maxFileCountAlert = -1;
			$("#"+pathListCounter+"_maxFilesCountAlert").val(maxFileCountAlert);
		}
		
		resetWarningDisplay();
		clearAllMessages();
		getDuplicateCheckParamList(pathListCounter);
		showProgressBar(pathListCounter);
		$.ajax({
			url: '<%=ControllerConstants.CREATE_COLLECTIONDRIVER_PATHLIST%>',
			cache: false,
			async: true,
			dataType: 'json',
			type: "POST",
			
			data:
			 {
				"name" 						:	$('#' + pathListCounter + '_name').val(),
				"readFilePath"          	:	$('#' + pathListCounter + '_readFilePath').val(),
				"writeFilePath" 			:	$('#' + pathListCounter + '_writeFilePath').val(),
				"fileNamePattern"           :   $('#' + pathListCounter + '_fileNamePattern').val(),
				"readFilenamePrefix" 		:	$('#' + pathListCounter + '_readFilenamePrefix').val(),
				"readFilenameSuffix" 		:   $('#' + pathListCounter + '_readFilenameSuffix').val(),
				"readFilenameContains" 		:   $('#' + pathListCounter + '_readFilenameContains').val(),
				"writeFilenamePrefix"		:	$('#' + pathListCounter + '_writeFilenamePrefix').val(),
				"maxFilesCountAlert"		:	$('#' + pathListCounter + '_maxFilesCountAlert').val(),
				"remoteFileAction"			:	$('#' + pathListCounter + '_remoteFileAction').val(),
				"remoteFileActionParamName"	:	$('#' + pathListCounter + '_remoteFileActionParamName').val(),
				"remoteFileActionValue"		:	$('#' + pathListCounter + '_remoteFileActionValue').val(),
				"remoteFileActionParamNameTwo"		:	$('#' + pathListCounter + '_remoteFileActionParamNameTwo').val(),
				"remoteFileActionValueTwo"	:	$('#' + pathListCounter + '_remoteFileActionValueTwo').val(),
				"fileGrepDateEnabled"		:	$('#' + pathListCounter + '_fileGrepDateEnabled').val(),
				"dateFormat"				:	$('#' + pathListCounter + '_dateFormat').val(),
				"position"					:	$('#' + pathListCounter + '_position').val(),
				"startIndex"				:	$('#' + pathListCounter + '_startIndex').val(),
				"endIndex"					:	$('#' + pathListCounter + '_endIndex').val(),
				"fileSeqAlertEnabled"		:	$('#' + pathListCounter + '_fileSeqAlertEnabled').val(),
				"endIndex"					:	$('#' + pathListCounter + '_endIndex').val(),
				"seqStartIndex"				:	$('#' + pathListCounter + '_seqStartIndex').val(),
				"seqEndIndex"				:	$('#' + pathListCounter + '_seqEndIndex').val(),
				"driver.id"					:	$('#driverId').val(),
				"pathListCount"				:   $('#' + pathListCounter + '_pathListCount').val(),
				"service.id"			    :   parseInt('${serviceId}'),
				
				"duplicateCheckParamName"	:   duplicateCheckParamArray.join(),
				"duplicateFileSuffix"		: 	$('#' + pathListCounter + '_duplicateFileSuffix').val(),
				"timeInterval"				:   $('#' + pathListCounter + '_timeInterval').val(),
				"referenceDevice"			:   $('#' + pathListCounter + '_referenceDevice').val(),

				"parentDevice.id"					:   $('#' + pathListCounter + '_deviceId').val(),
				"missingFileSequenceId.minValue"					:   $('#' + pathListCounter + '_missingFileSequenceId_minValue').val(),
				"missingFileSequenceId.maxValue"					:   $('#' + pathListCounter + '_missingFileSequenceId_maxValue').val(),
				"missingFileSequenceId.resetFrequency"			:   $('#' + pathListCounter + '_resetFrequency').val(),
				"fileSizeCheckMinValue"					:   $('#' + pathListCounter + '_fileSizeCheckMinValue').val(),
				"fileSizeCheckMaxValue"					:   $('#' + pathListCounter + '_fileSizeCheckMaxValue').val(),
				"fileSizeCheckEnabled"		:	$('#' + pathListCounter + '_fileSizeCheckEnabled').val(),
				"validFileTimeInterval"				:   $('#' + pathListCounter + '_validFileTimeInterval').val()
			},
			
			success: function(data){
				hideProgressBar(pathListCounter);

				var response = eval(data);
				
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				if(responseCode == "200"){
					
					resetWarningDisplay();
					clearAllMessages();
					showSuccessMsg(responseMsg);
					$("#title_"+pathListCounter).text(responseObject["name"] + "-" + responseObject['pathId']);
					$("#"+pathListCounter+"_updatebtn").show();
					$("#"+pathListCounter+"_savebtn").hide();
					$('#'+pathListCounter+'_id').val(responseObject["id"]);
					$('#'+pathListCounter+'_pathId').val(responseObject['pathId']);
					//$("#pathList_"+pathListCounter+"_block").collapse("toggle");
					$('#'+pathListCounter+'_destFileRenamingDiv').show();
				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
					
					// $("#create-service-buttons-div").show();
					hideProgressBar(pathListCounter);
					addErrorIconAndMsgForAjax(responseObject); 
					
					
				}else{
					hideProgressBar(pathListCounter);
					resetWarningDisplay();
					clearAllMessages();
					showErrorMsg(responseMsg);
				}
 			},
		    error: function (xhr,st,err){
		    	hideProgressBar(pathListCounter);
				handleGenericError(xhr,st,err);
			}
		});
		
	}
	
	
	function updatePathList(pathListCounter){
		resetWarningDisplay();
		clearAllMessages();
		getDuplicateCheckParamList(pathListCounter);
		showProgressBar(pathListCounter);
		var fsId = 0;
		if(fsId != null && fsId != '' && fsId != 'undefined '){
			fsId = $('#' + pathListCounter + '_missingFileSequenceIdVal').val();
		}
		
		var maxFileCountAlert = $("#"+pathListCounter+"_maxFilesCountAlert").val();
		 
		if(maxFileCountAlert === null || maxFileCountAlert === '' || maxFileCountAlert === 'undefined '){
			maxFileCountAlert = -1;
			$("#"+pathListCounter+"_maxFilesCountAlert").val(maxFileCountAlert);
		}
		var pathId = $('#'+pathListCounter+'_pathId').val();
		if(pathId == null || pathId == ''){
			pathId = '000';
		}
		
		$.ajax({
			url: '<%=ControllerConstants.UPDATE_COLLECTIONDRIVER_PATHLIST%>',
			cache: false,
			async: true,
			dataType: 'json',
			type: "POST",
			data:
			 {
				"name" 						:	$('#' + pathListCounter + '_name').val(),
				"readFilePath"          	:	$('#' + pathListCounter + '_readFilePath').val(),
				"writeFilePath" 			:	$('#' + pathListCounter + '_writeFilePath').val(),
				"fileNamePattern"           :   $('#' + pathListCounter + '_fileNamePattern').val(),
				"readFilenamePrefix" 		:	$('#' + pathListCounter + '_readFilenamePrefix').val(),
				"readFilenameSuffix" 		:   $('#' + pathListCounter + '_readFilenameSuffix').val(),
				"readFilenameContains" 		:   $('#' + pathListCounter + '_readFilenameContains').val(),
				"writeFilenamePrefix"		:	$('#' + pathListCounter + '_writeFilenamePrefix').val(),
				"maxFilesCountAlert"		:	$('#' + pathListCounter + '_maxFilesCountAlert').val(),
				"remoteFileAction"			:	$('#' + pathListCounter + '_remoteFileAction').val(),
				"remoteFileActionParamName"	:	$('#' + pathListCounter + '_remoteFileActionParamName').val(),
				"remoteFileActionValue"		:	$('#' + pathListCounter + '_remoteFileActionValue').val(),
				"remoteFileActionParamNameTwo":	$('#' + pathListCounter + '_remoteFileActionParamNameTwo').val(),
				"remoteFileActionValueTwo"	:	$('#' + pathListCounter + '_remoteFileActionValueTwo').val(),
				"fileGrepDateEnabled"		:	$('#' + pathListCounter + '_fileGrepDateEnabled').val(),
				"dateFormat"				:	$('#' + pathListCounter + '_dateFormat').val(),
				"position"					:	$('#' + pathListCounter + '_position').val(),
				"startIndex"				:	$('#' + pathListCounter + '_startIndex').val(),
				"endIndex"					:	$('#' + pathListCounter + '_endIndex').val(),
				"fileSeqAlertEnabled"		:	$('#' + pathListCounter + '_fileSeqAlertEnabled').val(),
				"endIndex"					:	$('#' + pathListCounter + '_endIndex').val(),
				"seqStartIndex"				:	$('#' + pathListCounter + '_seqStartIndex').val(),
				"seqEndIndex"				:	$('#' + pathListCounter + '_seqEndIndex').val(),
				"driver.id"					:	$('#driverId').val(),
				"pathId"				    :	pathId,
				"pathListCount"				:   $('#' + pathListCounter + '_pathListCount').val(),
				"id"						:	$('#' + pathListCounter + '_id').val(),
				"service.id"			    :   parseInt('${serviceId}'),
				"service.servInstanceId"	:   '${serviceInstanceId}',
				"duplicateCheckParamName"	:   duplicateCheckParamArray.join(),
				"duplicateFileSuffix"		:   $('#' + pathListCounter + '_duplicateFileSuffix').val(),
				"timeInterval"				:   $('#' + pathListCounter + '_timeInterval').val(),
				"referenceDevice"			:   $('#' + pathListCounter + '_referenceDevice').val(),
				
				"parentDevice.id"				                	:   $('#' + pathListCounter + '_deviceId').val(),
				"missingFileSequenceId.minValue"					:   $('#' + pathListCounter + '_missingFileSequenceId_minValue').val(),
				"missingFileSequenceId.maxValue"					:   $('#' + pathListCounter + '_missingFileSequenceId_maxValue').val(),
				"missingFileSequenceId.resetFrequency"		     	:   $('#' + pathListCounter + '_resetFrequency').val(),
				"fileSeqId"			                                :   fsId,
				"fileSizeCheckMinValue"					:   $('#' + pathListCounter + '_fileSizeCheckMinValue').val(),
				"fileSizeCheckMaxValue"					:   $('#' + pathListCounter + '_fileSizeCheckMaxValue').val(),
				"fileSizeCheckEnabled"		:	$('#' + pathListCounter + '_fileSizeCheckEnabled').val(),
				"validFileTimeInterval"		:	$('#' + pathListCounter + '_validFileTimeInterval').val()
			}, 
			
			success: function(data){
				 hideProgressBar(pathListCounter);

				var response = eval(data);
				
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				
				if(responseCode == "200"){
					resetWarningDisplay();
					clearAllMessages();
					showSuccessMsg(responseMsg);
					
					$("#title_"+pathListCounter).text(responseObject["name"] + "-" + responseObject['pathId']);
					//$("#pathList_"+pathListCounter+"_block").collapse("toggle");
					$('#destFileRenamingDiv').show();
					
				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
					hideProgressBar(pathListCounter);
					
					showErrorMsg(responseMsg);
					addErrorIconAndMsgForAjax(responseObject); 
					
					
				}else{
					hideProgressBar(pathListCounter);
					resetWarningDisplay();
					clearAllMessages();
					showErrorMsg(responseMsg);
					 
				}
			},
		    error: function (xhr,st,err){
		    	hideProgressBar(pathListCounter);
				handleGenericError(xhr,st,err);
			}
		});
		
	}
	
	var pathListId , blockCounter;
	
	function deletePathListPopup(counter){
		blockCounter=counter;
		
		pathListId=$('#'+counter+'_id').val();
		
		if(pathListId == null || pathListId == 'null' || pathListId==''){
			removeBlock(counter);
		}else{
			$("#pathlistMessage").click();
		}
		
	}
	
	
	function deletePathListDetails(){
		resetWarningDisplay();
		clearAllMessages();
		 $.ajax({
			url: '<%=ControllerConstants.DELETE_COLLECTION_DRIVER_PATHLIST%>',
			cache: false,
			async: true,
			dataType: 'json',
			type: "POST",
			data:
			 {
				"pathlistId"   : pathListId
			 }, 
			
			success: function(data){
				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				if(responseCode == "200"){
					resetWarningDisplay();
					clearAllMessages();
					showSuccessMsg(responseMsg);
					
					$("#"+blockCounter + "_form").remove();
					closeFancyBox();
				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
					showErrorMsg(responseMsg);
					closeFancyBox();
				}else{
					resetWarningDisplay();
					clearAllMessages();
					showErrorMsg(responseMsg);
					closeFancyBox();
				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		}); 
	}
	
	
	function removeBlock(counter){
		$("#"+counter+"_block").remove();
	}
	
</script>

<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'PATH_LIST_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" id="pathListConfig">
		<div class="title2">
			<strong><spring:message code="pathlist.header.section.title"></spring:message></strong>
			
			<span class="title2rightfield" style="margin-bottom: 10px;">
			<sec:authorize access="hasAuthority('CREATE_PATHLIST')">
				<span class="title2rightfield-icon1-text">
				<a href="#" onclick="addNewPathList();"><i class="fa fa-plus-circle"></i></a>
				<a href="#" onclick="addNewPathList();" id="addPathList">
				<spring:message code="pathlist.add.link.header"></spring:message></a>
				</span>
			</sec:authorize>
			</span>
		</div>
	<div class="tab-content no-padding  mtop10" id="divPathList">
		<c:if test="${pathList != null && fn:length(pathList) gt 0}">
			<c:forEach var="path" items="${pathList}">
				<script type="text/javascript">
				var missingFileSequenceId = ${path.missingFileSequenceId}
					addPathDetail('${path.id}','${path.name}','${path.fileNamePattern}','${path.readFilePath}','${path.writeFilePath}','${path.readFilenamePrefix}','${path.readFilenameSuffix}',
 							'${path.readFilenameContains}','${path.readFilenameExcludeTypes}','${path.writeFilenamePrefix}','${path.maxFilesCountAlert}',
 							'${path.remoteFileAction}',
 							'${path.remoteFileActionParamName}',
 							'${path.remoteFileActionValue}',
 							'${path.remoteFileActionParamNameTwo}',
 							'${path.remoteFileActionValueTwo}',
 							'${path.fileGrepDateEnabled}',
 							'${path.position}',
 							'${path.dateFormat}',
 							'${path.startIndex}',
 							'${path.endIndex}',
 							'${path.fileSeqAlertEnabled}',
 							'${path.seqStartIndex}',
 							'${path.seqEndIndex}',
 							'${path.duplicateFileSuffix}',
 							'${path.timeInterval}',
 							'${path.missingFileSequenceId.minValue}',
 							'${path.missingFileSequenceId.maxValue}',
 							'${path.parentDevice.id}',
 							'${path.parentDevice.name}',
 							'${path.referenceDevice}',
 							'${path.missingFileSequenceId.resetFrequency}',
 							'${path.missingFileSequenceId.id}',
 							'${path.duplicateCheckParamName}',
 							'${path.pathId}',
 							'${path.fileSizeCheckMinValue}',
 							'${path.fileSizeCheckMaxValue}',
 							'${path.fileSizeCheckEnabled}',
 							'${path.validFileTimeInterval}'
					);
					
 				</script> 
			</c:forEach>
		</c:if>
		<jsp:include page="deviceDialog.jsp"></jsp:include>
		<div class="fullwidth">
			<div id="pathList"></div>
		</div>
	</div>
	
	<!-- Pathlist Delete popup code start here -->
    	<div id="divMessage" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10" >
		           <h4 class="modal-title" id="status-title" ><spring:message code="ftp.driver.pathlist.delete.header.label"></spring:message></h4>
		        </div>
		        
		        <div class="modal-body padding10 inline-form">
		        	<span id="deleteDriverResponseMsg" style="display:none;">
		        		<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
		        	</span>
			        <p id="deleteWarningMessage">
			       		 <spring:message code="pahtlist.delete.warn.message"></spring:message>
			        </p>
		        </div>
		        
		       <!-- To Do add role here -->
			        <div id="inactive-driver-div" class="modal-footer padding10">
				        <sec:authorize access="hasAuthority('DELETE_PATHLIST')">
				            <button type="button" class="btn btn-grey btn-xs " onclick="deletePathListDetails();"><spring:message code="btn.label.yes"></spring:message></button>
				        </sec:authorize>
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
			        </div>
			         <div class="modal-footer padding10" id="reaload-driver-details" style="display:none;">
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox()();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
    	<a href="#divMessage" class="fancybox" style="display: none;" id="pathlistMessage">#</a>
    	<!-- Pathlist Delete popup code end here -->
    	
    	<!-- View char rename operation parameters popup code start here-->
		<a href="#divCharRenameParams" class="fancybox" style="display: none;" id="char_rename_param_link">#</a>
		<div id="divCharRenameParams" style="width:100%; display: none;" >
			    <div class="modal-content">
			        <div class="modal-header padding10">
			            <h4 class="modal-title"><spring:message code="distributionService.pathlist.char.rename.sub.header"></spring:message></h4>
			        </div>
			        <div class="modal-body padding10 inline-form">
			        	<div style="width= calc(); display: none; text-align: center; padding-bottom: 0px;" class="errorMessage" id="errorMessageCharacterRename">
							<span id = "error_text" class="title" >Given input values may not be as per defined guideline. Get more detail on hover of error icon.</span>
						</div> 
		        		<div class="fullwidth">
		        			<span class="title2rightfield"> 
							    <span class="title2rightfield-icon1-text" id="char_rename_add_link" >
				   				<sec:authorize access="hasAuthority('UPDATE_PATHLIST')">	
				   					<a href="#" onclick="addCharRenameOperation('','','','','','','','','','','','','','ADD');" ><i class="fa fa-plus-circle"></i></a>
				          			<a href="#" id="addCharacterRenameOperations" onclick="addCharRenameOperation('','','','','','','','','','','','','','ADD');" >
				   					<spring:message code="btn.label.add" ></spring:message></a>
				   				</sec:authorize>
				   				</span> 
				   			</span> 
		        		</div>
						<div class="fullwidth" id="plugin_char_rename_details" style="overflow: auto;height:300px;">
						</div>
			        </div>
			        <div id="view_char_rename_params_buttons-div" class="modal-footer padding10">
			         	<button id="closePopup_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox(); resetWarningMsg();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
			    </div>
			    <!-- /.modal-content --> 
			</div>
		<!-- View char rename operation parameters popup code end here-->
		
		<!-- Plugin delete popup code end here -->
		
		
		<a href="#divCharRenameDeletemPopup" class="fancybox" style="display: none;" id="delete_char_rename_param_link">#</a>
		
		<!-- Delete Char Rename operaton parameter pop up code start here -->
			<div id="divCharRenameDeletemPopup" style="width:100%; display: none;" >
		    <div class="modal-content">
		    	<input type="hidden" id="deleteCharRenameBlockId"/>
		    	<input type="hidden" id="deleteCharRenameId"/>
		    	
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="delete.collection.service.charoperation.label"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        	<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
		        	
		        	<div>
		        		<spring:message code="delete.characterRename.warn.msg"></spring:message>
		        	</div>
		        </div>
		        <div id="delete_buttons-div" class="modal-footer padding10">
		         	<button type="button" id="btndelete" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="deleteCharRenameOperationParams();"><spring:message code="btn.label.delete"></spring:message></button>
		         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBoxFromChildIFrame();againFileRenamePopUp();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		        <div id="delete_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
				</div>	
		        
		    </div>
		    <!-- /.modal-content --> 
		</div>
		
		<!-- Delete char Rename operation parameter pop up code end here -->
	
    	
</div>

<script>
var charRenameOperationCounter = 0;

	function resetPathListDiv(pathListCounter){
		resetWarningDisplay();
		clearAllMessagesPopUp();
		clearAllMessages();
		//Added below code for MED-4634 fix
		$('#'+ pathListCounter+'_name').val('');
		$('#'+ pathListCounter+'_readFilePath').val('');
		$('#'+ pathListCounter+'_writeFilePath').val('');
		$('#'+ pathListCounter+'_readFilenamePrefix').val('');
		$('#'+ pathListCounter+'_readFilenameSuffix').val('');
		$('#'+ pathListCounter+'_readFilenameContains').val('');
		$('#'+ pathListCounter+'_writeFilenamePrefix').val('');
		$('#'+ pathListCounter+'_maxFilesCountAlert').val('');
		$('#'+ pathListCounter+'_remoteFileActionValue').val('');
		$('#'+ pathListCounter+'_timeInterval').val('');
		$('#'+ pathListCounter+'_validFileTimeInterval').val('');

		if($('#'+ pathListCounter+'_fileGrepDateEnabled').val()=='true'){
			$('#'+ pathListCounter+'_dateFormat').val('');			
			$('#'+ pathListCounter+'_startIndex').val('');
			$('#'+ pathListCounter+'_endIndex').val('');
		}
		if($('#'+ pathListCounter+'_fileSeqAlertEnabled').val()=='true'){
			//$('#'+ pathListCounter+'_seqStartIndex input').val('');
			//$('#'+ pathListCounter+'_seqEndIndex input').val('');
			//$('#'+ pathListCounter+'_maxCounterLimit').val('');
			$('#'+ pathListCounter+'_seqStartIndex').val('');
		    $('#'+ pathListCounter+'_seqEndIndex').val('');
		    $('#'+ pathListCounter+'_missingFileSequenceId_minValue').val('');
		    $('#'+ pathListCounter+'_missingFileSequenceId_maxValue').val('');		    
		}
		$("#"+pathListCounter+"_fileSizeCheckEnabled").val('false');
		changeFileSizeCheck(pathListCounter);
		$('#'+ pathListCounter+'_fileSizeCheckMinValue').val('-1');
		$('#'+ pathListCounter+'_fileSizeCheckMaxValue').val('-1');		    
		
		
	}
	
	function validateAdditionalParameter(pathListCounter){
	
		resetWarningDisplay();
		clearAllMessagesPopUp();
		showProgressBar(pathListCounter);
		var startIndex = $("#"+pathListCounter+"_startIndex").val();
		var endIndex = $("#"+pathListCounter+"_endIndex").val();
		var seqStartIndex = $("#"+pathListCounter+"_seqStartIndex").val();
		var seqEndIndex = $("#"+pathListCounter+"_seqEndIndex").val();
		if(startIndex === null || startIndex === '' || startIndex === 'undefined'){
			$("#"+pathListCounter+"_startIndex").val('-1')
		}
		if(seqStartIndex === null || seqStartIndex === '' || seqStartIndex === 'undefined'){
			$("#"+pathListCounter+"_seqStartIndex").val('-1')
		}
		if(seqEndIndex === null || seqEndIndex === '' || seqEndIndex === 'undefined'){
			$("#"+pathListCounter+"_seqEndIndex").val('-1')
		}
		$.ajax({
			url: '<%=ControllerConstants.VALIDATE_ADDTIONAL_COLLECTIONPATHLIST_PARAM%>',
			cache: false,
			async: true,
			dataType: 'json',
			type: "POST",
			data:
			 {
				"fileGrepDateEnabled"		:	$('#' + pathListCounter + '_fileGrepDateEnabled').val(),
				"dateFormat"				:	$('#' + pathListCounter + '_dateFormat').val(),
				"position"					:	$('#' + pathListCounter + '_position').val(),
				"startIndex"				:	$('#' + pathListCounter + '_startIndex').val(),
				"endIndex"					:	$('#' + pathListCounter + '_endIndex').val(),
				"fileSeqAlertEnabled"		:	$('#' + pathListCounter + '_fileSeqAlertEnabled').val(),
				"endIndex"					:	$('#' + pathListCounter + '_endIndex').val(),
				"seqStartIndex"				:	$('#' + pathListCounter + '_seqStartIndex').val(),
				"seqEndIndex"				:	$('#' + pathListCounter + '_seqEndIndex').val(),
				"pathListCount"				:   $('#' + pathListCounter + '_pathListCount').val(),
				
			}, 
			
			success: function(data){
				 hideProgressBar(pathListCounter);

				var response = eval(data);
				
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				
				if(responseCode == "200"){
					resetWarningDisplay();
					clearAllMessagesPopUp();
					closeFancyBox();
					
				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
					hideProgressBar(pathListCounter);
					
					showErrorMsgPopUp(responseMsg);
					addErrorIconAndMsgForAjaxPopUp(responseObject); 
					
					
				}else{
					hideProgressBar(pathListCounter);
					resetWarningDisplay();
					clearAllMessagesPopUp();
					showErrorMsgPopUp(responseMsg);
					 
				}
			},
		    error: function (xhr,st,err){
		    	hideProgressBar(pathListCounter);
				handleGenericError(xhr,st,err);
			}
		});
		
	}
	function showErrorMsgPopUp(errorMsg){
		$(".divResponseMsg").html(
				'<div style="width: calc();" class="errorMessage" id="divButtonPopUp">'  +
					'<span id="span_success_txt" class="title">' + errorMsg + '</span>' +
				'</div>'	
		);
		$(".divResponseMsg").show();
	}

	function showSuccessMsgPopUp(msg){
		$(".divResponseMsg").html(
				'<div style="width:calc();" class="successMessage" id="divButtonPopUp">'  +
					'<span id="span_success_txt" class="title">' + msg + '</span>' +
				'</div>'	
		);
		$(".divResponseMsg").show();
	}
	
	function clearAllMessagesPopUp(){
		clearResponseMsgDivPopUp();
		clearResponseMsgPopUp();
		clearErrorMsgpopUp();
	}

	function clearResponseMsgDivPopUp(){
		$('#responseMsgDivpopUp').html('');
		$('.divResponseMsg').html('');
		$(".divResponseMsg").hide();
		
	}
	function clearResponseMsgPopUp(){
		$('#divResponseMsgPopUp').html('');
		$('.divResponseMsg').html('');
		$(".divResponseMsg").hide();
		
	}
	function clearErrorMsgpopUp(){
		$('#divErrorMsgPopUp').html('');
		$('.divResponseMsg').html('');
		$(".divResponseMsg").hide();
		
	}

	
	function showProgressBar(pathListCounter){
		$('#' + pathListCounter + '_buttons-div').hide();
		$('#' + pathListCounter + '_progress-bar-div').show();
	}

	function hideProgressBar(pathListCounter){
		$('#' + pathListCounter + '_buttons-div').show();
		$('#' + pathListCounter + '_progress-bar-div').hide();
	}
	
	$(window).keydown(function(event){
	    if(event.keyCode == 13) {
	      return false;
	    }
	  });
	
	function addCharRenameOperation(){
		addCharRenameOperation('','','','','','','','','','','','','','ADD');
	}
	
	function addCharRenameOperation(id,seqNumber,defaultValue,dateFormat,srcDateFormat,dateValue,startIndex,endIndex,position,length,padding,paddingValue,renameQuery,actionType){
		charRenameOperationCounter++
		
		console.log("addCharRenameOperation::startIndex::" + startIndex);
		var flag=0;
		 if(startIndex== 0){
			 startIndex= 1;
			 flag=1;
		 }
		
		
		 if(String(defaultValue) === 'null' || String(defaultValue) === 'undefined'){
			 defaultValue = '';
		 } 

		 if(String(renameQuery) === 'null' || String(renameQuery) === 'undefined'){
			 renameQuery = '';
		 } 
		 
		 if(String(paddingValue) === 'null' || String(paddingValue) === 'undefined'){
			 paddingValue = '';
		 } 
		 
		 if(startIndex==-1){
			 startIndex='';
		 }
		
		 if(endIndex==-1){
			 endIndex='';
		 }
		 if(length==-1){
			 length='';
		 }
		 
		 if(dateFormat != 'Other'){
			 $("#" + charRenameOperationCounter + "_srcDateFormat").val('');
		 }else{
			 $("#" + charRenameOperationCounter + "_srcDateFormat").val(srcDateFormat);
		 }
		
		var charHtmlBody =  "<form method='POST' id='" + charRenameOperationCounter + "_char_form'> "+
							"<div class='box box-warning' id='flipbox_char_"+charRenameOperationCounter+"'>  "+
						    "<input type='hidden' id='"+charRenameOperationCounter+"_char_rename_id'  value='"+id+"'> "+
							"<div class='box-header with-border'> "+
							"	<h3 class='box-title' id='title_"+charRenameOperationCounter+"'>"+
							"       	<spring:message code='collectionService.pathlist.char.rename.sub.header'></spring:message>"+
							"	</h3>  "+
							"	<div class='box-tools pull-right' style='top: -3px;' id='action_"+charRenameOperationCounter+"'> "+ 
							"		<button id='"+charRenameOperationCounter+"_collapse_block' class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' href='#"+charRenameOperationCounter+"_char_operation_block'> "+ 
							"			<i class='fa fa-minus'></i> "+
							"		</button> "+
							"		<sec:authorize access='hasAuthority(\'UPDATE_PATHLIST\')'>"+
						    "					&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;' onclick=displayDeleteCharRenamePopup(\'"+charRenameOperationCounter+"\');></a>&nbsp;" +
						    "		</sec:authorize>"+
							"	</div> "+
							"</div> "+
							"<div class='box-body inline-form accordion-body collapsed in' id='"+charRenameOperationCounter+"_char_operation_block'> "+ 
							"	<div class='fullwidth inline-form'>  "+
							"		 <div class='col-md-12 no-padding'>"+
						    "       	<spring:message code='collectionService.pathlist.plugin.sequence.label.tooltip' var='tooltip'></spring:message>"+
						    "       	<spring:message code='collectionService.pathlist.plugin.sequence.label' var='label'></spring:message>"+
						    "        	<div class='form-group'>"+
						    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
						    "         		<div class='input-group' style = 'width:40%;'>"+
						    "         			<input  id='" + charRenameOperationCounter + "_sequenceNo' class='form-control table-cell input-sm' tabindex='1' data-toggle='tooltip' data-placement='bottom' title='${tooltip}' onkeydown='isNumericOnKeyDown(event)' />"+
						    "         			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
						    "         		</div>"+
						    "        	</div>"+
						    "        </div>"+
							"		 <div class='col-md-12 no-padding'>"+
							"			<spring:message code='collectionService.pathlist.plugin.defaultValue.label' var='label'></spring:message> "+
							"			<spring:message code='collectionService.pathlist.plugin.defaultValue.label.tooltip' var='tooltip'></spring:message>"+
							"			<div class='col-md-12 no-padding form-group'>"+
							"			<div class='table-cell-label'>${label}</div> "+
							"			<div class='input-group' style='width:40%'>"+
							"				<input id='"+charRenameOperationCounter+"_defaultValue' value='"+defaultValue+"' class='form-control table-cell input-sm' tabindex='1' data-toggle='tooltip' data-placement='bottom' title='${tooltip}' onchange='disableDefaultValue("+charRenameOperationCounter+");'/>"+
							"				<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
							"			</div>"+
							"			</div>"+
							"		 </div>"+
							"		 <div class='col-md-12 no-padding'> "+
							"			<spring:message code='collectionService.pathlist.plugin.date.label' var='label'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'><strong>${label}</strong></div> "+ 
							"			</div> "+
							"		 </div> "+
							"		 <div class='col-md-6 no-padding'> "+
							"			<spring:message code='collectionService.pathlist.plugin.dateFormet.label' var='label'></spring:message> "+
							"			<spring:message code='collectionService.pathlist.plugin.dateFormet.label.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<select class='form-control table-cell input-sm' id='" + charRenameOperationCounter + "_dateFormatCharRename' tabindex='3' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' onchange = 'enableOtherTextBox("+charRenameOperationCounter+");'>"+
						    "				    	<c:forEach var='sourceDateFormetEnum' items='${sourceDateFormetEnum}' >"+
						    "							<c:if test='${sourceDateFormetEnum.name eq "NA"}'>"+											
							"								<option value=''>${sourceDateFormetEnum.name}</option>"+
							"							</c:if>"+
							"							<c:if test='${sourceDateFormetEnum.name ne "NA"}'>"+	
							"								<option value='${sourceDateFormetEnum.name}'>${sourceDateFormetEnum.name}</option>"+
							"							</c:if>"+
							"				    	</c:forEach>"+
							"					</select>"+
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							"				</div> "+
							"			</div> "+
							"		 </div> "+
							"		 <div class='col-md-6 no-padding'>"+
							"			<spring:message code='collectionService.pathlist.plugin.otherDateFormet.label' var='label'></spring:message> "+
							"			<spring:message code='collectionService.pathlist.plugin.otherDateFormet.label.tooltip' var='tooltip'></spring:message>"+
							"			<div class='col-md-6 no-padding form-group'>"+
							"			<div class='input-group' style='width:60%;'>"+
							"				<input id='"+charRenameOperationCounter+"_srcDateFormat'  value='"+srcDateFormat+"' readonly = 'true' class='form-control table-cell input-sm' tabindex='1' data-toggle='tooltip' data-placement='bottom' title='${tooltip}' />"+
							"				<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
							"			</div>"+
							"			</div>"+
							"		 </div>"+
							"		 <div class='col-md-12 no-padding'>"+
							"			<spring:message code='collectionService.pathlist.plugin.dateType.label' var='label'></spring:message> "+
							"			<spring:message code='collectionService.pathlist.plugin.dateType.label.tooltip' var='tooltip'></spring:message>"+
							"			<div class='col-md-12 no-padding form-group'>"+
							"			<div class='table-cell-label'>${label}</div> "+
							"			<div class='input-group' style='width:30%;'>"+
							"					<select class='form-control table-cell input-sm' id='" + charRenameOperationCounter + "_dateValue' tabindex='3' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
						    "				    	<c:forEach var='dateTypeEnum' items='${dateTypeEnum}' >"+
							"							<option value='${dateTypeEnum.name}'>${dateTypeEnum}</option>"+
							"				    	</c:forEach>"+
							"					</select>"+
							"			</div>"+
							"			</div>"+
							"		 </div>"+
							"		 <div class='col-md-12 no-padding'> "+
							"			<spring:message code='collectionService.pathlist.plugin.extractFromOriginal.label' var='label'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'><strong>${label}</strong></div> "+ 
							"			</div> "+
							"		 </div> "+
						    "		 <div class='col-md-6 no-padding'>"+
						    "		 <div class='form-group'>"+
							"				<div class='col-md-4 no-padding'>"+
							"       			<spring:message code='collectionService.pathlist.plugin.end.index.label.tooltip' var='maxIndex'></spring:message>"+
							"       			<spring:message code='collectionService.pathlist.plugin.start.index.label.tooltip' var='minIndex'></spring:message>"+
							"       			<spring:message code='collectionService.pathlist.plugin.index.label' var='label'></spring:message>"+
							"					<div class='table-cell-label'>${label}</div>"+
							"				</div>"+
							"				<div class='col-md-4 no-padding'>"+
							"					<div class='form-group'>"+
							"                         	<div class='input-group'>"+
							"                         		<input tabindex='4' id='" + charRenameOperationCounter + "_char_startIndex' class='form-control table-cell input-sm'  data-toggle='tooltip' data-placement='bottom' title='${minIndex}' value='"+startIndex+"' onkeydown='isNumericOnKeyDown(event)' onchange='disablePadding("+charRenameOperationCounter+");'/>"+
						    "         						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
							"                         	</div>"+
							"                        </div> "+	
							"				</div>"+
							"				<div class='col-md-4 no-padding'>"+
							"					<div class='form-group'>"+
							"                        	<div class='input-group'>"+ 
							"                         		<input tabindex='5' id='" + charRenameOperationCounter + "_char_endIndex' class='form-control table-cell input-sm'  data-toggle='tooltip' data-placement='bottom' title='${maxIndex}' value='"+endIndex+"'  onkeydown='isNumericOnKeyDown(event)'/>"+
						    "         						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
							"                         	</div>"+
							"                        </div> "+	
							"				</div>"+
							"		</div>"+
						    " 		</div>"+	
						    "		 <div class='col-md-6 no-padding'> "+
							"			<spring:message code='collectionService.pathlist.plugin.position.label' var='label'></spring:message> "+
							"			<spring:message code='collectionService.pathlist.plugin.position.label.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<select class='form-control table-cell input-sm' id='" + charRenameOperationCounter + "_positionCharForm' tabindex='3' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
						    "				    	<c:forEach var='positionEnum' items='${positionEnum}' >"+
							"							<option value='${positionEnum.value}'>${positionEnum}</option>"+
							"				    	</c:forEach>"+
							"					</select>"+
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							"				</div> "+
							"			</div> "+
							"		 </div> "+
							"		 <div class='col-md-12 no-padding'> "+
							"			<spring:message code='collectionService.pathlist.plugin.padding.label' var='label'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'><strong>${label}</strong></div> "+ 
							"			</div> "+
							"		 </div> "+
							"		 <div class='col-md-12 no-padding'>"+
						    "       	<spring:message code='collectionService.pathlist.plugin.length.label.tooltip' var='tooltip'></spring:message>"+
						    "       	<spring:message code='collectionService.pathlist.plugin.length.label' var='label'></spring:message>"+
						    "        	<div class='form-group'>"+
						    "    			<div class='table-cell-label'>${label}</div>"+
						    "         		<div class='input-group' style = 'width:40%;'>"+
						    "         			<input  id='" + charRenameOperationCounter + "_length' value='"+length+"' class='form-control table-cell input-sm' tabindex='1' data-toggle='tooltip' data-placement='bottom' title='${tooltip}' onkeydown='isNumericOnKeyDown(event)' onchange='disablePadding("+charRenameOperationCounter+");'/>"+
						    "         			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
						    "         		</div>"+
						    "        	</div>"+
						    "        </div>"+
						    "		 <div class='col-md-6 no-padding'> "+
							"			<spring:message code='collectionService.pathlist.plugin.padding.label' var='label'></spring:message> "+
							"			<spring:message code='collectionService.pathlist.plugin.padding.label.tooltip' var='tooltip'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'>${label}</div> "+ 
							"				<div class='input-group '> "+
							"					<select class='form-control table-cell input-sm' id='" + charRenameOperationCounter + "_paddingType' tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' >"+
						    "				    	<c:forEach var='positionEnum' items='${positionEnum}' >"+
							"							<option value='${positionEnum.value}'>${positionEnum}</option>"+
							"				    	</c:forEach>"+
							"					</select>"+
							"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
							"				</div> "+
							"			</div> "+
							"		 </div> "+
							"		 <div class='col-md-6 no-padding'>"+
						    "       	<spring:message code='collectionService.pathlist.plugin.default.label.tooltip' var='tooltip'></spring:message>"+
						    "       	<spring:message code='collectionService.pathlist.plugin.default.label' var='label'></spring:message>"+
						    "        	<div class='form-group'>"+
						    "    			<div class='table-cell-label'>${label}</div>"+
						    "         		<div class='input-group'>"+
						    "         			<input  id='" + charRenameOperationCounter + "_paddingValue' class='form-control table-cell input-sm' tabindex='6'  data-toggle='tooltip' data-placement='bottom' title='${tooltip}' value='"+paddingValue+"' maxLength='1'/>"+
						    "         			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
						    "         		</div>"+
						    "        	</div>"+
						    "        </div>"+
						    "		 <div class='col-md-12 no-padding'> "+
							"			<spring:message code='collectionService.pathlist.plugin.renameQuery.label' var='label'></spring:message> "+
							"			<div class='form-group'>  "+
							"				<div class='table-cell-label'><strong>${label}</strong></div> "+ 
							"			</div> "+
							"		 </div> "+
						    "		 <div class='col-md-12 no-padding'>"+
						    "       	<spring:message code='collectionService.pathlist.plugin.query.label.tooltip' var='tooltip'></spring:message>"+
						    "       	<spring:message code='collectionService.pathlist.plugin.query.label' var='label'></spring:message>"+
						    "        	<div class='form-group'>"+
						    "    			<div class='table-cell-label'>${label}</div>"+
						    "         		<div class='input-group'>"+
						    "         			<input  id='" + charRenameOperationCounter + "_query' class='form-control table-cell input-sm' tabindex='7'  data-toggle='tooltip' data-placement='bottom' title='${tooltip}' value='"+length+"' onchange='disableQuery("+charRenameOperationCounter+");'/>"+
						    "         			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
						    "         		</div>"+
						    "        	</div>"+
						    "        </div>"+
						    "		<div class='col-md-12 no-padding'>"+
						    "  			<div class='form-group'>"+
						    "      			<div id='" + charRenameOperationCounter + "_char_buttons-div' class='input-group '>"+
							"					<sec:authorize access='hasAuthority(\'UPDATE_PATHLIST\')'>"+	
				    		"						<button type='button' class='btn btn-grey btn-xs ' id='" + charRenameOperationCounter + "_char_savebtn' onclick=addCharRenamOperationParams(\'"+charRenameOperationCounter+"\');><spring:message code='btn.label.save'></spring:message></button>&nbsp;" +
							"						<button type='button' class='btn btn-grey btn-xs ' id='" + charRenameOperationCounter + "_char_updatebtn' onclick=updateCharRenamOperationParams(\'"+charRenameOperationCounter+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
							"						<button type='button' class='btn btn-grey btn-xs ' onclick=resetCharRenameParameters(\'"+charRenameOperationCounter+"\');><spring:message code='btn.label.reset'></spring:message></button>&nbsp;" +
							"					</sec:authorize>"+	
				     		"       		</div>"+
						    "					<div id='" + charRenameOperationCounter + "_char_progress-bar-div' class='input-group' style='display: none;'> "+
						    "						<label>Processing Request &nbsp;&nbsp; </label> "+
						    "							<img src='img/processing-bar.gif'>"+
							"					</div> "+
						    "   		</div>" +
						    "		</div>" +
						    
						    
							"	</div> "+
							"</div> "+
							"</div> "+
							"</form>";
							
			$('#plugin_char_rename_details').append(charHtmlBody);		
			
			if(actionType == 'UPDATE'){
				
				$('#' + charRenameOperationCounter + '_positionCharForm').val(position.toString());
				$('#' + charRenameOperationCounter + '_paddingType').val(padding.toString());
				if(startIndex!='' && startIndex>=1 && flag== 0){
					startIndex++;
					$('#' + charRenameOperationCounter + '_char_startIndex').val(startIndex);
				}
				$('#' + charRenameOperationCounter + '_query').val(renameQuery.toString());
				
				$("#"+charRenameOperationCounter+"_char_savebtn").hide();
				$("#"+charRenameOperationCounter+"_char_updatebtn").show();
				
				$("#"+charRenameOperationCounter+"_char_operation_block").collapse("toggle");
				
				$("#"+charRenameOperationCounter+"_sequenceNo").val(seqNumber);
				$("#"+charRenameOperationCounter+"_dateFormatCharRename").val(dateFormat).change();
				$("#"+charRenameOperationCounter+"_dateValue").val(dateValue);
				if(defaultValue!=null && defaultValue!=undefined && defaultValue!=""){
					disableDefaultValue(charRenameOperationCounter);
				} else if(renameQuery!=null && renameQuery!=undefined && renameQuery!=""){
					disableQuery(charRenameOperationCounter);
				} else if(dateFormat!=null && dateFormat!=undefined && dateFormat!=""){
					disableDateFormate(charRenameOperationCounter);
				} else if(startIndex>-1 || length>-1){
					disablePadding(charRenameOperationCounter);
				} 
				
			}else{
				$("#"+charRenameOperationCounter+"_char_updatebtn").hide();
				$("#"+charRenameOperationCounter+"_char_savebtn").show();
			}
			console.log("addCharRenameOperation:last::" + startIndex);
	}
	
	function againFileRenamePopUp(){
		viewFileCharRenameOperation(pathListCounter);
	}
	
	function viewFileCharRenameOperation(pathListCounter){
		currentSelectedPathListID=pathListCounter;
		$('#errorMessageCharacterRename').hide();
		resetWarningDisplay();
		clearAllMessagesPopUp();
		$.ajax({
			url : 'getCollectionCharRenameOperationById',
			cache : false,
			async : true,
			dataType : 'json',
			type : 'POST',
			data : {
				"id" : $('#'+pathListCounter+'_id').val()
			},
			success : function(data) {
				var response = data;
				var responseCode = response.code;
				var responseObject = response.object;
				
				$('#plugin_char_rename_details').html('');

				if (responseCode === "200") {

					if (responseObject !== 'undefined' && responseObject !== null) {
						$.each(responseObject, function(index, responseObject) {
							addCharRenameOperation(responseObject["id"],
									responseObject["sequenceNo"],
									responseObject["defaultValue"],
									responseObject["dateFormat"],
									responseObject["srcDateFormat"],
									responseObject["dateType"],
									responseObject["startIndex"],
									responseObject["endIndex"],
									responseObject["position"],
									responseObject["length"],
									responseObject["paddingType"],
									responseObject["paddingValue"],
									responseObject["query"],'UPDATE');
						});
					}
				}
				
				$("#char_rename_add_link").show();
				$("#char_rename_param_link").click();
			},
			error : function(xhr, st, err) {
				handleGenericError(xhr, st, err);
			}
		});

	}
	
	function addCharRenamOperationParams(counter){
		clearAllMessagesPopUp();
		resetWarningDisplayPopUp();
		$('#' + counter + '_char_buttons-div').hide();
		$('#' + counter + '_char_progress-bar-div').show();
		$('#' + counter + '_char_rename_id').val('0');

		var fileNameLength = $('#' + counter + '_length').val();
		if (fileNameLength == '' || fileNameLength == null) {
			fileNameLength = -1;
			$('#' + counter + '_length').val("");
		}

		var startIndex = $('#' + counter + '_char_startIndex').val();
		if (startIndex == '' || startIndex == null || startIndex==-1) {
			startIndex = -1;
			$('#' + counter + '_char_startIndex').val("");
		}
		
		var endIndex = $('#' + counter + '_char_endIndex').val();
		if (endIndex == '' || endIndex == null) {
			endIndex = -1;
			$('#' + counter + '_char_endIndex').val("");
		}
		var dateFormat = $('#' + counter + '_dateFormatCharRename').val();
		var srcDateFormat = '';
		if(dateFormat == 'Other'){
			srcDateFormat = $('#' + counter + '_srcDateFormat').val();
		}else{
			srcDateFormat = dateFormat;
		}
		
		$
				.ajax({
					url : 'createCollectionCharRenameParams',
					cache : false,
					async : true,
					dataType : 'json',
					type : "POST",
					data : {
						"id" 			: $('#' + counter + '_char_rename_id').val(),
						"sequenceNo" 	: $('#' + counter + '_sequenceNo').val(),
						"query" 		: $('#' + counter + '_query').val(),
						"position" 		: $('#' + counter + '_positionCharForm').val(),
						"startIndex" 	: startIndex,
						"endIndex" 		: endIndex,
						"paddingType" 	: $('#' + counter + '_paddingType').val(),
						"defaultValue" 	: $('#' + counter + '_defaultValue').val(),
						"length" 		: fileNameLength,
						"pathList.id" 	: $('#'+currentSelectedPathListID+'_id').val(),
						"paddingValue" 	: $('#' + counter + '_paddingValue').val(),
						"dateFormat" 	: dateFormat,
						"srcDateFormat" : srcDateFormat,
						"dateType" 	: $('#' + counter + '_dateValue').val(),
						"blockCount" : counter

					},

					success : function(data) {
						$('#' + counter + '_char_buttons-div').show();
						$('#' + counter + '_char_progress-bar-div').hide();

						var response = data;

						var responseCode = response.code;
						var responseMsg = response.msg;
						var responseObject = response.object;

						if (responseCode === "200") {
							$('#errorMessageCharacterRename').hide();
							clearAllMessagesPopUp();
							resetWarningDisplayPopUp();
							showSuccessMsgPopUp(responseMsg);
							
							$("#" + counter + "_char_updatebtn").show();
							$("#" + counter + "_char_savebtn").hide();

							$('#' + counter + '_char_rename_id').val(
									responseObject.id);
							$("#" + counter + "_char_operation_block").collapse(
									"toggle");

							$('#' + counter + '_sequenceNo').val(
									responseObject.sequenceNo);
							$('#' + counter + '_query').val(responseObject.query);
							$('#' + counter + '_positionCharForm').val(
									responseObject.position.toString());
							$('#' + counter + '_defaultValue').val(
									responseObject.defaultValue);
							$('#' + counter + '_paddingType').val(
									responseObject.paddingType.toString());
							
							var charStartIndex = responseObject.startIndex;
							console.log("charStartIndex1::" + charStartIndex);
							if(charStartIndex>=1){
								charStartIndex++;
								$('#' + counter + '_char_startIndex').val(charStartIndex);
								console.log("charStartIndex2::" + charStartIndex);
							} else {
								$('#' + counter + '_char_startIndex').val('');
								console.log("charStartIndex3::" + charStartIndex);
							}
							if(responseObject.endIndex==-1){
								$('#' + counter + '_char_endIndex').val('');
							} else {
								$('#' + counter + '_char_endIndex').val(responseObject.endIndex);
							}
							
							if(responseObject.length==-1){
								$('#' + counter + '_length').val('');
							}else{
								$('#' + counter + '_length').val(responseObject.length);
							}
							
							$('#' + counter + '_dateFomatCharForm').val(responseObject.dateFormat);
							if(responseObject.dateFormat == 'Other'){
								$('#' + counter + '_srcDateFomat').val(responseObject.srcDateFormat);	
							}else{	
								responseObject.srcDateFormat = '';
								$('#' + counter + '_srcDateFomat').val(responseObject.srcDateFormat);	
							}
							
						} else if (responseObject !== undefined
								&& responseObject !== 'undefined'
								&& responseCode === "400") {
							
							$('#' + counter + '_char_buttons-div').show();
							$('#' + counter + '_char_progress-bar-div').hide();
							addErrorIconAndMsgForAjax(responseObject);
							$('#errorMessageCharacterRename').show();
						} else {
							$('#' + counter + '_char_buttons-div').show();
							$('#' + counter + '_char_progress-bar-div').hide();
							clearAllMessagesPopUp();
							resetWarningDisplayPopUp();
							$('#errorMessageCharacterRename').hide();
							showErrorMsgPopUp(responseMsg);
						}
					},
					error : function(xhr, st, err) {
						$('#' + counter + '_char_buttons-div').show();
						$('#' + counter + '_char_progress-bar-div').hide();
						handleGenericError(xhr, st, err);
					}
				});
		
	}
	
	function enableOtherTextBox(counter){
		resetWarningDisplay();
		clearAllMessages();
		var dateFormat = $( "#" + counter + '_dateFormatCharRename').find(":selected").val();
	    	if(dateFormat == 'Other'){
			    $("#"+ counter + "_srcDateFormat").attr('readOnly',false);
		   }else{
			   $("#"+ counter + "_srcDateFormat").attr('readOnly',true);
			   $("#"+ counter + "_srcDateFormat").val('');
		   }
	    	disableDateFormate(counter);	
	}
	
	function disableDefaultValue(charRenameOperationCounter){
		if ($("#"+ charRenameOperationCounter + "_defaultValue").val() != "" || $("#"+ charRenameOperationCounter + "_defaultValue").val().length > 0) {
		   $("#"+ charRenameOperationCounter + "_dateFormatCharRename").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_srcDateFormat").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_dateValue").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_char_startIndex").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_char_endIndex").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_positionCharForm").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_length").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_paddingType").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_paddingValue").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_query").prop('disabled',true);
		   $("#"+ charRenameOperationCounter + "_dateFormatCharRename").val('');
		   $("#"+ charRenameOperationCounter + "_srcDateFormat").val('');
		   $("#"+ charRenameOperationCounter + "_char_startIndex").val('');
		   $("#"+ charRenameOperationCounter + "_char_endIndex").val('');
		   $("#"+ charRenameOperationCounter + "_length").val('');
		   $("#"+ charRenameOperationCounter + "_paddingValue").val('');
		   $("#"+ charRenameOperationCounter + "_query").val('');
		   
		} else {
		   $("#"+ charRenameOperationCounter + "_dateFormatCharRename").prop('disabled',false);
		   $("#"+ charRenameOperationCounter + "_srcDateFormat").prop('disabled',false);
		   $("#"+ charRenameOperationCounter + "_dateValue").prop('disabled',false);
		   $("#"+ charRenameOperationCounter + "_char_startIndex").prop('disabled',false);
		   $("#"+ charRenameOperationCounter + "_char_endIndex").prop('disabled',false);
		   $("#"+ charRenameOperationCounter + "_positionCharForm").prop('disabled',false);
		   $("#"+ charRenameOperationCounter + "_length").prop('disabled',false);
		   $("#"+ charRenameOperationCounter + "_paddingType").prop('disabled',false);
		   $("#"+ charRenameOperationCounter + "_paddingValue").prop('disabled',false);
		   $("#"+ charRenameOperationCounter + "_query").prop('disabled',false);
		}	   
	}
	
	function disableQuery(charRenameOperationCounter){
		if ($("#"+ charRenameOperationCounter + "_query").val() != "" || $("#"+ charRenameOperationCounter + "_defaultValue").val().length > 0) {
			   $("#"+ charRenameOperationCounter + "_defaultValue").prop('disabled',true);   
			   $("#"+ charRenameOperationCounter + "_dateFormatCharRename").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_srcDateFormat").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_dateValue").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_char_startIndex").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_char_endIndex").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_positionCharForm").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_length").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_paddingType").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_paddingValue").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_defaultValue").val('');
			   $("#"+ charRenameOperationCounter + "_dateFormatCharRename").val('');
			   $("#"+ charRenameOperationCounter + "_srcDateFormat").val('');
			   $("#"+ charRenameOperationCounter + "_char_startIndex").val('');
			   $("#"+ charRenameOperationCounter + "_char_endIndex").val('');
			   $("#"+ charRenameOperationCounter + "_length").val('');
			   $("#"+ charRenameOperationCounter + "_paddingValue").val('');
			} else {
			   $("#"+ charRenameOperationCounter + "_defaultValue").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_dateFormatCharRename").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_srcDateFormat").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_dateValue").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_char_startIndex").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_char_endIndex").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_positionCharForm").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_length").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_paddingType").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_paddingValue").prop('disabled',false);
			}
	}
	
	function disablePadding(charRenameOperationCounter){
			if ($("#"+ charRenameOperationCounter + "_char_startIndex").val() != "" || $("#"+ charRenameOperationCounter + "_char_startIndex").val().length > 0 || $("#"+ charRenameOperationCounter + "_length").val() != "" || $("#"+ charRenameOperationCounter + "_defaultValue").val().length > 0) {
			   $("#"+ charRenameOperationCounter + "_dateFormatCharRename").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_srcDateFormat").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_dateValue").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_defaultValue").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_query").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_defaultValue").val('');
			   $("#"+ charRenameOperationCounter + "_dateFormatCharRename").val('');
			   $("#"+ charRenameOperationCounter + "_srcDateFormat").val('');
			   $("#"+ charRenameOperationCounter + "_query").val('');
			} else {
			   $("#"+ charRenameOperationCounter + "_defaultValue").prop('disabled',false);	
			   $("#"+ charRenameOperationCounter + "_dateFormatCharRename").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_srcDateFormat").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_dateValue").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_query").prop('disabled',false);
			}
	}
	
	function disableDateFormate(charRenameOperationCounter){
		if ($("#"+ charRenameOperationCounter + "_dateFormatCharRename").val() != "") {
			   $("#"+ charRenameOperationCounter + "_defaultValue").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_char_startIndex").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_char_endIndex").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_positionCharForm").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_length").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_paddingType").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_paddingValue").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_query").prop('disabled',true);
			   $("#"+ charRenameOperationCounter + "_defaultValue").val('');
			   $("#"+ charRenameOperationCounter + "_char_startIndex").val('');
			   $("#"+ charRenameOperationCounter + "_char_endIndex").val('');
			   $("#"+ charRenameOperationCounter + "_length").val('');
			   $("#"+ charRenameOperationCounter + "_paddingValue").val('');
			   $("#"+ charRenameOperationCounter + "_query").val('');
			} else {
			   $("#"+ charRenameOperationCounter + "_defaultValue").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_char_startIndex").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_char_endIndex").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_positionCharForm").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_length").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_paddingType").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_paddingValue").prop('disabled',false);
			   $("#"+ charRenameOperationCounter + "_query").prop('disabled',false);
			}
	}
	var charRenameBlockCounter, charRenameId;
	function displayDeleteCharRenamePopup(counter) {
		charRenameId = $('#' + counter + '_char_rename_id').val();
		charRenameBlockCounter = counter;
		clearAllMessagesPopUp();
		resetWarningDisplay();

		if (charRenameId === null || charRenameId === 'null' || charRenameId === '') {
			$("#" + counter + "_char_form").remove();
		} else {
			$("#delete_char_rename_param_link").click();
		}
	}
	
	function deleteCharRenameOperationParams(){
		

		clearAllMessagesPopUp();
		resetWarningDisplayPopUp();

		$
				.ajax({
					url : 'deletePluginCharRenameParams',
					cache : false,
					async : true,
					dataType : 'json',
					type : "POST",
					data : {
						"id" : charRenameId
					},

					success : function(data) {
						var response = data;
						var responseCode = response.code;
						var responseMsg = response.msg;
						var responseObject = response.object;
						if (responseCode === "200") {
							$("#" + charRenameBlockCounter + "_char_form").remove();
							resetWarningDisplay();
							clearAllMessages();
							showSuccessMsg(responseMsg);
							closeFancyBox();
							againFileRenamePopUp();
						} else if (responseObject !== undefined
								&& responseObject !== 'undefined'
								&& responseCode === "400") {
							showErrorMsg(responseMsg);
						} else {
							showErrorMsg(responseMsg);
						}
					},
					error : function(xhr, st, err) {
						handleGenericError(xhr, st, err);
					}
				});

	}
	
	function updateCharRenamOperationParams(counter) {
		$('#errorMessageCharacterRename').hide();
		resetWarningDisplay();
		clearAllMessages();
		
		$('#' + counter + '_char_buttons-div').hide();
		$('#' + counter + '_char_progress-bar-div').show();
		var fileNameLength = $('#' + counter + '_length').val();
		if (fileNameLength == '' || fileNameLength == null) {
			fileNameLength = -1;
			$('#' + counter + '_length').val("");
		}
		var startIndex = $('#' + counter + '_char_startIndex').val();
		if (startIndex == '' || startIndex == null || startIndex==-1) {
			startIndex = -1;
			$('#' + counter + '_char_startIndex').val("");
		} 

		var endIndex = $('#' + counter + '_char_endIndex').val();
		if (endIndex == '' || endIndex == null) {
			endIndex = -1;
			$('#' + counter + '_char_endIndex').val("");
		}
		
		var dateFormat = $('#' + counter + '_dateFormatCharRename').val();
		var srcDateFormat = '';
		if(dateFormat == 'Other'){
			srcDateFormat = $('#' + counter + '_srcDateFormat').val();
		}else{
			srcDateFormat = dateFormat;
		}

		$
				.ajax({
					url : 'updateCollectionCharRenameParams',
					cache : false,
					async : true,
					dataType : 'json',
					type : "POST",
					data : {
						"id" 			: $('#' + counter + '_char_rename_id').val(),
						"sequenceNo" 	: $('#' + counter + '_sequenceNo').val(),
						"query" 		: $('#' + counter + '_query').val(),
						"position" 		: $('#' + counter + '_positionCharForm').val(),
						"startIndex" 	: startIndex,
						"endIndex" 		: endIndex,
						"paddingType" 	: $('#' + counter + '_paddingType').val(),
						"defaultValue" 	: $('#' + counter + '_defaultValue').val(),
						"length" 		: fileNameLength,
						"pathList.id" 	: $('#'+pathListCounter+'_id').val(),
						"paddingValue" 	: $('#' + counter + '_paddingValue').val(),
						"dateFormat" 	: dateFormat,
						"srcDateFormat" : srcDateFormat,
						"dateType" 	: $('#' + counter + '_dateValue').val(),
						"blockCount" : counter

					},

					success : function(data) {
						$('#' + counter + '_char_buttons-div').show();
						$('#' + counter + '_char_progress-bar-div').hide();

						var response = data;
						var responseCode = response.code;
						var responseMsg = response.msg;
						var responseObject = response.object;

						if (responseCode === "200") {
							resetWarningDisplay();
							clearAllMessages();
							showSuccessMsg(responseMsg);
							
							$("#" + counter + "_char_updatebtn").show();
							$("#" + counter + "_char_savebtn").hide();
							$("#" + counter + "_char_operation_block").collapse(
									"toggle");
							$('#' + counter + '_sequenceNo').val(
									responseObject.sequenceNo);
							$('#' + counter + '_query').val(responseObject.query);
							$('#' + counter + '_positionCharForm').val(
									responseObject.position + "");
							$('#' + counter + '_defaultValue').val(
									responseObject.defaultValue);
							$('#' + counter + '_paddingType').val(
									responseObject.paddingType + "");
							var charStartIndex = responseObject.startIndex;
							if(charStartIndex>=0){
								charStartIndex++;
								$('#' + counter + '_char_startIndex').val(charStartIndex);
							} else {
								$('#' + counter + '_char_startIndex').val('');
							}
							
							if(responseObject.endIndex==-1){
								$('#' + counter + '_char_endIndex').val('');
							} else {
								$('#' + counter + '_char_endIndex').val(responseObject.endIndex);
							}
							if(responseObject.length==-1){
								$('#' + counter + '_length').val('');
							}else{
								$('#' + counter + '_length').val(responseObject.length);
							}
							
							$('#' + counter + '_dateFomatCharForm').val(
									responseObject.dateFormat);
							if(responseObject.dateFormat == 'Other'){
								$('#' + counter + '_srcDateFomat').val(responseObject.srcDateFormat);	
							}else{
								$('#' + counter + '_srcDateFomat').val('');	
							}
							
						} else if (responseObject !== undefined
								&& responseObject !== 'undefined'
								&& responseCode === "400") {
							$('#' + counter + '_char_buttons-div').show();
							$('#' + counter + '_char_progress-bar-div').hide();
							addErrorIconAndMsgForAjax(responseObject);
							$('#errorMessageCharacterRename').show();
						} else {
							$('#' + counter + '_char_buttons-div').show();
							$('#' + counter + '_char_progress-bar-div').hide();
							resetWarningDisplay();
							clearAllMessages();
							showErrorMsg(responseMsg);
						}
					},
					error : function(xhr, st, err) {
						$('#' + counter + '_char_buttons-div').show();
						$('#' + counter + '_char_progress-bar-div').hide();
						handleGenericError(xhr, st, err);
					}
				});
	}
	
	function resetCharRenameParameters(counter){
			$("#" + counter + "_query").val('');
			$("#" + counter + "_char_startIndex").val('');
			$("#" + counter + "_char_endIndex").val('');
			$("#" + counter + "_length").val('');
			$("#" + counter + "_sequenceNo").val('');
			$("#" + counter + "_defaultValue").val('');
			$("#" + counter + "_positionCharForm").val($("#" + counter + "_positionCharForm option:first").val());
			$("#" + counter + "_paddingType").val($("#" + counter + "_paddingType option:first").val());
			$('#' + counter + '_paddingValue').val('');
			$('#' + counter + '_dateFormatCharRename').val($("#" + counter + "_dateFormatCharRename option:first").val());
			$('#' + counter + '_srcDateFormat').val('');
			$('#' + counter + '_srcDateFormat').attr('readonly',true);
			$('#' + counter + '_dateValue').val($("#" + counter + "_dateValue option:first").val());
	}
	
	function resetWarningMsg() {
		$("#errorMessageCharacterRename").hide();
	}
	
</script>

	
