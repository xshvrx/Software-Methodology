package models;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

/*
Rutgers University CS213 Software Methodology
Photos Application Implementation
@author Zaeem Zahid
@author Shiv Patel
*/

public class Photo implements Serializable {
    private String id;
    private String album;
    private String name;
    private String caption;
    private Date date;
    public List<Tag> tags;
    private File file;

    /**
     * @param picture
     * @return Photo
     */
    public static Photo deepCopy(Photo picture) {
        return new Photo(picture.getAlbum(), picture.getName(), picture.getCaption(), picture.getTags(),
                picture.getFile());
    }


    public Photo(String album, File file) {
        id = UUID.randomUUID().toString();
        this.album = album;
        date = new Date(file.lastModified());
        name = file.getName();

        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        String formattedDate = localDateTime
                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.getDefault()));
        caption = String.format("%s taken on %s", file.getName(), formattedDate);
        tags = new ArrayList<>();
        this.file = file;
    }


    public Photo(String album, String name, String caption, List<Tag> tags, File file) {
        id = UUID.randomUUID().toString();
        this.album = album;
        this.name = name;
        this.caption = caption;
        this.tags = tags;
        this.file = file;
        date = new Date(file.lastModified());
    }


    public Photo(String name) {
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
    public String getAlbum() {
        return album;
    }

    
    
    /** 
     * @param album
     */
    public void setAlbum(String album) {
        this.album = album;
    }


    
    /** 
     * @return String
     */
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getCaption() {
        return caption;
    }


    public void setCaption(String caption) {
        this.caption = caption;
    }


    public Date getDate() {
        return date;
    }


    public List<Tag> getTags() {
        return tags;
    }


    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }


    public File getFile() {
        return file;
    }


    public void setFile(File file) {
        this.file = file;
    }


    public boolean hasTag(Tag tag1) {
        for (Tag tag2 : tags) {
            if (tag1.equals(tag2)) {
                return true;
            }
        }
        return false;
    }


    public void removeTag(Tag tag) {
        for (Tag t : tags) {
            if (t.equals(tag)) {
                tags.remove(t);
                break;
            }
        }
    }


    public void addTag(Tag tag) {
        tags.add(tag);
    }
}