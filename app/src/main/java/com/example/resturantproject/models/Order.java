package com.example.resturantproject.models;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    private String userEmail;
    private String pizzaName;
    private String pizzaSize;
    private Date orderDate;
    private int pizzaCount;
    private double cost; // New field

    public Order(String userEmail, String pizzaName, String pizzaSize, Date orderDate, int pizzaCount, double cost) {
        this.userEmail = userEmail;
        this.pizzaName = pizzaName;
        this.pizzaSize = pizzaSize;
        this.orderDate = orderDate;
        this.pizzaCount = pizzaCount;
        this.cost = cost; // Initialize new field
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public String getPizzaSize() {
        return pizzaSize;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public int getPizzaCount() {
        return pizzaCount;
    }

    public double getCost() {
        return cost; // Getter for new field
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public void setPizzaSize(String pizzaSize) {
        this.pizzaSize = pizzaSize;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setPizzaCount(int pizzaCount) {
        this.pizzaCount = pizzaCount;
    }

    public void setCost(double cost) {
        this.cost = cost; // Setter for new field
    }
}
