package com.codeali.myapplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileNotFoundException;


public class KeyProvider extends ContentProvider {

    private static final String PROVIDER_NAME = "codeali.myapplication.keys";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/keys");
    private static final int KEYS = 1;
    private static final int KEY_ID = 2;
    private static final UriMatcher uriMatcher = getUriMatcher();
    private Context context;
    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "keys", KEYS);
        uriMatcher.addURI(PROVIDER_NAME, "keys/#", KEY_ID);
        return uriMatcher;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        File file =  new File("path to file");

        if (file.exists()) {
            return (ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE));
        }
        throw new FileNotFoundException(uri.getPath());
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case KEYS:
                return "vnd.android.cursor.dir/vnd.com.codeali.myapplication.provider.keys";
            case KEY_ID:
                return "vnd.android.cursor.item/vnd.com.codeali.myapplication.provider.keys";

        }
        return "";
    }

    @Override
    public boolean onCreate() {
        context = getContext();
        //keyDataBase = new KeyDataBase(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        throw new UnsupportedOperationException("Not supported by this provider");
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        throw new UnsupportedOperationException("Not supported by this provider");
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        throw new UnsupportedOperationException("Not supported by this provider");
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw new UnsupportedOperationException("Not supported by this provider");
    }
}
