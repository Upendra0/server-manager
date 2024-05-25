<%@page import="com.elitecore.sm.common.model.StateEnum"%>
<%@page import="com.elitecore.sm.services.model.PartitionFieldEnum"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@page import="com.elitecore.sm.services.model.HashDataTypeEnum"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-multiselect.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-multiselect.css" type="text/css"/>

<style>
.checkbox{
	color: #000 !important;
}

.btn-default span{
width: 201px !important;
background:none !important ;
}



</style>

<script>

var selectedPatternIds = [];

var patternListCounter=-1;

var patternTokenObject;

var patternAttributeList = [];

var patternArr=[];

var configuredRegExId= [];

var isDeleteAttribute=false;

var isGenerateToken=false;

var isloadCompletecall=true;

var isRegexNameUpdate=false , isRegexPattenUpdate=false;



 function editColumnFormatter(cellvalue, options, rowObject){
	return "<a href='#' class='link' onclick=openEditPopup('"+rowObject["patternListCounter"]+"','"+rowObject["id"]+"','"+options.rowId+"'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
} 

function displayGridForPattern(counter,attributeList){
	
	$("#"+counter+"_grid").jqGrid({
		datatype: "local",
        colNames:[
				  "<spring:message code='regex.parser.attr.grid.pattern.id' ></spring:message>",
				  "<spring:message code='regex.parser.attr.grid.attribute.id' ></spring:message>",
				  "<spring:message code='regex.parser.attr.grid.sequence.no' ></spring:message>",
                  "<spring:message code='regex.parser.attr.grid.sample.data' ></spring:message>",
                  "<spring:message code='regex.parser.attr.grid.unified.field.name' ></spring:message>",
                  "<spring:message code='regex.parser.attr.grid.descripation' ></spring:message>",
                  "<spring:message code='regex.parser.attr.grid.regEx' ></spring:message>",
                  "<spring:message code='regex.parser.attr.grid.default.value' ></spring:message>",
                  "<spring:message code='regex.parser.attr.grid.trim.char' ></spring:message>",
                  "<spring:message code='regex.parser.attr.grid.edit' ></spring:message>",
                  "<spring:message code='regex.parser.attr.grid.edit' ></spring:message>",
                 ],
		colModel:[
			{name:'patternId',index:'patternId',sortable:false,hidden:true},
			{name:'id',index:'id',hidden:true},
			{name:'seqNumber',index:'seqNumber',sortable:false},
        	{name:'sampleData',index:'sampleData',sortable:false},
        	{name:'unifiedField',index:'unifiedField',sortable:false},
            {name:'description',index:'description',sortable:false},
            {name:'regex',index:'regex',sortable:false},
            {name:'defaultValue',index:'defaultValue',sortable:false},
            {name:'trimChars',index:'trimChars',sortable:false},
        	{name:'edit',index:'edit',sortable:false,formatter:editColumnFormatter},
        	{name:'patternListCounter',index:'patternListCounter',sortable:false,hidden:true}, 
        ],
        
        rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
        rowList:[10,20,60,100],
        height: 'auto',
        mtype:'POST',
		sortname: 'id',
 		sortorder: "desc",
        //pager: "#"+counter+"_gridPagingDiv",
        contentType: "application/json; charset=utf-8",
        viewrecords: true,
        multiselect: true,
        timeout : 120000,
        loadtext: "Loading...",
        caption: "<spring:message code='regex.parser.attr.grid.header'></spring:message>",
        beforeRequest:function(){
            $(".ui-dialog-titlebar").hide();
        }, 
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        loadComplete: function(data) {
			$(".ui-dialog-titlebar").show();
 			
			if(isloadCompletecall){
				if(attributeList!=null && attributeList != 'undefined' && attributeList != undefined){
					
					$.each(attributeList,function(index,attribute){
		 				
		 				var rowData = {};
		 				rowData.id							= parseInt(attribute.id);
		 				rowData.patternId					= parseInt(attribute.patternId);
		 				rowData.seqNumber					= attribute.seqNumber;
		 				rowData.sampleData					= attribute.sampleData;
		 				rowData.unifiedField				= attribute.unifiedField;
		 				rowData.description					= attribute.description;
		 				rowData.regex						= attribute.regex;
		 				rowData.defaultValue				= attribute.defaultValue;
		 				rowData.trimChars					= attribute.trimChars;
		 				rowData.edit						="<a href='#' class='link' onclick=openEditPopup('"+counter+"','"+rowData.id+"','"+rowData.id+"'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
		 				rowData.patternListCounter          = counter;
		 				
		 				jQuery("#"+counter+"_grid").jqGrid('addRowData',rowData.id,rowData);
		 			
		 			});
				}
			}
 			
		}, 
		
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		onSelectAll:function(id,status){


		},
		recordtext: "<spring:message code='regex.parser.attr.grid.pager.total.records.text'></spring:message>",
        emptyrecords: "<spring:message code='regex.parser.attr.grid.empty.records'></spring:message>",
		loadtext: "<spring:message code='regex.parser.attr.grid.loading.text'></spring:message>",
		pgtext : "<spring:message code='regex.parser.attr.grid.pager.text'></spring:message>",
	}).navGrid("#gridPagingDiv",{edit:true,add:false,del:false,search:false});
	
	$(".ui-jqgrid-titlebar").hide();
}

function addPatternList(patternId,patternName,patternRegExId,patternRegEx,attributeList,mode){
	patternListCounter++;
	
	var html = 	"<form method='POST' id='" + patternListCounter + "_form'> "+
				"<div class='box box-warning' id='flipbox_'"+patternListCounter+"'>  "+
				"<input type='hidden' id='"+patternListCounter+"_parserMapping_id' value='${mappingId}'> "+
				"<input type='hidden' id='"+patternListCounter+"_patternId' name='"+patternListCounter+"_patternId' value='"+patternId+"'> "+
				"<input type='hidden' id='"+patternListCounter+"_patternList' name='"+patternListCounter+"_patternList'> "+
				"<div class='box-header with-border'> "+
				"	<h3 class='box-title' id='title_"+patternListCounter+"'><spring:message code='regex.parser.regex.pattern.detail.section.header'></spring:message> "+ 											
				"	</h3>  "+
				"	<div class='box-tools pull-right' style='top: -3px;' id='action_"+patternListCounter+"'> "+ 
				"		<button class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' href='#"+patternListCounter+"_block'> "+ 
				"			<i class='fa fa-minus'></i> "+
				"		</button> "+
				"					<sec:authorize access='hasAuthority(\'DELETE_PARSER\')'>"+
		        "						&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;' onclick=openDeletePopup(\'"+patternListCounter+"\','patternDelete');></a>" +
		        "					</sec:authorize>"+
				"	</div> "+
				"</div> "+
				"<div class='box-body inline-form accordion-body collapsed in' id='"+patternListCounter+"_block'> "+ 
				"	<div class='fullwidth inline-form'>  "+
				"		<div class='col-md-6 no-padding'> "+
				"			<spring:message code='regex.parser.attr.grid.regEx.pattern.name' var='tooltip' ></spring:message> "+ 
				"			<div class='form-group'>  "+
				"				<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+ 
				"				<div class='input-group '> "+
				"					<input type='text' id='"+patternListCounter+"_patternRegExName' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+patternName+"'/> "+
				"					<span class='input-group-addon last' id='"+patternListCounter+"_patternRegExName_edit' style='visibility: visible !important;'><a href='#' onclick=updatePatternName(\'"+patternListCounter+"\'); ><i class='fa fa-pencil' aria-hidden='true' style='color: gray;'></i></a></span>"+ 
				"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
				"				</div> "+
				"			</div> "+
				"		</div> "+
				"		<div class='col-md-6 no-padding'> "+ 
				"			<spring:message code='regex.parser.attr.grid.regEx.pattern.id' var='tooltip' ></spring:message> "+ 
				"			<div class='form-group'>  "+
				"				<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+ 
				"				<div class='input-group '> "+

				"					<select id='"+patternListCounter+"_patternRegExId' class='form-control table-cell input-sm' multiple='multiple'  data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='"+patternRegExId+"'>"+
				
		        "					</select>" +
		        "					<span class='input-group-addon last' id='"+patternListCounter+"_patternRegExId_edit' style='visibility: visible !important;'><a href='#' onclick=updatePatternRegEXId(\'"+patternListCounter+"\'); ><i class='fa fa-pencil' aria-hidden='true' style='color: gray;'></i></a></span>"+
				"					<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title=''></i></span> "+
				"				</div> "+
				"			</div> "+
				"		</div> "+
				"		<div class='col-md-12 no-padding'> "+ 
				"			<spring:message code='regex.parser.attr.grid.regEx.pattern' var='tooltip' ></spring:message> "+ 
				"			<div class='form-group'>  "+
				"				<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div> "+ 
				"				<div class='input-group '> "+
				"					<input type='text' id='"+patternListCounter+"_patternRegEx' class='form-control table-cell input-sm'  data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+patternRegEx+"'/>"+
				"					<span class='input-group-addon last' id='"+patternListCounter+"_patternRegEx_edit' style='visibility: visible !important;'><a href='#' onclick=updatePatternRegEX(\'"+patternListCounter+"\'); ><i class='fa fa-pencil' aria-hidden='true' style='color: gray;'></i></a></span>"+
				"					<span class='input-group-addon add-on last' ><i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title=''></i></span> "+
				"				</div> "+
				"			</div> "+
				"		</div> "+
				"		<div class='col-md-12 no-padding'> "+ 
				"			<div class='title2'> "+
				"				<spring:message code='regex.parser.attr.grid.header' ></spring:message>"+ 
				"					<span class='title2rightfield'> "+
				"		          		<span class='title2rightfield-icon1-text'> "+
				"		          			<a href='#' id='generateToken' onclick=generateToken(\'"+patternListCounter+"\',\'"+mode+"\')><i class='fa fa-list'></i></a> "+
				"	  						<a href='#' id='generateToken' onclick=generateToken(\'"+patternListCounter+"\',\'"+mode+"\')> "+
				"	  							<spring:message code='regex.parser.grid.generate.token' ></spring:message>  "+
				"	  						</a> "+
				"							</span>"+
				"		          		<span class='title2rightfield-icon1-text'> "+
				"		          			<a href='#' id='deleteAttribute' onclick=openDeletePopup('"+patternListCounter+"','attributeDelete');> <i class='fa fa-trash'></i></a> "+
				"		          			<a href='#' id='deleteAttribute' onclick=openDeletePopup('"+patternListCounter+"','attributeDelete');><spring:message code='btn.label.delete' ></spring:message></a> "+
				"		          		</span> "+
				"		          	</span> "+
				"			</div> "+
				"		</div> "+
				"		<div class='col-md-12 no-padding'> "+ 
				"			<div class='box-body table-responsive no-padding box'> "+
				"            	<table class='table table-hover' id='"+patternListCounter+"_grid'></table> "+
				"               	<div id='"+patternListCounter+"_gridPagingDiv'></div> "+
				"	           	<div class='clearfix'></div> "+
				"	           	<div id='divLoading' align='center' style='display: none;'><img src='img/preloaders/Preloader_10.gif' /></div> "+  
				"           </div> "+
				"		</div> "+
				"		<div class='col-md-12 no-padding'> "+ 
				"			<div class='form-group'> "+
				"				<div id='"+patternListCounter+"_buttons-div' class='input-group '> "+ 
				"					<sec:authorize access='hasAuthority(\'CREATE_PARSER\')'> "+
				"						<button type='button' class='btn btn-grey btn-xs' id='"+patternListCounter+"_addbtn' onclick=addPatternListtoDB(\'"+patternListCounter+"\')><spring:message code='btn.label.add'></spring:message></button>&nbsp;"+ 
				"					</sec:authorize> "+
				"					<sec:authorize access='hasAuthority(\'UPDATE_PARSER\')'> "+
				"						<button type='button' class='btn btn-grey btn-xs' id='"+patternListCounter+"_updatebtn' onclick=updatePatternDetail(\'"+patternListCounter+"\')><spring:message code='btn.label.update'></spring:message></button>&nbsp;"+ 
				"					</sec:authorize> "+
				"					<button type='button' class='btn btn-grey btn-xs' id='"+patternListCounter+"_resetbtn' onclick=resetPatternList(\'"+patternListCounter+"\')><spring:message code='btn.label.reset'></spring:message></button>&nbsp;"+  
				"				</div> "+
				"				<div id='"+patternListCounter+"_progress-bar-div' class='input-group' style='display: none;'>"+  
				"					<label>Processing Request &nbsp;&nbsp; </label>  "+
				"					<img src='img/processing-bar.gif'> "+
				"				</div> "+
				"			</div>  "+
				"		</div>  "+
				"	</div> "+
				"</div> "+
				"</div> "+
				"</form> ";
				
	$('#patternList').append(html);
	var optionhtml='';
	var configuredPatternregExId=patternRegExId.split(",");
	var availableIds=$("#avilablelogPatternRegexId").val();
	var availableIdsArr=availableIds.split(",");
	
	for(var i=0;i<availableIdsArr.length;i++){
		var found = $.inArray(availableIdsArr[i], configuredPatternregExId);
		if(found >= 0){
			optionhtml = "<option value='"+availableIdsArr[i]+"' selected >"+availableIdsArr[i]+"</option>";
		}else{
			optionhtml = "<option value='"+availableIdsArr[i]+"'>"+availableIdsArr[i]+"</option>";
		}
		
		$('#'+patternListCounter+'_patternRegExId').append(optionhtml);
	}
	
	$('#'+patternListCounter+'_patternRegExId').multiselect({
    
    	maxHeight: '200',
    /*	buttonWidth: '235', */
    	
        buttonWidth: 'auto',
        
        nonSelectedText: 'None selected',
        nSelectedText: 'selected',
        numberDisplayed: 3,
      
        

   
    	/* onChange: function(element, checked) {
    		
        		var logpatternIdArr=[];
        		
        		if($("#logPatternRegexId").val() != ""){
        			logpatternIdArr=$("#logPatternRegexId").val().split(",");	
        		}
        		
        		var found=$.inArray(element.val(), logpatternIdArr);
        		if(found < 0){
        			
        			logpatternIdArr.push([element.val()]);
        			
        			
        		}else{
        			if(checked == false){
        				logpatternIdArr.splice(found, 1);
        			}
        		}
        	
        	$("#logPatternRegexId").val(logpatternIdArr);

    		} */
	}); 

	$('#'+patternListCounter+'_patternRegExId').addClass('form-control table-cell input-sm');
	var flag = '${readOnlyFlag}';
	
	if(mode == 'update'){
		$('#'+patternListCounter+'_updatebtn').show();
		$('#'+patternListCounter+'_addbtn').hide();
		$('#'+patternListCounter+"_patternRegExName").prop('disabled',true);
		$('#'+patternListCounter+"_patternRegExId").prop('disabled',true);
		$('#'+patternListCounter+"_patternRegExId").multiselect("disable");
		$('#'+patternListCounter+"_patternRegEx").prop('disabled',true);
		$('#title_'+patternListCounter).html(patternName);
		if(flag == 'false'){
			$("#"+patternListCounter+"_block").collapse("toggle");
		}
		$('#'+patternListCounter+'_updatebtn').prop('disabled',true);
	}else{
		
		$('#'+patternListCounter+'_patternRegExName_edit').hide();
		$('#'+patternListCounter+'_patternRegExId_edit').hide();
		$('#'+patternListCounter+'_patternRegEx_edit').hide();
		$('#'+patternListCounter+'_updatebtn').hide();
		$('#'+patternListCounter+'_addbtn').show();
	}
	
	
	displayGridForPattern(patternListCounter,attributeList);
}

</script>
<c:if test="${patternList != null}">
<script>
$(document).ready(function() {
	
		patternArr=eval('${patternList}');	
		
		$.each(patternArr, function(index,pattern){
			addPatternList(pattern.id,pattern.patternRegExName,pattern.patternRegExId,pattern.patternRegEx,pattern.attributeList,'update');
		});
		
		$("#patternListDiv").show();
	
});
</script>
</c:if>

<div class="tab-content no-padding clearfix">
	<div class="fullwidth">	
		<div class="title2" id="patternListDiv" style="display: none">
   					<spring:message code="regex.parser.pattern.list" ></spring:message>
	   				 <span class="title2rightfield">
				          
				          	<sec:authorize access="hasAuthority('CREATE_PARSER')">
				          	<span class="title2rightfield-icon1-text">
				          		<a href="#" id="addPattern" onclick="addPatternList('','','','','','add');"><i class="fa fa-plus-circle"></i></a>
	          					<a href="#" id="addPattern" onclick="addPatternList('','','','','','add');">
	          						<spring:message code="regex.parser.add.pattern" ></spring:message>
	          					</a>
		          			</span>	
				          	</sec:authorize>
				          	</span>
		</div>
		<div class="clearfix"></div>
	</div>
	<div class="fullwidth"> 
		<div id="patternList">
		</div>
	</div>
</div>
<div id="divPatternRegExSampleData" style=" width:100%; display: none;" >
	<jsp:include page="regExPatternSampleDataPopUp.jsp"></jsp:include>
</div>
<a href="#divPatternRegExSampleData" class="fancybox" style="display: none;" id="patternSampleData">#</a>

<a href="#divEditAttributePopup" class="fancybox" style="display: none;" id="editPopup">#</a>
<div id="divEditAttributePopup" style="width:100%; display: none;" >
   <jsp:include page="editRegExParserAttributePopUp.jsp"></jsp:include>
</div>


<a href="#divDeleteComfirmPopup" class="fancybox" style="display: none;" id="deletePopup">#</a>
<div id="divDeleteComfirmPopup" style="width:100%; display: none;" >
    <div class="modal-content">
    	<input type="hidden" id="deleteBlockId"/>
    	<input type="hidden" id="deletePatternId"/>
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="delete.warn.attribute.label"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
        <div id="deletePopUpMsg">
       		<p id="delete-regex-attribute-warning">
			       <spring:message code="delete.warn.attribute.msg"></spring:message>
		 	</p>
         
        	<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
        	</div>
        </div>
        <div id="delete_buttons-div" class="modal-footer padding10">
         	<button type="button" id="btndelete" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="deletePatternAttribute();"><spring:message code="btn.label.delete"></spring:message></button>
         	<button type="button" id="btndeletePattern" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="deletePattern();"><spring:message code="btn.label.delete"></spring:message></button>
         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
        </div>
        <div id="delete_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
			<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
		</div>	
        
    </div>
    <!-- /.modal-content --> 
</div>
<a href="#divMessage" class="fancybox" style="display: none;" id="message">#</a>
<div id="divMessage" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="serverManagement.warn.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
			        <p id="moreWarn">
			        	<spring:message code="serverMgmt.more.instance.checked.warning"></spring:message>    
			        </p>
			        <p id="lessWarn">
			        	<spring:message code="serverMgmt.no.instance.checked.warning"></spring:message>    
			        </p>
		        </div>
		        <div class="modal-footer padding10">
		            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		    </div>
		    <!-- /.modal-content --> 
		</div>

<script type="text/javascript">



function resetPatternList(counter){
	$('#'+ counter+ '_block input').val('');
}

function showProgressBar(patternListCounter){
	$('#' + patternListCounter + '_buttons-div').hide();
	$('#' + patternListCounter + '_progress-bar-div').show();
}

function hideProgressBar(patternListCounter){
	$('#' + patternListCounter + '_buttons-div').show();
	$('#' + patternListCounter + '_progress-bar-div').hide();
}

function generateToken(patternListCounter,mode){
	
	clearAllMessagesPopUp();
	resetWarningDisplay();
	
	
	var patterns = $('#'+patternListCounter+'_patternRegExId option:selected');
	var selectedPatternIds = [];
	$(patterns).each(function(index, brand){
		selectedPatternIds.push([$(this).val()]);
	});

	$.ajax({
		url: '<%=ControllerConstants.GENERATE_REGEX_PATTERN_ATTRIBUTE%>',
		cache: false,
		async: true, 
		dataType: 'json',
		type: "POST",
		data: 
		{
			"patternRegExName" 				:	$('#'+patternListCounter+'_patternRegExName').val(),
			"patternRegExId" 				:	selectedPatternIds.toString(),
			"patternRegEx" 					:	$('#'+patternListCounter+'_patternRegEx').val(),
			"parserMapping.id"				:	$('#'+patternListCounter+'_parserMapping_id').val(),
			"parserMapping.logPatternRegex" :   $('#logPatternRegex').val(),
			"patternListCounter" 			:   patternListCounter,
		},
		 
		success: function(data){
			
			 var response = eval(data);
			 
			 var responseCode = response.code; 
			 var responseMsg = response.msg; 
			 var responseObject = response.object;
			 
			 if(responseCode == "200"){
				 clearAllMessagesPopUp();
				 resetWarningDisplay();
				 var tableString  = "<tr>";
				 tableString     += "<th><spring:message code='regex.parser.attr.grid.sequence.no' ></spring:message></th>";
				 
			  	var maxLength=0;
			  	
				$.each(responseObject,function(index,value){
					
					var temp=value.length;
					
					if(temp>maxLength){
						maxLength=temp;
					}
					tableString     += "<th><input type='radio' name='selected-pattern-id' value="+index+" id="+index+"></input>&nbsp;"+index+"</th>";
				 });
				
				tableString  += "</tr>";
				
				for(var i=1; i<=maxLength ; i++){
					
					tableString     += "<tr>";
					tableString     += "<td id='"+i+"'>"+i+"</td>";
					$.each(responseObject,function(index,value){
						tableString     += "<td id='"+i+"_"+index+"'></td>";
						
					});
					tableString     += "</tr>"; 
				}
				
				$("#tblPatternAttributeList").html(tableString);
				
				var i=1;
				$.each(responseObject,function(index,value){
					
					$.each(value,function(data,object){
						
						$('#'+i+"_"+index).html(object["sampleData"]);
						i++;
					});
					i=1;
				});
				
				patternTokenObject=responseObject;
				$("#patternListCounterPopUp").val(patternListCounter);
				$("#mode").val(mode);
				 $("#add-token-select").show();
				$("#patternSampleData").click();
			 }else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				
				 clearAllMessages();
				 resetWarningDisplay();
				 $("#add-token-select").hide();
				 $("#patternSampleData").click();
				 showErrorMsgPopUp(responseMsg);
						
			}else{
				
					resetWarningDisplay();
					clearAllMessages();
					 $("#add-token-select").hide();
					 $("#patternSampleData").click();
					 showErrorMsgPopUp(responseMsg);
					
				}
			 
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});

}

function addTokenToGrid(){
	clearAllMessages();
	resetWarningDisplay();
	
		
	attributeList=null;
	var selectedPatternId=$("input:radio[name='selected-pattern-id']:checked").val();

	var blockid=$("#patternListCounterPopUp").val();
	
	$("#"+blockid+"_grid").jqGrid('clearGridData');
	isloadCompletecall=false;


	$.each(patternTokenObject,function(index,value){
		
		if(index == selectedPatternId){
			
			$.each(value,function(data,object){
				
				parent.jQuery("#"+blockid+"_grid").jqGrid('addRowData',data,object);
				
			});
		}
		
	});
		

	 parent.jQuery("#"+blockid+"_grid").setGridParam({ rowNum: 10 }).trigger("reloadGrid");
	 
	 patternTokenObject=null;
	 
	 parent.closeFancyBoxFromChildIFrame();	
	 
	 if($("#mode").val() == 'update'){
		
		 isGenerateToken=true;
	 }
	 
}


function addPatternListtoDB(patternListCounter){
	clearAllMessages();
	resetWarningDisplay();
	showProgressBar(patternListCounter);
	
	var rows = $("#"+patternListCounter+"_grid").jqGrid('getDataIDs');
	patternAttributeList=[];

    for (i = 0; i < rows.length; i++)
    {
        var rowData = $("#"+patternListCounter+"_grid").jqGrid('getRowData', rows[i]);
        var regExParserAttribute = {};
        
        	regExParserAttribute.seqNumber=rowData['seqNumber'];
        	regExParserAttribute.sampleData=rowData['sampleData'];
        	regExParserAttribute.unifiedField=rowData['unifiedField'];
        	regExParserAttribute.description=rowData['description'];
        	regExParserAttribute.regex=rowData['regex'];
        	regExParserAttribute.defaultValue=rowData['defaultValue'];
        	regExParserAttribute.trimChars=rowData['trimChars'];
			
        	patternAttributeList.push(regExParserAttribute);
    }
    
    $('#'+patternListCounter+'_patternList').val(JSON.stringify(patternAttributeList));
    
    var patterns = $('#'+patternListCounter+'_patternRegExId option:selected');
	var selectedPatternIds = [];
	$(patterns).each(function(index, brand){
		selectedPatternIds.push([$(this).val()]);
	});
	
	
    var formData = $('#'+ patternListCounter + '_form').serialize();
    
    $.ajax({
		url: '<%=ControllerConstants.ADD_REGEX_PATTERN_AND_ATTRIBUTE%>',
		cache: false,
		async: true, 
		dataType: 'json',
		type: "POST",
		data: {
			
				"patternRegExName"        					  :	   $('#'+patternListCounter+'_patternRegExName').val(),
				"patternRegExId"          					  :	   selectedPatternIds.toString(),
				"patternRegEx"            					  :    $('#'+patternListCounter+'_patternRegEx').val(),
				"parserMapping.id" 		  					  :    $('#'+patternListCounter+'_parserMapping_id').val(),
				"parserMapping.logPatternRegexId"   		  :    $("#logPatternRegexId").val(),
				"parserMapping.avilablelogPatternRegexId"     :    $("#avilablelogPatternRegexId").val(),
				"patternAttributeList"    					  :    JSON.stringify(patternAttributeList),
				"patternListCounter"						  :	   patternListCounter,
		},
		
		 
		success: function(data){
			
			 var response = eval(data);
			 
			 var responseCode = response.code; 
			 var responseMsg = response.msg; 
			 var responseObject = response.object;
			 hideProgressBar(patternListCounter);
			 
			 if(responseCode == "200"){
				 resetWarningDisplay();
				 clearAllMessages();
				 
				 var respObj = eval(responseObject);
				 
				 $("#logPatternRegexId").val(respObj["logRegExPattern"]);
				 $("#"+patternListCounter+"_patternId").val(respObj["patternId"]);
				 $("#"+patternListCounter+"_parserMapping_id").val(respObj["parserMappingId"]);
				 $("#"+patternListCounter+"_grid").jqGrid('clearGridData');
				 
				var attributeList=respObj.attributeList;
				 
				 if(attributeList!=null && attributeList != 'undefined' && attributeList != undefined){
						
						$.each(attributeList,function(index,attribute){
			 				
			 				var rowData = {};
			 				rowData.id							= parseInt(attribute.id);
			 				rowData.patternId					= parseInt(attribute.patternId);
			 				rowData.seqNumber					= attribute.seqNumber;
			 				rowData.sampleData					= attribute.sampleData;
			 				rowData.unifiedField				= attribute.unifiedField;
			 				rowData.description					= attribute.description;
			 				rowData.regex						= attribute.regex;
			 				rowData.defaultValue				= attribute.defaultValue;
			 				rowData.trimChars					= attribute.trimChars;
			 				rowData.edit						="<a href='#' class='link' onclick=openEditPopup('"+patternListCounter+"','"+rowData.id+",'"+rowData.id+"); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
			 				rowData.patternListCounter          = patternListCounter;
			 				
			 				jQuery("#"+patternListCounter+"_grid").jqGrid('addRowData',rowData.id,rowData);
			 			
			 			});
					}
				 
				 	$('#'+patternListCounter+'_updatebtn').show();
				 	$('#'+patternListCounter+'_updatebtn').prop('disabled',true);
					$('#'+patternListCounter+'_addbtn').hide();
					$('#'+patternListCounter+"_patternRegExName").prop('disabled',true);
					$('#'+patternListCounter+"_patternRegExId").attr('disabled',true);
				//	$('#'+patternListCounter+"_patternRegExId").multiselect("disabled");
					$('#'+patternListCounter+"_patternRegEx").prop('disabled',true);
					$('#'+patternListCounter+'_patternRegExName_edit').show();
					$('#'+patternListCounter+'_patternRegExId_edit').show();
					$('#'+patternListCounter+'_patternRegEx_edit').show();
					$('#title_'+patternListCounter).html($('#'+patternListCounter+'_patternRegExName').val());
				 
				 showSuccessMsg(responseMsg);
				$("#"+patternListCounter+"_block").collapse("toggle");
				 
				 
			 }else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				
				 clearAllMessages();
				 resetWarningDisplay();
				 
				 $.each(responseObject, function(key,val){
						if (key.indexOf(".") >= 0)
							key = key.replace('.','_');
						
						if(key.indexOf("_patternRegExId")>=0){
							$("#" + key).next().next().next().children().first().attr("data-original-title",val);	
						}else{
							$("#" + key).next().next().children().first().attr("data-original-title",val);
						}
						

						addErrorClassWhenTitleExist($("#GENERIC_ERROR_MESSAGE", window.parent.document).val());
					});

						
				}else{
				
					resetWarningDisplay();
					clearAllMessages();
					showErrorMsg(responseMsg);
				}
			
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});

    
}


//open delete popup
function  openDeletePopup(counter,mode){
	
	clearAllMessagesPopUp();
	resetWarningDisplay();
	
	console.log("openDeletePopUp");
	$('#deleteBlockId').val(counter);
	//$('#delete_buttons-div #btndelete').attr('onClick','deleteParser("'+counter+'")');
	if(mode == 'patternDelete'){
		$("#btndeletePattern").show();
		$("#btndelete").hide();
		var patternId=$("#"+counter+"_patternId").val();
		console.log("patternId::"+patternId);
		if(patternId == null || patternId == '' || patternId == undefined || patternId == 'undefined'){
			
			$("#"+counter+"_form").remove();
		}else{
			$("#deletePatternId").val(patternId);
			$('#deletePopup').click();
		}
	}else{
		var myGrid = $("#"+counter+"_grid");
	    var selectedRowId = myGrid.jqGrid ('getGridParam', 'selarrrow');
	   
	    if(selectedRowId  == null || selectedRowId == ''){
	    	
			$("#lessWarn").show();
			$("#moreWarn").hide();
			$("#message").click();
			return;
		}else{
			$("#btndeletePattern").hide();
			$("#btndelete").show();
			$('#deletePopup').click();
		}
		
	}
	
	
}

//Delete Pattern Attribute and remove from grid
function deletePatternAttribute(){
	clearAllMessagesPopUp();
	resetWarningDisplay();

	showProgressBar('<%= BaseConstants.DELETE_MODE %>'); 
		var deleteblockId=$("#deleteBlockId").val();
		var myGrid = $("#"+deleteblockId+"_grid");
	    var selectedRowId = myGrid.jqGrid ('getGridParam', 'selarrrow');
	    var rowString = selectedRowId.join(",");
	    var rowIds = new Array();
	    rowIds=rowString.split(",");
	    var patternId;
	    
	    $.each(rowIds,function (index,rowId){
	    	patternId= myGrid.jqGrid('getCell', rowId, 'patternId');
	    	
	    	if(patternId == null || patternId == ''){
	    		$("#"+deleteblockId+"_grid").jqGrid('delRowData',rowId);
	    	}
	    	
	    });
	    
	    if(patternId != null && patternId!=''){
	    	
	    	 $.ajax({
	 			url: '<%=ControllerConstants.DELETE_REGEX_PATTERN_ATTRIBUTE%>',
	 			cache: false,
	 			async: true,
	 			dataType: 'json',
	 			type: "POST",
	 			data:
	 			{
	 				"patternAttrIdList" :  rowString
	 			}, 
	 			
	 			success: function(data){

	 				var response = eval(data);
	 				
	 				var responseCode = response.code; 
	 				var responseMsg = response.msg; 
	 				hideProgressBar('<%= BaseConstants.DELETE_MODE %>');
	 				
	 				if(responseCode == "200"){
	 					resetWarningDisplay();
	 					clearAllMessagesPopUp();
	 					showSuccessMsg(responseMsg);
	 					
	 					console.log("selectedRowId " +rowIds);
	 					
	 					for(var i=0;i<rowIds.length;i++){
	 						var rowid=rowIds[i];
	 						
	 						$("#"+deleteblockId+"_grid").jqGrid('delRowData',rowid);
	 						
	 					}
	 					
	 	 				closeFancyBox();
	 				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
	 					
	 					resetWarningDisplay();
	 					clearAllMessagesPopUp();
	 					addErrorIconAndMsgForAjaxPopUp(responseObject); 
	 				}else{
	 					
	 					resetWarningDisplay();
	 					clearAllMessagesPopUp();
	 					showErrorMsgPopUp(responseMsg);
	 				}
	 			},
	 		    error: function (xhr,st,err){
	 		    	
	 				handleGenericError(xhr,st,err);
	 			}
	 		});
	    }else{
	    	closeFancyBox();
	    }
	    
	    
}


function updatePatternDetail(patternListCounter){
	
	clearAllMessagesPopUp();
	resetWarningDisplay();
	
	if((isRegexPattenUpdate == true && isGenerateToken == true)){
		
		updatePatternToDB(patternListCounter);
		
	}else if(isRegexPattenUpdate == true && isGenerateToken == false){
		
		showErrorMsg("<spring:message code='regex.pattern.generate.token.message' ></spring:message>");
		
	}else if(isRegexNameUpdate){
		
		updatePatternToDB(patternListCounter);
	}    

}

function updatePatternName(patternListCounter){
	clearAllMessages();
	resetWarningDisplay();
	
	$('#'+patternListCounter+"_patternRegExName").prop('disabled',false);
	$('#'+patternListCounter+'_updatebtn').prop('disabled',false);
	
	isRegexNameUpdate=true;
	
}
function updatePatternRegEXId(patternListCounter){
	clearAllMessages();
	resetWarningDisplay();
	$('#'+patternListCounter+"_patternRegExId").prop('disabled',false);
	$('#'+patternListCounter+"_patternRegExId").multiselect("enable");
	$('#'+patternListCounter+'_updatebtn').prop('disabled',false);
	
	isGenerateToken=false;
	isRegexPattenUpdate=true;
	
	
}
function updatePatternRegEX(patternListCounter){
	clearAllMessages();
	resetWarningDisplay();
	$('#'+patternListCounter+"_patternRegEx").prop('disabled',false);
	$('#'+patternListCounter+'_updatebtn').prop('disabled',false);
	
	isGenerateToken=false;
	isRegexPattenUpdate=true;
	
}

function updatePatternToDB(patternListCounter){

	clearAllMessages();
	resetWarningDisplay();
	showProgressBar(patternListCounter); 
	var patterns = $('#'+patternListCounter+'_patternRegExId option:selected');
	var selectedPatternIds = [];
	$(patterns).each(function(index, brand){
		selectedPatternIds.push([$(this).val()]);
	});
	
	var rows = $("#"+patternListCounter+"_grid").jqGrid('getDataIDs');

    for (i = 0; i < rows.length; i++)
    {
        var rowData = $("#"+patternListCounter+"_grid").jqGrid('getRowData', rows[i]);
        var regExParserAttribute = {};
        
        	regExParserAttribute.seqNumber=rowData['seqNumber'];
        	regExParserAttribute.sampleData=rowData['sampleData'];
        	regExParserAttribute.unifiedField=rowData['unifiedField'];
        	regExParserAttribute.description=rowData['description'];
        	regExParserAttribute.regex=rowData['regex'];
        	regExParserAttribute.defaultValue=rowData['defaultValue'];
        	regExParserAttribute.trimChars=rowData['trimChars'];
			
        	patternAttributeList.push(regExParserAttribute);
    }
    
    $('#'+patternListCounter+'_patternList').val(JSON.stringify(patternAttributeList));

	    
    	 $.ajax({
 			url: '<%=ControllerConstants.UPDATE_REGEX_PATTERN_DETAIL%>',
 			cache: false,
 			async: true,
 			dataType: 'json',
 			type: "POST",
 			data:
 			{
 				"id"										  :	   $('#'+patternListCounter+'_patternId').val(),	
 				"patternRegExName"        					  :	   $('#'+patternListCounter+'_patternRegExName').val(),
				"patternRegExId"          					  :	   selectedPatternIds.toString(),
				"patternRegEx"            					  :    $('#'+patternListCounter+'_patternRegEx').val(),
				"parserMapping.id" 		  					  :    $('#'+patternListCounter+'_parserMapping_id').val(),
				"parserMapping.logPatternRegexId"   		  :    $("#logPatternRegexId").val(),
				"patternAttributeList"    					  :    JSON.stringify(patternAttributeList),
				"isDeleteAttribute"							  :    isRegexPattenUpdate,	
				"patternListCounter"						  :	   patternListCounter,
 			}, 
 			
 			success: function(data){

 				var response = eval(data);
 				
 				var responseCode = response.code; 
 				var responseMsg = response.msg; 
 				var responseObject=response.object;
 				hideProgressBar(patternListCounter);
 				
 				if(responseCode == "200"){
 					resetWarningDisplay();
 					clearAllMessages();
 					showSuccessMsg(responseMsg);
 					
 					$('#'+patternListCounter+"_patternRegExName").prop('disabled',true);
 					$('#'+patternListCounter+"_patternRegExId").attr('disabled',true);
 					//$('#'+patternListCounter+"_patternRegExId").multiselect("disabled");
 					$('#'+patternListCounter+"_patternRegEx").prop('disabled',true);
 					$("#logPatternRegexId").val(responseObject);
 					$('#title_'+patternListCounter).html($('#'+patternListCounter+'_patternRegExName').val());
 					$("#"+patternListCounter+"_block").collapse("toggle");
 					
 				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
 					
 					clearAllMessages();
					resetWarningDisplay();
 					 $.each(responseObject, function(key,val){
 						if (key.indexOf(".") >= 0)
 							key = key.replace('.','_');
 						if(key.indexOf("_patternRegExId")>=0){
							$("#" + key).next().next().next().children().first().attr("data-original-title",val);	
						}else{
							$("#" + key).next().next().children().first().attr("data-original-title",val);
						}
 						

 						addErrorClassWhenTitleExist($("#GENERIC_ERROR_MESSAGE", window.parent.document).val());
 					});

 				}else{
 					
 					resetWarningDisplay();
 					clearAllMessages();
 					showErrorMsg(responseMsg);
 				}
 			},
 		    error: function (xhr,st,err){
 		    	hideProgressBar(patternListCounter);
 				handleGenericError(xhr,st,err);
 			}
 		});

}

function deletePattern(){
	clearAllMessages();
	resetWarningDisplay();
	var patternId=$("#deletePatternId").val();
	var deleteblockId=$("#deleteBlockId").val();
	
	
	if(patternId != null && patternId!=''){
		$.ajax({
 			url: '<%=ControllerConstants.DELETE_REGEX_PATTERN%>',
 			cache: false,
 			async: true,
 			dataType: 'json',
 			type: "POST",
 			data:
 			{
 				"patternId" :  patternId
 			}, 
 			
 			success: function(data){

 				var response = eval(data);
 				
 				var responseCode = response.code; 
 				var responseMsg = response.msg; 
 				var responseObject=response.object;
 				
 				if(responseCode == "200"){
 					resetWarningDisplay();
 					clearAllMessages();
 					showSuccessMsg(responseMsg);
 					$("#"+deleteblockId+"_form").remove();
 					$("#logPatternRegexId").val(responseObject);
 	 				closeFancyBox();
 				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
 					<%-- hideProgressBar('<%= BaseConstants.DELETE_MODE %>') --%>
 					clearAllMessages();
					resetWarningDisplay();
 					addErrorIconAndMsgForAjaxPopUp(responseObject); 
 				}else{
 					<%-- hideProgressBar('<%= BaseConstants.DELETE_MODE %>'); --%>
 					resetWarningDisplay();
 					clearAllMessages();
 					showErrorMsgPopUp(responseMsg);
 				}
 			},
 		    error: function (xhr,st,err){
 		    	<%-- hideProgressBar('<%= BaseConstants.DELETE_MODE %>'); --%>
 				handleGenericError(xhr,st,err);
 			}
 		});

	}
}
</script>
    		




