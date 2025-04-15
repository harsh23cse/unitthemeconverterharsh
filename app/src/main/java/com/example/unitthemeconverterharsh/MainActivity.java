package com.example.unitthemeconverterharsh;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    EditText inputValue;
    Spinner fromUnitSpinner, toUnitSpinner;
    Button convertButton;
    TextView resultText;
    String[] units = {"Metre", "Centimetre", "Inch", "Foot", "Yard"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // ✅ Apply theme before super.onCreate and setContentView
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link UI
        inputValue = findViewById(R.id.inputValue);
        fromUnitSpinner = findViewById(R.id.fromUnitSpinner);
        toUnitSpinner = findViewById(R.id.toUnitSpinner);
        convertButton = findViewById(R.id.convertButton);
        resultText = findViewById(R.id.resultText);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromUnitSpinner.setAdapter(adapter);
        toUnitSpinner.setAdapter(adapter);

        convertButton.setOnClickListener(v -> convert());

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    // 🔄 Reload theme if user changes it in Settings
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("dark_mode", false);
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        int desiredMode = isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;

        if (currentMode != desiredMode) {
            AppCompatDelegate.setDefaultNightMode(desiredMode);
            recreate(); // Apply updated theme without flicker
        }
    }

    private void convert() {
        String inputStr = inputValue.getText().toString().trim();
        if (inputStr.isEmpty()) {
            resultText.setText("Please enter a value.");
            return;
        }

        double input = Double.parseDouble(inputStr);
        String fromUnit = fromUnitSpinner.getSelectedItem().toString();
        String toUnit = toUnitSpinner.getSelectedItem().toString();

        double metreValue = toMetres(input, fromUnit);
        double result = fromMetres(metreValue, toUnit);

        resultText.setText("Result : " + String.format("%.4f", result) + " " + toUnit);
    }

    private double toMetres(double value, String unit) {
        switch (unit) {
            case "Centimetre": return value / 100;
            case "Inch": return value * 0.0254;
            case "Foot": return value * 0.3048;
            case "Yard": return value * 0.9144;
            default: return value;
        }
    }

    private double fromMetres(double value, String unit) {
        switch (unit) {
            case "Centimetre": return value * 100;
            case "Inch": return value / 0.0254;
            case "Foot": return value / 0.3048;
            case "Yard": return value / 0.9144;
            default: return value;
        }
    }
}