package com.example.eshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.eshop.Model.Products;
import com.example.eshop.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Product_Details extends AppCompatActivity {
    //private FloatingActionButton addToCartbtn;
    private Button addToCartButton;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice,productDescription,productName;

    private String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");

        addToCartButton = (Button)findViewById(R.id.pd_add_to_cat_btn);
        //addToCartbtn = (FloatingActionButton) findViewById(R.id.add_product_to_cart);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        productImage = (ImageView)findViewById(R.id.product_image_details);
        productPrice = (TextView)findViewById(R.id.product_price_details);
        productDescription = (TextView)findViewById(R.id.product_description_details);
        productName = (TextView)findViewById(R.id.product_name_details);


        getProductDetails(productID);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addingToCartList();
            }
        });
    }

    private void addingToCartList() {
        String saveCurrentDate,saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat curretDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = curretDate.format(calForDate.getTime());

        SimpleDateFormat curretTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = curretTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){

                                                Toast.makeText(Product_Details.this,"Add to the Cart",Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(Product_Details.this,Home.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void getProductDetails(final String productID) {

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
