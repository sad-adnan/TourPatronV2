package pw.sadbd.tourpatron.LocationAndMap.SinglePlace;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class  RetrofitClient {
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/findplacefromtext/";
    public static Retrofit getClient(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
