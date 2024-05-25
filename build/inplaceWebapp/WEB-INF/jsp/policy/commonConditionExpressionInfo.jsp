<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="box box-warning" id="condition_expression_info_box" style="display:none;">
				<div class="box-header witd-border">
					<h3 class="box-title">
						<spring:message code="business.policy.expression.section.heading" ></spring:message>
					</h3>
					<div class="box-tools pull-right" style="top: -3px;">
						<button class="btn btn-box-tool" data-widget="collapse">
							<i class="fa fa-minus"></i>
						</button>
						&nbsp;&nbsp;<a class="ion-ios-close-empty remove-block" style="margin-top:12px;" onclick="removeConditionExpressionInfoBlock();"></a>
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
								<td><spring:message code="expression.info.type.notequalsto" ></spring:message></td>
								<td><spring:message code="expression.info.notequalsto.description" ></spring:message></td>
								<td><spring:message code="expression.info.notequalsto.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>2</td>
								<td><spring:message code="expression.info.type.equalsto" ></spring:message></td>
								<td><spring:message code="expression.info.equalsto.description" ></spring:message></td>
								<td><spring:message code="expression.info.equalsto.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>3</td>
								<td><spring:message code="expression.info.type.lessthanorequalto" ></spring:message></td>
								<td><spring:message code="expression.info.lessthanorequalto.description" ></spring:message></td>
								<td><spring:message code="expression.info.lessthanorequalto.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>4</td>
								<td><spring:message code="expression.info.type.lessthan" ></spring:message></td>
								<td><spring:message code="expression.info.lessthan.description" ></spring:message></td>
								<td><spring:message code="expression.info.lessthan.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>5</td>
								<td><spring:message code="expression.info.type.greaterthan" ></spring:message></td>
								<td><spring:message code="expression.info.greaterthan.description" ></spring:message></td>
								<td><spring:message code="expression.info.greaterthan.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>6</td>
								<td><spring:message code="expression.info.type.greaterthanorequalto" ></spring:message></td>
								<td><spring:message code="expression.info.greaterthanorequalto.description" ></spring:message></td>
								<td><spring:message code="expression.info.greaterthanorequalto.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>7</td>
								<td><spring:message code="expression.info.type.logicaland" ></spring:message></td>
								<td><spring:message code="expression.info.logicaland.description" ></spring:message></td>
								<td><spring:message code="expression.info.logicaland.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>8</td>
								<td><spring:message code="expression.info.type.logicalor" ></spring:message></td>
								<td><spring:message code="expression.info.logicalor.description" ></spring:message></td>
								<td><spring:message code="expression.info.logicalor.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>9</td>
								<td><spring:message code="expression.info.type.logicalnot" ></spring:message></td>
								<td><spring:message code="expression.info.logicalnot.description" ></spring:message></td>
								<td><spring:message code="expression.info.logicalnot.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>10</td>
								<td><spring:message code="expression.info.type.endWith" ></spring:message></td>
								<td><spring:message code="expression.info.endWith.description" ></spring:message></td>
								<td><spring:message code="expression.info.endWith.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>11</td>
								<td><spring:message code="expression.info.type.isBlank" ></spring:message></td>
								<td><spring:message code="expression.info.isBlank.description" ></spring:message></td>
								<td><spring:message code="expression.info.isBlank.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>12</td>
								<td><spring:message code="expression.info.type.startWith" ></spring:message></td>
								<td><spring:message code="expression.info.startWith.description" ></spring:message></td>
								<td><spring:message code="expression.info.startWith.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>13</td>
								<td><spring:message code="expression.info.type.validateRegex" ></spring:message></td>
								<td><spring:message code="expression.info.validateRegex.description" ></spring:message></td>
								<td><spring:message code="expression.info.validateRegex.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>14</td>
								<td><spring:message code="expression.info.type.inoperator" ></spring:message></td>
								<td><spring:message code="expression.info.inoperator.description" ></spring:message></td>
								<td><spring:message code="expression.info.inoperator.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>15</td>
								<td><spring:message code="expression.info.type.isnull" ></spring:message></td>
								<td><spring:message code="expression.info.isnull.description" ></spring:message></td>
								<td><spring:message code="expression.info.isnull.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>16</td>
								<td><spring:message code="expression.info.type.isdategreaterthan" ></spring:message></td>
								<td><spring:message code="expression.info.isdategreaterthan.description" ></spring:message></td>
								<td><spring:message code="expression.info.isdategreaterthan.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>17</td>
								<td><spring:message code="expression.info.type.isdatelessthan" ></spring:message></td>
								<td><spring:message code="expression.info.isdatelessthan.description" ></spring:message></td>
								<td><spring:message code="expression.info.isdatelessthan.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>18</td>
								<td><spring:message code="expression.info.type.isdateequals" ></spring:message></td>
								<td><spring:message code="expression.info.isdateequals.description" ></spring:message></td>
								<td><spring:message code="expression.info.isdateequals.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>19</td>
								<td><spring:message code="expression.info.type.bestmatch" ></spring:message></td>
								<td><spring:message code="expression.info.bestmatch.description" ></spring:message></td>
								<td><spring:message code="expression.info.bestmatch.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>20</td>
								<td><spring:message code="expression.info.type.contains" ></spring:message></td>
								<td><spring:message code="expression.info.contains.description" ></spring:message></td>
								<td><spring:message code="expression.info.contains.syntax" ></spring:message></td>
							</tr>
							
							<tr>
								<td>21</td>
								<td><spring:message code="expression.info.type.isdatevalid" ></spring:message></td>
								<td><spring:message code="expression.info.isdatevalid.description" ></spring:message></td>
								<td><spring:message code="expression.info.isdatevalid.syntax" ></spring:message></td>
							</tr>
							
						</table>
					</div>
				</div>	
	   	
	   	</div>
