
package org.example.ui.command.impl;

import org.example.service.ShelterService;
import org.example.ui.command.Command;

public class ListAdoptersCommand implements Command {

    private final ShelterService service;

    public ListAdoptersCommand(ShelterService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 0) {
            System.out.println("Использование: list adopters");
            return;
        }

        service.getAllAdopters().forEach(System.out::println);
    }
}
