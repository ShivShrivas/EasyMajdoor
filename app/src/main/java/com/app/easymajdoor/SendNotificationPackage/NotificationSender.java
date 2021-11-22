package com.app.easymajdoor.SendNotificationPackage;

public class NotificationSender {
    public Data data;
    public String to;
    NotificationSender(){}
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public NotificationSender(Data data, String to) {
        this.data = data;
        this.to = to;
    }
}
