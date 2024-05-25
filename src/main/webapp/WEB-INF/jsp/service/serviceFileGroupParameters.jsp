<%@ taglib uri="http://www.springframework.org/security/tags"	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>


<!-- section-3 start here -->
			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message
							code="parsingService.configuration.file.grouping.section.heading" ></spring:message>
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

					<div class="fullwidth inline-form">
						
						<div class="col-md-6 no-padding">
							<spring:message code="parsingService.configuration.file.group.label" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="fileGroupParam.fileGroupEnable" 
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="parsing-service-configuration-fileGroupEnable"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
	                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
	                             		</c:forEach>	
									</form:select>
									<spring:bind path="fileGroupParam.fileGroupEnable">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="parsing-service-configuration-fileGroupEnable_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>

						<div class="col-md-6 no-padding">
							<spring:message
								code="parsingService.configuration.file.group.type.label"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="fileGroupParam.groupingType"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="parsing-service-configuration-groupingType"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach items="${groupingType}" var="groupingType">
											<form:option value="${groupingType}">${groupingType}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="fileGroupParam.groupingType">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="parsing-service-configuration-groupingType_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="consolidation.service.archive.path.label"
								var="tooltip" ></spring:message>
						
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
	
									<form:input path="fileGroupParam.archivePath"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="data-service-configuration-archivePath"
										data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
	
									<spring:bind path="fileGroupParam.archivePath">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" 
											errorId="parsing-service-configuration-archivePath_error">
										</elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="consolidation.service.source.wise.archiving.label"
								var="tooltip" ></spring:message>
						
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">

								<form:select path="fileGroupParam.sourcewiseArchive"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="data-service-configuration-sourcewiseArchive"
										data-toggle="tooltip" data-placement="bottom" title="${tooltip }">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
											<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
										</c:forEach>
									</form:select>
	
									<spring:bind path="fileGroupParam.sourcewiseArchive">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" 
											errorId="parsing-service-configuration-sourceWiseArchive_error">
										</elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>

						<c:if test="${(serviceType ne 'DATA_CONSOLIDATION_SERVICE')}">
						<div class="col-md-6 no-padding">
							<spring:message
								code="parsingService.configuration.file.archive.path.label"	var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<i class='fa fa-square'></i></div>
								<div class="input-group ">
									<form:input path="*"
										cssClass="form-control table-cell input-sm" tabindex="4" id="archivePath" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" ></form:input>
									<spring:bind path="*">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="archivePath_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						</c:if>
					</div>	
						
					</div>
				<!-- Form content end here  -->
			</div>
			<!-- section-3 end here -->
