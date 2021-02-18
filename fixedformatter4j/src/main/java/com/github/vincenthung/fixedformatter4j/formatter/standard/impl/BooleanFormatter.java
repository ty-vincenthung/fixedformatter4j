package com.github.vincenthung.fixedformatter4j.formatter.standard.impl;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;

public class BooleanFormatter implements FixedFormatter<Boolean> {

	@Override
	public Boolean parse(String value, FormatInstructions instructions) throws FixedFormatException {
		String choppedString = instructions.getAlignment().remove(value, instructions.getPaddingChar());

		if (StringUtils.isEmpty(choppedString))
			return null;

		Boolean result = BooleanUtils.toBooleanObject(value,
				instructions.getFixedFormatBooleanData().getTrueValue(),
				instructions.getFixedFormatBooleanData().getFalseValue(),
				"");

		if (result == null)
			throw new FixedFormatException(String.format("Cannot parse %s (original: %s) to Boolean", choppedString, value));
		return result;

	}

	@Override
	public String format(Boolean value, FormatInstructions instructions) throws FixedFormatException {
		String booleanString = BooleanUtils.toString(value,
				instructions.getFixedFormatBooleanData().getTrueValue(),
				instructions.getFixedFormatBooleanData().getFalseValue(),
				null);
		return instructions.getAlignment().apply(booleanString, instructions.getLength(), instructions.getPaddingChar());
	}

}
