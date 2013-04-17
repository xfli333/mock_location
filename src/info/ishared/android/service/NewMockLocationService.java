package info.ishared.android.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import info.ishared.android.bean.LocationType;
import info.ishared.android.bean.MockLatLng;
import info.ishared.android.dao.MockLatLngDao;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seven
 * Date: 13-4-17
 * Time: PM5:42
 */
public class NewMockLocationService extends Service {
    private static final String LOG_TAG = "MockLocationProvider";
    private float accuracy;
    private double altitude;
    private float bearing;
    private Bundle bl;
    private boolean forFlag = true;
    private Handler handler = new Handler();
    private double lat;
    private double lng;

    private LatLng latLng;
    private LocationManager mLocationManager;
    private MockLatLngDao mockLatLngDao;
    private Runnable runnable = new Runnable() {
        public void run() {
            try {
                NewMockLocationService.this.mLocationManager.sendExtraCommand("gps", "force_xtra_injection", NewMockLocationService.this.bl);
                NewMockLocationService.this.mLocationManager.sendExtraCommand("gps", "force_time_injection", NewMockLocationService.this.bl);
                Location localLocation = NewMockLocationService.this.getLoc("gps");
                NewMockLocationService.this.mLocationManager.setTestProviderLocation("gps", localLocation);

                latLng = getMockLatLng();

                Log.d("mock", "gps: " + latLng.latitude + "," + latLng.longitude);
                NewMockLocationService.this.handler.postDelayed(this, 1000L);
                return;
            } catch (Exception localException) {
                while (true)
                    localException.printStackTrace();
            }
        }
    };
    private float speed;

    private Location getLoc(String paramString) {
        Location localLocation = new Location(paramString);
        localLocation.setLatitude(this.lat);
        localLocation.setLongitude(this.lng);
        localLocation.setAltitude(this.altitude);
        localLocation.setBearing(this.bearing);
        localLocation.setSpeed(this.speed);
        localLocation.setAccuracy(this.accuracy);
        localLocation.setTime(System.currentTimeMillis());
        return localLocation;
    }

    private void removeProvider() {
        try {
            this.mLocationManager.removeTestProvider("gps");
            return;
        } catch (Exception localException) {
            while (true)
                Log.e("MockLocationProvider", localException.getMessage());
        }
    }

    public IBinder onBind(Intent paramIntent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
        removeProvider();
        try {
            this.handler.removeCallbacks(this.runnable);
            return;
        } catch (Exception localException) {
            while (true)
                localException.printStackTrace();
        }
    }

    private LatLng getMockLatLng() {
        if (this.latLng == null) {
            List<MockLatLng> mockLatLngList = this.mockLatLngDao.queryMockLatLngByType(LocationType.LAST);
            if (!mockLatLngList.isEmpty()) {
                this.latLng = new LatLng(mockLatLngList.get(0).getLatitude(), mockLatLngList.get(0).getLongitude());
            }
        }
        return this.latLng;

    }

    public void onStart(Intent paramIntent, int paramInt) {
        super.onStart(paramIntent, paramInt);
    }

    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        this.mockLatLngDao = new MockLatLngDao(this);
        this.mLocationManager = ((LocationManager) getSystemService("location"));
        this.mLocationManager.addTestProvider("gps", false, false, false, false, true, true, true, 0, 5);
        this.mLocationManager.setTestProviderEnabled("gps", true);
        this.bl = paramIntent.getExtras();
        if (this.bl != null) {
            if (this.bl.containsKey("lat"))
                this.lat = paramIntent.getDoubleExtra("lat", 0.0D);
            if (this.bl.containsKey("lng"))
                this.lng = paramIntent.getDoubleExtra("lng", 0.0D);
            if (this.bl.containsKey("altitude"))
                this.altitude = paramIntent.getDoubleExtra("altitude", 0.0D);
            if (this.bl.containsKey("bearing"))
                this.bearing = paramIntent.getFloatExtra("bearing", 0.0F);
            if (this.bl.containsKey("speed"))
                this.speed = paramIntent.getFloatExtra("speed", 0.0F);
            if (this.bl.containsKey("accuracy"))
                this.accuracy = paramIntent.getFloatExtra("accuracy", 0.0F);
            this.handler.postDelayed(this.runnable, 1000L);
        }
        return 3;
    }

}
