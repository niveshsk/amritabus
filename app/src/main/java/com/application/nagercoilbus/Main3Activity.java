package com.application.nagercoilbus;

import android.app.Activity;
import android.content.Intent;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import com.ekalips.fancybuttonproj.FancyButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikelau.croperino.Croperino;
import com.mikelau.croperino.CroperinoConfig;
import com.mikelau.croperino.CroperinoFileUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Intent.ACTION_GET_CONTENT;
import static android.content.Intent.createChooser;

public class Main3Activity extends MainActivity {
    String username,department,rollno,url;
    String uid;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=firebaseDatabase.getReference();
    CircleImageView photo;
    DatabaseReference photourl;
    TextView protext;
FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout display=findViewById(R.id.activitydisplay);
        View view=getLayoutInflater().inflate(R.layout.activity_main3,null);
        display.addView(view);
        final SearchView searchView = findViewById(R.id.searchview);
        protext=findViewById(R.id.protext);
        firebaseStorage = FirebaseStorage.getInstance();
        new CroperinoConfig("IMG_" + System.currentTimeMillis() + ".jpg", "/amritabus/Pictures", "/sdcard/amritabus/Pictures");
        CroperinoFileUtil.verifyStoragePermissions(Main3Activity.this);

        CroperinoFileUtil.setupDirectory(Main3Activity.this);
        photo=findViewById(R.id.appbaruser);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Croperino.prepareGallery(Main3Activity.this);            }
        });
        TextView signout=findViewById(R.id.signout1);

        Intent intent=getIntent();

        String type=intent.getStringExtra("type");
         uid=intent.getStringExtra("uid");

        DatabaseReference usertype=databaseReference.child(type);
        DatabaseReference userid=usertype.child(uid);
        DatabaseReference name=userid.child("name");
        DatabaseReference dept=userid.child("dept");
        DatabaseReference regno=userid.child("regno");
        DatabaseReference board=userid.child("board");
        photourl=userid.child("photourl");
        photourl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    overridePendingTransition(0,0);
                    url=dataSnapshot.getValue(String.class);
                    Picasso.get().load(url).into(photo);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username=dataSnapshot.getValue(String.class);
                protext.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dept.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                department=dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        regno.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rollno=dataSnapshot.getValue(String.class);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        board.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                searchView.setQuery(dataSnapshot.getValue(String.class),false);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        String dbname="nglbus";
        DBHelper dbHelper=new DBHelper(Main3Activity.this,dbname);
        dbHelper.copyDataBase();
        final ArrayList<String> route=dbHelper.getbusro();
        final Object[] busno;
        busno= (Object[]) route.toArray();
        String[] colomn = new String[]{"_id", "name"};
        Object[] temp = new Object[]{0, "default"};
        ArrayList<Object> queryStations = new ArrayList<>();

        final MatrixCursor cursor = new MatrixCursor(colomn);
        for (int i = 0; i < route.size(); i++) {

            temp[0] = i;
            temp[1] = busno[i];
            cursor.addRow(temp);
            queryStations.add(temp);
        }
        final TodoCursorAdapter cursorAdapter = new TodoCursorAdapter(this, cursor);

        searchView.setSuggestionsAdapter(cursorAdapter);

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int i) {
                MatrixCursor temp = (MatrixCursor) cursorAdapter.getItem(i);
                searchView.setQuery(temp.getString(1), true);

                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.setQuery(s,false);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                final MatrixCursor c = new MatrixCursor(new String[]{ "_id", "cityName" });
                for (int i=0; i<route.size(); i++) {
                    if (busno[i].toString().contains(s))
                        c.addRow(new Object[] {i, busno[i]});
                }
                cursorAdapter.changeCursor(c);
                return false;
            }
        });

        final FancyButton button=findViewById(R.id.button3);
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               button.collapse();
               String sou1=searchView.getQuery().toString();
               String des1="College";

               {
                   String dbname = "nglbus";
                   DBHelper dbHelper = new DBHelper(Main3Activity.this, dbname);
                   final ListView lv = findViewById(R.id.list4);
                   ArrayList<String> busd = dbHelper.getBus(sou1, des1);
                   if (busd == null) {
                       lv.setVisibility(View.INVISIBLE);
                       Toast.makeText(getApplicationContext(), "NO bus AVAILABLE", Toast.LENGTH_LONG).show();

                   } else {
                       lv.setVisibility(View.VISIBLE);
                       ArrayAdapter adapter1 = new ArrayAdapter(Main3Activity.this, R.layout.layout2, R.id.bus1, busd.toArray());
                       lv.setAdapter(adapter1);
                       button.expand();

                       lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                           @Override
                           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                               String st= (String) lv.getItemAtPosition(i);
                               Intent intent =new Intent(Main3Activity.this,Main2Activity.class);
                               intent.putExtra("bus",st);
                               intent.putExtra("name",username);
                               intent.putExtra("url",url);
                               intent.putExtra("department",department);

                               startActivity(intent);
                           }
                       });
                   }
               }
           }
       });
    }

    private void choosephoto() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(ACTION_GET_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            startActivityForResult(createChooser(intent,"hello"),4);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CroperinoConfig.REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    /* Parameters of runCropImage = File, Activity Context, Image is Scalable or Not, Aspect Ratio X, Aspect Ratio Y, Button Bar Color, Background Color */
                    Croperino.runCropImage(CroperinoFileUtil.getTempFile(), Main3Activity.this, true, 1, 1, R.color.gray, R.color.gray_variant);
                }
                break;
            case CroperinoConfig.REQUEST_PICK_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    CroperinoFileUtil.newGalleryFile(data, Main3Activity.this);
                    Croperino.runCropImage(CroperinoFileUtil.getTempFile(), Main3Activity.this, true, 0, 0, R.color.gray, R.color.gray_variant);
                }
                break;
            case CroperinoConfig.REQUEST_CROP_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Uri i = Uri.fromFile(CroperinoFileUtil.getTempFile());
                    photo.setImageURI(i);
                    upload(i);
                }
                break;
            default:
                break;
        }
    }
 private void upload(Uri uri) {
        if(uri != null){
            StorageReference storageReference= firebaseStorage.getReference();
            final StorageReference storageReference1=storageReference.child("student/"+uid);
            storageReference1.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Main3Activity.this, "complered"+taskSnapshot.getStorage().getDownloadUrl(), Toast.LENGTH_SHORT).show();
                    Log.d("gr","fdfd  ");
                    Picasso.get().load(url).into(photo);
                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Main3Activity.this, "fail", Toast.LENGTH_SHORT).show();

                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                     Picasso.get().load(url).into(photo);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
            getdata();
        }
        else{
            Toast.makeText(this, "efrfgdfx", Toast.LENGTH_SHORT).show();
        }
    }

    private void getdata() {
        StorageReference storageReference= firebaseStorage.getReference();
        final StorageReference storageReference1=storageReference.child("student/"+uid);
        storageReference1.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Toast.makeText(Main3Activity.this, "compl"+task, Toast.LENGTH_SHORT).show();
                Log.d("datad","cd"+task);
            }
        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                overridePendingTransition(0,0);


                photourl.setValue(uri.toString());
                Picasso.get().load(uri).into(photo);

                url=uri.toString();

            }
        });
    }
}
