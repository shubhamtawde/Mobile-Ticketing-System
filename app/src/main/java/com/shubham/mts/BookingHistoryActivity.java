package com.shubham.mts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class BookingHistoryActivity extends AppCompatActivity {
    private RecyclerView list;
    private FirebaseFirestore db;
    String email;
    private BookingAdapter bookingAdapter;
    private List<Bookings> namesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
        db = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }
        list = (RecyclerView) findViewById(R.id.main_list);
        LinearLayoutManager ln = new LinearLayoutManager(this);
        list.setHasFixedSize(true);
        list.setLayoutManager(ln);
        bookingAdapter = new BookingAdapter(namesList);
        list.setAdapter(bookingAdapter);
        db.collection("bookings").whereEqualTo("email", email).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                namesList.clear();
                progressBar.setVisibility(View.INVISIBLE);
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Bookings bookings = doc.toObject(Bookings.class);
                    namesList.add(bookings);
                    bookingAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
