package nje.gamf.speedyspoon.Models;

import java.util.List;

public class Order {
    private String orderDate;
    private String status;
    private String totalAmount;
    private OrderDetail orderDetails;
    private String userID;

    public Order(){}

    public Order(String orderDate, String status, String totalAmount, OrderDetail orderDetails, String userID) {
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderDetails = orderDetails;
        this.userID = userID;
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

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
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
}
