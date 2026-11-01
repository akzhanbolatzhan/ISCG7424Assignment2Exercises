package com.example.iscg7424assignment2exercises.exercisea;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iscg7424assignment2exercises.R;
import com.example.iscg7424assignment2exercises.exercisea.model.car;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditCarActivity extends AppCompatActivity {
    private DatabaseReference carRef;

    EditText txtImageUrl, txtBrand, txtModel, txtPrice;
    NumberPicker numberPickerYear;
    ImageView btnBack, imgCarPreview;
    MaterialButton btnSaveCar, btnDeleteCar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_car);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String carId = getIntent().getStringExtra("carId");
        if (carId == null || carId.isEmpty()) {
            Toast.makeText(
                    this,
                    "Car ID not found",
                    Toast.LENGTH_SHORT
            ).show();
            finish();
            return;
        }
        carRef = FirebaseDatabase.getInstance()
                .getReference("cars")
                .child(carId);
        txtImageUrl = findViewById(R.id.txtImageUrl);
        txtBrand = findViewById(R.id.txtBrand);
        txtModel = findViewById(R.id.txtModel);
        txtPrice = findViewById(R.id.txtPrice);
        numberPickerYear = findViewById(R.id.numberPickerYear);
        numberPickerYear.setMinValue(1980);
        numberPickerYear.setMaxValue(
                Calendar.getInstance().get(Calendar.YEAR) + 1
        );
        numberPickerYear.setValue(2020);
        btnBack = findViewById(R.id.btnBack);
        btnSaveCar = findViewById(R.id.btnSaveCar);
        btnDeleteCar = findViewById(R.id.btnDeleteCar);
        imgCarPreview = findViewById(R.id.imgCarPreview);
        btnSaveCar.setEnabled(false);
        btnDeleteCar.setEnabled(false);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loadCar();
        btnSaveCar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveCar();
                    }
                }
        );
        btnDeleteCar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(EditCarActivity.this)
                                .setTitle("Delete Car") .setMessage("Are you sure you want to delete this car?")
                                .setPositiveButton("Delete", (dialog, which) -> {
                                    deleteCar();
                                })
                                .setNegativeButton("Cancel", (dialog, which) -> {
                                    dialog.dismiss();
                                }).show();
                    }
                }
        );
    }
    private void loadCar() {

        carRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(
                                EditCarActivity.this,
                                "Failed to load car: " + error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                    @Override
                    public void onDataChange(
                            @NonNull DataSnapshot snapshot) {
                        car selectedCar = snapshot.getValue(car.class);
                        if (selectedCar != null) {
                            txtBrand.setText(
                                    selectedCar.getBrand()
                            );
                            txtModel.setText(
                                    selectedCar.getModel()
                            );
                            txtPrice.setText(
                                    String.valueOf(selectedCar.getPrice())
                            );
                            int year = selectedCar.getYear();
                            year = Math.max(
                                    numberPickerYear.getMinValue(),
                                    Math.min(
                                            numberPickerYear.getMaxValue(),
                                            year
                                    )
                            );
                            numberPickerYear.setValue(year);
                            txtImageUrl.setText(
                                    selectedCar.getImageUrl()
                            );
                            Glide.with(EditCarActivity.this)
                                    .load(selectedCar.getImageUrl())
                                    .placeholder(android.R.drawable.ic_menu_gallery)
                                    .error(android.R.drawable.ic_menu_gallery)
                                    .into(imgCarPreview);
                            btnSaveCar.setEnabled(true);
                            btnDeleteCar.setEnabled(true);
                        } else {
                            Toast.makeText(
                                    EditCarActivity.this,
                                    "Car not found",
                                    Toast.LENGTH_SHORT
                            ).show();
                            finish();
                        }
                    }
                }
        );
    }

    // Save Car
    private void saveCar() {

        String brand = txtBrand.getText().toString().trim();

        String model = txtModel.getText().toString().trim();

        String priceText = txtPrice.getText().toString().trim();

        int year = numberPickerYear.getValue();

        String imageUrl = txtImageUrl.getText().toString().trim();

        // Check Empty Fields
        if (brand.isEmpty()
                || model.isEmpty()
                || priceText.isEmpty()
                || imageUrl.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please fill in all required fields",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Check Image URL
        if (!imageUrl.startsWith("https://")
                && !imageUrl.startsWith("http://")) {

            txtImageUrl.setError("Please enter a valid image URL");
            return;
        }

        // Convert Price
        double price;

        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {

            Toast.makeText(
                    this,
                    "Please enter a valid price",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (!Double.isFinite(price) || price <= 0) {
            txtPrice.setError("Invalid car price");
            return;
        }

        // Update Car
        Map<String, Object> updates = new HashMap<>();

        updates.put("brand", brand);
        updates.put("model", model);
        updates.put("price", price);
        updates.put("year", year);
        updates.put("imageUrl", imageUrl);

        btnSaveCar.setEnabled(false);

        carRef.updateChildren(updates)
                .addOnSuccessListener(aVoid -> {

                    Toast.makeText(
                            EditCarActivity.this,
                            "Car updated successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                })
                .addOnFailureListener(e -> {

                    btnSaveCar.setEnabled(true);

                    Toast.makeText(
                            EditCarActivity.this,
                            "Failed to update car: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    // Delete Car
    private void deleteCar() {

        btnDeleteCar.setEnabled(false);

        carRef.removeValue()
                .addOnSuccessListener(aVoid -> {

                    Toast.makeText(
                            EditCarActivity.this,
                            "Car deleted successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                })
                .addOnFailureListener(e -> {

                    btnDeleteCar.setEnabled(true);

                    Toast.makeText(
                            EditCarActivity.this,
                            "Failed to delete car: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
}