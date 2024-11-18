package com.mahidol.drugapi.notification.models.druggroup;

import com.google.gson.Gson;
import com.mahidol.drugapi.druggroup.dtos.response.DrugGroupDTO;
import com.mahidol.drugapi.notification.models.NotificationMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DrugGroupNotificationMessage implements NotificationMessage {
    private String deviceToken;
    private DrugGroupDTO drugGroup;

    @Override
    public String getAllDataJsonStr() {
        Gson parser = new Gson();
        return parser.toJson(drugGroup);
    }
}
