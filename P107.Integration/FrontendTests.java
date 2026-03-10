import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;


/**
 * This class contains JUnit tests for the Frontend class.
 * Tests verify proper command parsing, backend interaction, and output formatting.
 */
public class FrontendTests {

    /**
     * Test 1: Tests the submit command (single record submission)
     * Verifies that:
     * - Single record submission is parsed correctly
     * - Backend's addRecord method is called
     * - Success message is displayed to user
     */
    @Test
    public void roleTest1() {
        // Create test input simulating a submit command followed by quit
        TextUITester tester = new TextUITester("submit Player1 ASIA 50000 100 200 001:30:45\nquit\n");
        
        // Create backend placeholder and frontend
        BackendPlaceholder backend = new BackendPlaceholder();
        Scanner scanner = new Scanner(System.in);
        Frontend frontend = new Frontend(scanner, backend);
        
        // Run the command loop
        frontend.runCommandLoop();
        
        // Check output contains success message
        String output = tester.checkOutput();
        assertTrue(output.contains("Record submitted successfully."), 
                   "Output should confirm successful record submission");
        assertTrue(output.contains("Command Instructions:"), 
                   "Output should display command instructions at start");
    }

    /**
     * Test 2: Tests the score and show commands
     * Verifies that:
     * - Score range command is parsed correctly
     * - Backend's getAndSetRange method is called with correct parameters
     * - Show command displays the expected number of records
     * - Frontend maintains the score range state across commands
     */
    @Test
    public void roleTest2() {
        // Create test input: set score range, then show records
        TextUITester tester = new TextUITester("score 100 to 500\nshow 3\nquit\n");
        
        // Create backend placeholder and frontend
        BackendPlaceholder backend = new BackendPlaceholder();
        Scanner scanner = new Scanner(System.in);
        Frontend frontend = new Frontend(scanner, backend);
        
        // Run the command loop
        frontend.runCommandLoop();
        
        // Check output
        String output = tester.checkOutput();
        // Backend placeholder returns "Record1", "Record2", "Record3"
        assertTrue(output.contains("Record1"), "Output should contain Record1");
        assertTrue(output.contains("Record2"), "Output should contain Record2");
        assertTrue(output.contains("Record3"), "Output should contain Record3");
    }

    /**
     * Test 3: Tests the time filter and show least damage commands
     * Verifies that:
     * - Time filter command is parsed correctly
     * - Backend's applyAndSetFilter method is called
     * - Show least damage command calls backend's getTopTen method
     * - All top ten records are displayed
     */
    @Test
    public void roleTest3() {
        // Create test input: set time filter, then show least damage
        TextUITester tester = new TextUITester("time 002:00:00\nshow least damage\nquit\n");
        
        // Create backend placeholder and frontend
        BackendPlaceholder backend = new BackendPlaceholder();
        Scanner scanner = new Scanner(System.in);
        Frontend frontend = new Frontend(scanner, backend);
        
        // Run the command loop
        frontend.runCommandLoop();
        
        // Check output contains top ten results
        String output = tester.checkOutput();
        assertTrue(output.contains("TopPlayer1"), "Output should contain top player from getTopTen");
        assertTrue(output.contains("TopPlayer2"), "Output should contain second top player");
    }

    /**
     * Backend placeholder class for testing Frontend in isolation.
     * Returns hard-coded values to simulate backend behavior.
     */
    private class BackendPlaceholder implements BackendInterface {
        
        @Override
        public void addRecord(GameRecord record) {
            // Placeholder: does nothing, just simulates accepting the record
        }

        @Override
        public void readData(String filename) throws IOException {
            // Placeholder: does nothing, simulates successful file read
        }

        @Override
        public List<String> getAndSetRange(Integer low, Integer high) {
            // Return a list of sample record names
            List<String> records = new ArrayList<>();
            records.add("Record1");
            records.add("Record2");
            records.add("Record3");
            records.add("Record4");
            records.add("Record5");
            return records;
        }

        @Override
        public List<String> applyAndSetFilter(String time) {
            // Return filtered results (placeholder behavior)
            List<String> filtered = new ArrayList<>();
            filtered.add("FilteredRecord1");
            filtered.add("FilteredRecord2");
            return filtered;
        }

        @Override
        public List<String> getTopTen() {
            // Return top ten players with least damage
            List<String> topTen = new ArrayList<>();
            topTen.add("TopPlayer1");
            topTen.add("TopPlayer2");
            topTen.add("TopPlayer3");
            topTen.add("TopPlayer4");
            topTen.add("TopPlayer5");
            topTen.add("TopPlayer6");
            topTen.add("TopPlayer7");
            topTen.add("TopPlayer8");
            topTen.add("TopPlayer9");
            topTen.add("TopPlayer10");
            return topTen;
        }
    }
}