package edu.temple.mapchat;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
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

import static android.content.Context.MODE_PRIVATE;
import static edu.temple.mapchat.MainActivity.MY_PREFS_NAME;

/**
 * Created by Ziggy on 3/28/2018.
 */

public class RSAHandler {

    private Activity context;

    String chatPartner, username, pubks, priks;

    Cipher cipher, cipher1;

    byte [] decryptedBytes, encryptedBytes;

    SharedPreferences prefs = null;

    private static final String PROVIDER_NAME = "temple.mapchat.keys";

    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/keys");

    public RSAHandler(Activity context, String chatPartner, String username) {
        this.context = context;
        this.chatPartner = chatPartner;
        this.username = username;

        Cursor c = context.getContentResolver().query(CONTENT_URI, null, "USERNAME = '" + chatPartner + "'", null, "USERNAME");
        if(c.moveToFirst()) {
            this.pubks = c.getString(c.getColumnIndex("PUBLICKEY"));
        }

        this.prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        this.priks = prefs.getString("privatekey", null);
    }

    public RSAHandler(Activity context, String chatPartner, String username, String pubks, String priks) {
        this.context = context;
        this.chatPartner = chatPartner;
        this.username = username;

        this.pubks = pubks;

        this.priks = priks;
    }



    public String encryptMessage (String s){


            try {

                //Toast.makeText(ChatActivity.this, pk, Toast.LENGTH_LONG).show();

                cipher = Cipher.getInstance("RSA");

                byte[] sigBytes2 = Base64.decode(pubks, Base64.NO_WRAP);
                // Convert the public key bytes into a PublicKey object
                X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes2);
                KeyFactory keyFact = KeyFactory.getInstance("RSA", "BC");

                PublicKey pubk = keyFact.generatePublic(x509KeySpec);

                cipher.init(Cipher.ENCRYPT_MODE, pubk);

                encryptedBytes = cipher.doFinal(s.getBytes());

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
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


        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP);
    }

    public String decryptMessage (String s){
        try {



            Cursor c = context.getContentResolver().query(CONTENT_URI, null, "USERNAME = '" + username + "'", null, "USERNAME");
            if(c.moveToFirst()) {
                //Toast.makeText(ChatActivity.this, c.getString(c.getColumnIndex("PUBLICKEY")), Toast.LENGTH_LONG).show();
            }


            byte[] sigBytes2 = Base64.decode(priks, Base64.NO_WRAP);
            // Convert the public key bytes into a PublicKey object
            PKCS8EncodedKeySpec x509KeySpec = new PKCS8EncodedKeySpec(sigBytes2);
            KeyFactory keyFact = KeyFactory.getInstance("RSA");
            PrivateKey prik = keyFact.generatePrivate(x509KeySpec);
            cipher1 = Cipher.getInstance("RSA");
            cipher1.init(Cipher.DECRYPT_MODE, prik);

            Log.e("decrypt", sigBytes2.toString());
            Log.e("decrypt", s.toString());
            decryptedBytes = cipher1.doFinal(Base64.decode(s, Base64.NO_WRAP));
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

        return new String(decryptedBytes);
    }

    public String getPubks() {
        return pubks;
    }

    public String getPriks() {
        return priks;
    }
}
