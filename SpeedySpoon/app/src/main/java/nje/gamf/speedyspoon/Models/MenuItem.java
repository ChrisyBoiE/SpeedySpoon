package nje.gamf.speedyspoon.Models;

public class MenuItem {
    private String id;
    private String category;
    private String description;
    private String image;
    private String name;
    private int price;
    private String restaurantId;

    public MenuItem() {
        // Default constructor required for Firebase
    }

    public MenuItem(String id, String category, String description, String image, String name, int price, String restaurantId) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.image = image;
        this.name = name;
        this.price = price;
        this.restaurantId = restaurantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
