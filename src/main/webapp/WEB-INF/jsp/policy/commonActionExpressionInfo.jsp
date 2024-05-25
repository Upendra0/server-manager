<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="box box-warning" id="action_expression_info_box" style="display:none;">
				<div class="box-header witd-border">
					<h3 class="box-title">
						<spring:message code="business.policy.expression.section.heading" ></spring:message>
					</h3>
					<div class="box-tools pull-right" style="top: -3px;">
						<button class="btn btn-box-tool" data-widget="collapse">
							<i class="fa fa-minus"></i>
						</button>
						&nbsp;&nbsp;<a class="ion-ios-close-empty remove-block" style="margin-top:12px;" onclick="removeActionExpressionInfoBlock();"></a>
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
							<%-- <tr>
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
							</tr> --%>
							<tr>
								<td>1</td>
								<td><spring:message code="expression.info.type.trim.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.trim.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.trim.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>2</td>
								<td><spring:message code="expression.info.type.leftTrim.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.leftTrim.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.leftTrim.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>3</td>
								<td><spring:message code="expression.info.type.right.trim" ></spring:message></td>
								<td><spring:message code="expression.info.right.trim.description" ></spring:message></td>
								<td><spring:message code="expression.info.right.trim.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>4</td>
								<td><spring:message code="expression.info.type.lpad.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.lpad.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.lpad.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>5</td>
								<td><spring:message code="expression.info.type.absolute.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.absolute.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.absolute.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>6</td>
								<td><spring:message code="expression.info.type.addition.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.addition.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.addition.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>7</td>
								<td><spring:message code="expression.info.type.ceil.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.ceil.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.ceil.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>8</td>
								<td><spring:message code="expression.info.type.floor.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.floor.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.floor.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>9</td>
								<td><spring:message code="expression.info.type.DateDifference.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.DateDifference.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.DateDifference.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>10</td>
								<td><spring:message code="expression.info.type.Hour.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.Hour.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.Hour.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>11</td>
								<td><spring:message code="expression.info.type.Month.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.Month.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.Month.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>12</td>
								<td><spring:message code="expression.info.type.WeekDay.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.WeekDay.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.WeekDay.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>13</td>
								<td><spring:message code="expression.info.type.Year.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.Year.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.Year.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>14</td>
								<td><spring:message code="expression.info.type.max.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.max.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.max.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>15</td>
								<td><spring:message code="expression.info.type.min.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.min.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.min.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>16</td>
								<td><spring:message code="expression.info.type.round.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.round.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.round.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>17</td>
								<td><spring:message code="expression.info.type.random.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.random.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.random.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>18</td>
								<td><spring:message code="expression.info.type.concat.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.concat.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.concat.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>19</td>
								<td><spring:message code="expression.info.type.rpad.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.rpad.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.rpad.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>20</td>
								<td><spring:message code="expression.info.type.copy.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.copy.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.copy.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>21</td>
								<td><spring:message code="expression.info.type.sysDate.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.sysDate.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.sysDate.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>22</td>
								<td><spring:message code="expression.info.type.dateFormat.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.dateFormat.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.dateFormat.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>23</td>
								<td><spring:message code="expression.info.type.extract.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.extract.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.extract.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>24</td>
								<td><spring:message code="expression.info.type.formatDateToLong.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.formatDateToLong.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.formatDateToLong.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>25</td>
								<td><spring:message code="expression.info.type.length.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.length.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.length.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>26</td>
								<td><spring:message code="expression.info.type.toUpperCase.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.toUpperCase.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.toUpperCase.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>27</td>
								<td><spring:message code="expression.info.type.toLowerCase.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.toLowerCase.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.toLowerCase.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>28</td>
								<td><spring:message code="expression.info.type.netmatch.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.netmatch.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.netmatch.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>29</td>
								<td><spring:message code="expression.info.type.now.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.now.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.now.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>30</td>
								<td><spring:message code="expression.info.type.removeFiller.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.removeFiller.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.removeFiller.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>31</td>
								<td><spring:message code="expression.info.type.replace.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.replace.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.replace.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>32</td>
								<td><spring:message code="expression.info.type.replaceAll.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.replaceAll.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.replaceAll.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>33</td>
								<td><spring:message code="expression.info.type.replaceFirst.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.replaceFirst.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.replaceFirst.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>34</td>
								<td><spring:message code="expression.info.type.subString.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.subString.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.subString.syntax" ></spring:message></td>
							</tr>	
							<tr>
								<td>35</td>
								<td><spring:message code="expression.info.type.strip.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.strip.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.strip.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>36</td>
								<td><spring:message code="expression.info.type.swapNibble.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.swapNibble.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.swapNibble.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>37</td>
								<td><spring:message code="expression.info.type.formateDateAccordingToGMT.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.formateDateAccordingToGMT.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.formateDateAccordingToGMT.syntax" ></spring:message></td>
							</tr>
							<tr>
								<td>38</td>
								<td><spring:message code="expression.info.type.merge.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.merge.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.merge.syntax" ></spring:message></td>
							</tr>	
							<tr>
								<td>39</td>
								<td><spring:message code="expression.info.type.subStringWithChars.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.subStringWithChars.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.subStringWithChars.syntax" ></spring:message></td>
							</tr>
							<tr style="display: none;">
								<td>40</td>
								<td><spring:message code="expression.info.type.expression" ></spring:message></td>
								<td><spring:message code="expression.info.expression.description" ></spring:message></td>
								<td><spring:message code="expression.info.expression.syntax" ></spring:message></td>
							</tr>
						</table>
					</div>
				</div>	
	   	
	   	</div>
