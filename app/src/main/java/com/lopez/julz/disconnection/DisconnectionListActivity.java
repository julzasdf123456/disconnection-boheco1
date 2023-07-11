package com.lopez.julz.disconnection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.lopez.julz.disconnection.adapters.DiscoListAdapter;
import com.lopez.julz.disconnection.dao.AppDatabase;
import com.lopez.julz.disconnection.dao.DiscoListGrouped;
import com.lopez.julz.disconnection.dao.DisconnectionList;
import com.lopez.julz.disconnection.helpers.ObjectHelpers;

import java.util.ArrayList;
import java.util.List;

public class DisconnectionListActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public RecyclerView discoListRecyclerview;
    public List<DiscoListGrouped> disconnectionList;
    public DiscoListAdapter discoListAdapter;
    public EditText searchList;

    public AppDatabase db;

    String userId, scheduleId;
    public TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disconnection_list);

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("USERID");
        scheduleId = bundle.getString("SCHEDULEID");

        toolbar = findViewById(R.id.discoListToolbar);
        title = findViewById(R.id.title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(this,
                AppDatabase.class, ObjectHelpers.dbName()).fallbackToDestructiveMigration().build();

        discoListRecyclerview = findViewById(R.id.discoListRecyclerview);
        searchList = findViewById(R.id.searchList);
        disconnectionList = new ArrayList<>();
        discoListAdapter = new DiscoListAdapter(disconnectionList, this, userId, scheduleId);
        discoListRecyclerview.setAdapter(discoListAdapter);
        discoListRecyclerview.setLayoutManager(new LinearLayoutManager(this));

//        new FetchAllDisco().execute();
        searchList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                new Search().execute(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public class Search extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            disconnectionList.clear();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                if (strings[0] != null) {
                    String searchRegex = "%" + strings[0] + "%";
                    disconnectionList.addAll(db.disconnectionListDao().getSearch(searchRegex, scheduleId));
                } else {
                    disconnectionList.addAll(db.disconnectionListDao().getAllFromSched(scheduleId));
                }

            } catch (Exception e) {
                Log.e("ERR_GET_SEARCH", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            discoListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new FetchAllDisco().execute();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchAllDisco extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            disconnectionList.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                disconnectionList.addAll(db.disconnectionListDao().getAllFromSched(scheduleId));
            } catch (Exception e) {
                Log.e("ERR_GET_DSCO_LST", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            discoListAdapter.notifyDataSetChanged();

            title.setText("Disconnection List (" + disconnectionList.size() + ")");
        }
    }
}