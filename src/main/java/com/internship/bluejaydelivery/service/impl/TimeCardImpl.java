package com.internship.bluejaydelivery.service.impl;

import com.internship.bluejaydelivery.helper.HelperClass;
import com.internship.bluejaydelivery.models.EmployeeInfo;
import com.internship.bluejaydelivery.models.TimeCard;
import com.internship.bluejaydelivery.repositories.TimeCardRepo;
import com.internship.bluejaydelivery.service.TimeCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TimeCardImpl implements TimeCardService {

    @Autowired
    private TimeCardRepo timeCardRepo;

    @Override
    public void save(MultipartFile file) {
        try {
            List<TimeCard> allProduct = HelperClass.convertExcelToList(file.getInputStream());
            this.timeCardRepo.saveAll(allProduct);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TimeCard> getAllData() {
        return this.timeCardRepo.findAll();
    }

    @Override
    public List<EmployeeInfo> findEmployeesWithMoreThan10Hours() {
        List<TimeCard> allTimeCards = this.timeCardRepo.findAll();
        List<TimeCard> validTimeCards = allTimeCards.stream()
                .filter(timeCard -> timeCard.getTime() != null && !timeCard.getTime().isEmpty() &&
                        timeCard.getTimeOut() !=null && !timeCard.getTimeOut().isEmpty())
                .collect(Collectors.toList());
        validTimeCards.sort(Comparator.comparing(TimeCard::getEmployeeName).thenComparing(TimeCard::getTime));
        List<EmployeeInfo> result = new ArrayList<>();
        for (int i = 0; i < validTimeCards.size() - 1; i++) {
            TimeCard current = validTimeCards.get(i);
            TimeCard next = validTimeCards.get(i + 1);

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
            LocalDate currentDate = LocalDate.parse(current.getTime(), dateFormat);
            LocalDate nextDate = LocalDate.parse(next.getTime(), dateFormat);
            LocalDate checkNextDate =currentDate.plusDays(1);
            if(checkNextDate.equals(nextDate)){
                if (current.getEmployeeName().equals(next.getEmployeeName())) {
                    if(findDiffercence(current.getTimeOut(),next.getTime())){
                        EmployeeInfo emp = new EmployeeInfo();
                        emp.setId(current.getId());
                        emp.setEmployeeName(current.getEmployeeName());
                        emp.setPositionId(current.getPositionID());
                        result.add(emp);
                    }
                }
            }

        }
        return result;

    }

    @Override
    public List<EmployeeInfo> findEmployeesWithShortBreaks() {
        List<TimeCard> allTimeCards = this.timeCardRepo.findAll();
        List<TimeCard> validTimeCards = allTimeCards.stream()
                .filter(timeCard -> timeCard.getTime() != null && !timeCard.getTime().isEmpty() &&
                        timeCard.getTimeOut() !=null && !timeCard.getTimeOut().isEmpty())
                .collect(Collectors.toList());
        validTimeCards.sort(Comparator.comparing(TimeCard::getEmployeeName).thenComparing(TimeCard::getTimeOut));
        List<EmployeeInfo> result = new ArrayList<>();

        for (int i = 0; i < validTimeCards.size() - 1; i++) {
            TimeCard current = validTimeCards.get(i);
            TimeCard next = validTimeCards.get(i + 1);

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
            LocalDate currentDate = LocalDate.parse(current.getTime(), dateFormat);
            LocalDate nextDate = LocalDate.parse(next.getTime(), dateFormat);

            if(currentDate.equals(nextDate)){
                if (current.getEmployeeName().equals(next.getEmployeeName())) {

                    if(findDiffercence(current.getTimeOut(),next.getTime())){
                        EmployeeInfo emp = new EmployeeInfo();
                        emp.setId(current.getId());
                        emp.setEmployeeName(current.getEmployeeName());
                        emp.setPositionId(current.getPositionID());
                        result.add(emp);
                    }
                }
            }

        }

        return result;
    }

    private boolean findDiffercence(String timeOut, String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        String differenceFormat="";
        try {
            Date time1 = dateFormat.parse(timeOut);
            Date time2 = dateFormat.parse(time);

            long durationInMillis = time2.getTime() - time1.getTime();
            long minutesDifference = TimeUnit.MILLISECONDS.toMinutes(durationInMillis);
            long hours = minutesDifference / 60;
            long minutes = minutesDifference % 60;
            differenceFormat = String.format("%d:%02d", hours, minutes);

            if (hours < 10 || (hours == 10 && minutes <= 0)) {
                if (hours > 1 || (hours == 1 && minutes > 0)) {
                    return true;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public List<EmployeeInfo> findEmployeesWith7ConsecutiveDays() {
        List<TimeCard> allTimeCards = this.timeCardRepo.findAll();
        List<TimeCard> validTimeCards = allTimeCards.stream()
                .filter(timeCard -> timeCard.getTime() != null && !timeCard.getTime().isEmpty())
                .collect(Collectors.toList());
        validTimeCards.sort(Comparator.comparing(TimeCard::getEmployeeName).thenComparing(TimeCard::getTime));

        List<EmployeeInfo> employeesWith7ConsecutiveDays = new ArrayList<>();
        int consecutiveWorkdays = 1;

        for (int i = 0; i < validTimeCards.size() - 1; i++) {
            TimeCard current = validTimeCards.get(i);
            TimeCard next = validTimeCards.get(i + 1);

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
            LocalDate currentDate = LocalDate.parse(current.getTime(), dateFormat);
            LocalDate nextDate = LocalDate.parse(next.getTime(), dateFormat);

            if(!currentDate.equals(nextDate)){
                if (current.getEmployeeName().equals(next.getEmployeeName())) {
                    LocalDate ss =currentDate.plusDays(1);
                    String cS = ss.toString();
                    String nS = nextDate.toString();
                    if (cS.equals(nS)) {
                        consecutiveWorkdays++;
                    } else {
                        consecutiveWorkdays = 1;
                    }

                    if (consecutiveWorkdays == 7) {
                        EmployeeInfo emp = new EmployeeInfo();
                        emp.setId(current.getId());
                        emp.setEmployeeName(current.getEmployeeName());
                        emp.setPositionId(current.getPositionID());
                        employeesWith7ConsecutiveDays.add(emp);
                        consecutiveWorkdays = 1;
                    }
                }
            }

        }

        return employeesWith7ConsecutiveDays;
    }



    @Override
    public List<EmployeeInfo> findEmployeesWithMoreThan14Hours() {
        List<TimeCard> allTimecards = this.timeCardRepo.findAll();
        List<EmployeeInfo> employeesWithMoreThan14Hours = new ArrayList<>();
        for (TimeCard timecard : allTimecards) {
            String timecardHours = timecard.getTimecardHours();
            if (timecardHours == null || timecardHours.isEmpty()) {
                continue;
            }
            String[] parts = timecardHours.split(":");
            if (parts.length != 2) {
                continue;
            }

            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int totalMinutes = (hours * 60) + minutes;
            if (totalMinutes >= 14 * 60) {
                employeesWithMoreThan14Hours.add(new EmployeeInfo(timecard.getId(),timecard.getEmployeeName(),timecard.getPositionID()));
            }
        }
        return employeesWithMoreThan14Hours;
    }
}
