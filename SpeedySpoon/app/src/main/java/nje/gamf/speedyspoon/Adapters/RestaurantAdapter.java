package nje.gamf.speedyspoon.Adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import nje.gamf.speedyspoon.Models.Restaurant;
import nje.gamf.speedyspoon.R;
import nje.gamf.speedyspoon.RestaurantMenuActivity;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {
    private List<Restaurant> restaurants;
    private Random random;
    private static final String TAG = "RestaurantAdapter";
    private static final int[] RESTAURANT_IMAGES = {
            R.drawable.restaurant1,
            R.drawable.restaurant2,
            R.drawable.restaurant3,
            R.drawable.restaurant4,
            R.drawable.restaurant5,
            R.drawable.restaurant6
    };

    public RestaurantAdapter(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        this.random = new Random();
    }

    public void updateRestaurants(List<Restaurant> newRestaurants) {
        this.restaurants = newRestaurants;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        
        holder.restaurantName.setText(restaurant.getName());
        holder.restaurantCategory.setText(restaurant.getCuisineType());
        
        // Az értékeléseket állítja be
        double rating = restaurant.getRating();
        holder.restaurantRating.setRating((float) rating);
        holder.restaurantRatingCount.setText(String.format("(%.1f)", rating));
        
        // Alapértelmezett feliratok (Szállítási idő, minimum rendelés stb-stb)
        holder.restaurantDeliveryTime.setText("25-40 perc");
        int minOrder = restaurant.getMinimumOrder();
        holder.restaurantMinOrder.setText("Min. rendelés: " + minOrder + " Ft");
        holder.restaurantDeliveryFee.setText("Kiszállítás: 500 Ft");
        
        // Éttermi kép betöltése a model alapján
        try {
            String imageName = restaurant.getRestaurantImage();
            
            // Ha nincs kép megadva, használjunk random képet a beépített erőforrásokból
            if (imageName == null || imageName.isEmpty()) {
                int randomImageIndex = random.nextInt(RESTAURANT_IMAGES.length);
                int randomImageResource = RESTAURANT_IMAGES[randomImageIndex];
                
                Glide.with(holder.itemView.getContext())
                    .load(randomImageResource)
                    .centerCrop()
                    .into(holder.restaurantImage);
            } else {
                imageName = imageName.replace(".jpg", "");
                
                int imageResource = holder.itemView.getContext().getResources().getIdentifier(
                    imageName, "drawable", holder.itemView.getContext().getPackageName());
                
                if (imageResource != 0) {
                    Glide.with(holder.itemView.getContext())
                        .load(imageResource)
                        .centerCrop()
                        .into(holder.restaurantImage);
                } else {
                    // Ha nem található a kép, használjunk random képet
                    int randomImageIndex = random.nextInt(RESTAURANT_IMAGES.length);
                    int randomImageResource = RESTAURANT_IMAGES[randomImageIndex];
                    
                    Glide.with(holder.itemView.getContext())
                        .load(randomImageResource)
                        .centerCrop()
                        .into(holder.restaurantImage);
                }
            }
        } catch (Exception e) {
            // Hiba esetén fallback kép
            int randomImageIndex = random.nextInt(RESTAURANT_IMAGES.length);
            Glide.with(holder.itemView.getContext())
                .load(RESTAURANT_IMAGES[randomImageIndex])
                .centerCrop()
                .into(holder.restaurantImage);
        }

        // Click listener for the entire item
        holder.itemView.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(v.getContext(), RestaurantMenuActivity.class);
                
                // Ensure restaurant ID is not null
                String restaurantId = restaurant.getId();
                if (restaurantId == null || restaurantId.isEmpty()) {
                    restaurantId = "restaurant_" + (position + 1);
                    restaurant.setId(restaurantId);
                }
                
                intent.putExtra("restaurant", restaurant);
                intent.putExtra("restaurantId", restaurantId); // Add ID as separate extra for safety
                v.getContext().startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "Error starting RestaurantMenuActivity", e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        ImageView restaurantImage;
        TextView restaurantCategory;
        TextView restaurantOpen;
        TextView restaurantName;
        RatingBar restaurantRating;
        TextView restaurantRatingCount;
        TextView restaurantDeliveryTime;
        TextView restaurantMinOrder;
        TextView restaurantDeliveryFee;

        RestaurantViewHolder(View itemView) {
            super(itemView);
            restaurantImage = itemView.findViewById(R.id.restaurant_image);
            restaurantCategory = itemView.findViewById(R.id.restaurant_category);
            restaurantOpen = itemView.findViewById(R.id.restaurant_open);
            restaurantName = itemView.findViewById(R.id.restaurant_name);
            restaurantRating = itemView.findViewById(R.id.restaurant_rating);
            restaurantRatingCount = itemView.findViewById(R.id.restaurant_rating_count);
            restaurantDeliveryTime = itemView.findViewById(R.id.restaurant_delivery_time);
            restaurantMinOrder = itemView.findViewById(R.id.restaurant_min_order);
            restaurantDeliveryFee = itemView.findViewById(R.id.restaurant_delivery_fee);
        }
    }
} 