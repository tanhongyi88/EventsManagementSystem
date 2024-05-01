package com.fit2081.assignment1.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryRepository {
    private CategoryDao mCategoryDao;
    private LiveData<List<Category>> mAllCategories;

    CategoryRepository(Application application) {
        CategoryDatabase db = CategoryDatabase.getDatabase(application);
        mCategoryDao = db.categoryDao();
        mAllCategories = mCategoryDao.getAllCategories();
    }
    LiveData<List<Category>> getAllCategories() {
        return mAllCategories;
    }
    List<Category> getCategory(String categoryId) {
        return mCategoryDao.getCategory(categoryId);
    }
    void insert(Category category) {
        CategoryDatabase.databaseWriteExecutor.execute(() -> mCategoryDao.addCategory(category));
    }

    void deleteAll(){
        CategoryDatabase.databaseWriteExecutor.execute(()->{
            mCategoryDao.deleteAllCategories();
        });
    }
}
