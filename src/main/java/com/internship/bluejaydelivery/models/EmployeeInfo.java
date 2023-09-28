package com.internship.bluejaydelivery.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeInfo {

    private Integer id;
    private String employeeName;
    private String positionId;


}
