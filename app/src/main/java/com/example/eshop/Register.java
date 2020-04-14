package com.example.eshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private Button CreateAccount;
    private EditText InputName, InputPhoneNum, InputPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccount = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_name_input);
        InputPhoneNum = (EditText) findViewById(R.id.register_phone_number);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount() {
        String name = InputName.getText().toString();
        String phoneNum = InputPhoneNum.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please Enter Your Name..", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(phoneNum)){
            Toast.makeText(this,"Please Enter Your Phone Number..", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please Enter Your Password..", Toast.LENGTH_SHORT).show();
        }else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, While we are cheking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNum(name,phoneNum,password);
        }
    }

    private void ValidatePhoneNum(final String name, final String phoneNum, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!(dataSnapshot.child("Users").child(phoneNum).exists())){

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone",phoneNum);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);

                    RootRef.child("Users").child(phoneNum).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Register.this,"Congratulations, Your account create successfully", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(Register.this, Login.class);
                                        startActivity(intent);
                                    }else {
                                        loadingBar.dismiss();
                                        Toast.makeText(Register.this,"Error please try again..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else{

                    Toast.makeText(Register.this,"This "+ phoneNum +" already exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(Register.this,"Please try again using anther phone number", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Register.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
