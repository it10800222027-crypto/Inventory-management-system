package com.pewush.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword,editTextname;
    FirebaseAuth mAuth;
    Button buttonReg;
    ProgressBar progressBar;
    TextView textView;
    Spinner roleSpinner;
    String selectedRole;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        editTextEmail=findViewById(R.id.email);
        mAuth=FirebaseAuth.getInstance();
        editTextPassword=findViewById(R.id.password);
        buttonReg=findViewById(R.id.btn_signUp);
        progressBar=findViewById(R.id.progressBar);
        textView=findViewById(R.id.loginNow);
        editTextname=findViewById(R.id.name);
        roleSpinner = findViewById(R.id.roleSpinner);
        fStore=FirebaseFirestore.getInstance();




        String[] roles = {"Select Role", "User"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.custom_spinner_item, // Custom layout we'll define
                roles
        );

        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        roleSpinner.setAdapter(adapter);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
                finish();
            }
        });
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password, fullName;
                email= String.valueOf(editTextEmail.getText());
                password=String.valueOf(editTextPassword.getText());
                fullName=String.valueOf(editTextname.getText());
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(register.this, "Enter email",Toast.LENGTH_SHORT).show();
                    return;
                }if(TextUtils.isEmpty(password)){
                    Toast.makeText(register.this, "Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }if(TextUtils.isEmpty(fullName)) {
                    Toast.makeText(register.this, "Enter Your Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(register.this, "Account Created.", Toast.LENGTH_SHORT).show();
                                    DocumentReference df = fStore.collection("Users").document(user.getUid());
                                    Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                                    Map<String,Object> userInfo = new HashMap<>();
                                    selectedRole = roleSpinner.getSelectedItem().toString();
                                    userInfo.put("FullName",fullName);
                                    userInfo.put("userEmail",email);
                                    //specify if the user is Admin
                                    if("User".equals(selectedRole)){
                                        userInfo.put("isUser",true);
                                    }
                                    df.set(userInfo);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}