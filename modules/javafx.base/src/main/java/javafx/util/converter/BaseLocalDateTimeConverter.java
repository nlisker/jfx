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

import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DecimalStyle;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalQuery;
import java.util.Locale;
import java.util.Objects;

import javafx.util.StringConverter;

abstract class BaseTemporalConverter<T extends Temporal> extends BaseStringConverter<T> {

    protected static final FormatStyle DEFAULT_STYLE = FormatStyle.SHORT;
    protected static final Chronology DEFAULT_CHRONOLOGY = IsoChronology.INSTANCE;
    protected static final Locale DEFAULT_LOCALE = Locale.getDefault(Locale.Category.FORMAT);

    private final DateTimeFormatter formatter;
    private final DateTimeFormatter parser;

    protected BaseTemporalConverter(FormatStyle dateStyle, FormatStyle timeStyle, Locale locale, Chronology chronology) {
        locale = Objects.requireNonNullElse(locale, DEFAULT_LOCALE);
        chronology = Objects.requireNonNullElse(chronology, DEFAULT_CHRONOLOGY);
        parser = getDefaultParser(dateStyle, timeStyle, locale, chronology);
        formatter = getDefaultFormatter(dateStyle, timeStyle, locale, chronology);
    }

    protected BaseTemporalConverter(DateTimeFormatter formatter, DateTimeFormatter parser, FormatStyle dateStyle,
                                    FormatStyle timeStyle) {
        this.formatter = Objects.requireNonNullElse(formatter,
                getDefaultFormatter(dateStyle, timeStyle, DEFAULT_LOCALE, DEFAULT_CHRONOLOGY));
        this.parser = Objects.requireNonNullElse(parser, Objects.requireNonNullElse(formatter,
                getDefaultParser(dateStyle, timeStyle, DEFAULT_LOCALE, DEFAULT_CHRONOLOGY)));
    }

    private DateTimeFormatter getDefaultParser(FormatStyle dateStyle, FormatStyle timeStyle, Locale locale,
                                               Chronology chronology) {
        String pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(dateStyle, timeStyle, chronology, locale);
        return new DateTimeFormatterBuilder().parseLenient()
                                             .appendPattern(pattern)
                                             .toFormatter()
                                             .withChronology(chronology)
                                             .withDecimalStyle(DecimalStyle.of(locale));
    }

    /**
     * <p>Return a default <code>DateTimeFormatter</code> instance to use for formatting
     * and parsing in this {@link StringConverter}.</p>
     */
    private DateTimeFormatter getDefaultFormatter(FormatStyle dateStyle, FormatStyle timeStyle, Locale locale,
                                                  Chronology chronology) {
        var formatter = getLocalizedFormatter(dateStyle, timeStyle)
                            .withLocale(locale)
                            .withChronology(chronology)
                            .withDecimalStyle(DecimalStyle.of(locale));
        if (dateStyle != null) {
            formatter = fixFourDigitYear(formatter, dateStyle, timeStyle, chronology, locale);
        }
        return formatter;
    }

    private DateTimeFormatter fixFourDigitYear(DateTimeFormatter formatter, FormatStyle dateStyle, FormatStyle timeStyle,
                                               Chronology chronology, Locale locale) {
        String pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(dateStyle, timeStyle, chronology, locale);
        if (pattern.contains("yy") && !pattern.contains("yyy")) {
            // Modify pattern to show four-digit year, including leading zeros.
            String newPattern = pattern.replace("yy", "yyyy");
            return DateTimeFormatter.ofPattern(newPattern).withDecimalStyle(DecimalStyle.of(locale));
        }
        return formatter;
    }

    protected abstract DateTimeFormatter getLocalizedFormatter(FormatStyle dateStyle, FormatStyle timeStyle);

    @Override
    String toStringFromNonNull(T value) {
        return formatter.format(value);
    }

    @Override
    T fromNonEmptyString(String string) {
        return parser.parse(string, getTemporalQuery());
    }

    protected abstract TemporalQuery<T> getTemporalQuery();
}
