package com.example.tccproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refVehicle = database.getReference("MOTOR_STATUS").child("Motor_Veiculo");
    DatabaseReference refPerson = database.getReference("MOTOR_STATUS").child("Motor_Pedestre");

    Button signOutButton, vehicleButton, peopleButton, vehicleCloseButton;

    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView currentUserTV = findViewById(R.id.currentUserTV);

        signOutButton = findViewById(R.id.signOutButton);
        vehicleButton = findViewById(R.id.vehicleButton);
        peopleButton = findViewById(R.id.peopleButton);
        vehicleCloseButton = findViewById(R.id.vehicleCloseButton);

        mAuth = FirebaseAuth.getInstance();

        //String currentUserAux = mAuth.getCurrentUser().getEmail();

        //currentUserTV.setText(currentUserAux);

        vehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refVehicle.setValue(1);

            }
        });

        vehicleCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refVehicle.setValue(3);
            }
        });

        peopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refPerson.setValue(1);
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                }
            }
        };

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

    }
}
