package nje.gamf.speedyspoon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import nje.gamf.speedyspoon.Adapters.CheckoutItemAdapter;
import nje.gamf.speedyspoon.Models.Detail;
import nje.gamf.speedyspoon.Models.MenuItem;
import nje.gamf.speedyspoon.Models.Order;
import nje.gamf.speedyspoon.Models.OrderDetail;
import nje.gamf.speedyspoon.Models.User;

public class CheckoutActivity extends AppCompatActivity {
    private static final String TAG = "CheckoutActivity";
    private static final int DELIVERY_FEE = 500; // Fix kiszállítási díj: 500 Ft
    
    private RecyclerView orderItemsRecyclerView;
    private CheckoutItemAdapter adapter;
    private CartManager cartManager;
    
    private TextView restaurantNameTextView;
    private TextView subtotalTextView;
    private TextView deliveryFeeTextView;
    private TextView totalTextView;
    private Button placeOrderButton;
    
    private TextView addressNameTextView;
    private TextView addressDetailsTextView;
    private Button changeAddressButton;
    
    private CardView addressCard;
    private RadioGroup paymentRadioGroup;
    private RadioButton cashRadioButton;
    private RadioButton cardRadioButton;

    // Firebase references
    private DatabaseReference orderRef;
    private DatabaseReference orderDetailRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        
        // Inicializálás
        cartManager = CartManager.getInstance();
        
        // Firebase inicializálás
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        orderRef = database.getReference("orders");
        orderDetailRef = database.getReference("orderDetails");
        
        // UI elemek inicializálása
        setupUI();
        
        // Kosár tartalmának megjelenítése
        displayCartItems();
        
        // Összegek kiszámítása és megjelenítése
        calculateAndDisplayTotals();
        
        // Rendelés gomb beállítása
        setupPlaceOrderButton();
    }
    
    private void setupUI() {
        // Toolbar beállítása
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.checkout);
        
        // RecyclerView beállítása
        orderItemsRecyclerView = findViewById(R.id.order_items_recycler);
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Textview-k beállítása
        restaurantNameTextView = findViewById(R.id.restaurant_name);
        subtotalTextView = findViewById(R.id.subtotal_text);
        deliveryFeeTextView = findViewById(R.id.delivery_fee_text);
        totalTextView = findViewById(R.id.total_text);
        
        // Cím beállítása
        addressNameTextView = findViewById(R.id.address_name);
        addressDetailsTextView = findViewById(R.id.address_details);
        changeAddressButton = findViewById(R.id.change_address_button);
        addressCard = findViewById(R.id.address_card);
        
        // Fizetési mód beállítása
        paymentRadioGroup = findViewById(R.id.payment_radio_group);
        cashRadioButton = findViewById(R.id.cash_radio);
        cardRadioButton = findViewById(R.id.card_radio);
        
        // Rendelés gomb
        placeOrderButton = findViewById(R.id.place_order_button);
        
        // Alapértelmezett cím beállítása (valós app-ban ez a felhasználó címeiből jönne)
        addressNameTextView.setText("Otthon");
        addressDetailsTextView.setText("1234 Budapest, Példa utca 123.");
        
        // Cím változtatás kezelése
        changeAddressButton.setOnClickListener(v -> {
            Toast.makeText(this, "Cím változtatás funkció még fejlesztés alatt", Toast.LENGTH_SHORT).show();
            // TODO: Implementálni a cím kiválasztást
        });
    }
    
    private void displayCartItems() {
        // Kosár elemeinek lekérése
        List<MenuItem> cartItems = cartManager.getCartItems();
        
        // Ha üres a kosár, visszatérünk a főképernyőre
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "A kosár üres", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Étterem nevének megjelenítése (feltételezve, hogy minden tétel ugyanabból az étteremből van)
        if (!cartItems.isEmpty()) {
            String restaurantId = cartItems.get(0).getRestaurantId();
            // Itt normál esetben lekérnénk az étterem nevét egy repository-ból
            // Most egyszerűen csak megjelenítünk egy alapértelmezett nevet
            restaurantNameTextView.setText("Étterem");
        }
        
        // Adapter beállítása
        adapter = new CheckoutItemAdapter(cartItems);
        orderItemsRecyclerView.setAdapter(adapter);
    }
    
    private void calculateAndDisplayTotals() {
        // Részösszeg kiszámítása
        int subtotal = cartManager.calculateSubtotal();
        
        // Szállítási díj (fix)
        int deliveryFee = DELIVERY_FEE;
        
        // Végösszeg
        int total = subtotal + deliveryFee;
        
        // Adatok megjelenítése
        subtotalTextView.setText(formatPrice(subtotal));
        deliveryFeeTextView.setText(formatPrice(deliveryFee));
        totalTextView.setText(formatPrice(total));
        
        Log.d(TAG, "calculateAndDisplayTotals: Részösszeg: " + subtotal + ", Szállítási díj: " + 
              deliveryFee + ", Végösszeg: " + total);
    }
    
    /**
     * Ár formázása megfelelő formátumra
     */
    private String formatPrice(int price) {
        return price + " Ft";
    }
    
    private void setupPlaceOrderButton() {
        placeOrderButton.setOnClickListener(v -> {
            // Rendelés adatainak ellenőrzése
            if (validateOrderData()) {
                // Fizetési mód lekérése
                String paymentMethod = cashRadioButton.isChecked() ? "Készpénz" : "Bankkártya";
                
                // Rendelés feldolgozása
                processOrder(paymentMethod);
            }
        });
    }
    
    private boolean validateOrderData() {
        // Itt ellenőrizhetnénk a rendelés adatait
        // Például: van-e megadva cím, megfelelő-e a fizetési mód, stb.
        return true; // Most egyszerűen mindig igazzal térünk vissza
    }
    
    private void processOrder(String paymentMethod) {
        // Get the current user
        User currentUser = MainActivity.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Hiba: Nincs bejelentkezett felhasználó!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Get the cart items
        List<MenuItem> cartItems = cartManager.getCartItems();
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "A kosár üres!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            // Generate a random 6-digit order ID
            String orderId = generateOrderId();
            
            // Get current date in proper format
            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            
            // Get the restaurant ID from the first item (assuming all items are from the same restaurant)
            String restaurantId = cartItems.get(0).getRestaurantId();
            
            // Create order details
            List<Detail> details = new ArrayList<>();
            for (MenuItem item : cartItems) {
                int quantity = cartManager.getQuantity(item.getId());
                Detail detail = new Detail(
                        item.getId(),
                        orderId,
                        item.getPrice(),
                        quantity,
                        "" // No special instructions for now
                );
                details.add(detail);
            }
            
            // Create OrderDetail object
            OrderDetail orderDetail = new OrderDetail(details);
            
            // Calculate total amount
            int subtotal = cartManager.calculateSubtotal();
            int totalAmount = subtotal + DELIVERY_FEE;
            
            // Create Order object
            Order order = new Order(
                    currentDate,
                    "Feldolgozás alatt", // Initial status
                    totalAmount,
                    orderDetail,
                    currentUser.getEmail(), 
                    restaurantId, 
                    orderId       
            );
            
            // Set the order ID
            order.setId(orderId);
            
            // Save order to Firebase
            orderRef.child(orderId).setValue(order)
                    .addOnSuccessListener(aVoid -> {
                        // Order saved successfully
                        Log.d(TAG, "Order saved successfully with ID: " + orderId);
                        
                        // Clear the cart
                        cartManager.clearCart();
                        
                        // Show success message
                        Toast.makeText(CheckoutActivity.this, "Rendelés sikeresen leadva!", Toast.LENGTH_SHORT).show();
                        
                        // Navigate to OrderSuccessActivity to show the success screen
                        Intent intent = new Intent(CheckoutActivity.this, OrderSuccessActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        // Order failed to save
                        Log.e(TAG, "Error saving order: " + e.getMessage());
                        Toast.makeText(CheckoutActivity.this, "Hiba történt a rendelés mentése során: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            
        } catch (Exception e) {
            Log.e(TAG, "Error processing order: " + e.getMessage());
            Toast.makeText(this, "Hiba történt a rendelés feldolgozása során: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Generates a random 6-digit order ID
     * @return A string containing a 6-digit number
     */
    private String generateOrderId() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000); // This generates a 6-digit number
        return String.valueOf(number);
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}