import java.util.List;
import java.util.Scanner;
import java.io.*;

/**
 * Frontend class for CS400 Game Records Leaderboard
 * Implements the FrontendInterface and interacts with a BackendInterface
 * Tracks score ranges and allows users to submit, filter, and view records.
 */
public class Frontend implements FrontendInterface {

    private Scanner scanner;                // Scanner for user input
    private BackendInterface backend;       // Backend interface to handle data
    private Integer currentMinScore = null; // Track current minimum score range
    private Integer currentMaxScore = null; // Track current maximum score range

    // Constructor takes Scanner and BackendInterface
    public Frontend(Scanner in, BackendInterface backend) {
        this.scanner = in;
        this.backend = backend;
    }

    /**
     * Main loop that repeatedly asks the user for commands until "quit"
     */
    @Override
    public void runCommandLoop() {
        showCommandInstructions(); // Display instructions at start

        boolean running = true;

        while (running) {
            System.out.print("Enter command: ");
            String command = scanner.nextLine().trim();

            if (command.equalsIgnoreCase("quit")) {
                running = false; // Exit loop
            } else {
                try {
                    processSingleCommand(command);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Displays instructions for all available commands
     */
    @Override
    public void showCommandInstructions() {
        System.out.println("Command Instructions:");
        System.out.println("submit NAME CONTINENT SCORE DAMAGE_TAKEN DAMAGE_GIVEN COMPLETION_TIME");
        System.out.println("submit multiple FILEPATH");
        System.out.println("score MAX");
        System.out.println("score MIN to MAX");
        System.out.println("time TIME");
        System.out.println("show MAX_COUNT");
        System.out.println("show least damage");
        System.out.println("help");
        System.out.println("quit");
    }

    /**
     * Parses a single command string and calls the appropriate handler
     */
    @Override
    public void processSingleCommand(String command) {
        if (command == null || command.isEmpty()) {
            System.out.println("Error: Command cannot be empty.");
            return;
        }

        String[] tokens = command.split("\\s+");
        String cmd = tokens[0].toLowerCase();

        switch (cmd) {
            case "submit":
                handleSubmit(tokens);
                break;
            case "score":
                handleScore(tokens);
                break;
            case "time":
                handleTime(tokens);
                break;
            case "show":
                handleShow(tokens);
                break;
            case "help":
                showCommandInstructions();
                break;
            default:
                System.out.println("Error: Unknown command '" + cmd + "'.");
        }
    }

    /**
     * Handles the "submit" commands
     */
    private void handleSubmit(String[] tokens) {
        if (tokens.length == 7) { // Single record submit
            try {
                String name = tokens[1];
                GameRecord.Continent continent = GameRecord.Continent.valueOf(tokens[2].toUpperCase());
                int score = Integer.parseInt(tokens[3]);
                int damageTaken = Integer.parseInt(tokens[4]);
                int damageGiven = Integer.parseInt(tokens[5]);
                String completionTime = tokens[6];

                backend.addRecord(new GameRecord(name, continent, score, damageTaken, damageGiven, completionTime));
                System.out.println("Record submitted successfully.");

            } catch (Exception e) {
                System.out.println("Error: Invalid submit command arguments.");
            }

        } else if (tokens.length == 3 && tokens[1].equalsIgnoreCase("multiple")) { // Multiple submit
            try {
                String filepath = tokens[2];
                backend.readData(filepath);
                System.out.println("Records loaded from file: " + filepath);
            } catch (IOException e) {
                System.out.println("Error loading records from file: " + e.getMessage());
            }
        } else {
            System.out.println("Error: Invalid submit command syntax.");
        }
    }

    /**
     * Handles the "score" commands to set current score range
     */
    private void handleScore(String[] tokens) {
        try {
            if (tokens.length == 2) { // score MAX
                currentMinScore = null;
                currentMaxScore = Integer.parseInt(tokens[1]);
                backend.getAndSetRange(currentMinScore, currentMaxScore);
            } else if (tokens.length == 4 && tokens[2].equalsIgnoreCase("to")) { // score MIN to MAX
                currentMinScore = Integer.parseInt(tokens[1]);
                currentMaxScore = Integer.parseInt(tokens[3]);
                backend.getAndSetRange(currentMinScore, currentMaxScore);
            } else {
                System.out.println("Error: Invalid score command syntax.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Score must be an integer.");
        }
    }

    /**
     * Handles the "time" command to apply a completion time filter
     */
    private void handleTime(String[] tokens) {
        if (tokens.length != 2) {
            System.out.println("Error: Invalid time command syntax.");
            return;
        }
        String time = tokens[1];
        backend.applyAndSetFilter(time);
    }

    /**
     * Handles the "show" commands to display records
     */
    private void handleShow(String[] tokens) {
        try {
            if (tokens.length == 2) { // show MAX_COUNT
                int count = Integer.parseInt(tokens[1]);
                List<String> list = backend.getAndSetRange(currentMinScore, currentMaxScore);
                for (int i = 0; i < Math.min(count, list.size()); i++) {
                    System.out.println(list.get(i));
                }
            } else if (tokens.length == 3 // show least damage
                       && tokens[1].equalsIgnoreCase("least")
                       && tokens[2].equalsIgnoreCase("damage")) {
                List<String> top = backend.getTopTen();
                for (String name : top) {
                    System.out.println(name);
                }
            } else {
                System.out.println("Error: Invalid show command syntax.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number for show command.");
        }
    }
}
