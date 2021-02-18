package com.github.vincenthung.fixedformatter4j.formatter.standard;

import org.apache.commons.lang.StringUtils;

import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;

public abstract class AbstractNumberFormatter<T> implements FixedFormatter<T> {

	protected String getParsedString(String originalValue, FormatInstructions instructions) {
		if (StringUtils.isEmpty(instructions.getAlignment().remove(originalValue, instructions.getPaddingChar())))
			return null;

		return instructions.getFixedFormatNumberData().getSigning().remove(originalValue, instructions);
	}

	protected String formatString(String value, FormatInstructions instructions) {
		if (value == null)
			return instructions.getAlignment().apply(null, instructions.getLength(), instructions.getPaddingChar());
		return instructions.getFixedFormatNumberData().getSigning().apply(value, instructions);
	}

}
