package com.yamatoapps.pizzaordering;

import android.app.Activity;
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

public class MenuItemAdapter extends ArrayAdapter<Pizza> {
    public MenuItemAdapter(@NonNull Context context, ArrayList<Pizza> pizzaArrayList) {
        super(context, 0, pizzaArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pizza item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_item, parent, false);
        }
        ImageView image = (ImageView)convertView.findViewById(R.id.ivPhoto);
        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        TextView tvPrice = (TextView)convertView.findViewById(R.id.tvPrice);
        TextView tvDetails = (TextView)convertView.findViewById(R.id.tvDetails);

        Button btnEdit = convertView.findViewById(R.id.btnEdit);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);
        Intent placeOrderIntent = new Intent(parent.getContext(), OrderConfirmed.class);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Picasso.get().load(item.image_url).into(image);
        tvName.setText("Name: "+ item.name);
        tvPrice.setText("Price: "+ item.price);
        tvDetails.setText("Additional Details: "+ item.details);

        btnEdit.setOnClickListener(view -> {
            Intent editIntent = new Intent(parent.getContext(), EditPizza.class);
            editIntent.putExtra("document_id",item.id);
            parent.getContext().startActivity(editIntent);
        });

        btnDelete.setOnClickListener(view ->{
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(parent.getContext());
            alertDialogBuilder.setTitle("Delete Product");
            alertDialogBuilder.setMessage("Are you sure you want to delete this product?");
            alertDialogBuilder.setPositiveButton("NO", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            alertDialogBuilder.setNegativeButton("YES", (dialogInterface, i) -> {

                MaterialAlertDialogBuilder deleteDialogBuilder = new MaterialAlertDialogBuilder(parent.getContext());
                deleteDialogBuilder.setTitle("Delete success");
                deleteDialogBuilder.setMessage("Product deleted successfully!");
                deleteDialogBuilder.setPositiveButton("OK", (deleteDialogBuilderDialogInterface,j)->{
                    deleteDialogBuilderDialogInterface.dismiss();
                    Activity context = (Activity) parent.getContext();
                });
                db.collection("pizzas").document(item.id).delete().addOnSuccessListener(unused -> {
                    deleteDialogBuilder.create().show();
                    dialogInterface.dismiss();
                });
            });
            alertDialogBuilder.create().show();
        });

        return convertView;
    }
}
