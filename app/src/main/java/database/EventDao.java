package database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventDao {
    @Insert
    void insert(Event event);

    @Delete
    void delete(Event event);

    @Query("SELECT * FROM events")
    LiveData<List<Event>> getAll();

    @Query("DELETE FROM events")
    void deleteAll();
}
