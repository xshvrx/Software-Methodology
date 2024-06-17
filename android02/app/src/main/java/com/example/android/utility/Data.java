package com.example.android.utility;

import android.content.Context;

import com.example.android.models.Album;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class Data {
   private static ArrayList<Album> albums = new ArrayList<>();

    private static final String fileName = "photos.dat";

    public static void syncToDisk(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(albums);
            oos.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadFromDisk(Context context) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            albums = (ArrayList<Album>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException | ClassNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Album> getAlbums() {
        return albums;
    }

    public static Album getAlbum(String album) {
        Iterator<Album> it = albums.iterator();
        while (it.hasNext()) {
            Album a = it.next();
            if (a.getName().equals(album)) {
                return a;
            }
        }
        return null;
    }

    public static Album getAlbum(int position) {
        if (position < 0 || position >= albums.size()) {
            throw new ArrayIndexOutOfBoundsException("Album at position " + position + " does not exist!");
        }
        Album a = albums.get(position);
        if (a == null) {
            throw new ArrayIndexOutOfBoundsException("Album at position " + position + " is null!");
        }
        return a;
    }

    public static void addAlbum(Album album) {
        albums.add(album);
    }

    public static void removeAlbum(Album album) {
        albums.remove(album);
    }

    public static void removeAlbum(int index) {
        albums.remove(index);
    }
}
