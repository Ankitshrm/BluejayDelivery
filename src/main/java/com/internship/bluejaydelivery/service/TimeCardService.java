package com.internship.bluejaydelivery.service;

import com.internship.bluejaydelivery.models.EmployeeInfo;
import com.internship.bluejaydelivery.models.TimeCard;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TimeCardService {
    void save(MultipartFile file);
    List<TimeCard> getAllData();

    List<EmployeeInfo> findEmployeesWithMoreThan14Hours();

    List<EmployeeInfo> findEmployeesWithShortBreaks();

    List<EmployeeInfo> findEmployeesWith7ConsecutiveDays();

    List<EmployeeInfo> findEmployeesWithMoreThan10Hours();



}
