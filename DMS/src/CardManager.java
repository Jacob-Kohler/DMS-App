
import java.io.*;
import java.util.*;

/*
 * Name: Jacob Kohler
 * Course: CEN 3024
 * Date: October 26, 2026
 *
 * Class: CardManager
 * Description: Manages the Pokemon card collection for the DMS.
 * Handles adding, importing, removing, updating, searching, and
 * retrieving cards, as well as calculating collection values.
 */
public class CardManager {
    private ArrayList<PokemonCard> cards;
    private int nextID;

    // Initializes an empty card collection and starts the ID counter.
    public CardManager() {
        cards = new ArrayList<>();
        nextID = 1;
    }

    /*
     * Method: addCard
     * Purpose: Validates card information, combines matching records
     * when appropriate, or creates a new card with a generated ID.
     * Arguments: cardName, setName, cardNumber, rarity, quality,
     * cardValue, and quantity - the information for the card.
     * Return: The existing matching card or the newly created card.
     */
    public PokemonCard addCard(String cardName, String setName,
                               String cardNumber, String rarity,
                               String quality, double cardValue,
                               int quantity) {
        if (cardName == null || cardName.trim().isEmpty()
                || setName == null || setName.trim().isEmpty()
                || cardNumber == null || cardNumber.trim().isEmpty()
                || rarity == null || rarity.trim().isEmpty()
                || quality == null || quality.trim().isEmpty()
                || !Double.isFinite(cardValue) || cardValue < 0
                || quantity <= 0) {
            throw new IllegalArgumentException("Invalid card information.");
        }

        PokemonCard matchingCard = findMatchingCard(
                cardName, setName, cardNumber, rarity, quality);

        if (matchingCard != null
                && Double.compare(matchingCard.getCardValue(), cardValue) == 0) {
            long combinedQuantity =
                    (long) matchingCard.getQuantity() + quantity;

            if (combinedQuantity > Integer.MAX_VALUE) {
                throw new IllegalArgumentException(
                        "Combined quantity exceeds Java's supported integer range.");
            }

            matchingCard.setQuantity((int) combinedQuantity);
            return matchingCard;
        }

        PokemonCard newCard = new PokemonCard(
                generateCardID(), cardName, setName, cardNumber,
                rarity, quality, cardValue, quantity);

        cards.add(newCard);
        return newCard;
    }

    /*
     * Method: importCards
     * Purpose: Reads card records from the file path supplied through
     * the DMS import property and adds valid records to the collection.
     * Arguments: None.
     * Return: A list of cards imported from the file.
     */
    public ArrayList<PokemonCard> importCards() {
        ArrayList<PokemonCard> importedCards = new ArrayList<>();
        String fileName = System.getProperty("dms.importFile");

        if (fileName == null || fileName.trim().isEmpty()) {
            System.out.println("No file path was provided.");
            return importedCards;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    String[] data = line.split("-", -1);
                    int start = data.length == 8 ? 1 : 0;

                    if (data.length - start != 7) {
                        System.out.println(
                                "Skipped invalid record on line " + lineNumber);
                        continue;
                    }



                    PokemonCard card = addCard(
                            data[start].trim(),
                            data[start + 1].trim(),
                            data[start + 2].trim(),
                            data[start + 3].trim(),
                            data[start + 4].trim(),
                            Double.parseDouble(data[start + 5].trim()),
                            Integer.parseInt(data[start + 6].trim()));

                    if (!importedCards.contains(card)) {
                        importedCards.add(card);
                    }
                } catch (IllegalArgumentException exception) {
                    System.out.println("Skipped invalid record.");
                }
            }
        } catch (IOException exception) {
            System.out.println("Could not open file.");
        }

        return importedCards;
    }

    /*
     * Method: removeCard
     * Purpose: Removes the requested quantity of a card or deletes
     * the card record when its entire quantity is removed.
     * Arguments: cardID - the ID of the card; quantity - the number
     * of copies to remove.
     * Return: The affected card, or null if the request is invalid.
     */
    public PokemonCard removeCard(String cardID, int quantity) {
        PokemonCard card = findCard(cardID);

        if (card == null || quantity <= 0
                || quantity > card.getQuantity()) {
            return null;
        }

        if (quantity == card.getQuantity()) {
            cards.remove(card);
        } else {
            card.setQuantity(card.getQuantity() - quantity);
        }

        return card;
    }

    /*
     * Method: updateCard
     * Purpose: Updates a card's quality, individual value, and quantity.
     * A quantity of zero removes the card from the collection.
     * Arguments: cardID - the card ID; quality - the new condition;
     * cardValue - the new individual value; quantity - the new quantity.
     * Return: The updated or removed card, or null if the update fails.
     */
    public PokemonCard updateCard(String cardID, String quality,
                                  double cardValue, int quantity) {
        PokemonCard card = findCard(cardID);

        if (card == null || quality == null || quality.trim().isEmpty()
                || !Double.isFinite(cardValue) || cardValue < 0
                || quantity < 0) {
            return null;
        }

        if (quantity == 0) {
            cards.remove(card);
            return card;
        }

        try {
            card.setQuality(quality);
            card.setCardValue(cardValue);
            card.setQuantity(quantity);
            return card;
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    /*
     * Method: findCard
     * Purpose: Searches for a card using its ID without regard to case.
     * Arguments: cardID - the ID to search for.
     * Return: The matching PokemonCard, or null if it is not found.
     */
    public PokemonCard findCard(String cardID) {
        if (cardID == null || cardID.trim().isEmpty()) {
            return null;
        }

        for (PokemonCard card : cards) {
            if (card.getCardID().equalsIgnoreCase(cardID.trim())) {
                return card;
            }
        }

        return null;
    }

    /*
     * Method: findBySet
     * Purpose: Finds all cards belonging to the specified set.
     * Arguments: setName - the set name to search for.
     * Return: A list of matching cards, or an empty list if none match.
     */
    public ArrayList<PokemonCard> findBySet(String setName) {
        ArrayList<PokemonCard> results = new ArrayList<>();

        if (setName == null) {
            return results;
        }

        for (PokemonCard card : cards) {
            if (card.getSetName().equalsIgnoreCase(setName.trim())) {
                results.add(card);
            }
        }

        return results;
    }

    /*
     * Method: findByName
     * Purpose: Finds cards whose names match the supplied card name.
     * Arguments: cardName - the card name to search for.
     * Return: A list of matching cards, or an empty list if none match.
     */
    public ArrayList<PokemonCard> findByName(String cardName) {
        ArrayList<PokemonCard> results = new ArrayList<>();

        if (cardName == null) {
            return results;
        }

        for (PokemonCard card : cards) {
            if (card.getCardName().equalsIgnoreCase(cardName.trim())) {
                results.add(card);
            }
        }

        return results;
    }

    /*
     * Method: findByRarity
     * Purpose: Finds all cards with the specified rarity.
     * Arguments: rarity - the rarity to search for.
     * Return: A list of matching cards, or an empty list if none match.
     */
    public ArrayList<PokemonCard> findByRarity(String rarity) {
        ArrayList<PokemonCard> results = new ArrayList<>();

        if (rarity == null) {
            return results;
        }

        for (PokemonCard card : cards) {
            if (card.getRarity().equalsIgnoreCase(rarity.trim())) {
                results.add(card);
            }
        }

        return results;
    }

    /*
     * Method: findByQuality
     * Purpose: Finds all cards with the specified quality or condition.
     * Arguments: quality - the quality to search for.
     * Return: A list of matching cards, or an empty list if none match.
     */
    public ArrayList<PokemonCard> findByQuality(String quality) {
        ArrayList<PokemonCard> results = new ArrayList<>();

        if (quality == null) {
            return results;
        }

        for (PokemonCard card : cards) {
            if (card.getQuality().equalsIgnoreCase(quality.trim())) {
                results.add(card);
            }
        }

        return results;
    }

    /*
     * Method: findByValue
     * Purpose: Finds cards whose individual value meets the selected
     * greater-than, less-than, or equal-to comparison.
     * Arguments: cardValue - the value to compare; comparison - the
     * comparison type.
     * Return: A list of matching cards, or an empty list if none match.
     */
    public ArrayList<PokemonCard> findByValue(
            double cardValue, String comparison) {
        ArrayList<PokemonCard> results = new ArrayList<>();

        if (!Double.isFinite(cardValue) || cardValue < 0
                || comparison == null) {
            return results;
        }

        for (PokemonCard card : cards) {
            if (comparison.equalsIgnoreCase("greater")
                    && card.getCardValue() > cardValue) {
                results.add(card);
            } else if (comparison.equalsIgnoreCase("less")
                    && card.getCardValue() < cardValue) {
                results.add(card);
            } else if (comparison.equalsIgnoreCase("equal")
                    && Double.compare(card.getCardValue(), cardValue) == 0) {
                results.add(card);
            }
        }

        return results;
    }

    /*
     * Method: getAllCards
     * Purpose: Provides a copy of the current card collection.
     * Arguments: None.
     * Return: A new ArrayList containing the stored cards.
     */
    public ArrayList<PokemonCard> getAllCards() {
        return new ArrayList<>(cards);
    }

    /*
     * Method: calculateCollectionValue
     * Purpose: Calculates the combined value of all cards, accounting
     * for the quantity of each card.
     * Arguments: None.
     * Return: The total collection value.
     */
    public double calculateCollectionValue() {
        double total = 0;

        for (PokemonCard card : cards) {
            total += card.getCardValue() * card.getQuantity();
        }

        return total;
    }

    /*
     * Method: calculateValueBySet
     * Purpose: Calculates the combined value of all cards in a set.
     * Arguments: setName - the set whose cards should be included.
     * Return: The total value of the matching cards.
     */
    public double calculateValueBySet(String setName) {
        double total = 0;

        for (PokemonCard card : cards) {
            if (setName != null
                    && card.getSetName().equalsIgnoreCase(setName.trim())) {
                total += card.getCardValue() * card.getQuantity();
            }
        }

        return total;
    }

    /*
     * Method: calculateValueByName
     * Purpose: Calculates the combined value of cards with a matching name.
     * Arguments: cardName - the name of the cards to include.
     * Return: The total value of the matching cards.
     */
    public double calculateValueByName(String cardName) {
        double total = 0;

        for (PokemonCard card : cards) {
            if (cardName != null
                    && card.getCardName().equalsIgnoreCase(cardName.trim())) {
                total += card.getCardValue() * card.getQuantity();
            }
        }

        return total;
    }

    /*
     * Method: calculateValueByRarity
     * Purpose: Calculates the combined value of cards with a matching rarity.
     * Arguments: rarity - the rarity of the cards to include.
     * Return: The total value of the matching cards.
     */
    public double calculateValueByRarity(String rarity) {
        double total = 0;

        for (PokemonCard card : cards) {
            if (rarity != null
                    && card.getRarity().equalsIgnoreCase(rarity.trim())) {
                total += card.getCardValue() * card.getQuantity();
            }
        }

        return total;
    }

    /*
     * Method: findMatchingCard
     * Purpose: Finds a card with matching name, set, number, rarity,
     * and quality so matching records can be combined when appropriate.
     * Arguments: cardName, setName, cardNumber, rarity, and quality -
     * the identifying attributes to compare.
     * Return: The matching PokemonCard, or null if none match.
     */
    private PokemonCard findMatchingCard(
            String cardName, String setName, String cardNumber,
            String rarity, String quality) {
        if (cardName == null || setName == null || cardNumber == null
                || rarity == null || quality == null) {
            return null;
        }

        for (PokemonCard card : cards) {
            if (card.getCardName().equalsIgnoreCase(cardName.trim())
                    && card.getSetName().equalsIgnoreCase(setName.trim())
                    && card.getCardNumber().equalsIgnoreCase(cardNumber.trim())
                    && card.getRarity().equalsIgnoreCase(rarity.trim())
                    && card.getQuality().equalsIgnoreCase(quality.trim())) {
                return card;
            }
        }

        return null;
    }

    /*
     * Method: generateCardID
     * Purpose: Generates the next card ID and checks that it is not
     * already used in the collection.
     * Arguments: None.
     * Return: A unique card ID as a String.
     */
    private String generateCardID() {
        String cardID;

        do {
            cardID = String.format("CARD%04d", nextID);
            nextID++;
        } while (findCard(cardID) != null);

        return cardID;
    }
}
