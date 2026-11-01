package com.example.iscg7424assignment2exercises.exercisea;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iscg7424assignment2exercises.R;
import com.example.iscg7424assignment2exercises.exercisea.model.car;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddCarActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    EditText txtImageUrl, txtBrand, txtModel, txtPrice;
    MaterialButton btnAddCar;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_car);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("cars");
        txtImageUrl = findViewById(R.id.txtImageUrl);
        txtBrand = findViewById(R.id.txtBrand);
        txtModel = findViewById(R.id.txtModel);
        NumberPicker numberPickerYear = findViewById(R.id.numberPickerYear);
        numberPickerYear.setMinValue(1980);
        numberPickerYear.setMaxValue(Calendar.getInstance().get(Calendar.YEAR) + 1
        );
        numberPickerYear.setValue(2020);
        txtPrice = findViewById(R.id.txtPrice);
        btnBack = findViewById(R.id.btnBack);
        btnAddCar = findViewById(R.id.btnAddCar);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageUrl = txtImageUrl.getText().toString().trim();
                String brand = txtBrand.getText().toString().trim();
                String model = txtModel.getText().toString().trim();
                int year = numberPickerYear.getValue();
                String priceText = txtPrice.getText().toString().trim();
                if (imageUrl.isEmpty()
                        || brand.isEmpty()
                        || model.isEmpty()
                        || priceText.isEmpty()) {
                    Toast.makeText(
                            AddCarActivity.this,"Please enter all car details",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                if (!imageUrl.startsWith("https://")
                        && !imageUrl.startsWith("http://")) {
                    txtImageUrl.setError("Please enter a valid image URL");
                    return;
                }
                double price;
                try {
                    price = Double.parseDouble(priceText);
                } catch (NumberFormatException e) {
                    Toast.makeText(
                            AddCarActivity.this,"Please enter a valid year and price",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                if (!Double.isFinite(price) || price <= 0) {
                    txtPrice.setError("Invalid car price");
                    return;
                }
                String carId = reference.push().getKey();
                if (carId == null) {
                    Toast.makeText(
                            AddCarActivity.this,"Failed to generate car ID",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                car newCar = new car(
                        carId,
                        brand,
                        model,
                        year,
                        price,
                        imageUrl
                );
                btnAddCar.setEnabled(false);
                reference.child(carId).setValue(newCar)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(
                                    AddCarActivity.this,
                                    "Car added successfully",
                                    Toast.LENGTH_SHORT
                            ).show();
                            txtImageUrl.setText("");
                            txtBrand.setText("");
                            txtModel.setText("");
                            txtPrice.setText("");
                            btnAddCar.setEnabled(true);
                        });

            }
        });
    }
}