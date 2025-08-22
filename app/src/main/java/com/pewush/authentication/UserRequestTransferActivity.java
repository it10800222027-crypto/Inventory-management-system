package com.pewush.authentication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class UserRequestTransferActivity extends AppCompatActivity {

    Spinner assetSpinner;
    EditText toUserEmailEditText;
    Button requestTransferBtn;

    FirebaseFirestore db;
    FirebaseAuth auth;

    ArrayList<AdmViewAssetModel> myAssets;
    ArrayList<String> assetDisplayList;
    ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_transfer);

        assetSpinner = findViewById(R.id.assetSpinner);
        toUserEmailEditText = findViewById(R.id.toUserEmailEditText);
        requestTransferBtn = findViewById(R.id.requestTransferBtn);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        myAssets = new ArrayList<>();
        assetDisplayList = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item, assetDisplayList);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown); // Use same or custom dropdown layout
        assetSpinner.setAdapter(spinnerAdapter);

        loadMyAssets();

        requestTransferBtn.setOnClickListener(v -> requestTransfer());
    }

    private void loadMyAssets() {
        String userId = auth.getCurrentUser().getUid();
        String userEmail=auth.getCurrentUser().getEmail();
        db.collection("Assets")
                .whereIn("assignedTo", Arrays.asList(userEmail, userId))
                .get()
                .addOnSuccessListener(snapshot -> {
                    myAssets.clear();
                    assetDisplayList.clear();
                    for (QueryDocumentSnapshot doc : snapshot) {
                        AdmViewAssetModel asset = doc.toObject(AdmViewAssetModel.class);
                        myAssets.add(asset);
                        assetDisplayList.add(asset.getAssetName() + " (" + asset.getAssetId() + ")");
                    }
                    spinnerAdapter.notifyDataSetChanged();
                });
    }

    private void requestTransfer() {
        String toEmail = toUserEmailEditText.getText().toString().trim();
        int selectedPosition = assetSpinner.getSelectedItemPosition();

        if (toEmail.isEmpty() || selectedPosition == -1) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
            return;
        }

        AdmViewAssetModel selectedAsset = myAssets.get(selectedPosition);

        // Prepare request data
        TransferRequest request = new TransferRequest(
                auth.getCurrentUser().getEmail(),
                toEmail,
                selectedAsset.getAssetId(),
                selectedAsset.getAssetName(),
                "pending"
        );

        db.collection("transferRequests")
                .add(request)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(this, "Transfer request sent", Toast.LENGTH_SHORT).show();
                    finish(); // close activity
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to request transfer", Toast.LENGTH_SHORT).show());
    }

    public static class TransferRequest {
        public String fromUser, toUser, assetId, assetName, status;

        public TransferRequest() {}

        public TransferRequest(String fromUser, String toUser, String assetId, String assetName, String status) {
            this.fromUser = fromUser;
            this.toUser = toUser;
            this.assetId = assetId;
            this.assetName = assetName;
            this.status = status;
        }
    }
}