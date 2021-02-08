package ro.seacat.weatherapp.viewmodels;

import android.app.Application;
import android.content.res.Resources;
import android.location.Location;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import ro.seacat.weatherapp.R;
import ro.seacat.weatherapp.common.Utils;
import ro.seacat.weatherapp.data.WeatherRepository;
import ro.seacat.weatherapp.data.pojo.WeatherData;
import ro.seacat.weatherapp.data.pojo.WeatherDataResponse;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class ViewModelUnitTest {

  @Rule // -> allows liveData to work on different thread besides main, must be public!
  public InstantTaskExecutorRule executorRule = new InstantTaskExecutorRule();

  private final WeatherRepository weatherRepositoryMock = mock(WeatherRepository.class);
  private final Application appContextMock = mock(Application.class);
  private final Location locationMock = mock(Location.class);
  private final Utils utilsMock = mock(Utils.class);
  private final Resources resourcesMock = mock(Resources.class);
  private int loadingStateChanged;

  private final Observer<WeatherData> liveWeatherObserver = mock(Observer.class);
  private final Observer<Integer> errorObserver = mock(Observer.class);
  private final Observer<String> displayLastUpdatedMessageObserver = mock(Observer.class);
  private final Observer<Boolean> loadingObserver = mock(Observer.class);
  private final Observer<String> noDataObserver = mock(Observer.class);

  private MainActivityViewModel subject;

  @BeforeClass
  public static void setUpScheduler() {
    RxAndroidPlugins.reset();
    RxAndroidPlugins.setMainThreadSchedulerHandler(_not_used -> Schedulers.trampoline());
    RxJavaPlugins.setIoSchedulerHandler(__ -> Schedulers.trampoline());
  }

  @Before
  public void setUp() {
    loadingStateChanged = 0;
    when(appContextMock.getResources()).thenReturn(resourcesMock);
    when(resourcesMock.getString(R.string.message_loading)).thenReturn("loading");
    when(resourcesMock.getString(R.string.error_no_relevant_data)).thenReturn("error");

    subject = new MainActivityViewModel(weatherRepositoryMock, appContextMock, utilsMock);

    subject.getLiveWeather().observeForever(liveWeatherObserver);
    subject.getDisplayError().observeForever(errorObserver);
    subject.getDisplayLastUpdatedMessage().observeForever(displayLastUpdatedMessageObserver);
    subject.getLoading().observeForever(loadingObserver);
    subject.getNoData().observeForever(noDataObserver);
  }

  @Test
  public void fetchData_WithNoLocationNoNetworkAndEmptyStorage_ShouldNotUpdateWeatherUIOrShowError() {
    when(weatherRepositoryMock.fetchData(null)).thenReturn(Single.just(new WeatherDataResponse(null, null, true)));
    doAnswer((Answer<Boolean>) invocation -> {
      loadingStateChanged++;
      assertTrue(invocation.getArgument(0));
      return true;
    }).doAnswer((Answer<Boolean>) invocation -> {
      loadingStateChanged++;
      assertFalse(invocation.getArgument(0));
      return true;
    }).when(loadingObserver).onChanged(any());

    subject.fetchData(null);

    verifyNoInteractions(liveWeatherObserver);
    verify(noDataObserver).onChanged("error");
    verifyNoInteractions(errorObserver);
    assertEquals(2, loadingStateChanged);
    verifyNoInteractions(displayLastUpdatedMessageObserver);
  }

  @Test
  public void fetchData_WithLocationNoNetworkAndDataInStorage_ShouldUpdateUIAndShowError() {
    WeatherData weatherData = new WeatherData();
    when(weatherRepositoryMock.fetchData(locationMock)).thenReturn(Single.just(new WeatherDataResponse(weatherData, new Throwable(), true)));
    when(utilsMock.formatWarning(appContextMock, weatherData)).thenReturn("text");
    doAnswer((Answer<Boolean>) invocation -> {
      loadingStateChanged++;
      assertTrue(invocation.getArgument(0));
      return true;
    }).doAnswer((Answer<Boolean>) invocation -> {
      loadingStateChanged++;
      assertFalse(invocation.getArgument(0));
      return true;
    }).when(loadingObserver).onChanged(any());

    subject.fetchData(locationMock);

    verify(liveWeatherObserver).onChanged(weatherData);
    verify(errorObserver).onChanged(anyInt());
    verify(noDataObserver).onChanged("loading");
    assertEquals(2, loadingStateChanged);
    verify(displayLastUpdatedMessageObserver).onChanged(anyString());
  }

  @Test
  public void fetchData_WithLocationAndNetwork_ShouldUpdateUI() {
    WeatherData weatherData = new WeatherData();
    when(weatherRepositoryMock.fetchData(locationMock)).thenReturn(Single.just(new WeatherDataResponse(weatherData, null, false)));
    when(utilsMock.formatWarning(appContextMock, weatherData)).thenReturn("text");
    doAnswer((Answer<Boolean>) invocation -> {
      loadingStateChanged++;
      assertTrue(invocation.getArgument(0));
      return true;
    }).doAnswer((Answer<Boolean>) invocation -> {
      loadingStateChanged++;
      assertFalse(invocation.getArgument(0));
      return true;
    }).when(loadingObserver).onChanged(any());

    subject.fetchData(locationMock);

    verify(liveWeatherObserver).onChanged(weatherData);
    verifyNoInteractions(errorObserver);
    verify(noDataObserver).onChanged("loading");
    assertEquals(2, loadingStateChanged);
    verify(displayLastUpdatedMessageObserver).onChanged(null);
  }


}