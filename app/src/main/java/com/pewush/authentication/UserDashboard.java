package com.pewush.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserDashboard extends AppCompatActivity {
    FirebaseAuth Auth;
    FirebaseUser user;
    private long backPressedTime;
    CardView RequestBtn, viewMyAssetBtn,TransferRequestBtn;
    ImageView signOutBtn;
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed(); // exits app
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_dashboard);
        Auth=FirebaseAuth.getInstance();
        user=Auth.getCurrentUser();
        RequestBtn=findViewById(R.id.RequestButton);
        viewMyAssetBtn=findViewById(R.id.viewMyAssetButton);
        TransferRequestBtn=findViewById(R.id.TransferRequestButton);
        signOutBtn=findViewById(R.id.signOutButton);

        RequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserRequestAssetActivity.class);
                startActivity(intent);
            }
        });
        viewMyAssetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserViewMyAssets.class);
                startActivity(intent);
            }
        });
        TransferRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserRequestTransferActivity.class);
                startActivity(intent);
            }
        });
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}