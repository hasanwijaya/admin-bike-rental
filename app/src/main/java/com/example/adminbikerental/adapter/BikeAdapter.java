package com.example.adminbikerental.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adminbikerental.R;
import com.example.adminbikerental.model.Bike;
import com.example.adminbikerental.model.Customer;

import java.util.ArrayList;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.CustomerViewHolder> {
    private ArrayList<Bike> listBike;
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public BikeAdapter(ArrayList<Bike> listBike) {
        this.listBike = listBike;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bike, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomerViewHolder holder, int position) {
        Bike field = listBike.get(position);

        Glide.with(holder.itemView.getContext()).load(field.getImage()).into(holder.ivBike);

        holder.tvName.setText(field.getMerk());
        holder.tvCode.setText(field.getCode());
        holder.tvColor.setText(field.getColor());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listBike.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBike.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBike;
        TextView tvName, tvCode, tvColor;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBike = itemView.findViewById(R.id.iv_bike);
            tvName = itemView.findViewById(R.id.tv_name);
            tvCode = itemView.findViewById(R.id.tv_code);
            tvColor = itemView.findViewById(R.id.tv_color);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Bike data);
    }
}
