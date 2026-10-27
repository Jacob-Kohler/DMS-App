
import java.util.*;

/*
 * Name: Jacob Kohler
 * Course: CEN 3024
 * Date: October 26, 2026
 *
 * Class: DMSApp
 * Description: Provides the main menu and user interface for the
 * Card Vendor Data Management System (DMS). Allows users to add,
 * import, display, update, remove, search, and calculate the value
 * of Pokemon cards in the collection.
 */
public class DMSApp {
    private CardManager cardManager;
    private Scanner scanner;

    // Initializes the card manager and keyboard input scanner.
    public DMSApp() {
        cardManager = new CardManager();
        scanner = new Scanner(System.in);
    }

    /*
     * Method: main
     * Purpose: Starts the DMS application and begins the main menu loop.
     * Arguments: args - command-line arguments.
     * Return: None.
     */
    public static void main(String[] args) {
        DMSApp app = new DMSApp();
        app.run();
    }

    /*
     * Method: run
     * Purpose: Runs the main menu and directs the user to the selected
     * operation until the user chooses to exit.
     * Arguments: None.
     * Return: None.
     */
    public void run() {
        boolean running = true;

        while (running) {
            int choice = displayMenu();

            try {
                switch (choice) {
                    case 1:
                        addCard();
                        break;
                    case 2:
                        displayCards();
                        break;
                    case 3:
                        updateCard();
                        break;
                    case 4:
                        removeCard();
                        break;
                    case 5:
                        calculateCollectionValue();
                        break;
                    case 6:
                        importCards();
                        break;
                    case 7:
                        searchCards();
                        break;
                    case 8:
                        running = false;
                        System.out.println("Exiting DMS.");
                        break;
                    default:
                        System.out.println("Invalid menu option.");
                }
            } catch (IllegalArgumentException exception) {
                System.out.println("Invalid input: " + exception.getMessage());
            }
        }

        scanner.close();
    }

    /*
     * Method: displayMenu
     * Purpose: Displays the available DMS operations and obtains a menu choice.
     * Arguments: None.
     * Return: The user's selected menu option.
     */
    public int displayMenu() {
        System.out.println("\nCard Vendor DMS");
        System.out.println("1. Add Card");
        System.out.println("2. Display All Cards");
        System.out.println("3. Update Card");
        System.out.println("4. Remove Card");
        System.out.println("5. Calculate Collection Value");
        System.out.println("6. Import Cards from File");
        System.out.println("7. Search Cards");
        System.out.println("8. Exit");

        return readInt("Choose an option: ", 1, 8);
    }

    /*
     * Method: addCard
     * Purpose: Collects card details, adds the card through CardManager,
     * and displays the newly added card.
     * Arguments: None.
     * Return: The added PokemonCard.
     */
    public PokemonCard addCard() {
        System.out.println("\nAdd Card");

        String cardName = readRequired("Enter card name: ");
        String setName = readRequired("Enter set name: ");
        String cardNumber = readRequired("Enter card number: ");
        String rarity = readRequired("Enter rarity: ");
        String quality = readRequired("Enter quality: ");
        double cardValue = readDouble("Enter value per card: ");
        int quantity = readInt("Enter quantity: ", 1);

        PokemonCard card = cardManager.addCard(
                cardName, setName, cardNumber, rarity,
                quality, cardValue, quantity);

        System.out.println("Card added.");
        System.out.println(card);

        return card;
    }

    /*
     * Method: importCards
     * Purpose: Gets a text file path, imports its card records, and
     * displays the number of imported cards.
     * Arguments: None.
     * Return: A list of imported PokemonCard objects.
     */
    public ArrayList<PokemonCard> importCards() {
        System.out.println("\nImport Cards");
        System.out.println("Enter the full path to your text file.");
        String fileName = readRequired("File path: ");

        System.setProperty("dms.importFile", fileName);

        try {
            ArrayList<PokemonCard> importedCards =
                    cardManager.importCards();

            System.out.println("Imported "
                    + importedCards.size() + " cards.");

            return importedCards;
        } finally {
            System.clearProperty("dms.importFile");
        }
    }

    /*
     * Method: removeCard
     * Purpose: Finds a card by ID, asks how many copies to remove,
     * and displays the removal result.
     * Arguments: None.
     * Return: The removed or updated PokemonCard, or null if the
     * card could not be found or removed.
     */
    public PokemonCard removeCard() {
        String cardID = readRequired("Enter card ID to remove: ");
        PokemonCard card = cardManager.findCard(cardID);

        if (card == null) {
            System.out.println("Card ID not found.");
            return null;
        }

        System.out.println(card);
        int quantity = readInt(
                "Enter quantity to remove: ",
                1, card.getQuantity());

        PokemonCard removedCard = cardManager.removeCard(cardID, quantity);

        if (removedCard == null) {
            System.out.println("Unable to remove.");
        } else {
            System.out.println("Card removed.");
        }

        return removedCard;
    }

    /*
     * Method: updateCard
     * Purpose: Finds a card by ID and updates its quality, value,
     * and quantity.
     * Arguments: None.
     * Return: The updated PokemonCard, or null if the card was not found
     * or could not be updated.
     */
    public PokemonCard updateCard() {
        String cardID = readRequired("Enter card ID to update: ");
        PokemonCard card = cardManager.findCard(cardID);

        if (card == null) {
            System.out.println("Card ID not found.");
            return null;
        }

        System.out.println(card);

        String quality = readRequired("Enter new quality: ");
        double cardValue = readDouble("Enter new value per card: ");
        int quantity = readInt("Enter new quantity: ", 0);

        PokemonCard updatedCard = cardManager.updateCard(
                cardID, quality, cardValue, quantity);

        if (updatedCard == null) {
            System.out.println("Card could not be updated.");
        } else if (quantity == 0) {
            System.out.println("Card deleted.");
        } else {
            System.out.println("Card updated successfully.");
            System.out.println(updatedCard);
        }

        return updatedCard;
    }

    /*
     * Method: displayCards
     * Purpose: Retrieves and displays all cards currently stored in the DMS.
     * Arguments: None.
     * Return: A list containing all stored PokemonCard objects.
     */
    public ArrayList<PokemonCard> displayCards() {
        ArrayList<PokemonCard> allCards = cardManager.getAllCards();

        if (allCards.isEmpty()) {
            System.out.println("No cards are currently stored.");
        } else {
            for (PokemonCard card : allCards) {
                System.out.println("\n" + card);

            }
        }

        return allCards;
    }

    /*
     * Method: searchCards
     * Purpose: Lets the user search for cards by ID, set, name, rarity,
     * quality, or value, then displays the matching results.
     * Arguments: None.
     * Return: A list of matching PokemonCard objects.
     */
    public ArrayList<PokemonCard> searchCards() {
        System.out.println("\nSearch Cards");
        System.out.println("1. Card ID");
        System.out.println("2. Set");
        System.out.println("3. Name");
        System.out.println("4. Rarity");
        System.out.println("5. Quality");
        System.out.println("6. Value");
        System.out.println("0. Exit");

        int choice = readInt("Choose search type: ", 1, 6);
        ArrayList<PokemonCard> results = new ArrayList<>();

        switch (choice) {
            case 1:
                PokemonCard card = cardManager.findCard(
                        readRequired("Enter card ID: "));
                if (card != null) {
                    results.add(card);
                }
                break;
            case 2:
                results = cardManager.findBySet(
                        readRequired("Enter set name: "));
                break;
            case 3:
                results = cardManager.findByName(
                        readRequired("Enter card name: "));
                break;
            case 4:
                results = cardManager.findByRarity(
                        readRequired("Enter rarity: "));
                break;
            case 5:
                results = cardManager.findByQuality(
                        readRequired("Enter quality: "));
                break;
            case 6:
                double value = readDouble("Enter comparison value: ");
                System.out.println("1. Greater than");
                System.out.println("2. Less than");
                System.out.println("3. Equal to");

                int comparisonChoice = readInt("Choose comparison: ", 1, 3);
                String comparison = "greater";

                if (comparisonChoice == 2) {
                    comparison = "less";
                } else if (comparisonChoice == 3) {
                    comparison = "equal";
                }

                results = cardManager.findByValue(value, comparison);
                break;
            default:
                System.out.println("Invalid search option.");
        }

        if (results.isEmpty()) {
            System.out.println("No matching cards found.");
        } else {
            for (PokemonCard result : results) {
                System.out.println("\n" + result);

            }
        }

        return results;
    }

    /*
     * Method: calculateCollectionValue
     * Purpose: Calculates the total value of the entire collection or
     * selected cards grouped by set, name, or rarity.
     * Arguments: None.
     * Return: The calculated total value.
     */
    public double calculateCollectionValue() {
        System.out.println("\nCollection Value");
        System.out.println("1. Entire Collection");
        System.out.println("2. By Set");
        System.out.println("3. By Card Name");
        System.out.println("4. By Rarity");

        int choice = readInt("Choose an option: ", 1, 4);
        double total;

        switch (choice) {
            case 1:
                total = cardManager.calculateCollectionValue();
                break;
            case 2:
                total = cardManager.calculateValueBySet(
                        readRequired("Enter set name: "));
                break;
            case 3:
                total = cardManager.calculateValueByName(
                        readRequired("Enter card name: "));
                break;
            case 4:
                total = cardManager.calculateValueByRarity(
                        readRequired("Enter rarity: "));
                break;
            default:
                total = 0;
        }

        System.out.printf("Total collection value: $%.2f%n", total);
        return total;
    }

    /*
     * Method: readRequired
     * Purpose: Repeatedly prompts the user until non-empty text is entered.
     * Arguments: prompt - the message displayed before input.
     * Return: The trimmed, non-empty text entered by the user.
     */
    private String readRequired(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("Input cannot be empty.");
        }
    }

    /*
     * Method: readInt
     * Purpose: Reads an integer and repeats the prompt until the value
     * is at least the specified minimum.
     * Arguments: prompt - the input message; minimum - the lowest
     * accepted integer.
     * Return: The validated integer.
     */
    private int readInt(String prompt, int minimum) {
        while (true) {
            System.out.print(prompt);

            try {
                int value = Integer.parseInt(scanner.nextLine().trim());

                if (value >= minimum) {
                    return value;
                }
            } catch (NumberFormatException exception) {
                // Ask again for invalid input.
            }

            System.out.println("Enter a valid option.");
        }
    }

    /*
     * Method: readInt
     * Purpose: Reads an integer within the specified minimum and maximum.
     * Arguments: prompt - the input message; minimum - the lowest
     * accepted integer; maximum - the highest accepted integer.
     * Return: The validated integer.
     */
    private int readInt(String prompt, int minimum, int maximum) {
        while (true) {
            System.out.print(prompt);

            try {
                int value = Integer.parseInt(scanner.nextLine().trim());

                if (value >= minimum && value <= maximum) {
                    return value;
                }
            } catch (NumberFormatException exception) {
                // Ask again for invalid input.
            }

            System.out.println("Enter a valid option");
        }
    }

    /*
     * Method: readDouble
     * Purpose: Reads a finite, non-negative decimal number and repeats
     * the prompt when the input is invalid.
     * Arguments: prompt - the input message.
     * Return: The validated double value.
     */
    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                double value = Double.parseDouble(scanner.nextLine().trim());

                if (Double.isFinite(value) && value >= 0) {
                    return value;
                }
            } catch (NumberFormatException exception) {
                // Ask again for invalid input.
            }

            System.out.println("Enter a valid option");
        }
    }
}
