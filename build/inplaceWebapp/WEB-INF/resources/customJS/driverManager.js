var block = "<form method='POST' id='" + clientListCounter + "_form'> "+
		"<input type='hidden' id='" + clientListCounter + "_serviceId' name='serviceId' value='${serviceId}'> "+
		"<input type='hidden' id='"+clientListCounter+"_id' name='"+clientListCounter+"_id' > "+
		"<div class='box box-warning' id='flipbox_"+clientListCounter+"'>"+
	    "	<div class='box-header with-border'> "+
        "		<h3 class='box-title' id='title_"+clientListCounter+ "'><a href='#' class='title2rightfield-icon1-text' id='link_"+clientListCounter+ "' style='font-size: 12px;text-decoration:none'><spring:message code='netflow.collection.service.client.config.tab.header'></spring:message></a></h3>"+
        "		<div class='box-tools pull-right' id='action_"+clientListCounter+ "'>"+
        "			<button class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' data-parent='#driverList' href='#" + clientListCounter + "_block'><i class='fa fa-minus'></i></button>	"+
        "					<sec:authorize access='hasAuthority(\'DELETE_COLLECTION_CLIENT\')'>"+
        "			&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;' onclick='deleteClientPopup("+' "'+clientListCounter+'" ' + ")'></a>" +
        "					</sec:authorize>"+
        "		</div>"+
        "	</div>	"+
        "	<div class='box-body inline-form accordion-body collapse in' id='" + clientListCounter + "_block'>"+
     	"		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.name' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${tooltip}<span class='required'>*</span><i class='fa fa-square'></i></div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_name' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' style='display:inline-block;'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.ip' var='clientIP'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.port' var='port'></spring:message>"+
        "  			<div class='form-group'>"+
        "   			<div class='table-cell-label'>${clientIP}<span class='required'>*</span></div>"+
        "      			<div class='input-group'>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_clientIpAddress' data-toggle='tooltip' data-placement='bottom'  title='${clientIP}' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
		"					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_clientPort' data-toggle='tooltip' data-placement='bottom'  title='${port}' onkeydown='isNumericOnKeyDown(event)' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.loc' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_outFileLocation' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+
        "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div> "+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.bkp.path' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${tooltip}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_bkpBinaryfileLocation' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+
        "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div> "+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.format' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${tooltip}<span class='required'>*</span><i class='fa fa-square'></i></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_fileNameFormat' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='${client.fileNameFormat}' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.seq.enable' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${tooltip}<i class='fa fa-square'></i></div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_appendFileSequenceInFileName' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='${client.appendFileSequenceInFileName}'>";
						        <%
								 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
									 %>
									 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
									 <%
								     }
								%>
       
		block += "					</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' style='display:inline-block;'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.seq.range' var='seqRange'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.min.file.seq.range' var='minRange'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.max.file.seq.range' var='maxRange'></spring:message>"+
        "  			<div class='form-group'>"+
        "   			<div class='table-cell-label'>${seqRange}<i class='fa fa-square'></i></div>"+
        "      			<div class='input-group'>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_minFileSeqValue' data-toggle='tooltip' data-placement='bottom' title='${minRange}' onkeydown='isNumericOnKeyDown(event)' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
		"					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_maxFileSeqValue' data-toggle='tooltip' data-placement='bottom'  title='${maxRange}' onkeydown='isNumericOnKeyDown(event)' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.roll.type' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${tooltip}<span class='required'>*</span><i class='fa fa-square'></i></div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_rollingType' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' onchange='changeRollingTypeOption(\'"+clientListCounter+"\');' >"+
        "				    	<c:forEach var='rollingTypeEnum' items='${rollingTypeEnum}' >"+
		"							<option value='${rollingTypeEnum}' selected>${rollingTypeEnum}</option>"+
		"				    	</c:forEach>" +
	    "					</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.roll.time' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${tooltip}<i class='fa fa-square'></i></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_timeLogRollingUnit' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='${client.timeLogRollingUnit}' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.roll.unit' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${tooltip}<i class='fa fa-square'></i></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_volLogRollingUnit' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='${client.volLogRollingUnit}'  />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.compress' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${tooltip}<span class='required'>*</span><i class='fa fa-square'></i></div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_inputCompressed' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' >";
					        <%
							 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
	    block +=	"		</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.snmp.enable' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_snmpAlertEnable' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' onchange='changeSNMPAlterStatus(\'"+clientListCounter+"\')'>";
					        <%
							 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
		block +="			</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.snmp.interval' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${tooltip}</div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_alertInterval' data-toggle='tooltip' data-placement='bottom' onkeydown='isNumericOnKeyDown(event)'  title='${tooltip }'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.enabl.client' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_status' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
        "				    	<c:forEach var='stateEnum' items='${stateEnum}' >"+
		"							<option value='${stateEnum}'>${stateEnum}</option>"+
		"				    	</c:forEach>" +
		"					</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-12 no-padding'>"+
        "			<spring:message code='ftp.driver.mgmt.pathlist.max.count.limit' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "				<sec:authorize access='hasAuthority(\'CREATE_COLLECTION_CLIENT\')'>"+
        "      			<div id='" + clientListCounter + "_buttons-div' class='input-group '>"+
        "      				<button type='button' class='btn btn-grey btn-xs ' id='" + clientListCounter + "_savebtn' onclick='addClient(\'"+clientListCounter+"\');'><spring:message code='btn.label.save'></spring:message></button>&nbsp;" +
        "					<button type='button' class='btn btn-grey btn-xs ' id='" + clientListCounter + "_updatebtn' style='display:none;' onclick='updateClient(\'"+clientListCounter+"\');'><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
        "					<button type='button' class='btn btn-grey btn-xs ' tabindex='15' onclick='resetClientDetailDiv(\'"+clientListCounter+"\');'><spring:message code='btn.label.reset'></spring:message></button>&nbsp;" +
        "       		</div>"+
        "				</sec:authorize>"+
        "					<div id='" + clientListCounter + "_progress-bar-div' class='input-group' style='display: none;'> "+
        "						<label>Processing Request &nbsp;&nbsp; </label> "+
        "							<img src='img/processing-bar.gif'>"+
		"					</div> "+
        "   		</div>" +
        "		</div>" +
     	"	</div>"+
		"</div>"+
		"</form>";
