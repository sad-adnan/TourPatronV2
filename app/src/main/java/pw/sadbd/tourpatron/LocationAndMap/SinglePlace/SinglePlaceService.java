package pw.sadbd.tourpatron.LocationAndMap.SinglePlace;
import pw.sadbd.tourpatron.LocationAndMap.SinglePlace.Pojo.SinglePlace;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface SinglePlaceService {
    @GET
    Call<SinglePlace>getsinglePlaces(@Url String endUrl);
}
