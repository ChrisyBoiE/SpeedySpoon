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
    private DatabaseReference menuItemsRef;

    public MenuItemRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        menuItemsRef = database.getReference("menuItems");
    }

    public void fetchMenuItems(final MenuItemCallback callback) {
        menuItemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MenuItem> menuItemsList = new ArrayList<>();
                for (DataSnapshot menuItemSnapshot : dataSnapshot.getChildren()) {
                    MenuItem menuItem = menuItemSnapshot.getValue(MenuItem.class);
                    if (menuItem != null) {
                        menuItemsList.add(menuItem);
                    }
                }
                Log.d("testing", "Menu items fetched: " + menuItemsList.size());
                callback.onMenuItemsLoaded(menuItemsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }
}
