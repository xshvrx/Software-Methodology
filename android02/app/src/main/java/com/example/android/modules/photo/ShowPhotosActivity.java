package com.example.android.modules.photo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.R;
import com.example.android.models.Album;
import com.example.android.models.Photo;
import com.example.android.modules.selectedphoto.DisplayActivity;
import com.example.android.modules.slideshow.SlideShowActivity;
import com.example.android.utility.Data;

interface PhotoClickListener {
    void display(int position);
}

public class ShowPhotosActivity extends AppCompatActivity {

    Album album;
    PhotoAdapter adapter;
    private static final int PICK_IMAGE_FILE = 1;
    private static final int OPEN_IMAGE = 2;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_photos);
        Bundle bundle = getIntent().getExtras();
        album = Data.getAlbum(bundle.getInt("index"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.photoViewToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(album.getName());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        adapter = new PhotoAdapter(this, album, pos -> displayPhoto(pos));
        RecyclerView photoList = findViewById(R.id.photoListView);
        photoList.setLayoutManager(new GridLayoutManager(this, 3));
        photoList.setAdapter(adapter);
    }

    public void displayPhoto(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("albumIndex", Data.getAlbums().indexOf(album));
        bundle.putSerializable("photo", album.getPhotos().get(position));
        Intent intent = new Intent(this, DisplayActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, OPEN_IMAGE);
    }

    private void startSlideShow() {
        if (album.getPhotoCount() == 0) {
            Toast.makeText(
                    getApplicationContext(),
                    "There are no photos to present in this album!",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("albumIndex", Data.getAlbums().indexOf(album));
        Intent intent = new Intent(this, SlideShowActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_photos, menu);
        MenuItem menuAddButton = menu.findItem(R.id.plus);
        MenuItem menuPlayButton = menu.findItem(R.id.play_slideshow);

        menuAddButton.setOnMenuItemClickListener(item -> {
            addPhoto();
            return false;
        });
        menuPlayButton.setOnMenuItemClickListener(item -> {
            startSlideShow();
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void addPhoto() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_FILE);
    }

    private void doAddPhoto(Uri uri) {
        Photo newPhoto = null;
        for (Album a : Data.getAlbums()) {
            if (a != album) {
                newPhoto = a.getPhotoByFile(uri);
                if (newPhoto != null) {
                    break;
                }
            }
        }

        if (newPhoto == null) {
            String filename = getFileName(uri);
            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            newPhoto = new Photo(uri, filename);
        }
        album.addPhoto(newPhoto);
        adapter.notifyItemInserted(album.getPhotoCount());
    }

    @Override
    protected void onActivityResult(int request, int result, @Nullable Intent data) {
        super.onActivityResult(request, result, data);

        if (result == RESULT_OK) {
            if (request == PICK_IMAGE_FILE) {
                doAddPhoto(data.getData());
            } else if (request == OPEN_IMAGE) {
                if (data != null && data.getExtras() != null) {
                    Photo p = (Photo) data.getExtras().get("photo");
                    album.updatePhoto(p);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(
                    uri,
                    null,
                    null,
                    null,
                    null
            );
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    protected void onPause() {
        Data.syncToDisk(this);
        super.onPause();
    }
}