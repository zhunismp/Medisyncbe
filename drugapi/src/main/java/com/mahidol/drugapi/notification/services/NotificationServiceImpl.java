package com.mahidol.drugapi.notification.services;

import com.google.firebase.messaging.*;
import com.mahidol.drugapi.notification.models.NotificationMessage;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    public void send(NotificationMessage message) {
        Message fMessage = transformMessage(message);
        FirebaseMessaging.getInstance().sendAsync(fMessage);
    }

    private ApnsConfig getApnsConfig() {
        return ApnsConfig.builder()
                .setAps(Aps.builder().build())
                .build();
    }

    private Message transformMessage(NotificationMessage message) {
        Notification notification = Notification.builder()
                .setBody(message.getAllDataJsonStr())
                .build();

        return Message.builder()
                .setApnsConfig(getApnsConfig())
                .setNotification(notification)
                .setToken(message.getDeviceToken())
                .build();
    }
}
