package com.mahidol.drugapi.appointment.dtos.response;

import com.mahidol.drugapi.appointment.models.entities.Appointment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AppointmentDTO {
    private UUID id;
    private String title;
    private String medicName;
    private LocalDateTime datetime;
    private String destination;
    private String remark;

    public static AppointmentDTO fromAppointment(Appointment appointment) {
        return new AppointmentDTO()
                .setId(appointment.getId())
                .setDatetime(appointment.getDatetime())
                .setTitle(appointment.getTitle())
                .setMedicName(appointment.getMedicName())
                .setDestination(appointment.getDestination())
                .setRemark(appointment.getRemark());
    }
}
