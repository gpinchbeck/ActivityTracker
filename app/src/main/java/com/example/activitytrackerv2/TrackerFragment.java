package com.example.activitytrackerv2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.internal.location.zzz;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class TrackerFragment extends Fragment implements OnMapReadyCallback {

    View v;
    private AppCompatButton startStopBtn;
    private boolean workoutBegin;

    private Chronometer chronometer, chronometer2;
    private long pauseOffset;
    private boolean running;

    SensorManager sensorManager;
    Sensor stepSensor;
    private TextView stepText;
    private long stepCount = 0;

    private TextView distanceText;
    private float d;

    private TextView paceText;

    private SupportMapFragment mapFragment;

    SensorEventListener sensorEventListener = new StepClass();
    private GoogleMap map;
    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static String TAG;

    private Polyline polyline = null;
    List<LatLng> latLngList = new ArrayList<>();
    PolylineOptions polylineOptions = new PolylineOptions();

    LocationManager locationManager;
    LocationListener locationListener = new LocationGetter();

    int count = 0;

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_tracker, container, false);

        TAG = TrackerFragment.class.getSimpleName();

        startStopBtn = v.findViewById(R.id.startBtn);
        chronometer = v.findViewById(R.id.chronometer);
        chronometer2 = v.findViewById(R.id.chronometer2);

        stepText = v.findViewById(R.id.stepCountText);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        distanceText = v.findViewById(R.id.distanceText);
        paceText = v.findViewById(R.id.paceText);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        getLocationPermission();
        if (locationPermissionGranted) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startStopBtn.getText().toString().toLowerCase().equals("start")) {
                    workoutBegin = true;
                    startStopBtn.setText("Stop");
                    startWorkout();
                } else {
                    workoutBegin = false;
                    startStopBtn.setText("Start");
                    stopWorkout();
                }
            }
        });

        chronometer2.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer2) {
                //Toast.makeText(getContext(),""+chronometer2.getBase(),Toast.LENGTH_SHORT).show();
                if ((SystemClock.elapsedRealtime() - chronometer2.getBase()) >= 1000) {
                    count++;
                    if (!running){
                        stepCount = 0;
                    }
                    getDistance(stepCount);
                    if ((SystemClock.elapsedRealtime() - chronometer2.getBase()) >= 10000){
                        //Toast.makeText(getContext(), distance + " km", Toast.LENGTH_SHORT).show();
                        //map.clear();
                        chronometer2.setBase(SystemClock.elapsedRealtime());

                    }
                    getPace();
                    getDeviceLocation();
                    //Toast.makeText(getContext(), "Size: " + latLngList.size(), Toast.LENGTH_SHORT).show();
                    //map.addMarker(new MarkerOptions().position(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude() + x)));
                    //Polyline polyline = map.addPolyline(polylineOptions);
//                    Polyline polyline = map.addPolyline(new PolylineOptions().clickable(true).add(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())));
//                    map.addPolyline(new PolylineOptions().clickable(true).add(new LatLng(-33.8523341, 151.2106085)));
                    //Polyline line = map.addPolyline(new PolylineOptions().add(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), new LatLng(40.7+x, -74.0)).width(5).color(Color.RED));
                    //Toast.makeText(getContext(), "X: " + String.valueOf(x), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getContext(), "Lng: " + lastKnownLocation.getLongitude(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        updateLocationUI();
        getDeviceLocation();
    }

    private void updateLocationUI(){
        if (map == null){
            return;
        }
        try{
            if (locationPermissionGranted){
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            }else{
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e){
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation(){
        try{
            if (locationPermissionGranted){
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
//                Task<Location> currentResult = fusedLocationProviderClient.getCurrentLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()){
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null){
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()), 15));
                                latLngList.add(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()));
                                //Toast.makeText(getContext(), "Added LatLng",Toast.LENGTH_SHORT).show();

                                //map.addMarker(new MarkerOptions().position(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())));
                                //Toast.makeText(getContext(), "Lat: " + lastKnownLocation.getLatitude() + " Lng: " + lastKnownLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    public void startWorkout(){
        if (!running){
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            chronometer2.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer2.start();
            //Toast.makeText(getContext(), "Real: "+SystemClock.elapsedRealtime(), Toast.LENGTH_SHORT).show();
            map.clear();
            latLngList.clear();
            stepCount = 0;
            d = 0;
            getDistance(stepCount);
            running = true;
        }
    }

    public void stopWorkout(){
        if (running){
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer2.getBase();
            chronometer2.stop();
            running = false;
            polylineOptions.addAll(latLngList);
            polylineOptions.width(20).color(Color.RED);
            map.addPolyline(polylineOptions);
            //map.addPolyline(new PolylineOptions().addAll(latLngList).width(5).color(Color.GREEN));
//            Toast.makeText(getContext(),"LatLngList size: " + latLngList.size(), Toast.LENGTH_SHORT).show();
//            for (int i = 0; i < latLngList.size(); i++){
//                if (i+1 == latLngList.size()){
//                    break;
//                }
//                map.addPolyline(new PolylineOptions().add(latLngList.get(i),latLngList.get(i+1)).width(5).color(Color.GREEN));
//                Toast.makeText(getContext(),"Point made: " + i + " to: " + (i+1), Toast.LENGTH_SHORT).show();
//            }
            //map.addPolyline(new PolylineOptions().add(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), new LatLng(40.7, -74.0)).width(5).color(Color.RED));
        }
        pauseOffset = 0;
    }

    private class LocationGetter implements LocationListener{

        @Override
        public void onLocationChanged(@NonNull Location location) {
            //map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        }
        @Override
        public void onProviderDisabled(String provider) {
            Log.d("Latitude","disable");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("Latitude","enable");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("Latitude","status");
        }
    }

    private class StepClass implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent event) {
            Sensor sensor = event.sensor;

            if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
                stepCount++;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }


    @Override
    public void onResume(){
        super.onResume();
        sensorManager.registerListener(sensorEventListener, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onStop(){
        super.onStop();
        sensorManager.unregisterListener(sensorEventListener, stepSensor);
    }


    public void getDistance(long stepCount){

        d = 0;

        for (int i = 0; i < latLngList.size(); i++) {
            if (i+1 == latLngList.size()){
                break;
            }
            LatLng x = latLngList.get(i);
            LatLng y = latLngList.get(i+1);
            Location locationA = new Location("A");
            locationA.setLatitude(x.latitude);
            locationA.setLongitude(x.longitude);

            Location locationB = new Location("B");
            locationB.setLatitude(y.latitude);
            locationB.setLongitude(y.longitude);

            d += locationA.distanceTo(locationB);
        }
        //Toast.makeText(getContext(), "distance: " + d, Toast.LENGTH_SHORT).show();
//        distance = (stepCount*74) / 100000;
        d = d/1000;
        distanceText.setText(d + " km");
        stepText.setText(String.valueOf(stepCount));
    }

    public void getPace(){
        float pace = d/1000 / (SystemClock.elapsedRealtime() - chronometer.getBase());
        paceText.setText(pace + " k/h");
    }
}
