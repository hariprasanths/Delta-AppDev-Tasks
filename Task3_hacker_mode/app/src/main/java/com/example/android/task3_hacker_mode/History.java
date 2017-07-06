package com.example.android.task3_hacker_mode;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.android.task3_hacker_mode.Data.PokeContract;

public class History extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    PokeCursorAdapter pokeCursorAdapter;
    ListView pokeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("History");
        pokeListView = (ListView) findViewById(R.id.list);
        pokeCursorAdapter = new PokeCursorAdapter(this,null);
        pokeListView.setAdapter(pokeCursorAdapter);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_history,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.delete_button:
                return true;

            case R.id.delete_all_button:

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
        return new CursorLoader(this, PokeContract.CONTENT_URI,projection,null,null,null);
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
