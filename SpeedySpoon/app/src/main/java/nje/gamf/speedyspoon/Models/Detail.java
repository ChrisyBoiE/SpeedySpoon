package nje.gamf.speedyspoon.Models;

public class Detail {
    private String itemId;
    private String orderId;
    private Integer price;
    private Integer quantity;
    private String specialInstructions;

    public Detail(){
    }

    public Detail(String itemId, String orderId, Integer price, Integer quantity, String specialInstructions) {
        this.itemId = itemId;
        this.orderId = orderId;
        this.price = price;
        this.quantity = quantity;
        this.specialInstructions = specialInstructions;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
}
