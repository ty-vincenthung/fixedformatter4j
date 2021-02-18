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
package com.github.vincenthung.fixedformatter4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.github.vincenthung.fixedformatter4j.formatter.impl.GenericTypeFormatter;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface FixedFormatList {

	/**
	 * The length of the each formatted field in list
	 * @return the length as an int
	 */
	int eachLength();

	/**
	 * @return The direction of the padding of each in list. Defaults to {@link Align#LEFT}.
	 */
	Align align() default Align.LEFT;

	/**
	 * The character to pad with if the length is longer than the formatted data
	 * @return the padding character
	 */
	char paddingChar() default ' ';

	Class<?> elementClass();

	@SuppressWarnings("rawtypes")
	Class<? extends FixedFormatter> elementFormatter() default GenericTypeFormatter.class;

}
