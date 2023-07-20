package com.lopez.julz.disconnection.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.lopez.julz.disconnection.DisconnectionFormActivity;
import com.lopez.julz.disconnection.R;
import com.lopez.julz.disconnection.dao.DisconnectionList;
import com.lopez.julz.disconnection.helpers.MonthSelect;
import com.lopez.julz.disconnection.helpers.ObjectHelpers;

import java.time.Month;
import java.util.List;

public class UnpaidMonthsAdapter extends RecyclerView.Adapter<UnpaidMonthsAdapter.ViewHolder> {

    public Context context;
    public List<DisconnectionList> disconnectionLists;
    public MonthSelect monthSelected;

    public UnpaidMonthsAdapter(Context context, List<DisconnectionList> disconnectionLists, MonthSelect monthSelect) {
        this.context = context;
        this.disconnectionLists = disconnectionLists;
        this.monthSelected = monthSelect;
    }

    @NonNull
    @Override
    public UnpaidMonthsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recyclerview_unpaid_bills, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnpaidMonthsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DisconnectionList disconnectionList = disconnectionLists.get(position);

        holder.month.setText(ObjectHelpers.formatShortDate(disconnectionList.getServicePeriodEnd()));
        holder.amount.setText(ObjectHelpers.roundTwo(Double.valueOf(disconnectionList.getNetAmount())));

        if (disconnectionList.isSelected()) {
            holder.selectedItem.setImageResource(R.drawable.ic_baseline_check_circle_24);
        } else {
            holder.selectedItem.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
        }

        holder.monthSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (disconnectionList.isSelected()) {
                    disconnectionList.setSelected(false);
                    holder.selectedItem.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                    monthSelected.itemSelect(v, position);
                } else {
                    disconnectionList.setSelected(true);
                    holder.selectedItem.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    monthSelected.itemSelect(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return disconnectionLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView amount, month;
        ImageView selectedItem;
        MaterialCardView monthSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.amount);
            month = itemView.findViewById(R.id.billingMonth);
            selectedItem = itemView.findViewById(R.id.selectedItem);
            monthSelect = itemView.findViewById(R.id.monthSelect);
        }
    }


}
