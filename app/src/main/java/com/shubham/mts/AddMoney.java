package com.shubham.mts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class AddMoney extends AppCompatActivity {
    private EditText add;
    private Button add_btn;
    private int no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        add = (EditText) findViewById(R.id.add);
        add_btn = (Button) findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no = Integer.parseInt(add.getText().toString());
                if(add.getText().toString().isEmpty())
                {
                    add.setError("Enter a valid Amount!");
                }
                if(no<0){
                    add.setError("Enter a valid Amount!");
                }
                else
                {
                    Intent intent= new Intent(AddMoney.this, AddPayment.class);
                    ArrayList data = new ArrayList<>();
                    data.add(add.getText().toString());
                    intent.putStringArrayListExtra("data", data);
                    startActivity(intent);
                }
            }
        });
    }
}
