package proep.fhict.work.driver.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import proep.fhict.work.driver.APIService;
import proep.fhict.work.driver.APIUtils;
import proep.fhict.work.driver.R;
import proep.fhict.work.driver.middleware.GatewayClient;
import proep.fhict.work.driver.model.Address;
import proep.fhict.work.driver.model.Car;
import proep.fhict.work.driver.model.Driver;
import proep.fhict.work.driver.model.Fare;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int DEFAULT_ZOOM = 15;
    private static final String TAG = MainActivity.class.getSimpleName();
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location mLastKnownLocation;
    private GatewayClient gatewayClient = new GatewayClient();
//    private Driver driver;
    private APIService service = APIUtils.getAPIService();
    private Fare fare;
    private Driver driver;



    @Override
    protected void onCreate( Bundle savedInstanceState) {
       // mapIntent.setPackage("com.google.android.apps.maps")
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Address destinationAddress= (Address) getIntent().getSerializableExtra("destinationaddress:");
        String city = destinationAddress.getCity();
        String street = destinationAddress.getStreet();
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + city +" "+ "Visserstraat" + " 6");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fare = (Fare) getIntent().getSerializableExtra("Fare");
        driver = (Driver) getIntent().getSerializableExtra("driver");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        FloatingActionButton btnStart = findViewById(R.id.button_startFare);
        FloatingActionButton btnEnd = findViewById(R.id.button_endFare);
        btnStart.show();
        btnEnd.show();
         btnStart.setOnClickListener(this);
         btnEnd.setOnClickListener(this);

//        driver = (Driver) getIntent().getSerializableExtra("driver");

        String text = "Welcome, " + driver.getName().getFirst_name();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        TextView textView = headerLayout.findViewById(R.id.name);
        textView.setText(text);


    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just move the camera to current location and add the route to the first fare.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady( GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow( Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents( Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.app_bar_main,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
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

        Address startaddress= (Address) getIntent().getSerializableExtra("startaddress:");
        Double startlat = startaddress.getLatitude();
        Double startlong = startaddress.getLongitude();

        Address destinationAddress= (Address) getIntent().getSerializableExtra("destinationaddress:");
        Double destlat = destinationAddress.getLatitude();
        Double destlong = destinationAddress.getLongitude();

        final LatLng fareEndPoint = new LatLng(startlat,startlong);
        final LatLng fareStartPoint = new LatLng(startlat,startlong);

        Location loc1 = new Location("start");
        loc1.setLatitude(fareEndPoint.latitude);
        loc1.setLongitude(fareEndPoint.longitude);

        Location loc2 = new Location("end");
        loc2.setLatitude(fareStartPoint.latitude);
        loc2.setLongitude(fareStartPoint.longitude);

        //final float distanceInMeters = loc1.distanceTo(loc2);
        final float distanceInMeters = 1500; //hardcoded for now

        service.askForCarAndPrice(driver.getEmail()).enqueue(new Callback<Car>() {
            @Override
            public void onResponse( @NonNull Call<Car> call, @NonNull Response<Car> response ) {
                Car car = response.body();
                fare.setLicense_plate(car.getLicense_plate());
                fare.setDriver_email(driver.getEmail());

                fare.setTotal_price(car.getPrice_km()*((distanceInMeters)/1000));
                GatewayClient.sendMessage(new Gson().toJson(fare));
            }

            @Override
            public void onFailure( @NonNull Call<Car> call, Throwable t ) {

            }
        });


        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            LatLng currentLocation =  new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude());
                            String url = getDirectionsUrl(currentLocation, fareStartPoint);
                            // Start downloading json data from Google Directions API
                            DownloadTask downloadTask = new DownloadTask();
                            // Getting URL to the Google Directions API
                            downloadTask.execute(url);
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }

                }

    public void onClick(View v) {

        if (v.getId() == R.id.button_startFare) {}
        switch (v.getId()){
            case R.id.button_startFare:
                //action
                Address startaddress= (Address) getIntent().getSerializableExtra("startaddress:");
                Double startlat = startaddress.getLatitude();
                Double startlong = startaddress.getLongitude();

                Address destinationAddress= (Address) getIntent().getSerializableExtra("destinationaddress:");
                Double destlat = destinationAddress.getLatitude();
                Double destlong = destinationAddress.getLongitude();

                final LatLng fareEndPoint = new LatLng(startlat,startlong);
                final LatLng fareStartPoint = new LatLng(destlat,destlong);

                String url = getDirectionsUrl(fareStartPoint, fareEndPoint);
                // Start downloading json data from Google Directions API
                DownloadTask downloadTask = new DownloadTask();
                // Getting URL to the Google Directions API
                downloadTask.execute(url);

               // Address destinationAddress= (fare.getDestination_address());
                String city = destinationAddress.getCity();
                String street = destinationAddress.getStreet();
                String number = destinationAddress.getNumber().toString();
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + city +" "+ street + " " + number);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;

            case R.id.button_endFare:
                //action switch to main activity, update fare with fare completed bool to true
//                driver = (Driver) getIntent().getSerializableExtra("driver");
                final Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra("driver",driver);
                //Fare fare = (Fare) getIntent().getSerializableExtra("Fare");
                fare.setTrip_completed(true);
                GatewayClient.sendMessage(new Gson().toJson(fare));
                service.createFare(fare).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse( Call<ResponseBody> call, Response<ResponseBody> response ) {
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure( Call<ResponseBody> call, Throwable t ) {
                        Toast.makeText(getApplicationContext(),"Error FATAL",Toast.LENGTH_LONG).show();
                    }
                });
                break;
    }}

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = new PolylineOptions();
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    private String getDirectionsUrl( LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
        String MY_API_KEY = "AIzaSyBkmgvMGwIoLHHSc8ZbOtmeGEOUov_NPls" ;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters +"&key=" + MY_API_KEY;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
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
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }





    /*public method GetDrivingDistance(double lat1, double lat2, double long1, double long2)
    {
        url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=".lat1.",".long1."&destinations=".lat2.",".long2."&mode=driving";
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_PROXYPORT, 3128);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
        $response = curl_exec($ch);
        curl_close($ch);
        $response_a = json_decode($response, true);
        string dist = $response_a['rows'][0]['elements'][0]['distance']['text'];
        string time = $response_a['rows'][0]['elements'][0]['duration']['text'];

        return array('distance' => $dist, 'time' => $time);
    }*/
}
