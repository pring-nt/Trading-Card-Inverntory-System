package com.TradingCard;
import java.util.Scanner;
import java.util.ArrayList;

public class Collection {
    private ArrayList<Card> cards;
    public Collection(){cards = new ArrayList<>();
                       }
    
    public void addCard(){
        Card newCard = new Card();
        System.out.println("Enter card name: ");
        String cardName = scanner.nextString();

        System.out.println("Enter rarity: ");
        String cardRarity = scanner.nextString();

        if (cardRarity == Rare || cardRarity == Legendary){
        System.out.println("Enter variation: ");
        String cardVariation = scanner.nextString();
        } else {
        String cardVariation = "Rare";
        }
        System.out.println("Enter card value: ");
        BigDecimal cardValue = scanner.nextBigDecimal();
        scanner.nextLine();

        newCard.setName(cardRarity);
        newCard.setRarity(cardRarity);
        newCard.setVariation(cardVariation);
        newCard.setValue(cardValue);

        cards.add(newCard);
  }

  public void viewCollection(){
      for (int i = 0; i < cards.size(); i++) {
          System.out.println("Card #" + (i + 1));
          System.out.println("Name: " + cards.get(i).getName());
          System.out.println("Rarity: " + cards.get(i).getRarity());
          System.out.println("Variation: " + cards.get(i).getVariation());
          System.out.println("Value: " + cards.get(i).getValue());
          System.out.println("Count: " + cards.get(i).getCount());
      }
}
