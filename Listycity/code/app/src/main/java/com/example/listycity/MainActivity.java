package com.example.listycity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ListView cityList;
    ArrayAdapter<String> cityAdapter;
    ArrayList<String> datalist;
    Button addCityButton;
    Button deleteCityButton;
    int selectedPosition = -1;

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

        // Initialize views
        cityList = findViewById(R.id.city_list);
        addCityButton = findViewById(R.id.add_city_button);
        deleteCityButton = findViewById(R.id.delete_city_button);

        // Initialize data
        String []cities = {"Edmonton", "Vancouver", "Moscow", "Sydney", "Berlin", "Vienna", "Tokyo", "Beijing", "osaka", "New Delhi"};
        datalist = new ArrayList<>();
        datalist.addAll(Arrays.asList(cities));

        // adapter
        cityAdapter = new ArrayAdapter<>(this, R.layout.content, datalist);
        cityList.setAdapter(cityAdapter);

        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                String selectedCity = datalist.get(position);
                Toast.makeText(MainActivity.this,
                        "Selected: " + selectedCity,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Set OnClickListener for ADD CITY button
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCityDialog();
            }
        });

        // Set OnClickListener for DELETE CITY button
        deleteCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedCity();
            }
        });
    }


    //Shows an AlertDialog to allow the user to input a new city name
    private void showAddCityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New City");

        final EditText input = new EditText(this);
        input.setHint("Enter city name");
        input.setPadding(50, 20, 50, 20);
        builder.setView(input);

        builder.setPositiveButton("CONFIRM", (dialog, which) -> {
            String cityName = input.getText().toString().trim();
            if (!cityName.isEmpty()) {
                datalist.add(cityName);
                cityAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,
                        "Added: " + cityName,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this,
                        "City name cannot be empty",
                        Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * Deletes the currently selected city from the list
     */
    private void deleteSelectedCity() {
        if (selectedPosition != -1 && selectedPosition < datalist.size()) {
            String removedCity = datalist.get(selectedPosition);
            datalist.remove(selectedPosition);
            cityAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this,
                    "Deleted: " + removedCity,
                    Toast.LENGTH_SHORT).show();
            // Reset selection
            selectedPosition = -1;
        } else {
            // No city selected - prompt user
            Toast.makeText(MainActivity.this,
                    "Please select a city first by tapping it",
                    Toast.LENGTH_SHORT).show();
        }
    }
}