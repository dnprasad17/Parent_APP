package com.example.parent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class phone_entry extends AppCompatActivity {
    public TextInputLayout phone_number;
    Button proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_entry);

        //Hook
        proceed = findViewById(R.id.button_1);
        phone_number = findViewById(R.id.phone);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneString= phone_number.getEditText().getText().toString().trim();
                if(phoneString.isEmpty()){
                    phone_number.setError("Phone Number Required");
                    phone_number.requestFocus();
                }
                else if(phoneString.length()<10 || phoneString.length()>10){
                    phone_number.setError("Enter Valid Phone Number");
                    phone_number.requestFocus();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), verify_phone.class);
                    intent.putExtra("mobile", phoneString);
                    startActivity(intent);
                }
            }
        });
    }
}