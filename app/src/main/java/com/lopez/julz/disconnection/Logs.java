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
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.lopez.julz.disconnection.adapters.DiscoListAdapter;
import com.lopez.julz.disconnection.dao.AppDatabase;
import com.lopez.julz.disconnection.dao.DiscoListGrouped;
import com.lopez.julz.disconnection.helpers.ObjectHelpers;

import java.util.ArrayList;
import java.util.List;

public class Logs extends AppCompatActivity {

    public Toolbar toolbar;
    public RecyclerView discoListRecyclerview;
    public List<DiscoListGrouped> disconnectionList;
    public DiscoListAdapter discoListAdapter;

    public MaterialButton statusSelect;
    public String status = "Finished";

    public AppDatabase db;

    String userId, scheduleId;
    public TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        Bundle bundle = getIntent().getExtras();
        scheduleId = bundle.getString("SCHEDULEID");

        toolbar = findViewById(R.id.discoListToolbar);
        title = findViewById(R.id.title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        statusSelect = findViewById(R.id.statusSelect);

        db = Room.databaseBuilder(this,
                AppDatabase.class, ObjectHelpers.dbName()).fallbackToDestructiveMigration().build();

        discoListRecyclerview = findViewById(R.id.discoListRecyclerview);
        disconnectionList = new ArrayList<>();
        discoListAdapter = new DiscoListAdapter(disconnectionList, this, userId, scheduleId);
        discoListRecyclerview.setAdapter(discoListAdapter);
        discoListRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        statusSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("Finished")) {
                    status = "Unfinished";
                } else {
                    status = "Finished";
                }
                new FetchData().execute(status);
                statusSelect.setText(status);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new FetchData().execute(status);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchData extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            disconnectionList.clear();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                if (strings[0].equals("Finished")) {
                    disconnectionList.addAll(db.disconnectionListDao().getGroupedUploadable(scheduleId));
                } else {
                    disconnectionList.addAll(db.disconnectionListDao().getAllFromSched(scheduleId));
                }
            } catch (Exception e) {
                Log.e("ERR_GET_DSCO_LST", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            discoListAdapter.notifyDataSetChanged();

            title.setText("Disco List (" + disconnectionList.size() + ")");
        }
    }
}