package com.shubham.mts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private TextView login;
    private EditText name, email, mob, pass;
    private FirebaseAuth mAuth;
    private Button register_btn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        register_btn = (Button) findViewById(R.id.register_btn);
        login = (TextView) findViewById(R.id.login);
        name = (EditText) findViewById(R.id.register_name);
        email = (EditText) findViewById(R.id.register_email);
        mob = (EditText) findViewById(R.id.register_mobile);
        pass = (EditText) findViewById(R.id.register_password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = name.getText().toString();
                String e = email.getText().toString();
                String m = mob.getText().toString();
                String p = pass.getText().toString();
                if (n.isEmpty())
                    name.setError("Name is required!");
                if(e.isEmpty())
                    email.setError("Email is required!");
                if(m.isEmpty())
                    mob.setError("Mobile is required!");
                if(p.isEmpty())
                    pass.setError("Password is required!");
                if(p.length()<6)
                    pass.setError("Password should be of 6 characters!");
                if(!n.isEmpty() && !p.isEmpty() && !m.isEmpty() && !p.isEmpty())
                {
                    register(e,p,m,n);
                }
            }
        });
    }

    private void register(String e, String p, String m, String n) {
        final String mo = m;
        final String na = n;
        mAuth.createUserWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(Register.this, "Registered Successfully! Verify your email address!", Toast.LENGTH_SHORT).show();
                FirebaseUser user = mAuth.getCurrentUser();
                Map<String, String> users = new HashMap<>();
                users.put("balance", "0");
                users.put("email", user.getEmail());
                users.put("mobile", mo);
                users.put("name", na);
                db.collection("users").document(user.getEmail()).set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //success
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //fail
                    }
                });
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //
                    }
                });
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this, "Failed to register! Please try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
