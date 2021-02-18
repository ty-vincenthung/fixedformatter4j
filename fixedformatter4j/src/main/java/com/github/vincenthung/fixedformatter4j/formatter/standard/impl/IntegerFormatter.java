package com.github.vincenthung.fixedformatter4j.formatter.standard.impl;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;
import com.github.vincenthung.fixedformatter4j.formatter.standard.AbstractNumberFormatter;

public class IntegerFormatter extends AbstractNumberFormatter<Integer> implements FixedFormatter<Integer> {

	@Override
	public Integer parse(String value, FormatInstructions instructions) throws FixedFormatException {

		String parsedString = getParsedString(value, instructions);
		if (parsedString == null)
			return null;

		try {
			return Integer.parseInt(parsedString);
		} catch (NumberFormatException e) {
			throw new FixedFormatException(String.format("Cannot parse %s (original: %s) to Integer", parsedString, value));
		}

	}

	@Override
	public String format(Integer value, FormatInstructions instructions) throws FixedFormatException {
		return formatString(value == null ? null : Integer.toString(value), instructions);
	}

}
