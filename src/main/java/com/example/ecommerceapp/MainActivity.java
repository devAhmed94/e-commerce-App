package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ecommerceapp.model.Prevalent;
import com.example.ecommerceapp.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button btn_login,btn_join;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_login = findViewById(R.id.main_already_btn);
        btn_join = findViewById(R.id.main_join_btn);
        Paper.init(this);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
        String phoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String passwordKey = Paper.book().read(Prevalent.UserPasswordKey);
        if (phoneKey != "" && passwordKey != ""){
            if(!TextUtils.isEmpty(phoneKey) && !TextUtils.isEmpty(passwordKey)){
                allowAccess(phoneKey,passwordKey);
            }
        }

    }

    private void allowAccess(String phone, String password) {
        DatabaseReference dataReference = FirebaseDatabase.getInstance().getReference();
        dataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").child(phone).exists()){
                    Users user = snapshot.child("Users").child(phone).getValue(Users.class);
                    if(user.getPhone().equals(phone)){
                        if (user.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this, "wait, you are already login", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                        }else{
                            Toast.makeText(MainActivity.this, "your password incorrect", Toast.LENGTH_LONG).show();
                        }
                    }

                }else {
                    Toast.makeText(MainActivity.this, "not exist this phone number", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}