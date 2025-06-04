package com.yamatoapps.pizzaordering;

public class Pizza {
    public  String name = "";
    public  String details = "";
    public  double price= 0;
    public  String image_url= "";
    public String id = "";


    public Pizza(String name, String details, double price, String image_url, String id) {
        this.name = name;
        this.details = details;
        this.price = price;
        this.image_url = image_url;
        this.id = id;
    }
}
