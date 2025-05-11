package nje.gamf.speedyspoon.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nje.gamf.speedyspoon.CartManager;
import nje.gamf.speedyspoon.FavoritesManager;
import nje.gamf.speedyspoon.Models.MenuItem;
import nje.gamf.speedyspoon.R;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder> {
    private static final String TAG = "MenuItemAdapter";
    private List<MenuItem> menuItems;
    private boolean isInCartView; // Flag to determine if we're in the cart view
    private CartManager cartManager;
    private FavoritesManager favoritesManager;
    
    public MenuItemAdapter(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
        this.isInCartView = false;
        this.cartManager = CartManager.getInstance();
        this.favoritesManager = FavoritesManager.getInstance();
        Log.d(TAG, "Constructor: " + menuItems.size() + " menüelem, kosár nézet? " + isInCartView);
    }
    
    // Constructor for cart view
    public MenuItemAdapter(List<MenuItem> menuItems, boolean isInCartView) {
        this.menuItems = menuItems;
        this.isInCartView = isInCartView;
        this.cartManager = CartManager.getInstance();
        this.favoritesManager = FavoritesManager.getInstance();
        Log.d(TAG, "Constructor: " + menuItems.size() + " menüelem, kosár nézet? " + isInCartView);
    }
    
    // Constructor for cart view with favorite IDs
    public MenuItemAdapter(List<MenuItem> menuItems, boolean isInCartView, Set<String> favoriteIds) {
        this.menuItems = menuItems;
        this.isInCartView = isInCartView;
        this.cartManager = CartManager.getInstance();
        this.favoritesManager = FavoritesManager.getInstance();
        // Ha van átadott kedvenc ID lista, akkor frissítjük a FavoritesManager-t
        if (favoriteIds != null) {
            for (String id : favoriteIds) {
                this.favoritesManager.addFavorite(id);
            }
            Log.d(TAG, "Constructor (favoriteIds): " + menuItems.size() + " menüelem, kosár nézet? " + isInCartView 
                + ", kedvencek: " + favoriteIds.size());
        }
    }
    
    public void updateMenuItems(List<MenuItem> newMenuItems) {
        this.menuItems = newMenuItems;
        Log.d(TAG, "updateMenuItems: " + newMenuItems.size() + " menüelemre frissítve");
        notifyDataSetChanged();
    }
    
    // Method to add item to cart
    public void addToCart(MenuItem item) {
        cartManager.addToCart(item);
        Log.d(TAG, "addToCart: " + item.getId() + " - " + item.getName());
        notifyDataSetChanged();
    }
    
    // Method to remove item from cart
    public void removeFromCart(String itemId) {
        cartManager.removeFromCart(itemId);
        Log.d(TAG, "removeFromCart: " + itemId);
        notifyDataSetChanged();
    }
    
    // Method to update quantity
    public void updateQuantity(String itemId, int newQuantity) {
        cartManager.updateQuantity(itemId, newQuantity);
        Log.d(TAG, "updateQuantity: " + itemId + ", mennyiség: " + newQuantity);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_food_item, parent, false);
        return new MenuItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        MenuItem currentItem = menuItems.get(position);
        String itemId = currentItem.getId();
        
        Log.d(TAG, "onBindViewHolder: Menüelem bekötése: " + itemId + " - " + currentItem.getName());
        
        holder.nameTextView.setText(currentItem.getName());
        holder.descriptionTextView.setText(currentItem.getDescription());
        holder.priceTextView.setText(currentItem.getPrice() + " Ft");
        
        // Load image if available
        if (currentItem.getImage() != null && !currentItem.getImage().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                .load(currentItem.getImage())
                .centerCrop()
                .into(holder.imageView);
        } else {
            // Set a placeholder image
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
        }
        
        // Handle cart-related UI elements
        int quantity = cartManager.getQuantity(itemId);
        boolean isInCart = quantity > 0;
        
        // Cart-specific elements
        if (isInCart || isInCartView) {
            holder.quantityLayout.setVisibility(View.VISIBLE);
            holder.cartActionButton.setText("Eltávolítás");
            holder.cartActionButton.setVisibility(View.VISIBLE);
            // Mennyiség beállítása
            holder.quantityTextView.setText(String.valueOf(quantity));

            holder.decreaseButton.setOnClickListener(v -> {
                int currentQuantity = cartManager.getQuantity(itemId);
                if (currentQuantity > 1) {
                    updateQuantity(itemId, currentQuantity - 1);
                } else {
                    removeFromCart(itemId);
                }
            });
            holder.increaseButton.setOnClickListener(v -> {
                int currentQuantity = cartManager.getQuantity(itemId);
                updateQuantity(itemId, currentQuantity + 1);
            });
            holder.cartActionButton.setOnClickListener(v -> {
                removeFromCart(itemId);
                Toast.makeText(v.getContext(), "Eltávolítva a kosárból", Toast.LENGTH_SHORT).show();
            });
        } else {
            holder.quantityLayout.setVisibility(View.GONE);
            holder.cartActionButton.setText("Kosárba");
            holder.cartActionButton.setVisibility(View.VISIBLE);
            holder.cartActionButton.setOnClickListener(v -> {
                addToCart(currentItem);
                Toast.makeText(v.getContext(), "Hozzáadva a kosárhoz", Toast.LENGTH_SHORT).show();
            });
        }

        // Kedvenc ikon beállítása
        boolean isFavorite = favoritesManager.isFavorite(itemId);
        holder.favoriteButton.setImageResource(isFavorite ? R.drawable.ic_favorite_red : R.drawable.ic_favorite_gray);
        Log.d(TAG, "onBindViewHolder: Kedvenc állapot: " + itemId + " - " + currentItem.getName() + ", kedvenc: " + isFavorite);
        
        holder.favoriteButton.setOnClickListener(v -> {
            if (favoritesManager.isFavorite(itemId)) {
                // Kedvenc eltávolítása
                favoritesManager.removeFavorite(itemId);
                holder.favoriteButton.setImageResource(R.drawable.ic_favorite_gray);
                Log.d(TAG, "onFavoriteClick: Kedvenc eltávolítva: " + itemId + " - " + currentItem.getName());
                Toast.makeText(v.getContext(), "Eltávolítva a kedvencekből", Toast.LENGTH_SHORT).show();
            } else {
                // Kedvenc hozzáadása
                favoritesManager.addFavorite(itemId);
                holder.favoriteButton.setImageResource(R.drawable.ic_favorite_red);
                Log.d(TAG, "onFavoriteClick: Kedvenc hozzáadva: " + itemId + " - " + currentItem.getName());
                Toast.makeText(v.getContext(), "Hozzáadva a kedvencekhez", Toast.LENGTH_SHORT).show();
            }
            
            // Ha van listener, értesítheti a kedvencek változásáról
            if (onFavoriteChangedListener != null) {
                Set<String> newFavorites = favoritesManager.getFavoriteIds();
                Log.d(TAG, "onFavoriteClick: Kedvencek listája frissült, értesítés küldése a listener-nek. Kedvencek: " + newFavorites);
                onFavoriteChangedListener.onFavoriteChanged(newFavorites);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView descriptionTextView;
        public TextView priceTextView;
        public ImageView imageView;
        public LinearLayout quantityLayout;
        public TextView quantityTextView;
        public ImageButton decreaseButton;
        public ImageButton increaseButton;
        public Button cartActionButton;
        public ImageButton favoriteButton;

        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.item_name);
            descriptionTextView = itemView.findViewById(R.id.item_description);
            priceTextView = itemView.findViewById(R.id.item_price);
            imageView = itemView.findViewById(R.id.item_image);
            
            // Find cart-related views
            quantityLayout = itemView.findViewById(R.id.quantity_layout);
            quantityTextView = itemView.findViewById(R.id.item_quantity);
            decreaseButton = itemView.findViewById(R.id.decrease_button);
            increaseButton = itemView.findViewById(R.id.increase_button);
            cartActionButton = itemView.findViewById(R.id.cart_action_button);
            favoriteButton = itemView.findViewById(R.id.favorite_button);
        }
    }

    // Listener interfész a kedvencek változásához
    public interface OnFavoriteChangedListener {
        void onFavoriteChanged(Set<String> newFavorites);
    }
    private OnFavoriteChangedListener onFavoriteChangedListener;
    public void setOnFavoriteChangedListener(OnFavoriteChangedListener listener) {
        this.onFavoriteChangedListener = listener;
        Log.d(TAG, "setOnFavoriteChangedListener: Kedvenc változás listener beállítva");
    }

    public Set<String> getFavoriteIds() {
        Set<String> favorites = favoritesManager.getFavoriteIds();
        Log.d(TAG, "getFavoriteIds: Kedvencek lekérve, kedvencek száma: " + favorites.size() + ", kedvencek: " + favorites);
        return favorites;
    }
}
