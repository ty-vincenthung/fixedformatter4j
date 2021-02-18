package com.github.vincenthung.fixedformatter4j.formatter.standard.impl;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;

public class CharacterFormatter implements FixedFormatter<Character> {

	@Override
	public Character parse(String value, FormatInstructions instructions) throws FixedFormatException {
		String choppedString = instructions.getAlignment().remove(value, instructions.getPaddingChar());

		if (StringUtils.isEmpty(choppedString))
			return null;

		if (choppedString.length() != 1)
			throw new FixedFormatException(String.format("Cannot parse %s (original: %s) to Character", choppedString, value));
		return choppedString.charAt(0);

	}

	@Override
	public String format(Character value, FormatInstructions instructions) throws FixedFormatException {
		return instructions.getAlignment().apply(CharUtils.toString(value), instructions.getLength(), instructions.getPaddingChar());
	}

}
