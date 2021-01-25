package javafx.util.converter;

import javafx.util.StringConverter;

abstract class BaseStringConverter<T> extends StringConverter<T> {

    @Override
    public T fromString(String string) {
        if (string == null) {
            return null;
        }
        string = string.trim();
        if (string.isEmpty()) {
            return null;
        }
        return fromNonEmptyString(string);
    }

    abstract T fromNonEmptyString(String string);

    @Override
    public String toString(T object) {
        return object == null ? "" : toStringFromNonNull(object);
    }

    String toStringFromNonNull(T object) {
        return object.toString();
    }
}