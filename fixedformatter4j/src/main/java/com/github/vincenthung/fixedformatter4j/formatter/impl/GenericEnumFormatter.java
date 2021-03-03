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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatContext;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;
import com.github.vincenthung.fixedformatter4j.annotation.FixedFormatEnum;
import com.github.vincenthung.fixedformatter4j.manager.impl.ExtendedFixedFormatManagerImpl;
import com.github.vincenthung.fixedformatter4j.util.FixedFormatterUtil;

public class GenericEnumFormatter<T> implements FixedFormatter<T> {

	private FormatContext<T> context;

	public GenericEnumFormatter(FormatContext<T> context) {
		this.context = context;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T parse(String value, FormatInstructions instructions) throws FixedFormatException {

		if (!(instructions instanceof FixedFormatterInstruction)) {
			throw new FixedFormatException(String.format("Please use %s instead of %s for Enum formatting",
					ExtendedFixedFormatManagerImpl.class.getName(), FixedFormatManagerImpl.class.getName()));
		}

		FixedFormatterInstruction instrn = (FixedFormatterInstruction) instructions;
		FixedFormatEnum formatEnum = instrn.getFixedFormatEnum();
		if (formatEnum == null)
			throw new FixedFormatException(String.format("%s must be annotated to the getter for Generic Enum formatting",
					FixedFormatEnum.class.getName()));

		Class<T> dataType = context.getDataType();
		if (!dataType.isEnum())
			throw new FixedFormatException(String.format("%s only supports to parse Enum", getClass().getName()));

		if (formatEnum.useName()) {
			return (T) Enum.valueOf((Class) dataType, value);
		}

		// using Enum value
		Class<?> enumValueType;
		try {
			enumValueType = dataType.getMethod(formatEnum.getValueMethodName()).getReturnType();
		} catch (NoSuchMethodException | SecurityException e) {
			throw new FixedFormatException(String.format("Failed to obtain getValue Method [%s], please define for using enum value",
					formatEnum.getValueMethodName()));
		}

		if (enumValueType.equals(Void.TYPE))
			throw new FixedFormatException(String.format("The getValue Method [%s] returning void",
					formatEnum.getValueMethodName()));

		Method fromValueMethod;
		try {
			fromValueMethod = dataType.getMethod(formatEnum.fromValueMethodName(), enumValueType);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new FixedFormatException(String.format("Failed to obtain fromValue Method [%s], please define for using enum value",
					formatEnum.fromValueMethodName()));
		}

		FixedFormatter<?> formatter = FixedFormatterUtil.getFixedFormatterInstance(
				new FormatContext(0, enumValueType, GenericTypeFormatter.class));
		Object parsedData = formatter.parse(value, instructions);
		try {
			return (T) fromValueMethod.invoke(null, parsedData);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new FixedFormatException(String.format("Failed to invoke fromValue Method [%s] with parameter [%s (type: %s)]",
					formatEnum.fromValueMethodName(), parsedData, enumValueType.getName()));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String format(T value, FormatInstructions instructions) throws FixedFormatException {
		if (!(instructions instanceof FixedFormatterInstruction)) {
			throw new FixedFormatException(String.format("Please use %s instead of %s for Enum formatting",
					ExtendedFixedFormatManagerImpl.class.getName(), FixedFormatManagerImpl.class.getName()));
		}

		FixedFormatterInstruction instrn = (FixedFormatterInstruction) instructions;
		FixedFormatEnum formatEnum = instrn.getFixedFormatEnum();
		if (formatEnum == null)
			throw new FixedFormatException(String.format("%s must be annotated to the getter for Generic Enum formatting",
					FixedFormatEnum.class.getName()));

		Class<T> dataType = context.getDataType();
		if (!dataType.isEnum())
			throw new FixedFormatException(String.format("%s only supports to parse Enum", getClass().getName()));

		if (formatEnum.useName()) {
			return ((Enum<?>) value).name();
		}

		// using Enum value
		Method getValueMethod;
		try {
			getValueMethod = dataType.getMethod(formatEnum.getValueMethodName());
		} catch (NoSuchMethodException | SecurityException e) {
			throw new FixedFormatException(String.format("Failed to obtain getValue Method [%s], please define for using enum value",
					formatEnum.getValueMethodName()));
		}

		Object rawValue;
		try {
			rawValue = getValueMethod.invoke(value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new FixedFormatException(String.format("Failed to invoke getValue Method [%s (class: %s)] with enum [%s (type: %s)]",
					formatEnum.getValueMethodName(), dataType.getName(), value, value.getClass()));
		}

		FixedFormatter formatter = FixedFormatterUtil.getFixedFormatterInstance(
				new FormatContext(0, rawValue.getClass(), GenericTypeFormatter.class));

		return formatter.format(rawValue, instructions);

	}

}
