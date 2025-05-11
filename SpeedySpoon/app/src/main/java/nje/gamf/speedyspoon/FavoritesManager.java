package nje.gamf.speedyspoon;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Singleton osztály a kedvencek tárolására.
 * A kedvencek a SharedPreferences-ben tárolódnak, így az alkalmazás újraindításakor is megmaradnak.
 */
public class FavoritesManager {
    private static final String TAG = "FavoritesManager";
    private static final String PREFS_NAME = "SpeedySpoonPrefs";
    private static final String KEY_FAVORITES = "favorite_ids";
    
    private static FavoritesManager instance;
    private final Set<String> favoriteIds = new HashSet<>();
    private Context appContext;
    
    private FavoritesManager(Context context) {
        // Privát konstruktor a singleton mintához
        this.appContext = context.getApplicationContext();
        loadFavorites();
    }
    
    public static synchronized FavoritesManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("FavoritesManager nincs inicializálva! Hívd meg az init() metódust az Application osztályban!");
        }
        return instance;
    }
    
    /**
     * Inicializálja a FavoritesManager-t. Ezt a metódust az Application onCreate() metódusában kell meghívni.
     */
    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new FavoritesManager(context);
        }
    }
    
    /**
     * Betölti a kedvenceket a SharedPreferences-ből.
     */
    private void loadFavorites() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> savedIds = prefs.getStringSet(KEY_FAVORITES, new HashSet<>());
        favoriteIds.clear();
        favoriteIds.addAll(savedIds);
        Log.d(TAG, "loadFavorites: Betöltött kedvencek száma: " + favoriteIds.size() + ", kedvencek: " + favoriteIds);
    }
    
    /**
     * Elmenti a kedvenceket a SharedPreferences-be.
     */
    private void saveFavorites() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(KEY_FAVORITES, new HashSet<>(favoriteIds));
        editor.apply();
        Log.d(TAG, "saveFavorites: Elmentett kedvencek száma: " + favoriteIds.size() + ", kedvencek: " + favoriteIds);
    }
    
    /**
     * Hozzáad egy kedvenc ételt az ID alapján.
     * @param itemId Az étel azonosítója
     * @return true, ha hozzáadásra került, false ha már kedvenc volt
     */
    public boolean addFavorite(String itemId) {
        boolean result = favoriteIds.add(itemId);
        if (result) {
            saveFavorites();
            Log.d(TAG, "addFavorite: Kedvenc hozzáadva: " + itemId + ", kedvencek száma: " + favoriteIds.size());
        } else {
            Log.d(TAG, "addFavorite: Étel már kedvenc volt: " + itemId);
        }
        return result;
    }
    
    /**
     * Eltávolít egy ételt a kedvencek közül.
     * @param itemId Az étel azonosítója
     * @return true, ha sikerült eltávolítani, false ha nem volt kedvenc
     */
    public boolean removeFavorite(String itemId) {
        boolean result = favoriteIds.remove(itemId);
        if (result) {
            saveFavorites();
            Log.d(TAG, "removeFavorite: Kedvenc eltávolítva: " + itemId + ", kedvencek száma: " + favoriteIds.size());
        } else {
            Log.d(TAG, "removeFavorite: Étel nem volt kedvenc: " + itemId);
        }
        return result;
    }
    
    /**
     * Ellenőrzi, hogy egy étel kedvenc-e.
     * @param itemId Az étel azonosítója
     * @return true, ha kedvenc, false ha nem
     */
    public boolean isFavorite(String itemId) {
        boolean result = favoriteIds.contains(itemId);
        Log.d(TAG, "isFavorite: " + itemId + " kedvenc? " + result);
        return result;
    }
    
    /**
     * Lekérdezi a kedvenc ételek azonosítóit.
     * @return A kedvenc ételek ID-inak másolata (új Set objektum)
     */
    public Set<String> getFavoriteIds() {
        Log.d(TAG, "getFavoriteIds: Kedvencek száma: " + favoriteIds.size() + ", kedvencek: " + favoriteIds);
        return new HashSet<>(favoriteIds);
    }
    
    /**
     * Törli az összes kedvencet.
     */
    public void clearFavorites() {
        favoriteIds.clear();
        saveFavorites();
        Log.d(TAG, "clearFavorites: Minden kedvenc törölve");
    }
} 