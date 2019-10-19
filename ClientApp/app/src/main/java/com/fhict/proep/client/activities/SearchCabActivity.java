package com.fhict.proep.client.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fhict.proep.client.R;
import com.fhict.proep.client.middleware.GatewayClient;
import com.fhict.proep.client.models.Address;
import com.fhict.proep.client.models.Client;
import com.fhict.proep.client.models.Fare;
import com.fhict.proep.client.retrofit.APIService;
import com.fhict.proep.client.retrofit.APIUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class SearchCabActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private GoogleMap myMap;
    private ProgressBar myProgressBar;
    private TextView mySearchLabel;
    private TextView myAcceptLabel;
    private ImageView mySearchIcon;
    private Button acceptFareButton;
    private Button cancelFareButton;
    private View myMapLayout;
    private SupportMapFragment mapFragment;
    private TextView startAddress;
    private TextView destinationAddress;
    private TextView driverName;
    private TextView licensePlate;
    private TextView totalPrice;
    private Fare fare;
    private int counter;
    private APIService service = APIUtils.getAPIService();
    private CountDownTimer timer;
    private Client client;

    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private static final int DEFAULT_ZOOM = 15;
    private static final String TAG = MainActivity.class.getSimpleName();
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searching_cab_page);
        client = (Client) getIntent().getSerializableExtra("user");
        Address currentLocation = (Address) getIntent().getSerializableExtra("address");
        fare = (Fare) getIntent().getSerializableExtra("fare");
        if (client != null && currentLocation != null && fare != null) {
             timer = new CountDownTimer(10000, 1000){
                    public void onTick(long millisUntilFinished){
                        counter++;
                        System.out.println("second");
                        System.out.print(counter);
                        /*
                        On "Cancel" press, don't enter onFinish()
                        On "Proceed with fare" press, give "counter" max value (millisInFuture/1000)
                         */
                    }
                    public void onFinish(){
                        System.out.println("Sending....");
                        sendFare();
                    }
                }.start();
        }
        Channel channel = GatewayClient.getChannel();
        GatewayClient.receiveMessage(new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body)
                    throws IOException {
                long deliveryTag = envelope.getDeliveryTag();
                // (process the message components here ...)
                String jsonMessageBody = new String(body);
//                channel.basicAck(deliveryTag, false);
                Gson gson = new Gson();
                fare = gson.fromJson(jsonMessageBody,Fare.class);
                if (fare.isTrip_completed()) {
                    Intent intent = new Intent(SearchCabActivity.this,CompletedActivity.class);
                    intent.putExtra("user",client);
                    intent.putExtra("price", fare.getTotal_price());
                    startActivity(intent);
                }
                setData(fare);
                /*
                in labels here
                 */
            }
        });
        myProgressBar = findViewById(R.id.progressBar);
        mySearchIcon = findViewById(R.id.image_searching_cab);
        mySearchLabel = findViewById(R.id.searching_cab_label);
        myMapLayout = findViewById(R.id.linear_layout_map);
        myAcceptLabel = findViewById(R.id.textView_Message_Acceptance);
        startAddress = findViewById(R.id.textView_start_address);
        destinationAddress = findViewById(R.id.textView_destination_address);
        driverName = findViewById(R.id.textView_driver_name);
        licensePlate = findViewById(R.id.textView_licence_plate);
        totalPrice = findViewById(R.id.textView_total_price);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapID);
        acceptFareButton = findViewById(R.id.button_accept_fare);
        acceptFareButton.setOnClickListener(this);
        cancelFareButton = findViewById(R.id.button_cancel_fare);
        cancelFareButton.setOnClickListener(this);

        //de la alex
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

        // Hide contents from the searching taxi page and displaying the map
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_accept_fare){
            timer.cancel();
            sendFare();
        }else if(view.getId() == R.id.button_cancel_fare) {
            timer.cancel();
            this.finish();
        }
    }

    private void setData(Fare fare) {
        Log.d("TAGSETDATA", "Setting Data...");
        final String startAddressStr = fare.getStart_address().getStreet() + " "+ fare.getStart_address().getNumber();
        final String destinationAddressDes = fare.getDestination_address().getStreet() + " " + fare.getDestination_address().getNumber();
        new Handler(Looper.getMainLooper()).post(() -> {
            startAddress.setText("Pickup Address: " + startAddressStr);
            destinationAddress.setText("Destination Address: " + destinationAddressDes);
            driverName.setText("Driver Email: " + fare.getDriver_email());
            licensePlate.setText("License Plate: " + fare.getLicense_plate());
            totalPrice.setText("Total price: " + Double.toString(fare.getTotal_price()));
        });
    }

    // Map displaying with a given location
    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        myMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }
            @Override
            public View getInfoContents( Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.app_bar_main,
                        (FrameLayout) findViewById(R.id.mapID), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());
                return infoWindow;
            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = task.getResult();
                        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        myMap.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        myMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void updateLocationUI() {
        if (myMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                myMap.setMyLocationEnabled(true);
                myMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                myMap.setMyLocationEnabled(false);
                myMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void sendFare() {
        GatewayClient.sendMessage(new Gson().toJson(fare));
        myProgressBar.setVisibility(View.GONE);
        mySearchIcon.setVisibility(View.GONE);
        mySearchLabel.setVisibility(View.GONE);
        myAcceptLabel.setVisibility(View.GONE);
        acceptFareButton.setVisibility(View.GONE);
        cancelFareButton.setVisibility(View.GONE);
        startAddress.setVisibility(View.VISIBLE);
        destinationAddress.setVisibility(View.VISIBLE);
        driverName.setVisibility(View.VISIBLE);
        licensePlate.setVisibility(View.VISIBLE);
        totalPrice.setVisibility(View.VISIBLE);
        myMapLayout.setVisibility(View.VISIBLE);
        mapFragment.getMapAsync(this);
    }
}

