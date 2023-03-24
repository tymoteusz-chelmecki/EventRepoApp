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
        new EventQueryTask(eventDao, EventDao::insert).execute(event);
    }

    public void delete(Event event) {
        new EventQueryTask(eventDao, EventDao::delete).execute(event);
    }

    public void deleteAll() {
        new DeleteAllTask(eventDao).execute();
    }

    private static class EventQueryTask extends AsyncTask<Event, Void, Void> {
        private final EventDao eventDao;
        private final BiConsumer<EventDao, Event> queryAction;

        public EventQueryTask(EventDao eventDao, BiConsumer<EventDao, Event> queryAction) {
            super();
            this.eventDao = eventDao;
            this.queryAction = queryAction;
        }

        @Override
        protected Void doInBackground(Event... events) {
            Event event = Iterables.getOnlyElement(Arrays.asList(events));
            queryAction.accept(eventDao, event);
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
