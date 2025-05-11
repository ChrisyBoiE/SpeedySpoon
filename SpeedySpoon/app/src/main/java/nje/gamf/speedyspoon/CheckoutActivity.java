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

import java.util.ArrayList;
import java.util.List;

import nje.gamf.speedyspoon.Adapters.CheckoutItemAdapter;
import nje.gamf.speedyspoon.Models.MenuItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        
        // Inicializálás
        cartManager = CartManager.getInstance();
        
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
        // Rendelés feldolgozása
        // Valós alkalmazásban itt küldenénk el a rendelés adatait a szerverre
        
        Toast.makeText(this, "Rendelés feldolgozása... Fizetési mód: " + paymentMethod, Toast.LENGTH_SHORT).show();
        
        // Kosár kiürítése
        cartManager.clearCart();
        
        // Átirányítás a sikeres rendelés képernyőre
        Intent intent = new Intent(this, OrderSuccessActivity.class);
        startActivity(intent);
        finish();
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