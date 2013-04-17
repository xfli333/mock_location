package info.ishared.android.bean;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created with IntelliJ IDEA.
 * User: Seven
 * Date: 13-4-17
 * Time: PM4:42
 */
public class Preferences {
        public static final String PREF_ACCURACY = "pref_accuracy";
        public static final String PREF_ALTITUDE = "pref_altitude";
        public static final String PREF_BEARING = "pref_bearing";
        public static final String PREF_FORCE_UPDATE = "pref_update_force";
        public static final String PREF_GPS_OFFSET_X = "pref_gps_offset_x";
        public static final String PREF_GPS_OFFSET_Y = "pref_gps_offset_y";
        public static final String PREF_LAST_LAT = "pref_last_lat";
        public static final String PREF_LAST_LNG = "pref_last_lng";
        public static final String PREF_LAST_VERSION_CODE = "pref_update_last_version_code";
        public static final String PREF_LAST_ZOOM_LEVEL = "pref_last_zoom_level";
        public static final String PREF_LAUNCH_NUM = "pref_launch_num";
        public static final String PREF_LAUNCH_NUM_NEW = "pref_launch_num_new";
        public static final String PREF_MOCK_FLAG = "pref_mock_flag";
        public static final String PREF_MOCK_LAT = "pref_mock_lat";
        public static final String PREF_MOCK_LNG = "pref_mock_lng";
        public static final String PREF_SHORTCUT = "pref_short_cut";
        public static final String PREF_SPEED = "pref_speed";
        public static final String PREF_UPDATE_MESSAGE = "pref_update_msg";
        public static final String PREF_UPDATE_URL = "pref_update_url";
        private static SharedPreferences prefs;

    public static float getAccuracy()
    {
        return prefs.getFloat("pref_accuracy", 5.0F);
    }

    public static long getAltitude()
    {
        return prefs.getLong("pref_altitude", 0L);
    }

    public static float getBearing()
    {
        return prefs.getFloat("pref_bearing", 0.0F);
    }

    public static float getFloatValue(String paramString)
    {
        return prefs.getFloat(paramString, 0.0F);
    }

    public static boolean getForceUpdate()
    {
        return prefs.getBoolean("pref_update_force", false);
    }

    public static String getGPSOffsetX()
    {
        return prefs.getString("pref_gps_offset_x", "150");
    }

    public static String getGPSOffsetY()
    {
        return prefs.getString("pref_gps_offset_y", "710");
    }

    public static int getLastLat()
    {
        return prefs.getInt("pref_last_lat", 0);
    }

    public static int getLastLng()
    {
        return prefs.getInt("pref_last_lng", 0);
    }

    public static int getLastVersionCode()
    {
        return prefs.getInt("pref_update_last_version_code", 0);
    }

    public static int getLastZoomLevel()
    {
        return prefs.getInt("pref_last_zoom_level", 15);
    }

    public static int getLaunchNum()
    {
        return prefs.getInt("pref_launch_num_new", 0);
    }

    public static long getLongValue(String paramString)
    {
        return prefs.getLong(paramString, 0L);
    }

    public static boolean getMockFlag()
    {
        return prefs.getBoolean("pref_mock_flag", false);
    }

    public static float getMockLat()
    {
        return prefs.getFloat("pref_mock_lat", 0.0F);
    }

    public static float getMockLng()
    {
        return prefs.getFloat("pref_mock_lng", 0.0F);
    }

    public static boolean getShortCut()
    {
        return prefs.getBoolean("pref_short_cut", false);
    }

    public static float getSpeed()
    {
        return prefs.getFloat("pref_speed", 10.0F);
    }

    public static String getStringValue(String paramString)
    {
        return prefs.getString(paramString, "");
    }

    public static String getUpdateMsg()
    {
        return prefs.getString("pref_update_msg", "");
    }

    public static String getUpdateUrl()
    {
        return prefs.getString("pref_update_url", "");
    }

    public static void init(Application paramApplication)
    {
        prefs = paramApplication.getSharedPreferences("testgpsprefs", 0);
    }

    private static void put(String paramString, Object paramObject)
    {
        SharedPreferences.Editor localEditor = prefs.edit();
        if (paramObject.getClass() == Boolean.class)
            localEditor.putBoolean(paramString, ((Boolean)paramObject).booleanValue());
        if (paramObject.getClass() == String.class)
            localEditor.putString(paramString, (String)paramObject);
        if (paramObject.getClass() == Integer.class)
            localEditor.putInt(paramString, ((Integer)paramObject).intValue());
        if (paramObject.getClass() == Float.class)
            localEditor.putFloat(paramString, ((Float)paramObject).intValue());
        if (paramObject.getClass() == Long.class)
            localEditor.putLong(paramString, ((Long)paramObject).intValue());
        localEditor.commit();
    }

    public static void putAccuracy(float paramFloat)
    {
        put("pref_accuracy", Float.valueOf(paramFloat));
    }

    public static void putAltitude(long paramLong)
    {
        put("pref_altitude", Long.valueOf(paramLong));
    }

    public static void putBearing(float paramFloat)
    {
        put("pref_bearing", Float.valueOf(paramFloat));
    }

    public static void putForceUpdate(boolean paramBoolean)
    {
        put("pref_update_force", Boolean.valueOf(paramBoolean));
    }

    public static void putGPSOffsetX(String paramString)
    {
        put("pref_gps_offset_x", paramString);
    }

    public static void putGPSOffsetY(String paramString)
    {
        put("pref_gps_offset_y", paramString);
    }

    public static void putLastLat(int paramInt)
    {
        put("pref_last_lat", Integer.valueOf(paramInt));
    }

    public static void putLastLng(int paramInt)
    {
        put("pref_last_lng", Integer.valueOf(paramInt));
    }

    public static void putLastVersionCode(int paramInt)
    {
        put("pref_update_last_version_code", Integer.valueOf(paramInt));
    }

    public static void putLastZoomLevel(int paramInt)
    {
        put("pref_last_zoom_level", Integer.valueOf(paramInt));
    }

    public static void putLaunchNum(int paramInt)
    {
        put("pref_launch_num_new", Integer.valueOf(paramInt));
    }

    public static void putMockFlag(boolean paramBoolean)
    {
        put("pref_mock_flag", Boolean.valueOf(paramBoolean));
    }

    public static void putMockLat(float paramFloat)
    {
        put("pref_mock_lat", Float.valueOf(paramFloat));
    }

    public static void putMockLng(float paramFloat)
    {
        put("pref_mock_lng", Float.valueOf(paramFloat));
    }

    public static void putShortCut(boolean paramBoolean)
    {
        put("pref_short_cut", Boolean.valueOf(paramBoolean));
    }

    public static void putSpeed(float paramFloat)
    {
        put("pref_speed", Float.valueOf(paramFloat));
    }

    public static void putUpdateMsg(String paramString)
    {
        put("pref_update_msg", paramString);
    }

    public static void putUpdateUrl(String paramString)
    {
        put("pref_update_url", paramString);
    }
}
