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

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;

public class SqlDateFormatter implements FixedFormatter<Date> {

	@Override
	public Date parse(String value, FormatInstructions instructions) throws FixedFormatException {
		String choppedString = instructions.getAlignment().remove(value, instructions.getPaddingChar());
		if (StringUtils.isEmpty(choppedString))
			return null;

		try {
			String datePattern = instructions.getFixedFormatPatternData().getPattern();
			java.util.Date date = getDateFormatter(datePattern).parse(value);
			return new Date(date.getTime());
		} catch (ParseException e) {
			throw new FixedFormatException(String.format("Cannot parse %s (original: %s) to %s", choppedString, value, Date.class));
		}
	}

	@Override
	public String format(Date value, FormatInstructions instructions) throws FixedFormatException {
		if (value == null)
			return null;

		String datePattern = instructions.getFixedFormatPatternData().getPattern();

		return instructions.getAlignment().apply(getDateFormatter(datePattern).format(value),
				instructions.getLength(), instructions.getPaddingChar());
	}

	private DateFormat getDateFormatter(String pattern) {
		if (pattern == null)
			throw new FixedFormatException("Date pattern must be set in FixedFormatPattern.value");
		return new SimpleDateFormat(pattern);
	}

}
