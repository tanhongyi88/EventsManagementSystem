package com.fit2081.assignment1.provider;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "categoryId")
    private String categoryId;
    @ColumnInfo(name = "categoryName")
    private String name;
    @ColumnInfo(name = "eventCount")
    private int eventCount;
    @ColumnInfo(name = "isActive")
    private boolean isActive;
    @ColumnInfo(name = "eventLocation")
    private String location;

    @Ignore
    public Category(String categoryId, String name, int eventCount, boolean isActive) {
        this.categoryId = categoryId;
        this.name = name;
        this.eventCount = eventCount;
        this.isActive = isActive;
    }
    public Category(String categoryId, String name, int eventCount, boolean isActive, String location) {
        this.categoryId = categoryId;
        this.name = name;
        this.eventCount = eventCount;
        this.isActive = isActive;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void incrementEventCount() {
        this.eventCount += 1;
    }
    public void decrementEventCount() {
        this.eventCount -= 1;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", eventCount=" + eventCount +
                ", isActive=" + isActive +
                '}';
    }
}
