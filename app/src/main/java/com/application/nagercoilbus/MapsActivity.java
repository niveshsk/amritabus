package com.application.nagercoilbus;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.application.nagercoilbus.directionhelpers.FetchURL;
import com.application.nagercoilbus.directionhelpers.TaskLoadedCallback;
import com.ekalips.fancybuttonproj.FancyButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap,getmMap;
    Polyline currentPolyline;
    private MarkerOptions mycurrent,buscurrent,source,destination=null;
    LatLng my,route;
    Marker hello=null;
    Marker cu=null;
    String bus;
   String driveruid,name,url,department,drivername,phoneno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        getmMap=googleMap;
        Intent intent = getIntent();

        bus = intent.getStringExtra("bus").toLowerCase();
        driveruid = intent.getStringExtra("uid");
        name = intent.getStringExtra("name");
        url = intent.getStringExtra("url");
        department = intent.getStringExtra("department");
        LatLng latLng3 = new LatLng(8.2280743, 77.4154910);

        hello=mMap.addMarker(new MarkerOptions().position(latLng3).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker))).title("v").snippet("zx"));
mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
    @Override
    public void onPolylineClick(Polyline polyline) {
        polyline.getTag();

    }

});
        cu=mMap.addMarker(new MarkerOptions().position(latLng3).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker))).title("v").snippet("zx"));
        final FancyButton loc=findViewById(R.id.loca);
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loc.collapse();
                mycurtobuscur();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(my,20));
                loc.expand();
            }
        });

       final FancyButton dire=findViewById(R.id.direc);
        dire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dire.collapse();
                soutodes();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(route,15));
                dire.expand();
            }
        });

        final FancyButton coldire=findViewById(R.id.coldir);
        coldire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coldire.collapse();
                curtocol();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(route,15));
                coldire.expand();

            }
        });

        Log.d("hello","dfghfjg"+bus);

        String dbname="nglbus";
        DBHelper dbHelper=new DBHelper(MapsActivity.this,dbname);
        String busorigin=dbHelper.getOrigin(bus);
        LatLng latLng = new LatLng(8.2280743, 77.4154910);

        String[] location = busorigin.split(",");
        float latitude = Float.parseFloat(location[0]);
        float longe = Float.parseFloat(location[1]);
        LatLng latLng1 = new LatLng(latitude, longe);
        route=latLng1;
        MarkerOptions place1 = new MarkerOptions().position(latLng).title("college").snippet("AMRITA INSTITUTION");
        MarkerOptions place2 = new MarkerOptions().position(latLng1).title("Origin").snippet("<!DOCTYPE html><html> <body><p><font color="+"red"+">This is some text!</font></p> <p><font color="+"blue"+">This is some text!</font></p><p>The color attribute is not supported in HTML5. Use CSS instead.</p></body> </html>");
        source=place2;
        destination=place1;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1,15));
        mMap.addMarker(new MarkerOptions().position(latLng).title("college").snippet("AMRITA INSTITUTION"));
        mMap.addMarker(new MarkerOptions().position(latLng1).title("Origin").snippet("starting"));
        String directionurl = getUrl(place1.getPosition(), place2.getPosition());
        new FetchURL(MapsActivity.this).execute(directionurl, "driving");
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        final DatabaseReference driver1 = databaseReference.child("Driver");
        DatabaseReference driverne=driver1.child(driveruid).child("name");
        driverne.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                drivername=dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference driverph=driver1.child(driveruid).child("phno");
        driverph.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phoneno=dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference driuid1 = driver1.child(driveruid).child("location").child("latlang");
        driuid1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                LatLng latLng = new LatLng(dataSnapshot.child("latitude").getValue(float.class), dataSnapshot.child("longitude").getValue(float.class));
                String time = dataSnapshot.child("time").getValue(String.class);
                buscurrent= new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker))).title(bus).snippet("<!DOCTYPE html><html> <body><p><font color="+"red"+">This is some text!</font></p> <p><font color="+"blue"+">This is some text!</font></p><p>The color attribute is not supported in HTML5. Use CSS instead.</p></body> </html>");
                Toast.makeText(MapsActivity.this, "bus running now", Toast.LENGTH_SHORT).show();
                hello.remove();
                hello=mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker))).title(bus).snippet("Driver name:  "+drivername+"   Driver number :   "+phoneno));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }
                getmMap.setMyLocationEnabled(true);
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Toast.makeText(MapsActivity.this, "The location is  "+marker.getSnippet(), Toast.LENGTH_SHORT).show();
                    }
                });
                getmMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        Toast.makeText(MapsActivity.this, "you now here", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                });
                getmMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        LatLng myPoint=new LatLng(location.getLatitude(),location.getLongitude());
                        my=myPoint;
                        cu.remove();
                        mycurrent= new MarkerOptions().position(myPoint).title("").snippet(department);
                        cu=getmMap.addMarker(mycurrent);
                        googleMap.setInfoWindowAdapter(new CustomWindow1(MapsActivity.this,url,name,department));
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "no data found", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void curtocol() {
        String directionurl = getUrl(destination.getPosition(), mycurrent.getPosition());
        new FetchURL(MapsActivity.this).execute(directionurl, "driving");
    }

    private void soutodes() {
        String directionurl = getUrl(source.getPosition(), destination.getPosition());
        new FetchURL(MapsActivity.this).execute(directionurl, "driving");
    }

    private void mycurtobuscur() {
        String directionurl = getUrl(mycurrent.getPosition(), buscurrent.getPosition());
        new FetchURL(MapsActivity.this).execute(directionurl, "driving");
    }

    private String getUrl(LatLng origin, LatLng dest){
String str_org="origin="+origin.latitude+","+origin.longitude;
String str_des="destination="+dest.latitude+","+dest.longitude;
String mode="mode="+"driving";
String parameters=str_org+"&"+str_des+"&"+mode;
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+ "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        Log.d("helo",url);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        currentPolyline.setClickable(true);
        currentPolyline.setColor(Color.GREEN);
        currentPolyline.setTag("hello");

    }

    }

