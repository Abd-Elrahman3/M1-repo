package com.substream.domain;

/**
 * M1. BR-102 email rules, implemented with plain String operations (no regular expressions).
 */
public final class EmailValidator {

    private EmailValidator() {
    }

    /** BR-102: {@code null} and blank values are invalid. */
    public static boolean isValid(String email) {
        if (email == null) {
            return false;
        }

        String trimmed = email.trim();
        for (int i = 0; i < trimmed.length(); i++) {
            if (Character.isWhitespace(trimmed.charAt(i))) {
                return false;
            }
        }

        int atIndex = trimmed.indexOf('@');
        return atIndex > 0
                && atIndex == trimmed.lastIndexOf('@')
                && trimmed.indexOf('.', atIndex + 1) >= 0;
    }
}
