package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;

public class AuctionGUI extends JFrame {

    private JTextField sellersField;
    private JButton startAuctionButton;
    private JTextArea outputArea;

    public AuctionGUI() {
        setTitle("Auction System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create panel for inputs
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));

        inputPanel.add(new JLabel("Number of Sellers:"));
        sellersField = new JTextField();
        inputPanel.add(sellersField);

        startAuctionButton = new JButton("Start Auction");
        inputPanel.add(new JLabel()); // Empty cell for layout
        inputPanel.add(startAuctionButton);

        add(inputPanel, BorderLayout.NORTH);

        // Create panel for output
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        // Redirect console output to text area
        PrintStream printStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                outputArea.append(String.valueOf((char) b));
            }
        });
        System.setOut(printStream);
        System.setErr(printStream);

        // Add button action
        startAuctionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startAuction();
            }
        });
    }

    private void startAuction() {
        String sellersText = sellersField.getText();

        try {
            int numOfSellers = Integer.parseInt(sellersText);

            // Start the auction with the specified number of sellers
            AuctionStarter.startAuction(numOfSellers);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of sellers.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuctionGUI().setVisible(true));
    }
}
