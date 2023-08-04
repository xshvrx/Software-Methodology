package models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/*
Rutgers University CS213 Software Methodology
Photos Application Implementation
@author Zaeem Zahid
@author Shiv Patel
*/

public class PhotoList implements Serializable {
    private List<Photo> photoList = new ArrayList<Photo>();

    public final String storeFile = "src/attributes/photos.ser";
    static final long serialVersionUID = 1L;


    private void initialize() {
        try {
            FileInputStream fileIn = new FileInputStream(storeFile);
            ObjectInputStream ois = new ObjectInputStream(fileIn);
            photoList = (ArrayList<Photo>) ois.readObject();
            ois.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            photoList = new ArrayList<>();
        }
    }

    /**
     * @throws IOException
     */
    public void saveData() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeFile));
        oos.writeObject(photoList);
        oos.close();
    }

    
    /** 
     * @param picture
     * @throws IOException
     */
    public void addPhoto(Photo picture) throws IOException {
        initialize();
        photoList.add(picture);
        saveData();
    }

    
    /** 
     * @param photo
     * @throws IOException
     */
    public void removePhoto(Photo photo) throws IOException {
        initialize();
        photoList.remove(photo);
        saveData();
    }


    
    /** 
     * @param photo
     * @param album
     * @throws IOException
     */
    public void movePhoto(Photo photo, Album album) throws IOException {
        initialize();
        photoList.remove(photo);
        photo.setAlbum(album.getID());
        photoList.add(photo);
        saveData();
    }


    
    /** 
     * @param photo
     * @param album
     * @throws IOException
     */
    public void copyPhoto(Photo photo, Album album) throws IOException {
        initialize();
        photo.setAlbum(album.getID());
        photoList.add(photo);
        saveData();
    }


    public Photo getPhoto(String id) {
        initialize();
        List<Photo> filtered = photoList.stream().filter(p -> p.getID().equals(id)).collect(Collectors.toList());
        if (filtered.isEmpty()) {
            throw new NoSuchElementException("Photo does not exist.");
        }
        return filtered.get(0);
    }


    public void editPhoto(String id, String album, String name, String caption, List<Tag> tags, File file)
            throws IOException {
        initialize();
        List<Photo> filtered = photoList.stream().filter(p -> p.getID().equals(id)).collect(Collectors.toList());
        if (filtered.isEmpty()) {
            throw new NoSuchElementException("Photo does not exist.");
        }
        Photo picture = filtered.get(0);
        picture.setAlbum(album);
        picture.setName(name);
        picture.setCaption(caption);
        picture.setTags(tags);
        picture.setFile(file);
        saveData();
    }


    public void editPhotoTags(String id, List<Tag> tags) throws IOException {
        initialize();
        List<Photo> filtered = photoList.stream().filter(p -> p.getID().equals(id)).collect(Collectors.toList());
        if (filtered.isEmpty()) {
            throw new NoSuchElementException("Photo does not exist.");
        }
        Photo picture = filtered.get(0);
        picture.setTags(tags);
        saveData();
    }


    public void deletePhoto(String id) throws IOException {
        initialize();
        List<Photo> filtered = photoList.stream().filter(p -> p.getID().equals(id)).collect(Collectors.toList());
        if (filtered.isEmpty()) {
            throw new NoSuchElementException("The picture to delete does not exist.");
        }
        photoList = photoList.stream().filter(p -> !p.getID().equals(id)).collect(Collectors.toList());
        saveData();
    }


    public void deletePhotosByAlbum(String albumId) throws IOException {
        initialize();
        photoList = photoList.stream().filter(p -> !p.getAlbum().equals(albumId)).collect(Collectors.toList());
        saveData();
    }


    public List<Photo> getPhotos() {
        initialize();
        return photoList;
    }

}