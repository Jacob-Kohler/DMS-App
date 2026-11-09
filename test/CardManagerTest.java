
/*
 * Name: Jacob Kohler
 * Course: CEN 3024
 * Date: November 2, 2026
 *
 * Class: CardManagerTest
 * Description: Tests the CardManager class to verify that card
 * inventory can be added, removed, updated, searched, imported
 * from files, and used for collection value calculations.
 */

import org.junit.Test;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CardManagerTest {

    /*
     * Method: createManager
     * Purpose: Creates a new CardManager for testing.
     * Arguments: None.
     * Return: A new CardManager object.
     */
    private CardManager createManager() {
        return new CardManager();
    }

    /*
     * Method: addSampleCard
     * Purpose: Adds a sample card to the supplied CardManager.
     * Arguments: manager - the CardManager receiving the card.
     * Return: The newly added PokemonCard object.
     */
    private PokemonCard addSampleCard(CardManager manager) {
        return manager.addCard(
                "Mew ex", "Paldean Fates", "232/091",
                "Special Illustration Rare", "Near Mint (NM)",
                150.00, 2);
    }

    // ADD OBJECTS

    /*
     * Method: testAddCard
     * Purpose: Verifies that a card can be added to inventory.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testAddCard() {
        CardManager manager = createManager();

        PokemonCard card = addSampleCard(manager);

        assertNotNull(card);
        assertEquals("Mew ex", card.getCardName());
        assertEquals(2, card.getQuantity());
        assertEquals("CARD0001", card.getCardID());
    }

    /*
     * Method: testGenerateUniqueIDs
     * Purpose: Verifies that each new card receives a unique ID.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testGenerateUniqueIDs() {
        CardManager manager = createManager();

        PokemonCard firstCard = addSampleCard(manager);

        PokemonCard secondCard = manager.addCard(
                "Pikachu", "Surging Sparks", "238/191",
                "Ultra Rare", "Near Mint (NM)", 50.00, 1);

        assertNotEquals(firstCard.getCardID(), secondCard.getCardID());
        assertEquals("CARD0001", firstCard.getCardID());
        assertEquals("CARD0002", secondCard.getCardID());
    }

    /*
     * Method: testCombineMatchingCards
     * Purpose: Verifies that matching cards combine their quantities.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testCombineMatchingCards() {
        CardManager manager = createManager();

        PokemonCard firstCard = addSampleCard(manager);

        PokemonCard secondCard = manager.addCard(
                "Mew ex", "Paldean Fates", "232/091",
                "Special Illustration Rare", "Near Mint (NM)",
                150.00, 3);

        assertEquals(firstCard.getCardID(), secondCard.getCardID());
        assertEquals(5, secondCard.getQuantity());
        assertEquals(1, manager.getAllCards().size());
    }

    /*
     * Method: testDifferentValuesCreateSeparateCards
     * Purpose: Verifies that cards with different values remain separate.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testDifferentValuesCreateSeparateCards() {
        CardManager manager = createManager();

        addSampleCard(manager);

        manager.addCard(
                "Mew ex", "Paldean Fates", "232/091",
                "Special Illustration Rare", "Near Mint (NM)",
                175.00, 1);

        assertEquals(2, manager.getAllCards().size());
    }

    /*
     * Method: testRejectInvalidCard
     * Purpose: Verifies that invalid card information is rejected.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testRejectInvalidCard() {
        CardManager manager = createManager();

        assertThrows(IllegalArgumentException.class, () ->
                manager.addCard(
                        "", "Paldean Fates", "232/091",
                        "Rare", "Near Mint (NM)", 10.00, 1));
    }

    // REMOVE OBJECTS

    /*
     * Method: testRemovePartialQuantity
     * Purpose: Verifies that part of a card's quantity can be removed.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testRemovePartialQuantity() {
        CardManager manager = createManager();

        addSampleCard(manager);

        PokemonCard result = manager.removeCard("CARD0001", 1);

        assertNotNull(result);
        assertEquals(1, result.getQuantity());
        assertEquals(1, manager.getAllCards().size());
    }

    /*
     * Method: testRemoveEntireCard
     * Purpose: Verifies that a card is removed when its entire quantity
     * is removed from inventory.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testRemoveEntireCard() {
        CardManager manager = createManager();

        addSampleCard(manager);

        PokemonCard result = manager.removeCard("CARD0001", 2);

        assertNotNull(result);
        assertNull(manager.findCard("CARD0001"));
        assertEquals(0, manager.getAllCards().size());
    }

    /*
     * Method: testRejectInvalidRemoval
     * Purpose: Verifies that removing more cards than are available
     * does not change inventory.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testRejectInvalidRemoval() {
        CardManager manager = createManager();

        addSampleCard(manager);

        PokemonCard result = manager.removeCard("CARD0001", 3);

        assertNull(result);
        assertEquals(2, manager.findCard("CARD0001").getQuantity());
        assertEquals(1, manager.getAllCards().size());
    }

    /*
     * Method: testRemoveNonexistentCard
     * Purpose: Verifies that removing a nonexistent card does
     * not change inventory.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testRemoveNonexistentCard() {
        CardManager manager = createManager();

        addSampleCard(manager);

        PokemonCard result = manager.removeCard("CARD9999", 1);

        assertNull(result);
        assertEquals(1, manager.getAllCards().size());
        assertEquals(2, manager.findCard("CARD0001").getQuantity());
    }

    // UPDATE OBJECTS

    /*
     * Method: testUpdateCard
     * Purpose: Verifies that card quality, value, and quantity
     * can be updated.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testUpdateCard() {
        CardManager manager = createManager();

        addSampleCard(manager);

        PokemonCard updatedCard = manager.updateCard(
                "CARD0001", "Lightly Played (LP)", 125.00, 3);

        assertNotNull(updatedCard);
        assertEquals("Lightly Played (LP)", updatedCard.getQuality());
        assertEquals(125.00, updatedCard.getCardValue(), 0.001);
        assertEquals(3, updatedCard.getQuantity());
    }

    /*
     * Method: testUpdateQuantityToZeroDeletesCard
     * Purpose: Verifies that updating quantity to zero removes the card.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testUpdateQuantityToZeroDeletesCard() {
        CardManager manager = createManager();

        addSampleCard(manager);

        PokemonCard result = manager.updateCard(
                "CARD0001", "Near Mint (NM)", 150.00, 0);

        assertNotNull(result);
        assertNull(manager.findCard("CARD0001"));
        assertEquals(0, manager.getAllCards().size());
    }

    /*
     * Method: testRejectInvalidUpdate
     * Purpose: Verifies that an invalid card update is rejected
     * without changing the original card.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testRejectInvalidUpdate() {
        CardManager manager = createManager();

        PokemonCard originalCard = addSampleCard(manager);

        PokemonCard result = manager.updateCard(
                "CARD0001", "Perfect", 150.00, 2);

        assertNull(result);
        assertEquals("Near Mint (NM)", originalCard.getQuality());
    }

    /*
     * Method: testUpdateNonexistentCard
     * Purpose: Verifies that updating a nonexistent card fails
     * without changing inventory.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testUpdateNonexistentCard() {
        CardManager manager = createManager();

        addSampleCard(manager);

        PokemonCard result = manager.updateCard(
                "CARD9999", "Lightly Played (LP)", 125.00, 3);

        assertNull(result);
        assertEquals(1, manager.getAllCards().size());
        assertEquals("Near Mint (NM)",
                manager.findCard("CARD0001").getQuality());
    }

    // CUSTOM ACTION: COLLECTION VALUE CALCULATIONS

    /*
     * Method: testCalculateCollectionValue
     * Purpose: Verifies the total value of all cards in inventory.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testCalculateCollectionValue() {
        CardManager manager = createManager();

        addSampleCard(manager);

        manager.addCard(
                "Pikachu", "Surging Sparks", "238/191",
                "Ultra Rare", "Near Mint (NM)", 50.00, 1);

        assertEquals(350.00,
                manager.calculateCollectionValue(), 0.001);
    }

    /*
     * Method: testCalculateValueBySet
     * Purpose: Verifies the total value of cards from a specific set.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testCalculateValueBySet() {
        CardManager manager = createManager();

        addSampleCard(manager);

        manager.addCard(
                "Pikachu", "Surging Sparks", "238/191",
                "Ultra Rare", "Near Mint (NM)", 50.00, 1);

        assertEquals(300.00,
                manager.calculateValueBySet("Paldean Fates"), 0.001);
    }

    /*
     * Method: testCalculateValueByName
     * Purpose: Verifies the total value of cards with a specific name.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testCalculateValueByName() {
        CardManager manager = createManager();

        addSampleCard(manager);

        assertEquals(300.00,
                manager.calculateValueByName("Mew ex"), 0.001);
    }

    /*
     * Method: testCalculateValueByRarity
     * Purpose: Verifies the total value of cards with a specific rarity.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testCalculateValueByRarity() {
        CardManager manager = createManager();

        addSampleCard(manager);

        assertEquals(300.00,
                manager.calculateValueByRarity(
                        "Special Illustration Rare"), 0.001);
    }

    /*
     * Method: testCalculateCollectionValueWhenInventoryIsEmpty
     * Purpose: Verifies that an empty inventory has a total value of zero.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testCalculateCollectionValueWhenInventoryIsEmpty() {
        CardManager manager = createManager();

        assertEquals(0.00, manager.calculateCollectionValue(), 0.001);
    }

    /*
     * Method: testCalculateValueWithNoMatchingCards
     * Purpose: Verifies that filtered calculations return zero
     * when no cards match the requested criteria.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testCalculateValueWithNoMatchingCards() {
        CardManager manager = createManager();

        addSampleCard(manager);

        assertEquals(0.00,
                manager.calculateValueBySet("Surging Sparks"), 0.001);

        assertEquals(0.00,
                manager.calculateValueByName("Pikachu"), 0.001);

        assertEquals(0.00,
                manager.calculateValueByRarity("Ultra Rare"), 0.001);
    }

    // OPENING AND IMPORTING FILES

    /*
     * Method: testImportCards
     * Purpose: Verifies that a text file can be opened and its
     * valid card records imported into inventory.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testImportCards() throws Exception {
        CardManager manager = createManager();

        File file = File.createTempFile("dmsTest", ".txt");

        try {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println(
                        "Mew ex-Paldean Fates-232/091-Special Illustration Rare-Near Mint (NM)-150.00-2");
                writer.println(
                        "Pikachu-Surging Sparks-238/191-Ultra Rare-Near Mint (NM)-50.00-1");
            }

            System.setProperty("dms.importFile", file.getAbsolutePath());

            ArrayList<PokemonCard> importedCards = manager.importCards();

            assertEquals(2, importedCards.size());
            assertEquals(2, manager.getAllCards().size());
            assertEquals("Mew ex", importedCards.get(0).getCardName());
            assertEquals("Pikachu", importedCards.get(1).getCardName());
        } finally {
            System.clearProperty("dms.importFile");
            file.delete();
        }
    }

    /*
     * Method: testImportInvalidFilePath
     * Purpose: Verifies that an invalid file path is handled without
     * adding records to inventory.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testImportInvalidFilePath() throws Exception {
        CardManager manager = createManager();

        File file = File.createTempFile("dmsMissing", ".txt");

        try {
            assertTrue(file.delete());

            System.setProperty("dms.importFile", file.getAbsolutePath());

            ArrayList<PokemonCard> importedCards = manager.importCards();

            assertNotNull(importedCards);
            assertTrue(importedCards.isEmpty());
            assertTrue(manager.getAllCards().isEmpty());
        } finally {
            System.clearProperty("dms.importFile");
        }
    }

    // SEARCH AND DISPLAY INVENTORY

    /*
     * Method: testFindCard
     * Purpose: Verifies that cards can be found using their IDs.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testFindCard() {
        CardManager manager = createManager();

        PokemonCard card = addSampleCard(manager);

        assertEquals(card, manager.findCard("CARD0001"));
        assertEquals(card, manager.findCard("card0001"));
        assertNull(manager.findCard("CARD9999"));
    }

    /*
     * Method: testSearchCards
     * Purpose: Verifies that cards can be searched by set, name,
     * rarity, and quality.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testSearchCards() {
        CardManager manager = createManager();

        addSampleCard(manager);

        manager.addCard(
                "Pikachu", "Surging Sparks", "238/191",
                "Ultra Rare", "Lightly Played (LP)", 50.00, 1);

        assertEquals(1, manager.findBySet("Paldean Fates").size());
        assertEquals(1, manager.findByName("Mew ex").size());
        assertEquals(1, manager.findByRarity("Ultra Rare").size());
        assertEquals(1, manager.findByQuality("Near Mint (NM)").size());
    }

    /*
     * Method: testFindByValue
     * Purpose: Verifies that cards can be searched by value comparisons.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testFindByValue() {
        CardManager manager = createManager();

        addSampleCard(manager);

        manager.addCard(
                "Pikachu", "Surging Sparks", "238/191",
                "Ultra Rare", "Near Mint (NM)", 50.00, 1);

        assertEquals(1, manager.findByValue(100.00, "greater").size());
        assertEquals(1, manager.findByValue(100.00, "less").size());
        assertEquals(1, manager.findByValue(150.00, "equal").size());
    }

    /*
     * Method: testSearchCardsNoMatches
     * Purpose: Verifies that searches return empty lists when
     * no cards match the search criteria.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testSearchCardsNoMatches() {
        CardManager manager = createManager();

        addSampleCard(manager);

        assertTrue(manager.findBySet("Surging Sparks").isEmpty());
        assertTrue(manager.findByName("Pikachu").isEmpty());
        assertTrue(manager.findByRarity("Ultra Rare").isEmpty());
        assertTrue(manager.findByQuality("Damaged").isEmpty());
    }

    /*
     * Method: testFindByValueNoMatches
     * Purpose: Verifies that value searches return no results
     * when no cards meet the requested comparison.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testFindByValueNoMatches() {
        CardManager manager = createManager();

        addSampleCard(manager);

        assertTrue(manager.findByValue(200.00, "greater").isEmpty());
        assertTrue(manager.findByValue(100.00, "less").isEmpty());
    }

    /*
     * Method: testGetAllCards
     * Purpose: Verifies that all inventory cards can be retrieved
     * without allowing changes to the returned list to affect inventory.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testGetAllCards() {
        CardManager manager = createManager();

        addSampleCard(manager);

        ArrayList<PokemonCard> cards = manager.getAllCards();

        assertEquals(1, cards.size());

        cards.clear();

        assertEquals(1, manager.getAllCards().size());
    }

    /*
     * Method: testGetAllCardsWhenInventoryIsEmpty
     * Purpose: Verifies that an empty inventory returns an empty list.
     * Arguments: None.
     * Return: void.
     */
    @Test
    public void testGetAllCardsWhenInventoryIsEmpty() {
        CardManager manager = createManager();

        ArrayList<PokemonCard> cards = manager.getAllCards();

        assertNotNull(cards);
        assertTrue(cards.isEmpty());
    }
}
