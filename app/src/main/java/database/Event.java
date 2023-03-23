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

    @ColumnInfo(name = "date")
    private final String date;

    @ColumnInfo(name = "start_hour")
    private final String startHour;

    @ColumnInfo(name = "latitude")
    private final double latitude;

    @ColumnInfo(name = "longitude")
    private final double longitude;

    public Event(String name, String location, String date, String startHour,
                 double latitude, double longitude) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.startHour = startHour;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getDate() {
        return date;
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
