package com.example.android.task3_hacker_mode.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.task3_hacker_mode.Data.PokeContract.PokeEntry;

/**
 * Created by Hari on 06-07-2017.
 */

public class PokeDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "pokemons.db";

    public PokeDbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_TABLE = "CREATE TABLE " + PokeEntry.TABLE_NAME +
                " (" + PokeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PokeEntry.COLUMN_POKE_NAME + " TEXT, " +
                PokeEntry.COLUMN_POKE_IMAGE + " TEXT);";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
