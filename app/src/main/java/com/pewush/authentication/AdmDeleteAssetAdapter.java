package com.pewush.authentication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdmDeleteAssetAdapter extends RecyclerView.Adapter<AdmDeleteAssetAdapter.ViewHolder> {

    private ArrayList<AdmViewAssetModel> assetList;
    private AdmDeleteAssetActivity context;

    public AdmDeleteAssetAdapter(ArrayList<AdmViewAssetModel> assetList, AdmDeleteAssetActivity context) {
        this.assetList = assetList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdmDeleteAssetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_delete_asset, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdmDeleteAssetAdapter.ViewHolder holder, int position) {
        AdmViewAssetModel asset = assetList.get(position);
        holder.assetName.setText(asset.getAssetName());

        holder.deleteBtn.setOnClickListener(v -> {
            context.deleteAsset(asset.getAssetId(), position);
        });
    }

    @Override
    public int getItemCount() {
        return assetList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView assetName;
        ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            assetName = itemView.findViewById(R.id.assetNameText);
            deleteBtn = itemView.findViewById(R.id.deleteButton);
        }
    }
}
