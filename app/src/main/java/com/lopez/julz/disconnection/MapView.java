package com.lopez.julz.disconnection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonParser;
import com.lopez.julz.disconnection.dao.AppDatabase;
import com.lopez.julz.disconnection.dao.DisconnectionList;
import com.lopez.julz.disconnection.helpers.ObjectHelpers;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapView extends AppCompatActivity implements PermissionsListener, OnMapReadyCallback, LocationListener {

    public com.mapbox.mapboxsdk.maps.MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private LocationComponent locationComponent;
    public Style style;
    public SymbolManager symbolManager;

    public FloatingActionButton backBtnReading;

    public AppDatabase db;

    public String scheduleId, userId;

    public List<DisconnectionList> disconnectionLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getResources().getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map_view);

        db = Room.databaseBuilder(this, AppDatabase.class, ObjectHelpers.dbName()).fallbackToDestructiveMigration().build();

        scheduleId = getIntent().getExtras().getString("SCHEDULEID");
        userId = getIntent().getExtras().getString("USERID");

        mapView = findViewById(R.id.mapviewReading);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        backBtnReading = findViewById(R.id.backBtnReading);
        disconnectionLists = new ArrayList<>();

        backBtnReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        try {
            this.mapboxMap = mapboxMap;
            mapboxMap.setStyle(new Style.Builder()
                    .fromUri(getResources().getString(R.string.mapbox_style)), new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    setStyle(style);

                    enableLocationComponent(style);

                    new GetData().execute();
                }
            });
        } catch (Exception e) {
            Log.e("ERR_INIT_MAPBOX", e.getMessage());
        }
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        try {
            // Check if permissions are enabled and if not request
            if (PermissionsManager.areLocationPermissionsGranted(this)) {

                // Get an instance of the component
                locationComponent = mapboxMap.getLocationComponent();

                // Activate with options
                locationComponent.activateLocationComponent(
                        LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

                // Enable to make component visible
                locationComponent.setLocationComponentEnabled(true);

                // Set the component's camera mode
                locationComponent.setCameraMode(CameraMode.TRACKING);

                // Set the component's render mode
                locationComponent.setRenderMode(RenderMode.COMPASS);

            } else {
                permissionsManager = new PermissionsManager(this);
                permissionsManager.requestLocationPermissions(this);
            }
        } catch (Exception e) {
            Log.e("ERR_LOAD_MAP", e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            int res = checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE);
            if (res != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE}, 123);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public class GetData extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (symbolManager != null) {
                symbolManager.deleteAll();
            }
            symbolManager = new SymbolManager(mapView, mapboxMap, style);

            symbolManager.setIconAllowOverlap(true);
            symbolManager.setTextAllowOverlap(true);
            disconnectionLists.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                disconnectionLists.addAll(db.disconnectionListDao().getAllMapView(scheduleId));

                int size = disconnectionLists.size();
                for (int i=0; i<size; i++) {
                    publishProgress(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            DisconnectionList disconnectionList = disconnectionLists.get(values[0]);

            if (disconnectionList != null) {
                if (hasLatLong(disconnectionList)) {
                    String split[] = disconnectionList.getAccountCoordinates().split(",");
                    SymbolOptions symbolOptions;
                    if (split.length == 2) {
                        if ((Object)disconnectionList.getUploadStatus() == null) {
                            Log.e("TEST", disconnectionList.getId() + " - " + disconnectionList.getUploadStatus() + " BLUE");
                            symbolOptions = new SymbolOptions()
                                    .withLatLng(new LatLng(Double.valueOf(split[0]), Double.valueOf(split[1])))
                                    .withData(new JsonParser().parse("{" +
                                            "'acctNo' : '" + disconnectionList.getAccountNumber() + "'," +
                                            "'scheduleId' : '" + scheduleId + "'}"))
                                    .withIconImage("place-black-24dp")
                                    .withIconSize(1.2f);
                        } else {
                            Log.e("TEST", disconnectionList.getId() + " - " + disconnectionList.getUploadStatus() + " RED");
                            symbolOptions = new SymbolOptions()
                                    .withLatLng(new LatLng(Double.valueOf(split[0]), Double.valueOf(split[1])))
                                    .withData(new JsonParser().parse("{" +
                                            "'acctNo' : '" + disconnectionList.getAccountNumber() + "'," +
                                            "'scheduleId' : '" + scheduleId + "'}"))
                                    .withIconImage("location-green")
                                    .withIconSize(1f);
                        }
                        Symbol symbol = symbolManager.create(symbolOptions);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            try {
                if (symbolManager != null) {
                    symbolManager.addClickListener(new OnSymbolClickListener() {
                        @Override
                        public void onAnnotationClick(Symbol symbol) {
                            Intent intent = new Intent(MapView.this, DisconnectionFormActivity.class);
                            intent.putExtra("USERID", userId);
                            intent.putExtra("ACCTNO", symbol.getData().getAsJsonObject().get("acctNo").getAsString());
                            intent.putExtra("SCHEDULEID", scheduleId);
                            startActivity(intent);
                        }
                    });
                }
            } catch (Exception e) {
                Toast.makeText(MapView.this, "Error map data!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public boolean hasLatLong(DisconnectionList disconnectionList) {
        try {
            if (disconnectionList.getAccountCoordinates() != null | disconnectionList.getAccountCoordinates().length() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}