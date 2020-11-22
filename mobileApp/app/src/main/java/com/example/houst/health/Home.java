package com.example.houst.health;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.houst.health.Model.Hopital;
import com.example.houst.health.Model.Pharmacie;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback
        ,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {
    private  static  int UPDATE_INTERVAL=5000;
    private  static  int FASTEST_INTERVAL=3000;
    private  static  int DISPLACEMENT=10;

    private static final int MY_PERMISSION_REQUEST_CODE = 7000;
    private static final int PLAY_SERVICE_RES_REQUEST = 7001;

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    FirebaseDatabase db ;
    DatabaseReference hopitaux,pharmacies;

    Query hopitauxetatiques,hopitauxprives,pharmaciesdenuit,pharmaciesdejour;
    Marker mUserMarker;
    //TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("E-HEALTH");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent i =getIntent();
        String name=i.getStringExtra("name");
        //tv=findViewById(R.id.textViewuser);
        //tv.setText(name);

        db = FirebaseDatabase.getInstance();

        hopitaux=db.getReference("ehealth").child("hopitaux");
        pharmacies=db.getReference("ehealth").child("pharmacie");


        hopitauxetatiques= hopitaux.orderByChild("type").equalTo("Etatique");
        hopitauxprives=hopitaux.orderByChild("type").equalTo("Privé");

        pharmaciesdenuit=pharmacies.orderByChild("nuitjour").equalTo("Nuit");
        pharmaciesdejour=pharmacies.orderByChild("nuitjour").equalTo("Jour");



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.textViewuser);
        navUsername.setText(name);
        //MAps
        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpLocation();
        showAllMarkers();


    }
public void showAllMarkers(){

    hopitauxetatiques.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot s :dataSnapshot.getChildren()) {

                Hopital hopital = s.getValue(Hopital.class);
                Gson gson = new Gson();
                String markerToString0 = gson.toJson(hopital);
                LatLng location=new LatLng(Double.parseDouble(hopital.lat),Double.parseDouble(hopital.lng));
                mMap.addMarker(new MarkerOptions().position(location).title("hopital").snippet(
                        markerToString0).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                        .showInfoWindow();


            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });




    hopitauxprives.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot s :dataSnapshot.getChildren()) {
                Hopital hopital = s.getValue(Hopital.class);
                Gson gson = new Gson();
                String markerToString1 = gson.toJson(hopital);
                LatLng location=new LatLng(Double.parseDouble(hopital.lat),Double.parseDouble(hopital.lng));
                mMap.addMarker(new MarkerOptions().position(location).title("hopital").snippet(
                        markerToString1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)))
                        .showInfoWindow();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

    pharmaciesdenuit.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot s :dataSnapshot.getChildren()) {
                Pharmacie pharmacie = s.getValue(Pharmacie.class);
                Gson gson = new Gson();
                String markerToString2 = gson.toJson(pharmacie);

                LatLng location=new LatLng(Double.parseDouble(pharmacie.lat),Double.parseDouble(pharmacie.lng));
                mMap.addMarker(new MarkerOptions().position(location).title(
                        "pharmacie" ).snippet(
                        markerToString2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                        .showInfoWindow();

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

    pharmaciesdejour.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot s :dataSnapshot.getChildren()) {
//                        Pharmacie pharmacie =s.getValue(Pharmacie.class);
                Pharmacie pharmacie = s.getValue(Pharmacie.class);
                Gson gson = new Gson();
                String markerToString3 = gson.toJson(pharmacie);
                //Toast.makeText(Home.this,pharmacie.nom,Toast.LENGTH_LONG).show();
                LatLng location=new LatLng(Double.parseDouble(pharmacie.lat),Double.parseDouble(pharmacie.lng));
                mMap.addMarker(new MarkerOptions().position(location).title(
                        "pharmacie").snippet(
                        markerToString3).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
                        .showInfoWindow();
                //).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//                            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.hopetatique) {
            setTitle("Hopitaux etatiques");
            mMap.clear();
            setUpLocation();
            hopitauxetatiques.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot s :dataSnapshot.getChildren()) {

                        Hopital hopital = s.getValue(Hopital.class);
                        Gson gson = new Gson();
                        String markerToString0 = gson.toJson(hopital);
                        LatLng location=new LatLng(Double.parseDouble(hopital.lat),Double.parseDouble(hopital.lng));
                        mMap.addMarker(new MarkerOptions().position(location).title("hopital").snippet(
                                markerToString0).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                        .showInfoWindow();


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } else if (id == R.id.hopprive) {
            mMap.clear();
            setUpLocation();
            setTitle("Hopitaux Privés");

            hopitauxprives.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot s :dataSnapshot.getChildren()) {
                        Hopital hopital = s.getValue(Hopital.class);
                        Gson gson = new Gson();
                        String markerToString1 = gson.toJson(hopital);
                        LatLng location=new LatLng(Double.parseDouble(hopital.lat),Double.parseDouble(hopital.lng));
                        mMap.addMarker(new MarkerOptions().position(location).title("hopital").snippet(
                                markerToString1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)))
                                .showInfoWindow();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else if (id == R.id.pharmanuit) {
            setTitle("Pharmacies de nuit");
            mMap.clear();
            setUpLocation();
            pharmaciesdenuit.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot s :dataSnapshot.getChildren()) {
                        Pharmacie pharmacie = s.getValue(Pharmacie.class);
                        Gson gson = new Gson();
                        String markerToString2 = gson.toJson(pharmacie);

                        LatLng location=new LatLng(Double.parseDouble(pharmacie.lat),Double.parseDouble(pharmacie.lng));
                        mMap.addMarker(new MarkerOptions().position(location).title(
                               "pharmacie" ).snippet(
                                markerToString2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                                .showInfoWindow();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else if (id == R.id.pharmajour) {
            mMap.clear();
            setUpLocation();
            setTitle("Pharmacies de jour");
            pharmaciesdejour.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot s :dataSnapshot.getChildren()) {
//                        Pharmacie pharmacie =s.getValue(Pharmacie.class);
                        Pharmacie pharmacie = s.getValue(Pharmacie.class);
                        Gson gson = new Gson();
                        String markerToString3 = gson.toJson(pharmacie);
                        //Toast.makeText(Home.this,pharmacie.nom,Toast.LENGTH_LONG).show();
                        LatLng location=new LatLng(Double.parseDouble(pharmacie.lat),Double.parseDouble(pharmacie.lng));
                        mMap.addMarker(new MarkerOptions().position(location).title(
                                "pharmacie").snippet(
                                markerToString3).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
                                .showInfoWindow();
                        //).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//                            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (id == R.id.help) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View info = inflater.inflate(R.layout.info_alert, null);
            final AlertDialog dialog = new AlertDialog.Builder(this).setView(info)
                    .setTitle("Signification")
                    .setPositiveButton("ok", null)
                    .create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialogInterface) {


                    Button btnpositive = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                    btnpositive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                }
            });
            dialog.show();

        } else if (id == R.id.quitter) {
            Intent i1 = new Intent(Home.this,MainActivity.class);
            startActivity(i1);
            finish();

        }else if (id == R.id.tous) {
            setTitle("Tous");
            mMap.clear();
            setUpLocation();
          showAllMarkers();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        //googleMap.setOnMarkerClickListener(this);
        //mMap.setInfoWindowAdapter(new CustomInfoWindo(this));
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));


    }

    private void setUpLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Request runtime permession
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        } else {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();

            }
        }

    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

            final double latitude = mLastLocation.getLatitude();
            final double longitude = mLastLocation.getLongitude();
            //Add Marker
            if (mUserMarker != null)
                mUserMarker.remove();//Remove Already Marker
            mUserMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(String.format("Votre")).snippet("position gps").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));


            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
            //loadSocieteMarkers();
            Log.d("ERROR", String.format("your location was changed: %f /%f", latitude, longitude));
        } else {
            Log.d("ERROR", "Cannot get your location");
        }
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_RES_REQUEST).show();
            else {
                Toast.makeText(this, "This device is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    }

}
