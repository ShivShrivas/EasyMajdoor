package com.app.easymajdoor.model;

public class NewRequestForMazdoor {
    String UserId,MazdoorType,MazdoorId,MazdoorLat,MazdoorLong;
    public NewRequestForMazdoor(){}
    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getMazdoorType() {
        return MazdoorType;
    }

    public void setMazdoorType(String mazdoorType) {
        MazdoorType = mazdoorType;
    }

    public String getMazdoorId() {
        return MazdoorId;
    }

    public void setMazdoorId(String mazdoorId) {
        MazdoorId = mazdoorId;
    }

    public String getMazdoorLat() {
        return MazdoorLat;
    }

    public void setMazdoorLat(String mazdoorLat) {
        MazdoorLat = mazdoorLat;
    }

    public String getMazdoorLong() {
        return MazdoorLong;
    }

    public void setMazdoorLong(String mazdoorLong) {
        MazdoorLong = mazdoorLong;
    }

    public NewRequestForMazdoor(String userId, String mazdoorType, String mazdoorId, String mazdoorLat, String mazdoorLong) {
        UserId = userId;
        MazdoorType = mazdoorType;
        MazdoorId = mazdoorId;
        MazdoorLat = mazdoorLat;
        MazdoorLong = mazdoorLong;
    }
}
