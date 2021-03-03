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
package com.github.vincenthung.fixedformatter4j.manager.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Fields;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatBoolean;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatNumber;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;
import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.FixedFormatUtil;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatContext;
import com.ancientprogramming.fixedformat4j.format.ParseException;
import com.github.vincenthung.fixedformatter4j.annotation.FixedFormatEnum;
import com.github.vincenthung.fixedformatter4j.annotation.FixedFormatList;
import com.github.vincenthung.fixedformatter4j.formatter.impl.FixedFormatterInstruction;
import com.github.vincenthung.fixedformatter4j.util.FixedFormatterUtil;

public class ExtendedFixedFormatManagerImpl implements FixedFormatManager {

	private static final Log Logger = LogFactory.getLog(ExtendedFixedFormatManagerImpl.class);

	public <T> T load(Class<T> fixedFormatRecordClass, String data) throws FixedFormatException {
		List<Pair<Method, Object>> setterValuePair = new ArrayList<Pair<Method, Object>>();

		getAndAssertRecordAnnotation(fixedFormatRecordClass);

		// Create record instance
		T instance = createInstance(fixedFormatRecordClass);

		// Process the setter-and-value pairs
		Method[] methods = fixedFormatRecordClass.getMethods();
		for (Method method: methods) {

			Field fieldAnnotation = method.getAnnotation(Field.class);
			Fields fieldsAnnotation = method.getAnnotation(Fields.class);

			if (fieldAnnotation != null) {
				Method setter = getSetter(fixedFormatRecordClass, method);
				setterValuePair.add(Pair.of(setter, readDataAccordingFieldAnnotation(fixedFormatRecordClass, data, method, fieldAnnotation)));
			} else if (fieldsAnnotation != null) {
				Method setter = getSetter(fixedFormatRecordClass, method);
				if (ArrayUtils.isEmpty(fieldsAnnotation.value()))
					throw new FixedFormatException(String.format("%s annotation must contain minimum one %s annotation",
							Fields.class.getName(), Field.class.getName()));
				setterValuePair.add(Pair.of(setter, readDataAccordingFieldAnnotation(fixedFormatRecordClass, data, method, fieldsAnnotation.value()[0])));
			}
		}

		for (Pair<Method, Object> pair : setterValuePair) {
			Method setter = pair.getLeft();
			Object value = pair.getRight();
			try {
				setter.invoke(instance, value);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new FixedFormatException(String.format("could not invoke method %s.%s(%s) with param %s",
						fixedFormatRecordClass.getName(), setter.getName(), setter.getParameters()[0].getName(), value), e);
			}
		}

		return instance;
	}

	protected <T> Object readDataAccordingFieldAnnotation(Class<T> clazz, String data, Method method, Field fieldAnno) throws ParseException {
		Class<?> dataType = getDataType(method);

		FormatContext<?> context = getFormatContext(dataType, fieldAnno);
		FixedFormatter<?> formatter = FixedFormatterUtil.getFixedFormatterInstance(context);
		FixedFormatterInstruction instruction = getFixedFormatterInstruction(method, fieldAnno);

		String dataToParse = FixedFormatUtil.fetchData(data, instruction, context);
		Object parsedData;
		try {
			parsedData = formatter.parse(dataToParse, instruction);
		} catch (RuntimeException e) {
			throw new ParseException(data, dataToParse, clazz, method, context, instruction, e);
		}
		if (Logger.isDebugEnabled()) {
			Logger.debug("the parsed data[" + parsedData + "]");
		}
		return parsedData;
	}

	protected <T> Method getSetter(Class<T> fixedFormatRecordClass, Method method) throws FixedFormatException {
		Class<?> dataType = getDataType(method);
		String setterName = "set" + FixedFormatterUtil.stripMethodPrefix(method.getName());
		try {
			return fixedFormatRecordClass.getMethod(setterName, dataType);
		} catch (NoSuchMethodException e) {
			throw new FixedFormatException(String.format("setter method named %s.%s(%s) does not exist",
					fixedFormatRecordClass.getName(), setterName, dataType));
		}
	}

	protected <T> T createInstance(Class<T> fixedFormatRecordClass) throws FixedFormatException {
		// Referencing to the original author(s)
		try {
			return fixedFormatRecordClass.getDeclaredConstructor().newInstance();
		} catch (NoSuchMethodException e) {
			// Probably inner class
			Class<?> declaringClass = fixedFormatRecordClass.getDeclaringClass();
			if (declaringClass != null) {
				try {
					Object declaringClassInstance;
					try {
						declaringClassInstance = declaringClass.getDeclaredConstructor().newInstance();
					} catch (NoSuchMethodException dex) {
						throw new FixedFormatException(
								String.format("Trying to create instance of innerclass %s, "
										+ "but the declaring class %s is missing a default constructor which is nessesary to be loaded through %s",
										fixedFormatRecordClass.getName(), declaringClass.getName(), getClass().getName()));
					} catch (Exception de) {
						throw new FixedFormatException(
								String.format("unable to create instance of declaring class %s, which is needed to instansiate %s",
										declaringClass.getName(), fixedFormatRecordClass.getName()), de);
					}
					return fixedFormatRecordClass.getDeclaredConstructor(declaringClass).newInstance(declaringClassInstance);
				} catch (FixedFormatException ex) {
					throw ex;
		        } catch (NoSuchMethodException ex) {
		        	throw new FixedFormatException(
		        			String.format("%s is missing a default constructor which is nessesary to be loaded through %s",
		        					fixedFormatRecordClass.getName(), getClass().getName()));
		        } catch (Exception ex) {
					throw new FixedFormatException(String.format("unable to create instance of %s", fixedFormatRecordClass.getName()), e);
				}
			} else
				throw new FixedFormatException(String.format("%s is missing a default constructor which is nessesary to be loaded through %s",
						fixedFormatRecordClass.getName(), getClass().getName()));

		} catch (FixedFormatException e) {
			throw e;
		} catch (Exception e) {
			throw new FixedFormatException(String.format("unable to create instance of %s", fixedFormatRecordClass.getName()), e);
	    }
	}

	public <T> String export(T fixedFormatRecord) throws FixedFormatException {
		return export("", fixedFormatRecord);
	}

	public <T> String export(String existingData, T fixedFormatRecord) throws FixedFormatException {
		StringBuffer buffer = new StringBuffer(existingData);

		Record record = getAndAssertRecordAnnotation(fixedFormatRecord.getClass());

		// exportedData = {offset: formatted data}
		Map<Integer, String> exportedData = getExportedDataMap(fixedFormatRecord);

		exportedData.forEach((offset, formattedData) -> formatData(buffer, record.paddingChar(), offset, formattedData));

		if (record.length() > -1) {
			if (buffer.length() < record.length())
				buffer.append(StringUtils.leftPad("", record.length() - buffer.length(), record.paddingChar()));
		}

		return buffer.toString();
	}

	protected <T> Map<Integer, String> getExportedDataMap(T fixedFormatRecord) throws FixedFormatException {
		Map<Integer, String> exportedData = new TreeMap<Integer, String>();

		Method[] methods = fixedFormatRecord.getClass().getMethods();
		for (Method method : methods) {
			Field fieldAnnotation = method.getAnnotation(Field.class);
			Fields fieldsAnnotation = method.getAnnotation(Fields.class);
			if (fieldAnnotation != null) {
				String formattedData = exportDataAccordingFieldAnnotation(fixedFormatRecord, method, fieldAnnotation);
				exportedData.put(fieldAnnotation.offset(), formattedData);
			} else if (fieldsAnnotation != null) {
				for (Field field : fieldsAnnotation.value()) {
					String formattedData = exportDataAccordingFieldAnnotation(fixedFormatRecord, method, fieldAnnotation);
					exportedData.put(field.offset(), formattedData);
				}
			}
		}

		return exportedData;
	}

	protected void formatData(StringBuffer buffer, char paddingChar, Integer offset, String formattedData) {
		int zeroBasedOffset = offset - 1;
		int length = formattedData.length();
		if (buffer.length() < zeroBasedOffset + length)
			buffer.append(StringUtils.leftPad("", zeroBasedOffset + length - buffer.length(), paddingChar));
		buffer.replace(zeroBasedOffset, zeroBasedOffset + length, formattedData);
	}

	protected Class<?> getDataType(Method method) {
		if (allowedAnnotatedMethod(method))
			return method.getReturnType();
		else
			throw new FixedFormatException(
					String.format("Cannot annotate method %s, with %s annotation. %s annotations must be placed on methods starting with 'get' or 'is'",
							method.getName(), Field.class.getName(), Field.class.getName()));

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T> String exportDataAccordingFieldAnnotation(T fixedFormatRecord, Method method, Field fieldAnno) {

		Class<?> dataType = getDataType(method);

		FormatContext<?> context = getFormatContext(dataType, fieldAnno);
		FixedFormatter formatter = FixedFormatterUtil.getFixedFormatterInstance(context);
		FixedFormatterInstruction instruction = getFixedFormatterInstruction(method, fieldAnno);

		Object attr;
		try {
			attr = method.invoke(fixedFormatRecord);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new FixedFormatException(String.format("could not invoke method %s.%s(%s)",
					fixedFormatRecord.getClass().getName(), method.getName(), dataType), e);
		}
		String formattedData = formatter.format(attr, instruction);
		if (Logger.isDebugEnabled())
			Logger.debug(String.format("exported %s ", formattedData));

		return formattedData;
	}

	protected <T> Record getAndAssertRecordAnnotation(Class<T> fixedFormatRecordClass) {
		Record recordAnno = fixedFormatRecordClass.getAnnotation(Record.class);
		if (recordAnno == null) {
			throw new FixedFormatException(String.format("%s has to be marked with the record annotation to be loaded", fixedFormatRecordClass.getName()));
		}
		return recordAnno;
	}

	protected <K> FormatContext<K> getFormatContext(Class<K> dataType, Field fieldAnno) {
		return fieldAnno == null ? null : new FormatContext<K>(fieldAnno.offset(), dataType, fieldAnno.formatter());
	}

	protected FixedFormatterInstruction getFixedFormatterInstruction(Method method, Field fieldAnno) {
		return new FixedFormatterInstruction(
				fieldAnno.length(), fieldAnno.align(), fieldAnno.paddingChar(),
				method.getAnnotation(FixedFormatPattern.class),
				method.getAnnotation(FixedFormatBoolean.class),
				method.getAnnotation(FixedFormatNumber.class),
				method.getAnnotation(FixedFormatDecimal.class),
				method.getAnnotation(FixedFormatList.class),
				method.getAnnotation(FixedFormatEnum.class));
	}

	protected boolean allowedAnnotatedMethod(Method method) {
		String methodName = method.getName();
		if (methodName.startsWith("get") || methodName.startsWith("is"))
			return true;
		return false;
	}

}
