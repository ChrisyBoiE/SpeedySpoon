package nje.gamf.speedyspoon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nje.gamf.speedyspoon.Models.User;

public class ProfileActivity extends AppCompatActivity {
    // Mezők és gombok deklarálása
    private TextInputEditText nameInput, emailInput, phoneInput, addressInput, cityInput;
    private Button saveButton;
    private User currentUser;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Mezők inicializálása
        initializeViews();
        
        // Felhasználó adatainak betöltése
        loadUserData();
        
        // Mentés gomb beállítása
        setupSaveButton();
        
        // Alsó navigáció beállítása
        setupBottomNavigation();
    }

    // Mezők és gombok inicializálása
    private void initializeViews() {
        nameInput = findViewById(R.id.input_name);
        emailInput = findViewById(R.id.input_email);
        phoneInput = findViewById(R.id.input_phone);
        addressInput = findViewById(R.id.input_address);
        cityInput = findViewById(R.id.input_city);
        saveButton = findViewById(R.id.btn_save);
    }

    // Felhasználó adatainak betöltése a mezőkbe
    private void loadUserData() {
        currentUser = MainActivity.getCurrentUser();
        if (currentUser != null) {
            nameInput.setText(currentUser.getUsername());
            emailInput.setText(currentUser.getEmail());
            phoneInput.setText(currentUser.getPhoneNumber());
            if (currentUser.getAddress() != null) {
                addressInput.setText(currentUser.getAddress());
            }
            if (currentUser.getCity() != null) {
                cityInput.setText(currentUser.getCity());
            }
        }
    }

    // Mentés gomb kezelése és adatok frissítése
    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> {
            if (currentUser != null) {
                // Lokális adatok frissítése
                currentUser.setUsername(nameInput.getText().toString());
                currentUser.setEmail(emailInput.getText().toString());
                currentUser.setPhoneNumber(phoneInput.getText().toString());
                currentUser.setAddress(addressInput.getText().toString());
                currentUser.setCity(cityInput.getText().toString());

                // Firebase adatbázis frissítése
                userRef = FirebaseDatabase.getInstance().getReference("users");
                
                // Felhasználó keresése email alapján
                userRef.orderByChild("email").equalTo(currentUser.getEmail())
                    .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                        @Override
                        public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Felhasználó kulcsának megtalálása
                                String userKey = dataSnapshot.getChildren().iterator().next().getKey();
                                // Adatok frissítése az adatbázisban
                                userRef.child(userKey).setValue(currentUser)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(ProfileActivity.this, 
                                            "Profil sikeresen frissítve!", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(ProfileActivity.this, 
                                            "Hiba történt a mentés során!", Toast.LENGTH_SHORT).show();
                                    });
                            }
                        }

                        @Override
                        public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                            Toast.makeText(ProfileActivity.this, 
                                "Hiba történt az adatok frissítése során!", Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });
    }

    // Alsó navigációs sáv beállítása
    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_restaurants) {
                Intent intent = new Intent(ProfileActivity.this, RestaurantsActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_favorites) {
                Intent intent = new Intent(ProfileActivity.this, FavoritesActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_orders) {
                Intent intent = new Intent(ProfileActivity.this, OrderDetailsActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            return true;
        });
    }
}