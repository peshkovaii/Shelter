
package org.example.ui.command.impl;

import org.example.core.Animal;
import org.example.core.enums.Species;
import org.example.service.ShelterService;
import org.example.ui.command.Command;

public class AddAnimalCommand implements Command {

    private final ShelterService service;

    public AddAnimalCommand(ShelterService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 4) {
            System.out.println("Использование: add animal <name> <species> <breed> <age>");
            return;
        }

        Animal animal = new Animal(
                args[0],
                Species.valueOf(args[1].toUpperCase()),
                args[2],
                Integer.parseInt(args[3])
        );

        service.addAnimal(animal);
        System.out.println("Добавлено: " + animal);
    }
}
