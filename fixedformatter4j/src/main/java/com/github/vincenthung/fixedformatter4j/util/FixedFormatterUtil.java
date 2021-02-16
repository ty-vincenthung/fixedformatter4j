package com.github.vincenthung.fixedformatter4j.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ancientprogramming.fixedformat4j.annotation.FixedFormatBoolean;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatNumber;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.format.FixedFormatUtil;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.FormatContext;
import com.ancientprogramming.fixedformat4j.format.data.FixedFormatBooleanData;
import com.ancientprogramming.fixedformat4j.format.data.FixedFormatDecimalData;
import com.ancientprogramming.fixedformat4j.format.data.FixedFormatNumberData;
import com.ancientprogramming.fixedformat4j.format.data.FixedFormatPatternData;

public class FixedFormatterUtil {

	public static FixedFormatter<?> getFixedFormatterInstance(FormatContext<?> context) {
		return context == null ? FixedFormatUtil.getFixedFormatterInstance(null, null)
				: FixedFormatUtil.getFixedFormatterInstance(context.getFormatter(), context);
	}

	public static String stripMethodPrefix(String methodName) {
		Pattern pattern = Pattern.compile("^(?:(?:get)|(?:is)|(?:set))(.*)$");
		Matcher matcher = pattern.matcher(methodName);
		if (matcher.matches())
			return matcher.group(1);
		return methodName;
	}

	public static FixedFormatPatternData convert(FixedFormatPattern anno) {
		return anno == null ? FixedFormatPatternData.DEFAULT : new FixedFormatPatternData(anno.value());
	}

	public static FixedFormatBooleanData convert(FixedFormatBoolean anno) {
		return anno == null ? FixedFormatBooleanData.DEFAULT
				: new FixedFormatBooleanData(anno.trueValue(), anno.falseValue());
	}

	public static FixedFormatNumberData convert(FixedFormatNumber anno) {
		return anno == null ? FixedFormatNumberData.DEFAULT
				: new FixedFormatNumberData(anno.sign(), anno.positiveSign(), anno.negativeSign());
	}

	public static FixedFormatDecimalData convert(FixedFormatDecimal anno) {
		return anno == null ? FixedFormatDecimalData.DEFAULT
				: new FixedFormatDecimalData(anno.decimals(), anno.useDecimalDelimiter(), anno.decimalDelimiter());
	}
}
