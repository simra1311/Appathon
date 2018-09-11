package com.example.android.mykaarma.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.mykaarma.ClassObjects.Dealer;
import com.example.android.mykaarma.ClassObjects.NetworkConnection;
import com.example.android.mykaarma.R;
import com.example.android.mykaarma.adapters.ListAdapter;
import com.example.android.mykaarma.fetchdata.fetch;
import com.example.android.mykaarma.fetchdata.responsedata;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This activity is for searching and displaying user address, finding nearby dealers', setting up constraints and filters
 * @author Simra Afreen
 * @version 1.0.0
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        com.google.android.gms.location.LocationListener,
        AdapterView.OnItemSelectedListener, ListAdapter.Listener,
        AppCompatCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private static GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private AppCompatDelegate delegate;
    private String make="", distance="30000", latitude="28.570606", longitude="77.017375",price="500000";
    String searchResult="", distanceQuery="30000",priceQuery="10000000";
    private Button searchButton;
    private Spinner spinner,spinner1;
    EditText searchQuery;
    private ArrayList<Dealer> dealerArrayList;
    private ListAdapter listAdapter;
    private ListView listView;
    ImageButton button;
//    RecyclerView recyclerView;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 5;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        searchQuery = findViewById(R.id.make);
        spinner = findViewById(R.id.spinner);

//        Toolbar toolbar = findViewById(R.id.toolbaar);
//        delegate.setSupportActionBar(toolbar);
//        mCameraPosition = CameraPosition.createFromAttributes(this,)
//        mCameraPosition = new CameraPosition.Builder().build();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.distances_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner1 = findViewById(R.id.price);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.prices_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        button =findViewById(R.id.mails);
//        FetchMake();
        searchButton = findViewById(R.id.search_button1);
        listView = findViewById(R.id.list);
        dealerArrayList = new ArrayList<>();
//        dealerArrayList.add(new Dealer("123",new LatLng(240.45,64.054)));
//        dealerArrayList.add(new Dealer("123",new LatLng(240.45,64.054)));
//        listAdapter = new ListAdapter(dealerArrayList,this,this);
//        listView.setAdapter(listAdapter);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setButtons();
        FetchData(false);
        CallAPI();
//        Log.d("size",dealerArrayList.size()+"");
    }

//    private void FetchMake() {
//        final String url = getResources().getString(R.string.domain) + "make.php";
//        //= ArrayAdapter.createFromResource(this, R.array.distances_array, android.R.layout.simple_spinner_item);
//        List<String> list = Arrays.asList(getResources().getStringArray(R.array.make));
//        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        searchQuery.setAdapter(spinnerAdapter);
////        spinnerAdapter.add("Select");
//        spinnerAdapter.notifyDataSetChanged();
//
//        Map<String, String> params = new HashMap<>();
//        new fetch(url, params, getApplicationContext()).startfetch(new responsedata() {
//            @Override
//            public void response(JSONObject data) {
//                Log.d("Responsedata1911", data.toString());
////                parsejson(data);
//                Log.d("responsedata",url);
//
//                try {
//                    if(data.getInt("conn_status")==1 && data.getInt("data")==1){
//                        for (int i=0;i<data.length();++i){
//                            try {
//                                JSONObject jsonObject = data.getJSONArray("make_list").getJSONObject(i);
//                                String make = jsonObject.getString("Make");
//                                spinnerAdapter.add(make);
//                                spinnerAdapter.notifyDataSetChanged();
//                            }
//                            catch (JSONException e){
//
//                            }
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
////                Toast.makeText(MapsActivity.this,url,Toast.LENGTH_SHORT).show();
//            }
//        });
//        if (!NetworkConnection.isConnected(MapsActivity.this)){
//            Toast.makeText(MapsActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
//        }
//    }

    /**
     * This function fetches the dealers information filtered by the given parameters
     * @param flag
     */
    private void FetchData(Boolean flag) {
        final String url = getResources().getString(R.string.domain) + "search.php";
         distance = distanceQuery;

        if (!flag && distanceQuery.compareTo("Select")==0) distance = 30000+"";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!Objects.equals(priceQuery, "")) price = priceQuery;
        }

        if (priceQuery.compareTo("Select")==0) price = "10000000";
        if (!Objects.equals(searchResult, "")) make = searchResult;

        Map<String, String> params = new HashMap<>();
        params.put("make", make);
        params.put("distance", distance);
        params.put("latitude",latitude);
        params.put("longitude",longitude);
        params.put("price",price);

        Log.d("dist", params+"");
        new fetch(url, params, getApplicationContext()).startfetch(new responsedata() {
            @Override
            public void response(JSONObject data) {
                Log.d("Responsedata1911", data.toString());
//                parsejson(data);
                Log.d("responsedata",url);

                try {
                    if(data.getInt("conn_status")==1 && data.getInt("data")==1){
                        ParseFunction(data.getJSONArray("dealers"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        if (!NetworkConnection.isConnected(MapsActivity.this)){
            Toast.makeText(MapsActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This function accepts a JSONArray and adds the dealers' information to the dealerArrayList
     * @param jsonArray
     */
    private void ParseFunction(JSONArray jsonArray) {
        dealerArrayList.clear();        //clear the current list

        for (int i=0;i<jsonArray.length();i++){
            try {
                double lat,lon;
                final JSONObject data = jsonArray.getJSONObject(i);
                final String ID = data.getString("DealerName");
                final String email = data.getString("DealerEmail");
                final String dealerID = data.getString("DealerID");
                final String PriceInINR = data.getString("PriceInINR");
                final String DealerAvgRatingOutOf5 = data.getString("DealerAvgRatingOutOf5");

                lon = Double.parseDouble(data.getString("DealerLongitude"));
                lat = Double.parseDouble(data.getString("DealerLatitude"));

                final Dealer dealer = new Dealer(ID,email,dealerID,PriceInINR,DealerAvgRatingOutOf5,new LatLng(lat,lon));
                dealer.ID = ID;

                dealerArrayList.add(dealer);
                listAdapter = new ListAdapter(dealerArrayList,this,this);
                listView.setAdapter(listAdapter);
                //get the dealer address from LatLng
                String add = dealer.getAddress(MapsActivity.this,lat,lon);
                if (add.substring(0,4).equals("null")){
                    add = "Not found" + add.substring(4);
                    Log.d("address",add);
                }
                dealer.address = add;

                mMap.addMarker(new MarkerOptions().title(ID+","+email+","+dealerID+","+DealerAvgRatingOutOf5+","+PriceInINR).position(new LatLng(lat,lon)));
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if (marker.getTitle().compareTo("current location")!=0) {
                            String idPlusEmail = marker.getTitle();
                            String res[] = idPlusEmail.split(",");
                            String id = res[0];
                            String Email = res[1];
                            String dealeruserID = res[2];
                            String rating = res[3];
                            String price = res[4];

                            LatLng latLng = marker.getPosition();
                            Dealer dealer = new Dealer(id,Email,dealeruserID,price,rating, latLng);
                            dealer.address = dealer.getAddress(MapsActivity.this, latLng.latitude, latLng.longitude);

                            Intent intent = new Intent(MapsActivity.this, DealersInfo.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Dealer", dealer);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            return true;
                        }
                        return false;
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("sizedealer",dealerArrayList.size()+"");
        listView.setAdapter(listAdapter);
        if (dealerArrayList.size()==0){
            Toast.makeText(MapsActivity.this,"No results found!",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Register new user for sign up
     */
    private void CallAPI() {

        String url = getApplicationContext().getString(R.string.domain) + "signup.php";
//        Log.d("responsedata", url);

        Map<String, String> params = new HashMap<>();
        params.put("fname", "Vinodk");
        params.put("lname", "Kumark");
        params.put("email", "abcd@gmail.com");
        params.put("phone", "8300840829");
        params.put("pass", "123456");

        new fetch(url, params, getApplicationContext()).startfetch(new responsedata() {
            @Override
            public void response(JSONObject data) {
//                Log.d("Responsedata1911", data.toString());
            }
        });

    }

    /**
     * set OnClickListeners for all the spinners and search button as well as list
     */
    private void setButtons(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                searchResult = searchQuery.getEditableText().toString();

                mMap.addMarker(new MarkerOptions().title("current position").position(new LatLng(Double.valueOf(latitude),Double.valueOf(longitude))).draggable(true));
                FetchData(false);
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                distanceQuery = item.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                priceQuery = item.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dealer dealer = dealerArrayList.get(i);
                Toast.makeText(MapsActivity.this,dealer.ID+""+dealer.address,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MapsActivity.this,DealersInfo.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Dealer",dealer);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MyMails.class));
            }
        });
    }

    /**
     * Saves the state of the map when the activity is paused.
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Creating the menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Setting menu options
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_get_place) {
            Log.d(TAG, "reset pressed");
//            Toast.makeText(MapsActivity.this, "reset", Toast.LENGTH_SHORT).show();
            mMap.clear();
            FetchData(false);
            getDeviceLocation();
            updateLocationUI();
            return true;
        }
        return false;
    }

    /**
     * This callback is triggered when the map is ready to be used
     * Handles all map click listeners , drag events
     *
     * @param map
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        // Prompt the user for permission.
        getLocationPermission();
        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
        updateLocationUI();

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                Log.d("System out", "onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                Log.d("System out", "onMarkerDragEnd..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                latitude = arg0.getPosition().latitude+"";
                longitude = arg0.getPosition().longitude+"";
                Log.d("address",latitude+" "+longitude);
                mMap.clear();
                FetchData(false);
                mMap.addMarker(new MarkerOptions().position(arg0.getPosition()).title("current location").draggable(true));
//                getDeviceLocation();
                updateLocationUI();
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                float res[]=new float[1];
                int lat = (int)Math.round(Double.parseDouble(latitude));
                int lon = (int)Math.round(Double.parseDouble(longitude));
                Location.distanceBetween(latLng.latitude,latLng.longitude,lat,lon,res);
                Log.d("dista",res[0]/1000+"");
                mMap.clear();
                distanceQuery = Math.round(res[0])/1000+"";
                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(latitude),Double.valueOf(longitude))).draggable(true).title("current location"));
                mMap.addCircle(new CircleOptions().center(new LatLng(Double.valueOf(latitude),Double.valueOf(longitude))).radius(res[0]).strokeColor(Color.BLUE));
                FetchData(true);
            }
        });
    }

    /**
     * To get the current location of the device
     */
    private void getDeviceLocation() {
//      Get the best and most recent location of the device, which may be null in rare cases when a location is not available.
        Log.d(TAG, "inside getloc");
        LatLng ans = new LatLng(0, 0);
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            Log.d("latitude", mLastKnownLocation.getLatitude() + " ");
                            LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(latLng).title("current location").draggable(true));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
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
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
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

    /**
     * Handles the result of the request for location permissions
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    /**
     * Get the last known location
     */
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
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mMap.clear();

        MarkerOptions mp1 = new MarkerOptions();
        mp1.position(new LatLng(location.getLatitude(),
                location.getLongitude()));

        mp1.draggable(true);
        mp1.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        mMap.addMarker(mp1);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location
                        .getLongitude()), 20));
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(int position) {
        Dealer dealer = dealerArrayList.get(position);
        Intent intent = new Intent(MapsActivity.this,DealersInfo.class);
        intent.putExtra("id",dealer.ID);
        startActivity(intent);
    }
    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
    }
    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
    }
    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }


}