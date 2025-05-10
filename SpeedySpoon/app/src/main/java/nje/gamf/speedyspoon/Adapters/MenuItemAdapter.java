package nje.gamf.speedyspoon.Adapters;

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
import java.util.List;
import java.util.Map;

import nje.gamf.speedyspoon.CartManager;
import nje.gamf.speedyspoon.Models.MenuItem;
import nje.gamf.speedyspoon.R;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder> {

    private List<MenuItem> menuItems;
    private boolean isInCartView; // Flag to determine if we're in the cart view
    private CartManager cartManager;
    
    public MenuItemAdapter(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
        this.isInCartView = false;
        this.cartManager = CartManager.getInstance();
    }
    
    // Constructor for cart view
    public MenuItemAdapter(List<MenuItem> menuItems, boolean isInCartView) {
        this.menuItems = menuItems;
        this.isInCartView = isInCartView;
        this.cartManager = CartManager.getInstance();
    }
    
    public void updateMenuItems(List<MenuItem> newMenuItems) {
        this.menuItems = newMenuItems;
        notifyDataSetChanged();
    }
    
    // Method to add item to cart
    public void addToCart(MenuItem item) {
        cartManager.addToCart(item);
        notifyDataSetChanged();
    }
    
    // Method to remove item from cart
    public void removeFromCart(String itemId) {
        cartManager.removeFromCart(itemId);
        notifyDataSetChanged();
    }
    
    // Method to update quantity
    public void updateQuantity(String itemId, int newQuantity) {
        cartManager.updateQuantity(itemId, newQuantity);
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
        String itemId = currentItem.getId();
        int quantity = cartManager.getQuantity(itemId);
        boolean isInCart = quantity > 0;
        
        // Cart-specific elements
        if (isInCart || isInCartView) {
            // Show quantity layout and remove button, hide add to cart button
            holder.quantityLayout.setVisibility(View.VISIBLE);
            holder.removeButton.setVisibility(View.VISIBLE);
            holder.addToCartButton.setVisibility(View.GONE);
            
            // Set the current quantity
            holder.quantityTextView.setText(String.valueOf(quantity));
            
            // Set click listeners for quantity adjustment
            holder.decreaseButton.setOnClickListener(v -> {
                int currentQuantity = cartManager.getQuantity(itemId);
                if (currentQuantity > 1) {
                    updateQuantity(itemId, currentQuantity - 1);
                } else {
                    // If quantity would become 0, remove the item
                    removeFromCart(itemId);
                    // Hide quantity layout, show add to cart button
                    holder.quantityLayout.setVisibility(View.GONE);
                    holder.removeButton.setVisibility(View.GONE);
                    holder.addToCartButton.setVisibility(View.VISIBLE);
                }
            });
            
            holder.increaseButton.setOnClickListener(v -> {
                int currentQuantity = cartManager.getQuantity(itemId);
                updateQuantity(itemId, currentQuantity + 1);
            });
            
            holder.removeButton.setOnClickListener(v -> {
                removeFromCart(itemId);
                // Hide quantity layout, show add to cart button if not in cart view
                if (!isInCartView) {
                    holder.quantityLayout.setVisibility(View.GONE);
                    holder.removeButton.setVisibility(View.GONE);
                    holder.addToCartButton.setVisibility(View.VISIBLE);
                }
                Toast.makeText(v.getContext(), "Eltávolítva a kosárból", Toast.LENGTH_SHORT).show();
            });
        } else {
            // Hide quantity layout and remove button, show add to cart button
            holder.quantityLayout.setVisibility(View.GONE);
            holder.removeButton.setVisibility(View.GONE);
            holder.addToCartButton.setVisibility(View.VISIBLE);
            
            // Set click listener for add to cart button
            holder.addToCartButton.setOnClickListener(v -> {
                addToCart(currentItem);
                // Update UI immediately
                holder.quantityLayout.setVisibility(View.VISIBLE);
                holder.removeButton.setVisibility(View.VISIBLE);
                holder.addToCartButton.setVisibility(View.GONE);
                holder.quantityTextView.setText("1");
                Toast.makeText(v.getContext(), "Hozzáadva a kosárhoz", Toast.LENGTH_SHORT).show();
            });
        }
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
        public Button removeButton;
        public Button addToCartButton;

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
            removeButton = itemView.findViewById(R.id.remove_button);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
        }
    }
}
