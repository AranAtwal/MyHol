package com.example.aranatwal.courseworkv3.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlaceVisitedData {
    public final List<PlaceVisited> visitedPlaces;
    private static PlaceVisitedData instance;

    public PlaceVisitedData() {
        this.visitedPlaces = new ArrayList<>();
    }

    public static PlaceVisitedData getInstance() {
        if (instance == null) {
            instance = new PlaceVisitedData();
        }
        return  instance;
    }

    public void createPlaceVisited(PlaceVisited placeVisited){
        visitedPlaces.add(placeVisited);
    }

    public List<PlaceVisited> getPlacesVisited() {
        return visitedPlaces;
    }

    public PlaceVisited getPlaceVisited(int num){
        return visitedPlaces.get(num-1);
    }

    public void updatePlaceVisited(String title, String description, Date dateFrom, int id) {

        PlaceVisited pl = instance.getPlaceVisited(id);
        pl.setDescription(description);
        pl.setTitle(title);
        pl.setDateFrom(dateFrom);
    }

    public int getNextId() {
        return visitedPlaces.size();
    }

    public String getStringSize() {
        if(visitedPlaces.size()>0) {
            return Integer.toString(visitedPlaces.size());
        }
        return Integer.toString(0);
    }

    public int getSize() {
        return visitedPlaces.size();
    }

}