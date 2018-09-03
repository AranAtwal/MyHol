package com.example.aranatwal.courseworkv3.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlaceVisited implements Serializable {

    private String title;
    private String description;
    private MyPlace place;
    private MyPhotos photos;
    private Date dateFrom;
    private String dateFromString;

    public int id;
    public static int id_count = 0;

    public PlaceVisited() {
        photos = new MyPhotos();
    }

    public PlaceVisited(String title, String description) {
        this();
        this.description = description;
        this.title = title;

        //increments counter
        id = ++id_count;

    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        //avoids null pointer
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //building for share feature
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("My Place Visited\n");

        if(getTitle()!=null) {
            sb.append("Title: ").append(getTitle()).append("\n");
        }
        if(getDescription()!=null) {
            sb.append("Description: ").append(getDescription()).append("\n");
        }
        if(getDateFrom()!=null) {
            sb.append("Date: ").append(getDateFromString());
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

}