package com.pewush.authentication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdmAssignAssetActivity extends AppCompatActivity {

    EditText editUserId;
    Spinner assetSpinner;
    Button btnAssign;
    FirebaseFirestore db;

    ArrayList<String> assetIds = new ArrayList<>();
    ArrayList<String> assetDisplayList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_asset);

        editUserId = findViewById(R.id.editUserId);
        assetSpinner = findViewById(R.id.assetSpinner);
        btnAssign = findViewById(R.id.btnAssign);
        db = FirebaseFirestore.getInstance();

        loadAvailableAssets();

        btnAssign.setOnClickListener(view -> assignAssetToUser());
    }

    private void loadAvailableAssets() {
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
                    assetSpinner.setAdapter(adapter);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load assets", Toast.LENGTH_SHORT).show());
    }

    private void assignAssetToUser() {
        String userId = editUserId.getText().toString().trim();
        int selectedPosition = assetSpinner.getSelectedItemPosition();

        if (userId.isEmpty() || selectedPosition < 0 || selectedPosition >= assetIds.size()) {
            Toast.makeText(this, "Enter valid user ID and select an asset", Toast.LENGTH_SHORT).show();
            return;
        }

        String assetId = assetIds.get(selectedPosition);
        DocumentReference assetRef = db.collection("Assets").document(assetId);

        // Update asset as assigned
        Map<String, Object> update = new HashMap<>();
        update.put("status","assigned");
        update.put("assignedTo", userId);

        assetRef.update(update)
                .addOnSuccessListener(unused -> Toast.makeText(this, "Asset Assigned Successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to assign asset", Toast.LENGTH_SHORT).show());
    }
}