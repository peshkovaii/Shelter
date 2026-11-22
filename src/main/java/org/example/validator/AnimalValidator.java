package org.example.validator;

import org.example.core.Animal;

public class AnimalValidator {
    public void validate(Animal animal){
        if (animal == null){
            throw new IllegalArgumentException("Животное не указано");
        }

        if (animal.getName() == null || animal.getName().isBlank() ){
            throw new IllegalArgumentException("Кличка не может быть пустой");
        }

        if (animal.getSpecies()==null){
            throw new IllegalArgumentException(("Не указан вид животного"));
        }

        if (animal.getAge()<0){
            throw new IllegalArgumentException("Возраст не может быть отрицательным");
        }
    }
}

