package info.ishared.android.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import com.google.android.gms.maps.model.LatLng;
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

    private MockLatLngDao mockLatLngDao;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mockLatLngDao = new MockLatLngDao(this);
        if (locationManager.getProvider(provider) == null) {
            locationManager.addTestProvider(provider, false, true, false, false, false, false, false, 0, 5);
            locationManager.setTestProviderEnabled(provider, true);
        }

        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        mockLocation(getLatLng());
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }

    private void mockLocation(LatLng latLng) {
        Location loc = new Location(provider);
        loc.setTime(System.currentTimeMillis());
        if(latLng ==null){
            List<MockLatLng> mockLatLngList= this.mockLatLngDao.queryMockLatLngByType(LocationType.LAST);
            if(!mockLatLngList.isEmpty()){
                loc.setLatitude(mockLatLngList.get(0).getLatitude());
                loc.setLongitude(mockLatLngList.get(0).getLongitude());
                latLng=new LatLng(mockLatLngList.get(0).getLatitude(),mockLatLngList.get(0).getLongitude());
            }
        }else{
            loc.setLatitude(latLng.latitude);
            loc.setLongitude(latLng.longitude);
        }
        locationManager.setTestProviderLocation(provider, loc);
    }

    public static LatLng getLatLng() {
        return latLng;
    }
}
