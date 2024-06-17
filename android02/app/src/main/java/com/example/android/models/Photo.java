package com.example.android.models;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Tag> tags = new ArrayList<>();
    private String filePath;
    private String caption = "";

    public Photo(Uri f) {
        filePath = f.toString();
    }

    public Photo(Uri u, String f) {
        this(u);
        caption = f;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        if (caption != null)
            this.caption = caption;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public Uri getUri() {
        return Uri.parse(filePath);
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public boolean hasTag(Tag tag1) {
        for (Tag tag2 : tags) {
            if (tag1.equals(tag2)) {
                return true;
            }
        }
        return false;
    }

    public void deleteTag(Tag tag) {
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

    public void updatePhoto(Photo photo) {
        filePath = photo.filePath;
        caption = photo.caption;
        tags = photo.tags;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Photo)) {
            return false;
        }

        Photo p = (Photo) object;
        return p.filePath.equals(filePath);
    }
}

