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
} 