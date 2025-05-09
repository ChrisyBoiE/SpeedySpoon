package nje.gamf.speedyspoon;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import nje.gamf.speedyspoon.Adapters.MenuItemAdapter;
import nje.gamf.speedyspoon.Models.MenuItem;
import nje.gamf.speedyspoon.Repositories.MenuItemCallback;
import nje.gamf.speedyspoon.Repositories.MenuItemRepository;

public class MainActivity extends AppCompatActivity {

    private MenuItemRepository menuItemRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Testing menu item repo
        menuItemRepository = new MenuItemRepository();

        menuItemRepository.fetchMenuItems(new MenuItemCallback() {
            @Override
            public void onMenuItemsLoaded(List<MenuItem> menuItems) {
                RecyclerView recyclerView = findViewById(R.id.recommended_recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                MenuItemAdapter adapter = new MenuItemAdapter(menuItems);
                recyclerView.setAdapter(adapter);
                Log.d("testing", "Menu items loaded: " + menuItems.size());
            }

            @Override
            public void onError(DatabaseError error) {
                Log.w("testing", "Failed to read menu items", error.toException());
            }
        });

    }
}