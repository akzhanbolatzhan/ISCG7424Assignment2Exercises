package com.example.iscg7424assignment2exercises.exerciseb;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iscg7424assignment2exercises.R;
import com.example.iscg7424assignment2exercises.exerciseb.adapter.CustomerAdapter;
import com.example.iscg7424assignment2exercises.exerciseb.model.Customer;
import com.example.iscg7424assignment2exercises.exerciseb.model.Customers;
import com.example.iscg7424assignment2exercises.exerciseb.network.CustomerRESTAPI;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiActivity extends AppCompatActivity {

    private final String BASE_URL = "https://dummyjson.com/";

    private CustomerAdapter adapter;
    private List<Customer> customerList;
    private RecyclerView recyclerViewCustomers;
    private MaterialButton btnMakeApiCall;
    private TextView tvStatus;
    private ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_api);
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets( WindowInsetsCompat.Type.systemBars()
                    );
                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );
        btnMakeApiCall = findViewById(R.id.btnMakeApiCall);
        tvStatus = findViewById(R.id.tvStatus);
        btnBack = findViewById(R.id.btnBack);
        recyclerViewCustomers = findViewById(R.id.recyclerViewCustomers);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        customerList = new ArrayList<>();
        adapter = new CustomerAdapter(this, customerList);

        recyclerViewCustomers.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerViewCustomers.setAdapter(adapter);
        btnMakeApiCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
    }
    public void start() {

        tvStatus.setText("Loading customers...");
        btnMakeApiCall.setEnabled(false);
        customerList.clear();
        adapter.notifyDataSetChanged();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CustomerRESTAPI customerRESTAPI =
                retrofit.create(CustomerRESTAPI.class);
        Call<Customers> call = customerRESTAPI.getCustomers();
        call.enqueue(new Callback<Customers>() {
            @Override
            public void onResponse(
                    @NonNull Call<Customers> call,
                    @NonNull Response<Customers> response) {
                btnMakeApiCall.setEnabled(true);
                if (response.isSuccessful()
                        && response.body() != null) {
                    Customers customers = response.body();
                    List<Customer> users = customers.getUsers();
                    if (users != null && !users.isEmpty()) {

                        customerList.clear();
                        customerList.addAll(users);
                        adapter.notifyDataSetChanged();
                        tvStatus.setText(
                                "Successfully loaded " + customerList.size() + " customers"
                        );

                        Toast.makeText(
                                ApiActivity.this, "Customers loaded successfully",
                                Toast.LENGTH_SHORT
                        ).show();

                    }
                }
            }
            @Override
            public void onFailure(
                    @NonNull Call<Customers> call,
                    @NonNull Throwable t) {
                btnMakeApiCall.setEnabled(true);
                tvStatus.setText("Failed to load customers");
                Toast.makeText(
                        ApiActivity.this,"Network Error: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}