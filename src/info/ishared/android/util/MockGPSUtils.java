package info.ishared.android.util;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import info.ishared.android.MainActivity;
import info.ishared.android.bean.Preferences;
import info.ishared.android.service.MockLocationService;


/**
 * Created with IntelliJ IDEA.
 * User: Seven
 * Date: 13-4-17
 * Time: PM4:36
 */
public class MockGPSUtils {
    public static LocationManager mLocationManager;
    public static MyNotification mNotification;

    public static void endTestGPS(Context paramContext) {
        if (mLocationManager == null)
            mLocationManager = getTestLocationManager(paramContext);
        if (mNotification == null)
            mNotification = getMyNotification(paramContext);
        mNotification.cancelNotify();
        try {
            mLocationManager.removeTestProvider("gps");
            paramContext.stopService(new Intent(paramContext, MockLocationService.class));
            return;
        } catch (Exception localException) {
            while (true)
                Log.e("MockGPSUtils", localException.getMessage());
        }
    }

    public static MyNotification getMyNotification(Context paramContext) {
        return new MyNotification(paramContext, 1000);
    }

    public static LocationManager getTestLocationManager(Context paramContext) {
        try {
            mLocationManager = (LocationManager) paramContext.getSystemService("location");
            mLocationManager.addTestProvider("gps", false, false, false, false, true, true, true, 0, 5);

        } catch (Exception localException) {
            localException.printStackTrace();
            LocationManager localLocationManager = null;
        }
        return mLocationManager;
    }

    public static void startTestGPS(Context paramContext, String paramString, double paramDouble1, double paramDouble2) {
        startTestGPS(paramContext, paramString, paramDouble1, paramDouble2, Preferences.getSpeed(), Preferences.getAccuracy(), Preferences.getBearing(), true);
    }

    public static void startTestGPS(Context paramContext, String paramString, double paramDouble1, double paramDouble2, float paramFloat1, float paramFloat2, float paramFloat3, boolean paramBoolean) {
        if (mNotification == null) mNotification = getMyNotification(paramContext);
        mNotification.cancelNotify();
        mLocationManager = getTestLocationManager(paramContext);
        if (mLocationManager == null)
            if (!paramBoolean) {
                Intent localIntent2 = new Intent(paramContext, MainActivity.class);
                localIntent2.setFlags(268435456);
                paramContext.startActivity(localIntent2);
                endTestGPS(paramContext);
            }
        while (true) {
            mLocationManager.addTestProvider("gps", false, false, false, false, true, true, true, 0, 5);
            Intent localIntent1 = new Intent(paramContext, MockLocationService.class);
            localIntent1.putExtra("lat", paramDouble1);
            localIntent1.putExtra("lng", paramDouble2);
            localIntent1.putExtra("accuracy", paramFloat2);
            localIntent1.putExtra("bearing", paramFloat3);
            localIntent1.putExtra("speed", paramFloat1);
            paramContext.startService(localIntent1);
            mNotification.startNotify(paramString + ": " + paramDouble1 + ", " + paramDouble2, paramDouble1, paramDouble2);
        }
    }
}
