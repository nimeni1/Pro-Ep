package com.fhict.proep.client.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fhict.proep.client.R;
import com.fhict.proep.client.middleware.GatewayClient;
import com.fhict.proep.client.models.Address;
import com.fhict.proep.client.models.Client;
import com.fhict.proep.client.models.Fare;
import com.fhict.proep.client.retrofit.APIService;
import com.fhict.proep.client.retrofit.APIUtils;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private APIService service = APIUtils.getAPIService();
    private GatewayClient gatewayClient = new GatewayClient();
    public int counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Client client = (Client) getIntent().getSerializableExtra("user");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Geocoder geocoder = new Geocoder(getApplicationContext());
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        List<android.location.Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String street = addresses.get(0).getThoroughfare();
        String houseNR = addresses.get(0).getSubThoroughfare();
        String postalCode = addresses.get(0).getPostalCode();
        String city = addresses.get(0).getLocality();
        final Address startAddress = new Address(street, houseNR, city, postalCode, latitude, longitude);
        TextView textView = findViewById(R.id.pickup_field);
        textView.setText(street + " " + houseNR + ", " + city);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        TextView nameTextView = headerLayout.findViewById(R.id.nameView);
        String text = "Welcome, " + client.getName().getFirstName();
        nameTextView.setText(text);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.button_searchCab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SearchCabActivity.class);
            intent.putExtra("address", startAddress);
            intent.putExtra("user", client);
            try {
                String destination = readDestinationLocation();
                List<android.location.Address> destinations = geocoder.getFromLocationName(destination, 1);
                if (destinations != null) {
                    String street1 = destinations.get(0).getThoroughfare();
                    String houseNR1 = destinations.get(0).getSubThoroughfare();
                    String postalCode1 = destinations.get(0).getPostalCode();
                    String city1 = destinations.get(0).getLocality();
                    Address finalDestination = new Address(street1, houseNR1, city1, postalCode1, destinations.get(0).getLatitude(), destinations.get(0).getLongitude());
                    Fare model = new Fare(client.getEmail(), startAddress, finalDestination);
//                    GatewayClient.sendMessage(new Gson().toJson(model));
                    intent.putExtra("fare",model);
                    startActivity(intent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public String readDestinationLocation(){
        String destinationField = ((EditText)findViewById(R.id.destination_field)).getText().toString();
        return destinationField;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            // Handle the camera action
        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToSearchCabActivity() {
        startActivity(new Intent(this,SearchCabActivity.class));
    }





}

//    FloatingActionButton fab = findViewById(R.id.button_searchCab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                CountDownTimer timer = new CountDownTimer(2000, 1000){
//                    public void onTick(long millisUntilFinished){
//                        counter++;
//                        /*
//                        On "Cancel" press, don't enter onFinish()
//                        On "Proceed with fare" press, give "counter" max value (millisInFuture/1000)
//                         */
//                    }
//                    public void onFinish(){
//                        System.out.println("counter = " + counter);
//                        Intent intent = new Intent(getApplicationContext(), SearchCabActivity.class);
//                        intent.putExtra("address",startAddress);
//                        intent.putExtra("user", client);
//                        Toast.makeText(getApplicationContext(),"5 seconds past",Toast.LENGTH_SHORT).show();
//                        try {
//                            String destination = readDestinationLocation();
//                            List<android.location.Address> destinations = geocoder.getFromLocationName(destination,1);
//                            if (destinations != null) {
//                                String street = destinations.get(0).getThoroughfare();
//                                String houseNR = destinations.get(0).getSubThoroughfare();
//                                String postalCode = destinations.get(0).getPostalCode();
//                                String city = destinations.get(0).getLocality();
//                                Address finalDestination = new Address(street,houseNR,city,postalCode,destinations.get(0).getLatitude(),destinations.get(0).getLatitude());
//                                Gson gson = new Gson();
//                                Fare model = new Fare(client.getEmail(),startAddress,finalDestination);
//                                String json = gson.toJson(model);
//                                Fare fare = gson.fromJson(json, Fare.class);
//                                gatewayClient.sendMessage(json);
//                                service.updateFare(json).enqueue(new Callback<ResponseBody>() {
//                                    @Override
//                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                        Toast.makeText(getApplicationContext(),"Sent",Toast.LENGTH_LONG).show();
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                        Toast.makeText(getApplicationContext(),"Not Sent",Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                                //startActivity(intent);
//                            } else {
//                                Toast.makeText(getApplicationContext(),"Error loading location",Toast.LENGTH_LONG).show();
//                            }
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.start();
//                goToSearchCabActivity();
//            }
//        });
