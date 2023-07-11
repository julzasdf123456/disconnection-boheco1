package com.lopez.julz.disconnection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lopez.julz.disconnection.dao.AppDatabase;
import com.lopez.julz.disconnection.dao.Settings;
import com.lopez.julz.disconnection.helpers.ObjectHelpers;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    public Toolbar toolbar;

    public Spinner serverSelect, officeSelect;

    public FloatingActionButton saveBtn;

    public AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        db = Room.databaseBuilder(this, AppDatabase.class, ObjectHelpers.dbName()).fallbackToDestructiveMigration().build();

        toolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        serverSelect = findViewById(R.id.serverSelect);
        officeSelect = findViewById(R.id.officeSelect);
        saveBtn = findViewById(R.id.saveBtn);

        officeSelect.setVisibility(View.GONE);

        populateSpinners();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SaveSettings().execute(serverSelect.getSelectedItem().toString(), officeSelect.getSelectedItem().toString());
            }
        });
    }

    public void populateSpinners() {
        try {
            List<String> servers = new ArrayList<>();
//            servers.add("192.168.110.94");
            servers.add("192.168.10.161");
            ArrayAdapter serversAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, servers.toArray());
            serversAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            serverSelect.setAdapter(serversAdapter);

            List<String> office = new ArrayList<>();
            office.add("CADIZ");
            office.add("CALATRAVA");
            office.add("EB MAGALONA");
            office.add("ESCALANTE");
            office.add("MANAPLA");
            office.add("SAGAY");
            office.add("SAN CARLOS");
            office.add("TOBOSO");
            office.add("VICTORIAS");
            office.add("MO");
            ArrayAdapter officeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, office.toArray());
            officeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            officeSelect.setAdapter(officeAdapter);

        } catch (Exception e) {
            Log.e("ERR_POP_SPINNRS", e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class SaveSettings extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Settings settings = new Settings(strings[0], strings[1]); // 0 = server, 1 = office
                db.settingsDao().insertAll(settings);
            } catch (Exception e) {
                Log.e("ERR_SV_SETTINGS", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            finish();
            Toast.makeText(SettingsActivity.this, "Settings saved!", Toast.LENGTH_SHORT).show();
        }
    }
}