package nje.gamf.speedyspoon.Models;

import java.util.List;

public class Order {
    private String id;
    private String orderDate;
    private String status;
    private Integer totalAmount;
    private OrderDetail orderDetails;
    private String userID;
    private String restaurant_id; 
    private String order_id;      

    public Order(){}

    public Order(String orderDate, String status, Integer totalAmount, OrderDetail orderDetails, String userID, String restaurant_id, String order_id) { 
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderDetails = orderDetails;
        this.userID = userID;
        this.restaurant_id = restaurant_id; 
        this.order_id = order_id;          
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderDetail getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetail orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
