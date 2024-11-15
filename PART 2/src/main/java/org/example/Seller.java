package org.example;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.Random;

public class Seller extends Agent {
    // Random generator to create random offers
    Random random = new Random();

    @Override
    protected void setup() {
        // Add cyclic behavior to the agent
        addBehaviour(new CyclicBehaviour() {

            @Override
            public void action() {
                // Define message template to match incoming INFORM messages
                MessageTemplate mProp = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

                // Receive the proposition message
                ACLMessage proposition = receive(mProp);

                // Check if the proposition is not null and has the correct ontology
                if (proposition != null && proposition.getPerformative() == ACLMessage.INFORM
                        && proposition.getOntology().equals("proposition sending")) {
                    Proposal props;
                    try {
                        // Extract the content of the proposition message
                        props = (Proposal) proposition.getContentObject();
                    } catch (UnreadableException e) {
                        throw new RuntimeException(e);
                    }

                    // Create a new offer by modifying the received proposition
                    Proposal sellerOffer = new Proposal();
                    sellerOffer.price = props.price + random.nextInt(50);
                    sellerOffer.quality = props.quality + random.nextInt(50);
                    sellerOffer.deliveryCost = props.deliveryCost + random.nextInt(50);

                    // Send the offer back to the buyer
                    ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
                    msg.setOntology("offre seller");
                    try {
                        msg.setContentObject(sellerOffer);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    msg.addReceiver(new AID("Buyer", AID.ISLOCALNAME));
                    myAgent.send(msg);

                    // Suspend the agent after sending the offer
                    myAgent.doSuspend();
                }
            }
        });
    }
}
