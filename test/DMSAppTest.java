
/*
 * Name: Jacob Kohler
 * Course: CEN 3024
 * Date: November 2, 2026
 *
 * Class: DMSAppTest
 * Description: Tests the DMS application menu and user interactions.
 * Verifies that users can add, remove, update, import, display,
 * and calculate the value of Pokémon cards through the application.
 */

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import static org.junit.Assert.*;

public class DMSAppTest {

    /*
     * Method: runApp
     * Purpose: Runs the DMS application using simulated keyboard input
     * and captures the application's output.
     * Arguments: input - the simulated user input.
     * Return: The text printed by the application.
     */
    private String runApp(String input) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        ByteArrayInputStream testInput =
                new ByteArrayInputStream(input.getBytes());

        ByteArrayOutputStream testOutput =
                new ByteArrayOutputStream();

        try {
            System.setIn(testInput);
            System.setOut(new PrintStream(testOutput));

            DMSApp app = new DMSApp();
            app.run();

            return testOutput.toString();
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    // MENU DISPLAY AND VALIDATION

    /*
     * Method: testDisplayMainMenu
     * Purpose: Verifies that the main menu displays all available options.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testDisplayMainMenu() {
        String output = runApp("8\n");

        assertTrue(output.contains("Card Vendor DMS"));
        assertTrue(output.contains("1. Add Card"));
        assertTrue(output.contains("2. Display All Cards"));
        assertTrue(output.contains("3. Update Card"));
        assertTrue(output.contains("4. Remove Card"));
        assertTrue(output.contains("5. Calculate Collection Value"));
        assertTrue(output.contains("6. Import Cards from File"));
        assertTrue(output.contains("7. Search Cards"));
        assertTrue(output.contains("8. Exit"));
    }

    /*
     * Method: testInvalidMenuInput
     * Purpose: Verifies that non-numeric menu input is rejected.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testInvalidMenuInput() {
        String output = runApp("abc\n8\n");

        assertTrue(output.contains("Enter a valid option"));
        assertTrue(output.contains("Exiting DMS."));
    }

    /*
     * Method: testOutOfRangeMenuInput
     * Purpose: Verifies that menu choices outside the allowed range
     * are rejected.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testOutOfRangeMenuInput() {
        String output = runApp("9\n8\n");

        assertTrue(output.contains("Enter a valid option"));
        assertTrue(output.contains("Exiting DMS."));
    }

    // OPENING AND IMPORTING FILES

    /*
     * Method: testImportCardsThroughMenu
     * Purpose: Verifies that a user can open a text file, import
     * its card records, and display the imported cards.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testImportCardsThroughMenu() throws Exception {
        File file = File.createTempFile("dmsAppTest", ".txt");

        try {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println(
                        "Mew ex-Paldean Fates-232/091-Special Illustration Rare-Near Mint (NM)-150.00-2");
                writer.println(
                        "Pikachu-Surging Sparks-238/191-Ultra Rare-Near Mint (NM)-50.00-1");
            }

            String input =
                    "6\n" +
                            file.getAbsolutePath() + "\n" +
                            "2\n" +
                            "8\n";

            String output = runApp(input);

            assertTrue(output.contains("Mew ex"));
            assertTrue(output.contains("Pikachu"));
            assertTrue(output.contains("Paldean Fates"));
            assertTrue(output.contains("Surging Sparks"));
        } finally {
            file.delete();
        }
    }

    /*
     * Method: testInvalidFilePath
     * Purpose: Verifies that an invalid file path is handled
     * without crashing the application.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testInvalidFilePath() throws Exception {
        File file = File.createTempFile("dmsMissing", ".txt");

        try {
            assertTrue(file.delete());

            String input =
                    "6\n" +
                            file.getAbsolutePath() + "\n" +
                            "8\n";

            String output = runApp(input);

            assertTrue(output.contains("Exiting DMS."));
            assertFalse(output.contains("Mew ex"));
        } finally {
            file.delete();
        }
    }

    // DISPLAY INVENTORY

    /*
     * Method: testDisplayCardsThroughMenu
     * Purpose: Verifies that cards added to inventory can be displayed.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testDisplayCardsThroughMenu() {
        String input =
                "1\n" +
                        "Mew ex\n" +
                        "Paldean Fates\n" +
                        "232/091\n" +
                        "7\n" +
                        "1\n" +
                        "150.00\n" +
                        "2\n" +
                        "2\n" +
                        "8\n";

        String output = runApp(input);

        assertTrue(output.contains("Mew ex"));
        assertTrue(output.contains("Paldean Fates"));
        assertTrue(output.contains("Total Value: $300.00"));
    }

    // ADD OBJECTS

    /*
     * Method: testAddCardThroughMenu
     * Purpose: Verifies that a user can add a valid card through the menu.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testAddCardThroughMenu() {
        String input =
                "1\n" +
                        "Mew ex\n" +
                        "Paldean Fates\n" +
                        "232/091\n" +
                        "7\n" +
                        "1\n" +
                        "150.00\n" +
                        "2\n" +
                        "8\n";

        String output = runApp(input);

        assertTrue(output.contains("Card added"));
        assertTrue(output.contains("Mew ex"));
        assertTrue(output.contains("CARD0001"));
    }

    /*
     * Method: testAddCardRejectsEmptyName
     * Purpose: Verifies that an empty card name is rejected and
     * the user can enter a valid name afterward.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testAddCardRejectsEmptyName() {
        String input =
                "1\n" +
                        "\n" +
                        "Mew ex\n" +
                        "Paldean Fates\n" +
                        "232/091\n" +
                        "7\n" +
                        "1\n" +
                        "150.00\n" +
                        "2\n" +
                        "8\n";

        String output = runApp(input);

        assertTrue(output.contains("Input cannot be empty"));
        assertTrue(output.contains("Mew ex"));
        assertTrue(output.contains("Card added"));
    }

    // REMOVE OBJECTS

    /*
     * Method: testRemoveCardThroughMenu
     * Purpose: Verifies that a user can remove part of a card's
     * quantity through the application menu.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testRemoveCardThroughMenu() {
        String input =
                "1\n" +
                        "Mew ex\n" +
                        "Paldean Fates\n" +
                        "232/091\n" +
                        "7\n" +
                        "1\n" +
                        "150.00\n" +
                        "2\n" +
                        "4\n" +
                        "CARD0001\n" +
                        "1\n" +
                        "2\n" +
                        "8\n";

        String output = runApp(input);

        assertTrue(output.contains("Mew ex"));
        assertTrue(output.contains("CARD0001"));
        assertTrue(output.contains("Total Value: $150.00"));
    }

    /*
     * Method: testRemoveNonexistentCardThroughMenu
     * Purpose: Verifies that attempting to remove a nonexistent card
     * does not change the existing inventory.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testRemoveNonexistentCardThroughMenu() {
        String input =
                "1\n" +
                        "Mew ex\n" +
                        "Paldean Fates\n" +
                        "232/091\n" +
                        "7\n" +
                        "1\n" +
                        "150.00\n" +
                        "2\n" +
                        "4\n" +
                        "CARD9999\n" +
                        "2\n" +
                        "8\n";

        String output = runApp(input);

        assertTrue(output.contains("Mew ex"));
        assertTrue(output.contains("CARD0001"));
        assertTrue(output.contains("Total Value: $300.00"));
    }

    // UPDATE OBJECTS

    /*
     * Method: testUpdateCardThroughMenu
     * Purpose: Verifies that a user can update a card's quality
     * through the application menu.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testUpdateCardThroughMenu() {
        String input =
                "1\n" +
                        "Mew ex\n" +
                        "Paldean Fates\n" +
                        "232/091\n" +
                        "7\n" +
                        "1\n" +
                        "150.00\n" +
                        "2\n" +
                        "3\n" +
                        "CARD0001\n" +
                        "1\n" +
                        "2\n" +
                        "2\n" +
                        "8\n";

        String output = runApp(input);

        assertTrue(output.contains("Mew ex"));
        assertTrue(output.contains("Lightly Played (LP)"));
    }

    /*
     * Method: testUpdateNonexistentCardThroughMenu
     * Purpose: Verifies that attempting to update a nonexistent card
     * does not change an existing card.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testUpdateNonexistentCardThroughMenu() {
        String input =
                "1\n" +
                        "Mew ex\n" +
                        "Paldean Fates\n" +
                        "232/091\n" +
                        "7\n" +
                        "1\n" +
                        "150.00\n" +
                        "2\n" +
                        "3\n" +
                        "CARD9999\n" +
                        "2\n" +
                        "8\n";

        String output = runApp(input);

        assertTrue(output.contains("Mew ex"));
        assertTrue(output.contains("Near Mint (NM)"));
        assertTrue(output.contains("Total Value: $300.00"));
    }

    // CUSTOM ACTION: COLLECTION VALUE

    /*
     * Method: testCollectionValueThroughMenu
     * Purpose: Verifies that the user can calculate the total
     * collection value through the menu.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testCollectionValueThroughMenu() {
        String input =
                "1\n" +
                        "Mew ex\n" +
                        "Paldean Fates\n" +
                        "232/091\n" +
                        "7\n" +
                        "1\n" +
                        "150.00\n" +
                        "2\n" +
                        "5\n" +
                        "1\n" +
                        "8\n";

        String output = runApp(input);

        assertTrue(output.contains("Total collection value: $300.00"));
    }

    // RETURN TO MENU AND EXIT

    /*
     * Method: testReturnToMainMenuFromSearch
     * Purpose: Verifies that the user can return to the main menu
     * from the search menu.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testReturnToMainMenuFromSearch() {
        String output = runApp("7\n0\n8\n");

        assertTrue(output.contains("Search Cards"));
        assertTrue(output.contains("Exiting DMS."));
    }

    /*
     * Method: testExitApplication
     * Purpose: Verifies that the application exits when the user
     * selects the exit option.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testExitApplication() {
        String output = runApp("8\n");

        assertTrue(output.contains("Exiting DMS."));
    }
}
