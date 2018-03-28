package edu.temple.mapchat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class MainActivity extends AppCompatActivity {

    private Button start;
    private EditText username;

    KeyPairGenerator kpg;
    KeyPair kp;
    PublicKey publicKey;
    PrivateKey privateKey;

    public static final String MY_PREFS_NAME = "myprefs";

    private static final String PROVIDER_NAME = "temple.mapchat.keys";

    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/keys");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        start = (Button) findViewById(R.id.button);
        username = (EditText) findViewById(R.id.editText);

        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);


        username.setText(prefs.getString("username", null));

        checkLocationPermission();

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button


                if (checkLocationPermission()) {

                    if (isEmpty(username)) {
                        Toast.makeText(MainActivity.this, "Enter a username", Toast.LENGTH_LONG).show();
                    } else {

                        try {

                            Log.e("username", prefs.getString("username", "null"));

                            if (username.getText().toString() != prefs.getString("username", null)){ // Username Changed
                                // Change Username
                                editor.putString("username",  username.getText().toString());

                                // Change Keys - Generate Public and Private

                                kpg = KeyPairGenerator.getInstance("RSA");

                                kpg.initialize(1024);
                                kp = kpg.genKeyPair();
                                publicKey = kp.getPublic();
                                privateKey = kp.getPrivate();

                                // Send the public key bytes to the other party...
                                byte[] publicKeyBytes = publicKey.getEncoded();
                                byte[] privateKeyBytes = privateKey.getEncoded();

                                //Convert Public key to String
                                String pubk = Base64.encodeToString(publicKeyBytes, Base64.NO_WRAP);
                                String prik = Base64.encodeToString(privateKeyBytes, Base64.NO_WRAP);

                                // Reset Table
                                KeyDataBase keyDataBase = new KeyDataBase(MainActivity.this);
                                keyDataBase.resetTable();

                                // Add Public Key
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("USERNAME", username.getText().toString());
                                contentValues.put("PUBLICKEY" , pubk);

                                Cursor c = getContentResolver().query(CONTENT_URI, null, "USERNAME = '" + username.getText().toString() + "'", null, "USERNAME");

                                if (c.moveToFirst()) {
                                    getContentResolver().update(CONTENT_URI, contentValues, "USERNAME = '" + username.getText().toString() + "'", null);
                                }
                                else{
                                    getContentResolver().insert(CONTENT_URI, contentValues);
                                }

                                // Add Private Key
                                editor.putString("privatekey", prik);

                                editor.apply();
                            }

                            Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
                            startActivity(myIntent);
                            finish();

                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    }
                }






            }
        });

    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Missing GPS Permission!")
                        .setMessage("Please grant location permissions!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}
