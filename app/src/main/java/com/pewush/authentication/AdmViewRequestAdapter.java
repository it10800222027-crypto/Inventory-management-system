package com.pewush.authentication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdmViewRequestAdapter extends RecyclerView.Adapter<AdmViewRequestAdapter.ViewHolder> {

    private final ArrayList<AdmViewRequestModel> requestList;
    private final Context context;
    FirebaseFirestore db;
    public AdmViewRequestAdapter(ArrayList<AdmViewRequestModel> requestList, Context context) {
        this.requestList = requestList;
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userId, assetType, status;
        Button btnApprove, btnReject;
        public ViewHolder(View view) {
            super(view);
            userId = view.findViewById(R.id.textUserId);
            assetType = view.findViewById(R.id.textAssetType);
            status = view.findViewById(R.id.textStatus);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }

    @NonNull
    @Override
    public AdmViewRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdmViewRequestModel model = requestList.get(position);
        holder.userId.setText("User ID: " + model.getUserId());
        holder.assetType.setText("Asset: " + model.getAssetType());
        holder.status.setText("Status: " + model.getStatus());
        if (!model.getStatus().equalsIgnoreCase("pending")) {
            holder.btnApprove.setVisibility(View.GONE);
            holder.btnReject.setVisibility(View.GONE);
        }

        holder.btnApprove.setOnClickListener(v -> {
            db.collection("Assets")
                    .whereEqualTo("assetType", model.getAssetType())
                    .whereEqualTo("status", "available")
                    .limit(1)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            String assetId = queryDocumentSnapshots.getDocuments().get(0).getId();

                            db.collection("Assets").document(assetId)
                                    .update("status", "assigned", "assignedTo", model.getUserId())
                                    .addOnSuccessListener(unused -> {
                                        // Now update the request status to "approved"
                                        db.collection("assetRequests")
                                                .document(model.getRequestId())
                                                .update("status", "approved")
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(context, "Approved & Asset Assigned", Toast.LENGTH_SHORT).show();
                                                    model.status = "assigned";
                                                    notifyItemChanged(holder.getAdapterPosition());
                                                });
                                    });
                        } else {
                            Toast.makeText(context, "No available asset of this type", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error checking assets", Toast.LENGTH_SHORT).show();
                    });
        });
        holder.btnReject.setOnClickListener(v -> {
            db.collection("assetRequests")
                    .document(model.getRequestId())
                    .update("status", "rejected")
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(context, "Rejected", Toast.LENGTH_SHORT).show();
                        model.status = "rejected";
                        notifyItemChanged(holder.getAdapterPosition());
                    });
        });
    }
    @Override
    public int getItemCount() {
        return requestList.size();
    }
}
