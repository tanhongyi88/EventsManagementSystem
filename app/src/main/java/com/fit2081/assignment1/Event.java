package com.fit2081.assignment1;

public class Event {

    private String id;
    private String name;
    private String categoryId;
    private int ticketsAvailable;
    private boolean isActive;

    public Event(String id, String name, String categoryId, int ticketsAvailable, boolean isActive) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.ticketsAvailable = ticketsAvailable;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getTicketsAvailable() {
        return ticketsAvailable;
    }

    public void setTicketsAvailable(int ticketsAvailable) {
        this.ticketsAvailable = ticketsAvailable;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
