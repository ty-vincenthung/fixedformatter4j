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
