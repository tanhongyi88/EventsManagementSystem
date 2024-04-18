package com.fit2081.assignment1;

public class Category {
    private String id;
    private String name;
    private int eventCount;
    private boolean isActive;

    public Category(String id, String name, int eventCount, boolean isActive) {
        this.id = id;
        this.name = name;
        this.eventCount = eventCount;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
