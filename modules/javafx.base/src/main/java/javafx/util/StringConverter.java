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

package javafx.util;

/**
 * Defines conversion behaviors between {@code String} and other types. Each subclass defines a formatting (type to
 * string) and a parsing (string to type) behaviors. Subclasses exist for all primitive wrappers, including specialized
 * formatters for numbers, and dates and times.
 * 
 * @implNote Most implementations opt for the following behaviors: when formatting, a {@code null} reference gives an empty
 * string, otherwise the type's {@code toString} is used if it is suitable. When parsing, a {@code null} or empty
 * {@code String} gives a {@code null} reference. This behavior is neither guaranteed nor required from implementing
 * classes.
 *
 * @param <T> the type associated with the string conversions
 *
 * @since JavaFX 2.0
 */
public abstract class StringConverter<T> {

    /**
     * Creates a default {@code StringConverter}.
     */
    public StringConverter() {
    }

    /**
    * Converts an object to a string form. The format of the returned string is defined by the specific converter.
    *
    * @param object the object of type {@code T} to convert
    * @return a string representation of the object passed in
    */
    public abstract String toString(T object);

    /**
    * Converts a string to an object. The parsing mechanism is defined by the specific converter.
    *
    * @param string the {@code String} to convert
    * @return an object of type {@code T} created from the string passed in.
    */
    public abstract T fromString(String string);
}
