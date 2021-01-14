package com.example.trawell;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    EditText fname,lname,email,password,address;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        fname = findViewById(R.id.edtfirstname);
        lname = findViewById(R.id.edtlastname);
        email = findViewById(R.id.edtLoginEmail);
        password = findViewById(R.id.edtloginpassword);
        address = findViewById(R.id.address);
        db = FirebaseFirestore.getInstance();
        findViewById(R.id.btnlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signup(fname.getText().toString(),lname.getText().toString(),email.getText().toString(),password.getText().toString(),address.getText().toString());
            }
        });
    }

    private void signup(String firstname, String lastname, String email, String password, String address) {
        Map<String, Object> user = new HashMap<>();
        user.put("firstname", firstname);
        user.put("lastname", lastname);
        user.put("email", email);
        user.put("password", password);
        user.put("address", address);


// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(RegistrationActivity.this, "Registration Completed", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrationActivity.this, "Error in Registration", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}