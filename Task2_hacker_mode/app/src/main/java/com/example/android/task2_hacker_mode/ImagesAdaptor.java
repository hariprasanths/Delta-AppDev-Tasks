package com.example.android.task2_hacker_mode;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Hari on 13-06-2017.
 */

public class ImagesAdaptor extends ArrayAdapter<ImageWithCaption> {
    ImageWithCaption currentImageWithCaption;
    private SparseBooleanArray mSelectedItemsIds;

    public ImagesAdaptor(@NonNull Context context, @NonNull List<ImageWithCaption> objects) {
        super(context, 0, objects);
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        currentImageWithCaption = getItem(position);
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image_view);
        imageView.setImageBitmap(currentImageWithCaption.getimageResouseID());
        TextView textView = (TextView) listItemView.findViewById(R.id.text_view);
        textView.setText(currentImageWithCaption.getcaption());
        return listItemView;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;

    }
}
