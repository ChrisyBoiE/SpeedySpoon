package nje.gamf.speedyspoon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity a sikeres rendelés képernyőhöz.
 * Megjeleníti a sikeres rendelés üzenetét és lehetőséget biztosít
 * a visszatérésre a főképernyőre.
 */
public class OrderSuccessActivity extends AppCompatActivity {
    
    private static final String TAG = "OrderSuccessActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);
        
        // Visszatérés gomb beállítása
        Button returnButton = findViewById(R.id.return_to_main_button);
        returnButton.setOnClickListener(v -> {
            Log.d(TAG, "Return button clicked, navigating to MainActivity home screen");
            navigateToHomeScreen();
        });
    }
    
    /**
     * Navigates to the MainActivity and triggers showing the home screen
     */
    private void navigateToHomeScreen() {
        // Create intent to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        
        // Add a flag to indicate that we want to show the home screen
        intent.putExtra("SHOW_HOME_SCREEN", true);
        
        // Clear any previous activities from the stack
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        
        Log.d(TAG, "Starting MainActivity with SHOW_HOME_SCREEN flag");
        startActivity(intent);
        finish();
    }
    
    @Override
    public void onBackPressed() {
        Log.d(TAG, "Back button pressed, navigating to MainActivity home screen");
        navigateToHomeScreen();
    }
} 