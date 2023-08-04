package models;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
Rutgers University CS213 Software Methodology
Photos Application Implementation
@author Zaeem Zahid
@author Shiv Patel
*/

public class User implements Serializable {

    private String id;
    private String username;
    private String password;

    public User(String username, String password) {
        id = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
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
    public String getUsername() {
        return username;
    }

    
    /** 
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }


    
    /** 
     * @return String
     */
    public String getPassword() {
        return password;
    }


    
    /** 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    public List<Album> getAlbums() {
        AlbumList albumList = new AlbumList();
        List<Album> albums = albumList.getAlbums();
        return albums.stream().filter(a -> a.getUser().equals(id)).collect(Collectors.toList());
    }


    public void addAlbum(Album album) throws IOException {
        album.setUser(id);
        AlbumList albumList = new AlbumList();
        albumList.addAlbum(album);
    }


    public List<Photo> getPhotos() {
        List<Album> albums = getAlbums();
        List<Photo> pictures = new ArrayList<>();
        for (Album album : albums) {
            pictures.addAll(album.getPhotos());
        }
        return pictures;
    }


    public boolean hasAlbum(String name) {
        AlbumList albumList = new AlbumList();
        List<Album> albums = albumList.getAlbums();
        List<Album> filtered = albums.stream()
                .filter(a -> a.getName().equals(name) && a.getUser().equals(id))
                .collect(Collectors.toList());
        return !filtered.isEmpty();
    }


    public boolean hasPhoto(String name) {
        List<Photo> photos = getPhotos();
        List<Photo> filtered = photos.stream()
                .filter(p -> p.getName().equals(name))
                .collect(Collectors.toList());
        return !filtered.isEmpty();
    }


    public void removeAlbum(Album album) throws IOException {
        AlbumList albumList = new AlbumList();
        albumList.removeAlbum(album);
    }


    public void renameAlbum(Album currentAlbum, String name) {
        AlbumList albumList = new AlbumList();
        albumList.renameAlbum(currentAlbum, name);
    }


    public Album getAlbum(String name) {
        AlbumList albumList = new AlbumList();
        List<Album> albums = albumList.getAlbums();
        List<Album> filtered = albums.stream()
                .filter(a -> a.getName().equals(name) && a.getUser().equals(id))
                .collect(Collectors.toList());
        
                if (filtered.isEmpty()) {
            return null;
        } else {
            return filtered.get(0);
        }
    }
}
