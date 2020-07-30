/*
 * Copyright (c) 2010, 2015, Oracle and/or its affiliates. All rights reserved.
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

package test.javafx.util.converter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import javafx.util.converter.NumberStringConverter;
import javafx.util.converter.NumberStringConverterShim;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 */
public class NumberStringConverterTest {

    private NumberStringConverter converter;

    @Before
    public void setup() {
        converter = new NumberStringConverter();
    }

    /*********************************************************************
     * Test constructors
     ********************************************************************/

    @Test
    public void testDefaultConstructor() {
        var nsc = new NumberStringConverter();
        var numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        var cachedNumberFormat = NumberStringConverterShim.getNumberFormat(nsc);
        assertEquals(numberFormat, cachedNumberFormat);
    }

    @Test
    public void testConstructor_locale() {
        var nsc = new NumberStringConverter(Locale.CANADA);
        var numberFormat = NumberFormat.getNumberInstance(Locale.CANADA);
        var cachedNumberFormat = NumberStringConverterShim.getNumberFormat(nsc);
        assertEquals(numberFormat, cachedNumberFormat);
    }

    @Test
    public void testConstructor_pattern() {
        var nsc = new NumberStringConverter("#,##,###,####");
        var symbols = new DecimalFormatSymbols(Locale.getDefault());
        var numberFormat = new DecimalFormat("#,##,###,####", symbols);
        var cachedNumberFormat = NumberStringConverterShim.getNumberFormat(nsc);
        assertEquals(numberFormat, cachedNumberFormat);
        assertTrue(cachedNumberFormat instanceof DecimalFormat);
    }

    @Test
    public void testConstructor_locale_pattern() {
        var nsc = new NumberStringConverter(Locale.CANADA, "#,##,###,####");
        var symbols = new DecimalFormatSymbols(Locale.CANADA);
        var numberFormat = new DecimalFormat("#,##,###,####", symbols);
        var cachedNumberFormat = NumberStringConverterShim.getNumberFormat(nsc);
        assertEquals(numberFormat, cachedNumberFormat);
        assertTrue(cachedNumberFormat instanceof DecimalFormat);
    }

    @Test
    public void testConstructor_numberFormat() {
        var numberFormat = NumberFormat.getCurrencyInstance(Locale.JAPAN);
        var nsc = new NumberStringConverter(numberFormat);
        var cachedNumberFormat = NumberStringConverterShim.getNumberFormat(nsc);
        assertEquals(numberFormat, cachedNumberFormat);
    }

    /*********************************************************************
     * Test toString / fromString methods
     ********************************************************************/

    @Test
    public void fromString_testValidInput() {
        assertEquals(10L, converter.fromString("10"));
    }

    @Test
    public void fromString_testValidInputWithWhiteSpace() {
        assertEquals(10L, converter.fromString("      10      "));
    }

    @Test(expected=RuntimeException.class)
    public void fromString_testInvalidInput() {
        converter.fromString("abcdefg");
    }

    @Test
    public void toString_validInput() {
        assertEquals("10", converter.toString(10L));
    }
}
