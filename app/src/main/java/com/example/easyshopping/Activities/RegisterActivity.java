package com.example.easyshopping.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.Data.UserDataModel;
import com.example.easyshopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //Views
    EditText emailEditText, passwordEditText, nameEditText, phoneEditText;
    AppCompatButton registerButton;
    LinearLayout loginLayout;
    ProgressBar progressBar;


    //Objects
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    Room_Helper helper;

    //Lists
    ArrayList<UserDataModel> user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        InitView();


    }

    private void InitView() {

        emailEditText = findViewById(R.id.regi_email_input_edt);
        passwordEditText = findViewById(R.id.regi_pass_edt_text);
        nameEditText = findViewById(R.id.regi_name_input_edt);
        registerButton = findViewById(R.id.regi_btn);
        loginLayout = findViewById(R.id.login_text_layout);
        progressBar = findViewById(R.id.regi_progressBar);
        phoneEditText = findViewById(R.id.regi_phone_input_edt);

        registerButton.setOnClickListener(this);
        loginLayout.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        helper = Room_Helper.GetDB(this);
    }

    @Override
    public void onClick(View view) {
        int ID = view.getId();

        if (ID == R.id.regi_btn) {

            RegisterFun();

        } else if (ID == R.id.login_text_layout) {

            LoginFun();

        }
    }

    private void RegisterFun() {

        progressBar.setVisibility(View.VISIBLE);

        String email, password, name, phone;
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        name = nameEditText.getText().toString();
        phone = phoneEditText.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Valid email is required");
            emailEditText.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Valid email is required");
            emailEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            nameEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone) || !Patterns.PHONE.matcher(phone).matches()) {
            phoneEditText.setError("Valid phone number is required");
            phoneEditText.requestFocus();
            return;
        }

        // register user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // hide the progress bar
                                    progressBar.setVisibility(View.GONE);


                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    String userId = user.getUid();

                                    // Create a HashMap to store user details
                                    HashMap<String, String> userDetails = new HashMap<>();
                                    userDetails.put("userId", userId);
                                    userDetails.put("name", name);
                                    userDetails.put("email", email);
                                    userDetails.put("phone", phone);
                                    userDetails.put("profileImageUrl", "");


                                    // Store user details in the database
                                    databaseReference = FirebaseDatabase.getInstance().getReference("users");

                                    String userFireKey = databaseReference.push().getKey();
                                    databaseReference.child(userId).child(userFireKey).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                                Bundle bundle = new Bundle();
                                                bundle.putString("userFireKey", userFireKey);

                                                Intent intent = new Intent(RegisterActivity.this, ImageUploadActivity.class);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                                finish();


                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Registration failed!!", Toast.LENGTH_LONG).show();
                                                finish();
                                                startActivity(getIntent());
                                            }
                                        }
                                    });
                                } else {

                                    // sign-in failed
                                    Toast.makeText(RegisterActivity.this, "Registration failed!!", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    finish();
                                    startActivity(getIntent());
                                    // hide the progress bar

                                }

                            }
                        });


    }

    private void LoginFun() {

        Intent ilogin = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(ilogin);
        finish();
    }


}