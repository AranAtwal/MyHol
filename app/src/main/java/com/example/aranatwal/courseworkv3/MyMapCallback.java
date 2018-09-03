package com.example.aranatwal.courseworkv3;

import com.example.aranatwal.courseworkv3.model.MyPlace;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;


public class MyMapCallback implements OnMapReadyCallback {
    private ArrayList<MyPlace> myPlaces;
    private MyPlace myPlace;
    GoogleMap mMap;

    //constructor for when a holiday has places visited added to it
    public MyMapCallback(MyPlace myPlace, ArrayList<MyPlace> myPlaces) {
        this.myPlaces = myPlaces;
        this.myPlace = myPlace;

    }
    //standard constructor for a simple holiday or place visited
    public MyMapCallback(MyPlace myPlace) {
        this.myPlace = myPlace;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //makes markers blue for places visted
        if (myPlaces!=null) {
            for (MyPlace myPlace :
                    myPlaces) {
                LatLng loc2 = myPlace.getLatLng();
                String name2 = myPlace.getName();
                if (loc2 != null && name2 != null) {
                    mMap.addMarker(new MarkerOptions().position(loc2).title(name2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                } 

            }
        }

        //  get the location
        LatLng loc = myPlace.getLatLng();
        String name = myPlace.getName();
        if (loc != null && name != null) {
            Marker m = mMap.addMarker(new MarkerOptions().position(loc).title(name));
            m.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 12));

            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }
}