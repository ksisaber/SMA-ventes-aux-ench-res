package org.example;

import jade.core.Profile;
import jade.wrapper.ControllerException;

public class Main {

    public static int nb_buyers = 10;
    public static void main(String[] args) {
        try {
            jade.core.Runtime rt = jade.core.Runtime.instance();
            jade.core.ProfileImpl p = new jade.core.ProfileImpl(false);
            p.setParameter(Profile.GUI, "true");
            p.setParameter(Profile.LOCAL_HOST, "localhost");
            jade.wrapper.AgentContainer container1 = rt.createMainContainer(p);

            //duration in seconds
            Object[] bidDuration = {"1"};
            jade.wrapper.AgentController countdownAgent = container1.createNewAgent("Countdown", "org.example.Duration", bidDuration);
            countdownAgent.start(); // Start CountDown Agent


            for (int i = 0; i < nb_buyers; i++) {
                jade.wrapper.AgentController buyer = container1.createNewAgent("Buyer0".concat(String.valueOf(i)), "org.example.Buyer", null);
                buyer.start(); // Start buyers
            }

            // Define arguments for Seller
            Object[] startPrice = {"1500"};
            jade.wrapper.AgentController seller = container1.createNewAgent("Seller", "org.example.Seller", startPrice);
            seller.start(); // Start seller

            container1.start();

        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }
}