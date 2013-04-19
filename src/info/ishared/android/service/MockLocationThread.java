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
import java.util.concurrent.TimeUnit;

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
    private Handler handler;
    private Location loc;


    public MockLocationThread(Context paramContext) {
        this.context = paramContext;
        this.handler = new Handler();
        this.locationManager = (LocationManager) paramContext.getSystemService("location");
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
        mockLocation();
        handler.postDelayed(this, 3000L);
    }

    public void unRegister() {
        try {
            this.locationManager.removeTestProvider("gps");
            handler.removeCallbacks(this);
        } catch (Exception localException) {
        }
    }

    private void mockLocation() {
        Log.d(AppConfig.TAG, "mockLocation ......"+this.loc.getLatitude()+","+this.loc.getLongitude());
        this.locationManager.setTestProviderLocation(provider, this.loc);

    }

    public void setNewLocation(double paramDouble1, double paramDouble2) {
        this.loc = new Location(provider);
        this.loc.setLongitude(paramDouble2);
        this.loc.setLatitude(paramDouble1);

        this.loc.setAltitude(65);
        this.loc.setBearing(0);
        this.loc.setSpeed(0);
        this.loc.setAccuracy(1);
        this.loc.setTime(System.currentTimeMillis());
        this.handler.removeCallbacks(this);
        this.handler.post(this);
    }
}
