package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

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

    @ColumnInfo(name = "hour")
    private final int hour;

    @ColumnInfo(name = "minute")
    private final int minute;

    @ColumnInfo(name = "latitude")
    private final double latitude;

    @ColumnInfo(name = "longitude")
    private final double longitude;

    public Event(String name, String location, int day, int month, int year, int hour, int minute,
                 double latitude, double longitude) {
        this.name = name;
        this.location = location;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
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

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Event)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Event other = (Event) obj;
        return other.name.equals(this.name)
                && other.location.equals(this.location)
                && other.year == this.year
                && other.month == this.month
                && other.day == this.day
                && other.hour == this.hour
                && other.minute == this.minute
                && other.latitude == this.latitude
                && other.longitude == this.longitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, year, month, day, hour, minute, latitude, longitude);
    }
}
