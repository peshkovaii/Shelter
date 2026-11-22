
package org.example.factory;

import org.example.service.ShelterService;
import org.example.ui.ConsoleUI;
import org.example.ui.command.impl.*;

public class ConsoleUIFactory {

    public static ConsoleUI create(ShelterService service) {
        ConsoleUI ui = new ConsoleUI();

        ui.register("add animal", new AddAnimalCommand(service));
        ui.register("add adopter", new AddAdopterCommand(service));
        ui.register("list animals", new ListAnimalsCommand(service));
        ui.register("list adopters", new ListAdoptersCommand(service));
        ui.register("list requests", new ListRequestsCommand(service));
        ui.register("get animal", new GetAnimalCommand(service));
        ui.register("delete animal", new DeleteAnimalCommand(service));
        ui.register("adopt", new CreateAdoptionRequestCommand(service));
        ui.register("approve", new ApproveAdoptionRequestCommand(service));

        return ui;
    }
}
