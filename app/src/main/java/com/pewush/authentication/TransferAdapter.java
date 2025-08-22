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
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.ViewHolder> {

    List<TransferRequest> transferList;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public TransferAdapter(List<TransferRequest> transferList, Context context) {
        this.transferList = transferList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fromUser, toUser, assetName, status;
        Button btnApprove, btnReject;

        public ViewHolder(View itemView) {
            super(itemView);
            fromUser = itemView.findViewById(R.id.textFromUser);
            toUser = itemView.findViewById(R.id.textToUser);
            assetName = itemView.findViewById(R.id.textAssetName);
            status = itemView.findViewById(R.id.textStatus);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }

    @NonNull
    @Override
    public TransferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transfer_request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransferAdapter.ViewHolder holder, int position) {
        TransferRequest model = transferList.get(position);

        holder.fromUser.setText("From: " + model.getFromUser());
        holder.toUser.setText("To: " + model.getToUser());
        holder.assetName.setText("Asset: " + model.getAssetName() + " (" + model.getAssetId() + ")");
        holder.status.setText("Status: " + model.getStatus());

        if (!model.getStatus().equalsIgnoreCase("pending")) {
            holder.btnApprove.setVisibility(View.GONE);
            holder.btnReject.setVisibility(View.GONE);
        }

        holder.btnApprove.setOnClickListener(v -> {
            db.collection("Assets")
                    .whereEqualTo("assetId", model.getAssetId())
                    .limit(1)
                    .get()
                    .addOnSuccessListener(snapshot -> {
                        if (!snapshot.isEmpty()) {
                            String docId = snapshot.getDocuments().get(0).getId();
                            db.collection("Assets").document(docId)
                                    .update("assignedTo", model.getToUser())
                                    .addOnSuccessListener(unused -> {
                                        updateTransferStatus(model, "approved", holder);
                                    });
                        } else {
                            Toast.makeText(context, "Asset not found", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        holder.btnReject.setOnClickListener(v -> {
            updateTransferStatus(model, "rejected", holder);
        });
    }

    private void updateTransferStatus(TransferRequest model, String statusValue , ViewHolder holder) {
        db.collection("transferRequests")
                .whereEqualTo("assetId", model.getAssetId())
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(snapshot -> {
                    for (QueryDocumentSnapshot doc : snapshot) {
                        db.collection("transferRequests").document(doc.getId())
                                .update("status", "assigned")
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(context, "Transfer " + statusValue, Toast.LENGTH_SHORT).show();
                                    transferList.get(holder.getAdapterPosition()).setStatus(statusValue);
                                    notifyItemChanged(holder.getAdapterPosition());
                                });
                    }
                });
    }

    @Override
    public int getItemCount() {
        return transferList.size();
    }
}