package com.example.android.task3_hacker_mode;

import android.content.Context;
import android.database.Cursor;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.task3_hacker_mode.Data.PokeContract;

/**
 * Created by Hari on 06-07-2017.
 */

public class PokeCursorAdapter extends CursorAdapter {

    SparseBooleanArray mSelectedIds;

    public PokeCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mSelectedIds = new SparseBooleanArray();

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView pokeImageView = (ImageView) view.findViewById(R.id.poke_image_view);
        TextView pokeNameView = (TextView) view.findViewById(R.id.poke_name_textview);

        String pokeName = cursor.getString(cursor.getColumnIndex(PokeContract.PokeEntry.COLUMN_POKE_NAME));
        String pokeImage = cursor.getString(cursor.getColumnIndex(PokeContract.PokeEntry.COLUMN_POKE_IMAGE));

        pokeNameView.setText(pokeName);
        Glide.with(context).load(pokeImage).override(500, 500).into(pokeImageView);
    }

    public void toggleSelection(int position)
    {
        selectView(position, !mSelectedIds.get(position));
    }
    private void selectView(int position, Boolean value)
    {
        if(value)
        {
            mSelectedIds.put(position, value);
        }
        else
            mSelectedIds.delete(position);
        notifyDataSetChanged();
    }
    public void removeSelection()
    {
        mSelectedIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }
    public SparseBooleanArray getSelectedIds()
    {
        return mSelectedIds;
    }

}
