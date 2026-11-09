package com.substream.domain;

import com.substream.exception.InvalidCustomerDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerTest {

    private static final LocalDate CREATED_AT = LocalDate.of(2024, 1, 15);

    @Test
    void storesNormalizedDataAndPreservesInnerSpaces() {
        Customer customer = new Customer("customer-1", " \tAcme  Corp\r\n ",
                " \tINFO@EXAMPLE.COM\r\n ", CREATED_AT);

        assertAll(
                () -> assertEquals("customer-1", customer.getId()),
                () -> assertEquals("Acme  Corp", customer.getName()),
                () -> assertEquals("info@example.com", customer.getEmail()),
                () -> assertEquals(CREATED_AT, customer.getCreatedAt()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\r\n", " \t\n ", "\u2003"})
    void rejectsNullOrBlankNames(String name) {
        assertThrows(InvalidCustomerDataException.class,
                () -> new Customer("customer-1", name, "a@example.com", CREATED_AT));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 99, 100})
    void checksNameLengthAfterTrimming(int length) {
        String name = "a".repeat(length);

        Customer customer = new Customer("customer-1", " \t" + name + "\r\n ",
                "a@example.com", CREATED_AT);

        assertEquals(name, customer.getName());
    }

    @ParameterizedTest
    @ValueSource(ints = {101, 102})
    void rejectsNamesAboveTheLimitAfterTrimming(int length) {
        assertThrows(InvalidCustomerDataException.class,
                () -> new Customer("customer-1", " \t" + "a".repeat(length) + "\n ",
                        "a@example.com", CREATED_AT));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            " \t\n ", "example.com", "@example.com", "a@@example.com", "a@example",
            "a.b@example", "a b@example.com", "a\tb@example.com", "a@example.\ncom"
    })
    void translatesInvalidEmailsIntoTheBusinessException(String email) {
        assertThrows(InvalidCustomerDataException.class,
                () -> new Customer("customer-1", "Acme", email, CREATED_AT));
    }

    @Test
    void acceptsMinimalEmailsWithoutAddingExtraValidation() {
        Customer customer = new Customer("customer-1", "Acme", " A@. ", CREATED_AT);

        assertEquals("a@.", customer.getEmail());
    }

    @Test
    void equalityAndHashCodeDependOnlyOnId() {
        Customer first = new Customer("same-id", "Acme", "a@example.com", CREATED_AT);
        Customer second = new Customer("same-id", "Other", "b@example.com", CREATED_AT.plusDays(1));
        Customer third = new Customer("same-id", "Third", "c@example.com", CREATED_AT.minusDays(1));
        Customer differentId = new Customer("other-id", "Acme", "a@example.com", CREATED_AT);

        assertAll(
                () -> assertEquals(first, first),
                () -> assertEquals(first, second),
                () -> assertEquals(second, first),
                () -> assertEquals(second, third),
                () -> assertEquals(first, third),
                () -> assertEquals(first.hashCode(), second.hashCode()),
                () -> assertEquals(1, new HashSet<>(java.util.List.of(first, second, third)).size()),
                () -> assertNotEquals(first, differentId),
                () -> assertNotEquals(first, null),
                () -> assertFalse(first.equals("same-id")));
    }
}
