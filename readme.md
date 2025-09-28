# Multi-Agent Auction System

This project explores **Multi-Agent Systems (MAS)** through the implementation of auction and negotiation scenarios. It highlights how autonomous agents can interact, compete, and collaborate in distributed environments.

---

## Theoretical Background

### Multi-Agent Systems (MAS)

A **Multi-Agent System** is a collection of autonomous software entities (agents) capable of perceiving their environment, making decisions, and interacting with each other. MAS are widely used in distributed problem-solving, negotiation, and simulation of real-world processes.

### Auctions in MAS

An **auction** is a negotiation mechanism where participants (buyers and sellers) exchange bids according to predefined rules. In MAS, auctions are often used to model decision-making, competition, and resource allocation. The seller typically initiates the process, and buyers compete by submitting increasingly higher offers until a termination condition is reached.

### Mobile Agents

A **mobile agent** is an agent capable of migrating between containers or platforms during execution. Mobility enhances flexibility and scalability by allowing computation to move closer to resources or other agents. Mobile agents are useful in scenarios such as distributed decision-making, e-commerce, and dynamic resource discovery.

---

## Project Overview

The project is divided into two main parts:

### Part 1: Multi-Agent Negotiation (1 Seller – Several Buyers)

Implements an auction scenario with one seller and multiple buyers (minimum two).

**Auction Flow:**

1. The seller puts an item up for sale with an opening price.
2. Buyers make bids higher than the current price.
3. The seller shares the highest offer with all buyers.
4. The process repeats until no further bids are made or the auction time ends.
5. If the final price is higher than the seller’s hidden reserve price, the item is sold to the winning buyer.

**Files (Part 1):**

* `Buyer.java` – Buyer agent logic
* `Seller.java` – Seller agent logic
* `Duration.java` – Auction timing management
* `Main.java` – Entry point for execution
* `AuctionGUI.java` – Auction graphical interface

---

### Part 2: Multi-Criteria Decision & Mobile Agents (1 Buyer – Several Sellers)

Extends the project with **multi-criteria decision-making** and introduces a **mobile buyer agent**.

* The buyer evaluates offers from multiple sellers based on multiple criteria (e.g., price, quality, delivery time).
* The buyer is mobile, supporting:

  * **Inter-container migration**
  * **Inter-platform migration**

This part adapts **Exercise 5 from SW4** to integrate agent mobility.

**Files (Part 2):**

* `Buyer.java` – Mobile buyer agent
* `Seller.java` – Seller agent logic
* `Proposal.java` – Represents sellers’ offers
* `AuctionStarter.java` – Initializes the MAS environment
* `AuctionGUI.java` – Graphical interface
* `Main.java` – Entry point

---

## Project Structure

```
PART 1/
 └── src/main/java/org/example/
      ├── AuctionGUI.java
      ├── Buyer.java
      ├── Duration.java
      ├── Main.java
      └── Seller.java

PART 2/
 └── src/main/java/org/example/
      ├── AuctionGUI.java
      ├── AuctionStarter.java
      ├── Buyer.java
      ├── Main.java
      ├── Proposal.java
      └── Seller.java
```

---

## Requirements

* Java 8+
* [JADE Framework](https://jade.tilab.com/) (Java Agent DEvelopment Framework)
* Maven (for dependency management)

---

## How to Run

1. Clone the repository:

   ```bash
   git clone <repo-url>
   cd <repo-folder>
   ```
2. Build the project with Maven:

   ```bash
   mvn clean install
   ```
3. Launch Part 1 or Part 2 by running the corresponding `Main.java` class.
4. Use the GUI to observe the auction process.

---

## Authors

* Project developed as part of the **Multi-Agent Systems coursework**.
