package nje.gamf.speedyspoon.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseError;

import java.util.List;

import nje.gamf.speedyspoon.Models.Category;
import nje.gamf.speedyspoon.Repositories.CategoriesCallback;
import nje.gamf.speedyspoon.Repositories.CategoriesRepository;

public class CategoriesViewModel extends ViewModel {

    private CategoriesRepository categoriesRepository;
    private MutableLiveData<List<Category>> _categories = new MutableLiveData<>();
    public LiveData<List<Category>> categories = _categories;

    public CategoriesViewModel() {
        categoriesRepository = new CategoriesRepository();
        fetchCategories();
    }

    public void fetchCategories() {
        categoriesRepository.fetchCategories(new CategoriesCallback() {
            @Override
            public void onCategoriesLoaded(List<Category> categories) {
                _categories.postValue(categories);
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }
}