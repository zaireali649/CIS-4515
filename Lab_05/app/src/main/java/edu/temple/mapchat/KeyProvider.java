package edu.temple.mapchat;

/**
 * Created by Ziggy on 3/26/2018.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;


public class KeyProvider extends ContentProvider {

    private static final String PROVIDER_NAME = "temple.mapchat.keys";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/keys");
    private static final int KEYS = 1;
    private static final int KEY_ID = 2;
    private static final UriMatcher uriMatcher = getUriMatcher();
    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "keys", KEYS);
        uriMatcher.addURI(PROVIDER_NAME, "keys/#", KEY_ID);
        return uriMatcher;
    }

    private KeyDataBase keyDataBase = null;

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case KEYS:
                return "vnd.android.cursor.dir/vnd.edu.temple.mapchat.provider.keys";
            case KEY_ID:
                return "vnd.android.cursor.item/vnd.edu.temple.mapchat.provider.keys";

        }
        return "";
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        keyDataBase = new KeyDataBase(context);
        return true;
    }



    public void resetDatabase(){
        keyDataBase.resetTable();
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        return keyDataBase.getKeys("", projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try {
            long id = keyDataBase.addNewKey(values);
            Uri returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
            return returnUri;
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == KEY_ID) {
            id = uri.getPathSegments().get(1);
        }

        return keyDataBase.deleteKeys(id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == KEY_ID) {
            //Update is for one single image. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }

        return keyDataBase.updateKeys(id, values);
    }
}