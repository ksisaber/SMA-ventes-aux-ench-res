package org.example;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import static jade.tools.rma.StartDialog.getArguments;

import static org.example.Main.nb_buyers;

public class Duration extends Agent {

   protected void setup(){
    Object[] args = this.getArguments();
    int duration = Integer.parseInt(args[0].toString()) * 20;//Set the duration in seconds
    addBehaviour(new CountDown(duration));

   }
   class CountDown extends Behaviour {
    int duration;
    public CountDown(int duration) {
        this.duration = duration;
    }

    @Override
    public void action() {
        // Decrease the duration by 1
        duration--; 

        // Delaythe behavior execution for 1 second
        try {
            Thread.sleep(1000); // Sleep for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean done() {

        // send ending alert before exiting
        if(duration == 0){
            ACLMessage msg = new ACLMessage(ACLMessage.DISCONFIRM);
            msg.setContent("done");
            for (int i = 0; i < nb_buyers; i++) {
                msg.addReceiver(new AID("Buyer0".concat(String.valueOf(i)),AID.ISLOCALNAME));
            }
            msg.addReceiver(new AID("Seller",AID.ISLOCALNAME));
            myAgent.send(msg);

        }

        return duration == 0;
    }
}
}
