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
