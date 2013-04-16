package info.ishared.android;

import android.content.Intent;
import android.location.LocationManager;
import com.google.android.gms.maps.model.LatLng;
import info.ishared.android.bean.LocationType;
import info.ishared.android.bean.MockLatLng;
import info.ishared.android.dao.MockLatLngDao;
import info.ishared.android.service.MockLocationService;

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
    LocationManager locationManager;

    String provider = LocationManager.GPS_PROVIDER;
    private ExecutorService executor = Executors.newFixedThreadPool(1);

    private boolean flag = true;
    private LatLng latLng;

    private MockLatLngDao mockLatLngDao;

    public MainController(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        mockLatLngDao = new MockLatLngDao(mainActivity);
    }

    public void startMockLocation(LatLng latLng) {
        saveOrUpdateCurrentMockLocation(latLng);
        Intent intent = new Intent(mainActivity, MockLocationService.class);
//
//        if (SystemUtils.isServiceWorked(mainActivity, "info.ishared.android.service.MockLocationService")) {
//            mainActivity.stopService(intent);
//        }
//
//        MockLocationService.latLng = latLng;
//        mainActivity.startService(intent);

//        Intent intent = new Intent(mainActivity, FakeGPSService.class);
//
//        if (SystemUtils.isServiceWorked(mainActivity, "info.ishared.android.service.MockLocationService")) {
//            mainActivity.stopService(intent);
//        }
//
//        MockLocationService.latLng = latLng;
        mainActivity.startService(intent);
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
