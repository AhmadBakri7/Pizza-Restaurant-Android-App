package com.example.resturantproject.models;

public class Pizza {

    private String name;
    private String description;
    private double price;
    private String category;
    private int imageSrc;

    // Constructor, getters, and setters

    public Pizza(String name, String description, double price, String category, int imageSrc) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageSrc = imageSrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(int imageSrc) {
        this.imageSrc = imageSrc;
    }
}
