package com.pewush.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginPage extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    FirebaseAuth mAuth;
    Button buttonLogin;
    ProgressBar progressBar;
    TextView textView;
    FirebaseFirestore fStore;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("Users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Boolean isAdmin = documentSnapshot.getBoolean("isAdmin");
                            if (isAdmin != null && isAdmin) {
                                // Admin logged in
                                Intent intent = new Intent(getApplicationContext(), AdminDashboard.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Regular user logged in
                                Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Failed to fetch user role", Toast.LENGTH_SHORT).show();
                    });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        editTextEmail=findViewById(R.id.email);
        mAuth=FirebaseAuth.getInstance();
        editTextPassword=findViewById(R.id.password);
        buttonLogin=findViewById(R.id.btn_login);
        progressBar=findViewById(R.id.progressBar);
        textView=findViewById(R.id.registerNow);
        fStore=FirebaseFirestore.getInstance();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), register.class);
                startActivity(intent);
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email=String.valueOf(editTextEmail.getText());
                password=String.valueOf(editTextPassword.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginPage.this, "Enter email",Toast.LENGTH_SHORT).show();
                    return;
                }if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginPage.this, "Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    AuthResult authResult = task.getResult();
                                    Toast.makeText(getApplicationContext(), "Login Successful.", Toast.LENGTH_SHORT).show();
                                    checkUserAccessLevel(authResult.getUser().getUid());
                                } else {
                                    Toast.makeText(LoginPage.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
    private void checkUserAccessLevel(String Uid){
        DocumentReference df = fStore.collection("Users").document(Uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG","onSuccess"+documentSnapshot.getData());

                Boolean isAdmin = documentSnapshot.getBoolean("isAdmin");
                Boolean isUser = documentSnapshot.getBoolean("isUser");

                if (isAdmin != null && isAdmin) {
                    Intent intent = new Intent(getApplicationContext(), AdminDashboard.class);
                    startActivity(intent);
                    finish();
                }

                if (isUser != null && isUser) {
                    Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}