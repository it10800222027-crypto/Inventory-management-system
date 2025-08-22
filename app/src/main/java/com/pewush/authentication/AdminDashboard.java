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

public class AdminDashboard extends AppCompatActivity {
    FirebaseAuth Auth;
    FirebaseUser user;
    ImageView signOutBtn;
    CardView AddBtn, AssignBtn, DeleteBtn, TransferBtn, ViewRequestBtn,AllAssetsBtn,transferReqBtn;
    private long backPressedTime;

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
        setContentView(R.layout.activity_admin_dashboard);
        Auth = FirebaseAuth.getInstance();
        user=Auth.getCurrentUser();
        signOutBtn =findViewById(R.id.signOutImg);
        AddBtn=findViewById(R.id.AddButton);
        AssignBtn=findViewById(R.id.AssignButton);
        DeleteBtn=findViewById(R.id.DeleteButton);
        TransferBtn=findViewById(R.id.TransferButton);
        ViewRequestBtn=findViewById(R.id.ViewRequestButton);
        AllAssetsBtn=findViewById(R.id.viewAllAssetsButton);
        transferReqBtn=findViewById(R.id.ViewTransferRequestButton);
        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdmAddAssetsActivity.class);
                startActivity(intent);
            }
        });

        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdmDeleteAssetActivity.class);
                startActivity(intent);
            }
        });

        AssignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdmAssignAssetActivity.class);
                startActivity(intent);
            }
        });

        TransferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdmTransferAssetActivity.class);
                startActivity(intent);
            }
        });

        ViewRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdmViewRequestsActivity.class);
                startActivity(intent);
            }
        });

        AllAssetsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdmViewAllAssets.class);
                startActivity(intent);
            }
        });
        transferReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdmViewTransferActivity.class);;
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