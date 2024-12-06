package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class GreenhouseDetailActivity extends AppCompatActivity {

    private TextView greenhouseNameTextView, squareFeetTextView, locationTextView, temperatureTextView, humidityTextView;
    private String greenhouseId;
    private Map<String, Switch> pumpSwitches = new HashMap<>();
    private int pumpCount = 0;
    private static final int MAX_PUMPS = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greenhouse_detail);

        greenhouseNameTextView = findViewById(R.id.greenhouseNameTextView);
        squareFeetTextView = findViewById(R.id.squareFeetTextView);
        locationTextView = findViewById(R.id.locationTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView); // New TextView for temperature
        humidityTextView = findViewById(R.id.humidityTextView); // New TextView for humidity

        greenhouseId = getIntent().getStringExtra("greenhouseId");

        if (greenhouseId != null) {
            loadGreenhouseDetails(greenhouseId);
        } else {
            Toast.makeText(this, "Error: No Greenhouse ID", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if no ID
        }

        Button addPumpButton = findViewById(R.id.addPumpButton);
        addPumpButton.setOnClickListener(v -> addPump());
    }

    private void loadGreenhouseDetails(String greenhouseId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Greenhouses").child(greenhouseId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Greenhouse greenhouse = dataSnapshot.getValue(Greenhouse.class);
                    if (greenhouse != null) {
                        greenhouseNameTextView.setText(greenhouse.getName());
                        squareFeetTextView.setText("Area: " + greenhouse.getSquareFeet() + " sq. ft.");
                        locationTextView.setText("Location: " + greenhouse.getLocation());

                        // Set temperature and humidity
                        String temperature = "Temperature: " + greenhouse.getTemperature() + "°C";
                        String humidity = "Humidity: " + greenhouse.getHumidity() + "%";
                        temperatureTextView.setText(temperature);
                        humidityTextView.setText(humidity);
                    }
                } else {
                    Toast.makeText(GreenhouseDetailActivity.this, "Greenhouse not found", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity if no greenhouse data found
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(GreenhouseDetailActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                finish(); // Close activity if database fetch fails
            }
        });
    }

    private void addPump() {
        if (pumpCount >= MAX_PUMPS) {
            Toast.makeText(this, "Maximum number of pumps reached", Toast.LENGTH_SHORT).show();
            return;
        }

        pumpCount++;
        Switch pumpSwitch = new Switch(GreenhouseDetailActivity.this);
        pumpSwitch.setChecked(false);
        pumpSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> updatePumpState("pump" + pumpCount, isChecked));

        LinearLayout pumpLayout = new LinearLayout(GreenhouseDetailActivity.this);
        pumpLayout.setOrientation(LinearLayout.HORIZONTAL);
        pumpLayout.setPadding(10, 10, 10, 10);

        TextView pumpIdTextView = new TextView(GreenhouseDetailActivity.this);
        pumpIdTextView.setText("Pump " + pumpCount);
        pumpIdTextView.setTextSize(18);
        pumpIdTextView.setTextColor(getResources().getColor(R.color.primary_Strincolor));

        pumpLayout.addView(pumpIdTextView);
        pumpLayout.addView(pumpSwitch);

        LinearLayout pumpListLayout = findViewById(R.id.pumpList);
        pumpListLayout.addView(pumpLayout);

        DatabaseReference greenhouseRef = FirebaseDatabase.getInstance().getReference("Greenhouses").child(greenhouseId);
        greenhouseRef.child("pumps").child("pump" + pumpCount).setValue(false);
    }

    private void updatePumpState(String pumpId, boolean state) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Greenhouses").child(greenhouseId).child("pumps");
        databaseReference.child(pumpId).setValue(state);
    }
}
