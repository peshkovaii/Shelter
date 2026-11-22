
package org.example.ui.command.impl;

import org.example.core.Adopter;
import org.example.service.ShelterService;
import org.example.ui.command.Command;

import java.util.Arrays;

public class AddAdopterCommand implements Command {

    private final ShelterService service;

    public AddAdopterCommand(ShelterService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            System.out.println("Использование: add adopter <ФИО> <телефон> <email>");
            return;
        }

        String fullName = String.join(" ",
                Arrays.copyOfRange(args, 0, args.length - 2));

        String phone = args[args.length - 2];
        String email = args[args.length - 1];

        Adopter adopter = new Adopter(fullName, phone, email);

        service.addAdopter(adopter);
        System.out.println("Добавлено: " + adopter);
    }
}
