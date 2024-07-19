package com.example.resturantproject.models;

import java.io.Serializable;
import java.util.Date;

public class SpecialOffer implements Serializable {
    private String pizzaName;
    private String pizzaSize;
    private Date offerStartDate;
    private Date offerEndDate;
    private double totalPrice;
    private int pizzaCount; // Add this line

    public SpecialOffer(String pizzaName, String pizzaSize, Date offerStartDate, Date offerEndDate, double totalPrice, int pizzaCount) {
        this.pizzaName = pizzaName;
        this.pizzaSize = pizzaSize;
        this.offerStartDate = offerStartDate;
        this.offerEndDate = offerEndDate;
        this.totalPrice = totalPrice;
        this.pizzaCount = pizzaCount; // Add this line
    }

    // Getter and setter methods for all fields including pizzaCount
    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public String getPizzaSize() {
        return pizzaSize;
    }

    public void setPizzaSize(String pizzaSize) {
        this.pizzaSize = pizzaSize;
    }

    public Date getOfferStartDate() {
        return offerStartDate;
    }

    public void setOfferStartDate(Date offerStartDate) {
        this.offerStartDate = offerStartDate;
    }

    public Date getOfferEndDate() {
        return offerEndDate;
    }

    public void setOfferEndDate(Date offerEndDate) {
        this.offerEndDate = offerEndDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getPizzaCount() {
        return pizzaCount;
    }

    public void setPizzaCount(int pizzaCount) {
        this.pizzaCount = pizzaCount;
    }
}
