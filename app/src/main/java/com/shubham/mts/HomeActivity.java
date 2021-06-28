package com.shubham.mts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private Button logout;
    private Button profile;
    private Button book, show;
    private Button history, add;
    private FirebaseAuth mAuth;
    private String e = "";
    private ProgressBar p;
    private TextView b;
    private ImageView more;
    private ArrayList<String> data = new ArrayList<String>();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.settings) {
            Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Cities.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.logout)
            Toast.makeText(getApplicationContext(), "Help", Toast.LENGTH_SHORT).show();
        else if(item.getItemId() == R.id.bluetooth) {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
        }
        return false;
    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.settings) {
                    Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(getApplicationContext(), Cities.class);
                    //startActivity(intent);
                }
                else if (menuItem.getItemId() == R.id.logout){
                    Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
                    logout();
                }
                else if(menuItem.getItemId() == R.id.bluetooth) {
                    Intent intent = new Intent(getApplicationContext(), Bluetooth.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        add = (Button) findViewById(R.id.add_money);
        show = (Button) findViewById(R.id.show);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ShowTicket.class);
                startActivity(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddMoney.class);
                startActivity(intent);
            }
        });
        p = (ProgressBar) findViewById(R.id.balance_load);
        book = (Button) findViewById(R.id.book);
        more = (ImageView) findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BookTicketActivity.class);
                startActivity(intent);
            }
        });
        history = (Button) findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BookingHistoryActivity.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        b = (TextView) findViewById(R.id.balance);
        if (mAuth.getCurrentUser() != null)
            e = mAuth.getCurrentUser().getEmail().toString();
        Log.d("T", e);

        db.collection("users").document(e).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        Toast.makeText(HomeActivity.this, "Document exists!", Toast.LENGTH_SHORT).show();
                        String name = doc.get("name").toString();
                        String mobile = doc.get("mobile").toString();
                        String balance = doc.get("balance").toString();
                        b.setText("₹" + balance);
                        b.setVisibility(View.VISIBLE);
                        p.setVisibility(View.INVISIBLE);
                        data.add(name);
                        data.add(mobile);
                    } else {
                        Toast.makeText(HomeActivity.this, "No Document", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Failed " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putStringArrayListExtra("data", data);
                startActivity(intent);
            }
        });
        //logout = (Button) findViewById(R.id.logout);

        /*logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });*/

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {

                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                }
            }
        };

    }

}
