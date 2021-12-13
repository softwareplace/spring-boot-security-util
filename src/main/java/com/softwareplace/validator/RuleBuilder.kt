package com.softwareplace.validator;

import java.util.Arrays;
import java.util.List;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.IllegalCharacterRule;
import org.passay.LengthRule;
import org.passay.Rule;
import org.passay.WhitespaceRule;

public interface RuleBuilder {

	int PASSWORD_MIN_SIZE = 6;
	int PASSWORD_MAX_SIZE = 24;

	/**
	 * @return {@link  LengthRule} default with minimum size of {@link  #PASSWORD_MIN_SIZE}
	 * and maximum siz {@link #PASSWORD_MAX_SIZE}
	 */
	default Rule lengthRule() {
		return new LengthRule(PASSWORD_MIN_SIZE, PASSWORD_MAX_SIZE);
	}

	/**
	 * @return at least one upper-case character
	 */
	default Rule upperCaseRule() {
		return new CharacterRule(EnglishCharacterData.UpperCase, 1);
	}

	/**
	 * @return at least one lower-case character
	 */
	default Rule lowerCaseRule() {
		return new CharacterRule(EnglishCharacterData.LowerCase, 1);
	}

	/**
	 * @return at least one digit character
	 */
	default Rule digitRule() {
		return new CharacterRule(EnglishCharacterData.Digit, 1);
	}

	// at least one symbol (special character)

	/**
	 * @return at least one upper-case character
	 */
	default Rule specialRule() {
		return new CharacterRule(EnglishCharacterData.Special, 1);
	}

	/**
	 * @return {@link IllegalCharacterRule} with { '\t', '\n', '\u001F', '\u001E', '\f' } for validation
	 */
	default Rule illegalCharactersRule() {
		return new IllegalCharacterRule(new char[] { '\t', '\n', '\u001F', '\u001E', '\f' });
	}

	/**
	 * @return {@link WhitespaceRule}
	 */
	default Rule whiteSpaceRule() {
		return new WhitespaceRule();
	}

	default List<Rule> defaultRules() {
		return Arrays.asList(
				lengthRule(),
				upperCaseRule(),
				lowerCaseRule(),
				digitRule(),
				specialRule(),
				illegalCharactersRule(),
				whiteSpaceRule()
		);
	}
}
