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
package com.github.vincenthung.fixedformatter4j.formatter.standard;

import org.apache.commons.lang.StringUtils;

import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;

public abstract class AbstractNumberFormatter<T> implements FixedFormatter<T> {

	protected String getParsedString(String originalValue, FormatInstructions instructions) {
		if (StringUtils.isBlank(instructions.getAlignment().remove(originalValue, instructions.getPaddingChar())))
			return null;

		return instructions.getFixedFormatNumberData().getSigning().remove(originalValue, instructions);
	}

	protected String formatString(String value, FormatInstructions instructions) {
		if (value == null)
			return instructions.getAlignment().apply(null, instructions.getLength(), instructions.getPaddingChar());
		return instructions.getFixedFormatNumberData().getSigning().apply(value, instructions);
	}

}
