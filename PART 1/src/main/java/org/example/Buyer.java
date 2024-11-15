package org.example;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Random;

import static org.example.Main.nb_buyers;



public class Buyer extends Agent {
    // Variable to keep track of the highest price received so far
    int highestPrice = 0;
    static int activeAgents = nb_buyers ;

    @Override
    protected void setup() {
        // Initialize a random generator and an array of bid increment values
        Random randomGenerator = new Random();
        int[] bidIncrements = {50, 100, 200, 300, 500, 1000};

        // Add cyclic behavior to the agent
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                try {
                    // Sleep for 3 seconds before performing any action
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // Define message templates for receiving highest price updates
                MessageTemplate highestPriceTemplate = MessageTemplate.or(
                        MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                        MessageTemplate.MatchPerformative(ACLMessage.PROPOSE)
                );
                ACLMessage highestPriceMessage = receive(highestPriceTemplate);

                // Check if a new highest price has been received
                if (highestPriceMessage != null && Integer.parseInt(highestPriceMessage.getContent()) > highestPrice) {
                    highestPrice = Integer.parseInt(highestPriceMessage.getContent());

                    // Log the received starting price if it's a proposal message
                    if (highestPriceMessage.getPerformative() == ACLMessage.PROPOSE) {
                        System.out.println("Seller set starting price at: " + highestPrice + "$ MSG: received from " + highestPriceMessage.getSender().getLocalName());
                    }

                    // Send a new proposition to the seller with an incremented bid
                    int newBid = highestPrice + bidIncrements[randomGenerator.nextInt(bidIncrements.length)];
                    ACLMessage bidMessage = new ACLMessage(ACLMessage.PROPOSE);
                    bidMessage.setContent(String.valueOf(newBid));
                    bidMessage.addReceiver(new AID("Seller", AID.ISLOCALNAME));
                    myAgent.send(bidMessage);
                }

                // Define a message template for receiving the end of bid countdown
                MessageTemplate countdownTemplate = MessageTemplate.MatchPerformative(ACLMessage.DISCONFIRM);
                ACLMessage countdownMessage = receive(countdownTemplate);

                // Check if the countdown message indicates the end of the auction
                if (countdownMessage != null && countdownMessage.getContent().equals("done")) {
                    System.out.println(myAgent.getLocalName() + " is deleted");
                    myAgent.doDelete(); // Terminate the agent
                    
                }
            }
        });
    }

}
