package pw.sadbd.tourpatron.WeatherUtils.CurrentWeather;

import pw.sadbd.tourpatron.WeatherUtils.CurrentWeather.CurrentWeatherPojo.CurrentWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface CurrentWeatherService {
    @GET
    Call<CurrentWeather> getCurrentWeather(@Url String endUrl);
}
