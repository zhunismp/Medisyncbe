package com.mahidol.drugapi.notification.services;

import com.mahidol.drugapi.notification.models.NotificationMessage;

public interface NotificationService {
    void send(NotificationMessage message);
}
