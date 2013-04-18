package info.ishared.android;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.gms.maps.model.LatLng;
import info.ishared.android.bean.LocationType;
import info.ishared.android.bean.MockLatLng;
import info.ishared.android.dao.MockLatLngDao;
import info.ishared.android.service.MockLocationService;
import info.ishared.android.service.NewMockLocationService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: Seven
 * Date: 13-4-11
 * Time: AM11:53
 */
public class MainController {

    private MainActivity mainActivity;


    private MockLatLngDao mockLatLngDao;

    public MainController(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        mockLatLngDao = new MockLatLngDao(mainActivity);
    }

    public void startMockLocation(LatLng latLng) {
        saveOrUpdateCurrentMockLocation(latLng);
//        Intent intent = new Intent(mainActivity, MockLocationService.class);
//        Intent intent = new Intent(mainActivity, NewMockLocationService.class);
//
//        if (SystemUtils.isServiceWorked(mainActivity, "info.ishared.android.service.MockLocationService")) {
//            mainActivity.stopService(intent);
//        }
//


        mainActivity.startService(new Intent(mainActivity, MockLocationService.class).putExtra("latitude",latLng.latitude).putExtra("longitude",latLng.longitude));
    }

    public void stopMockLocationService() {
        Intent intent = new Intent(mainActivity, MockLocationService.class);
//        Intent intent = new Intent(mainActivity, FakeGPSService.class);
        mainActivity.stopService(intent);
    }

    public LatLng getLastMockLocation() {
        List<MockLatLng> mockLatLngList = this.mockLatLngDao.queryMockLatLngByType(LocationType.LAST);
        if (!mockLatLngList.isEmpty()) {
            return new LatLng(mockLatLngList.get(0).getLatitude(), mockLatLngList.get(0).getLongitude());
        }
        return null;
//        return new LatLng(30.66, 104.07);
    }


    private void saveOrUpdateCurrentMockLocation(LatLng latLng) {
        List<MockLatLng> mockLatLngList = this.mockLatLngDao.queryMockLatLngByType(LocationType.LAST);
        if (mockLatLngList.isEmpty()) {
            MockLatLng mockLatLng = new MockLatLng();
            mockLatLng.setLatitude(latLng.latitude);
            mockLatLng.setLongitude(latLng.longitude);
            mockLatLng.setLocationType(LocationType.LAST.name());
            this.mockLatLngDao.insertCurrentMockLocation(mockLatLng);
        } else {
            MockLatLng mockLatLng = mockLatLngList.get(0);
            mockLatLng.setLatitude(latLng.latitude);
            mockLatLng.setLongitude(latLng.longitude);
            this.mockLatLngDao.updateCurrentMockLocation(mockLatLng);
        }

    }
}
