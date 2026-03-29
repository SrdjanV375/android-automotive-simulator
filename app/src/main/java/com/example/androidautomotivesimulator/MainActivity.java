package com.example.androidautomotivesimulator;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidautomotivesimulator.R;


import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView speedText, batteryText;
    private ProgressBar progressBar;
    private MapView mapView;

    private final Handler handler = new Handler();
    private final NativeBridge nativeBridge = new NativeBridge();

    private int speed = 0;
    private int battery = 100;

    private List<GeoPoint> route = new ArrayList<>();
    private int routeIndex = 0;

    private Marker vehicleMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        routeIndex = 0;

        speedText = findViewById(R.id.speedText);
        batteryText = findViewById(R.id.batteryText);
        progressBar = findViewById(R.id.progressBar);
        mapView = findViewById(R.id.map);

        Configuration.getInstance().setUserAgentValue(getPackageName());
        Configuration.getInstance().setOsmdroidBasePath(new File(getCacheDir(), "osmdroid"));
        Configuration.getInstance().setOsmdroidTileCache(new File(getCacheDir(), "tile"));

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        createOfflineRoute();
        drawRoute();

        vehicleMarker = new Marker(mapView);
        vehicleMarker.setPosition(route.get(0));
        vehicleMarker.setTitle("Vehicle");
        mapView.getOverlayManager().add(vehicleMarker);

        handler.post(updateRunnable);
    }

    private void createOfflineRoute() {
        route.add(new GeoPoint(44.787197, 20.457273));
        route.add(new GeoPoint(44.82, 20.40));
        route.add(new GeoPoint(44.90, 20.20));
        route.add(new GeoPoint(45.02, 20.00));
        route.add(new GeoPoint(45.12, 19.90));
        route.add(new GeoPoint(45.20, 19.87));
        route.add(new GeoPoint(45.2671, 19.8335));
    }

    private void drawRoute() {
        Polyline polyline = new Polyline();
        polyline.setPoints(route);
        mapView.getOverlayManager().add(polyline);
    }

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {

            speed += nativeBridge.getSpeedDelta();
            if (speed < 0) speed = 0;
            if (speed > 120) speed = 120;

            battery += nativeBridge.getBatteryDelta();
            if (battery < 5) battery = 5;

            speedText.setText("Speed: " + speed + " km/h");
            batteryText.setText("Battery: " + battery + "%");
            progressBar.setProgress(speed);

            if (routeIndex < route.size()) {
                GeoPoint currentPos = vehicleMarker.getPosition();
                GeoPoint targetPos = route.get(routeIndex);

                double speedMS = speed / 3.6;
                double distanceStep = speedMS * 5.0;
                double distance = currentPos.distanceToAsDouble(targetPos);

                if (distanceStep >= distance) {
                    vehicleMarker.setPosition(targetPos);
                    routeIndex++;
                } else {
                    double latStep = (targetPos.getLatitude() - currentPos.getLatitude()) * (distanceStep / distance);
                    double lonStep = (targetPos.getLongitude() - currentPos.getLongitude()) * (distanceStep / distance);

                    double newLat = currentPos.getLatitude() + latStep;
                    double newLon = currentPos.getLongitude() + lonStep;

                    vehicleMarker.setPosition(new GeoPoint(newLat, newLon));
                    Log.d("Vehicle", "Speed: " + speed + " km/h, Pos: " + newLat + "," + newLon);

                }

                mapView.getController().setCenter(vehicleMarker.getPosition());
            }else{

                speed = 0;
                Log.d("Vehicle", "Arrived at destination, stopping updates.");
                handler.removeCallbacks(updateRunnable);
                return;

            }

            mapView.invalidate();
            handler.postDelayed(this, 1000);
        }
    };

}
