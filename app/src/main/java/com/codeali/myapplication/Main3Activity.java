package com.codeali.myapplication;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Main3Activity extends AppCompatActivity {

    private TextView mTextView;
    Cipher cipher,cipher1;
    byte [] decryptedBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mTextView = (TextView) findViewById(R.id.mTextView);

    }


    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred

            File pub = new File(new File(Environment.getExternalStorageDirectory() + "/Download/"), "mypublic.pem");
            if(pub.exists()){
                //Toast.makeText(Main3Activity.this, "Got Beam Key", Toast.LENGTH_SHORT).show();
                try {
                    Scanner in = new Scanner(pub);
                    in.nextLine();
                    String line = in.nextLine(); // Public Key bytes

                    byte[] sigBytes2 = Base64.decode(line, Base64.NO_WRAP);

                    // Convert the public key bytes into a PublicKey object
                    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes2);
                    KeyFactory keyFact = KeyFactory.getInstance("RSA", "BC");
                    PublicKey pubk = keyFact.generatePublic(x509KeySpec);

                    cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.DECRYPT_MODE, pubk);

                    decryptedBytes = cipher.doFinal(message.getRecords()[0].getPayload());




                    mTextView.setText(new String(decryptedBytes));


                    //Toast.makeText(MainActivity.this, line, Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }


            }
            else{
                mTextView.setText("Can't decrypt without public key!");
                //Toast.makeText(Main3Activity.this, "No Beam Key", Toast.LENGTH_SHORT).show();
            }



        } else
            mTextView.setText("Waiting for NDEF Message");

    }
}
