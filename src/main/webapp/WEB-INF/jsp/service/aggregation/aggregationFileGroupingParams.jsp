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
							code="aggregationService.configuration.file.grouping.section.heading" ></spring:message>
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
							<spring:message
								code="aggregationService.configuration.file.group.label"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="fileGroupingParameter.fileGroupEnable" cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileGroupingParameter-fileGroupEnable"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
	                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
	                             		</c:forEach>	
									</form:select>
									<spring:bind path="fileGroupingParameter.fileGroupEnable">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message
								code="aggregationService.configuration.file.group.type.label"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="fileGroupingParameter.groupingType"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileGroupingParameter-groupingType"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach items="${groupingType}" var="groupingType">
											<form:option value="${groupingType}">${groupingType}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="fileGroupingParameter.groupingType">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message
								code="aggregationService.configuration.file.group.archivepath"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="fileGroupingParameter.archivePath"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileGroupingParameter-enableForArchivePath" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip }"></form:input>
									<spring:bind path="fileGroupingParameter.archivePath">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>						
												
						<div class="col-md-6 no-padding">
							<spring:message
								code="file.grouping.sourcewise.archive"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group ">
									<form:select path="fileGroupingParameter.sourcewiseArchive" cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileGroupingParameter-sourcewiseArchive"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
	                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
	                             		</c:forEach>	
									</form:select>
									<spring:bind path="fileGroupingParameter.sourcewiseArchive">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
					</div>	
						
					</div>
				<!-- Form content end here  -->
			</div>
			
			<!-- section-3 end here -->
