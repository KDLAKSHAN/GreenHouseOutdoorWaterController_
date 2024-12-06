package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddGreenhouseActivity extends AppCompatActivity {

    private EditText greenhouseNameEditText, squareFeetEditText, locationEditText;
    private Button addGreenhouseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_greenhouse);

        greenhouseNameEditText = findViewById(R.id.greenhouseNameEditText);
        squareFeetEditText = findViewById(R.id.squareFeetEditText);
        locationEditText = findViewById(R.id.locationEditText);
        addGreenhouseButton = findViewById(R.id.addGreenhouseButton);

        addGreenhouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String greenhouseName = greenhouseNameEditText.getText().toString();
                String squareFeet = squareFeetEditText.getText().toString();
                String location = locationEditText.getText().toString();

                if (!greenhouseName.isEmpty() && !squareFeet.isEmpty() && !location.isEmpty()) {
                    addGreenhouseToFirebase(greenhouseName, squareFeet, location);
                } else {
                    Toast.makeText(AddGreenhouseActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addGreenhouseToFirebase(String greenhouseName, String squareFeet, String location) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Greenhouses");
        String id = databaseReference.push().getKey();
        Greenhouse greenhouse = new Greenhouse(greenhouseName, squareFeet, location);

        if (id != null) {
            databaseReference.child(id).setValue(greenhouse);
            Toast.makeText(this, "Greenhouse Added!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddGreenhouseActivity.this, DisplayActivity.class);
            startActivity(intent);
        }
    }
}
