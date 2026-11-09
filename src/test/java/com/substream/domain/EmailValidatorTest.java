package com.substream.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailValidatorTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            " ", "\t\r\n", "example.com", "@example.com", "a@@example.com", "a@b@c.com",
            "a@example", "a.b@example", "a@", "a b@example.com", "a\tb@example.com",
            "a\nb@example.com", "a\rb@example.com", "a@exam ple.com", "a@example.\tcom",
            "a@example.\ncom", "a\u2003b@example.com"
    })
    void rejectsAnEmailWhenAnyBr102ConditionFails(String email) {
        assertFalse(EmailValidator.isValid(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a@example.com", " \tINFO@EXAMPLE.COM\r\n ", "first.last+tag@sub-domain.example.com",
            "first_last@example.com", "a@.", ".@example.com", "a@.com", "a@example."
    })
    void acceptsExactlyTheMinimalBr102RulesAfterTrimming(String email) {
        assertTrue(EmailValidator.isValid(email));
    }
}
