package models;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
Rutgers University CS213 Software Methodology
Photos Application Implementation
@author Zaeem Zahid
@author Shiv Patel
*/

public class Album implements Serializable {

    private String id;
    private String name;
    private String user;

    public Album(String name, String user) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.user = user;
    }

    public Album(String name) {
        id = UUID.randomUUID().toString();
        this.name = name;
    }

    /**
     * @return String
     */
    public String getID() {
        return id;
    }

    
    /** 
     * @return String
     */
    public String getUser() {
        return this.user;
    }

    
    /** 
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }


    
    /** 
     * @return String
     */
    public String getName() {
        return name;
    }


    
    /** 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }


    public List<Photo> getPhotos() {
        PhotoList photoList = new PhotoList();
        List<Photo> pictures = photoList.getPhotos();
        return pictures.stream().filter(p -> p.getAlbum().equals(id)).collect(Collectors.toList());
    }


    public void addPhoto(Photo picture) throws IOException {
        picture.setAlbum(id);
        PhotoList photoList = new PhotoList();
        photoList.addPhoto(picture);
    }


    public void removePhoto(Photo picture) throws IOException {
        picture.setAlbum(id);
        PhotoList photoList = new PhotoList();
        photoList.removePhoto(picture);
    }


    public void movePhoto(Photo picture, Album album) throws IOException {
        picture.setAlbum(id);
        PhotoList photoList = new PhotoList();
        photoList.movePhoto(picture, album);
    }


    public void copyPhoto(Photo picture, Album album) throws IOException {
        picture.setAlbum(id);
        PhotoList photoList = new PhotoList();
        photoList.copyPhoto(picture, album);
    }


    public boolean hasPhoto(Photo photo) {
        PhotoList photoList = new PhotoList();
        List<Photo> pictures = photoList.getPhotos();
        return pictures.stream().filter(p -> p.getAlbum().equals(id)).collect(Collectors.toList()).contains(photo);
    }


    public Photo getOldestPhoto() {
        PhotoList photoList = new PhotoList();
        List<Photo> pictures = photoList.getPhotos();
        return pictures.stream().filter(p -> p.getAlbum().equals(id)).collect(Collectors.toList()).stream()
                .min((p1, p2) -> p1.getDate().compareTo(p2.getDate())).get();
    }


    public Photo getNewestPhoto() {
        PhotoList photoList = new PhotoList();
        List<Photo> pictures = photoList.getPhotos();
        return pictures.stream().filter(p -> p.getAlbum().equals(id)).collect(Collectors.toList()).stream()
                .max((p1, p2) -> p1.getDate().compareTo(p2.getDate())).get();
    }


    public Photo getPhoto(Photo photo) {
        PhotoList photoList = new PhotoList();
        List<Photo> pictures = photoList.getPhotos();
        return pictures.stream().filter(p -> p.getAlbum().equals(id)).collect(Collectors.toList()).stream()
                .filter(p -> p.equals(photo)).findFirst().get();
    }


    public List<Photo> conjuctiveSearch(Tag tag1, Tag tag2) {
        List<Photo> photos = getPhotos();
        List<Photo> photoswithtag1 = photos.stream().filter(p -> p.hasTag(tag1)).collect(Collectors.toList());
        List<Photo> photoswithtag2 = photos.stream().filter(p -> p.hasTag(tag2)).collect(Collectors.toList());
        photoswithtag1.retainAll(photoswithtag2);
        return photoswithtag1;
    }


    public List<Photo> disjunctiveSearch(Tag tag1, Tag tag2) {
        List<Photo> photos = getPhotos();
        List<Photo> photoswithtag1 = photos.stream().filter(p -> p.hasTag(tag1)).collect(Collectors.toList());
        List<Photo> photoswithtag2 = photos.stream().filter(p -> p.hasTag(tag2)).collect(Collectors.toList());
        photoswithtag1.addAll(photoswithtag2.stream().filter(p -> !p.hasTag(tag1)).collect(Collectors.toList()));
        return photoswithtag1;
    }
}