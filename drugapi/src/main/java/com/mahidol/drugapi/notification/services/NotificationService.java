package com.mahidol.drugapi.notification.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.mahidol.drugapi.notification.models.NotificationMessage;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendNotification(NotificationMessage message) {
        Message m = Message.builder()
                .putData("data", message.getAllDataJsonStr())
                .setToken(message.getDeviceToken())
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(m);
            System.out.println("Succeed sending notification: " + response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
