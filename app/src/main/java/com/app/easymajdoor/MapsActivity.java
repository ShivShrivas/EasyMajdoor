package com.app.easymajdoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.easymajdoor.SendNotificationPackage.APIService;
import com.app.easymajdoor.SendNotificationPackage.Client;
import com.app.easymajdoor.SendNotificationPackage.Data;
import com.app.easymajdoor.SendNotificationPackage.MyResponse;
import com.app.easymajdoor.SendNotificationPackage.NotificationSender;
import com.app.easymajdoor.model.NewRequestForMazdoor;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity  implements LocationListener, OnMapReadyCallback {

    GoogleMap map;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReferenceMazdoor;
    APIService apiService;
    SupportMapFragment mapFragment;
    SearchView searchLocation;
    FusedLocationProviderClient client;
    private GoogleApiClient mGoogleApiClient;
    ImageView currentLocationBtn;
    LocationManager locationManager;
    LinearLayout plumber,electrician,painter,labour,barber,sweaper;
    Double MazdoorLatitude,MazdoorLongitude;
    String mazdoorType="";
    NewRequestForMazdoor newRequestForMazdoor;
    ArrayList arrayListOfMazdoor;
    Button getMazdoor;
    NavigationView navigationView;
    String refreshToken="";
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar mainToolbar;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_maps);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        client = LocationServices.getFusedLocationProviderClient(this);
        searchLocation = findViewById(R.id.searchLocation);
        navigationView = findViewById(R.id.nav_view);
        mainToolbar = findViewById(R.id.mainToolbar);
        drawerLayout = findViewById(R.id.mainPageDrawer);
        plumber=findViewById(R.id.layout_plumber);
        electrician=findViewById(R.id.layout_Electrician);
        painter=findViewById(R.id.layout_painter);
        labour=findViewById(R.id.layout_labour);
        barber=findViewById(R.id.layout_barber);
        sweaper=findViewById(R.id.layout_sweaper);
        getMazdoor=findViewById(R.id.buttongetMazdoor);
        currentLocationBtn = findViewById(R.id.currentLocationBtn);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("UsersLocation");
        databaseReferenceMazdoor=firebaseDatabase.getReference();
        newRequestForMazdoor=new NewRequestForMazdoor();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        mapFragment.getMapAsync(this);
        setSupportActionBar(mainToolbar);
        actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawerLayout,mainToolbar,R.string.nav_open,R.string.nav_close );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        UpdateToken();
        FirebaseMessaging.getInstance().subscribeToTopic("all");
            plumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allReset();
                    plumber.setBackgroundResource(R.drawable.onclick_ractangle);
                    getMazdoor.setVisibility(View.VISIBLE);
                    getMazdoor.setText("Get Plumber");
                    mazdoorType="Plumber";


                    databaseReferenceMazdoor.child("MazdoorLocation").child("plumber").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            arrayListOfMazdoor = new ArrayList();
                            Iterator<DataSnapshot> items = snapshot.getChildren().iterator();
                            int counter = 0;
                            while (items.hasNext()) {
                                DataSnapshot item = items.next();
                                LatLng latLng= new LatLng(Double.parseDouble(item.child("latitude").getValue().toString()),Double.parseDouble(item.child("longitude").getValue().toString()));
                                map.addMarker(new MarkerOptions().position(latLng).title("plumber"));
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            }

//
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
            });

            electrician.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allReset();
                    electrician.setBackgroundResource(R.drawable.onclick_ractangle);
                    getMazdoor.setVisibility(View.VISIBLE);
                    getMazdoor.setText("Get Electrician");
                    mazdoorType="Electrician";

                    databaseReferenceMazdoor.child("electrician").child("e1").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                             MazdoorLatitude=snapshot.child("latitude").toString();
//                            MazdoorLongitude=snapshot.child("longitude").toString();
                            map.clear();
                         getCurrntLocation();

                            LatLng latLng= new LatLng(Double.parseDouble(snapshot.child("latitude").getValue().toString()),Double.parseDouble(snapshot.child("longitude").getValue().toString()));
                            map.addMarker(new MarkerOptions().position(latLng).title("Electrician"));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//                            getCurrntLocation();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

            painter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allReset();
                    painter.setBackgroundResource(R.drawable.onclick_ractangle);
                    getMazdoor.setVisibility(View.VISIBLE);
                    getMazdoor.setText("Get Painter");
                }
            });

            labour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allReset();
                    labour.setBackgroundResource(R.drawable.onclick_ractangle);
                    getMazdoor.setVisibility(View.VISIBLE);
                    getMazdoor.setText("Get Labour");
                }
            });

            barber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allReset();
                    barber.setBackgroundResource(R.drawable.onclick_ractangle);
                    getMazdoor.setVisibility(View.VISIBLE);
                    getMazdoor.setText("Get Barber");
                }
            });

            sweaper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allReset();
                    sweaper.setBackgroundResource(R.drawable.onclick_ractangle);
                    getMazdoor.setVisibility(View.VISIBLE);
                    getMazdoor.setText("Get Sweaper");
                }
            });
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrntLocation();

        }else{
            ActivityCompat.requestPermissions(MapsActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
        currentLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrntLocation();
            }
        });
        getMazdoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] usertoken = {new String()};

                FirebaseDatabase.getInstance().getReference().child("Tokens").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                       usertoken[0] =dataSnapshot.getValue(String.class);
                        ArrayList<String> keyList = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String userId=child.getKey();
                    FirebaseDatabase.getInstance().getReference().child("MazdoorLocation").child("plumber").child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Toast.makeText(getApplicationContext(),snapshot.child("latitude").getValue()+"//yewala/"+snapshot.child("longitude").getValue(), Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("UsersLocation").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                        Toast.makeText(getApplicationContext(),snapshot1.child("latitude").getValue()+"///"+snapshot1.child("longitude").getValue(), Toast.LENGTH_SHORT).show();
                                        if(snapshot.child("latitude").getValue()!=null && snapshot.child("longitude").getValue()!=null && snapshot1.child("latitude").getValue()!=null && snapshot1.child("longitude").getValue()!=null){
                                            Location startPoint=new Location("locationA");
                                            startPoint.setLatitude(Double.parseDouble(snapshot.child("latitude").getValue().toString()));
                                            startPoint.setLongitude(Double.parseDouble(snapshot.child("longitude").getValue().toString()));

                                            Location endPoint=new Location("locatioB");
                                            endPoint.setLatitude(Double.parseDouble(snapshot1.child("latitude").getValue().toString()));
                                            endPoint.setLongitude(Double.parseDouble(snapshot1.child("longitude").getValue().toString()));

                                            double distance=startPoint.distanceTo(endPoint);
                                            if( distance<5000){
                                                keyList.add( child.getValue().toString());
                                                FcmNotificationsSender fcmNotificationsSender=new FcmNotificationsSender(child.getValue().toString(),"Hello Plumber sahab",FirebaseAuth.getInstance().getCurrentUser().getUid(),getApplicationContext(),MapsActivity.this);
                                                fcmNotificationsSender.SendNotifications();
                                                HashMap hashMap= new HashMap();
                                                hashMap.put("latitude",snapshot1.child("latitude").getValue().toString());
                                                hashMap.put("longitude",snapshot1.child("longitude").getValue().toString());
                                                hashMap.put("userID",FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                                hashMap.put("status","nun");
                                                FirebaseDatabase.getInstance().getReference("plumber").child(child.getKey()).child("NewRequest").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMap);
                                            }
                                            Toast.makeText(getApplicationContext(), "Distance"+distance, Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(getApplicationContext(), "somtignh wen v", Toast.LENGTH_SHORT).show();

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                sendNotifications(usertoken[0],"achabhai","hao koi na");

            }



        });
        searchLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String getLocation = searchLocation.getQuery().toString();
                List<Address> addressList = null;

                if (getLocation != null || !getLocation.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(getLocation, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(getLocation));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    public void sendNotifications(String usertoken, String title, String message) {
        Toast.makeText(MapsActivity.this, ""+title+"==="+message, Toast.LENGTH_SHORT).show();
        Data data = new Data(title, message);
    Toast.makeText(MapsActivity.this, ""+data, Toast.LENGTH_SHORT).show();

//        NotificationSender sender = ;
//        Toast.makeText(MapsActivity.this, ""+sender.getTo(), Toast.LENGTH_SHORT).show();

//        Toast.makeText(MapsActivity.this, ""+sender, Toast.LENGTH_LONG);

        apiService.sendNotifcation(new NotificationSender(data, usertoken)).enqueue(new Callback<MyResponse>() {

            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                Toast.makeText(MapsActivity.this, "response done ", Toast.LENGTH_LONG);

                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(MapsActivity.this, "Failed ", Toast.LENGTH_LONG);
                    }else
                        Toast.makeText(MapsActivity.this, "seuccess ", Toast.LENGTH_LONG);

                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(MapsActivity.this, "faill h be "+t, Toast.LENGTH_LONG);

            }
        });
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, (LocationListener) this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }
    private void drawCircle(LatLng point){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(3000);

        // Border color of the circle
        circleOptions.strokeColor(R.color.colorPrimary);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        map.addCircle(circleOptions);

    }
    void UpdateToken(){

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token ->
        {
            if (!TextUtils.isEmpty(token)) {
                Log.d("TAG", "UpdateToken: "+token);
                FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
            } else{
                Log.w("TAG", "token should not be null...");
            }
        });

    }
    private void getCurrntLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            map.clear();
                            LatLng latLng= new LatLng(location.getLatitude(),location.getLongitude());
                            MarkerOptions markerOptions=new MarkerOptions().position(latLng)
                                    .title("your are here!!!")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
                            map.addMarker(markerOptions);
                            drawCircle(latLng);
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(latLng);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    });
                }
            }
        });
    }

    public void allReset(){
        plumber.setBackgroundResource(R.drawable.simple_ractangle);
        electrician.setBackgroundResource(R.drawable.simple_ractangle);
        painter.setBackgroundResource(R.drawable.simple_ractangle);
        labour.setBackgroundResource(R.drawable.simple_ractangle);
        barber.setBackgroundResource(R.drawable.simple_ractangle);
        sweaper.setBackgroundResource(R.drawable.simple_ractangle);

    }

    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrntLocation();
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map=googleMap;
    }
}
