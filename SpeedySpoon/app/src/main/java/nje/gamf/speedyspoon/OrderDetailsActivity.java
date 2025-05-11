package nje.gamf.speedyspoon;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OrderDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_orders);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_restaurants) {
                Intent intent = new Intent(OrderDetailsActivity.this, RestaurantsActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_favorites) {
                Intent intent = new Intent(OrderDetailsActivity.this, FavoritesActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
                Intent intent = new Intent(OrderDetailsActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            // Ha az Orders-ra kattintasz, NE csináljon semmit
            return true;
        });
    }
} 