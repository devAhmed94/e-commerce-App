package com.example.ecommerceapp.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.model.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class ResetPasswordActivity extends AppCompatActivity {

    String check = "";
    TextView tv_title, tv_questions;
    EditText et_phone, et_question1, et_question2;
    Button btn_verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        check = getIntent().getStringExtra("check");
        tv_title = findViewById(R.id.reset_tv_title);
        tv_questions = findViewById(R.id.reset_password_text_answer);
        et_phone = findViewById(R.id.reset_et_phone);
        et_question1 = findViewById(R.id.reset_et_question1);
        et_question2 = findViewById(R.id.reset_et_question2);
        btn_verify = findViewById(R.id.reset_btn_verify);


    }

    @Override
    protected void onStart() {
        super.onStart();
        et_phone.setVisibility(View.GONE);
        if (check.equals("setting")) {
            tv_title.setText("Set Questions");
            tv_questions.setText("Please set answer for the following Security Question ?");
            btn_verify.setText("Set");
            displayAnswers();
            btn_verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswerQuestions();
                }
            });


        }

        else if (check.equals("login")) {
            et_phone.setVisibility(View.VISIBLE);
            btn_verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyMethod();
                }
            });

        }
    }

    private void verifyMethod() {
        String phone = et_phone.getText().toString().trim();
        String verify_answer1 = et_question1.getText().toString().toLowerCase().trim();
        String verify_answer2 = et_question2.getText().toString().toLowerCase().trim();

        if (phone.equals("") || verify_answer1.equals("") || verify_answer2.equals("")){
            Toast.makeText(this, "please complete the form.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(phone);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {


                        if(snapshot.hasChild("SecurityQuestions")){
                            String ans1 = snapshot.child("SecurityQuestions").child("answer1").getValue().toString();
                            String ans2 = snapshot.child("SecurityQuestions").child("answer2").getValue().toString();
                            if (!verify_answer1.equals(ans1)){
                                Toast.makeText(ResetPasswordActivity.this, "your answer 1 is wrong", Toast.LENGTH_SHORT).show();
                                return;
                            }else if (!verify_answer2.equals(ans2)){
                                Toast.makeText(ResetPasswordActivity.this, "your answer 2 is wrong", Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");
                                EditText newPassword =new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write password here....");
                                builder.setView(newPassword);
                                builder.setPositiveButton("change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(!newPassword.getText().toString().equals("")){
                                            reference.child("password").setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(ResetPasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                                                dialog.dismiss();
                                                                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));

                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });

                                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                       dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                        }else {
                            Toast.makeText(ResetPasswordActivity.this, " you have not verification for your password", Toast.LENGTH_SHORT).show();
                        }


                }else {
                    Toast.makeText(ResetPasswordActivity.this, " the user not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAnswerQuestions() {
        String answer1 = et_question1.getText().toString().toLowerCase();
        String answer2 = et_question2.getText().toString().toLowerCase();
        if (answer1.equals("") && answer2.equals("")) {
            Toast.makeText(this, "answers is empty", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(Prevalent.onlineUser.getPhone());

            HashMap<String, Object> map = new HashMap<>();
            map.put("answer1", answer1);
            map.put("answer2", answer2);
            reference.child("SecurityQuestions").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, "done", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ResetPasswordActivity.this, HomeActivity.class));
                    }
                }
            });
        }
    }

    private void displayAnswers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").
                child(Prevalent.onlineUser.getPhone()).child("SecurityQuestions");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ans1 = snapshot.child("answer1").getValue().toString();
                    String ans2 = snapshot.child("answer2").getValue().toString();
                    et_question1.setText(ans1);
                    et_question2.setText(ans2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}