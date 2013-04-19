package info.ishared.android;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import info.ishared.android.bean.MockLatLng;
import info.ishared.android.util.AlertDialogUtils;
import info.ishared.android.util.FormatUtils;
import info.ishared.android.util.ToastUtils;

import java.util.*;

public class MainActivity extends SherlockMapActivity {
    /**
     * Called when the activity is first created.
     */

    private static final int MOVE = 0;
    private static final int DELETE = 1;


    GoogleMap mMap;

    private Marker previousMarker;

    private Handler mHandler;

    PopupWindow mPopupWindow;

    private ListView listView;

    private String title[] = {"停止模拟", "收藏位置", "查看收藏", "帮助说明", "退出程序"};
    LayoutInflater layoutInflater;

    MainController mainController;

    private LatLng defaultLatLng;

    private ListView mFavListView;
    private Dialog mFavDialog;
    protected SimpleAdapter adapter;
    protected List<Map<String, String>> favLocationData = new ArrayList<Map<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mainController = new MainController(this);
        mHandler = new Handler();
        defaultLatLng = this.mainController.getLastMockLocation();
        mFavDialog = new Dialog(MainActivity.this);
        mFavDialog.setContentView(R.layout.dialog_fav_list_view);
        mFavDialog.setCancelable(true);
        mFavDialog.setTitle("收藏列表");
        mFavListView=(ListView)mFavDialog.findViewById(R.id.fav_location_list_view);

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

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Long id = Long.valueOf(favLocationData.get(menuInfo.position).get("id"));
        Double latitude =  Double.valueOf(favLocationData.get(menuInfo.position).get("latitude"));
        Double longitude =  Double.valueOf(favLocationData.get(menuInfo.position).get("longitude"));
        switch (item.getItemId()) {
            case MOVE:
                LatLng latLng =new LatLng(latitude,longitude);
                previousMarker = mMap.addMarker(new MarkerOptions().draggable(true).position(latLng).title("坐标:").snippet(FormatUtils.formatLatLngNumber(latitude) + "," + FormatUtils.formatLatLngNumber(longitude)));
                previousMarker.showInfoWindow();
                break;
            case DELETE:
                mainController.deleteFavLocation(id);
                adapter.notifyDataSetChanged();
                ToastUtils.showMessage(this,"删除成功");
                mFavDialog.cancel();
                break;
            default:
                break;
        }

        return false;
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

    private void showFavLocation() {

        initListViewData();
        initListViewGUI();
        mFavDialog.show();
    }

    private void initListViewGUI() {
        adapter = new SimpleAdapter(this, favLocationData, R.layout.fav_location_list_item, new String[]{"name", "location"}, new int[]{R.id.fav_name, R.id.fav_location}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position % 2 != 0)
                    view.setBackgroundResource(R.drawable.table_background_selector);
                else
                    view.setBackgroundResource(R.drawable.table_background_alternate_selector);
                return view;
            }
        };
        mFavListView.setAdapter(adapter);
        mFavListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, MOVE, 0, "移到该地点");
                menu.add(0, DELETE, 0, "删除该地点");

            }
        });
    }

    private void initListViewData() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                favLocationData.clear();
                List<MockLatLng> favLocationList = mainController.getAllFavLocation();
                Collections.sort(favLocationList, new Comparator<MockLatLng>() {
                    @Override
                    public int compare(MockLatLng lhs, MockLatLng  rhs) {
                        return lhs.getFavName().compareTo(rhs.getFavName());
                    }
                });
                for (MockLatLng mockLatLng : favLocationList) {
                    Map<String, String> map = new HashMap<String, String>(2);
                    map.put("id", mockLatLng.getId()+"");
                    map.put("name", mockLatLng.getFavName());
                    map.put("location", FormatUtils.formatLatLngNumber(mockLatLng.getLatitude()) + "," + FormatUtils.formatLatLngNumber(mockLatLng.getLongitude()));
                    map.put("latitude",mockLatLng.getLatitude().toString());
                    map.put("longitude",mockLatLng.getLongitude().toString());
                    favLocationData.add(map);
                }
                adapter.notifyDataSetChanged();
            }
        });

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
                        AlertDialogUtils.showInputDialog(MainActivity.this, previousMarker.getSnippet(), new AlertDialogUtils.CallBack() {
                            @Override
                            public void execute(Object... obj) {
                                mainController.favCurrentLocation(obj[0].toString(), previousMarker.getPosition());
                                ToastUtils.showMessage(MainActivity.this, "收藏成功");
                            }
                        });
                    }
                    break;
                case 2:

                    showFavLocation();

                    break;
                case 4:
                    mainController.stopMockLocationService();
                    MainActivity.this.finish();
                    break;
            }

        }
    }
}
