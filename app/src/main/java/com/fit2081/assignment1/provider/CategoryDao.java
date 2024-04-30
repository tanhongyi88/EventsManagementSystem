package com.fit2081.assignment1.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("select * from categories")
    LiveData<List<Category>> getAllCategories();

    @Query("select * from categories where categoryId=:categoryId")
    LiveData<List<Category>> getCategory(String categoryId);

    @Insert
    void addCategory(Category category);

    @Query("delete from categories where categoryId=:categoryId")
    void deleteCategory(String categoryId);

    @Query("delete from categories")
    void deleteAllCategories();
}
