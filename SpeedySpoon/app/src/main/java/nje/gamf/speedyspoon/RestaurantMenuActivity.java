package nje.gamf.speedyspoon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nje.gamf.speedyspoon.Adapters.MenuItemAdapter;
import nje.gamf.speedyspoon.Models.Restaurant;
import nje.gamf.speedyspoon.Repositories.MenuItemCallback;
import nje.gamf.speedyspoon.Repositories.MenuItemRepository;

public class RestaurantMenuActivity extends AppCompatActivity {
    private RecyclerView menuRecyclerView;
    private TextView emptyView;
    private TextView restaurantTitleTextView;
    private TextView restaurantDescriptionTextView;
    private MenuItemRepository menuItemRepository;
    private MenuItemAdapter menuItemAdapter;
    private Restaurant restaurant;
    private String restaurantId;
    private DatabaseReference databaseReference;
    private static final String TAG = "RestaurantMenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get restaurant data from intent
        if (getIntent() != null && getIntent().hasExtra("restaurant")) {
            restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");
        }
        
        // If no restaurant object or restaurant has no ID, check if we have a restaurantId parameter
        if (restaurant == null && getIntent() != null && getIntent().hasExtra("restaurantId")) {
            restaurantId = getIntent().getStringExtra("restaurantId");
            // Load restaurant data from Firebase
            loadRestaurantData(restaurantId);
        } else if (restaurant != null) {
            restaurantId = restaurant.getId();
            setupUI();
        } else {
            // No usable restaurant data
            Toast.makeText(this, "Hiba történt az étterem betöltése közben", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }
    
    private void loadRestaurantData(String restaurantId) {
        databaseReference.child("restaurants").child(restaurantId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            restaurant = dataSnapshot.getValue(Restaurant.class);
                            if (restaurant != null) {
                                restaurant.setId(restaurantId);
                                setupUI();
                            } else {
                                handleError("Nem sikerült betölteni az étterem adatait");
                            }
                        } else {
                            handleError("Az étterem nem található");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        handleError("Hiba történt az adatok betöltése közben: " + error.getMessage());
                    }
                });
    }
    
    private void handleError(String errorMessage) {
        Log.e(TAG, errorMessage);
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        finish();
    }
    
    private void setupUI() {
        // Setup toolbar with back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(restaurant != null ? restaurant.getName() : "Étterem");

        // Initialize views
        menuRecyclerView = findViewById(R.id.menu_recycler_view);
        emptyView = findViewById(R.id.empty_view);
        
        // Setup additional restaurant info
        try {
            restaurantTitleTextView = findViewById(R.id.restaurant_title);
            restaurantDescriptionTextView = findViewById(R.id.restaurant_description);
            
            if (restaurantTitleTextView != null && restaurant != null) {
                restaurantTitleTextView.setText(restaurant.getName());
            }
            
            if (restaurantDescriptionTextView != null && restaurant != null) {
                restaurantDescriptionTextView.setText(restaurant.getDescription());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting up restaurant info", e);
        }

        // Setup RecyclerView
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuItemAdapter = new MenuItemAdapter(new ArrayList<>());
        menuRecyclerView.setAdapter(menuItemAdapter);

        // Load menu items
        loadMenuItemsForRestaurant();
    }

    private void loadMenuItemsForRestaurant() {
        // Log for debugging
        Log.d(TAG, "Loading menu items for restaurant ID: " + restaurantId);
        
        // Query the specific restaurant to get its menuItems
        databaseReference.child("restaurants").child(restaurantId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Log.e(TAG, "Restaurant not found in database: " + restaurantId);
                            updateEmptyView(new ArrayList<>());
                            return;
                        }
                        
                        // Get the menuItems map from the restaurant
                        DataSnapshot menuItemsSnapshot = dataSnapshot.child("menuItems");
                        if (!menuItemsSnapshot.exists() || !menuItemsSnapshot.hasChildren()) {
                            Log.d(TAG, "No menuItems found for restaurant: " + restaurantId);
                            updateEmptyView(new ArrayList<>());
                            return;
                        }
                        
                        // Create a list to store all menu items
                        final List<nje.gamf.speedyspoon.Models.MenuItem> menuItems = new ArrayList<>();
                        final Map<String, Boolean> processedItems = new HashMap<>();
                        final int[] expectedItemCount = {(int) menuItemsSnapshot.getChildrenCount()};
                        
                        Log.d(TAG, "Found " + expectedItemCount[0] + " menuItems to load");
                        
                        // Iterate through all menuItems entries
                        for (DataSnapshot itemSnapshot : menuItemsSnapshot.getChildren()) {
                            String menuItemId = itemSnapshot.getKey();
                            Boolean isActive = itemSnapshot.getValue(Boolean.class);
                            
                            // Only process items that are true (active)
                            if (isActive != null && isActive) {
                                Log.d(TAG, "Processing menu item ID: " + menuItemId);
                                
                                // Get the menu item details
                                databaseReference.child("menuItems").child(menuItemId)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot itemData) {
                                                // Mark this item as processed
                                                processedItems.put(menuItemId, true);
                                                
                                                if (itemData.exists()) {
                                                    nje.gamf.speedyspoon.Models.MenuItem menuItem = 
                                                            itemData.getValue(nje.gamf.speedyspoon.Models.MenuItem.class);
                                                    if (menuItem != null) {
                                                        menuItem.setId(menuItemId);
                                                        menuItems.add(menuItem);
                                                        Log.d(TAG, "Added menu item: " + menuItem.getName() + " (ID: " + menuItemId + ")");
                                                    } else {
                                                        Log.e(TAG, "Failed to parse menu item: " + menuItemId);
                                                    }
                                                } else {
                                                    Log.e(TAG, "Menu item not found: " + menuItemId);
                                                }
                                                
                                                // Check if all items are processed
                                                checkAndUpdateUI(menuItems, processedItems, expectedItemCount[0]);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError error) {
                                                // Mark this item as processed even if there's an error
                                                processedItems.put(menuItemId, true);
                                                Log.e(TAG, "Error loading menu item " + menuItemId + ": " + error.getMessage());
                                                
                                                // Check if all items are processed
                                                checkAndUpdateUI(menuItems, processedItems, expectedItemCount[0]);
                                            }
                                        });
                            } else {
                                // Mark inactive items as processed
                                processedItems.put(menuItemId, true);
                                Log.d(TAG, "Skipping inactive menu item: " + menuItemId);
                                
                                // Check if all items are processed
                                checkAndUpdateUI(menuItems, processedItems, expectedItemCount[0]);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e(TAG, "Failed to load restaurant data: " + error.getMessage());
                        Toast.makeText(RestaurantMenuActivity.this, 
                                "Hiba történt az étterem adatok betöltése közben", 
                                Toast.LENGTH_SHORT).show();
                        updateEmptyView(new ArrayList<>());
                    }
                });
    }
    
    private void checkAndUpdateUI(List<nje.gamf.speedyspoon.Models.MenuItem> menuItems, 
                                 Map<String, Boolean> processedItems, 
                                 int expectedCount) {
        // Only update UI if all items are processed
        if (processedItems.size() >= expectedCount) {
            Log.d(TAG, "All menu items processed. Found " + menuItems.size() + " active items");
            
            // Update the UI on the main thread
            runOnUiThread(() -> {
                menuItemAdapter.updateMenuItems(menuItems);
                updateEmptyView(menuItems);
                
                // Show a toast with the count for debugging
                Toast.makeText(RestaurantMenuActivity.this, 
                        "Betöltve: " + menuItems.size() + " étel", 
                        Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void updateEmptyView(List<nje.gamf.speedyspoon.Models.MenuItem> menuItems) {
        if (menuItems == null || menuItems.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            menuRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            menuRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_restaurant_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_cart) {
            // Kosár activity indítása
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 