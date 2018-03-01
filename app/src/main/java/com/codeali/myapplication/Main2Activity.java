package com.codeali.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;



public class Main2Activity extends AppCompatActivity {

    private String mParentPath;
    private File mParentPathFile;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    private void handleViewIntent() {
        mIntent = getIntent();
        String action = mIntent.getAction();
        if(TextUtils.equals(action, Intent.ACTION_VIEW)) {
            Uri beamUri = mIntent.getData();
            if(TextUtils.equals(beamUri.getScheme(), "file")) {
                mParentPath = handleFileUri(beamUri);
                Toast.makeText(this, mParentPath, Toast.LENGTH_SHORT).show();

            } else if (TextUtils.equals(beamUri.getScheme(), "content")) {
                mParentPathFile = handleContentUri(beamUri);
                Toast.makeText(this, (CharSequence) mParentPathFile, Toast.LENGTH_SHORT).show();
            }
        }
    }
    public String handleFileUri(Uri beamUri) {
        String fileName = beamUri.getPath();
        File copiedFile = new File(fileName);
        return copiedFile.getParent();
    }
    public File handleContentUri(Uri beamUri) {
        int fileNameIndex;
        File copiedFile;
        String fileName;
        if (!TextUtils.equals(beamUri.getAuthority(), MediaStore.AUTHORITY)) {
            /*
             * Handle content URIs for other content providers
             */
            return null;
            // For a MediaStore content URI
        } else {
            // Get the column that contains the file name
            String[] projection = { MediaStore.MediaColumns.DATA };
            Cursor pathCursor =
                    getContentResolver().query(beamUri, projection,
                            null, null, null);
            // Check for a valid cursor
            if (pathCursor != null &&
                    pathCursor.moveToFirst()) {
                // Get the column index in the Cursor
                fileNameIndex = pathCursor.getColumnIndex(
                        MediaStore.MediaColumns.DATA);
                // Get the full file name including path
                fileName = pathCursor.getString(fileNameIndex);
                // Create a File object for the filename
                copiedFile = new File(fileName);
                // Return the parent directory of the file
                return new File(copiedFile.getParent());
            } else {
                // The query didn't work; return null
                return null;
            }
        }

    }
}
