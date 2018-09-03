package com.example.aranatwal.courseworkv3.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Holiday implements Serializable {

    private String title;
    private String description;
    private MyPlace place;
    private MyPhotos photos;
    private Date dateFrom;
    private Date dateTo;
    private String dateFromString;
    private String dateToString;

    private ArrayList<PlaceVisited> placeVisiteds = new ArrayList<PlaceVisited>();

    public int id;
    private static int id_count = 0;

    public Holiday() {
        photos = new MyPhotos();
    }

    public Holiday(String title, String description) {
        this();
        this.description = description;
        this.title = title;
        //incrementing counter for id
        id = ++id_count;

    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        if(title == null) {
            return "";
        }else {
            return title;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //building string for share function
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("My Holiday\n");

        if(getTitle()!=null) {
            sb.append("Title: ").append(getTitle()).append("\n");
        }
        if(getDescription()!=null) {
            sb.append("Description: ").append(getDescription()).append("\n");
        }
        if(getDateFrom()!=null) {
            sb.append("Date: ").append(getDateFromString());
        }
        if(getDateTo()!=null) {
            sb.append(" - ").append(getDateToString());
        }
        if(getPlace()!=null) {
            sb.append("\nLocation: ").append(getPlace().getName()).append("\n");
        }

        return String.valueOf(sb);
    }

    public void addPlace(MyPlace place) {
        this.place = place;
    }

    public MyPlace getPlace() {
        return place;
    }

    public MyPhotos getPhotos() {
        return photos;
    }

    public void setPhotos(MyPhotos photos) {
        this.photos = photos;
    }

    public String getDateFromString() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = getDateFrom();
        setDateFromString(dateFormat.format(date));

        return dateFromString;
    }

    public void setDateFromString(String dateFromString) {
        this.dateFromString = dateFromString;
    }

    public String getDateToString() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = getDateTo();
        setDateToString(dateFormat.format(date));

        return dateToString;
    }

    public void setDateToString(String dateToString) {
        this.dateToString = dateToString;
    }

    public ArrayList<PlaceVisited> getPlaceVisiteds() {
        return placeVisiteds;
    }

    public void setPlaceVisiteds(ArrayList<PlaceVisited> placeVisiteds) {
        this.placeVisiteds = placeVisiteds;
    }

    public void addPlaceVisited (PlaceVisited placeVisited) {
        placeVisiteds.add(placeVisited);
    }



}