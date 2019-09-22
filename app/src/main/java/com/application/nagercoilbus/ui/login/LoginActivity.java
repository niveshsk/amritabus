package com.application.nagercoilbus.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.ContentResolver;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.application.nagercoilbus.DBHelper;
import com.application.nagercoilbus.R;
import com.application.nagercoilbus.userActivity;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private FirebaseAuth mAuth;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        String dbname="nglbus";
        final DBHelper dbHelper=new DBHelper(LoginActivity.this,dbname);
        final AutoCompleteTextView busro;
        busro=findViewById(R.id.busro);
        ArrayList<String> busn = dbHelper.getBusno();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(LoginActivity.this, R.layout.support_simple_spinner_dropdown_item, busn);
        busro.setAdapter(adapter1);
        busro.setThreshold(0);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        final LinearLayout linearLayout=findViewById(R.id.driverreg);
        linearLayout.setVisibility(View.INVISIBLE);
        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        Button register=findViewById(R.id.button4);
        register.setVisibility(View.VISIBLE);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                final EditText name,email,phno,dob;
                final String route=busro.getText().toString();
                name=findViewById(R.id.name);
                email=findViewById(R.id.email);
                phno=findViewById(R.id.phno);
                dob=findViewById(R.id.dob);
                final String mail=email.getText().toString().trim();
                final String pass=dob.getText().toString().trim();
                final String uname=name.getText().toString();
                final String uphno=phno.getText().toString();

                if(email.equals("")){

                    email.setError("Enter the mail id correctly");
                    Toast.makeText(LoginActivity.this, "Enter important fields", Toast.LENGTH_SHORT).show();
                    loadingProgressBar.setVisibility(View.INVISIBLE);

                }
                else if(pass.equals("")){
                    dob.setError("Enter the date of birth");
                    Toast.makeText(LoginActivity.this, "Enter important fields", Toast.LENGTH_SHORT).show();
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                }
                else if(uname.equals("")){
                    name.setError("Enter the boarding point");
                    Toast.makeText(LoginActivity.this, "Enter important fields", Toast.LENGTH_SHORT).show();
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                }
                else if(uphno.equals("")){
                    phno.setError("Enter department");
                    Toast.makeText(LoginActivity.this, "Enter important fields", Toast.LENGTH_SHORT).show();
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                }
                else if(route.equals("")){
                    dob.setError("Enter the register number should be 12 numbers");
                    Toast.makeText(LoginActivity.this, "Enter important fields", Toast.LENGTH_SHORT).show();
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                }else {
                    mAuth.createUserWithEmailAndPassword(mail,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user2 = mAuth.getCurrentUser();
                            assert user2 != null;
                            String uid = user2.getUid();

                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                            DatabaseReference databaseReference1 = firebaseDatabase.getReference();

                            DatabaseReference usertype = databaseReference1.child("Driver");

                            DatabaseReference userid = usertype.child(uid);
                            DatabaseReference busro= userid.child("busro");
                            DatabaseReference mail1 = userid.child("mail");
                            DatabaseReference name1 = userid.child("name");
                            DatabaseReference phno1 = userid.child("phno");
                            name1.setValue(uname);
                            mail1.setValue(mail);
                            phno1.setValue(uphno);
                            busro.setValue(route);
                            DatabaseReference busno=databaseReference1.child("busno");
                            DatabaseReference busroute=busno.child(route);
                            DatabaseReference busuid=busroute.child("uid");
                            busuid.setValue(uid);

                            loadingProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, "you sucessfully registered go to amrita driver bus app", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(LoginActivity.this,com.application.nagercoilbus.userActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                        }
                    });

                }

            }
        });
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    username.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    password.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(username.getText().toString(),
                        password.getText().toString());
            }
        };
        username.addTextChangedListener(afterTextChangedListener);
        password.addTextChangedListener(afterTextChangedListener);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(username.getText().toString(),
                            password.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingProgressBar.setVisibility(View.VISIBLE);
                String username1=username.getText().toString();
                String password1=password.getText().toString();
                if(username1.equals("amritaadmin@gmail.com") && password1.equals("amritaadmin@9623")){
                    username.setVisibility(View.INVISIBLE);
                    password.setVisibility(View.INVISIBLE);
                    loginButton.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Invalid admin login", Toast.LENGTH_SHORT).show();
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                }

            }
        });
    }






    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
