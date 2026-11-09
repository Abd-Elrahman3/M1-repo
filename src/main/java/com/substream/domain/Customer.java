package com.substream.domain;

import com.substream.exception.InvalidCustomerDataException;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

/**
 * M1. A corporate customer (README, section 4.1).
 */
public final class Customer implements Identifiable<String> {

    public static final int MAX_NAME_LENGTH = 100;

    private final String id;
    private final String name;
    private final String email;
    private final LocalDate createdAt;

    /**
     * BR-101 / BR-102: throws {@code InvalidCustomerDataException} for an invalid name or email.
     * Stores the name trimmed and the email trimmed and lower-cased.
     */
    public Customer(String id, String name, String email, LocalDate createdAt) {
        if (name == null || name.trim().isBlank() || name.trim().length() > MAX_NAME_LENGTH) {
            throw new InvalidCustomerDataException("Name must contain 1 to " + MAX_NAME_LENGTH
                    + " characters after trimming");
        }
        if (!EmailValidator.isValid(email)) {
            throw new InvalidCustomerDataException("Email must have no whitespace, one @ with a local part,"
                    + " and a dot after @");
        }

        this.id = id;
        this.name = name.trim();
        this.email = email.trim().toLowerCase(Locale.ROOT);
        this.createdAt = createdAt;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /** Used by the provided {@code EmailNotificationService}. */
    public String getEmail() {
        return email;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    /** Section 4.7: equality is based on {@code id} only. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o instanceof Customer other && Objects.equals(getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
