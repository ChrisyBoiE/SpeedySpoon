package nje.gamf.speedyspoon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import nje.gamf.speedyspoon.Adapters.OrderItemAdapter;
import nje.gamf.speedyspoon.Models.Detail;
import nje.gamf.speedyspoon.Models.Order;
import nje.gamf.speedyspoon.Models.Status;
import nje.gamf.speedyspoon.Models.User;
import nje.gamf.speedyspoon.Repositories.OrderCallback;
import nje.gamf.speedyspoon.Repositories.OrderRepository;

// Activity egy rendelés részletes megjelenítéséhez
public class SingleOrderDetailsActivity extends AppCompatActivity implements OrderCallback {
    // UI elemek
    private TextView orderNumber; // Rendelés száma
    private TextView orderDate; // Rendelés dátuma
    private TextView orderStatus; // Rendelés állapota
    private TextView restaurantName; // Étterem neve
    private TextView subtotalText; // Részösszeg
    private TextView deliveryFeeText; // Szállítási költség
    private TextView totalText; // Végösszeg
    private TextView addressName; // Szállítási cím neve
    private TextView addressDetails; // Szállítási cím részletei
    private RecyclerView orderItemsRecycler; // Rendelés tételek listája
    private Button orderAgainButton; // Újrarendelés gomb

    // Adatok
    private String orderId; // Rendelés azonosító
    private OrderRepository orderRepository; // Rendelések kezelése
    private Order order; // Aktuális rendelés
    private OrderItemAdapter orderItemAdapter; // Adapter a rendelés tételeihez

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        try {
            // Adatok lekérése az Intent-ből
            Intent intent = getIntent();
            if (intent != null) {
                orderId = intent.getStringExtra("order_id");
            }

            if (orderId == null || orderId.isEmpty()) {
                Toast.makeText(this, "Hiba történt a rendelés betöltésekor", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Repository inicializálása
            orderRepository = new OrderRepository();

            // UI elemek inicializálása
            initializeViews();

            // Toolbar beállítása
            setupToolbar();

            // Alsó navigáció beállítása
            setupBottomNavigation();

            // Rendelés adatainak betöltése
            loadOrderDetails();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Hiba történt az alkalmazás indításakor", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // UI elemek inicializálása
    private void initializeViews() {
        try {
            // View elemek keresése
            orderNumber = findViewById(R.id.order_number);
            orderDate = findViewById(R.id.order_date);
            orderStatus = findViewById(R.id.order_status);
            restaurantName = findViewById(R.id.restaurant_name);
            subtotalText = findViewById(R.id.subtotal_text);
            deliveryFeeText = findViewById(R.id.delivery_fee_text);
            totalText = findViewById(R.id.total_text);
            addressName = findViewById(R.id.address_name);
            addressDetails = findViewById(R.id.address_details);
            orderItemsRecycler = findViewById(R.id.order_items_recycler);
            orderItemsRecycler.setLayoutManager(new LinearLayoutManager(this));
            orderItemAdapter = new OrderItemAdapter(new ArrayList<>());
            orderItemsRecycler.setAdapter(orderItemAdapter);
            orderAgainButton = findViewById(R.id.order_again_button);

            // Újrarendelés gomb kezelése
            orderAgainButton.setOnClickListener(v -> {
                Intent intent = new Intent(SingleOrderDetailsActivity.this, RestaurantsActivity.class);
                startActivity(intent);
                finish();
            });
        } catch (Exception e) {
            throw e;
        }
    }

    // Toolbar beállítása
    private void setupToolbar() {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        } catch (Exception e) {
            throw e;
        }
    }

    // Alsó navigáció beállítása
    private void setupBottomNavigation() {
        try {
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.navigation_orders);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                if (item.getItemId() == R.id.navigation_home) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.navigation_restaurants) {
                    startActivity(new Intent(this, RestaurantsActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.navigation_favorites) {
                    startActivity(new Intent(this, FavoritesActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.navigation_profile) {
                    startActivity(new Intent(this, ProfileActivity.class));
                    finish();
                    return true;
                }
                return true;
            });
        } catch (Exception e) {
            throw e;
        }
    }

    // Rendelés adatainak betöltése
    private void loadOrderDetails() {
        try {
            orderRepository.fetchOrderById(orderId, new OrderCallback() {
                @Override
                public void onOrdersLoaded(List<Order> orders) {
                    if (orders != null && !orders.isEmpty()) {
                        order = orders.get(0);
                        displayOrderDetails();
                    } else {
                        Toast.makeText(SingleOrderDetailsActivity.this, "Nem sikerült betölteni a rendelést", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onOrderStatusLoaded(List<Status> orderStatus) {
                }

                @Override
                public void onOrderDetailsLoaded(List<Detail> orderDetails) {
                }

                @Override
                public void onError(DatabaseError error) {
                    Toast.makeText(SingleOrderDetailsActivity.this, "Hiba történt: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Hiba történt a rendelés betöltésekor", Toast.LENGTH_SHORT).show();
        }
    }

    // Rendelés adatainak megjelenítése
    private void displayOrderDetails() {
        try {
            if (order == null) {
                Toast.makeText(this, "Nem sikerült betölteni a rendelést", Toast.LENGTH_SHORT).show();
                return;
            }

            // Rendelés azonosító megjelenítése
            orderNumber.setText("Rendelés #" + orderId);
            
            // Rendelés dátumának beállítása
            String date = order.getOrderDate();
            if (date != null && !date.isEmpty()) {
                orderDate.setText("Dátum: " + date);
            } else {
                orderDate.setText("Dátum: Ismeretlen");
            }
            
            // Rendelés állapotának beállítása
            String status = order.getStatus();
            if (status != null && !status.isEmpty()) {
                orderStatus.setText(status);
            } else {
                orderStatus.setText("Státusz: Ismeretlen");
            }
            
            // Étterem neve beállítása
            restaurantName.setText("Olasz Konyha");
            
            // Rendelés tételek megjelenítése
            if (order.getOrderDetails() != null && order.getOrderDetails().getOrderDetails() != null) {
                orderItemAdapter.updateItems(order.getOrderDetails().getOrderDetails());
                
                // Részösszeg és végösszeg számítása
                int subtotal = 0;
                for (Detail detail : order.getOrderDetails().getOrderDetails()) {
                    subtotal += detail.getPrice() * detail.getQuantity();
                }
                subtotalText.setText(subtotal + " Ft");
                deliveryFeeText.setText("500 Ft");
                totalText.setText((subtotal + 500) + " Ft");
            }
            
            // Szállítási cím beállítása
            User currentUser = MainActivity.getCurrentUser();
            if (currentUser != null) {
                // Mindig "Otthon" címként jelenítjük meg
                addressName.setText("Otthon");
                
                // Cím részletek összeállítása
                StringBuilder addressText = new StringBuilder();
                if (currentUser.getAddress() != null && !currentUser.getAddress().isEmpty()) {
                    addressText.append(currentUser.getAddress());
                    
                    if (currentUser.getCity() != null && !currentUser.getCity().isEmpty()) {
                        addressText.append(", ").append(currentUser.getCity());
                    }
                } else {
                    addressText.append("Nincs megadva szállítási cím");
                }
                
                addressDetails.setText(addressText.toString());
            } else {
                addressName.setText("Otthon");
                addressDetails.setText("Nincs elérhető szállítási cím");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    // Callback metódusok
    @Override
    public void onOrdersLoaded(List<Order> orders) {
    }

    @Override
    public void onOrderStatusLoaded(List<Status> orderStatus) {
    }

    @Override
    public void onOrderDetailsLoaded(List<Detail> orderDetails) {
    }

    @Override
    public void onError(DatabaseError error) {
        Toast.makeText(this, "Hiba történt: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }
} 