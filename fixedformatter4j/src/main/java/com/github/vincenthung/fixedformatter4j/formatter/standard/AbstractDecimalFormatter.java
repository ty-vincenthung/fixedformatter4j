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

import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;
import com.ancientprogramming.fixedformat4j.format.data.FixedFormatDecimalData;

public abstract class AbstractDecimalFormatter<T> extends AbstractNumberFormatter<T> {

	protected static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat() {
		private static final long serialVersionUID = 2215098541788458754L;
		{ setDecimalSeparatorAlwaysShown(true); setGroupingUsed(false); }
	};

	protected static final char DECIMAL_SEPARATOR = DECIMAL_FORMATTER.getDecimalFormatSymbols().getDecimalSeparator();

	@Override
	protected String getParsedString(String originalValue, FormatInstructions instructions) {
		String parsedString = super.getParsedString(originalValue, instructions);
		if (parsedString == null)
			return null;

		FixedFormatDecimalData formatDecimal = instructions.getFixedFormatDecimalData();

		if (formatDecimal.isUseDecimalDelimiter())
			parsedString = parsedString.replace(formatDecimal.getDecimalDelimiter(), '.');
		else {
			int decimalPlaces = formatDecimal.getDecimals();
			if (decimalPlaces > 0) {
				if (decimalPlaces >= parsedString.length())
					parsedString = String.format("0.%s", StringUtils.leftPad(parsedString, decimalPlaces, '0'));
				else
					parsedString = String.format("%s.%s",
							parsedString.substring(0, parsedString.length() - decimalPlaces),
							parsedString.substring(parsedString.length() - decimalPlaces, parsedString.length()));
			}
		}
		return parsedString;
	}

	@Override
	protected String formatString(String value, FormatInstructions instructions) {
		if (value != null) {
			FixedFormatDecimalData formatDecimal = instructions.getFixedFormatDecimalData();

			String beforeDelimiter = value.substring(0, value.indexOf(DECIMAL_SEPARATOR));
			String afterDelimiter = value.substring(value.indexOf(DECIMAL_SEPARATOR) + 1, value.length());
			// Right Pad the Decimals if not exceed
			afterDelimiter = StringUtils.rightPad(afterDelimiter, formatDecimal.getDecimals(), '0');
			// Reduce decimals
			afterDelimiter = afterDelimiter.substring(0, formatDecimal.getDecimals());

			value = String.format("%s%s%s",
					beforeDelimiter, formatDecimal.isUseDecimalDelimiter() ? formatDecimal.getDecimalDelimiter() : "", afterDelimiter);
		}

		return super.formatString(value, instructions);
	}

	public String format(T value, FormatInstructions instructions) throws FixedFormatException {
		return formatString(value == null ? null : DECIMAL_FORMATTER.format(value), instructions);
	}

}
