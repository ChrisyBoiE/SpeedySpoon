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

// Repository az éttermek kezeléséhez az adatbázisban
public class RestaurantRepository {
    private DatabaseReference restaurantsRef; // Éttermek referenciája
    private static final String TAG = "RestaurantRepository"; // LOG azonosító

    public RestaurantRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://speedyspoon-be378-default-rtdb.europe-west1.firebasedatabase.app/");
        restaurantsRef = database.getReference("restaurants");
    }

    // Összes étterem lekérése
    public void fetchRestaurants(final RestaurantCallback callback) {
        restaurantsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Restaurant> restaurantsList = new ArrayList<>();
                for (DataSnapshot restaurantSnapshot : dataSnapshot.getChildren()) {
                    try {
                        String restaurantId = restaurantSnapshot.getKey();
                        Restaurant restaurant = restaurantSnapshot.getValue(Restaurant.class);
                        if (restaurant != null) {
                            // ID beállítása a Firebase kulcsból
                            restaurant.setId(restaurantId);
                            Log.d(TAG, "Restaurant loaded: " + restaurant.getName() + 
                                " with ID: " + restaurantId + 
                                ", Min order: " + restaurant.getMinimumOrder());
                            restaurantsList.add(restaurant);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing restaurant data", e);
                    }
                }
                Log.d(TAG, "Restaurants fetched: " + restaurantsList.size());
                callback.onRestaurantsLoaded(restaurantsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error fetching restaurants", databaseError.toException());
                callback.onError(databaseError);
            }
        });
    }
    
    // Egy étterem lekérése ID alapján
    public void fetchRestaurantById(String restaurantId, final RestaurantCallback callback) {
        restaurantsRef.child(restaurantId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                    if (restaurant != null) {
                        restaurant.setId(restaurantId);
                        List<Restaurant> restaurantsList = new ArrayList<>();
                        restaurantsList.add(restaurant);
                        callback.onRestaurantsLoaded(restaurantsList);
                    } else {
                        callback.onError(null);
                    }
                } else {
                    callback.onError(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }
}