package com.phoenixcorp.indiepaw;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends AppCompatActivity {
    Button signup,login;
    GifImageView gif;
    TextView signinText,greetings;
    TextInputLayout email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = findViewById(R.id.SignUp);
        login = findViewById(R.id.Login);
        gif = findViewById(R.id.gifImageView);
        signinText = findViewById(R.id.textView2);
        greetings = findViewById(R.id.Greetings);
        email = findViewById(R.id.outlinedTextField2);
        password = findViewById(R.id.outlinedTextField3);


        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);

                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View,String>(gif,"gifImageView2");
                pairs[1] = new Pair<View,String>(greetings,"text_animation");
                pairs[2] = new Pair<View,String>(signinText,"textview2_trans");
                pairs[3] = new Pair<View,String>(email,"email_trans");
                pairs[4] = new Pair<View,String>(password,"password_trans");
                pairs[5] = new Pair<View,String>(login,"button1_trans");
                pairs[6] = new Pair<View,String>(signup,"button2_trans");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
                startActivity(intent,options.toBundle());
                finish();

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, DefaultPageActivity.class);
                startActivity(intent);
                finish();
                // Do something in response to button click
            }
        });
    }

}