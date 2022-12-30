package com.example.medicare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.medicare.model.UserSignIn;
import com.example.medicare.model.UserSignUp;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class UserProfile extends AppCompatActivity {

    public static final String TAG = "TAG"; //*
    EditText fullName1, username1, email1, phone1, address1, password1;
    String fullN, userN, em, pho, add, pass;
    String userID;
    Button btnSave, btnEditProfile, btnDeleteProfile, btnCancel;
    ProgressBar progressBar;

    DatabaseReference reference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent data = getIntent();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User");
        userID = firebaseUser.getUid();

        fullName1 = findViewById(R.id.fullName1);
        username1 = findViewById(R.id.username1);
        email1 = findViewById(R.id.email1);
        phone1 = findViewById(R.id.phone1);
        address1 = findViewById(R.id.address1);
        password1 = findViewById(R.id.password1);
        btnSave = findViewById(R.id.btnSave);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnDeleteProfile = findViewById(R.id.btnDeleteProfile);
        btnCancel = findViewById(R.id.btnCancel);


        //Show user profile
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserSignUp userProfile = snapshot.getValue(UserSignUp.class);

                        if (userProfile != null) {
                            fullN = userProfile.getFullName();
                            userN = userProfile.getUsername();
                            em = userProfile.getEmail();
                            pho = userProfile.getPhone();
                            add = userProfile.getAddress();
                            pass = userProfile.getPassword();

                            fullName1.setText(fullN);
                            username1.setText(userN);
                            email1.setText(em);
                            phone1.setText(pho);
                            address1.setText(add);
                            password1.setText(pass);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(UserProfile.this, "Cannot retrieve user profile", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        //Update user profile
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (isFullNameChanged() || isUsernameChanged() || isEmailChanged() || isPhoneChanged() || isAddressChanged() || isPasswordChanged()) {
                            Toast.makeText(UserProfile.this, "Data has been updated", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(UserProfile.this, "Data is same and cannot be updated", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });


        //Delete user profile
        btnDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email1.setText(firebaseUser.getEmail());

                AlertDialog.Builder dialog = new AlertDialog.Builder(UserProfile.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will completely remove your account from this app");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                FirebaseDatabase.getInstance().getReference().child("User")
                                        .child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(UserProfile.this, "Account deleted", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(UserProfile.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();

            }
        });

        //Cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancel = new Intent(UserProfile.this, Home.class);
                startActivity(cancel);
                finish();
            }
        });


    }

    //Edit profile method
    private boolean isPasswordChanged() {
        if (!pass.equals(password1.getText().toString())) {

            reference.child(userID).child("password").setValue(password1.getText().toString());
            pass = password1.getText().toString();
            firebaseUser.updatePassword(pass);
            return true;
        } else {
            return false;
        }
    }

    private boolean isAddressChanged() {
        if (!add.equals(address1.getText().toString())) {

            reference.child(userID).child("address").setValue(address1.getText().toString());
            add = address1.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isPhoneChanged() {
        if (!pho.equals(phone1.getText().toString())) {

            reference.child(userID).child("phone").setValue(phone1.getText().toString());
            pho = phone1.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmailChanged() {
        if (!em.equals(email1.getText().toString())) {

            reference.child(userID).child("email").setValue(email1.getText().toString());
            em = email1.getText().toString();
            firebaseUser.updateEmail(em);
            return true;

        } else {
            return false;
        }
    }

    private boolean isUsernameChanged() {
        if (!userN.equals(username1.getText().toString())) {

            reference.child(userID).child("username").setValue(username1.getText().toString());
            userN = username1.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isFullNameChanged() {
        if (!fullN.equals(fullName1.getText().toString())) {

            reference.child(userID).child("fullName").setValue(fullName1.getText().toString());
            fullN = fullName1.getText().toString();
            return true;
        } else {
            return false;
        }
    }

}
