package nje.gamf.speedyspoon;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;

import java.util.List;

import nje.gamf.speedyspoon.Adapters.MenuItemAdapter;
import nje.gamf.speedyspoon.Models.Category;
import nje.gamf.speedyspoon.Models.MenuItem;
import nje.gamf.speedyspoon.Models.Restaurant;
import nje.gamf.speedyspoon.Repositories.CategoriesCallback;
import nje.gamf.speedyspoon.Repositories.CategoriesRepository;
import nje.gamf.speedyspoon.Repositories.MenuItemCallback;
import nje.gamf.speedyspoon.Repositories.MenuItemRepository;
import nje.gamf.speedyspoon.Repositories.RestaurantCallback;
import nje.gamf.speedyspoon.Repositories.RestaurantRepository;

public class MainActivity extends AppCompatActivity {

    private MenuItemRepository menuItemRepository;
    private RestaurantRepository restaurantRepository;
    private CategoriesRepository categoriesRepository;

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

        // Testing menu item repo
        menuItemRepository = new MenuItemRepository();

        menuItemRepository.fetchMenuItems(new MenuItemCallback() {
            @Override
            public void onMenuItemsLoaded(List<MenuItem> menuItems) {
                RecyclerView recyclerView = findViewById(R.id.recommended_recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                MenuItemAdapter adapter = new MenuItemAdapter(menuItems);
                recyclerView.setAdapter(adapter);
                Log.d("testing", "Menu items loaded: " + menuItems.size());
            }

            @Override
            public void onError(DatabaseError error) {
                Log.w("testing", "Failed to read menu items", error.toException());
            }
        });

        // testing categories repo
        categoriesRepository = new CategoriesRepository();

        categoriesRepository.fetchCategories(new CategoriesCallback() {
            @Override
            public void onCategoriesLoaded(List<Category> categories) {
                Log.d("testing", "categories loaded: " + categories.size());
            }

            @Override
            public void onError(DatabaseError error) {
                Log.w("testing", "Failed to read categories", error.toException());
            }
        });

        // testing restaurant repo
        restaurantRepository = new RestaurantRepository();

        restaurantRepository.fetchRestaurants(new RestaurantCallback() {
            @Override
            public void onRestaurantsLoaded(List<Restaurant> restaurants) {
                Log.d("testing", "restaurants loaded: " + restaurants.size());
            }

            @Override
            public void onError(DatabaseError error) {
                Log.w("testing", "Failed to read restaurants", error.toException());
            }
        });

    }
}