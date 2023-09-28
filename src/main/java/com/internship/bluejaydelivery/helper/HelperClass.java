package com.internship.bluejaydelivery.helper;

import com.internship.bluejaydelivery.models.TimeCard;
import com.internship.bluejaydelivery.repositories.TimeCardRepo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class HelperClass {
    public static Boolean checkExcelFormat (MultipartFile file){
        String contentType =file.getContentType();
        if(contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){
            return true;
        }
        return false;
    }

    public static List<TimeCard> convertExcelToList(InputStream is){
        List<TimeCard> list = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                Iterator<Cell> cells =row.iterator();
                int cid =0;
                TimeCard p = new TimeCard();
                while(cells.hasNext()){
                    Cell cell = cells.next();
                    switch (cid){
                        case 0:
                            if (cell.getCellType() == CellType.STRING) {
                                p.setPositionID(cell.getStringCellValue());
                            } else if (cell.getCellType() == CellType.NUMERIC) {
                                p.setPositionID(String.valueOf((int) cell.getNumericCellValue()));
                            }
                            break;
                        case 1:
                            p.setPositionStatus(cell.getStringCellValue());
                            break;
                        case 2:
                            if (cell.getCellType() == CellType.STRING) {
                                p.setTime(cell.getStringCellValue());
                            } else if (cell.getCellType() == CellType.NUMERIC) {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    Date dateValue = cell.getDateCellValue();
                                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                                    p.setTime(dateFormat.format(dateValue));
                                } else {
                                    p.setTime(String.valueOf(cell.getNumericCellValue()));
                                }
                            }
                            break;
                        case 3:
                            if (cell.getCellType() == CellType.STRING) {
                                p.setTimeOut(cell.getStringCellValue());
                            } else if (cell.getCellType() == CellType.NUMERIC) {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    Date dateValue = cell.getDateCellValue();
                                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                                    p.setTimeOut(dateFormat.format(dateValue));
                                } else {
                                    p.setTimeOut(String.valueOf(cell.getNumericCellValue()));
                                }
                            }
                            break;
                        case 4:
                            if (cell.getCellType() == CellType.STRING) {
                                p.setTimecardHours(cell.getStringCellValue());
                            } else if (cell.getCellType() == CellType.NUMERIC) {
                                p.setTimecardHours(String.valueOf(cell.getNumericCellValue()));
                            }
                            break;
                        case 5:
                            if (cell.getCellType() == CellType.STRING) {
                                p.setPayCycleStartDate(cell.getStringCellValue());
                            } else if (cell.getCellType() == CellType.NUMERIC) {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    Date dateValue = cell.getDateCellValue();
                                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                    p.setPayCycleStartDate(dateFormat.format(dateValue));
                                } else {
                                    p.setPayCycleStartDate(String.valueOf(cell.getNumericCellValue()));
                                }
                            }
                            break;
                        case 6:
                            if (cell.getCellType() == CellType.STRING) {
                                p.setPayCycleEndDate(cell.getStringCellValue());
                            } else if (cell.getCellType() == CellType.NUMERIC) {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    Date dateValue = cell.getDateCellValue();
                                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                    p.setPayCycleEndDate(dateFormat.format(dateValue));
                                } else {
                                    p.setPayCycleEndDate(String.valueOf(cell.getNumericCellValue()));
                                }
                            }
                            break;
                        case 7:
                            p.setEmployeeName(cell.getStringCellValue());
                            break;
                        case 8:
                            if (cell.getCellType() == CellType.STRING) {
                                p.setFileNumber(cell.getStringCellValue());
                            } else if (cell.getCellType() == CellType.NUMERIC) {
                                p.setFileNumber(String.valueOf((int) cell.getNumericCellValue()));
                            }
                            break;
                        default:
                            break;
                    }
                    cid++;
                }
                list.add(p);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }


}

