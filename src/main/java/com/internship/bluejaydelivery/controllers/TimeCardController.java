package com.internship.bluejaydelivery.controllers;


import com.internship.bluejaydelivery.helper.HelperClass;
import com.internship.bluejaydelivery.models.EmployeeInfo;
import com.internship.bluejaydelivery.models.TimeCard;
import com.internship.bluejaydelivery.service.TimeCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/timecard")
@CrossOrigin("*")
public class TimeCardController {

    @Autowired
    private TimeCardService timeCardService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file){
        if (HelperClass.checkExcelFormat(file)){
            this.timeCardService.save(file);
            return ResponseEntity.ok(Map.of("message","file is uploaded and data is saved in db"));
        }
        return ResponseEntity.badRequest().body("Please upload excel file");
    }

    @GetMapping("/consecutive-workdays")
    public List<EmployeeInfo> getEmployeesWith7ConsecutiveWorkdays() {
        List<EmployeeInfo> employeesWith7ConsecutiveWorkdays = this.timeCardService.findEmployeesWith7ConsecutiveDays();
        return employeesWith7ConsecutiveWorkdays;
    }

    @GetMapping("/employees/resting-less10hours-more1hour")
    public ResponseEntity<List<EmployeeInfo>> getEmployeesWithShortBreaks() {
        List<EmployeeInfo> employees = this.timeCardService.findEmployeesWithShortBreaks();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/employees/working-above14hours")
    public ResponseEntity<List<EmployeeInfo>> getEmployeesWithLongShift() {
        List<EmployeeInfo> employees = this.timeCardService.findEmployeesWithMoreThan14Hours();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TimeCard>> getAllProducts(){
        List<TimeCard> all= this.timeCardService.getAllData();
        return ResponseEntity.ok().body(all);
    }
}
