package nje.gamf.speedyspoon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nje.gamf.speedyspoon.Models.MenuItem;

public class CartManager {
    private static CartManager instance;
    private final Map<String, Integer> cartItems; // itemId -> quantity
    private final Map<String, MenuItem> itemDetails; // itemId -> MenuItem

    private CartManager() {
        cartItems = new HashMap<>();
        itemDetails = new HashMap<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(MenuItem item) {
        String id = item.getId();
        int current = cartItems.getOrDefault(id, 0);
        cartItems.put(id, current + 1);
        itemDetails.put(id, item);
    }

    public void removeFromCart(String itemId) {
        cartItems.remove(itemId);
        itemDetails.remove(itemId);
    }

    public void updateQuantity(String itemId, int quantity) {
        if (quantity <= 0) {
            removeFromCart(itemId);
        } else {
            cartItems.put(itemId, quantity);
        }
    }

    public int getQuantity(String itemId) {
        return cartItems.getOrDefault(itemId, 0);
    }

    public List<MenuItem> getCartItems() {
        List<MenuItem> items = new ArrayList<>();
        for (String id : cartItems.keySet()) {
            MenuItem item = itemDetails.get(id);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    public Map<String, Integer> getCartMap() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
        itemDetails.clear();
    }
    
    /**
     * Kiszámolja a kosár tartalmának részösszegét (szállítási díj nélkül)
     * @return a részösszeg forintban
     */
    public int calculateSubtotal() {
        int subtotal = 0;
        for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
            String itemId = entry.getKey();
            int quantity = entry.getValue();
            MenuItem item = itemDetails.get(itemId);
            if (item != null) {
                subtotal += item.getPrice() * quantity;
            }
        }
        return subtotal;
    }
    
    /**
     * Visszaadja a kosárban lévő elemek számát
     * @return az elemek száma
     */
    public int getItemCount() {
        int count = 0;
        for (int quantity : cartItems.values()) {
            count += quantity;
        }
        return count;
    }
    
    /**
     * Ellenőrzi, hogy a kosár üres-e
     * @return true, ha a kosár üres
     */
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
} 