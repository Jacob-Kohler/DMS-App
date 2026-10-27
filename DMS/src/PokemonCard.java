
/*
 * Name: Jacob Kohler
 * Course: CEN 3024
 * Date: October 26, 2026
 *
 * Class: PokemonCard
 * Description: Represents a Pokemon card in the DMS.
 * Stores the card's identifying information, rarity, quality,
 * value, and quantity. It validates card information
 * and provides a formatted display of the card and its total value.
 */

public class PokemonCard {
    private String cardID;
    private String cardName;
    private String setName;
    private String cardNumber;
    private String rarity;
    private String quality;
    private double cardValue;
    private int quantity;

    // Constructor that generates a unique ID for the card.
    public PokemonCard(String cardName, String setName, String cardNumber,
                       String rarity, String quality, double cardValue,
                       int quantity) {
        this("CARD-" + java.util.UUID.randomUUID(),
                cardName, setName, cardNumber, rarity, quality,
                cardValue, quantity);
    }

    // Constructor that initializes the card with the supplied ID.
    PokemonCard(String cardID, String cardName, String setName,
                String cardNumber, String rarity, String quality,
                double cardValue, int quantity) {
        if (cardID == null || cardID.trim().isEmpty()
                || cardName == null || cardName.trim().isEmpty()
                || setName == null || setName.trim().isEmpty()
                || cardNumber == null || cardNumber.trim().isEmpty()
                || rarity == null || rarity.trim().isEmpty()
                || quality == null || quality.trim().isEmpty()
                || !Double.isFinite(cardValue) || cardValue < 0
                || quantity <= 0) {
            throw new IllegalArgumentException("Invalid card information.");
        }

        this.cardID = cardID;
        this.cardName = cardName.trim();
        this.setName = setName.trim();
        this.cardNumber = cardNumber.trim();
        this.rarity = rarity.trim();
        this.quality = quality.trim();
        this.cardValue = cardValue;
        this.quantity = quantity;
    }

    // Returns the card ID.
    public String getCardID() {
        return cardID;
    }

    // Returns the card name.
    public String getCardName() {
        return cardName;
    }

    // Returns the card set name.
    public String getSetName() {
        return setName;
    }

    // Returns the card number.
    public String getCardNumber() {
        return cardNumber;
    }

    // Returns the card rarity.
    public String getRarity() {
        return rarity;
    }

    // Returns the card quality or condition.
    public String getQuality() {
        return quality;
    }

    // Returns the value of one card.
    public double getCardValue() {
        return cardValue;
    }

    // Returns the number of copies of the card.
    public int getQuantity() {
        return quantity;
    }

    // Updates the card name after validating the input.
    public void setCardName(String cardName) {
        if (cardName == null || cardName.trim().isEmpty()) {
            throw new IllegalArgumentException("Card name cannot be empty.");
        }
        this.cardName = cardName.trim();
    }

    // Updates the set name after validating the input.
    public void setSetName(String setName) {
        if (setName == null || setName.trim().isEmpty()) {
            throw new IllegalArgumentException("Set name cannot be empty.");
        }
        this.setName = setName.trim();
    }

    // Updates the card number after validating the input.
    public void setCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number cannot be empty.");
        }
        this.cardNumber = cardNumber.trim();
    }

    // Updates the card rarity after validating the input.
    public void setRarity(String rarity) {
        if (rarity == null || rarity.trim().isEmpty()) {
            throw new IllegalArgumentException("Rarity cannot be empty.");
        }
        this.rarity = rarity.trim();
    }

    // Updates the card quality after validating the input.
    public void setQuality(String quality) {
        if (quality == null || quality.trim().isEmpty()) {
            throw new IllegalArgumentException("Quality cannot be empty.");
        }
        this.quality = quality.trim();
    }

    // Updates the card value after validating the input.
    public void setCardValue(double cardValue) {
        if (!Double.isFinite(cardValue) || cardValue < 0) {
            throw new IllegalArgumentException(
                    "Card value must be a positive number.");
        }
        this.cardValue = cardValue;
    }

    // Updates the quantity after validating the input.
    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be a positive whole number.");
        }
        this.quantity = quantity;
    }

    /*
     * Method: toString
     * Purpose: Creates a formatted text description of the card,
     * including its identifying information, individual value,
     * quantity, and total value.
     * Arguments: None.
     * Return: A String containing the card details and total value.
     */
    @Override
    public String toString() {
        return "Card ID: " + cardID
                + "\nCard Name: " + cardName
                + "\nSet Name: " + setName
                + "\nCard Number: " + cardNumber
                + "\nRarity: " + rarity
                + "\nQuality: " + quality
                + "\nValue per Card: $" + String.format("%.2f", cardValue)
                + "\nQuantity: " + quantity
                + "\nTotal Value: $"
                + String.format("%.2f", cardValue * quantity);
    }
}
