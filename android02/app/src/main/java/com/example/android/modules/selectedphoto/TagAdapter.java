package com.example.android.modules.selectedphoto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.models.Photo;
import com.example.android.models.Tag;

import java.util.Iterator;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Photo photo;
    private DisplayActivity.TagClickListener clickListener;

    public TagAdapter(Context context, Photo photo, DisplayActivity.TagClickListener clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.photo = photo;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public TagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.tag_recycler_item, parent, false);
        return new TagAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagAdapter.ViewHolder holder, int position) {
        List<Tag> tags = photo.getTags();
        Iterator<Tag> iterator = tags.iterator();
        
        for (int i = 0; i < position; i++) {
            iterator.next();
        }
        
        Tag tag = iterator.next();

        holder.tag.setText(tag.toString());
    }

    @Override
    public int getItemCount() {
        return photo.getTags().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tag;
        ImageButton remove;

        public ViewHolder(@NonNull View view) {
            super(view);
            tag = view.findViewById(R.id.tagItemText);
            remove = view.findViewById(R.id.tagItemRemove);
            remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tagItemRemove) {
                String str = tag.getText().toString();

                String[] tokens = str.split(", ");

                String key = tokens[0].substring(tokens[0].indexOf(":") + 2);
                String value = tokens[1].substring(tokens[1].indexOf(":") + 2);
                
                Tag tag = new Tag(key, value);

                List<Tag> tags = photo.getTags();
                Iterator<Tag> iterator = tags.iterator();
                
                while (iterator.hasNext()) {
                    Tag t = iterator.next();
                    if (t.equals(tag)) {
                        iterator.remove();
                        break;
                    }
                }

                notifyDataSetChanged();
            }
        }
    }
}
