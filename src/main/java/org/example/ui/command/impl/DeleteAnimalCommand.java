
package org.example.ui.command.impl;

import org.example.service.ShelterService;
import org.example.ui.command.Command;

public class DeleteAnimalCommand implements Command {

    private final ShelterService service;

    public DeleteAnimalCommand(ShelterService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            System.out.println("Использование: delete animal <id>");
            return;
        }

        Long id = Long.parseLong(args[0]);

        service.deleteAnimal(id);
        System.out.println("Животное удалено");
    }
}
