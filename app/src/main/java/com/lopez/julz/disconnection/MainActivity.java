package com.lopez.julz.disconnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lopez.julz.disconnection.adapters.DisconnectionMenuAdapter;
import com.lopez.julz.disconnection.dao.AppDatabase;
import com.lopez.julz.disconnection.dao.Users;
import com.lopez.julz.disconnection.dao.UsersDao;
import com.lopez.julz.disconnection.helpers.HomeMenu;
import com.lopez.julz.disconnection.helpers.ObjectHelpers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView menu_recyclerview_disco;
    public List<HomeMenu> homeMenuList;
    public DisconnectionMenuAdapter homeMenuAdapter;

    FloatingActionButton settingsBtn, logout;

    public AppDatabase db;

    public String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        window.setAttributes(winParams);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_main);

        userId = getIntent().getExtras().getString("USERID");
        db = Room.databaseBuilder(this, AppDatabase.class, ObjectHelpers.dbName()).fallbackToDestructiveMigration().build();

        menu_recyclerview_disco = findViewById(R.id.menu_recyclerview_disco);
        homeMenuList = new ArrayList<>();
        settingsBtn = findViewById(R.id.settingsBtn);
        logout = findViewById(R.id.logout);
        homeMenuAdapter = new DisconnectionMenuAdapter(homeMenuList, this, userId);
        menu_recyclerview_disco.setAdapter(homeMenuAdapter);
        menu_recyclerview_disco.setLayoutManager(new GridLayoutManager(this, 2));

        addMenu();

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Logout().execute();
            }
        });
    }

    public void addMenu() {
        try {
            homeMenuList.add(new HomeMenu(getDrawable(R.drawable.ic_baseline_download_24), "Download", "#4caf50"));
            homeMenuList.add(new HomeMenu(getDrawable(R.drawable.ic_baseline_file_upload_24), "Upload", "#ff7043"));
            homeMenuList.add(new HomeMenu(getDrawable(R.drawable.ic_baseline_data_thresholding_24), "Disconnection List", "#5c6bc0"));

            homeMenuAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("ERR_ADD_MENU", e.getMessage());
        }
    }

    public class Logout extends AsyncTask<Void, Void, Void> {

        boolean isSuccessful = false;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                UsersDao usersDao = db.usersDao();
                Users user = usersDao.getOneById(userId);
                if (user != null) {
                    user.setLoggedIn("NULL");
                    usersDao.updateAll(user);
                    isSuccessful = true;
                } else {
                    isSuccessful = false;
                }
            } catch (Exception e) {
                Log.e("ERR_LGOUT", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (isSuccessful) {
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        }
    }
}