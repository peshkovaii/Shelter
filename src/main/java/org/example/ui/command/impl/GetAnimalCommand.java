
package org.example.ui.command.impl;

import org.example.service.ShelterService;
import org.example.ui.command.Command;

public class GetAnimalCommand implements Command {

    private final ShelterService service;

    public GetAnimalCommand(ShelterService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            System.out.println("Использование: get animal <id>");
            return;
        }

        Long id = Long.parseLong(args[0]);

        System.out.println(service.getAnimal(id));
    }
}
