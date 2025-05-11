package nje.gamf.speedyspoon;

import android.app.Application;
import android.util.Log;

/**
 * Az alkalmazás Application osztálya.
 * Itt inicializáljuk a globális komponenseket, mint például a FavoritesManager.
 */
public class SpeedySpoonApp extends Application {
    private static final String TAG = "SpeedySpoonApp";

    @Override
    public void onCreate() {
        super.onCreate();
        
        // Inicializáljuk a kedvencek kezelőjét
        FavoritesManager.init(this);
        Log.d(TAG, "onCreate: FavoritesManager inicializálva");
    }
} 