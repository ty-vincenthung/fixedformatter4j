package com.github.vincenthung.fixedformatter4j.formatter.standard.impl;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;
import com.github.vincenthung.fixedformatter4j.formatter.standard.AbstractNumberFormatter;

public class ShortFormatter extends AbstractNumberFormatter<Short> implements FixedFormatter<Short> {

	@Override
	public Short parse(String value, FormatInstructions instructions) throws FixedFormatException {

		String parsedString = getParsedString(value, instructions);
		if (parsedString == null)
			return null;

		try {
			return Short.parseShort(parsedString);
		} catch (NumberFormatException e) {
			throw new FixedFormatException(String.format("Cannot parse %s (original: %s) to Short", parsedString, value));
		}

	}

	@Override
	public String format(Short value, FormatInstructions instructions) throws FixedFormatException {
		return formatString(value == null ? null : Short.toString(value), instructions);
	}

}
