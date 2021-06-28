package com.shubham.mts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private Button login_btn, forgot;
    private CheckBox remember;
    private TextView register;

    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        register = (TextView) findViewById(R.id.register);
        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);
        login_btn = (Button) findViewById(R.id.login_btn);
        forgot = (Button) findViewById(R.id.forgot);
        remember = (CheckBox) findViewById(R.id.remember);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {

                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }
            }
        };

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

        remember.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(MainActivity.this, "Check to Remember User", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (password.getText().toString().length() <6) {
                        Toast.makeText(getApplicationContext(), "Password should be of min 6 characters!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Correct Password Length!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void reset() {
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }

    private void register() {
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    }

    private void signIn() {
        String e = email.getText().toString();
        String p = password.getText().toString();

        if (TextUtils.isEmpty(e) || TextUtils.isEmpty(p)) {
            Toast.makeText(this, "Fields Empty", Toast.LENGTH_SHORT).show();
        } else {
            login_btn.setText("Signing in...");
            mAuth.signInWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    } else {
                        login_btn.setText("Sign In successful!");
                    }
                }
            });
        }
    }
}
