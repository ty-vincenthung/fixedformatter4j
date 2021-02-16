package com.github.vincenthung.fixedformatter4j.formatter.impl;

import java.util.ArrayList;
import java.util.List;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatContext;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;
import com.github.vincenthung.fixedformatter4j.annotation.FixedFormatList;
import com.github.vincenthung.fixedformatter4j.util.FixedFormatterUtil;

public class GenericListFormatter<T> implements FixedFormatter<List<T>> {

	private FormatContext<List<T>> context;

	public GenericListFormatter(FormatContext<List<T>> context) {
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	public List<T> parse(String value, FormatInstructions instructions) throws FixedFormatException {

		System.out.println(instructions);
		if (!(instructions instanceof FixedFormatterInstruction))
			throw new FixedFormatException("Please use ExtendedFixedFormatManagerImpl instead of FixedFormatManagerImpl for List parsing");

		FixedFormatterInstruction instrn = (FixedFormatterInstruction) instructions;
		FixedFormatList formatList = instrn.getFixedFormatList();
		if (formatList == null)
			throw new FixedFormatException(String.format("%s must be annotated to the getter Generic List parsing", FixedFormatList.class.getName()));

		if (formatList.eachLength() <= 0)
			throw new FixedFormatException("The eachLength must be greater than 0 for list parsing");

		if (instrn.getLength() % formatList.eachLength() != 0)
			throw new FixedFormatException("The length is not a multiple of eachLength for list parsing");

		List<T> result = new ArrayList<T>();
		FixedFormatter<?> formatter = getElementFormatter(new FormatContext<T>(0, (Class<T>) formatList.elementClass(), formatList.elementFormatter()));
		for (int offset = 0; offset < instrn.getLength(); offset += formatList.eachLength()) {
			result.add((T) formatter.parse(value.substring(offset, offset + formatList.eachLength()),
					new FormatInstructions(0, formatList.align(), formatList.paddingChar(),
							instrn.getFixedFormatPatternData(), instrn.getFixedFormatBooleanData(),
							instrn.getFixedFormatNumberData(), instrn.getFixedFormatDecimalData())));

		}

		return result;
	}

	public String format(List<T> value, FormatInstructions instructions) throws FixedFormatException {

		if (!(instructions instanceof FixedFormatterInstruction)) {
			throw new FixedFormatException("Please use ExtendedFixedFormatManagerImpl instead of FixedFormatManagerImpl for List formatting");
		}
		FixedFormatterInstruction instrn = (FixedFormatterInstruction) instructions;

		return null;
	}

	protected FixedFormatter<?> getElementFormatter(FormatContext<T> context) {
		return FixedFormatterUtil.getFixedFormatterInstance(context);
	}

}
