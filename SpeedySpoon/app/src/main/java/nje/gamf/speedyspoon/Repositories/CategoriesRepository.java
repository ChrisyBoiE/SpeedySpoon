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

import nje.gamf.speedyspoon.Models.Category;

public class CategoriesRepository {
    private DatabaseReference categoriesRef;

    public CategoriesRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        categoriesRef = database.getReference("categories");
    }

    public void fetchCategories(final CategoriesCallback callback) {
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Category> categories = new ArrayList<>();
                for (DataSnapshot menuItemSnapshot : dataSnapshot.getChildren()) {
                    Category category = menuItemSnapshot.getValue(Category.class);
                    if (category != null) {
                        categories.add(category);
                    }
                }
                Log.d("testing", "Categories fetched: " + categories.size());
                callback.onCategoriesLoaded(categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }
}
