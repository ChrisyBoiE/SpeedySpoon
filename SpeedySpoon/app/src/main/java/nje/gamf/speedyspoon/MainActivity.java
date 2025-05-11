package nje.gamf.speedyspoon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import nje.gamf.speedyspoon.Adapters.MenuItemAdapter;

public class MainActivity extends AppCompatActivity {
    private static final int RECOMMENDED_ITEM_COUNT = 4; // Maximum number of recommended items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Load recommended menu items
        loadRecommendedMenuItems();

        // Bottom navigation setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_restaurants) {
                Intent intent = new Intent(MainActivity.this, RestaurantsActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.navigation_favorites) {
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.navigation_orders) {
                Intent intent = new Intent(MainActivity.this, OrderDetailsActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            return true;
        });
    }

    private void loadRecommendedMenuItems() {
        // Initialize Firebase reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Create a query to fetch all menu items
        Query menuItemsQuery = databaseReference.child("menuItems").limitToFirst(RECOMMENDED_ITEM_COUNT);
        
        // Fetch the top 4 menu items for recommendations
        menuItemsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<nje.gamf.speedyspoon.Models.MenuItem> recommendedItems = new ArrayList<>();
                
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    nje.gamf.speedyspoon.Models.MenuItem menuItem = snapshot.getValue(nje.gamf.speedyspoon.Models.MenuItem.class);
                    if (menuItem != null) {
                        menuItem.setId(snapshot.getKey());
                        recommendedItems.add(menuItem);
                    }
                    
                    // Stop after 4 items
                    if (recommendedItems.size() >= RECOMMENDED_ITEM_COUNT) {
                        break;
                    }
                }
                
                // Setup the RecyclerView
                RecyclerView recyclerView = findViewById(R.id.recommended_recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                MenuItemAdapter adapter = new MenuItemAdapter(recommendedItems);
                recyclerView.setAdapter(adapter);
                
                Log.d("MainActivity", "Recommended items loaded: " + recommendedItems.size());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("MainActivity", "Failed to read recommended menu items", error.toException());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }
}