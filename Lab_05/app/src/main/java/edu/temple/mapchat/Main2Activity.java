package edu.temple.mapchat;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static edu.temple.mapchat.MainActivity.MY_PREFS_NAME;

public class Main2Activity extends AppCompatActivity implements ListFragment.OnFragmentInteractionListener, NfcAdapter.CreateNdefMessageCallback {


    String GetLocationsURL = "https://kamorris.com/lab/get_locations.php";
    String SetLocationURL = "https://kamorris.com/lab/register_location.php";
    String RegisterTokenURL = "https://kamorris.com/lab/fcm_register.php";
    private ListView userlv;
    private String username;
    private Button keysButton;

    private byte[] jsonToByte;
    private JSONObject jsonObj;

    private LocationManager lm;
    private Location location;
    public double longitude, longitudeLast;
    public double latitude, latitudeLast;

    private Boolean update = false;

    private static final String PROVIDER_NAME = "temple.mapchat.keys";

    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/keys");


    MapView mMapView;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        username = prefs.getString("username", null);

        jsonObj = new JSONObject ();

        Cursor c = getContentResolver().query(CONTENT_URI, null, "USERNAME = '" + username + "'", null, "USERNAME");
        if(c.moveToFirst()){
            //Toast.makeText(Main2Activity.this, "Public: " + c.getString(c.getColumnIndex( "PUBLICKEY")), Toast.LENGTH_LONG).show();

            String keypub  = "-----BEGIN PUBLIC KEY-----\n" +
                    c.getString(c.getColumnIndex( "PUBLICKEY")) +
                    "-----END PUBLIC KEY-----";

            try {
                jsonObj.put("user", username);
                jsonObj.put("key", keypub);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        jsonToByte = jsonObj.toString().getBytes(); //convert json to bytes


        mMapView = (MapView) findViewById(R.id.mapView);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null){
            longitude = location.getLongitude();
            longitudeLast =longitude;
            latitude = location.getLatitude();
            latitudeLast = latitude;
        }
        else{
            longitude = 0;
            longitudeLast = 0;
            latitude = 0;
            latitudeLast = 0;
        }

        //Toast.makeText(Main2Activity.this, String.valueOf(longitude), Toast.LENGTH_LONG).show();

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                //Toast.makeText(Main2Activity.this, String.valueOf(longitude), Toast.LENGTH_LONG).show();

                if(getDistanceBetweenTwoPoints(latitude, longitude, latitudeLast, longitudeLast) > 10){
                    longitudeLast = longitude;
                    latitudeLast = latitude;
                    new SendData(Main2Activity.this).execute();
                    //Toast.makeText(Main2Activity.this, String.valueOf("UPDATE"), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);



        userlv = (ListView) findViewById(R.id.userListView);

        new RegisterToken(Main2Activity.this).execute();

        new SendData(Main2Activity.this).execute();

        new GetData(Main2Activity.this).execute();


        scheduleUpdate();

        /*String msg = "test";

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();



        RSAHandler rsaHandler = new RSAHandler(this, username, username);

        Toast.makeText(this, rsaHandler.decryptMessage(rsaHandler.encryptMessage(msg)), Toast.LENGTH_LONG).show();*/

        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            Toast.makeText(this, "Sorry this device does not have NFC.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(this, this);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class GetData extends AsyncTask<Void, Void, Void>
    {
        public Context context;

        String ResultHolder;

        List<User> userList;

        public GetData(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            HttpServicesClass httpServiceObject = new HttpServicesClass(GetLocationsURL);
            try
            {
                httpServiceObject.ExecutePostRequest();

                if(httpServiceObject.getResponseCode() == 200)
                {
                    ResultHolder = httpServiceObject.getResponse();

                    if(ResultHolder != null)
                    {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(ResultHolder);

                            JSONObject jsonObject;

                            User user;

                            userList = new ArrayList<>();

                            Log.e("BLAH ", jsonArray.toString());

                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                user = new User();

                                jsonObject = jsonArray.getJSONObject(i);

                                user.UserName = jsonObject.getString("username");
                                user.Latitude = jsonObject.getString("latitude");
                                user.Longitude = jsonObject.getString("longitude");
                                user.ULatitude = String.valueOf(latitudeLast);
                                user.ULongitude = String.valueOf(longitudeLast);

                                if (!user.UserName.equals(username)){
                                    userList.add(user);
                                }

                            }
                        }
                        catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Toast.makeText(context, httpServiceObject.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            if(userList != null)
            {
                //Toast.makeText(Main2Activity.this, userList.get(0).UserName, Toast.LENGTH_LONG).show();

                final Partners partners = new Partners(userList, latitudeLast, longitudeLast);


                partners.sort();

                ListAdapterClass adapter = new ListAdapterClass(partners.partners, context);

                userlv.setAdapter(adapter);

                //adapter.notifyDataSetChanged();

                userlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        User u = partners.partners.get(position);
                        //prestationEco str = (prestationEco)o; //As you are using Default String Adapter
                        //Toast.makeText(getBaseContext(), u.UserName, Toast.LENGTH_SHORT).show();

                        Cursor c = getContentResolver().query(CONTENT_URI, null, "USERNAME = '" + u.UserName + "'", null, "USERNAME");
                        if(c.moveToFirst()){
                            //Toast.makeText(Main2Activity.this, "Public: " + c.getString(c.getColumnIndex( "PUBLICKEY")), Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(Main2Activity.this, ChatActivity.class);
                            myIntent.putExtra("user", u.UserName);
                            startActivity(myIntent);
                        }
                        else {
                            Toast.makeText(Main2Activity.this, "Meet up with " + u.UserName + " in order to exchange public keys!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap mMap) {
                        GoogleMap googleMap = mMap;

                        mMap.clear();

                        // For showing a move to my location button
                        //googleMap.setMyLocationEnabled(true);

                        for (int i = 0; i< userList.size(); i++){

                            // For dropping a marker at a point on the Map
                            LatLng sydney = new LatLng(Double.parseDouble(userList.get(i).Latitude),Double.parseDouble(userList.get(i).Longitude));
                            googleMap.addMarker(new MarkerOptions().position(sydney).title(userList.get(i).UserName));

                        }

                        LatLng sydney = new LatLng(latitudeLast,longitudeLast);
                        googleMap.addMarker(new MarkerOptions().position(sydney).title("Me").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                        if (!update) {
                            // For zooming automatically to the location of the marker
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            update = true;
                        }


                    }
                });
            }
        }
    }

    private class SendData extends AsyncTask<Void, Void, Void> {
        public Context context;


        public SendData(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // url where the data will be posted

// HttpClient
            HttpClient httpClient = new DefaultHttpClient();

// post header
            HttpPost httpPost = new HttpPost(SetLocationURL);

// add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("user", username));
            nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(latitudeLast)));
            nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(longitudeLast)));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

// execute HTTP post request
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {

                String responseStr = null;
                try {
                    responseStr = EntityUtils.toString(resEntity).trim();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("BLAH", "Response: " + responseStr);

                // you can add an if statement here and do other actions based on the response
            }
            return null;
        }


    }

    private class RegisterToken extends AsyncTask<Void, Void, Void> {
        public Context context;


        public RegisterToken(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // url where the data will be posted

            // HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // post header
            HttpPost httpPost = new HttpPost(RegisterTokenURL);

            // add your data
            final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("user", username));
            nameValuePairs.add(new BasicNameValuePair("token", FirebaseInstanceId.getInstance().getToken()));



            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            // execute HTTP post request
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {

                String responseStr = null;
                try {
                    responseStr = EntityUtils.toString(resEntity).trim();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("TOKEN", "Response: " + responseStr);

                // you can add an if statement here and do other actions based on the response
            }
            return null;
        }
    }


    public void scheduleUpdate() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                new GetData(Main2Activity.this).execute();
                Log.e("BLAH: ", "UPDATE");
                new Handler().postDelayed(this, 30000);
            }
        }, 30000);
    }

    private float getDistanceBetweenTwoPoints(double lat1,double lon1,double lat2,double lon2) {

        float[] distance = new float[2];

        Location.distanceBetween( lat1, lon1,
                lat2, lon2, distance);

        return distance[0];
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", jsonToByte);
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("newMessage")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent myIntent = new Intent(Main2Activity.this, ChatActivity.class);
            myIntent.putExtra("user", intent.getExtras().getString("user"));
            myIntent.putExtra("message", intent.getExtras().getString("message"));
            startActivity(myIntent);
        }
    };
}
