package com.lopez.julz.disconnection.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.lopez.julz.disconnection.DisconnectionListActivity;
import com.lopez.julz.disconnection.R;
import com.lopez.julz.disconnection.dao.DisconnectionList;
import com.lopez.julz.disconnection.dao.Schedules;
import com.lopez.julz.disconnection.helpers.ObjectHelpers;
import com.lopez.julz.disconnection.objects.DiscoGroup;

import java.util.List;

public class DiscoGroupAdapter  extends RecyclerView.Adapter<DiscoGroupAdapter.ViewHolder> {

    public List<Schedules> disconnectionLists;
    public Context context;
    public String userId;

    public DiscoGroupAdapter(List<Schedules> disconnectionLists, Context context, String userId) {
        this.disconnectionLists = disconnectionLists;
        this.context = context;
        this.userId = userId;
    }

    @NonNull
    @Override
    public DiscoGroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.download_schedule_recyclerview_layout, parent, false);

        return new DiscoGroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoGroupAdapter.ViewHolder holder, int position) {
        Schedules discoGroup = disconnectionLists.get(position);

        holder.day.setText("Day: " + ObjectHelpers.formatShortDateWithDate(discoGroup.getDay()));
        holder.period.setText("Billing Month: " + ObjectHelpers.formatShortDate(discoGroup.getServicePeriodEnd()));

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DisconnectionListActivity.class);
                intent.putExtra("USERID", userId);
                intent.putExtra("SCHEDULEID", discoGroup.getId());
                context.startActivity(intent);
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
        public MaterialCardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.day);
            period = itemView.findViewById(R.id.period);
            download = itemView.findViewById(R.id.download);
            parent = itemView.findViewById(R.id.parent);
            downloadProgress = itemView.findViewById(R.id.downloadProgress);

            download.setVisibility(View.GONE);
            downloadProgress.setVisibility(View.GONE);
        }
    }
}
