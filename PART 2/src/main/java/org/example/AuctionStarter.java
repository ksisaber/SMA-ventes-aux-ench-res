package org.example;

import jade.core.Profile;
import jade.wrapper.ControllerException;

public class AuctionStarter {
    public static Boolean INTER_CONTAINER = true;

    public static void startAuction(int numOfSellers) {
        try {
            // Update the number of sellers in Main
            Main.nb_sellers = numOfSellers;

            // Create the first container
            jade.core.Runtime rt = jade.core.Runtime.instance();
            jade.core.ProfileImpl p1 = new jade.core.ProfileImpl(false);
            p1.setParameter(Profile.GUI, "true");
            p1.setParameter(Profile.LOCAL_HOST, "localhost");
            p1.setParameter(Profile.CONTAINER_NAME, "container1");
            if (!INTER_CONTAINER) {
                p1.setParameter(Profile.PLATFORM_ID, "platform1");
            }

            jade.wrapper.AgentContainer container1 = rt.createMainContainer(p1);

            // Create sellers in the first container
            for (int i = 0; i < numOfSellers; i++) {
                jade.wrapper.AgentController seller = container1.createNewAgent("Seller" + i, "org.example.Seller", null);
                seller.start(); // Start sellers
            }
            // Start the first container
            container1.start();

            // Create the second container
            jade.core.Runtime rt2 = jade.core.Runtime.instance();
            jade.core.ProfileImpl p2 = new jade.core.ProfileImpl(false);
            p2.setParameter(Profile.GUI, "true");
            p2.setParameter(Profile.LOCAL_HOST, "localhost");
            p2.setParameter(Profile.CONTAINER_NAME, "container2");
            if (!INTER_CONTAINER) {
                p2.setParameter(Profile.PLATFORM_ID, "platform2");
            }
            jade.wrapper.AgentContainer container2 = rt2.createAgentContainer(p2);

            // Define arguments for Buyer
            Object[] proposal = {"4", "7", "4"};

            // Create a buyer in the second container
            jade.wrapper.AgentController buyer = container2.createNewAgent("Buyer", "org.example.Buyer", proposal);
            buyer.start(); // Start buyer

            // Start the second container
            container2.start();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }
}
