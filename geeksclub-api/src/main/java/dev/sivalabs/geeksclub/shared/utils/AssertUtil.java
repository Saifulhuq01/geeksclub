package dev.sivalabs.geeksclub.shared.utils;

public class AssertUtil {
    private AssertUtil() {}

    public static <T> T requireNotNull(T obj, String message) {
        if (obj == null) throw new IllegalArgumentException(message);
        return obj;
    }

    public static String requireNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static String requireLengthBetween(String value, int min, int max, String message) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (value.length() < min || value.length() > max) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static int requireMin(int value, int min, String message) {
        if (value < min) throw new IllegalArgumentException(message);
        return value;
    }
}
