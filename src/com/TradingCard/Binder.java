package com.TradingCard;

public class Binder {
    private String name;
    private Card[] cards;
    
    public Binder (){
        name = "";
        cards = new Card[20];
    }

    public void viewBinder(){
        System.out.println("Binder Name: " + this.name);
        for (int i = 0; i < 20; i++){
      
    }
}
