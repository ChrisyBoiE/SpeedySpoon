package nje.gamf.speedyspoon.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nje.gamf.speedyspoon.CartManager;
import nje.gamf.speedyspoon.Models.MenuItem;
import nje.gamf.speedyspoon.R;

/**
 * Adapter a checkout képernyőn megjelenő kosár tartalmának megjelenítéséhez.
 * Ez egy egyszerűbb verzió mint a MenuItemAdapter, csak az étel nevét, 
 * mennyiségét és az árat mutatja.
 */
public class CheckoutItemAdapter extends RecyclerView.Adapter<CheckoutItemAdapter.CheckoutItemViewHolder> {
    private List<MenuItem> menuItems;
    private CartManager cartManager;
    
    public CheckoutItemAdapter(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
        this.cartManager = CartManager.getInstance();
    }
    
    @NonNull
    @Override
    public CheckoutItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkout_summary, parent, false);
        return new CheckoutItemViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CheckoutItemViewHolder holder, int position) {
        MenuItem item = menuItems.get(position);
        int quantity = cartManager.getQuantity(item.getId());
        
        // Étel neve
        holder.nameTextView.setText(item.getName());
        
        // Mennyiség × ár formátum
        String quantityPrice = quantity + " × " + item.getPrice() + " Ft";
        holder.quantityPriceTextView.setText(quantityPrice);
        
        // Teljes ár (mennyiség × darabár)
        int totalPrice = quantity * item.getPrice();
        holder.totalPriceTextView.setText(totalPrice + " Ft");
    }
    
    @Override
    public int getItemCount() {
        return menuItems.size();
    }
    
    /**
     * ViewHolder a checkout elemekhez
     */
    static class CheckoutItemViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView quantityPriceTextView;
        TextView totalPriceTextView;
        
        public CheckoutItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.item_name);
            quantityPriceTextView = itemView.findViewById(R.id.item_quantity_price);
            totalPriceTextView = itemView.findViewById(R.id.item_total_price);
        }
    }
} 