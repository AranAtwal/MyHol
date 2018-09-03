package com.example.aranatwal.courseworkv3.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HolidayData {
    public final List<Holiday> holidays;
    private static HolidayData instance;

    public HolidayData() {
        this.holidays = new ArrayList<>();
    }

    public static HolidayData getInstance() {
        if (instance == null) {
            instance = new HolidayData();
        }
        return  instance;
    }

    public void createHoliday(Holiday hol){
        holidays.add(hol);
    }

    public List<Holiday> getHolidays() {
        return holidays;
    }

    public Holiday getHoliday(int num){
        return holidays.get(num-1);
    }

    //updating all info from basic details page
    public void updateHoliday(String title, String description, Date dateFrom, Date dateTo, int id) {

        Holiday hol = instance.getHoliday(id);
        hol.setDescription(description);
        hol.setTitle(title);
        hol.setDateFrom(dateFrom);
        hol.setDateTo(dateTo);
    }

    public int getNextId() {
        return holidays.size();
    }

    public String getStringSize() {
        //avoiding null pointer
        if(holidays.size()>0) {
            return Integer.toString(holidays.size());
        }
        return Integer.toString(0);
    }

    public int getSize() {
        return holidays.size();
    }

}