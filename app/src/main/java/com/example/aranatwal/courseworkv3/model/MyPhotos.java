package com.example.aranatwal.courseworkv3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyPhotos implements Serializable{
    public final List<String> photoStrings;
    private static MyPhotos instance;
    //global list of photos for travel gallery where they are not associated to a holiday or place visited
    public static List<String> globalPhotos = new ArrayList<String>();

    public MyPhotos() {
        this.photoStrings = new ArrayList<String>();
    }

    public static MyPhotos getInstance() {
        if (instance == null) {
            instance = new MyPhotos();
        }
        return  instance;
    }

    public void addPhoto(String path) {
        photoStrings.add(path);
    }

    public ArrayList<String> getPhotoStrings() {
        return (ArrayList<String>) photoStrings;
    }

    public void addGlobalPhoto(String path) {
        globalPhotos.add(path);
    }

    public ArrayList<String> getGlobalPhotoStrings() {
        return (ArrayList<String>) globalPhotos;
    }


}