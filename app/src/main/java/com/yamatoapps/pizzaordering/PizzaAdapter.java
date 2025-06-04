package com.yamatoapps.pizzaordering;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PizzaAdapter extends ArrayAdapter<Pizza> {
    public PizzaAdapter(@NonNull Context context, ArrayList<Pizza> pizzaArrayList) {
        super(context, 0, pizzaArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pizza item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_item, parent, false);
        }

        ImageView image = (ImageView)convertView.findViewById(R.id.ivPhoto);
        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        TextView tvPrice = (TextView)convertView.findViewById(R.id.tvPrice);
        TextView tvDetails = (TextView)convertView.findViewById(R.id.tvDetails);

        Button btnPlaceOrder = convertView.findViewById(R.id.btnPlaceOrder);
        Button btnSeeDetails = convertView.findViewById(R.id.btnSeeDetails);

        Intent placeOrderIntent = new Intent(parent.getContext(), OrderConfirmed.class);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        btnPlaceOrder.setOnClickListener(view -> {
            placeOrderIntent.putExtra("document_id",item.id);
            Map<String,Object> pizza_order = new HashMap<String, Object>();
            pizza_order.put("item_name",item.name);
            pizza_order.put("item_price",item.price);
            pizza_order.put("item_details",item.details);
            pizza_order.put("quantity",1);
            pizza_order.put("total",item.price);
            pizza_order.put("image_url",item.image_url);
            pizza_order.put("date_ordered", Calendar.getInstance().getTime());
            ProgressDialog progressDialog  = new ProgressDialog(parent.getContext());
            progressDialog.setTitle("Processing Order");
            progressDialog.setMessage("Processing your order. Please wait.");
            progressDialog.show();


            db.collection("pizza_orders").add(pizza_order)
                    .addOnSuccessListener(documentReference -> {
                        progressDialog.dismiss();
                        placeOrderIntent.putExtra("order_id",documentReference.getId());
                        parent.getContext().startActivity(placeOrderIntent);
                    });
        });



        Picasso.get().load(item.image_url).into(image);
        Log.e("Image","Successfully loaded image!" + image.getResources());
        tvName.setText("Name: "+ item.name);
        tvPrice.setText("Price: "+ item.price);
        tvDetails.setText("Additional Details: "+ item.details);
        return convertView;
    }
}
