<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>

<script>
	var rowcounter = 0;
</script>

<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'CONSOLIDATION_SERVICE_MAPPING')}"><c:out value="active"></c:out></c:if>" id="serviceMap">
     <div class="tab-content no-padding  mtop10">
     	<div class="fullwidth">
   				<div class="title2">
   					<spring:message code="consolidation.service.serv.mapp.grid.caption" ></spring:message>
	   				 <span class="title2rightfield">
	   				 	<span class="title2rightfield-icon1-text">
<!-- 				          	<sec:authorize access="hasAuthority('DELETE_SERVER_AND_SERVER_INSTANCES')"> -->
				          		<a href="#" onclick="addMapPopup();"><i class="fa fa-plus-circle"></i></a>
				          		<a href="#" id="addMapping" onclick="addMapPopup();"><spring:message code="btn.label.add" ></spring:message></a>
<!-- 				          	</sec:authorize> -->
				          </span>
				          <span class="title2rightfield-icon1-text">
<!-- 				          	<sec:authorize access="hasAuthority('DELETE_SERVER_AND_SERVER_INSTANCES')"> -->
				          		<a href="#" onclick="alert('delete mapping');"><i class="fa fa-trash"></i></a>
				          		<a href="#" id="deleteMapping" onclick="alert('delete mapping');"><spring:message code="btn.label.delete" ></spring:message></a>
<!-- 				          	</sec:authorize> -->
				          </span>
			          </span>
		          </div>
   			</div>
           	<!-- Morris chart - Sales -->
            <div class="box-body table-responsive no-padding box">
             	<table class="table table-hover" id="grid"></table>
               	<div id="gridPagingDiv"></div> 
	           	<div class="clearfix"></div> 
	           	<div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>  
            </div>
    </div>
</div>

<a href="#divAddMapping" class="fancybox" style="display: none;" id="addMapping-a">#</a>
<div id="divAddMapping" style=" width:100%; display: none;" >
  <div class="modal-content">
        <div class="modal-header padding10" >
            <h4 class="modal-title"><spring:message code="consolidation.service.serv.mapp.app.popup.header"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form" style="background: none;border: none !important;">
	         	<div class="col-md-12 no-padding">
	        		<div class="form-group">
	            		<spring:message code="consolidation.service.serv.mapp.app.popup.ser.type" var="tooltip"></spring:message>
	         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
	             		<div class="input-group ">
	             			<select id="sel-service-type" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onchange="addendServiceName();">
	             			</select>
	             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
	             		</div>
	             	</div>
	         	</div>
	         	<div class="col-md-12 no-padding">
	        		<div class="form-group">
	            		<spring:message code="consolidation.service.serv.mapp.app.popup.ser.nm" var="tooltip"></spring:message>
	         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
	             		<div class="input-group ">
	             			<select id="sel-service-name" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }">
	             			</select>
	             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
	             		</div>
	             	</div>
	         	</div>
	         	<div class="col-md-12 no-padding">
	        		<div class="form-group">
	            		<spring:message code="consolidation.service.serv.mapp.app.popup.conso.stats.alias" var="tooltip"></spring:message>
	         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
	             		<div class="input-group ">
	             			<input type="text" id="stats-alias" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" >
	             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
	             		</div>
	             	</div>
	         	</div>
	         	<div class="col-md-12 no-padding">
	        		<div class="form-group">
	            		<spring:message code="consolidation.service.serv.mapp.app.popup.conso.stats.stage" var="tooltip"></spring:message>
	         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
	             		<div class="input-group ">
	             			<input type="radio" name="stats-stage" value="enable"/>Enable
	             			<input type="radio" name="stats-stage" value="disable"/>Disable
	             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
	             		</div>
	             	</div>
	         	</div>
        </div>
        <div id="add-map-buttons" class="modal-footer padding10">
        	<div class="col-md-12 no-padding">
        		<div class="form-group">
             		<div class="input-group ">
						<button type="button" id="btnSaveAttr" class="btn btn-grey btn-xs " onclick="addMapDetail();closeFancyBox();"><spring:message code="btn.label.save"></spring:message></button>&nbsp;&nbsp;
		    			<button type="button" id="btnUpdtAttr" style="display: none;" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="updtAttr('0');closeFancyBox();"><spring:message code="btn.label.update"></spring:message></button>
		    			<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
		    		</div>
		    	</div>
		    </div>
		</div>
		<div id="service-mapp-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
			<jsp:include page="../common/processing-bar.jsp"></jsp:include>
		</div>
    </div>
    <!-- /.modal-content --> 
</div>

<script>
	
	$(document).ready(function() {
		$("#grid").jqGrid({
        	url: "",
            datatype: "local",
            colNames:[
                      "<spring:message code='consolidation.service.serv.mapp.grid.column.ser.id' ></spring:message>",
                      "<spring:message code='consolidation.service.serv.mapp.grid.column.ser.type' ></spring:message>",
                      "<spring:message code='consolidation.service.serv.mapp.grid.column.ser.nm' ></spring:message>",
                      "<spring:message code='consolidation.service.serv.mapp.grid.column.con.stats.alias' ></spring:message>",
                      "<spring:message code='consolidation.service.serv.mapp.grid.column.enable' ></spring:message>",
                      "<spring:message code='consolidation.service.serv.mapp.grid.column.edit' ></spring:message>"
                     ],
			colModel:[
            	{name:'id',index:'id',sortable:false,hidden: true},
            	{name:'serviceType',index:'serviceType',sortable:false},
                {name:'serviceName',index:'serviceName',sortable:false,formatter:nameColumnFormatter},
            	{name:'statsAlias',index:'statsAlias',sortable:false},
            	{name:'statsStage',index:'statsStage',sortable:false, align:'center',formatter:enableColumnFormatter},
            	{name:'',index:'',sortable:false, align:'center',formatter:editFormatter}
            ],
            rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
            rowList:[10,20,60,100],
            height: 'auto',
            //width: "1000px",
			sortname: 'id',
     		sortorder: "desc",
            pager: "#gridPagingDiv",
            viewrecords: true,
            multiselect: true,
            timeout : 120000,
            loadtext: "Loading...",
            caption: "<spring:message code="consolidation.service.serv.mapp.grid.caption"></spring:message>",
            beforeRequest:function(){
                $(".ui-dialog-titlebar").hide();
            },
     		loadComplete: function(data) {
     			$(".ui-dialog-titlebar").show();
			},
			onPaging: function (pgButton) {
				clearResponseMsgDiv();
			},
			loadError : function(xhr,st,err) {
				handleGenericError(xhr,st,err);
			},
			beforeSelectRow: function (rowid, e){
				
			},
			recordtext: "<spring:message code="consolidation.service.serv.mapp.grid.pager.total.records.text"></spring:message>",
	        emptyrecords: "<spring:message code="consolidation.service.serv.mapp.grid.empty.records"></spring:message>",
			loadtext: "<spring:message code="consolidation.service.serv.mapp.grid.loading.text"></spring:message>",
			pgtext : "<spring:message code="consolidation.service.serv.mapp.grid.pager.text"></spring:message>"
		}).navGrid("#gridPagingDiv",{edit:false,add:false,del:false,search:false});
		
		$(".ui-jqgrid-titlebar").hide();
		
		var serviceTypeList = JSON.parse('${serviceType}');
		if(Object.keys(serviceTypeList).length>0){
			$.each(serviceTypeList,function (key,val){
				$('#sel-service-type').append($("<option></option>")
				         .attr("value",key)
				         .text(val));
			});
		}
	});
	
	function enableColumnFormatter(cellvalue, options, rowObject){
		if(cellvalue=='enable')
			return '<input type="checkbox" style="cursor: pointer;" checked/>';
		else
			return '<input type="checkbox" style="cursor: pointer;"/>';	
	}
	
	function nameColumnFormatter(cellvalue, options, rowObject){
		return '<a href="#" class="link" onclick="alert(\'go to service\')">'+cellvalue+'</a>';
	}
	
	function editFormatter(){
		return '<i class="fa fa-pencil-square-o" aria-hidden="true" onclick="alert(\'edit mapping\')" style="cursor: pointer;" ></i>';
	}
	
	function addMapPopup(){
		$('#sel-service-name').html('');
		$('#stats-alias').val('');
		$('input[name="stats-stage"]').prop('checked', false);
		
		$('#addMapping-a').click();	
	}
	
	function addendServiceName(){
		var text = $("#sel-service-type option:selected").text();
		text = text.split('_')[0];
		$('#sel-service-name').html('');
		$('#sel-service-name').append($("<option></option>")
		         .attr("value",text + ' Service For Gujarat')
		         .text(text + ' Service For Gujarat'));
		$('#sel-service-name').append($("<option></option>")
		         .attr("value",text + ' Service For Maharashtra')
		         .text(text + ' Service For Maharashtra'));
		$('#sel-service-name').append($("<option></option>")
		         .attr("value",text + ' Service For Himachal')
		         .text(text + ' Service For Himachal'));
	}
	
	function addMapDetail(){
		var mapConfig = {};
		rowcounter++;
		
		mapConfig.id 				= rowcounter;
		mapConfig.serviceType 		= parent.$("#sel-service-type option:selected").text();
		mapConfig.serviceName 		= parent.$('#sel-service-name').val();
		mapConfig.statsAlias 		= parent.$('#stats-alias').val();
		mapConfig.statsStage 		= parent.$("input:radio[name='stats-stage']:checked").val();
		
		jQuery("#grid").addRowData(rowcounter, mapConfig);
	}

</script>

                                                    
