package ro.seacat.weatherapp.data;

import android.location.Location;

import org.junit.Test;

import java.util.Date;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import ro.seacat.weatherapp.api.WeatherAPI;
import ro.seacat.weatherapp.common.Utils;
import ro.seacat.weatherapp.data.pojo.WeatherData;
import ro.seacat.weatherapp.data.pojo.WeatherDataResponse;
import ro.seacat.weatherapp.data.pojo.WeatherRaw;
import ro.seacat.weatherapp.data.util.WeatherDataTranslator;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeatherRepositoryTest {

  private final WeatherAPI weatherAPIMock = mock(WeatherAPI.class);
  private final WeatherDao weatherDaoMock = mock(WeatherDao.class);
  private final WeatherDataTranslator translatorMock = mock(WeatherDataTranslator.class);
  private final Utils utils = mock(Utils.class);
  private final Location location = mock(Location.class);

  private final WeatherRepository subject = new WeatherRepository(weatherAPIMock, weatherDaoMock, translatorMock, utils);

  @Test
  public void fetchData_WithNullLocationAndNoStorage_ShouldNotReturnData() {
    when(weatherDaoMock.getData()).thenReturn(Single.error(new Throwable()));

    TestObserver<WeatherDataResponse> testObserver = subject.fetchData(null).test();

    WeatherDataResponse weatherDataResponse = testObserver.values().get(0);
    assertNull(weatherDataResponse.getWeatherData());
    assertNull(weatherDataResponse.getError());
    assertTrue(weatherDataResponse.isFromStorage());
    testObserver.assertNoErrors();

    testObserver.dispose();
  }

  @Test
  public void fetchData_WithNullLocationAndStorage_ShouldReturnDataFromStorage() {
    WeatherData weatherData = new WeatherData();
    weatherData.lastFetched = new Date();

    when(weatherDaoMock.getData()).thenReturn(Single.just(weatherData));

    TestObserver<WeatherDataResponse> testObserver = subject.fetchData(null).test();

    WeatherDataResponse weatherDataResponse = testObserver.values().get(0);
    assertEquals(weatherDataResponse.getWeatherData(), weatherData);
    assertNull(weatherDataResponse.getError());
    assertTrue(weatherDataResponse.isFromStorage());
    testObserver.assertNoErrors();

    testObserver.dispose();
  }

  @Test
  public void fetchData_WithNullLocationAndOldStorage_ShouldNotReturnDataFromStorage() {
    WeatherData weatherData = new WeatherData();
    weatherData.latitude = 2.2;
    weatherData.longitude = 2.2;
    weatherData.lastFetched = new Date((new Date()).getTime() - 24 * 60 * 60 * 1000);

    when(weatherDaoMock.getData()).thenReturn(Single.just(weatherData));

    TestObserver<WeatherDataResponse> testObserver = subject.fetchData(null).test();

    WeatherDataResponse weatherDataResponse = testObserver.values().get(0);
    assertNull(weatherDataResponse.getWeatherData());
    assertNull(weatherDataResponse.getError());
    assertTrue(weatherDataResponse.isFromStorage());
    testObserver.assertNoErrors();

    verify(weatherDaoMock).deleteByLatLong(weatherData.latitude, weatherData.longitude);

    testObserver.dispose();
  }

  @Test
  public void fetchData_WithLocationWithNoNetworkWithNoStorage_ShouldNotReturnData() {
    WeatherData weatherData = new WeatherData();
    weatherData.lastFetched = new Date();
    Throwable apiThrowable = mock(Throwable.class);

    when(weatherAPIMock.getByLatLong(anyDouble(), anyDouble(), anyString(), anyString())).thenReturn(Single.error(apiThrowable));
    when(weatherDaoMock.findByLatLong(anyDouble(), anyDouble())).thenReturn(Single.error(new Throwable()));

    TestObserver<WeatherDataResponse> testObserver = subject.fetchData(location).test();

    WeatherDataResponse weatherDataResponse = testObserver.values().get(0);
    assertNull(weatherDataResponse.getWeatherData());
    assertEquals(apiThrowable, weatherDataResponse.getError());
    assertTrue(weatherDataResponse.isFromStorage());
    testObserver.assertNoErrors();

    testObserver.dispose();
  }

  @Test
  public void fetchData_WithLocationWithNoNetworkWithStorage_ShouldReturnDataFromStorage() {
    WeatherData weatherData = new WeatherData();
    weatherData.lastFetched = new Date();
    Throwable apiThrowable = mock(Throwable.class);

    when(weatherAPIMock.getByLatLong(anyDouble(), anyDouble(), anyString(), anyString())).thenReturn(Single.error(apiThrowable));
    when(weatherDaoMock.findByLatLong(anyDouble(), anyDouble())).thenReturn(Single.just(weatherData));
    when(location.getLatitude()).thenReturn(2.2);
    when(location.getLongitude()).thenReturn(2.2);
    when(utils.formatCoordinates(2.2)).thenReturn(2.2);

    TestObserver<WeatherDataResponse> testObserver = subject.fetchData(location).test();

    WeatherDataResponse response = testObserver.values().get(0);
    assertEquals(response.getWeatherData(), weatherData);
    assertEquals(apiThrowable, response.getError());
    assertTrue(response.isFromStorage());
    testObserver.assertNoErrors();

    testObserver.dispose();
  }

  @Test
  public void fetchData_WithLocationWithNoNetworkWithOldStorage_ShouldNotReturnDataFromStorage() {
    WeatherData weatherData = new WeatherData();
    weatherData.lastFetched = new Date((new Date()).getTime() - 24 * 60 * 60 * 1000);
    weatherData.latitude = 2.2;
    weatherData.longitude = 2.2;
    Throwable apiThrowable = mock(Throwable.class);

    when(weatherAPIMock.getByLatLong(anyDouble(), anyDouble(), anyString(), anyString())).thenReturn(Single.error(apiThrowable));
    when(weatherDaoMock.findByLatLong(anyDouble(), anyDouble())).thenReturn(Single.just(weatherData));
    when(location.getLatitude()).thenReturn(2.2);
    when(location.getLongitude()).thenReturn(2.2);
    when(utils.formatCoordinates(2.2)).thenReturn(2.2);

    TestObserver<WeatherDataResponse> testObserver = subject.fetchData(location).test();

    WeatherDataResponse response = testObserver.values().get(0);
    assertNull(response.getWeatherData());
    assertEquals(apiThrowable, response.getError());
    assertTrue(response.isFromStorage());
    testObserver.assertNoErrors();

    verify(weatherDaoMock).deleteByLatLong(weatherData.latitude, weatherData.longitude);

    testObserver.dispose();
  }

  @Test
  public void fetchData_WithLocationWithNetwork_ShouldReturnDataFromNetwork() {
    WeatherData weatherData = new WeatherData();
    weatherData.lastFetched = new Date();
    weatherData.latitude = 2.2;
    weatherData.longitude = 2.2;

    when(weatherAPIMock.getByLatLong(anyDouble(), anyDouble(), anyString(), anyString())).thenReturn(Single.just(mock(WeatherRaw.class)));
    when(translatorMock.translate(any(WeatherRaw.class))).thenReturn(weatherData);
    when(location.getLatitude()).thenReturn(2.2);
    when(location.getLongitude()).thenReturn(2.2);
    when(utils.formatCoordinates(2.2)).thenReturn(2.2);

    TestObserver<WeatherDataResponse> testObserver = subject.fetchData(location).test();

    WeatherDataResponse response = testObserver.values().get(0);
    assertEquals(response.getWeatherData(), weatherData);
    assertNull(response.getError());
    assertFalse(response.isFromStorage());
    testObserver.assertNoErrors();

    verify(weatherDaoMock).deleteByLatLong(weatherData.latitude, weatherData.longitude);
    verify(weatherDaoMock).insert(weatherData);

    testObserver.dispose();
  }

}