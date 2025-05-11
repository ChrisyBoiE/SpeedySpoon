package nje.gamf.speedyspoon.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nje.gamf.speedyspoon.Models.Order;
import nje.gamf.speedyspoon.R;

/**
 * Adapter a rendelések megjelenítéséhez a RecyclerView-ban
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orders;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    public void updateOrders(List<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        try {
            if (orders == null || orders.isEmpty()) {
                return;
            }

            Order order = orders.get(position);

            // Rendelés számának beállítása
            holder.orderNumber.setText("Rendelés #" + (position + 1));
            
            // Rendelés státuszának beállítása
            String status = order.getStatus();
            if (status != null && !status.isEmpty()) {
                holder.orderStatus.setText(status);
            } else {
                holder.orderStatus.setText("Ismeretlen");
            }
            
            // Éttermi név beállítása (egyelőre statikus)
            holder.restaurantName.setText("Olasz Konyha");
            
            // Rendelés dátumának beállítása
            String date = order.getOrderDate();
            if (date != null && !date.isEmpty()) {
                holder.orderDate.setText("Dátum: " + date);
            } else {
                holder.orderDate.setText("Dátum: Ismeretlen");
            }
            
            // Rendelés tételek számának beállítása
            int itemCount = 0;
            if (order.getOrderDetails() != null && order.getOrderDetails().getOrderDetails() != null) {
                itemCount = order.getOrderDetails().getOrderDetails().size();
            }
            holder.orderItems.setText(itemCount + " tétel");
            
            // Rendelés összegének beállítása
            Integer totalAmount = order.getTotalAmount();
            if (totalAmount != null) {
                holder.orderTotal.setText("Összesen: " + totalAmount + " Ft");
            } else {
                holder.orderTotal.setText("Összesen: 0 Ft");
            }
            
            // Újrarendelés gomb kezelése
            holder.orderAgainButton.setOnClickListener(v -> {
                // Megvalósítani, ha marad idő: Implement újrarendelés funkció
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumber;
        TextView orderStatus;
        TextView restaurantName;
        TextView orderDate;
        TextView orderItems;
        TextView orderTotal;
        Button orderAgainButton;

        OrderViewHolder(View itemView) {
            super(itemView);
            try {
                orderNumber = itemView.findViewById(R.id.order_number);
                orderStatus = itemView.findViewById(R.id.order_status);
                restaurantName = itemView.findViewById(R.id.restaurant_name);
                orderDate = itemView.findViewById(R.id.order_date);
                orderItems = itemView.findViewById(R.id.order_items);
                orderTotal = itemView.findViewById(R.id.order_total);
                orderAgainButton = itemView.findViewById(R.id.order_again_button);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
} 