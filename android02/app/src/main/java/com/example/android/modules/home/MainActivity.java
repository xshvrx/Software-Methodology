package com.example.android.modules.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.models.Album;
import com.example.android.models.Photo;
import com.example.android.models.Tag;
import com.example.android.modules.photo.ShowPhotosActivity;
import com.example.android.utility.Data;
import com.example.android.utility.DialogPopup;

import java.util.ArrayList;
import java.util.List;

interface AlbumClickListener {
    void showMore(int pos, View view);
    void open(int pos);
}

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView albumList;
    AlbumAdapter adapter;
    PhotoResultAdapter photoAdapter;
    Toolbar toolbar;
    ArrayList<Album> albums;
    int index;
    TextView noResultBox;
    MenuItem undoButton;
    MenuItem searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        toolbar = findViewById(R.id.mytoolbar1);
        setSupportActionBar(toolbar);

        Data.loadFromDisk(this);
        albums = Data.getAlbums();


        adapter = new AlbumAdapter(this, albums, new AlbumClickListener() {
            @Override
            public void showMore(int pos, View anchor) {
                showInfo(pos, anchor);
            }

            @Override
            public void open(int pos) {
                openAlbum(pos);
            }
        });
        noResultBox = findViewById(R.id.noAlbumResult);
        if (albums.size() == 0) {
            noResultBox.setVisibility(View.VISIBLE);
            noResultBox.setText("No Albums Found");
        } else {
            noResultBox.setVisibility(View.GONE);
        }

        albumList = findViewById(R.id.albumListView1);

        noResultBox.setVisibility(albums.size() == 0 ? View.VISIBLE : View.GONE);
        albumList.setLayoutManager(new LinearLayoutManager(this));
        albumList.setAdapter(adapter);
    }

    public void showInfo(int position, View anchor) {
        index = position;
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.more_info_popup);
        popup.show();
    }

    private void openAlbum(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("index", position);
        Intent intent = new Intent(this, ShowPhotosActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void imageSearch() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        String[] spinnerOptions = {"Person", "Location"};

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.search_photos_popup, null);
        RadioGroup radioGroup = dialogView.findViewById(R.id.searchOptions);
        RadioButton searchSingle = dialogView.findViewById(R.id.searchSingleOption);
        RadioButton searchOR = dialogView.findViewById(R.id.searchOROption);
        RadioButton searchAND = dialogView.findViewById(R.id.searchANDOption);
        AutoCompleteTextView value1 = dialogView.findViewById(R.id.SearchTagValue1);
        AutoCompleteTextView value2 = dialogView.findViewById(R.id.SearchTagValue2);
        Spinner key1 = dialogView.findViewById(R.id.TagOptions1);
        Spinner key2 = dialogView.findViewById(R.id.TagOptions2);
        value1.setThreshold(1);
        value2.setThreshold(1);

        key2.setVisibility(View.GONE);
        value2.setVisibility(View.GONE);
        searchSingle.setOnClickListener(v -> {
            key2.setVisibility(View.GONE);
            value2.setVisibility(View.GONE);
        });
        searchOR.setOnClickListener(v -> {
            key2.setVisibility(View.VISIBLE);
            value2.setVisibility(View.VISIBLE);
        });
        searchAND.setOnClickListener(v -> {
            key2.setVisibility(View.VISIBLE);
            value2.setVisibility(View.VISIBLE);
        });

        ArrayAdapter<String> key1Adapter = new ArrayAdapter<>(this, R.layout.spinner_list_item, spinnerOptions);
        key1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        key1.setAdapter(key1Adapter);
        
        ArrayAdapter<String> key2Adapter = new ArrayAdapter<>(this, R.layout.spinner_list_item, spinnerOptions);
        key2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        key2.setAdapter(key2Adapter);

        ArrayList<String> personSuggestions =  new ArrayList<>();
        ArrayList<String> locationSuggestions =  new ArrayList<>();
        for (Album a : albums) {
            for (Photo p : a.getPhotos()) {
                for (Tag t : p.getTags()) {
                    String key = t.getName();
                    String value = t.getValue();
                    if (key.equals("Person") && !personSuggestions.contains(value)) {
                        personSuggestions.add(value);
                    } else if (key.equals("Location") && !locationSuggestions.contains(value)) {
                        locationSuggestions.add(value);
                    }
                }
            }
        }

        ArrayAdapter<String> personAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,
                personSuggestions
        );
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,
                locationSuggestions
        );
        value1.setAdapter(personAdapter);
        key1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String key = (String) parent.getItemAtPosition(position);
                if (key.equals("Person")) {
                    value1.setAdapter(personAdapter);
                } else if (key.equals("Location")) {
                    value1.setAdapter(locationAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter<String> personAdapter2 = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,
                personSuggestions
        );
        ArrayAdapter<String> locationAdapter2 = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,
                locationSuggestions
        );
        value2.setAdapter(personAdapter2);
        key2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String key = (String) parent.getItemAtPosition(position);
                if (key.equals("Person")) {
                    value2.setAdapter(personAdapter2);
                } else if (key.equals("Location")) {
                    value2.setAdapter(locationAdapter2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        alert.setView(dialogView);
        alert.setPositiveButton("Search", (dialog, whichButton) -> {
            Tag tag1 = new Tag((String) key1.getSelectedItem(), (String) value1.getText().toString());
            Tag tag2 = new Tag((String) key2.getSelectedItem(), (String) value2.getText().toString());

            int id = radioGroup.getCheckedRadioButtonId();

            int operation;
            switch (id) {
                case R.id.searchANDOption:
                    operation = 2;
                    break;
                case R.id.searchOROption:
                    operation = 1;
                    break;
                default:
                    operation = 0;
                    break;
            }

            ArrayList<Photo> results = new ArrayList<>();
            for (Album a : albums) {
                for (Photo p : a.getPhotos()) {
                    boolean toAdd = false;
                    switch (operation) {
                        case 0:
                            if (p.hasTag(tag1)) 
                                toAdd = true;
                            break;
                        case 1:
                            if (p.hasTag(tag1) || p.hasTag(tag2))
                                toAdd = true;
                            break;
                        case 2:
                            if (p.hasTag(tag1) && p.hasTag(tag2))
                                toAdd = true;
                            break;
                    }
                    if (toAdd && !results.contains(p))
                        results.add(p);
                }
            }
            setRecyclerToPhotoAdapter(results);
        });
        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {});

        alert.show();
    }


    private void setRecyclerToAlbumAdapter() {
        searchButton.setVisible(true);
        undoButton.setVisible(false);
        toolbar.setTitle("Albums");
        noResultBox.setVisibility(albums.size() == 0 ? View.VISIBLE : View.GONE);
        albumList.setLayoutManager(new LinearLayoutManager(this));
        albumList.setAdapter(adapter);
    }

    private void setRecyclerToPhotoAdapter(List<Photo> photoResults) {
        searchButton.setVisible(false);
        undoButton.setVisible(true);
        toolbar.setTitle("Search Results");
        noResultBox.setVisibility(photoResults.size() == 0 ? View.VISIBLE : View.GONE);
        noResultBox.setText("Nothing Found");
        photoAdapter = new PhotoResultAdapter(this, photoResults);
        albumList.setLayoutManager(new GridLayoutManager(this, 3));
        albumList.setAdapter(photoAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_albums, menu);
        searchButton = menu.findItem(R.id.search_action);
        MenuItem menuAddButton = menu.findItem(R.id.plus);
        undoButton = menu.findItem(R.id.undo_action);
        undoButton.setVisible(false);

        menuAddButton.setOnMenuItemClickListener(item -> {
            DialogPopup.showDialogPopup(
                    this,
                    this::createAlbum,
                    "Create New Album",
                    "Enter the album name:",
                    ""
            );
            return false;
        });
        undoButton.setOnMenuItemClickListener(item -> {
            setRecyclerToAlbumAdapter();
            return false;
        });


        searchButton.setOnMenuItemClickListener(item -> {
            imageSearch();
            return false;
        });


        return super.onCreateOptionsMenu(menu);
    }

    public Void createAlbum(String name) {
        if (name == null || duplicateAlbumName(name))
            return null;
        
        albums.add(new Album(name));
        adapter.notifyItemInserted(albums.size());
        
        if (albums.size() == 0) {
            noResultBox.setVisibility(View.VISIBLE);
            noResultBox.setText("No Albums");
        } else {
            noResultBox.setVisibility(View.GONE);
        }
        
        setRecyclerToAlbumAdapter();
        return null;
    }
    
    private boolean duplicateAlbumName(String name) {
        for (Album a : albums) {
            if (a.getName().equalsIgnoreCase(name)) {
                Toast.makeText(getApplicationContext(), "Album name already exists", Toast.LENGTH_LONG).show();
                return true;
            }
        }
        
        return false;
    }

    public void deleteAlbum() {
        albums.remove(index);
        adapter.notifyItemRemoved(index);
        if (albums.size() == 0) {
            noResultBox.setVisibility(View.VISIBLE);
            noResultBox.setText("No Albums");
        } else {
            noResultBox.setVisibility(View.GONE);
        }
    }

    public Void editName(String name) {
        if (duplicateAlbumName(name))
            return null;
        albums.get(index).setName(name);
        adapter.notifyDataSetChanged();
        return null;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_edit_name:
            DialogPopup.showDialogPopup(this, this::editName, "Edit Album Name", "", albums.get(index).getName());
            break;
        case R.id.menu_delete:
            DialogPopup.showConfirmationPopup(this, this::deleteAlbum, "Delete Album", "Are you sure you want to delete '" + albums.get(index).getName() + "'?");
        }
        return false;
    }

    @Override
    protected void onPause() {
        Data.syncToDisk(this);
        adapter.notifyDataSetChanged();
        super.onPause();
    }

    @Override
    protected void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }
}