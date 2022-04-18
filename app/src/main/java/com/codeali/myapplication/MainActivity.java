package com.codeali.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback{

    KeyPairGenerator kpg;
    KeyPair kp;
    PublicKey publicKey;
    PrivateKey privateKey;
    byte [] encryptedBytes = null;
    byte [] decryptedBytes;
    Cipher cipher,cipher1;
    String decrypted;

    private NfcAdapter nfcAdapter;





    private NfcAdapter mNfcAdapter;
    private Uri[] mFileUris = new Uri[10];
    boolean mAndroidBeamAvailable = false;


    Button getKeyButton, encryptButton, decryptButton;
    EditText encryptEdit;

    private static final String PROVIDER_NAME = "codeali.myapplication.keys";

    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/keys");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PackageManager pm = this.getPackageManager();
        // Check whether NFC is available on device
        if (!pm.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            // NFC is not available on the device.
            Toast.makeText(this, "The device does not has NFC hardware.",
                    Toast.LENGTH_SHORT).show();
        }
        // Check whether device is running Android 4.1 or higher
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            // Android Beam feature is not supported.
            Toast.makeText(this, "Android Beam is not supported.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            // NFC and Android Beam file transfer is supported.
            //Toast.makeText(this, "Android Beam is supported on your device.", Toast.LENGTH_SHORT).show();
            if (isWriteStoragePermissionGranted() && isReadStoragePermissionGranted()){

            }
        }

        getKeyButton = (Button) findViewById(R.id.getKeyButton);
        encryptButton = (Button) findViewById(R.id.encryptButton);
        decryptButton = (Button) findViewById(R.id.decryptButton);


        encryptEdit = (EditText) findViewById(R.id.encryptEdit);

        getKeyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (isWriteStoragePermissionGranted() && isReadStoragePermissionGranted()){
                    getKeys();
                }
            }
        });

        encryptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                File root = new File(Environment.getExternalStorageDirectory(), "Keys");
                if (!root.exists()) {
                    root.mkdirs();
                }
                if(new File(root, "mypublic.pem").exists()){
                    //sendFile(v, "mypublic.pem", root);

                    mNfcAdapter = NfcAdapter.getDefaultAdapter(MainActivity.this);
                    FileUriCallBack mFileUriCallback = new FileUriCallBack();
                    mNfcAdapter.setBeamPushUrisCallback(mFileUriCallback, MainActivity.this);



                }
                else{
                    Toast.makeText(MainActivity.this, "No Keys!",
                            Toast.LENGTH_LONG).show();
                }

                /*Cursor c = getContentResolver().query(CONTENT_URI, null, "ALIAS = 'appkeys'", null, "ALIAS");

                if (c.moveToFirst()) {
                    try {
                        String plain = String.valueOf(encryptEdit.getText());

                        cipher = Cipher.getInstance("RSA");

                        byte[] sigBytes2 = Base64.decode(c.getString(c.getColumnIndex( "PUBLICKEY")), Base64.NO_WRAP);
                        // Convert the public key bytes into a PublicKey object
                        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes2);
                        KeyFactory keyFact = KeyFactory.getInstance("RSA", "BC");
                        PublicKey pubk = keyFact.generatePublic(x509KeySpec);

                        cipher.init(Cipher.ENCRYPT_MODE, pubk);
                        encryptedBytes = cipher.doFinal(plain.getBytes());
                        Toast.makeText(MainActivity.this, "Encrypted",
                                Toast.LENGTH_SHORT).show();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (NoSuchProviderException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "No keys",
                            Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        decryptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                File root = new File(Environment.getExternalStorageDirectory(), "Keys");
                if (!root.exists()) {
                    root.mkdirs();
                }
                if(new File(root, "private.pem").exists()){
                    //sendFile(v, "mypublic.pem", root);

                    nfcAdapter = NfcAdapter.getDefaultAdapter(MainActivity.this);

                    nfcAdapter.setNdefPushMessageCallback(MainActivity.this, MainActivity.this);





                }
                else{
                    Toast.makeText(MainActivity.this, "No Keys!",
                            Toast.LENGTH_LONG).show();
                }








                /*if (encryptedBytes != null) {
                    try {

                        Cursor c = getContentResolver().query(CONTENT_URI, null, "ALIAS = 'appkeys'", null, "ALIAS");
                        c.moveToFirst();

                        byte[] sigBytes2 = Base64.decode(c.getString(c.getColumnIndex( "PRIVATEKEY")), Base64.NO_WRAP);
                        // Convert the public key bytes into a PublicKey object
                        PKCS8EncodedKeySpec x509KeySpec = new PKCS8EncodedKeySpec(sigBytes2);
                        KeyFactory keyFact = KeyFactory.getInstance("RSA");
                        PrivateKey prik = keyFact.generatePrivate(x509KeySpec);

                        cipher1 = Cipher.getInstance("RSA");
                        cipher1.init(Cipher.DECRYPT_MODE, prik);
                        decryptedBytes = cipher1.doFinal(encryptedBytes);
                        decrypted = new String(decryptedBytes);

                        //encryptEdit.setText(decrypted);
                        Toast.makeText(MainActivity.this, "Decrypted text: " + decrypted,
                                Toast.LENGTH_SHORT).show();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Nothing Encrypted",
                            Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted2");
            return true;
        }
    }

    public void sendFile(View view, String fileName, File fileDirectory) {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // Check whether NFC is enabled on device
        if(!nfcAdapter.isEnabled()){
            // NFC is disabled, show the settings UI
            // to enable NFC
            Toast.makeText(this, "Please enable NFC.",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }
        // Check whether Android Beam feature is enabled on device
        else if(!nfcAdapter.isNdefPushEnabled()) {
            // Android Beam is disabled, show the settings UI
            // to enable Android Beam
            Toast.makeText(this, "Please enable Android Beam.",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
        }
        else {
            // NFC and Android Beam both are enabled

            // File to be transferred

            // Create a new file using the specified directory and name
            File fileToTransfer = new File(fileDirectory, fileName);
            fileToTransfer.setReadable(true, false);

            nfcAdapter.setBeamPushUris(
                    new Uri[]{Uri.fromFile(fileToTransfer)}, this);
        }
    }

    public void getKeys(){
        try {
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

            String keypri  = "-----BEGIN RSA PRIVATE KEY-----\n" +
                    prik +
                    "\n-----END RSA PRIVATE KEY-----\n";

            String keypub  = "-----BEGIN RSA PUBLIC KEY-----\n" +
                    pubk +
                    "\n-----END RSA PUBLIC KEY-----\n";

            ContentValues contentValues = new ContentValues();
            contentValues.put("ALIAS", "appkeys");
            contentValues.put("PUBLICKEY" , keypub);
            contentValues.put("PRIVATEKEY", keypri);

                    /*Cursor c = getContentResolver().query(CONTENT_URI, null, "ALIAS = 'appkeys'", null, "ALIAS");

                    if (c.moveToFirst()) {
                        getContentResolver().update(CONTENT_URI, contentValues, "ALIAS = 'appkeys'", null);
                    }
                    else{
                        getContentResolver().insert(CONTENT_URI, contentValues);
                    }*/

            //getContentResolver().insert(CONTENT_URI, contentValues);

            File root = new File(Environment.getExternalStorageDirectory(), "Keys");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, "mypublic.pem");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(keypub);
            writer.flush();
            writer.close();

            gpxfile = new File(root, "private.pem");
            writer = new FileWriter(gpxfile);
            writer.append(keypri);
            writer.flush();
            writer.close();

            Toast.makeText(MainActivity.this, "Keys Generated!",
                    Toast.LENGTH_LONG).show();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private class FileUriCallBack implements NfcAdapter.CreateBeamUrisCallback {

        public FileUriCallBack() {

        }

        @Override
        public Uri[] createBeamUris(NfcEvent event) {

            File root = new File(Environment.getExternalStorageDirectory(), "Keys");

            File requestFile = new File(root, "mypublic.pem");
            requestFile.setReadable(true, false);
            Uri fileUri = Uri.fromFile(requestFile);
            if(fileUri != null) {
                mFileUris[0] = fileUri;
            }
            // TODO Auto-generated method stub
            return mFileUris;
        }

    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {

        //try {

        try {
            Scanner in = new Scanner(new File(new File(Environment.getExternalStorageDirectory(), "Keys"), "private.pem"));
            in.nextLine();
            String line = in.nextLine(); // Private Key bytes

            String message = encryptEdit.getText().toString();


            byte[] sigBytes2 = Base64.decode(line, Base64.NO_WRAP);

            // Convert the public key bytes into a PublicKey object
            PKCS8EncodedKeySpec x509KeySpec = new PKCS8EncodedKeySpec(sigBytes2);
            KeyFactory keyFact = KeyFactory.getInstance("RSA");
            PrivateKey prik = keyFact.generatePrivate(x509KeySpec);

            cipher1 = Cipher.getInstance("RSA");
            cipher1.init(Cipher.ENCRYPT_MODE, prik);
            encryptedBytes = cipher1.doFinal(message.getBytes());
            //Toast.makeText(MainActivity.this, "Got bytes",Toast.LENGTH_LONG).show();

            NdefRecord ndefRecord = NdefRecord.createMime("text/plain", encryptedBytes);
            //NdefRecord ndefRecord = NdefRecord.createMime("text/plain", "blah".getBytes());
            NdefMessage ndefMessage = new NdefMessage(ndefRecord);
            return ndefMessage;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }




}
