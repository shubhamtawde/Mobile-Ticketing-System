package com.shubham.mts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private String name;
    private String gender;
    private String mobile;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        ArrayList<String> data = getIntent().getStringArrayListExtra("data");
        name = data.get(0);
        mobile = data.get(1);
        TextView user_name = (TextView) findViewById(R.id.user_name);
        EditText u_name = (EditText) findViewById(R.id.u_name);
        EditText user_email = (EditText) findViewById(R.id.user_email);
        EditText mob = (EditText) findViewById(R.id.mobile);
        user_name.setText(name);
        u_name.setText(name);
        mob.setText(mobile);
        user_email.setText(mAuth.getCurrentUser().getEmail().toString());
    }
}
