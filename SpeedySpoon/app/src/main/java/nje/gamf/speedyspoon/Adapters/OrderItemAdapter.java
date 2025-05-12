package nje.gamf.speedyspoon.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nje.gamf.speedyspoon.Models.Detail;
import nje.gamf.speedyspoon.Models.MenuItem;
import nje.gamf.speedyspoon.R;
import nje.gamf.speedyspoon.Repositories.MenuItemCallback;
import nje.gamf.speedyspoon.Repositories.MenuItemRepository;

// Adapter a rendelés tételek megjelenítéséhez
public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    private List<Detail> orderItems; // Rendelés tételek listája
    private Map<String, String> menuItemNames; // Étel nevek tárolása
    private MenuItemRepository menuItemRepository; // Étel adatok lekérdezése

    public OrderItemAdapter(List<Detail> orderItems) {
        this.orderItems = orderItems;
        this.menuItemNames = new HashMap<>();
        this.menuItemRepository = new MenuItemRepository();
        loadMenuItemNames();
    }

    // Étel nevek betöltése az adatbázisból
    private void loadMenuItemNames() {
        menuItemRepository.getAllMenuItems(new MenuItemCallback() {
            @Override
            public void onMenuItemsLoaded(List<MenuItem> menuItems) {
                for (MenuItem menuItem : menuItems) {
                    menuItemNames.put(menuItem.getId(), menuItem.getName());
                }
                notifyDataSetChanged();
            }

            @Override
            public void onError(com.google.firebase.database.DatabaseError error) {
                
            }
        });
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkout_summary, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        Detail item = orderItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return orderItems != null ? orderItems.size() : 0;
    }

    // Lista frissítése új tételekkel
    public void updateItems(List<Detail> newItems) {
        this.orderItems = newItems;
        notifyDataSetChanged();
    }

    // ViewHolder a rendelés tételek megjelenítéséhez
    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemName; // Étel neve
        private final TextView itemQuantityPrice; // Mennyiség és egységár
        private final TextView itemTotalPrice; // Teljes ár

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemQuantityPrice = itemView.findViewById(R.id.item_quantity_price);
            itemTotalPrice = itemView.findViewById(R.id.item_total_price);
        }

        // Tétel adatainak megjelenítése
        public void bind(Detail item) {
            String name = menuItemNames.get(item.getItemId());
            if (name != null) {
                itemName.setText(name);
            } else {
                itemName.setText("Termék " + item.getItemId());
            }
            itemQuantityPrice.setText(item.getQuantity() + " × " + item.getPrice() + " Ft");
            itemTotalPrice.setText((item.getQuantity() * item.getPrice()) + " Ft");
        }
    }
} 