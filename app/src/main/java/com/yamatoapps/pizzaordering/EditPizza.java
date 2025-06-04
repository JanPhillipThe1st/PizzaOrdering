package com.yamatoapps.pizzaordering;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditPizza extends AppCompatActivity {
    Uri fileUri;
    TextView tvName,tvPrice,tvDetails;
    Button btnViewOrders, btnManageMenu;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String document_id = "";
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pizza);
        tvName = findViewById(R.id.textInputName);
        tvPrice = findViewById(R.id.textInputPrice);
        tvDetails = findViewById(R.id.textInputDetails);
        Button btnPost = findViewById(R.id.btnPost) ;
        document_id = getIntent().getStringExtra("document_id");
        iv = findViewById(R.id.ivListingPhoto);

        db.collection("pizzas").document(document_id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Picasso.get().load(documentSnapshot.getString("image_url")).into(iv);
                    tvName.setText(documentSnapshot.getString("name"));
                    tvPrice.setText(documentSnapshot.getDouble("price").toString());
                    tvDetails.setText(documentSnapshot.getString("details"));
                });

        Button btnUploadImage = findViewById(R.id.btnUploadImage);
        Intent imageIntent = new Intent();
        btnUploadImage.setOnClickListener(view ->{
            imageIntent.setType("image/*");
            imageIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(imageIntent,"Pick image to upload"),22);
        });
        btnPost.setOnClickListener(view ->{
            uploadImage();
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data != null) {
            fileUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),fileUri);
                iv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public  void uploadImage(){
        if (fileUri != null){
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.setMessage("Adding your product...");
            progressDialog.show();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child(UUID.randomUUID().toString());
            UploadTask uploadTask = (UploadTask) storageReference.putFile(fileUri).addOnSuccessListener(taskSnapshot -> {

            }).addOnFailureListener(listener->{
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Fail to Upload Image..", Toast.LENGTH_SHORT)
                        .show();
            });
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Map<String, Object> listing = new HashMap<>();
                        listing.put("name", tvName.getText().toString());
                        listing.put("details",  tvDetails.getText().toString());
                        listing.put("price",  Double.parseDouble(tvPrice.getText().toString()));
                        listing.put("image_url",  task.getResult());

                        progressDialog.dismiss();
                        db.collection("pizzas").document(document_id)
                                .update(listing).addOnSuccessListener(unused -> {
                                    finish();
                                });
                        Toast.makeText(getApplicationContext(), "Congratulations! your Product is added..", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }
    }
}