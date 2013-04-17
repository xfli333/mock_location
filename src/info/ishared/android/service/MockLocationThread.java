package info.ishared.android.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Handler;
import android.provider.Settings;
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
            this.locationManager.addTestProvider(provider, false, false, false, false,true, true, true, 0, 5);
            this.locationManager.setTestProviderEnabled(provider, true);
            return;
        } catch (IllegalArgumentException localIllegalArgumentException) {
            this.locationManager.removeTestProvider(provider);
            this.locationManager.addTestProvider(provider, false, false, false, false, true, true, true, 0, 5);
        }
    }

    @Override
    public void run() {
            mockLocation(this.latLng);
        handler.postDelayed(this, 10000);
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
        Long time = System.currentTimeMillis();
        loc.setTime(time);

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
        locationManager.setTestProviderStatus(provider, LocationProvider.AVAILABLE, null, time);
        locationManager.setTestProviderLocation(provider, loc);

    }

    protected void enableMockAndGetOldValue() {
        int i = 1;
        try {
            i = Settings.Secure.getInt(this.context.getContentResolver(), "mock_location");
            Settings.Secure.putInt(this.context.getContentResolver(), "mock_location", 1);
        } catch (Exception localException) {
        }
    }

    protected void restoreMock(int paramInt) {
        try {
            Settings.Secure.putInt(this.context.getContentResolver(), "mock_location", paramInt);

        } catch (Exception localException) {
        }
    }
}
