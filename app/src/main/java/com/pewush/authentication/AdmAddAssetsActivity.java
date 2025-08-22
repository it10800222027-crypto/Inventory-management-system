package com.pewush.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdmAddAssetsActivity extends AppCompatActivity {
    private EditText assetNameInput, assetIdInput, assetTypeInput;
    Button addAssetBtn;
    private FirebaseFirestore firestore;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_assets);

        assetNameInput = findViewById(R.id.assetName);
        assetIdInput = findViewById(R.id.assetId);
        assetTypeInput = findViewById(R.id.assetType);
        addAssetBtn = findViewById(R.id.addAssetButton);

        firestore = FirebaseFirestore.getInstance();

        addAssetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String assetName = assetNameInput.getText().toString().trim();
                String assetId = assetIdInput.getText().toString().trim();
                String assetType = assetTypeInput.getText().toString().trim();

                if (assetName.isEmpty() || assetId.isEmpty() || assetType.isEmpty()) {
                    Toast.makeText(AdmAddAssetsActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> asset = new HashMap<>();
                asset.put("assetName", assetName);
                asset.put("assetId", assetId);
                asset.put("assetType", assetType);
                asset.put("assignedTo", null);
                asset.put("timestamp", System.currentTimeMillis());
                asset.put("status", "available");
                firestore.collection("Assets")
                        .document(assetId)
                        .set(asset)
                        .addOnSuccessListener(unused ->
                                Toast.makeText(AdmAddAssetsActivity.this, "Asset added successfully!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(AdmAddAssetsActivity.this, "Failed to add asset.", Toast.LENGTH_SHORT).show());
            }
        });
    }
}