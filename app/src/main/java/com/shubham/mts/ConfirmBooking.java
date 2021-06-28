package com.shubham.mts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ConfirmBooking extends AppCompatActivity {
    private TextView sr1;
    private String mail, balance, n;
    private int update;
    private TextView sr2;
    private TextView fare;
    private Button book;
    private RadioButton bal, card;
    private ArrayList dat = new ArrayList<>();
    private AlertDialog.Builder builder;;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        builder = new AlertDialog.Builder(this);
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            mail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        builder.setMessage("Use Balance").setTitle("Use Balance");
        final ArrayList data = getIntent().getStringArrayListExtra("data");
        String s1 = data.get(0).toString();
        String s2 = data.get(1).toString();
        n = data.get(2).toString();
        String num = data.get(3).toString();
        sr1 = (TextView) findViewById(R.id.sr1);
        sr2 = (TextView) findViewById(R.id.sr2);
        fare = (TextView) findViewById(R.id.cost);
        book = (Button) findViewById(R.id.book_btn);
        bal = (RadioButton) findViewById(R.id.bal);
        card = (RadioButton) findViewById(R.id.bal2);
        sr1.setText(s1);
        sr2.setText(s2);
        fare.setText(n);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dat.add(data.get(0));
                dat.add(data.get(1));
                dat.add(data.get(2));
                dat.add(data.get(3));
                if(card.isChecked()) {
                    Intent intent = new Intent(ConfirmBooking.this, Payment.class);
                    intent.putStringArrayListExtra("data", dat);
                    startActivity(intent);
                }
                else if(bal.isChecked()){
                    //
                    db.collection("users").document(mail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot doc = task.getResult();
                                if (doc.exists()) {
                                    Toast.makeText(ConfirmBooking.this, "Document exists!", Toast.LENGTH_SHORT).show();
                                    balance = doc.get("balance").toString();
                                }
                            }
                        }
                    });
                    builder.setMessage("Do you want to use balance?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    update = Integer.parseInt(balance);
                                    update = update - Integer.parseInt(n);
                                    balance = Integer.toString(update);
                                    db.collection("users").document(mail).update("balance", balance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(ConfirmBooking.this, "Balance updated!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ConfirmBooking.this, BookingDetails.class);
                                            intent.putStringArrayListExtra("data", dat);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                    Toast.makeText(getApplicationContext(),"NO",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Use Balance");
                    alert.show();
                }
            }
        });
    }
}
