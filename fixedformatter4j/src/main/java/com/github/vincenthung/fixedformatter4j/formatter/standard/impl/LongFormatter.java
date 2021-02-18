package com.github.vincenthung.fixedformatter4j.formatter.standard.impl;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;
import com.github.vincenthung.fixedformatter4j.formatter.standard.AbstractNumberFormatter;

public class LongFormatter extends AbstractNumberFormatter<Long> implements FixedFormatter<Long> {

	@Override
	public Long parse(String value, FormatInstructions instructions) throws FixedFormatException {

		String parsedString = getParsedString(value, instructions);
		if (parsedString == null)
			return null;

		try {
			return Long.parseLong(parsedString);
		} catch (NumberFormatException e) {
			throw new FixedFormatException(String.format("Cannot parse %s (original: %s) to Long", parsedString, value));
		}

	}

	@Override
	public String format(Long value, FormatInstructions instructions) throws FixedFormatException {
		return formatString(value == null ? null : Long.toString(value), instructions);
	}

}
