package pw.sadbd.tourpatron.LocationAndMap.NearbySearch;
import pw.sadbd.tourpatron.LocationAndMap.NearbySearch.NearbyPojo.NearbyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NearbyService {
    @GET
    Call<NearbyResponse>getNearbyPlaces(@Url String endUrl);
}
