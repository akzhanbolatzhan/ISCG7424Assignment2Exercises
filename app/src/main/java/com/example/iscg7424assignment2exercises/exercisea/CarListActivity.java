package com.example.iscg7424assignment2exercises.exercisea;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.example.iscg7424assignment2exercises.exercisea.adapter.CarAdapter;
import com.example.iscg7424assignment2exercises.exercisea.model.car;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.appcompat.widget.SearchView;
import java.util.ArrayList;
import java.util.List;

public class CarListActivity extends AppCompatActivity {

    private CarAdapter adapter;
    private List<car> carList;
    private DatabaseReference carsRef;
    private ValueEventListener carsListener;
    private RecyclerView recyclerViewCars;
    private MaterialButton btnAddNewCar;
    private ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_car_list);
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
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
        recyclerViewCars = findViewById(R.id.recyclerViewCars);
        btnAddNewCar = findViewById(R.id.btnAddNewCar);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAddNewCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        CarListActivity.this,AddCarActivity.class
                );
                startActivity(intent);
            }
        });
        carList = new ArrayList<>();
        carsRef = FirebaseDatabase.getInstance()
                .getReference("cars");
        adapter = new CarAdapter(this, carList);
        recyclerViewCars.setLayoutManager(
                new LinearLayoutManager(this)
        );
        recyclerViewCars.setAdapter(adapter);
        loadCars();

        SearchView svSearch = findViewById(R.id.svSearch);

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.filter(newText);
                }
                return true;
            }
        });
    }
    private void loadCars() {
        carsListener = carsRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(
                            @NonNull DataSnapshot snapshot) {
                        carList.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            car selectedCar = child.getValue(car.class);
                            String carId = child.getKey();
                            if (selectedCar != null && carId != null) {
                                selectedCar.setCarId(carId);
                                carList.add(selectedCar);
                            }
                        }
                        adapter.updateData(new ArrayList<>(carList));
                    }
                    @Override
                    public void onCancelled(
                            @NonNull DatabaseError error) {
                        Toast.makeText(
                                CarListActivity.this, "Failed to load cars: " + error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (carsListener != null) {
            carsRef.removeEventListener(carsListener);
        }
    }
}