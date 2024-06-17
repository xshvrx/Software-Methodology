package com.example.android.modules.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.models.Album;
import com.example.android.models.Photo;

import java.util.List;

public class SlideShow extends ViewModel {
    private Album album;
    private int currentPhoto = 0;
    private MutableLiveData<List<Photo>> photos = new MutableLiveData<>();
    private MutableLiveData<Photo> selected = new MutableLiveData<>();

    public LiveData<Photo> getSelected() {
        return selected;
    }

    public LiveData<List<Photo>> getPhotos(Album a) {
        album = a;
        loadPhotos();
        selected.setValue(photos.getValue().get(currentPhoto));
        return photos;
    }

    private void loadPhotos() {
        photos.setValue(album.getPhotos());
    }

    public void loadNextImage() {
        int nextIndex = currentPhoto + 1;
        if (nextIndex >= photos.getValue().size()) {
            nextIndex = 0;
        }
        currentPhoto = nextIndex;
        selected.setValue(photos.getValue().get(currentPhoto));
    }

    public void loadPrevImage() {
        int previousIndex = currentPhoto - 1;
        if (previousIndex < 0) {
            previousIndex = photos.getValue().size() - 1;
        }
        currentPhoto = previousIndex;
        selected.setValue(photos.getValue().get(currentPhoto));
    }
}
