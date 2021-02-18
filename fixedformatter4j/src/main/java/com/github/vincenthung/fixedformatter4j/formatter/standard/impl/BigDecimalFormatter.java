package com.github.vincenthung.fixedformatter4j.formatter.standard.impl;

import java.math.BigDecimal;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;
import com.github.vincenthung.fixedformatter4j.formatter.standard.AbstractDecimalFormatter;

public class BigDecimalFormatter extends AbstractDecimalFormatter<BigDecimal> implements FixedFormatter<BigDecimal> {

	@Override
	public BigDecimal parse(String value, FormatInstructions instructions) throws FixedFormatException {

		String parsedString = getParsedString(value, instructions);
		if (parsedString == null)
			return null;

		try {
			return new BigDecimal(parsedString);
		} catch (NumberFormatException e) {
			throw new FixedFormatException(String.format("Cannot parse %s (original: %s) to BigDecimal", parsedString, value));
		}

	}

}
