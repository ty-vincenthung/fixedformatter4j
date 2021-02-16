package com.github.vincenthung.fixedformatter4j.formatter.impl;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatBoolean;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatNumber;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;
import com.ancientprogramming.fixedformat4j.format.data.FixedFormatBooleanData;
import com.ancientprogramming.fixedformat4j.format.data.FixedFormatDecimalData;
import com.ancientprogramming.fixedformat4j.format.data.FixedFormatNumberData;
import com.ancientprogramming.fixedformat4j.format.data.FixedFormatPatternData;
import com.github.vincenthung.fixedformatter4j.annotation.FixedFormatList;
import com.github.vincenthung.fixedformatter4j.util.FixedFormatterUtil;

public class FixedFormatterInstruction extends FormatInstructions {

	private FixedFormatList fixedFormatList;

	// Backward compatibility
	public FixedFormatterInstruction(int length, Align alignment, char paddingChar,
			FixedFormatPatternData fixedFormatPatternData, FixedFormatBooleanData fixedFormatBooleanData,
			FixedFormatNumberData fixedFormatNumberData, FixedFormatDecimalData fixedFormatDecimalData) {
		super(length, alignment, paddingChar, fixedFormatPatternData, fixedFormatBooleanData, fixedFormatNumberData,
				fixedFormatDecimalData);
	}

	public FixedFormatterInstruction(int length, Align alignment, char paddingChar,
			FixedFormatPattern fixedFormatPattern, FixedFormatBoolean fixedFormatBoolean,
			FixedFormatNumber fixedFormatNumber, FixedFormatDecimal fixedFormatDecimal,
			FixedFormatList fixedFormatList) {

		super(length, alignment, paddingChar,
				FixedFormatterUtil.convert(fixedFormatPattern),
				FixedFormatterUtil.convert(fixedFormatBoolean),
				FixedFormatterUtil.convert(fixedFormatNumber),
				FixedFormatterUtil.convert(fixedFormatDecimal));

		this.fixedFormatList = fixedFormatList;
	}

	public FixedFormatList getFixedFormatList() {
		return fixedFormatList;
	}

	@Override
	public String toString() {
		return "FixedFormatterInstruction [fixedFormatList=" + fixedFormatList + ", FormatInstructions.toString()=" + super.toString()
				+ "]";
	}

}
