package com.shubham.mts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class BookingDetails extends AppCompatActivity {
    private String route, n, num, ID;
    private TextView sr1, sr2, id, cost;
    private Button gohome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        final ArrayList data = getIntent().getStringArrayListExtra("data");
        route = data.get(0).toString();
        n = data.get(1).toString();
        num = data.get(2).toString();
        ID = data.get(3).toString();
        sr1 = (TextView) findViewById(R.id.sr1);
        sr2 = (TextView) findViewById(R.id.sr2);
        id = (TextView) findViewById(R.id.ID);
        cost = (TextView) findViewById(R.id.cost);
        gohome = (Button) findViewById(R.id.gohome);
        sr1.setText(num);
        sr2.setText(route);
        id.setText(ID);
        cost.setText("₹" + n);
        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingDetails.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}
