package edu.temple.mapchat;

/**
 * Created by Ziggy on 3/26/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;



public class KeyDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "KeyDatabaseChat.db";
    private static final String TABLE_NAME = "keystore";
    private static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME +
            " (USERNAME TEXT PRIMARY KEY, PUBLICKEY TEXT )";

    private static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME ;

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
            sortOrder = "USERNAME";
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
            return getWritableDatabase().delete(TABLE_NAME, "USERNAME=?", new String[]{id});
        }
    }

    public int updateKeys(String id, ContentValues values) {
        if(id == null) {
            return getWritableDatabase().update(TABLE_NAME, values, null, null);
        } else {
            return getWritableDatabase().update(TABLE_NAME, values, "USERNAME=?", new String[]{id});
        }
    }

    public void resetTable() {
        getWritableDatabase().execSQL(SQL_DROP);
        getWritableDatabase().execSQL(SQL_CREATE);
    }
}
