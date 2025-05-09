package nje.gamf.speedyspoon.Repositories;

import com.google.firebase.database.DatabaseError;

import java.util.List;

import nje.gamf.speedyspoon.Models.Restaurant;

public interface RestaurantCallback {
    void onRestaurantsLoaded(List<Restaurant> restaurants);
    void onError(DatabaseError error);
}