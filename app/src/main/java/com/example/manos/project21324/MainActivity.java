package com.example.manos.project21324;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity {
    DatabaseHandler db;
    LocationManager locationManager;
    LocationListener locationListener;

    EditText latitudeView;
    EditText longitudeView;
    EditText useridView;
    EditText datetimeView;

    String latitudeValue;
    String longitudeValue;
    String useridValue;
    String datetimeValue;

    Button saveDataButton;
    Button nextActivityButton;
    Location location;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener () {

            @Override
            public void onLocationChanged(Location location) {
//                Log.d("Location", location.toString());
                latitudeView.setText(String.valueOf(location.getLatitude()), TextView.BufferType.EDITABLE);
                longitudeView.setText(String.valueOf(location.getLongitude()), TextView.BufferType.EDITABLE);
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

        initializeViews();
        this.db = new DatabaseHandler(this); //initialize db object
        final Handler dateHandler = new Handler(getMainLooper()); //datehandler for dt view

        //change dateTime view every 10 milliseconds
        dateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                datetimeView.setText(new SimpleDateFormat("d/M/yyyy HH:mm:ss", Locale.ENGLISH).format(new Date()));
                dateHandler.postDelayed(this, 10);
            }
        }, 10);

        saveValidation();

        List<Coordinates> Coordinates = db.getAllCoordinates();
        for (Coordinates cn : Coordinates) {
            db.deleteCoordinates(new Coordinates(cn.get_id()));
            String log = "userid: " + cn.get_userid() + " Id: "+cn.get_id()+" ,Latitude: " + cn.get_latitude() + " ,Longitude: " + cn.get_longitude() + " ,dt:" + cn.get_dt();
            // Writing Coordinates to log
            Log.d("Initial Coordinates: ", log);
        }
    }

    //Onclick insert data to SQLite.Then log the row inserted and all rows in database.
    View.OnClickListener saveDataListener = new View.OnClickListener() {
        public void onClick(View view) {
            Toast.makeText(getBaseContext(), "Successfully Saved Data to Database!", Toast.LENGTH_LONG).show();
            latitudeValue = latitudeView.getText().toString();
            longitudeValue = longitudeView.getText().toString();
            useridValue = useridView.getText().toString();
            datetimeValue = datetimeView.getText().toString();

            // Inserting Coordinates
            Log.d("Insert: ", "Inserting Coordinates..");
            db.addCoordinates(new Coordinates(useridValue, latitudeValue, longitudeValue, datetimeValue));


            // Reading all Coordinates
            Log.d("Reading: ", "Reading all coordinates..");
            List<Coordinates> Coordinates = db.getAllCoordinates();
            for (Coordinates cn : Coordinates) {
                String log = "userid: " + cn.get_userid()+ " Id: "+cn.get_id()+" Latitude: " + cn.get_latitude() + " Longitude: " + cn.get_longitude() + " dt:" + cn.get_dt();
                Log.d("Coordinates: ", log);

            }
        }
    };

    //Get elements of screen.
    public void initializeViews () {
        useridView = findViewById(R.id.userID);
        latitudeView = findViewById(R.id.latitudeID);
        longitudeView = findViewById(R.id.longitudeID);
        datetimeView = findViewById(R.id.dateTimeID);
        datetimeView.setEnabled(false);

        saveDataButton = findViewById(R.id.buttonID);
        saveDataButton.setOnClickListener(saveDataListener);
        saveDataButton.setEnabled(false);

        nextActivityButton = findViewById(R.id.nextActivityButtonID);
        nextActivityButton.setOnClickListener(nextActivityListener);
    }

    //validate whether all editTexts have values or not.If not, button is not clickable. This triggers every time a text changes(except dt).
    public void saveValidation () {
        useridView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0 && latitudeView.getText().length() != 0 && longitudeView.getText().length() != 0 ){
                    saveDataButton.setEnabled(true);
                } else {
                    saveDataButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        latitudeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0 && useridView.getText().length() != 0 && longitudeView.getText().length() != 0) {
                    saveDataButton.setEnabled(true);
                } else {
                    saveDataButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        longitudeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0 && latitudeView.getText().length() !=0 && useridView.getText().length() != 0) {
                    saveDataButton.setEnabled(true);
                } else {
                    saveDataButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //onclick go to next activity
    View.OnClickListener nextActivityListener = new View.OnClickListener() {
        public void onClick(View view) {
            startActivity(new Intent( MainActivity.this, SecondActivity.class));
        }
    };
}