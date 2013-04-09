package info.ishared.android;

import android.app.Activity;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.google.android.maps.MapView;

public class MainActivity extends SherlockMapActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//        MapView mapView = (MapView)findViewById(R.id.map);
//        mapView.setBuiltInZoomControls(true);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
