
package org.example.ui;

import org.example.ui.command.Command;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);
    private final Map<String, Command> commands = new LinkedHashMap<>();

    public void register(String name, Command command) {
        commands.put(name, command);
    }

    public void run() {
        printHelp();

        boolean running = true;

        while (running) {
            System.out.print("> ");

            if (!scanner.hasNextLine()) {
                break;
            }

            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\s+");
            String commandName = parts[0].toLowerCase();
            int argumentStart = 1;

            if (parts.length > 1) {
                String twoWords = commandName + " " + parts[1].toLowerCase();

                if (commands.containsKey(twoWords)) {
                    commandName = twoWords;
                    argumentStart = 2;
                }
            }

            if (commandName.equals("exit")) {
                running = false;
                continue;
            }

            if (commandName.equals("help")) {
                printHelp();
                continue;
            }

            Command command = commands.get(commandName);

            if (command == null) {
                System.out.println("Неизвестная команда. Введите help");
                continue;
            }

            String[] args = Arrays.copyOfRange(
                    parts, argumentStart, parts.length
            );

            try {
                command.execute(args);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число");
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void printHelp() {
        System.out.println("""
                Доступные команды:
                help
                add animal <name> <species> <breed> <age>
                add adopter <ФИО> <телефон> <email>
                list animals
                list adopters
                list requests
                get animal <id>
                delete animal <id>
                adopt <animalId> <adopterId>
                approve <requestId>
                exit
                """);
    }
}
