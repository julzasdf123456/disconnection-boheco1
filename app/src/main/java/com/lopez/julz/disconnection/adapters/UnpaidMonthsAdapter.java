package com.lopez.julz.disconnection.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lopez.julz.disconnection.R;
import com.lopez.julz.disconnection.dao.DisconnectionList;
import com.lopez.julz.disconnection.helpers.ObjectHelpers;

import java.util.List;

public class UnpaidMonthsAdapter extends RecyclerView.Adapter<UnpaidMonthsAdapter.ViewHolder> {

    public Context context;
    public List<DisconnectionList> disconnectionLists;

    public UnpaidMonthsAdapter(Context context, List<DisconnectionList> disconnectionLists) {
        this.context = context;
        this.disconnectionLists = disconnectionLists;
    }

    @NonNull
    @Override
    public UnpaidMonthsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recyclerview_unpaid_bills, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnpaidMonthsAdapter.ViewHolder holder, int position) {
        DisconnectionList disconnectionList = disconnectionLists.get(position);

        holder.month.setText(ObjectHelpers.formatShortDate(disconnectionList.getServicePeriodEnd()));
        holder.amount.setText(ObjectHelpers.roundTwo(Double.valueOf(disconnectionList.getNetAmount())));
    }

    @Override
    public int getItemCount() {
        return disconnectionLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView amount, month;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.amount);
            month = itemView.findViewById(R.id.billingMonth);
        }
    }
}
