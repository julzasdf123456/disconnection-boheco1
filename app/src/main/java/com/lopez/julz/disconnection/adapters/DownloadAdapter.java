package com.lopez.julz.disconnection.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.lopez.julz.disconnection.R;
import com.lopez.julz.disconnection.api.RequestPlaceHolder;
import com.lopez.julz.disconnection.dao.AppDatabase;
import com.lopez.julz.disconnection.dao.DisconnectionList;
import com.lopez.julz.disconnection.dao.Schedules;
import com.lopez.julz.disconnection.helpers.ObjectHelpers;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {

    public List<Schedules> disconnectionLists;
    public Context context;
    public RequestPlaceHolder requestPlaceHolder;
    public AppDatabase db;

    public DownloadAdapter(List<Schedules> disconnectionLists, Context context, RequestPlaceHolder requestPlaceHolder, AppDatabase db) {
        this.disconnectionLists = disconnectionLists;
        this.context = context;
        this.requestPlaceHolder = requestPlaceHolder;
        this.db = db;
    }

    @NonNull
    @Override
    public DownloadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.download_schedule_recyclerview_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadAdapter.ViewHolder holder, int position) {
        Schedules disconnectionList = disconnectionLists.get(position);

        holder.day.setText("Day: " + ObjectHelpers.formatShortDateWithDate(disconnectionList.getDay()));
        holder.period.setText("Billing Month: " + ObjectHelpers.formatShortDate(disconnectionList.getServicePeriodEnd()));

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.download.setEnabled(false);
                fetchDownloadableAccounts(disconnectionList.getId(), holder, disconnectionList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return disconnectionLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView day, period;
        public FloatingActionButton download;
        public CircularProgressIndicator downloadProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.day);
            download = itemView.findViewById(R.id.download);
            period = itemView.findViewById(R.id.period);
            downloadProgress = itemView.findViewById(R.id.downloadProgress);

            downloadProgress.setProgress(0);
        }
    }

    public void fetchDownloadableAccounts(String id, ViewHolder holder, Schedules schedule) {
        try {
            holder.downloadProgress.setIndeterminate(true);
            holder.downloadProgress.setProgress(0);
            Call<List<DisconnectionList>> discoList = requestPlaceHolder.getDisconnectionList(id);

            discoList.enqueue(new Callback<List<DisconnectionList>>() {
                @Override
                public void onResponse(Call<List<DisconnectionList>> call, Response<List<DisconnectionList>> response) {
                    if (response.isSuccessful()) {
                        holder.downloadProgress.setIndeterminate(false);
                        new SaveDiscoList(holder, response.body().size(), id, schedule).execute(response.body());
                    } else {
                        try {
                            Log.e("ERR_GET_DSC_LIST", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(context, "Error fetching disco list!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<DisconnectionList>> call, Throwable t) {
                    t.printStackTrace();
                    Log.e("ERR_GET_DSC_LIST", t.getMessage());
                    Toast.makeText(context, "Error fetching disco list!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.getMessage();
            Toast.makeText(context, "Error fetching disco list!", Toast.LENGTH_SHORT).show();
        }
    }

    public class SaveDiscoList extends AsyncTask<List<DisconnectionList>, Integer, Void> {

        boolean isSaved = false;
        public ViewHolder holder;
        public int size;
        public String id;
        public Schedules schedule;

        public SaveDiscoList(ViewHolder holder, int size, String schedId, Schedules schedule) {
            this.holder = holder;
            this.size = size;
            id = schedId;
            this.schedule = schedule;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            holder.downloadProgress.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(List<DisconnectionList>... lists) {
            try {
                db.schedulesDao().insertAll(schedule);

                List<DisconnectionList> disconnectionList = lists[0];

                int i=0;
                for (DisconnectionList disco : disconnectionList) {
                    db.disconnectionListDao().insertAll(disco);
                    publishProgress(i);
                    i++;
                }
                isSaved = true;
            } catch (Exception e) {
                e.printStackTrace();
                isSaved = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            if (isSaved) {
                Toast.makeText(context, "Schedule downloaded!", Toast.LENGTH_SHORT).show();
                updateDownloadedSched(id);
            } else {
                Toast.makeText(context, "Error downloading list! Try again later.", Toast.LENGTH_SHORT).show();
            }
            holder.downloadProgress.setProgress(0);
        }
    }

    public void updateDownloadedSched(String id) {
        try {
            Call<Void> update = requestPlaceHolder.updateDownloadedSchedule(id, ObjectHelpers.getDeviceName());

            update.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful())  {
                        Log.e("DSCO_UPDATE", "Updated disco sched successfully");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
