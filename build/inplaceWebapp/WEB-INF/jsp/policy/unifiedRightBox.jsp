<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	<div class="col-md-3 padding10 " id="unified_field_mapping_div" style="display:none;">
				<div class="box box-warning">
					<div class="box-header witd-border">
						<h3 class="box-title">
							<spring:message code="business.policy.unified.section.heading" ></spring:message>
						</h3>
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
						<div class="fullwidtd inline-form" style="overflow: auto;">
								 
								 <div class="col-md-12 no-padding">
									<div class="form-group">
										<div class="input-group ">
											<select class="form-control table-cell input-sm" tabindex="4"
												id="svcExecParams-startupMode"
												data-toggle="tooltip" data-placement="bottom"
												title="${tooltip }">
										        <option selected="selected"> FTP_GURGAON PARSING&nbsp;&nbsp;SERVICE</option>
										        <option selected="selected"> SFTP PUNJAB PARSING&nbsp;&nbsp;SERVICE</option>
									         </select>
									        <span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
										</div>
									</div>
								</div>
								  <div class="col-md-12 no-padding">
									<div class="form-group">
										<div class="input-group ">
											<select class="form-control table-cell input-sm" tabindex="4"
												id="svcExecParams-startupMode"
												data-toggle="tooltip" data-placement="bottom"
												 title="${tooltip }">
										     		<c:forEach items="${parserList}" var="parserList">
														<option value="${parserList}">${parserList}</option>
													</c:forEach>
									       		</select>
									        <span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
										</div>
									</div>
								</div>
								
								<table class="table table-hover table-bordered" style="widtd:100%" border="1">
								<tr>
									<th> <spring:message code="business.policy.unified.section.column.unified" ></spring:message></th>
									<th> <spring:message code="business.policy.unified.section.column.description" ></spring:message></th>
								</tr>
								 <c:forEach items="${fieldList}" var="field">
										<tr>
				   							<td style="padding-left: 10px;">${field.unifiedField}</td>
				   							<td style="padding-left: 10px;">${field.description}</td>
				   						</tr>
								</c:forEach> 
								</table>
						</div>
					</div>	
				</div>
	</div>
