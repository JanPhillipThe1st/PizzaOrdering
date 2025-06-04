package com.yamatoapps.pizzaordering;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class OrderConfirmed extends AppCompatActivity {
    String item_id = "", order_id = "";
    Button btnExit;
    TextView tvPizzaName,tvPizzaPrize,tvOrderTotal;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmed);

        btnExit = findViewById(R.id.btnExit);
        tvPizzaName = findViewById(R.id.tvPizzaName);
        tvPizzaPrize = findViewById(R.id.tvPizzaPrize);
        tvOrderTotal = findViewById(R.id.tvOrderTotal);
        item_id = getIntent().getStringExtra("document_id");
        order_id = getIntent().getStringExtra("order_id");

        db.collection("pizza_orders").document(order_id).get().addOnSuccessListener(documentSnapshot -> {
            tvPizzaName.setText(documentSnapshot.getString("item_name"));
            tvPizzaPrize.setText(documentSnapshot.getDouble("item_price").toString());
            tvOrderTotal.setText(documentSnapshot.getDouble("total").toString());
        });

        btnExit.setOnClickListener(view -> {
            finish();
        });


    }
}