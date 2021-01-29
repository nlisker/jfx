/*
 * Copyright (c) 2014, 2021, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package javafx.util.converter;

import java.time.LocalTime;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DecimalStyle;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.Objects;

import javafx.util.StringConverter;

/**
 * A {@code StringConverter} implementation for {@link LocalTime} values. Instances of this class are immutable.
 *
 * @see LocalDateStringConverter
 * @see LocalDateTimeStringConverter
 * @since JavaFX 8u40
 */
public class LocalTimeStringConverter extends BaseStringConverter<LocalTime> {

    private static final FormatStyle DEFAULT_STYLE = FormatStyle.SHORT;
    private static final Chronology DEFAULT_CHRONOLOGY = IsoChronology.INSTANCE;
    private static Locale defaultLocale() {
        return Locale.getDefault(Locale.Category.FORMAT);
    }

    private final DateTimeFormatter formatter;
    private final DateTimeFormatter parser;

    /**
     * Create a {@code LocalTimeStringConverter} using a
     * default formatter and parser with {@link FormatStyle#SHORT}, and the
     * user's {@link Locale}.
     */
    public LocalTimeStringConverter() {
        this(DEFAULT_STYLE);
    }

    /**
     * Create a {@code LocalTimeStringConverter} using a
     * default formatter and parser with the specified {@link FormatStyle} and
     * based on the user's {@link Locale}.
     *
     * @param timeStyle the {@code FormatStyle} that will be used by the default
     * formatter and parser. If null then {@link FormatStyle#SHORT} will be used.
     */
    public LocalTimeStringConverter(FormatStyle timeStyle) {
        this(timeStyle, defaultLocale());
    }

    /**
     * Create a {@code LocalTimeStringConverter} using a
     * default formatter and parser with the specified {@link FormatStyle}
     * and {@link Locale}.
     *
     * @param timeStyle The {@link FormatStyle} that will be used by the default
     * formatter and parser. If {@code null} then {@link FormatStyle#SHORT} will be used.
     * @param locale the {@link Locale} that will be used by the default
     * formatter and parser. If {@code null} then
     * {@code Locale.getDefault(Locale.Category.FORMAT)} will be used.
     */
    public LocalTimeStringConverter(FormatStyle timeStyle, Locale locale) {
        timeStyle = Objects.requireNonNullElse(timeStyle, DEFAULT_STYLE);
        locale = Objects.requireNonNullElseGet(locale, () -> defaultLocale());
        parser = getDefaultParser(timeStyle, locale);
        formatter = getDefaultFormatter(timeStyle, locale);
    }

    /**
     * Create a {@code LocalTimeStringConverter} using the
     * supplied formatter and parser, which are responsible for
     * choosing the desired {@link Locale}.
     *
     * <p>For example, a fixed pattern can be used for converting both ways:</p>
     * <blockquote><pre>
     * String pattern = "HH:mm:ss";
     * DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
     * StringConverter&lt;LocalTime&gt; converter =
     *     DateTimeStringConverter.getLocalTimeConverter(formatter, null);
     * </pre></blockquote>
     *
     * @param formatter an instance of {@link DateTimeFormatter} which
     * will be used for formatting by the {@code toString()} method. If {@code null}
     * then a default formatter will be used.
     * @param parser an instance of {@link DateTimeFormatter} which
     * will be used for parsing by the {@code fromString()} method. This can
     * be identical to formatter. If {@code null}, then formatter will be
     * used, and if that is also {@code null}, then a default parser will be
     * used.
     */
    public LocalTimeStringConverter(DateTimeFormatter formatter, DateTimeFormatter parser) {
        this.formatter = formatter == null ? getDefaultFormatter(DEFAULT_STYLE, defaultLocale()) : formatter;
        this.parser = parser == null ?
                (formatter == null ? getDefaultParser(DEFAULT_STYLE, defaultLocale()) : this.formatter) : parser;
    }

    private DateTimeFormatter getDefaultParser(FormatStyle timeStyle, Locale locale) {
        String pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(null, timeStyle, DEFAULT_CHRONOLOGY, locale);
        return new DateTimeFormatterBuilder().parseLenient()
                                             .appendPattern(pattern)
                                             .toFormatter()
                                             .withDecimalStyle(DecimalStyle.of(locale));
    }

    /**
     * <p>Return a default <code>DateTimeFormatter</code> instance to use for formatting
     * and parsing in this {@link StringConverter}.</p>
     */
    private DateTimeFormatter getDefaultFormatter(FormatStyle timeStyle, Locale locale) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(timeStyle);
        return formatter.withLocale(locale)
                        .withDecimalStyle(DecimalStyle.of(locale));
    }

    @Override
    LocalTime fromNonEmptyString(String string) {
        TemporalAccessor temporal = parser.parse(string);
        return LocalTime.from(temporal);
    }

    @Override
    String toStringFromNonNull(LocalTime value) {
        return formatter.format(value);
    }
}
