<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="box box-warning" id="expression_info_box" style="display:none;">
				<div class="box-header witd-border">
					<h3 class="box-title">
						<spring:message code="business.policy.expression.section.heading" ></spring:message>
					</h3>
					<div class="box-tools pull-right" style="top: -3px;">
						<button class="btn btn-box-tool" data-widget="collapse">
							<i class="fa fa-minus"></i>
						</button>
						&nbsp;&nbsp;<a class="ion-ios-close-empty remove-block" style="margin-top:12px;" onclick="removeExpressionInfoBlock();"></a>
					</div>
					
					<!-- /.box-tools -->
				</div>
				<!-- /.box-header end here -->
				<!-- Form content start here  -->
				<div class="box-body">
					<div class="fullwidtd inline-form" style="overflow: auto; height:200px;">
						
						<table class="table table-hover table-bordered" style="width:100%;" border="1" >
							<tr>
								<th>#</th>
								<th><spring:message code="business.policy.expression.section.column.expression" ></spring:message> </th>
								<th><spring:message code="business.policy.expression.section.column.description" ></spring:message></th>
								<th><spring:message code="business.policy.expression.section.column.syntax" ></spring:message></th>
							</tr>
							<tr>
								<td>1</td>
								<td><spring:message code="expression.info.type.static" ></spring:message></td>
								<td><spring:message code="expression.info.static.description" ></spring:message></td>
								<td><spring:message code="expression.info.static.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>2</td>
								<td><spring:message code="expression.info.type.contains" ></spring:message></td>
								<td><spring:message code="expression.info.contains.description" ></spring:message></td>
								<td><spring:message code="expression.info.contains.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>3</td>
								<td><spring:message code="expression.info.type.extract" ></spring:message></td>
								<td><spring:message code="expression.info.extract.description" ></spring:message></td>
								<td><spring:message code="expression.info.extract.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>4</td>
								<td><spring:message code="expression.info.type.copy" ></spring:message></td>
								<td><spring:message code="expression.info.copy.description" ></spring:message></td>
								<td><spring:message code="expression.info.copy.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>5</td>
								<td><spring:message code="expression.info.type.right.trim" ></spring:message></td>
								<td><spring:message code="expression.info.right.trim.description" ></spring:message></td>
								<td><spring:message code="expression.info.right.trim.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>6</td>
								<td><spring:message code="expression.info.type.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.syntax" ></spring:message></td>
							</tr>
						</table>
					</div>
				</div>	
	   	
	   	</div>
