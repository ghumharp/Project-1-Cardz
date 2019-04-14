/**
 * SYST 17796 Project Winter 2019 Base code.
 * Students can modify and extend to implement their game.
 * Add your name as a modifier and the date!
 * @modifier Harpreet Ghuman 2019
 */
package ca.sheridancollege.project;

import java.util.ArrayList;

/**
 * A class that models each Player in the game. Players have an identifier, which should be unique.
 * @author dancye, 2018
 */
public abstract class Player 
{
    private String playerID; //the unique ID for this player
     ArrayList<Card> hand;//represents the dealer's hand
private int handvalue=0;//value of the dealer's hand (starts at 0)
private Card[] aHand;//used to convert the dealer's hand to an array
private int AceCounter;//counts the aces in the dealer's hand
Player(GroupOfCards deck)
{
    hand = new ArrayList<>();
    aHand = new Card[]{};
    int AceCounter=0;
    for(int i=0; i<2; i++)
    {
        hand.add(deck.drawCard());
    }
    aHand = hand.toArray(aHand);
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
    
    /**
     * A constructor that allows you to set the player's unique ID
     * @param name the unique ID to assign to this player.
     */
    public Player(String name)
    {
        playerID= name;
    }
    
    /**
     * @return the playerID
     */
    public String getPlayerID() 
    {
        return playerID;
    }

    /**
     * Ensure that the playerID is unique
     * @param givenID the playerID to set
     */
    public void setPlayerID(String givenID) 
    {
        playerID = givenID;
    }
    
    public void showFirstCard()
{
    Card[] firstCard = new Card[]{};
    firstCard = hand.toArray(firstCard);
    System.out.println("["+firstCard[0]+"]");
}
/*
 * Gives the dealer another card and updates the value of his hand. Takes into account the value of aces.
 */
public void Hit(GroupOfCards deck)
{
    hand.add(deck.drawCard());
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
 * Determines if the dealer wants to hit according to classic Blackjack rules.
 */
public boolean wantsToHit()
{
    if(handvalue<17)
    {
        return true;
    }
    return false;
}
/*
 * Returns true if the dealer has blackjack.
 */
public boolean hasBlackJack()
{
    if(hand.size()==2 && handvalue==21)
    {
        System.out.println("The dealer has blackjack!");
        return true;
    }
    return false;
}
/*
 * Prints the dealer's hand.
 */
public void showHand()
{
    System.out.println(hand);
}
/*
 * Returns the value of the dealer's hand.
 */
public int getHandValue()
{
    return handvalue;
}
/*
 * Determines if a dealer has busted.
 */
public boolean busted(int handvalue)
{
    if(handvalue>21)
    {
        System.out.println("The dealer busted!");
        return true;
    }
    return false;
}
/*
 * Takes the turn for the dealer and returns the value of his hand.
 */
public int takeTurn(GroupOfCards deck)
{
    while(wantsToHit())
    {
        System.out.println("The dealer hits");
        Hit(deck);
        if(busted(handvalue))
        {
            break;
        }
    }
    if(handvalue<=21)
    {
        System.out.print("The dealer stands.");
    }
    return handvalue;
}
    /**
     * The method to be instantiated when you subclass the Player class
     * with your specific type of Player and filled in with logic to play your game.
     */
    public abstract void play();
    
}
