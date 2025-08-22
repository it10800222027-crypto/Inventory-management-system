package com.pewush.authentication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdmViewAllAssets extends AppCompatActivity {
    RecyclerView recyclerView;
    List<AdmViewAssetModel> assetList;
    AdmViewAssetAdapter adapter;
    FirebaseFirestore db;
    private long backPressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_assets);
        recyclerView = findViewById(R.id.assetRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assetList = new ArrayList<>();
        adapter = new AdmViewAssetAdapter(assetList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadAssetsFromFirestore();
    }
    private void loadAssetsFromFirestore() {
        db.collection("Assets")  // your collection name
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    assetList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        AdmViewAssetModel asset = doc.toObject(AdmViewAssetModel.class);
                        assetList.add(asset);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading assets", Toast.LENGTH_SHORT).show();
                });
    }
}