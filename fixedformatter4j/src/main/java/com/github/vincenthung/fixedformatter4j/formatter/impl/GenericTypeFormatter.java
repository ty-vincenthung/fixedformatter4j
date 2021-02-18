package com.github.vincenthung.fixedformatter4j.formatter.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatContext;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;
import com.github.vincenthung.fixedformatter4j.formatter.standard.impl.BigDecimalFormatter;
import com.github.vincenthung.fixedformatter4j.formatter.standard.impl.BooleanFormatter;
import com.github.vincenthung.fixedformatter4j.formatter.standard.impl.CharacterFormatter;
import com.github.vincenthung.fixedformatter4j.formatter.standard.impl.DoubleFormatter;
import com.github.vincenthung.fixedformatter4j.formatter.standard.impl.FloatFormatter;
import com.github.vincenthung.fixedformatter4j.formatter.standard.impl.IntegerFormatter;
import com.github.vincenthung.fixedformatter4j.formatter.standard.impl.LongFormatter;
import com.github.vincenthung.fixedformatter4j.formatter.standard.impl.ShortFormatter;
import com.github.vincenthung.fixedformatter4j.formatter.standard.impl.SqlDateFormatter;
import com.github.vincenthung.fixedformatter4j.formatter.standard.impl.StringFormatter;
import com.github.vincenthung.fixedformatter4j.formatter.standard.impl.UtilDateFormatter;

/**
 * Generic formatter of formatting java standard library classes with null handling.
 * {@link String}, {@link Integer}, {@link Long}, {@link Short}, {@link Date},
 * {@link Character}, {@link Boolean}, {@link Double}, {@link Float} and {@link BigDecimal}
 *
 */
public class GenericTypeFormatter<T> implements FixedFormatter<T> {

	/**
	 * Replaceable for customized type formatter
	 */
	public static Map<Class<? extends Serializable>, Class<? extends FixedFormatter<?>>> KNOWN_FORMATTERS =
			new HashMap<Class<? extends Serializable>, Class<? extends FixedFormatter<?>>>();

	static {
		// Boolean
		KNOWN_FORMATTERS.put(boolean.class, BooleanFormatter.class);
		KNOWN_FORMATTERS.put(Boolean.class, BooleanFormatter.class);

		// String, Character
		KNOWN_FORMATTERS.put(String.class, StringFormatter.class);
		KNOWN_FORMATTERS.put(char.class, CharacterFormatter.class);
		KNOWN_FORMATTERS.put(Character.class, CharacterFormatter.class);

		// Integer, Short, Long
		KNOWN_FORMATTERS.put(int.class, IntegerFormatter.class);
		KNOWN_FORMATTERS.put(Integer.class, IntegerFormatter.class);
		KNOWN_FORMATTERS.put(long.class, LongFormatter.class);
		KNOWN_FORMATTERS.put(Long.class, LongFormatter.class);
		KNOWN_FORMATTERS.put(short.class, ShortFormatter.class);
		KNOWN_FORMATTERS.put(Short.class, ShortFormatter.class);

		// Double, Float, BigDecimal
		KNOWN_FORMATTERS.put(double.class, DoubleFormatter.class);
		KNOWN_FORMATTERS.put(Double.class, DoubleFormatter.class);
		KNOWN_FORMATTERS.put(float.class, FloatFormatter.class);
		KNOWN_FORMATTERS.put(Float.class, FloatFormatter.class);
		KNOWN_FORMATTERS.put(BigDecimal.class,  BigDecimalFormatter.class);

		// java.sql.Date, java.util.Date
		KNOWN_FORMATTERS.put(java.util.Date.class, UtilDateFormatter.class);
		KNOWN_FORMATTERS.put(java.sql.Date.class, SqlDateFormatter.class);
	}

	private FormatContext<T> context;

	public GenericTypeFormatter(FormatContext<T> context) {
		this.context = context;
	}

	@Override
	public T parse(String value, FormatInstructions instructions) throws FixedFormatException {
		FixedFormatter<T> formatter = getFixedFormatterInstanceByClass(context.getDataType());
		return formatter.parse(value, instructions);
	}

	@Override
	public String format(T value, FormatInstructions instructions) throws FixedFormatException {
		FixedFormatter<T> formatter = getFixedFormatterInstanceByClass(context.getDataType());
		return formatter.format(value, instructions);
	}

	@SuppressWarnings("unchecked")
	protected FixedFormatter<T> getFixedFormatterInstanceByClass(Class<?> dataType) {
		Class<? extends FixedFormatter<?>> formatterClass = KNOWN_FORMATTERS.get(dataType);

		if (formatterClass != null) {
			try {
				return (FixedFormatter<T>) formatterClass.getConstructor().newInstance();
			} catch (NoSuchMethodException e) {
				throw new FixedFormatException("Could not create instance of[" + formatterClass.getName() + "] because no default constructor exists");
			} catch (Exception e) {
				throw new FixedFormatException("Could not create instance of[" + formatterClass.getName() + "]", e);
			}
		} else {
			throw new FixedFormatException(String.format("%s cannot handle dataType[%s]. Specify / Add your own custom FixedFormatter for this datatype.",
					getClass().getName(), dataType.getName()));
		}
	}

}
