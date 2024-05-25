package com.elitecore.sm.rulelookup.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.errorreprocess.model.AutoErrorReprocessDetail;
import com.elitecore.sm.rulelookup.model.AutoReloadJobDetail;

@Component
public class AutoErrorReprocessingConfigValidator extends BaseValidator {

	@Override
	public boolean supports(Class<?> clazz) {
		return AutoReloadJobDetail.class.isAssignableFrom(clazz);
	}

	public void validateAutoErrorReprocessing(Object target, Errors errors,
			List<ImportValidationErrors> importErrorList, String entityName, boolean validateForImport) {
		this.errors = errors;
		AutoErrorReprocessDetail autoErrorReprocessDetail = (AutoErrorReprocessDetail) target;

		// serviceInstance
		if (String.valueOf(autoErrorReprocessDetail.getServerInstanceId()) == null
				|| String.valueOf(autoErrorReprocessDetail.getServerInstanceId()).equals("-1")) {
			setErrorFieldErrorMessage("", "serverInstance", null, entityName, validateForImport,
					"autoErrorReprocessing.serverInstance.invalid",
					getMessage("autoErrorReprocessing.serverInstance.invalid"));
		}
		// category
		if (autoErrorReprocessDetail.getCategory() == null || autoErrorReprocessDetail.getCategory().equals("-1")) {
			setErrorFieldErrorMessage("", "category", null, entityName, validateForImport,
					"autoErrorReprocessing.category.invalid",
					getMessage("autoErrorReprocessing.category.invalid"));
		}
		// job and trigger
		if (autoErrorReprocessDetail.getJob().getTrigger() == null
				|| autoErrorReprocessDetail.getJob().getTrigger().getID() == 0) {
			setErrorFieldErrorMessage("", "job", null, entityName, validateForImport,
					"autoErrorReprocessing.job.invalid", getMessage("autoErrorReprocessing.job.invalid"));
		}
		// rule
		if (autoErrorReprocessDetail.getRule() == null || autoErrorReprocessDetail.getRule().isEmpty()
				|| autoErrorReprocessDetail.getRule().equals("-1")) {
			setErrorFieldErrorMessage("", "rule", null, entityName, validateForImport,
					"autoErrorReprocessing.rule.invalid", getMessage("autoErrorReprocessing.rule.invalid"));
		}

	}

}
