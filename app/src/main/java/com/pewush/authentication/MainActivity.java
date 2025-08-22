package com.pewush.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // No one is logged in, go to LoginActivity
            startActivity(new Intent(this, LoginPage.class));
            finish();
        } else {
            // Check whether it's Admin or User from Firestore
            String uid = currentUser.getUid();
            db.collection("Users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Boolean isAdmin = documentSnapshot.getBoolean("isAdmin");
                            Boolean isUser = documentSnapshot.getBoolean("isUser");

                            if (Boolean.TRUE.equals(isAdmin)) {
                                startActivity(new Intent(this, AdminDashboard.class));
                            } else if (Boolean.TRUE.equals(isUser)) {
                                startActivity(new Intent(this, UserDashboard.class));
                            } else {
                                startActivity(new Intent(this, LoginPage.class));
                            }
                        } else {
                            startActivity(new Intent(this, LoginPage.class));
                        }
                        finish(); // Close LauncherActivity
                    })
                    .addOnFailureListener(e -> {
                        // In case of error, go to login
                        startActivity(new Intent(this, LoginPage.class));
                        finish();
                    });
        }
    }
}