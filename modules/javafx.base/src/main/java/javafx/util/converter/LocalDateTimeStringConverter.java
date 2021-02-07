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

import java.time.LocalDateTime;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalQuery;
import java.util.Locale;
import java.util.Objects;

/**
 * A {@code StringConverter} implementation for {@link LocalDateTime} values. Instances of this class are immutable.
 *
 * @see LocalDateStringConverter
 * @see LocalTimeStringConverter
 * @since JavaFX 8u40
 */
public class LocalDateTimeStringConverter extends BaseTemporalConverter<LocalDateTime> {

    /**
     * Create a {@code LocalDateTimeStringConverter} using a
     * default formatter and parser based on {@link IsoChronology},
     * {@link FormatStyle#SHORT} for both date and time, and the user's
     * {@link Locale}.
     *
     * <p>This converter ensures symmetry between the {@code toString()} and
     * {@code fromString()} methods. Many of the default locale based patterns used by
     * {@link DateTimeFormatter} will display only two digits for the year when
     * formatting to a string. This would cause a value like 1955 to be
     * displayed as 55, which in turn would be parsed back as 2055. This
     * converter modifies two-digit year patterns to always use four digits. The
     * input parsing is not affected, so two digit year values can still be
     * parsed as expected in these locales.</p>
     */
    public LocalDateTimeStringConverter() {
        this(DEFAULT_STYLE, DEFAULT_STYLE);
    }

    /**
     * Create a {@code LocalDateTimeStringConverter} using
     * a default formatter and parser based on {@link IsoChronology}, the
     * specified {@link FormatStyle} values for date and time, and the user's
     * {@link Locale}.
     *
     * @param dateStyle the {@code FormatStyle} that will be used by the default
     * formatter and parser for the date. If {@code null} then {@link FormatStyle#SHORT}
     * will be used.
     * @param timeStyle the {@code FormatStyle} that will be used by the default
     * formatter and parser for the time. If {@code null} then {@code FormatStyle.SHORT}
     * will be used.
     */
    public LocalDateTimeStringConverter(FormatStyle dateStyle, FormatStyle timeStyle) {
        this(dateStyle, timeStyle, DEFAULT_LOCALE, DEFAULT_CHRONOLOGY);
    }

    /**
     * Create a {@code LocalDateTimeStringConverter} using a
     * default formatter and parser, which will be based on the supplied
     * {@link FormatStyle}s, {@link Locale}, and {@link Chronology}.
     *
     * @param dateStyle the {@code FormatStyle} that will be used by the default
     * formatter and parser for the date. If {@code null} then {@link FormatStyle#SHORT}
     * will be used.
     * @param timeStyle the {@code FormatStyle} that will be used by the default
     * formatter and parser for the time. If {@code null} then {@code FormatStyle.SHORT}
     * will be used.
     * @param locale the {@code Locale} that will be used by the
     * default formatter and parser. If {@code null} then
     * {@code Locale.getDefault(Locale.Category.FORMAT)} will be used.
     * @param chronology the {@code Chronology} that will be used by the default
     * formatter and parser. If {@code null} then {@link IsoChronology#INSTANCE} will be
     * used.
     */
    public LocalDateTimeStringConverter(FormatStyle dateStyle, FormatStyle timeStyle, Locale locale, Chronology chronology) {
        super(Objects.requireNonNullElse(dateStyle, DEFAULT_STYLE),
              Objects.requireNonNullElse(dateStyle, DEFAULT_STYLE),
              locale, chronology);
    }

    /**
     * Create a {@code LocalDateTimeStringConverter} using
     * the supplied formatter and parser.
     *
     * <p>For example, to use a fixed pattern for converting both ways:</p>
     * <blockquote><pre>
     * String pattern = "yyyy-MM-dd HH:mm";
     * DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
     * StringConverter&lt;LocalDateTime&gt; converter =
     *     DateTimeStringConverter.getLocalDateTimeConverter(formatter, null);
     * </pre></blockquote>
     *
     * Note that the formatter and parser can be created to handle non-default
     * {@link Locale} and {@link Chronology} as needed.
     *
     * @param formatter an instance of {@link DateTimeFormatter} which will be
     * used for formatting by the {@code toString()} method. If {@code null} then a default
     * formatter will be used.
     * @param parser an instance of {@code DateTimeFormatter} which will be used
     * for parsing by the {@code fromString()} method. This can be identical to
     * formatter. If {@code null} then formatter will be used, and if that is also {@code null},
     * then a default parser will be used.
     */
    public LocalDateTimeStringConverter(DateTimeFormatter formatter, DateTimeFormatter parser) {
        super(formatter, parser, DEFAULT_STYLE, DEFAULT_STYLE);
    }

    @Override
    protected DateTimeFormatter getLocalizedFormatter(FormatStyle dateStyle, FormatStyle timeStyle) {
        return DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle);
    }

    @Override
    protected TemporalQuery<LocalDateTime> getTemporalQuery() {
        return LocalDateTime::from;
    }
}
