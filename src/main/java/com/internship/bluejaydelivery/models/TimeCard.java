package com.internship.bluejaydelivery.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TimeCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String PositionID;
    private String PositionStatus;
    private String Time;
    private String TimeOut;
    private String TimecardHours;
    private String PayCycleStartDate;
    private String PayCycleEndDate;
    private String EmployeeName;
    private String FileNumber;

}
