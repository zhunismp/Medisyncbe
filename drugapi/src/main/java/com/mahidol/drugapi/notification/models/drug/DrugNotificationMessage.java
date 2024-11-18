package com.mahidol.drugapi.notification.models.drug;

import com.google.gson.Gson;
import com.mahidol.drugapi.drug.dtos.response.DrugDTO;
import com.mahidol.drugapi.notification.models.NotificationMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DrugNotificationMessage implements NotificationMessage {

    private String deviceToken;
    private DrugDTO drug;


    @Override
    public String getAllDataJsonStr() {
        Gson parser = new Gson();
        return parser.toJson(drug);
    }
}
