package com.example.iscg7424assignment2exercises.exerciseb.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iscg7424assignment2exercises.R;
import com.example.iscg7424assignment2exercises.exerciseb.model.Customer;

import java.util.List;

public class CustomerAdapter
        extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    private Context context;
    private List<Customer> customerList;
    public CustomerAdapter(Context context, List<Customer> customerList) {
        this.context = context;
        this.customerList = customerList;
    }
    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(
            @NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.tvCustomerName.setText(customer.getFirstName() + " " + customer.getLastName());
        holder.tvCustomerEmail.setText("Email: " + customer.getEmail());
        holder.tvCustomerPhone.setText("Phone: " + customer.getPhone());
        holder.tvCustomerId.setText("Customer ID: " + customer.getId());

        Glide.with(holder.itemView)
                .load(customer.getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(holder.imgCustomer);
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class CustomerViewHolder
            extends RecyclerView.ViewHolder {
        ImageView imgCustomer;
        TextView tvCustomerName, tvCustomerEmail;
        TextView tvCustomerPhone, tvCustomerId;
        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCustomer = itemView.findViewById(R.id.imgCustomer);

            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);

            tvCustomerEmail =itemView.findViewById(R.id.tvCustomerEmail);

            tvCustomerPhone = itemView.findViewById(R.id.tvCustomerPhone);

            tvCustomerId = itemView.findViewById(R.id.tvCustomerId);
        }
    }
}