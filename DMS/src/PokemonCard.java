/*
 * Name: Jacob Kohler
 * Course: CEN 3024
 * Date: November 2, 2026
 *
 * Class: PokemonCard
 * Description: Represents a Pokemon card in the DMS.
 * Stores and validates the card's identifying information,
 * rarity, quality, value, and quantity.
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
                || !Double.isFinite(cardValue) || cardValue < 0
                || quantity <= 0) {
            throw new IllegalArgumentException("Invalid card information.");
        }

        this.cardID = cardID;
        this.cardName = cardName.trim();
        this.setName = setName.trim();
        this.cardNumber = cardNumber.trim();
        this.rarity = validateRarity(rarity);
        this.quality = validateQuality(quality);
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

    // Updates the card rarity using one of the supported options.
    public void setRarity(String rarity) {
        this.rarity = validateRarity(rarity);
    }

    // Updates the card quality using one of the supported options.
    public void setQuality(String quality) {
        this.quality = validateQuality(quality);
    }

    // Updates the card value after validating the input.
    public void setCardValue(double cardValue) {
        if (!Double.isFinite(cardValue) || cardValue < 0) {
            throw new IllegalArgumentException(
                    "Card value cannot be negative.");
        }
        this.cardValue = cardValue;
    }

    // Updates the quantity after validating the input.
    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be more than 1.");
        }
        this.quantity = quantity;
    }


    /*
     * Method: validateRarity
     * Purpose: Checks the rarity against supported Pokémon card rarities.
     * Arguments: rarity - the rarity entered for the card.
     * Return: The matching standardized rarity name.
     */
    private String validateRarity(String rarity) {
        String[] validRarities = {
                // Basic rarities
                "Common",
                "Uncommon",
                "Rare",
                "Holo Rare",

                // Modern rarities
                "Double Rare",
                "Ultra Rare",
                "Illustration Rare",
                "Special Illustration Rare",
                "Hyper Rare",

                // Special rarities
                "ACE SPEC Rare",
                "Amazing Rare",
                "Radiant Rare",
                "Shiny Rare",
                "Shiny Ultra Rare",
                "Black White Rare",
                "Mega Attack Rare",
                "Mega Hyper Rare",

                // Promotional classification
                "Promo"
        };

        if (rarity != null) {
            for (String validRarity : validRarities) {
                if (validRarity.equalsIgnoreCase(rarity.trim())) {
                    return validRarity;
                }
            }
        }

        throw new IllegalArgumentException(
                "Invalid rarity. Please select a supported Pokémon card rarity.");
    }



    /*
     * Method: validateQuality
     * Purpose: Checks the card condition against supported choices.
     * Arguments: quality - the condition entered for the card.
     * Return: The matching standardized quality name.
     */
    private String validateQuality(String quality) {
        String[][] validQualities = {
                {"Near Mint", "Near Mint (NM)"},
                {"NM", "Near Mint (NM)"},
                {"(NM)", "Near Mint (NM)"},
                {"Lightly Played", "Lightly Played (LP)"},
                {"LP", "Lightly Played (LP)"},
                {"(LP)", "Lightly Played (LP)"},
                {"Moderately Played", "Moderately Played (MP)"},
                {"MP", "Moderately Played (MP)"},
                {"(MP)", "Moderately Played (MP)"},
                {"Heavily Played", "Heavily Played (HP)"},
                {"HP", "Heavily Played (HP)"},
                {"(HP)", "Heavily Played (HP)"},
                {"Damaged", "Damaged (DMG)"},
                {"DMG", "Damaged (DMG)"},
                {"(DMG)", "Damaged (DMG)"}
        };

        if (quality != null) {
            for (String[] validQuality : validQualities) {
                if (validQuality[0].equalsIgnoreCase(quality.trim())
                        || validQuality[1].equalsIgnoreCase(quality.trim())) {
                    return validQuality[1];
                }
            }
        }

        throw new IllegalArgumentException("Invalid quality.");
    }


    /*
     * Method: toString
     * Purpose: Creates a formatted description of the card, including
     * its identifying information, individual value, quantity, and total.
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
