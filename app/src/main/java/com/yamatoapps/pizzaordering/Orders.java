package com.yamatoapps.pizzaordering;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Orders extends AppCompatActivity {
    FirebaseFirestore db =  FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        GridView gvOrders = findViewById(R.id.gvOrders);
        ArrayList<Order> orderArrayList = new ArrayList<Order>();
        OrderAdapter adapter = new OrderAdapter(Orders.this,0,orderArrayList);
        db.collection("pizza_orders").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for  (DocumentSnapshot pizzaDocument : queryDocumentSnapshots ){
                adapter.add(new Order(
                        pizzaDocument.getString("item_name"),
                        pizzaDocument.getDate("date_ordered", DocumentSnapshot.ServerTimestampBehavior.ESTIMATE),
                        1,
                        pizzaDocument.getDouble("total"),
                        pizzaDocument.getId(),
                        pizzaDocument.getString("image_url")

                ));
            }
            gvOrders.setAdapter(adapter);
        });
    }
}