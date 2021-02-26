/* Copyright 2021 vincenthung or the original authors.
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
*/
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
import com.github.vincenthung.fixedformatter4j.annotation.FixedFormatEnum;
import com.github.vincenthung.fixedformatter4j.annotation.FixedFormatList;
import com.github.vincenthung.fixedformatter4j.util.FixedFormatterUtil;

public class FixedFormatterInstruction extends FormatInstructions {

	private FixedFormatList fixedFormatList;
	private FixedFormatEnum fixedFormatEnum;

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
			FixedFormatList fixedFormatList, FixedFormatEnum fixedFormatEnum) {

		super(length, alignment, paddingChar,
				FixedFormatterUtil.convert(fixedFormatPattern),
				FixedFormatterUtil.convert(fixedFormatBoolean),
				FixedFormatterUtil.convert(fixedFormatNumber),
				FixedFormatterUtil.convert(fixedFormatDecimal));

		this.fixedFormatList = fixedFormatList;
		this.fixedFormatEnum = fixedFormatEnum;
	}

	public FixedFormatList getFixedFormatList() {
		return fixedFormatList;
	}

	public FixedFormatEnum getFixedFormatEnum() {
		return fixedFormatEnum;
	}

	@Override
	public String toString() {
		return "FixedFormatterInstruction [fixedFormatList=" + fixedFormatList + ", fixedFormatEnum=" + fixedFormatEnum
				+ ", FormatInstructions.toString()=" + super.toString() + "]";
	}

}
