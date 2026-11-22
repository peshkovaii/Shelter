package org.example.validator;

import org.example.core.Adopter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AdopterValidatorTest {

    private final AdopterValidator validator = new AdopterValidator();

    @Test
    void validAdopterPassesValidation() {
        Adopter adopter = new Adopter("Иванов Иван", "+79991234567", "ivan@mail.ru");
        assertDoesNotThrow(() -> validator.validate(adopter));
    }

    @Test
    void nullAdopterIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(null));
    }

    @Test
    void blankFullNameIsRejected() {
        Adopter adopter = new Adopter("   ", "+79991234567", "ivan@mail.ru");
        assertThrows(IllegalArgumentException.class, () -> validator.validate(adopter));
    }

    @Test
    void blankPhoneIsRejected() {
        Adopter adopter = new Adopter("Иванов Иван", "", "ivan@mail.ru");
        assertThrows(IllegalArgumentException.class, () -> validator.validate(adopter));
    }

    @Test
    void emailWithoutAtIsRejected() {
        Adopter adopter = new Adopter("Иванов Иван", "+79991234567", "ivanmail.ru");
        assertThrows(IllegalArgumentException.class, () -> validator.validate(adopter));
    }

    @Test
    void blankEmailIsRejected() {
        Adopter adopter = new Adopter("Иванов Иван", "+79991234567", "  ");
        assertThrows(IllegalArgumentException.class, () -> validator.validate(adopter));
    }
}