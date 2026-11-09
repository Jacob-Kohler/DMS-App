
/*
 * Name: Jacob Kohler
 * Course: CEN 3024
 * Date: November 2, 2026
 *
 * Class: PokemonCardTest
 * Description: Tests the PokemonCard class to verify that card
 * objects are created correctly, validate their information,
 * update their attributes, and display their information properly.
 */

import org.junit.Test;
import static org.junit.Assert.*;

public class PokemonCardTest {

    /*
     * Method: testCreateValidCard
     * Purpose: Verifies that a card can be created with valid information.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testCreateValidCard() {
        PokemonCard card = new PokemonCard(
                "Mew ex", "Paldean Fates", "232/091",
                "Special Illustration Rare", "Near Mint (NM)",
                150.00, 2);

        assertEquals("Mew ex", card.getCardName());
        assertEquals("Paldean Fates", card.getSetName());
        assertEquals("232/091", card.getCardNumber());
        assertEquals("Special Illustration Rare", card.getRarity());
        assertEquals("Near Mint (NM)", card.getQuality());
        assertEquals(150.00, card.getCardValue(), 0.001);
        assertEquals(2, card.getQuantity());
    }

    /*
     * Method: testToString
     * Purpose: Verifies that the card information is formatted correctly.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testToString() {
        PokemonCard card = new PokemonCard(
                "Mew ex", "Paldean Fates", "232/091",
                "Rare", "Near Mint (NM)", 50.00, 2);

        String result = card.toString();

        assertTrue(result.contains("Mew ex"));
        assertTrue(result.contains("Paldean Fates"));
        assertTrue(result.contains("Total Value: $100.00"));
    }

    /*
     * Method: testSetters
     * Purpose: Verifies that card attributes can be updated correctly.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testSetters() {
        PokemonCard card = new PokemonCard(
                "Mew ex", "Paldean Fates", "232/091",
                "Rare", "Near Mint (NM)", 10.00, 1);

        card.setCardName("Pikachu");
        card.setSetName("Surging Sparks");
        card.setCardNumber("238/191");
        card.setRarity("Ultra Rare");
        card.setQuality("Lightly Played (LP)");
        card.setCardValue(25.00);
        card.setQuantity(3);

        assertEquals("Pikachu", card.getCardName());
        assertEquals("Surging Sparks", card.getSetName());
        assertEquals("238/191", card.getCardNumber());
        assertEquals("Ultra Rare", card.getRarity());
        assertEquals("Lightly Played (LP)", card.getQuality());
        assertEquals(25.00, card.getCardValue(), 0.001);
        assertEquals(3, card.getQuantity());
    }

    /*
     * Method: testAcceptZeroCardValue
     * Purpose: Verifies that a card with a zero value is accepted.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testAcceptZeroCardValue() {
        PokemonCard card = new PokemonCard(
                "Mew ex", "Paldean Fates", "232/091",
                "Rare", "Near Mint (NM)", 0.00, 1);

        assertEquals(0.00, card.getCardValue(), 0.001);
    }

    /*
     * Method: testRejectEmptyCardName
     * Purpose: Verifies that an empty card name is rejected.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testRejectEmptyCardName() {
        assertThrows(IllegalArgumentException.class, () ->
                new PokemonCard("", "Paldean Fates", "232/091",
                        "Rare", "Near Mint (NM)", 10.00, 1));
    }

    /*
     * Method: testRejectEmptySetName
     * Purpose: Verifies that an empty set name is rejected.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testRejectEmptySetName() {
        assertThrows(IllegalArgumentException.class, () ->
                new PokemonCard("Mew ex", "", "232/091",
                        "Rare", "Near Mint (NM)", 10.00, 1));
    }

    /*
     * Method: testRejectEmptyCardNumber
     * Purpose: Verifies that an empty card number is rejected.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testRejectEmptyCardNumber() {
        assertThrows(IllegalArgumentException.class, () ->
                new PokemonCard("Mew ex", "Paldean Fates", "",
                        "Rare", "Near Mint (NM)", 10.00, 1));
    }

    /*
     * Method: testRejectInvalidRarity
     * Purpose: Verifies that an unsupported rarity is rejected.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testRejectInvalidRarity() {
        assertThrows(IllegalArgumentException.class, () ->
                new PokemonCard("Mew ex", "Paldean Fates", "232/091",
                        "Mythical", "Near Mint (NM)", 10.00, 1));
    }

    /*
     * Method: testRejectInvalidQuality
     * Purpose: Verifies that an unsupported card quality is rejected.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testRejectInvalidQuality() {
        assertThrows(IllegalArgumentException.class, () ->
                new PokemonCard("Mew ex", "Paldean Fates", "232/091",
                        "Rare", "Perfect", 10.00, 1));
    }

    /*
     * Method: testRejectNegativeValue
     * Purpose: Verifies that a negative card value is rejected.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testRejectNegativeValue() {
        assertThrows(IllegalArgumentException.class, () ->
                new PokemonCard("Mew ex", "Paldean Fates", "232/091",
                        "Rare", "Near Mint (NM)", -10.00, 1));
    }

    /*
     * Method: testRejectNonFiniteValue
     * Purpose: Verifies that a non-finite card value is rejected.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testRejectNonFiniteValue() {
        assertThrows(IllegalArgumentException.class, () ->
                new PokemonCard("Mew ex", "Paldean Fates", "232/091",
                        "Rare", "Near Mint (NM)", Double.NaN, 1));
    }

    /*
     * Method: testRejectZeroQuantity
     * Purpose: Verifies that a card cannot be created with zero quantity.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testRejectZeroQuantity() {
        assertThrows(IllegalArgumentException.class, () ->
                new PokemonCard("Mew ex", "Paldean Fates", "232/091",
                        "Rare", "Near Mint (NM)", 10.00, 0));
    }

    /*
     * Method: testRejectNegativeQuantity
     * Purpose: Verifies that a negative card quantity is rejected.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testRejectNegativeQuantity() {
        assertThrows(IllegalArgumentException.class, () ->
                new PokemonCard("Mew ex", "Paldean Fates", "232/091",
                        "Rare", "Near Mint (NM)", 10.00, -1));
    }

    /*
     * Method: testSettersRejectInvalidValues
     * Purpose: Verifies that setters reject invalid information
     * without changing the existing card information.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testSettersRejectInvalidValues() {
        PokemonCard card = new PokemonCard(
                "Mew ex", "Paldean Fates", "232/091",
                "Rare", "Near Mint (NM)", 10.00, 1);

        assertThrows(IllegalArgumentException.class,
                () -> card.setCardName(""));

        assertThrows(IllegalArgumentException.class,
                () -> card.setSetName(""));

        assertThrows(IllegalArgumentException.class,
                () -> card.setCardNumber(""));

        assertThrows(IllegalArgumentException.class,
                () -> card.setRarity("Mythical"));

        assertThrows(IllegalArgumentException.class,
                () -> card.setQuality("Perfect"));

        assertThrows(IllegalArgumentException.class,
                () -> card.setCardValue(-10.00));

        assertThrows(IllegalArgumentException.class,
                () -> card.setQuantity(0));

        assertEquals("Mew ex", card.getCardName());
        assertEquals("Paldean Fates", card.getSetName());
        assertEquals("232/091", card.getCardNumber());
        assertEquals("Rare", card.getRarity());
        assertEquals("Near Mint (NM)", card.getQuality());
        assertEquals(10.00, card.getCardValue(), 0.001);
        assertEquals(1, card.getQuantity());
    }
}
