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

    @Query("SELECT * FROM events WHERE name LIKE '%' || :name || '%'")
    LiveData<List<Event>> getByName(String name);

    @Query("SELECT * FROM events WHERE day = :day AND month = :month AND year = :year " +
            "ORDER BY year, month, day ASC")
    LiveData<List<Event>> getForDate(int day, int month, int year);

    @Query("SELECT * FROM events WHERE (day >= :fromDay AND month >= :fromMonth AND year >= :fromYear "+
            "AND day <= :toDay AND month <= :toMonth AND year <= :toYear ) " +
            "ORDER BY year, month, day ASC")
    LiveData<List<Event>> getBetweenDates(int fromDay, int fromMonth, int fromYear,
                                          int toDay, int toMonth, int toYear);

    @Query("DELETE FROM events")
    void deleteAll();
}
