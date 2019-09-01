package pw.sadbd.tourpatron.WeatherUtils.ForecastWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ForecastService {
    @GET
    Call<ForecasetWeather> getForcaseWeather(@Url String endUrl);
}
