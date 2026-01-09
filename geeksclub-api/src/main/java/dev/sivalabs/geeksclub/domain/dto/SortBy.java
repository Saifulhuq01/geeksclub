package dev.sivalabs.geeksclub.domain.dto;

public enum SortBy {
    RECENT,
    UPVOTED,
    DOWNVOTED,
    TRENDING;

    public static SortBy fromString(String value) {
        if (value == null) {
            return RECENT;
        }
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return RECENT;
        }
    }
}
