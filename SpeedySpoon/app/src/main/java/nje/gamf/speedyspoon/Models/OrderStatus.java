package nje.gamf.speedyspoon.Models;

import java.util.List;

public class OrderStatus {
    private List<Status> orderStatus;

    public OrderStatus() {
    }

    public OrderStatus(List<Status> orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<Status> getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(List<Status> orderStatus) {
        this.orderStatus = orderStatus;
    }
}
