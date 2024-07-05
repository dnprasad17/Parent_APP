package com.example.parent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class verify_phone extends AppCompatActivity {
    private FirebaseAuth mAuth;


    String verificationmanual;
    Button verify_btn;
    EditText phoneNoEnt;
    ProgressBar progressBar;
    String phone_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        mAuth = FirebaseAuth.getInstance();
        //Hooks
        verify_btn = findViewById(R.id.verify_btn);
        phoneNoEnt = findViewById(R.id.verification_code_entered_by_user);
        progressBar = findViewById(R.id.progress_bar);


        progressBar.setVisibility(View.GONE);

        //get phone number
        phone_number = getIntent().getStringExtra("mobile");

        sendVerifaction(phone_number);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code= phoneNoEnt.getText().toString();
                if(code.isEmpty() || code.length()<6){
                    phoneNoEnt.setError("WRONG OTP");
                    phoneNoEnt.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });
    }

    private void sendVerifaction(String phone_number) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + phone_number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationmanual = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(verify_phone.this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String verificationCode){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationmanual,verificationCode);
        signinByCred(credential);
    }


    private void signinByCred(PhoneAuthCredential credential){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(verify_phone.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                           //initializing attributes of a student
                            userHelperClass user=new userHelperClass();

                            FirebaseDatabase.getInstance().getReference("students")
                                    .child(phone_number)
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                    }else{
                                        Toast.makeText(verify_phone.this,task.getException().getMessage(),Toast.LENGTH_SHORT);
                                    }
                                }
                            });

                            //adding days field explicitly
                            FirebaseDatabase.getInstance().getReference("students")
                                    .child(phone_number).child("days")
                                    .setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                                        Toast.makeText(verify_phone.this, "Account Creation Successful", Toast.LENGTH_SHORT).show();
                                        intent.putExtra("mobile", phone_number);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(verify_phone.this,task.getException().getMessage(),Toast.LENGTH_SHORT);
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(verify_phone.this,task.getException().getMessage(),Toast.LENGTH_SHORT);
                        }
                    }
                });
    }
}