package nje.gamf.speedyspoon.Models;

import java.io.Serializable;
import java.util.Map;
import com.google.firebase.database.PropertyName;

public class Restaurant implements Serializable {
    private String id;
    private String address;
    private String cuisineType;
    private String description;
    private Map<String, Boolean> menuItems;
    private String name;
    private double rating;
    private String restaurantImage;
    private int minimumOrder;

    // Üres konstruktor Firebase-hez szükséges
    public Restaurant() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @PropertyName("restaurant_image")
    public String getRestaurantImage() {
        return restaurantImage;
    }

    @PropertyName("restaurant_image")
    public void setRestaurantImage(String restaurantImage) {
        this.restaurantImage = restaurantImage;
    }

    @PropertyName("minimum_order")
    public int getMinimumOrder() {
        return minimumOrder;
    }

    @PropertyName("minimum_order")
    public void setMinimumOrder(int minimumOrder) {
        this.minimumOrder = minimumOrder;
    }
}