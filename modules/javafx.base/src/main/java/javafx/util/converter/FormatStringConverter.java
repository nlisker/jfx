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

import java.text.*;
import javafx.beans.NamedArg;
import javafx.util.StringConverter;

/**
 * A {@code StringConverter} implementation that can use a {@link Format}
 * instance to convert arbitrary types to and from a string.
 *
 * @param <T> the type converted to/from a string
 *
 * @since JavaFX 2.2
 */
public class FormatStringConverter<T> extends BaseStringConverter<T> {

    private final Format format;

    /**
     * Creates a {@code StringConverter} for arbitrary types using the given {@code Format}
     *
     * @param format the {@code Format} instance for formatting and parsing in this {@code StringConverter}
     */
    public FormatStringConverter(@NamedArg("format") Format format) {
        this.format = format;
    }

    @Override
    T fromNonEmptyString(String string) {
        var pos = new ParsePosition(0);
        @SuppressWarnings("unchecked")
        T result = (T) format.parseObject(string, pos);
        if (pos.getIndex() != string.length()) {
            throw new RuntimeException("Parsed string not according to the format");
        }
        return result;
    }

    @Override
    String toStringFromNonNull(T object) {
        return format.format(object);
    }

    /**
     * Returns the {@code Format} instance uses for formatting and parsing in this {@code StringConverter}.
     *
     * @return the {@code Format} instance for formatting and parsing in this {@code StringConverter}
     */
    protected Format getFormat() {
        return format;
    }
}
