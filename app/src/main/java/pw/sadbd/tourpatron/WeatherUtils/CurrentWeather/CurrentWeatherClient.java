package pw.sadbd.tourpatron.WeatherUtils.CurrentWeather;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrentWeatherClient {
    public  static Retrofit getClient(String baseurl){
        return new Retrofit
                .Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
