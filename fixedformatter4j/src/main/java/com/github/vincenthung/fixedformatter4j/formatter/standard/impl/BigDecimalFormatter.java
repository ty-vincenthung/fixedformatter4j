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
