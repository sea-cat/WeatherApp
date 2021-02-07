package ro.seacat.weatherapp;

import android.app.Application;
import android.location.Location;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import io.reactivex.Single;
import ro.seacat.weatherapp.data.WeatherRepository;
import ro.seacat.weatherapp.data.pojo.WeatherData;
import ro.seacat.weatherapp.data.pojo.WeatherDataResponse;
import ro.seacat.weatherapp.ui.MainActivityViewModel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ViewModelUnitTest {

  @Rule // -> allows liveData to work on different thread besides main, must be public!
  public InstantTaskExecutorRule executorRule = new InstantTaskExecutorRule();

  @Mock
  private final WeatherRepository weatherRepositoryMock = mock(WeatherRepository.class);
  @Mock
  private final Application appContextMock = mock(Application.class);
  private final Location location = mock(Location.class);

  private final Observer<WeatherData> liveWeatherObserver = mock(Observer.class);
  private final Observer<Integer> errorObserver = mock(Observer.class);
  private final Observer<Boolean> loadingObserver = mock(Observer.class);
  private final MainActivityViewModel subject = new MainActivityViewModel(weatherRepositoryMock, appContextMock);

  @Before
  public void setUp() {
    //mock the live data observer
    subject.getLiveWeather().observeForever(liveWeatherObserver);
    subject.getDisplayError().observeForever(errorObserver);
  }

  @Test
  public void testWeatherLiveDataWithError() {
    when(weatherRepositoryMock.fetchData(location)).thenReturn(Single.just(new WeatherDataResponse(null, new Throwable(), true)));

    subject.fetchData(location);
    verifyNoInteractions(liveWeatherObserver);
    verify(errorObserver).onChanged(any());
  }

  private void testWeatherLiveDataSuccess() {

  }

}