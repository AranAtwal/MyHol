package com.example.aranatwal.courseworkv3.model;

import com.google.android.gms.maps.model.LatLng;
import java.io.Serializable;

public class MyPlace implements Serializable{
    
    private transient LatLng latlng;
    private String name;

    public MyPlace(LatLng location, String name) {
        latlng = location;
        this.name = name;
    }

    public LatLng getLatLng() {
        return latlng;
    }

    public String getName() {
        return name;
    }
}
