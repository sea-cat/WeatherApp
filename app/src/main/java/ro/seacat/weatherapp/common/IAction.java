package ro.seacat.weatherapp.common;

public interface IAction<WMessage> {
  void perform(WMessage message);
}