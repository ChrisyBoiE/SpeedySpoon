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
    
    @PropertyName("restaurant_image")
    private String restaurantImage;
    
    @PropertyName("minimum_order")
    private int minimum_order;
    private String restaurant_id; 

    public Restaurant() {
        
    }

    public Restaurant(String id, String address, String cuisineType, String description, Map<String, Boolean> menuItems, String name, double rating, String restaurantImage, int minimum_order, String restaurant_id) { 
        this.id = id;
        this.address = address;
        this.cuisineType = cuisineType;
        this.description = description;
        this.menuItems = menuItems;
        this.name = name;
        this.rating = rating;
        this.restaurantImage = restaurantImage;
        this.minimum_order = minimum_order;
        this.restaurant_id = restaurant_id; 
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
        return minimum_order;
    }

    @PropertyName("minimum_order")
    public void setMinimumOrder(int minimum_order) {
        this.minimum_order = minimum_order;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }
}