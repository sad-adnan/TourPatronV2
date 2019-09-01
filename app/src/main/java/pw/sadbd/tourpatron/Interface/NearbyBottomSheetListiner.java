package pw.sadbd.tourpatron.Interface;

import pw.sadbd.tourpatron.LocationAndMap.NearbySearch.NearbyPojo.Result;

import java.util.List;

public interface NearbyBottomSheetListiner {
    void getNearbyObject(List<Result> resultList);
}
