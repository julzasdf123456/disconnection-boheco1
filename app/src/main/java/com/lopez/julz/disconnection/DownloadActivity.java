package com.lopez.julz.disconnection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lopez.julz.disconnection.adapters.DownloadAdapter;
import com.lopez.julz.disconnection.api.RequestPlaceHolder;
import com.lopez.julz.disconnection.api.RetrofitBuilder;
import com.lopez.julz.disconnection.dao.AppDatabase;
import com.lopez.julz.disconnection.dao.DisconnectionList;
import com.lopez.julz.disconnection.dao.Schedules;
import com.lopez.julz.disconnection.dao.Settings;
import com.lopez.julz.disconnection.dao.Users;
import com.lopez.julz.disconnection.helpers.AlertHelpers;
import com.lopez.julz.disconnection.helpers.ObjectHelpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadActivity extends AppCompatActivity {

    public RecyclerView downloadRecyclerview;
    public DownloadAdapter downloadAdapter;
    public Toolbar toolbar;

    public RetrofitBuilder retrofitBuilder;
    private RequestPlaceHolder requestPlaceHolder;

    public AppDatabase db;
    public Settings settings;

    public List<Schedules> schedulesList;

    public String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        toolbar = findViewById(R.id.downloadToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userId = getIntent().getExtras().getString("USERID");

        db = Room.databaseBuilder(this,
                AppDatabase.class, ObjectHelpers.dbName()).fallbackToDestructiveMigration().build();

        new FetchSettings().execute();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public class FetchSettings extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                settings = db.settingsDao().getSettings();
            } catch (Exception e) {
                Log.e("ERR_FETCH_SETTINGS", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (settings != null) {
                retrofitBuilder = new RetrofitBuilder(settings.getDefaultServer());
                Log.e("ET", settings.getDefaultServer());
                requestPlaceHolder = retrofitBuilder.getRetrofit().create(RequestPlaceHolder.class);

                downloadRecyclerview = findViewById(R.id.downloadRecyclerview);

                schedulesList = new ArrayList<>();
                downloadAdapter = new DownloadAdapter(schedulesList, DownloadActivity.this, requestPlaceHolder, db);
                downloadRecyclerview.setAdapter(downloadAdapter);
                downloadRecyclerview.setLayoutManager(new LinearLayoutManager(DownloadActivity.this));

                fetchSchedules(userId);
            } else {
                AlertHelpers.showMessageDialog(DownloadActivity.this, "Settings Not Initialized", "Failed to load settings. Go to settings and set all necessary parameters to continue.");
            }
        }
    }

    public void fetchSchedules(String userId) {
        try {
            Call<List<Schedules>> schedCall = requestPlaceHolder.getShedulesList(userId);

            schedCall.enqueue(new Callback<List<Schedules>>() {
                @Override
                public void onResponse(Call<List<Schedules>> call, Response<List<Schedules>> response) {
                    if (response.isSuccessful()) {
                        schedulesList.addAll(response.body());
                        downloadAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(DownloadActivity.this, "Error getting sched!", Toast.LENGTH_SHORT).show();
                        try {
                            Log.e("ERR_DL_SCHEDS", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Schedules>> call, Throwable t) {
                    Toast.makeText(DownloadActivity.this, "Error getting sched!", Toast.LENGTH_SHORT).show();
                    Log.e("ERR_DL_SCHEDS", t.getMessage());
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error getting sched!", Toast.LENGTH_SHORT).show();
        }
    }
}