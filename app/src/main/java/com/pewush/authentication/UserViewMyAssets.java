package com.pewush.authentication;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class UserViewMyAssets extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<AdmViewAssetModel> myAssetList;
    AdmViewAssetAdapter adapter;
    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_assets);

        recyclerView = findViewById(R.id.myAssetsRecyclerView);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        myAssetList = new ArrayList<>();
        adapter = new AdmViewAssetAdapter(myAssetList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadAssetsAssignedToUser();
    }

    private void loadAssetsAssignedToUser() {
        String userId = auth.getCurrentUser().getUid();
        String userEmail = auth.getCurrentUser().getEmail();
        db.collection("Assets")
                .whereIn("assignedTo", Arrays.asList(userEmail, userId))
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    myAssetList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        AdmViewAssetModel asset = doc.toObject(AdmViewAssetModel.class);
                        myAssetList.add(asset);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("MyAssets", "Error loading assets", e));
    }
}