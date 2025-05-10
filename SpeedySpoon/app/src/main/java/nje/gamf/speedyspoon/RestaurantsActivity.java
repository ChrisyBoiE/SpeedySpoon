package nje.gamf.speedyspoon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import nje.gamf.speedyspoon.Adapters.RestaurantAdapter;
import nje.gamf.speedyspoon.Models.Restaurant;
import nje.gamf.speedyspoon.Repositories.RestaurantCallback;
import nje.gamf.speedyspoon.Repositories.RestaurantRepository;

public class RestaurantsActivity extends AppCompatActivity implements RestaurantCallback {
    private RecyclerView restaurantsRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyView;
    private EditText searchEditText;
    private ChipGroup categoryChipGroup;
    private RestaurantRepository restaurantRepository;
    private RestaurantAdapter restaurantAdapter;
    private List<Restaurant> allRestaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        // A nézeteket inicializáljuk
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        restaurantsRecyclerView = findViewById(R.id.restaurants_recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        emptyView = findViewById(R.id.empty_view);
        searchEditText = findViewById(R.id.search_edit_text);
        categoryChipGroup = findViewById(R.id.category_chip_group);

        // RecyclerView beállítjuk
        restaurantsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantAdapter = new RestaurantAdapter(new ArrayList<>());
        restaurantsRecyclerView.setAdapter(restaurantAdapter);

        // Inicializáljuk a Restaurant repository-t
        restaurantRepository = new RestaurantRepository();

        // Setup SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(this::loadRestaurants);

        // Kereső
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            filterRestaurants();
            return true;
        });

        // Kategóriák szűrése
        categoryChipGroup.setOnCheckedChangeListener((group, checkedId) -> filterRestaurants());

        // Éttermek betöltése
        loadRestaurants();

        // Alsó navigáció megjelenítése
        setupBottomNavigation();
    }

    private void loadRestaurants() {
        swipeRefreshLayout.setRefreshing(true);
        restaurantRepository.fetchRestaurants(this);
    }

    private void filterRestaurants() {
        if (allRestaurants == null) return;

        String searchQuery = searchEditText.getText().toString().toLowerCase();
        String selectedCategory = null;

        int selectedChipId = categoryChipGroup.getCheckedChipId();
        if (selectedChipId != View.NO_ID) {
            Chip selectedChip = findViewById(selectedChipId);
            selectedCategory = selectedChip.getText().toString();
        }

        List<Restaurant> filteredList = new ArrayList<>();
        for (Restaurant restaurant : allRestaurants) {
            boolean matchesSearch = searchQuery.isEmpty() ||
                    restaurant.getName().toLowerCase().contains(searchQuery) ||
                    restaurant.getDescription().toLowerCase().contains(searchQuery);
            
            boolean matchesCategory = selectedCategory == null ||
                    selectedCategory.equals(getString(R.string.category_all)) ||
                    restaurant.getCuisineType().equals(selectedCategory);

            if (matchesSearch && matchesCategory) {
                filteredList.add(restaurant);
            }
        }

        restaurantAdapter.updateRestaurants(filteredList);
        updateEmptyView(filteredList.isEmpty());
    }

    private void updateEmptyView(boolean isEmpty) {
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        restaurantsRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_restaurants);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_favorites) {
                Intent intent = new Intent(RestaurantsActivity.this, FavoritesActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_orders) {
                Intent intent = new Intent(RestaurantsActivity.this, OrderDetailsActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
                Intent intent = new Intent(RestaurantsActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            return true;
        });
    }

    @Override
    public void onRestaurantsLoaded(List<Restaurant> restaurants) {
        swipeRefreshLayout.setRefreshing(false);
        allRestaurants = restaurants;
        filterRestaurants();
    }

    @Override
    public void onError(DatabaseError error) {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, "Hiba történt az éttermek betöltése közben", Toast.LENGTH_SHORT).show();
    }
} 