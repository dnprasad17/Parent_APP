package com.example.parent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3500;

    //variables

    Animation topAnim,bottomAnim;
    ImageView image;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animation
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        //Hooks
        image = findViewById(R.id.schoolkid_1);
        text = findViewById(R.id.p_a_r_e_n_t);

        //Asign_Animation

        image.setAnimation(topAnim);
        text.setAnimation(bottomAnim);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            Intent intent = new Intent(getApplicationContext(), WAITING.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, phone_entry.class);
                    Pair[] pairs = new Pair[1];
                    pairs[0] = new Pair<View, String>(image, "logo");

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }, SPLASH_SCREEN);
        }
    }
}
