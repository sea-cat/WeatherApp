package ro.seacat.weatherapp.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import ro.seacat.weatherapp.data.util.Converters;

@Database(entities = {WeatherData.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

  public abstract WeatherDao weatherDao();
}
