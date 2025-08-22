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

public class AdmTransferAssetActivity extends AppCompatActivity {

    EditText editCurrentUserId, editTargetUserId;
    Spinner ownedAssetSpinner;
    Button btnTransfer;
    FirebaseFirestore db;

    ArrayList<String> assetIdList = new ArrayList<>();
    ArrayList<String> assetDisplayList = new ArrayList<>();
    ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_asset);

        editCurrentUserId = findViewById(R.id.editCurrentUserId);
        editTargetUserId = findViewById(R.id.editTargetUserId);
        ownedAssetSpinner = findViewById(R.id.ownedAssetSpinner);
        btnTransfer = findViewById(R.id.btnTransfer);
        db = FirebaseFirestore.getInstance();

        btnTransfer.setOnClickListener(v -> {
            String currentUser = editCurrentUserId.getText().toString().trim();
            String targetUser = editTargetUserId.getText().toString().trim();

            if (currentUser.isEmpty() || targetUser.isEmpty()) {
                Toast.makeText(this, "Enter both user IDs", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedPos = ownedAssetSpinner.getSelectedItemPosition();
            if (selectedPos < 0 || selectedPos >= assetIdList.size()) {
                Toast.makeText(this, "Select a valid asset", Toast.LENGTH_SHORT).show();
                return;
            }

            String assetId = assetIdList.get(selectedPos);
            transferAsset(assetId, targetUser);
        });

        editCurrentUserId.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String userId = editCurrentUserId.getText().toString().trim();
                if (!userId.isEmpty()) {
                    loadUserAssets(userId);
                }
            }
        });
    }

    private void loadUserAssets(String userId) {
        db.collection("Assets")
                .whereEqualTo("assignedTo", userId)
                .get()
                .addOnSuccessListener(querySnapshots -> {
                    assetDisplayList.clear();
                    assetIdList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        String assetName = doc.getString("assetName");
                        String assetType = doc.getString("assetType");
                        assetDisplayList.add(assetName + " (" + assetType + ")");
                        assetIdList.add(doc.getId());
                    }

                    spinnerAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_dropdown, assetDisplayList);
                    ownedAssetSpinner.setAdapter(spinnerAdapter);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load assets", Toast.LENGTH_SHORT).show());
    }

    private void transferAsset(String assetId, String targetUserId) {
        DocumentReference assetRef = db.collection("Assets").document(assetId);
        Map<String, Object> update = new HashMap<>();
        update.put("assignedTo", targetUserId);
        update.put("status","assigned");
        assetRef.update(update)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Asset transferred successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Transfer failed", Toast.LENGTH_SHORT).show();
                });
    }
}