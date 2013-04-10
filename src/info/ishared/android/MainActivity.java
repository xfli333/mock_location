package info.ishared.android;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import info.ishared.android.util.FormatUtils;

public class MainActivity extends SherlockMapActivity {
    /**
     * Called when the activity is first created.
     */

    GoogleMap mMap;

    private Marker previousMarker;

    private Handler mHandler;

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


}
