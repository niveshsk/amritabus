package com.application.nagercoilbus;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main2Activity extends MainActivity{
    String uid;
    TextView protext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout display=findViewById(R.id.activitydisplay);
        View view=getLayoutInflater().inflate(R.layout.activity_main2,null);
        display.addView(view);
        Bundle extras = getIntent().getExtras();
        TextView tv = findViewById(R.id.textView3);

        final String bus=extras.getString("bus").toLowerCase();
        final String name=extras.getString("name");
        final String url=extras.getString("url");
        final String department=extras.getString("department");
        CircleImageView photo=findViewById(R.id.appbaruser);
        Picasso.get().load(url).into(photo);
        protext=findViewById(R.id.protext);
        protext.setText(name);
        final ImageButton button1 = findViewById(R.id.imageButton3);
        final ImageButton button2 = findViewById(R.id.imageButton4);
        DatabaseReference firebaseDatabase=FirebaseDatabase.getInstance().getReference();
        DatabaseReference driuid=firebaseDatabase.child("busno").child(bus).child("uid");
        driuid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uid=dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        String dbname = "nglbus";
        DBHelper dbHelper = new DBHelper(Main2Activity.this, dbname);
        ArrayList<String> busdri = dbHelper.getdriver(bus);
        String dri = busdri.get(0);
        final String drinu = busdri.get(1);
        tv.setText(dri);
        button1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + drinu));//change the number
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),"CHECK the permission",Toast.LENGTH_LONG).show();
                }
                startActivity(callIntent);
            }
        });
        ArrayList<HashMap<String, String>> busd = dbHelper.getAllBus(bus);
        ArrayList<String> busstop=dbHelper.getBusStop(bus);
        ArrayList<String> bustime=dbHelper.getBusTime(bus);

        ListView lv = findViewById(R.id.list5);
        MyListAdapter myListAdapter=new MyListAdapter(Main2Activity.this,busstop,bustime);
        lv.setAdapter(myListAdapter);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Main2Activity.this,MapsActivity.class);
                intent.putExtra("bus",bus);
                intent.putExtra("uid",uid);
                intent.putExtra("name",name);
                intent.putExtra("url",url);
                intent.putExtra("department",department);

                startActivity(intent);

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), ""+adapterView, Toast.LENGTH_LONG).show();
            }
        });
    }
    public class MyListAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private final String[] busst;
        private final String[] busti;


        public MyListAdapter(Activity context, ArrayList<String> busst, ArrayList<String> busti) {
            super(context, R.layout.layout3, busst);

            this.context=context;
            this.busst= busst.toArray(new String[0]);
            this.busti= busti.toArray(new String[0]);

        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.layout3, null,true);

            TextView titleText = (TextView) rowView.findViewById(R.id.busstop);
            TextView subtitleText = (TextView) rowView.findViewById(R.id.bustime);

            titleText.setText(busst[position]);
            subtitleText.setText(busti[position]);

            return rowView;

        };
    }
}

