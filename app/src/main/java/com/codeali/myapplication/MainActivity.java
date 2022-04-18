package com.codeali.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity {

    KeyPairGenerator kpg;
    KeyPair kp;
    PublicKey publicKey;
    PrivateKey privateKey;
    byte [] encryptedBytes = null;
    byte [] decryptedBytes;
    Cipher cipher,cipher1;
    String decrypted;

    Button getKeyButton, encryptButton, decryptButton;
    EditText encryptEdit;

    private static final String PROVIDER_NAME = "codeali.myapplication.keys";

    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/keys");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getKeyButton = (Button) findViewById(R.id.getKeyButton);
        encryptButton = (Button) findViewById(R.id.encryptButton);
        decryptButton = (Button) findViewById(R.id.decryptButton);

        encryptEdit = (EditText) findViewById(R.id.encryptEdit);

        getKeyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("ALIAS", "appkeys");
                    contentValues.put("PUBLICKEY" , pubk);
                    contentValues.put("PRIVATEKEY", prik);

                    Cursor c = getContentResolver().query(CONTENT_URI, null, "ALIAS = 'appkeys'", null, "ALIAS");

                    if (c.moveToFirst()) {
                        getContentResolver().update(CONTENT_URI, contentValues, "ALIAS = 'appkeys'", null);
                    }
                    else{
                        getContentResolver().insert(CONTENT_URI, contentValues);
                    }

                    Toast.makeText(MainActivity.this, "Keys Generated!",
                            Toast.LENGTH_LONG).show();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });

        encryptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Cursor c = getContentResolver().query(CONTENT_URI, null, "ALIAS = 'appkeys'", null, "ALIAS");

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
                }
            }
        });

        decryptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (encryptedBytes != null) {
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
                }
            }
        });
    }
}
