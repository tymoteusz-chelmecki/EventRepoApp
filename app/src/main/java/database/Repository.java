package database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.google.common.collect.Iterables;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class Repository {
    private final EventDao eventDao;
    private final LiveData<List<Event>> events;

    public Repository(Application application) {
        eventDao = EventDatabase.getInstance(application).eventDao();
        events = eventDao.getAll();
    }

    public LiveData<List<Event>> getAllEvents() {
        return events;
    }

    public void insert(Event event) {
        new EventStatementTask(eventDao, EventDao::insert).execute(event);
    }

    public void delete(Event event) {
        new EventStatementTask(eventDao, EventDao::delete).execute(event);
    }

    public void deleteAll() {
        new DeleteAllTask(eventDao).execute();
    }

    public LiveData<List<Event>> getByName(String name) {
        return eventDao.getByName(name);
    }

    public LiveData<List<Event>> getByDate(String date) {
        String[] splitDate = date.trim().split("-");
        if (splitDate.length != 3) {
            throw new IllegalArgumentException(
                    date + " is not in a supported date format (dd-mm-yyyy)");
        }
        int day = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]);
        int year = Integer.parseInt(splitDate[2]);
        return getByDate(day, month, year);
    }

    public LiveData<List<Event>> getByDate(int day, int month, int year) {
        return eventDao.getByDate(day, month, year);
    }

    public LiveData<List<Event>> getBetweenDates(int fromDay, int fromMonth, int fromYear,
                                                 int toDay, int toMonth, int toYear) {
        return eventDao.getBetweenDates(fromDay, fromMonth, fromYear, toDay, toMonth, toYear);
    }

    private static class EventStatementTask extends AsyncTask<Event, Void, Void> {
        private final EventDao eventDao;
        private final BiConsumer<EventDao, Event> eventStatement;

        public EventStatementTask(EventDao eventDao, BiConsumer<EventDao, Event> eventStatement) {
            super();
            this.eventDao = eventDao;
            this.eventStatement = eventStatement;
        }

        @Override
        protected Void doInBackground(Event... events) {
            Event event = Iterables.getOnlyElement(Arrays.asList(events));
            eventStatement.accept(eventDao, event);
            return null;
        }
    }

    private static class DeleteAllTask extends AsyncTask<Void, Void, Void> {
        private final EventDao eventDao;

        public DeleteAllTask(EventDao eventDao) {
            super();
            this.eventDao = eventDao;
        }

        @Override
        protected Void doInBackground(Void ... voids) {
            eventDao.deleteAll();
            return null;
        }
    }
}
