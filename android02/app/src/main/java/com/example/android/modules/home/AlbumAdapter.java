package com.example.android.modules.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.models.Album;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<Album> data;
    private LayoutInflater inflater;
    private AlbumClickListener clickListener;

    public AlbumAdapter(Context context, List<Album> data, AlbumClickListener clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        View view = inflater.inflate(R.layout.album_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = data.get(position);

        String name;
        if (album.getName().length() > 15) {
            name = album.getName().substring(0, 15) + "...";
        } else {
            name = album.getName();
        }

        holder.textView.setText(name + " (" + album.getPhotoCount() + " photos)");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        ImageButton imageButton;

        public ViewHolder(@NonNull View view) {
            super(view);
            textView = view.findViewById(R.id.albumName);
            imageButton = view.findViewById(R.id.buttonMore);

            view.setOnClickListener(this);
            imageButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.buttonMore:
                    clickListener.showMore(getAdapterPosition(), view);
                    break;
                default:
                    clickListener.open(getAdapterPosition());
            }
        }
    }
}
