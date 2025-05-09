package nje.gamf.speedyspoon.Repositories;

import com.google.firebase.database.DatabaseError;

import java.util.List;

import nje.gamf.speedyspoon.Models.MenuItem;

public interface MenuItemCallback {
    void onMenuItemsLoaded(List<MenuItem> menuItems);
    void onError(DatabaseError error);
}
