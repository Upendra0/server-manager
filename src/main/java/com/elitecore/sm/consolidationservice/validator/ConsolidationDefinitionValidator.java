package com.elitecore.sm.consolidationservice.validator;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.services.model.DataConsolidationService;

@Component(value = "consolidationDefinitionValidator")
public class ConsolidationDefinitionValidator extends BaseValidator {

	@Override
	public boolean supports(Class<?> clazz) {
		boolean retunStatement = false;
		if ((DataConsolidation.class.isAssignableFrom(clazz))
				|| (DataConsolidationService.class.isAssignableFrom(clazz))) {
			retunStatement = true;
		}

		return retunStatement;
	}
}
