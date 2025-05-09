package nje.gamf.speedyspoon.Models;

public class MenuItem {
    private String category;
    private String description;
    private String image;
    private String name;
    private int price;
    private String restaurantID;

    public MenuItem() {
    }

    public MenuItem(String category, String description, String image, String name, int price, String restaurantID) {
        this.category = category;
        this.description = description;
        this.image = image;
        this.name = name;
        this.price = price;
        this.restaurantID = restaurantID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }
}
