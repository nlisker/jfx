/*
 * Copyright (c) 2010, 2021, Oracle and/or its affiliates. All rights reserved.
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javafx.util.StringConverter;

/**
 * A {@code StringConverter} implementation for {@link Date} values that represent dates and times. Instances of this
 * class are immutable.
 * <p>
 * Note that using {@code Date} is not recommended in JDK versions where {@link java.time.LocalDateTime} is available,
 * in which case {@link LocalDateTimeStringConverter} should be used.
 *
 * @since JavaFX 2.1
 */
public class DateTimeStringConverter extends BaseStringConverter<Date> {

    protected final Locale locale;
    protected final String pattern;
    protected final DateFormat dateFormat;

    /**
     * @since JavaFX 8u40
     */
    protected final int dateStyle;

    /**
     * @since JavaFX 8u40
     */
    protected final int timeStyle;

    /**
     * Create a {@link StringConverter} for {@link Date} values, using
     * {@link DateFormat#DEFAULT} styles for date and time.
     */
    public DateTimeStringConverter() {
        this(null, null, null, DateFormat.DEFAULT, DateFormat.DEFAULT);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using specified
     * {@link DateFormat} styles for date and time.
     *
     * @param dateStyle the given formatting style. For example,
     * {@link DateFormat#SHORT} for "M/d/yy" in the US locale.
     * @param timeStyle the given formatting style. For example,
     * {@link DateFormat#SHORT} for "h:mm a" in the US locale.
     *
     * @since JavaFX 8u40
     */
    public DateTimeStringConverter(int dateStyle, int timeStyle) {
        this(null, null, null, dateStyle, timeStyle);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using the
     * specified locale and {@link DateFormat#DEFAULT} styles for date and time.
     *
     * @param locale the given locale.
     */
    public DateTimeStringConverter(Locale locale) {
        this(locale, null, null, DateFormat.DEFAULT, DateFormat.DEFAULT);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using specified
     * locale and {@link DateFormat} styles for date and time.
     *
     * @param locale the given locale.
     * @param dateStyle the given formatting style. For example,
     * {@link DateFormat#SHORT} for "M/d/yy" in the US locale.
     * @param timeStyle the given formatting style. For example,
     * {@link DateFormat#SHORT} for "h:mm a" in the US locale.
     *
     * @since JavaFX 8u40
     */
    public DateTimeStringConverter(Locale locale, int dateStyle, int timeStyle) {
        this(locale, null, null, dateStyle, timeStyle);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using the
     * specified pattern.
     *
     * @param pattern the pattern describing the date and time format.
     */
    public DateTimeStringConverter(String pattern) {
        this(null, pattern, null, DateFormat.DEFAULT, DateFormat.DEFAULT);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using the
     * specified locale and pattern.
     *
     * @param locale the given locale.
     * @param pattern the pattern describing the date and time format.
     */
    public DateTimeStringConverter(Locale locale, String pattern) {
        this(locale, pattern, null, DateFormat.DEFAULT, DateFormat.DEFAULT);
    }

    /**
     * Create a {@link StringConverter} for {@link Date} values, using the
     * specified {@link DateFormat} formatter.
     *
     * @param dateFormat the {@link DateFormat} to be used for formatting and
     * parsing.
     */
    public DateTimeStringConverter(DateFormat dateFormat) {
        this(null, null, dateFormat, DateFormat.DEFAULT, DateFormat.DEFAULT);
    }

    DateTimeStringConverter(Locale locale, String pattern, DateFormat dateFormat, int dateStyle, int timeStyle) {
        this.locale = (locale != null) ? locale : Locale.getDefault(Locale.Category.FORMAT);
        this.pattern = pattern;
        this.dateStyle = dateStyle;
        this.timeStyle = timeStyle;

        if (dateFormat != null) {
            this.dateFormat = dateFormat;
            return;
        }
        this.dateFormat = pattern != null ? new SimpleDateFormat(pattern, locale) : getSpecialziedDataFormat();
        this.dateFormat.setLenient(false);
    }

    // treat as protected
    DateFormat getSpecialziedDataFormat() {
        return DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
    }

    @Override
    Date fromNonEmptyString(String string) {
        try {
            return dateFormat.parse(string);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    String toStringFromNonNull(Date data) {
        return dateFormat.format(data);
    }

    /**
     * Returns the {@code DateFormat} used for formatting and parsing in this {@code DateTimeStringConverter}.
     *
     * @return the {@code DateFormat} used for formatting and parsing in this {@code DateTimeStringConverter}
     */
    protected DateFormat getDateFormat() {
        return dateFormat;
    }
}
