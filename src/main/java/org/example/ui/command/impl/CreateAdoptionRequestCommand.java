
package org.example.ui.command.impl;

import org.example.core.AdoptionRequest;
import org.example.service.ShelterService;
import org.example.ui.command.Command;

public class CreateAdoptionRequestCommand implements Command {

    private final ShelterService service;

    public CreateAdoptionRequestCommand(ShelterService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 2) {
            System.out.println("Использование: adopt <animalId> <adopterId>");
            return;
        }

        Long animalId = Long.parseLong(args[0]);
        Long adopterId = Long.parseLong(args[1]);

        AdoptionRequest request = service.createRequest(animalId, adopterId);

        System.out.println("Создана заявка: " + request);
    }
}
