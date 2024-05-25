<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>

			<div class="modal-content">
					<div class="modal-header padding10">
						<h4 class="modal-title">
							<span id="add_label" style="display: none;"><spring:message
									code="parser.attribute.create.heading.label" ></spring:message></span> 
							<span id="update_label" style="display: none;"><spring:message
									code="parser.attribute.update.heading.label" ></spring:message></span>
							<span id="view_label" style="display: none;"><spring:message
									code="parser.attribute.view.heading.label" ></spring:message></span>
						</h4>
					</div>
					<div class="modal-body padding10 inline-form">
					<div id="AddPopUpMsg">
						<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
						</div>
						<c:if test="${(plugInType ne 'ASN1_COMPOSER_PLUGIN') and (plugInType ne 'TAP_COMPOSER_PLUGIN') and (plugInType ne 'RAP_COMPOSER_PLUGIN') and (plugInType ne 'NRTRDE_COMPOSER_PLUGIN')}">
							<div class="col-md-6 no-padding" id="seqNo">
							<div class="form-group">
								<spring:message code="regex.parser.attr.grid.sequence.no"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group">
								<form:input path="sequenceNumber"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="sequenceNumber" data-toggle="tooltip" data-placement="bottom"
										title="${tooltip }" onkeydown="isNumericOnKeyDown(event)"></form:input>
									<spring:bind path="sequenceNumber">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
							</div>
						</c:if>
						<c:if test="${(plugInType ne 'ASN1_COMPOSER_PLUGIN') and (plugInType ne 'TAP_COMPOSER_PLUGIN') and (plugInType ne 'RAP_COMPOSER_PLUGIN') and (plugInType ne 'NRTRDE_COMPOSER_PLUGIN') and (plugInType ne 'FIXED_LENGTH_ASCII_COMPOSER_PLUGIN')}">
						<div class="col-md-6 no-padding">
						<div class="form-group">
							<spring:message code="composer.attr.grid.destination.field.name" var="label" ></spring:message>
							<spring:message code="composer.attr.grid.destination.field.name.tooltip" var="tooltip" ></spring:message>
							<div class="table-cell-label">${label}</div>
							<div class="input-group">
								<form:input path="destinationField"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="destinationField" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }"></form:input>
								<spring:bind path="destinationField">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
						</div>
						</c:if>
						<c:if test="${(plugInType eq 'ASN1_COMPOSER_PLUGIN') or (plugInType eq 'TAP_COMPOSER_PLUGIN') or (plugInType eq 'RAP_COMPOSER_PLUGIN') or (plugInType eq 'NRTRDE_COMPOSER_PLUGIN')}">
						<div class="col-md-6 no-padding">
						<div class="form-group">
							<spring:message code="composer.attr.grid.destination.field.name" var="label" ></spring:message>
							<spring:message code="composer.attr.grid.destination.field.name.tooltip" var="tooltip" ></spring:message>
							<div class="table-cell-label">${label}</div>
							<div class="input-group">
								<form:input path="destinationField"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="destinationField" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }"></form:input>
								<spring:bind path="destinationField">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
						</div>
						<input type="hidden" id="attributeTypeHidden" value=""/>
						</c:if>
						<div class="col-md-6 no-padding">
						<div class="form-group">
							<spring:message code="parsing.service.add.attr.uni.field" var="label" ></spring:message>
							<spring:message code="parsing.service.add.attr.uni.field.tooltip" var="tooltip" ></spring:message>
							<div class="table-cell-label">${label}</div>
							<div class="input-group">
								<c:choose>
								<c:when test="${(plugInType eq 'ASCII_COMPOSER_PLUGIN' )or (plugInType eq 'ASN1_COMPOSER_PLUGIN')or (plugInType eq 'FIXED_LENGTH_ASCII_COMPOSER_PLUGIN')or (plugInType eq 'XML_COMPOSER_PLUGIN')}">
									<jsp:include page="../../common/formAutocomplete.jsp">
										<jsp:param name="unifiedField" value="unifiedField" ></jsp:param>
										<jsp:param name="unifiedFieldPath" value="unifiedField" ></jsp:param>
									</jsp:include>
								</c:when>
								<c:otherwise>
									<form:select path="unifiedField"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="unifiedField" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip }">
										<option value="" >Select unified field</option>
										<c:forEach items="${unifiedField}" var="unifiedField">
											<form:option value="${unifiedField}">${unifiedField}</form:option>
										</c:forEach>
									</form:select>
								</c:otherwise>
								</c:choose>
								<spring:bind path="unifiedField">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
						</div>
						
						<div class="col-md-6 no-padding">
						<div class="form-group">
							<spring:message code="parsing.service.add.attr.desc" var="label" ></spring:message>
							<spring:message code="parsing.service.add.attr.desc.tooltip" var="tooltip" ></spring:message>
							<div class="table-cell-label">${label}</div>
							<div class="input-group">
								<form:input path="description"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="description" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }"></form:input>
								<spring:bind path="description">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
						</div>
						<c:if test="${(plugInType ne 'XML_COMPOSER_PLUGIN')}">
						<div class="col-md-6 no-padding">
						<div class="form-group">
							<spring:message code="parsing.service.add.attr.default.val" var="label" ></spring:message>
							<spring:message code="parsing.service.add.attr.default.val.tooltip" var="tooltip" ></spring:message>
							<div class="table-cell-label">${label}</div>
							<div class="input-group">
								<form:input path="defualtValue"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="defualtValue" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }"></form:input>
								<spring:bind path="defualtValue">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
						</div>
						</c:if>
						<c:if test="${(plugInType ne 'ASN1_COMPOSER_PLUGIN') and (plugInType ne 'TAP_COMPOSER_PLUGIN') and (plugInType ne 'RAP_COMPOSER_PLUGIN') and (plugInType ne 'NRTRDE_COMPOSER_PLUGIN') and (plugInType ne 'FIXED_LENGTH_ASCII_COMPOSER_PLUGIN')}">
							<div class="col-md-6 no-padding">
								<div class="form-group">
									<spring:message code="composer.attr.grid.data.type" var="label" ></spring:message>
									<spring:message code="composer.attr.grid.data.type.tooltip" var="tooltip" ></spring:message>
									<div class="table-cell-label">${label}</div>
									<div class="input-group">
										<form:select path="dataType"
											cssClass="form-control table-cell input-sm" tabindex="5"
											id="dataType" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip }" onChange="changeDataType();">
											<c:forEach items="${dataTypeEnum}" var="dataTypeEnum">
												<form:option value="${dataTypeEnum}">${dataTypeEnum}</form:option>
											</c:forEach>
										</form:select>
										<spring:bind path="dataType">
											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
						</c:if>
						<c:if test="${(plugInType ne 'XML_COMPOSER_PLUGIN')}">
						<div class="col-md-6 no-padding">
						<div class="form-group">
							<spring:message code="parsing.service.add.attr.trim.char" var="label" ></spring:message>
							<spring:message code="parsing.service.add.attr.trim.char.tooltip" var="tooltip" ></spring:message>
							<div class="table-cell-label">${label}</div>
							<div class="input-group">
								<form:input path="trimchars"
									cssClass="form-control table-cell input-sm" tabindex="7"
									id="trimchars" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }"></form:input>
								<spring:bind path="trimchars">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
						</div>
						</c:if>
						
						<c:if test="${(plugInType eq 'ASCII_COMPOSER_PLUGIN') or (plugInType eq 'DETAIL_LOCAL_COMPOSER_PLUGIN')}">
							<div class="col-md-6 no-padding">
								<div class="form-group">
									<spring:message code="parsing.service.add.attr.trim.position" var="label" ></spring:message>
									<spring:message code="parsing.service.add.attr.trim.position.tooltip" var="tooltip" ></spring:message>
									<div class="table-cell-label">${label}</div>
									<div class="input-group">
										<form:select path="trimPosition" cssClass="form-control table-cell input-sm" tabindex="5" id="trimPosition" 
											data-toggle="tooltip" data-placement="bottom" title="${tooltip }">
											<c:forEach items="${trimPosition}" var="trimPosition">
												<form:option value="${trimPosition.value}">${trimPosition}</form:option>
											</c:forEach>
										</form:select>
										<spring:bind path="trimPosition">
											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
						</c:if>
						
						<c:if test="${(plugInType eq 'ASCII_COMPOSER_PLUGIN')}">
							<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="parser.grid.date.format.label" var="label" ></spring:message>
								<spring:message code="parser.grid.date.format.label.tooltip" var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}</div>
								<div class="input-group">
									<form:input path="dateFormat"
										cssClass="form-control table-cell input-sm" tabindex="6"
										id="dateFormat" data-toggle="tooltip" data-placement="bottom"
										title="${tooltip }"></form:input>
									<spring:bind path="dateFormat">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
							</div>
						</c:if>
						<c:if test="${(plugInType eq 'ASCII_COMPOSER_PLUGIN')}">
							<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="composer.attribute.replace.condition.list" var="label" ></spring:message>
								<spring:message code="composer.attribute.replace.condition.list.tooltip" var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}</div>
								<div class="input-group">
									<form:input path="replaceConditionList"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="replaceConditionList" data-toggle="tooltip" data-placement="bottom"
										title="${tooltip }"></form:input>
									<spring:bind path="replaceConditionList">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
							</div>
							
							 <div class="clearfix"></div>
							
							<hr style ="margin-top: 0px;margin-bottom: 0px;">
							
							<div class="title2">
								<spring:message code="composer.attribute.enable.padding"></spring:message>
							</div>
							
							<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="composer.attribute.enable.padding" var="label" ></spring:message>
								<spring:message code="composer.attribute.enable.padding.tooltip" var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}</div>
								<div class="input-group">
									<form:select path="paddingEnable"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="paddingEnable" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip }" onChange="changePaddingParamter()">
										
										<c:forEach items="${trueFalseEnum}" var="trueFalseEnum">
												<option value="${trueFalseEnum.name}" >${trueFalseEnum}</option>
										</c:forEach>
													
									</form:select>
									<spring:bind path="paddingEnable">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
							</div>
							<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="composer.attribute.padding.length" var="label" ></spring:message>
								<spring:message code="composer.attribute.padding.length.tooltip" var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}</div>
								<div class="input-group">
									<form:input path="length"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="length" data-toggle="tooltip" data-placement="bottom"
										title="${tooltip }" onkeydown="isNumericOnKeyDown(event)"></form:input>
									<spring:bind path="length">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
							</div>
						</c:if>
						<c:if test="${(plugInType eq 'ASCII_COMPOSER_PLUGIN') or (plugInType eq 'FIXED_LENGTH_ASCII_COMPOSER_PLUGIN')}">
							<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="composer.attribute.padding.type" var="label" ></spring:message>
								<spring:message code="composer.attribute.padding.type.tooltip" var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}</div>
								<div class="input-group">
									<form:select path="paddingType"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="paddingType" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip }">
										<c:forEach items="${paddingTypeEnum}" var="paddingTypeEnum">
											<form:option value="${paddingTypeEnum}">${paddingTypeEnum}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="paddingType">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
									
								</div>
							</div>
							</div>
							<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="composer.attribute.padding.character" var="label" ></spring:message>
								<spring:message code="composer.attribute.padding.character.tooltip" var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}</div>
								<div class="input-group">
									<form:input path="paddingChar"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="paddingChar" data-toggle="tooltip" data-placement="bottom"
										title="${tooltip }"></form:input>
									<spring:bind path="paddingChar">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
							</div>
						</c:if>
						<c:if test="${(plugInType eq 'ASCII_COMPOSER_PLUGIN')or(plugInType eq 'FIXED_LENGTH_ASCII_COMPOSER_PLUGIN')}">
							<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="composer.attribute.padding.prefix" var="label" ></spring:message>
								<spring:message code="composer.attribute.padding.prefix.tooltip" var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}</div>
								<div class="input-group">
									<form:input path="prefix"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="prefix" data-toggle="tooltip" data-placement="top"
										title="${tooltip }"></form:input>
									<spring:bind path="prefix">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
							</div>
							<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="composer.attribute.padding.suffix" var="label" ></spring:message>
								<spring:message code="composer.attribute.padding.suffix.tooltip" var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}</div>
								<div class="input-group">
									<form:input path="suffix"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="suffix" data-toggle="tooltip" data-placement="top"
										title="${tooltip }"></form:input>
									<spring:bind path="suffix">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
							</div>
						</c:if>
						<c:if test="${(plugInType eq 'ASN1_COMPOSER_PLUGIN') or (plugInType eq 'TAP_COMPOSER_PLUGIN') or (plugInType eq 'RAP_COMPOSER_PLUGIN') or (plugInType eq 'NRTRDE_COMPOSER_PLUGIN')}">
							<div class="col-md-6 no-padding">
								<div class="form-group">
									<spring:message code="parser.grid.asn1datatype.format.label.tooltip"
										var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
									<div class="input-group">
										<form:input path="asn1DataType"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="asn1DataType" data-toggle="tooltip" data-placement="top"
										title="${tooltip }"></form:input>
										<spring:bind path="asn1DataType">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>					
									</div>
								</div>
							</div>
							<div class="col-md-6 no-padding">
								<div class="form-group">
									<spring:message code="parser.grid.destinationFieldFormat.format.label.tooltip"
										var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group">
										<form:select path="destFieldDataFormat" id="destFieldDataFormat"
											class="form-control table-cell input-sm" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}">
											<option value="" >Select data Type</option>
											<c:forEach var="destFieldDataFormat" items="${destFieldDataFormat}">
												<option value="${destFieldDataFormat}">${destFieldDataFormat}</option>
											</c:forEach>
										</form:select>
										<spring:bind path="destFieldDataFormat">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
									</div>
								</div>
							</div>
							<div class="col-md-6 no-padding">
								<div class="form-group">
									<spring:message code="parser.grid.argumentDataType.format.label.tooltip"
										var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group">
										<form:select path="argumentDataType" id="argumentDataType"
											class="form-control table-cell input-sm" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}">
											<option value="" >Select data Type</option>
											<c:forEach var="argumentDataType" items="${argumentDataType}">
												<option value="${argumentDataType}">${argumentDataType}</option>
											</c:forEach>
											
										</form:select>
										<spring:bind path="argumentDataType">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
							<div class="col-md-12 no-padding">
								<div class="form-group">
									<spring:message code="parser.grid.choiceId.format.label.tooltip"
										var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group">
										<form:input path="choiceId"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="choiceId" data-toggle="tooltip" data-placement="top"
										title="${tooltip }" onkeydown="isNumericOnKeyDown(event)"></form:input>
									
									<spring:bind path="choiceId">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
							<div class="col-md-12 no-padding">
								<div class="form-group">
									<spring:message code="parser.grid.child.attrib.format.label.tooltip"
										var="tooltip" ></spring:message>
										<spring:message code="parser.grid.child.attrib.format.label"
										var="label" ></spring:message>
									<div class="table-cell-label">${label}</div>
									<div class="input-group">
										<form:input path="childAttributes"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="childAttributes" data-toggle="tooltip" data-placement="top"
										title="${tooltip }"></form:input>
										<spring:bind path="childAttributes">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>	
						</c:if>
						
						<c:if test="${(plugInType eq 'FIXED_LENGTH_ASCII_COMPOSER_PLUGIN')}">
							<div class="col-md-6 no-padding">
								<div class="form-group">
									<spring:message code="fixed.length.ascii.composer.datatype"
										var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group">
										<form:select path="dataType" id="fixedDataType"
											class="form-control table-cell input-sm" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}">
											<c:forEach items="${fixedLengthDataTypeEnum}" var="fixedLengthDataTypeEnum">
												<form:option value="${fixedLengthDataTypeEnum}">${fixedLengthDataTypeEnum}</form:option>
											</c:forEach>
										</form:select>
										<spring:bind path="dataType">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
							<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="fixed.length.ascii.composer.date.format" var="label" ></spring:message>
								<spring:message code="parser.grid.date.format.label.tooltip" var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}</div>
								<div class="input-group">
									<form:input path="fixedLengthDateFormat"
										cssClass="form-control table-cell input-sm" tabindex="6"
										id="fixedLengthDateFormat" data-toggle="tooltip" data-placement="bottom"
										title="${tooltip }"></form:input>
									<spring:bind path="fixedLengthDateFormat">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
							</div>
							<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.length" var="label" ></spring:message>
								<spring:message code="parsing.service.attr.grid.column.length" var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group">
									<form:input path="fixedLength"
										cssClass="form-control table-cell input-sm" tabindex="6"
										id="fixedLength" data-toggle="tooltip" data-placement="bottom"
										title="${tooltip }" onkeydown="isNumericOnKeyDown(event)" maxlength="5"></form:input>
									<spring:bind path="fixedLength">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind> 
								</div>
							</div>
							</div>
						</c:if>
						
						<c:if test="${(plugInType eq 'TAP_COMPOSER_PLUGIN') or (plugInType eq 'RAP_COMPOSER_PLUGIN') or (plugInType eq 'NRTRDE_COMPOSER_PLUGIN')}">
							<div class="col-md-6 no-padding">
								<div class="form-group">
									<spring:message code="roaming.plugin.attr.composefromjson.label.tooltip" var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group">
										<form:select path="composeFromJsonEnable" id="composeFromJsonEnable"
											class="form-control table-cell input-sm" data-toggle="tooltip"
											data-placement="top" title="${tooltip}">
											<c:forEach items="${trueFalseEnum}" var="trueFalseEnum">
												<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
											</c:forEach>
										</form:select>
										<spring:bind path="composeFromJsonEnable">
										    <elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									    </spring:bind>	
									</div>
								</div>
							</div>
							<div class="col-md-6 no-padding">
								<div class="form-group">
									<spring:message code="roaming.plugin.attr.clonerecord.label.tooltip" var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group">
										<form:select path="cloneRecordEnable" id="cloneRecordEnable"
												class="form-control table-cell input-sm" data-toggle="tooltip"
												data-placement="top" title="${tooltip}">
												<c:forEach items="${trueFalseEnum}" var="trueFalseEnum">
													<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
												</c:forEach>
										</form:select>
										<spring:bind path="cloneRecordEnable">
											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>	
									</div>
								</div>
							</div>
						</c:if>
						
					</div>
					<div class="modal-footer padding10">
					<sec:authorize access="hasAuthority('CREATE_COMPOSER')">
						<button type="button" id="addNewAttribute" class="btn btn-grey btn-xs " style="display: none" onclick="createUpdateAttributeMapping('ADD');">
							<spring:message code="btn.label.add" ></spring:message>
						</button>
						</sec:authorize>
						<sec:authorize access="hasAuthority('UPDATE_COMPOSER')">
						<button type="button" id="editAttributeDistribution" class="btn btn-grey btn-xs " style="display: none" onclick="createUpdateAttributeMapping('EDIT');">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
						</sec:authorize>
						<button type="button" class="btn btn-grey btn-xs "
							data-dismiss="modal" id="closebtn"
							onclick="closeFancyBox();reloadAttributeGridData();">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs "
							data-dismiss="modal" id="viewClose"
							onclick="closeFancyBox();">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
					</div>
				</div>
				
<script type="text/javascript">
$(document).ready(function() {
	changeDataType();
	changePaddingParamter();
});

function createUpdateAttributeMapping(operationType){
	var actionCommand = "";
	var pluginType = '${plugInType}';
	if(pluginType === 'ASCII_COMPOSER_PLUGIN'){
		actionCommand = '<%=ControllerConstants.ADD_EDIT_ASCII_COMPOSER_ATTRIBUTE %>';
	}else if(pluginType === 'ASN1_COMPOSER_PLUGIN'){
		actionCommand = '<%=ControllerConstants.ADD_EDIT_ASN1_COMPOSER_ATTRIBUTE %>';
	}else if(pluginType === 'XML_COMPOSER_PLUGIN'){
		actionCommand = '<%=ControllerConstants.ADD_EDIT_XML_COMPOSER_ATTRIBUTE %>';
	}else if(pluginType === 'FIXED_LENGTH_ASCII_COMPOSER_PLUGIN'){
		actionCommand = '<%=ControllerConstants.ADD_EDIT_FIXED_LENGTH_ASCII_COMPOSER_ATTRIBUTE %>'
	}else if(pluginType === jsSpringMsg.tapComposer){
		actionCommand = '<%=ControllerConstants.ADD_EDIT_TAP_COMPOSER_ATTRIBUTE %>';
	}else if(pluginType === jsSpringMsg.rapComposer){
		actionCommand = '<%=ControllerConstants.ADD_EDIT_RAP_COMPOSER_ATTRIBUTE %>';
	}else if(pluginType === jsSpringMsg.nrtrdeComposer){
		actionCommand = '<%=ControllerConstants.ADD_EDIT_NRTRDE_COMPOSER_ATTRIBUTE %>';
	}
	if(actionCommand != ""){
		createUpdateComposerAttribute(actionCommand,0,operationType);
	}
}



function changeDataType(){
	
	var dataType=$("#dataType").find(":selected").val();
	if(dataType == 'DATE'){
		$("#dateFormat").attr("readOnly",false);
	}else{
		$("#dateFormat").attr("readOnly",true);
	}
}

function changePaddingParamter(){
	var pluginType = '${plugInType}';
	if(pluginType !== 'FIXED_LENGTH_ASCII_COMPOSER_PLUGIN'){
	var paddingEnable=$("#paddingEnable").find(":selected").val();
		if(paddingEnable == 'true'){
			
			$("#length").attr("readOnly",false)
			$("#paddingType").prop("disabled",false)
			$("#paddingChar").attr("readOnly",false)
			$("#prefix").attr("readOnly",false)
			$("#suffix").attr("readOnly",false)
			
		}else{
			$("#length").attr("readOnly",true)
			$("#paddingType").prop("disabled",true)
			$("#paddingChar").attr("readOnly",true)
			$("#prefix").attr("readOnly",true)
			$("#suffix").attr("readOnly",true)
		}
	}
}
</script>				
