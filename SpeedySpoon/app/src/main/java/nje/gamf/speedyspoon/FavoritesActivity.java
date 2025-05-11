package nje.gamf.speedyspoon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nje.gamf.speedyspoon.Adapters.MenuItemAdapter;
import nje.gamf.speedyspoon.Models.MenuItem;
import nje.gamf.speedyspoon.Repositories.MenuItemRepository;
import nje.gamf.speedyspoon.Repositories.MenuItemCallback;

public class FavoritesActivity extends AppCompatActivity implements MenuItemAdapter.OnFavoriteChangedListener {
    private static final String TAG = "FavoritesActivity";
    private RecyclerView favoritesRecyclerView;
    private MenuItemAdapter adapter;
    private List<MenuItem> favoriteMenuItems = new ArrayList<>();
    private FavoritesManager favoritesManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout emptyFavoritesLayout;
    private MenuItemRepository menuItemRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Inicializálás
        favoritesManager = FavoritesManager.getInstance();
        menuItemRepository = new MenuItemRepository();

        // Ellenőrzés: vannak-e kedvencek?
        Set<String> favoriteIds = favoritesManager.getFavoriteIds();
        Log.d(TAG, "onCreate: Kedvencek száma: " + favoriteIds.size() + ", kedvencek: " + favoriteIds);

        // UI elemek inicializálása
        setupUI();
        
        // Bottom navigation beállítása
        setupBottomNavigation();
        
        // Kedvencek betöltése
        loadFavorites();
    }

    private void setupUI() {
        // Toolbar beállítása
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // RecyclerView beállítása
        favoritesRecyclerView = findViewById(R.id.favorites_recycler_view);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MenuItemAdapter(favoriteMenuItems, false);
        adapter.setOnFavoriteChangedListener(this);
        favoritesRecyclerView.setAdapter(adapter);

        // Üres nézet beállítása
        emptyFavoritesLayout = findViewById(R.id.empty_favorites_layout);
        Button browseRestaurantsButton = findViewById(R.id.browse_restaurants_button);
        browseRestaurantsButton.setOnClickListener(v -> {
            Intent intent = new Intent(FavoritesActivity.this, RestaurantsActivity.class);
            startActivity(intent);
        });

        // Swipe refresh beállítása
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this::loadFavorites);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.primaryDark);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_favorites);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_restaurants) {
                Intent intent = new Intent(FavoritesActivity.this, RestaurantsActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_orders) {
                Intent intent = new Intent(FavoritesActivity.this, OrderDetailsActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
                Intent intent = new Intent(FavoritesActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            // Favorites-ra nem kell semmit csinálni, mert már ott vagyunk
            return true;
        });
    }

    private void loadFavorites() {
        swipeRefreshLayout.setRefreshing(true);
        
        // Kedvenc ID-k lekérése
        Set<String> favoriteIds = favoritesManager.getFavoriteIds();
        Log.d(TAG, "loadFavorites: Kedvencek száma: " + favoriteIds.size() + ", kedvencek: " + favoriteIds);
        
        // Ha nincsenek kedvencek, mutassuk az üres nézetet
        if (favoriteIds.isEmpty()) {
            Log.d(TAG, "loadFavorites: Nincsenek kedvencek, üres nézet megjelenítése");
            showEmptyView();
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        
        // Menüelemek lekérése a repository-ból
        Log.d(TAG, "loadFavorites: Menüelemek lekérése kezdődik");
        menuItemRepository.getAllMenuItems(new MenuItemCallback() {
            @Override
            public void onMenuItemsLoaded(List<MenuItem> menuItems) {
                Log.d(TAG, "onMenuItemsLoaded: " + menuItems.size() + " menüelem betöltve");
                favoriteMenuItems.clear();
                
                // Csak a kedvenceket szűrjük
                for (MenuItem item : menuItems) {
                    Log.d(TAG, "onMenuItemsLoaded: Ellenőrzés: " + item.getId() + " - " + item.getName() + 
                        ", kedvenc? " + (favoriteIds.contains(item.getId()) ? "IGEN" : "NEM"));
                    if (favoriteIds.contains(item.getId())) {
                        favoriteMenuItems.add(item);
                        Log.d(TAG, "onMenuItemsLoaded: Hozzáadva a kedvencekhez: " + item.getName());
                    }
                }
                
                Log.d(TAG, "onMenuItemsLoaded: " + favoriteMenuItems.size() + " kedvenc menüelem szűrve");
                if (favoriteMenuItems.isEmpty()) {
                    Log.d(TAG, "onMenuItemsLoaded: Nincsenek kedvenc ételek, üres nézet megjelenítése");
                    showEmptyView();
                } else {
                    Log.d(TAG, "onMenuItemsLoaded: Kedvenc ételek megjelenítése");
                    showContentView();
                }
                
                adapter.updateMenuItems(favoriteMenuItems);
                swipeRefreshLayout.setRefreshing(false);
            }
            
            @Override
            public void onError(DatabaseError error) {
                Log.e(TAG, "onError: " + error.getMessage());
                showEmptyView();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(FavoritesActivity.this, 
                    "Hiba történt az adatok betöltésekor: " + error.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void showEmptyView() {
        Log.d(TAG, "showEmptyView: Üres nézet megjelenítése");
        emptyFavoritesLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
    }
    
    private void showContentView() {
        Log.d(TAG, "showContentView: Tartalmi nézet megjelenítése");
        emptyFavoritesLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFavoriteChanged(Set<String> newFavorites) {
        Log.d(TAG, "onFavoriteChanged: Kedvencek változtak: " + newFavorites.size() + " elem, kedvencek: " + newFavorites);
        // Kedvencek változtak, újratöltjük a listát
        loadFavorites();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Activity folytatódik, kedvencek újratöltése");
        // Frissítjük a listát, amikor visszatérünk az activity-hez
        loadFavorites();
    }
} 