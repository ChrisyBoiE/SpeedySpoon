package nje.gamf.speedyspoon;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import nje.gamf.speedyspoon.Models.Category;
import nje.gamf.speedyspoon.ViewModels.CategoriesViewModel;

public class RestaurantsActivity extends AppCompatActivity {

    private CategoriesViewModel categoriesViewModel;
    private ChipGroup categoryChipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

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

        // Add categories to chip
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

            // Add the chip to the ChipGroup
            categoryChipGroup.addView(chip);
        }
    }
} 