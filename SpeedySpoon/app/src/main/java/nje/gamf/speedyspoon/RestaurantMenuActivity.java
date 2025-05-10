package nje.gamf.speedyspoon;

import android.os.Bundle;
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
import java.util.List;

import nje.gamf.speedyspoon.Adapters.MenuItemAdapter;
import nje.gamf.speedyspoon.Models.Restaurant;
import nje.gamf.speedyspoon.Repositories.MenuItemCallback;
import nje.gamf.speedyspoon.Repositories.MenuItemRepository;

public class RestaurantMenuActivity extends AppCompatActivity {
    private RecyclerView menuRecyclerView;
    private TextView emptyView;
    private MenuItemRepository menuItemRepository;
    private MenuItemAdapter menuItemAdapter;
    private Restaurant restaurant;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get restaurant data from intent
        restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");
        if (restaurant == null) {
            Toast.makeText(this, "Hiba történt az étterem betöltése közben", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Setup toolbar with back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(restaurant.getName());

        // Initialize views
        menuRecyclerView = findViewById(R.id.menu_recycler_view);
        emptyView = findViewById(R.id.empty_view);

        // Setup RecyclerView
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuItemAdapter = new MenuItemAdapter(new ArrayList<>());
        menuRecyclerView.setAdapter(menuItemAdapter);

        // Initialize repository
        menuItemRepository = new MenuItemRepository();

        // Load menu items
        loadMenuItems();
    }

    private void loadMenuItems() {
        // Get menu items from Firebase
        databaseReference.child("menuItems")
                .orderByChild("restaurantId")
                .equalTo(restaurant.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<nje.gamf.speedyspoon.Models.MenuItem> menuItems = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            nje.gamf.speedyspoon.Models.MenuItem menuItem = snapshot.getValue(nje.gamf.speedyspoon.Models.MenuItem.class);
                            if (menuItem != null) {
                                menuItem.setId(snapshot.getKey());
                                menuItems.add(menuItem);
                            }
                        }
                        
                        menuItemAdapter.updateMenuItems(menuItems);
                        updateEmptyView(menuItems);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(RestaurantMenuActivity.this,
                                "Hiba történt az ételek betöltése közben: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 