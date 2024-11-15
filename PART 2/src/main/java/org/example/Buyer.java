package org.example;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.PlatformID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.*;

import static org.example.Main.*;



public class Buyer extends Agent {
    // Declaration of instance variables
    Object[] proposition;
    Proposal p = new Proposal(); // Creating an instance of Proposition class
    List<Integer> prefs = List.of(0,1,0); // List to store preferences
    int sellerCpt= 0; // Counter to track the number of sellers
    Map<String, Double> offers =  new HashMap<>(); // Map to store seller offers

    @Override
    protected void setup() {
         // Check if INTER_CONTAINER is true
         if (INTER_CONTAINER){
             // Move the buyer to the seller's container
             String containerName = "container1";
             ContainerID destination = new ContainerID();
             destination.setName(containerName);
             try {
                 Thread.sleep(7000); // Sleep for 1 second
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }

             doMove(destination);
         } else {
            /* AID remoteAMS = new AID("ams@remotePlatform:1099/JADE", AID.ISGUID);
               remoteAMS.addAddresses("http://"SRV_URB:7778/acc");
               PlatformID destination = new PlatformID(remoteAMS);
               doMove(destination);*/
         }

        // Send proposition to sellers
        proposition = getArguments(); // Get the proposition arguments
        p.price = Integer.parseInt(proposition[0].toString()); // Set the price from arguments
        p.quality = Integer.parseInt(proposition[1].toString()); // Set the quality from arguments
        p.deliveryCost = Integer.parseInt(proposition[2].toString()); // Set the delivery cost from arguments

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM); // Create a new ACLMessage
        msg.setOntology("proposition sending"); // Set the message ontology
        msg.setLanguage("JavaSerialization"); // Set the message language
        try {
            msg.setContentObject(p); // Set the content of the message to the proposition object
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Add receivers for the message
        for (int i = 0; i < nb_sellers; i++) {
            msg.addReceiver(new AID("Seller".concat(String.valueOf(i)),AID.ISLOCALNAME));
        }
        send(msg); // Send the message to sellers

        // Add behavior to handle seller offers
         addBehaviour(new Behaviour() {
            @Override
            public void action() {
                try {
                    // Define message template to match incoming PROPOSE messages
                    MessageTemplate MsgT = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
                    ACLMessage offer = receive(MsgT); // Receive the offer message

                    // Check if the offer message is not null and has the correct ontology
                    if(offer != null && offer.getPerformative() == ACLMessage.PROPOSE &&
                            offer.getOntology().equals("offre seller")){

                        // Extract the content of the offer message
                        Proposal sellerOffer = (Proposal) offer.getContentObject();

                        // Log the received offer and the sender
                        System.out.println("received buying "+ sellerOffer.toString() + " from " +  offer.getSender().getLocalName());
                        sellerCpt++; // Increment the seller counter

                        double value = 0; // Initialize the value variable

                        // Calculate the value based on preferences and normalize the attributes
                        int i = 0;
                        while (i < p.getAsArray().size()) {
                            if (prefs.get(i) == 1) {
                                // Maximize
                                value -= normalise(p.getAsArray().get(i), p) * normalise(sellerOffer.getAsArray().get(i), sellerOffer);
                            } else {
                                // Minimize
                                value += normalise(p.getAsArray().get(i), p) * normalise(sellerOffer.getAsArray().get(i), sellerOffer);
                            }
                            i++;
                        }
                        
                        System.out.println(value); // Print the calculated value
                        offers.put(offer.getSender().getName(), value); // Add the offer to the map
                    }
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }

            }

             @Override
             public boolean done() {
                 // Check if offers have been received from all sellers
                 if (offers.size() == nb_sellers) {
                     // Get the minimum value from the map
                     OptionalDouble minValueOptional = offers.values().stream().mapToDouble(Double::doubleValue).min();

                     // Check if the minimum value exists
                     if (minValueOptional.isPresent()) {
                         double minValue = minValueOptional.getAsDouble();

                         // Find the key corresponding to the minimum value
                         Optional<String> minKeyOptional = offers.entrySet().stream()
                                 .filter(entry -> entry.getValue().doubleValue() == minValue)
                                 .map(Map.Entry::getKey)
                                 .findFirst();

                         // Check if the key corresponding to the minimum value exists
                         if (minKeyOptional.isPresent()) {
                             String minKey = minKeyOptional.get();
                             // Print the winner with the minimum offer value
                             System.out.println("The seller " + minKey + " won with the best value : " + minValue);
                         } else {
                             System.out.println("No seller found with the minimum offer value");
                         }
                     } else {
                         System.out.println("0 : No offers available");
                     }

                     return true; // Indicate that the behavior is done
                 }
                 return false; // Indicate that the behavior is not done yet

             }
         });
    }
}