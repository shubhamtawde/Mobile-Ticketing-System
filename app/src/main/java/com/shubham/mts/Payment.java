package com.shubham.mts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Payment extends AppCompatActivity {
    private Button book;
    private String email;
    String s1, d1, n, num;
    private EditText no, namec, expiry, cvv;
    private int flag1, flag2, flag3, flag4, flag5;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        book = (Button) findViewById(R.id.book_btn);
        if(FirebaseAuth.getInstance().getCurrentUser()!= null)
            email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        no = (EditText) findViewById(R.id.no);
        namec = (EditText) findViewById(R.id.namec);
        expiry = (EditText) findViewById(R.id.expiry);
        cvv = (EditText) findViewById(R.id.cvv);
        final ArrayList data = getIntent().getStringArrayListExtra("data");
        s1 = data.get(0).toString();
        d1 = data.get(1).toString();
        n = data.get(2).toString();
        num = data.get(3).toString();
        book.setText("Confirm to pay ₹" + n);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag1 = 0;
                flag2 = 0;
                flag3 = 0;
                flag4 = 0;
                flag5 = 0;
                if (no.getText().toString().isEmpty()) {
                    flag1 = 1;
                    no.setError("Card Number cannot be empty!");
                }
                if (no.getText().toString().length() < 16) {
                    flag2 = 1;
                    no.setError("Card number cannot be less than 16!");
                }
                if (namec.getText().toString().isEmpty()) {
                    flag3 = 1;
                    namec.setError("Enter name!");
                }
                if (expiry.getText().toString().isEmpty()) {
                    flag4 = 1;
                    expiry.setError("Enter Expiry Date!");
                }
                if (cvv.getText().toString().isEmpty()) {
                    flag5 = 1;
                    cvv.setError("Enter CVV!");
                }
                if (flag1 == 0 && flag2 == 0 && flag3 == 0 && flag4 == 0 && flag5 == 0) {
                    Map<String, String> user = new HashMap<>();
                    String route = s1 + "->" + d1;
                    String ID = getSaltString();
                    user.put("route", route);
                    user.put("ID", ID);
                    user.put("fare", "₹"+n);
                    user.put("email", email);
                    user.put("num", num);
                    db.collection("bookings").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(Payment.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Payment.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                    DatabaseHandler db1 = new DatabaseHandler(Payment.this);
                    boolean res = db1.addBooking(ID, num, route, n);
                    if(res == false)
                        Toast.makeText(Payment.this, "Failed to add", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Payment.this, "Added!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Payment.this, BookingDetails.class);
                    ArrayList data = new ArrayList<>();
                    data.add(route);
                    data.add(n);
                    data.add(num);
                    data.add(ID);
                    intent.putStringArrayListExtra("data", data);
                    startActivity(intent);
                }
            }
        });
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
