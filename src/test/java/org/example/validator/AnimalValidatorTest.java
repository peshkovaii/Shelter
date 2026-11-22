package org.example.validator;

import org.example.core.Animal;
import org.example.core.enums.Species;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AnimalValidatorTest {

    private final AnimalValidator validator = new AnimalValidator();

    @Test
    void validAnimalPassesValidation() {
        Animal animal = new Animal("Барсик", Species.CAT, "Британская", 3);
        assertDoesNotThrow(() -> validator.validate(animal));
    }

    @Test
    void nullAnimalIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(null));
    }

    @Test
    void blankNameIsRejected() {
        Animal animal = new Animal("   ", Species.CAT, "Британская", 3);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(animal));
    }

    @Test
    void nullSpeciesIsRejected() {
        Animal animal = new Animal("Барсик", null, "Британская", 3);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(animal));
    }

    @Test
    void negativeAgeIsRejected() {
        Animal animal = new Animal("Барсик", Species.CAT, "Британская", -1);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(animal));
    }
}