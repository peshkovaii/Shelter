
package org.example.ui.command.impl;

import org.example.service.ShelterService;
import org.example.ui.command.Command;

public class ApproveAdoptionRequestCommand implements Command {

    private final ShelterService service;

    public ApproveAdoptionRequestCommand(ShelterService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            System.out.println("Использование: approve <requestId>");
            return;
        }

        Long requestId = Long.parseLong(args[0]);

        service.approveRequest(requestId);
        System.out.println("Заявка одобрена");
    }
}
