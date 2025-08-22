package com.pewush.authentication;

public class AdmViewAssetModel {
    private String assetName, assetId, assetType, status;

    public AdmViewAssetModel() {} // Needed for FireStore

    public AdmViewAssetModel(String name, String id, String type, String status) {
        this.assetName = name;
        this.assetId = id;
        this.assetType = type;
        this.status=status;
    }
    public AdmViewAssetModel(String assetId, String assetName) {
        this.assetId = assetId;
        this.assetName = assetName;
    }
    public String getAssetName() { return assetName; }
    public String getAssetId() { return assetId; }
    public String getAssetType() { return assetType; }
    public String getStatus() {return status; }
}
