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
							<spring:message
								code="parsingService.configuration.file.group.label"
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
								code="parsingService.configuration.file.group.type.label"
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
	                    	<spring:message code="file.grouping.date.type" var="label"></spring:message>
	                    	<spring:message code="file.grouping.date.type.tooltip" var="tooltip"></spring:message>
	                      	<div class="form-group">
	                        	<div class="table-cell-label">${label}</div>
	                          	<div class="input-group ">
		                            <form:select path="fileGroupingParameter.groupingDateType" cssClass="form-control table-cell input-sm" id="fileGroupingParameter-groupingDateType" tabindex="20" data-toggle="tooltip" data-placement="top"  title="${tooltip }">
		                             	<c:forEach var="groupDateTypeEnum" items="${groupDateTypeEnum}">
	                             			<form:option value="${groupDateTypeEnum.value}">${groupDateTypeEnum}</form:option>
	                             		</c:forEach>
									</form:select>
									<spring:bind path="fileGroupingParameter.groupingDateType">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="fileGroupingParameter-groupingDateType_error"></elitecoreError:showError>
									</spring:bind>		
	                            </div>
	                        </div>
	                    </div>
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="processingService.configuration.file.group.archive"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="fileGroupingParameter.enableForArchive" 
										cssClass="form-control table-cell input-sm" tabindex="4"	
										id="fileGroupingParameter-enableForArchive" data-toggle="tooltip" 
										data-placement="bottom" title="${tooltip }">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
	                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
	                             		</c:forEach>
									</form:select>
									<spring:bind path="fileGroupingParameter.enableForArchive">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="processingService.configuration.file.group.archivepath"
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
								code="processingService.configuration.file.group.filter"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="fileGroupingParameter.enableForFilter" cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileGroupingParameter-enableForFilter"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
	                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
	                             		</c:forEach>	
									</form:select>
									<spring:bind path="fileGroupingParameter.enableForFilter">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="processingService.configuration.file.group.filterpath"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="fileGroupingParameter.filterDirPath"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileGroupingParameter-enableForFilterPath" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip }"></form:input>
									<spring:bind path="fileGroupingParameter.filterDirPath">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="processingService.configuration.file.group.invalid"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="fileGroupingParameter.enableForInvalid" cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileGroupingParameter-enableForInvalid"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
	                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
	                             		</c:forEach>	
									</form:select>
									<spring:bind path="fileGroupingParameter.enableForInvalid">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="processingService.configuration.file.group.invalidpath"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="fileGroupingParameter.invalidDirPath"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileGroupingParameter-enableForInvalidPath" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip }"></form:input>
									<spring:bind path="fileGroupingParameter.invalidDirPath">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="processingService.configuration.file.group.duplicate"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="fileGroupingParameter.enableForDuplicate" cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileGroupingParameter-enableForDuplicate"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
	                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
	                             		</c:forEach>	
									</form:select>
									<spring:bind path="fileGroupingParameter.enableForDuplicate">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="processingService.configuration.file.group.duplicatepath"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="fileGroupingParameter.duplicateDirPath"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileGroupingParameter-enableForDuplicatePath" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip }"></form:input>
									<spring:bind path="fileGroupingParameter.duplicateDirPath">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="processingService.configuration.file.policy.group"	var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="fileGroupingParameter.filterGroupType"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileGroupingParameter-filterGroupType"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach items="${filterGroupType}" var="filterGroupType">
											<form:option value="${filterGroupType}">${filterGroupType}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="fileGroupingParameter.filterGroupType">
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
								<div class="table-cell-label">${tooltip}</div>
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
