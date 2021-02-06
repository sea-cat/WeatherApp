package ro.seacat.weatherapp;

import android.app.Application;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import ro.seacat.weatherapp.data.WeatherRepository;
import ro.seacat.weatherapp.data.pojo.WeatherData;
import ro.seacat.weatherapp.ui.MainActivityViewModel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

  @Rule // -> allows liveData to work on different thread besides main, must be public!
  public InstantTaskExecutorRule executorRule = new InstantTaskExecutorRule();

  @Mock
  private WeatherRepository weatherRepositoryMock = mock(WeatherRepository.class);
  @Mock
  private Application appContextMock = mock(Application.class);

  private Observer<WeatherData> liveWeatherObserver = mock(Observer.class);;
  private Observer<Integer> errorObserver = mock(Observer.class);
  private Observer<Boolean> loadingObserver = mock(Observer.class);
  private MainActivityViewModel subject = new MainActivityViewModel(weatherRepositoryMock, appContextMock);

  @Before
  public void setUp() {
    //mock the live data observer
    subject.getLiveWeather().observeForever(liveWeatherObserver);
  }

  @Test
  public void testWeatherLiveDataWithError() {
//    when(weatherRepositoryMock.getStoredWeather(anyInt(), anyInt())).thenReturn(Maybe.error(Throwable::new));
//    when(weatherRepositoryMock.getNetworkWeather(anyDouble(), anyDouble())).thenReturn(Observable.error(Throwable::new));
//
//    subject.populateView();
//    verifyNoInteractions(liveWeatherObserver);
//    verify(errorObserver).onChanged(any());
  }

  private void testWeatherLiveDataSuccess() {

  }

}