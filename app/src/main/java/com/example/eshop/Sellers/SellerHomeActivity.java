package com.example.eshop.Sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eshop.MainActivity;
import com.example.eshop.R;

public class SellerHomeActivity extends AppCompatActivity
{
    private Button addProduct, sellerLogout;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        addProduct = (Button) findViewById(R.id.seller_add_product);
        sellerLogout = (Button) findViewById(R.id.seller_logout);
        loadingBar = new ProgressDialog(this);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerHomeActivity.this, SellerProductCategory.class);
                startActivity(intent);
            }
        });

        sellerLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(SellerHomeActivity.this, MainActivity.class);
                startActivity(intent2);
            }
        });

    }
}
