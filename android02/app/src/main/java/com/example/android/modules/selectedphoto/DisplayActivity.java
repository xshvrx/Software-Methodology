package com.example.android.modules.selectedphoto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.models.Album;
import com.example.android.models.Photo;
import com.example.android.models.Tag;
import com.example.android.utility.Data;
import com.example.android.utility.DialogPopup;

import java.util.ArrayList;
import java.util.function.Consumer;

public class DisplayActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    Toolbar toolbar;
    View tags;
    ImageView display;
    Photo photo;
    Button addButton;
    int index;
    boolean showUI = true;
    TagAdapter adapter;
    Animation animateShowTags;
    Animation animateHideTags;
    Animation animateShowToolbar;
    Animation animateHideToolbar;

    public interface TagClickListener {
        void removeTag(String key, String value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view);
        Bundle bundle = getIntent().getExtras();
        photo = (Photo) bundle.getSerializable("photo");
        index = bundle.getInt("albumIndex");

        toolbar = findViewById(R.id.displayPhotoToolbar);
        tags = findViewById(R.id.tagControls);
        display = findViewById(R.id.mainImageDisplay);
        
        addButton = findViewById(R.id.buttonAddTag);
        addButton.setOnClickListener(v -> addTag());

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setTitle(photo.getCaption());

        display.setOnClickListener(v -> showUI());
        display.setImageURI(photo.getUri());

        animateHideTags = AnimationUtils.loadAnimation(this, R.anim.animation_hide_tags);
        animateShowTags = AnimationUtils.loadAnimation(this, R.anim.animation_show_tags);
        animateHideToolbar = AnimationUtils.loadAnimation(this, R.anim.animation_hide_toolbar);
        animateShowToolbar = AnimationUtils.loadAnimation(this, R.anim.animation_show_toolbar);

        adapter = new TagAdapter(this, photo, (key, val) -> {
            for (Tag t : photo.getTags()) {
                if (t.getName().equals(key) && t.getValue().equals(val)) {
                    photo.deleteTag(t);
                    break;
                }
            }

            adapter.notifyDataSetChanged();
        });

        RecyclerView tagList = findViewById(R.id.tagRecycler);
        tagList.setLayoutManager(new LinearLayoutManager(this));
        tagList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_display_photo, menu);
        MenuItem editButton = menu.findItem(R.id.editName);
        MenuItem deleteButton = menu.findItem(R.id.deletePhoto);
        MenuItem moreButton = menu.findItem(R.id.showMoveOptions);

        editButton.setOnMenuItemClickListener(item -> {
            DialogPopup.showDialogPopup(this, this::editCaption, "Edit Photo Caption", "", photo.getCaption());
            return false;
        });
        deleteButton.setOnMenuItemClickListener(item -> {
            DialogPopup.showConfirmationPopup(this, this::deletePhoto, "Delete Photo", "Are you sure you want to delete this photo?");
            return false;
        });
        moreButton.setOnMenuItemClickListener(item -> {
            PopupMenu popup = new PopupMenu(this, findViewById(R.id.showMoveOptions));
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.photo_move_popup);
            popup.show();
            return false;
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void addTag() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.create_tag_popup, null);
        RadioGroup radioGroup = dialogView.findViewById(R.id.tagKeyGroup);
        EditText editText = dialogView.findViewById(R.id.tagValue);
        alert.setView(dialogView);

        alert.setPositiveButton("Confirm", (dialog, whichButton) -> {
            int selected = radioGroup.getCheckedRadioButtonId();
            String key = ((RadioButton) dialogView.findViewById(selected)).getText().toString();

            for (Tag t : photo.getTags()) {
                if (t.getName().equals(key)) {
                    photo.deleteTag(t);
                    break;
                }
            }

            Tag tag = new Tag(key, editText.getText().toString().trim());
            photo.addTag(tag);

            adapter.notifyDataSetChanged();
        });
        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {});
        AlertDialog d = alert.create();
        alert.show();
    }

    public Void editCaption(String caption) {
        photo.setCaption(caption);
        toolbar.setTitle(caption);
        return null;
    }

    private void deletePhoto() {
        Data.getAlbum(index).deletePhoto(photo);
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void showUI() {
        int i = (showUI = !showUI) ? View.VISIBLE : View.INVISIBLE;
        if (showUI) {
            tags.startAnimation(animateShowTags);
            toolbar.startAnimation(animateShowToolbar);
        } else {
            tags.startAnimation(animateHideTags);
            toolbar.startAnimation(animateHideToolbar);
        }
        toolbar.setVisibility(i);
        tags.setVisibility(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("photo", photo);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.move_to:
                copyTo(this::movePhoto);
                break;
            case R.id.copy_to:
                copyTo(this::copyPhoto);
        }
        return false;
    }

    private void copyTo(Consumer<String> onConfirm) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("title");
        alert.setMessage("age");
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_copy_photo_to, null);
        alert.setView(dialogView);
        Spinner spinner = dialogView.findViewById(R.id.spinner);
        ArrayList<String> albumNames = new ArrayList<>();
        for (Album a : Data.getAlbums()) {
            if (!a.containsPhoto(photo))
                albumNames.add(a.getName());
        }
        if (albumNames.size() == 0) {
            displayResult("Photo is already in all albums!");
            return;
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        alert.setPositiveButton("Confirm", (dialog, whichButton) -> onConfirm.accept((String) spinner.getSelectedItem()));
        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {});
        alert.show();
    }

    private void copyPhoto(String photo) {
        Album album = Data.getAlbum(photo);
        if(album == null) {
            displayResult("Album does not exist!");
            return;
        }
        if (album.containsPhoto(this.photo)) {
            displayResult("Photo is already in all albums!");
            return;
        }
        album.addPhoto(this.photo);
        displayResult("Photo successfully copied!");
    }

    private void movePhoto(String photo) {
        Album album = Data.getAlbum(photo);
        if (album == null) {
            displayResult("Album does not exist!");
            return;
        }
        album.addPhoto(this.photo);
        Data.getAlbum(index).deletePhoto(this.photo);
        displayResult("Photo successfully moved!");
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void displayResult(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        Data.syncToDisk(this);
        super.onPause();
    }
}