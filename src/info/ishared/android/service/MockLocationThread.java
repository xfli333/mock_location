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
    private LocationManager locationManager;
    private LatLng latLng;
    private MockLatLngDao mockLatLngDao;
    private Handler handler;
    private Location loc;


    public MockLocationThread(Context paramContext) {
        this.context = paramContext;
        this.handler = new Handler();
        this.locationManager = (LocationManager) paramContext.getSystemService("location");
        this.mockLatLngDao = new MockLatLngDao(context);
        loc = new Location(provider);
        try {
            this.locationManager.addTestProvider("gps", false, false, false, false, false, false, false, 1, 1);
            this.locationManager.setTestProviderEnabled(provider, true);
            return;
        } catch (IllegalArgumentException localIllegalArgumentException) {
            Log.d(AppConfig.TAG, "localIllegalArgumentException ......");
            this.locationManager.removeTestProvider(provider);
            this.locationManager.addTestProvider("gps", false, false, false, false, false, false, false, 1, 1);
        }
    }

    @Override
    public void run() {

        mockLocation(this.latLng);
        handler.postDelayed(this, 3000L);
    }

    public void unRegister() {
        try {
            this.locationManager.removeTestProvider("gps");
            handler.removeCallbacks(this);
        } catch (Exception localException) {
        }
    }

    private void mockLocation(LatLng latLng) {
        Log.d(AppConfig.TAG, "mockLocation ......"+this.loc.getLatitude()+","+this.loc.getLongitude());

        Long time = System.currentTimeMillis();
        this.loc.setTime(time);

//        if (latLng == null) {
//            Log.d(AppConfig.TAG, "latLng is null");
//            List<MockLatLng> mockLatLngList = this.mockLatLngDao.queryMockLatLngByType(LocationType.LAST);
//            if (!mockLatLngList.isEmpty()) {
//                this.loc.setLatitude(mockLatLngList.get(0).getLatitude());
//                this.loc.setLongitude(mockLatLngList.get(0).getLongitude());
//                this.latLng = new LatLng(mockLatLngList.get(0).getLatitude(), mockLatLngList.get(0).getLongitude());
//            }
//        } else {
//            this.loc.setLatitude(latLng.latitude);
//            this.loc.setLongitude(latLng.longitude);
//        }


//        locationManager.setTestProviderEnabled(provider, true);
//        locationManager.setTestProviderStatus(provider, LocationProvider.AVAILABLE, null, time);
        this.locationManager.setTestProviderLocation(provider, this.loc);
//        Settings.Secure.putInt(this.context.getContentResolver(), "mock_location", 1);

    }

    public void setNewLocation(double paramDouble1, double paramDouble2) {
        Location localLocation = new Location("gps");
        localLocation.setLongitude(paramDouble2);
        localLocation.setLatitude(paramDouble1);
        this.loc = localLocation;
        this.handler.removeCallbacks(this);
        this.handler.post(this);
    }
}
