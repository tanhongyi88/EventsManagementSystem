package com.fit2081.assignment1.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventDao {
    @Query("select * from events")
    LiveData<List<Event>> getAllEvents();

    @Query("select * from events where eventId=:eventId")
    List<Event> getEvent(String eventId);

    @Insert
    void addEvent(Event event);

    @Query("delete from events where eventId=:eventId")
    void deleteEvent(String eventId);

    @Query("delete from events")
    void deleteAllEvent();
}
