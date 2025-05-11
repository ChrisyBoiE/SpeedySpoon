package nje.gamf.speedyspoon.Repositories;

import com.google.firebase.database.DatabaseError;

import java.util.List;

import nje.gamf.speedyspoon.Models.Order;

public interface OrderCallback {
    void onOrdersLoaded(List<Order> orders);

    void onError(DatabaseError error);
}
