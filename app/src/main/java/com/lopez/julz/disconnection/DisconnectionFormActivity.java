package com.lopez.julz.disconnection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lopez.julz.disconnection.adapters.UnpaidMonthsAdapter;
import com.lopez.julz.disconnection.dao.AppDatabase;
import com.lopez.julz.disconnection.dao.DisconnectionList;
import com.lopez.julz.disconnection.helpers.AlertHelpers;
import com.lopez.julz.disconnection.helpers.ObjectHelpers;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import java.util.ArrayList;
import java.util.List;

public class DisconnectionFormActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

    public TextView accountName, accountNo, change;
    public TextView meterNo, address, poleNo, amountDue, totalSurcharges, serviceFee, totalAmountDue;

    public RadioButton disconnectButton;

    public FloatingActionButton saveBtn;

    public MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private LocationComponent locationComponent;
    public Style style;
    public SymbolManager symbolManager;

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1002;

    public Toolbar toolbarDisconnectionForm;

    public String userId, acctNo, scheduleId;

    public List<DisconnectionList> disconnectionList;
    public UnpaidMonthsAdapter unpaidMonthsAdapter;
    public RecyclerView unpaidMonthsRecyclerView;

    public AppDatabase db;

    public double totalAmountPayable = 0;

    public EditText amountPaid, remarks;
    public RadioGroup assessment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_disconnection_form);

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("USERID");
        acctNo = bundle.getString("ACCTNO");
        scheduleId = bundle.getString("SCHEDULEID");

        db = Room.databaseBuilder(this, AppDatabase.class, ObjectHelpers.dbName()).fallbackToDestructiveMigration().build();

        toolbarDisconnectionForm = findViewById(R.id.toolbarDisconnectionForm);
        setSupportActionBar(toolbarDisconnectionForm);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        disconnectionList = new ArrayList<>();

        accountName = findViewById(R.id.accountName);
        accountNo = findViewById(R.id.accountNo);
        address = findViewById(R.id.address);
        disconnectButton = findViewById(R.id.disconnectButton);
        saveBtn = findViewById(R.id.saveDisconnectionData);
        mapView = findViewById(R.id.mapviewDisconnectionForm);
        meterNo = findViewById(R.id.meterNo);
        poleNo = findViewById(R.id.poleNo);
        unpaidMonthsRecyclerView = findViewById(R.id.unpaidMonthsRecyclerView);
        unpaidMonthsAdapter = new UnpaidMonthsAdapter(this, disconnectionList);
        unpaidMonthsRecyclerView.setAdapter(unpaidMonthsAdapter);
        unpaidMonthsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        amountDue = findViewById(R.id.amountDue);
        totalSurcharges = findViewById(R.id.totalSurcharges);
        serviceFee = findViewById(R.id.serviceFee);
        totalAmountDue = findViewById(R.id.totalAmountDue);
        amountPaid = findViewById(R.id.amountPaid);
        change = findViewById(R.id.change);
        assessment = findViewById(R.id.assessment);
        remarks = findViewById(R.id.remarks);

        // MAP
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        new GetDetails().execute();

        amountPaid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    double amount = 0;
                    if (s != null && s.length() > 0) {
                        amount = Double.valueOf(s.toString());
                    } else {
                        amount = 0;
                    }

                    double changeVal = amount - totalAmountPayable;

                    if (changeVal < 0) {
                        change.setText(ObjectHelpers.roundTwo(0.0));
                    } else {
                        change.setText(ObjectHelpers.roundTwo(changeVal));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    change.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (locationComponent != null) {
//                    try {
//                        disconnectionList.setLatitude(locationComponent.getLastKnownLocation().getLatitude() + "");
//                        disconnectionList.setLongitude(locationComponent.getLastKnownLocation().getLongitude() + "");
//                    } catch (Exception e) {
//                        Log.e("ERR_GET_LOC", e.getMessage());
//                    }
//                }
//                disconnectionList.setUserId(userId);
//                if (disconnectButton.isChecked()) {
//                    disconnectionList.setUploadStatus(null);
//                } else {
//                    disconnectionList.setUploadStatus("No");
//                }
//
//                new SaveDisconnectionData().execute();

                String assessmentSelected = ObjectHelpers.getSelectedTextFromRadioGroup(assessment, assessment);
                if (assessmentSelected != null && assessmentSelected.length() > 0) {
                    double amnt = ObjectHelpers.doubleStringNull(amountPaid.getText().toString());
                    if (assessmentSelected.equals("Paid") && totalAmountPayable > amnt) {
                        AlertHelpers.showMessageDialog(DisconnectionFormActivity.this, "Invalid amount inputted!", "Amount paid should not be less than the amount due.");
                    } else {
                        new SaveDisconnectionData().execute();
                    }
                } else {
                    new SaveDisconnectionData().execute();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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

                    plotMarker();

                    enableLocationComponent(style);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "READ_PHONE_STATE Denied", Toast.LENGTH_SHORT)
                            .show();
                } else {

                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public boolean hasLatLong(DisconnectionList disconnectionList) {
        if (disconnectionList.getAccountCoordinates() != null && disconnectionList.getAccountCoordinates().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void plotMarker() {
        try {
            if (symbolManager != null) {
                symbolManager.deleteAll();
            }
            symbolManager = new SymbolManager(mapView, mapboxMap, style);

            symbolManager.setIconAllowOverlap(true);
            symbolManager.setTextAllowOverlap(true);

            if (disconnectionList != null && disconnectionList.size() > 0) {
                /**
                 * PLOT TO MAP
                 */
                DisconnectionList disclist = disconnectionList.get(0);
                if (hasLatLong(disclist)) {
                    String split[] = disclist.getAccountCoordinates().split(",");
                    SymbolOptions symbolOptions;
                    symbolOptions = new SymbolOptions()
                            .withLatLng(new LatLng(Double.valueOf(split[0]), Double.valueOf(split[1])))
                            .withIconImage("place-black-24dp")
                            .withIconSize(1f);

                    Symbol symbol = symbolManager.create(symbolOptions);

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(Double.valueOf(split[0]), Double.valueOf(split[1])))
                            .zoom(13f)
                            .build();

                    if (mapboxMap != null) {
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1200);
                    } else {
                        Toast.makeText(DisconnectionFormActivity.this, "Map is still loading, try again in a couple of seconds", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("ERR_PLOT_MRKER", e.getMessage());
        }
    }

    public class GetDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                disconnectionList.addAll(db.disconnectionListDao().getConsumerDiscoData(acctNo, scheduleId));
            } catch (Exception e) {
                Log.e("ERR_GET_DETAILS", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            if (disconnectionList != null && disconnectionList.size() > 0) {
                DisconnectionList dscList = disconnectionList.get(0);
                accountName.setText(dscList.getConsumerName());
                accountNo.setText(dscList.getAccountNumber());
                meterNo.setText(dscList.getMeterNumber());
                address.setText(dscList.getConsumerAddress());
                poleNo.setText(dscList.getPoleNumber());
                unpaidMonthsAdapter.notifyDataSetChanged();

                double totalAmount = ObjectHelpers.getTotalAmount(disconnectionList);
                double totalSurcharge = ObjectHelpers.getTotalSucharges(disconnectionList);
                double serviceFees = 33.6;
                totalAmountPayable = totalAmount + totalSurcharge + serviceFees;

                amountDue.setText(ObjectHelpers.roundTwo(totalAmount));
                totalSurcharges.setText(ObjectHelpers.roundTwo(totalSurcharge));
                serviceFee.setText(ObjectHelpers.roundTwo(serviceFees));
                totalAmountDue.setText(ObjectHelpers.roundTwo(totalAmountPayable));

                // IF DATA IS ALREADY INPUTTED
                amountPaid.setText((ObjectHelpers.getTotalAmountPaid(disconnectionList) > 0 ? ObjectHelpers.getTotalAmountPaid(disconnectionList) : "")+"");

                if (dscList.getStatus() != null && dscList.getStatus().length() > 0) {
                    assessment.check(getAssessment(dscList.getStatus()));
                }

                remarks.setText(dscList.getNotes());
            } else {
                AlertHelpers.showMessageDialog(DisconnectionFormActivity.this, "No data found!", "No data found for this account!");
            }
        }
    }

    public class SaveDisconnectionData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... Void) {
            try {
                String assessmentSelected = ObjectHelpers.getSelectedTextFromRadioGroup(assessment, assessment);
                if (assessmentSelected != null && assessmentSelected.length() > 0) {
                    double amnt = ObjectHelpers.doubleStringNull(amountPaid.getText().toString());
                    if (assessmentSelected.equals("Paid") && totalAmountPayable > amnt) {

                    } else {
                        if (assessmentSelected.equals("Disconnected") | assessmentSelected.equals("Promised")) {
                            for (DisconnectionList disco:
                                 disconnectionList) {
                                disco.setStatus(assessmentSelected);
                                disco.setUploadStatus("Uploadable");
                                disco.setNotes(remarks.getText().toString());
                                disco.setPaidAmount("0");
                                disco.setServiceFee("0");
                                if (locationComponent != null) {
                                    try {
                                        disco.setLatitude(locationComponent.getLastKnownLocation().getLatitude() + "");
                                        disco.setLongitude(locationComponent.getLastKnownLocation().getLongitude() + "");
                                    } catch (Exception e) {
                                        Log.e("ERR_GET_LOC", e.getMessage());
                                    }
                                }
                                disco.setDisconnectionDate(ObjectHelpers.getCurrentTimestamp());
                                db.disconnectionListDao().updateAll(disco);
                            }
                        } else if (assessmentSelected.equals("Paid")) {
                            for (DisconnectionList disco:
                                    disconnectionList) {
                                disco.setStatus(assessmentSelected);
                                disco.setUploadStatus("Uploadable");
                                disco.setNotes(remarks.getText().toString());
                                disco.setPaidAmount(ObjectHelpers.getTotalPaidAmount(disco) + "");
                                disco.setServiceFee("0");
                                if (locationComponent != null) {
                                    try {
                                        disco.setLatitude(locationComponent.getLastKnownLocation().getLatitude() + "");
                                        disco.setLongitude(locationComponent.getLastKnownLocation().getLongitude() + "");
                                    } catch (Exception e) {
                                        Log.e("ERR_GET_LOC", e.getMessage());
                                    }
                                }
                                disco.setDisconnectionDate(ObjectHelpers.getCurrentTimestamp());
                                db.disconnectionListDao().updateAll(disco);
                            }
                        }
                    }
                } else {
                    for (DisconnectionList disco:
                            disconnectionList) {
                        disco.setStatus(assessmentSelected);
                        disco.setNotes(remarks.getText().toString());
                        disco.setPaidAmount(ObjectHelpers.getTotalPaidAmount(disco) + "");
                        disco.setServiceFee("0");
                        if (locationComponent != null) {
                            try {
                                disco.setLatitude(locationComponent.getLastKnownLocation().getLatitude() + "");
                                disco.setLongitude(locationComponent.getLastKnownLocation().getLongitude() + "");
                            } catch (Exception e) {
                                Log.e("ERR_GET_LOC", e.getMessage());
                            }
                        }
                        disco.setDisconnectionDate(ObjectHelpers.getCurrentTimestamp());
                        db.disconnectionListDao().updateAll(disco);
                    }
                }
            } catch (Exception e) {
                Log.e("ERR_SV_DSCO_DATA", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Toast.makeText(DisconnectionFormActivity.this, "Disconnection saved!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public int getAssessment(String assessment) {
        if (assessment != null) {
            if (assessment.equals("Disconnected")) {
                return R.id.disconnectButton;
            } else if (assessment.equals("Paid")) {
                return R.id.paidButton;
            } else {
                return R.id.promisedButton;
            }
        } else {
            return 0;
        }
    }
}