package com.mahidol.drugapi.appointment.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class SearchAppointmentResponse {
    private List<AppointmentDTO> data;
    private int total;
}
