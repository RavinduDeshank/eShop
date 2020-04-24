package com.example.eshop.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eshop.Home;
import com.example.eshop.MainActivity;
import com.example.eshop.R;
import com.example.eshop.Sellers.SellerHomeActivity;

public class AdminHome extends AppCompatActivity {

    private Button logOutbtn,checkOrderbtn,maintainProductbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logOutbtn =(Button) findViewById(R.id.admin_logout);
        checkOrderbtn = (Button)findViewById(R.id.admin_cheak_new);
        maintainProductbtn =(Button) findViewById(R.id.admin_maintain_product);

        maintainProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, Home.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });
        logOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        checkOrderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, SellerHomeActivity.class);
                startActivity(intent);

                //need to add admin new order activity
            }
        });

    }
}
