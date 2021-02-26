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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatContext;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;
import com.github.vincenthung.fixedformatter4j.annotation.FixedFormatList;
import com.github.vincenthung.fixedformatter4j.util.FixedFormatterUtil;

public class GenericListFormatter<T> implements FixedFormatter<List<T>> {

	@SuppressWarnings("unchecked")
	public List<T> parse(String value, FormatInstructions instructions) throws FixedFormatException {

		if (!(instructions instanceof FixedFormatterInstruction))
			throw new FixedFormatException("Please use ExtendedFixedFormatManagerImpl instead of FixedFormatManagerImpl for List parsing");

		FixedFormatterInstruction instrn = (FixedFormatterInstruction) instructions;
		FixedFormatList formatList = instrn.getFixedFormatList();
		if (formatList == null)
			throw new FixedFormatException(String.format("%s must be annotated to the getter for Generic List parsing", FixedFormatList.class.getName()));

		if (formatList.eachLength() <= 0)
			throw new FixedFormatException("The eachLength must be greater than 0 for list parsing");

		if (instrn.getLength() % formatList.eachLength() != 0)
			throw new FixedFormatException("The length is not a multiple of eachLength for list parsing");

		List<T> result = new ArrayList<T>();
		FixedFormatter<?> formatter = FixedFormatterUtil.getFixedFormatterInstance(
				new FormatContext<T>(0, (Class<T>) formatList.elementClass(), formatList.elementFormatter()));
		for (int offset = 0; offset < instrn.getLength(); offset += formatList.eachLength()) {
			String elementString = value.substring(offset, offset + formatList.eachLength());
			if (StringUtils.isEmpty(instructions.getAlignment().remove(elementString, instructions.getPaddingChar())))
				continue;
			Object parsedData = formatter.parse(elementString,
					new FormatInstructions(formatList.eachLength(), formatList.align(), formatList.paddingChar(),
							instrn.getFixedFormatPatternData(), instrn.getFixedFormatBooleanData(),
							instrn.getFixedFormatNumberData(), instrn.getFixedFormatDecimalData()));
			if (parsedData != null) result.add((T) parsedData);
		}
		return result.isEmpty() ? null : result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String format(List<T> value, FormatInstructions instructions) throws FixedFormatException {

		if (!(instructions instanceof FixedFormatterInstruction)) {
			throw new FixedFormatException("Please use ExtendedFixedFormatManagerImpl instead of FixedFormatManagerImpl for List formatting");
		}

		FixedFormatterInstruction instrn = (FixedFormatterInstruction) instructions;
		FixedFormatList formatList = instrn.getFixedFormatList();
		if (formatList == null)
			throw new FixedFormatException(String.format("%s must be annotated to the getter for Generic List formatting", FixedFormatList.class.getName()));

		if (formatList.eachLength() <= 0)
			throw new FixedFormatException("The eachLength must be greater than 0 for list formatting");

		if (instrn.getLength() % formatList.eachLength() != 0)
			throw new FixedFormatException("The length is not a multiple of eachLength for list formatting");

		FixedFormatter formatter = FixedFormatterUtil.getFixedFormatterInstance(
				new FormatContext<T>(0, (Class<T>) formatList.elementClass(), formatList.elementFormatter()));

		StringBuilder sb = new StringBuilder();
		if (value != null) {
			for (T element : value) {
				String formattedStr = formatter.format(element,
						new FormatInstructions(formatList.eachLength(), formatList.align(), formatList.paddingChar(),
								instrn.getFixedFormatPatternData(), instrn.getFixedFormatBooleanData(),
								instrn.getFixedFormatNumberData(), instrn.getFixedFormatDecimalData()));
				if (formattedStr != null) {
					if (formattedStr.length() < formatList.eachLength())
						sb.append(formatList.align().apply(formattedStr, formatList.eachLength(), formatList.paddingChar()));
					else
						sb.append(formattedStr);
				}
			}
		}

		String result = sb.toString();
		if (result.length() < instrn.getLength())
			result = StringUtils.rightPad(result, instrn.getLength(), instrn.getPaddingChar());

		if (result.length() > instrn.getLength())
			throw new FixedFormatException(String.format("List size %s with eachLength=%s exceed the total length=%s",
					value.size(), formatList.eachLength(), instrn.getLength()));
		return result;
	}

}
