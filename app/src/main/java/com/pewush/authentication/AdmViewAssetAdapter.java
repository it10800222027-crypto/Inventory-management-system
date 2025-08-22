package com.pewush.authentication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdmViewAssetAdapter extends RecyclerView.Adapter<AdmViewAssetAdapter.AssetViewHolder> {
    private List<AdmViewAssetModel> assetList;

    public AdmViewAssetAdapter(List<AdmViewAssetModel> assetList) {
        this.assetList = assetList;
    }

    @NonNull
    @Override
    public AssetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_item, parent, false);
        return new AssetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetViewHolder holder, int position) {
        AdmViewAssetModel asset = assetList.get(position);
        holder.name.setText(asset.getAssetName());
        holder.id.setText("ID: " + asset.getAssetId());
        holder.type.setText("Type: " + asset.getAssetType());
        holder.status.setText("status: "+asset.getStatus());
    }

    @Override
    public int getItemCount() {
        return assetList.size();
    }

    public static class AssetViewHolder extends RecyclerView.ViewHolder {
        TextView name, id, type, status;

        public AssetViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.assetNameText);
            id = itemView.findViewById(R.id.assetIdText);
            type = itemView.findViewById(R.id.assetTypeText);
            status=itemView.findViewById(R.id.assetStatus);
        }
    }
}
