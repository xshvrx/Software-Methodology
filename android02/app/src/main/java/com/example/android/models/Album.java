package com.example.android.models;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Album implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private ArrayList<Photo> photos;
    Map<String, Photo> photoMap;

    public Album(String name) {
        this.name = name;
        photos = new ArrayList<>();
        photoMap = new HashMap<>();
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public Photo getPhotoByFile(String filePath) {
       return photoMap.get(filePath);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhotoCount() {
        return photos.size();
    }

    public Photo getPhotoByFile(Uri uri) {
        return getPhotoByFile(uri.toString());
    }

    public boolean addPhoto(Photo p) {
        if (photos.contains(p)) {
            return false;
        }
        photos.add(p);
        photoMap.put(p.getUri().toString(), p);
        return true;
    }

    public boolean containsPhoto(Photo photo) {
        return photos.contains(photo);
    }

    public void updatePhoto(Photo photo) {
        int x = photos.indexOf(photo);
        assert x >= 0;
        photos.get(x).updatePhoto(photo);
    }

    public void deletePhoto(Photo photo) {
        photos.remove(photo);
        photoMap.remove(photo.getUri().toString());
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Album)) {
            return false;
        }
        
        Album a = (Album) object;
        return a.name.equals(name);
    }
}
