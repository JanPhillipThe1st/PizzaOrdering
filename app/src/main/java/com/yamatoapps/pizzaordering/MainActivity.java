package com.yamatoapps.pizzaordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnLoginCustomer  = findViewById(R.id.btnLoginCustomer);
        TextInputEditText tiUsername = findViewById(R.id.tiUsername), tiPassword = findViewById(R.id.tiPassword);
        btnLoginCustomer.setOnClickListener(view ->{
            ProgressDialog progressDialog= new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Logging in");
            progressDialog.setMessage("Logging in...");
            progressDialog.show();
            //Code for checking on the user type
            FirebaseFirestore db =  FirebaseFirestore.getInstance();
            //Database query
            db.collection("pizza_ordering_users").where(Filter.and(
                    Filter.equalTo("username",tiUsername.getText().toString()),
                    Filter.equalTo("password",tiPassword.getText().toString()),Filter.equalTo("type","admin"))).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if ( queryDocumentSnapshots.size() > 0){
                        progressDialog.dismiss();
                        startActivity(new Intent(MainActivity.this, Admin.class));
                        Toast.makeText(MainActivity.this,"Logged in as Admin",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        db.collection("pizza_ordering_users").where(Filter.and(
                                Filter.equalTo("username",tiUsername.getText().toString()),
                                Filter.equalTo("password",tiPassword.getText().toString()),Filter.equalTo("type","customer")
                        )).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshotsCustomer) {
                                if ( queryDocumentSnapshotsCustomer.getDocuments().size()>0){
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this,"Logged in as Customer",Toast.LENGTH_SHORT).show();
                                    Intent customerIntent = new Intent(MainActivity.this, Catalogue.class);
                                    customerIntent.putExtra("profile_picture",queryDocumentSnapshotsCustomer.getDocuments().get(0).getString("profile_picture"));
                                    customerIntent.putExtra("id",queryDocumentSnapshotsCustomer.getDocuments().get(0).getId());
                                    customerIntent.putExtra("name",queryDocumentSnapshotsCustomer.getDocuments().get(0).getString("name"));
                                    customerIntent.putExtra("old_password",queryDocumentSnapshotsCustomer.getDocuments().get(0).getString("password"));
                                    startActivity( customerIntent);
                                }
                                else {
                                    AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                                    progressDialog.dismiss();
                                    ab.setTitle("Invalid Login");
                                    ab.setMessage("No user could be found");
                                    ab.setNegativeButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {

                                    });
                                    AlertDialog alertDialog = ab.create();
                                    alertDialog.show();
                                }
                            }

                        });
                    }
                }
            });
        });
    }
}