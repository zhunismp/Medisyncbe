package com.mahidol.drugapi.notification.models;

public interface NotificationMessage {
    String getDeviceToken();

    String getAllDataJsonStr();
}
