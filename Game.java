/**
 * SYST 17796 Project Winter 2019 Base code. Students can modify and extend to
 * implement their game. Add your name as a modifier and the date!
 * @modifier Harpreet Ghuman 2019
 */
package ca.sheridancollege.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The class that models your game. You should create a more specific child of
 * this class and instantiate the methods given.
 *
 * @author dancye, 2018
 */
public abstract class Game {

    private final String gameName;//the title of the game
    private ArrayList<Player> players;// the players of the game
    private static int cash;//cash the user bets with
    private static int bet;//how much the user wants to bet
    private static int AceCounter;//how many aces are in the user's hand
    private static ArrayList<Card> hand;//represents the user's hand
    private static int handvalue;//the value of the user's hand
    private static String name;//name of the user

    public static void main(String[] args) {
        System.out.println("Hi! What is your name?");
        Scanner scan = new Scanner(System.in);
        name = scan.nextLine();
        System.out.println("Hello, " + name + ", lets play some BlackJack!");
        System.out.println("How much cash do you want to start with?");
        Scanner money = new Scanner(System.in);
        cash = money.nextInt();
        System.out.println("You start with cash: " + cash);
        while (cash > 0) {
            GroupOfCards deck = new GroupOfCards();//initialize deck, dealer, hands, and set the bet.
            deck.shuffle();
            AceCounter = 0;
            Player dealer = new Player(deck) {
                @Override
                public void play() {}};
            List<Card> hand = new ArrayList<>();
            hand.add(deck.drawCard());
            hand.add(deck.drawCard());
            System.out.println("How much would you like to bet?");
            bet = Bet(cash);
            System.out.println("Cash:" + (cash - bet));
            System.out.println("Money on the table:" + bet);
            System.out.println("Here is your hand: ");
            System.out.println(hand);
            int handvalue = calcHandValue(hand);
            System.out.println("The dealer is showing: ");
            dealer.showFirstCard();
            if (hasBlackJack(handvalue) && dealer.hasBlackJack())//check if both the user and dealer have blackjack.
            {
                Tie();
            } else if (hasBlackJack(handvalue))//check if the user has blackjack.
            {
                System.out.println("You have BlackJack!");
                System.out.println("You win 2x your money back!");
                cash = cash + bet;
                Win();
            } else if (dealer.hasBlackJack())//check if the dealer has blackjack.
            {
                System.out.println("Here is the dealer's hand:");
                dealer.showHand();
                Lose();
            } else {
                if (2 * bet < cash)//check if the user can double down.
                {
                    System.out.println("Would you like to double down?");//allows the user to double down.
                    Scanner doubledown = new Scanner(System.in);
                    String doubled = doubledown.nextLine();
                    while (!isyesorno(doubled)) {
                        System.out.println("Please enter yes or no.");
                        doubled = doubledown.nextLine();
                    }
                    if (doubled.equals("yes")) {
                        System.out.println("You have opted to double down!");
                        bet = 2 * bet;
                        System.out.println("Cash:" + (cash - bet));
                        System.out.println("Money on the table:" + bet);
                    }
                }
                System.out.println("Would you like to hit or stand?");//ask if the user will hit or stand
                Scanner hitorstand = new Scanner(System.in);
                String hitter = hitorstand.nextLine();
                while (!isHitorStand(hitter)) {
                    System.out.println("Please enter 'hit' or 'stand'.");
                    hitter = hitorstand.nextLine();
                }
                while (hitter.equals("hit"))//hits the user as many times as he or she pleases.
                {
                    Hit(deck, hand);
                    System.out.println("Your hand is now:");
                    System.out.println(hand);
                    handvalue = calcHandValue(hand);
                    if (checkBust(handvalue))//checks if the user busted
                    {
                        Lose();
                        break;
                    }
                    /*
                    if (handvalue <= 21 && hand.size() == 5)//checks for a five card trick.
                    {
                        fivecardtrick();
                        break;
                    }
                    */
                    System.out.println("Would you like to hit or stand?");
                    hitter = hitorstand.nextLine();
                }
                if (hitter.equals("stand"))//lets the user stand.
                {
                    int dealerhand = dealer.takeTurn(deck);//takes the turn for the dealer.
                    System.out.println("");
                    System.out.println("Here is the dealer's hand:");
                    dealer.showHand();
                    if (dealerhand > 21)//if the dealer busted, user wins.
                    {
                        Win();
                    } else {
                        int you = 21 - handvalue;//check who is closer to 21 and determine winner
                        int deal = 21 - dealerhand;
                        if (you == deal) {
                            Tie();
                        }
                        if (you < deal) {
                            Win();
                        }
                        if (deal < you) {
                            Lose();
                        }
                    }
                }
            }
            System.out.println("Would you like to play again?");//ask if the user wants to keep going
            Scanner yesorno = new Scanner(System.in);
            String answer = yesorno.nextLine();
            while (!isyesorno(answer)) {
                System.out.println("Please answer yes or no.");
                answer = yesorno.nextLine();
            }
            if (answer.equals("no")) {
                break;
            }
        }
        System.out.println("Your cash is: " + cash);//if user doesn't want to play or runs out of cash, either congratulates them on their winnings or lets them know
        if (cash == 0) {
            System.out.println("You ran out of cash!");
        } else {
            System.out.println("Enjoy your winnings, " + name + "!");
        }
    }
    /*
     * Checks if the user has blackjack.
     */

    public static int calcHandValue(List<Card> hand) {
        Card[] aHand = new Card[]{};
        aHand = hand.toArray(aHand);
        int handvalue = 0;
        for (int i = 0; i < aHand.length; i++) {
            handvalue += aHand[i].getValue();
            if (aHand[i].getValue() == 11) {
                AceCounter++;
            }
            while (AceCounter > 0 && handvalue > 21) {
                handvalue -= 10;
                AceCounter--;
            }
        }
        return handvalue;
    }
    /*
     * Asks the user how much he or she would like to bet.
     */

    public static int Bet(int cash) {
        Scanner sc = new Scanner(System.in);
        int bet = sc.nextInt();
        while (bet > cash) {
            System.out.println("You cannot bet more cash than you have!");
            System.out.println("How much would you like to bet?");
            bet = sc.nextInt();
        }
        return bet;
    }
    /*
     * Called if the user wins.
     */

    public static void Lose()
{
    System.out.println("Sorry, you lose!");
    cash=cash-bet;
    System.out.println("Cash: "+cash);
}
/*
 * Called if the user pushes
 */
public static void Tie()
{
    System.out.println("It's a Tie!");
    System.out.println("Dealer Wins");
    cash=cash-bet;
    System.out.println("Cash: "+cash);
}
/*
 * Adds a card to user's hand and calculates the value of that hand. Aces are taken into account.
 */
public static void Hit(GroupOfCards deck, List<Card> hand)
{
    hand.add(deck.drawCard());
    Card[] aHand = new Card[]{};
    aHand = hand.toArray(aHand);
    handvalue = 0;
    for(int i=0; i<aHand.length; i++)
    {
        handvalue += aHand[i].getValue();
        if(aHand[i].getValue()==11)
        {
            AceCounter++;
        }
        while(AceCounter>0 && handvalue>21)
        {
            handvalue-=10;
            AceCounter--;
        }
    }
}
/*
 * Determines if a user has input hit or stand.
 */
public static boolean isHitorStand(String hitter)
{
    if(hitter.equals("hit") || hitter.equals("stand"))
    {
        return true;
    }
    return false;
}
/*
 * Determines if a user has busted.
 */
public static boolean checkBust(int handvalue)
{
    if(handvalue>21)
    {
        System.out.println("You have busted!");
        return true;
    }
    return false;
}
/*
 * Determines if a user has input yes or no.
 */
public static boolean isyesorno(String answer)
{
    if(answer.equals("yes") || answer.equals("no"))
    {
        return true;
    }
    return false;
}
/*
 * Called if the user has a five card trick.
 
public static void fivecardtrick()
{
    System.out.println("You have achieved a five card trick!");
    Win();
}
   */ 
    public static void Win() {
        System.out.println("Congratulations, you win!");
        cash = cash + bet;
        System.out.println("Cash: " + cash);
    }

    public static boolean hasBlackJack(int handValue) {
        if (handValue == 21) {
            return true;
        }
        return false;
    }

    public Game(String givenName) {
        gameName = givenName;
        players = new ArrayList();
    }

    /**
     * @return the gameName
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * @return the players of this game
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * @param players the players of this game
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Play the game. This might be one method or many method calls depending on
     * your game.
     */
    public abstract void play();

    /**
     * When the game is over, use this method to declare and display a winning
     * player.
     */
    public abstract void declareWinner();

}//end class
