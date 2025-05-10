package nje.gamf.speedyspoon;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nje.gamf.speedyspoon.Adapters.RestaurantAdapter;
import nje.gamf.speedyspoon.Models.Category;
import nje.gamf.speedyspoon.Models.MenuItem;
import nje.gamf.speedyspoon.Models.Restaurant;
import nje.gamf.speedyspoon.Repositories.RestaurantCallback;
import nje.gamf.speedyspoon.Repositories.RestaurantRepository;
import nje.gamf.speedyspoon.ViewModels.CategoriesViewModel;
import androidx.annotation.NonNull;

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
    private String selectedCategory = "All";
    private DatabaseReference menuItemsRef;
    private Map<String, List<String>> restaurantMenuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        // Firebase inicializálása
        menuItemsRef = FirebaseDatabase.getInstance().getReference("menuItems");
        restaurantMenuItems = new HashMap<>();

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

        // Keresés beállítása
        setupSearch();

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

        // Add "All" category chip
        Chip allChip = new Chip(this);
        allChip.setText("Összes");
        allChip.setChipDrawable(ChipDrawable.createFromAttributes(this,
                null,
                0,
                com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice));
        allChip.setCheckable(true);
        allChip.setChecked(true);
        allChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedCategory = "All";
                applyFilters();
            }
        });
        categoryChipGroup.addView(allChip);

        // Add other category chips
        for (Category category : categories) {
            Chip chip = new Chip(this);
            chip.setText(category.getName());

            chip.setChipDrawable(ChipDrawable.createFromAttributes(this,
                    null,
                    0,
                    com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice));

            chip.setCheckable(true);

            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedCategory = category.getName();
                    applyFilters();
                }
            });

            categoryChipGroup.addView(chip);
        }
    }

    private void loadRestaurants() {
        swipeRefreshLayout.setRefreshing(true);
        
        // Menüelemek betöltése
        menuItemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                restaurantMenuItems.clear();
                for (DataSnapshot menuItemSnapshot : dataSnapshot.getChildren()) {
                    MenuItem menuItem = menuItemSnapshot.getValue(MenuItem.class);
                    if (menuItem != null) {
                        String restaurantId = menuItem.getRestaurantId();
                        if (!restaurantMenuItems.containsKey(restaurantId)) {
                            restaurantMenuItems.put(restaurantId, new ArrayList<>());
                        }
                        restaurantMenuItems.get(restaurantId).add(menuItem.getCategory());
                    }
                }
                // Éttermek betöltése a menüelemek után
                loadRestaurantsData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RestaurantsActivity.this, 
                    "Hiba történt a menüelemek betöltése közben", 
                    Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadRestaurantsData() {
        restaurantRepository.fetchRestaurants(new RestaurantCallback() {
            @Override
            public void onRestaurantsLoaded(List<Restaurant> restaurants) {
                allRestaurants = restaurants;
                applyFilters();
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

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRestaurants(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterRestaurants(String query) {
        if (allRestaurants == null) return;

        List<Restaurant> filteredList = new ArrayList<>();
        String searchQuery = query.toLowerCase().trim();

        for (Restaurant restaurant : allRestaurants) {
            boolean matchesSearch = searchQuery.isEmpty() ||
                    restaurant.getName().toLowerCase().contains(searchQuery) ||
                    restaurant.getCuisineType().toLowerCase().contains(searchQuery) ||
                    restaurant.getDescription().toLowerCase().contains(searchQuery);

            if (matchesSearch) {
                filteredList.add(restaurant);
            }
        }

        restaurantAdapter.updateRestaurants(filteredList);
        updateEmptyView();
    }

    private void applyFilters() {
        if (allRestaurants == null) return;

        String searchQuery = searchEditText.getText().toString().toLowerCase().trim();
        List<Restaurant> filteredList = new ArrayList<>();

        for (Restaurant restaurant : allRestaurants) {
            boolean matchesSearch = searchQuery.isEmpty() ||
                    restaurant.getName().toLowerCase().contains(searchQuery) ||
                    restaurant.getCuisineType().toLowerCase().contains(searchQuery) ||
                    restaurant.getDescription().toLowerCase().contains(searchQuery);

            boolean matchesCategory = selectedCategory.equals("All") ||
                    hasMenuItemInCategory(restaurant, selectedCategory);

            if (matchesSearch && matchesCategory) {
                filteredList.add(restaurant);
            }
        }

        restaurantAdapter.updateRestaurants(filteredList);
        updateEmptyView();
    }

    private boolean hasMenuItemInCategory(Restaurant restaurant, String category) {
        // Az étterem ID-jét használjuk a menüelemek kereséséhez
        String restaurantId = restaurant.getId();
        List<String> categories = restaurantMenuItems.get(restaurantId);
        return categories != null && categories.contains(category);
    }

    private void updateEmptyView() {
        if (restaurantAdapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            restaurantsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            restaurantsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}