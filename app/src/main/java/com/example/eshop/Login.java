package com.example.eshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eshop.Model.Users;
import com.example.eshop.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {

    private EditText InputPhoneNum,InputPassword;
    private Button Loginbtn;
    private ProgressDialog loadingBar;
    private TextView AdminLink,NotAdmin;

    private String parentDbName = "Users";
    private CheckBox chBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Loginbtn = (Button)findViewById(R.id.login_btn);
        InputPhoneNum = (EditText)findViewById(R.id.login_phone_number);
        InputPassword = (EditText)findViewById(R.id.login_password_input);
        AdminLink = (TextView)findViewById(R.id.admin_panel_link);
        NotAdmin = (TextView)findViewById(R.id.not_admin_panel_link);

        loadingBar = new ProgressDialog(this);

        chBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginbtn.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdmin.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });
        NotAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginbtn.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdmin.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }

    private void LoginUser() {

        String phoneNum = InputPhoneNum.getText().toString();
        String password = InputPassword.getText().toString();

         if(TextUtils.isEmpty(phoneNum)){
            Toast.makeText(this,"Please Enter Your Phone Number..", Toast.LENGTH_SHORT).show();
         }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please Enter Your Password..", Toast.LENGTH_SHORT).show();
         }
         else{

             loadingBar.setTitle("Create Account");
             loadingBar.setMessage("Please wait, While we are cheking the credentials");
             loadingBar.setCanceledOnTouchOutside(false);
             loadingBar.show();

             AllowAccessToAccount(phoneNum,password);
         }
    }

    private void AllowAccessToAccount(final String phoneNum, final String password){

        if(chBoxRememberMe.isChecked() ){
            Paper.book().write(Prevalent.UserPhoneKey,phoneNum);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(parentDbName).child(phoneNum).exists()){

                    Users userData = dataSnapshot.child(parentDbName).child(phoneNum).getValue(Users.class);

                    if(userData.getPhone().equals(phoneNum)){

                        if(userData.getPassword().equals(password)){

                            if (parentDbName.equals("Admins")){

                                Toast.makeText(Login.this,"Welcome Admin",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(Login.this, AdminCategory.class);
                                startActivity(intent);

                            }else if(parentDbName.equals("Users")){

                                Toast.makeText(Login.this,"Welcome to eShop",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(Login.this, Home.class);
                                startActivity(intent);
                            }

                        }else{

                            loadingBar.dismiss();
                            Toast.makeText(Login.this,"Password is Incorrect",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(Login.this,"Account with this "+ phoneNum +" Number do not exists",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
