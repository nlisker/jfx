package javafx.util.converter;

import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;
import java.util.Locale;

abstract class BaseTemporalConverter<T extends Temporal> extends BaseStringConverter<T> {

    protected static final FormatStyle DEFAULT_STYLE = FormatStyle.SHORT;
    protected static final Chronology DEFAULT_CHRONOLOGY = IsoChronology.INSTANCE;
    protected static Locale defaultLocale() {
        return Locale.getDefault(Locale.Category.FORMAT);
    }

    protected final DateTimeFormatter formatter;
    protected final DateTimeFormatter parser;


}
