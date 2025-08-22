package com.pewush.authentication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdmViewTransferActivity extends AppCompatActivity {

    RecyclerView requestsRecyclerView;
    TransferAdapter adapter;
    ArrayList<TransferRequest> transferList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_transfer_request);

        requestsRecyclerView = findViewById(R.id.requestsRecyclerView);
        transferList = new ArrayList<>();
        adapter = new TransferAdapter(transferList, this);

        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestsRecyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadTransferRequests();
    }

    private void loadTransferRequests() {
        db.collection("transferRequests")
                .get()
                .addOnSuccessListener(snapshot -> {
                    transferList.clear();
                    for (QueryDocumentSnapshot doc : snapshot) {
                        TransferRequest request = doc.toObject(TransferRequest.class);
                        transferList.add(request);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load transfer requests", Toast.LENGTH_SHORT).show();
                });
    }
}
