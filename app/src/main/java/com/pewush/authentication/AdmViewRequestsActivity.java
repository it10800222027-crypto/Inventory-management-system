package com.pewush.authentication;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class AdmViewRequestsActivity extends AppCompatActivity {

    RecyclerView requestsRecyclerView;
    FirebaseFirestore db;
    ArrayList<AdmViewRequestModel> requestList;
    AdmViewRequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        requestsRecyclerView = findViewById(R.id.requestsRecyclerView);
        db = FirebaseFirestore.getInstance();
        requestList = new ArrayList<>();
        adapter = new AdmViewRequestAdapter(requestList, this);
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestsRecyclerView.setAdapter(adapter);

        fetchRequests();
    }

    private void fetchRequests() {
        db.collection("assetRequests")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    requestList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        AdmViewRequestModel model = new AdmViewRequestModel(
                                doc.getString("userId"),
                                doc.getString("userName"),
                                doc.getString("assetType"),
                                doc.getString("status"),
                                doc.getId()
                        );
                        requestList.add(model);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Failed to load requests", e);
                });
    }
}
