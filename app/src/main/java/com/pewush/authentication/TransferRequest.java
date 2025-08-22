package com.pewush.authentication;

public class TransferRequest {
    private String fromUser;
    private String toUser;
    private String assetId;
    private String assetName;
    private String status;

    public TransferRequest() {}

    public TransferRequest(String fromUser, String toUser, String assetId, String assetName, String status) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.assetId = assetId;
        this.assetName = assetName;
        this.status = status;
    }

    public String getFromUser() { return fromUser; }
    public String getToUser() { return toUser; }
    public String getAssetId() { return assetId; }
    public String getAssetName() { return assetName; }
    public String getStatus() { return status; }

    public void setFromUser(String fromUser) { this.fromUser = fromUser; }
    public void setToUser(String toUser) { this.toUser = toUser; }
    public void setAssetId(String assetId) { this.assetId = assetId; }
    public void setAssetName(String assetName) { this.assetName = assetName; }
    public void setStatus(String status) { this.status = status; }
}
