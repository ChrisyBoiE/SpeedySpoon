package nje.gamf.speedyspoon.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nje.gamf.speedyspoon.Models.MenuItem;
import nje.gamf.speedyspoon.R;

// In Java
public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder> {

    private List<MenuItem> menuItems;
    public MenuItemAdapter(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item, parent, false);
        return new MenuItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        MenuItem currentItem = menuItems.get(position);
        holder.nameTextView.setText(currentItem.getName());
        holder.descriptionTextView.setText(currentItem.getDescription());
        holder.priceTextView.setText(currentItem.getPrice() + " Ft");
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView descriptionTextView;
        public TextView priceTextView;

        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewMenuItemName);
            descriptionTextView = itemView.findViewById(R.id.textViewMenuItemDescription);
            priceTextView = itemView.findViewById(R.id.textViewMenuItemPrice);
        }
    }
}
