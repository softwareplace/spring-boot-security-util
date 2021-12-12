package com.softwareplace.validator;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

	private List<Rule> rules;

	@Override public void initialize(ValidPassword constraintAnnotation) {
		try {
			this.rules = constraintAnnotation.rulesBuilder().getDeclaredConstructor().newInstance().defaultRules();
		} catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
			e.printStackTrace();
			rules = new RuleBuilderImpl().defaultRules();
		}
	}

	@Override public boolean isValid(String password, ConstraintValidatorContext context) {

		PasswordValidator validator = new PasswordValidator(rules);
		RuleResult result = validator.validate(new PasswordData(password));
		if (result.isValid()) {
			return true;
		}
		List<String> messages = validator.getMessages(result);

		String messageTemplate = String.join(",", messages);
		context.buildConstraintViolationWithTemplate(messageTemplate)
				.addConstraintViolation()
				.disableDefaultConstraintViolation();
		return false;
	}
}
