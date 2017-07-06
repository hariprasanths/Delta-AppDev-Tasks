package com.example.android.task3_hacker_mode;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.task3_hacker_mode.Data.PokeContract;

public class History extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    PokeCursorAdapter pokeCursorAdapter;
    ListView pokeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("History");
        pokeListView = (ListView) findViewById(R.id.list);
        pokeCursorAdapter = new PokeCursorAdapter(this, null);
        pokeListView.setAdapter(pokeCursorAdapter);
        pokeListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        pokeListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                int checkedCount = pokeListView.getCheckedItemCount();
                mode.setTitle(checkedCount + " item selected");
                pokeCursorAdapter.toggleSelection(position);
                pokeCursorAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_selected, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                Uri currentUri;
                switch (item.getItemId()) {
                    case R.id.delete_button:
                        SparseBooleanArray selectedids = pokeCursorAdapter.getSelectedIds();
                        for (int i = (selectedids.size() - 1); i >= 0; i--) {
                            if (selectedids.valueAt(i)) {
                                currentUri = ContentUris.withAppendedId(PokeContract.CONTENT_URI, selectedids.keyAt(i) + 1);
                                int j = getContentResolver().delete(currentUri, null, null);
                                pokeCursorAdapter.notifyDataSetChanged();
                            }
                        }
                        mode.finish();
                        return true;
                    default:
                        return false;
                }


            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                pokeCursorAdapter.removeSelection();
            }
        });
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_all_button:
                getContentResolver().delete(PokeContract.CONTENT_URI, null, null);
                Toast.makeText(getApplicationContext(), "HISTORY has been deleted", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {PokeContract.PokeEntry._ID,
                PokeContract.PokeEntry.COLUMN_POKE_NAME,
                PokeContract.PokeEntry.COLUMN_POKE_IMAGE};
        return new CursorLoader(this, PokeContract.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        pokeCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        pokeCursorAdapter.swapCursor(null);
    }
}
