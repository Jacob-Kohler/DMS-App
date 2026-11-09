
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/*
 * Name: Jacob Kohler
 * Course: CEN 3024
 * Date: November 9, 2026
 *
 * Class: DMSGUI
 * Description: Provides a graphical interface for the Card Vendor
 * Data Management System. Allows users to manage, search, import,
 * and calculate Pokemon card inventory values.
 */
public class DMSGUI extends JFrame {

    private CardManager cardManager;
    private JTable cardTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JLabel totalCardsLabel;
    private JLabel collectionValueLabel;
    private JTextField searchField;

    // Initializes the GUI and card manager.
    public DMSGUI() {
        cardManager = new CardManager();

        setTitle("Pokemon Card DMS");
        setSize(1100, 650);
        setMinimumSize(new Dimension(950, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildGUI();
        refreshTable();
    }

    /*
     * Method: main
     * Purpose: Starts the graphical application.
     * Arguments: args - command-line arguments.
     * Return: None.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DMSGUI gui = new DMSGUI();
            gui.setVisible(true);
        });
    }

    /*
     * Method: buildGUI
     * Purpose: Creates the main window and its components.
     * Arguments: None.
     * Return: None.
     */
    private void buildGUI() {
        setLayout(new BorderLayout(10, 10));

        Color darkNavy = new Color(35, 45, 65);
        Color lightBackground = new Color(245, 247, 250);
        Color accentBlue = new Color(55, 115, 190);

        getContentPane().setBackground(lightBackground);

        // Header
        JPanel header = new JPanel(new BorderLayout(15, 10));
        header.setBackground(darkNavy);
        header.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("POKEMON CARD DMS");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        header.add(title, BorderLayout.WEST);

        // Summary panels
        JPanel summary = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 12, 0));
        summary.setBackground(darkNavy);

        JPanel cardCountPanel = new JPanel(new BorderLayout());
        cardCountPanel.setBackground(new Color(55, 70, 95));
        cardCountPanel.setBorder(
                BorderFactory.createEmptyBorder(8, 15, 8, 15));

        totalCardsLabel = new JLabel("Total Cards: 0");
        totalCardsLabel.setFont(new Font("Arial", Font.BOLD, 13));
        totalCardsLabel.setForeground(Color.WHITE);

        cardCountPanel.add(totalCardsLabel, BorderLayout.CENTER);

        JPanel valuePanel = new JPanel(new BorderLayout());
        valuePanel.setBackground(new Color(55, 70, 95));
        valuePanel.setBorder(
                BorderFactory.createEmptyBorder(8, 15, 8, 15));

        collectionValueLabel = new JLabel("Collection Value: $0.00");
        collectionValueLabel.setFont(new Font("Arial", Font.BOLD, 13));
        collectionValueLabel.setForeground(Color.WHITE);

        valuePanel.add(collectionValueLabel, BorderLayout.CENTER);

        summary.add(cardCountPanel);
        summary.add(valuePanel);

        header.add(summary, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Inventory table
        String[] columns = {
                "Card ID", "Card Name", "Set Name",
                "Card Number", "Rarity", "Quality",
                "Card Value", "Quantity", "Total Value"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cardTable = new JTable(tableModel);
        cardTable.setRowHeight(34);
        cardTable.setFont(new Font("Arial", Font.PLAIN, 13));
        cardTable.setForeground(new Color(45, 50, 60));
        cardTable.setBackground(Color.WHITE);
        cardTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        cardTable.setSelectionBackground(
                new Color(205, 225, 245));
        cardTable.setSelectionForeground(Color.BLACK);
        cardTable.setShowGrid(true);
        cardTable.setGridColor(new Color(225, 230, 237));
        cardTable.setIntercellSpacing(new Dimension(1, 1));
        cardTable.setFillsViewportHeight(true);

        // Alternating inventory row colors
        cardTable.setDefaultRenderer(Object.class,
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table, Object value, boolean isSelected,
                            boolean hasFocus, int row, int column) {

                        Component component =
                                super.getTableCellRendererComponent(
                                        table, value, isSelected,
                                        hasFocus, row, column);

                        if (isSelected) {
                            component.setBackground(
                                    new Color(205, 225, 245));
                        } else if (row % 2 == 0) {
                            component.setBackground(Color.WHITE);
                        } else {
                            component.setBackground(
                                    new Color(242, 246, 250));
                        }

                        return component;
                    }
                });

        // Table header
        cardTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13));
        cardTable.getTableHeader().setBackground(darkNavy);
        cardTable.getTableHeader().setForeground(Color.WHITE);
        cardTable.getTableHeader().setPreferredSize(
                new Dimension(0, 38));
        cardTable.getTableHeader().setReorderingAllowed(false);

        sorter = new TableRowSorter<>(tableModel);
        cardTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(cardTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 15, 5, 15),
                BorderFactory.createTitledBorder("Card Inventory")
        ));

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        // Controls
        JPanel controls = new JPanel(
                new GridLayout(2, 4, 12, 12));

        controls.setBackground(lightBackground);
        controls.setBorder(
                BorderFactory.createEmptyBorder(10, 15, 18, 15));

        addButton(controls, "Add Card", e -> addCard());
        addButton(controls, "Update Card", e -> updateCard());
        addButton(controls, "Remove Card", e -> removeCard());
        addButton(controls, "Import Cards", e -> importCards());

        addButton(controls, "Calculate Value",
                e -> calculateCollectionValue());

        addButton(controls, "Exit", e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit?",
                    "Exit DMS",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        addButton(controls, "Clear Search", e -> clearSearch());

        // Search Inventory button
        JButton searchButton = new JButton("Search Inventory");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(accentBlue);
        searchButton.setFocusPainted(false);
        searchButton.setOpaque(true);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.setBorder(
                BorderFactory.createEmptyBorder(12, 15, 12, 15));

        searchButton.addActionListener(e -> {
            searchField = new JTextField(20);

            JPanel searchPanel = new JPanel(new BorderLayout(8, 8));
            searchPanel.add(
                    new JLabel("Enter card name, set, ID, or rarity:"),
                    BorderLayout.NORTH);
            searchPanel.add(searchField, BorderLayout.CENTER);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    searchPanel,
                    "Search Inventory",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                searchCards();
            }
        });

        controls.add(searchButton);

        add(controls, BorderLayout.SOUTH);
    }

    // Creates a colored button and connects it to an operation.
    private void addButton(JPanel panel, String name,
                           java.awt.event.ActionListener action) {
        JButton button = new JButton(name);

        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(
                BorderFactory.createEmptyBorder(12, 15, 12, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        switch (name) {
            case "Add Card":
                button.setBackground(new Color(45, 145, 95));
                break;

            case "Update Card":
                button.setBackground(new Color(55, 115, 190));
                break;

            case "Remove Card":
                button.setBackground(new Color(195, 65, 65));
                break;

            case "Import Cards":
                button.setBackground(new Color(125, 85, 170));
                break;

            case "Calculate Value":
                button.setBackground(new Color(190, 140, 45));
                break;

            case "Clear Search":
                button.setBackground(new Color(105, 115, 130));
                break;

            case "Exit":
                button.setBackground(new Color(145, 45, 45));
                break;

            default:
                button.setBackground(new Color(80, 100, 125));
                break;
        }

        button.addActionListener(action);
        panel.add(button);
    }

    /*
     * Method: refreshTable
     * Purpose: Displays the current inventory and collection value.
     * Arguments: None.
     * Return: None.
     */
    private void refreshTable() {
        tableModel.setRowCount(0);

        ArrayList<PokemonCard> cards = cardManager.getAllCards();

        for (PokemonCard card : cards) {
            double total = card.getCardValue() * card.getQuantity();

            tableModel.addRow(new Object[]{
                    card.getCardID(),
                    card.getCardName(),
                    card.getSetName(),
                    card.getCardNumber(),
                    card.getRarity(),
                    card.getQuality(),
                    String.format("$%.2f", card.getCardValue()),
                    card.getQuantity(),
                    String.format("$%.2f", total)
            });
        }

        totalCardsLabel.setText("Total Cards: " + cards.size());

        collectionValueLabel.setText(
                String.format("Collection Value: $%.2f",
                        cardManager.calculateCollectionValue()));
    }

    /*
     * Method: addCard
     * Purpose: Collects card information and adds it to inventory.
     * Arguments: None.
     * Return: None.
     */
    private void addCard() {
        JTextField nameField = new JTextField();
        JTextField setField = new JTextField();
        JTextField numberField = new JTextField();
        JTextField valueField = new JTextField();
        JTextField quantityField = new JTextField();

        String[] rarities = {
                "Common", "Uncommon", "Rare", "Holo Rare",
                "Double Rare", "Ultra Rare", "Illustration Rare",
                "Special Illustration Rare", "Hyper Rare",
                "ACE SPEC Rare", "Amazing Rare", "Radiant Rare",
                "Shiny Rare", "Shiny Ultra Rare", "Black White Rare",
                "Mega Attack Rare", "Mega Hyper Rare", "Promo"
        };

        String[] qualities = {
                "Near Mint (NM)", "Lightly Played (LP)",
                "Moderately Played (MP)", "Heavily Played (HP)",
                "Damaged (DMG)"
        };

        JComboBox<String> rarityBox = new JComboBox<>(rarities);
        JComboBox<String> qualityBox = new JComboBox<>(qualities);

        JPanel panel = new JPanel(new GridLayout(7, 2, 8, 8));

        panel.add(new JLabel("Card Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Set Name:"));
        panel.add(setField);

        panel.add(new JLabel("Card Number:"));
        panel.add(numberField);

        panel.add(new JLabel("Rarity:"));
        panel.add(rarityBox);

        panel.add(new JLabel("Quality:"));
        panel.add(qualityBox);

        panel.add(new JLabel("Value per Card:"));
        panel.add(valueField);

        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Add Card",
                JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            PokemonCard card = cardManager.addCard(
                    nameField.getText().trim(),
                    setField.getText().trim(),
                    numberField.getText().trim(),
                    (String) rarityBox.getSelectedItem(),
                    (String) qualityBox.getSelectedItem(),
                    Double.parseDouble(valueField.getText().trim()),
                    Integer.parseInt(quantityField.getText().trim())
            );

            refreshTable();

            JOptionPane.showMessageDialog(this,
                    "Card added successfully!\nCard ID: "
                            + card.getCardID());

        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    /*
     * Method: updateCard
     * Purpose: Updates the selected card's quality, value, or quantity.
     * Arguments: None.
     * Return: None.
     */
    private void updateCard() {
        PokemonCard card = getSelectedCard();

        if (card == null) {
            return;
        }

        String[] options = {
                "Update Quality", "Update Value",
                "Update Quantity", "Update All", "Cancel"
        };

        String choice = (String) JOptionPane.showInputDialog(
                this,
                "Select an update option:",
                "Update Card",
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (choice == null || choice.equals("Cancel")) {
            return;
        }

        String[] qualities = {
                "Near Mint (NM)", "Lightly Played (LP)",
                "Moderately Played (MP)", "Heavily Played (HP)",
                "Damaged (DMG)"
        };

        JComboBox<String> qualityBox = new JComboBox<>(qualities);
        qualityBox.setSelectedItem(card.getQuality());

        JTextField valueField =
                new JTextField(String.valueOf(card.getCardValue()));

        JTextField quantityField =
                new JTextField(String.valueOf(card.getQuantity()));

        boolean changeQuality = choice.equals("Update Quality")
                || choice.equals("Update All");

        boolean changeValue = choice.equals("Update Value")
                || choice.equals("Update All");

        boolean changeQuantity = choice.equals("Update Quantity")
                || choice.equals("Update All");

        qualityBox.setEnabled(changeQuality);
        valueField.setEnabled(changeValue);
        quantityField.setEnabled(changeQuantity);

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));

        panel.add(new JLabel("Quality:"));
        panel.add(qualityBox);

        panel.add(new JLabel("Value:"));
        panel.add(valueField);

        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Edit Card",
                JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String quality = card.getQuality();
            double value = card.getCardValue();
            int quantity = card.getQuantity();

            if (changeQuality) {
                quality = (String) qualityBox.getSelectedItem();
            }

            if (changeValue) {
                value = Double.parseDouble(valueField.getText().trim());
            }

            if (changeQuantity) {
                quantity = Integer.parseInt(
                        quantityField.getText().trim());
            }

            PokemonCard updated = cardManager.updateCard(
                    card.getCardID(), quality, value, quantity);

            if (updated == null) {
                showError("Unable to update card.");
            } else {
                JOptionPane.showMessageDialog(this,
                        quantity == 0
                                ? "Card removed from inventory."
                                : "Card updated successfully.");
            }

            refreshTable();

        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    /*
     * Method: removeCard
     * Purpose: Removes a selected quantity of a card from inventory.
     * Arguments: None.
     * Return: None.
     */
    private void removeCard() {
        PokemonCard card = getSelectedCard();

        if (card == null) {
            return;
        }

        String input = JOptionPane.showInputDialog(
                this,
                "How many would you like to remove?");

        if (input == null) {
            return;
        }

        try {
            int quantity = Integer.parseInt(input.trim());

            if (quantity < 1 || quantity > card.getQuantity()) {
                showError("Invalid Quantity. How many would you like to remove?");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Remove " + quantity + " copies of "
                            + card.getCardName() + "?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            PokemonCard removed = cardManager.removeCard(
                    card.getCardID(), quantity);

            if (removed == null) {
                showError("Unable to remove card.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Inventory updated successfully.");
            }

            refreshTable();

        } catch (NumberFormatException exception) {
            showError("Enter a valid number.");
        }
    }

    /*
     * Method: getSelectedCard
     * Purpose: Finds the card selected in the inventory table.
     * Arguments: None.
     * Return: The selected card or null.
     */
    private PokemonCard getSelectedCard() {
        int selectedRow = cardTable.getSelectedRow();

        if (selectedRow == -1) {
            showError("Please select a card from the inventory.");
            return null;
        }

        int modelRow = cardTable.convertRowIndexToModel(selectedRow);
        String cardID = (String) tableModel.getValueAt(modelRow, 0);

        for (PokemonCard card : cardManager.getAllCards()) {
            if (card.getCardID().equalsIgnoreCase(cardID)) {
                return card;
            }
        }

        showError("Card could not be found.");
        return null;
    }

    /*
     * Method: searchCards
     * Purpose: Filters inventory using the entered search text.
     * Arguments: None.
     * Return: None.
     */
    private void searchCards() {
        String search = searchField.getText().trim();

        if (search.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(
                    RowFilter.regexFilter(
                            "(?i)" + java.util.regex.Pattern.quote(search)));
        }
    }

    // Clears the inventory search filter.
    private void clearSearch() {
        if (searchField != null) {
            searchField.setText("");
        }

        sorter.setRowFilter(null);
    }

    /*
     * Method: importCards
     * Purpose: Imports card records from a selected text file.
     * Arguments: None.
     * Return: None.
     */
    private void importCards() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Card Data File");

        int result = chooser.showOpenDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        System.setProperty("dms.importFile", file.getAbsolutePath());

        try {
            ArrayList<PokemonCard> imported =
                    cardManager.importCards();

            refreshTable();

            JOptionPane.showMessageDialog(this,
                    "Import completed.\nImported records: "
                            + imported.size());

        } finally {
            System.clearProperty("dms.importFile");
        }
    }

    /*
     * Method: calculateCollectionValue
     * Purpose: Calculates the collection value by collection,
     * set, card name, or rarity.
     * Arguments: None.
     * Return: None.
     */
    private void calculateCollectionValue() {
        String[] options = {
                "Entire Collection",
                "By Set",
                "By Card Name",
                "By Rarity"
        };

        String choice = (String) JOptionPane.showInputDialog(
                this,
                "What would you like to calculate collection value of?:",
                "Collection Value",
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (choice == null) {
            return;
        }

        double total;

        if (choice.equals("Entire Collection")) {
            total = cardManager.calculateCollectionValue();

        } else if (choice.equals("By Set")) {
            String set = JOptionPane.showInputDialog(
                    this, "Enter set name:");

            if (set == null) {
                return;
            }

            total = cardManager.calculateValueBySet(set);

        } else if (choice.equals("By Card Name")) {
            String name = JOptionPane.showInputDialog(
                    this, "Enter card name:");

            if (name == null) {
                return;
            }

            total = cardManager.calculateValueByName(name);

        } else {
            String[] rarities = {
                    "Common", "Uncommon", "Rare", "Holo Rare",
                    "Double Rare", "Ultra Rare", "Illustration Rare",
                    "Special Illustration Rare", "Hyper Rare",
                    "ACE SPEC Rare", "Amazing Rare", "Radiant Rare",
                    "Shiny Rare", "Shiny Ultra Rare", "Black White Rare",
                    "Mega Attack Rare", "Mega Hyper Rare", "Promo"
            };

            String rarity = (String) JOptionPane.showInputDialog(
                    this,
                    "Select rarity:",
                    "Calculate by Rarity",
                    JOptionPane.QUESTION_MESSAGE,
                    null, rarities, rarities[0]);

            if (rarity == null) {
                return;
            }

            total = cardManager.calculateValueByRarity(rarity);
        }

        JOptionPane.showMessageDialog(this,
                String.format("Collection Value: $%.2f", total));
    }

    // Displays an error message.
    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
    }
}
