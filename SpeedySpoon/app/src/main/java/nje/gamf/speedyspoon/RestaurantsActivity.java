package nje.gamf.speedyspoon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import nje.gamf.speedyspoon.Adapters.RestaurantAdapter;
import nje.gamf.speedyspoon.Models.Category;
import nje.gamf.speedyspoon.Models.Restaurant;
import nje.gamf.speedyspoon.Repositories.RestaurantCallback;
import nje.gamf.speedyspoon.Repositories.RestaurantRepository;
import nje.gamf.speedyspoon.ViewModels.CategoriesViewModel;

public class RestaurantsActivity extends AppCompatActivity {
    private CategoriesViewModel categoriesViewModel;
    private ChipGroup categoryChipGroup;
    private RecyclerView restaurantsRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyView;
    private EditText searchEditText;
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

        // Éttermek betöltése
        loadRestaurants();

        // Alsó navigáció megjelenítése
        setupBottomNavigation();
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
            // Restaurants-ra nem kell semmit csinálni, mert már ott vagyunk
            return true;
        });

        categoryChipGroup = findViewById(R.id.category_chip_group);
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        categoriesViewModel.categories.observe(this, this::displayCategories);
    }

    private void displayCategories(List<Category> categories) {
        categoryChipGroup.removeAllViews();

        for (Category category : categories) {
            Chip chip = new Chip(this);
            chip.setText(category.getName());

            chip.setChipDrawable(ChipDrawable.createFromAttributes(this,
                    null,
                    0,
                    com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice));

            chip.setCheckable(true);

            if (category.equals("All")) {
                chip.setChecked(true);
            }

            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    String selectedCategory = ((Chip) buttonView).getText().toString();
                }
            });

            categoryChipGroup.addView(chip);
        }
    }

    private void loadRestaurants() {
        swipeRefreshLayout.setRefreshing(true);
        restaurantRepository.fetchRestaurants(new RestaurantCallback() {
            @Override
            public void onRestaurantsLoaded(List<Restaurant> restaurants) {
                allRestaurants = restaurants;
                restaurantAdapter.updateRestaurants(restaurants);
                updateEmptyView();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(DatabaseError error) {
                Toast.makeText(RestaurantsActivity.this, 
                    "Hiba történt az éttermek betöltése közben", 
                    Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateEmptyView() {
        if (allRestaurants == null || allRestaurants.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            restaurantsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            restaurantsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}