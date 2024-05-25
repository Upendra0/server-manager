<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.parser.model.UnifiedFieldEnum"%>

<script>
	var pluginCounter=0;
	var rowcounter=0;
	var gridCounter=0;
	
	$(document).ready(function() {
		var unifieldAttrList = {};
		<% 
			for (UnifiedFieldEnum p : UnifiedFieldEnum.values()){
				%>
					unifieldAttrList['<%= p %>']='<%= p.getName() %>';
				<%
			}
		%>
		fillDropBox(unifieldAttrList);
	});
	
	function fillDropBox(unifieldAttrList){
		$.each(unifieldAttrList, function(key,value){
			$('#unified-field').append($('<option>', {
			    value: key,
			    text: value
			}));
		});
	}
</script>

<script>
	function addStatsDtl(){
		pluginCounter++;
		var html ='	<div class="box box-warning" id="'+pluginCounter+'_block">'+
				'    <div class="box-header with-border">'+ 
				'		<h3 class="box-title" id="title_pathListCounter "><spring:message code="consolidation.service.consoli.defi.stats.dtl.header"></spring:message></h3>'+
				'	    <div class="box-tools pull-right">'+
				'	        <button class="btn btn-box-tool block-collapse-btn" data-toggle="collapse" data-parent="#pathListConfig" href="#attrList_'+pluginCounter+'_block"><i class="fa fa-minus"></i></button>'+
				'	        &nbsp;&nbsp;<a class="ion-ios-close-empty remove-block" style="margin-top:12px;" onclick="removeBlock(\''+pluginCounter+'\')"></a>&nbsp;		'+
				'	    </div>'+
				'	</div>	'+
				'	<div class="box-body inline-form accordion-body collapse in" id="attrList_'+pluginCounter+'_block">'+
				'	 	<div class="col-md-12 no-padding">'+
				'	    	<spring:message code="consolidation.service.consoli.defi.stats.name" var="tooltip"></spring:message>'+
				'	      	<div class="form-group">'+
				'	        	<div class="table-cell-label" style="width: 25%;">${tooltip}<span class="required">*</span></div>'+
				'	          	<div class="input-group ">'+
				'	    			<input class="form-control table-cell input-sm" id="ftp-name" tabindex="1" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}"/>'+
				'					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> '+
				'	           </div>'+
				'	       </div>'+
				'	    </div>'+
				'	    <div class="col-md-12 no-padding">'+
				'	    	<spring:message code="consolidation.service.consoli.defi.stats.alias" var="tooltip"></spring:message>'+
				'	    	<div class="form-group">'+
				'	    		<div class="table-cell-label" style="width: 25%;">${tooltip}<span class="required">*</span></div>'+
				'	    		<div class="input-group">'+
				'	    			<input class="form-control table-cell input-sm" id="ftp-readFilePath" tabindex="2" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"/>'+
				'					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> '+
				'	    		</div>'+
				'	    	</div>'+
				'	    </div>'+
				'		<div class="col-md-12 no-padding">'+
				'				<div class="title2">'+
				'					<spring:message code="consolidation.service.consoli.defi.attr.list.grid.caption" ></spring:message>'+
				'				 	<span class="title2rightfield">'+
				'		          		<span class="title2rightfield-icon1-text">'+
				'		          			<a href="#" id="a-img-add-plugin" class="title2rightfield-icon1" onclick="addAttributePopup(\''+pluginCounter+'\');"></a>'+
				'		          			<a href="#" id="a-add-plugin" onclick="addAttributePopup(\''+pluginCounter+'\');"><spring:message code="btn.label.add" ></spring:message></a>'+
				'		          		</span>'+
				'		          		<span class="title2rightfield-icon1-text">'+
				'		          			<a href="#" class="title2rightfield-icon2" onclick="alert(\'delete attribute\');"></a>'+
				'		          			<a href="#" id="deleteInstance" onclick="alert(\'delete attribute\');"><spring:message code="btn.label.delete" ></spring:message></a>'+
				'		          		</span>'+
				'	          		</span>'+
				'	      		</div>'+
				'		</div>'+
				'		<div class="box-body table-responsive no-padding box">'+
				'	 		<table class="table table-hover" id="grid_'+pluginCounter+'""></table>'+
				'	   		<div id="grid_'+pluginCounter+'_PagingDiv"></div> '+
				'	   		<div class="clearfix"></div> '+
				'		</div>'+
				'		<div class="col-md-9 no-padding">'+
	        	'			<div class="form-group">'+
	        	'				<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="collps('+pluginCounter+');closeFancyBox();"><spring:message code="btn.label.submit"></spring:message></button>'+ 
	        	'				&nbsp;<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="resetInput(\''+pluginCounter+'\');"><spring:message code="btn.label.reset"></spring:message></button>'+
	        	'			</div>'+
	        	'		</div>'+
				'     </div> '+
				'	</div>';
	
	$('#divStatsList').append(html);
	loadGrid(pluginCounter);
	}
	
	function addAttrDetail(counter){
		var attrConfig = {};
		gridCounter = counter;
		rowcounter++;
		
		attrConfig.id 				= rowcounter;
		attrConfig.source 			= parent.$('#source-field').val();
		attrConfig.unifiedField 	= parent.$('#unified-field').val();
		attrConfig.operation 		= parent.$('#stats-oper').val();
		attrConfig.datatype 		= parent.$('#data-type').val();
		
		jQuery("#grid_"+counter).addRowData(rowcounter, attrConfig);
	}
	
	function collps(counter){
		$("#attrList_"+counter+"_block").collapse("toggle");
	}
	
</script>

<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'CONSOLIDATION_DEFINITION')}"><c:out value="active"></c:out></c:if>" id="conslDefi">
	<div class="tab-content no-padding  mtop10" id="divStatsList">
		<div class="fullwidth">
				<div class="title2">
					<strong><spring:message code="consolidation.service.consoli.defi.list.label"></spring:message></strong>
					<span class="title2rightfield">
						<span class="title2rightfield-icon1-text">
							<a href="#" onclick="addStatsDtl();" class="link"><spring:message code="consolidation.service.consoli.defi.add.stats.dtl"></spring:message></a><br/>
						</span>
					</span>
				</div>
		</div>
		
		<div class="box box-warning" id="0_block">
		    <div class="box-header with-border"> 
		    	<h3 class="box-title" id="title_pathListCounter "><spring:message code="consolidation.service.consoli.defi.stats.dtl.header"></spring:message></h3>
		        <div class="box-tools pull-right">
		            <button class="btn btn-box-tool block-collapse-btn" data-toggle="collapse" data-parent="#divStatsList" href="#attrList_0_block"><i class="fa fa-minus"></i></button>
		            &nbsp;&nbsp;<a class="ion-ios-close-empty remove-block" style="margin-top:12px;" onclick="removeBlock(0);"></a>&nbsp;		
		        </div>
		    </div>	
			<div class="box-body inline-form accordion-body collapse in" id="attrList_0_block">
		     	<div class="col-md-9 no-padding">
		        	<spring:message code="consolidation.service.consoli.defi.stats.name" var="tooltip"></spring:message>
		          	<div class="form-group">
		            	<div class="table-cell-label" style="width: 25%;">${tooltip}<span class="required">*</span></div>
		              	<div class="input-group">
		        			<input class="form-control table-cell input-sm" id="ftp-name" tabindex="1" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}"/>
							<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
		               </div>
		           </div>
		        </div>
		        <div class="col-md-9 no-padding">
		        	<spring:message code="consolidation.service.consoli.defi.stats.alias" var="tooltip"></spring:message>
		        	<div class="form-group">
		        		<div class="table-cell-label" style="width: 25%;">${tooltip}<span class="required">*</span></div>
		        		<div class="input-group">
		        			<input class="form-control table-cell input-sm" id="ftp-readFilePath" tabindex="2" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"/>
							<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
		        		</div>
		        	</div>
		        </div>
		    
		    	<div class="col-md-12 no-padding">
   					<div class="title2">
   						<spring:message code="consolidation.service.consoli.defi.attr.list.grid.caption" ></spring:message>
	   				 	<span class="title2rightfield">
				        	<span class="title2rightfield-icon1-text">
<!-- 				          	<sec:authorize access="hasAuthority('DELETE_SERVER_AND_SERVER_INSTANCES')"> -->
								<i class="fa fa-plus-circle" onclick="addAttributePopup('0');"></i>
				          		<a href="#" id="a-add-plugin" onclick="addAttributePopup('0');"><spring:message code="btn.label.add" ></spring:message></a>
<!-- 				          	</sec:authorize> -->
				          	</span>
				          	<span class="title2rightfield-icon1-text">
<!-- 				          	<sec:authorize access="hasAuthority('DELETE_SERVER_AND_SERVER_INSTANCES')"> -->
								<i class="fa fa-trash" onclick="alert('delete attribute');"></i>
				          		<a href="#" id="deleteInstance" onclick="alert('delete attribute');"><spring:message code="btn.label.delete" ></spring:message></a>
<!-- 				          	</sec:authorize> -->
				          	</span>
			          </span>
		          </div>
   				</div>
   				<div class="box-body table-responsive no-padding box">
             		<table class="table table-hover" id="grid_0"></table>
               		<div id="grid_0_PagingDiv"></div> 
	           		<div class="clearfix"></div> 
            	</div>
            	<div class="col-md-9 no-padding">
		        	<div class="form-group">
		        		<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="collps(0);closeFancyBox();"><spring:message code="btn.label.submit"></spring:message></button> 
		        		&nbsp;<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="resetInput(0);"><spring:message code="btn.label.reset"></spring:message></button>
		        	</div>
		        </div>
		    </div> 
		    
		</div>
		
	</div>
</div>

<a href="#divAttrConfig" class="fancybox" style="display: none;" id="addAttr">#</a>
<div id="divAttrConfig" style=" width:100%; display: none;" >
  <div class="modal-content">
      	<input type="hidden" id="" name="" value=""/>
        <div class="modal-header padding10" >
            <h4 class="modal-title"><spring:message code="consolidation.service.consoli.defi.add.atr.popup.header"></spring:message></h4>
        </div>
        <div class="modal-body inline-form">
        	
       		<input type="hidden" id="importInstanceId" name="importInstanceId" />
         	<div class="col-md-12 no-padding">
        		<div class="form-group">
            		<spring:message code="consolidation.service.consoli.defi.add.atr.popup.source.field.nm" var="tooltip"></spring:message>
         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
             		<div class="input-group ">
             			<input type="text" id="source-field" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" >
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             		</div>
             	</div>
         	</div>
         	<div class="col-md-12 no-padding">
        		<div class="form-group">
            		<spring:message code="consolidation.service.consoli.defi.add.atr.popup.unif.field.nm" var="tooltip"></spring:message>
         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
             		<div class="input-group ">
             			<select id="unified-field" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" >
             			</select>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             		</div>
             	</div>
         	</div>
         	<div class="col-md-12 no-padding">
        		<div class="form-group">
            		<spring:message code="consolidation.service.consoli.defi.add.atr.popup.stats.oper" var="tooltip"></spring:message>
         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
             		<div class="input-group ">
             			<select id="stats-oper" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" >
             			<c:if test="${statisticalOperEnum != null }">
             				<c:forEach var="statisticalOperEnum" items="${statisticalOperEnum}">
             					<option value="${statisticalOperEnum}">${statisticalOperEnum}</option>	
             				</c:forEach>
             			</c:if>
             			</select>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             		</div>
             	</div>
         	</div>
         	<div class="col-md-12 no-padding">
        		<div class="form-group">
            		<spring:message code="consolidation.service.consoli.defi.add.atr.popup.data.type" var="tooltip"></spring:message>
         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
             		<div class="input-group ">
             			<select id="data-type" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" >
             			<c:if test="${statisticalDataTypeEnum != null }">
             				<c:forEach var="statisticalDataTypeEnum" items="${statisticalDataTypeEnum}">
             					<option value="${statisticalDataTypeEnum}">${statisticalDataTypeEnum}</option>	
             				</c:forEach>
             			</c:if>
             			</select>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             		</div>
             	</div>
         	</div>
         
        </div>
        <div id="add-attr-buttons" class="modal-footer padding10">
        	<div class="col-md-12 no-padding">
        		<div class="form-group">
             		<div class="input-group ">
						<button type="button" id="btnSaveAttr" class="btn btn-grey btn-xs " onclick="addPluginDetail('0'),closeFancyBox();"><spring:message code="btn.label.save"></spring:message></button>&nbsp;&nbsp;
		    			<button type="button" id="btnUpdtAttr" style="display: none;" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="updtAttr('0');closeFancyBox();"><spring:message code="btn.label.update"></spring:message></button>
		    			<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
		    		</div>
		    	</div>
		    </div>
		</div>
		<div id="import-config-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
			<jsp:include page="../common/processing-bar.jsp"></jsp:include>
		</div>
    </div>
    <!-- /.modal-content --> 
</div>
<script>
	
	$(document).ready(function() {
		$("#grid_0").jqGrid({
        	url: "",
            datatype: "local",
            colNames:[
                      "<spring:message code='serverManagement.grid.column.id' ></spring:message>",
                      "<spring:message code='consolidation.service.consoli.defi.attr.list.grid.column.field.tit' ></spring:message>",
                      "<spring:message code='consolidation.service.consoli.defi.attr.list.grid.column.uni.field.nm' ></spring:message>",
                      "<spring:message code='consolidation.service.consoli.defi.attr.list.grid.column.stats.oper' ></spring:message>",
                      "<spring:message code='consolidation.service.consoli.defi.attr.list.grid.column.data.type' ></spring:message>",
                      "<spring:message code='consolidation.service.consoli.defi.attr.list.grid.column.edit' ></spring:message>"
                     ],
			colModel:[
            	{name:'id',index:'id',sortable:true,hidden: true},
            	{name:'source',index:'source'},
                {name:'unifiedField',index:'unifiedField',sortable:true},
            	{name:'operation',index:'operation',sortable:true},
            	{name:'datatype',index:'datatype',sortable:true},
            	{name:'',index:'',sortable:false, align:'center',formatter:editColumnFormatter}
            ],
            rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
            rowList:[10,20,60,100],
            height: 'auto',
            //width: "1000px",
			sortname: 'id',
     		sortorder: "desc",
            pager: "#grid_0_PagingDiv",
            viewrecords: true,
            multiselect: true,
            timeout : 120000,
            loadtext: "Loading...",
            caption: "<spring:message code="serverManagement.grid.caption"></spring:message>",
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
			recordtext: "<spring:message code="distribution.service.ftp.pathlist.plugin.grid.pager.total.records.text"></spring:message>",
	        emptyrecords: "<spring:message code="distribution.service.ftp.pathlist.plugin.grid.empty.records"></spring:message>",
			loadtext: "<spring:message code="distribution.service.ftp.pathlist.plugin.grid.loading.text"></spring:message>",
			pgtext : "<spring:message code="distribution.service.ftp.pathlist.plugin.grid.pager.text"></spring:message>"
		}).navGrid("#grid_0_PagingDiv",{edit:false,add:false,del:false,search:false});
		
		$(".ui-jqgrid-titlebar").hide();
	});
	
	function loadGrid(counter){
		$("#grid_"+counter).jqGrid({
			datatype: "local",
            colNames:[
                      "<spring:message code='serverManagement.grid.column.id' ></spring:message>",
                      "<spring:message code='consolidation.service.consoli.defi.attr.list.grid.column.field.tit' ></spring:message>",
                      "<spring:message code='consolidation.service.consoli.defi.attr.list.grid.column.uni.field.nm' ></spring:message>",
                      "<spring:message code='consolidation.service.consoli.defi.attr.list.grid.column.stats.oper' ></spring:message>",
                      "<spring:message code='consolidation.service.consoli.defi.attr.list.grid.column.data.type' ></spring:message>",
                      "<spring:message code='consolidation.service.consoli.defi.attr.list.grid.column.edit' ></spring:message>"
                     ],
			colModel:[
            	{name:'id',index:'id',sortable:true,hidden: true},
            	{name:'source',index:'source',align:'center'},
                {name:'unifiedField',index:'unifiedField',sortable:true, align:'center'},
            	{name:'operation',index:'operation',sortable:true, align:'center'},
            	{name:'datatype',index:'datatype',sortable:true, align:'center'},
            	{name:'',index:'',sortable:false, align:'center',formatter:editColumnFormatter}
            ],
            rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
            rowList:[10,20,60,100],
            height: 'auto',
            //width: "1000px",
			sortname: 'id',
     		sortorder: "desc",
            pager: "#grid_"+counter+"_PagingDiv",
            viewrecords: true,
            multiselect: true,
            timeout : 120000,
            loadtext: "Loading...",
            caption: "<spring:message code="serverManagement.grid.caption"></spring:message>",
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
			recordtext: "<spring:message code="distribution.service.ftp.pathlist.plugin.grid.pager.total.records.text"></spring:message>",
	        emptyrecords: "<spring:message code="distribution.service.ftp.pathlist.plugin.grid.empty.records"></spring:message>",
			loadtext: "<spring:message code="distribution.service.ftp.pathlist.plugin.grid.loading.text"></spring:message>",
			pgtext : "<spring:message code="distribution.service.ftp.pathlist.plugin.grid.pager.text"></spring:message>"
		}).navGrid("#grid_0_PagingDiv",{edit:false,add:false,del:false,search:false});
		
		$(".ui-jqgrid-titlebar").hide();
	}
	
	function addAttributePopup(counter){
		$('#divAttrConfig input, textarea').val('');
 		$('#btnSaveAttr').attr("onclick","addAttrDetail('"+counter+"'),closeFancyBox()");
 		$('#btnSaveAttr').css('display','inline-block');
		$('#btnUpdtAttr').css('display','none');
		$('#addAttr').click();
	}
	
	function editColumnFormatter(cellvalue, options, rowObject){
		return '<i class="fa fa-pencil-square-o" onclick="editAttr('+rowObject['id']+','+gridCounter+')" style="cursor: pointer;" width="14px" height="14px"></i>';
	}
	
	function removeBlock(counter){
		$("#"+counter+"_block").remove();
	}
	
	function resetInput(counter){
		$("#"+counter+"_block input, textarea").val('');
	}
	
	function editAttr(rowId,gridCounter){
		var dataFromTheRow = jQuery("#grid_"+gridCounter).jqGrid ('getRowData', rowId);
		
		$('#btnUpdtAttr').attr("onclick","updateAttr('"+rowId+"','"+gridCounter+"'),closeFancyBox()");
		
		$('#source-field').val(dataFromTheRow['source']);
		$('#unified-field').val(dataFromTheRow['unifiedField']);
		$('#stats-oper').val(dataFromTheRow['operation']);
		$('#data-type').val(dataFromTheRow['datatype']);
		$('#btnSaveAttr').css('display','none');
		$('#btnUpdtAttr').css('display','inline-block');
		$('#addAttr').click();
	}
	
	function updateAttr(rowId,gridCounter){
		var myGrid = $("#grid_"+gridCounter);
		
		var attrConfig = {};
		attrConfig.source 			= parent.$('#source-field').val();
		attrConfig.unifiedField 	= parent.$('#unified-field').val();
		attrConfig.operation 		= parent.$('#stats-oper').val();
		attrConfig.datatype 		= parent.$('#data-type').val();
		myGrid.jqGrid('setRowData',rowId,attrConfig);
	}
	
</script>

	
