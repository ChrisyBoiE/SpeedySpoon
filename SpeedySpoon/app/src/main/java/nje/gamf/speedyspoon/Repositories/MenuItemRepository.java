package nje.gamf.speedyspoon.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import nje.gamf.speedyspoon.Models.MenuItem;

public class MenuItemRepository {
    private static final String TAG = "MenuItemRepository";
    private DatabaseReference menuItemsRef;

    public MenuItemRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        menuItemsRef = database.getReference("menuItems");
    }

    public void fetchMenuItems(final MenuItemCallback callback) {
        Log.d(TAG, "fetchMenuItems: Menüelemek lekérése kezdődött");
        menuItemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MenuItem> menuItemsList = new ArrayList<>();
                for (DataSnapshot menuItemSnapshot : dataSnapshot.getChildren()) {
                    MenuItem menuItem = menuItemSnapshot.getValue(MenuItem.class);
                    String itemId = menuItemSnapshot.getKey();
                    
                    if (menuItem != null) {
                        // Fontos: az ID-t be kell állítani, különben null marad
                        menuItem.setId(itemId);
                        menuItemsList.add(menuItem);
                        Log.d(TAG, "fetchMenuItems: Menüelem feldolgozva: " + itemId + " - " + menuItem.getName());
                    }
                }
                Log.d(TAG, "fetchMenuItems: Menüelemek betöltve: " + menuItemsList.size() + " elem");
                callback.onMenuItemsLoaded(menuItemsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "fetchMenuItems: Hiba történt: " + databaseError.getMessage());
                callback.onError(databaseError);
            }
        });
    }
    
    // Alias for fetchMenuItems for better naming consistency
    public void getAllMenuItems(final MenuItemCallback callback) {
        Log.d(TAG, "getAllMenuItems: Menüelemek lekérése delegálva a fetchMenuItems metódushoz");
        fetchMenuItems(callback);
    }
}
