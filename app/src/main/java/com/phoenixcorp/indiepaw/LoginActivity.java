package com.phoenixcorp.indiepaw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends AppCompatActivity {
    Button signup,login;
    GifImageView gif;
    TextView signinText,greetings;
    TextInputLayout email, password;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initializing all the variables

        signup = findViewById(R.id.SignUp);
        login = findViewById(R.id.Login);
        gif = findViewById(R.id.gifImageView);
        signinText = findViewById(R.id.textView2);
        greetings = findViewById(R.id.Greetings);
        email = findViewById(R.id.emailTextField);
        password = findViewById(R.id.PasswordTextField);

        auth = FirebaseAuth.getInstance();


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

                String emailText = email.getEditText().getText().toString();
                String passwordText = password.getEditText().getText().toString();

                if(passwordText.isEmpty()){
                    password.setError("Field cannot be empty");
                }
                else if( emailText.isEmpty()){
                    email.setError("Field cannot be empty");
                }
                else{
                    loginUser(emailText,passwordText);
                }

            }
        });
    }

    private void loginUser(String emailText, String passwordText) {



        auth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Login Successful!",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, DefaultPageActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    email.setError("");
                    password.setError("");
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}