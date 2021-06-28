package com.shubham.mts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BookTicketActivity extends AppCompatActivity {
    private Spinner spinner;
    private FirebaseFirestore db;
    String m[];
    private Button book;
    String src, dest;
    private int cost;
    private String arr[] = {"Andheri", "Vile Parle", "Amethi", "Virar"};
    private String arr1[] = {"Andheri", "Vile Parle", "Amethi", "Virar"};
    AutoCompleteTextView autoCompleteTextView, autoCompleteTextView2;
    String z;
    ArrayList<String> route = new ArrayList<>();
    ArrayList<String> no = new ArrayList<>();
    String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ticket);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.sr1);
        autoCompleteTextView2 = (AutoCompleteTextView) findViewById(R.id.sr2);
        book = (Button) findViewById(R.id.book_btn);
        final int fare[][] = {{0, 20, 30, 40}, {20, 0, 30, 40,}, {20, 30, 0, 40}, {30, 10, 20, 0}};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, arr);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(arrayAdapter);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, arr1);
        autoCompleteTextView2.setThreshold(1);
        autoCompleteTextView2.setAdapter(arrayAdapter);
        book.setClickable(true);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (autoCompleteTextView.getText().toString().isEmpty() && autoCompleteTextView2.getText().toString().isEmpty())
                    Toast.makeText(BookTicketActivity.this, "Enter source and destination!", Toast.LENGTH_SHORT).show();
                if (autoCompleteTextView.getText().toString().equals(autoCompleteTextView2.getText().toString()))
                    Toast.makeText(BookTicketActivity.this, "Source and destination cannot be same!", Toast.LENGTH_SHORT).show();
                else {
                    String s1 = autoCompleteTextView.getText().toString();
                    String d1 = autoCompleteTextView2.getText().toString();
                    int i, j;
                    for (i = 0; i < arr.length; i++) {
                        if (arr[i].equals(s1))
                            break;
                    }
                    for (j = 0; j < arr.length; j++) {
                        if (arr1[j].equals(d1))
                            break;
                    }
                    cost = fare[i][j];
                    Toast.makeText(BookTicketActivity.this, "Book Ticket " + cost, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BookTicketActivity.this, ConfirmBooking.class);
                    ArrayList data = new ArrayList<>();
                    data.add(s1);
                    data.add(d1);
                    data.add(cost);
                    data.add(num);
                    intent.putStringArrayListExtra("data", data);
                    startActivity(intent);
                }
            }
        });
        spinner = (Spinner) findViewById(R.id.bus_name);
        db = FirebaseFirestore.getInstance();
        db.collection("bus").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        no.add(doc.getId());
                    }
                    ArrayAdapter<String> aa = new ArrayAdapter<String>(BookTicketActivity.this, android.R.layout.simple_spinner_item, no);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //Setting the ArrayAdapter data on the Spinner
                    spinner.setAdapter(aa);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Toast.makeText(BookTicketActivity.this, no.get(i), Toast.LENGTH_SHORT).show();
                            num = no.get(i);
                            Log.d("num", num);
                            db.collection("bus").document(num).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        //z = documentSnapshot.get("route").toString();
                                        //Log.d("Z", z);
                                        //m = z.split(",");
                                    } else
                                        Toast.makeText(BookTicketActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }
            }
        });


    }


}
