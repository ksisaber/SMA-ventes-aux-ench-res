package org.example;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import static org.example.Main.nb_buyers;

public class Seller extends Agent {

    // Variable to keep track of the current highest price
    int currentHighestPrice = 0;
    // Variable to store the name of the buyer with the highest offer
    String highestOfferBuyer;

    @Override
    protected void setup() {
        // Get the starting price from the agent arguments
        Object[] args = this.getArguments();
        int startPrice = Integer.parseInt(args[0].toString());
        currentHighestPrice = startPrice;

        // Sending the start price to all buyers
        ACLMessage startPriceMessage = new ACLMessage(ACLMessage.PROPOSE);
        startPriceMessage.setContent(String.valueOf(startPrice));
        for (int i = 0; i < nb_buyers; i++) {
            startPriceMessage.addReceiver(new AID("Buyer0".concat(String.valueOf(i)), AID.ISLOCALNAME));
        }
        this.send(startPriceMessage);

        // Add cyclic behavior to handle incoming messages
        this.addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                // Define message template for receiving price offers
                MessageTemplate priceOfferTemplate = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
                ACLMessage priceOfferMessage = receive(priceOfferTemplate); // Receive the price offer
                
                // Check if a new price offer has been received and if it is higher than the current highest price
                if (priceOfferMessage != null && Integer.parseInt(priceOfferMessage.getContent()) > currentHighestPrice) {
                    
                    // Update the highest price and the buyer with the highest offer
                    System.out.println("Highest price " + priceOfferMessage.getContent() + " from " + priceOfferMessage.getSender().getName());
                    currentHighestPrice = Integer.parseInt(priceOfferMessage.getContent());
                    highestOfferBuyer = String.valueOf(priceOfferMessage.getSender().getName());

                    // Send the updated highest price to all buyers
                    ACLMessage updatedPriceMessage = new ACLMessage(ACLMessage.INFORM);
                    updatedPriceMessage.setContent(String.valueOf(currentHighestPrice));
                    for (int i = 0; i < nb_buyers; i++) {
                        updatedPriceMessage.addReceiver(new AID("Buyer0".concat(String.valueOf(i)), AID.ISLOCALNAME));
                    }
                    myAgent.send(updatedPriceMessage);
                }

                // Define message template for receiving the end of bid countdown
                MessageTemplate countdownTemplate = MessageTemplate.MatchPerformative(ACLMessage.DISCONFIRM);
                ACLMessage countdownMessage = receive(countdownTemplate);
                
                // Check if the countdown message indicates the end of the auction
                if (countdownMessage != null && countdownMessage.getContent().equals("done")) {
                    System.out.printf("Highest price is " + currentHighestPrice + "$. Product has been sold to " + highestOfferBuyer);
                    System.out.println(myAgent.getLocalName() + " is deleted");
                    myAgent.doDelete(); // Terminate the agent
                }
            }
        });
    }
}
