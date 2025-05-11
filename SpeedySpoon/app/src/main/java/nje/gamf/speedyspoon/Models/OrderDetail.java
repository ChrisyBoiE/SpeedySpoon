package nje.gamf.speedyspoon.Models;

import java.util.List;

public class OrderDetail {
    private List<Detail> orderDetails;

    public OrderDetail() {}

    public OrderDetail(List<Detail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<Detail> getOrderDetails() {
        return orderDetails;
    }
    public void setOrderDetails(List<Detail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
