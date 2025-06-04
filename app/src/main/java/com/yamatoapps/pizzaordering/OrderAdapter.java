package com.yamatoapps.pizzaordering;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderAdapter extends ArrayAdapter<Order> {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public OrderAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Order> orders) {
        super(context, 0, orders);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Order item = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_item, parent, false);
        }
        TextView tvName,tvDateOrdered,tvPrice;
        ImageView ivPhoto;
        Button btnConfirm;
        ivPhoto = convertView.findViewById(R.id.ivPhoto);
        tvName = convertView.findViewById(R.id.tvName);
        tvDateOrdered = convertView.findViewById(R.id.tvDateOrdered);
        tvPrice = convertView.findViewById(R.id.tvPrice);
        btnConfirm = convertView.findViewById(R.id.btnConfirm);

        Picasso.get().load(item.image_url).into(ivPhoto);
        tvName.setText(item.pizza_name);
        tvDateOrdered.setText(item.date_ordered.toString());
        tvPrice.setText(item.total.toString());

        btnConfirm.setOnClickListener(view -> {
            Map<String,Object> updated_order = new HashMap<String, Object>();
            db.collection("pizza_orders").document(item.id).update("status","confirmed").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(parent.getContext());
                    alertDialogBuilder.setTitle("Success");
                    alertDialogBuilder.setMessage("Order has been successfully confirmed!");
                }
            });
        });
        return  convertView;
    }
}
