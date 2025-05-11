package nje.gamf.speedyspoon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import nje.gamf.speedyspoon.Adapters.OrderAdapter;
import nje.gamf.speedyspoon.Models.Detail;
import nje.gamf.speedyspoon.Models.Order;
import nje.gamf.speedyspoon.Models.Status;
import nje.gamf.speedyspoon.Models.User;
import nje.gamf.speedyspoon.Repositories.OrderCallback;
import nje.gamf.speedyspoon.Repositories.OrderRepository;

public class OrderDetailsActivity extends AppCompatActivity implements OrderCallback {
    private RecyclerView ordersRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout emptyOrdersLayout;
    private OrderAdapter orderAdapter;
    private OrderRepository orderRepository;
    private List<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        try {
            // Inicializáljuk a változókat
            orders = new ArrayList<>();
            orderRepository = new OrderRepository();
            
            // UI elemek inicializálása
            initializeViews();
            
            // RecyclerView beállítása
            setupRecyclerView();
            
            // SwipeRefreshLayout beállítása
            setupSwipeRefresh();
            
            // Alsó navigáció beállítása
            setupBottomNavigation();
            
            // Rendelések betöltése
            loadOrders();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Hiba történt az alkalmazás indításakor", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        try {
            ordersRecyclerView = findViewById(R.id.orders_recycler_view);
            swipeRefreshLayout = findViewById(R.id.swipe_refresh);
            emptyOrdersLayout = findViewById(R.id.empty_orders_layout);
        } catch (Exception e) {
            throw e;
        }
    }

    private void setupRecyclerView() {
        try {
            orderAdapter = new OrderAdapter(orders);
            ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            ordersRecyclerView.setAdapter(orderAdapter);
        } catch (Exception e) {
            throw e;
        }
    }

    private void setupSwipeRefresh() {
        try {
            swipeRefreshLayout.setOnRefreshListener(this::loadOrders);
        } catch (Exception e) {
            throw e;
        }
    }

    private void loadOrders() {
        try {
            User currentUser = MainActivity.getCurrentUser();
            if (currentUser != null) {
                orderRepository.fetchOrderByEmail(currentUser.getEmail(), this);
            } else {
                Toast.makeText(this, "Kérjük, jelentkezzen be!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Hiba történt a rendelések betöltésekor", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void setupBottomNavigation() {
        try {
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
                return true;
            });
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void onOrdersLoaded(List<Order> orders) {
        try {
            if (orders == null || orders.isEmpty()) {
                emptyOrdersLayout.setVisibility(View.VISIBLE);
                ordersRecyclerView.setVisibility(View.GONE);
                return;
            }

            emptyOrdersLayout.setVisibility(View.GONE);
            ordersRecyclerView.setVisibility(View.VISIBLE);
            orderAdapter.updateOrders(orders);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Hiba történt a rendelések betöltésekor", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOrderStatusLoaded(List<Status> orderStatus) {
        // Nem használjuk ezt a callback-et
    }

    @Override
    public void onOrderDetailsLoaded(List<Detail> orderDetails) {
        // Nem használjuk ezt a callback-et
    }

    @Override
    public void onError(DatabaseError error) {
        Toast.makeText(this, "Hiba történt: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);
    }
} 