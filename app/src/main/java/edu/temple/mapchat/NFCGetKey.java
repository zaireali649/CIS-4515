package edu.temple.mapchat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class NFCGetKey extends AppCompatActivity {

    private TextView mTextView;

    private static final String PROVIDER_NAME = "temple.mapchat.keys";

    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/keys");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcget_key);
        mTextView = (TextView) findViewById(R.id.text_view);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            mTextView.setText(new String(message.getRecords()[0].getPayload()));

            String JSONstring = new String(message.getRecords()[0].getPayload());

            JSONObject jsonObj = null;

            try {
                jsonObj = new JSONObject(JSONstring);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Toast.makeText(NFCGetKey.this, "JSON: " + jsonObj.toString(), Toast.LENGTH_LONG).show();

            Toast.makeText(NFCGetKey.this, "I got them keys, keys, keys", Toast.LENGTH_LONG).show();

            String username = null;
            String pubk = null;
            try {
                username = jsonObj.getString("user");
                pubk = jsonObj.getString("key");

                pubk = pubk.replace("-----BEGIN PUBLIC KEY-----", "");
                pubk = pubk.replace("-----END PUBLIC KEY-----", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            ContentValues contentValues = new ContentValues();
            contentValues.put("USERNAME", username);
            contentValues.put("PUBLICKEY" , pubk);

            Cursor c = getContentResolver().query(CONTENT_URI, null, "USERNAME = '" + username + "'", null, "USERNAME");

            if (c.moveToFirst()) {
                getContentResolver().update(CONTENT_URI, contentValues, "USERNAME = '" + username + "'", null);
            }
            else{
                getContentResolver().insert(CONTENT_URI, contentValues);
            }


            finish();


        } else
            mTextView.setText("Waiting for NDEF Message");

    }
}
