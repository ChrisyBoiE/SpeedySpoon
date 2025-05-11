package nje.gamf.speedyspoon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity a sikeres rendelés képernyőhöz.
 * Megjeleníti a sikeres rendelés üzenetét és lehetőséget biztosít
 * a visszatérésre a főképernyőre.
 */
public class OrderSuccessActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);
        
        // Visszatérés gomb beállítása
        Button returnButton = findViewById(R.id.return_to_main_button);
        returnButton.setOnClickListener(v -> {
            // Vissza a főképernyőre
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
} 