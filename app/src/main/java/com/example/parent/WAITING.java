package com.example.parent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WAITING extends AppCompatActivity {
    public Button button;
    public Button button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        button=findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WAITING.this, "The Admin has been notified", Toast.LENGTH_SHORT).show();
            }
        });

        button2=findViewById(R.id.button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), phone_entry.class);
                startActivity(intent);
            }
        });


    }

}
