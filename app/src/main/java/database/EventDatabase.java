package database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Event.class}, version = 4)
public abstract class EventDatabase extends RoomDatabase {
    public static String DB_NAME = "event_db";
    private static EventDatabase database;

    public abstract EventDao eventDao();

    public static synchronized EventDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),
                            EventDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
}
