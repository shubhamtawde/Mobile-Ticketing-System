package com.shubham.mts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AddPayment extends AppCompatActivity {
    private Button book;
    private int update;
    private String amount, bal = "", newbal;
    private EditText no, namec, expiry, cvv;
    private String email;
    private int flag1, flag2, flag3, flag4, flag5;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final ArrayList data = getIntent().getStringArrayListExtra("data");
        amount = data.get(0).toString();
        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
        book = (Button) findViewById(R.id.book_btn);
        no = (EditText) findViewById(R.id.no);
        namec = (EditText) findViewById(R.id.namec);
        expiry = (EditText) findViewById(R.id.expiry);
        cvv = (EditText) findViewById(R.id.cvv);
        book.setText("Confirm to pay ₹" + amount);
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
                    db.collection("users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                DocumentSnapshot documentSnapshot = task.getResult();
                                if(documentSnapshot.exists()) {
                                    bal = documentSnapshot.get("balance").toString();
                                    update = Integer.parseInt(bal);
                                    update = update + Integer.parseInt(amount);
                                    newbal = Integer.toString(update);
                                    Toast.makeText(AddPayment.this, newbal, Toast.LENGTH_SHORT).show();
                                    db.collection("users").document(email).update("balance", newbal).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddPayment.this, "Balance Successfully Updated!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(AddPayment.this, HomeActivity.class);
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AddPayment.this, "Unable to update balance please try again later!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddPayment.this, "Failed to retrieve balance! Try again later!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(AddPayment.this, newbal, Toast.LENGTH_SHORT).show();


                }
            }
        });

    }
}