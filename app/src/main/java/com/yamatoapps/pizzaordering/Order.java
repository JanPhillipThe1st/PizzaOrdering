package com.yamatoapps.pizzaordering;

import java.util.Calendar;
import java.util.Date;

public class Order {
    public String pizza_name = "";
    public Date date_ordered = Calendar.getInstance().getTime();
    public  int quantity = 1;
    public Double total = 0.00;
    public String id = "";
    public String image_url = "";


    public Order(String pizza_name, Date date_ordered, int quantity, Double total, String id) {
        this.pizza_name = pizza_name;
        this.date_ordered = date_ordered;
        this.quantity = quantity;
        this.total = total;
        this.id = id;
    }

    public Order(String pizza_name, Date date_ordered, int quantity, Double total, String id, String image_url) {
        this.pizza_name = pizza_name;
        this.date_ordered = date_ordered;
        this.quantity = quantity;
        this.total = total;
        this.id = id;
        this.image_url = image_url;
    }
}
