package nje.gamf.speedyspoon.Repositories;

import com.google.firebase.database.DatabaseError;

import java.util.List;

import nje.gamf.speedyspoon.Models.Detail;
import nje.gamf.speedyspoon.Models.Order;
import nje.gamf.speedyspoon.Models.Status;

public interface OrderCallback {
    void onOrdersLoaded(List<Order> orders);
    void onOrderStatusLoaded(List<Status> orderStatus);
    void onOrderDetailsLoaded(List<Detail> orderDetails);

    void onError(DatabaseError error);
}
