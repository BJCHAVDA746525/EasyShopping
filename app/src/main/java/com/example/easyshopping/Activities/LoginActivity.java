package com.example.easyshopping.Activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.easyshopping.R;
import com.example.easyshopping.WorkManager.FireUserSync;
import com.example.easyshopping.WorkManager.SyncScheduler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //Variables


    //Views
    EditText emailEdt, passEdt;
    AppCompatButton LoginBtn;
    LinearLayout RegisterBtn;
    ProgressBar progressbar;
    private TextInputLayout emailInputLayout, passwordInputLayout;

    //Objects
    private FirebaseAuth mAuth;
    Room_Helper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitView();
    }

    private void InitView() {
        //Views
        emailEdt = findViewById(R.id.email_input_edt);
        passEdt = findViewById(R.id.pass_edt_text);
        LoginBtn = findViewById(R.id.login_btn);
        RegisterBtn = findViewById(R.id.register_text_layout);
        progressbar = findViewById(R.id.progressBar);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);

        //OnClicks
        LoginBtn.setOnClickListener(this);
        RegisterBtn.setOnClickListener(this);

        //Object Initiation
        mAuth = FirebaseAuth.getInstance();
        helper = Room_Helper.GetDB(LoginActivity.this);
    }

    @Override
    public void onClick(View view) {
        int ID = view.getId();

        if (ID == R.id.login_btn) {

            if (ValidateLoginInput()) {
                String email = emailInputLayout.getEditText().getText().toString().trim();
                String password = passwordInputLayout.getEditText().getText().toString().trim();
                LoginFun(email, password);
            }


        } else if (ID == R.id.register_text_layout) {

            RegisterFun();
        }


    }

    private Boolean ValidateLoginInput() {
        String Email = emailInputLayout.getEditText().getText().toString().trim();
        String Password = passwordInputLayout.getEditText().getText().toString().trim();

        //Email Validation
        if (Email.isEmpty()) {
            emailInputLayout.setError("Email can't be Empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            emailInputLayout.setError("Invalid Email");
            return false;
        } else {
            emailInputLayout.setError(null);  // Clear error
        }

        //Password Validation
        if (Password.isEmpty()) {
            passwordInputLayout.setError("Password can't Be Empty");
            return false;
        } else if (Password.length() < 3) {
            passwordInputLayout.setError("Password must be atleast 3 character long");
            return false;
        } else {
            passwordInputLayout.setError(null);
        }

        return true;
    }

    private void RegisterFun() {
        Intent iregi = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(iregi);
    }

    private void LoginFun(String email, String password) {
        LoginBtn.setVisibility(View.GONE);
        progressbar.setVisibility(View.VISIBLE);

        // signing existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this.getApplicationContext(), "Login successful!!", Toast.LENGTH_LONG).show();
                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);
                                    LoginBtn.setVisibility(View.VISIBLE);

                                    SyncScheduler syncScheduler = new SyncScheduler();
                                    syncScheduler.UserSync();

                                    // if sign-in is successful
                                    Intent intent = new Intent(LoginActivity.this, ShoppingActivity.class);
                                    startActivity(intent);
                                    finish();


                                } else {
                                    progressbar.setVisibility(View.GONE);
                                    LoginBtn.setVisibility(View.VISIBLE);

                                    // sign-in failed
                                    Toast.makeText(LoginActivity.this.getApplicationContext(), "Login failed, Please Try Again", Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(getIntent());
                                    // hide the progress bar

                                }
                            }
                        });
    }


}

