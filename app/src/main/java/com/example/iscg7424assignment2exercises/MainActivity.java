package com.example.iscg7424assignment2exercises;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iscg7424assignment2exercises.exercisea.AddCarActivity;
import com.example.iscg7424assignment2exercises.exercisea.CarListActivity;
import com.example.iscg7424assignment2exercises.exerciseb.ApiActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnExerciseA, btnExerciseB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnExerciseA = findViewById(R.id.btnExerciseA);
        btnExerciseB = findViewById(R.id.btnExerciseB);
        btnExerciseA.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    CarListActivity.class
            );
            startActivity(intent);
        });
        btnExerciseB.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    ApiActivity.class
            );
            startActivity(intent);
        });
    }
}