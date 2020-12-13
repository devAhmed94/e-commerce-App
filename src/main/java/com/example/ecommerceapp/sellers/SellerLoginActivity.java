package com.example.ecommerceapp.sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {

    Button seller_login_btn;
    EditText et_email, et_password;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);
        progressDialog = new ProgressDialog(SellerLoginActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();
        seller_login_btn = findViewById(R.id.seller_login_btn_login);
        et_email = findViewById(R.id.seller_login_et_email);
        et_password = findViewById(R.id.seller_login_et_password);
        seller_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginMethod();
            }
        });
    }

    private void loginMethod() {
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if (!email.equals("") && !password.equals("")) {
            progressDialog.setTitle("Creation");
            progressDialog.setMessage("wait for register your account ....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(SellerLoginActivity.this, "done successful", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(SellerLoginActivity.this,SellerHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(SellerLoginActivity.this, "not successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        } else {
            Toast.makeText(this, "plz complete your information", Toast.LENGTH_SHORT).show();
        }
    }
}