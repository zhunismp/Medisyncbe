package com.mahidol.drugapi.appointment.dtos.response;

import com.mahidol.drugapi.appointment.models.entities.Appointment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AppointmentDTO {
    private UUID id;
    private String title;
    private String medicName;
    private LocalDate date;
    private LocalTime time;
    private String destination;
    private String remark;

    public static AppointmentDTO fromAppointment(Appointment appointment) {
        return new AppointmentDTO()
                .setId(appointment.getId())
                .setTime(appointment.getTime())
                .setDate(appointment.getDate())
                .setTitle(appointment.getTitle())
                .setMedicName(appointment.getMedicName())
                .setDestination(appointment.getDestination())
                .setRemark(appointment.getRemark());
    }
}
