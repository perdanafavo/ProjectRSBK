package com.androidtan.favoperdana.androidngunyah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidtan.favoperdana.androidngunyah.Common.Common;
import com.androidtan.favoperdana.androidngunyah.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnSignIn;
    FirebaseDatabase database;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        setVariable();
        createView();
    }

    private void createView() {
        //init Firebase
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("user");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    final ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
                    mDialog.setMessage("Please waiting...");
                    mDialog.show();

                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //Check if user not exist in database
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {

                                //Get user information
                                mDialog.dismiss();
                                User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                                user.setPhone(edtPhone.getText().toString()); //Set Phone
                                if (user.getPassword().equals(edtPassword.getText().toString())) {
                                    {
                                        Intent homeIntent = new Intent(SignInActivity.this, HomeActivity.class);
                                        Common.currentUser = user;
                                        startActivity(homeIntent);
                                        finish();

                                        table_user.removeEventListener(this);
                                    }
                                } else {
                                    Toast.makeText(SignInActivity.this, "Wrong password !!!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(SignInActivity.this, "User not exist in Database", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(SignInActivity.this, "please check your connection !!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setVariable() {
        edtPassword = findViewById(R.id.edtPassword);
        edtPhone = findViewById(R.id.edtPhone);
        btnSignIn = findViewById(R.id.btnSignIn);
    }
}
