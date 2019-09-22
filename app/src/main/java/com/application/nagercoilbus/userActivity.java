package com.application.nagercoilbus;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.application.nagercoilbus.ui.login.LoginActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class userActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private int requestcode=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        LinearLayout mainactivity=findViewById(R.id.activityfr);
mainactivity.setVisibility(View.VISIBLE);
        final ImageView progressBar=findViewById(R.id.imageView7);
        Glide.with(this)
                .load(R.drawable.safe)
                .into(progressBar);
               requestPerm(requestcode);
               final LinearLayout linearLayout2=findViewById(R.id.userre);
        linearLayout2.setVisibility(View.INVISIBLE);
        final LinearLayout linearLayout1=findViewById(R.id.userlog);
        linearLayout1.setVisibility(View.VISIBLE);
        String dbname="nglbus";
        final DBHelper dbHelper=new DBHelper(userActivity.this,dbname);
        dbHelper.copyDataBase();

        progressBar.setVisibility(View.GONE);
        progressBar.setVisibility(View.INVISIBLE);
        TextView driverlog=findViewById(R.id.textView15);
        driverlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(userActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        TextView create=findViewById(R.id.textView14);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.INVISIBLE);
                Button button=findViewById(R.id.register);
                final EditText regno;
                final AutoCompleteTextView board,dept;
                final String[] selected = {""};
                board=findViewById(R.id.board);
                linearLayout2.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.INVISIBLE);

                ArrayList<String> route=dbHelper.getbusro();
                ArrayAdapter<String> adapter=new ArrayAdapter<>(userActivity.this,R.layout.support_simple_spinner_dropdown_item,route);
                board.setAdapter(adapter);
                board.setThreshold(0);
                dept=findViewById(R.id.dept);
                ArrayList<String> deptco=dbHelper.collegeDepartment();
                ArrayAdapter<String> adapter3=new ArrayAdapter<>(userActivity.this,R.layout.support_simple_spinner_dropdown_item,deptco);
                dept.setAdapter(adapter3);
                dept.setThreshold(0);



                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        linearLayout1.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);

                        final EditText name,mail,dob,regno;
                        final AutoCompleteTextView board,dept;
                        name=findViewById(R.id.uname);
                        mail=findViewById(R.id.uemail);
                        dob=findViewById(R.id.dob);
                        board=findViewById(R.id.board);
                        dept=findViewById(R.id.dept);
                        regno=findViewById(R.id.regno);
                        final String uname=name.getText().toString();
                        final String pass=dob.getText().toString();
                        final String email=mail.getText().toString();
                        final String uboard=board.getText().toString();
                        final String udept=dept.getText().toString();
                        final String uregno=regno.getText().toString();

                        if(email.equals("")){
                            mail.setError("Enter the mail id correctly");
                            Toast.makeText(userActivity.this, "Enter important fields", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                        else if(name.equals("")){
                            dept.setError("Enter Name");
                            Toast.makeText(userActivity.this, "Enter important fields", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else if(pass.equals("")){
                            dob.setError("Enter the date of birth");
                            Toast.makeText(userActivity.this, "Enter important fields", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else if(uboard.equals("")){
                            board.setError("Enter the boarding point");
                            Toast.makeText(userActivity.this, "Enter important fields", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else if(udept.equals("")){
                            dept.setError("Enter department");
                            Toast.makeText(userActivity.this, "Enter important fields", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else if(uregno.equals("")&& uregno.length()<=12){
                            regno.setError("Enter the register number should be 12 numbers");
                            Toast.makeText(userActivity.this, "Enter important fields", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        else {
                            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user1 = mAuth.getCurrentUser();
                                        String uid = null;
                                        if (user1 != null) {
                                            uid = user1.getUid();
                                        }

                                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                            DatabaseReference databaseReference1 = firebaseDatabase.getReference();
                                            DatabaseReference usertype = databaseReference1.child("Student");
                                            DatabaseReference userid = null;
                                            if (uid != null) {
                                                userid = usertype.child(uid);
                                            }
                                            DatabaseReference mail = null;
                                            if (userid != null) {
                                                mail = userid.child("mail");
                                            }
                                            DatabaseReference name = null;
                                            if (userid != null) {
                                                name = userid.child("name");
                                            }
                                            DatabaseReference board = userid.child("board");
                                            DatabaseReference dept = userid.child("dept");
                                            DatabaseReference regno = userid.child("regno");
                                            name.setValue(uname);
                                            if (mail != null) {
                                                mail.setValue(email);
                                            }
                                            board.setValue(uboard);
                                            dept.setValue(udept);
                                            regno.setValue(uregno);



                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(userActivity.this, "Succes", Toast.LENGTH_SHORT).show();
                                        linearLayout1.setVisibility(View.VISIBLE);

                                        linearLayout2.setVisibility(View.INVISIBLE);


                                    } else {
                                        progressBar.setVisibility(View.INVISIBLE);

                                        Toast.makeText(userActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }
                });

            }
        });
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Button signin=findViewById(R.id.sign);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
               EditText mail =findViewById(R.id.email);
               EditText pass=findViewById(R.id.password);
               String email=mail.getText().toString();
               String epass=pass.getText().toString();
               if(email.equals("")) {
                   mail.setError("Enter the mail id correctly");
                   Toast.makeText(userActivity.this, "Enter important fields", Toast.LENGTH_SHORT).show();
                   progressBar.setVisibility(View.INVISIBLE);
               }
                else if(epass.equals("")){
                   pass.setError("Enter the date of birth");
                   Toast.makeText(userActivity.this, "Enter important fields", Toast.LENGTH_SHORT).show();
                   progressBar.setVisibility(View.INVISIBLE);
               }
               else{
               mAuth.signInWithEmailAndPassword(email,epass).addOnCompleteListener(userActivity.this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           FirebaseUser user=mAuth.getCurrentUser();
                           String uid= null;
                           if (user != null) {
                               uid = user.getUid();
                           }
                           TextView usid=findViewById(R.id.textView13);
                           usid.setText(uid);
                           FirebaseDatabase database=FirebaseDatabase.getInstance();
                           DatabaseReference databaseReference=database.getReference();
                           DatabaseReference usertype=databaseReference.child("Student");
                           DatabaseReference type=usertype.child(uid);
                           type.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   if(dataSnapshot.exists()){

                                       TextView userid=findViewById(R.id.textView13);
                                       String uid=userid.getText().toString();
                                       Intent intent=new Intent(userActivity.this,Main3Activity.class);
                                       intent.putExtra("type","Student");
                                       intent.putExtra("uid",uid);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                       startActivity(intent);
                                       progressBar.setVisibility(View.INVISIBLE);
                                       finish();
                                   }
                                   else{
                                       Toast.makeText(userActivity.this, "I think you driver pleae login in Amritadriver application", Toast.LENGTH_SHORT).show();

                                   }
                               }
                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {

                               }
                           });

                       }
                       else{
                           Toast.makeText(userActivity.this, "Invalid user", Toast.LENGTH_SHORT).show();
                           progressBar.setVisibility(View.INVISIBLE);

                       }
                   }
               });
          }}
        });

    }
    private void requestPerm(int i) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(userActivity.this, Manifest.permission.CALL_PHONE)){
            new AlertDialog.Builder(userActivity.this).setTitle("permission need").setMessage("permission need").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(userActivity.this,new String[]{Manifest.permission.CALL_PHONE},requestcode);
                }
            })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }else{
            ActivityCompat.requestPermissions(userActivity.this,new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},requestcode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode== requestcode){
            if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){

            }else{
                Toast.makeText(userActivity.this,"check permission deny",Toast.LENGTH_LONG).show();
            }

        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null)
            mAuth.signOut();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
