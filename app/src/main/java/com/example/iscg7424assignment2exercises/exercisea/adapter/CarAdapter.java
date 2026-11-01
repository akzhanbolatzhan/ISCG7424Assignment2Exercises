package com.example.iscg7424assignment2exercises.exercisea.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.Glide;
import com.example.iscg7424assignment2exercises.R;
import com.example.iscg7424assignment2exercises.exercisea.EditCarActivity;
import com.example.iscg7424assignment2exercises.exercisea.model.car;

import java.util.List;
import java.util.Locale;
public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private final Context context;
    private final List<car> carList;
    private List<car> fullList = new ArrayList<>();
    private String currentQuery = "";
    public CarAdapter(Context context, List<car> carList) {
        this.context = context;
        this.carList = carList;
    }
    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }
    @Override
    public void onBindViewHolder(
            @NonNull CarViewHolder holder, int position) {
        car selectedCar = carList.get(position);
        holder.tvCarName.setText(
                selectedCar.getBrand() + " " + selectedCar.getModel()
        );
        holder.tvCarPrice.setText(
                String.format(
                        Locale.getDefault(),
                        "Price: $%,.2f",
                        selectedCar.getPrice()
                )
        );
        holder.tvCarYear.setText(
                "Year: " + selectedCar.getYear()
        );
        Glide.with(holder.itemView)
                .load(selectedCar.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(holder.imgCar);
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(
                        context,
                        EditCarActivity.class
                );
                intent.putExtra("carId", selectedCar.getCarId());
                context.startActivity(intent);
            }
        });
    }
    public void updateData(List<car> newList) {
        fullList.clear();
        fullList.addAll(newList);
        filter(currentQuery);
    }
    public void filter(String query) {
        currentQuery = query == null ? "" : query.toLowerCase().trim();
        carList.clear();

        for (car Car : fullList) {
            if (currentQuery.isEmpty()
                    || (Car.getBrand() != null && Car.getBrand().toLowerCase().contains(currentQuery))
                    || (Car.getModel() != null && Car.getModel().toLowerCase().contains(currentQuery))) {
                carList.add(Car);
            }
        }

        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return carList.size();
    }
    public static class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCar, imgEdit;
        TextView tvCarName, tvCarPrice, tvCarYear;
        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCar = itemView.findViewById(R.id.imgCar);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            tvCarPrice = itemView.findViewById(R.id.tvCarPrice);
            tvCarYear = itemView.findViewById(R.id.tvCarYear);
        }
    }
}