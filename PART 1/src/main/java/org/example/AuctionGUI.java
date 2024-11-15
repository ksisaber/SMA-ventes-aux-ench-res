package org.example;

import javax.swing.*;

import jade.core.Profile;
import jade.wrapper.ControllerException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;

public class AuctionGUI extends JFrame {

    private JTextField buyersField;
    private JTextField priceField;
    private JButton startAuctionButton;
    private JTextArea outputArea;

    public AuctionGUI() {
        setTitle("Auction System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create panel for inputs
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Number of Buyers:"));
        buyersField = new JTextField();
        inputPanel.add(buyersField);

        inputPanel.add(new JLabel("Starting Price:"));
        priceField = new JTextField();
        inputPanel.add(priceField);

        startAuctionButton = new JButton("Start Auction");
        inputPanel.add(new JLabel());
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
        String buyersText = buyersField.getText();
        String priceText = priceField.getText();

        try {
            int numOfBuyers = Integer.parseInt(buyersText);
            int startPrice = Integer.parseInt(priceText);

            // Update the number of buyers in Main
            Main.nb_buyers = numOfBuyers;

            // Start the auction
            startAuction(startPrice);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for buyers and price.");
        }
    }

    private void startAuction(int startPrice) {
        try {
            jade.core.Runtime rt = jade.core.Runtime.instance();
            jade.core.ProfileImpl p = new jade.core.ProfileImpl(false);
            p.setParameter(Profile.GUI, "true");
            p.setParameter(Profile.LOCAL_HOST, "localhost");
            jade.wrapper.AgentContainer container1 = rt.createMainContainer(p);

            // duration in seconds
            Object[] bidDuration = {"1"};
            jade.wrapper.AgentController countdownAgent = container1.createNewAgent("Countdown", "org.example.Duration", bidDuration);
            countdownAgent.start(); // Start CountDown Agent

            for (int i = 0; i < Main.nb_buyers; i++) {
                jade.wrapper.AgentController buyer = container1.createNewAgent("Buyer0".concat(String.valueOf(i)), "org.example.Buyer", null);
                buyer.start(); // Start buyers
            }

            // Define arguments for Seller
            Object[] startPriceArgs = {String.valueOf(startPrice)};
            jade.wrapper.AgentController seller = container1.createNewAgent("Seller", "org.example.Seller", startPriceArgs);
            seller.start(); // Start seller

            container1.start();

        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AuctionGUI().setVisible(true);
            }
        });
    }
}


