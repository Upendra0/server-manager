<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.model.RollingTypeEnum"%>
<%@page import="com.elitecore.sm.common.model.StateEnum"%>
<%@page import="com.elitecore.sm.common.model.DistributionDriverTypeEnum"%>
<%@page import="com.elitecore.sm.common.model.TrueFalseEnum"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
	
<script>
$(document).ready(function() {
	peerlist = eval('${peerList}');
	if(peerlist != 'undefined' && peerlist != null  ){
		$.each(peerlist, function(index,peer){
			console.log(peer);
			// console.log(peer.name);
			
			addNewPeerDetail(peer.id,peer.status,peer.name,peer.identity,peer.realmName,peer.watchDogInterval,
					peer.requestTimeOut,peer.fileNameFormat,peer.outFileLocation,peer.fileSeqEnable,peer.appendpaddinginfileseq,
					peer.minFileSeq,peer.maxFileSeq,peer.logRollingUnitTime,peer.logRollingUnitVol,
					peer.inputCompressed,peer.avpWrapper,'UPDATE');
		});	
	}
	
});

	var peerListCounter=-1;
	
	function addNewPeerDetail(id,peerStatus,name,identity,realmName,watchDogInterval,
								reqTimeOut,fileNameFormation,fileLocation,enableFileSeq,appendpaddinginfileseq,
								minRange,maxRange,rollingTime,rollingUnit,fileCompression,diameterAVPs,actionType){
		peerListCounter++;
		var block = "<form method='POST' id='" + peerListCounter + "_form'> "+
		"<input type='hidden' id='" + peerListCounter + "_serviceId' name='serviceId' value='${serviceId}'> "+
		"<input type='hidden' id='"+peerListCounter+"_id' name='"+peerListCounter+"_id' value='"+id+"'> "+
		"<input type='hidden' id='"+peerListCounter+"_hdnPeerStatus' name='"+peerListCounter+"_hdnPeerStatus' value='"+peerStatus+"'> "+
		"<div class='box box-warning' id='flipbox_"+peerListCounter+"'>"+
	    "	<div class='box-header with-border'> "+
	    "		<h3 class='box-title' id='title_"+peerListCounter+ "'>"+
	    "			<a href='#' class='title2rightfield-icon1-text' id='link_"+peerListCounter+ "' style='font-size: 12px;text-decoration:none'> " + 
	    "				<spring:message code='diameter.collection.service.peer.tab.header'></spring:message>"+ 
	    "			</a>" + 
	    "		</h3>"+
	    "		<div class='box-tools pull-right' id='action_"+peerListCounter+ "'>"+
	    "			<button class='btn btn-box-tool block-collapse-btn' id='editPeer_"+peerListCounter+ "' data-toggle='collapse' data-parent='#driverList' href='#" + peerListCounter + "_block'><i class='fa fa-minus'></i></button>	"+
	    "					<sec:authorize access='hasAuthority(\'DELETE_COLLECTION_CLIENT\')'>"+
	    "			&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;' id='deletePeerPopup_"+peerListCounter+ "' onclick='deletePeerPopup("+' "'+peerListCounter+'" ' + ")'></a>" +
	    "					</sec:authorize>"+
	    "		</div>"+
	    "	</div>	"+
	    "	<div class='box-body inline-form accordion-body collapse in' id='" + peerListCounter + "_block'>"+
	    "		<div class='col-md-6 no-padding'>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.enable.peer' var='label'></spring:message>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.enable.peer.tooltip' var='tooltip'></spring:message>"+
	    "  			<div class='form-group'>"+
	    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
	    "      			<div class='input-group '>"+
	    "					<select class='form-control table-cell input-sm' id='" + peerListCounter + "_status' " +  
		"							tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' >"+
	    "				    	<c:forEach var='stateEnum' items='${stateEnum}' >"+
		"							<option value='${stateEnum}'>${stateEnum}</option>"+
		"				    	</c:forEach>"+
		"					</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    "       		</div>"+
	    "  	 		</div>"+
	    "		</div>"+
	 	"		<div class='col-md-6 no-padding'>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.name' var='label'></spring:message>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.name.tooltip' var='tooltip'></spring:message>"+
	    "  			<div class='form-group'>"+
	    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
	    "      			<div class='input-group '>"+
	    "					<input class='form-control table-cell input-sm' id='" + peerListCounter + "_name' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' tabindex='4' value='"+name+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
	    "       		</div>"+
	    "  	 		</div>"+
	    "		</div>"+
	    "		<div class='col-md-6 no-padding'>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.identity' var='label'></spring:message>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.identity.tooltip' var='tooltip'></spring:message>"+
	    "  			<div class='form-group'>"+
	    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
	    "      			<div class='input-group '>"+
	    "					<input class='form-control table-cell input-sm' id='" + peerListCounter + "_identity' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' tabindex='4' value='"+identity+"'/>"+
	    "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    "        		</div>"+
	    "     		</div>"+
	    "		</div> " +
	    "		<div class='col-md-6 no-padding'>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.realm.name' var='label'></spring:message>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.realm.name.tooltip' var='tooltip'></spring:message>"+
	    "  			<div class='form-group'>"+
	    "    			<div class='table-cell-label'>${label}</div>"+
	    "      			<div class='input-group '>"+
	    "					<input class='form-control table-cell input-sm' id='" + peerListCounter + "_realmName' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' tabindex='4' value='"+realmName+"'/>"+
	    "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    "        		</div>"+
	    "     		</div>"+
	    "		</div> " +
	    "		<div class='col-md-6 no-padding'>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.watchdog.interval' var='label'></spring:message>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.watchdog.interval.tooltip' var='tooltip'></spring:message>"+
	    "  			<div class='form-group'>"+
	    "    			<div class='table-cell-label'>${label}</div>"+
	    "      			<div class='input-group '>"+
	    "					<input class='form-control table-cell input-sm' id='" + peerListCounter + "_watchDogInterval' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' tabindex='4' value='"+watchDogInterval+"' onkeydown='isNumericOnKeyDown(event)' />"+
	    "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    "        		</div>"+
	    "     		</div>"+
	    "		</div> " +
	    "		<div class='col-md-6 no-padding'>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.request.timeout' var='label'></spring:message>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.request.timeout.tooltip' var='tooltip'></spring:message>"+
	    "  			<div class='form-group'>"+
	    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
	    "      			<div class='input-group '>"+
	    "					<input class='form-control table-cell input-sm' id='" + peerListCounter + "_requestTimeOut' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' tabindex='4' value='"+reqTimeOut+"' onkeydown='isNumericOnKeyDown(event)' />"+
	    "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    "        		</div>"+
	    "     		</div>"+
	    "		</div> " +
	    "		<div class='col-md-6 no-padding'>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.file.name.formation' var='label'></spring:message>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.file.name.formation.tooltip' var='tooltip'></spring:message>"+
	    "  			<div class='form-group'>"+
	    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
	    "      			<div class='input-group '>"+
	    "					<input class='form-control table-cell input-sm' id='" + peerListCounter + "_fileNameFormat' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' tabindex='4' value='"+fileNameFormation+"'/>"+
	    "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    "        		</div>"+
	    "     		</div>"+
	    "		</div> " +
	    "		<div class='col-md-6 no-padding'>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.file.location' var='label'></spring:message>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.file.location.tooltip' var='tooltip'></spring:message>"+
	    "  			<div class='form-group'>"+
	    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
	    "      			<div class='input-group '>"+
	    "					<input class='form-control table-cell input-sm' id='" + peerListCounter + "_outFileLocation' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' tabindex='4' value='"+fileLocation+"'/>"+
	    "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    "        		</div>"+
	    "     		</div>"+
	    "		</div> " +
	    "		<div class='col-md-6 no-padding'>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.enable.file.sequance' var='label'></spring:message>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.enable.file.sequance.tooltip' var='tooltip'></spring:message>"+
	    "  			<div class='form-group'>"+
	    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
	    "      			<div class='input-group '>"+
	    "					<select class='form-control table-cell input-sm' id='" + peerListCounter + "_fileSeqEnable' " +  
		"							tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' onchange='changeSequenceRangeStatus("+ peerListCounter +")' >"+
	    "				    	<c:forEach var='truefalseEnum' items='${truefalseEnum}' >"+
		"							<option value='${truefalseEnum.name}'>${truefalseEnum}</option>"+
		"				    	</c:forEach>"+
		"					</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    "       		</div>"+
	    "  	 		</div>"+
	    "		</div>"+
	    "		<div class='col-md-6 no-padding'>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.file.padding.enable' var='label'></spring:message>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.file.padding.enable.tooltip' var='tooltip'></spring:message>"+
	    "  			<div class='form-group'>"+
	    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
	    "      			<div class='input-group '>"+
	    "					<select class='form-control table-cell input-sm' id='" + peerListCounter + "_appendFilePaddingInFileName' " +  
		"							tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' >"+
	    "				    	<c:forEach var='truefalseEnum' items='${truefalseEnum}' >"+
		"							<option value='${truefalseEnum.name}'>${truefalseEnum}</option>"+
		"				    	</c:forEach>"+
		"					</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    "       		</div>"+
	    "  	 		</div>"+
	    "		</div>"+
	    "		<div class='col-md-6 no-padding' style='display:inline-block;'>"+
        "			<spring:message code='diameter.collection.service.peer.tab.peer.file.sequance.range' var='seqRange'></spring:message>"+
        "			<spring:message code='diameter.collection.service.peer.tab.peer.min.file.seq.range' var='minRange'></spring:message>"+
        "			<spring:message code='diameter.collection.service.peer.tab.peer.max.file.seq.range' var='maxRange'></spring:message>"+
        "  			<div class='form-group'>"+
        "   			<div class='table-cell-label'>${seqRange}</div>"+
        "      			<div class='input-group'>"+
        "      				<input class='form-control table-cell input-sm' id='" + peerListCounter + "_minFileSeq' data-toggle='tooltip' data-placement='bottom'  title='${minRange}' onkeydown='isNumericOnKeyDown(event)' value='"+minRange+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
		"					<input class='form-control table-cell input-sm' id='" + peerListCounter + "_maxFileSeq' data-toggle='tooltip' data-placement='bottom'  title='${maxRange}' onkeydown='isNumericOnKeyDown(event)' value='"+maxRange+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "     		</div>"+
        "		</div>"+
	    "		<div class='col-md-6 no-padding'>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.file.rolling.time' var='label'></spring:message>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.file.rolling.time.tooltip' var='tooltip'></spring:message>"+
	    "  			<div class='form-group'>"+
	    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
	    "      			<div class='input-group '>"+
	    "					<input class='form-control table-cell input-sm' id='" + peerListCounter + "_logRollingUnitTime' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' tabindex='4' value='"+rollingTime+"' onkeydown='isNumericOnKeyDown(event)' />"+
	    "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    "        		</div>"+
	    "     		</div>"+
	    "		</div> " +
	    "		<div class='col-md-6 no-padding'>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.file.rolling.unit' var='label'></spring:message>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.file.rolling.unit.tooltip' var='tooltip'></spring:message>"+
	    "  			<div class='form-group'>"+
	    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
	    "      			<div class='input-group '>"+
	    "					<input class='form-control table-cell input-sm' id='" + peerListCounter + "_logRollingUnitVol' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' tabindex='4' value='"+rollingUnit+"' onkeydown='isNumericOnKeyDown(event)' />"+
	    "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    "        		</div>"+
	    "     		</div>"+
	    "		</div> " +
	    "		<div class='col-md-6 no-padding'>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.file.compression' var='label'></spring:message>"+
	    "			<spring:message code='diameter.collection.service.peer.tab.peer.file.compression.tooltip' var='tooltip'></spring:message>"+
	    "  			<div class='form-group'>"+
	    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
	    "      			<div class='input-group '>"+
	    "					<select class='form-control table-cell input-sm' id='" + peerListCounter + "_inputCompressed' " +  
		"							tabindex='4' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' >"+
	    "				    	<c:forEach var='truefalseEnum' items='${truefalseEnum}' >"+
		"							<option value='${truefalseEnum.name}'>${truefalseEnum}</option>"+
		"				    	</c:forEach>"+
		"					</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    "       		</div>"+
	    "  	 		</div>"+
	    "		</div>"+
	    	    
		"		<div class='col-md-12 no-padding'>"+
	    "  			<div class='form-group'>"+
	    "      			<div id='" + peerListCounter + "_buttons-div' class='input-group '>"+
		"					<button type='button' class='btn btn-grey btn-xs ' id='" + peerListCounter + "_savebtn' onclick=createPeer(\'"+peerListCounter+"\');><spring:message code='btn.label.save'></spring:message></button>&nbsp;" +
		"					<button type='button' class='btn btn-grey btn-xs ' id='" + peerListCounter + "_updatebtn' onclick=updatePeer(\'"+peerListCounter+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
        "					<button type='button' class='btn btn-grey btn-xs ' onclick=resetPeerDetailDiv(\'"+peerListCounter+"\');><spring:message code='btn.label.reset'></spring:message></button>&nbsp;" +
	    "       		</div>"+
        "				<div id='" + peerListCounter + "_progress-bar-div' class='input-group' style='display: none;'> "+
        "					<label>Processing Request &nbsp;&nbsp; </label> "+
        "						<img src='img/processing-bar.gif'>"+
	    "				</div> "+
	    "   		</div>" +
	    "		</div>" +
	    //Additional Avp Grid Start
		"		<div class='col-md-12 no-padding gridBlock' > "+ 
		"			<div class='title2'> "+
		"				<spring:message code='diameter.peer.avp.grid.header' ></spring:message>"+ 
		"					<span class='title2rightfield'> "+
		"		          		<span class='title2rightfield-icon1-text'> "+
		"		          			<a href='#' name='" + peerListCounter +  "_addlinkicon' onclick=addAdditionalAvp('"+peerListCounter+"');><i class='fa fa-plus-circle'></i></a> "+
		"	  						<a href='#' name='" + peerListCounter +  "_addlink' id='addAVP' onclick=addAdditionalAvp('"+peerListCounter+"');> "+
		"	  							<spring:message code='btn.label.add' ></spring:message>  "+
		"	  						</a> "+
		"	      				</span>	  "+
		"		          		<span class='title2rightfield-icon1-text'> "+
		"		          			<a href='#' onclick=openDeletePopup('"+peerListCounter+"');> <i class='fa fa-trash'></i></a> "+
		"		          			<a href='#' id='deleteService' onclick=openDeletePopup('"+peerListCounter+"');><spring:message code='btn.label.delete' ></spring:message></a> "+
		"		          		</span> "+
		"		          	</span> "+
		"			</div> "+
		"		</div> "+
		
		"		<div class='col-md-12 no-padding' id='"+peerListCounter+"_pagingDiv' > "+ 
		"			<div class='box-body table-responsive no-padding box'> "+
		"            	<table class='table table-hover' id='"+peerListCounter+"_grid'></table> "+
		"               	<div id='"+peerListCounter+"_gridPagingDiv' style='display:none'></div> "+
		"	           	<div class='clearfix'></div> "+
		"	           	<div id='divLoading' align='center' style='display: none;'><img src='img/preloaders/Preloader_10.gif' /></div> "+  
		"           </div> "+
		"		</div> "+
		
		//Additional Avp Grid End
	    //Main Div and Form
	 	"	</div>"+
		"</div>"+
		"</form>";

		$('#peerList').prepend(block);
		$('#'+ peerListCounter + '_savebtn').hide();
		$('#'+ peerListCounter + '_updatebtn').hide();
		$('#'+ peerListCounter + '_status option[value="DELETED"]').remove();
		$('#'+ peerListCounter + '_status').val(peerStatus);
		$('#'+ peerListCounter + '_inputCompressed').val(fileCompression.toString());
		$('#'+ peerListCounter + '_fileSeqEnable').val(enableFileSeq.toString());
		$('#'+ peerListCounter + '_appendFilePaddingInFileName').val(appendpaddinginfileseq.toString());
		if (actionType == 'ADD')
		{
			$('#'+ peerListCounter + '_savebtn').show();
			$("#link_"+peerListCounter).html("<spring:message code='diameter.collection.service.peer.tab.header' ></spring:message>");
			$('#'+peerListCounter+'_block .gridBlock').hide();
			$('#'+peerListCounter+'_pagingDiv').hide();
			
		}
		else
		{
			$('#'+ peerListCounter + '_updatebtn').show();	
			$("#link_"+peerListCounter).html(name);
			displayGridForAvplist(peerListCounter,diameterAVPs);
			$("#"+peerListCounter+"_block").collapse("toggle");
		}
		changeSequenceRangeStatus(peerListCounter);		
	}


</script>
<div class="tab-pane" id="diameter-peer-config">
	<div class="tab-content no-padding">
		<div class="fullwidth mtop10">
			<div class="title2">
				<spring:message code="diameter.peer.list.caption" ></spring:message>
				<span class="title2rightfield"> <sec:authorize
						access="hasAuthority('CREATE_COLLECTION_CLIENT')">
						<span class="title2rightfield-icon1-text"> 
							<a href="#" onclick="addNewPeerDetail(0,'ACTIVE','','','','6','3000','DP{yyyyMMddHHmmssSSS}.log','','false','false','-1','-1','2','1024','false','','ADD');">
								<i class="fa fa-plus-circle"></i>
							</a>
							<a href="#" id="addPeer" onclick="addNewPeerDetail(0,'ACTIVE','','','','6','3000','DP{yyyyMMddHHmmssSSS}.log','','false','false','-1','-1','2','1024','false','','ADD');"> 
								<spring:message code="btn.label.add" ></spring:message>
							</a>
						</span>
					</sec:authorize>
				</span>
			</div>
			<div class="clearfix"></div>
		</div>

		<!-- Peer List -->

		<div class="fullwidth">
			<div id="peerList"></div>
		</div>
	</div>
</div>


<!-- Peer Delete popup code start here -->
<div id="divMessage" style="width: 100%; display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title" id="warning-title">
				<spring:message code="collection.peer.delete.header.label" ></spring:message>
			</h4>
			<h4 class="modal-title" id="status-title" style="display: none;">
				<spring:message code="serverManagement.warn.popup.header" ></spring:message>
			</h4>
		</div>

		<div class="modal-body padding10 inline-form">
			<span id="deletePeerResponseMsg" style="display: none;"> 
				<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
			</span>
			<p id="warningMessage">
				<spring:message code="peer.enable.delete.warning.message" ></spring:message>
			</p>
			<p id="deleteWarningMessage">
				<spring:message code="peer.delete.warning.message" ></spring:message>
			</p>
		</div>

		<div class="modal-footer padding10" id="active-peer-div">
			<button type="button" class="btn btn-grey btn-xs "
				data-dismiss="modal" onclick="closeFancyBox();">
				<spring:message code="btn.label.close" ></spring:message>
			</button>
		</div>

		<sec:authorize access="hasAuthority('DELETE_COLLECTION_CLIENT')">
			<div id="inactive-peer-div" class="modal-footer padding10">
				<button type="button" class="btn btn-grey btn-xs " id="deletePeer"
					onclick="deletePeer();">
					<spring:message code="btn.label.yes" ></spring:message>
				</button>
				<button type="button" class="btn btn-grey btn-xs "
					data-dismiss="modal" onclick="closeFancyBox();">
					<spring:message code="btn.label.no" ></spring:message>
				</button>
			</div>
		</sec:authorize>
	</div>
	
</div>
<a href="#divMessage" class="fancybox" style="display: none;"
	id="peerMessage">#</a>
<!-- Peer Delete popup code end here -->

<!-- AVP add Popup code Start here -->
<a href="#divAddAVPPopup" class="fancybox" style="display: none;" id="addMore">#</a>
<div id="divAddAVPPopup" style="width:100%; display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
            <h4 class="modal-title"><spring:message	code="diameter.peer.avp.add.popup.header" ></spring:message></h4>
        </div>
		<div class="modal-body padding10 inline-form">
			<div class="fullwidth no-padding tab-content">
				<div class="modal-body padding10 inline-form">	
					<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
				  	<input type="hidden" id="addBlockId"/>
				  	<input type="hidden" id="avpAction" />
				  	<input type="hidden" id="addAVPId" value = "0" />
				   	<!-- <input type="hidden" id="create_parserMappingId"/> -->
					
					<div class="col-md-6 no-padding">
		           		<spring:message code="diameter.peer.avp.add.popup.vendor.id" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
		         			<div class="input-group">
		         				<input tabindex="4" id="vendorId" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" onkeydown='isNumericOnKeyDown(event)'/>
				             	<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
				            </div>
		            	</div>
		            </div>
		            
		            <div class="col-md-6 no-padding">
		           		<spring:message code="diameter.peer.avp.add.popup.attribute.id" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
			             	<div class="input-group">
			             		<input tabindex="4" id="attributeId" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" onkeydown='isNumericOnKeyDown(event)' />
			             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
			             	</div>
		            	</div>
		            </div>
		            
		            <div class="col-md-6 no-padding">
		           		<spring:message code="diameter.peer.avp.add.popup.value" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
		             		<div class="input-group">
		             			<input tabindex="4" id="attributeValue" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
		             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		            
				</div>
		        <div id="create_buttons-div" class="modal-footer padding10">
		         	<button id="addAVPBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="addUpdateAVPDetail();"><spring:message code="btn.label.submit"></spring:message></button>
		         	<button id="closeAVPBtn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="resetcreateAVPPopUp();closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		        <div id="create_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
				</div>											        
			</div>														
		</div>
        <div id="add_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
			<label>Processing Request &nbsp;&nbsp; </label>
			<img src="img/processing-bar.gif" class="mCS_img_loaded">
		</div>
		
	</div>
</div>		
<!-- AVP add Popup code End here -->

<!-- AVP Delete Popup code Start here -->
<a href="#divDeleteComfirmPopup" class="fancybox" style="display: none;" id="deletePopup">#</a>
<div id="divDeleteComfirmPopup" style="width:100%; display: none;" >
    <div class="modal-content">
    	<input type="hidden" id="deleteBlockId"/>
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="device.delete.popup.warning.heading.label"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
        	<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
        </div>
        
		<div class="modal-body padding10 inline-form">
			<span id="warning_msg" style="display: none;"><spring:message code="warning.avp.message"></spring:message></span>			
			<span id="delete_avp_note" style="display: none;"><spring:message code="delete.avp.warn.msg"></spring:message></span>
		</div>
        
        <div id="delete_buttons-div" class="modal-footer padding10">
         	<button type="button" id="btndelete" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="openDeletePopup();" style="display: none;"><spring:message code="btn.label.delete"></spring:message></button>
         	<button type="button" class="btn btn-grey btn-xs " id="delete_close_btn" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
        </div>
        <div id="delete_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
			<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
		</div>	
    </div>
</div>
<!-- AVP Delete Popup code End here -->

<script>
function changeSequenceRangeStatus(counter){
	var seqEnable = $('#'+counter+'_fileSeqEnable').val();
	if(seqEnable=='true'){
		$('#'+counter+'_appendFilePaddingInFileName').attr('disabled',false);
		$('#'+counter+'_minFileSeq').attr('readonly',false);
		$('#'+counter+'_maxFileSeq').attr('readonly',false);
	} else {
		$('#'+counter+'_appendFilePaddingInFileName').attr('disabled',true);
		$('#'+counter+'_minFileSeq').attr('readonly',true);
		$('#'+counter+'_minFileSeq').val('-1');
		$('#'+counter+'_maxFileSeq').attr('readonly',true);
		$('#'+counter+'_maxFileSeq').val('-1');
	}
}

function resetPeerDetailDiv(divCounter){
	$('#'+divCounter + '_block input').val('');
	$('#'+divCounter + '_status').val($('#'+divCounter + '_status option:first').val());
	$('#'+divCounter + '_fileSeqEnable').val($('#'+divCounter + '_fileSeqEnable option:first').val());
	$('#'+divCounter + '_inputCompressed').val($('#'+divCounter + '_inputCompressed option:first').val());
	changeSequenceRangeStatus(divCounter);
}

function createPeer(counter){
	createUpdatePeer(counter, 'ADD', '<%=ControllerConstants.CREATE_DIAMETER_COLLECTION_PEER%>');
}

function updatePeer(counter){
	createUpdatePeer(counter, 'UPDATE', '<%=ControllerConstants.UPDATE_DIAMETER_COLLECTION_PEER%>');
}

function createUpdatePeer(counter, action, actionURL) {
	resetWarningDisplay();
	clearAllMessages();
	showProgressBar(counter);
	
	var minFileRange=$('#' + counter + '_minFileSeq').val();
	var maxFileRange=$('#' + counter + '_maxFileSeq').val();
	if(minFileRange==''){
		minFileRange='-1';
	}
	if(maxFileRange==''){
		maxFileRange='-1';
	}
	
	var watchDogInterval=$('#' + counter + '_watchDogInterval').val();
	if(watchDogInterval==''){
		$('#' + counter + '_watchDogInterval').val('-1');
	}
	$.ajax({
		url: actionURL,
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"id"							:	$('#' + counter + '_id').val(),
			"status"						:	$('#' + counter + '_status').val(),
			"name" 							:	$('#' + counter + '_name').val(),
			"identity" 						:	$('#' + counter + '_identity').val(),
			"realmName" 					:	$('#' + counter + '_realmName').val(),
			"watchDogInterval" 				:	$('#' + counter + '_watchDogInterval').val(),
			"requestTimeOut" 				:	$('#' + counter + '_requestTimeOut').val(),
			"fileNameFormat" 				:	$('#' + counter + '_fileNameFormat').val(),
			"outFileLocation" 				:	$('#' + counter + '_outFileLocation').val(),
			"fileSeqEnable" 				:	$('#' + counter + '_fileSeqEnable').val(),
			"appendFilePaddingInFileName" 	:	$('#' + counter + '_appendFilePaddingInFileName').val(),
			"minFileSeq"					:	minFileRange,
			"maxFileSeq"					:	maxFileRange,
			"logRollingUnitVol" 			:	$('#' + counter + '_logRollingUnitVol').val(),
			"logRollingUnitTime" 			:	$('#' + counter + '_logRollingUnitTime').val(),
			"inputCompressed" 				:	$('#' + counter + '_inputCompressed').val(),
			"service.id"					:   $('#' + counter + '_serviceId').val(),
			"peerCount"						: 	counter,
			"serviceType"					:	'${serviceType}'
		}, 
		
		success: function(data){
			hideProgressBar(counter);

			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode == "200"){
				
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);

				$("#link_"+counter).text(responseObject["name"]);
				$("#"+counter+"_updatebtn").show();
				$("#"+counter+"_savebtn").hide();
				$('#'+counter+'_id').val(responseObject["id"]);
				$("#"+counter+"_hdnPeerStatus").val(responseObject["status"]);
				$("#"+counter+"_block").collapse("toggle");
				if (action == "ADD")
				{
					displayGridForAvplist(counter,[]);
					$('#'+counter+'_block .gridBlock').show();
					$('#'+peerListCounter+'_pagingDiv').show();
				}
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				hideProgressBar(counter);
				addErrorIconAndMsgForAjax(responseObject); 
			}else{
				hideProgressBar(counter);
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(counter);
			handleGenericError(xhr,st,err);
		}
	});
	
}
var blockCounter;
var deletePeerId;

function deletePeerPopup(counter){
	clearAllMessages();
	$("#deletePeerResponseMsg").hide();
	blockCounter = counter;

	deletePeerId= $('#'+counter+'_id').val();
	var peerStatus = $('#'+counter+'_hdnPeerStatus').val();
	
	if(deletePeerId == null || deletePeerId == 'null' || deletePeerId == '' || deletePeerId == '0'){
		$("#"+counter+"_form").remove();
	}else{
		if(peerStatus == "INACTIVE"){
			$("#deleteWarningMessage").show();
			$("#inactive-peer-div").show();
			$("#warningMessage").hide();
			$("#active-peer-div").hide();
			$("#peerMessage").click();
		}else{
			$("#deleteWarningMessage").hide();
			$("#warningMessage").show();
			$("#inactive-peer-div").hide();
			$("#active-peer-div").show();
			$("#peerMessage").click();	
		}
	}
}

function deletePeer(){
	resetWarningDisplay();
	clearAllMessages();
	 $.ajax({
		url: '<%=ControllerConstants.DELETE_DIAMETER_COLLECTION_PEER%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"peerId"   : deletePeerId,
		}, 
		
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			$("#deleteWarningMessage").hide();
			$("#inactive-peer-div").hide();
			$("#warningMessage").hide();
			$("#active-peer-div").hide();
		
			$("#warning-title").hide();
			$("#status-title").show();
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				$("#deletePeerResponseMsg").show(); // div handles response message of delete operation
				$('#active-peer-div').show(); // To show close button for popup after successful delete
				showSuccessMsg(responseMsg);
				closeFancyBox();
				$('#'+blockCounter+'_form').remove();
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				$("#deletePeerResponseMsg").show();// div handles response message of delete operation
				$('#active-peer-div').show(); // To show close button for popup after successful delete
				showErrorMsgPopUp(responseMsg);
				parent.resizeFancyBox();
			}else{
				resetWarningDisplay();
				clearAllMessages();
				$("#deletePeerResponseMsg").show();// div handles response message of delete operation
				$('#active-peer-div').show();
				showErrorMsgPopUp(responseMsg);
				parent.resizeFancyBox();
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	}); 
}

$(window).keydown(function(event){
    if(event.keyCode == 13) {
      return false;
    }
  });
  
//open add avp popup
function addAdditionalAvp(blockId){
	$('#addBlockId').val(blockId);
	clearAllMessagesPopUp();
	resetWarningDisplay();
	
	$('#create_buttons-div').show();
	$('#create_progress-bar-div').hide();
	/* $('#create_name').val(''); */
	$("#addAVPId").val("0");
	$("#avpAction").val("AVPADD");
	$('#addMore').click();	
}

function resetcreateAVPPopUp(){
	$('#vendorId').val('');
	$('#attributeId').val('');
	$('#attributeValue').val('');
	clearResponseMsgDiv();
}

function addUpdateAVPDetail()
{
	resetWarningDisplay();
	clearAllMessages();
	var actionURL;
	var counter = $('#addBlockId').val();
	var peerId = $('#'+counter+'_id').val();
	var avpId = $('#addAVPId').val();
	var avpAction = $("#avpAction").val();
	if (avpAction == "AVPADD")
		actionURL = '<%=ControllerConstants.CREATE_AVP%>';
	else
		actionURL = '<%=ControllerConstants.UPDATE_AVP%>';
	
	$.ajax({
		url: actionURL,
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"id"								:	avpId,
			"peer.id"							:	peerId,
			"vendorId"							:   $('#vendorId').val(),
			"attributeId" 						:	$('#attributeId').val() ,
			"value"      						:	$('#attributeValue').val()
		}, 
		
		success: function(data){
			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			/* alert(responseCode + " -- " + responseMsg + " -- " + responseObject ); */
			
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
				
				var avp = eval(responseObject);
				
 				var rowData = {};
 				rowData.id						= avp.id;
 				//rowData.checkbox				= '<input type="checkbox"></input>';
 				rowData.vendorId				= avp.vendorId;
 				rowData.attributeId				= avp.attributeId;
 				rowData.attributeValue			= avp.value;
 				rowData.edit					='<a onclick="openEditPopup('+counter+','+rowData.id+')" id="editAVP_'+rowData.id+'"> <i class="fa fa-pencil-square-o" aria-hidden="true" id="img_'+rowData.id+'" width="10px" data-toggle="tooltip" data-placement="bottom" style="cursor:pointer;"></i> </a>';
 				
 				if (avpAction == "AVPUPDATE")
 					$("#"+counter+"_grid").jqGrid('setRowData', rowData.id, rowData);
 				else
 					jQuery("#"+counter+"_grid").jqGrid('addRowData',rowData.id,rowData);	
 				
 				$('#vendorId').val('');
 				$('#attributeId').val('');
 				$('#attributeValue').val('');
				closeFancyBox();
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				addErrorIconAndMsgForAjaxPopUp(responseObject); 
			}else{
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}


function displayGridForAvplist(counter,diameterAVPs){
	$.each(diameterAVPs,function(index,avp){
		avp.edit	='<a onclick="openEditPopup('+counter+','+parseInt(avp.id)+')" id="editAVP_'+avp.id+'"> <i class="fa fa-pencil-square-o" aria-hidden="true" id="img_'+parseInt(avp.id)+'" data-toggle="tooltip" data-placement="bottom" style="cursor:pointer;" ></i> </a>';
	});
	
	var uniqueGridId = counter+"_grid";
	var selectAllCheckboxId = counter+"_selectAllAvp";
	var childCheckboxName = counter+"_AvpCheckbox";
	$("#"+uniqueGridId).jqGrid({
    	url: "",
        datatype: "local",
        colNames:[
                  "<input type='checkbox' id='"+selectAllCheckboxId+"' onclick='avpHeaderCheckbox(event, this, \""+childCheckboxName+"\")'></input>",
				  "<spring:message code='diameter.peer.avp.grid.column.id' ></spring:message>",
                  "<spring:message code='diameter.peer.avp.grid.column.vendor.id' ></spring:message>",
                  "<spring:message code='diameter.peer.avp.grid.column.attribute.id' ></spring:message>",
                  "<spring:message code='diameter.peer.avp.grid.column.attribute.value' ></spring:message>",
                  "<spring:message code='diameter.peer.avp.grid.column.edit' ></spring:message>"
                 ],
		colModel:[
			{name:'checkbox',index:'checkbox',sortable:false,align:'center', width : '10%', formatter: function(cellvalue, options, rowObject) {
				return avpCheckboxFormatter(cellvalue, options, rowObject, uniqueGridId, selectAllCheckboxId, childCheckboxName);	
			}},
			{name:'id',index:'id',sortable:true,hidden:true},
        	{name:'vendorId',index:'vendorId',sortable:true,align:'center',width : '20%'},
        	{name:'attributeId',index:'attributeId',sortable:true,align:'center',width : '20%'},
        	{name:'attributeValue',index:'attributeValue',sortable:true,width : '30%'},
        	{name:'edit',index:'edit',sortable:false,align:'center',width : '20%'}
        ],
        data:diameterAVPs,
        rowNum: <%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
        rowList:[10,20,60,100],
        height: 'auto',
        mtype:'POST',
		sortname: 'id',
 		sortorder: "desc",
        pager: "#"+counter+"_gridPagingDiv",
        contentType: "application/json; charset=utf-8",
        viewrecords: true,
        //multiselect: true,
        timeout : 120000,
        loadtext: "Loading...",
        caption: "<spring:message code='diameter.peer.avp.grid.header'></spring:message>",
        beforeRequest:function(){
            $(".ui-dialog-titlebar").hide();
        }, 
        beforeSend: function(xhr) {
        	xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
 		 loadComplete: function(data) {
			$(".ui-dialog-titlebar").show();
		}, 
		onPaging: function (pgButton) {
			
		},
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		onSelectAll:function(id,status){
		},
		recordtext: "<spring:message code='diameter.peer.avp.grid.pager.total.records.text'></spring:message>",
        emptyrecords: "<spring:message code='diameter.peer.avp.grid.empty.records'></spring:message>",
		loadtext: "<spring:message code='diameter.peer.avp.grid.loading.text'></spring:message>",
		pgtext : "<spring:message code='diameter.peer.avp.grid.pager.text'></spring:message>",
	}).navGrid("#gridPagingDiv",{edit:true,add:false,del:false,search:false});
	
	$(".ui-jqgrid-titlebar").hide();
}

function avpCheckboxFormatter(cellvalue, options, rowObject, uniqueGridId, rootCheckboxId, childCheckboxName) {
	var uniqueId = "checkbox";
	if(rowObject["name"] !== "") {
		uniqueId += "_" + rowObject["name"]; 
	}
	return '<input type="checkbox" id="'+uniqueId+'" name="'+childCheckboxName+'" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \''+uniqueGridId+'\', \''+rootCheckboxId+'\', \''+childCheckboxName+'\')"/>';
}

/*
 * this function will handle select all/ de select all event
 */
function avpHeaderCheckbox(e, element, childCheckboxName) {
    e = e || event;/* get IE event ( not passed ) */
    e.stopPropagation ? e.stopPropagation() : e.cancelBubble = false;

    //if root checkbox is checked :-> all child checkbox should ne checked
    if(element.checked) {
    	$('input:checkbox[name="'+childCheckboxName+'"]').prop('checked',true);
    } else {
    	//if root checkbox is unchecked :-> all child checkbox should be unchecked
    	$('input:checkbox[name="'+childCheckboxName+'"]').prop('checked',false);
    }
}

/*
 * this function will handle root checkbox from child checkbox
 */
function updateRootCheckbox(element, gridId, rootCheckboxId, childCheckboxName) {
	if(!element.checked){
		// if current child checkbox is not checked : uncheck root checkbox
		$('input:checkbox[id="'+rootCheckboxId+'"]').prop('checked',false);
	} else {
		//if current child checkboox is checked and all checkbox are checked then check root checkbox
		var count = jQuery("#"+gridId).jqGrid('getGridParam', 'records');
		if ($('input:checkbox[name="'+childCheckboxName+'"]:checked').length === count) {
			$('input:checkbox[id="'+rootCheckboxId+'"]').prop('checked',true);
	    }
	}
}

//open edit AVP popup
function  openEditPopup(counter,rowId){
	var dataFromTheRow = jQuery("#"+counter+"_grid").jqGrid ('getRowData', rowId);

	$('#addBlockId').val(counter);
	$("#addAVPId").val(dataFromTheRow.id);
	$('#vendorId').val(dataFromTheRow.vendorId);
	$('#attributeId').val(dataFromTheRow.attributeId);
	$('#attributeValue').val(dataFromTheRow.attributeValue);
	$("#avpAction").val("AVPUPDATE");

	clearAllMessagesPopUp();
	resetWarningDisplay();
	$('#addMore').click();
}


//open delete popup
function openDeletePopup(counter){
	clearAllMessagesPopUp();
	resetWarningDisplay();
	
	var childCheckboxName = counter+"_AvpCheckbox";
	var selectedAVPCheckboxes = [];
    $.each($("input[name='"+childCheckboxName+"']:checked"), function(){            
    	selectedAVPCheckboxes.push($(this).val());
    });
    var selectedRowId = "";
    if(selectedAVPCheckboxes.length > 0) {
    	selectedRowId = selectedAVPCheckboxes[0];
    }
	
	$("#warning_msg").hide();
	$("#delete_avp_note").hide();
	$("#delete_progress-bar-div").hide();
	$("#btndelete").hide();
	$("#delete_close_btn").show();
	$("#delete_buttons-div").show();
	
	if(selectedRowId != null && selectedRowId != ''){
		$("#delete_avp_note").show();
		$("#btndelete").show();
		$("#delete_close_btn").show();		
		$('#delete_buttons-div #btndelete').attr('onClick','deleteAVP("'+counter+'")');
		$('#deleteBlockId').val(counter);	
	}else{
		$("#warning_msg").show();	
		$("#delete_close_btn").show();		
	}
	$('#deletePopup').click();
}


//delete all AVP selected in grid
function deleteAVP(blockId){	
    
    var childCheckboxName = blockId+"_AvpCheckbox";
	var selectedAVPCheckboxes = [];
    $.each($("input[name='"+childCheckboxName+"']:checked"), function(){            
    	selectedAVPCheckboxes.push($(this).val());
    });
    var rowString = selectedAVPCheckboxes.join(",");
    var rowIds = new Array();
    rowIds = rowString.split(",");
   
    $.ajax({
		url: '<%=ControllerConstants.DELETE_AVP%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		{
			"avpIdList" 					:	rowString
		}, 
		
		success: function(data){
			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode == "200"){
				var selectAllCheckboxId = blockId+"_selectAllAvp";
				$('input:checkbox[id="'+selectAllCheckboxId+'"]').prop('checked',false);
				
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
				
				for(var i=0;i<rowIds.length;i++){
					var rowid=rowIds[i];
					$("#"+blockId+"_grid").jqGrid('delRowData',rowid);
				}
				
				closeFancyBox();
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				addErrorIconAndMsgForAjaxPopUp(responseObject); 
			}else{
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}
</script>

