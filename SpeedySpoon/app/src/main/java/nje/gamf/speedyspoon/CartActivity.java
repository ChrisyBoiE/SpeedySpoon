package nje.gamf.speedyspoon;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nje.gamf.speedyspoon.Adapters.MenuItemAdapter;

public class CartActivity extends AppCompatActivity {
    
    private RecyclerView cartRecyclerView;
    private View emptyCartLayout;
    private Button checkoutButton;
    private CartManager cartManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        
        // Toolbar beállítása
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kosár");
        
        // CartManager inicializálása
        cartManager = CartManager.getInstance();
        
        // Nézetek inicializálása
        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        emptyCartLayout = findViewById(R.id.empty_cart_layout);
        checkoutButton = findViewById(R.id.checkout_button);
        
        // Böngéssz az éttermek között gomb kezelése
        Button browseRestaurantsButton = findViewById(R.id.browse_restaurants_button);
        browseRestaurantsButton.setOnClickListener(v -> {
            // Navigálás az éttermek fülre
            finish();
        });
        
        // RecyclerView beállítása
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Fizetés gomb beállítása
        checkoutButton.setOnClickListener(v -> {
            // Navigálás a checkout képernyőre
            Intent intent = new Intent(this, CheckoutActivity.class);
            startActivity(intent);
        });
        
        // Kosár tartalmának betöltése
        updateCartUI();
    }
    
    private void updateCartUI() {
        List<nje.gamf.speedyspoon.Models.MenuItem> cartItems = cartManager.getCartItems();
        
        if (cartItems.isEmpty()) {
            // Üres kosár üzenet megjelenítése, RecyclerView és fizetés gomb elrejtése
            emptyCartLayout.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.GONE);
        } else {
            // RecyclerView és fizetés gomb megjelenítése, üres kosár üzenet elrejtése
            emptyCartLayout.setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.VISIBLE);
            
            // Adapter beállítása a kosár tartalmával
            MenuItemAdapter adapter = new MenuItemAdapter(cartItems, true);
            cartRecyclerView.setAdapter(adapter);
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // A kosár tartalmának frissítése, amikor visszatérünk az activity-hez
        // Például ha a checkout képernyőről térünk vissza
        updateCartUI();
    }
} 