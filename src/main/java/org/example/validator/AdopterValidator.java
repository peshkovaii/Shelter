package org.example.validator;

import org.example.core.Adopter;

public class AdopterValidator {

    public void validate(Adopter adopter) {
        if (adopter == null) {
            throw new IllegalArgumentException("Усыновитель не указан");
        }

        if (adopter.getFullName() == null || adopter.getFullName().isBlank()) {
            throw new IllegalArgumentException("ФИО не указано");
        }

        if (adopter.getPhone() == null || adopter.getPhone().isBlank()) {
            throw new IllegalArgumentException("Телефон не указан");
        }

        if (adopter.getMail() == null || adopter.getMail().isBlank()
                || !adopter.getMail().contains("@")) {
            throw new IllegalArgumentException("Некорректно введен или отсутвует email");
        }


    }


}
