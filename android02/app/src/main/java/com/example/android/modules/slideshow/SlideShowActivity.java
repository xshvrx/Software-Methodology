package com.example.android.modules.slideshow;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.models.Photo;
import com.example.android.utility.Data;

public class SlideShowActivity extends AppCompatActivity {
    private SlideShow slideShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int albumIndex = getIntent().getExtras().getInt("index");
        slideShow = new ViewModelProvider(this).get(SlideShow.class);
        slideShow.getPhotos(Data.getAlbum(albumIndex));
        slideShow.getSelected().observe(this, photos -> updateImage());

        setContentView(R.layout.slide_show_view);

        findViewById(R.id.slideShowNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextImage();
            }
        });

        findViewById(R.id.slideShowPrev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPrevImage();
            }
        });


    }

    private void loadNextImage() {
        slideShow.loadNextImage();
    }

    private void loadPrevImage() {
        slideShow.loadPrevImage();
    }

    private void updateImage() {
        Photo photo = slideShow.getSelected().getValue();
        Uri uri = photo.getUri();
        String caption = photo.getCaption();
        ImageView imageView = findViewById(R.id.slideShowImage);
        imageView.setImageURI(uri);
        TextView textView = findViewById(R.id.slideShowImageCaption);
        textView.setText(caption);
    }
}
