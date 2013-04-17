package info.ishared.android.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import info.ishared.android.MainActivity;

/**
 * Created with IntelliJ IDEA.
 * User: Seven
 * Date: 13-4-17
 * Time: PM4:38
 */
public class MyNotification {
    public Context mContext;
    public Notification mNotification;
    private int mNotificationID;
    public NotificationManager mNotificationManager;

    public MyNotification(Context paramContext, int paramInt)
    {
        this.mContext = paramContext;
        this.mNotificationManager = ((NotificationManager)paramContext.getSystemService("notification"));
        this.mNotification = new Notification(2130837520, paramContext.getString(2131034112), System.currentTimeMillis());
        this.mNotification.flags = 32;
        Notification localNotification = this.mNotification;
        localNotification.defaults = (0x4 | localNotification.defaults);
        this.mNotification.setLatestEventInfo(paramContext, paramContext.getString(2131034112), paramContext.getString(2131034112), null);
        this.mNotificationID = paramInt;
    }

    public void cancelNotify()
    {
        this.mNotificationManager.cancel(this.mNotificationID);
    }

    public void startNotify(String paramString, double paramDouble1, double paramDouble2)
    {
        this.mNotification = new Notification(2130837520, paramString, System.currentTimeMillis());
        this.mNotification.flags = 32;
        Notification localNotification = this.mNotification;
        localNotification.defaults = (0x4 | localNotification.defaults);
        this.mNotification.setLatestEventInfo(this.mContext, this.mContext.getString(2131034112), paramString, null);
        Intent localIntent = new Intent(this.mContext, MainActivity.class);
        localIntent.putExtra("lat", paramDouble1);
        localIntent.putExtra("lng", paramDouble2);
        localIntent.setFlags(268435456);
        PendingIntent localPendingIntent = PendingIntent.getActivity(this.mContext, 0, localIntent, 134217728);
        this.mNotification.contentIntent = localPendingIntent;
        this.mNotificationManager.notify(this.mNotificationID, this.mNotification);
    }

    public void updateMessage(String paramString)
    {
        this.mNotification.setLatestEventInfo(this.mContext, this.mContext.getString(2131034112), paramString, null);
        this.mNotificationManager.notify(this.mNotificationID, this.mNotification);
    }
}
