package info.ishared.android.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.*;
import android.location.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import info.ishared.android.AppConfig;
import info.ishared.android.MainActivity;
import info.ishared.android.R;
import info.ishared.android.bean.LocationType;
import info.ishared.android.bean.MockLatLng;
import info.ishared.android.dao.MockLatLngDao;
import info.ishared.android.util.FormatUtils;
import info.ishared.android.util.SystemUtils;

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

    MockLocationThread mockLocationThread;
    NotificationManager notificationManager;
    private String lastMessage;
    private static final int notificationID = 10;
    private MockLatLngDao mockLatLngDao;

    BroadcastReceiver broadcastSettingsChanged = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            updateNotification(lastMessage);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        this.mockLocationThread = new MockLocationThread(getApplicationContext());
        this.notificationManager = ((NotificationManager) getSystemService("notification"));
        mockLatLngDao = new MockLatLngDao(getApplicationContext());
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction(AppConfig.SETTINGS_CHANGED);
        registerReceiver(this.broadcastSettingsChanged, localIntentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        double d1 = 0d;
        double d2 = 0d;
        if (intent == null) {
            Log.d(AppConfig.TAG, "query db...");
            List<MockLatLng> mockLatLngList = this.mockLatLngDao.queryMockLatLngByType(LocationType.LAST);
            if (!mockLatLngList.isEmpty()) {
                d1 = mockLatLngList.get(0).getLatitude();
                d2 = mockLatLngList.get(0).getLongitude();
            }
        } else {
            d1 = intent.getDoubleExtra("latitude", 0.0D);
            d2 = intent.getDoubleExtra("longitude", 0.0D);
        }
        this.mockLocationThread.setNewLocation(d1, d2);
        updateNotification(FormatUtils.formatLatLngNumber(d1) + "," + FormatUtils.formatLatLngNumber(d2));
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        Log.d(AppConfig.TAG, "service onDestroy");
        this.mockLocationThread.unRegister();
        unregisterReceiver(this.broadcastSettingsChanged);
        stopNotification();
        super.onDestroy();
    }

    private void stopNotification() {
        this.notificationManager.cancel(notificationID);
    }

    private void updateNotification(String paramString) {
        this.lastMessage = paramString;
        boolean isServiceRunning = SystemUtils.isServiceWorked(this, "info.ishared.android.service.MockLocationService");
        String notificationText = isServiceRunning ? "筋斗云正在运行" : "筋斗云已经停止";
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.drawable.ic_launcher).setContentTitle(notificationText).setContentText(paramString).setContentIntent(pendingIntent).build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
        notification.flags |= Notification.FLAG_NO_CLEAR; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
        notification.flags |= Notification.FLAG_SHOW_LIGHTS; // set LED on
        // notification.defaults = Notification.DEFAULT_LIGHTS; //默认Notification lights;
        notification.ledARGB = R.color.abs__holo_blue_light; // LED 颜色;
        notification.ledOnMS = 5000; // LED 亮时间
        notificationManager.notify(notificationID, notification);
    }

}
