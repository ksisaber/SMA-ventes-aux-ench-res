package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Proposal implements Serializable {

   Integer price;
   Integer quality;
   Integer deliveryCost;

   public List<Integer> getAsArray(){
      List<Integer> result = new ArrayList<>();
      result.add(price);
      result.add(quality);
      result.add(deliveryCost);
      return result;
   }

   @Override
   public String toString() {
      return "Proposal{" +
              "price=" + price +
              ", quality=" + quality +
              ", deliveryCost=" + deliveryCost +
              '}';
   }
}
