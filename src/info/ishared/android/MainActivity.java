package info.ishared.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import info.ishared.android.util.FormatUtils;
import info.ishared.android.util.SystemUtils;

public class MainActivity extends SherlockMapActivity {
    /**
     * Called when the activity is first created.
     */

    GoogleMap mMap;

    private Marker previousMarker;

    private Handler mHandler;

    PopupWindow mPopupWindow;

    private ListView listView;

    private String title[] = {"设置当前位置", "收藏", "查看收藏", "退出"};
    LayoutInflater layoutInflater;

    MainController mainController;

    private LatLng defaultLatLng;

    int notificationID = 10;
    NotificationManager notificationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mainController = new MainController(this);
        mHandler = new Handler();
        defaultLatLng = this.mainController.getLastMockLocation();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationID);
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        if (defaultLatLng != null) {
            previousMarker = mMap.addMarker(new MarkerOptions().draggable(true).position(defaultLatLng).title("坐标:").snippet(FormatUtils.formatLatLngNumber(defaultLatLng.latitude) + "," + FormatUtils.formatLatLngNumber(defaultLatLng.longitude)));
            previousMarker.showInfoWindow();
        } else {
            defaultLatLng = new LatLng(30.66, 104.07);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 6));


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (previousMarker != null) previousMarker.remove();
                previousMarker = mMap.addMarker(new MarkerOptions().draggable(true).position(latLng).title("坐标:").snippet(FormatUtils.formatLatLngNumber(latLng.latitude) + "," + FormatUtils.formatLatLngNumber(latLng.longitude)));
                previousMarker.showInfoWindow();
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                marker.hideInfoWindow();
                marker.setSnippet(FormatUtils.formatLatLngNumber(marker.getPosition().latitude) + "," + FormatUtils.formatLatLngNumber(marker.getPosition().longitude));
                marker.showInfoWindow();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
            }
        });

//        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                marker.remove();
//            }
//        });
    }


    private void initNotification() {

        boolean isServiceRunning = SystemUtils.isServiceWorked(this, "info.ishared.android.service.MockLocationService");
            String notificationText = isServiceRunning ? "筋斗云正在运行" : "筋斗云已经停止";

            // Create the notification
            Notification notification = new Notification(R.drawable.ic_launcher, notificationText,
                    System.currentTimeMillis());
            notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
            notification.flags |= Notification.FLAG_NO_CLEAR; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
            notification.flags |= Notification.FLAG_SHOW_LIGHTS; // set LED on
            // notification.defaults = Notification.DEFAULT_LIGHTS; //默认Notification lights;
            notification.ledARGB = R.color.abs__holo_blue_light; // LED 颜色;
            notification.ledOnMS = 5000; // LED 亮时间

            // Create the notification expanded message
            // When the user clicks on it, it opens your activity
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            notification.setLatestEventInfo(this, notificationText, FormatUtils.formatLatLngNumber(defaultLatLng.latitude) + "," + FormatUtils.formatLatLngNumber(defaultLatLng.longitude), pendingIntent);
            // Show notification
            notificationManager.notify(notificationID, notification);
        }


        @Override
        protected boolean isRouteDisplayed () {
            return false;
        }


        @Override
        public void onBackPressed () {
            initNotification();
            super.onBackPressed();
//        this.finish();
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){

            switch (item.getItemId()) {
                case R.id.menu_set:
                    Toast.makeText(this, previousMarker.getSnippet(), Toast.LENGTH_SHORT).show();
                    mainController.startMockLocation(previousMarker.getPosition());
                    break;
                case R.id.menu_more:
//                    mainController.stopMockLocationService();
                showPopupWindow(this.findViewById(R.id.menu_more));
                    break;

            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getSupportMenuInflater().inflate(R.menu.activity_main, menu);
            return super.onCreateOptionsMenu(menu);
        }


    private void showPopupWindow(View view) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        } else {
            layoutInflater = getLayoutInflater();
            View menu_view = layoutInflater.inflate(R.layout.pop_menu, null);
            mPopupWindow = new PopupWindow(menu_view, 200, 240);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable() );
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);
            listView = (ListView) menu_view.findViewById(R.id.lv_dialog);
            listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.text, R.id.tv_text, title));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    mPopupWindow.dismiss();
                    mPopupWindow = null;

                    switch (arg2){
                        case 3:
                            mainController.stopMockLocationService();
                            notificationManager.cancel(notificationID);
                            MainActivity.this.finish();
                            break;
                    }



                }
            });


            mPopupWindow.showAsDropDown(view, 10, 0);

        }
    }

    public Marker getPreviousMarker() {
        return previousMarker;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d(AppConfig.TAG,"onTouchEvent");
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        return super.onTouchEvent(event);
    }
}
