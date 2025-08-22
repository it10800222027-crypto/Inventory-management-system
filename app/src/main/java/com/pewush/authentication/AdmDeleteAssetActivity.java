package com.pewush.authentication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdmDeleteAssetActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdmDeleteAssetAdapter adapter;
    private ArrayList<AdmViewAssetModel> assetList;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_asset);
        recyclerView = findViewById(R.id.deleteAssetRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        assetList = new ArrayList<>();
        adapter = new AdmDeleteAssetAdapter(assetList, this);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        fetchAssets();
    }
    private void fetchAssets() {
        db.collection("Assets")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    assetList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String id = doc.getId();
                        String name = doc.getString("assetName");
                        assetList.add(new AdmViewAssetModel(id, name));
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(AdmDeleteAssetActivity.this, "Failed to fetch assets", Toast.LENGTH_SHORT).show()
                );
    }

    public void deleteAsset(String docId, int position) {
        db.collection("Assets").document(docId)
                .delete()
                .addOnSuccessListener(unused -> {
                    assetList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, "Asset deleted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show()
                );
    }
}