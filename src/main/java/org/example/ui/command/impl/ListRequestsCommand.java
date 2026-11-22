
package org.example.ui.command.impl;

import org.example.service.ShelterService;
import org.example.ui.command.Command;

public class ListRequestsCommand implements Command {

    private final ShelterService service;

    public ListRequestsCommand(ShelterService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 0) {
            System.out.println("Использование: list requests");
            return;
        }

        service.getAllRequests().forEach(System.out::println);
    }
}
