import java.util.Scanner;


class Card {
    private int suit = 0;
    private int value = 1;
    private boolean isFaceUp = true;
    
    
    Card(int value, int suit) {
        this.value = value;
        this.suit = suit;
    }
    
    
    int getValue() {
        return value;
    }
    
    
    int getSuit() {
        return suit;
    }
    
    
    void flip() {
        isFaceUp = !isFaceUp;
    }
    
    
    public String toString() {
        if (!isFaceUp) {
            return "XX";
        }
        String suit = "♦♣♥♠";
		String value = " A23456789TJQK";
        return "" + value.charAt(this.value) + suit.charAt(this.suit);
    }
}


class Deck {
    private int cardsDrawn = 0;
    private Card[] cardsArray = new Card[52];
    
    
    Deck() {
        for(int value = 1; value < 14; value ++) {
            for(int suit = 0; suit < 4; suit ++) {
                cardsArray[(value - 1) * 4 + suit] = new Card(value, suit);
            }
        }
    }
    
    
    Card getCard(int index) {
        return cardsArray[index];
    }
    
    
    Card draw(boolean faceUp) {
        Card card = draw();
        card.flip();
        return card;
    }
    
    
    Card draw() {
        cardsDrawn++;
        if (cardsDrawn < 52) return cardsArray[cardsDrawn - 1];
        return null;
    }
    
    
    void reset() {
        cardsDrawn = 0;
    }
    
    
    void shuffle() {
        for(int i = cardsDrawn; i < 52; i++) {
            int random = (int)(Math.random() * (52 - i) + i);
            Card temp = this.cardsArray[random];
            this.cardsArray[random] = this.cardsArray[i];
            this.cardsArray[i] = temp;
        }
    }
}


class BlackJack {
    private Deck deck;
    private Card[][] hands;
    private int[] numCards;
    private int[] hardScores;
    private int[] softScores;
    private boolean[] stood;
    private int whosTurn;
    private int numPlayers;
    
    
    private final int DEALER = 0;
    
    
    BlackJack() {
        deck = new Deck();
    }
    
    
    void newGame(int numPlayers){
        this.numPlayers = numPlayers;
        deck.reset();
        deck.shuffle();
        
        hands = new Card[numPlayers + 1][5];
        numCards = new int[numPlayers + 1];
        hardScores = new int[numPlayers + 1];
        softScores = new int[numPlayers + 1];
        stood = new boolean[numPlayers + 1];
        whosTurn = 1;
        
        // Every player draws two cards.
        for (int i = 0; i <= numPlayers; i++) {
            hit(i);
            hit(i);
        }
        
        // Dealer's first card is face-down.
        hands[DEALER][0].flip();
        printState();
        
        Scanner sc = new Scanner(System.in);
        
        // Players' turns.
        while (!isDealersTurn()) {
            // Skip the player's turn if they've already stood.
            if (stood[whosTurn]) {
                ++whosTurn;
                if (whosTurn == numPlayers + 1) whosTurn = 1;
                continue;
            }
            
            // If they get an automatic 21 at the start.
            if (softScores[whosTurn] == 21) {
                System.out.println("PLAYER " + whosTurn + " GOT BLACKJACK!");
                stand(whosTurn);
                ++whosTurn;
                if (whosTurn == numPlayers + 1) whosTurn = 1;
                continue;
            }
            
            System.out.println("\nPlayer " + whosTurn + "'s turn.");
            System.out.println("Press H to hit, or S to stand: ");
            String input = sc.nextLine();
            
            if (input.equals("h") || input.equals("H")) {
                hit(whosTurn);
            } else {
                stand(whosTurn);
                
                ++whosTurn;
                if (whosTurn == numPlayers + 1) whosTurn = 1;
                continue;
            }
            
            printState();
            
            // Automatically stand once 21 or over.
            if (hardScores[whosTurn] > 21) {
                System.out.println("PLAYER " + whosTurn + " BUSTED!");
                stand(whosTurn);
            } else if (hardScores[whosTurn] == 21 || softScores[whosTurn] == 21 || numCards[whosTurn] == 5) {
                System.out.println("PLAYER " + whosTurn + " GOT BLACKJACK!");
                stand(whosTurn);
            }
            
            ++whosTurn;
            if (whosTurn == numPlayers + 1) whosTurn = 1;
        }
        
        // Dealer's turn.
        System.out.println("\nDEALER'S TURN");
        hands[DEALER][0].flip();
        
        while(true) {
            printState();
            System.out.print("\nPress ENTER to continue: ");
            sc.nextLine();
            
            // Automatically stand once 17 or over.
            if (hardScores[DEALER] >= 17 || softScores[DEALER] >= 17 && softScores[DEALER] <= 21) {
                stand(DEALER);
                break;
            }
            hit(DEALER);
        }
        
        // Final results.
        for (int who = 1; who <= numPlayers; ++who) {
            if (hardScores[who] > 21) {
                System.out.println("Player " + who + " busted.");
            } else if (hardScores[DEALER] > 21) {
                System.out.println("Player " + who + " beat the dealer.");
            } else if (softScores[who] < 22 && softScores[DEALER] < 22 && softScores[who] >= softScores[DEALER]) {
                if (softScores[who] == softScores[DEALER]) {
                    System.out.println("Player " + who + " tied the dealer.");
                } else {
                    System.out.println("Player " + who + " beat the dealer.");
                }
            } else if (softScores[who] < 22 && softScores[who] >= hardScores[DEALER]) {
                if (softScores[who] == hardScores[DEALER]) {
                    System.out.println("Player " + who + " tied the dealer.");
                } else {
                    System.out.println("Player " + who + " beat the dealer.");
                }
            } else if (softScores[DEALER] < 22 && hardScores[who] >= softScores[DEALER]) {
                if (hardScores[who] == softScores[DEALER]) {
                    System.out.println("Player " + who + " tied the dealer.");
                } else {
                    System.out.println("Player " + who + " beat the dealer.");
                }
            } else if (hardScores[who] >= hardScores[DEALER]) {
                if (hardScores[who] == hardScores[DEALER]) {
                    System.out.println("Player " + who + " tied the dealer.");
                } else {
                    System.out.println("Player " + who + " beat the dealer.");
                }
            } else {
                System.out.println("Player " + who + " lost to the dealer.");
            }
        }
    }
    
    
    // Who draws one card. Updates the scores.
    void hit(int who) {
        Card card = deck.draw();
        hands[who][numCards[who]] = card;
        numCards[who]++;
        
        // Update the scores
        if (card.getValue() > 9) {
            hardScores[who] += 10;
            softScores[who] += 10;
        } else if (card.getValue() == 1) {
            hardScores[who] += 1;
            softScores[who] += 11;
        } else {
            hardScores[who] += card.getValue();
            softScores[who] += card.getValue();
        }
    }
    
    
    void stand(int who) {
        stood[who] = true;
    }
    
    
    void printState(){
        System.out.print("Dealer: ");
        for (int i = 0; i < numCards[DEALER]; ++i) {
            System.out.print(hands[DEALER][i] + " ");
        }
        System.out.println();
        
        
        for (int who = 1; who <= numPlayers; ++who) {
            System.out.print("Player " + who + ": ");
            for (int i = 0; i < numCards[who]; ++i) {
               System.out.print(hands[who][i] + " ");
            }
            System.out.println();
        }
    }
    
    
    boolean isDealersTurn() {
        for (int who = 1; who <= numPlayers; ++who) {
            if (!stood[who]) return false;
        }
        return true;
    }
}


public class Main {
    public static void main(String []args){
        Scanner sc = new Scanner(System.in);
        
        BlackJack game = new BlackJack();
        
        while(true) {
            System.out.print("How many players? ");
            game.newGame(Integer.parseInt(sc.nextLine()));
            System.out.print("Play Again? [Y/N]: ");
            String input = sc.nextLine();
            if (input.equals("n") || input.equals("N")) {
                return;
            }
            System.out.println();
        }
    }
}
