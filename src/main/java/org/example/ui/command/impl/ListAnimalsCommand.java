
package org.example.ui.command.impl;

import org.example.service.ShelterService;
import org.example.ui.command.Command;

public class ListAnimalsCommand implements Command {

    private final ShelterService service;

    public ListAnimalsCommand(ShelterService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 0) {
            System.out.println("Использование: list animals");
            return;
        }

        service.getAllAnimals().forEach(System.out::println);
    }
}
