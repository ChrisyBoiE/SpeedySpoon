package nje.gamf.speedyspoon.Models;

import java.util.Map;

public class Restaurant {
    private String address;
    private String cuisineType;
    private String description;
    private Map<String, Boolean> menuItems;
    private String name;
    private double rating;

    public Restaurant() {
        // Default constructor required for Firebase
    }

    public Restaurant(String address, String cuisineType, String description, Map<String, Boolean> menuItems, String name, double rating) {
        this.address = address;
        this.cuisineType = cuisineType;
        this.description = description;
        this.menuItems = menuItems;
        this.name = name;
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Boolean> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(Map<String, Boolean> menuItems) {
        this.menuItems = menuItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}