package nje.gamf.speedyspoon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nje.gamf.speedyspoon.Adapters.MenuItemAdapter;
import nje.gamf.speedyspoon.Models.User;
import nje.gamf.speedyspoon.Repositories.UserRepository;

public class MainActivity extends AppCompatActivity {
    // Ajánlott menüpontok maximális száma
    private static final int RECOMMENDED_ITEM_COUNT = 4;
    private TextInputEditText emailInput, passwordInput;
    private Button loginButton, registerButton;
    private TextView registerText, loginText;
    private UserRepository userRepository;
    
    // Aktuális felhasználó tárolása
    private static User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        userRepository = new UserRepository();
        showLoginScreen();
    }

    // Bejelentkező képernyő megjelenítése
    private void showLoginScreen() {
        setContentView(R.layout.activity_login);

        // Bejelentkezési mezők inicializálása
        emailInput = findViewById(R.id.input_email);
        passwordInput = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.btn_login);
        registerText = findViewById(R.id.text_register);

        // Bejelentkezés gomb kezelése
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            // Mezők ellenőrzése
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Kérjük, töltse ki mindkét mezőt!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Bejelentkezés kezelése
            userRepository.loginUser(email, password, new UserRepository.UserLoginCallback() {
                @Override
                public void onLoginSuccess(User user) {
                    // Felhasználó adatainak tárolása
                    currentUser = user;
                    Toast.makeText(MainActivity.this, "Sikeres bejelentkezés!", Toast.LENGTH_SHORT).show();
                    showHomeScreen();
                }

                @Override
                public void onLoginFailure(String errorMessage) {
                    Toast.makeText(MainActivity.this, "Hiba történt: " + errorMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUserNotFound() {
                    Toast.makeText(MainActivity.this, "Felhasználó nem található!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onIncorrectPassword() {
                    Toast.makeText(MainActivity.this, "Hibás jelszó!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Regisztráció gomb kezelése
        registerText.setOnClickListener(v -> showRegisterScreen());
    }

    // Regisztrációs képernyő megjelenítése
    private void showRegisterScreen() {
        setContentView(R.layout.activity_register);

        // Regisztrációs mezők inicializálása
        TextInputEditText nameInput = findViewById(R.id.input_name);
        emailInput = findViewById(R.id.input_email);
        passwordInput = findViewById(R.id.input_password);
        registerButton = findViewById(R.id.btn_register);
        loginText = findViewById(R.id.text_login);

        // Regisztráció gomb kezelése
        registerButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            // Mezők ellenőrzése
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Kérjük, töltse ki az összes mezőt!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Új felhasználó létrehozása
            User newUser = new User(email, password, "", name, new HashMap<>());

            // Regisztráció kezelése
            userRepository.registerUser(newUser, new UserRepository.UserRegistrationCallback() {
                @Override
                public void onRegistrationSuccess() {
                    Toast.makeText(MainActivity.this, "Sikeres regisztráció!", Toast.LENGTH_SHORT).show();
                    showHomeScreen();
                }

                @Override
                public void onRegistrationFailure(String errorMessage) {
                    Toast.makeText(MainActivity.this, "Hiba történt: " + errorMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUserAlreadyExists() {
                    Toast.makeText(MainActivity.this, "Ez az email cím már foglalt!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Bejelentkezés gomb kezelése
        loginText.setOnClickListener(v -> showLoginScreen());
    }

    // Főképernyő megjelenítése
    private void showHomeScreen() {
        setContentView(R.layout.activity_home);
        
        // Edge-to-edge beállítások
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Alsó navigáció beállítása
        setupBottomNavigation();

        // Ajánlott menüpontok betöltése
        loadRecommendedMenuItems();
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
                if (recyclerView != null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                MenuItemAdapter adapter = new MenuItemAdapter(recommendedItems);
                recyclerView.setAdapter(adapter);
                }
                
                Log.d("MainActivity", "Recommended items loaded: " + recommendedItems.size());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("MainActivity", "Failed to read recommended menu items", error.toException());
            }
        });
    }

    // Alsó navigáció beállítása
    private void setupBottomNavigation() {
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

    @Override
    protected void onResume() {
        super.onResume();
        // Csak akkor állítsuk be a navigációt, ha a home képernyőn vagyunk
        if (findViewById(R.id.bottom_navigation) != null) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    // Felhasználó adatainak lekérdezése
    public static User getCurrentUser() {
        return currentUser;
    }

    // Felhasználó kijelentkeztetése
    public static void logoutUser() {
        currentUser = null;
    }
}