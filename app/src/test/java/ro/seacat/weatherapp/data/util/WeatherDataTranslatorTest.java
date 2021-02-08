package ro.seacat.weatherapp.data.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import ro.seacat.weatherapp.common.Utils;
import ro.seacat.weatherapp.data.pojo.WeatherData;
import ro.seacat.weatherapp.data.pojo.WeatherRaw;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherDataTranslatorTest {

  private final Utils utils = mock(Utils.class);
  private final WeatherDataTranslator translator = new WeatherDataTranslator(utils);

  @Test
  public void translate_NullRaw_ShouldReturnNull() {
    assertNull(translator.translate(null));
  }

  @Test
  public void translate_RawWithMissingInnerObjects_ShouldReturnEmptyData() {
    WeatherData weatherData = translator.translate(new WeatherRaw());
    assertNotNull(weatherData.lastFetched);
  }

  @Test
  public void translate_RawWithEmptyInnerObjects_ShouldTranslate() {
    WeatherRaw raw = new WeatherRaw();
    raw.setWeather(new ArrayList<>());
    raw.setCoordinates(new WeatherRaw.Coordinates());
    raw.setTemperature(new WeatherRaw.Temperature());
    raw.setWind(new WeatherRaw.Wind());

    WeatherData weatherData = translator.translate(new WeatherRaw());

    assertNotNull(weatherData);
    assertValues(weatherData, null, null, null, null, null, null, null, null);
  }

  @Test
  public void translate_RawWit_ShouldTranslate() {
    when(utils.formatCoordinates(1.1)).thenReturn(1.1);
    when(utils.formatCoordinates(2.2)).thenReturn(2.2);

    WeatherRaw raw = new WeatherRaw();
    raw.setWeather(Collections.singletonList(new WeatherRaw.Weather("Sunny", "09x")));
    raw.setCoordinates(new WeatherRaw.Coordinates(1.1, 2.2));
    raw.setTemperature(new WeatherRaw.Temperature(3.3));
    raw.setWind(new WeatherRaw.Wind(4.4, 5.5));
    raw.setCityName("Belfast");

    WeatherData weatherData = translator.translate(raw);

    assertNotNull(weatherData);
    assertValues(weatherData, "Sunny", 3.3, 4.4, 5.5, "09x", 1.1, 2.2, "Belfast");
  }

  private void assertValues(WeatherData weatherData, String currentCondition, Double temperature, Double windSpeed, Double windDegrees,
                            String icon, Double latitude, Double longitude, String cityName) {
    assertEquals(weatherData.currentCondition, currentCondition);
    assertEquals(weatherData.temperature, temperature);
    assertEquals(weatherData.windSpeed, windSpeed);
    assertEquals(weatherData.windDegrees, windDegrees);
    assertEquals(weatherData.icon, icon);
    assertEquals(weatherData.latitude, latitude);
    assertEquals(weatherData.longitude, longitude);
    assertEquals(weatherData.cityName, cityName);
    assertNotNull(weatherData.lastFetched);
  }

}