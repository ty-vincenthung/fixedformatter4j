package com.github.vincenthung.fixedformatter4j.formatter.standard.impl;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;
import com.github.vincenthung.fixedformatter4j.formatter.standard.AbstractDecimalFormatter;

public class FloatFormatter extends AbstractDecimalFormatter<Float> implements FixedFormatter<Float> {

	@Override
	public Float parse(String value, FormatInstructions instructions) throws FixedFormatException {

		String parsedString = getParsedString(value, instructions);
		if (parsedString == null)
			return null;

		try {
			return Float.parseFloat(parsedString);
		} catch (NumberFormatException e) {
			throw new FixedFormatException(String.format("Cannot parse %s (original: %s) to Float", parsedString, value));
		}

	}

}
