package com.yamatoapps.pizzaordering;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Catalogue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);
        Button btnBack = findViewById(R.id.btnBack);
        GridView gvPizza = findViewById(R.id.gvPizzaCatalogue);
        ArrayList<Pizza> itemsArrayList= new ArrayList<Pizza>();
        PizzaAdapter adapter = new PizzaAdapter(this, itemsArrayList);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pizzas").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    adapter.add(new Pizza(document.getString("name"),
                            document.getString("details"),
                            Double.parseDouble(document.get("price").toString()),
                            document.getString("image_url"),
                            document.getId()));
                }
                gvPizza.setAdapter(adapter);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}