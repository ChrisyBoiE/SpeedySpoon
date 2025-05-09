package nje.gamf.speedyspoon.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import nje.gamf.speedyspoon.Models.Restaurant;

public class RestaurantRepository {
    private DatabaseReference restaurantsRef;

    public RestaurantRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        restaurantsRef = database.getReference("restaurants");
    }

    public void fetchRestaurants(final RestaurantCallback callback) {
        restaurantsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Restaurant> restaurantsList = new ArrayList<>();
                for (DataSnapshot restaurantSnapshot : dataSnapshot.getChildren()) {
                    Restaurant restaurant = restaurantSnapshot.getValue(Restaurant.class);
                    if (restaurant != null) {
                        restaurantsList.add(restaurant);
                    }
                }
                Log.d("testing", "Restaurants fetched: " + restaurantsList.size());
                callback.onRestaurantsLoaded(restaurantsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }
}