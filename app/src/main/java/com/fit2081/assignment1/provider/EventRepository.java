package com.fit2081.assignment1.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class EventRepository {
    private EventDao mEventDao;
    private LiveData<List<Event>> mAllEvents;

    EventRepository(Application application) {
        EventDatabase db = EventDatabase.getDatabase(application);
        mEventDao = db.eventDao();
        mAllEvents = mEventDao.getAllEvents();
    }
    LiveData<List<Event>> getAllEvents() {
        return mAllEvents;
    }
    void insert(Event event) {
        EventDatabase.databaseWriteExecutor.execute(() -> mEventDao.addEvent(event));
    }

    void deleteAll(){
        EventDatabase.databaseWriteExecutor.execute(()->{
            mEventDao.deleteAllEvents();
        });
    }
}
