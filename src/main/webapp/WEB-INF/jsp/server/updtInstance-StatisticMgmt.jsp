<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>

<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_INSTANCE_STATISTIC')}"><c:out value="active"></c:out></c:if>" id="Statistic">
 	<form:form modelAttribute="server_instance_form_bean" method="POST" action="<%= ControllerConstants.UPDATE_STATISTIC_CONFIG %>" id="serverInstance-updateStatistic-form" enctype="multipart/form-data">
     <div class="box-body padding0">
     	
     	<input type="hidden" id="instanceStatus" name="instanceStatus" value="${serverInstanceStatus}" />
		<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.UPDATE_INSTANCE_STATISTIC %>" />
		<input type="hidden" name="servicesList" id="servicesList" />
		     	
     	<form:input type="hidden" path="id"  id="instance-id" ></form:input>
		<form:input type="hidden" path="logsDetail.level" name="logsDetail.level"></form:input>
		<form:input type="hidden" path="logsDetail.rollingType" name="logsDetail.rollingType"></form:input>
		<form:input type="hidden" path="logsDetail.rollingValue" name="logsDetail.rollingValue"></form:input>
		<form:input type="hidden" path="logsDetail.maxRollingUnit" name="logsDetail.maxRollingUnit"></form:input>
		<form:input type="hidden" path="logsDetail.logPathLocation" name="logsDetail.logPathLocation"></form:input>
		<form:input type="hidden" path="thresholdSysAlertEnable" name="thresholdSysAlertEnable"></form:input>
		<form:input type="hidden" path="thresholdMemory" name="thresholdMemory"></form:input>
		<form:input type="hidden" path="thresholdTimeInterval" name="thresholdTimeInterval"></form:input>
		<form:input type="hidden" path="loadAverage" name="loadAverage"></form:input>
		
		<form:input type="hidden" path="name" name="name" id="instance-name"></form:input>
		<form:input type="hidden" path="description" name="description"></form:input>
		<form:input type="hidden" path="server.id" name="server.id" id="instance-server-id"></form:input>
		<form:input type="hidden" path="server.name" name="server.name" id="instance-server-name"></form:input>
		<form:input type="hidden" path="server.description" name="server.description" id="instance-server-description"></form:input>
		<form:input type="hidden" path="server.ipAddress" name="server.ipAddress" id="instance-server-ip"></form:input>
		<form:input type="hidden" path="server.utilityPort" name="server.utilityPort" id="instance-server-utilityport"></form:input>
		<form:input type="hidden" path="server.serverType.id" name="server.serverType.id" id="instance-server-serverType"></form:input>
		<form:input type="hidden" path="port" name="port"></form:input>
		<form:input type="hidden" path="minMemoryAllocation" name="minMemoryAllocation"></form:input>
		<form:input type="hidden" path="maxMemoryAllocation" name="maxMemoryAllocation"></form:input>
		<form:input type="hidden" path="maxConnectionRetry" name="maxConnectionRetry"></form:input>
		<form:input type="hidden" path="retryInterval" name="retryInterval"></form:input>
		<form:input type="hidden" path="connectionTimeout" name="connectionTimeout"></form:input>
		<form:input type="hidden" path="scriptName" name="scriptName"></form:input>
		<form:input type="hidden" path="serverHome" name="serverHome"></form:input>
		<form:input type="hidden" path="javaHome" name="javaHome"></form:input>
		<form:input type="hidden" path="minDiskSpace" name="minDiskSpace"></form:input>
		<form:input type="hidden" path="databaseInit" name="databaseInit"></form:input>
		<form:input type="hidden" path="serverManagerDatasourceConfig.id" name="serverManagerDatasourceConfig.id" id="serverManagerDatasourceConfig_id"></form:input>
		<form:input type="hidden" path="serverManagerDatasourceConfig.name" name="serverManagerDatasourceConfig.name" id="serverManagerDatasourceConfig_name"></form:input>
		<form:input type="hidden" path="serverManagerDatasourceConfig.username" name="serverManagerDatasourceConfig.username" id="serverManagerDatasourceConfig_username"></form:input>
		<form:input type="hidden" path="serverManagerDatasourceConfig.password" name="serverManagerDatasourceConfig.password" id="serverManagerDatasourceConfig_password"></form:input>
		<form:input type="hidden" path="iploggerDatasourceConfig.id" name="iploggerDatasourceConfig.id"></form:input>
		<form:input type="hidden" path="iploggerDatasourceConfig.name" name="iploggerDatasourceConfig.name"></form:input>
		<form:input type="hidden" path="iploggerDatasourceConfig.username" name="iploggerDatasourceConfig.username"></form:input>
		<form:input type="hidden" path="iploggerDatasourceConfig.password" name="iploggerDatasourceConfig.password"></form:input>
		
		
        <div class="fullwidth ">
           <div class="box box-warning">
                 <div class="box-header with-border">
                     <h3 class="box-title"><spring:message code="updtInstacne.statistic.file.statistic.tab.heading"></spring:message></h3>
                     <div class="box-tools pull-right">
                         <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                     </div>
                     <!-- /.box-tools --> 
                 </div>
                 <!-- /.box-header -->
                 <div class="box-body inline-form " >
                 		<div class="col-md-6 no-padding">
                         	<spring:message code="updtInstacne.statistic.file.storage.enable.sts" var="tooltip"></spring:message>
                          	<div class="form-group">
                            	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
                              	<div class="input-group ">
									<form:select path="fileStatInDBEnable" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-fileStatInDBEnable" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="changeFileState(this.value)">
	                             		<form:option value="true">Enable</form:option>
	                            		<form:option value="false">Disable</form:option>
									</form:select>
									<spring:bind path="fileStatInDBEnable">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
                                </div>
                             </div>
                        </div>

                        <div class="col-md-6 no-padding">
                          	<spring:message code="updtInstacne.statistic.dest.loc" var="tooltip"></spring:message>
                            <div class="form-group">
                            	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
                              	<div class="input-group ">
                              	<c:choose>
    									<c:when test="${(server_instance_form_bean.fileStatInDBEnable eq false)}">
                              				<form:input path="fileStorageLocation" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-fileStorageLocation" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" readonly="true"></form:input>
		                                    <spring:bind path="fileStorageLocation">
												<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
											</spring:bind>	
										</c:when>
										<c:otherwise>
											<form:input path="fileStorageLocation" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-fileStorageLocation" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
		                                    <spring:bind path="fileStorageLocation">
												<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
											</spring:bind>	
										</c:otherwise>
								</c:choose>
                                </div>
                            </div>
	                     </div>
                 </div>
                 <!-- /.box-body --> 
             </div>
        </div>
        <div class="fullwidth">
           <div class="box box-warning">
                 <div class="box-header with-border">
                     <h3 class="box-title"><spring:message code="updtInstacne.statistic.db.statistic.tab.heading"></spring:message></h3>
                     <div class="box-tools pull-right">
                         <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                     </div>
                     <!-- /.box-tools --> 
                 </div>
                 <!-- /.box-header -->
                 <div class="box-body inline-form">
                 	<div class="col-md-6 no-padding">
                       	<spring:message code="updtInstacne.statistic.db.storage.enable.sts" var="tooltip"></spring:message>
                        	<div class="form-group">
                          	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
                            	<div class="input-group ">
								<form:select path="fileCdrSummaryDBEnable" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-fileCdrSummaryDBEnable" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="changeDBState(this.value)">
	                            	<form:option value="true">Enable</form:option>
	                            	<form:option value="false">Disable</form:option>
								</form:select>
								<spring:bind path="fileCdrSummaryDBEnable">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
                              </div>
                           </div>
                    </div>
					<div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.statistic.ds" var="tooltip"></spring:message>
                       	<div class="form-group">
                           	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
                            <div class="input-group ">
                            	<c:set var="chk1" value="${server_instance_form_bean.serverManagerDatasourceConfig.id}"></c:set>                            	
                            	<c:choose>
    								<c:when test="${(server_instance_form_bean.fileCdrSummaryDBEnable eq 'false')}">
		                             	<form:select path="serverManagerDatasourceConfig.id" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-serverManagerDatasourceConfig-name" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="changeDSConfigForStatistic(this.value)" readonly="true" disabled="true">
		                             	<c:forEach items="${dsConfigList}" var="dsConfig">
		                             		<c:set var="val2" value="${dsConfig.id}"></c:set>
		                             		<c:choose> 
		                             			<c:when test="${chk1==val2}">
													<form:option value="${dsConfig.id}" selected="true">${dsConfig.name}</form:option>
												</c:when>
												<c:otherwise>
													<form:option value="${dsConfig.id}">${dsConfig.name}</form:option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
										</form:select>
									</c:when>
									<c:otherwise>
										<form:select path="serverManagerDatasourceConfig.id" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-datasourceConfig-name" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="changeDSConfigForStatistic(this.value)">
		                             	<c:forEach items="${dsConfigList}" var="dsConfig">
		                             		<c:set var="val2" value="${dsConfig.id}"></c:set>
		                             		<c:choose> 
		                             			<c:when test="${chk1==val2}">
													<form:option value="${dsConfig.id}" selected="true">${dsConfig.name}</form:option>
												</c:when>
												<c:otherwise>
													<form:option value="${dsConfig.id}">${dsConfig.name}</form:option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
										</form:select>
									</c:otherwise>
								</c:choose>
                                <spring:bind path="serverManagerDatasourceConfig.id">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>								
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.data.source.url" var="tooltip"></spring:message>
                        <div class="form-group">
                        	<div class="table-cell-label">${tooltip}</div>
                            <div class="input-group ">
                             	<input class="form-control table-cell input-sm" tabindex="4" id="instance_statistic_serverManagerDatasourceConfig_connURL" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" readonly="true"/>
                               	<spring:bind path="serverManagerDatasourceConfig.connURL">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
                            </div>
                        </div>
                    </div>	
                 </div>
                  <!-- /.box-body --> 
             </div>
        </div>
     </div>
	</form:form>
	
	<div class="col-md-12 no-padding mtop5">
		<div class="fullwidth">
	       	<div class="title2"><spring:message code="updtInstacne.statistic.service.list.grid.caption"></spring:message>
	          
	      	</div>
	    </div>
	    <!-- Morris chart - Sales -->
        <div class="box-body table-responsive no-padding box" style="padding-top: 5px;">
        		<table class="table table-hover" id="stataGrid"></table>
            	<div id="stataGridPagingDiv"></div> 
           		<div class="clearfix"></div>   
	    </div>
    </div>
</div>

 <div style="display: none;">
	<form id="service_form" method="GET" action="<%= ControllerConstants.INIT_UPDATE_SERVICE %>">
		<input type="hidden" id="serviceId" name="serviceId">
		<input type="hidden" id="serviceType" name="serviceType">
		<input type="hidden" id="serviceName" name="serviceName">
		<input type="hidden" id="instanceId" name="instanceId">
	</form>
</div>
		

<script type="text/javascript">
		var servicesList = {};
		
		$(document).ready(function() {

 				$("#stataGrid").jqGrid({
		        	url: "<%= ControllerConstants.GET_SERVICE_LIST %>",
		        	mtype:"GET",
		        	postData: {
		        		serverInstanceId :$("#instance-id").val()
		        	},
		            datatype: "json",
		            colNames:[
		                      "<spring:message code='updtInstacne.statistic.grid.column.service.id' ></spring:message>",
		                      "<spring:message code='updtInstacne.statistic.grid.column.service.id' ></spring:message>",
		                      "<spring:message code='updtInstacne.statistic.grid.column.service.name' ></spring:message>",
		                      "<spring:message code='updtInstacne.statistic.grid.column.service.type' ></spring:message>",
		                      "<spring:message code='updtInstacne.statistic.grid.column.file.stats' ></spring:message>",
		                      "<spring:message code='updtInstacne.statistic.grid.column..db.stats' ></spring:message>"
		                     ],
					colModel:[
		            	{name:'id',index:'id',hidden:true},
		            	{name:'servInstanceId',index:'servInstanceId',hidden:false},
		                {name:'name',index:'name',sortable:true,formatter:serviceNameFormatter},
		                {name:'svcType',index:'svcType',hidden:true},
		            	{name:'fileState',index:'enableFileStats',sortable:false, formatter: fileStatisticFormatter,align:'center'},
		            	{name:'dbState',index:'enableDBStats',sortable:false,formatter: dbStatisticFormatter,align:'center'}
		            ],
		            rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		            rowList:[10,20,60,100],
		            height: 'auto',
					sortname: 'id',
		     		sortorder: "desc",
		            pager: "#stataGridPagingDiv",
		            viewrecords: true,
		            caption: "<spring:message code="staffMgmt.grid.caption"></spring:message>",
		     		loadComplete: function(data) {
		    	 		
		    	 		/* $('#stataGrid .checkboxbg').change(function(){
		    	 			changeStateForInstance(this);
		    			}); */
		    	 		
		    	 		// change file state in db state on page load
						changeFileState($('#instance-fileStatInDBEnable').val());
						changeDBState($('#instance-fileCdrSummaryDBEnable').val());
		    	 		
		    	 		// if validation fails than restore old service check list status 
		    	 		var oldServiceList ='${servicesList}';
		    	 		if(!oldServiceList){
		    	 			return;
		    	 		}
						var result = $.parseJSON(oldServiceList);
						$.each(result, function(k, v) {
							
							if(v.fileState!='undefined' && v.fileState==true)
						    	$("#stataGrid").jqGrid("setCell", k, "fileState", true);	
						    else if(v.fileState!='undefined' && v.fileState==false)	{
						    	$("#stataGrid").jqGrid("setCell", k, 'fileState',false);
						    }
							
							console.log("Key::" + k + ":Value:::" + v);
						    if(v.dbState!='undefined' && v.dbState==true)
						    	$("#stataGrid").jqGrid("setCell", k, "dbState", true);	
						    else if(v.dbState!='undefined' && v.dbState==false)	{
						    	$("#stataGrid").jqGrid("setCell", k, "dbState", false);
						    }
						});
					},
					onPaging: function (pgButton) {
						//clearResponseMsgDiv();
						//clearStaffGrid();
					},
					loadError : function(xhr,st,err) {
						handleGenericError(xhr,st,err);
					},
					beforeSelectRow: function (rowid, e){
						// this blank function will not select the entire row. Only checkbox can be selectable.
						
					},
					recordtext: "<spring:message code="updtInstacne.service.list.grid.pager.total.records.text"></spring:message>",
			        emptyrecords: "<spring:message code="updtInstacne.service.list.grid.empty.records"></spring:message>",
					loadtext: "<spring:message code="updtInstacne.service.list.grid.loading.text"></spring:message>",
					pgtext : "<spring:message code="updtInstacne.service.list.grid.pager.text"></spring:message>"
				}).navGrid("#stataGridPagingDiv",{edit:false,add:false,del:false,search:false});
				
				$(".ui-jqgrid-titlebar").hide();
				resizeStaffGrid();
				
 				changeDBstatisticsBasedonDBInit();
 				changeDSConfigForStatistic('${server_instance_form_bean.serverManagerDatasourceConfig.id}');
				changeDSConfig('${server_instance_form_bean.serverManagerDatasourceConfig.id}');
		});
		
		function fileStatisticFormatter(cellvalue, options, rowObject){
			
			//var toggleId = rowObject["id"] + "_file_" + cellvalue;
			//var toggleId = rowObject["name"] + "_file_" + "chkbox";
			var toggleId = rowObject["servInstanceId"] + "_" + rowObject["serviceType"] + "_file_" + "CHKBOX";
			if(cellvalue == false){
				return '<input class="checkboxbg" id="'+ toggleId +'" onchange="changeStateForInstance(this.id)" type="checkbox">';
			}else{
				return '<input class="checkboxbg" id="'+ toggleId +'" onchange="changeStateForInstance(this.id)" type="checkbox" checked="checked">';
			}
		}
		
		function dbStatisticFormatter(cellvalue, options, rowObject){
			//var toggleId = rowObject["id"] + "_db_" + cellvalue;
			//var toggleId = rowObject["name"] + "_db_" + "chkbox";
			var toggleId = rowObject["servInstanceId"] + "_" + rowObject["serviceType"] + "_db_" + "CHKBOX";
			if(rowObject["svcType"]== 'Collection Service' || rowObject["svcType"]=='Distribution Service' || rowObject["svcType"]=='Parsing Service' || rowObject["svcType"]=='Processing Service'
				|| rowObject["svcType"]=='Aggregation Service' || rowObject["svcType"]=='Data Consolidation Service'){
				
				if(cellvalue == false){
						return '<input class="checkboxbg" id="'+ toggleId +'" onchange="changeStateForInstance(this.id)" type="checkbox">';
				}else{
					return '<input class="checkboxbg" id="'+ toggleId +'" onchange="changeStateForInstance(this.id)" type="checkbox" checked="checked">';
				}
			}else{
				return ""; 
			}
		}
		
		function changeStateForInstance(id){
			if($('#'+id).is(':checked')){
				$('#'+id).attr('checked',true);
			} else{
				$('#'+id).attr('checked',false) ;
			}
		}
		
		function changeFileStateChecks(status){
			if(status=='true'){
				$("input[type='checkbox'][id*='_file_']").each(function() {
				    $(this).removeAttr('disabled');
				});
			} else {
				$("input[type='checkbox'][id*='_file_']").each(function() {
				    $(this).attr('disabled','disabled');
				});
			}
		} 
		
		function changeDBStateChecks(status){
			if(status=='true'){
				$("input[type='checkbox'][id*='db']").each(function() {
				    $(this).removeAttr('disabled');
				});
			} else {
				$("input[type='checkbox'][id*='db']").each(function() {
				    $(this).attr('disabled','disabled');
				});
			}
		}
		
		function submitStatisticForm(){
			
			$('#instance-serverManagerDatasourceConfig-name').removeAttr('disabled');
			//MED-4624 -starts
			var isFileCdrDBEnable = '${server_instance_form_bean.fileCdrSummaryDBEnable}';						
			var selectedDS=0;
			
			if(isFileCdrDBEnable=='true'){
				selectedDS = $('#instance-datasourceConfig-name').val();
			}else{
				selectedDS=$('#instance-serverManagerDatasourceConfig-name').val();
			}
			$('#serverManagerDatasourceConfig_id').val(selectedDS);			
			//MED-4624 -ends
			servicesList = {};
			var idList = $('#stataGrid').jqGrid('getDataIDs');
			var myGrid = $('#stataGrid');
			
			for(var counter=0;counter<idList.length;counter++){
				
				var rowdata =myGrid.getRowData(idList[counter]);
				var dbState = rowdata.dbState;  
				var fileState = rowdata.fileState;
				servicesList[idList[counter]]={};
				servicesList[idList[counter]].dbState=$(dbState).is(':checked');
				servicesList[idList[counter]].fileState=$(fileState).is(':checked');
			}			
			$('#servicesList').val(JSON.stringify(servicesList));
			$("#serverInstance-updateStatistic-form").submit();
		} 
		
		function resizeStaffGrid(){
			var $grid = $("#stataGrid"),
		    newWidth = $grid.closest(".ui-jqgrid").parent().width();
	 	    if(newWidth < 1000){
		    	newWidth = 800;
		    }
		    //$grid.jqGrid("setGridWidth", newWidth, true);
		}

		function resetStatisticFields(){
			resetWarningDisplay();
			$("#instance-fileStorageLocation").val('');
		}
		
		$(document).ready(function() {
			$('#lblInstanceName').text($('#instance-name').val());
			$('#lblInstanceHost').text($('#instance-server-ip').val());
			
			$("#serverInstance-updateStatistic-form input,#serverInstance-updateStatistic-form select,#serverInstance-updateStatistic-form textarea").keypress(function (event) {
			    if (event.which == 13) {
			    	submitStatisticForm();
			    }
			});
			
		});
		
		function changeFileState(value){
			if(value=='false'){
				
				$('#instance-fileStorageLocation').attr('readonly',true);
			} else {
				
				$('#instance-fileStorageLocation').attr('readonly',false);
			}
			changeFileStateChecks(value);
		}
		
		function changeDBState(value){
			//MED-4624 -starts
			var isFileCdrDBEnable = '${server_instance_form_bean.fileCdrSummaryDBEnable}';
			if(isFileCdrDBEnable=='true'){
				if(value=='true'){
					$('#instance-datasourceConfig-name').attr('readonly',false);
					$('#instance-datasourceConfig-name').removeAttr('disabled');
				} else {
					$('#instance-datasourceConfig-name').attr('readonly',true);
					$('#instance-datasourceConfig-name').attr('disabled','disabled');
				}				
			}else{
			//MED-4624 -ends
				if(value=='true'){
					$('#instance-serverManagerDatasourceConfig-name').attr('readonly',false);
					$('#instance-serverManagerDatasourceConfig-name').removeAttr('disabled');
				} else {
					$('#instance-serverManagerDatasourceConfig-name').attr('readonly',true);
					$('#instance-serverManagerDatasourceConfig-name').attr('disabled','disabled');
				}
			}			
			changeDBStateChecks(value);
		}
		
		function serviceNameFormatter(cellvalue, options, rowObject){
			var serverInstanceId = $('#instance-id').val();
			<sec:authorize access="!hasAuthority('VIEW_SERVICE_INSTANCE')">
				return cellvalue;
			</sec:authorize>
			<sec:authorize access="hasAuthority('VIEW_SERVICE_INSTANCE')">
				return '<a class="link" onclick="viewService('+"'" + rowObject["id"]+ "','"+rowObject["serviceType"]+"','"+rowObject["name"]+"','"+serverInstanceId+"'" + ')">' + cellvalue + '</a>' ;
			</sec:authorize>
		}
		
		function viewService(serviceId,serviceType,serviceName,instanceId){
			$("#serviceId").val(serviceId);	
			$("#serviceType").val(serviceType);
			$("#serviceName").val(serviceName);
			$("#instanceId").val(instanceId);
			$("#service_form").submit();
		}
		
		function changeDBstatisticsBasedonDBInit(){
			
			var dbInit = '${dbInit}';
			if(dbInit=='true'){
				$('#instance-fileCdrSummaryDBEnable').attr('readonly',false);
				$('#instance-fileCdrSummaryDBEnable').removeAttr('disabled');
			} else {
				$('#instance-fileCdrSummaryDBEnable').attr('readonly',true);
				$('#instance-fileCdrSummaryDBEnable').attr('disabled','disabled');
			}
			
			changeDBStateChecks(dbInit);
		}
		
		//MED-4624 - starts
		var dsConfigList = {};		
	    <c:forEach var="dsConfig" items="${dsConfigList}" >
	    	dsConfigList["${dsConfig.id}"]=["${dsConfig.name}","${dsConfig.connURL}","${dsConfig.username}","${dsConfig.password}"]
	    </c:forEach>	
		function changeDSConfig(id) {
			var obj = dsConfigList[id];
			if(obj != undefined && obj.length > 0){				
				$("#instance_statistic_serverManagerDatasourceConfig_connURL").val(obj[1]);
				$("#serverManagerDatasourceConfig_username").val(obj[2]);
				$("#serverManagerDatasourceConfig_password").val(obj[3]);
			}
		}

		function changeDSConfigForStatistic(id) {			
			var obj = dsConfigList[id];
			if(obj != undefined && obj.length > 0){						
				//given correct id by modified the wrong id which was pointing nowhere			
				$("#instance_statistic_serverManagerDatasourceConfig_connURL").val(obj[1]);	
			}
		}			
		//MED-4624 - ends
		
</script>
