package com.shubham.mts;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ShowTicket extends AppCompatActivity {
    private TextView noact;
    private ProgressBar pr;
    private RecyclerView list;
    private BookingAdapter bookingAdapter;
    private TextView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ticket);


        noact = (TextView) findViewById(R.id.load);
        pr = (ProgressBar) findViewById(R.id.progress);
        DatabaseHandler db = new DatabaseHandler(this);
        Cursor res = db.getAllContacts();
        if (res.getCount() == 0) {
            pr.setVisibility(View.INVISIBLE);
            noact.setVisibility(View.VISIBLE);
        } else {
            pr.setVisibility(View.INVISIBLE);
            noact.setVisibility(View.INVISIBLE);
            listView = (TextView) findViewById(R.id.listView);
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("Id :" + res.getString(0) + "\n");
                buffer.append("Bus Number :" + res.getString(1) + "\n");
                buffer.append("Route :" + res.getString(2) + "\n\n");
                //buffer.append("Fare :" + res.getString(3) + "\n\n");
            }
            listView.setText(buffer.toString());
        }
    }
}
