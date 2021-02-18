package com.github.vincenthung.fixedformatter4j.formatter.standard.impl;

import org.apache.commons.lang.StringUtils;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;

public class StringFormatter implements FixedFormatter<String> {

	@Override
	public String parse(String value, FormatInstructions instructions) throws FixedFormatException {
		String choppedString = instructions.getAlignment().remove(value, instructions.getPaddingChar());
		return StringUtils.isEmpty(choppedString) ? null : choppedString;
	}

	@Override
	public String format(String value, FormatInstructions instructions) throws FixedFormatException {
		return instructions.getAlignment().apply(value == null ? "" : value, instructions.getLength(), instructions.getPaddingChar());
	}

}
