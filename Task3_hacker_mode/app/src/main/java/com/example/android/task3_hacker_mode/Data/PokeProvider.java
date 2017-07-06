package com.example.android.task3_hacker_mode.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.task3_hacker_mode.Data.PokeContract.PokeEntry;

/**
 * Created by Hari on 06-07-2017.
 */

public class PokeProvider extends ContentProvider {

    final static int POKEMONS = 1;
    final static int POKEMONS_ID = 2;

    final static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(PokeContract.CONTENT_AUTHORITY, PokeContract.POKE_PATH, POKEMONS);
        uriMatcher.addURI(PokeContract.CONTENT_AUTHORITY, PokeContract.POKE_PATH + "/#", POKEMONS_ID);
    }

    PokeDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new PokeDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case POKEMONS:
                cursor = sqLiteDatabase.query(PokeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case POKEMONS_ID:
                selection = PokeEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(PokeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException();
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (uriMatcher.match(uri)) {
            case POKEMONS:
                return PokeEntry.CONTENT_LIST_TYPE;
            case POKEMONS_ID:
                return PokeEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException();
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        switch (uriMatcher.match(uri)) {
            case POKEMONS:
                SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                long rowId = sqLiteDatabase.insert(PokeEntry.TABLE_NAME, null, values);
                return ContentUris.withAppendedId(uri, rowId);
            default:
                throw new IllegalArgumentException();

        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {


        int rowsDeleted;
        switch (uriMatcher.match(uri)) {
            case POKEMONS:
                SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                rowsDeleted = sqLiteDatabase.delete(PokeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case POKEMONS_ID:
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                selection = PokeEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(PokeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException();

        }
        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {


        return 0;
    }
}
