package com.example.userp.detecttest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private static final String TAG = "MainActivity";

    /*** Temperate Start Buttons ***/
    private Button startBeaconMonitoringBtn = null;
    private Button startAccelBtn = null;
    private Button speedBtn = null;
    private Button getStateBtn = null;
    private Button resetBtn = null;

    private Button setMagneticDashBoardBtn = null;
    private Button setMagneticFrontBtn = null;
    private Button setMagneticBackBtn = null;

    /** Sensor **/
    private GPSController gpsController = null;
    private MagnetController magnetController = null;
    private AccelController accelController = null;

    private MySystem mySystem = MySystem.getInstance();

    private TextView currentStateTextView = null;

    private BeaconController beaconController = null;
    private BeaconScanController beaconScanController = null;

    String BEACON_PARSER = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25";
    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gpsController = new GPSController(this);
        magnetController = new MagnetController(this);
        accelController = new AccelController(this);

        currentStateTextView = (TextView) findViewById(R.id.stateTextView);

        //initializing starting buttons
        startBeaconMonitoringBtn = (Button)findViewById(R.id.startBeaconMonitoringBtn);
        startAccelBtn = (Button)findViewById(R.id.startAccelBtn);
        speedBtn = (Button)findViewById(R.id.speedButton);
        getStateBtn = (Button)findViewById(R.id.getStateButton);
        resetBtn = (Button)findViewById(R.id.resetStateBtn);

        setMagneticBackBtn = (Button)findViewById(R.id.setMagneticBackBtn);
        setMagneticDashBoardBtn = (Button)findViewById(R.id.setMagneticDashBoardBtn);
        setMagneticFrontBtn = (Button) findViewById(R.id.setMagneticFrontBtn);

        gpsController.startGPS();
        magnetController.startMangetometer();

        beaconController = new BeaconController(this);
        beaconScanController = new BeaconScanController(this);

        startBeaconMonitoringBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beaconController.startBeaconTransmitter(2550, 2);
                beaconScanController.startBeaconScan();
            }
        });
        startAccelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accelController.startAccel();
            }
        });
        speedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySystem.setState(MySystem.SYSTEM_START);
                currentStateTextView.setText("Current state is "+MySystem.getInstance().getState());
            }
        });
        getStateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Current state is "+MySystem.getInstance().getState()+"\nMagnetic state is "+MySystem.getInstance().getMagneticState().getState(), Toast.LENGTH_SHORT).show();
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySystem.setState(MySystem.SYSTEM_SLEEP);
                mySystem.setMagneticState(new MagneticState(System.currentTimeMillis(), MagneticState.NON_STATE));
            }
        });
        setMagneticFrontBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySystem.setMagneticState(new MagneticState(System.currentTimeMillis(), MagneticState.FRONT_SEAT));
            }
        });
        setMagneticBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySystem.setMagneticState(new MagneticState(System.currentTimeMillis(), MagneticState.BACK_SEAT));
            }
        });
        setMagneticDashBoardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySystem.setMagneticState(new MagneticState(System.currentTimeMillis(), MagneticState.DASH_BOARD));
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        mySystem.setState(MySystem.SYSTEM_SLEEP);
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "onBeaconServiceConnect()");

    }
}


