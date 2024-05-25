<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<style>
.left{float:left}
.working-thread-left{width: 67%; float:left;}
</style>
<div class="box box-warning">
	<div class="box-header with-border">
		<h3 class="box-title">
			<spring:message
				code="ascii.plugin.advance.details.heading.label" ></spring:message>
		</h3>
		<div class="box-tools pull-right">
			<button class="btn btn-box-tool" data-widget="collapse">
				<i class="fa fa-minus"></i>
			</button>
		</div>
		<!-- /.box-tools -->
	</div>
	<!-- /.box-header -->
	<div class="box-body inline-form ">
	
	
		<div class="col-md-6 no-padding">
				<spring:message code="asn1.plugin.advance.details.record.main.attribute"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
					<div class="input-group ">
						<form:input path="recMainAttribute"
							cssClass="form-control table-cell input-sm" tabindex="1"
							id="recMainAttribute" data-toggle="tooltip" data-placement="bottom"
							title="${tooltip }" ></form:input>
						<spring:bind path="recMainAttribute">
							<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
		</div>
		<div class="col-md-6 no-padding" id="removeAddByteDiv" >
			<spring:message code="asn1.plugin.advance.details.do.remove.additional.byte.label" var="label" ></spring:message>
			<spring:message code="asn1.plugin.advance.details.do.remove.additional.byte.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:select path="removeAddByte" cssClass="form-control table-cell input-sm" tabindex="2"
										id="removeAddByte"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }" onChange="changeHeaderOffset();">
										 
					<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
						<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
					</c:forEach>
										
					</form:select>
					<spring:bind path="removeAddByte">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		<div class="col-md-6 no-padding" id="headerOffsetDiv">
				<spring:message code="asn1.plugin.advance.details.header.offset"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}</div>
					<div class="input-group ">
						<form:input path="headerOffset"
							cssClass="form-control table-cell input-sm" tabindex="3"
							id="headerOffset" data-toggle="tooltip" data-placement="bottom"
							title="${tooltip }" onkeydown="isNumericOnKeyDown(event)" maxlength="5" ></form:input>
						<spring:bind path="headerOffset">
							<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
		</div>
		<div class="col-md-6 no-padding" id="recOffsetDiv">
				<spring:message code="asn1.plugin.advance.details.record.offset"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}</div>
					<div class="input-group ">
						<form:input path="recOffset"
							cssClass="form-control table-cell input-sm" tabindex="4"
							id="recOffset" data-toggle="tooltip" data-placement="bottom"
							title="${tooltip }" onkeydown="isNumericOnKeyDown(event)" maxlength="5" ></form:input>
						<spring:bind path="recOffset">
							<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
		</div>
		
		<!-- asn to xml --> 
		
		<div class="col-md-6 no-padding" id="skipAttributeMappingDiv" >
			<spring:message code="asn1.plugin.advance.details.do.skip.attribute.mapping.label" var="label" ></spring:message>
			<spring:message code="asn1.plugin.advance.details.do.skip.attribute.mapping.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:select path="skipAttributeMapping" cssClass="form-control table-cell input-sm" tabindex="2"
										id="skipAttributeMapping"
										data-toggle="tooltip" onChange="changeSkipAttributeMapping();" data-placement="bottom" title="${tooltip}">
										 
					<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
						<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
					</c:forEach>
										
					</form:select>
					<spring:bind path="skipAttributeMapping">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding" id="decodeFormatDiv" >
			<spring:message code="asn1.plugin.advance.details.do.skip.attribute.mapping.decode.type.label" var="label" ></spring:message>
			<spring:message code="asn1.plugin.advance.details.do.skip.attribute.mapping.decode.type.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:select path="decodeFormat" cssClass="form-control table-cell input-sm" tabindex="2"
										id="decodeFormat"
										data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
										 
						<c:forEach var="asn1DecodeTypeEnum" items="${asn1DecodeTypeEnum}">
							<form:option value="${asn1DecodeTypeEnum.value}">${asn1DecodeTypeEnum}</form:option>
						</c:forEach>
					</form:select>
					<spring:bind path="decodeFormat">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding" id="rootNodeNameDiv">
				<spring:message code="asn1.plugin.advance.details.do.skip.attribute.mapping.root.node.name.label"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}</div>
					<div class="input-group ">
						<form:input path="rootNodeName"
							cssClass="form-control table-cell input-sm" tabindex="4"
							id="rootNodeName" data-toggle="tooltip" data-placement="bottom"
							title="${tooltip }" maxlength="100" ></form:input>
						<spring:bind path="rootNodeName">
							<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
		</div>
		
		<%-- <div class="col-md-6 no-padding" id="bufferSizeDiv">
				<spring:message code="asn1.plugin.advance.details.do.skip.attribute.mapping.buffer.size.label"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}</div>
					<div class="input-group ">
						<form:input path="bufferSize"
							cssClass="form-control table-cell input-sm" tabindex="4"
							id="bufferSize" data-toggle="tooltip" data-placement="bottom"
							title="${tooltip }" onkeydown="isNumericOnKeyDown(event)" maxlength="9" ></form:input>
						<spring:bind path="bufferSize">
							<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
		</div> --%>
		
		<!-- asn to xml -->
		
		<div class="col-md-6 no-padding" id="removeAddHeaderFooterDiv" >
			<spring:message code="asn1.plugin.advance.details.do.remove.additional.header.footer.label" var="label" ></spring:message>
			<spring:message code="asn1.plugin.advance.details.do.remove.additional.header.footer.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:select path="removeAddHeaderFooter" cssClass="form-control table-cell input-sm" tabindex="2"
										id="removeAddHeaderFooter"
										data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
										 
					<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
						<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
					</c:forEach>
										
					</form:select>
					<spring:bind path="removeAddHeaderFooter">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding" id="removeFillersDiv" >
			<spring:message code="asn1.plugin.advance.details.do.remove.filler.label" var="label" ></spring:message>
			<spring:message code="asn1.plugin.advance.details.do.remove.filler.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:select path="removeFillers" cssClass="form-control table-cell input-sm" tabindex="5"
										id="removeFillers"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }" onChange="changeRecordStartID();">
										 
					<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
						<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
					</c:forEach>
										
					</form:select>
					<spring:bind path="removeFillers">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding" id="recordStartIdsDiv">
				<spring:message code="asn1.plugin.advance.details.record.start.ids"
					var="label" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<form:input path="recordStartIds" maxlength="100"
							cssClass="form-control table-cell input-sm" tabindex="6"
							id="recordStartIds"  data-toggle="tooltip" data-placement="bottom"
							title="${tooltip }" ></form:input>
						<spring:bind path="recordStartIds">
							<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
		</div>
	</div>
	<!-- /.box-body -->
</div>

<script type="text/javascript">
$(document).ready(function() {
	
	changeHeaderOffset();
	changeRecordStartID();
	changeSkipAttributeMapping();
	
});


function changeHeaderOffset(){
	
	$('#recOffsetDiv').hide();
	$('#headerOffsetDiv').hide();
	if($('#removeAddByte').val()=='true' || $('#removeAddByte').val()=='True'){
		$('#headerOffsetDiv').show();
		$('#recOffsetDiv').show();
		
	}
}

function changeRecordStartID(){
	
	$('#recordStartIdsDiv').hide();
	if($('#removeFillers').val()=='true' || $('#removeFillers').val()=='True'){
		$('#recordStartIdsDiv').show();

	}
}

function changeSkipAttributeMapping(){
	$('#rootNodeNameDiv').hide();
	$('#decodeFormatDiv').hide();
	//$('#bufferSizeDiv').hide();
	if($('#skipAttributeMapping').val()=='true' || $('#skipAttributeMapping').val()=='True'){
		$('#rootNodeNameDiv').show();
		$('#decodeFormatDiv').show();
		//$('#bufferSizeDiv').show();
	}
}

</script>
