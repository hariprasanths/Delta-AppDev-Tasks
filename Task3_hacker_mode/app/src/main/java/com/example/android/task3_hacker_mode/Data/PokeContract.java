package com.example.android.task3_hacker_mode.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Hari on 06-07-2017.
 */

public class PokeContract {

    PokeContract(){}
    public static final String CONTENT_AUTHORITY = "com.example.android.task3_hacker_mode";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String POKE_PATH = "task3_hacker_mode";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,POKE_PATH);

    public static final class PokeEntry implements BaseColumns{

        public final static String TABLE_NAME = "pokemons";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_POKE_NAME = "name";
        public final static String COLUMN_POKE_IMAGE = "image";
        public final static String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + POKE_PATH;
        public final static String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + POKE_PATH;
    }

}
