package info.ishared.android.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Handler;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import info.ishared.android.AppConfig;
import info.ishared.android.bean.LocationType;
import info.ishared.android.bean.MockLatLng;
import info.ishared.android.dao.MockLatLngDao;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seven
 * Date: 13-4-16
 * Time: PM4:38
 */
public class MockLocationThread implements Runnable {
    private String provider = LocationManager.GPS_PROVIDER;
    private Context context;
    private Location lastLocation;
    private LocationManager locationManager;
    private LatLng latLng;
    private MockLatLngDao mockLatLngDao;
    private Handler handler;


    public MockLocationThread(Context paramContext, Handler handler) {
        this.context = paramContext;
        this.handler = handler;
        this.locationManager = ((LocationManager) paramContext.getSystemService("location"));
        this.mockLatLngDao = new MockLatLngDao(context);
        try {
            this.locationManager.addTestProvider(provider, false, false, false, false, false, false, false, 1, 1);
            this.locationManager.setTestProviderEnabled(provider, true);
            return;
        } catch (IllegalArgumentException localIllegalArgumentException) {
            this.locationManager.removeTestProvider(provider);
            this.locationManager.addTestProvider(provider, false, false, false, false, false, false, false, 1, 1);
        }
    }

    @Override
    public void run() {
        mockLocation(this.latLng);
        handler.postDelayed(this, 3000);
    }

    public void unRegister() {
        try {
            this.locationManager.removeTestProvider("gps");
        } catch (Exception localException) {
        }
    }

    private void mockLocation(LatLng latLng) {
        Log.d(AppConfig.TAG, "mockLocation .........");
        Location loc = new Location(provider);
        loc.setTime(System.currentTimeMillis());

        if (latLng == null) {
            Log.d(AppConfig.TAG, "latLng is null");
            List<MockLatLng> mockLatLngList = this.mockLatLngDao.queryMockLatLngByType(LocationType.LAST);
            if (!mockLatLngList.isEmpty()) {
                loc.setLatitude(mockLatLngList.get(0).getLatitude());
                loc.setLongitude(mockLatLngList.get(0).getLongitude());
                this.latLng = new LatLng(mockLatLngList.get(0).getLatitude(), mockLatLngList.get(0).getLongitude());
            }
        } else {
            loc.setLatitude(latLng.latitude);
            loc.setLongitude(latLng.longitude);
        }
        locationManager.setTestProviderEnabled(provider, true);
        locationManager.setTestProviderStatus(provider, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
        locationManager.setTestProviderLocation(provider, loc);

    }
}
