package com.pewush.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserRequestAssetActivity extends AppCompatActivity {
    Spinner assetTypeSpinner;
    Button submitRequestButton;
    Spinner getAssetSpinner;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ArrayList<String> assetDisplayList = new ArrayList<>();
    ArrayList<String> assetIds = new ArrayList<>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_asset);
        assetTypeSpinner = findViewById(R.id.assetTypeSpinner);
        submitRequestButton = findViewById(R.id.submitRequestButton);
        getAssetSpinner=findViewById(R.id.assetSpinner);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String[] assetTypes = {"Laptop", "Monitor", "Mouse", "Keyboard", "Printer", "Mobile"};
        ArrayAdapter<String> Adapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item, assetTypes);
        Adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown); // Use same or custom dropdown layout
        assetTypeSpinner.setAdapter(Adapter);
        loadAvailableAssets();
        submitRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedAsset = assetTypeSpinner.getSelectedItem().toString();
                String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

                if (userId == null) {
                    Toast.makeText(UserRequestAssetActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> requestData = new HashMap<>();
                requestData.put("userId", userId);
                requestData.put("assetType", selectedAsset);
                requestData.put("status", "pending");
                requestData.put("timestamp", System.currentTimeMillis());

                db.collection("assetRequests")
                        .add(requestData)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(UserRequestAssetActivity.this, "Request submitted successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity if needed
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(UserRequestAssetActivity.this, "Failed to submit request", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
    public void loadAvailableAssets() {
        db.collection("Assets")
                .whereEqualTo("status", "available")
                .get()
                .addOnSuccessListener(querySnapshots -> {
                    assetDisplayList.clear();
                    assetIds.clear();
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        String id = doc.getId();
                        String name = doc.getString("assetName");
                        String type = doc.getString("assetType");
                        assetDisplayList.add(name + " (" + type + ")");
                        assetIds.add(id);
                    }
                    adapter = new ArrayAdapter<>(this, R.layout.custom_spinner_dropdown, assetDisplayList);
                    getAssetSpinner.setAdapter(adapter);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load assets", Toast.LENGTH_SHORT).show());
    }
}