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
import android.provider.Settings;
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
import info.ishared.android.service.MockLocationService;
import info.ishared.android.util.AlertDialogUtils;
import info.ishared.android.util.FormatUtils;
import info.ishared.android.util.SystemUtils;
import info.ishared.android.util.ToastUtils;

public class MainActivity extends SherlockMapActivity {
    /**
     * Called when the activity is first created.
     */

    GoogleMap mMap;

    private Marker previousMarker;

    private Handler mHandler;

    PopupWindow mPopupWindow;

    private ListView listView;

    private String title[] = {"停止模拟", "收藏位置", "查看收藏", "帮助说明", "退出程序"};
    LayoutInflater layoutInflater;

    MainController mainController;

    private LatLng defaultLatLng;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mainController = new MainController(this);
        mHandler = new Handler();
        defaultLatLng = this.mainController.getLastMockLocation();
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

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }


    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_set:
                if (previousMarker == null) {
                    AlertDialogUtils.showConfirmDiaLog(this, "请先设置一个要模拟的位置.");
                } else {
                    mainController.startMockLocation(previousMarker.getPosition());
                }
                break;
            case R.id.menu_more:
                showPopupWindow(this.findViewById(R.id.menu_more));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            mPopupWindow = new PopupWindow(menu_view, 200, 285);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);
            listView = (ListView) menu_view.findViewById(R.id.lv_dialog);
            listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.text, R.id.tv_text, title));
            listView.setOnItemClickListener(new MyOnItemClickListener());
            mPopupWindow.showAsDropDown(view, 10, 0);

        }
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
            switch (position) {
                case 0:
                    mainController.stopMockLocationService();
                    ToastUtils.showMessage(MainActivity.this, "停止模拟位置");
                    break;
                case 1:
                    if (previousMarker != null) {
                        AlertDialogUtils.showInputDialog(MainActivity.this,previousMarker.getSnippet(), new AlertDialogUtils.CallBack() {
                            @Override
                            public void execute(Object... obj) {
                                ToastUtils.showMessage(MainActivity.this, obj[0].toString());
                            }
                        });
                    }
                    break;
                case 4:
                    mainController.stopMockLocationService();
                    MainActivity.this.finish();
                    break;
            }

        }
    }
}
