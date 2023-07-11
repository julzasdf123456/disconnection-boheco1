package com.lopez.julz.disconnection.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.lopez.julz.disconnection.DisconnectionFormActivity;
import com.lopez.julz.disconnection.R;
import com.lopez.julz.disconnection.dao.DiscoListGrouped;
import com.lopez.julz.disconnection.dao.DisconnectionList;
import com.lopez.julz.disconnection.helpers.ObjectHelpers;

import java.util.List;

public class DiscoListAdapter  extends RecyclerView.Adapter<DiscoListAdapter.ViewHolder>{

    public List<DiscoListGrouped> disconnectionLists;
    public Context context;
    public String userId;
    public String scheduleId;

    public DiscoListAdapter(List<DiscoListGrouped> disconnectionLists, Context context, String userId, String scheduleId) {
        this.disconnectionLists = disconnectionLists;
        this.context = context;
        this.userId = userId;
        this.scheduleId = scheduleId;
    }

    @NonNull
    @Override
    public DiscoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recyclerview_layout_download, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoListAdapter.ViewHolder holder, int position) {
        DiscoListGrouped disconnectionList = disconnectionLists.get(position);

        holder.accountNo.setText(disconnectionList.getAccountNumber() + " | Meter No: " + disconnectionList.getMeterNumber());
        holder.consumerName.setText(disconnectionList.getConsumerName());
        holder.address.setText(disconnectionList.getConsumerAddress());
//        holder.period.setText(ObjectHelpers.formatShortDate(disconnectionList.getServicePeriodEnd()));

//        if (disconnectionList.getUploadStatus() != null && disconnectionList.getUploadStatus().equals("Uploadable")) {
//            holder.consumerName.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_baseline_check_circle_18), null, null, null);
//        } else {
//            holder.consumerName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//        }

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DisconnectionFormActivity.class);
                intent.putExtra("USERID", userId);
                intent.putExtra("ACCTNO", disconnectionList.getAccountNumber());
                intent.putExtra("SCHEDULEID", scheduleId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return disconnectionLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView accountNo, consumerName, address, period;
        public MaterialCardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            accountNo = itemView.findViewById(R.id.accountNo);
            consumerName = itemView.findViewById(R.id.consumerName);
            address = itemView.findViewById(R.id.address);
            period = itemView.findViewById(R.id.period);
            parent = itemView.findViewById(R.id.parent);

            period.setVisibility(View.GONE);
        }
    }
}
