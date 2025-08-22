package com.pewush.authentication;

public class AdmViewRequestModel {
    String userId;
    String assetType;
    String status;
    String userName, requestId;

    public AdmViewRequestModel() {}
    public AdmViewRequestModel(String userId, String userName, String assetType, String status, String requestId) {
        this.userId = userId;
        this.userName = userName;
        this.assetType = assetType;
        this.status = status;
        this.requestId = requestId;
    }
    public String getUserName() {return userName;}
    public String getUserId() { return userId; }
    public String getAssetType() { return assetType; }
    public String getStatus() { return status; }
    public String getRequestId() { return requestId; }
}
