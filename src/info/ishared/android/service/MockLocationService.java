package info.ishared.android.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import info.ishared.android.AppConfig;
import info.ishared.android.bean.LocationType;
import info.ishared.android.bean.MockLatLng;
import info.ishared.android.dao.MockLatLngDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Seven
 * Date: 13-4-11
 * Time: PM12:23
 */
public class MockLocationService extends Service {

    LocationManager locationManager;

    String provider = LocationManager.GPS_PROVIDER;
    private ExecutorService executor = Executors.newFixedThreadPool(1);

    public static LatLng latLng;
    private boolean flag = true;

    private MockLatLngDao mockLatLngDao;
    private Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        mockLatLngDao = new MockLatLngDao(this);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setAltitudeRequired(false);
//        criteria.setBearingRequired(false);
//        criteria.setCostAllowed(true);
//        criteria.setPowerRequirement(Criteria.POWER_LOW);
//        provider = locationManager.getBestProvider(criteria, true);
        if (locationManager.getProvider(provider) != null) {
            locationManager.removeTestProvider(provider);
        }

        locationManager.addTestProvider(provider, true, //requiresNetwork,
                false, // requiresSatellite,
                true, // requiresCell,
                false, // hasMonetaryCost,
                false, // supportsAltitude,
                false, // supportsSpeed, s
                false, // upportsBearing,
                Criteria.POWER_LOW, // powerRequirement
                Criteria.ACCURACY_FINE); // accuracy


        handler = new Handler();
        handler.postDelayed(locationUpdateThread, 1000);

//        Location loc = new Location(provider);
//        loc.setLatitude(30);
//        loc.setLongitude(100);
//
//        loc.setTime(System.currentTimeMillis());
////        Log.d(LOG_TAG, "Sending update for " + provider);
//        locationManager.setTestProviderLocation(provider, loc);

//        mockLocation(getLatLng());
    }


    Thread locationUpdateThread = new Thread() {

        public void run() {
            Log.d(AppConfig.TAG, "locationUpdateThread onDestroy");

            mockLocation(getLatLng());
//            Location loc = new Location(provider);
//            loc.setLatitude(30);
//            loc.setLongitude(100);
//
//            loc.setTime(System.currentTimeMillis());
//            locationManager.setTestProviderLocation(provider, loc);
            handler.postDelayed(this, 1000);
        };

    };

//    @Override
    public void oanCreate() {
        super.onCreate();
        flag = true;
        Log.d(AppConfig.TAG, "service onCreate");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        provider = locationManager.getBestProvider(criteria, true);
        if(provider == null) provider=LocationManager.GPS_PROVIDER;
//        LocationListener locationListener = new LocationListener() {
//            public void onLocationChanged(Location location) {
//                // Called when a new location is found by the network location provider.
////                makeUseOfNewLocation(location);
//                Log.d(AppConfig.TAG, "onLocationChanged");
//            }
//
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//                Log.d(AppConfig.TAG, "onStatusChanged");
//            }
//
//            public void onProviderEnabled(String provider) {
//                Log.d(AppConfig.TAG, "onProviderEnabled");
//            }
//
//            public void onProviderDisabled(String provider) {
//                Log.d(AppConfig.TAG, "onProviderDisabled");
//            }
//        };
//
//// Register the listener with the Location Manager to receive location updates
//        locationManager.requestLocationUpdates(provider, 0, 0, locationListener);



        mockLatLngDao = new MockLatLngDao(this);


        if (locationManager.getProvider(provider) != null) {
            locationManager.removeTestProvider(provider);
        }

        if (locationManager.getProvider(provider) == null) {
            locationManager.addTestProvider(provider, false, true, false, false, false, false, false, 1, 1);
            locationManager.setTestProviderEnabled(provider, true);
        }

//
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                while (flag) {
//                    try {
//                        mockLocation(getLatLng());
//                        TimeUnit.SECONDS.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
    }

    @Override
    public void onDestroy() {
        Log.d(AppConfig.TAG, "service onDestroy");
        handler.removeCallbacks(locationUpdateThread);
        locationManager.setTestProviderEnabled(provider, false);
        locationManager.removeTestProvider(provider);
        super.onDestroy();
//        flag = false;
//        executor.shutdownNow();
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

            locationManager.setTestProviderLocation(provider, loc);

    }

    public static LatLng getLatLng() {
        return latLng;
    }
}
