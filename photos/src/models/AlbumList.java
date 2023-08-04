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

public class AlbumList implements Serializable {
    private List<Album> albumList = new ArrayList<Album>();

    static final long serialVersionUID = 1L;
    public final String storeFile = "src/attributes/albums.ser";

    
    private void initialize() {
        try {
            FileInputStream fileIn = new FileInputStream(storeFile);
            ObjectInputStream ois = new ObjectInputStream(fileIn);
            albumList = (ArrayList<Album>) ois.readObject();
            ois.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            albumList = new ArrayList<Album>();
        }
    }

    /**
     * @throws IOException
     */
    public void saveData() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeFile));
        oos.writeObject(albumList);
        oos.close();
    }

    
    /** 
     * @param album
     * @throws IOException
     */
    public void addAlbum(Album album) throws IOException {
        initialize();

        List<Album> filtered = albumList.stream()
                .filter(a -> a.getName().equals(album.getName()) && a.getUser().equals(album.getUser()))
                .collect(Collectors.toList());
        if (!filtered.isEmpty()) {
            throw new IllegalArgumentException("Album name must be unique per user.");
        }
        albumList.add(album);
        saveData();
    }

    
    /** 
     * @param id
     * @return Album
     */
    public Album getAlbum(String id) {
        initialize();
        List<Album> filtered = albumList.stream().filter(a -> a.getID().equals(id)).collect(Collectors.toList());
        if (filtered.isEmpty()) {
            throw new NoSuchElementException("Album does not exist.");
        }
        return filtered.get(0);
    }


    
    /** 
     * @param id
     * @param name
     * @throws IOException
     */
    public void editAlbum(String id, String name) throws IOException {
        initialize();
        List<Album> filtered = albumList.stream().filter(a -> a.getID().equals(id)).collect(Collectors.toList());
        if (filtered.isEmpty()) {
            throw new NoSuchElementException("Album does not exist.");
        }
        Album album = filtered.get(0);

        if (name.equals(album.getName())) {
            return;
        }

        List<Album> dupFiltered = albumList.stream()
                .filter(a -> a.getName().equals(name) && a.getUser().equals(album.getUser()))
                .collect(Collectors.toList());
        if (!dupFiltered.isEmpty()) {
            throw new IllegalArgumentException("Album name must be unique per user.");
        }

        album.setName(name);

        saveData();
    }


    
    /** 
     * @param id
     * @throws IOException
     */
    public void deleteAlbum(String id) throws IOException {
        initialize();
        List<Album> filtered = albumList.stream().filter(a -> a.getID().equals(id)).collect(Collectors.toList());
        if (filtered.isEmpty()) {
            throw new NoSuchElementException("The album to delete does not exist.");
        }

        albumList = albumList.stream().filter(a -> !a.getID().equals(id)).collect(Collectors.toList());
        saveData();

        PhotoList photoList = new PhotoList();
        try {
            photoList.deletePhotosByAlbum(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public List<Album> getAlbums() {
        initialize();
        return albumList;
    }

    
    public void removeAlbum(Album album) {
        initialize();
        albumList.remove(album);
    }

    
    public void renameAlbum(Album currentAlbum, String name) {
        initialize();
        currentAlbum.setName(name);
    }

}