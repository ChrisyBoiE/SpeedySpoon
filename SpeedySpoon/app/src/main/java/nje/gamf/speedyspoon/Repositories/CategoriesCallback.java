package nje.gamf.speedyspoon.Repositories;

import com.google.firebase.database.DatabaseError;

import java.util.List;

import nje.gamf.speedyspoon.Models.Category;

public interface CategoriesCallback {
    void onCategoriesLoaded(List<Category> categories);
    void onError(DatabaseError error);
}
