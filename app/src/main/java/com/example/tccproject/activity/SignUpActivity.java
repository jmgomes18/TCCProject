package com.example.tccproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, passwordInput, cargoInput;
    private FirebaseAuth mAuth;
    private static final String TAG = "";
    private static final String DATA = MoradorActivity.class.getSimpleName();
    private ProgressBar progressBar;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        mFirebaseInstance.getReference("app_title").setValue("Realtime Database");

        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(DATA, "App title updated");

                String appTitle = dataSnapshot.getValue(String.class);
                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(DATA, "App title updated");
            }
        });


        TextView alreadySigned = findViewById(R.id.alreadyLogged);

        alreadySigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        nameInput = findViewById(R.id.inputName);
        cargoInput = findViewById(R.id.inputCargo);
        emailInput = findViewById(R.id.inputEmail);
        progressBar = findViewById(R.id.progressBar);
        passwordInput = findViewById(R.id.inputPassword);
        Button signUpButton = findViewById(R.id.signUpButton);

        alreadySigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameInput.getText().toString();
                final String email = emailInput.getText().toString();
                final String password = passwordInput.getText().toString();
                final String cargo = cargoInput.getText().toString();

                if(TextUtils.isEmpty(name)) {
                    nameInput.setError("Enter a valid name");
                    Toast.makeText(getApplicationContext(), "Enter user name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailInput.setError("Enter a valid email address");
                    Toast.makeText(getApplicationContext(), "Enter email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setBackgroundColor(Color.CYAN);
                progressBar.setVisibility(View.VISIBLE);
                int progressValue = progressBar.getProgress();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if(task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            User user = new User(name, email, password, cargo);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "SUCCESS", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(SignUpActivity.this, "FAILED TO CREATE USER", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

    }

}
