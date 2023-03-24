package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "name")
    private final String name;

    @ColumnInfo(name = "location")
    private final String location;

    @ColumnInfo(name = "day")
    private final int day;

    @ColumnInfo(name = "month")
    private final int month;

    @ColumnInfo(name = "year")
    private final int year;

    @ColumnInfo(name = "start_hour")
    private final String startHour;

    @ColumnInfo(name = "latitude")
    private final double latitude;

    @ColumnInfo(name = "longitude")
    private final double longitude;

    public Event(String name, String location, int day, int month, int year, String startHour,
                 double latitude, double longitude) {
        this.name = name;
        this.location = location;
        this.day = day;
        this.month = month;
        this.year = year;
        this.startHour = startHour;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getStartHour() {
        return startHour;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
