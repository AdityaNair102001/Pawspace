package com.phoenixcorp.indiepaw;

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
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5600;
    Animation topAnim, bottomAnim;
    GifImageView gif;
    TextView appname, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);


        gif = findViewById(R.id.gifImageView);
        appname = findViewById(R.id.Pawspace);
        description = findViewById(R.id.textV);

        gif.setAnimation(topAnim);
        appname.setAnimation(bottomAnim);
        description.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(gif,"gifImageView2");
                pairs[1] = new Pair<View,String>(appname,"text_animation");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                startActivity(intent,options.toBundle());
                finish();
            }
        },SPLASH_SCREEN);



    }
}