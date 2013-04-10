package info.ishared.android;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
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

public class MainActivity extends SherlockMapActivity {
    /**
     * Called when the activity is first created.
     */

    GoogleMap mMap;

    private Marker previousMarker;

    private Handler mHandler;

    PopupWindow mPopupWindow;

    private ListView listView;

    private String title[] = {"全部", "我的微博", "周边", "智能排版", "同学"};
    LayoutInflater layoutInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mHandler = new Handler();


        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.66, 104.07), 6));
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_set:
                Toast.makeText(this, previousMarker.getSnippet(), Toast.LENGTH_SHORT).show();
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
            mPopupWindow = new PopupWindow(menu_view, 200, 300);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);
            listView = (ListView) menu_view.findViewById(R.id.lv_dialog);
            listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.text, R.id.tv_text, title));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    mPopupWindow.dismiss();
                    mPopupWindow = null;
                }
            });


            mPopupWindow.showAsDropDown(view, 10, 0);

        }
    }

}
