package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DisplayActivity extends AppCompatActivity {

    private LinearLayout greenhouseList;
    private ImageButton addButton;
    private TextView userNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        greenhouseList = findViewById(R.id.greenhouseList);
        addButton = findViewById(R.id.addButton);
        userNameTextView = findViewById(R.id.userName);

        // Update user name from Firebase
        updateUserName();

        // Add button listener to navigate to AddGreenhouseActivity
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(DisplayActivity.this, AddGreenhouseActivity.class);
                startActivity(addIntent);
            }
        });

        // Fetch and display greenhouse data from Firebase
        loadGreenhouses();
    }

    private void updateUserName() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                userNameTextView.setText(displayName);
            } else {
                userNameTextView.setText("User"); // Fallback if display name is not set
            }
        } else {
            userNameTextView.setText("Guest");
        }
    }

    private void loadGreenhouses() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Greenhouses");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                greenhouseList.removeAllViews(); // Clear the list before adding new data

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String greenhouseId = snapshot.getKey();
                    String greenhouseName = snapshot.child("name").getValue(String.class);
                    String squareFeet = snapshot.child("squareFeet").getValue(String.class);
                    String location = snapshot.child("location").getValue(String.class);

                    // Add greenhouse card to the list
                    addGreenhouseCard(greenhouseName, squareFeet, location, greenhouseId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DisplayActivity.this, "Failed to load greenhouses", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addGreenhouseCard(String greenhouseName, String squareFeet, String location, String greenhouseId) {
        View greenhouseCard = getLayoutInflater().inflate(R.layout.greenhouse_card, null);

        TextView greenhouseNameTextView = greenhouseCard.findViewById(R.id.greenhouseNameTextView);
        TextView squareFeetTextView = greenhouseCard.findViewById(R.id.squareFeetTextView);
        TextView locationTextView = greenhouseCard.findViewById(R.id.locationTextView);

        greenhouseNameTextView.setText(greenhouseName);
        squareFeetTextView.setText("Area: " + squareFeet + " sq. ft.");
        locationTextView.setText("Location: " + location);

        // Set click listener to navigate to GreenhouseDetailActivity
        greenhouseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if greenhouseId is not null before navigating
                if (greenhouseId != null) {
                    Intent intent = new Intent(DisplayActivity.this, GreenhouseDetailActivity.class);
                    intent.putExtra("greenhouseId", greenhouseId);
                    startActivity(intent);
                } else {
                    Toast.makeText(DisplayActivity.this, "Error: No Greenhouse ID", Toast.LENGTH_SHORT).show();
                }
            }
        });

        greenhouseList.addView(greenhouseCard);
    }
}
