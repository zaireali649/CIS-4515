package com.codeali.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;



public class KeyDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "KeyDatabase.db";
    private static final String TABLE_NAME = "keystore";
    private static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME +
            " (_id INTEGER INCREMENT, ALIAS TEXT PRIMARY KEY, PUBLICKEY TEXT , PRIVATEKEY TEXT )";

    private static final String SQL_DROP = "DROP TABLE IS EXISTS " + TABLE_NAME ;

    KeyDataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        onCreate(db);
    }

    public Cursor getKeys(String alias, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqliteQueryBuilder = new SQLiteQueryBuilder();
        sqliteQueryBuilder.setTables(TABLE_NAME);

        /*if(alias != null) {
            sqliteQueryBuilder.appendWhere("ALIAS" + " = " + alias);
        }*/

        if(sortOrder == null || sortOrder == "") {
            sortOrder = "ALIAS";
        }
        Cursor cursor = sqliteQueryBuilder.query(getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        return cursor;
    }

    public long addNewKey(ContentValues values) throws SQLException {
        long id = getWritableDatabase().insert(TABLE_NAME, "", values);
        if(id <=0 ) {
            throw new SQLException("Failed to add a key");
        }

        return id;
    }

    public int deleteKeys(String id) {
        if(id == null) {
            return getWritableDatabase().delete(TABLE_NAME, null , null);
        } else {
            return getWritableDatabase().delete(TABLE_NAME, "_id=?", new String[]{id});
        }
    }

    public int updateKeys(String id, ContentValues values) {
        if(id == null) {
            return getWritableDatabase().update(TABLE_NAME, values, null, null);
        } else {
            return getWritableDatabase().update(TABLE_NAME, values, "_id=?", new String[]{id});
        }
    }
}