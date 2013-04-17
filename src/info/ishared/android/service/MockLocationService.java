package info.ishared.android.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.*;
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

    private Handler handler;
    MockLocationThread mockLocationThread;
    BroadcastReceiver broadcastSettingsChanged = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        handler = new Handler();
        mockLocationThread = new MockLocationThread(getApplicationContext(), handler);
        handler.postDelayed(mockLocationThread, 3000);

    }

    @Override
    public void onDestroy() {
        Log.d(AppConfig.TAG, "service onDestroy");
        mockLocationThread.unRegister();
        handler.removeCallbacks(mockLocationThread);
        super.onDestroy();
    }

}
