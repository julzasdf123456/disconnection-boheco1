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

import com.lopez.julz.disconnection.adapters.DiscoGroupAdapter;
import com.lopez.julz.disconnection.api.RequestPlaceHolder;
import com.lopez.julz.disconnection.api.RetrofitBuilder;
import com.lopez.julz.disconnection.dao.AppDatabase;
import com.lopez.julz.disconnection.dao.Schedules;
import com.lopez.julz.disconnection.helpers.ObjectHelpers;
import com.lopez.julz.disconnection.objects.DiscoGroup;

import java.util.ArrayList;
import java.util.List;

public class DiscoViewGroup extends AppCompatActivity {

    public Toolbar discoGroupToolbar;

    public RecyclerView discoGroupRecyclerview;
    public DiscoGroupAdapter discoGroupAdapter;
    public List<Schedules> schedulesList;

    public AppDatabase db;

    public String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disco_view_group);

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("USERID");

        db = Room.databaseBuilder(this, AppDatabase.class, ObjectHelpers.dbName()).fallbackToDestructiveMigration().build();

        discoGroupToolbar = findViewById(R.id.discoGroupToolbar);
        setSupportActionBar(discoGroupToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        discoGroupRecyclerview = findViewById(R.id.discoGroupRecyclerview);
        schedulesList = new ArrayList<>();
        discoGroupAdapter = new DiscoGroupAdapter(schedulesList, this, userId);
        discoGroupRecyclerview.setAdapter(discoGroupAdapter);
        discoGroupRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        new GetGroupings().execute();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class GetGroupings extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            schedulesList.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                schedulesList.addAll(db.schedulesDao().getActiveSchedules());
            } catch (Exception e) {
                Log.e("ERR_GETGRPINGS", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            discoGroupAdapter.notifyDataSetChanged();
        }
    }
}