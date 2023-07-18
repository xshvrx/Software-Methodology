/*
Rutgers University CS213 Software Methodology
Assignment 1 - Song Library Application
@author Zaeem Zahid
@author Shiv Patel
*/

package models;

public class Song implements Comparable<Song>{
    private String title;
    private String artist;
    private String album;
    private String year;

    public Song() {
        this.title = "";
        this.artist = "";
        this.album = "";
        this.year = "";
    }

    public Song(String title, String artist, String album, String year) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getYear() {
        return year;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return title + " by " + artist;
    }

    @Override
    public int compareTo(Song s) {
        if(this.title.toLowerCase().compareTo(s.title.toLowerCase()) == 0) {
            return this.artist.toLowerCase().compareTo(s.artist.toLowerCase());
        } else {
            return this.title.toLowerCase().compareTo(s.title.toLowerCase());
        }
    }
}
