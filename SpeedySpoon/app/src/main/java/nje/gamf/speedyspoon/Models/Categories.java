package nje.gamf.speedyspoon.Models;

import java.util.Map;

public class Categories {
    private Map<String, Category> categories;

    public Categories() {
    }

    public Map<String, Category> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, Category> categories) {
        this.categories = categories;
    }
}
